import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { ComuneAssociatoGreg } from '@greg-app/app/dto/ComuneAssociatoGreg';
import { ComuneGreg } from '@greg-app/app/dto/ComuneGreg';
import { CronologiaGreg } from '@greg-app/app/dto/CronologiaGreg';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { PrestazioneAssociataGreg } from '@greg-app/app/dto/PrestazioneAssociataGreg';
import { PrestazioneGreg } from '@greg-app/app/dto/PrestazioneGreg';
import { ProvinciaGreg } from '@greg-app/app/dto/ProvinciaGreg';
import { ResponsabileEnteGreg } from '@greg-app/app/dto/ResponsabileEnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { DatiEnteToSave } from '@greg-app/app/dto/DatiEnte';
import { TipoEnteGreg } from '@greg-app/app/dto/TipoEnteGreg';
import { AslGreg } from '@greg-app/app/dto/AslGreg';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DATIENTE_INFO, DOC, ERRORS, MSG, PATTERN, PREST_RES_SEMIRES, RENDICONTAZIONE_STATUS, SECTION, TIPOLOGIA_FONDI } from '@greg-app/constants/greg-constants';
import { AllegatoGreg } from '@greg-app/app/dto/AllegatoGreg';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { CurrencyFormat } from '@greg-app/app/currencyFormatter';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { FileAllegatoToUploadGreg } from '@greg-app/app/dto/FileAllegatoToUploadGreg';
import { formatBase64 } from '@greg-app/shared/util';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { ModelObbligo } from '@greg-app/app/dto/ModelObbligo';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { CronologiaModelliArchivioComponent } from '../cronologia-modelli-archivio/cronologia-modelli-archivio.component';
import { UtenzeFnps } from '@greg-app/app/dto/UtenzeFnps';
import { Fondi } from '@greg-app/app/dto/Fondi';


