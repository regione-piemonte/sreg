import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Navigation, Router} from '@angular/router';
import {CronologiaModelliComponent} from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import {GregBOClient} from '@greg-app/app/GregBOClient';
import {AppToastService} from '@greg-shared/toast/app-toast.service';
import {ERRORS, OPERAZIONE, RENDICONTAZIONE_STATUS, SECTION} from '@greg-app/constants/greg-constants';
import {forkJoin} from 'rxjs';
import {GregErrorService} from '@greg-app/shared/error/greg-error.service';
import {InfoRendicontazioneOperatore} from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { MissioniB } from '@greg-app/app/dto/MissioniB';
import { ProgrammiB } from '@greg-app/app/dto/ProgrammiB';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { RendicontazioneModB } from '@greg-app/app/dto/RendicontazioneModB';
import { RendicontazioneTotaliSpeseMissioni } from '@greg-app/app/dto/RendicontazioneTotaliSpeseMissioni';
import { RendicontazioneTotaliMacroaggregati } from '@greg-app/app/dto/RendicontazioneTotaliMacroaggregati';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { MatDialog } from '@angular/material';
import { MotivazionePopupComponent } from '../motivazione-popup/motivazione-popup.component';
import { ProgrammiMissioneTotaliModB } from '@greg-app/app/dto/ProgrammiMissioneTotaliModB';
import { EsportaModelloBGreg } from '@greg-app/app/dto/EsportaModelloBGreg';
import { MsgInformativo } from '@greg-app/app/dto/MsgInformativo';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';

@Component({
    selector: 'app-modelloB',
    templateUrl: './modelloB.component.html',
    styleUrls: ['./modelloB.component.css']
})
export class ModelloBComponent implements OnInit {

    navigation: Navigation;
    idRendicontazioneEnte;
    rendicontazioneModB: RendicontazioneModB;
    rendicontazioneModBExport: RendicontazioneModB;
    rendicontazioneModBInitial: RendicontazioneModB;

    TData: MissioniB[] = []; // dati della tabella principale

    rowTotaleSpeseCorrenti: Array<number> =[];
    totaleSpeseCorrenti: Array<string> =[];
    rowTotaleSpeseContoCapitale: Array<number> =[];
    totaleSpeseContoCapitale: Array<string> =[];
    rowTotaleSpeseIncrementoAttFinanz: Array<number> =[];
    totaleSpeseIncrementoAttFinanz: Array<string> =[];
    rowTotaleMissione: Array<number> =[];
    totaleMissione: Array<string> =[];
    rowTotaleSpeseCorrentiMissione: Array<number> =[];
    totaleSpeseCorrentiMissione: Array<string> =[];
  

    missioniModB: Array<MissioniB>;
    missioniModBTotali: Array<MissioniB>
    espansa: boolean;
    state = 'default';    
    
    totaleSpesaTutteMissioni = 0;
    totaleSpesaCorrenteMis1204 = 0;
    totaleSpesaCorrenteMis01041215 = 0;
    totaleSpesaCorrenteTutteMissioni = 0;
    totaleCompilazione = 0;

    totaleSpesaTutteMissioniS: string;
    totaleSpesaCorrenteMis1204S: string;
    totaleSpesaCorrenteMis01041215S: string;
    totaleSpesaCorrenteTutteMissioniS: string;
    totaleCompilazioneS: string;
    sommaSottotitoliTitolo1Miss4: string;
    titolo: string;
    imgTooltip:string;

    totaliModB: Array<ProgrammiMissioneTotaliModB>;
    valoreMiss12Prog1: string;
    valoreMiss12Prog2: string;
    totaleSpesaCorrente: RendicontazioneTotaliSpeseMissioni;
    totaleSpesaMissione: RendicontazioneTotaliMacroaggregati;
    diffTotSpesaC: boolean = false;
    diffTotSpesaM: boolean = false;
    diffTotSpesaCMissione1: boolean = false;
    diffSommaSottotitTitolo1Miss4: boolean = false;
    diffTotaleMiss12: boolean = false;
    diffDiCuiProg2Miss1: boolean = false;
    diffDiCuiProg7Miss12: boolean = false;
    diffDiCuiProg4Miss12: boolean = false;
    ischangedvar:boolean;
    erroreexport :string;
    colonnaTotaleAltre: Array<number> = [2, 2, 2, 1];
    inputRadioButton: boolean = false;

    warnings: string[] = [];
    errors: string[] = [];
    // ruoloente: string;
    messaggio: string;
    titoloPopUp: string;
    esito: string;
    obblMotivazione: boolean;
    warningCheck: any;

    infoOperatore: RendicontazioneEnteGreg;
    tabtranche : ModelTabTranche;
    errorMessage = {
        error: {descrizione: ''},
        message: '',
        name: '',
        status: '',
        statusText: '',
        url: '',
        date: Date
    };
    isModelloEnabled = false;
    msgModelloNotEnabled: string = '';
    descTitoliModB: MsgInformativo[] = [];
    descTotaliMissione: MsgInformativo[] = [];

