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
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { MatDialog } from '@angular/material';
import { RendicontazioneModF } from '@greg-app/app/dto/RendicontazioneModF';
import { PersonaleEnteF } from '@greg-app/app/dto/PersonaleEnteF';
import { ConteggiF } from '@greg-app/app/dto/ConteggiF';
import { ConteggiPersonaleF } from '@greg-app/app/dto/ConteggiPersonaleF';
import { ConteggiOreF } from '@greg-app/app/dto/ConteggiOreF';
import { EsportaModelloFGreg } from '@greg-app/app/dto/EsportaModelloFGreg';
import { parse } from 'querystring';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { CronologiaModelliArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/cronologia-modelli-archivio/cronologia-modelli-archivio.component';

@Component({
    selector: 'app-modelloF-archivio',
    templateUrl: './modelloF-archivio.component.html',
    styleUrls: ['./modelloF-archivio.component.css']
})
export class ModelloFArchivioComponent implements OnInit {

    navigation: Navigation;
    idRendicontazioneEnte;
    
    rendicontazioneModF: RendicontazioneModF;
    rendicontazioneModFInitial: RendicontazioneModF;

    TDataPersonale: PersonaleEnteF[] = []; // dati della tabella principale
    TDataOre: PersonaleEnteF[] = []; // dati della tabella principale
    conteggioP: ConteggiPersonaleF;
    conteggioO: ConteggiOreF;

    conteggiModF: ConteggiF;

    espansa: boolean;
    state = 'default';    
    
    rowTotaleP: Array<number> = [];
    totaleRP: Array<string> = [];
    colTotaleP: Array<number> = [];
    totaleCP: Array<string> = [];
    colTotaleOpP: Array<number> = [];
    totaleCOpP: Array<string> = [];
    totTotCP: number;
    totaleTotCP: string;
    totTotCOpP: number;
    totaleTotCOpP: string;

    rowTotaleO: Array<number> = [];
    totaleRO: Array<string> = [];
    colTotaleO: Array<number> = [];
    totaleCO: Array<string> = [];
    totaleOpAttO: Array<number> = [];
    totaleCOpAttO: Array<string> = [];
    colTotaleOpO: Array<number> = [];
    totaleCOpO: Array<string> = [];
    colMonteOreDip: Array<number> = [];
    monteOreDip: Array<string> = [];
    colMonteOreNonDip: Array<number> = [];
    monteOreNonDip: Array<string> = [];
    totaleColMonteOreDip: number;
    totaleMonteOreDip: string;
    totaleColMonteOreNonDip: number;
    totaleMonteOreNonDip: string;
    totTotCO: number;
    totaleTotCO: string;
    totTotCOpAttO: number;
    totaleTotCOpAttO: string;
    totTotCOpO: number;
    totaleTotCOpO: string;
    rendicontazione: RendicontazioneEnteGreg;
    
    totaleDiCui: Array<number> = [];

    titolo: string;
    msg1:string;
    msg2:string;
    msg3:string;
    msg4:string;
    msg5:string;
    tooltip: string;
    ischangedvar:boolean;
    erroreexport :string;
    err01:string;

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

    @ViewChild(CronologiaModelliArchivioComponent, {static: false}) cronologiaMod: CronologiaModelliArchivioComponent;
//    @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;
    mostrasalva = true;
    infoOperatoreBeforeSave: RendicontazioneEnteGreg;
    rendicontazioneModFBeforeSave: RendicontazioneModF;
    statoInitial: string;
    erroreStato: string;
    erroreModifica: string;
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
        this.client.mostrabottoniera=false;
        this.client.spinEmitter.emit(true);
		this.client.nomemodello=SECTION.CODMODELLOF;
        forkJoin({
            conteggi: this.client.getConteggiModF(),
            infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
			tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOF),
            rendicontazione: this.client.getRendicontazioneModF(this.rendicontazione.idRendicontazioneEnte),
            rendicontazioneInitial: this.client.getRendicontazioneModF(this.rendicontazione.idRendicontazioneEnte),
            msg1: this.client.getMsgInformativi(SECTION.MODELLOFMSG1),
            msg2: this.client.getMsgInformativi(SECTION.MODELLOFMSG2),
            msg3: this.client.getMsgInformativi(SECTION.MODELLOFMSG3),
            msg4: this.client.getMsgInformativi(SECTION.MODELLOFMSG4),
            msg5: this.client.getMsgInformativi(SECTION.MODELLOFMSG5),
            erroreexport: this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
            err01: this.client.getMsgApplicativo(ERRORS.ERROR_MODF01),
            erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
            erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
            tooltip: this.client.getMsgInformativi(SECTION.MODFTOOLTIP)
        }).subscribe(({conteggi, infoOperatore, tranche, rendicontazione, rendicontazioneInitial, msg1, msg2, msg3, msg4, msg5, erroreexport, err01, erroreStato, erroreModifica, tooltip}) => {
            this.infoOperatore = infoOperatore;
            this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
            // this.client.componinote = !this.client.readOnlyII;
            this.tabtranche = tranche;
            if(this.tabtranche!=null){
                this.titolo=this.tabtranche.desEstesaTab;
//                this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
             }
            this.rendicontazioneModF = rendicontazione;
            this.rendicontazioneModFInitial = rendicontazioneInitial;
            this.conteggiModF = conteggi;
            this.conteggioP = this.conteggiModF.conteggioPersonale;
            this.conteggioO = this.conteggiModF.conteggioOre;
            this.TDataPersonale = this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte;
            this.TDataOre = this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte;
        
            this.msg1=msg1[0].testoMsgInformativo;
            this.msg2=msg2[0].testoMsgInformativo;
            this.msg3=msg3[0].testoMsgInformativo;
            this.msg4=msg4[0].testoMsgInformativo;
            this.msg5=msg5[0].testoMsgInformativo;
            this.tooltip=tooltip[0].testoMsgInformativo;
            this.erroreStato = erroreStato.testoMsgApplicativo;
            this.erroreModifica = erroreModifica.testoMsgApplicativo;
            this.erroreexport = erroreexport.testoMsgApplicativo;
            this.err01=err01.testoMsgApplicativo;
            this.initialTransform();
            this.initCalc();
          
		    this.client.mostrabottoniera=true;
            this.client.spinEmitter.emit(false); 
        }, err => {
	       this.client.mostrabottoniera=true;
           this.client.spinEmitter.emit(false);
        });
    }
    
    initRowTotale(){
        this.totTotCOpP=0;
        this.totaleTotCOpP="0";
        this.totTotCP=0; 
        this.totaleTotCP="0";
        for(let i=0; i<this.TDataPersonale.length; i++){
            this.rowTotaleP[i]=0;
            this.totaleRP[i]="0";
            this.totaleColMonteOreDip=0;
            this.totaleColMonteOreNonDip=0;
            this.totaleMonteOreDip="0";
            this.totaleMonteOreNonDip="0";
            for(let j=0; j<this.TDataPersonale[i].listaValori.length; j++){
               this.colTotaleP[j]=0;
               this.totaleCP[j]="0";
               this.colTotaleOpP[j]=0;
               this.totaleCOpP[j]="0";
               this.monteOreDip[j]="0";
               this.monteOreNonDip[j]="0";
                 
            }
        }
        this.totTotCO=0;
        this.totaleTotCO="0";
        this.totTotCOpO=0;
        this.totaleTotCOpO="0";
        this.totTotCOpAttO=0;
        this.totaleTotCOpAttO="0";
        for(let i=0; i<this.TDataOre.length; i++){
            this.rowTotaleO[i]=0;
            this.totaleRO[i]="0";
            for(let j=0; j<this.TDataOre[i].listaValori.length; j++){
               this.colTotaleO[j]=0;
               this.totaleCO[j]="0";
               this.colTotaleOpO[j]=0;
               this.totaleCOpO[j]="0";
               this.totaleOpAttO[j]=0;
               this.totaleCOpAttO[j]="0"
            }
        }
    }

    initDiCui(){
        for(let i=5; i<this.TDataPersonale.length; i++){
            for(let j=0; j<this.TDataPersonale[i].listaValori.length; j++){
               this.totaleDiCui[j]=0;
            }
        }
    }

    initCalc(){  
        for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                if (this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) {
                    this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore).toString();
                }
            }
        }   
        for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                if (this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) {
                    this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore).toString();
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
        //conteggio personale
        for(let i=0; i<this.TDataPersonale.length-4; i++){
            for(let j=0; j<this.TDataPersonale[i].listaValori.length; j++){
                this.rowTotaleP[i]+=this.parsingInt(this.TDataPersonale[i].listaValori[j].valore);
                this.totTotCP+=this.parsingInt(this.TDataPersonale[i].listaValori[j].valore);
                for(let k=0; k<this.TDataPersonale.length-4; k++){
                    this.colTotaleP[j]+=this.parsingInt(this.TDataPersonale[k].listaValori[j].valore);
                }
                this.totaleCP[j]=this.colTotaleP[j].toString();
                this.colTotaleP[j]=0;
            }
            this.totaleRP[i]=this.rowTotaleP[i].toString();
            this.rowTotaleP[i]=0;
        }
        this.totaleTotCP=this.totTotCP.toString();
        
        //personale non dipendenti
        for(let j=0; j<this.TDataPersonale[4].listaValori.length; j++){
            this.rowTotaleP[4]+=this.parsingInt(this.TDataPersonale[4].listaValori[j].valore);
            this.colTotaleOpP[j]+=this.parsingInt(this.TDataPersonale[4].listaValori[j].valore);
            this.colTotaleOpP[j]+=this.parsingInt(this.totaleCP[j]);
        }
        this.totaleRP[4]=this.rowTotaleP[4].toString();
        this.rowTotaleP[4]=0;

        for(let j=0; j<this.TDataPersonale[4].listaValori.length; j++){
            this.totaleCOpP[j]=this.colTotaleOpP[j].toString();
            this.colTotaleOpP[j]=0;
        }
        this.totTotCOpP=this.parsingInt(this.totaleTotCP)+this.parsingInt(this.totaleRP[4]);
        this.totaleTotCOpP=this.totTotCOpP.toString();

        for(let i=5; i<this.TDataPersonale.length; i++){
            for(let j=0; j<this.TDataPersonale[i].listaValori.length; j++){
                this.rowTotaleP[i]+=this.parsingInt(this.TDataPersonale[i].listaValori[j].valore);
            }
            this.totaleRP[i]=this.rowTotaleP[i].toString();
        }

        //conteggio ore
        for(let i=0; i<this.TDataOre.length-2; i++){
            for(let j=0; j<this.TDataOre[i].listaValori.length; j++){
                this.rowTotaleO[i]+=this.parsingInt(this.TDataOre[i].listaValori[j].valore);
                this.totTotCO+=this.parsingInt(this.TDataOre[i].listaValori[j].valore);
                
                for(let k=0; k<this.TDataOre.length-2; k++){
                    this.colTotaleO[j]+=this.parsingInt(this.TDataOre[k].listaValori[j].valore);
                }
                this.totaleCO[j]=this.colTotaleO[j].toString();

                
                this.colTotaleO[j]=0;
            }
            this.totaleRO[i]=this.rowTotaleO[i].toString();
            this.rowTotaleO[i]=0;
        }
        this.totaleTotCO=this.totTotCO.toString();
        
        for(let j=0; j<this.TDataOre[5].listaValori.length; j++){
            this.rowTotaleO[5]+=this.parsingInt(this.TDataOre[5].listaValori[j].valore);
            this.colTotaleOpO[j]+=this.parsingInt(this.TDataOre[5].listaValori[j].valore);
            this.colTotaleOpO[j]+=this.parsingInt(this.totaleCO[j]);
        }
        this.totaleRO[5]=this.rowTotaleO[5].toString();
        this.rowTotaleO[5]=0;

        for(let j=0; j<this.TDataOre[5].listaValori.length; j++){
            this.totaleCOpO[j]=this.colTotaleOpO[j].toString();
            this.colTotaleOpO[j]=0;
            this.colMonteOreDip[j] = this.roundTo(this.parsingInt(this.totaleCP[j])==0 ? 0 : this.parsingInt(this.totaleCOpO[j])/this.parsingInt(this.totaleCP[j]),1);
            this.colMonteOreNonDip[j] = this.roundTo(this.parsingInt(this.TDataPersonale[4].listaValori[j].valore)==0 || this.parsingInt(this.TDataPersonale[4].listaValori[j].valore)==null ? 0 : this.parsingInt(this.TDataOre[6].listaValori[j].valore)/this.parsingInt(this.TDataPersonale[4].listaValori[j].valore),1);
            this.monteOreDip[j] = this.colMonteOreDip[j].toString();
            this.monteOreNonDip[j] = this.colMonteOreNonDip[j].toString();
            this.totaleColMonteOreDip += this.colMonteOreDip[j];
            this.totaleColMonteOreNonDip += this.colMonteOreNonDip[j];
        }
        this.totaleMonteOreDip = this.roundTo(this.totaleColMonteOreDip,1).toString();
        this.totaleMonteOreNonDip = this.roundTo(this.totaleColMonteOreNonDip,1).toString();
        this.totTotCOpO=this.parsingInt(this.totaleTotCO)+this.parsingInt(this.totaleRO[5]);
        this.totaleTotCOpO=this.totTotCOpO.toString();

        for(let j=0; j<this.TDataOre[6].listaValori.length; j++){
            this.rowTotaleO[6]+=this.parsingInt(this.TDataOre[6].listaValori[j].valore);
            this.totaleOpAttO[j]+=this.parsingInt(this.TDataOre[6].listaValori[j].valore);
            this.totaleOpAttO[j]+=this.parsingInt(this.totaleCOpO[j]);

        }
        this.totaleRO[6]=this.rowTotaleO[6].toString();
        this.rowTotaleO[6]=0;

        for(let j=0; j<this.TDataOre[5].listaValori.length; j++){
            this.totaleCOpAttO[j]=this.totaleOpAttO[j].toString();
            this.totaleOpAttO[j]=0;
        }
        this.totTotCOpAttO=this.parsingInt(this.totaleTotCOpO)+this.parsingInt(this.totaleRO[6]);
        this.totaleTotCOpAttO=this.totTotCOpAttO.toString();

  }
  
  roundTo = function(num: number, places: number) {
    const factor = 10 ** places;
    return Math.round(num * factor) / factor;
  };

  changeKey(i:number, j:number, valore: any, conteggiP?:boolean) {
        if(conteggiP){        
            if(valore=="" || valore == null || valore==undefined){
                this.TDataPersonale[i].listaValori[j].valore=null;
                this.calcTableValue();
            } else {
                this.TDataPersonale[i].listaValori[j].valore=parseInt(valore).toString();
            }
            
        } else {
            if(valore=="" || valore == null || valore==undefined){
                this.TDataOre[i].listaValori[j].valore=null;
            } else {
                this.TDataOre[i].listaValori[j].valore=parseInt(valore).toString();
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

    salvaModifiche(event) {
        this.client.nomemodello = SECTION.CODMODELLOF;
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
                this.client.getRendicontazioneModF(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
                    this.rendicontazioneModFBeforeSave = response;
                    this.rendicontazioneBeforeSaveTransform();
                    this.ischangedvar = false;
                    let initial;
                    let normal;
                    for(let i=0; i<this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte.length; i++){
                        for(let j=0; j<this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                            initial = this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore;
                            normal = this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore;
                            if (normal!==initial) {
                                modify=true;
                            }
                        }
                    }
                    for(let i=0; i<this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte.length; i++){
                        for(let j=0; j<this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                            initial = this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore;
                            normal = this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore;
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
                        this.initDiCui();
                        for(let i=5; i<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte.length; i++){
                            for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                                if(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore>this.parsingInt(this.totaleCOpP[j])){
                                    this.errorMessage.error.descrizione =  this.err01.replace("DICUI", this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].descPersonaleEnte).replace("PROFILO", this.rendicontazioneModF.conteggi.conteggioPersonale.listaProfiloProfessionale[j].descProfiloProfessionale);
                                    this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                                    this.client.spinEmitter.emit(false);
                                    return;
                                }
                            }
                            
                        }

//                        if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
							if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0]) 
							&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "")) {
                            this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                            this.client.spinEmitter.emit(false);
                            return;
                        } else {
                            this.client.spinEmitter.emit(true);
                            this.rendicontazioneModF.profilo=this.client.profilo;

                            for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte.length;i++){
                                for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                                    if(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore){
                                        this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore).toString();
                                    }
                                }
                            }

                            for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte.length;i++){
                                for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                                    if(this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore ){
                                        this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore).toString();
                                    }
                                }
                            }
                            this.rendicontazioneModF.idRendicontazioneEnte=this.rendicontazione.idRendicontazioneEnte;
                        this.client.saveModelloF(this.rendicontazioneModF, this.cronologiaMod.cronologia.notaEnte, this.cronologiaMod.cronologia.notaInterna)
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
//                            this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOF);
                    
                            this.initCalc();

                            if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                                this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                            }else if(this.client.operazione == OPERAZIONE.CHECK){
                                this.check();
                              }
                            else{
                                this.toastService.showSuccess({text: data.messaggio});
                                this.client.spinEmitter.emit(false);
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
        this.client.getRendicontazioneModF(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
          this.rendicontazioneModFInitial = response;
          this.initialTransform();
        })
    }
      
    initialTransform() {
        for(let i=0; i<this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                if (this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) {
                    this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore).toString();
                }
            }
        }
        for(let i=0; i<this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                if (this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) {
                    this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore).toString();
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
            let esporta: EsportaModelloFGreg = new EsportaModelloFGreg();
            esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
            for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte.length;i++){
                for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                    if(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore){
                        this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore).toString();
                    }
                }
            }
            for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte.length;i++){
                for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                    if(this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore){
                        this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore).toString();
                    }
                }
            }            
            esporta.rendModF = this.rendicontazioneModF;
            esporta.totaleCP = this.totaleCP;
            esporta.totaleRP = this.totaleRP;
            esporta.totaleTotCP = this.totaleTotCP;
            esporta.totaleCOpP = this.totaleCOpP;
            esporta.totaleTotCOpP = this.totaleTotCOpP;
            esporta.totaleRO = this.totaleRO;
            esporta.totaleCO = this.totaleCO;
            esporta.totaleTotCO = this.totaleTotCO;
            esporta.totaleCOpO = this.totaleCOpO;
            esporta.totaleTotCOpO = this.totaleTotCOpO;
            esporta.totaleCOpAttO = this.totaleCOpAttO;
            esporta.totaleTotCOpAttO = this.totaleTotCOpAttO;
            esporta.denominazioneEnte = this.datiEnte.denominazione;
            this.client.esportaModelloF(esporta).subscribe(res => {
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
        for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                initial = this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModFInitial.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore;
                normal = this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModF.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore;
                if (normal!==initial) {
                    this.ischangedvar=true;
                }
            }
        }
        for(let i=0; i<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                initial = this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModFInitial.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore;
                normal = this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore ? parseInt(this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) : this.rendicontazioneModF.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore;
                if (normal!==initial) {
                    this.ischangedvar=true;
                }
            }
        }
	}

    rendicontazioneBeforeSaveTransform() {
        for(let i=0; i<this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori.length; j++){
                if (this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore) {
                    this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModFBeforeSave.conteggi.conteggioPersonale.listaPersonaleEnte[i].listaValori[j].valore).toString();
                }
            }
        }
        for(let i=0; i<this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte.length; i++){
            for(let j=0; j<this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori.length; j++){
                if (this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore) {
                    this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore = parseInt(this.rendicontazioneModFBeforeSave.conteggi.conteggioOre.listaPersonaleEnte[i].listaValori[j].valore).toString();
                }
            }
        }
    }

    checkValidita(j: number, lato: boolean){
        if(lato){
            if( (this.parsingInt(this.TDataPersonale[4].listaValori[j].valore)>0 && ((this.parsingInt(this.TDataOre[6].listaValori[j].valore)==0) || (this.parsingInt(this.TDataOre[6].listaValori[j].valore)==null))) || ((this.parsingInt(this.TDataPersonale[4].listaValori[j].valore)==0 || (this.parsingInt(this.TDataPersonale[4].listaValori[j].valore)==null)) && this.parsingInt(this.TDataOre[6].listaValori[j].valore)>0) ){
                this.monteOreNonDip[j]="";
                return true;
            }
        } else {
            if((this.parsingInt(this.totaleCOpO[j])>0 && this.parsingInt(this.totaleCP[j])==0) || (this.parsingInt(this.totaleCOpO[j])==0 && this.parsingInt(this.totaleCP[j])>0)){
                this.monteOreDip[j]="";
                return true;
            }
        }
      
     
     
    }

    check(){
        this.client.checkModelloF(this.idRendicontazioneEnte).subscribe(
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
                motivazione.codModello = SECTION.CODMODELLOF;
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
