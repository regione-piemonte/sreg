import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTable, MatTableDataSource } from '@angular/material';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { ComuneAssociatoGreg } from '@greg-app/app/dto/ComuneAssociatoGreg';
import { ComuneGreg } from '@greg-app/app/dto/ComuneGreg';
import { CronologiaGreg } from '@greg-app/app/dto/CronologiaGreg';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { ProvinciaGreg } from '@greg-app/app/dto/ProvinciaGreg';
import { ResponsabileEnteGreg } from '@greg-app/app/dto/ResponsabileEnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { TipoEnteGreg } from '@greg-app/app/dto/TipoEnteGreg';
import { AslGreg } from '@greg-app/app/dto/AslGreg';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DATIENTE_INFO, DOC, ERRORS, MSG, PATTERN, SECTION, STATO_ABILITAZIONE, STATO_ENTE, STATO_ENTE_DESC_LUNGA } from '@greg-app/constants/greg-constants';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { CurrencyFormat } from '@greg-app/app/currencyFormatter';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { AnagraficaEnteGreg } from '@greg-app/app/dto/AnagraficaEnteGreg';
import { DatiAnagraficiToSave } from '@greg-app/app/dto/DatiAnagraficiToSave';
import { StatoEnteGreg } from '@greg-app/app/dto/StatoEnteGreg';
import { RipristinaEnteComponent } from '@greg-operatore/components/ripristina-ente/ripristina-ente.component';
import { formatDate } from '@angular/common';
import { StoricoComponent } from '@greg-operatore/components/enti-gestori-attivi/Storico/storico.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { Utenti } from '@greg-app/app/dto/Utenti';
import { RicercaUtenti } from '@greg-app/app/dto/RicercaUtenti';
import { ListeConfiguratore } from '@greg-app/app/dto/ListeConfiguratore';
import { Abilitazioni } from '@greg-app/app/dto/Abilitazioni';
import { CancPopupComponent } from '@greg-operatore/components/configuratore-prestazioni/canc-popup/canc-popup.component';
import { UserInfoGreg } from '@greg-app/app/dto/UserInfoGreg';
import { ModificaProfiloPopupComponent } from './modifica-profilo-popup/modifica-profilo-popup.component';
import { CreaProfiloPopupComponent } from './crea-profilo-popup/crea-profilo-popup.component';
import { ListaProfilo } from '@greg-app/app/dto/ListaProfilo';
import { ListaAzione } from '@greg-app/app/dto/ListaAzione';
import { RicercaProfilo } from '@greg-app/app/dto/RicercaProfilo';

@Component({
  selector: 'app-profili',
  templateUrl: './profili.component.html',
  styleUrls: ['./profili.component.css'],
  animations: [
    // Each unique animation requires its own trigger. The first argument of the trigger function is the name
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in'))
    ])
  ]
})
export class ProfiliComponent implements OnInit {

  utenti: Utenti[];
  utente: UserInfoGreg;
  searchForm: FormGroup;
  azioneForm = new FormControl();