@Component({
	selector: 'app-dati-ente-Archivio',
	templateUrl: './dati-ente-archivio.component.html',
	styleUrls: ['./dati-ente-archivio.component.css'],
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
export class DatiEnteArchivioComponent implements OnInit {

	state: string = 'default';
	espansa: boolean;
	enteValues: string[] = [];

	docIniziale: string = DOC.DOC_INIZIALE;
	docFinale: string = DOC.DOC_FINALE;

	@ViewChild('comuniTable', { static: false }) comuniTable: MatTable<any>;
	@ViewChild('prestazioniTable', { static: false }) prestazioniTable: MatTable<any>;
	@ViewChild('documentiTable', { static: false }) documentiTable: MatTable<any>;
	@ViewChild('modelliTable', { static: false }) modelliTable: MatTable<any>;
	@ViewChild(CronologiaModelliArchivioComponent, { static: false }) cronologiaMod: CronologiaModelliArchivioComponent;

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

	listaPrestazioniInit: PrestazioneGreg[] = [];
	listaPrestazioniGeneral: PrestazioneGreg[] = [];
	listaPrestazioni: PrestazioneGreg[] = [];
	listaPrestazioniRes: PrestazioneGreg[] = [];
	listaPrestazioniSemires: PrestazioneGreg[] = [];

	msgErrorPrest: string = '';
	msgErrorDoc: string = '';
	msgErrorComuneAlreadyPresent: string = '';
	msgErrorModelloAlreadyPresent: string = '';
	msgErrorPrestAlreadyPresent: string = '';
	msgErrorUploadAllegati: string = '';
	msgErrorDeletePrestVal: string = '';
	msgErrorDeletePrestValModC: string = '';
	msgErrorDeleteComuneVal: string = '';
	msgErrorDeleteModelliVal: string = '';
	listaTipoEnti: TipoEnteGreg[] = [];
	listaAsl: AslGreg[] = [];

	denominazioneEnte: EnteGreg;


	// cronologia: CronologiaGreg = new CronologiaGreg();

	comune: ComuneAssociatoGreg;
	comuneSelected: number;
	modelloSelected: number;
	provinciaSelected: any;
	prestazione: PrestazioneAssociataGreg;
	prestazioneSelected: number;

	listaComuniAssociati: ComuneAssociatoGreg[] = [];
	listaPrestazioniAssociate: PrestazioneAssociataGreg[] = [];
	listaCronologiaAssociata: CronologiaGreg[] = [];

	listaPrestazioniValorizzateModA: string[] = [];
	listaPrestazioniValorizzateModB1: string[] = [];
	listaPrestazioniValorizzateModC: string[] = [];
	listaComuniValorizzateModA1: string[] = [];
	listaComuniValorizzateModA2: string[] = [];
	listaComuniValorizzateModE: string[] = [];

	columnsComuniAssociati: Array<string> = ['codiceIstat', 'comune', 'dal', 'al', 'azioni'];
	columnsComuniAssociatiEnte: Array<string> = ['codiceIstat', 'comune', 'dal', 'al'];
	columnsPrestazioniAssociat: Array<string> = ['codice', 'prestazione', 'azioni'];
	columnsListaDocAllegati: Array<string> = ['documento', 'tipologia', 'azioni'];
	columnsCronologia: Array<string> = ['dataEora', 'utente', 'statoRendicontazione', 'notaEnte', 'notaInterna'];
	columnsCronologiaEnte: Array<string> = ['dataEora', 'utente', 'statoRendicontazione', 'notaEnte'];
	columnsModelliAssociati: Array<string> = ['modello', 'obbligatorio'];
	columnsModelliAssociatiNoAzione: Array<string> = ['modello', 'obbligatorio'];
	navigation: Navigation;
	ente: EnteGreg;

	// sezione documenti
	listaDocumentiAllegati: AllegatoGreg[] = [];
	fileIniziale: File = null;
	fileFinale: File = null;
	docInizialeName: string = '';
	docFinaleName: string = '';
	errDocFinale: string = '';
	errDocIniziale: string = '';
	errDocFinaleAllegato: string = '';
	errDocInizialeAllegato: string = '';
	formattedFileIniziale: string = '';
	formattedFileFinale: string = '';
	fileInizialeToUpload: FileAllegatoToUploadGreg;
	fileFinaleToUpload: FileAllegatoToUploadGreg;

	errPippi: string;

	datiEnteToSave: DatiEnteToSave;

	enteForm: FormGroup = new FormGroup({});
	responsabileEnteForm: FormGroup = new FormGroup({});
	parametriForm: FormGroup = new FormGroup({});
	checkForm: FormGroup = new FormGroup({});

	requiredDocIn: boolean;
	okDocIn: boolean = false;
	requiredDocFi: boolean;
	okDocFi: boolean = false;

	popoverParamTitle: string = DATIENTE_INFO.INFO_PARAM_TITLE;
	popoverParamBody: string = '';
	popoverDocTitle: string = DATIENTE_INFO.INFO_DOC_TITLE;
	popoverDocBody: string = '';

	popoverDocInizialiTitle: string = DATIENTE_INFO.INFO_DOC_INIZIALI_TITLE;
	popoverDocInizialiBody: string = '';
	popoverDocFinaliTitle: string = DATIENTE_INFO.INFO_DOC_FINALI_TITLE;
	popoverDocFinaliBody: string = '';

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
	datiEnte: DatiEnteGreg;
	rendicontazione: RendicontazioneEnteGreg;
	modelli: ModelTabTranche[] = [];
	modelliassociati: ModelTabTranche[] = [];
	listaModelliToAss: ModelTabTranche[] = [];
	obbligatorioSelect: boolean = false;
	obbligatorio: ModelObbligo[] = [];
	model: string;
	utenzeFnps: UtenzeFnps[];
	fnps: Fondi = new Fondi();
	quote: Fondi[] = [];
	azioniSistema: Fondi[] = [];
	fondi: Fondi[];
	fondoDaAssegnare: Fondi[];
	fondoSelezionato: Fondi = new Fondi();
	idSelectedAltreQuote: number;
	altreQuoteDaAssegnare: Fondi[];
	idSelectedAzioni: number;
	azioniDaAssegnare: Fondi[];
	azioneSelezionato: Fondi = new Fondi();
	idSelectedRegola: number;
	regolaDaAssegnare: Fondi[];
	totaleFondi: number = 0;
	totaleFondiDaStampare: string = '0,00';
	labelFondo: string;
	regolaDaAssegnareAzioni: Fondi[];
	idSelectedRegolaAzioni: number;
	fondoFnps: Fondi;
	totale: number = 0;
	totaleString: string = "0,00";
	messaggioPopupCalcolo: string;
	constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder,
		public client: GregBOClient, private modalService: NgbModal, public toastService: AppToastService,
		private currencyFormat: CurrencyFormat, private gregError: GregErrorService) {
		this.navigation = this.router.getCurrentNavigation();
		let enteValues: string[] = [];
		this.route.fragment.subscribe((frag: string) => {
			enteValues.push(frag);
		});
		this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
		this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : enteValues[0][1];
		this.model = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.model : null;
	}

	ngOnInit() {
		// if(this.model){
		//   this.routeToModello();
		// }
		this.fileIniziale = null;
		this.fileFinale = null;
		this.formattedFileIniziale = '';
		this.formattedFileFinale = '';
		this.fileInizialeToUpload = null;
		this.fileFinaleToUpload = null;
		this.errDocFinale = '';
		this.errDocIniziale = '';
		this.errDocFinaleAllegato = '';
		this.errDocInizialeAllegato = '';
		this.docInizialeName = '';
		this.docFinaleName = '';
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

		this.parametriForm = this.fb.group({
			fnps: [''],
			vincoloFondo: [''],
			pippi: ['']
		})

		this.checkForm = this.fb.group({
			strutturaResidenziale: [''],
			centroDiurnoStruttSemires: ['']
		})

		this.client.spinEmitter.emit(true);
		forkJoin({
			msgErrorPrest: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_PRESTAZIONI),
			msgErrorDoc: this.client.getMsgApplicativo(ERRORS.ERROR_UPDATE_DOC_ALLEGATI),
			msgErrorComuneAlreadyPresent: this.client.getMsgApplicativo(ERRORS.ERROR_COMUNE_ALREADY_PRESENT),
			msgErrorPrestAlreadyPresent: this.client.getMsgApplicativo(ERRORS.ERROR_PREST_ALREADY_PRESENT),
			msgErrorUploadAllegati: this.client.getMsgApplicativo(ERRORS.ERROR_UPLOAD_ALLEGATI),
			msgInformativi: this.client.getMsgInformativi(SECTION.DATI_ENTE),
			msgErrorDeletePrestVal: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_PREST_ASS),
			msgErrorDeletePrestValModC: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_PREST_ASS_MODC),
			msgErrorDeleteComuneVal: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_COMUNE_ASS),
			errPippi: this.client.getMsgApplicativo(ERRORS.ERROR_PIPPI_FNPS),
			msgErrorModelloAlreadyPresent: this.client.getMsgApplicativo(ERRORS.ERROR_MODELLO_ALREADY_PRESENT),
			prestazioniValModA: this.client.getListaPrestazioniValorizzateModA(this.rendicontazione.idRendicontazioneEnte),
			prestazioniValModB1: this.client.getListaPrestazioniValorizzateModB1(this.rendicontazione.idRendicontazioneEnte),
			prestazioniValModC: this.client.getListaPrestazioniValorizzateModC(this.rendicontazione.idRendicontazioneEnte),
			schedaEnte: this.client.getSchedaEnte(this.rendicontazione.idRendicontazioneEnte, this.rendicontazione.annoEsercizio),
			comAssociati: this.client.getComuniAssociati(this.rendicontazione.idSchedaEnte, this.rendicontazione.annoEsercizio),//sostituire id ente con data fine validita
			prestazioni: this.client.getPrestazioni(this.rendicontazione.annoEsercizio),
			presAssociate: this.client.getPrestazioniAssociate(this.rendicontazione.idRendicontazioneEnte),
			tipoEnte: this.client.getListaTipoEnte(),
			docAssociati: this.client.getDocAssociati(this.rendicontazione.idRendicontazioneEnte),
			prestRes: this.client.getPrestazioniResSemires(PREST_RES_SEMIRES.COD_TIPOLOGIA, PREST_RES_SEMIRES.TIPO_STRUTT_RES, this.rendicontazione.annoEsercizio),
			prestSemires: this.client.getPrestazioniResSemires(PREST_RES_SEMIRES.COD_TIPOLOGIA, PREST_RES_SEMIRES.TIPO_STRUTT_CD, this.rendicontazione.annoEsercizio),
			msgTab: this.client.getMsgApplicativo(MSG.WARN_TAB),
			modelli: this.client.getModelli(),
			modelliassociati: this.client.getModelliassociati(this.rendicontazione.idRendicontazioneEnte),
			obbligatorio: this.client.getAllObbligo(),
			msgErrorDeleteModelliVal: this.client.getMsgApplicativo(ERRORS.ERROR_DELETE_MODELLI_ASS),
			utenzeFnps: this.client.getUtenzeFnpsByAnno(this.rendicontazione.annoEsercizio),
			fondi: this.client.getFondiEnte(this.rendicontazione.idRendicontazioneEnte),
			fondoDaAssegnare: this.client.getFondiAnno(this.rendicontazione.annoEsercizio),
			messaggioPopupCalcolo: this.client.getMsgInformativi(SECTION.MSGCALCOLO),
		})
			.subscribe(({ msgErrorPrest, msgErrorDoc, msgErrorComuneAlreadyPresent, msgErrorPrestAlreadyPresent, msgErrorUploadAllegati,
				msgInformativi, msgErrorDeletePrestVal, msgErrorDeletePrestValModC, msgErrorDeleteComuneVal, prestazioniValModA, prestazioniValModB1, prestazioniValModC,
				schedaEnte,
				comAssociati, prestazioni, presAssociate,
				tipoEnte,
				docAssociati, prestRes, prestSemires, msgTab, errPippi, modelli, modelliassociati, obbligatorio, msgErrorDeleteModelliVal, utenzeFnps, fondi, fondoDaAssegnare,messaggioPopupCalcolo }) => {
				this.msgErrorPrest = msgErrorPrest.testoMsgApplicativo;
				this.msgErrorDoc = msgErrorDoc.testoMsgApplicativo;
				this.msgErrorComuneAlreadyPresent = msgErrorComuneAlreadyPresent.testoMsgApplicativo;
				this.msgErrorPrestAlreadyPresent = msgErrorPrestAlreadyPresent.testoMsgApplicativo;
				this.msgErrorUploadAllegati = msgErrorUploadAllegati.testoMsgApplicativo;
				this.messaggioPopupCalcolo = messaggioPopupCalcolo[0].testoMsgInformativo;
				this.popoverParamBody = msgInformativi[0].testoMsgInformativo;
				this.popoverDocBody = msgInformativi[1].testoMsgInformativo;
				this.popoverDocInizialiBody = msgInformativi[2].testoMsgInformativo;
				this.popoverDocFinaliBody = msgInformativi[3].testoMsgInformativo;

				this.msgErrorDeletePrestVal = msgErrorDeletePrestVal.testoMsgApplicativo;
				this.msgErrorDeletePrestValModC = msgErrorDeletePrestValModC.testoMsgApplicativo;
				this.msgErrorDeleteComuneVal = msgErrorDeleteComuneVal.testoMsgApplicativo;
				this.msgErrorDeleteModelliVal = msgErrorDeleteModelliVal.testoMsgApplicativo;
				this.msgTab = msgTab.testoMsgApplicativo;
				this.errPippi = errPippi.testoMsgApplicativo;
				this.listaPrestazioniValorizzateModA = prestazioniValModA;
				this.listaPrestazioniValorizzateModB1 = prestazioniValModB1;
				this.listaPrestazioniValorizzateModC = prestazioniValModC;
				this.utenzeFnps = utenzeFnps;
				for (let u of this.utenzeFnps) {
					u.valorePercentuale = this.transformPercentuale(parseFloat(u.valorePercentuale));
					this.totale += this.parsingFloat(u.valorePercentuale);
				}
				this.totaleString = this.transformPercentuale(this.totale);
				this.fondoDaAssegnare = fondoDaAssegnare;
				this.labelFondo = this.fondoDaAssegnare.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.FNPS)[0].descFondo;
				this.fondoFnps = this.fondoDaAssegnare.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.FNPS)[0];
				this.fondi = fondi;
				this.fnps = this.fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.FNPS)[0];
				if (this.fnps) {
					this.fnps.valore = this.transform(parseFloat(this.fnps.valore))
				} else {
					this.fnps = new Fondi();
					this.fnps.codFondo = this.fondoFnps.codFondo;
					this.fnps.descFondo = this.fondoFnps.descFondo;
					this.fnps.idFondo = this.fondoFnps.idFondo;
					this.fnps.codTipologiaFondo = this.fondoFnps.codTipologiaFondo;
				}
				this.quote = this.fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.ALTREQUOTE);
				for (let q of this.quote) {
					q.valore = this.transform(parseFloat(q.valore));
					q.esistente = true;
				}
				this.calcTableValue();
				this.azioniSistema = this.fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.AZIONISISTEMA);
				for (let q of this.azioniSistema) {
					q.valore = this.transform(parseFloat(q.valore));
					q.esistente = true;
				}
				this.altreQuoteDaAssegnare = this.fondoDaAssegnare.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.ALTREQUOTE);
				this.idSelectedAltreQuote = 0;

				this.azioniDaAssegnare = this.fondoDaAssegnare.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.AZIONISISTEMA);
				this.idSelectedAzioni = 0;

				this.denominazioneEnte = schedaEnte;

				this.initFormDenominazione(this.denominazioneEnte);
				this.listaTipoEnti = tipoEnte;
				this.listaProvince = [];
				this.listaProvince.push(new ProvinciaGreg(this.denominazioneEnte.comune.provincia.idProvincia, this.denominazioneEnte.comune.provincia.codIstatProvincia, this.denominazioneEnte.comune.provincia.desProvincia, null, null, null, null));
				this.listaComuniDenominazione = [];
				this.listaComuniDenominazione.push(new ComuneGreg(this.denominazioneEnte.comune.idComune, this.denominazioneEnte.comune.codIstatComune, this.denominazioneEnte.comune.desComune, null, null, null, null, null));
				this.listaAsl = [];
				this.listaAsl.push(new AslGreg(this.denominazioneEnte.asl.idAsl, this.denominazioneEnte.asl.codAsl, this.denominazioneEnte.asl.desAsl, null, null, null, null));

				this.initResponabileEnteForm(this.denominazioneEnte.responsabileEnte);
				this.listaComuniAssociati = comAssociati;
				this.modelli = modelli;
				this.modelliassociati = modelliassociati;
				this.obbligatorio = obbligatorio;
				this.initListaModelliToAss();
				this.initParametriForm(this.denominazioneEnte);

				this.listaPrestazioniRes = prestRes;
				this.listaPrestazioniSemires = prestSemires;
				this.listaPrestazioni = prestazioni;
				this.listaPrestazioniAssociate = presAssociate;

				this.listaPrestazioniInit = prestazioni;
				this.listaPrestazioniGeneral = [];
				this.listaPrestazioniGeneral = this.listaPrestazioniGeneral.concat(prestRes, prestSemires, prestazioni);
				this.initPrestazioniAssociate();
				this.listaDocumentiAllegati = docAssociati;
				this.checkRequiredDoc();

				this.client.spinEmitter.emit(false);
			},
				err => {
					this.client.spinEmitter.emit(false);
				});
	}


	initFormDenominazione(schedaEnte: EnteGreg) {
		this.enteForm = this.fb.group({
			idSchedaEntegestore: [schedaEnte.idSchedaEntegestore],
			codiceRegionale: [schedaEnte.codiceRegionale, [Validators.pattern('[0-9]*')]],
			codiceFiscale: [schedaEnte.codiceFiscale, [Validators.maxLength(11)]],
			denominazione: [schedaEnte.denominazione, [Validators.maxLength(300)]],
			partitaIva: [schedaEnte.partitaIva, [Validators.maxLength(12)]],
			tipoEnte: [schedaEnte.tipoEnte.codTipoEnte],
			comune: [schedaEnte.comune.idComune],
			codiceIstat: [schedaEnte.codiceIstat, [Validators.maxLength(9)]],
			provincia: [schedaEnte.comune.provincia.idProvincia],
			asl: [schedaEnte.asl.idAsl],
			email: [schedaEnte.email, [Validators.email]],
			telefono: [schedaEnte.telefono, [Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12)]],
			pec: [schedaEnte.pec, [Validators.email]],
			rendicontazione: [schedaEnte.rendicontazione]
		})
	}

	initResponabileEnteForm(resEnte: ResponsabileEnteGreg) {
		this.responsabileEnteForm = this.fb.group({
			idResponsabile: [resEnte.idResponsabile],
			nome: [resEnte.nome, [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40)]],
			cognome: [resEnte.cognome, [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40)]],
			cellulare: [resEnte.cellulare, [Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12)]],
			telefono: [resEnte.telefono, [Validators.pattern('[0-9]*[--/]*[0-9]*'), Validators.maxLength(12)]],
			codiceFiscale: [resEnte.codiceFiscale, [Validators.maxLength(16)]],
			eMail: [resEnte.eMail, [Validators.email]]
		})
	}

	initParametriForm(schedaEnte: EnteGreg) {
		let valore = schedaEnte.fnps;
		this.parametriForm = this.fb.group({
			fnps: [this.currencyFormat.transform(schedaEnte.fnps)],
			vincoloFondo: [schedaEnte.vincoloFondo, [Validators.pattern('[0-9]*'), Validators.maxLength(2)]],
			pippi: [this.currencyFormat.transform(schedaEnte.pippi)]
		})
		this.checkForm = this.fb.group({
			strutturaResidenziale: [schedaEnte.strutturaResidenziale],
			centroDiurnoStruttSemires: [schedaEnte.centroDiurnoStruttSemires]
		})
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

	initListaModelliToAss() {
		this.listaModelliToAss = this.modelli;
		if (this.modelliassociati.length != 0) {
			for (let modello of this.modelliassociati) {
				this.listaModelliToAss = this.listaModelliToAss.filter(mod => mod.codTab !== modello.codTab);
			}
			this.listaModelliToAss = this.listaModelliToAss;
		}
		else {
			this.listaModelliToAss = this.modelli;
		}
	}

	initPrestazioniAssociate() {
		this.listaPrestazioni = this.listaPrestazioniGeneral;
		if (this.listaPrestazioniAssociate.length != 0) {
			for (let prestazione of this.listaPrestazioniAssociate) {
				this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestazione.codicePrestazione);
			}
			this.listaPrestazioni = this.listaPrestazioni
		}
		this.initPrestRes();
		this.initPrestSemires();
	}

	refreshPrestRes() {
		this.prestazioneSelected = null;
		let trovato: boolean = false;
		if (!this.checkForm.controls.strutturaResidenziale.value) {
			for (let prestAss of this.listaPrestazioniAssociate) {
				for (let prestRes of this.listaPrestazioniRes) {
					if (prestRes.codicePrestazione == prestAss.codicePrestazione) {
						trovato = true;
					}
				}
			}
			if (trovato) {
				this.checkForm.patchValue({ strutturaResidenziale: !this.checkForm.controls.strutturaResidenziale.value });
				this.toastService.showError({ text: this.msgErrorPrest.replace('TIPOLOGIA', 'Strutture Residenziali') });
			}
			else {
				this.listaPrestazioni = [];
				this.listaPrestazioni = this.listaPrestazioniInit;
				this.initPrestazioniAssociate();
			}
		}
		else {
			this.listaPrestazioni = [];
			this.listaPrestazioni = this.listaPrestazioniInit;
			this.initPrestazioniAssociate();
		}
	}

	refreshPrestSemiRes() {
		this.prestazioneSelected = null;
		let trovato: boolean = false;
		if (!this.checkForm.controls.centroDiurnoStruttSemires.value) {
			for (let prestAss of this.listaPrestazioniAssociate) {
				for (let prestSemiRes of this.listaPrestazioniSemires) {
					if (prestSemiRes.codicePrestazione == prestAss.codicePrestazione) {
						trovato = true;
					}
				}
			}
			if (trovato) {
				this.checkForm.patchValue({ centroDiurnoStruttSemires: !this.checkForm.controls.centroDiurnoStruttSemires.value });
				this.toastService.showError({ text: this.msgErrorPrest.replace('TIPOLOGIA', 'Centri Diurni /Strutture Semi-residenziali') });
			}
			else {
				this.listaPrestazioni = [];
				this.listaPrestazioni = this.listaPrestazioniInit;
				this.initPrestazioniAssociate();
			}
		}
		else {
			this.listaPrestazioni = [];
			this.listaPrestazioni = this.listaPrestazioniInit;
			this.initPrestazioniAssociate();
		}
	}

	initPrestRes() {
		if (this.checkForm.controls.strutturaResidenziale.value) {
			for (let prestRes of this.listaPrestazioniRes) {
				let idxPrest = this.listaPrestazioni.findIndex(prest => prest.codicePrestazione == prestRes.codicePrestazione);
				// Elimino la madre
				if (idxPrest == -1) {
					this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestRes.codicePrestazione);
				}
			}
		}
		else {
			for (let prestRes of this.listaPrestazioniRes) {
				this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestRes.codicePrestazione);
			}
		}
		if (this.listaPrestazioniAssociate.length != 0) {
			for (let prestazione of this.listaPrestazioniAssociate) {
				this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestazione.codicePrestazione);
			}
		}
		this.listaPrestazioni.sort(function (a, b) {
			return a.codicePrestazione.toLocaleLowerCase().localeCompare(b.codicePrestazione.toLocaleLowerCase());
		})
	}

	initPrestSemires() {
		if (this.checkForm.controls.centroDiurnoStruttSemires.value) {
			//   this.listaPrestazioni = this.listaPrestazioni.concat(this.listaPrestazioniSemires);
			for (let prestRes of this.listaPrestazioniSemires) {
				let idxPrest = this.listaPrestazioni.findIndex(prest => prest.codicePrestazione == prestRes.codicePrestazione);
				// Elimino la madre
				if (idxPrest == -1) {
					this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestRes.codicePrestazione);
				}
			}
		}
		else {
			for (let prestSemires of this.listaPrestazioniSemires) {
				this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestSemires.codicePrestazione);
			}
		}
		if (this.listaPrestazioniAssociate.length != 0) {
			for (let prestazione of this.listaPrestazioniAssociate) {
				this.listaPrestazioni = this.listaPrestazioni.filter(prest => prest.codicePrestazione !== prestazione.codicePrestazione);
			}
		}
		this.listaPrestazioni.sort(function (a, b) {
			return a.codicePrestazione.toLocaleLowerCase().localeCompare(b.codicePrestazione.toLocaleLowerCase());
		})
	}

	checkRequiredDoc() {
		if (this.denominazioneEnte.rendicontazione) {
			if (this.client.azioni.get("DatiEnteAllegati")[0]) {
				this.visibleDocI = false;
				this.visibleDocII = false;
				this.visibleTrashDocI = false;
				this.visibleTrashDocII = false;
			}
			else {
				if ((this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.DA_COMPILARE_I ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_I ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.ATTESA_RETTIFICA_I)
					&& this.client.azioni.get("DatiEnteAllegati")[1]) {
					this.visibleDocI = !this.client.azioni.get("DatiEnteAllegati")[0];
					this.visibleDocII = !this.client.azioni.get("DatiEnteAllegati")[0];
					this.avoidAllegatiChangeI = false;
					this.avoidAllegatiChangeII = false;
					this.visibleTrashDocI = !this.client.azioni.get("DatiEnteAllegati")[0];
					this.visibleTrashDocII = !this.client.azioni.get("DatiEnteAllegati")[0];
				}
				if ((this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.IN_RIESAME_I ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.IN_RIESAME_II ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.STORICIZZATA ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.VALIDATA ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.ATTESA_VALIDAZIONE)
					&& this.client.azioni.get("DatiEnteAllegati")[1]) {
					this.visibleDocI = false;
					this.visibleDocII = false;
					this.avoidAllegatiChangeI = true;
					this.avoidAllegatiChangeII = true;
					this.visibleTrashDocI = false;
					this.visibleTrashDocII = false;
				}
				if ((this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.DA_COMPILARE_II ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_II ||
					this.denominazioneEnte.rendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.ATTESA_RETTIFICA_II)
					&& this.client.azioni.get("DatiEnteAllegati")[1]) {
					this.visibleDocI = false;
					this.visibleDocII = !this.client.azioni.get("DatiEnteAllegati")[0];
					this.avoidAllegatiChangeI = true;
					this.avoidAllegatiChangeII = false;
					this.visibleTrashDocI = false;
					this.visibleTrashDocII = !this.client.azioni.get("DatiEnteAllegati")[0];
				}
			}
		}
	}

	addPrestazioneAssociata() {
		let prestazione = this.listaPrestazioni.find(prest => prest.idPrestazione == this.prestazioneSelected);
		if (prestazione) {
			let alreadyPresent = this.listaPrestazioniAssociate.find(x => x.idPrestazione === prestazione.idPrestazione);
			if (prestazione && !alreadyPresent) {
				// Aggiungo la prestazione madre
				let prestMadre = new PrestazioneAssociataGreg(null, prestazione.codicePrestazione, prestazione.desPrestazione, prestazione.idPrestazione, false);
				this.listaPrestazioniAssociate.push(prestMadre);
				this.listaPrestazioniAssociate.sort(function (a, b) {
					return a.codicePrestazione.toLocaleLowerCase().localeCompare(b.codicePrestazione.toLocaleLowerCase());
				})
				this.prestazioniTable.renderRows();
			} else if (alreadyPresent) {
				this.toastService.showError({ text: this.msgErrorPrestAlreadyPresent });
			}
		}
		this.initPrestazioniAssociate();
		this.prestazioneSelected = null;
	}

	deletePrestazioneAssociata(codPrest: string) {
		// TODO prima di eliminare controllare che nei modelli A e B1 non ci siano valori popolati per quelle prestazioni
		let prestValModA = this.listaPrestazioniValorizzateModA.find(prest => prest == codPrest);
		let prestValModB1 = this.listaPrestazioniValorizzateModB1.find(prest => prest == codPrest);
		let prestValModC = this.listaPrestazioniValorizzateModC.find(prest => prest == codPrest);
		if (prestValModA) {
			let msgErrorA = this.msgErrorDeletePrestVal.replace("OGGETTORIMOZIONE", "della Prestazione")
				.replace("TIPOVOCE", "Entrata")
				.replace("MOD", "A");
			this.errorMessage.error.descrizione = msgErrorA;
			this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: msgErrorA }));
		}
		else if (prestValModB1) {
			let msgErrorB1 = this.msgErrorDeletePrestVal.replace("OGGETTORIMOZIONE", "della Prestazione")
				.replace("TIPOVOCE", "Spesa")
				.replace("MOD", "B1");
			this.errorMessage.error.descrizione = msgErrorB1;
			this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: msgErrorB1 }));
		}
		else if (prestValModC) {
			let msgErrorC = this.msgErrorDeletePrestValModC.replace("OGGETTORIMOZIONE", "della Prestazione")
				.replace("MOD", "C");
			this.errorMessage.error.descrizione = msgErrorC;
			this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: msgErrorC }));
		}
		else {
			let prestMadre = this.listaPrestazioniGeneral.find(prest => prest.codicePrestazione == codPrest);
			if (prestMadre && !prestMadre.prestFiglia) {
				let idxPrestMadre = this.listaPrestazioniAssociate.findIndex(prest => prest.codicePrestazione == codPrest);
				// Elimino la madre
				if (idxPrestMadre !== -1) {
					this.listaPrestazioniAssociate.splice(idxPrestMadre, 1);
					this.prestazioniTable.renderRows();
				}
			}
			this.prestazioniTable.renderRows();
			this.initPrestazioniAssociate();
		}
	}

	dettaglioPrestazione(codPrest: string, path: string) {
		this.client.dettaglioPrestazione = true;
		const navigationExtras: NavigationExtras = {
			relativeTo: this.route,
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				codPrestazione: codPrest
			}
		};
		this.router.navigate([path], navigationExtras);
	}

	scaricaDocumento(doc: AllegatoGreg) {
		this.client.spinEmitter.emit(true);
		this.client.getAllegatoToDownload(doc.pkAllegatoAssociato).subscribe(
			(docToDownload: AllegatoGreg) => {
				const linkSource = 'data:application/pdf/zip;base64,' + ' ' + docToDownload.file;
				const downloadLink = document.createElement("a");
				const fileName = docToDownload.nomeFile;
				downloadLink.href = linkSource;
				downloadLink.download = fileName;
				downloadLink.click();
				downloadLink.remove();
				this.client.spinEmitter.emit(false);
			},
			err => {
				this.client.spinEmitter.emit(false);
			}
		);
	}

	eliminaDocumento(index: number) {
		this.listaDocumentiAllegati.splice(index, 1);
		index === 0 ? this.docInizialeName = '' : this.docFinaleName = '';
		this.documentiTable.renderRows();
		this.verificaPresenzaAllegatoIniziale();
		this.verificaPresenzaAllegatoFinale();
	}

	uploadFileIniziale(event) {
		this.errDocInizialeAllegato = undefined;
		if (this.controlUploadFile(event)) {
			this.okDocIn = true;
			this.fileIniziale = event.target.files[0];
			this.docInizialeName = this.fileIniziale.name;
			this.buttonAllegatoIniziale = false;
			const reader = new FileReader();
			reader.readAsDataURL(this.fileIniziale);
			reader.onload = () => {
				this.formattedFileIniziale = formatBase64(JSON.stringify(reader.result));
			};
		} else {
			this.errDocInizialeAllegato = this.msgErrorUploadAllegati;
			this.docInizialeName = '';
		}
	}

	uploadFileFinale(event) {
		this.errDocFinaleAllegato = undefined;
		if (this.controlUploadFile(event)) {
			this.okDocFi = true;
			this.fileFinale = event.target.files[0];
			this.docFinaleName = this.fileFinale.name;
			this.buttonAllegatoFinale = false;
			const reader = new FileReader();
			reader.readAsDataURL(this.fileFinale);
			reader.onload = () => {
				this.formattedFileFinale = formatBase64(JSON.stringify(reader.result));
			};
		} else {
			this.errDocFinaleAllegato = this.msgErrorUploadAllegati;
			this.docFinaleName = '';
		}
	}

	allegaFileIniziale() {
		this.fileInizialeToUpload = new FileAllegatoToUploadGreg(this.formattedFileIniziale, this.fileIniziale.size, this.fileIniziale.name, this.fileIniziale.type, '', '');
		this.listaDocumentiAllegati.push(new AllegatoGreg(null, null, this.fileIniziale.size, this.docInizialeName, null, DOC.DOC_INIZIALE, null, true))
		this.documentiTable.renderRows();
		this.fileIniziale = null;
		this.docInizialeName = '';
		this.buttonAllegatoIniziale = true;
		this.verificaPresenzaAllegatoIniziale();
	}

	allegaFileFinale() {
		this.fileFinaleToUpload = new FileAllegatoToUploadGreg(this.formattedFileFinale, this.fileFinale.size, this.fileFinale.name, this.fileFinale.type, '', '');
		this.listaDocumentiAllegati.push(new AllegatoGreg(null, null, this.fileFinale.size, this.docFinaleName, null, DOC.DOC_FINALE, null, true))
		this.documentiTable.renderRows();
		this.fileFinale = null;
		this.docFinaleName = '';
		this.buttonAllegatoFinale = true;
		this.verificaPresenzaAllegatoFinale();
	}

	controlUploadFile(event) {
		let maxFileSize = 31 * 1024 * 1024;
		if (event.target.files[0].size <= maxFileSize) {
			if (event.target.files[0].type == "application/x-compressed" || event.target.files[0].type == "application/x-zip-compressed" ||
				event.target.files[0].type == "application/zip" || event.target.files[0].type == "multipart/x-zip" || event.target.files[0].type == "application/pdf") {
				return true;
			}
		}
		return false;
	}

	verificaPresenzaAllegatoIniziale() {
		if (this.listaDocumentiAllegati.length == 0) {
			this.errDocIniziale = '';
			return false;
		}
		else {
			for (let docAll of this.listaDocumentiAllegati) {
				if (docAll.tipoDocumentazione === DOC.DOC_INIZIALE) {
					this.errDocIniziale = this.msgErrorDoc.replace('TIPODOCUMENTO', DOC.DOC_INIZIALE);
					return true;
				}
			}
			this.errDocIniziale = '';
			return false;
		}
	}

	verificaPresenzaAllegatoFinale() {
		if (this.listaDocumentiAllegati.length == 0) {
			this.errDocFinale = '';
			return false;
		}
		else {
			for (let docAll of this.listaDocumentiAllegati) {
				if (docAll.tipoDocumentazione === DOC.DOC_FINALE) {
					this.errDocFinale = this.msgErrorDoc.replace('TIPODOCUMENTO', DOC.DOC_FINALE);
					return true;
				}
			}
			this.errDocFinale = '';
			return false;
		}
	}

	changeKeyFNPS(valore: any) {
		if (valore == "" || valore == null) {
			this.fnps.valore = null;
		}
		else {
			if (valore.indexOf('.') !== -1) {
				if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
					valore = [valore, '0'].join('');
				this.fnps.valore = valore;
			}
			else this.fnps.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
		}
	}

	changeKeyDaAssegnare(valore: any) {
		if (valore == "" || valore == null) {
			this.fondoSelezionato.valore = null;
		}
		else {
			if (valore.indexOf('.') !== -1) {
				if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
					valore = [valore, '0'].join('');
				this.fondoSelezionato.valore = valore;
			}
			else this.fondoSelezionato.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
		}
	}

	changeKeyDaAssegnareAzione(valore: any) {
		if (valore == "" || valore == null) {
			this.azioneSelezionato.valore = null;
		}
		else {
			if (valore.indexOf('.') !== -1) {
				if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
					valore = [valore, '0'].join('');
				this.azioneSelezionato.valore = valore;
			}
			else this.azioneSelezionato.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
		}
	}



	changeKeyQuota(valore: any, i: number) {
		if (valore == "" || valore == null) {
			this.quote[i].valore = null;
		}
		else {
			if (valore.indexOf('.') !== -1) {
				if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
					valore = [valore, '0'].join('');
				this.quote[i].valore = valore;
			}
			else this.quote[i].valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
		}
	}

	changeKeyAzione(valore: any, i: number) {
		if (valore == "" || valore == null) {
			this.azioniSistema[i].valore = null;
		}
		else {
			if (valore.indexOf('.') !== -1) {
				if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
					valore = [valore, '0'].join('');
				this.azioniSistema[i].valore = valore;
			}
			else this.azioniSistema[i].valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
		}
	}


	changeKeyPippi(valore: any) {
		if (valore == "" || valore == null) {
			this.parametriForm.patchValue({ pippi: null });
		}
		else {
			if (valore.indexOf('.') !== -1) {
				if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
					valore = [valore, '0'].join('');
				this.parametriForm.patchValue({ pippi: valore });
			}
			else this.parametriForm.patchValue({ pippi: this.transform(parseFloat(valore.toString().replace(',', '.'))) });
		}
	}

	isNumber(value: string | number): boolean {
		return ((value != null) &&
			(value !== '') &&
			!isNaN(Number(value.toString().replace(',', '.'))));
	}

	transform(value: number,
		currencySign: string = '',
		decimalLength: number = 2,
		chunkDelimiter: string = '.',
		decimalDelimiter: string = ',',
		chunkLength: number = 3): string {
		let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
		if (this.isNumber(value)) {
			if (value != null && value != undefined) {
				let num = value.toFixed(Math.max(0, ~~decimalLength));
				return currencySign + (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
			}
		}
		return undefined;
	}

	salvaModifiche() {
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
		}
		else {
			//this.setNewTipoEnte();
			//this.setNewComune();
			//this.setNewAsl();
			this.datiEnteToSave = new DatiEnteToSave();
			this.datiEnteToSave.rendicontazioneEnte = new RendicontazioneEnteGreg(this.rendicontazione.idRendicontazioneEnte,
				null, null, null, null, null, null, null, null);
			//  delete this.enteForm.value.provincia;
			this.datiEnteToSave.rendicontazioneEnte.idSchedaEnte = this.denominazioneEnte.idSchedaEntegestore;
			//   this.datiEnteToSave.datiEnte = this.enteForm.value;
			this.datiEnteToSave.rendicontazioneEnte.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
			let fnps = this.parametriForm.controls.fnps.value;
			this.datiEnteToSave.rendicontazioneEnte.fnps = fnps ? parseFloat(fnps.toString().replaceAll('.', '').replace(',', '.')) : null;
			this.datiEnteToSave.rendicontazioneEnte.vincoloFondo = this.parametriForm.controls.vincoloFondo.value;
			let pippi = this.parametriForm.controls.pippi.value;
			this.datiEnteToSave.rendicontazioneEnte.pippi = pippi ? parseFloat(pippi.toString().replaceAll('.', '').replace(',', '.')) : null;
			if (this.datiEnteToSave.rendicontazioneEnte.pippi > this.datiEnteToSave.rendicontazioneEnte.fnps) {
				this.errorMessage.error.descrizione = this.errPippi;
				this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
				return
			}
			this.datiEnteToSave.rendicontazioneEnte.centroDiurnoStruttSemires = this.checkForm.controls.centroDiurnoStruttSemires.value;
			this.datiEnteToSave.rendicontazioneEnte.strutturaResidenziale = this.checkForm.controls.strutturaResidenziale.value;
			this.datiEnteToSave.profilo = this.client.profilo;

			this.datiEnteToSave.modelli = this.modelliassociati;
			this.datiEnteToSave.prestazioniAssociate = this.listaPrestazioniAssociate;
			this.datiEnteToSave.cronologia = this.cronologiaMod.cronologia;
			this.datiEnteToSave.allegatiAssociati = this.listaDocumentiAllegati;

			this.datiEnteToSave.fileIniziale = this.fileInizialeToUpload;
			this.datiEnteToSave.fileFinale = this.fileFinaleToUpload;

			this.client.spinEmitter.emit(true);

			this.client.saveDatiEnte(this.datiEnteToSave)
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
						}
						this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(data => {
							this.rendicontazione.statoRendicontazione.codStatoRendicontazione = data.statoRendicontazione.codStatoRendicontazione
						});
						this.ngOnInit();

						this.cronologiaMod.cronologia.notaEnte = null;
						this.cronologiaMod.cronologia.notaInterna = null;

						this.espansa = false;
						this.apricronologia();
						this.toastService.showSuccess({ text: data.descrizione });
						this.onReloadPage();
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
			this.enteForm.value.provincia = '';
			this.prestazioneSelected = null;
		}
	}





	onReloadPage() {
		// this.router.routeReuseStrategy.shouldReuseRoute = () => false;
		// this.router.onSameUrlNavigation = 'reload';
		let currentUrl = this.router.url;
		const navigationExtras: NavigationExtras = {
			relativeTo: this.route,
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				datiEnte: this.datiEnte
			}
		};
		this.router.navigateByUrl('/', navigationExtras).then(() => {
			this.router.navigate([currentUrl], navigationExtras);
		});
	}

	apricronologia() {
		if (this.espansa)
			this.espansa = false;
		else
			this.espansa = true;
		this.state = (this.state === 'default' ? 'rotated' : 'default');
	}

	routeTo(path: string) {
		const navigationExtras: NavigationExtras = {
			relativeTo: this.route,
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				datiEnte: this.datiEnte
			}
		};
		this.router.navigate([path], navigationExtras);
	}
	selectModello() {
		this.obbligatorioSelect = false;
	}

	addModelloAssociato() {
		let modello = this.modelli.find(modello => modello.idTab == this.modelloSelected);
		let obbligatorio;
		if (this.obbligatorioSelect) {
			obbligatorio = this.obbligatorio.find(obbligo => obbligo.codObbligo == 'OB');
		}
		else {
			obbligatorio = this.obbligatorio.find(obbligo => obbligo.codObbligo == 'FA');
		}
		if (modello) {
			let alreadyPresent = this.modelliassociati.find(x => x.idTab === modello.idTab);
			if (modello && !alreadyPresent) {
				let newmodello = new ModelTabTranche();
				newmodello.idTab = modello.idTab;
				newmodello.codTab = modello.codTab;
				newmodello.desTab = modello.desTab;
				newmodello.desEstesaTab = modello.desEstesaTab;
				newmodello.idObbligo = obbligatorio.idObbligo;
				newmodello.codObbligo = obbligatorio.codObbligo;
				newmodello.desObbligo = obbligatorio.desObbligo;
				this.modelliassociati.push(newmodello);
				this.modelliassociati.sort(function (a, b) {
					return a.desEstesaTab.toLocaleLowerCase().localeCompare(b.desEstesaTab.toLocaleLowerCase());
				})
				this.modelliTable.renderRows();
				this.modelloSelected = undefined;
				this.obbligatorioSelect = false;
			} else if (alreadyPresent) {
				this.toastService.showError({ text: this.msgErrorModelloAlreadyPresent });
			}
		}
		this.initListaModelliToAss();
		this.modelloSelected = undefined;
		this.obbligatorioSelect = false;
	}

	deleteModelloAssociato(idTab: number, codTab: string, desTab: string) {

		this.client.getVerificaModelliVuoto(codTab, this.rendicontazione.idRendicontazioneEnte).subscribe(
			condati => {
				condati = condati;
				// TODO prima di eliminare controllare che nei modelli non ci siano dati
				if (condati.descrizione == 'pieno') {
					if (codTab = "MA")
						codTab = desTab;
					let msgError = this.msgErrorDeleteModelliVal.replace("OGGETTORIMOZIONE", codTab);
					this.errorMessage.error.descrizione = msgError;
					this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: msgError }));
				}
				else {
					let indexModello = this.modelliassociati.findIndex(modello => modello.idTab == idTab);
					if (indexModello !== -1) {
						this.modelliassociati.splice(indexModello, 1);
						this.modelliTable.renderRows();
						this.initListaModelliToAss();
					}
				}
			}
		);

	}
	aggiuntitutteprestazioni(event) {
		if (event.target.checked && this.listaPrestazioni.length != 0) {
			for (let prestazione of this.listaPrestazioni) {
				let idxPrest = this.listaPrestazioniAssociate.findIndex(prest => prest.codicePrestazione == prestazione.codicePrestazione);
				if (idxPrest == -1) {
					// Aggiungo la prestazione madre
					let prestMadre = new PrestazioneAssociataGreg(null, prestazione.codicePrestazione, prestazione.desPrestazione, prestazione.idPrestazione, false);
					this.listaPrestazioniAssociate.push(prestMadre);
					this.listaPrestazioniAssociate.sort(function (a, b) {
						return a.codicePrestazione.toLocaleLowerCase().localeCompare(b.codicePrestazione.toLocaleLowerCase());
					})
					this.prestazioniTable.renderRows();
				}
			}
			event.target.checked = false;
			//	this.aggiungiSelected = null;
			//	this.isChecked = false;
			this.initPrestazioniAssociate();
			this.prestazioneSelected = null;
		}
	}

	routeToModello() {
		this.client.spinEmitter.emit(true);
		const navigationExtras: NavigationExtras = {
			relativeTo: this.route,
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				datiEnte: this.datiEnte,

			}
		}
		this.router.navigate(['../all-d'], navigationExtras)
	}

	calcTableValue() {
		this.totaleFondi = 0;
		this.totaleFondi += this.fnps.valore ? this.parsingFloat(this.fnps.valore) : 0;
		for (let q of this.quote) {
			if (q.funzioneRegola == '+') {
				this.totaleFondi += this.parsingFloat(q.valore)
			} else {
				this.totaleFondi -= this.parsingFloat(q.valore)
			}

		}
		this.totaleFondiDaStampare = this.transform(this.totaleFondi);
	}

	transformPercentuale(value: number,
		currencySign: string = '',
		decimalLength: number = 2,
		chunkDelimiter: string = '.',
		decimalDelimiter: string = ',',
		chunkLength: number = 3): string {
		let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
		if (this.isNumber(value)) {
			if (value != null && value != undefined) {
				let num = value.toFixed(Math.max(0, ~~decimalLength));
				return currencySign + (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
			}
		}
		return undefined;
	}

	sortFondo(a: Fondi, b: Fondi) {
		if (a.codFondo < b.codFondo) {
			return -1;
		} else {
			return 1;
		}
	}

	parsingFloat(el) {
		if (el == '' || el == '-') el = null;
		el = el ? parseFloat(el.toString().replaceAll('.', '').replace(',', '.')) : el;
		return el;
	}
}
