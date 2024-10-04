import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { DatiModelloA1Greg } from '@greg-app/app/dto/DatiModelloA1Greg';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { EsportaModelloA1Greg } from '@greg-app/app/dto/EsportaModelloA1Greg';
import { GenericResponseGreg } from '@greg-app/app/dto/GenericResponseGreg';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { ResponseEsportaModelliGreg } from '@greg-app/app/dto/ResponseEsportaModelliGreg';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { SalvaModelloA1Greg } from '@greg-app/app/dto/SalvaModelloA1Greg';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';
import { VociModelloA1Greg } from '@greg-app/app/dto/VociModelloA1Greg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ERRORS, OPERAZIONE,  RENDICONTAZIONE_STATUS,  SECTION } from '@greg-app/constants/greg-constants';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';


@Component({
  selector: 'app-modelloA1',
  templateUrl: './modelloA1.component.html',
  styleUrls: ['./modelloA1.component.css'],
})
export class ModelloA1Component implements OnInit {

  navigation: Navigation;
  ente: EnteGreg;
  a: any[];
 
  listavoci: VociModelloA1Greg[] = [];
  listadati: DatiModelloA1Greg[] = [];
  idRendicontazioneEnte;
  visibileSection:boolean;
  espansa:boolean;
  state: string = 'default';
  mostrasalva:boolean;
  enteValues: string[] = [];
  statoRendicontazione: string;
  rendicontazione: RendicontazioneEnteGreg;
  messaggioinf : string;
  intestazione : string;
  listadatiInitial: DatiModelloA1Greg[] = [];
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
	ischangedvar:boolean;

  @ViewChild(CronologiaModelliComponent, {static: false}) cronologiaMod: CronologiaModelliComponent;
  @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;
  // visible: boolean;
  infoOperatore: RendicontazioneEnteGreg;
	tabtranche : ModelTabTranche;
  rendicontazioneModA1BeforeSave: DatiModelloA1Greg[];
  infoOperatoreBeforeSave: RendicontazioneEnteGreg;
  statoInitial: string;
  erroreStato: string;
  erroreModifica: string;
  datiEnte: DatiEnteGreg;

  warnings: string[] = [];
  errors: string[] = [];
  // ruoloente: string;
  messaggio: string;
  titoloPopUp: string;
  esito: string;
  obblMotivazione: boolean;
  warningCheck: any;

