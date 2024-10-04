import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
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
import { DATIENTE_INFO, DOC, ERRORS, MSG, PATTERN, SECTION, STATO_ENTE, STATO_ENTE_DESC_LUNGA } from '@greg-app/constants/greg-constants';
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
import { elementEventFullName } from '@angular/compiler/src/view_compiler/view_compiler';
import { ListaGreg } from '@greg-app/app/dto/ListaGreg';

@Component({
  selector: 'app-nuovo-ente',
  templateUrl: './nuovo-ente.component.html',
  styleUrls: ['./nuovo-ente.component.css']
})
export class NuovoEnteComponent implements OnInit {

  state: string = 'default';
  espansa: boolean;
  enteValues: string[] = [];
  dataSistema = new Date();
  docIniziale: string = DOC.DOC_INIZIALE;
  docFinale: string = DOC.DOC_FINALE;

  @ViewChild('comuniTable', { static: false }) comuniTable: MatTable<any>;

  disabled: boolean;
  visible: boolean;
  visibleDocI: boolean = true;
  visibleDocII: boolean = true;
  visibleTrashDocI: boolean = true;
  visibleTrashDocII: boolean = true;
  buttonAllegatoIniziale: boolean = true;
  buttonAllegatoFinale: boolean = true;
  avoidAllegatiChangeI: boolean = false;
  avoidAllegatiChangeII: boolean = false;

  listaComuniGeneral: ComuneGreg[];
  listaComuniDenominazione: ComuneGreg[] = [];
  listaComuniToAss: ComuneGreg[] = [];
  listaProvince: ProvinciaGreg[];

  msgErrorPrest: string = '';
  msgErrorDoc: string = '';
  msgErrorComuneAlreadyPresent: string = '';
  msgErrorPrestAlreadyPresent: string = '';
  msgErrorUploadAllegati: string = '';
  msgErrorDeletePrestVal: string = '';
  msgErrorDeletePrestValModC: string = '';
  msgErrorDeleteComuneVal: string = '';

  listaTipoEnti: TipoEnteGreg[] = [];
  listaAsl: AslGreg[] = [];

  denominazioneEnte: AnagraficaEnteGreg;
  cronologia: CronologiaGreg = new CronologiaGreg();

  comune: ComuneAssociatoGreg;
  comuneSelected: string;
  provinciaSelected: string;

  listaComuniAssociati: ComuneAssociatoGreg[] = [];

  modificaEnte: boolean = false;
  modificaResp: boolean = false;
  modificaComune: boolean = false;

  listaComuniValorizzateModA1: string[] = [];
  listaComuniValorizzateModA2: string[] = [];
  listaComuniValorizzateModE: string[] = [];

  columnsComuniAssociati: Array<string> = ['codiceIstat', 'comune', 'azioni'];

  navigation: Navigation;
  ente: EnteGreg;
  warn: string = '';

  datiEnteToSave: DatiAnagraficiToSave;

  enteForm: FormGroup = new FormGroup({});
  responsabileEnteForm: FormGroup = new FormGroup({});

  popoverParamTitle: string = DATIENTE_INFO.INFO_PARAM_TITLE;
  popoverParamBody: string = '';

  aperturaForm: FormGroup;

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
 

  constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder,
    public client: GregBOClient, public toastService: AppToastService, private gregError: GregErrorService) {
    this.navigation = this.router.getCurrentNavigation();
    let enteValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      enteValues.push(frag);
    });
  }

  ngOnInit() {

    this.aperturaForm = this.fb.group({
      dataApertura: ['', Validators.compose([Validators.required, this.noWhitespaceValidator])],
    });

    this.enteForm = this.fb.group({
      codiceRegionale: ['', Validators.compose([Validators.pattern('[0-9]*'), Validators.required])],
      codiceFiscale: ['',Validators.compose([Validators.maxLength(11), Validators.required, this.noWhitespaceValidator])],
      denominazione: ['', Validators.compose([Validators.maxLength(300), Validators.required, this.noWhitespaceValidator])],
      partitaIva: ['', Validators.maxLength(12)],
      tipoEnte: ['', Validators.required],
      comune: ['', Validators.required],
      codiceIstat: ['', Validators.compose([Validators.maxLength(9), Validators.required, this.noWhitespaceValidator])],
      provincia: ['', Validators.required],
      asl: ['', Validators.required],
      email: ['', Validators.compose([Validators.email, Validators.required])],
      telefono: ['', Validators.compose([Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12), Validators.required])],
      pec: ['', Validators.compose([Validators.email, Validators.required])],
    })

    this.responsabileEnteForm = this.fb.group({
      nome: ['', Validators.compose([Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator])],
      cognome: ['', Validators.compose([Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator])],
      cellulare: ['', Validators.compose([Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12)])],
      telefono: ['', Validators.compose([Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12), Validators.required])],
      codiceFiscale: ['', Validators.compose([Validators.maxLength(16), Validators.required, this.noWhitespaceValidator])],
      eMail: ['', Validators.compose([Validators.email, Validators.required])],
    })
  
    this.dataSistema.setHours(0,0,0,0);
    
    this.client.spinEmitter.emit(true);
      forkJoin({

        msgErrorComuneAlreadyPresent: this.client.getMsgApplicativo(ERRORS.ERROR_COMUNE_ALREADY_PRESENT),
        msgInformativi: this.client.getMsgInformativi(SECTION.DATI_ENTE),
  
        msgErrorDeleteComuneVal: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_COMUNE_ASS),
      
       
        province: this.client.getDatiEnteProvince(formatDate(new Date(),'yyyy-MM-dd','en')),
        comuni: this.client.getListaComuni(formatDate(new Date(),'yyyy-MM-dd','en')),
        tipoEnte: this.client.getListaTipoEnte(),
        asl: this.client.getAsl(formatDate(new Date(),'yyyy-MM-dd','en')),
        warn: this.client.getMsgApplicativo(MSG.WARNDATIANAGRAFICI),
        msgTab: this.client.getMsgApplicativo(MSG.WARN_TAB),
      })
        .subscribe(({ msgErrorComuneAlreadyPresent, msgInformativi, msgErrorDeleteComuneVal, province, comuni,  tipoEnte, asl, msgTab, warn }) => {
          this.warn = warn.testoMsgApplicativo;
          this.msgErrorComuneAlreadyPresent = msgErrorComuneAlreadyPresent.testoMsgApplicativo;
          this.client.azioni;
          this.popoverParamBody = msgInformativi[0].testoMsgInformativo;
          this.msgErrorDeleteComuneVal = msgErrorDeleteComuneVal.testoMsgApplicativo;
          this.msgTab = msgTab.testoMsgApplicativo;
          this.listaComuniGeneral = comuni;
  
          this.listaTipoEnti = tipoEnte;
          this.listaProvince = province;

          this.listaAsl = asl;
  
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false);
          });
    
    
  }

  initListaComuniToAss() {
    this.listaComuniToAss = this.listaComuniGeneral;
    if (this.listaComuniAssociati.length != 0) {
      for (let comune of this.listaComuniAssociati) {
        this.listaComuniToAss = this.listaComuniToAss.filter(com => com.codIstatComune !== comune.codiceIstat);
      }
      this.listaComuniToAss = this.listaComuniToAss;
    }
    else {
      this.listaComuniToAss = this.listaComuniGeneral;
    }
  }

  addComuneAssociato() {
    let comune = this.listaComuniGeneral.find(comune => comune.codIstatComune == this.comuneSelected);
    if (comune) {
      let alreadyPresent = this.listaComuniAssociati.find(x => x.idComune === comune.idComune);
      if (comune && !alreadyPresent) {
        this.listaComuniAssociati.push(new ComuneAssociatoGreg(null, comune.codIstatComune, comune.desComune, comune.idComune,null,null));
        this.listaComuniAssociati.sort(function (a, b) {
          return a.desComune.toLocaleLowerCase().localeCompare(b.desComune.toLocaleLowerCase());
        })
        this.comuniTable.renderRows();
        this.comuneSelected = undefined;
      } else if (alreadyPresent) {
        this.toastService.showError({ text: this.msgErrorComuneAlreadyPresent });
      }
    }
    this.filterComuni();

    this.comuneSelected = null;
  }

  deleteComuneAssociato(codComune: string) {
    let indexComune = this.listaComuniAssociati.findIndex(comune => comune.codiceIstat == codComune);
    if (indexComune !== -1) {
      this.listaComuniAssociati.splice(indexComune, 1);
      this.comuniTable.renderRows();
    }
    this.filterComuni();
  }

  // filtra i comuni nella sezione denominazione in base alla provincia selezionata
  setListaComuniDenominazione() {
    this.listaComuniDenominazione = this.listaComuniGeneral;
    let provincia = this.enteForm.value.provincia;
    if (provincia) {
      this.listaComuniDenominazione = this.listaComuniDenominazione.filter(element => element.provincia.codIstatProvincia == provincia);
    } else if (!provincia) {
      this.listaComuniDenominazione = this.listaComuniGeneral;
    }
    // in sezione denominazione ogni volta che si cambia provincia vengono resettati i valori del comune ma salvati quelli della provincia
    this.enteForm.value.comune = undefined;
  }

  // filtra i comuni per la provincia selezionata
  filterComuni() {
    this.initListaComuniToAss();
    let provincia = this.provinciaSelected;
    if (provincia) {
      this.listaComuniToAss = this.listaComuniToAss.filter(element => element.provincia.codIstatProvincia == provincia);
    } else if (provincia == null) {
      this.listaComuniToAss = this.listaComuniToAss;
      this.comuneSelected = null;
    }
  }

  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }

  // autoseleziona la provincia per il comune selezionato
  selectProvincia() {
    let provincia = this.provinciaSelected;
    let comune = this.listaComuniGeneral.find(comune => comune.codIstatComune == this.comuneSelected);
    if (comune) {
      this.provinciaSelected = this.listaProvince.find(provincia => provincia.codIstatProvincia == comune.provincia.codIstatProvincia).codIstatProvincia;
      this.provinciaSelected = this.provinciaSelected;
    }
    else {
      this.initListaComuniToAss();
      this.provinciaSelected = null;
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


  salvaModifiche() {
      this.setNewTipoEnte();
      this.setNewComune();
      this.setNewAsl();
      this.datiEnteToSave = new DatiAnagraficiToSave();
      delete this.enteForm.value.provincia;

      this.datiEnteToSave.datiEnte = this.enteForm.value;


      this.datiEnteToSave.datiEnte.responsabileEnte = this.responsabileEnteForm.value;   
    
      this.datiEnteToSave.comuniAssociati = this.listaComuniAssociati;
    
      this.datiEnteToSave.profilo = this.client.profilo;
      this.datiEnteToSave.listaSelezionata = this.client.listaSelezionata;
      this.datiEnteToSave.dataApertura = this.aperturaForm.controls.dataApertura.value;
      this.client.spinEmitter.emit(true);
      this.provinciaSelected = null;
      this.comuneSelected = null;
      this.listaComuniToAss = this.listaComuniGeneral;
        this.client.creaNuovoEnte(this.datiEnteToSave)
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
                this.client.listaenti.push(data.idEnte);
                this.client.ricercaEnte = [];
                this.client.ricercaEnteCruscotto = [];
                this.ngOnInit();
                // this.client.spinEmitter.emit(false);

                this.toastService.showSuccess({ text: data.descrizione });
                this.router.navigate(["/operatore-regionale/enti-gestori-attivi"], { relativeTo: this.route, skipLocationChange: true });
              }
              // 	this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(data => {
              //     this.rendicontazione.statoRendicontazione.codStatoRendicontazione = data.statoRendicontazione.codStatoRendicontazione
              // });
            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
      this.enteForm.value.provincia = '';

  }







}