  popoverParamTitle: string = DATIENTE_INFO.INFO_PARAM_TITLE;
  popoverParamBody: string = '';

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }


  listaProfilo: ListaProfilo[];
  dataListaRichieste: MatTableDataSource<ListaProfilo>;
  displayedColumns: string[] = ['profilo', 'cestino', 'matita', 'occhietto'];
  secondRow: string[] = ['azione', 'vuoto1', 'salva', 'vuoto2'];
  azioneRow: string[] = ['azione', 'cestino'];
  profili: ListeConfiguratore[];
  // liste: ListeConfiguratore[];
  // enti: ListeConfiguratore[];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  messaggioPopupEliminaAzione: string;
  messaggioPopupEliminaProfilo: string;
  toastElimina: string;
  azioni: ListaAzione[];
  azioneSelezionata: ListaAzione;
  azioneFiltered: ListaAzione[];
  @ViewChild('azioniTable', { static: false }) azioniTable: MatTable<any>;
  constructor(private fb: FormBuilder,
    public client: GregBOClient, public toastService: AppToastService,
    private dialog: MatDialog, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.searchForm = this.fb.group({

      profilo: [],
      // lista: [],
      // ente: []
      azione: []
    });
    this.dataListaRichieste = new MatTableDataSource<ListaProfilo>();
    forkJoin({
      profili: this.client.getProfili(),
      // liste: this.client.getListe(),
      // enti: this.client.getEnti(),
      azioni: this.client.getAzioni(),

      popUpEliminaAzione: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINAAZIONE),
      popUpEliminaProfilo: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINAPROFILO),
      toastElimina: this.client.getMsgInformativi(SECTION.CONFERMAAGGIORNAMENTOPROFILO)
    })
      .subscribe(({ profili, azioni, popUpEliminaAzione, popUpEliminaProfilo, toastElimina }) => {
        this.profili = profili;
        // this.liste = liste;
        // this.enti = enti;
        this.azioni = azioni;
        this.messaggioPopupEliminaAzione = popUpEliminaAzione[0].testoMsgInformativo;
        this.messaggioPopupEliminaProfilo = popUpEliminaProfilo[0].testoMsgInformativo;
        this.toastElimina = toastElimina[0].testoMsgInformativo;

        this.avviaRicerca();

        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
        this.dataListaRichieste.paginator = this.paginator;
        this.azioneFiltered = this.azioni;
        this.azioneForm.valueChanges.subscribe(val => {
          if (val == "") {
            this.searchForm.controls.azione.patchValue(null);
          }
          this.azioneFiltered = this.filterValuesAzioni(val);
        })
      }
      )
  }

  annullaRicerca() {
    this.searchForm.reset();
    this.dataListaRichieste.data = [];
  }


  eliminaProfilo(profilo: ListaProfilo) {
    let messaggio = this.messaggioPopupEliminaProfilo.replace("PROFILO", profilo.codProfilo + ' - ' + profilo.descProfilo);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.client.spinEmitter.emit(true);
        let p: ListaProfilo = new ListaProfilo();
        p.idProfilo = profilo.idProfilo;
        p.codProfilo = profilo.codProfilo;
        p.descProfilo = profilo.descProfilo;
        p.TipoProfilo = profilo.TipoProfilo;
        p.infoProfilo = profilo.infoProfilo;
        p.azioni = profilo.azioni;
        this.client.eliminaProfilo(p).subscribe((r) => {
          let profili = this.dataListaRichieste.data;
          this.profili = this.profili.filter((a) => a.id != p.idProfilo);
          let i = this.dataListaRichieste.data.indexOf(profilo);
          profili.splice(i, 1);
          this.dataListaRichieste.data = profili;
          this.toastService.showSuccess({ text: r.messaggio });
          if (this.client.profiloSelezionato.codProfilo == profilo.codProfilo) {
            this.client.spinEmitter.emit(false);
            this.redirect();
          }
          this.client.spinEmitter.emit(false);
        }, err => {
          this.client.spinEmitter.emit(false);
        })
      }
    })
  }

  openModificaProfilo(profilo: ListaProfilo) {
    const dialogRef = this.dialog.open(ModificaProfiloPopupComponent, {
      width: '1280px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Modifica dati profilo', profilo: profilo, profili: this.dataListaRichieste.data }
    });
    dialogRef.afterClosed().subscribe((p: ListaProfilo) => {
      if (p) {
        this.client.spinEmitter.emit(true);
        p.idProfilo = profilo.idProfilo;
        this.client.modificaProfilo(p).subscribe((r) => {
          this.client.getAzioniProfilo(p).subscribe((a) => {
            let i = this.dataListaRichieste.data.indexOf(profilo);
            this.dataListaRichieste.data[i].descProfilo = p.descProfilo;
            a.forEach(b => {
              b.stato = STATO_ABILITAZIONE.ESISTENTE;
            })
            this.dataListaRichieste.data[i].azioni = a;
            this.dataListaRichieste.data[i].dataAzioni.data = a;
            this.toastService.showSuccess({ text: r.messaggio });
            if (this.client.profiloSelezionato.codProfilo == profilo.codProfilo) {
              this.client.spinEmitter.emit(false);
              this.redirect();
            }
            this.client.spinEmitter.emit(false);
          }, err => {
            this.client.spinEmitter.emit(false);
          });
        }, err => {
          this.client.spinEmitter.emit(false);

        })
      }
    });
  }

  sortProfilo(a: ListaProfilo, b: ListaProfilo) {
    if (a.codProfilo < b.codProfilo) {
      return -1;
    } else if (a.codProfilo > b.codProfilo) {
      return 1;
    } else {
      return 0;
    }
  }

  sortListaProfilo(a: ListeConfiguratore, b: ListeConfiguratore) {
    if (a.codice < b.codice) {
      return -1;
    } else if (a.codice > b.codice) {
      return 1;
    } else {
      return 0;
    }
  }

  sortAzioni(a: ListaAzione, b: ListaAzione) {
    if (a.codAzione < b.codAzione) {
      return -1;
    } else if (a.codAzione > b.codAzione) {
      return 1;
    } else {
      return 0;
    }
  }

  creaProfilo() {
    let profilo: ListaProfilo = new ListaProfilo();
    const dialogRef = this.dialog.open(CreaProfiloPopupComponent, {
      width: '1280px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Crea profilo', profili: this.listaProfilo, azioniDaAssegnare: this.azioni }
    });
    dialogRef.afterClosed().subscribe((p: ListaProfilo) => {
      if (p) {
        this.client.spinEmitter.emit(true);

        p.profilo = false;
        p.azione = new ListaAzione();
        p.azione.idAzione = 0;
        p.azioni.forEach(a => {
          a.stato = STATO_ABILITAZIONE.ESISTENTE;
        })
        p.dataAzioni = new MatTableDataSource<ListaAzione>();
        p.dataAzioni.data = p.azioni;
        p.azioniMancate = p.azioniMancate;
        let profili = this.dataListaRichieste.data;
        let newProfilo = new ListeConfiguratore();
        newProfilo.id = p.idProfilo;
        newProfilo.codice = p.codProfilo;
        newProfilo.descrizione = p.descProfilo;
        this.profili.push(newProfilo);
        this.profili.sort((a, b) => this.sortListaProfilo(a, b));
        profili.push(p);
        profili.sort((a, b) => this.sortProfilo(a, b));
        this.dataListaRichieste.data = profili;
        this.client.spinEmitter.emit(false);
      }
    })
  }

  changeProfilo(profilo: ListaProfilo) {
    profilo.profilo = !profilo.profilo;
    for (let u of this.dataListaRichieste.data) {
      if (u.idProfilo != profilo.idProfilo) {
        u.profilo = false;
      }
    }
  }

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    const profilo = this.searchForm.controls.profilo.value && this.searchForm.controls.profilo.value != "" ? this.searchForm.controls.profilo.value : null;
    const azione = this.searchForm.controls.azione.value && this.searchForm.controls.azione.value != "" ? this.searchForm.controls.azione.value : null;
    const ricerca = new RicercaProfilo(profilo, null, null, azione);
    this.client.getListaProfilo(ricerca).subscribe((response: ListaProfilo[]) => {
      response.forEach(element => {
        element.profilo = false;
        element.azione = new ListaAzione();
        element.azione.idAzione = 0;
        element.azioni.forEach(a => {
          a.stato = STATO_ABILITAZIONE.ESISTENTE;
        })
        element.dataAzioni = new MatTableDataSource<ListaAzione>();
        element.dataAzioni.data = element.azioni;
      })
      this.dataListaRichieste.data = response as any;
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
      if (ricerca.profilo == null && ricerca.azione == null) {
        this.listaProfilo = this.dataListaRichieste.data;
      }
      this.client.spinEmitter.emit(false);
    },
      () => {
        this.client.spinEmitter.emit(false);
      });
  }

  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  aggiungiAzione(profilo: ListaProfilo) {
    profilo.azione.stato = STATO_ABILITAZIONE.NUOVO;
    profilo.azioni.push(profilo.azione);
    profilo.azioni.sort((a, b) => this.sortAzioni(a, b));
    profilo.dataAzioni.data = profilo.azioni;
    profilo.azioniMancate = profilo.azioniMancate.filter((a) => a.idAzione != profilo.azione.idAzione);
    profilo.azione = new ListaAzione();
    profilo.azione.idAzione = 0;
  }



  eliminaAzione(profilo: ListaProfilo, azione: ListaAzione) {
    if (azione.stato == STATO_ABILITAZIONE.NUOVO) {
      this.client.spinEmitter.emit(true);
      profilo.azioni = profilo.azioni.filter((a) => a.idAzione != azione.idAzione);
      azione.idProfiloAzione = null;
      profilo.azioniMancate.push(azione);
      profilo.azioniMancate.sort((a, b) => this.sortAzioni(a, b))
      profilo.dataAzioni.data = profilo.azioni;
      this.toastService.showSuccess({ text: this.toastElimina });
      this.client.spinEmitter.emit(false);
    }
    else {
      let messaggio = this.messaggioPopupEliminaAzione.replace("PROFILO", profilo.codProfilo + ' - ' + profilo.descProfilo).replace("AZIONE", azione.codAzione + ' - ' + azione.descAzione);
      const dialogRef = this.dialog.open(CancPopupComponent, {
        width: '650px',
        disableClose: true,
        autoFocus: true,
        data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
      });
      dialogRef.afterClosed().subscribe(r => {
        if (r) {
          this.client.spinEmitter.emit(true);
          this.client.eliminaAzione(azione).subscribe((r) => {
            let trovato = false;
            if (this.client.profiloSelezionato.codProfilo == profilo.codProfilo) {
              trovato = true;
            }
            profilo.azioni = profilo.azioni.filter((a) => a.idAzione != azione.idAzione);
            azione.idProfiloAzione = null;
            profilo.azioniMancate.push(azione);
            profilo.azioniMancate.sort((a, b) => this.sortAzioni(a, b))
            profilo.dataAzioni.data = profilo.azioni;
            this.toastService.showSuccess({ text: r.messaggio });
            if (trovato) {
              this.client.spinEmitter.emit(false);
              this.redirect();
            }
            this.client.spinEmitter.emit(false);
          }, err => {
            this.client.spinEmitter.emit(false);
          })
        }
      })
    }

  }

  onSelectionChangedAzione(profilo: ListaProfilo, value: number) {
    let azione = profilo.azioniMancate.find((e) => e.idAzione == value);
    profilo.azione = azione;

  }

  redirect() {
    this.client.ricercaEnte = [];
    this.client.ricercaEnteCruscotto = []
    this.client.filtroEnte = null;
    this.client.filtroEnteCruscotto = null;
    this.client.listaStatiSalvato = [];
    this.client.listaStatiSalvatoCruscotto = [];
    this.client.paginaSalvataCruscotto = 0;
    this.client.inviaIFatto = false;
    this.client.inviaIIFatto = false;
    this.client.goToCruscotto = false;
    this.client.goToConfiguratore = false;
    this.client.goToNuovaPrestazione = false;
    this.client.goToArchivio = false;
    this.client.selectprofiloazioneDaGestioneUtenti().subscribe(result => {
      this.utente = result;
      this.client.utenteloggato = this.utente;
      const navigationExtras: NavigationExtras = {
        relativeTo: this.route,
        skipLocationChange: true,
        state: {
          utente: this.utente
        }
      };
      this.router.navigate(["../../../selezione-profilo-applicativo"], navigationExtras);
    }, err => {
      this.client.spinEmitter.emit(false);
    });
  }

  salvaAzioni(profilo: ListaProfilo) {
    let p: ListaProfilo = new ListaProfilo();
    p.idProfilo = profilo.idProfilo;
    p.codProfilo = profilo.codProfilo;
    p.descProfilo = profilo.descProfilo;
    p.TipoProfilo = profilo.TipoProfilo;
    p.infoProfilo = profilo.infoProfilo;
    p.azioni = profilo.azioni;
    this.client.spinEmitter.emit(true);
    this.client.modificaAzioni(p).subscribe((r) => {
      this.client.getAzioniProfilo(p).subscribe((a) => {
        this.toastService.showSuccess({ text: r.messaggio });
        a.forEach(b => {
          b.stato = STATO_ABILITAZIONE.ESISTENTE;
        })
        if (this.client.profiloSelezionato.codProfilo == p.codProfilo) {

          profilo.azioni = a;
          profilo.dataAzioni.data = profilo.azioni;
          this.client.spinEmitter.emit(false);
          this.redirect();

        }

        profilo.azioni = a;
        profilo.dataAzioni.data = profilo.azioni;
        this.client.spinEmitter.emit(false);
      }, err => {
        this.client.spinEmitter.emit(false);
      })
    }, err => {
      this.client.spinEmitter.emit(false);
    })
  }


  disabilitaAggiungiAzioni(profilo: ListaProfilo) {
    if (profilo.azione.idAzione == 0) {
      return true;
    }
    return false;
  }

  optionAzioneSelected(selectedOption: any) {
    // let azione = selectedOption.value.split(' - ');
    // this.searchForm.controls.azione.patchValue(this.azioni.find((e)=>e.codAzione==azione[0]).idAzione);
    const value = selectedOption.value;
    this.searchForm.controls.azione.patchValue(this.azioni.find((e) => e.codAzione == value.codAzione).idAzione);
  }

  displayFn(azione: ListaAzione): string {
    return azione && azione.descAzione ? azione.descAzione : '';
  }

  filterValuesAzioni(search: any) {
    if (typeof search === 'string') {
      if (search == "") {
        this.azioneSelezionata = new ListaAzione();
      }
      else {
        if (this.azioni.find((e) => e.descAzione == search) == null) {
          this.searchForm.controls.azione.patchValue('0');
        }
      }
      if (search && this.azioni && this.azioni.length > 0) {
        return this.azioni.filter(
          l => l.descAzione.toUpperCase().trim().includes(search.toUpperCase().trim()))
      } else {
        return this.azioni;
      }
    } else if (typeof search === 'object' && search !== null){
      if (search.descAzione == "") {
        this.azioneSelezionata = new ListaAzione();
      }
      else {
        if (this.azioni.find((e) => e.codAzione == search.codAzione) == null) {
          this.searchForm.controls.azione.patchValue('0');
        }
      }
      if (search && this.azioni && this.azioni.length > 0) {
        return this.azioni.filter(
          l => l.descAzione.toUpperCase().trim().includes(search.descAzione.toUpperCase().trim()))
      } else {
        return this.azioni;
      }
    }
  }
}
