import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { forkJoin } from 'rxjs';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ERRORS, OPERAZIONE,   RENDICONTAZIONE_STATUS,   SECTION } from '@greg-app/constants/greg-constants';
import { Macroaggregati } from '@greg-app/app/dto/Macroaggregati';
import { SpesaMissione } from '@greg-app/app/dto/SpesaMissione';
import { RendicontazioneMacroaggregati } from '@greg-app/app/dto/RendicontazioneMacroaggregati';
import { MacroaggregatiRend } from '@greg-app/app/dto/MacroaggregatiRend';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { MatDialog } from '@angular/material';
import { InfoPopup } from './info-popup/info-popup.component';
import { EsportaMacroaggregatiGreg } from '@greg-app/app/dto/EsportaMacroaggregatiGreg';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';


@Component({	
  selector: 'app-macroaggregati',
  templateUrl: './macroaggregati.component.html',
  styleUrls: ['./macroaggregati.component.css']
})
export class MacroaggregatiComponent implements OnInit {

  navigation: Navigation;
  
  @ViewChild(CronologiaModelliComponent, {static: false}) cronologiaMod: CronologiaModelliComponent;
  @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;

  visible: boolean = false;

  idRendicontazioneEnte;

  columnsHeader:Array<string>=['SPESE CORRENTE - di cui IMPEGNI Esercizio finanziario', 'Macroaggregati PER RACCORDO con MOD B1']
  columnsMacroaggregatiData: Array<Macroaggregati>; 
  columnsMacroaggregati: Array<string> = [];
  columnsCodifica: Array<string> = []; // Lista dei nomi delle colonne x tabella
  rowSpesaMissione: Array<SpesaMissione> = [];
  columnTotale: Array<number> =[];
  columnTotaleS: Array<number> =[];
  rowTotale: Array<number> =[];
  totaleR: Array<string> = [];
  totaleC: Array<string> = [];
  totaleCS: Array<string> = [];
  totale: number = 0;
  totaleS: number = 0;
  messaggioTooltip: string;
  imgTooltip: string;
  messaggioPopup: string;
  titolo: string;
  

  rendicontazioneMacroaggregati: RendicontazioneMacroaggregati;  
  rowSpan: Array<number> = [];
  columnSpanH: Array<number> = [];
  columnSpanM: Array<number> = [];

  TData: MacroaggregatiRend[] = []; // dati della tabella principale
   tabtranche : ModelTabTranche;
  espansa:boolean = false;
  state: string = 'default';

  enteValues: string[] = [];
   ischangedvar:boolean;
   erroreexport :string;
  errorMessage = { 
    error: {descrizione:''},
    message:'',
    name:'',
    status:'',
    statusText:'',
    url:'',
    date: Date
  }

  rendicontazioneMacroaggregatiInitial: RendicontazioneMacroaggregati;
  infoOperatore: RendicontazioneEnteGreg;
  rendicontazione: RendicontazioneEnteGreg;
  mostrasalva: boolean = true;
  infoOperatoreBeforeSave: RendicontazioneEnteGreg;
  statoInitial: string;
  rendicontazioneMacroaggregatiBeforeSave: RendicontazioneMacroaggregati;
  erroreModifica: string;
  erroreStato: string;
  datiEnte: DatiEnteGreg;

