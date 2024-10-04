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
import { ListaProfilo } from '@greg-app/app/dto/ListaProfilo';
import { ListaAzione } from '@greg-app/app/dto/ListaAzione';
import { RicercaProfilo } from '@greg-app/app/dto/RicercaProfilo';
import { CancPopupComponent } from '@greg-operatore/components/configuratore-prestazioni/canc-popup/canc-popup.component';
import { CreaProfiloPopupComponent } from '../profili/crea-profilo-popup/crea-profilo-popup.component';
import { ModificaProfiloPopupComponent } from '../profili/modifica-profilo-popup/modifica-profilo-popup.component';
import { ModificaListaPopupComponent } from './modifica-lista-popup/modifica-lista-popup.component';
import { CreaListaPopupComponent } from './crea-lista-popup/crea-lista-popup.component';
import { ListeConfiguratore } from '@greg-app/app/dto/ListeConfiguratore';
import { Utenti } from '@greg-app/app/dto/Utenti';
import { UserInfoGreg } from '@greg-app/app/dto/UserInfoGreg';
import { ListaLista } from '@greg-app/app/dto/ListaLista';
import { ListaEnte } from '@greg-app/app/dto/ListaEnte';

@Component({
  selector: 'app-liste',
  templateUrl: './liste.component.html',
  styleUrls: ['./liste.component.css'],
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
export class ListeComponent implements OnInit {

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


  listaLista: ListaLista[];
  dataListaRichieste: MatTableDataSource<ListaLista>;
  displayedColumns: string[] = ['lista', 'cestino', 'matita', 'occhietto'];
  secondRow: string[] = ['enti', 'vuoto1', 'salva', 'vuoto2'];
  enteRow: string[] = ['ente', 'cestino'];
  liste: ListeConfiguratore[];
  enti: ListeConfiguratore[];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  messaggioPopupEliminaEnte: string;
  messaggioPopupEliminaLista: string;
  toastElimina: string;
  listaSelezionata: ListeConfiguratore;
  listaFiltered: ListeConfiguratore[];
  enteSelezionata: ListeConfiguratore;
  enteFiltered: ListeConfiguratore[];
  @ViewChild('azioniTable', { static: false }) azioniTable: MatTable<any>;
  entiLista: ListaEnte[];
  constructor(private fb: FormBuilder,
    public client: GregBOClient, public toastService: AppToastService,
    private dialog: MatDialog, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.searchForm = this.fb.group({
      lista: [],
      ente: []
    });
    this.dataListaRichieste = new MatTableDataSource<ListaLista>();

    forkJoin({
      liste: this.client.getListe(),
      enti: this.client.getEnti(),
      entiLista: this.client.getEntiLista(),
      popUpEliminaEnte: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINAENTE),
      popUpEliminaLista: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINALISTA),
      toastElimina: this.client.getMsgInformativi(SECTION.CONFERMAAGGIORNAMENTOLISTA)
    })
      .subscribe(({ liste, enti, entiLista, popUpEliminaEnte, popUpEliminaLista, toastElimina }) => {
        this.liste = liste;
        this.enti = enti;
        this.entiLista = entiLista;
        this.messaggioPopupEliminaEnte = popUpEliminaEnte[0].testoMsgInformativo;
        this.messaggioPopupEliminaLista = popUpEliminaLista[0].testoMsgInformativo;
        this.toastElimina = toastElimina[0].testoMsgInformativo;

        this.avviaRicerca();

        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;
        this.dataListaRichieste.paginator = this.paginator;
        this.listaFiltered = this.liste;
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


  eliminaLista(lista: ListaLista) {
    let messaggio = this.messaggioPopupEliminaLista.replace("LISTA", lista.codLista + ' - ' + lista.descLista);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.client.spinEmitter.emit(true);
        let p: ListaLista = new ListaLista();
        p.idLista = lista.idLista;
        p.codLista = lista.codLista;
        p.descLista = lista.descLista;
        p.infoLista = lista.infoLista;
        p.enti = lista.enti;
        this.client.eliminaLista(p).subscribe((r) => {
          let profili = this.dataListaRichieste.data;
          this.liste = this.liste.filter((a) => a.id != p.idLista);
          let i = this.dataListaRichieste.data.indexOf(lista);
          profili.splice(i, 1);
          this.dataListaRichieste.data = profili;
          this.toastService.showSuccess({ text: r.messaggio });
          if (this.client.listaSelezionata.codLista == lista.codLista) {
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

  openModificaLista(lista: ListaLista) {
    const dialogRef = this.dialog.open(ModificaListaPopupComponent, {
      width: '1280px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Modifica dati lista', lista: lista, liste: this.dataListaRichieste.data }
    });
    dialogRef.afterClosed().subscribe((p: ListaLista) => {
      if (p) {
        this.client.spinEmitter.emit(true);
        p.idLista = lista.idLista;
        this.client.modificaLista(p).subscribe((r) => {
          this.client.getListaEnti(p).subscribe((a) => {
             a.forEach(b => {
                b.stato = STATO_ABILITAZIONE.ESISTENTE;
            })
            let i = this.dataListaRichieste.data.indexOf(lista);
            this.dataListaRichieste.data[i].descLista = p.descLista;
            this.dataListaRichieste.data[i].enti =a;
            this.dataListaRichieste.data[i].dataEnti.data = a;
            this.toastService.showSuccess({ text: r.messaggio });
            if (this.client.listaSelezionata.codLista == lista.codLista) {
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

  sortLista(a: ListaLista, b: ListaLista) {
    if (a.codLista < b.codLista) {
      return -1;
    } else if (a.codLista > b.codLista) {
      return 1;
    } else {
      return 0;
    }
  }

  sortEnte(a: ListaEnte, b: ListaEnte) {
    if (a.codEnte < b.codEnte) {
      return -1;
    } else if (a.codEnte > b.codEnte) {
      return 1;
    } else {
      return 0;
    }
  }

  creaLista() {
    let lista: ListaLista = new ListaLista();
    const dialogRef = this.dialog.open(CreaListaPopupComponent, {
      width: '1280px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Crea lista', liste: this.listaLista, entiDaAssegnare: this.entiLista }
    });
    dialogRef.afterClosed().subscribe((p: ListaLista) => {
      if (p) {
        this.client.spinEmitter.emit(true);
       
          p.lista = false;
          p.ente = new ListaEnte();
          p.ente.idEnte = 0;
          p.enti.forEach(a => {
            a.stato = STATO_ABILITAZIONE.ESISTENTE;
          })
          p.dataEnti = new MatTableDataSource<ListaEnte>();
          p.dataEnti.data = p.enti;
          p.entiMancate = p.entiMancate;
        let liste = this.dataListaRichieste.data;
        let newLista = new ListeConfiguratore();
        newLista.id = p.idLista;
        newLista.codice = p.codLista;
        newLista.descrizione = p.descLista;
        this.liste.push(newLista);
        this.liste.sort((a, b) => this.sortListaLista(a, b));
        liste.push(p);
        liste.sort((a, b) => this.sortLista(a, b));
        this.dataListaRichieste.data = liste;
        this.client.spinEmitter.emit(false);
      }
    })
  }

  sortListaLista(a: ListeConfiguratore, b: ListeConfiguratore) {
    if (a.codice < b.codice) {
      return -1;
    } else if (a.codice > b.codice) {
      return 1;
    } else {
      return 0;
    }
  }

  changeLista(lista: ListaLista) {
    lista.lista = !lista.lista;
    for (let u of this.dataListaRichieste.data) {
      if (u.idLista != lista.idLista) {
        u.lista = false;
      }
    }
  }

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    const lista = this.searchForm.controls.lista.value && this.searchForm.controls.lista.value != "" ? this.searchForm.controls.lista.value : null;
    const ente = this.searchForm.controls.ente.value && this.searchForm.controls.ente.value != "" ? this.searchForm.controls.ente.value : null;
    const ricerca = new RicercaProfilo(null, lista, ente, null);
    this.client.getLista(ricerca).subscribe((response: ListaLista[]) => {
      response.forEach(element => {
        element.lista = false;
        element.ente = new ListaEnte();
        element.ente.idEnte = 0;
        element.enti.forEach(a => {
          a.stato = STATO_ABILITAZIONE.ESISTENTE;
        })
        element.dataEnti = new MatTableDataSource<ListaEnte>();
        element.dataEnti.data = element.enti;
      })
      this.dataListaRichieste.data = response as any;
      this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
      this.dataListaRichieste.sort = this.sort;
      this.dataListaRichieste.paginator = this.paginator;
      if(ricerca.lista==null && ricerca.ente==null){
        this.listaLista = this.dataListaRichieste.data;
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

  aggiungiEnte(lista: ListaLista) {
    lista.ente.stato = STATO_ABILITAZIONE.NUOVO;
    lista.enti.push(lista.ente);
    lista.enti.sort((a, b) => this.sortEnte(a, b));
    lista.dataEnti.data = lista.enti;
    lista.entiMancate = lista.entiMancate.filter((a) => a.idEnte != lista.ente.idEnte);
    lista.ente = new ListaEnte();
    lista.ente.idEnte = 0;
  }



  eliminaEnte(lista: ListaLista, ente: ListaEnte) {
    if (ente.stato == STATO_ABILITAZIONE.NUOVO) {
      this.client.spinEmitter.emit(true);
      lista.enti = lista.enti.filter((a) => a.idEnte != ente.idEnte);
      ente.idListaEnte = null;
      lista.entiMancate.push(ente);
      lista.entiMancate.sort((a, b) => this.sortEnte(a, b))
      lista.dataEnti.data = lista.enti;
      this.toastService.showSuccess({ text: this.toastElimina });
      this.client.spinEmitter.emit(false);
    }
    else {
      let messaggio = this.messaggioPopupEliminaEnte.replace("LISTA", lista.codLista + ' - ' + lista.descLista).replace("ENTE", ente.codEnte + ' - ' + ente.descEnte);
      const dialogRef = this.dialog.open(CancPopupComponent, {
        width: '650px',
        disableClose: true,
        autoFocus: true,
        data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
      });
      dialogRef.afterClosed().subscribe(r => {
        if (r) {
          this.client.spinEmitter.emit(true);
          this.client.eliminaEnte(ente).subscribe((r) => {
            let trovato = false;
            if (this.client.listaSelezionata.codLista == lista.codLista) {
              trovato = true;
            }
            lista.enti = lista.enti.filter((a) => a.idEnte != ente.idEnte);
            ente.idListaEnte = null;
            lista.entiMancate.push(ente);
            lista.entiMancate.sort((a, b) => this.sortEnte(a, b))
            lista.dataEnti.data = lista.enti;
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

  onSelectionChangedEnte(lista: ListaLista, value: number) {
    let ente = lista.entiMancate.find((e) => e.idEnte == value);
    lista.ente = ente;

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

  salvaEnti(lista: ListaLista) {
    let p: ListaLista = new ListaLista();
    p.idLista = lista.idLista;
    p.codLista = lista.codLista;
    p.descLista = lista.descLista;
    p.infoLista = lista.infoLista;
    p.enti = lista.enti;
    this.client.spinEmitter.emit(true);
    this.client.modificaEnti(p).subscribe((r) => {
      this.client.getListaEnti(p).subscribe((a) => {
        this.toastService.showSuccess({ text: r.messaggio });
        a.forEach(b => {
          b.stato = STATO_ABILITAZIONE.ESISTENTE;
        })
        if (this.client.listaSelezionata.codLista == p.codLista) {

          lista.enti = a;
          lista.dataEnti.data = lista.enti;
          this.client.spinEmitter.emit(false);
          this.redirect();

        }
        
        lista.enti = a;
        lista.dataEnti.data = lista.enti;
        this.client.spinEmitter.emit(false);
      }, err => {
        this.client.spinEmitter.emit(false);
      })
    }, err => {
      this.client.spinEmitter.emit(false);
    })
  }


  disabilitaAggiungiEnti(lista: ListaLista) {
    if (lista.ente.idEnte == 0) {
      return true;
    }
    return false;
  }
  optionListaSelected(selectedOption: any) {
    let lista = selectedOption.value.split(' - ');
    this.searchForm.controls.lista.patchValue(this.liste.find((l)=>l.codice==lista[0]).id);
  }

  filterValuesListe(search: string) {
    if (search == "") {
      this.listaSelezionata = new ListeConfiguratore();
    }
    else{
      if(this.liste.find((e) => (e.codice + ' - ' + e.descrizione) == search)==null){
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
    this.searchForm.controls.ente.patchValue(this.enti.find((e)=>e.codice==ente[0]).id);
  }

  filterValuesEnti(search: string) {
    if (search == "") {
      this.enteSelezionata = new ListeConfiguratore();
    }
    else{
      if(this.enti.find((e) => (e.codice + ' - ' + e.descrizione) == search)==null){
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
