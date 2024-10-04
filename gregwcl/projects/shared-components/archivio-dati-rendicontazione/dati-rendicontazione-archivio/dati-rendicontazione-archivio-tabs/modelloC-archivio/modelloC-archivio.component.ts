import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Navigation, Router} from '@angular/router';
import {CronologiaModelliComponent} from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import {GregBOClient} from '@greg-app/app/GregBOClient';
import {AppToastService} from '@greg-shared/toast/app-toast.service';
import {ERRORS, OPERAZIONE, RENDICONTAZIONE_STATUS,  SECTION, TRANCHE} from '@greg-app/constants/greg-constants';
import {forkJoin} from 'rxjs';
import {GregErrorService} from '@greg-app/shared/error/greg-error.service';
import {InfoRendicontazioneOperatore} from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { MatDialog } from '@angular/material';
import { PrestazioniC } from '@greg-app/app/dto/PrestazioniC';
import { RendicontazioneModC } from '@greg-app/app/dto/RendicontazioneModC';
import { TargetUtenzeC } from '@greg-app/app/dto/TargetUtenzeC';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { EsportaModelloBGreg } from '@greg-app/app/dto/EsportaModelloBGreg';
import { EsportaModelloCGreg } from '@greg-app/app/dto/EsportaModelloCGreg';
import { ThrowStmt } from '@angular/compiler';
import { StringMap } from '@angular/compiler/src/compiler_facade_interface';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';
import { CronologiaModelliArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/cronologia-modelli-archivio/cronologia-modelli-archivio.component';

@Component({
    selector: 'app-modelloC-archivio',
    templateUrl: './modelloC-archivio.component.html',
    styleUrls: ['./modelloC-archivio.component.css'],
    animations: [
        // Each unique animation requires its own trigger. The first argument of the trigger function is the name
        trigger('rotatedState', [
          state('rotated', style({ transform: 'rotate(0)' })),
          state('default', style({ transform: 'rotate(-180deg)' })),   
          transition('default => rotated', animate('100ms ease-in')),
          transition('rotated => default', animate('100ms ease-out'))
      ])
    ]
})
export class ModelloCArchivioComponent implements OnInit {

    navigation: Navigation;
    idRendicontazioneEnte;
    rendicontazioneModC: RendicontazioneModC;
    rendicontazioneModCInitial: RendicontazioneModC;
    rendicontazioneModCExport: RendicontazioneModC;

    TData: PrestazioniC[] = []; // dati della tabella principale

    prestazioniModC: Array<PrestazioniC>;

    espansa: boolean;
    state = 'default';    
    
    totale: Array<number> = [];
    totaleDettagliUtenza: Array<number> = [];
    totaleDisabilita: Array<number> = [];
    totaleDettagliDisabilita: Array<number> = [];
    totaleUtenza: Array<string> = [];

    infoModC: string;
    infoModCSplitted: Array<string> = [];
    infoDisab: string;
    infoTotDisab: string;
    infoTotDisabAdulti: string;
    R_A_1_1: string;
    R_A_2_1: string;
    noPrest: string;
    err01:string;
    err02:string;
    err03:string;
    err04:string;
    err05:string;
    err06:string;
    err07:string;
    err08:string;
    err09:string;
    err10:string;

    ischangedvar:boolean;
    erroreexport :string;
    titolo: string;

    infoOperatore: RendicontazioneEnteGreg;
    tabtranche : ModelTabTranche;
    statoInitial: string;
    infoOperatoreBeforeSave: RendicontazioneEnteGreg;

    warnings: string[] = [];
    errors: string[] = [];
    // ruoloente: string;
    messaggio: string;
    titoloPopUp: string;
    esito: string;
    obblMotivazione: boolean;
    warningCheck: any;

    errorMessage = {
        error: {descrizione: ''},
        message: '',
        name: '',
        status: '',
        statusText: '',
        url: '',
        date: Date
    };

    @ViewChild(CronologiaModelliArchivioComponent, {static: false}) cronologiaMod: CronologiaModelliArchivioComponent;
//    @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;
    mostrasalva = true;
    rendicontazioneModCBeforeSave: RendicontazioneModC;
    erroreStato: string;
    erroreModifica: string;
  datiEnte: DatiEnteGreg;
rendicontazione:RendicontazioneEnteGreg;
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
        this.client.mostrabottoniera=false;
        this.client.spinEmitter.emit(true);
		this.client.nomemodello=SECTION.CODMODELLOC;
        forkJoin({
            prestazioni: this.client.getPrestazioniModC(this.rendicontazione.idRendicontazioneEnte),
            infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
			tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOC),
            rendicontazione: this.client.getRendicontazioneModC(this.rendicontazione.idRendicontazioneEnte),
            rendicontazioneInitial: this.client.getRendicontazioneModC(this.rendicontazione.idRendicontazioneEnte),
            info: this.client.getMsgInformativi(SECTION.MODELLOCINFO),
            infoDisab: this.client.getMsgInformativi(SECTION.MODELLOCINFODISABILITA),
            infoTotDisab: this.client.getMsgInformativi(SECTION.MODELLOCINFOTOTDISABILITA),
            infoTotDisabAdulti: this.client.getMsgInformativi(SECTION.MODELLOCINFOTOTDISABILITAADULTI),
            R_A_1_1: this.client.getMsgInformativi(SECTION.MODELLOC_R_A_1_1),
            R_A_2_1: this.client.getMsgInformativi(SECTION.MODELLOC_R_A_2_1),
            noPrest: this.client.getMsgInformativi(SECTION.MODELLOC_TAB),
            err01: this.client.getMsgApplicativo(ERRORS.ERROR_MODC01),
            err02: this.client.getMsgApplicativo(ERRORS.ERROR_MODC02),
            err03: this.client.getMsgApplicativo(ERRORS.ERROR_MODC03),
            err04: this.client.getMsgApplicativo(ERRORS.ERROR_MODC04),
            err05: this.client.getMsgApplicativo(ERRORS.ERROR_MODC05),
            err06: this.client.getMsgApplicativo(ERRORS.ERROR_MODC06),
            err07: this.client.getMsgApplicativo(ERRORS.ERROR_MODC07),
            err08: this.client.getMsgApplicativo(ERRORS.ERROR_MODC08),
            err09: this.client.getMsgApplicativo(ERRORS.ERROR_MODC09),
            err10: this.client.getMsgApplicativo(ERRORS.ERROR_MODC10),
            erroreexport: this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
            erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
            erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
        }).subscribe(({prestazioni, infoOperatore, tranche, rendicontazione, rendicontazioneInitial, info, infoDisab, err01, err02, err03, err04, err05, err06, infoTotDisab, infoTotDisabAdulti, erroreexport, R_A_1_1, R_A_2_1, noPrest, erroreModifica, erroreStato, err07, err08, err09, err10}) => {
            this.noPrest=noPrest[0].testoMsgInformativo;
            this.prestazioniModC = prestazioni;
            this.infoOperatore = infoOperatore;
            this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
            this.tabtranche = tranche;
            if(this.tabtranche!=null){
                this.titolo=this.tabtranche.desEstesaTab;
//                this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
             }
            this.rendicontazioneModC = rendicontazione;
            this.rendicontazioneModCInitial = rendicontazioneInitial;
            this.rendicontazioneModCExport = rendicontazione;
            this.infoModC=info[0].testoMsgInformativo;
            this.initInfoModC();
            this.infoDisab=infoDisab[0].testoMsgInformativo;
            this.infoTotDisab=infoTotDisab[0].testoMsgInformativo;
            this.infoTotDisabAdulti=infoTotDisabAdulti[0].testoMsgInformativo;
            this.R_A_1_1=R_A_1_1[0].testoMsgInformativo;
            this.R_A_2_1=R_A_2_1[0].testoMsgInformativo;
            this.err01=err01.testoMsgApplicativo;
            this.err02=err02.testoMsgApplicativo;
            this.err03=err03.testoMsgApplicativo;
            this.err04=err04.testoMsgApplicativo;
            this.err05=err05.testoMsgApplicativo;
            this.err06=err06.testoMsgApplicativo;
            this.err07=err07.testoMsgApplicativo;
            this.err08=err08.testoMsgApplicativo;
            this.err09=err09.testoMsgApplicativo;
            this.err10=err10.testoMsgApplicativo;
            this.erroreStato = erroreStato.testoMsgApplicativo;
            this.erroreModifica = erroreModifica.testoMsgApplicativo;
            this.erroreexport = erroreexport.testoMsgApplicativo;
            this.TData = this.rendicontazioneModC.listaPrestazioni;
            this.initialTransform();
            this.defaultState();
            this.initCalc();
            this.client.mostrabottoniera=true;
                this.client.spinEmitter.emit(false); 
            }, err => {
            this.client.mostrabottoniera=true;
            this.client.spinEmitter.emit(false);
            });
    }

    initInfoModC(){
        let s1=this.infoModC.substr(0, 6);
        let s2=this.infoModC.substr(6, 15);
        let s3=this.infoModC.substr(21, 5);
        let s4=this.infoModC.substr(26, 15);
        let s5=this.infoModC.substr(41, 43);
        let s6=this.infoModC.substr(84, 20);
        let s7=this.infoModC.substr(104, 1);

        this.infoModCSplitted.push(s1, s2, s3, s4, s5, s6, s7);
    }


    defaultState(){
        for(let i=0; i<this.TData.length; i++){
           for(let j=0; j<this.TData[i].listaTargetUtenze.length; j++){
               this.TData[i].listaTargetUtenze[j].rotate='default';
               this.TData[i].listaTargetUtenze[j].showDisabilita=false;
           }
        }
    }

    espandiPart2(target: TargetUtenzeC) {
            target.showDisabilita = !target.showDisabilita;
            target.rotate = (target.rotate === 'default' ? 'rotated' : 'default'); 
    }

    initRowTotale(){
        for(let i=0; i<this.TData.length; i++){
            this.totale[i]=0;
            for(let j=0; j<this.TData[i].listaTargetUtenze.length; j++){
                
                this.totaleDisabilita[j]=0;
                for(let k=0; k<this.TData[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                    this.totaleDettagliUtenza[k]=0;
                }
                for(let k=0; k<this.TData[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                    this.totaleDettagliDisabilita[k]=0;
                }
            }
        }
    }

    initCalc(){     
        for(let i=0; i<this.rendicontazioneModC.listaPrestazioni.length; i++){
            for(let j=0; j<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze.length; j++){
                this.totaleDisabilita[j]=0;
                for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                    if (this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) {
                        this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore).toString();
                    }
                }
                for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                    for(let l=0; l<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                        if (this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) {
                            this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore).toString();
                            if(k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length-1){
                                 this.totaleDisabilita[j]+=parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore);
                            }
                        }
                    }
                    if (this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) {
                        this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore).toString();
                    }
                }
                if (this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore) {
                    if(i==1 && (j==1 || j==3)){
                        this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore = this.totaleDisabilita[j].toString();
                    }else{
                        this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore).toString();
                    }
                }
            }
        } 
        this.calcTableValue();
    }

    parsingInt(el) {
        if (el == '') el = null;
        el = el ? parseInt(el.toString()) : el;
        return el;
    }

    // Calcoli effettuati ad ogni cambiamento dei valori
    calcTableValue() {     
        this.initRowTotale();
        for(let i=0; i<this.TData.length; i++){
            for(let j=0; j<this.TData[i].listaTargetUtenze.length-1; j++){
                for(let k=0;k<this.TData[i].listaTargetUtenze[j].listaDisabilita.length-1; k++){
                    this.totaleDisabilita[j]+=this.parsingInt(this.TData[i].listaTargetUtenze[j].listaDisabilita[k].valore);
                }
                if(i==1 && (j==1 || j==3)){
                    this.TData[i].listaTargetUtenze[j].valore = this.totaleDisabilita[j].toString();
                }
                this.totale[i]+=this.parsingInt(this.TData[i].listaTargetUtenze[j].valore);
            }
            this.totaleUtenza[i]=this.totale[i].toString();
            this.totale[i]=0;
        }

  }
  
  infoDisabilita(target: TargetUtenzeC){
      
      let messaggio=this.infoDisab.replace("TARGET1", target.descTargetUtenze).replace("TARGET2", target.descTargetUtenze);
      return messaggio;
  }

  infoTotDisabilita(target: TargetUtenzeC){
    let messaggio=this.infoTotDisab.replace("TARGET", target.descTargetUtenze);
    return messaggio;
  }

  infoTotDisabilitaAdulti(target: TargetUtenzeC){
    let messaggio=this.infoTotDisabAdulti.replace("TARGET", target.descTargetUtenze);
    return messaggio;
  }

  changeKey(i:number, j:number, valore: any, k?:number, l?: number, disa?:boolean) {
        if(k!==undefined){
            if(disa){
                if(l!==undefined){
                    if(valore=="" || valore == null || valore==undefined){
                        this.TData[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore=null;
                    } else {
                        this.TData[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore=parseInt(valore).toString();
                    }
                } else {
                    if(valore=="" || valore == null || valore==undefined){
                        this.TData[i].listaTargetUtenze[j].listaDisabilita[k].valore=null;
                    } else {
                        this.TData[i].listaTargetUtenze[j].listaDisabilita[k].valore=parseInt(valore).toString();
                    }
                }
            } else {
                if(valore=="" || valore == null || valore==undefined){
                    this.TData[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore=null;
                } else {
                    this.TData[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore=parseInt(valore).toString();
                }
            }
        } else {
            if(valore=="" || valore == null || valore==undefined){
                this.TData[i].listaTargetUtenze[j].valore=null;
            } else {
                this.TData[i].listaTargetUtenze[j].valore=parseInt(valore).toString();
            }
        }
    }

    numberOnly(event): boolean {
        const charCode = (event.which) ? event.which : event.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
          return false;
        } 
        if (event.target.value.length<=9 && (event.target.selectionEnd - event.target.selectionStart)>0) {
            return true;
        }
        if (event.target.value.length>=9) {
            return false;
        }
       
        return true;
    }

        //todo: inserire messaggio errore in db per controllo salvataggi diversi in contemporanea
    salvaModifiche(event) {
        this.client.nomemodello = SECTION.CODMODELLOC;
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
                let initial;
                let normal;
                this.client.getRendicontazioneModC(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
                    this.rendicontazioneModCBeforeSave = response;
                    this.rendicontazioneBeforeSaveTransform();
                    for(let i=0; i<this.rendicontazioneModCBeforeSave.listaPrestazioni.length; i++){
                        for(let j=0; j<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze.length; j++){
                            for(let k=0; k<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                                initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore ? parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) : this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore;
                                normal = this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore ? parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) : this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore;
                                if (normal!==initial) {
                                    modify=true;
                                }
                            }
                            for(let k=0; k<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                                for(let l=0; l<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                                    initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore ? parseInt( this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) :  this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore;
                                    normal = this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore ? parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) : this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore;
                                    if (normal!==initial) {
                                        modify=true;
                                    }
                                }
                                initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore ? parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) : this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore;
                                normal = this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore ? parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) : this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore;
                                if (normal!==initial) {
                                    modify=true;
                                }
                            }
                            initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore ? parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore) : this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore;
                            normal = this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore ? parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore) : this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore;
                            if (normal!==initial) {
                                modify=true;
                            }
                        }
                    }

                    if(modify){
                        this.errorMessage.error.descrizione  = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                        this.client.spinEmitter.emit(false);
                        return;
                    } else {
                        //Controlli
                        this.initRowTotale();
                        for(let i=0; i<this.rendicontazioneModC.listaPrestazioni.length; i++){
                            for(let j=0; j<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze.length; j++){
                                if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].codTargetUtenze!="U28"){
                                    this.totale[i]+=this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore);
                                }
                                for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                                    // this.totaleDettagliUtenza[j]+=this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore);
                                    //Controllo "di cui" target utenza
                                    if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].codTargetUtenze!="U28"){
                                        this.totaleDettagliUtenza[k]+=this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore);
                                    }
                                    if(this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore)>this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore)){
                                        this.errorMessage.error.descrizione =  this.errorMessage.error.descrizione =  this.errorMessage.error.descrizione =  this.err05.replace("DICUI", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].descDettagliUtenze).replace("TARGET", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].descTargetUtenze).replace("PRESTAZIONE", this.rendicontazioneModC.listaPrestazioni[i].descPrestazione);
                                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                        this.client.spinEmitter.emit(false);
                                        return;
                                    }
                                }
                                if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].codTargetUtenze=="U28"){
                                    for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                                        //nuovo errore, vedere messaggio
                                        if(this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore)>this.totaleDettagliUtenza[k]){
                                            this.errorMessage.error.descrizione = this.err09.replace("DETTAGLI", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].descDettagliUtenze).replace("PRESTAZIONE", this.rendicontazioneModC.listaPrestazioni[i].descPrestazione);
                                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                            this.client.spinEmitter.emit(false);
                                            return;
                                        }
                                        //nuovo errore, vedere messaggio
                                    
                                        if((this.totaleDettagliUtenza[k]>0  && (this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore)==0 || this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore)==null)) || (this.totaleDettagliUtenza[k]==0  && this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore)>0)){
                                            this.errorMessage.error.descrizione =  this.err10.replace("DETTAGLI", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].descDettagliUtenze).replace("PRESTAZIONE", this.rendicontazioneModC.listaPrestazioni[i].descPrestazione);
                                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                            this.client.spinEmitter.emit(false);
                                            return;
                                        }
                                    }
                                }
                                
                                for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length-1; k++){
                                    this.totaleDisabilita[j]+=this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore);
                                    for(let l=0; l<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                                        this.totaleDettagliDisabilita[k]+=this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore);
                                    }
                                    //Controllo "di cui" disabilita
                                    if(i==1 && (j==1 || j==3) && k<3 && this.totaleDettagliDisabilita[k]>this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore)){
                                        this.errorMessage.error.descrizione = this.errorMessage.error.descrizione =  this.err04.replace("DISABILITA", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].descDisabilita).replace("TARGET", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].descTargetUtenze);
                                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                        this.client.spinEmitter.emit(false);
                                        return;
                                    }
                                    this.totaleDettagliDisabilita[k]=0;
                                }
                                // //Controllo totale disabilita con target utenza
                                // let valueConf = this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore===null ? 0 : this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore);
                                //     if(i==1 && (j==1 || j==3) && this.totaleDisabilita[j]!=valueConf){
                                //         this.errorMessage.error.descrizione =  this.errorMessage.error.descrizione =  this.err03.replace("TARGET1", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].descTargetUtenze).replace("TARGET2", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].descTargetUtenze);
                                //         this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                //           this.client.spinEmitter.emit(false);
                                             // return;
                                //     }
                                let sommaNuclei = this.parsingInt(this.rendicontazioneModC.listaPrestazioni[1].listaTargetUtenze[1].listaDisabilita[7].valore)+this.parsingInt(this.rendicontazioneModC.listaPrestazioni[1].listaTargetUtenze[3].listaDisabilita[7].valore);

                                if(sommaNuclei>this.parsingInt(this.rendicontazioneModC.listaPrestazioni[1].listaTargetUtenze[6].valore)){
                                    this.errorMessage.error.descrizione = this.errorMessage.error.descrizione =  this.err06;
                                        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                        this.client.spinEmitter.emit(false);
                                        return;
                                }
                                
                                
                                //Controllo nuclei familiari disabilita
                                if(i==1 && (j==1 || j==3) && this.totaleDisabilita[j]<this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[7].valore)){
                                    this.errorMessage.error.descrizione =  this.errorMessage.error.descrizione =  this.err02.replace("TARGET", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].descTargetUtenze);
                                    this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                    this.client.spinEmitter.emit(false);
                                    return;
                                }
                                if(i==1 && (j==1 || j==3) && ((this.totaleDisabilita[j]>0 && (this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[7].valore)==0 || this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[7].valore)==null )) || (this.totaleDisabilita[j]==0 && this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[7].valore)>0))){
                                    this.errorMessage.error.descrizione =  this.errorMessage.error.descrizione =  this.err08.replace("TARGET", this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].descTargetUtenze);
                                    this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                    this.client.spinEmitter.emit(false);
                                    return;
                                }
                            }
                            //Controllo nuclei familiari target utenza
                            if(this.totale[i]<this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[6].valore)){
                                this.errorMessage.error.descrizione =  this.err01.replace("PRESTAZIONE", this.rendicontazioneModC.listaPrestazioni[i].descPrestazione);
                                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                this.client.spinEmitter.emit(false);
                                return;
                            }
                            //nuovo errore, vedere messaggio
                            if((this.totale[i]>0  && (this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[6].valore)==0 || this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[6].valore)==null)) || (this.totale[i]==0 && this.parsingInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[6].valore)>0)){
                                this.errorMessage.error.descrizione = this.err07.replace("PRESTAZIONE", this.rendicontazioneModC.listaPrestazioni[i].descPrestazione);
                                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                this.client.spinEmitter.emit(false);
                                return;
                            }

                        }

        //                if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
							if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
							&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "")) {
                            this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                            this.client.spinEmitter.emit(false);
                            return;
                        } else {
                            this.rendicontazioneModC.profilo=this.client.profilo;

                            for(let i=0; i<this.rendicontazioneModC.listaPrestazioni.length; i++){
                                for(let j=0; j<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze.length; j++){
                                    for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                                        if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore){
                                            this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore).toString();
                                        }
                                    }
                                    for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                                        for(let l=0; l<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                                            if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore){
                                                this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore).toString();
                                            }
                                        }
                                        if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore){
                                            this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore).toString();
                                        }
                                    }
                                    if(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore){
                                        this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore = parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore).toString();
                                    }
                                }
                            }
                            this.rendicontazioneModC.idRendicontazioneEnte=this.rendicontazione.idRendicontazioneEnte;
                        this.client.saveModelloC(this.rendicontazioneModC, this.cronologiaMod.cronologia.notaEnte, this.cronologiaMod.cronologia.notaInterna)
                        .subscribe(
                            (data: ResponseSalvaModelloGreg) => {
                            if(data.warnings && data.warnings.length > 0){
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
//                            this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOC);
                    
                            this.initCalc();
                            if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                                this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                            }else if(this.client.operazione == OPERAZIONE.CHECK){
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

    @HostListener('document:changeTabEvent')
    checkEdited(event) {
        this.ischanged();
        if(this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
        else        document.dispatchEvent(this.client.changeTabEmitter);
    }

    refillInitials() {
        this.client.getRendicontazioneModC(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
          this.rendicontazioneModCInitial = response;
          this.initialTransform();
          this.client.spinEmitter.emit(false);
        })
    }
      
    initialTransform() {
        this.initRowTotale();
        for(let i=0; i<this.rendicontazioneModCInitial.listaPrestazioni.length; i++){
            for(let j=0; j<this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze.length; j++){
                for(let k=0; k<this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                    if (this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) {
                        this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore = parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore).toString();
                    }
                }
                for(let k=0; k<this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                    for(let l=0; l<this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                        if (this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) {
                            this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore = parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore).toString();
                        }
                    }
                    if (this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) {
                        this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore = parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore).toString();
                        if(k<this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length-1){
                            this.totaleDisabilita[j]+=parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore);
                        }
                    }
                }
                if(i==1 && (j==1 || j==3)){
                    this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore = this.totaleDisabilita[j].toString();
                } else{
                    if (this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore) {
                         this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore = parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore).toString();
                    }
                }
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
		    let esporta: EsportaModelloCGreg = new EsportaModelloCGreg();
		    esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
            esporta.totaleUtenti = this.totaleUtenza;
            esporta.denominazioneEnte = this.datiEnte.denominazione;
            
            for(let i=0; i<this.rendicontazioneModCExport.listaPrestazioni.length; i++){
                for(let j=0; j<this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze.length; j++){
                    this.totaleDisabilita[j]=0;
                    if(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].valore){
                        this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].valore = parseInt(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].valore).toString();
                    }
                    for(let k=0; k<this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                        if(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore){
                            this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore = parseInt(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore).toString();
                        }
                    }
                    for(let k=0; k<this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                        if(k<this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length-1){
                             this.totaleDisabilita[j]+=this.parsingInt(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore);
                        }
                        if(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore){
                            this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore = parseInt(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore).toString(); 
                        }
                        for(let l=0; l<this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                            if(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore){
                                this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore = parseInt(this.rendicontazioneModCExport.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore).toString();
                            }
                        }
                    }
                }
            }

            esporta.totaleMinori = this.totaleDisabilita[1].toString();
            esporta.totaleAdulti = this.totaleDisabilita[3].toString();
            this.totaleDisabilita[1]=0;
            this.totaleDisabilita[3]=0;
            
            esporta.datiC = this.rendicontazioneModCExport;
            esporta.prestazioniC = this.prestazioniModC;
            this.client.esportaModelloC(esporta).subscribe(res => {
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
		this.ischangedvar = false;
        let initial;
        let normal;
        for(let i=0; i<this.rendicontazioneModC.listaPrestazioni.length; i++){
            for(let j=0; j<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze.length; j++){
                for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                    initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore ? parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) : this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore;
                    normal = this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore ? parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) : this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore;
                    if (normal!==initial) {
                        this.ischangedvar=true;
                    }
                }
                for(let k=0; k<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                    for(let l=0; l<this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                        initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore ? parseInt( this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) :  this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore;
                        normal = this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore ? parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) : this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore;
                        if (normal!==initial) {
                            this.ischangedvar=true;
                        }
                    }
                    initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore ? parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) : this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore;
                    normal = this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore ? parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) : this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore;
                    if (normal!==initial) {
                        this.ischangedvar=true;
                    }
                }
                initial = this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore ? parseInt(this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore) : this.rendicontazioneModCInitial.listaPrestazioni[i].listaTargetUtenze[j].valore;
                normal = this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore ? parseInt(this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore) : this.rendicontazioneModC.listaPrestazioni[i].listaTargetUtenze[j].valore;
                if (normal!==initial) {
                    this.ischangedvar=true;
                }
            }
        }
	}

    rendicontazioneBeforeSaveTransform() {
        this.initRowTotale();
        for(let i=0; i<this.rendicontazioneModCBeforeSave.listaPrestazioni.length; i++){
            for(let j=0; j<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze.length; j++){
                for(let k=0; k<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze.length; k++){
                    if (this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore) {
                        this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore = parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDettagliUtenze[k].valore).toString();
                    }
                }
                for(let k=0; k<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length; k++){
                    for(let l=0; l<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita.length; l++){
                        if (this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore) {
                            this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore = parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].listaDettagliDisabilita[l].valore).toString();
                        }
                    }
                    if (this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore) {
                        this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore = parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore).toString();
                        if(k<this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita.length-1){
                            this.totaleDisabilita[j]+=parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].listaDisabilita[k].valore);
                        }
                    }
                }
                if(i==1 && (j==1 || j==3)){
                    this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore = this.totaleDisabilita[j].toString();
                } else{
                    if (this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore) {
                         this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore = parseInt(this.rendicontazioneModCBeforeSave.listaPrestazioni[i].listaTargetUtenze[j].valore).toString();
                    }
                }
            }
        }
    }

    check(){
        this.client.checkModelloC(this.idRendicontazioneEnte).subscribe(
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
                motivazione.codModello = SECTION.CODMODELLOC;
                motivazione.idRendicontazione = this.idRendicontazioneEnte;
                motivazione.nota = result.nota;
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
