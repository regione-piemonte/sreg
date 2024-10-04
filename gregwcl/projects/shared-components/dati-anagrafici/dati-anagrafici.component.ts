import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatTable } from '@angular/material';
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
import { ChiudiEnteComponent } from '../../operatore-regionale/components/chiudi-ente/chiudi-ente.component'
import { AppToastService } from '../toast/app-toast.service';
import { TipoEnteGreg } from '@greg-app/app/dto/TipoEnteGreg';
import { AslGreg } from '@greg-app/app/dto/AslGreg';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DATIENTE_INFO, DOC, ERRORS, MSG, PATTERN, SECTION, STATO_ENTE, STATO_ENTE_DESC_LUNGA } from '@greg-app/constants/greg-constants';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { CurrencyFormat } from '@greg-app/app/currencyFormatter';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { AnagraficaEnteGreg } from '@greg-app/app/dto/AnagraficaEnteGreg';
import { DatiAnagraficiToSave } from '@greg-app/app/dto/DatiAnagraficiToSave';
import { StatoEnteComponent } from './stato-ente/stato-ente.component';
import { StatoEnteGreg } from '@greg-app/app/dto/StatoEnteGreg';
import { RipristinaEnteComponent } from '@greg-operatore/components/ripristina-ente/ripristina-ente.component';
import { formatDate } from '@angular/common';
import { StoricoComponent } from '@greg-operatore/components/enti-gestori-attivi/Storico/storico.component';
import { CancPopupComponent } from '@greg-operatore/components/configuratore-prestazioni/canc-popup/canc-popup.component';

