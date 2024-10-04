import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatTable } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { FondiEnteAllontanamentoZeroDTO } from '@greg-app/app/dto/FondiEnteAllontanamentoZeroDTO';
import { MsgInformativo } from '@greg-app/app/dto/MsgInformativo';
import { DatiAllontanamentoZero, PrestazioniAllontanamentoZeroDTO } from '@greg-app/app/dto/PrestazioniAllontanamentoZeroDTO';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { DATIENTE_INFO, DOC, ERRORS, OPERAZIONE, RENDICONTAZIONE_STATUS, SECTION } from '@greg-app/constants/greg-constants';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';
import { MotivazionePopupComponent } from '../motivazione-popup/motivazione-popup.component';
import { formatBase64 } from '@greg-app/shared/util';
import { AllegatoGreg } from '@greg-app/app/dto/AllegatoGreg';
import { FileAllegatoToUploadGreg } from '@greg-app/app/dto/FileAllegatoToUploadGreg';
import { ValidazioneAlZero } from '@greg-app/app/dto/ValidazioneAlZeroDTO';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ProfiloGreg } from '@greg-app/app/dto/ProfiloGreg';
import { PrestazioniAllontanamentoZeroCSV_DTO, PrestazioniAlZeroCSV_DTO } from '@greg-app/app/dto/PrestazioniAlZeroCSV_DTO';
import { ConfermaValidazionePopupComponent } from './conferma-validazione-popup/conferma-validazione-popup';

@Component({
    selector: 'app-modelloAlZero',
    templateUrl: './modelloAlZero.component.html',
    styleUrls: ['./modelloAlZero.component.css']
})
export class ModelloAlZero implements OnInit {

    // CHECKS
    isValidModello: boolean = false;
    checkB1: boolean = false;
    // ANGULAR ESSENTIALS
    navigation: Navigation;
    titolo: string = '';

    // STYLE TABLE
    primoHeader = `Interventi a sostegno della genitorialità e norme per la prevenzione degli allontanamenti dal nucleo familiare d'origine, di cui alla l.r. 17/2022.`;
    secondoHeader = 'Spesa di-cui Al-zero su Fam e Minori';

    primaRiga = 'Massimale corrispondente alla spesa da B1';
    secondaRiga = 'Spesa rendicontata Mod. Al. Zero';

    // INFO GENERICHE
    infoOperatore: RendicontazioneEnteGreg;
    statoInitial: string = '';
    annoGestione: string = '';
    idRendicontazioneEnte;
    @ViewChild(CronologiaModelliComponent, { static: false }) cronologiaMod: CronologiaModelliComponent;
    errorMessage = {
        error: { descrizione: '' },
        message: '',
        name: '',
        status: '',
        statusText: '',
        url: '',
        date: Date
    };
    profilo: ProfiloGreg;
    // RENDICONTAZIONE
    rendicontazione: RendicontazioneEnteGreg;

    // ENTE
    datiEnte: DatiEnteGreg;
    fondiEnte: FondiEnteAllontanamentoZeroDTO = {
        fondoRegionale: null,
        quotaAllontanamentoZero: null,
        labelFondiRegionale: null,
        labelQuotaAllontanamentoZero: null
    };
    // MSG INFO
    infoAlZero01: string = '';
    infoAlZero02: string = '';
    infoAlZero02_parte1: string = '';
    infoAlZero02_parte2: string = '';
    infoAlZero03: string = '';
    infoAlZero04: string = '';
    infoAlZero05: string = '';
    infoAlZero06: string = '';
    infoAlZero07: string = '';
    label: MsgInformativo[] = [];
    msgGiustificazione: string = '';
    superamentoMassimali: boolean = false;
    erroreStato: string;
    erroreModifica: string;

    // PRESTAZIONI
    datiAlZero: DatiAllontanamentoZero;
    datiAlZeroOld: DatiAllontanamentoZero;
    datiAlZeroConcorrenza: DatiAllontanamentoZero;
    infoOperatoreBeforeSave: RendicontazioneEnteGreg;
    // listaPrestazioni: PrestazioniAllontanamentoZeroDTO[] = [];
    totaleValoriB1: number = 0;
    totaleValoriAlZero: number = 0;
    residuo: number = 0;

    // VALIDAZIONE
    validazioneAlZero: boolean = false;
    // FILES
    isFileCaricato: boolean = false;
    @ViewChild('documentiTable', { static: false }) documentiTable: MatTable<any>;
    opoverDocTitle: string = DATIENTE_INFO.INFO_DOC_TITLE;
    popoverDocBody: string = '';
    popoverPrestTitle: string = DATIENTE_INFO.INFO_PREST_TITLE;
    popoverPrestBody: string = '';
    fileAlZero: File = null;
    docAlZeroName: string = '';
    formattedFileAlZero: string = '';
    listaDocumentiAllegati: AllegatoGreg[] = [];
    columnsListaDocAllegati: Array<string> = ['documento', 'tipologia', 'azioni'];
    fileAlZeroToUpload: FileAllegatoToUploadGreg;
    isPresenteDocAlZero: boolean = false;

    // AZIONI
    disabilitaInput: boolean = false;