    @ViewChild(CronologiaModelliComponent, {static: false}) cronologiaMod: CronologiaModelliComponent;
    @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;
    mostrasalva = true;
    infoOperatoreBeforeSave: RendicontazioneEnteGreg;
    statoInitial: string;
    rendicontazioneModBBeforeSave: RendicontazioneModB;
    rendicontazione: RendicontazioneEnteGreg;
    erroreStato: string;
    erroreModifica: string;
    totaleSpesaCorrenteBS: RendicontazioneTotaliSpeseMissioni;
    totaleSpesaMissioneBS: RendicontazioneTotaliMacroaggregati;
    totaliModBBS: ProgrammiMissioneTotaliModB[];
    datiEnte: DatiEnteGreg;
    constructor(public client: GregBOClient, private router: Router, private route: ActivatedRoute, private dialog: MatDialog,
                public toastService: AppToastService, private gregError: GregErrorService, public pulsanti:PulsantiFunzioniComponent) {
        this.navigation = this.router.getCurrentNavigation();
      	let enteValues: string[] = [];
      this.route.fragment.subscribe( (frag: string) => {
        enteValues.push(frag);
      });
         this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
        this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : enteValues[0][1];
		this.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
    }

    ngOnInit() {
        this.client.spinEmitter.emit(true);
        this.client.nomemodello=SECTION.CODMODELLOB;
        forkJoin({
            descTitoliTotaliModB: this.client.getMsgInformativi(SECTION.MODELLOBDESCTOTALI),
            descTotaliMissione: this.client.getMsgInformativi(SECTION.MODELLOBDESCTOTALIMISSIONE),
            totaleSpesaCorrente: this.client.getRendicontazioneTotaliSpese(this.rendicontazione.idRendicontazioneEnte),
            totaleSpesaMissione: this.client.getRendicontazioneTotaliMacroaggregati(this.rendicontazione.idRendicontazioneEnte),
            totaliModB : this.client.getProgrammiMissioneTotaliModB(this.rendicontazione.idRendicontazioneEnte),
            infoB: this.client.getMsgInformativi(SECTION.MODELLOBTAB),
            canActiveModB: this.client.canActiveModB(this.rendicontazione.idRendicontazioneEnte),
        }).subscribe(({descTitoliTotaliModB, descTotaliMissione, totaleSpesaCorrente, totaleSpesaMissione, totaliModB, infoB, canActiveModB}) => {
            this.descTitoliModB = descTitoliTotaliModB;
            this.descTotaliMissione = descTotaliMissione;
            this.totaliModB = totaliModB;
            this.totaleSpesaCorrente = totaleSpesaCorrente;
            this.totaleSpesaMissione = totaleSpesaMissione;
            this.msgModelloNotEnabled = infoB[0].testoMsgInformativo;
            this.isModelloEnabled = canActiveModB;
            if(canActiveModB){
                this.onInitModB();
            }
            else {
                this.client.mostrabottoniera=false;
                this.toastService.showError({text: this.msgModelloNotEnabled});
                this.client.spinEmitter.emit(false);
            }
        }, 
        err => {
            this.client.mostrabottoniera=true;
            this.client.spinEmitter.emit(false);
        });
    }

