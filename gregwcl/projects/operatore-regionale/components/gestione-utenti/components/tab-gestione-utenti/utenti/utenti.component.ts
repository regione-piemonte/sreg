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
import { ModificaUtentePopupComponent } from './modifica-utente-popup/modifica-utente-popup.component';
import { CreaUtentePopupComponent } from './crea-utente-popup/crea-utente-popup.component';
import { ListaLista } from '@greg-app/app/dto/ListaLista';
import { CancUtentePopupComponent } from './canc-utente-popup/canc-utente-popup.component';
import { ContainerOperatoreComponent } from '@greg-operatore/components/container-operatore.component';

@Component({
  selector: 'app-utenti',
  templateUrl: './utenti.component.html',
  styleUrls: ['./utenti.component.css'],
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
export class UtentiComponent implements OnInit {

  utenti: Utenti[];
  utente: UserInfoGreg;
  searchForm: FormGroup;
  listaForm = new FormControl();
  enteForm = new FormControl();

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



  dataListaRichieste: MatTableDataSource<Utenti>;
  displayedColumns: string[] = ['cognome', 'nome', 'codiceFiscale', 'email', 'cestino', 'matita', 'occhietto'];
  // secondRow: string[] = ['titoloProfilo', 'titoloLista', 'titoloInizio', 'titoloFine', 'vuoto1', 'vuoto2'];
  secondRow: string[] = ['abilitazioni'];
  thirdRow: string[] = ['profilo', 'lista', 'inizioValidita', 'fineValidita', 'cestinoAbilitazione', 'salva'];
  profili: ListeConfiguratore[];
  liste: ListeConfiguratore[];
  enti: ListeConfiguratore[];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  messaggioPopupEliminaAbilitazione: string;
  messaggioPopupEliminaUtente: string;
  toastElimina: string;
  listaSelezionata: ListeConfiguratore;
  listaFiltered: ListeConfiguratore[];
  enteSelezionata: ListeConfiguratore;
  enteFiltered: ListeConfiguratore[];
  @ViewChild(ContainerOperatoreComponent, { static: false }) container: ContainerOperatoreComponent;
  constructor(private fb: FormBuilder,
    public client: GregBOClient, public toastService: AppToastService,
    private dialog: MatDialog, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.searchForm = this.fb.group({
      nome: [],
      cognome: [],
      codiceFiscale: [],
      email: [],
      profilo: [],
      lista: [],
      ente: [], 
      attivo: []
    });
    this.dataListaRichieste = new MatTableDataSource<Utenti>();
    forkJoin({
      profili: this.client.getProfili(),
      liste: this.client.getListe(),
      enti: this.client.getEnti(),
      popUpEliminaAbilitazione: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINAABILITAZIONE),
      popUpEliminaUtente: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINAUTENTE),
      toastElimina: this.client.getMsgInformativi(SECTION.CONFERMAAGGIORNAMENTOUTENTE)
    })
      .subscribe(({ profili, liste, enti, popUpEliminaAbilitazione, popUpEliminaUtente, toastElimina }) => {
        this.profili = profili;
        this.liste = liste;
        this.enti = enti;
        this.messaggioPopupEliminaAbilitazione = popUpEliminaAbilitazione[0].testoMsgInformativo;
        this.messaggioPopupEliminaUtente = popUpEliminaUtente[0].testoMsgInformativo;
        this.toastElimina = toastElimina[0].testoMsgInformativo;
        this.avviaRicerca();
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
        this.dataListaRichieste.paginator = this.paginator;
        this.listaFiltered = liste;
        this.listaForm.valueChanges.subscribe(val => {
          if (val == "") {
            this.searchForm.controls.lista.patchValue(null);
          }
          this.listaFiltered = this.filterValuesListe(val);
        })
        this.enteFiltered = this.enti;
        this.enteForm.valueChanges.subscribe(val => {
          if (val == "") {
            this.searchForm.controls.ente.patchValue(null);
          }
          this.enteFiltered = this.filterValuesEnti(val);
        })
      }
      )
  }

  annullaRicerca() {
    this.searchForm.reset();
    this.dataListaRichieste.data = [];
  }

  eliminaUtente(utente: Utenti) {
    let messaggio = this.messaggioPopupEliminaUtente.replace("UTENTE", utente.nome + ' ' + utente.cognome + ' (' + utente.codiceFiscale + ')');
    const dialogRef = this.dialog.open(CancUtentePopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio, utente: utente }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        utente = r;
        this.client.spinEmitter.emit(true);
        this.client.eliminaUtente(utente).subscribe((r) => {
          let utenti = this.dataListaRichieste.data;
          let i = this.dataListaRichieste.data.indexOf(utente);
          utenti.splice(i, 1);
          this.dataListaRichieste.data = utenti;
          this.toastService.showSuccess({ text: r.messaggio });
          if (this.client.utenteloggato.codFisc == utente.codiceFiscale) {
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

  openModificaUtente(utente: Utenti) {
    const dialogRef = this.dialog.open(ModificaUtentePopupComponent, {
      width: '1280px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Modifica dati anagrafici utente', utente: utente }
    });
    dialogRef.afterClosed().subscribe((u: Utenti) => {
      if (u) {
        this.client.spinEmitter.emit(true);
        u.idUtente = utente.idUtente;
        this.client.modificaUtente(u).subscribe((r) => {

          let i = this.dataListaRichieste.data.indexOf(utente);
          this.dataListaRichieste.data[i].nome = u.nome.toUpperCase();
          this.dataListaRichieste.data[i].cognome = u.cognome.toUpperCase();
          this.dataListaRichieste.data[i].codiceFiscale = u.codiceFiscale.toUpperCase();
          this.dataListaRichieste.data[i].email = u.email;
          let utenti: Utenti[];
          utenti = this.dataListaRichieste.data;
          utenti.sort((a: Utenti, b: Utenti) => this.sortUtenti(a, b));
          this.dataListaRichieste.data = utenti;
          this.toastService.showSuccess({ text: r.messaggio });
          if (this.client.utenteloggato.codFisc == utente.codiceFiscale) {
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

  sortUtenti(a: Utenti, b: Utenti) {
    if (a.cognome < b.cognome) {
      return -1;
    } else if (a.cognome > b.cognome) {
      return 1;
    } else {
      return 0;
    }
  }

  creaUtente() {
    let utente: Utenti = new Utenti();
    const dialogRef = this.dialog.open(CreaUtentePopupComponent, {
      width: '1280px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Crea utente', profili: this.profili, liste: this.liste }
    });
    dialogRef.afterClosed().subscribe((u: Utenti) => {
      if (u) {
        this.client.spinEmitter.emit(true);
        this.client.getAbilitazioni(u).subscribe((a) => {
          let ute = a.find(a => a.dataFineValidita == null)
          if (ute) {
            utente.maxData = new Date(ute.dataInizioValidita);
          } else {
            utente.maxData = null;
          }
          a.forEach(a => {
            a.dataInizioValidita = new Date(a.dataInizioValidita);
            if (utente.maxData) {
              if (utente.maxData.getTime() < a.dataInizioValidita.getTime()) {
                if (a.dataFineValidita == null) {
                  utente.maxData = a.dataInizioValidita;
                }
              }
            }
            if (a.dataFineValidita) {
              a.dataFineValidita = new Date(a.dataFineValidita);
            }
          })
          u.abilitazioni = a;
          let utenti = this.dataListaRichieste.data;
          utenti.push(u);
          utenti.sort((a, b) => this.sortUtenti(a, b));
          this.dataListaRichieste.data = utenti;
          this.client.spinEmitter.emit(false);
        }, err => {
          this.client.spinEmitter.emit(false);
        })
      }
    })
  }

  changeUtente(utente: Utenti) {
    utente.utente = !utente.utente;
    for (let u of this.dataListaRichieste.data) {
      if (u.codiceFiscale != utente.codiceFiscale) {
        u.utente = false;
      }
    }
  }

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    const nome = this.searchForm.controls.nome.value && this.searchForm.controls.nome.value != "" ? this.searchForm.controls.nome.value : null;
    const cognome = this.searchForm.controls.cognome.value && this.searchForm.controls.cognome.value != "" ? this.searchForm.controls.cognome.value : null;
    const codiceFiscale = this.searchForm.controls.codiceFiscale.value && this.searchForm.controls.codiceFiscale.value != "" ? this.searchForm.controls.codiceFiscale.value : null;
    const email = this.searchForm.controls.email.value && this.searchForm.controls.email.value != "" ? this.searchForm.controls.email.value : null;
    const profilo = this.searchForm.controls.profilo.value && this.searchForm.controls.profilo.value != "" ? this.searchForm.controls.profilo.value : null;
    const lista = this.searchForm.controls.lista.value && this.searchForm.controls.lista.value != "" ? this.searchForm.controls.lista.value : null;
    const ente = this.searchForm.controls.ente.value && this.searchForm.controls.ente.value != "" ? this.searchForm.controls.ente.value : null;
    const attivo = this.searchForm.controls.attivo.value && this.searchForm.controls.attivo.value != "" ? this.searchForm.controls.attivo.value : false;
    const ricerca = new RicercaUtenti(nome, cognome, codiceFiscale, email, profilo, lista, ente, attivo);
    this.client.getUtenti(ricerca).subscribe((response: Utenti[]) => {
      response.forEach(element => {
        element.utente = false;
        if (element.maxData) {
          element.maxData = new Date(element.maxData);
        }
        element.abilitazioni.forEach(a => {
          a.dataInizioValidita = new Date(a.dataInizioValidita);
          if (a.dataFineValidita) {
            a.dataFineValidita = new Date(a.dataFineValidita);
          }
        })
      })
      this.dataListaRichieste.data = response as any;
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
      this.client.spinEmitter.emit(false);
    },
      () => {
        this.client.spinEmitter.emit(false);
      });
  }

  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  aggiungiAbilitazione(utente: Utenti) {
    let newProfilo: ListeConfiguratore = new ListeConfiguratore();
    let newLista: ListeConfiguratore = new ListeConfiguratore();
    newProfilo.id = 0;
    newLista.id = 0;
    let newAbilitazione: Abilitazioni = new Abilitazioni();
    newAbilitazione.lista = newLista;
    newAbilitazione.profilo = newProfilo;
    newAbilitazione.stato = STATO_ABILITAZIONE.NUOVO;
    utente.abilitazioni.push(newAbilitazione);
  }

  eliminaAbilitazione(utente: Utenti, i: number) {
    if (utente.abilitazioni[i].lista.id == 0 || utente.abilitazioni[i].profilo.id == 0 || utente.abilitazioni[i].stato == STATO_ABILITAZIONE.NUOVO) {
      this.client.spinEmitter.emit(true);
      utente.abilitazioni.splice(i, 1);
      this.toastService.showSuccess({ text: this.toastElimina });
      this.client.spinEmitter.emit(false);
    }
    else {
      let messaggio = this.messaggioPopupEliminaAbilitazione.replace("PROFILO", utente.abilitazioni[i].profilo.codice + ' - ' + utente.abilitazioni[i].profilo.descrizione).replace("UTENTE", utente.nome + ' ' + utente.cognome + ' (' + utente.codiceFiscale + ')').replace("LISTA", utente.abilitazioni[i].lista.codice + ' - ' + utente.abilitazioni[i].lista.descrizione);
      const dialogRef = this.dialog.open(CancPopupComponent, {
        width: '650px',
        disableClose: true,
        autoFocus: true,
        data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
      });
      dialogRef.afterClosed().subscribe(r => {
        if (r) {
          this.client.spinEmitter.emit(true);
          this.client.eliminaAbilitazione(utente.abilitazioni[i]).subscribe((r) => {
            let trovato = false;
            if (this.client.listaSelezionata.codLista == utente.abilitazioni[i].lista.codice && this.client.profiloSelezionato.codProfilo == utente.abilitazioni[i].profilo.codice) {
              trovato = true;
            }
            utente.abilitazioni.splice(i, 1);
            this.toastService.showSuccess({ text: r.messaggio });
            if (trovato) {
              this.client.spinEmitter.emit(false);
              this.redirect();
            }
            if(this.client.utenteloggato.codFisc == utente.codiceFiscale){
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

  onSelectionChangedLista(utente: Utenti, value: number, i: number) {
    if (value == 0) {
      let newLista: ListeConfiguratore = new ListeConfiguratore();
      newLista.id = 0;
      utente.abilitazioni[i].lista = newLista;
      if (utente.abilitazioni[i].stato != STATO_ABILITAZIONE.NUOVO) {
        utente.abilitazioni[i].stato = STATO_ABILITAZIONE.MODIFICATO;
      }
    } else {
      let lista = this.liste.find((e) => e.id == value);
      utente.abilitazioni[i].lista = lista;
      if (utente.abilitazioni[i].stato != STATO_ABILITAZIONE.NUOVO) {
        utente.abilitazioni[i].stato = STATO_ABILITAZIONE.MODIFICATO;
      }
    }
  }

  onSelectionChangedProfilo(utente: Utenti, value: number, i: number) {
    if (value == 0) {
      let newProfilo: ListeConfiguratore = new ListeConfiguratore();
      newProfilo.id = 0;
      utente.abilitazioni[i].profilo = newProfilo;
      if (utente.abilitazioni[i].stato != STATO_ABILITAZIONE.NUOVO) {
        utente.abilitazioni[i].stato = STATO_ABILITAZIONE.MODIFICATO;
      }
    } else {
      let profilo = this.profili.find((e) => e.id == value);
      utente.abilitazioni[i].profilo = profilo;
      if (utente.abilitazioni[i].stato != STATO_ABILITAZIONE.NUOVO) {
        utente.abilitazioni[i].stato = STATO_ABILITAZIONE.MODIFICATO;
      }
    }
  }

  cambiaDataFine(utente: Utenti, event: any, i: number) {
    if (utente.abilitazioni[i].stato != STATO_ABILITAZIONE.NUOVO) {
      utente.abilitazioni[i].stato = STATO_ABILITAZIONE.MODIFICATO;
    }
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

  salvaAbilitazioni(utente: Utenti) {
    this.client.spinEmitter.emit(true);
    this.client.modificaAbilitazione(utente).subscribe((r) => {
      this.client.getAbilitazioni(utente).subscribe((a) => {
        let ute = a.find(a => a.dataFineValidita == null)
        if (ute) {
          utente.maxData = new Date(ute.dataInizioValidita);
        } else {
          utente.maxData = null;
        }
        a.forEach(a => {
          a.dataInizioValidita = new Date(a.dataInizioValidita);
          if (utente.maxData) {
            if (utente.maxData.getTime() < a.dataInizioValidita.getTime()) {
              if (a.dataFineValidita == null) {
                utente.maxData = a.dataInizioValidita;
              }
            }
          }
          if (a.dataFineValidita) {
            a.dataFineValidita = new Date(a.dataFineValidita);
          }
        })
        this.toastService.showSuccess({ text: r.messaggio });
        if (this.client.utenteloggato.codFisc == utente.codiceFiscale) {
          let abilitazioneP = utente.abilitazioni.find((e) => e.profilo.codice == this.client.profiloSelezionato.codProfilo && e.stato == STATO_ABILITAZIONE.MODIFICATO);
          let abilitazioneL = utente.abilitazioni.find((e) => e.lista.codice == this.client.listaSelezionata.codLista && e.stato == STATO_ABILITAZIONE.MODIFICATO);
          if ((abilitazioneL != null || abilitazioneL != undefined) && (abilitazioneP != null || abilitazioneP != undefined)) {
            utente.abilitazioni = a;
            this.client.spinEmitter.emit(false);
            this.redirect();
          }else{
            utente.abilitazioni = a;
            this.client.spinEmitter.emit(false);
            this.redirect();
          }
        }
       
        utente.abilitazioni = a;
        this.client.spinEmitter.emit(false);
      }, err => {
        this.client.spinEmitter.emit(false);
      })
    }, err => {
      this.client.spinEmitter.emit(false);
    })
  }

  disabilitaSalvaAbilitazione(utente: Utenti) {
    let trovato: boolean = false;
    utente.abilitazioni.forEach((a) => {
      if (a.profilo.id == 0) {
        trovato = true;
      }
      if (a.lista.id == 0) {
        trovato = true;
      }
      if (a.dataInizioValidita === null || a.dataInizioValidita === undefined) {
        trovato = true;
      }
    })
    return trovato;
  }

  optionListaSelected(selectedOption: any) {
    let lista = selectedOption.value.split(' - ');
    this.searchForm.controls.lista.patchValue(this.liste.find((l) => l.codice == lista[0]).id);
  }

  filterValuesListe(search: string) {
    if (search == "") {
      this.listaSelezionata = new ListeConfiguratore();
    }
    else {
      if (this.liste.find((e) => (e.codice + ' - ' + e.descrizione) == search) == null) {
        this.searchForm.controls.lista.patchValue('0');
      }
    }
    if (search && this.liste && this.liste.length > 0) {
      return this.liste.filter(
        l => (l.codice + ' - ' + l.descrizione).toUpperCase().trim().includes(search.toUpperCase().trim()) ||
          l.codice.toUpperCase().trim().includes(search.toUpperCase().trim()) || l.descrizione.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.liste;
    }
  }
  optionEnteSelected(selectedOption: any) {
    let ente = selectedOption.value.split(' - ');
    this.searchForm.controls.ente.patchValue(this.enti.find((e) => e.codice == ente[0]).id);
  }

  filterValuesEnti(search: string) {
    if (search == "") {
      this.enteSelezionata = new ListeConfiguratore();
    }
    else {
      if (this.enti.find((e) => (e.codice + ' - ' + e.descrizione) == search) == null) {
        this.searchForm.controls.ente.patchValue('0');
      }
    }
    if (search && this.enti && this.enti.length > 0) {
      return this.enti.filter(
        l => (l.codice + ' - ' + l.descrizione).toUpperCase().trim().includes(search.toUpperCase().trim()) ||
          l.codice.toUpperCase().trim().includes(search.toUpperCase().trim()) || l.descrizione.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.enti;
    }
  }
}
