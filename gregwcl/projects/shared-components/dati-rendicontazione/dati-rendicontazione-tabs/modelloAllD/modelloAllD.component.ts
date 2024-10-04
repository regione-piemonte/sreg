import { AfterContentChecked, ChangeDetectorRef, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { ERRORS, OPERAZIONE, RENDICONTAZIONE_STATUS, SECTION, STATO_ENTE, TIPOLOGIA_FONDI, TRANCHE } from '@greg-app/constants/greg-constants';
import { forkJoin } from 'rxjs';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { MatDialog } from '@angular/material';
import { VociAllD } from '@greg-app/app/dto/VociAllD';
import { RendicontazioneModAllD } from '@greg-app/app/dto/RendicontazioneModAllD';
import { ModelPrestazioneAllD } from '@greg-app/app/dto/ModelPrestazioneAllD';
import { ModelUtenzaAllD } from '@greg-app/app/dto/ModelUtenzaAllD';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { EsportaModelloCGreg } from '@greg-app/app/dto/EsportaModelloCGreg';
import { EsportaModuloFnps } from '@greg-app/app/dto/EsportaModuloFnps';
import { MotivazionePopupComponent } from '../motivazione-popup/motivazione-popup.component';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { Fondi } from '@greg-app/app/dto/Fondi';
import { MsgInformativo } from '@greg-app/app/dto/MsgInformativo';
import { PrestazioniAlZeroFNPS } from '@greg-app/app/dto/PrestazioniAllontanamentoZeroFnpsDTO';
import { UtenzeFnps } from '@greg-app/app/dto/UtenzeFnps';

@Component({
    selector: 'app-modelloAllD',
    templateUrl: './modelloAllD.component.html',
    styleUrls: ['./modelloAllD.component.css']
})
export class ModelloAllDComponent implements OnInit, AfterContentChecked {

    navigation: Navigation;
    idRendicontazioneEnte;
    ischangedvar: boolean;
    isCompiledFnps: boolean;
    isCompiledB1: boolean;

    fnps: string;
    vincolo: string;
    fnps50: string;
    altroFnps: string;
    valCalcFNPS: number;
    annoGestione: string;

    vociAllD: VociAllD;

    infoAllD01: string;
    infoAllD02: string;
    infoAllD03: string;
    infoAllD04: string;
    infoAllD05: string;
    infoAllD06: string;
    infoAllD07: string;
    infoAllD09: string;
    errorAllDValidationeAlZero: string;
    infoAllD02Splitted: Array<string> = [];

    titolo: string;

    infoOperatore: RendicontazioneEnteGreg;
    tabtranche: ModelTabTranche;
    statoInitial: string;
    infoOperatoreBeforeSave: RendicontazioneEnteGreg;
    rendicontazione: RendicontazioneEnteGreg;
    rendicontazioneAllD: RendicontazioneModAllD;
    rendicontazioneInitialAllD: RendicontazioneModAllD;
    rendicontazioneBeforeSaveAllD: RendicontazioneModAllD;
    rendicontazioneAllDExport: RendicontazioneModAllD;
    TData: ModelPrestazioneAllD[];
    utenze: ModelUtenzaAllD[];

    totale: Array<number> = [];
    totaleUtenza: Array<string> = [];
    totalone: number;
    totaloneUtenze: string;
    totaleB1: Array<number> = [];
    totaleUtenzaB1: Array<string> = [];
    totaloneB1: number = 0;
    totaloneUtenzeB1: string;
    totaleFNPS: Array<number> = [];
    totaleUtenzaFNPS: Array<string> = [];
    totaloneFNPS: number = 0;
    totaloneUtenzeFNPS: string;
    totalonePerCalcRowFNPS: string;
    totaloneDaRendicontare: string;
    giustificazione: string;
    nonPareggioFNPS: boolean = false;

    fondi: Fondi[];
    quote: Fondi[] = [];
    azioniSistema: Fondi[] = [];
    totFnpsQuote: number = 0;
    totFnpsQuoteString: string = '0,00';
    sommatoriaFnpsVincoli: number = 0;
    sommatoriaAzioniSistema: number = 0;
    sommatoriaB1NonCalcolo: number = 0;
    percentualeRiproporzione: number[] = [];
    percentualeRiproporzioneString: string[] = [];
    percentualeFnps: number[] = [];
    percentualeFnpsString: string[] = [];
    totaleAzioni: number = 0;
    totaloneDaRendicontareAzione: string = '0,00'

    totRendInfTotRiprFNPS: boolean;

    percTotale: string = '0';

    testoRosso: boolean = false;
    totUtenzeDivTotFnps: boolean = false;
    contrTotFamiglie: boolean = false;

    erroreStato: string;
    erroreModifica: string;
    erroreFnps1: string;
    erroreFnps2: string;
    erroreFnps3: string;
    erroreFnps4: string;
    erroreFnps5: string;
    erroreFnps6: string;
    erroreexport: string;
    label: MsgInformativo[] = [];
    soglia: number;
    msgSoglia: string;

    errorMessage = {
        error: { descrizione: '' },
        message: '',
        name: '',
        status: '',
        statusText: '',
        url: '',
        date: Date
    };

    prestazioniALZeroFNPS: PrestazioniAlZeroFNPS[];
    // VALIDAZIONE
    validazioneAlZero: boolean = true;

    @ViewChild(CronologiaModelliComponent, { static: false }) cronologiaMod: CronologiaModelliComponent;
    @ViewChild(PulsantiFunzioniComponent, { static: false }) pulsantiMod: PulsantiFunzioniComponent;
    mostrasalva = true;
    datiEnte: DatiEnteGreg;
    checkResiduoLeps: boolean;

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
        this.client.mostrabottoniera = false;

        // RIMUOVI BOTTONI ALLONTANAMENTO ZERO
        this.client.SalvaModelliIV = false;
        this.client.ValidaAlZero = false;
        this.client.AnnullaValidazioneAlZero = false;

        this.client.spinEmitter.emit(true);
        this.client.nomemodello = SECTION.CODALLEGATOD;
        this.client.isModuloFnpsIsCompiled(this.rendicontazione.idRendicontazioneEnte).subscribe((result) => {
            this.isCompiledFnps = result;
            this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe((result) => {
                this.infoOperatore = result;
                this.statoInitial = result.statoRendicontazione.codStatoRendicontazione;
                this.annoGestione = result.annoEsercizio.toString();
                this.client.getValidazioneModelloAlZero(this.rendicontazione.idRendicontazioneEnte).subscribe((result) => {
                    if (this.infoOperatore.annoEsercizio >= 2023) {
                    this.validazioneAlZero = result;
                    }
                    if (this.isCompiledFnps && this.checkStatoRendicontazione() && this.validazioneAlZero) {
                        forkJoin({
                            // infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
                            // ente: this.client.getSchedaEnte(this.idRendicontazioneEnte,this.annoEsercizio),
                            infoAllD01: this.client.getMsgInformativi(SECTION.ALLD01),
                            infoAllD02: this.client.getMsgInformativi(SECTION.ALLD02),
                            infoAllD03: this.client.getMsgInformativi(SECTION.ALLD03),
                            infoAllD04: this.client.getMsgInformativi(SECTION.ALLD04),
                            infoAllD05: this.client.getMsgInformativi(SECTION.ALLD05),
                            infoAllD09: this.client.getMsgInformativi(SECTION.ALLD09),
                            erroreStato: this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
                            erroreModifica: this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
                            erroreFnps1: this.client.getMsgApplicativo(ERRORS.ERROR_FNPS01),
                            erroreFnps2: this.client.getMsgApplicativo(ERRORS.ERROR_FNPS02),
                            erroreFnps3: this.client.getMsgApplicativo(ERRORS.ERROR_FNPS03),
                            erroreFnps4: this.client.getMsgApplicativo(ERRORS.ERROR_FNPS04),
                            erroreFnps5: this.client.getMsgApplicativo(ERRORS.ERROR_FNPS05),
                            erroreFnps6: this.client.getMsgApplicativo(ERRORS.ERROR_FNPS06),
                            voci: this.client.getVociAllD(this.rendicontazione.idRendicontazioneEnte),
                            rendicontazione: this.client.getRendicontazioneModAllD(this.rendicontazione.idRendicontazioneEnte),
                            rendicontazioneInitial: this.client.getRendicontazioneModAllD(this.rendicontazione.idRendicontazioneEnte),
                            erroreexport: this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
                            labelFnps: this.client.getMsgInformativi(SECTION.LABELFNPS),
                            // soglia: this.client.getParametro(SECTION.SOGLIAFNPS),
                            fondi: this.client.getFondiEnte(this.rendicontazione.idRendicontazioneEnte),
                            prestazioniALZeroFNPS: this.client.getPrestazioniModelloAlZeroFNPS(this.rendicontazione.idRendicontazioneEnte)
                        }).subscribe(({ prestazioniALZeroFNPS, infoAllD01, infoAllD02, infoAllD03, infoAllD04, infoAllD05, infoAllD09, erroreStato, erroreModifica, voci, rendicontazione, rendicontazioneInitial, erroreFnps1, erroreFnps2, erroreFnps3, erroreFnps4, erroreexport, fondi, erroreFnps5, labelFnps, erroreFnps6 }) => {
                            // this.infoOperatore = infoOperatore;
                            // this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
                            this.fondi = fondi;

                            this.prestazioniALZeroFNPS = prestazioniALZeroFNPS; //AL ZERO

                            this.titolo = "Modulo FNPS";
                            this.pulsantiMod.abilitaPulsantiFromModello(TRANCHE.III_TRANCHE);
                            this.label = labelFnps;
                            // this.vincolo=infoOperatore.vincoloFondo.toString();
                            // this.valCalcFNPS=100-infoOperatore.vincoloFondo;
                            this.infoAllD01 = infoAllD01[0].testoMsgInformativo;
                            this.infoAllD02 = infoAllD02[0].testoMsgInformativo;
                            // this.annoGestione = infoOperatore.annoEsercizio.toString();
                            this.infoAllD03 = infoAllD03[0].testoMsgInformativo.replace('ANNO', this.annoGestione);
                            // this.infoAllD04=infoAllD04[0].testoMsgInformativo.replace('VALORE', this.vincolo);
                            this.infoAllD05 = infoAllD05[0].testoMsgInformativo;

                            // Tooltip TOTALE RENDICONTAZIONE MODULO FNPS
                            this.infoAllD09 = infoAllD09[0].testoMsgInformativo;
                            
                            this.erroreStato = erroreStato.testoMsgApplicativo;
                            this.erroreModifica = erroreModifica.testoMsgApplicativo;
                            // this.soglia = parseInt(this.vincolo);
                            // this.msgSoglia = soglia.informativa.replace('SOGLIA', this.soglia.toString());
                            this.erroreFnps1 = erroreFnps1.testoMsgApplicativo;
                            this.erroreFnps2 = erroreFnps2.testoMsgApplicativo;
                            this.erroreFnps3 = erroreFnps3.testoMsgApplicativo;
                            this.erroreFnps4 = erroreFnps4.testoMsgApplicativo;
                            this.erroreFnps5 = erroreFnps5.testoMsgApplicativo;
                            this.erroreFnps6 = erroreFnps6.testoMsgApplicativo;
                            // this.initInfoAllD02();

                            this.fnps = this.transform(parseFloat(this.fondi.filter(f => f.codTipologiaFondo === TIPOLOGIA_FONDI.FNPS)[0].valore));
                            this.quote = this.fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.ALTREQUOTE);

                            this.totFnpsQuote += this.parsingFloat(this.fnps);
                            for (let q of this.quote) {

                                q.valore = this.transform(parseFloat(q.valore));
                                q.valoreSpesaFnps = this.transform(parseFloat(q.valoreSpesaFnps));
                                if (q.funzioneRegola == '+') {
                                    this.totFnpsQuote += this.parsingFloat(q.valore);
                                } else if (q.funzioneRegola == '-') {
                                    this.totFnpsQuote -= this.parsingFloat(q.valore);
                                }

                            }
                            this.totFnpsQuoteString = this.transform(this.totFnpsQuote);

                            this.azioniSistema = this.fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.AZIONISISTEMA);
                            for (let q of this.azioniSistema) {
                                q.valore = this.transform(parseFloat(q.valore));
                                q.valoreSpesaFnps = this.transform(parseFloat(q.valoreSpesaFnps));
                            }
                            // this.fnps50 = this.transform((infoOperatore.fnps*infoOperatore.vincoloFondo)/100);
                            // this.altroFnps = this.transform((infoOperatore.fnps*(100-infoOperatore.vincoloFondo)/100));
                            this.vociAllD = new VociAllD();
                            this.vociAllD = voci;
                            this.rendicontazioneAllD = rendicontazione;
                            this.rendicontazioneInitialAllD = rendicontazioneInitial;
                            this.rendicontazioneAllDExport = rendicontazione;
                            this.TData = this.rendicontazioneAllD.vociAllD.listaPrestazione;
                            this.utenze = this.rendicontazioneAllD.vociAllD.listaTargetUtenze;
                            for (let u of this.utenze) {
                                u.valorePercentuale = this.transform(parseFloat(u.valorePercentuale));
                            }
                            this.rendicontazioneAllD.azioniSistema = JSON.parse(JSON.stringify(this.azioniSistema))
                            this.rendicontazioneInitialAllD.azioniSistema = JSON.parse(JSON.stringify(this.azioniSistema));
                            this.rendicontazioneAllD.quote = JSON.parse(JSON.stringify(this.quote))
                            this.rendicontazioneInitialAllD.quote = JSON.parse(JSON.stringify(this.quote));
                            this.initCalcFormula();
                            this.initB1();
                            this.calcTotaleB1FNPS();
                            this.initCalc();
                            this.initialTransform();
                            this.erroreexport = erroreexport.testoMsgApplicativo;
                            this.client.mostrabottoniera = true;
                            this.client.spinEmitter.emit(false);
                        }, err => {
                            this.client.mostrabottoniera = true;
                            this.client.spinEmitter.emit(false);
                        });
                    } else {
                        forkJoin({
                            infoAllD06: this.client.getMsgInformativi(SECTION.ALLD06),
                            infoAllD07: this.client.getMsgInformativi(SECTION.ALLD07),
                            infoAllD08: this.client.getMsgInformativi(SECTION.ALLD08),
                        }).subscribe(({ infoAllD06, infoAllD07, infoAllD08 }) => {
                            this.infoAllD06 = infoAllD06[0].testoMsgInformativo;
                            this.infoAllD07 = infoAllD07[0].testoMsgInformativo;
                            this.errorAllDValidationeAlZero = infoAllD08[0].testoMsgInformativo;
                            this.client.mostrabottoniera = false;
                            if (!this.isCompiledFnps) {
                                this.toastService.showError({ text: this.infoAllD06 });
                            }
                            if (!this.checkStatoRendicontazione()) {
                                this.toastService.showError({ text: this.infoAllD07 });
                            }
                            if (!this.validazioneAlZero) {
                                this.toastService.showError({ text: this.errorAllDValidationeAlZero });
                            }
                            this.client.spinEmitter.emit(false);
                        });
                    }
                }, err => {
                    this.validazioneAlZero = false;
                    this.client.spinEmitter.emit(false);
                });
            }, err => {
                this.validazioneAlZero = false;
                this.client.spinEmitter.emit(false);
            });

        }, err => {
            this.client.mostrabottoniera = true;
            this.client.spinEmitter.emit(false);
        });
    }

    // initInfoAllD02(){
    //     let s1=this.infoAllD02.substr(0, 18);
    //     let s2=this.infoAllD02.substr(18, 9);
    //     let s3=this.infoAllD02.substr(27, 142)

    //     this.infoAllD02Splitted.push(s1, s2, s3);
    // }


    parsingFloat(el) {
        if (el == '' || el == '-') el = null;
        el = el ? parseFloat(el.toString().replaceAll('.', '').replace(',', '.')) : el;
        return el;
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

    initB1() {
        for (let i = 0; i < this.TData.length; i++) {
            for (let j = 0; j < this.TData[i].utenze.length; j++) {
                if (this.TData[i].utenze[j].valoreB1 != null && this.TData[i].utenze[j].valoreB1 != undefined) {
                    this.TData[i].utenze[j].valoreB1 = this.transform(this.parsingFloat(this.transform(parseFloat(this.TData[i].utenze[j].valoreB1))));
                }
                if (this.TData[i].utenze[j].valorePercentuale != null && this.TData[i].utenze[j].valorePercentuale != undefined) {
                    this.TData[i].utenze[j].valorePercentuale = this.transform(parseFloat(this.TData[i].utenze[j].valorePercentuale));
                }
            }
        }
    }

    initCalc() {
        for (let i = 0; i < this.TData.length; i++) {
            for (let j = 0; j < this.TData[i].utenze.length; j++) {
                if (this.TData[i].utenze[j].valore != null && this.TData[i].utenze[j].valore != undefined) {
                    this.TData[i].utenze[j].valore = this.transform(parseFloat(this.TData[i].utenze[j].valore));
                }
                if (this.TData[i].utenze[j].valorePercentuale != null && this.TData[i].utenze[j].valorePercentuale != undefined) {
                    this.TData[i].utenze[j].valorePercentuale = this.transform(parseFloat(this.TData[i].utenze[j].valorePercentuale));
                }
            }
        }
        for (let u of this.vociAllD.listaTargetUtenze) {
            if (u.valore != null && u.valore != undefined) {
                u.valore = this.transform(parseFloat(u.valore));
            }
            if (u.valorePercentuale != null && u.valorePercentuale != undefined) {
                u.valorePercentuale = this.transform(parseFloat(u.valorePercentuale));
            }
        }
        this.calcTableValue();
    }

    // Calcoli effettuati ad ogni cambiamento dei valori
    calcTableValue() {
        this.initRowTotale();
        let utenze: ModelUtenzaAllD[] = this.rendicontazioneAllD.vociAllD.listaTargetUtenze;
        for (let i = 0; i < this.TData.length; i++) {
            for (let j = 0; j < this.TData[i].utenze.length; j++) {
                if (this.TData[i].utenze[j].codUtenza = utenze[j].codUtenza) {
                    this.totale[j] += this.parsingFloat(this.TData[i].utenze[j].valore);
                }
                //    if(j==0){
                //     this.totale[j]+=this.parsingFloat(this.TData[i].utenze[j].valore);
                //    }
                //    if(j==1){
                //     this.totale[j]+=this.parsingFloat(this.TData[i].utenze[j].valore);
                //    }
                //    if(j==2){
                //     this.totale[j]+=this.parsingFloat(this.TData[i].utenze[j].valore);
                //    }
                //    if(j==3){
                //     this.totale[j]+=this.parsingFloat(this.TData[i].utenze[j].valore);
                //    }
                //    if(j==4){
                //     this.totale[j]+=this.parsingFloat(this.TData[i].utenze[j].valore);
                //    }
                //    if(j==5){
                //     this.totale[j]+=this.parsingFloat(this.TData[i].utenze[j].valore);
                //    }

            }

        }

        for (let j = 0; j < this.utenze.length; j++) {
            this.totaleUtenza[j] = this.transform(this.totale[j]);

            this.totalone += this.totale[j];
            if (this.parsingFloat(this.totFnpsQuoteString) != 0) {
                this.percentualeFnps[j] = this.roundToTwo(((this.parsingFloat(this.totaleUtenza[j]) / this.parsingFloat(this.totFnpsQuoteString)) * 100), 2);
            } else {
                this.percentualeFnps[j] = 0;
            }
            this.percentualeFnpsString[j] = this.transform(this.percentualeFnps[j]);
            this.totale[j] = 0;
        }
        this.totaloneUtenze = (this.transform(this.totalone));
        this.totalone = 0;

        if (this.parsingFloat(this.totaloneUtenze) != this.parsingFloat(this.totFnpsQuoteString)) {
            if ((this.parsingFloat(this.totaloneUtenze) < this.parsingFloat(this.totFnpsQuoteString))) {
                this.nonPareggioFNPS = true;
            } else {
                this.nonPareggioFNPS = false;
            }
            this.totUtenzeDivTotFnps = true;
        } else {
            this.rendicontazioneAllD.giustificazione = null;
            this.nonPareggioFNPS = false;
            this.totUtenzeDivTotFnps = false;
        }

        this.percTotale = this.truncateDecimals((this.parsingFloat(this.totaloneUtenze) / this.parsingFloat(this.totFnpsQuoteString)) * 100, 2).toString();
        this.totaloneDaRendicontare = this.transform(this.parsingFloat(this.totFnpsQuoteString) - this.parsingFloat(this.totaloneUtenze));
        let sommaAzioni = 0;
        for (let a of this.rendicontazioneAllD.azioniSistema) {
            if (a.valoreSpesaFnps && a.leps) {
                this.totaleAzioni += this.parsingFloat(a.valoreSpesaFnps);
            }
            if (a.valore && a.leps) {
                sommaAzioni += this.parsingFloat(a.valore);
            }
        }
        for (let a of this.rendicontazioneAllD.quote) {
            if (a.valoreSpesaFnps && a.leps) {
                this.totaleAzioni += this.parsingFloat(a.valoreSpesaFnps);
            }
            if (a.valore && a.leps) {
                sommaAzioni += this.parsingFloat(a.valore);
            }
        }
        this.totaloneDaRendicontareAzione = this.transform((sommaAzioni - this.totaleAzioni));
        if (this.parsingFloat(this.totaloneDaRendicontare) < this.parsingFloat(this.totaloneDaRendicontareAzione)) {
            this.checkResiduoLeps = true;
        } else {
            this.checkResiduoLeps = false;
        }
        this.totaleAzioni = 0;
    }

    truncateDecimals = function (number, digits) {
        var multiplier = Math.pow(10, digits),
            adjustedNum = number * multiplier,
            truncatedNum = Math[adjustedNum < 0 ? 'ceil' : 'floor'](adjustedNum);

        return truncatedNum / multiplier;
    };

    initRowTotale() {
        this.totalone = 0;
        this.totaloneB1 = 0;
        this.totaloneFNPS = 0;
        for (let i = 0; i < this.TData.length; i++) {
            for (let j = 0; j < this.TData[i].utenze.length; j++) {
                this.totale[j] = 0;
                this.totaleB1[j] = 0;
                this.totaleFNPS[j] = 0;
            }
        }
    }

    calcTotaleB1FNPS() {
        this.initRowTotale();
        for (let i = 0; i < this.TData.length; i++) {
            for (let j = 0; j < this.TData[i].utenze.length; j++) {

                this.totaleB1[j] += this.parsingFloat(this.TData[i].utenze[j].valoreB1);

                if (!this.TData[i].utenze[j].utilizzatoPerCalcolo) {
                    this.sommatoriaB1NonCalcolo += this.parsingFloat(this.TData[i].utenze[j].valoreB1);
                }
            }
        }

        for (let j = 0; j < this.utenze.length; j++) {
            this.totaleUtenzaB1[j] = this.transform(this.totaleB1[j]);
            this.totaloneB1 += this.totaleB1[j];
            this.totaleB1[j] = 0;
        }
        this.totaloneUtenzeB1 = this.transform(this.totaloneB1);
        this.totalonePerCalcRowFNPS = this.transform(this.sommatoriaB1NonCalcolo);
        this.sommatoriaB1NonCalcolo = 0;

        let sommatoria = 0; // sommatoria valori input al zero
        for (let i = 0; i < this.prestazioniALZeroFNPS.length; i++) {
            sommatoria += this.prestazioniALZeroFNPS[i].sommaValore;
        }

        for (let i = 0; i < this.TData.length; i++) {
            for (let j = 0; j < this.TData[i].utenze.length; j++) {
                if (this.TData[i].utenze[j].utilizzatoPerCalcolo) {
                    if (this.parsingFloat(this.totaleUtenzaB1[j]) != 0) {
                        let fnpsCalcolo = this.roundToTwo((this.parsingFloat(this.totFnpsQuoteString) * this.parsingFloat(this.TData[i].utenze[j].valorePercentuale) / 100), 2);
                        // this.TData[i].utenze[j].valoreFNPS = this.transform(this.roundToTwo((fnpsCalcolo * (this.parsingFloat(this.TData[i].utenze[j].valoreB1))) / this.parsingFloat(this.totaleUtenzaB1[j]), 2));
                        this.TData[i].utenze[j].valoreFNPS = this.transform(this.roundToTwo((fnpsCalcolo * (this.parsingFloat(this.TData[i].utenze[j].valoreB1))) / (this.parsingFloat(this.totaleUtenzaB1[j])), 2));
                    } else {
                        this.TData[i].utenze[j].valoreFNPS = '0,00';
                    }

                } else {
                    if (this.parsingFloat(this.totalonePerCalcRowFNPS) != 0) {
                        this.TData[i].utenze[j].valoreFNPS = this.transform(this.roundToTwo(this.parsingFloat(this.altroFnps) * (this.parsingFloat(this.TData[i].utenze[j].valoreB1) / (this.parsingFloat(this.totalonePerCalcRowFNPS))), 2));
                    } else {
                        this.TData[i].utenze[j].valoreFNPS = '0,00';
                    }
                }

                this.totaleFNPS[j] += this.parsingFloat(this.TData[i].utenze[j].valoreFNPS);

            }
        }

        for (let j = 0; j < this.utenze.length; j++) {
            this.totaleUtenzaFNPS[j] = this.transform(this.totaleFNPS[j]);
            this.totaloneFNPS += this.totaleFNPS[j];
            if (this.parsingFloat(this.totFnpsQuoteString) != 0) {
                this.percentualeRiproporzione[j] = this.roundToTwo(((this.parsingFloat(this.totaleUtenzaFNPS[j]) / this.parsingFloat(this.totFnpsQuoteString)) * 100), 2);
            } else {
                this.percentualeRiproporzione[j] = 0;
            }
            this.percentualeRiproporzioneString[j] = this.transform(this.percentualeRiproporzione[j]);
            this.totaleFNPS[j] = 0;
        }
        this.totaloneUtenzeFNPS = this.transform(this.totaloneFNPS);
    }

    getValueAlZeroFamiglieMinori(codUtenza: string, codPrestazione: string) {
        if (codUtenza === 'U09') {
            const prestAlZero = this.prestazioniALZeroFNPS.filter((e) => e.codPrestMinisteriale === codPrestazione);
            if (prestAlZero && prestAlZero.length === 1) {
                return prestAlZero[0].sommaValore;
            }
        }
        return 0;
    }

    roundToTwo(num: number, places: number) {
        const factor = 10 ** places;
        return Math.round((num + Number.EPSILON) * factor) / factor;
    };

    changeKey(i: number, valore: any, j: number) {
        if (valore == "" || valore == null || valore == undefined) {
            this.TData[i].utenze[j].valore = null;
        }
        else {
            if (valore.indexOf('.') != -1) {
                if (valore.indexOf(',') != -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
                    valore = [valore, '0'].join('');
                }
                this.TData[i].utenze[j].valore = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
            }
            else {
                this.TData[i].utenze[j].valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
            }
        }
        this.calcTableValue();
    }

    errorSpesaFnpsMaggSpesaB1(spesaFnps: string, spesaB1: string) {
        if (this.parsingFloat(spesaFnps) > this.parsingFloat(spesaB1)) {
            this.testoRosso = true;
            return this.testoRosso;
        }
        this.testoRosso = false;
        return this.testoRosso;
    }

    errorSpesaFamiglie(spesaFnps: string, utenza: ModelUtenzaAllD) {
        if (utenza.utilizzatoPerCalcolo) {
            let fnpsCalcolato = this.transform(this.roundToTwo((this.parsingFloat(this.totFnpsQuoteString) * this.parsingFloat(utenza.valorePercentuale) / 100), 2));
            if (this.parsingFloat(spesaFnps) < this.parsingFloat(fnpsCalcolato) || this.parsingFloat(spesaFnps) > this.parsingFloat(this.totFnpsQuoteString)) {
                this.contrTotFamiglie = true;
            } else {
                this.contrTotFamiglie = false;
            }
        } else {
            this.contrTotFamiglie = false;
        }
        return this.contrTotFamiglie;
    }


    //         //todo: inserire messaggio errore in db per controllo salvataggi diversi in contemporanea
    salvaModifiche(event) {
        this.client.nomemodello = SECTION.CODALLEGATOD;
        this.client.spinEmitter.emit(true);
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
                this.client.getRendicontazioneModAllD(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
                    this.rendicontazioneBeforeSaveAllD = response;
                    this.rendicontazioneBeforeSaveTransform();
                    for (let i = 0; i < this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione.length; i++) {
                        for (let j = 0; j < this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione[i].utenze.length; j++) {
                            if (this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione[i].utenze[j].valore !== this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore) {
                                modify = true;
                            }
                        }
                    }
                    if (this.rendicontazioneBeforeSaveAllD.giustificazione != this.rendicontazioneInitialAllD.giustificazione) {
                        modify = true;
                    }

                    if (modify) {
                        this.errorMessage.error.descrizione = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                        this.client.spinEmitter.emit(false);
                        return;
                    } else {
                        //Controlli
                        this.initRowTotale();
                        let redText: boolean = false;
                        let index: number;
                        let codPrestazione: string;
                        let prestazione: string;
                        let utenza: string;
                        for (let i = 0; i < this.rendicontazioneAllD.vociAllD.listaPrestazione.length; i++) {
                            for (let j = 0; j < this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze.length; j++) {
                                let fnpsCalcolato = this.roundToTwo(((this.parsingFloat(this.totFnpsQuoteString) * this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale)) / 100), 2);
                                if (this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore) > this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreB1)) {
                                    redText = true;
                                    codPrestazione = this.rendicontazioneAllD.vociAllD.listaPrestazione[i].codPrestazione;
                                    prestazione = this.rendicontazioneAllD.vociAllD.listaPrestazione[i].descPrestazione;
                                    utenza = this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].descUtenza;
                                }
                                if (this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].utilizzatoPerCalcolo) {
                                    if ((this.parsingFloat(this.totaleUtenza[j]) < fnpsCalcolato) || (this.parsingFloat(this.totaleUtenza[index]) > this.parsingFloat(this.totFnpsQuoteString))) {
                                        this.errorMessage.error.descrizione = this.erroreFnps3.replace("FNPS50", this.transform(fnpsCalcolato)).replace("FNPS100", this.totFnpsQuoteString).replace("VINCOLO", this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale).replace("UTENZA", this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].descUtenza);
                                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                                        this.client.spinEmitter.emit(false);
                                        return;
                                    }
                                }
                            }
                        }

                        for (let a of this.rendicontazioneAllD.azioniSistema) {
                            if (this.parsingFloat(a.valore) < this.parsingFloat(a.valoreSpesaFnps)) {
                                this.errorMessage.error.descrizione = this.erroreFnps5.replace("AZIONE", a.descFondo).replace("VALORESPESA", a.valoreSpesaFnps).replace("VALORE", a.valore);
                                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                                this.client.spinEmitter.emit(false);
                                return;
                            }
                        }

                        for (let a of this.rendicontazioneAllD.quote) {
                            if (this.parsingFloat(a.valore) < this.parsingFloat(a.valoreSpesaFnps)) {
                                this.errorMessage.error.descrizione = this.erroreFnps5.replace("AZIONE", a.descFondo).replace("VALORESPESA", a.valoreSpesaFnps).replace("VALORE", a.valore);
                                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                                this.client.spinEmitter.emit(false);
                                return;
                            }
                        }
                        if (this.checkResiduoLeps) {
                            this.errorMessage.error.descrizione = this.erroreFnps6;
                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                            this.client.spinEmitter.emit(false);
                            return;
                        }

                        if (redText) {
                            this.errorMessage.error.descrizione = this.erroreFnps1.replace("CODPRESTAZIONE", codPrestazione).replace("PRESTAZIONE", prestazione).replace("UTENZA", utenza);
                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                            this.client.spinEmitter.emit(false);
                            return;
                        }
                        if (this.parsingFloat(this.totaloneUtenze) < this.parsingFloat(this.totFnpsQuoteString)) {
                            this.totRendInfTotRiprFNPS = true;
                        } else {
                            this.totRendInfTotRiprFNPS = false;
                        }

                        if (this.parsingFloat(this.totaloneUtenze) < this.parsingFloat(this.totFnpsQuoteString) && (this.rendicontazioneAllD.giustificazione === null || this.rendicontazioneAllD.giustificazione === undefined || this.rendicontazioneAllD.giustificazione === "" || this.rendicontazioneAllD.giustificazione.trim().length == 0)) {
                            this.errorMessage.error.descrizione = this.erroreFnps2;
                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                            this.client.spinEmitter.emit(false);
                            return;
                        }
                        if (this.parsingFloat(this.totaloneUtenze) > this.parsingFloat(this.totFnpsQuoteString)) {
                            this.errorMessage.error.descrizione = this.erroreFnps4;
                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
                            this.client.spinEmitter.emit(false);
                            return;
                        }
                        //if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
                        if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
                            && (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length == 0)) {
                            this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
                            this.client.spinEmitter.emit(false);
                            return;
                        } else {
                            this.rendicontazioneAllD.azzeramentoGiustificativo = false;
                            if (this.rendicontazioneAllD.giustificazione === null && this.rendicontazioneInitialAllD.giustificazione !== null) {
                                this.rendicontazioneAllD.azzeramentoGiustificativo = true;
                            }
                            if (this.rendicontazioneAllD.azzeramentoGiustificativo && (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length == 0)) {
                                this.errorMessage.error.descrizione = "Il residuo presente e' stato ora azzerato. Fornire nel campo note una breve spiegazione.";
                                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Il residuo presente e' stato ora azzerato. Fornire nel campo note una breve spiegazione." }))
                                this.client.spinEmitter.emit(false);
                                return;
                            } else {
                                this.rendicontazioneAllD.profilo = this.client.profilo;

                                for (let i = 0; i < this.rendicontazioneAllD.vociAllD.listaPrestazione.length; i++) {
                                    for (let j = 0; j < this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze.length; j++) {
                                        if (this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore) {
                                            this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore = this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore);
                                        }
                                        if (this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreB1) {
                                            this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreB1 = this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreB1);
                                        }
                                        if (this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreFNPS) {
                                            this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreFNPS = this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valoreFNPS);
                                        }
                                        if (this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale) {
                                            this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale = this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale);
                                        }
                                    }
                                }
                                this.rendicontazioneAllD.giustificazionePerInf = this.totRendInfTotRiprFNPS;
                                for (let i = 0; i < this.rendicontazioneAllD.vociAllD.listaTargetUtenze.length; i++) {
                                    if (this.rendicontazioneAllD.vociAllD.listaTargetUtenze[i].valorePercentuale) {
                                        this.rendicontazioneAllD.vociAllD.listaTargetUtenze[i].valorePercentuale = this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaTargetUtenze[i].valorePercentuale);
                                    }
                                }
                                this.rendicontazioneAllD.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                                for (let a of this.rendicontazioneAllD.azioniSistema) {
                                    if (a.valoreSpesaFnps) {
                                        a.valoreSpesaFnps = this.parsingFloat(a.valoreSpesaFnps);
                                    }
                                    if (a.valore) {
                                        a.valore = this.parsingFloat(a.valore);
                                    }
                                }
                                for (let a of this.rendicontazioneAllD.quote) {
                                    if (a.valoreSpesaFnps) {
                                        a.valoreSpesaFnps = this.parsingFloat(a.valoreSpesaFnps);
                                    }
                                    if (a.valore) {
                                        a.valore = this.parsingFloat(a.valore);
                                    }
                                }


                                this.client.saveModelloAllD(this.rendicontazioneAllD, this.cronologiaMod.cronologia.notaEnte, this.cronologiaMod.cronologia.notaInterna)
                                    .subscribe(
                                        (data: ResponseSalvaModelloGreg) => {
                                            if (data.warnings && data.warnings.length > 0) {
                                                for (let warn of data.warnings) {
                                                    this.errorMessage.error.descrizione = warn;
                                                    this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
                                                }
                                            }
                                            this.refillInitials();
                                            this.cronologiaMod.ngOnInit();
                                            this.cronologiaMod.espansa = true;
                                            this.cronologiaMod.state = 'rotated';
                                            this.cronologiaMod.apricronologia();
                                            this.cronologiaMod.cronologia.notaEnte = null;
                                            this.cronologiaMod.cronologia.notaInterna = null;

                                            //ricalcolo pulsantiera in caso di cambio stato
                                            this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODALLEGATOD);

                                            this.initB1();
                                            this.calcTotaleB1FNPS();
                                            this.initCalc();

                                            if (this.client.operazione == OPERAZIONE.INVIAMODELLI) {
                                                this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                                            }
                                            else {
                                                this.toastService.showSuccess({ text: data.messaggio });
                                                // this.client.spinEmitter.emit(false);
                                            }
                                        },
                                        err => {
                                            this.initB1();
                                            this.calcTotaleB1FNPS();
                                            this.initCalc();
                                            this.client.spinEmitter.emit(false);
                                        }
                                    );
                            }
                        }
                    }
                });
            }
        });
    }

    @HostListener('document:changeTabEvent')
    checkEdited(event) {
        this.ischanged();
        if (this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
        else document.dispatchEvent(this.client.changeTabEmitter);
    }

    refillInitials() {
        this.client.getRendicontazioneModAllD(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
            this.client.getFondiEnte(this.rendicontazione.idRendicontazioneEnte).subscribe(fondi => {
                this.rendicontazioneInitialAllD = response;

                this.azioniSistema = fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.AZIONISISTEMA);
                for (let q of this.azioniSistema) {
                    q.valore = this.transform(parseFloat(q.valore));
                    q.valoreSpesaFnps = this.transform(parseFloat(q.valoreSpesaFnps));
                }
                for (let u of this.utenze) {
                    u.valorePercentuale = this.transform(parseFloat(u.valorePercentuale));
                }
                this.rendicontazioneAllD.azioniSistema = JSON.parse(JSON.stringify(this.azioniSistema))
                this.rendicontazioneInitialAllD.azioniSistema = JSON.parse(JSON.stringify(this.azioniSistema));
                this.quote = fondi.filter(f => f.codTipologiaFondo == TIPOLOGIA_FONDI.ALTREQUOTE);
                for (let q of this.quote) {
                    q.valore = this.transform(parseFloat(q.valore));
                    q.valoreSpesaFnps = this.transform(parseFloat(q.valoreSpesaFnps));
                }
                this.rendicontazioneAllD.quote = JSON.parse(JSON.stringify(this.quote))
                this.rendicontazioneInitialAllD.quote = JSON.parse(JSON.stringify(this.quote));
                this.initialTransform();
                this.client.spinEmitter.emit(false);
            })
        })
    }

    initialTransform() {
        this.initRowTotale();
        for (let i = 0; i < this.rendicontazioneInitialAllD.vociAllD.listaPrestazione.length; i++) {
            for (let j = 0; j < this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze.length; j++) {
                if (this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore) {
                    this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore = this.transform(parseFloat(this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore));
                }
            }
        }
        for (let i = 0; i < this.rendicontazioneInitialAllD.vociAllD.listaTargetUtenze.length; i++) {
            if (this.rendicontazioneInitialAllD.vociAllD.listaTargetUtenze[i].valorePercentuale) {
                this.rendicontazioneInitialAllD.vociAllD.listaTargetUtenze[i].valorePercentuale = this.transform(parseFloat(this.rendicontazioneInitialAllD.vociAllD.listaTargetUtenze[i].valorePercentuale));
            }
        }
    }

    esportaexcel() {
        this.ischanged();
        if (this.ischangedvar) {
            this.toastService.showError({ text: this.erroreexport });
        }
        else {
            this.client.spinEmitter.emit(true);
            let esporta: EsportaModuloFnps = new EsportaModuloFnps();
            esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
            esporta.annoGestione = this.infoOperatore.annoEsercizio.toString();
            esporta.prestazioniFnps = this.vociAllD.listaPrestazione;

            esporta.areeUtenze = this.vociAllD.aree;
            esporta.fnps = this.totFnpsQuoteString;
            esporta.denominazioneEnte = this.datiEnte.denominazione;
            esporta.totaloneUtenze = this.totaloneUtenze;
            esporta.residuo = this.totaloneDaRendicontare.toString();
            esporta.giustificazione = this.rendicontazioneAllD.giustificazione;
            esporta.residuoAzioni = this.totaloneDaRendicontareAzione.toString();

            for (let i = 0; i < this.rendicontazioneAllDExport.vociAllD.listaPrestazione.length; i++) {
                for (let j = 0; j < this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze.length; j++) {
                    this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valore = this.parsingFloat(this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valore);
                    this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valoreB1 = this.parsingFloat(this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valoreB1);
                    this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valoreFNPS = this.parsingFloat(this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valoreFNPS);
                    this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale = this.parsingFloat(this.rendicontazioneAllDExport.vociAllD.listaPrestazione[i].utenze[j].valorePercentuale);
                }
            }
            for (let i = 0; i < this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze.length; i++) {
                this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze[i].valorePercentuale = this.parsingFloat(this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze[i].valorePercentuale);
            }
            esporta.totaleUtenze = [];
            for (let i = 0; i < this.totaleUtenza.length; i++) {
                esporta.totaleUtenze[i] = this.parsingFloat(this.totaleUtenza[i]);
            }
            for (let a of this.rendicontazioneAllDExport.azioniSistema) {
                a.valore = this.parsingFloat(a.valore);
                a.valoreSpesaFnps = this.parsingFloat(a.valoreSpesaFnps);
            }
            for (let a of this.rendicontazioneAllDExport.quote) {
                a.valore = this.parsingFloat(a.valore);
                a.valoreSpesaFnps = this.parsingFloat(a.valoreSpesaFnps);
            }
            for (let u of this.vociAllD.listaTargetUtenze) {
                u.valorePercentuale = this.parsingFloat(u.valorePercentuale);
            }
            esporta.datiFnps = this.rendicontazioneAllDExport;
            esporta.utenzeFnps = this.vociAllD.listaTargetUtenze;
            this.client.esportaModuloFnps(esporta).subscribe(res => {
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
                    this.initB1();
                    this.calcTotaleB1FNPS();
                    this.initCalc();
                    for (let i = 0; i < this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze.length; i++) {
                        this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze[i].valorePercentuale = this.transform(parseFloat(this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze[i].valorePercentuale));
                    }
                    for (let a of this.rendicontazioneAllDExport.azioniSistema) {
                        a.valore = this.transform(parseFloat(a.valore));
                        a.valoreSpesaFnps = this.transform(parseFloat(a.valoreSpesaFnps));
                    }
                    for (let a of this.rendicontazioneAllDExport.quote) {
                        a.valore = this.transform(parseFloat(a.valore));
                        a.valoreSpesaFnps = this.transform(parseFloat(a.valoreSpesaFnps));
                    }
                }
                this.toastService.showSuccess({ text: messaggio });
                this.client.spinEmitter.emit(false);
            },
                err => {
                    this.initB1();
                    this.calcTotaleB1FNPS();
                    this.initCalc();
                    for (let i = 0; i < this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze.length; i++) {
                        this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze[i].valorePercentuale = this.transform(parseFloat(this.rendicontazioneAllDExport.vociAllD.listaTargetUtenze[i].valorePercentuale));
                    }
                    for (let a of this.rendicontazioneAllDExport.azioniSistema) {
                        a.valore = this.transform(parseFloat(a.valore));
                        a.valoreSpesaFnps = this.transform(parseFloat(a.valoreSpesaFnps));
                    }
                    this.client.spinEmitter.emit(false);
                });
        }
    }

    ischanged() {
        this.ischangedvar = false;
        let initial;
        let normal;
        if (this.isCompiledFnps && this.checkStatoRendicontazione()) {
            if (this.rendicontazioneAllD.giustificazione != this.rendicontazioneInitialAllD.giustificazione) {
                this.ischangedvar = true;
            }
            for (let i = 0; i < this.rendicontazioneAllD.vociAllD.listaPrestazione.length; i++) {
                for (let j = 0; j < this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze.length; j++) {
                    initial = this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore ? this.parsingFloat(this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore) : this.rendicontazioneInitialAllD.vociAllD.listaPrestazione[i].utenze[j].valore;
                    normal = this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore ? this.parsingFloat(this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore) : this.rendicontazioneAllD.vociAllD.listaPrestazione[i].utenze[j].valore;
                    if (normal !== initial) {
                        this.ischangedvar = true;
                    }
                }
            }
            for (let i = 0; i < this.rendicontazioneAllD.azioniSistema.length; i++) {
                initial = this.rendicontazioneInitialAllD.azioniSistema[i].valoreSpesaFnps ? this.parsingFloat(this.rendicontazioneInitialAllD.azioniSistema[i].valoreSpesaFnps) : this.rendicontazioneInitialAllD.azioniSistema[i].valoreSpesaFnps;
                normal = this.rendicontazioneAllD.azioniSistema[i].valoreSpesaFnps ? this.parsingFloat(this.rendicontazioneAllD.azioniSistema[i].valoreSpesaFnps) : this.rendicontazioneAllD.azioniSistema[i].valoreSpesaFnps;
                if (normal !== initial) {
                    this.ischangedvar = true;
                }
            }
            for (let i = 0; i < this.rendicontazioneAllD.quote.length; i++) {
                initial = this.rendicontazioneInitialAllD.quote[i].valoreSpesaFnps ? this.parsingFloat(this.rendicontazioneInitialAllD.quote[i].valoreSpesaFnps) : this.rendicontazioneInitialAllD.quote[i].valoreSpesaFnps;
                normal = this.rendicontazioneAllD.quote[i].valoreSpesaFnps ? this.parsingFloat(this.rendicontazioneAllD.quote[i].valoreSpesaFnps) : this.rendicontazioneAllD.quote[i].valoreSpesaFnps;
                if (normal !== initial) {
                    this.ischangedvar = true;
                }
            }
        }

    }

    rendicontazioneBeforeSaveTransform() {
        this.initRowTotale();
        for (let i = 0; i < this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione.length; i++) {
            for (let j = 0; j < this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione[i].utenze.length; j++) {
                if (this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione[i].utenze[j].valore) {
                    this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione[i].utenze[j].valore = this.transform(parseFloat(this.rendicontazioneBeforeSaveAllD.vociAllD.listaPrestazione[i].utenze[j].valore));
                }
            }
        }
    }

    openDialogMotivazione(testo: string) {
        if (this.nonPareggioFNPS) {
            const dialogRef = this.dialog.open(MotivazionePopupComponent, {
                width: '600px',
                disableClose: true,
                autoFocus: true,
                data: { titolo: 'Giustificazione', motivazione: testo, placeholder: "Inserire qui la giustificazione", maxlength: 600 }
            });
            dialogRef.afterClosed().subscribe(result => {
                if (result) {
                    this.rendicontazioneAllD.giustificazione = result.motivazione;
                }
            });
        }
    }

    ngAfterContentChecked(): void {
        this.changeDetector.detectChanges();
    }

    checkStatoRendicontazione() {
        switch (this.rendicontazione.statoRendicontazione.codStatoRendicontazione) {
            case RENDICONTAZIONE_STATUS.IN_RIESAME_II:
            case RENDICONTAZIONE_STATUS.ATTESA_VALIDAZIONE:
            case RENDICONTAZIONE_STATUS.VALIDATA:
            case RENDICONTAZIONE_STATUS.STORICIZZATA:
            case RENDICONTAZIONE_STATUS.CONCLUSA:
                return true;
        }
        return false;
    }

    changeKeyAzione(valore: any, i: number) {
        if (valore == "" || valore == null) {
            this.rendicontazioneAllD.azioniSistema[i].valoreSpesaFnps = null;
        }
        else {
            if (valore.indexOf('.') !== -1) {
                if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
                    valore = [valore, '0'].join('');
                this.rendicontazioneAllD.azioniSistema[i].valoreSpesaFnps = valore;
            }
            else this.rendicontazioneAllD.azioniSistema[i].valoreSpesaFnps = this.transform(parseFloat(valore.toString().replace(',', '.')));
        }
    }

    changeKeyAltre(valore: any, i: number) {
        if (valore == "" || valore == null) {
            this.rendicontazioneAllD.quote[i].valoreSpesaFnps = null;
        }
        else {
            if (valore.indexOf('.') !== -1) {
                if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2)
                    valore = [valore, '0'].join('');
                this.rendicontazioneAllD.quote[i].valoreSpesaFnps = valore;
            }
            else this.rendicontazioneAllD.quote[i].valoreSpesaFnps = this.transform(parseFloat(valore.toString().replace(',', '.')));
        }
    }


    initCalcFormula() {
        this.initSommatorie();
        for (let u of this.utenze) {
            if (u.utilizzatoPerCalcolo) {
                this.sommatoriaFnpsVincoli += (this.roundToTwo(this.parsingFloat(this.totFnpsQuoteString) * this.parsingFloat(u.valorePercentuale) / 100, 2));
            }
        }
        for (let a of this.rendicontazioneAllD.azioniSistema) {
            if (a.funzioneRegola == '+') {
                this.sommatoriaAzioniSistema += this.parsingFloat(a.valore);
            } else if (a.funzioneRegola == '-') {
                this.sommatoriaAzioniSistema -= this.parsingFloat(a.valore);
            }
        }

        this.altroFnps = this.transform(this.parsingFloat(this.totFnpsQuoteString) - this.sommatoriaFnpsVincoli + this.sommatoriaAzioniSistema);
    }

    initSommatorie() {
        this.sommatoriaFnpsVincoli = 0;
        this.sommatoriaAzioniSistema = 0;
        this.sommatoriaB1NonCalcolo = 0;
    }

    checkLepsAzioni() {
        if (this.rendicontazioneAllD) {
            return this.rendicontazioneAllD.azioniSistema.some(element => element.leps);
        } else {
            return false;
        }
    }

    checkLepsQuote() {
        if (this.rendicontazioneAllD) {
            return this.rendicontazioneAllD.quote.some(element => element.leps);
        } else {
            return false;
        }
    }
}