    constructor(public client: GregBOClient, private router: Router, private route: ActivatedRoute, private dialog: MatDialog,
        public toastService: AppToastService, private gregError: GregErrorService, private changeDetector: ChangeDetectorRef, public pulsanti: PulsantiFunzioniComponent) {
        this.navigation = this.router.getCurrentNavigation();
        let enteValues: string[] = [];
        this.route.fragment.subscribe((frag: string) => {
            enteValues.push(frag);
        });
        this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
        this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : enteValues[0][1];
        this.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
    }

    ngOnInit() {
        this.client.spinEmitter.emit(true);
        this.client.mostrabottoniera = false;
        this.superamentoMassimali = false;
        this.disableButtons();  // DISABLE NOT NECESSARY BUTTONS
        forkJoin({
            checkB1: this.client.checkB1EntryForAlZero(this.idRendicontazioneEnte),
            fondiEnte: this.client.getFondiEnteModelloAlZero(this.idRendicontazioneEnte)
        }).subscribe(({ checkB1, fondiEnte }) => {
            this.fondiEnte = fondiEnte;
            this.checkB1 = checkB1;
            // Controllo azioni visualizzae modifica del modello
            if (this.client.azioni.get('ModelloAlZero')[0]) { // azione visualizza
                this.disabilitaInput = true;
            } else {    // azione modifica
                this.disabilitaInput = false;
            }
            if (!this.checkB1) {
                this.disabilitaInput = true;
            }
            if (!this.checkStatoRendicontazione()) {
                this.disabilitaInput = true;
            }
            /**
             * Check stato tendicontazione
             * Check almeno una entry nel fondo B1 valorizzato
             * Check fondo regionale not null and != 0
             */
            if (![null, 0].includes(this.fondiEnte.fondoRegionale)) {
                this.isValidModello = true;
                forkJoin({
                    infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
                    infoAlZero01: this.client.getMsgInformativi(SECTION.ALZERO01),
                    infoAlZero02: this.client.getMsgInformativi(SECTION.ALZERO02),
                    infoAlZero03: this.client.getMsgInformativi(SECTION.ALZERO03),
                    infoAlZero04: this.client.getMsgInformativi(SECTION.ALZERO04),
                    label: this.client.getMsgInformativi(SECTION.LABELALZERO),
                    msgGiustificazione: this.client.getMsgInformativi(SECTION.ALZEROGIUSTIFICAZIONE),
                    datiAlZero: this.client.getPrestazioniModelloAlZero(this.rendicontazione.idRendicontazioneEnte),
                    validazione: this.client.getValidazioneModelloAlZero(this.rendicontazione.idRendicontazioneEnte),
                    erroreModifica: this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
                }).subscribe(({ infoOperatore, datiAlZero, validazione, label, erroreModifica, msgGiustificazione, infoAlZero01, infoAlZero02, infoAlZero03, infoAlZero04 }) => {
                    // GENERIC
                    this.erroreModifica = erroreModifica.testoMsgApplicativo;
                    this.titolo = 'Modulo Allontanamento Zero';
                    this.infoOperatore = infoOperatore;
                    this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
                    this.annoGestione = infoOperatore.annoEsercizio.toString();
                    // VALIDAZIONE
                    this.validazioneAlZero = validazione;
                    // MSG INFO
                    this.infoAlZero01 = infoAlZero01[0].testoMsgInformativo;
                    this.infoAlZero02 = infoAlZero02[0].testoMsgInformativo;
                    let parts: string[] = this.infoAlZero02.split('<br>');
                    this.infoAlZero02_parte1 = parts[0];
                    this.infoAlZero02_parte2 = parts[1];
                    this.infoAlZero03 = infoAlZero03[0].testoMsgInformativo;
                    this.infoAlZero04 = infoAlZero04[0].testoMsgInformativo;
                    this.label = label;
                    this.msgGiustificazione = msgGiustificazione[0].testoMsgInformativo;
                    // LISTA PRESTAZIONI
                    this.datiAlZero = datiAlZero;
                    this.datiAlZeroOld = {
                        fileAlZero: this.datiAlZero.fileAlZero,
                        fileAlZerodb: this.datiAlZero.fileAlZerodb,
                        giustificativo: this.datiAlZero.giustificativo,
                        lista: this.datiAlZero.lista.map(item => ({ ...item })),
                        profilo: this.datiAlZero.profilo,
                        notaEnte: '',
                        notaInterna: '',
                        confermaResponsabile: false
                    };
                    this.profilo = this.datiAlZero.profilo;
                    // Check if file is yet upload
                    if (this.datiAlZero.fileAlZerodb) {
                        this.isPresenteDocAlZero = true;
                        this.isFileCaricato = true;
                        this.docAlZeroName = this.datiAlZero.fileAlZerodb.nomeFile;
                        this.listaDocumentiAllegati = [];
                        this.listaDocumentiAllegati.push(this.datiAlZero.fileAlZerodb);
                        this.documentiTable.renderRows();
                    }
                    // this.listaPrestazioni = datiAlZero.lista;
                    this.totaleValoriB1 = 0;
                    this.totaleValoriAlZero = 0;
                    this.datiAlZero.lista.forEach((e) => {
                        e.valorePerPrestazioneAlZeroString = this.formatoItaliano(e.valorePerPrestazioneAlZero);
                        if (e.valorePerFamiglieMinoriB1) {  // Calcolo somma valori B1
                            this.totaleValoriB1 += e.valorePerFamiglieMinoriB1;
                        }
                        if (e.valorePerPrestazioneAlZero) { // Calcolo somma valori Allontanamento Zero
                            this.totaleValoriAlZero += e.valorePerPrestazioneAlZero;
                        }
                    }); // Utile per classi e valore di input
                    this.totaleValoriAlZero = parseFloat(this.totaleValoriAlZero.toFixed(2));
                    if (this.fondiEnte.quotaAllontanamentoZero && this.totaleValoriAlZero) { // Calcolo del residuo
                        this.residuo = this.fondiEnte.quotaAllontanamentoZero - this.totaleValoriAlZero;
                    } else if (this.fondiEnte.quotaAllontanamentoZero && !this.totaleValoriAlZero) {
                        this.residuo = this.fondiEnte.quotaAllontanamentoZero;
                    }

                    // ABILITAZIONE BOTTONI
                    if (this.validazioneAlZero && this.client.azioni.get('InvalidaAlZero')[1]) {
                        this.client.AnnullaValidazioneAlZero = true;
                    } else {
                        this.client.AnnullaValidazioneAlZero = false;
                    }
                    if (!this.validazioneAlZero && this.client.azioni.get('ValidaAlZero')[1]) {
                        this.client.ValidaAlZero = true;
                    } else {
                        this.client.ValidaAlZero = false;
                    }
                    if (this.validazioneAlZero) {
                        this.client.SalvaModelliIV = false;
                    } else {
                        this.client.SalvaModelliIV = true;
                    }
                    this.client.mostrabottoniera = true;

                    this.client.spinEmitter.emit(false);
                }, err => {
                    this.client.mostrabottoniera = false;
                    this.client.spinEmitter.emit(false);
                });
            } else {
                this.isValidModello = false;
                forkJoin({
                    ALZERO05: this.client.getMsgInformativi(SECTION.ALZERO05),
                    ALZERO06: this.client.getMsgInformativi(SECTION.ALZERO06),
                    ALZERO07: this.client.getMsgInformativi(SECTION.ALZERO07),
                }).subscribe(({ ALZERO05, ALZERO06, ALZERO07 }) => {
                    // this.infoAlZero05 = ALZERO05[0].testoMsgInformativo;
                    // this.infoAlZero06 = ALZERO06[0].testoMsgInformativo;
                    this.infoAlZero07 = ALZERO07[0].testoMsgInformativo;
                    this.client.mostrabottoniera = false;
                    // if (!this.checkB1) {
                    //     this.toastService.showError({ text: this.infoAlZero05 });
                    // }
                    // if (!this.checkStatoRendicontazione()) {
                    //     this.toastService.showError({ text: this.infoAlZero06 });
                    // }
                    if ([null, 0].includes(this.fondiEnte.fondoRegionale)) {
                        this.toastService.showError({ text: this.infoAlZero07 });
                    }
                    this.client.spinEmitter.emit(false);
                });
            }
        }, err => {
            this.isValidModello = false;
            this.client.mostrabottoniera = false;
            this.client.spinEmitter.emit(false);
        });
    }