@Component({
  selector: 'app-dati-anagrafici',
  templateUrl: './dati-anagrafici.component.html',
  styleUrls: ['./dati-anagrafici.component.css'],
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
export class DatiAnagraficiComponent implements OnInit {

  state: string = 'default';
  espansa: boolean;
  statecomune: string = 'default';
  espansacomune: boolean;
  enteValues: string[] = [];
  dataModifica = new Date(new Date().getFullYear(), 0, 1);
  dataSistema = new Date();
  dataModificata: Date = null;
  @ViewChild('comuniTable', { static: false }) comuniTable: MatTable<any>;
  @ViewChild(StatoEnteComponent, { static: false }) cronologiaStatoMod: StatoEnteComponent;


  listaComuniGeneral: ComuneGreg[];
  listaComuniGeneralMod: ComuneGreg[];
  listaComuniDenominazione: ComuneGreg[] = [];
  listaComuniToAss: ComuneGreg[] = [];
  listaProvince: ProvinciaGreg[] = [];
  listaProvinceSede: ProvinciaGreg[] = [];
  listaComuniGeneralSede: ComuneGreg[];
  msgErrorComuneAlreadyPresent: string = '';
  msgErrorDeleteComuneVal: string = '';

  listaTipoEnti: TipoEnteGreg[] = [];
  listaAsl: AslGreg[] = [];

  denominazioneEnte: AnagraficaEnteGreg;
  cronologia: CronologiaGreg = new CronologiaGreg();

  comune: ComuneAssociatoGreg;
  comuneSelected: string;
  provinciaSelected: string;

  listaComuniAssociati: ComuneAssociatoGreg[] = [];
  listaComuniEliminati: ComuneAssociatoGreg[] = [];

  modificaEnte: boolean = false;
  modificaResp: boolean = false;
  modificaComune: boolean = false;

  columnsComuniAssociati: Array<string> = ['codiceIstat', 'comune', 'dal', 'azioni'];
  columnsComuniAssociatiEnte: Array<string> = ['codiceIstat', 'comune', 'dal'];
  columnsComuniEliminatiEnte: Array<string> = ['codiceIstat', 'comune', 'al'];

  navigation: Navigation;
  ente: EnteGreg;
  warn: string = '';

  datiEnteToSave: DatiAnagraficiToSave;

  enteForm: FormGroup = new FormGroup({});
  responsabileEnteForm: FormGroup = new FormGroup({});

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

  msgTab: string;
  idScheda: number;
  comuniIniziali: ComuneAssociatoGreg[];
  dataFineValidita: Date;
  statoEnte: string;
  lastStato: StatoEnteGreg;
  dataInizioValidita: Date;
  numeroperiodi: number = 0;
  modifica: boolean = false;
  dataMax: string;
  dataMaxPopUp: any;
  msgPopUp: string;
  constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder,
    public client: GregBOClient, private modalService: NgbModal, public toastService: AppToastService,
    private currencyFormat: CurrencyFormat, private gregError: GregErrorService, private dialog: MatDialog) {
    this.navigation = this.router.getCurrentNavigation();
    let enteValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      enteValues.push(frag);
    });
    this.idScheda = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.idSchedaEnteGestore : enteValues[0][0];
    this.statoEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.statoEnte : enteValues[0][0];
    this.dataFineValidita = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.dataFineValidita : null;
    this.dataInizioValidita = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.dataInizioValidita : null;
    this.numeroperiodi = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.numeroperiodi : null;
  }

  ngOnInit() {
    this.modificaComune = false;
    this.modificaEnte = false;
    this.modificaResp = false;
    this.enteForm = this.fb.group({
      codiceRegionale: [''],
      codiceFiscale: [''],
      denominazione: [''],
      partitaIva: [''],
      tipoEnte: [''],
      comune: [''],
      codiceIstat: [''],
      provincia: [''],
      asl: [''],
      email: [''],
      telefono: [''],
      pec: [''],
    })

    this.responsabileEnteForm = this.fb.group({
      nome: [''],
      cognome: [''],
      cellulare: [''],
      telefono: [''],
      codiceFiscale: [''],
      eMail: [''],
    })

    this.dataSistema.setHours(0, 0, 0, 0);

    this.client.spinEmitter.emit(true);
    if (this.dataFineValidita == null) {
      forkJoin({

        msgErrorComuneAlreadyPresent: this.client.getMsgApplicativo(ERRORS.ERROR_COMUNE_ALREADY_PRESENT),
        msgInformativi: this.client.getMsgInformativi(SECTION.DATI_ENTE),

        msgErrorDeleteComuneVal: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_COMUNE_ASS),

        schedaEnte: this.client.getSchedaAnagrafica(this.idScheda, formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        comuni: this.client.getProvinceComuniLiberi(formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        comunimod: this.client.getProvinceComuniLiberi(formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        provincesede: this.client.getDatiEnteProvince(formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        comunisede: this.client.getListaComuni(formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        comAssociati: this.client.getComuniAnagraficaAssociati(this.idScheda, 'null'),
        comuniIniziali: this.client.getComuniAnagraficaAssociati(this.idScheda, 'null'),
        comEliminati: this.client.getComuniAnagraficaAssociati(this.idScheda, formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        tipoEnte: this.client.getListaTipoEnte(),
        asl: this.client.getAsl(formatDate(new Date(), 'yyyy-MM-dd', 'en')),
        warn: this.client.getMsgApplicativo(MSG.WARNDATIANAGRAFICI),
        msgTab: this.client.getMsgApplicativo(MSG.WARN_TAB),
        lastStato: this.client.getLastStato(this.idScheda),
        dataMax: this.client.getDataInizioMax(this.idScheda),
        dataMaxPopUp: this.client.getMsgInformativi(SECTION.MSGDATAVARIAZIONE),
        dataMaxTitlePopUp: this.client.getMsgInformativi(SECTION.MSGTITLEDATAVARIAZIONE),
        msgPopUp: this.client.getMsgInformativi(SECTION.MSGPOPUPDATAVARIAZIONE),
      })
        .subscribe(({ msgErrorComuneAlreadyPresent, msgInformativi, msgErrorDeleteComuneVal, schedaEnte,
          provincesede,
          comuni, comAssociati,
          comunisede,
          tipoEnte, asl, msgTab, warn, lastStato, comEliminati, comuniIniziali, comunimod, dataMax, dataMaxPopUp, dataMaxTitlePopUp, msgPopUp}) => {
          this.warn = warn.testoMsgApplicativo;
          this.dataMax = dataMax.stato;
          this.msgPopUp = msgPopUp[0].testoMsgInformativo;
          this.dataMaxPopUp = dataMaxPopUp[0].testoMsgInformativo;
          this.popoverParamTitle = dataMaxTitlePopUp[0].testoMsgInformativo;
          this.msgErrorComuneAlreadyPresent = msgErrorComuneAlreadyPresent.testoMsgApplicativo;
          this.client.azioni;
          this.popoverParamBody = msgInformativi[0].testoMsgInformativo;
          this.msgErrorDeleteComuneVal = msgErrorDeleteComuneVal.testoMsgApplicativo;
          this.msgTab = msgTab.testoMsgApplicativo;
          this.denominazioneEnte = schedaEnte;
          this.listaComuniGeneral = comuni;
          this.listaComuniGeneralMod = comunimod;
          this.listaComuniGeneralSede = comunisede;
          this.listaComuniEliminati = comEliminati;
          this.initListaProvince('initial');
          this.initFormDenominazione(this.denominazioneEnte);
          this.listaTipoEnti = tipoEnte;
          this.listaProvinceSede = provincesede;
          this.listaComuniDenominazione = this.listaComuniGeneralSede.filter(element => element.provincia.codIstatProvincia == this.denominazioneEnte.comune.provincia.codIstatProvincia);

          this.listaAsl = asl;
          this.initResponabileEnteForm(this.denominazioneEnte.responsabileEnte);
          this.comuniIniziali = comuniIniziali;
          this.listaComuniAssociati = comAssociati;
          this.initListaComuniToAss();
          this.lastStato = lastStato;
          let data = new Date(this.lastStato.dal);
          if (this.lastStato.stato == STATO_ENTE_DESC_LUNGA.CHIUSO) {
            if (data <= this.dataSistema) {
              this.statoEnte = "CHI";
            } else {
              this.statoEnte = "APE";
            }
          } else {
            this.statoEnte = "APE";
          }

          this.cronologiaStatoMod.ngOnInit();

          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false);
          });
    } else {
      forkJoin({
        msgErrorComuneAlreadyPresent: this.client.getMsgApplicativo(ERRORS.ERROR_COMUNE_ALREADY_PRESENT),
        msgInformativi: this.client.getMsgInformativi(SECTION.DATI_ENTE),

        msgErrorDeleteComuneVal: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_COMUNE_ASS),

        schedaEnte: this.client.getSchedaAnagraficaStorico(this.idScheda, this.dataFineValidita.toString()),
        province: this.client.getDatiEnteProvince(null),
        comuni: this.client.getListaComuni(null),
        comunimod: this.client.getListaComuni(null),
        comAssociati: this.client.getComuniAnagraficaAssociatiStorico(this.idScheda, this.dataFineValidita.toString(), 'null'),
        comEliminati: this.client.getComuniAnagraficaAssociatiStorico(this.idScheda, this.dataFineValidita.toString(), this.dataInizioValidita.toString()),
        tipoEnte: this.client.getListaTipoEnte(),
        asl: this.client.getAsl(null),
        warn: this.client.getMsgApplicativo(MSG.WARNDATIANAGRAFICI),
        msgTab: this.client.getMsgApplicativo(MSG.WARN_TAB),
      })
        .subscribe(({ msgErrorComuneAlreadyPresent, msgInformativi, msgErrorDeleteComuneVal, schedaEnte, province, comuni, comAssociati, tipoEnte, asl, msgTab, warn, comEliminati, comunimod }) => {
          this.warn = warn.testoMsgApplicativo;
          this.msgErrorComuneAlreadyPresent = msgErrorComuneAlreadyPresent.testoMsgApplicativo;
          this.client.azioni;
          this.popoverParamBody = msgInformativi[0].testoMsgInformativo;
          this.msgErrorDeleteComuneVal = msgErrorDeleteComuneVal.testoMsgApplicativo;
          this.msgTab = msgTab.testoMsgApplicativo;
          this.denominazioneEnte = schedaEnte;
          this.listaComuniGeneral = comuni;
          this.listaComuniGeneralMod = comunimod;
          this.initFormDenominazione(this.denominazioneEnte);
          this.listaTipoEnti = tipoEnte;
          this.listaProvinceSede = province;
          this.listaComuniDenominazione = this.listaComuniGeneral.filter(element => element.provincia.codIstatProvincia == this.denominazioneEnte.comune.provincia.codIstatProvincia);

          this.listaAsl = asl;
          this.initResponabileEnteForm(this.denominazioneEnte.responsabileEnte);

          this.listaComuniAssociati = comAssociati;
          this.listaComuniEliminati = comEliminati;
          this.initListaComuniToAss();

          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false);
          });
    }

  }


  initFormDenominazione(schedaEnte: AnagraficaEnteGreg) {
    this.enteForm = this.fb.group({
      idSchedaEntegestore: [this.idScheda],
      codiceRegionale: [schedaEnte.codiceRegionale, [Validators.pattern('[0-9]*'), Validators.required]],
      codiceFiscale: [schedaEnte.codiceFiscale, [Validators.maxLength(11), Validators.required, this.noWhitespaceValidator]],
      denominazione: [schedaEnte.denominazione, [Validators.maxLength(300), Validators.required, this.noWhitespaceValidator]],
      partitaIva: [schedaEnte.partitaIva, [Validators.maxLength(12)]],
      tipoEnte: [schedaEnte.tipoEnte.codTipoEnte, Validators.required],
      comune: [schedaEnte.comune.codIstatComune, Validators.required],
      codiceIstat: [schedaEnte.codiceIstat, [Validators.maxLength(9), Validators.required, this.noWhitespaceValidator]],
      provincia: [schedaEnte.comune.provincia.codIstatProvincia, Validators.required],
      asl: [schedaEnte.asl.codAsl, Validators.required],
      email: [schedaEnte.email, [Validators.email, Validators.required]],
      telefono: [schedaEnte.telefono, [Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12), Validators.required]],
      pec: [schedaEnte.pec, [Validators.email, Validators.required]]
    })
  }

  initResponabileEnteForm(resEnte: ResponsabileEnteGreg) {
    this.responsabileEnteForm = this.fb.group({
      idResponsabile: [resEnte.idResponsabile],
      nome: [resEnte.nome, [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator]],
      cognome: [resEnte.cognome, [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator]],
      cellulare: [resEnte.cellulare, [Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12)]],
      telefono: [resEnte.telefono, [Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12), Validators.required]],
      codiceFiscale: [resEnte.codiceFiscale, [Validators.maxLength(16), Validators.required, this.noWhitespaceValidator]],
      eMail: [resEnte.eMail, [Validators.email, Validators.required]]
    })
  }

  initListaComuniToAss() {
    this.listaComuniToAss = this.listaComuniGeneralMod;
    if (this.listaComuniAssociati.length != 0) {
      for (let comune of this.listaComuniAssociati) {
        this.listaComuniToAss = this.listaComuniToAss.filter(com => com.codIstatComune !== comune.codiceIstat);
      }
      this.listaComuniToAss = this.listaComuniToAss;
    }
    else {
      this.listaComuniToAss = this.listaComuniGeneralMod;
    }
  }

  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }
  addComuneAssociato() {
    let comune = this.listaComuniGeneralMod.find(comune => comune.codIstatComune == this.comuneSelected);
    if (comune) {
      let alreadyPresent = this.listaComuniAssociati.find(x => x.idComune === comune.idComune);
      if (comune && !alreadyPresent) {
        this.listaComuniAssociati.push(new ComuneAssociatoGreg(null, comune.codIstatComune, comune.desComune, comune.idComune, null, null));
        this.listaComuniAssociati.sort(function (a, b) {
          return a.desComune.toLocaleLowerCase().localeCompare(b.desComune.toLocaleLowerCase());
        })
        this.comuniTable.renderRows();
        let indexComune = this.listaComuniGeneralMod.findIndex(comune => comune.codIstatComune == this.comuneSelected);
        this.listaComuniGeneralMod.splice(indexComune, 1);
        this.comuneSelected = 'null';
      } else if (alreadyPresent) {
        this.toastService.showError({ text: this.msgErrorComuneAlreadyPresent });
      }
    }
    this.filterComuni();
    this.initListaProvince('add');
    this.comuneSelected = 'null';
  }

  deleteComuneAssociato(codComune: string) {
    let comune = this.listaComuniGeneralSede.find(comune => comune.codIstatComune == codComune);
    let indexComune = this.listaComuniAssociati.findIndex(comune => comune.codiceIstat == codComune);
    if (indexComune !== -1) {
      this.listaComuniAssociati.splice(indexComune, 1);
      this.comuniTable.renderRows();
      //devi aggiungere il comune e provincia nell'elenco iniziale filtrato
      this.listaComuniGeneralMod.push(new ComuneGreg(comune.idComune, comune.codIstatComune, comune.desComune, null, null, null, null, comune.provincia));
      this.listaComuniGeneralMod.sort(function (a, b) {
        return a.desComune.toLocaleLowerCase().localeCompare(b.desComune.toLocaleLowerCase());
      })
    }
    this.provinciaSelected = 'null';
    this.comuneSelected = 'null';
    this.filterComuni();
    this.initListaProvince('del');
  }

  // filtra i comuni nella sezione denominazione in base alla provincia selezionata
  setListaComuniDenominazione() {
    this.listaComuniDenominazione = this.listaComuniGeneralSede;
    let provincia = this.enteForm.value.provincia;
    if (provincia) {
      this.listaComuniDenominazione = this.listaComuniDenominazione.filter(element => element.provincia.codIstatProvincia == provincia);
    } else if (!provincia) {
      this.listaComuniDenominazione = this.listaComuniGeneralSede;
    }
    // in sezione denominazione ogni volta che si cambia provincia vengono resettati i valori del comune ma salvati quelli della provincia
    this.enteForm.value.comune = undefined;
  }

  // filtra i comuni per la provincia selezionata
  filterComuni() {
    this.initListaComuniToAss();
    let provincia = this.provinciaSelected;
    if (provincia == 'null' || provincia == '') {
      this.listaComuniToAss = this.listaComuniToAss;
      this.comuneSelected = 'null';
    }
    else {
      this.listaComuniToAss = this.listaComuniToAss.filter(element => element.provincia.codIstatProvincia == provincia);
    }
  }

  // autoseleziona la provincia per il comune selezionato
  selectProvincia() {
    let provincia = this.provinciaSelected;
    let comune = this.listaComuniGeneralMod.find(comune => comune.codIstatComune == this.comuneSelected);
    if (comune) {
      this.provinciaSelected = this.listaProvince.find(provincia => provincia.codIstatProvincia == comune.provincia.codIstatProvincia).codIstatProvincia;
      this.provinciaSelected = this.provinciaSelected;
    }
    else {
      this.initListaComuniToAss();
      this.provinciaSelected = 'null';
    }
  }

  // setta l'oggetto tipoEnte della sezione denominazione
  setNewTipoEnte() {
    let tipoEnte = this.listaTipoEnti.find(tE => tE.codTipoEnte == this.enteForm.value.tipoEnte);
    if (tipoEnte) {
      this.enteForm.value.tipoEnte = tipoEnte;
    }
  }

  // setta l'oggetto comune della sezione denominazione
  setNewComune() {
    let comune = this.listaComuniDenominazione.find(comune => comune.codIstatComune == this.enteForm.value.comune);
    if (comune) {
      this.enteForm.value.comune = comune;
    }
    let provincia = this.listaProvince.find(provincia => provincia.codIstatProvincia == this.enteForm.value.provincia);
    if (provincia) {
      this.enteForm.value.comune.provincia = provincia;
    }
  }

  // setta l'oggetto asl della sezione denominazione
  setNewAsl() {
    let asl = this.listaAsl.find(asl => asl.codAsl == this.enteForm.value.asl);
    if (asl) {
      this.enteForm.value.asl = asl;
    }
  }

  backButton() {
    this.router.navigate(["/operatore-regionale/enti-gestori-attivi"], { relativeTo: this.route, skipLocationChange: true });
  }

  chiudiEnte() {
    // this.modalService.open(ChiudiEnteComponent, { size: 'lg' });
    // Serve a passare eventuali dati alla modale
    const modalRef = this.modalService.open(ChiudiEnteComponent, { size: 'lg' });
    modalRef.componentInstance.idSchedaEnteGestore = this.idScheda;
    modalRef.componentInstance.denominazione = this.denominazioneEnte.denominazione;
    modalRef.componentInstance.email = this.denominazioneEnte.email;
    modalRef.componentInstance.nome = this.denominazioneEnte.responsabileEnte.nome;
    modalRef.componentInstance.cognome = this.denominazioneEnte.responsabileEnte.cognome;
    modalRef.componentInstance.emailResp = this.denominazioneEnte.responsabileEnte.eMail;
    modalRef.result.then((result) => {
      if (result) {
        //this.ngOnInit();
        this.client.getLastStato(this.idScheda).subscribe(lastStato => {
          this.lastStato = lastStato;
          let data = new Date(this.lastStato.dal);
          if (this.lastStato.stato == STATO_ENTE_DESC_LUNGA.CHIUSO) {
            if (data <= this.dataSistema) {
              this.statoEnte = "CHI";
            } else {
              this.statoEnte = "APE";
            }
          } else {
            this.statoEnte = "APE";
          }
          this.controlloStatoChiudi();
          this.controlloStato();
          this.cronologiaStatoMod.ngOnInit();
          this.client.ricercaEnte = [];
          this.client.ricercaEnteCruscotto = [];
        });
      }
    }).catch((res) => { });
    // if(modalRef.){
    //   this.ngOnInit();
    // }
  }

  salvaModifiche() {
    this.setNewTipoEnte();
    this.setNewComune();
    this.setNewAsl();
    this.datiEnteToSave = new DatiAnagraficiToSave();
    this.datiEnteToSave.sameData = false;
    delete this.enteForm.value.provincia;

    this.datiEnteToSave.datiEnte = this.enteForm.value;

    if (this.datiEnteToSave.datiEnte.denominazione != this.denominazioneEnte.denominazione ||
      this.datiEnteToSave.datiEnte.partitaIva != this.denominazioneEnte.partitaIva ||
      this.datiEnteToSave.datiEnte.tipoEnte.codTipoEnte != this.denominazioneEnte.tipoEnte.codTipoEnte ||
      this.datiEnteToSave.datiEnte.comune.codIstatComune != this.denominazioneEnte.comune.codIstatComune ||
      this.datiEnteToSave.datiEnte.codiceIstat != this.denominazioneEnte.codiceIstat ||
      this.datiEnteToSave.datiEnte.asl.codAsl != this.denominazioneEnte.asl.codAsl ||
      this.datiEnteToSave.datiEnte.email != this.denominazioneEnte.email ||
      this.datiEnteToSave.datiEnte.telefono != this.denominazioneEnte.telefono ||
      this.datiEnteToSave.datiEnte.pec != this.denominazioneEnte.pec) {
      this.modificaEnte = true;
    }

    this.datiEnteToSave.datiEnte.responsabileEnte = this.responsabileEnteForm.value;

    if (this.datiEnteToSave.datiEnte.responsabileEnte.nome != this.denominazioneEnte.responsabileEnte.nome ||
      this.datiEnteToSave.datiEnte.responsabileEnte.cognome != this.denominazioneEnte.responsabileEnte.cognome ||
      this.datiEnteToSave.datiEnte.responsabileEnte.cellulare != this.denominazioneEnte.responsabileEnte.cellulare ||
      this.datiEnteToSave.datiEnte.responsabileEnte.telefono != this.denominazioneEnte.responsabileEnte.telefono ||
      this.datiEnteToSave.datiEnte.responsabileEnte.codiceFiscale != this.denominazioneEnte.responsabileEnte.codiceFiscale ||
      this.datiEnteToSave.datiEnte.responsabileEnte.eMail != this.denominazioneEnte.responsabileEnte.eMail) {
      this.modificaResp = true;
    }
    
    this.datiEnteToSave.dataModifica = this.dataModificata;

    this.datiEnteToSave.comuniAssociati = this.listaComuniAssociati;
    let esiste: boolean;
    for (let i = 0; i < this.datiEnteToSave.comuniAssociati.length; i++) {
      esiste = false;
      for (let j = 0; j < this.comuniIniziali.length; j++) {
        if (this.comuniIniziali[j].codiceIstat == this.datiEnteToSave.comuniAssociati[i].codiceIstat) {
          esiste = true;
        }
      }
      if (!esiste) {
        this.modificaComune = true;
      }
    }

    let esiste2: boolean;
    for (let i = 0; i < this.comuniIniziali.length; i++) {
      esiste2 = false;
      for (let j = 0; j < this.datiEnteToSave.comuniAssociati.length; j++) {
        if (this.comuniIniziali[i].codiceIstat == this.datiEnteToSave.comuniAssociati[j].codiceIstat) {
          esiste2 = true;
        }
      }
      if (!esiste2) {
        this.modificaComune = true;
      }
    }

    this.datiEnteToSave.modificaEnte = this.modificaEnte;
    this.datiEnteToSave.modificaResp = this.modificaResp;
    this.datiEnteToSave.modificaComune = this.modificaComune;
    this.datiEnteToSave.profilo = this.client.profilo;
    
    this.provinciaSelected = 'null';
    this.comuneSelected = 'null';
    this.listaComuniToAss = this.listaComuniGeneralMod;
    let d = this.dataMax.split('/')
    if (this.modificaEnte || this.modificaResp || this.modificaComune) {
      if (new Date(d[2] + '-'+d[1]+'-'+d[0] + ' 00:00:00.000').getTime() == this.dataModificata.getTime()) {
        let messaggio = this.msgPopUp;
        const dialogRef = this.dialog.open(CancPopupComponent, {
          width: '650px',
          disableClose: true,
          autoFocus: true,
          data: { titolo: 'Conferma Salvataggio', messaggio: messaggio }
        });
        dialogRef.afterClosed().subscribe(r => {
          if (r) {
            this.client.spinEmitter.emit(true);
            this.datiEnteToSave.sameData = true;
            this.client.saveDatiAnagrafici(this.datiEnteToSave)
              .subscribe(
                (data: GenericResponseWarnErrGreg) => {
                  if (data.warnings && data.warnings.length > 0) {
                    for (let warn of data.warnings) {
                      this.errorMessage.error.descrizione = warn;
                      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
                    }
                  }
                  if (data.errors && data.errors.length > 0) {
                    for (let err of data.errors) {
                      this.errorMessage.error.descrizione = err;
                      this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
                    }
                  } else {
                    this.ngOnInit();
                    this.modifica = false;
                    // this.client.spinEmitter.emit(false);
                    this.toastService.showSuccess({ text: data.descrizione });
                  }
                  // 	this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(data => {
                  //     this.rendicontazione.statoRendicontazione.codStatoRendicontazione = data.statoRendicontazione.codStatoRendicontazione
                  // });
                },
                err => {
                  this.client.spinEmitter.emit(false);
                }
              );
          }else {
            this.client.spinEmitter.emit(false);
          }
        })
      }else {
      this.client.spinEmitter.emit(true);
      this.client.saveDatiAnagrafici(this.datiEnteToSave)
        .subscribe(
          (data: GenericResponseWarnErrGreg) => {
            if (data.warnings && data.warnings.length > 0) {
              for (let warn of data.warnings) {
                this.errorMessage.error.descrizione = warn;
                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
              }
            }
            if (data.errors && data.errors.length > 0) {
              for (let err of data.errors) {
                this.errorMessage.error.descrizione = err;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
              }
            } else {
              this.ngOnInit();
              this.modifica = false;
              // this.client.spinEmitter.emit(false);
              this.toastService.showSuccess({ text: data.descrizione });
            }
            // 	this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(data => {
            //     this.rendicontazione.statoRendicontazione.codStatoRendicontazione = data.statoRendicontazione.codStatoRendicontazione
            // });
          },
          err => {
            this.client.spinEmitter.emit(false);
          }
        );
      }
    } else {
      this.errorMessage.error.descrizione = this.warn;
      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
      this.client.spinEmitter.emit(false);
    }
    this.enteForm.value.provincia = '';

  }

  apricronologia() {
    if (this.espansa)
      this.espansa = false;
    else
      this.espansa = true;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
  }

  apricomuneeliminato() {
    if (this.espansacomune)
      this.espansacomune = false;
    else
      this.espansacomune = true;
    this.statecomune = (this.statecomune === 'default' ? 'rotated' : 'default');
  }
  //  routeTo(path: string){
  //     const navigationExtras: NavigationExtras = {
  //       relativeTo: this.route, 
  //       skipLocationChange: true,
  //       state: {
  //         rendicontazione: this.rendicontazione,
  // 		 datiEnte: this.datiEnte
  //       }
  //     };
  //     this.router.navigate([path], navigationExtras);
  //   }

  controlloStatoChiudi() {
    if (this.lastStato && this.lastStato.stato == STATO_ENTE_DESC_LUNGA.CHIUSO) {
      return true;
    }
    return false;
  }

  controlloStato() {
    if (this.statoEnte == STATO_ENTE.CHIUSO) {
      return true;
    }
    return false;
  }

  disabilita() {
    let dFV = this.dataFineValidita != null;
    let cS = this.controlloStato();
    let m = !this.modifica;
    return dFV || cS || m;
  }

  disabilitaContatti() {
    let dFV = this.dataFineValidita != null;
    let cS = this.controlloStato();
    let m = !this.modifica;
    return dFV || cS || m || this.client.azioni.get('EnteContatti')[0];
  }

  disabilitaResponsabile() {
    let dFV = this.dataFineValidita != null;
    let cS = this.controlloStato();
    let m = !this.modifica;
    return dFV || cS || m || this.client.azioni.get('EnteResponsabile')[0];
  }

  prova() {
    let value = this.enteForm.get('denominazione').value;
    let valid = this.enteForm.get('denominazione').valid;
    if (this.disabilita()) {
      // codiceRegionale: [''],
      // codiceFiscale: [''],
      // denominazione: [''],
      // partitaIva: [''],
      // tipoEnte: [''],
      // comune: [''],
      // codiceIstat: [''],
      // provincia: [''],
      // asl: [''],
      // email: [''],
      // telefono: [''],
      // pec: [''],
      this.enteForm.controls.denominazione.disable();
      this.enteForm.controls.partitaIva.disable();
      this.enteForm.controls.tipoEnte.disable();
      this.enteForm.controls.comune.disable();
      this.enteForm.controls.codiceIstat.disable();
      this.enteForm.controls.provincia.disable();
      this.enteForm.controls.asl.disable();
      this.enteForm.controls.email.disable();
      this.enteForm.controls.telefono.disable();
      this.enteForm.controls.pec.disable();
      this.responsabileEnteForm.disable();
    } else {
      this.enteForm.enable();
      this.enteForm.controls.codiceRegionale.disable();
      this.enteForm.controls.codiceFiscale.disable();
      this.responsabileEnteForm.enable();
    }
    return true;
  }

  ripristinaEnte() {
    // this.modalService.open(ChiudiEnteComponent, { size: 'lg' });
    // Serve a passare eventuali dati alla modale
    const modalRef = this.modalService.open(RipristinaEnteComponent, { size: 'lg' });
    modalRef.componentInstance.idSchedaEnteGestore = this.idScheda;
    modalRef.componentInstance.denominazione = this.denominazioneEnte.denominazione;
    modalRef.componentInstance.email = this.denominazioneEnte.email;
    modalRef.componentInstance.nome = this.denominazioneEnte.responsabileEnte.nome;
    modalRef.componentInstance.cognome = this.denominazioneEnte.responsabileEnte.cognome;
    modalRef.componentInstance.emailResp = this.denominazioneEnte.responsabileEnte.eMail;
    modalRef.result.then((result) => {
      if (result) {
        //this.ngOnInit();
        this.client.getLastStato(this.idScheda).subscribe(lastStato => {
          this.lastStato = lastStato;
          let data = new Date(this.lastStato.dal);
          if (this.lastStato.stato == STATO_ENTE_DESC_LUNGA.CHIUSO) {
            if (data <= this.dataSistema) {
              this.statoEnte = "CHI";
            } else {
              this.statoEnte = "APE";
            }
          } else {
            this.statoEnte = "APE";
          }
          this.client.ricercaEnte = [];
          this.client.ricercaEnteCruscotto = [];
          this.controlloStatoChiudi();
          this.controlloStato();
          this.cronologiaStatoMod.ngOnInit();
        });
      }
    }).catch((res) => { });
    // if(modalRef.){
    //   this.ngOnInit();
    // }
  }

  openStorico() {
    const modalRef = this.modalService.open(StoricoComponent, { size: 'lg' });
    modalRef.componentInstance.archive = true;
    modalRef.componentInstance.idSchedaEnteGestore = this.idScheda;
    modalRef.componentInstance.denominazione = this.denominazioneEnte.denominazione;
    modalRef.componentInstance.statoEnte = this.statoEnte;
  }


  initListaProvince(fase: string) {
    if (fase == 'initial') {
      if (this.listaComuniGeneralMod.length != 0) {
        for (let comune of this.listaComuniGeneralMod) {
          let indexProvicia = this.listaProvince.findIndex(provincia => provincia.codIstatProvincia == comune.provincia.codIstatProvincia);
          if (indexProvicia === -1) {
            this.listaProvince.push(comune.provincia);
            this.listaProvince.sort(function (a, b) {
              return a.desProvincia.toLocaleLowerCase().localeCompare(b.desProvincia.toLocaleLowerCase());
            })
          }
        }
      }
    }
    else if (fase == 'add') {
      if (this.listaComuniToAss.length == 0) {
        let indexProvincia = this.listaProvince.findIndex(provincia => provincia.codIstatProvincia == this.provinciaSelected);
        this.listaProvince.splice(indexProvincia, 1);
      }
    }
    else if (fase == 'del') {
      if (this.listaComuniToAss.length != 0) {
        for (let comune of this.listaComuniToAss) {
          let indexProvicia = this.listaProvince.findIndex(provincia => provincia.codIstatProvincia == comune.provincia.codIstatProvincia);
          if (indexProvicia === -1) {
            this.listaProvince.push(comune.provincia);
            this.listaProvince.sort(function (a, b) {
              return a.desProvincia.toLocaleLowerCase().localeCompare(b.desProvincia.toLocaleLowerCase());
            })
          }
        }
      }
    }
    this.provinciaSelected = 'null';
    this.comuneSelected = 'null';
  }
  openModifica() {
    this.dataModificata = null;
    this.modifica = true;
    this.provinciaSelected = 'null';
    this.comuneSelected = 'null';
  }

  closeModifica() {
    this.modifica = false;
    this.listaComuniToAss = this.listaComuniGeneral;
    this.initListaProvince('initial');
    this.listaComuniAssociati = this.comuniIniziali;
  }

  disabledSalva(){
    if((!this.enteForm.valid || !this.responsabileEnteForm.valid || this.dataModificata==null)){
      return true;
    }
    return false;
  }
}