    onInitModB() {
        this.client.mostrabottoniera=false;
        this.client.spinEmitter.emit(true);
		this.client.nomemodello=SECTION.CODMODELLOB;
        forkJoin({
            missioni: this.client.getMissioniModB(),
            infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
			tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOB),
            rendicontazione: this.client.getRendicontazioneModB(this.rendicontazione.idRendicontazioneEnte),
            rendicontazioneInitial: this.client.getRendicontazioneModB(this.rendicontazione.idRendicontazioneEnte),
            totaleSpesaCorrente: this.client.getRendicontazioneTotaliSpese(this.rendicontazione.idRendicontazioneEnte),
            totaleSpesaMissione: this.client.getRendicontazioneTotaliMacroaggregati(this.rendicontazione.idRendicontazioneEnte),
            erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
            totaliModB : this.client.getProgrammiMissioneTotaliModB(this.rendicontazione.idRendicontazioneEnte),
            tooltipImg: this.client.getMsgInformativi(SECTION.MACROAGGREGATIIMGM),
            erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
            erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
        }).subscribe(({missioni, infoOperatore, tranche, rendicontazione, rendicontazioneInitial, totaleSpesaCorrente, totaleSpesaMissione,erroreexport,totaliModB, tooltipImg, erroreStato, erroreModifica}) => {
            this.initMissioniModBTotali(missioni);
            this.missioniModB = missioni;
            this.totaliModB = totaliModB;
            this.valoreMiss12Prog1 = this.totaliModB[0].valore;
            this.valoreMiss12Prog2 = this.totaliModB[1].valore;
            this.infoOperatore = infoOperatore;
            this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
            this.tabtranche = tranche;
            if(this.tabtranche!=null){
                this.titolo=this.tabtranche.desEstesaTab;
                this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
             }
           
            this.rendicontazioneModB = rendicontazione;
            this.rendicontazioneModBExport = rendicontazione;
            this.rendicontazioneModBInitial = rendicontazioneInitial;
            this.totaleSpesaCorrente = totaleSpesaCorrente;
            this.totaleSpesaMissione = totaleSpesaMissione;
            this.initiValueFromModB();
	        this.erroreexport = erroreexport.testoMsgApplicativo;
            this.imgTooltip=tooltipImg[0].testoMsgInformativo;
            this.erroreStato = erroreStato.testoMsgApplicativo;
            this.erroreModifica = erroreModifica.testoMsgApplicativo;
            this.initialTransform();
            this.TData = this.rendicontazioneModB.listaMissioni;
            this.initRowTotale();
            this.initCalc();
        
		    this.client.mostrabottoniera=true;
            this.client.spinEmitter.emit(false); 
            }, err => {
                this.client.mostrabottoniera=true;
                this.client.spinEmitter.emit(false);
            });
    }

    initMissioniModBTotali(missioni: MissioniB[]){
        this.missioniModBTotali = [];
        missioni.forEach((value, index) => {
            if(index !== 7){
                this.missioniModBTotali.push(value);
            }
        });
    }

    initiValueFromModB(){
        this.totaliModB.forEach((totale, index) => {
            this.rendicontazioneModB.listaMissioni[2].listaProgramma[index].listaTitolo[0].valore = totale.valore;
            this.rendicontazioneModBInitial.listaMissioni[2].listaProgramma[index].listaTitolo[0].valore = totale.valore;
        })
        //this.rendicontazioneModBInitial.listaMissioni[1].listaProgramma[0].listaTitolo[0].valore = this.transform(this.totaleSpesaCorrente.valoriSpese[2].totale);
    }

    setMyStyles(colore: string){
        let style = colore ? '5px solid ' + colore : 'none'
        let styles = {
            'border-left': style,
            'opacity': '1',
            'padding-left' : '5px'
        };
        return styles;
    }

    parsingFloat(el) {
        if (el == '') el = null;
        el = el ? parseFloat(el.toString().replaceAll('.','').replace(',','.')) : el;
        return el;
    }

    initRowTotale(){
        for(let i=0; i<this.TData.length; i++){
          this.rowTotaleSpeseCorrenti[i]=0;
          this.rowTotaleSpeseContoCapitale[i]=0;
          this.rowTotaleSpeseIncrementoAttFinanz[i]=0;
          this.rowTotaleMissione[i]=0;
        }

        for(let i=0; i<5; i++){
            this.rowTotaleSpeseCorrentiMissione[i] = 0;
        }
        this.totaleSpesaTutteMissioni = 0;
        this.totaleSpesaCorrenteMis01041215 = 0;
        this.totaleSpesaCorrenteMis1204 = 0;
        this.totaleSpesaCorrenteTutteMissioni = 0;
        this.totaleCompilazione = 0;
    }

    initCalc(){
        for(let i=0; i<this.rendicontazioneModB.listaMissioni.length; i++){
            for(let j=0; j<this.rendicontazioneModB.listaMissioni[i].listaProgramma.length; j++){
                for(let k=0; k<this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                    for(let l=0; l<this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                        if (this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore != null && this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore != undefined) {
                            this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore = this.transform(parseFloat(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore));
                        }
                    }
                    if (this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore != null && this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore != undefined) {
                        this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore = this.transform(parseFloat(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore));
                    }
                }
            }
        }
        this.calcTableValue();
    }

    // Calcoli effettuati ad ogni cambiamento dei valori
  calcTableValue() {
    this.initRowTotale();
    
    this.TData[2].listaProgramma[0].listaTitolo[0].valore = this.transform(parseFloat(this.valoreMiss12Prog1) - this.parsingFloat(this.rendicontazioneModB.listaMissioni[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[0].valore));
    this.TData[2].listaProgramma[1].listaTitolo[0].valore = this.transform(parseFloat(this.valoreMiss12Prog2) - this.parsingFloat(this.rendicontazioneModB.listaMissioni[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[1].valore));

    // Calcolo Valore Titolo1 Missione4 (somma dei sottotitoli relativi)
    this.sommaSottotitoliTitolo1Miss4 = this.transform(this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[0].valore) + 
    this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[1].valore));
    this.TData[1].listaProgramma[0].listaTitolo[0].valore = this.transform(this.totaleSpesaCorrente.valoriSpese[2].totale);
    this.rendicontazioneModBInitial.listaMissioni[1].listaProgramma[0].listaTitolo[0].valore = this.transform(this.totaleSpesaCorrente.valoriSpese[2].totale);
    for(let i=0; i<this.TData.length; i++){
        for(let j=0; j<this.TData[i].listaProgramma.length; j++){
            for(let k=0; k<this.TData[i].listaProgramma[j].listaTitolo.length; k++){
                if(k == 0  && this.TData[i].listaProgramma[j].listaTitolo[k].codTitolo === "1"){
                    this.rowTotaleSpeseCorrenti[i]+=this.parsingFloat(this.TData[i].listaProgramma[j].listaTitolo[k].valore);
                }
                if(k == 1  && this.TData[i].listaProgramma[j].listaTitolo[k].codTitolo === "2"){            
                    this.rowTotaleSpeseContoCapitale[i]+= this.parsingFloat(this.TData[i].listaProgramma[j].listaTitolo[k].valore);
                }
                if(k == 2  && this.TData[i].listaProgramma[j].listaTitolo[k].codTitolo === "3"){
                    this.rowTotaleSpeseIncrementoAttFinanz[i]+=this.parsingFloat(this.TData[i].listaProgramma[j].listaTitolo[k].valore);
                }
            }
        }
        this.totaleSpeseCorrenti[i]=this.transform(this.rowTotaleSpeseCorrenti[i]);
        this.totaleSpeseContoCapitale[i]=this.transform(this.rowTotaleSpeseContoCapitale[i]);
        this.totaleSpeseIncrementoAttFinanz[i]=this.transform(this.rowTotaleSpeseIncrementoAttFinanz[i]);
        this.rowTotaleMissione[i]+= this.rowTotaleSpeseCorrenti[i]+this.rowTotaleSpeseContoCapitale[i]+this.rowTotaleSpeseIncrementoAttFinanz[i];

        // Calcolo il Totale Missione (Missione 50)
        if(i == 5){
            this.rowTotaleMissione[i] = this.parsingFloat(this.TData[i].listaProgramma[0].listaTitolo[0].valore) + 
                this.parsingFloat(this.TData[i].listaProgramma[1].listaTitolo[0].valore);
        }
        // Calcolo il Totale Missione (Missione 60)
        if(i == 6){
            this.rowTotaleMissione[i] = this.parsingFloat(this.TData[i].listaProgramma[0].listaTitolo[0].valore) + 
                this.parsingFloat(this.TData[i].listaProgramma[0].listaTitolo[1].valore);
        }
        // Calcolo il Totale Missione (Missione 99)
        if(i == 7){
            this.rowTotaleMissione[i] = this.parsingFloat(this.TData[i].listaProgramma[0].listaTitolo[0].valore) + 
                this.parsingFloat(this.TData[i].listaProgramma[1].listaTitolo[0].valore);
        }
        // Calcolo il Totale Missione (Missioni ALTRE)
        if(i == 8){
            this.rowTotaleMissione[i] = 0;
            for (let titolo of this.TData[i].listaProgramma[0].listaTitolo){
                this.rowTotaleMissione[i] += this.parsingFloat(titolo.valore);
            }
        }

        this.totaleMissione[i]=this.transform(this.rowTotaleMissione[i]);

        // Calcolo Totali Spesa Correnti Missioni, Tab in Alto Grgigia
        if(i == 0){
            this.rowTotaleSpeseCorrentiMissione[0] = this.totaleSpesaCorrente.valoriSpese[i].totale;
            this.totaleSpeseCorrentiMissione[0] = this.transform(this.rowTotaleSpeseCorrentiMissione[0]);
        }
        if(i == 1){
            this.rowTotaleSpeseCorrentiMissione[1] = this.rowTotaleSpeseCorrenti[i];
            this.totaleSpeseCorrentiMissione[1] = this.transform(this.rowTotaleSpeseCorrentiMissione[1]);
        }
        if(i == 2){
            this.rowTotaleSpeseCorrentiMissione[2] = this.totaleSpesaCorrente.valoriSpese[1].totale;
            this.totaleSpeseCorrentiMissione[2] = this.transform(this.rowTotaleSpeseCorrentiMissione[2]);
        }
        if(i == 3){
            this.rowTotaleSpeseCorrentiMissione[3] = this.rowTotaleSpeseCorrenti[i];
            this.totaleSpeseCorrentiMissione[3] = this.transform(this.rowTotaleSpeseCorrentiMissione[3]);
        }
        if(i == 4){
            this.rowTotaleSpeseCorrentiMissione[4] = this.rowTotaleSpeseCorrenti[i];
            this.totaleSpeseCorrentiMissione[4] = this.transform(this.rowTotaleSpeseCorrentiMissione[4]);
        }
        if(i == 5){
            this.rowTotaleSpeseCorrentiMissione[5] = this.rowTotaleSpeseCorrenti[i];
            this.totaleSpeseCorrentiMissione[5] = this.transform(this.rowTotaleSpeseCorrentiMissione[5]);
        }
        if(i == 6){
            this.rowTotaleSpeseCorrentiMissione[6] = this.rowTotaleSpeseCorrenti[i];
            this.totaleSpeseCorrentiMissione[6] = this.transform(this.rowTotaleSpeseCorrentiMissione[6]);
        }
        if(i == 8){
            this.rowTotaleSpeseCorrentiMissione[7] = this.rowTotaleSpeseCorrenti[i];
            this.totaleSpeseCorrentiMissione[7] = this.transform(this.rowTotaleSpeseCorrentiMissione[7]);
        }
    }

    for(let value of this.rowTotaleMissione){
        this.totaleSpesaTutteMissioni += value;
    }
    for(let i=0; i<4; i++){
        this.totaleSpesaCorrenteMis01041215 += this.rowTotaleSpeseCorrenti[i];
    }
    this.totaleSpesaCorrenteMis1204 += this.rowTotaleSpeseCorrenti[2] + 
        this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[0].valore) + 
        this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[1].valore);

    for(let value of this.rowTotaleSpeseCorrenti){
        this.totaleCompilazione += value;
    }
    this.calcTotali();
    this.viewImportiWithMacroModBOthers();
  }

  calcTotali(){
    this.totaleSpesaTutteMissioniS = this.transform(this.totaleSpesaTutteMissioni);
    this.totaleSpesaCorrenteMis01041215S = this.transform(this.totaleSpesaCorrenteMis01041215);

    // Valori che derivano dal Macroaggregati
    this.totaleSpesaCorrenteMis1204S = this.transform(this.totaleSpesaMissione.valoriMacroaggregati[7].totale);
    this.totaleSpesaCorrenteTutteMissioniS = this.transform(this.totaleSpesaCorrente.valoriSpese[3].totale);

    this.totaleCompilazioneS = this.transform(this.totaleCompilazione);
  }

  viewImportiWithMacroModBOthers(){
    if(this.totaleSpesaCorrenteTutteMissioniS != this.totaleCompilazioneS){
        this.diffTotSpesaC = true;
    }
    else {
        this.diffTotSpesaC = false;
    }
    let mis1204 = this.transform(this.totaleSpesaCorrenteMis1204);
    if(this.totaleSpesaCorrenteMis1204S != mis1204){
        this.diffTotSpesaM = true;
    }
    else {
        this.diffTotSpesaM = false;
    }

    if(this.transform(this.totaleSpesaCorrente.valoriSpese[0].totale) != this.transform(this.rowTotaleSpeseCorrenti[0])){
        this.diffTotSpesaCMissione1 = true;
    }
    else {
        this.diffTotSpesaCMissione1 = false;
    }

    if(this.sommaSottotitoliTitolo1Miss4 != this.transform(this.totaleSpesaCorrente.valoriSpese[2].totale)){
        this.diffSommaSottotitTitolo1Miss4 = true;
    }
    else {
        this.diffSommaSottotitTitolo1Miss4 = false;
    }
    //Confronto Totale SpeseCorrentiMiss12 Modello B con Totale Missione12 Modello Macroaggregati
    if(this.totaleSpeseCorrenti[2] != this.transform(this.totaleSpesaCorrente.valoriSpese[1].totale)){
        this.diffTotaleMiss12 = true;
    }
    else {
        this.diffTotaleMiss12 = false;
    }    
    //confronto di cui
    if(this.parsingFloat(this.TData[0].listaProgramma[1].listaTitolo[0].valore)<this.parsingFloat(this.TData[0].listaProgramma[1].listaTitolo[0].listaSottotitolo[0].valore)){
        this.diffDiCuiProg2Miss1=true;
    } else {
        this.diffDiCuiProg2Miss1=false;
    }

    if(this.parsingFloat(this.TData[2].listaProgramma[6].listaTitolo[0].valore)<this.parsingFloat(this.TData[2].listaProgramma[6].listaTitolo[0].listaSottotitolo[0].valore)){
        this.diffDiCuiProg7Miss12=true;
    } else {
        this.diffDiCuiProg7Miss12=false;
    }

    if(this.parsingFloat(this.TData[2].listaProgramma[3].listaTitolo[0].valore)<this.parsingFloat(this.TData[2].listaProgramma[3].listaTitolo[0].listaSottotitolo[0].valore)){
        this.diffDiCuiProg4Miss12=true;
    } else {
        this.diffDiCuiProg4Miss12=false;
    }

  }

  changeKey(i:number, j:number, k:number, valore: any, l?: number) {
        if(l !== undefined){
            if(valore=="" || valore == null || valore==undefined){
                this.TData[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore=null;
                    this.calcTableValue();
            }
            else {
                if(valore.indexOf('.') != -1) {
                    if(valore.indexOf(',') != -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2){
                    valore = [valore, '0'].join('');
                    }
                    this.TData[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore=this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
                }
                else{
                    this.TData[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore= this.transform(parseFloat(valore.toString().replace(',','.')));
                }
            }    
        }
        else {
            if(valore=="" || valore == null || valore==undefined){
                this.TData[i].listaProgramma[j].listaTitolo[k].valore=null;
                    this.calcTableValue();
            }
            else {
                if(valore.indexOf('.') != -1) {
                    if(valore.indexOf(',') != -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2){
                    valore = [valore, '0'].join('');
                    }
                    this.TData[i].listaProgramma[j].listaTitolo[k].valore=this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
                }
                else{
                    this.TData[i].listaProgramma[j].listaTitolo[k].valore= this.transform(parseFloat(valore.toString().replace(',','.')));
                }
            }    
        }
    }

    onChangeRadio(value: boolean, i: number, j: number){
        this.TData[i].listaProgramma[j].flagConf = value;
        if(!value){
            this.TData[i].listaProgramma[j].motivazione = null;
        }
    }

    flag(i: number, j: number){
        return this.TData[i].listaProgramma[j].flagConf;
    }

    openDialogMotivazione(i: number, j: number){
        const dialogRef = this.dialog.open(MotivazionePopupComponent, {
            width: '600px',
            disableClose : true,
            autoFocus : true,
            data: {titolo: 'Motivazione', motivazione: this.TData[i].listaProgramma[j].motivazione, placeholder: "Inserire qui la motivazione", maxlength: 600}
         });
           dialogRef.afterClosed().subscribe(result => {
          if(result) {
            this.TData[i].listaProgramma[j].motivazione = result.motivazione;
          }
        });
    }

    salvaModifiche(event) {
        this.client.nomemodello = SECTION.CODMODELLOB;
        this.client.spinEmitter.emit(true);
        this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
            this.infoOperatoreBeforeSave = response;
            let statoBeforeSave = this.infoOperatoreBeforeSave.statoRendicontazione.codStatoRendicontazione;
            if(this.statoInitial === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_I && statoBeforeSave === RENDICONTAZIONE_STATUS.IN_RIESAME_I){
                this.errorMessage.error.descrizione  =this.erroreStato;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
                return;
              } else if(this.statoInitial === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_II && statoBeforeSave === RENDICONTAZIONE_STATUS.IN_RIESAME_II){
                this.errorMessage.error.descrizione  =this.erroreStato;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
                return;
              } else {
                let modify: boolean = false;
                forkJoin({
                    rendicontazioneBS: this.client.getRendicontazioneModB(this.rendicontazione.idRendicontazioneEnte),
                    totaleSpesaCorrenteBS: this.client.getRendicontazioneTotaliSpese(this.rendicontazione.idRendicontazioneEnte),
                    totaleSpesaMissioneBS: this.client.getRendicontazioneTotaliMacroaggregati(this.rendicontazione.idRendicontazioneEnte),
                    totaliModBBS : this.client.getProgrammiMissioneTotaliModB(this.rendicontazione.idRendicontazioneEnte),
                }).subscribe(({rendicontazioneBS, totaleSpesaCorrenteBS, totaleSpesaMissioneBS, totaliModBBS}) => {
                    this.rendicontazioneModBBeforeSave = rendicontazioneBS;
                    this.totaleSpesaCorrenteBS = totaleSpesaCorrenteBS;
                    this.totaleSpesaMissioneBS = totaleSpesaMissioneBS
                    this.totaliModBBS = totaliModBBS;
                    this.rendicontazioneBeforeSaveTransform();
                    if(this.rendicontazioneModBBeforeSave !== undefined && this.rendicontazioneModBInitial !== undefined){
                        for(let i=0; i<this.rendicontazioneModBBeforeSave.listaMissioni.length; i++){
                            for(let j=0; j<this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma.length; j++){
                                for(let k=0; k<this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                                    for(let l=0; l<this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                                        if (this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore) {
                                            modify = true;
                                        }
                                    }
                                    if(this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore){
                                        modify = true;
                                    }
                                }
                                if(this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].flagConf !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].flagConf){
                                    modify = true;
                                    }
                                if(this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].motivazione !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].motivazione){
                                    modify = true;
                                }
                            }
                        }
                    }
                    for(let i=0; i<this.totaleSpesaCorrenteBS.valoriSpese.length; i++){
                        if(this.totaleSpesaCorrenteBS.valoriSpese[i].totale !== this.totaleSpesaCorrente.valoriSpese[i].totale){
                            modify = true;
                        }
                    }
                    for(let i=0; i<this.totaleSpesaMissioneBS.valoriMacroaggregati.length; i++){
                        if(this.totaleSpesaMissioneBS.valoriMacroaggregati[i].totale !== this.totaleSpesaMissione.valoriMacroaggregati[i].totale){
                            modify = true;
                        }
                    }
                    for(let i=0; i<this.totaliModBBS.length; i++){
                        if(this.totaliModBBS[i].valore !== this.totaliModB[i].valore){
                            modify = true;
                        }
                    }
                    if(modify){
                        this.errorMessage.error.descrizione  = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                        this.client.spinEmitter.emit(false);
                        return;
                    } else {
                        if(this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].valore) !==
                            (this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[0].valore) + 
                            this.parsingFloat(this.TData[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[1].valore))
                            ){
                                this.errorMessage.error.descrizione = "La somma delle due specificazioni Missione04/Programma06/Titolo1 non deve essere diverso dal totale";
                                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc : "La somma delle due specificazioni Missione04/Programma06/Titolo1 non deve essere superiore al totale" }))
                                this.client.spinEmitter.emit(false);
                                return;
                        }

                        // Verifica presenza Motivazione se Flag NO
                        for(let i=0; i<this.rendicontazioneModB.listaMissioni.length; i++){
                            for(let j=0; j<this.rendicontazioneModB.listaMissioni[i].listaProgramma.length; j++){
                                const missione = this.rendicontazioneModB.listaMissioni[i];
                                const programma = this.rendicontazioneModB.listaMissioni[i].listaProgramma[j];
                                if(programma.flagConf && (programma.motivazione == null || programma.motivazione == "" || programma.motivazione.trim().length==0)){
                                    this.errorMessage.error.descrizione = "Non e\' stata inserita la Motivazione della mancata conformita\' al " + programma.descProgramma + " della " + missione.descMissione + ".";
                                    this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : this.errorMessage.error.descrizione }));
                                    this.client.spinEmitter.emit(false);
                                    return;
                                }
                            }
                        }

                        //if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
						if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
						&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length==0)) {
                            this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                            this.client.spinEmitter.emit(false);
                            return;
                        } else {
                            this.client.spinEmitter.emit(true);
                            this.rendicontazioneModB.profilo=this.client.profilo;
                            this.rendicontazioneModB.idRendicontazioneEnte=this.rendicontazione.idRendicontazioneEnte;
                            for(let i=0; i<this.rendicontazioneModB.listaMissioni.length; i++){
                                for(let j=0; j<this.rendicontazioneModB.listaMissioni[i].listaProgramma.length; j++){
                                    for(let k=0; k<this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                                        for(let l=0; l<this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                                            this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore = this.parsingFloat(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore);
                                        }
                                        this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore = this.parsingFloat(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore);
                                    }
                                }
                            }
                        this.client.saveModelloB(this.rendicontazioneModB, this.cronologiaMod.cronologia.notaEnte, this.cronologiaMod.cronologia.notaInterna)
                        .subscribe(
                            (data: ResponseSalvaModelloGreg) => {
                            if(data.warnings && data.warnings.length > 0 && this.client.operazione != OPERAZIONE.INVIAMODELLI){
                                for(let warn of data.warnings){
                                this.errorMessage.error.descrizione = warn;
                                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : warn }))
                                }
                            }
                            this.refillInitials();
                            this.cronologiaMod.ngOnInit();
                                this.cronologiaMod.espansa=true;
                                this.cronologiaMod.state='rotated';
                                this.cronologiaMod.apricronologia();
                                this.cronologiaMod.cronologia.notaEnte = null;
                                this.cronologiaMod.cronologia.notaInterna = null;
                    
                            //ricalcolo pulsantiera in caso di cambio stato
                            this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOB);
                    
                            this.initCalc();
                            if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                                this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                            } else if(this.client.operazione == OPERAZIONE.CHECK){
                                this.check();
                              }
                            else{
                                this.toastService.showSuccess({text: data.messaggio});
                                // this.client.spinEmitter.emit(false);
                            }
                            },
                            err => {
                            this.initCalc();
                            this.client.spinEmitter.emit(false);
                            }
                        );
                        }
                    }
                });
            }
        });

        
    }

    getMissioneSpan(missioni: MissioniB){
        return this.getColSpan(missioni.listaProgramma[0])+1;
    }

    getColonnaTotaleAltreSpan(i: number){
        return this.colonnaTotaleAltre[i];
    }

    getColSpan(programmi : ProgrammiB){
        if(programmi.listaTitolo.length>=3){
            return programmi.listaTitolo.length;
        }
        return 3;
    }

    getLunTitolo(programmi : ProgrammiB){
        return programmi.listaTitolo.length;
    }

    getColore(programmi : ProgrammiB){
        return programmi.colore;
    }

    @HostListener('document:changeTabEvent')
    checkEdited(event) {
    this.ischanged();
    if(this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
    else        document.dispatchEvent(this.client.changeTabEmitter);
    }

    refillInitials() {
        this.client.getRendicontazioneModB(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
		this.rendicontazioneModBInitial = response;
          this.initialTransform();
          this.client.spinEmitter.emit(false);
        })  
    }
      
    initialTransform() {
        for(let i=0; i<this.rendicontazioneModBInitial.listaMissioni.length; i++){
            for(let j=0; j<this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma.length; j++){
                for(let k=0; k<this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                    for(let l=0; l<this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                        if (this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore != null && this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore != undefined) {
                            this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore = this.transform(parseFloat(this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore));
                        }
                    }
                    if (this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore  != null && this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore != undefined) {
                    this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore = this.transform(parseFloat(this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore));
                    }
                }
            }
        }
        this.rendicontazioneModBInitial.listaMissioni[2].listaProgramma[0].listaTitolo[0].valore = this.transform(parseFloat(this.valoreMiss12Prog1) - this.parsingFloat(this.rendicontazioneModBInitial.listaMissioni[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[0].valore));
        this.rendicontazioneModBInitial.listaMissioni[2].listaProgramma[1].listaTitolo[0].valore = this.transform(parseFloat(this.valoreMiss12Prog2) - this.parsingFloat(this.rendicontazioneModBInitial.listaMissioni[1].listaProgramma[0].listaTitolo[0].listaSottotitolo[1].valore));
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

    isNumber(value: string | number): boolean {
        return ((value != null) &&
          (value !== '') &&
          !isNaN(Number(value.toString().replace(',', '.'))));
    }

    esportaexcel() {
		this.ischanged();
		if (this.ischangedvar) {
			this.toastService.showError({ text: this.erroreexport });
		}
		else {
            this.client.spinEmitter.emit(true);
		    let esporta: EsportaModelloBGreg = new EsportaModelloBGreg();
		    esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
            esporta.totaleSpesaTutteMissioni = this.totaleSpesaTutteMissioni;
            esporta.totaleSpesaCorrenteMis1204 = this.totaleSpesaMissione.valoriMacroaggregati[7].totale;
            esporta.totaleSpesaCorrenteMis01041215 = this.totaleSpesaCorrenteMis01041215;
            esporta.totaleSpesaCorrenteTutteMissioni = this.totaleSpesaCorrente.valoriSpese[3].totale;

            esporta.rowTotaleSpeseCorrenti = this.totaleSpeseCorrenti;
            esporta.rowTotaleSpeseContoCapitale = this.totaleSpeseContoCapitale;
            esporta.rowTotaleSpeseIncrementoAttFinanz = this.totaleSpeseIncrementoAttFinanz;
            esporta.rowTotaleMissione = this.totaleMissione;
            esporta.denominazioneEnte = this.datiEnte.denominazione;
            for(let i=0; i<this.rendicontazioneModBExport.listaMissioni.length; i++){
                for(let j=0; j<this.rendicontazioneModBExport.listaMissioni[i].listaProgramma.length; j++){
                    for(let k=0; k<this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                        for(let l=0; l<this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                            this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore = this.parsingFloat(this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore);
                        }
                        this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore = this.parsingFloat(this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore);
                    }
                }
            }

            esporta.datiB = this.rendicontazioneModBExport;
            esporta.missioniB = this.missioniModB;
            this.client.esportaModelloB(esporta).subscribe(res => {
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
                    this.initCalc();
                }
                this.toastService.showSuccess({ text: messaggio });
                this.client.spinEmitter.emit(false);
            },
            err => {
                this.initCalc();
                this.client.spinEmitter.emit(false);
            });
		}
	}

    esportaexcelIstat() {
		this.ischanged();
		if (this.ischangedvar) {
			this.toastService.showError({ text: this.erroreexport });
		}
		else {
            this.client.spinEmitter.emit(true);
		    let esporta: EsportaModelloBGreg = new EsportaModelloBGreg();
		    esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
         
            esporta.denominazioneEnte = this.datiEnte.denominazione;
            for(let i=0; i<this.rendicontazioneModBExport.listaMissioni.length; i++){
                for(let j=0; j<this.rendicontazioneModBExport.listaMissioni[i].listaProgramma.length; j++){
                    for(let k=0; k<this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                        for(let l=0; l<this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                            this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore = this.parsingFloat(this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore);
                        }
                        this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore = this.parsingFloat(this.rendicontazioneModBExport.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore);
                    }
                }
            }
            this.client.esportaIstat(esporta).subscribe(res => {
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
                    this.initCalc();
                }
                this.toastService.showSuccess({ text: messaggio });
                this.client.spinEmitter.emit(false);
            },
            err => {
                this.initCalc();
                this.client.spinEmitter.emit(false);
            });
		}
	}

	ischanged() {
        if(this.rendicontazioneModB !== undefined && this.rendicontazioneModBInitial !== undefined){
            this.ischangedvar = false;
            for(let i=0; i<this.rendicontazioneModB.listaMissioni.length; i++){
                for(let j=0; j<this.rendicontazioneModB.listaMissioni[i].listaProgramma.length; j++){
                    for(let k=0; k<this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                        for(let l=0; l<this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                            if (this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore) {
                            this.ischangedvar = true;
                            }
                        }
                        if(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore){
                            this.ischangedvar = true;
                        }
                    }
                    if(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].flagConf !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].flagConf){
                        this.ischangedvar = true;
                        }
                    if(this.rendicontazioneModB.listaMissioni[i].listaProgramma[j].motivazione !== this.rendicontazioneModBInitial.listaMissioni[i].listaProgramma[j].motivazione){
                    this.ischangedvar = true;
                    }
                }
            }
        }
	}

    rendicontazioneBeforeSaveTransform() {
        for(let i=0; i<this.rendicontazioneModBBeforeSave.listaMissioni.length; i++){
            for(let j=0; j<this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma.length; j++){
                for(let k=0; k<this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo.length; k++){
                    for(let l=0; l<this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo.length; l++){
                        if (this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore != null && this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore != undefined) {
                            this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore = this.transform(parseFloat(this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].listaSottotitolo[l].valore));
                        }
                    }
                    if (this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore  != null && this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore != undefined) {
                    this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore = this.transform(parseFloat(this.rendicontazioneModBBeforeSave.listaMissioni[i].listaProgramma[j].listaTitolo[k].valore));
                    }
                }
            }
        }
    }

    check(){
        this.client.checkModelloB(this.idRendicontazioneEnte).subscribe(
            (data: GenericResponseWarnErrGreg) => {
                this.warnings = data.warnings;
                this.errors = data.errors;
                this.messaggio = data.descrizione;
                this.titoloPopUp = "Check";
                this.esito = data.id;
                this.obblMotivazione = data.obblMotivazione;
                this.warningCheck = data.warningCheck;
                this.openDialog();
                this.client.spinEmitter.emit(false);
            },
            err => {
                this.client.spinEmitter.emit(false);
            }
        )
    }
    
    openDialog(){
        const dialogRef = this.dialog.open(MessaggioPopupComponent, {
            width: '70%',
            disableClose: true,
            autoFocus: true,
            data: { titolo: this.titoloPopUp, warnings: this.warnings, errors: this.errors, messaggio: this.messaggio, esito: this.esito, nota: "", chiudi: null, obblMotivazione: this.obblMotivazione, warningCheck: this.warningCheck, check: true}
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result.chiudi) { 
                this.client.spinEmitter.emit(true);
                let motivazione: SalvaMotivazioneCheck = new SalvaMotivazioneCheck();
                motivazione.codModello = SECTION.CODMODELLOB;
                motivazione.idRendicontazione = this.idRendicontazioneEnte;
                motivazione.nota = result.nota;
                motivazione.modello = 'Mod. B';
                this.client.saveMotivazioneCheck(motivazione).subscribe((result)=>{
                    this.toastService.showSuccess({text: result.messaggio});
                    this.cronologiaMod.ngOnInit();
                    this.cronologiaMod.espansa = false;
                    this.cronologiaMod.state = 'rotated';
                    this.cronologiaMod.apricronologia();
                    this.cronologiaMod.cronologia.notaEnte = null;
                    this.cronologiaMod.cronologia.notaInterna = null;
                    this.client.spinEmitter.emit(false);
                },err=> {
                    this.client.spinEmitter.emit(false);
                });
            }
        })
    }

}