    salvaModifiche(event) {
        this.client.spinEmitter.emit(true);
        // Check validazione
        if (this.validazioneAlZero) {
            this.errorMessage.error.descrizione = "Il modello non puo' essere salvato se e' stato validato precedentemente";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.client.spinEmitter.emit(false);
            return;
        }
        // Chekc totale valori AlZero e fondo assegnato
        debugger
        if (this.totaleValoriAlZero > this.fondiEnte.quotaAllontanamentoZero) {
            this.errorMessage.error.descrizione = "Il totale del data entry inserito non deve superare il fondo assegnato (quota allontanamento zero)";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.client.spinEmitter.emit(false);
            return;
        }
        /**
         * CONTROL:
         * Per poter salvare il modello in presenza di un residuo deve 
         * valorizzarsi il campo note giustificative.
         */
        // if (this.residuo > 0 && ['', ' ', null].includes(this.datiAlZero.giustificativo)) {
        //     this.errorMessage.error.descrizione = "Presenza di residuo, inserisci un giustificativo";
        //     this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
        //     this.client.spinEmitter.emit(false);
        //     return;
        // }

        // Controllo sui superamenti dei massimali
        for (let i = 0; i < this.datiAlZero.lista.length; i++) {
            if (this.datiAlZero.lista[i].valorePerFamiglieMinoriB1 < this.datiAlZero.lista[i].valorePerPrestazioneAlZero) {
                this.superamentoMassimali = true;
            }
        }
        if (this.superamentoMassimali) {
            this.errorMessage.error.descrizione = "Qualche data entry supera il massimale";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.client.spinEmitter.emit(false);
            this.superamentoMassimali = false;
            return;
        }

        //  Check obbligatoria nota, nota obbligatoria solo per Operatore regionale
        if (this.client.azioni.get('CronologiaRegionale')[1] && this.client.azioni.get('ValidaAlZero')[1]) {
            if ((this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length == 0)) {
                this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
                return;
            }
        }

        // Gestione del file 
        if (this.isFileCaricato) {  // Se e' stato caricato un file o era gia' caricato
            if (this.datiAlZero.fileAlZerodb && !this.fileAlZero) { // Esisteva gia' un file e non ne è stato caricato un'altro
                this.datiAlZero.fileAlZero = null;
            }
            if (this.datiAlZero.fileAlZerodb && this.fileAlZero) {  // Se esisteva gia' un file ma ne e' stato caricato un altro
                this.datiAlZero.fileAlZerodb = null;
                this.datiAlZero.fileAlZero = this.fileAlZeroToUpload;
            }
            if (!this.datiAlZero.fileAlZerodb && this.fileAlZero) {
                this.datiAlZero.fileAlZerodb = null;
                this.datiAlZero.fileAlZero = this.fileAlZeroToUpload;
            }
        } else {    // Se nessun file e' stato mai caricato
            this.datiAlZero.fileAlZerodb = null;
            this.datiAlZero.fileAlZero = null;
        }

        if (this.residuo === 0) {   // Creo nuovo record in giustificativo con 
            this.datiAlZero.giustificativo = null;
        }

        // REMOVE ALL UNECESSARY ATTRIBUTES
        this.datiAlZero.lista.forEach((e) => delete e.valorePerPrestazioneAlZeroString);
        // Prendo nota ente e nota interna
        this.datiAlZero.notaEnte = this.cronologiaMod.cronologia.notaEnte;
        this.datiAlZero.notaInterna = this.cronologiaMod.cronologia.notaInterna;
        this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
            this.infoOperatoreBeforeSave = response;
            let statoBeforeSave = this.infoOperatoreBeforeSave.statoRendicontazione.codStatoRendicontazione;
            if (this.statoInitial === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_I && statoBeforeSave === RENDICONTAZIONE_STATUS.IN_RIESAME_I) {
                this.errorMessage.error.descrizione = this.erroreStato;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
                return;
            } else if (this.statoInitial === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_II && statoBeforeSave === RENDICONTAZIONE_STATUS.IN_RIESAME_II) {
                this.errorMessage.error.descrizione = this.erroreStato;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
                return;
            } else {
                let modify: boolean = false;
                this.client.getPrestazioniModelloAlZero(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
                    this.datiAlZeroConcorrenza = response;
                    let modify: boolean = false;
                    for (let i = 0; i < this.datiAlZeroOld.lista.length; i++) {
                        if (this.datiAlZeroOld.lista[i].valorePerPrestazioneAlZero !== this.datiAlZeroConcorrenza.lista[i].valorePerPrestazioneAlZero) {
                            modify = true;
                        }
                    }
                    if (this.datiAlZeroOld.giustificativo !== this.datiAlZeroConcorrenza.giustificativo) {
                        modify = true;
                    }
                    if (modify) {
                        this.errorMessage.error.descrizione = this.erroreModifica.replace('MODELLO', 'Allontanamento Zero');
                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                        this.client.spinEmitter.emit(false);
                        return;
                    } else {
                        // SALVATAGGIO 
                        if (this.client.operazione === OPERAZIONE.VALIDAZIONE_AL_ZERO) {
                            this.datiAlZero.confermaResponsabile = true;
                        }
                        this.client.salvaDatiModelloAlZero(this.datiAlZero, this.rendicontazione.idRendicontazioneEnte)
                            .subscribe(
                                (data) => {
                                    this.client.getPrestazioniModelloAlZero(this.rendicontazione.idRendicontazioneEnte)
                                        .subscribe(
                                            (data) => {
                                                if (this.residuo !== 0) {   // Creo nuovo record in giustificativo con 
                                                    this.errorMessage.error.descrizione = "ATTENZIONE! Salvataggio avvenuto con residuo";
                                                    this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                                                } else {
                                                    this.toastService.showSuccess({ text: 'Salvataggio avvenuto con successo' });
                                                }

                                                this.datiAlZero = data;
                                                this.datiAlZeroOld = {
                                                    fileAlZero: this.datiAlZero.fileAlZero,
                                                    fileAlZerodb: this.datiAlZero.fileAlZerodb,
                                                    giustificativo: this.datiAlZero.giustificativo,
                                                    lista: this.datiAlZero.lista.map(item => ({ ...item })),
                                                    profilo: this.datiAlZero.profilo,
                                                    notaEnte: '',
                                                    notaInterna: '',
                                                    confermaResponsabile: false
                                                };
                                                this.totaleValoriB1 = 0;
                                                this.totaleValoriAlZero = 0;
                                                this.datiAlZero.lista.forEach((e) => {
                                                    e.valorePerPrestazioneAlZeroString = this.formatoItaliano(e.valorePerPrestazioneAlZero);
                                                    if (e.valorePerFamiglieMinoriB1) {  // Calcolo somma valori B1
                                                        this.totaleValoriB1 += e.valorePerFamiglieMinoriB1;
                                                    }
                                                    if (e.valorePerPrestazioneAlZero) { // Calcolo somma valori Allontanamento Zero
                                                        this.totaleValoriAlZero += e.valorePerPrestazioneAlZero;
                                                    }
                                                }); // Utile per classi e valore di input
                                                this.totaleValoriAlZero = parseFloat(this.totaleValoriAlZero.toFixed(2));
                                                if (this.fondiEnte.quotaAllontanamentoZero && this.totaleValoriAlZero) { // Calcolo del residuo
                                                    this.residuo = this.fondiEnte.quotaAllontanamentoZero - this.totaleValoriAlZero;
                                                } else if (this.fondiEnte.quotaAllontanamentoZero && !this.totaleValoriAlZero) {
                                                    this.residuo = this.fondiEnte.quotaAllontanamentoZero;
                                                }
                                                if (this.client.operazione === OPERAZIONE.VALIDAZIONE_AL_ZERO) {
                                                    this.validaAlZero();
                                                }
                                                this.superamentoMassimali = false;
                                                // Check if file is yet upload
                                                if (this.datiAlZero.fileAlZerodb) {
                                                    this.isPresenteDocAlZero = true;
                                                    this.isFileCaricato = true;
                                                    this.docAlZeroName = this.datiAlZero.fileAlZerodb.nomeFile;
                                                    this.listaDocumentiAllegati = [];
                                                    this.listaDocumentiAllegati.push(this.datiAlZero.fileAlZerodb);
                                                    this.documentiTable.renderRows();
                                                }
                                                this.cronologiaMod.ngOnInit();
                                                this.cronologiaMod.espansa = true;
                                                this.cronologiaMod.state = 'rotated';
                                                this.cronologiaMod.apricronologia();
                                                if (this.client.operazione !== OPERAZIONE.VALIDAZIONE_AL_ZERO) {
                                                    this.cronologiaMod.cronologia.notaEnte = null;
                                                    this.cronologiaMod.cronologia.notaInterna = null;
                                                }
                                                this.client.spinEmitter.emit(false);
                                            },
                                            err => {
                                                this.errorMessage.error.descrizione = "Errore durante il salvataggio dei dati";
                                                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                                                this.client.spinEmitter.emit(false);
                                                this.superamentoMassimali = false;
                                            }
                                        );
                                },
                                err => {
                                    this.errorMessage.error.descrizione = "Errore durante il salvataggio dei dati";
                                    this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                                    this.client.spinEmitter.emit(false);
                                }
                            );
                    }
                });
            }
        });
    }

    validaAlZero() {
        // CONTROLLO RESIDUO 
        if (this.residuo !== 0 && (this.datiAlZero.giustificativo === '' || this.datiAlZero.giustificativo === null)) {
            this.errorMessage.error.descrizione = "Attenzione: essendo presente un residuo rispetto al raggiungimento del Totale Allontanamento Zero per poter confermare deve essere inserita una giustificazione";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.client.spinEmitter.emit(false);
            return;
        }

        // CONTROL: il sesiduo non deve essere ugugale alla quota
        if (this.residuo === this.fondiEnte.quotaAllontanamentoZero) {
            this.errorMessage.error.descrizione = "Impossibile confermare perchè non è stato rendicontato alcun valore";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.client.spinEmitter.emit(false);
            return;
        }
        // CONTROL: all valid data entry must be not null
        // let flagValid = true;
        // this.datiAlZero.lista.forEach((e) => {
        //     if (e.valorePerFamiglieMinoriB1) {  // valid data entry
        //         if (!e.valorePerPrestazioneAlZero) { // is set data entry
        //             flagValid = false;
        //         }
        //     }
        // });
        // if (!flagValid) {
        //     this.errorMessage.error.descrizione = "Non tutte le Spesa rendicontata Mod. Al. Zero sono state compilate!";
        //     this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
        //     this.client.spinEmitter.emit(false);
        //     return;
        // }

        // Check obbligatoria nota, nota obbligatoria solo per Operatore regionale
        // if (this.client.azioni.get('ValidaAlZero')[1]) {
        //     if ((this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length == 0)) {
        //         this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
        //         this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
        //         this.client.spinEmitter.emit(false);
        //         return;
        //     }
        // }

        // Check esistenza file
        if (!this.isFileCaricato) {
            this.errorMessage.error.descrizione = "Inserire documentazione obbligatoria";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.client.spinEmitter.emit(false);
            return;
        }

        if (this.residuo !== 0) {
            const dialogRef = this.dialog.open(ConfermaValidazionePopupComponent, {
                width: '600px',
                maxHeight: '90vh',
                // panelClass: 'custom-modal',
                data: { giustificativo: this.datiAlZero.giustificativo }
            });  // Apro la modal
            dialogRef.afterClosed().subscribe(
                (result) => {
                    if (result === 'validazione') {
                        this.client.spinEmitter.emit(true);

                        let payload: ValidazioneAlZero = {
                            idRendicontazioneEnte: this.rendicontazione.idRendicontazioneEnte,
                            toggle: true,
                            cronologia: this.cronologiaMod.cronologia,
                            profilo: this.client.profilo,
                            notaEnte: this.cronologiaMod.cronologia.notaEnte,
                            notaInterna: this.cronologiaMod.cronologia.notaInterna,
                            modello: 'Mod. Al-Zero'
                        }
                        forkJoin({
                            response: this.client.toggleValidazioneAlZero(payload),
                        }).subscribe(({ response }) => {
                            forkJoin({
                                validazione: this.client.getValidazioneModelloAlZero(this.rendicontazione.idRendicontazioneEnte)
                            }).subscribe(({ validazione }) => {
                                this.validazioneAlZero = validazione;
                                // ABILITAZIONE BOTTONI
                                if (this.validazioneAlZero && this.client.azioni.get('InvalidaAlZero')[1]) {
                                    this.client.AnnullaValidazioneAlZero = true;
                                } else {
                                    this.client.AnnullaValidazioneAlZero = false;
                                }
                                if (!this.validazioneAlZero && this.client.azioni.get('ValidaAlZero')[1]) {
                                    this.client.ValidaAlZero = true;
                                } else {
                                    this.client.ValidaAlZero = false;
                                }
                                if (this.validazioneAlZero) {
                                    this.client.SalvaModelliIV = false;
                                } else {
                                    this.client.SalvaModelliIV = true;
                                }
                                this.cronologiaMod.ngOnInit();
                                this.cronologiaMod.espansa = true;
                                this.cronologiaMod.state = 'rotated';
                                this.cronologiaMod.apricronologia();
                                this.cronologiaMod.cronologia.notaEnte = null;
                                this.cronologiaMod.cronologia.notaInterna = null;
                                this.client.spinEmitter.emit(false);
                                this.toastService.showSuccess({ text: 'Conferma avvenuta con successo' });
                            }, err => {
                                this.client.spinEmitter.emit(false);
                            });
                        }, err => {
                            this.client.spinEmitter.emit(false);
                        });
                        this.client.spinEmitter.emit(false);
                    }
                }
            );
        } else {
            this.client.spinEmitter.emit(true);
            let payload: ValidazioneAlZero = {
                idRendicontazioneEnte: this.rendicontazione.idRendicontazioneEnte,
                toggle: true,
                cronologia: this.cronologiaMod.cronologia,
                profilo: this.client.profilo,
                notaEnte: this.cronologiaMod.cronologia.notaEnte,
                notaInterna: this.cronologiaMod.cronologia.notaInterna,
                modello: 'Mod. Al-Zero'
            }
            forkJoin({
                response: this.client.toggleValidazioneAlZero(payload),
            }).subscribe(({ response }) => {
                forkJoin({
                    validazione: this.client.getValidazioneModelloAlZero(this.rendicontazione.idRendicontazioneEnte)
                }).subscribe(({ validazione }) => {
                    this.validazioneAlZero = validazione;
                    // ABILITAZIONE BOTTONI
                    if (this.validazioneAlZero && this.client.azioni.get('InvalidaAlZero')[1]) {
                        this.client.AnnullaValidazioneAlZero = true;
                    } else {
                        this.client.AnnullaValidazioneAlZero = false;
                    }
                    if (!this.validazioneAlZero && this.client.azioni.get('ValidaAlZero')[1]) {
                        this.client.ValidaAlZero = true;
                    } else {
                        this.client.ValidaAlZero = false;
                    }
                    if (this.validazioneAlZero) {
                        this.client.SalvaModelliIV = false;
                    } else {
                        this.client.SalvaModelliIV = true;
                    }
                    this.cronologiaMod.ngOnInit();
                    this.cronologiaMod.espansa = true;
                    this.cronologiaMod.state = 'rotated';
                    this.cronologiaMod.apricronologia();
                    this.cronologiaMod.cronologia.notaEnte = null;
                    this.cronologiaMod.cronologia.notaInterna = null;
                    this.client.spinEmitter.emit(false);
                    this.toastService.showSuccess({ text: 'Conferma avvenuta con successo' });
                    this.client.spinEmitter.emit(false);
                }, err => {
                    this.client.spinEmitter.emit(false);
                });
            }, err => {
                this.client.spinEmitter.emit(false);
            });
        }
    }

    annullaValidazioneAlZero(event) {
        this.client.spinEmitter.emit(true);
        // Check obbligatoria nota, nota obbligatoria solo per Operatore regionale
        // if (this.client.azioni.get('CronologiaRegionale')[1] && this.client.azioni.get('InvalidaAlZero')[1]) {
        //     if ((this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length == 0)) {
        //         this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
        //         this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
        //         this.client.spinEmitter.emit(false);
        //         return;
        //     }
        // }

        let payload: ValidazioneAlZero = {
            idRendicontazioneEnte: this.rendicontazione.idRendicontazioneEnte,
            toggle: false,
            cronologia: this.cronologiaMod.cronologia,
            profilo: this.client.profilo,
            notaEnte: this.cronologiaMod.cronologia.notaEnte,
            notaInterna: this.cronologiaMod.cronologia.notaInterna,
            modello: 'Mod. Al-Zero'
        }
        forkJoin({
            response: this.client.toggleValidazioneAlZero(payload),
        }).subscribe(({ response }) => {
            forkJoin({
                validazione: this.client.getValidazioneModelloAlZero(this.rendicontazione.idRendicontazioneEnte)
            }).subscribe(({ validazione }) => {
                this.validazioneAlZero = validazione;
                // ABILITAZIONE BOTTONI
                if (this.validazioneAlZero && this.client.azioni.get('InvalidaAlZero')[1]) {
                    this.client.AnnullaValidazioneAlZero = true;
                } else {
                    this.client.AnnullaValidazioneAlZero = false;
                }
                if (!this.validazioneAlZero && this.client.azioni.get('ValidaAlZero')[1]) {
                    this.client.ValidaAlZero = true;
                } else {
                    this.client.ValidaAlZero = false;
                }
                if (this.validazioneAlZero) {
                    this.client.SalvaModelliIV = false;
                } else {
                    this.client.SalvaModelliIV = true;
                }
                this.cronologiaMod.ngOnInit();
                this.cronologiaMod.espansa = true;
                this.cronologiaMod.state = 'rotated';
                this.cronologiaMod.apricronologia();
                this.cronologiaMod.cronologia.notaEnte = null;
                this.cronologiaMod.cronologia.notaInterna = null;
                this.client.spinEmitter.emit(false);
                this.toastService.showSuccess({ text: 'Annullamento validazione avvenuta con successo' });
            }, err => {
                this.client.spinEmitter.emit(false);
            });
        }, err => {
            this.client.spinEmitter.emit(false);
        });

        this.client.spinEmitter.emit(false);
    }


    ngAfterContentChecked(): void {
        this.changeDetector.detectChanges();
    }

    // CHEK STATO
    checkStatoRendicontazione() {
        switch (this.rendicontazione.statoRendicontazione.codStatoRendicontazione) {
            case RENDICONTAZIONE_STATUS.IN_RIESAME_I:
            case RENDICONTAZIONE_STATUS.DA_COMPILARE_II:
            case RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_II:
            case RENDICONTAZIONE_STATUS.ATTESA_RETTIFICA_II:
            case RENDICONTAZIONE_STATUS.IN_RIESAME_II:
            case RENDICONTAZIONE_STATUS.ATTESA_VALIDAZIONE:
            case RENDICONTAZIONE_STATUS.VALIDATA:
            case RENDICONTAZIONE_STATUS.STORICIZZATA:
            case RENDICONTAZIONE_STATUS.CONCLUSA:
                return true;
        }
        return false;
    }

    // FROMAT NUMBER
    formatoItaliano(numero: number): string {
        if (numero === 0) {
            return '0,00';
        }
        if (!numero) {
            return null;
        }
        return numero.toLocaleString('it-IT', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    formatoItalianoInputChange(numero: string, i: number) {
        if (numero !== '') {
            this.datiAlZero.lista[i].valorePerPrestazioneAlZeroString = this.datiAlZero.lista[i].valorePerPrestazioneAlZero.toLocaleString('it-IT', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        }
    }

    formatoItalianoInputChangeRealTime(numero: string, i: number) {
        if (!numero || numero == '') {
            this.datiAlZero.lista[i].valorePerPrestazioneAlZeroString = null;
            this.datiAlZero.lista[i].valorePerPrestazioneAlZero = null;
        }
        // Rimuovi eventuali separatori delle migliaia e sostituisci la virgola con il punto per il formato americano
        let risultato = numero.replace(/\./g, '');
        // Sostituisci le virgole con punti
        const numeroAmericano = risultato.replace(/,/g, '.');

        const numeroConvertito = parseFloat(numeroAmericano);
        // Verifica se il numero è valido
        if (isNaN(numeroConvertito)) {
            this.datiAlZero.lista[i].valorePerPrestazioneAlZeroString = null;
            this.datiAlZero.lista[i].valorePerPrestazioneAlZero = null;
        } else {
            this.datiAlZero.lista[i].valorePerPrestazioneAlZero = numeroConvertito;
        }
        this.ricalcolo();
    }


    errorSpesaAlZeroMaggSpesaB1(i: number): boolean {
        return this.datiAlZero.lista[i].valorePerFamiglieMinoriB1 < this.datiAlZero.lista[i].valorePerPrestazioneAlZero ? true : false;
    }

    // GIUSTIFICAZIONE
    openDialogMotivazione() {
        if (this.checkGiustificazione()) {
            const dialogRef = this.dialog.open(MotivazionePopupComponent, {
                width: '600px',
                disableClose: true,
                autoFocus: true,
                data: { titolo: 'Giustificazione', motivazione: this.datiAlZero.giustificativo, placeholder: "Inserire qui la giustificazione", maxlength: 600 }
            });
            dialogRef.afterClosed().subscribe(result => {
                if (result) {
                    this.datiAlZero.giustificativo = result.motivazione;
                }
            });
        }
    }
    checkGiustificazione(): boolean {
        return this.residuo >= 0 ? true : false;
    }

    // Ricalcolo dei dati 
    ricalcolo() {
        this.totaleValoriAlZero = 0;
        this.datiAlZero.lista.forEach((e) => {
            if (e.valorePerPrestazioneAlZero) { // Calcolo somma valori Allontanamento Zero
                this.totaleValoriAlZero += e.valorePerPrestazioneAlZero;
            }
        });
        this.totaleValoriAlZero = parseFloat(this.totaleValoriAlZero.toFixed(2));
        // Prevedo errori di calcolo
        if (this.totaleValoriB1) {
            this.residuo = this.fondiEnte.quotaAllontanamentoZero - (this.totaleValoriAlZero ? this.totaleValoriAlZero : 0);
        }
    }

    // DISABLED ALL NOT NECESSARY BUTTONS
    disableButtons() {
        this.client.SalvaModelliI = false;
        this.client.SalvaModelliII = false;
        this.client.SalvaModelliIII = false;
        this.client.RiapriRendicontazione = false;
        this.client.Concludi = false;
        this.client.ConfermaDatiI = false;
        this.client.ConfermaDatiII = false;
        this.client.RichiestaRettificaI = false;
        this.client.RichiestaRettificaII = false;
        this.client.InviaI = false;
        this.client.InviaII = false;
        this.client.CheckI = false;
        this.client.CheckII = false;
        this.client.Storicizza = false;
        this.client.Valida = false;
    }

    //FILES
    onFileSelected(event) {
        if (this.controlUploadFile(event)) {
            this.fileAlZero = null;
            this.fileAlZero = event.target.files[0];
            this.docAlZeroName = this.fileAlZero.name;
            const reader = new FileReader();
            reader.readAsDataURL(this.fileAlZero);
            reader.onload = () => {
                this.formattedFileAlZero = formatBase64(JSON.stringify(reader.result));
            };
        } else {
            this.errorMessage.error.descrizione = "Inserire un file firmato";
            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            this.docAlZeroName = '';
        }
    }
    controlUploadFile(event) {
        let maxFileSize = 31 * 1024 * 1024;
        if (event.target.files[0].size <= maxFileSize) {
            // if (event.target.files[0].type == "application/pdf") {
            //     return true;
            // }
            // CHECK File firmato
            if (event.target.files[0].type == "application/pkcs7-mime") {
                return true;
            }
        }
        return false;
    }
    allegaFileAlzero() {
        if (!this.fileAlZero) {
            return;
        }
        this.isFileCaricato = true;
        this.fileAlZeroToUpload = new FileAllegatoToUploadGreg(this.formattedFileAlZero, this.fileAlZero.size, this.fileAlZero.name, this.fileAlZero.type, null, 'ALZ');
        this.listaDocumentiAllegati.push(new AllegatoGreg(null, null, this.fileAlZero.size, this.docAlZeroName, null, DOC.DOC_AL_ZERO, null, true))
        this.documentiTable.renderRows();
        this.docAlZeroName = '';
        this.verificaPresenzaAllegatoAlZero();
    }
    eliminaDocumento(index: number) {
        this.listaDocumentiAllegati.splice(index, 1);
        index === 0 ? this.docAlZeroName = '' : this.docAlZeroName = '';
        this.documentiTable.renderRows();
        this.verificaPresenzaAllegatoAlZero();
        this.isFileCaricato = false;
    }
    verificaPresenzaAllegatoAlZero() {
        if (this.listaDocumentiAllegati.length == 0) {
            // this.errDocIniziale = '';
            this.isPresenteDocAlZero = false;
            return false;
        }
        else {
            for (let docAll of this.listaDocumentiAllegati) {
                if (docAll.tipoDocumentazione === DOC.DOC_AL_ZERO) {
                    this.isPresenteDocAlZero = true;
                    return true;
                }
            }
            // this.errDocIniziale = '';
            this.isPresenteDocAlZero = false;
            return false;
        }
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

    esportaModuloAlZero() {
        if (this.checkCambioDati()) {
            this.errorMessage.error.descrizione = "I dati sono cambiati. Non e' consentita l'esportazione in excel. Per effettuare l'esportazione e' necessario effettuare un salvataggio.";
            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
            return;
        }
        let listaCSV: PrestazioniAllontanamentoZeroCSV_DTO[] = [];
        for (let i = 0; i < this.datiAlZero.lista.length; i++) {
            let item: PrestazioniAllontanamentoZeroCSV_DTO = {
                prestazioneCod: this.datiAlZero.lista[i].prestazioneCod,
                prestazioneDesc: this.datiAlZero.lista[i].prestazioneDesc,
                prestazioneId: this.datiAlZero.lista[i].prestazioneId,
                tooltipDesc: this.datiAlZero.lista[i].tooltipDesc,
                valorePerFamiglieMinoriB1: this.formatoItaliano(this.datiAlZero.lista[i].valorePerFamiglieMinoriB1),
                valorePerPrestazioneAlZero: this.formatoItaliano(this.datiAlZero.lista[i].valorePerPrestazioneAlZero),
            }
            listaCSV.push(item);
        }

        let datiAlzeroCSV: PrestazioniAlZeroCSV_DTO = {
            fileAlZero: this.datiAlZero.fileAlZero,
            fileAlZerodb: this.datiAlZero.fileAlZerodb,
            giustificativo: this.datiAlZero.giustificativo,
            fondoRegionale: this.formatoItaliano(this.fondiEnte.fondoRegionale),
            quotaAlZero: this.formatoItaliano(this.fondiEnte.quotaAllontanamentoZero),
            lista: listaCSV,
            residuo: this.residuo !== 0 ? this.formatoItaliano(this.residuo) : '0',
            totaleValoriAlZero: this.totaleValoriAlZero !== 0 ? this.formatoItaliano(this.totaleValoriAlZero) : '0',
            totaleValoriB1: this.totaleValoriB1 !== 0 ? this.formatoItaliano(this.totaleValoriB1) : '0',
            annoEsercizio: this.infoOperatore.annoEsercizio,
            denominazione: this.datiEnte.denominazione
        }
        this.client.esportaModuloAlZero(datiAlzeroCSV, this.idRendicontazioneEnte).subscribe(res => {
            const name: string = res.get('name') as string;
            const messaggio: string = res.get('messaggio') as string;
            this.client.messaggioFeedback = messaggio;
            if (name != 'vuoto') {
                const linkSource = 'data:application/octet-stream;charset=utf-8;base64,' + ' ' + res.get('file') as string;
                const link = document.createElement('a');
                link.href = linkSource;
                link.download = name;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
            this.toastService.showSuccess({ text: messaggio });
            this.client.spinEmitter.emit(false);
        },
            err => {
                this.errorMessage.error.descrizione = "Errore durante il download del file";
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
            });
    }

    // CHECK CAMBIO DATI
    checkCambioDati(): boolean {
        let flag: boolean = false;
        for (let i = 0; i < this.datiAlZero.lista.length; i++) {
            if (this.datiAlZero.lista[i].valorePerPrestazioneAlZero !== this.datiAlZeroOld.lista[i].valorePerPrestazioneAlZero) {
                flag = true;
            }
        }
        if (this.datiAlZero.giustificativo !== this.datiAlZeroOld.giustificativo) {
            flag = true;
        }
        return flag;
    }
}
