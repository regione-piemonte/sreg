import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatTable } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { ComuneAssociatoGreg } from '@greg-app/app/dto/ComuneAssociatoGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ERRORS, OPERAZIONE,  RENDICONTAZIONE_STATUS,  SECTION } from '@greg-app/constants/greg-constants';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { AttivitaSocioAssist } from '@greg-app/app/dto/AttivitaSocioAssist';
import { ComuniAttivitaValoriModE } from '@greg-app/app/dto/ComuniAttivitaValoriModE';
import { RendicontazioneAttivitaModE } from '@greg-app/app/dto/RendicontazioneAttivitaModE';
import { ConfermaCancellazionePopup } from '../conferma-cancellazione-popup/conferma-cancellazione-popup.component';
import { SalvaModelloEGreg } from '@greg-app/app/dto/SalvaModelloEGreg';
import { EsportaModelloEGreg } from '@greg-app/app/dto/EsportaModelloEGreg';
import { RendicontazioneModE } from '@greg-app/app/dto/RendicontazioneModE';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';

@Component({
  selector: 'app-modelloE',
  templateUrl: './modelloE.component.html',
  styleUrls: ['./modelloE.component.css'],
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
export class ModelloEComponent implements OnInit {
  @ViewChild('rendicontazioniComuniTable', {static : false}) rendicontazioniComuniTable: MatTable<any>;
  @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;
  @ViewChild(CronologiaModelliComponent, {static: false}) cronologiaMod: CronologiaModelliComponent;

  navigation: Navigation;
  idRendicontazioneEnte;
  infoOperatore: RendicontazioneEnteGreg;
  tabtranche : ModelTabTranche;
  mostrasalva:boolean = true;

  titolo: string = '';
  tooltipTitle: string = '';
  msgCancellazione: string = '';
  
  listaComuniAssociati: ComuneAssociatoGreg[];
  comuneAll: ComuneAssociatoGreg = new ComuneAssociatoGreg(null, "ALL", null, null,null,null);
  aggiungiButton: boolean = false;
  comuneSelected: ComuneAssociatoGreg = null;
  listaIntestazioni: AttivitaSocioAssist[];
  columnsTable: Array<string> = [];
  tooltipColumnsTable: Array<string> = [];

  RendModE: ComuniAttivitaValoriModE[] = [];
  RendModEInitial: ComuniAttivitaValoriModE[] = [];
  rendicontazioneModE: ComuniAttivitaValoriModE = new ComuniAttivitaValoriModE();
  rowTotaleRiga: Array<number> =[];
  totaleRiga: Array<string> =[];
  ischangedvar:boolean;
  erroreexport :string;
  rowTotaleColonna: Array<number> =[];
  totaleColonna: Array<string> =[];
  calledFromDB: boolean = false;
  rendicontazione: RendicontazioneEnteGreg;

  errorMessage = { 
    error: {descrizione:''},
    message:'',
    name:'',
    status:'',
    statusText:'',
    url:'',
    date: Date
  }
  infoOperatoreBeforeSave: RendicontazioneEnteGreg;
  statoInitial: string;
  rendicontazioneModEBeforeSave: RendicontazioneModE;
  erroreStato: string;
  erroreModifica: string;
  datiEnte: DatiEnteGreg;

  constructor(private router: Router, private route: ActivatedRoute, public client: GregBOClient, private dialog: MatDialog,
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
		this.client.nomemodello=SECTION.CODMODELLOE;
    forkJoin({
      // Recupero le informazione della rendicontazione da visualizzare se l'utente e' operatore
      infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
      // Recupero titolo e tooltip
      titolo: this.client.getMsgInformativi(SECTION.MODELLOE),
      tooltip: this.client.getMsgInformativi(SECTION.MODELLOE_POPOVER),
      msgCancellazione: this.client.getMsgInformativi(SECTION.MODELLOE_CANCELLAZIONE),
      //Recupero le intestazioni della tabella
      intestazioni: this.client.getAttivitaSocioAssist(),
      // Recupero i comuni associati all'ente
      comuni: this.client.getComuniAssociati(this.rendicontazione.idSchedaEnte ,this.rendicontazione.annoEsercizio),
      rendicontazione: this.client.getRendicontazioneModE(this.rendicontazione.idRendicontazioneEnte),
      rendicontazioneInitial: this.client.getRendicontazioneModE(this.rendicontazione.idRendicontazioneEnte),
      tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOE),
      erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
      erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
      erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
    }).subscribe(({ infoOperatore, titolo, tooltip, msgCancellazione, intestazioni, comuni, rendicontazione, rendicontazioneInitial, tranche,erroreexport, erroreModifica, erroreStato }) => {
        this.infoOperatore = infoOperatore;
        this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
        this.tooltipTitle = tooltip[0].testoMsgInformativo;
        this.msgCancellazione = msgCancellazione[0].testoMsgInformativo;
        this.listaComuniAssociati = comuni;
        this.listaIntestazioni = intestazioni;
        this.RendModE = rendicontazione.comuniAttivitaValori;
        this.RendModEInitial = rendicontazioneInitial.comuniAttivitaValori;
        this.orderRows();
        this.initialTransform();
        this.initColumns(intestazioni);
        this.erroreexport = erroreexport.testoMsgApplicativo;
        this.initRowTotale();
        this.calledFromDB=true;
        this.initCalc();
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
      err => {
        this.client.mostrabottoniera=true;
        this.client.spinEmitter.emit(false);
      });
  }

  orderRows(){
    this.RendModE.sort(function(a, b) {
      return a.descComune.toUpperCase().localeCompare(b.descComune.toUpperCase());
    })
    this.RendModEInitial.sort(function(a, b) {
      return a.descComune.toUpperCase().localeCompare(b.descComune.toUpperCase());
    })
  }

  // Inizializza le colonne della tabella principale del modello 
  initColumns(intestazioni: AttivitaSocioAssist[]) {
    this.columnsTable.push('Cod.ISTAT', 'Comune', 'TOTALE SPESE DIRETTE', 'AZIONI');
    this.tooltipColumnsTable.push('','','','');
    intestazioni.forEach((element, index) => {
          this.columnsTable.splice(2+index, 0, element.descAttivitaSocioAssist);
          this.tooltipColumnsTable.splice(2+index,0,element.tooltipDescAttivitaSocioAssist);
    })
  }

  initCalc(){
    for(let i=0; i<this.RendModE.length; i++){
      for(let j=0; j<this.RendModE[i].attivita.length; j++){
        if (this.RendModE[i].attivita[j].valore != null && this.RendModE[i].attivita[j].valore != undefined){
          if(this.calledFromDB){
            this.RendModE[i].attivita[j].valore = this.transform(parseFloat(this.RendModE[i].attivita[j].valore));
          }else{
            this.RendModE[i].attivita[j].valore = this.transform(this.parsingFloat(this.RendModE[i].attivita[j].valore));
          }
         
        }
      }
    }
    this.calcTableValue();
  }

  changeKey(rowI:number, j:number, valore: any) {
    if(valore=="" || valore == null || valore==undefined){
        this.RendModE[rowI].attivita[j].valore=null;
            this.calcTableValue();
    }
    else {
        if(valore.indexOf('.') != -1) {
            if(valore.indexOf(',') != -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2){
            valore = [valore, '0'].join('');
            }
            this.RendModE[rowI].attivita[j].valore=this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
        }
        else{
            this.RendModE[rowI].attivita[j].valore= this.transform(parseFloat(valore.toString().replace(',','.')));
        }
    }
  }

  calcTableValue(){
    this.initRowTotale();

    for(let i=0; i<this.RendModE.length; i++){
      for(let j=0; j<this.RendModE[i].attivita.length; j++){
        this.rowTotaleRiga[i]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
      }
      this.totaleRiga[i]=this.transform(this.rowTotaleRiga[i]);
    }

    for(let i=0; i<this.RendModE.length; i++){
      for(let j=0; j<this.RendModE[i].attivita.length; j++){
        switch(j) {
          case 0: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 1: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 2: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 3: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 4: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 5: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 6: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 7: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
          case 8: {
            this.rowTotaleColonna[j]+=this.parsingFloat(this.RendModE[i].attivita[j].valore);
            break;
          }
        }
        this.totaleColonna[j]=this.transform(this.rowTotaleColonna[j]);
      }
    }
    // Sommo i totali riga TOTALI
    for(let i=0; i<this.rowTotaleColonna.length-1; i++){
      this.rowTotaleColonna[9]+=this.rowTotaleColonna[i];
    }
    this.totaleColonna[9]=this.transform(this.rowTotaleColonna[9]);
  }

  initRowTotale(){
    for(let i=0; i<this.RendModE.length; i++){
      this.rowTotaleRiga[i]=0;
      this.totaleRiga[i]=this.transform(this.rowTotaleRiga[i]);
    }

    for(let i=0; i<this.listaIntestazioni.length; i++){
      this.rowTotaleColonna[i]=0;
      this.totaleColonna[i]=this.transform(this.rowTotaleColonna[i]);
    }
    // Totale Spese Dirette Riga TOTALI
    this.rowTotaleColonna[9]=0;
    this.totaleColonna[9]=this.transform(this.rowTotaleColonna[9]);
  }

  parsingFloat(el) {
    if (el == '') el = null;
    el = el ? parseFloat(el.toString().replaceAll('.','').replace(',','.')) : el;
    return el;
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

  activateButton() {
    this.aggiungiButton = this.comuneSelected !== null && this.listaComuniAssociati.length != 0? true : false;
  }

  aggiungiRendicontazioneComune() {
    if(this.comuneSelected.codiceIstat === "ALL"){
      for(let comuneToAdd of this.listaComuniAssociati) {
        let trovato = this.RendModE.find(rend => rend.codIstatComune == comuneToAdd.codiceIstat)
        if(trovato == undefined){          
          //Verifico se gia' e' stato aggiunto il comune        
          this.rendicontazioneModE = new ComuniAttivitaValoriModE();
          this.rendicontazioneModE.idComune = comuneToAdd.idComune;
          this.rendicontazioneModE.codIstatComune = comuneToAdd.codiceIstat;
          this.rendicontazioneModE.descComune = comuneToAdd.desComune;
          this.rendicontazioneModE.attivita = [];
          for(let attivita of this.listaIntestazioni){
            let rendAttivita = new RendicontazioneAttivitaModE();
            rendAttivita.idAttivitaSocioAssist = attivita.idAttivitaSocioAssist;
            rendAttivita.codAttivitaSocioAssist = attivita.codAttivitaSocioAssist;
            rendAttivita.descAttivitaSocioAssist = attivita.descAttivitaSocioAssist;
            rendAttivita.valore = null;
            this.rendicontazioneModE.attivita.push(rendAttivita);
          }
          this.RendModE.push(this.rendicontazioneModE);
        }
      }
    }
    else {
      for(let rend of this.RendModE) {
        if(rend.codIstatComune == this.comuneSelected.codiceIstat){
          this.errorMessage.error.descrizione = 'Comune gia\' aggiunto';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Comune gia\' aggiunto' }))
          return;
        }
      }
      this.rendicontazioneModE = new ComuniAttivitaValoriModE();
      this.rendicontazioneModE.idComune = this.comuneSelected.idComune;
      this.rendicontazioneModE.codIstatComune = this.comuneSelected.codiceIstat;
      this.rendicontazioneModE.descComune = this.comuneSelected.desComune;
      this.rendicontazioneModE.attivita = [];
      for(let attivita of this.listaIntestazioni){
        let rendAttivita = new RendicontazioneAttivitaModE();
        rendAttivita.idAttivitaSocioAssist = attivita.idAttivitaSocioAssist;
        rendAttivita.codAttivitaSocioAssist = attivita.codAttivitaSocioAssist;
        rendAttivita.descAttivitaSocioAssist = attivita.descAttivitaSocioAssist;
        rendAttivita.valore = null;
        this.rendicontazioneModE.attivita.push(rendAttivita);
      }
      this.RendModE.push(this.rendicontazioneModE);
    }
    this.orderRows();
    this.calledFromDB=false;
    this.initCalc();
    this.rendicontazioniComuniTable.renderRows();
    this.aggiungiButton = false;
    this.comuneSelected = null;
  }

  openDialogDeleteComune(codIstatComune: string){
    const dialogRef = this.dialog.open(ConfermaCancellazionePopup, {
        width: '650px',
        disableClose : true,
        autoFocus : true,
        data: {titolo: 'Conferma eliminazione', messaggio: this.msgCancellazione}
     });
      dialogRef.afterClosed().subscribe(result => {
      if(result) {
        if(result.delete){
          this.eliminaComune(codIstatComune);
        }
      }
    });
  }

  eliminaComune(codIstatComune: string) {
    let rendOfComuneToRemove = this.RendModE.find(rend => rend.codIstatComune === codIstatComune);

    const index: number = this.RendModE.indexOf(rendOfComuneToRemove);
    if (index !== -1) {
        this.RendModE.splice(index, 1);
    }
    this.rendicontazioniComuniTable.renderRows();
    this.calcTableValue();
  }

  refillInitials() {
    this.client.getRendicontazioneModE(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
      this.RendModEInitial = response.comuniAttivitaValori;
      this.orderRows();
      this.initialTransform();
    })
  }

  initialTransform() {
    for(let i=0; i<this.RendModEInitial.length; i++){
      for(let j=0; j<this.RendModEInitial[i].attivita.length; j++){
        if (this.RendModEInitial[i].attivita[j].valore != null && this.RendModE[i].attivita[j].valore != undefined) {
          this.RendModEInitial[i].attivita[j].valore = this.transform(parseFloat(this.RendModEInitial[i].attivita[j].valore));
        }
      }
    }
  }

  salvaModifiche(event) {
		this.client.nomemodello=SECTION.CODMODELLOE;
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
          this.client.getRendicontazioneModE(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
              this.rendicontazioneModEBeforeSave = response;
              this.rendicontazioneBeforeSaveTransform();
              if(this.rendicontazioneModEBeforeSave.comuniAttivitaValori.length != this.RendModEInitial.length){
                modify = true;
              } else {
              this.orderRowsBeforeSave();
              this.rendicontazioneModEBeforeSave.comuniAttivitaValori.forEach((elem, index) => {
                this.rendicontazioneModEBeforeSave.comuniAttivitaValori[index].attivita.forEach((el, ind) => {
                  if(this.rendicontazioneModEBeforeSave.comuniAttivitaValori[index].attivita[ind] !== el || this.rendicontazioneModEBeforeSave.comuniAttivitaValori[index] !== elem) {
                   modify = true;
                  }
                })
              });
                for(let i=0; i<this.rendicontazioneModEBeforeSave.comuniAttivitaValori.length; i++){
                  for(let j=0; j<this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita.length; j++){
                   if (this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita[j].valore !== this.RendModEInitial[i].attivita[j].valore) {
                      modify = true;
                    }
                  }
                }
              }
            
              if(modify){
                  this.errorMessage.error.descrizione  = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
                  this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                  this.client.spinEmitter.emit(false);
                  return;
              } else {
             //   if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
				if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
				&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length==0)) {
                  this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                  this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                  this.client.spinEmitter.emit(false);
                  return;
                } else {
                  this.client.spinEmitter.emit(true);
            
                  for(let i=0; i<this.RendModE.length; i++){
                    for(let j=0; j<this.RendModE[i].attivita.length; j++){
                      this.RendModE[i].attivita[j].valore = this.parsingFloat(this.RendModE[i].attivita[j].valore);
                    }
                  }
            
                  let salva: SalvaModelloEGreg = new SalvaModelloEGreg();
                  salva.idEnte = this.rendicontazione.idSchedaEnte;
                  salva.idRendicontazioneEnte =  this.rendicontazione.idRendicontazioneEnte;
                  salva.listaRendicontazione = this.RendModE;
                  salva.cronologia = this.cronologiaMod.cronologia;
                  salva.profilo = this.client.profilo;
                  
                  this.client.salvaModelloE(salva).subscribe(data => {
                    if(data.esito == "OK") {
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
                      this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOE);
                      this.refillInitials();
                      this.updateTableAfterSave();
                      this.calledFromDB=true;
                      this.initCalc();
                      if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                        this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                      }
                      else{
                        this.toastService.showSuccess({text: data.messaggio});
                        this.client.spinEmitter.emit(false);
                      }
                    }
                  }, err => {
                    this.calledFromDB=true;
                    this.initCalc();
                    this.client.spinEmitter.emit(false);
                  });
                }
              }
            });
          }
      });
  }

  updateTableAfterSave(){
    let valueNull;
    let idxRend;
    let indexPre;
    let del = false;
    let i;
    for(i=0; i<this.RendModE.length; i++){
      if(del){
        i = indexPre;
        del=false;
      }
      valueNull=true;
      idxRend = i;
      for(let j=0; j<this.RendModE[i].attivita.length; j++){
        if(this.RendModE[i].attivita[j].valore !== null) {
          valueNull = false;
        }
      }
      if(valueNull){
        this.RendModE.splice(idxRend, 1);
        this.rendicontazioniComuniTable.renderRows();
        indexPre=i;
        del = true;
      }
    }
    if(this.RendModE[indexPre]!==undefined){
      for(let j=0; j<this.RendModE[indexPre].attivita.length; j++){
       idxRend = indexPre;
        if(this.RendModE[indexPre].attivita[j].valore !== null) {
          valueNull = false;
        }
       }
       if(valueNull){
       this.RendModE.splice(idxRend, 1);
       this.rendicontazioniComuniTable.renderRows();
      }
    }

    // this.RendModE.forEach((elem, index) => {
    //   // if(del){
    //   //   index = indexPre;
    //   //   del=false;
    //   // }
    //   valueNull=true;
    //   idxRend = index;
    //   this.RendModE[index].attivita.forEach((el, ind) => {
    //     if(this.RendModE[index].attivita[ind].valore !== null) {
    //       valueNull = false;
    //     }
    //   }
    //   )
    //   if(valueNull){
    //     this.RendModE.splice(idxRend, 1);
    //     this.rendicontazioniComuniTable.renderRows();
    //     // indexPre=index;
    //     // del = true;
    //   }
    // });
  }

  @HostListener('document:changeTabEvent')
  checkEdited(event) {
    if(this.RendModE.length !== this.RendModEInitial.length) {
        document.dispatchEvent(this.client.notSavedEvent);
        return;
    }
    this.ischanged();
    if(this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
    else document.dispatchEvent(this.client.changeTabEmitter);
    }

  esportaexcel() {
    this.updateTableAfterSave();
		this.ischanged();
		if (this.ischangedvar) {
			this.toastService.showError({ text: this.erroreexport });
		}
		else {
      this.client.spinEmitter.emit(true);
      let esporta: EsportaModelloEGreg = new EsportaModelloEGreg();
      esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;

      for(let i=0; i<this.RendModE.length; i++){
        for(let j=0; j<this.RendModE[i].attivita.length; j++){
          this.RendModE[i].attivita[j].valore = this.parsingFloat(this.RendModE[i].attivita[j].valore);
        }
      }
      
      esporta.rendModE = this.RendModE;
      esporta.totaleRiga = this.totaleRiga;
      esporta.totaleColonna = this.totaleColonna;
     esporta.denominazioneEnte = this.datiEnte.denominazione;
      this.client.esportaModelloE(esporta).subscribe(res => {
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
              this.calledFromDB=true;
              this.initCalc();
          }
          this.toastService.showSuccess({ text: messaggio });
          this.client.spinEmitter.emit(false);
      },
      err => {
          this.calledFromDB=true;
          this.initCalc();
          this.client.spinEmitter.emit(false);
      });
    }
	}

	ischanged() {
		this.ischangedvar = false;
    this.RendModE.forEach((elem, index) => {
      this.RendModE[index].attivita.forEach((el, ind) => {
        if(this.RendModE[index].attivita[ind] !== el || this.RendModE[index] !== elem) {
         this.ischangedvar = true;
        }
      })
    });

    for(let i=0; i<this.RendModE.length; i++){
      for(let j=0; j<this.RendModE[i].attivita.length; j++){
        if (this.RendModE[i].attivita[j].valore !== this.RendModEInitial[i].attivita[j].valore) {
            this.ischangedvar = true;
        }
      }
    }
  }

  rendicontazioneBeforeSaveTransform() {
    for(let i=0; i<this.rendicontazioneModEBeforeSave.comuniAttivitaValori.length; i++){
      for(let j=0; j<this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita.length; j++){
        if (this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita[j].valore != null && this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita[j].valore != undefined) {
          this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita[j].valore = this.transform(parseFloat(this.rendicontazioneModEBeforeSave.comuniAttivitaValori[i].attivita[j].valore));
        }
      }
    }
  }

  orderRowsBeforeSave(){
    this.rendicontazioneModEBeforeSave.comuniAttivitaValori.sort(function(a, b) {
      return a.descComune.toUpperCase().localeCompare(b.descComune.toUpperCase());
    })
    this.rendicontazioneModEBeforeSave.comuniAttivitaValori.sort(function(a, b) {
      return a.descComune.toUpperCase().localeCompare(b.descComune.toUpperCase());
    })
  }

}