  warnings: string[] = [];
  errors: string[] = [];
  // ruoloente: string;
  messaggio: string;
  titoloPopUp: string;
  esito: string;
  obblMotivazione: boolean;
  warningCheck: any;

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
	  this.client.nomemodello=SECTION.CODMODELLOMACRO;
    forkJoin({
      Macroaggregati: this.client.getMacroaggregati(),
      SpesaMissione: this.client.getSpesaMissione(),
      rendicontazione: this.client.getRendicontazioneMacroaggregati(this.rendicontazione.idRendicontazioneEnte),
      rendicontazioneInitial: this.client.getRendicontazioneMacroaggregati(this.rendicontazione.idRendicontazioneEnte),
      infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
      tooltip: this.client.getMsgInformativi(SECTION.MACROAGGREGATITOOLTIP),
      tooltipImg: this.client.getMsgInformativi(SECTION.MACROAGGREGATIIMGM),
      popUp: this.client.getMsgInformativi(SECTION.MACROAGGREGATIPOPUP),
      tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOMACRO),
      erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
      erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
      erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
    })
    .subscribe(({Macroaggregati, SpesaMissione, rendicontazione, rendicontazioneInitial, tooltip, tooltipImg, popUp, infoOperatore,tranche,erroreexport, erroreModifica, erroreStato}) => {
      this.columnsMacroaggregatiData = Macroaggregati as Macroaggregati[];
      this.rowSpesaMissione = SpesaMissione as SpesaMissione[];
      this.rendicontazioneMacroaggregati = rendicontazione;
      this.rendicontazioneMacroaggregatiInitial = rendicontazioneInitial;
      this.messaggioTooltip = tooltip[0].testoMsgInformativo;
      this.imgTooltip=tooltipImg[0].testoMsgInformativo;
      this.messaggioPopup=popUp[0].testoMsgInformativo;
      this.initialTransform();
      this.infoOperatore = infoOperatore;
      this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
      this.initColumns(this.columnsMacroaggregatiData);
      this.initCodifica(this.columnsMacroaggregatiData);
      this.initColumnSpanH();
      this.initColumnSpanM(this.columnsMacroaggregati);
      this.initRowSpan(this.rowSpesaMissione, this.columnsCodifica);
      this.initRowTotale();
      this.initColumnTotale();
      this.initValueRendicontazioneIntables();
      this.initCalc();
		  this.erroreexport = erroreexport.testoMsgApplicativo;
      this.tabtranche = tranche;
      if(this.tabtranche!=null){
         this.titolo=this.tabtranche.desEstesaTab;
         this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
      }
      this.erroreStato = erroreStato.testoMsgApplicativo;
      this.erroreModifica = erroreModifica.testoMsgApplicativo;
     
	  this.client.mostrabottoniera=true;
      this.client.spinEmitter.emit(false);
    },
    err => {this.client.mostrabottoniera=true;
		this.client.spinEmitter.emit(false);}
    );

  }

    // Inizializza le colonne della tabella principale del modello 
    initColumns(macroaggregati: Macroaggregati[]) {
      this.columnsMacroaggregati.push('MISSIONI E PROGRAMMI \\ MACROAGGREGATI')
        macroaggregati.forEach(element => {
          this.columnsMacroaggregati.push(element.descMacroaggregati + ' ' + element.altraDescMacroaggregati)
        });
        this.columnsMacroaggregati.push('TOTALE')
    }
    
    //Inizializza le colonne della tabella principale del modello 
    initCodifica(macroaggregati: Macroaggregati[]) {
      this.columnsCodifica.push('Missione', 'Programma')
        macroaggregati.forEach(element => {
          this.columnsCodifica.push(element.codificaMacroaggregati)
        });
        this.columnsCodifica.push('Tot')
    }
    
    initColumnSpanH(){
      this.columnSpanH[0]=2;
      this.columnSpanH[1]=8;
    }

    getColumnSpanH(i:number){
      return this.columnSpanH[i];
    }

    initColumnSpanM(macro:string[]){
      macro.forEach(() => {
        this.columnSpanM.push(1)
      });
      this.columnSpanM[0]=2;
    }

    getColumnSpanM(i:number){
      return this.columnSpanM[i];
    }

    initRowSpan(spesaMissione: SpesaMissione[], codifica:string[]){
      spesaMissione.forEach(() => {
        codifica.forEach(() => {
          this.rowSpan.push(1)
        });
      });
      this.rowSpan[0]=2;
    }

    getRowSpan(col:number, i:number) {
      return this.rowSpan[i+col +(i*this.rowSpesaMissione.length)];
    }

    initRowTotale(){
      for(let i=0; i<this.rowSpesaMissione.length; i++){
        this.rowTotale[i]=0;
      }
    }

    initColumnTotale(){
      for(let i=0; i<this.columnsMacroaggregatiData.length+1; i++){
        this.columnTotale[i]=0;
        this.columnTotaleS[i]=0;
      }
    }

    initCalc(){
      for(let i=0; i<this.rendicontazioneMacroaggregati.valoriMacroaggregati.length-2; i++){
        for(let j=0; j<this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi.length; j++){
            if(this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value != null && this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value != undefined){
              this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value = this.transform(parseFloat(this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value));
            }
          }
      }
      this.calcTableValue();
    }

  // Ordina le righe e i dati delle righe per la propriet� ordinamento
  sortMacroaggregatiTable() {
    this.TData.sort(function (a, b) {
      return - ( b.ordinamento - a.ordinamento );
    });
    this.rowSpesaMissione.sort(function (a, b) {
      return - ( b.ordinamento - a.ordinamento );
    });
  }

  // Crea array dei valori per ogni tabella
  initValueRendicontazioneIntables() {
    this.TData = this.rendicontazioneMacroaggregati.valoriMacroaggregati;
    this.sortMacroaggregatiTable();
  }


  changeKey(i:number, valore: any, j:number) {
        if(valore=="" || valore == null || valore==undefined){
            this.TData[i].campi[j-2].value=null;
            if((j-2)==0 && this.TData[i].campi[3].value!==null){
              this.TData[i].campi[6].value=this.transform(this.parsingFloat(this.TData[i].campi[6].value)+this.parsingFloat(this.TData[i].campi[3].value));
              this.openDialogInfo(this.TData[i].campi[3].value);
              this.TData[i].campi[3].value=null;
              this.calcTableValue();
            }
        }
        else {
          if(valore.indexOf('.') != -1) {
            if(valore.indexOf(',') != -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2){
              valore = [valore, '0'].join('');
            }  
              this.TData[i].campi[j-2].value=this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
              if((j-2)==0 && this.TData[i].campi[3].value!==null && this.TData[i].campi[0].value=="0,00"){
                this.TData[i].campi[6].value=this.transform(this.parsingFloat(this.TData[i].campi[6].value)+this.parsingFloat(this.TData[i].campi[3].value));
                this.openDialogInfo(this.TData[i].campi[3].value);
                this.TData[i].campi[3].value=null;
                this.calcTableValue();
              }
          }
          else{
             
              this.TData[i].campi[j-2].value= this.transform(parseFloat(valore.toString().replace(',','.'))); 
              if((j-2)==0 && this.TData[i].campi[3].value!==null && this.TData[i].campi[0].value=="0,00"){
                this.TData[i].campi[6].value=this.transform(this.parsingFloat(this.TData[i].campi[6].value)+this.parsingFloat(this.TData[i].campi[3].value));
                this.openDialogInfo(this.TData[i].campi[3].value);
                this.TData[i].campi[3].value=null;
                this.calcTableValue();
              }
          }
        }
        
    }

    openDialogInfo(val1){
      let messaggio=this.messaggioPopup.replace("IMPORTO", val1);
      const dialogRef = this.dialog.open(InfoPopup, {
          width: '650px',
          disableClose : true,
          autoFocus : true,
          data: {titolo: 'Informazione', messaggio: messaggio}
       });
    }
    

  // Calcoli effettuati ad ogni cambiamento dei valori
  calcTableValue() {
    
    for(let i=0; i<this.TData.length-2; i++){
      for(let j=0; j<this.TData[i].campi.length; j++){

        this.rowTotale[i]+=this.parsingFloat(this.TData[i].campi[j].value);
        this.totale+=this.parsingFloat(this.TData[i].campi[j].value);
        if(i>0 && i<4){
          this.totaleS+=this.parsingFloat(this.TData[i].campi[j].value);
        }
        for(let l=0; l<this.TData.length-2; l++){
          this.columnTotale[j]+=this.parsingFloat(this.TData[l].campi[j].value);
          if(l>0 && l<4){
            this.columnTotaleS[j]+=this.parsingFloat(this.TData[l].campi[j].value);
          }
        }
        
        this.totaleC[j]=this.transform(this.columnTotale[j]);
        this.totaleCS[j]=this.transform(this.columnTotaleS[j]);
        this.columnTotale[j]=0;
        this.columnTotaleS[j]=0;
      }
      this.totaleR[i]=this.transform(this.rowTotale[i]);
      this.rowTotale[i]=0;
    }
    this.totaleR[5]=this.transform(this.totale);
    this.totaleR[6]=this.transform(this.totaleS);
    this.totale=0;
    this.totaleS=0;

  }

  parsingFloat(el) {
    if (el == '') el = null;
    el = el ? parseFloat(el.toString().replaceAll('.','').replace(',','.')) : el;
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

  apricronologia(){
    if (this.espansa)
    this.espansa=false;
    else
    this.espansa=true;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
  }

  salvaModifiche(event) {
		this.client.nomemodello=SECTION.CODMODELLOMACRO;
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
            this.client.getRendicontazioneMacroaggregati(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
                this.rendicontazioneMacroaggregatiBeforeSave = response;
                this.rendicontazioneBeforeSaveTransform();
                for (let i = 0; i < this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati.length - 2; i++) {
                  for (let j = 0; j < this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi.length; j++) {
                    if (this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi[j].value !== this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi[j].value) {
                      modify = true;
                    }
                  }
                }
                if(modify){
                  this.errorMessage.error.descrizione  = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
                  this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                  this.client.spinEmitter.emit(false);
                  return;
              } else {
                //if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
				if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0]) 
				&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length==0)) {
                  this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                  this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                  this.client.spinEmitter.emit(false);
                  return;
                } else {
                  this.client.spinEmitter.emit(true);
                  this.rendicontazioneMacroaggregati.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                  this.rendicontazioneMacroaggregati.profilo=this.client.profilo;
                for(let i=0; i<this.rendicontazioneMacroaggregati.valoriMacroaggregati.length-2; i++){
                  for(let j=0; j<this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi.length; j++){
                      this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value = this.parsingFloat(this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value);
                  }
                }
                this.rendicontazioneMacroaggregati.idSchedaEnteGestore = this.rendicontazione.idSchedaEnte;
                this.client.saveMacroaggregati(this.rendicontazioneMacroaggregati, this.cronologiaMod.cronologia.notaEnte, this.cronologiaMod.cronologia.notaInterna)
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
                      this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOMACRO);
            
                    this.initCalc();
                    if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                      this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                    } else if(this.client.operazione == OPERAZIONE.CHECK){
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
    this.client.getRendicontazioneMacroaggregati(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
      this.rendicontazioneMacroaggregatiInitial = response;
      this.initialTransform();
    })
  }
  
  initialTransform() {
    for(let i=0; i<this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati.length-2; i++){
      for(let j=0; j<this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi.length; j++){
        if (this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi[j].value != null && this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi[j].value != undefined) {
          this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi[j].value = this.transform(parseFloat(this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi[j].value));
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
      let esporta = new EsportaMacroaggregatiGreg();
      this.rendicontazioneMacroaggregati.profilo=this.client.profilo;
      for(let i=0; i<this.rendicontazioneMacroaggregati.valoriMacroaggregati.length-2; i++){
        for(let j=0; j<this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi.length; j++){
          this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value = this.parsingFloat(this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value);
        }
      }
	  this.rendicontazioneMacroaggregati.denominazioneEnte = this.datiEnte.denominazione;
      esporta.datiRendicontazione = this.rendicontazioneMacroaggregati;
      esporta.totaliR = this.totaleR;
      esporta.totaliC = this.totaleC;
      esporta.totaliCS = this.totaleCS;
      this.client.esportaMacroaggregati(esporta).subscribe(res => {
        const name: string = res.get('name') as string;
        const messaggio: string = res.get('messaggio') as string;
        this.client.messaggioFeedback = messaggio;
        this.initCalc();
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
      }, err => {
        this.initCalc();
        this.client.spinEmitter.emit(false);
      });
		}
	}

	ischanged() {
		this.ischangedvar = false;
		for (let i = 0; i < this.rendicontazioneMacroaggregati.valoriMacroaggregati.length - 2; i++) {
			for (let j = 0; j < this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi.length; j++) {
				if (this.rendicontazioneMacroaggregati.valoriMacroaggregati[i].campi[j].value !== this.rendicontazioneMacroaggregatiInitial.valoriMacroaggregati[i].campi[j].value) {
					this.ischangedvar = true;

				}
			}
		}
	}
  
  rendicontazioneBeforeSaveTransform() {
    for(let i=0; i<this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati.length-2; i++){
      for(let j=0; j<this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi.length; j++){
        if (this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi[j].value != null && this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi[j].value != undefined) {
          this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi[j].value = this.transform(parseFloat(this.rendicontazioneMacroaggregatiBeforeSave.valoriMacroaggregati[i].campi[j].value));
        }
      }
    }
  }

  check(){
    this.client.checkMacroaggregati(this.idRendicontazioneEnte).subscribe(
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
            motivazione.codModello = SECTION.CODMODELLOMACRO;
            motivazione.idRendicontazione = this.idRendicontazioneEnte;
            motivazione.nota = result.nota;
            motivazione.modello = 'Mod. Macroaggregati';
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