  constructor(private router: Router, private route: ActivatedRoute, public client: GregBOClient
    , public toastService: AppToastService, private gregError: GregErrorService, public pulsanti:PulsantiFunzioniComponent, private dialog: MatDialog,) {
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
	  this.client.nomemodello=SECTION.CODMODELLOA1;
    forkJoin({
      dati: this.client.getDatiModelloA1( this.rendicontazione.idRendicontazioneEnte ),
	    voci : this.client.getVociModelloA1(this.rendicontazione.idRendicontazioneEnte),
   		msgInformativi: this.client.getMsgInformativi(SECTION.MODELLOA1),
      infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
      tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOA1),
      intestazione: this.client.getMsgInformativiPerCod("10"),
      erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
      erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
      erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
    })
    .subscribe(({ dati,voci,msgInformativi, infoOperatore,tranche,intestazione,erroreexport, erroreModifica, erroreStato }) => {
      this.listadati = dati as any;
      this.refillInitials();
	    this.listavoci = voci as any;      
      this.infoOperatore = infoOperatore;
      this.statoInitial = this.infoOperatore.statoRendicontazione.codStatoRendicontazione;
		  this.messaggioinf = msgInformativi[0].testoMsgInformativo;
		  this.erroreexport = erroreexport.testoMsgApplicativo;
      this.tabtranche = tranche;
      if(this.tabtranche!=null){
        this.intestazione=this.tabtranche.desEstesaTab;
        this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
     }
     
      this.erroreStato = erroreStato.testoMsgApplicativo;
      this.erroreModifica = erroreModifica.testoMsgApplicativo;
  
      this.client.mostrabottoniera=true;
      this.client.spinEmitter.emit(false);
    },
      err => {
	      this.client.mostrabottoniera=true;
        this.client.spinEmitter.emit(false);
      }
    );
  }

  changeKey(Key:string ,valore: any,i:number) {

    delete this.listadati[i].valore[Key];
    if(valore=="" || valore == null){
      this.listadati[i].valore[Key] = null;
    }
    else {
      if(valore.indexOf('.') !== -1) {
        if(valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2)
            valore = [valore, '0'].join('');
        this.listadati[i].valore[Key]= this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
      }
      else
        this.listadati[i].valore[Key]= this.transform(parseFloat(valore.toString().replace(',','.')));
    }
      
  }

  // FIX focus da input a input premendo tab
  trackByFn(index, item) {
    return index;  }

  downloadReport() {
  }

  aggiornatotale(Key:string ,valore: any,i:number){

    delete this.listadati[i].valore[Key];
        if(valore=="" || valore==null || valore=="0,00" || valore==",00"  || valore=="00"  || valore=="0,0"){
          valore="0,00";
      this.listadati[i].valore[Key] = null;
    }
    else {
      this.listadati[i].valore[Key] = valore;
    }
    
    var importotot=0;
    var lunghezza=this.listadati.length-1;
    for (let j=0; j<=this.listadati.length-2;j++){
    var valore1 = this.listadati[j].valore[Key];
      if(valore1!="" && valore1!=null){
      importotot = importotot + parseFloat(valore1.toString().replaceAll('.','').replace(',','.'));	
      }
    }
	  if (importotot != 0)
		  this.listadati[lunghezza].valore[Key] = this.transform(importotot).toString();
	  else
		  this.listadati[lunghezza].valore[Key] = "0,00";

  }

  isNumber(value: string | number): boolean {
    return ((value != null) &&
            (value !== '') &&
            !isNaN(Number(value.toString().replace(',','.'))));
  }

  transform(value: number,
          currencySign: string = '',
          decimalLength: number = 2, 
          chunkDelimiter: string = '.', 
          decimalDelimiter:string = ',',
          chunkLength: number = 3): string {
          let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
      if (this.isNumber(value)){
        if (value != null && value != undefined) {
          let num = value.toFixed(Math.max(0, ~~decimalLength));
          return currencySign+(decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
        }
      }
        return undefined;
  }

  salvaModifiche(event){
	this.client.nomemodello=SECTION.CODMODELLOA1;

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
          this.client.getDatiModelloA1(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
              this.rendicontazioneModA1BeforeSave = response;
              this.rendicontazioneBeforeSaveTransform();
              this.rendicontazioneModA1BeforeSave.forEach((elem, index) => {
                const map = new Map(Object.entries(elem.valore));
                map.forEach((val, i) => {
                  if(this.listadatiInitial[index].valore[i] !== val) {
                   modify = true;
                  }
                })
              });
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
                let salva: SalvaModelloA1Greg = new SalvaModelloA1Greg();
                salva.idEnte = this.rendicontazione.idSchedaEnte;
                salva.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                salva.cronologia = this.cronologiaMod.cronologia;
                salva.datiA1 = this.listadati;
                salva.profilo = this.client.profilo;
                this.client.spinEmitter.emit(true);
                this.client.saveModelloA1(salva)
                .subscribe(
                  (data: ResponseSalvaModelloGreg) => {
                    if(data.warnings && data.warnings.length > 0){
                      for(let warn of data.warnings){
                      this.errorMessage.error.descrizione = warn;
                      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : warn }))
                      }
                    }
                    if(data.errors && data.errors.length > 0){
                      for(let err of data.errors){
                      this.errorMessage.error.descrizione = err;
                      this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc : err }))
                      }
                    }
                  this.cronologiaMod.ngOnInit();
                  this.cronologiaMod.espansa=false;
                  this.cronologiaMod.state='rotated';
                  this.cronologiaMod.apricronologia();
                  this.cronologiaMod.cronologia.notaEnte = null;
                  this.cronologiaMod.cronologia.notaInterna = null;
          
                  //ricalcolo pulsantiera in caso di cambio stato
                  this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOA1);
          
                  this.refillInitials();
                  if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                    this.pulsanti.inviamodelli(this.rendicontazione.idRendicontazioneEnte);
                  } else if(this.client.operazione == OPERAZIONE.CHECK){
                    this.check();
                  } else{
                    this.toastService.showSuccess({text: data.descrizione});
                    this.client.spinEmitter.emit(false);
                  }
                  },
                  err => {
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
	
    if(this.listadati.length !== this.listadatiInitial.length) {
        document.dispatchEvent(this.client.notSavedEvent);
        return;
    }
    this.ischanged();
    if(this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
    else        document.dispatchEvent(this.client.changeTabEmitter);
  }

  refillInitials() {
    this.listadatiInitial = [];
    this.client.getDatiModelloA1( this.rendicontazione.idRendicontazioneEnte ).subscribe(response => {
      this.listadatiInitial = response as any;
    })
  }

	esportaexcel() {
		 this.ischanged();
		 if(this.ischangedvar){
			this.toastService.showError({ text: this.erroreexport }); 
			}
		else {
		this.client.spinEmitter.emit(true);
		let esporta: EsportaModelloA1Greg = new EsportaModelloA1Greg();
		esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
		esporta.datiA1 = this.listadati;
		esporta.vociA1 = this.listavoci;
		esporta.denominazioneEnte = this.datiEnte.denominazione;
		this.client.esportaModelloA1(esporta)
			.subscribe(
				res => {
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
					this.client.spinEmitter.emit(false);
				}
			);
			}
	}
	
	ischanged(){
		this.ischangedvar = false;
    this.listadati.forEach((elem, index) => {
      const map = new Map(Object.entries(elem.valore));
      map.forEach((val, i) => {
        if(this.listadatiInitial[index].valore[i] !== val) {
         this.ischangedvar = true;
        }
      })
    });
	}

  rendicontazioneBeforeSaveTransform() {
    for(let i=0; i<this.rendicontazioneModA1BeforeSave.length; i++){
      for(let j=0; j<this.rendicontazioneModA1BeforeSave[i].valore.length; j++){
        for(let k=0; k<this.rendicontazioneModA1BeforeSave[i].valore[j].length; k++){
          if(this.rendicontazioneModA1BeforeSave[i].valore[j][k] != null && this.rendicontazioneModA1BeforeSave[i].valore[j][k] != undefined) {
            this.rendicontazioneModA1BeforeSave[i].valore[j][k] = this.transform(parseFloat(this.rendicontazioneModA1BeforeSave[i].valore[j][k]));
          }
        }
      }
    }
  }

  controlloCompilazione(codVoce: string, element: DatiModelloA1Greg){
    let annoInizio = new Date(element.dataInizioValidita).getFullYear();
    let annoFine: number;
    let annoEsercizioSucc =this.rendicontazione.annoEsercizio+1;
    if(element.dataFineValidita!= null){
       annoFine = new Date(element.dataFineValidita).getFullYear();
    } else {
      annoFine = null;
    }

    if(annoInizio<=this.rendicontazione.annoEsercizio && (annoFine==null || annoFine>this.rendicontazione.annoEsercizio)){
      return false;
    } else if(annoInizio<=this.rendicontazione.annoEsercizio && annoFine==this.rendicontazione.annoEsercizio){
      if(codVoce == 'I1' || codVoce == 'I2'){
        return false;
      } else {
        return true;
      }
    } else if(annoInizio==annoEsercizioSucc){
      if(codVoce == 'I1' || codVoce == 'I2'){
        return true;
      } else {
        return false;
      }
    }
   
  }

  check(){
    this.client.checkModelloA1(this.idRendicontazioneEnte).subscribe(
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
            motivazione.codModello = SECTION.CODMODELLOA1;
            motivazione.idRendicontazione = this.idRendicontazioneEnte;
            motivazione.nota = result.nota;
            motivazione.modello = 'Mod. A1';
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
