import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatHorizontalStepper, MatTable } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { CausaleGreg } from '@greg-app/app/dto/CausaleGreg';
import { ComuneAssociatoGreg } from '@greg-app/app/dto/ComuneAssociatoGreg';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { SalvaModelloA2Greg } from '@greg-app/app/dto/SalvaModelloA2Greg';
import { TrasferimentoGreg } from '@greg-app/app/dto/TrasferimentoGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ERRORS, OPERAZIONE, RENDICONTAZIONE_STATUS, SECTION, TRANCHE } from '@greg-app/constants/greg-constants';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';
import { CurrencyFormat } from '@greg-app/app/currencyFormatter';
import { getLocaleNumberSymbol, NumberSymbol } from '@angular/common';
import { delay } from 'rxjs/operators';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { EsportaModelloA2Greg } from '@greg-app/app/dto/EsportaModelloA2Greg';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { CronologiaModelliArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/cronologia-modelli-archivio/cronologia-modelli-archivio.component';



@Component({
  selector: 'app-modelloA2-archivio',
  templateUrl: './modelloA2-archivio.component.html',
  styleUrls: ['./modelloA2-archivio.component.css'],
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
export class ModelloA2ArchivioComponent implements OnInit {
  @ViewChild('causaliTable', {static : false}) causaliTable: MatTable<any>;
  @ViewChild('trasferimentiEnteTable', {static : false}) trasferimentiEnteTable: MatTable<any>;
  @ViewChild('trasferimentiEnteTableByCausale', {static : false}) trasferimentiEnteTableByCausale: MatTable<any>;
  @ViewChild('trasferimentiComuneTable', {static : false}) trasferimentiComuneTable: MatTable<any>;
  @ViewChild('trasferimentiComuneTableByCausale', {static : false}) trasferimentiComuneTableByCausale: MatTable<any>;

  navigation: Navigation;
  ente: EnteGreg;
  trasferimentiComune: TrasferimentoGreg[] = [];
  trasferimentiEnte: TrasferimentoGreg[] = [];
  trasferimentiComuneFiltered: TrasferimentoGreg[] = [];
  trasferimentiEnteFiltered: TrasferimentoGreg[] = [];
  displayedColumns: string[] = ['codIstat', 'comune', 'causale', 'importo', 'elimina'];
 // listaCronologiaAssociata: MatTableDataSource<CronologiaGreg[]>;
  listaCausali: CausaleGreg[] = [];
  listaCausaliStatic: CausaleGreg[] = [];
  // columnsCronologia: Array<string> = ['dataEora', 'utente', 'statoRendicontazione', 'notaEnte', 'notaInterna'];
  //cronologia: CronologiaGreg = new CronologiaGreg();
  idRendicontazioneEnte;
  listaComuniAssociati: ComuneAssociatoGreg[];
  activeTab = 1;
  causaleInput: string;
  currentCausale: CausaleGreg = new CausaleGreg();
  comuneSelected: ComuneAssociatoGreg = null;
  comuneSelectedAlt: ComuneAssociatoGreg = null;
  causaleSelected: CausaleGreg = null;
  causaleSelectedAlt: CausaleGreg = null;
  errorMessage = { 
    error: {descrizione:''},
    message:'',
    name:'',
    status:'',
    statusText:'',
    url:'',
    date: Date
  }
  intestazione: string;
  tabtranche : ModelTabTranche;
  trasfEnte:string;
  trasfComune:string;
  totaleEnte;
  totaleComune;
 // espansa:boolean;
  state: string = 'default';
  visibileSection:boolean;
  ischangedvar:boolean;
   erroreexport :string;
  enteValues: string[] = [];

  ruolo: string;
  mostrasalva:boolean = true;
  // visible: boolean;
  infoOperatore: RendicontazioneEnteGreg;
  listaCausaliInitial: CausaleGreg[] = [];
  trasferimentiEnteInitial: TrasferimentoGreg[] = [];
  trasferimentiComuneInitial: TrasferimentoGreg[] = [];

//  @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;
  @ViewChild(CronologiaModelliArchivioComponent, {static: false}) cronologiaMod: CronologiaModelliArchivioComponent;
  @ViewChild('stepper', {static:false}) stepper: MatHorizontalStepper;
  infoOperatoreBeforeSave: RendicontazioneEnteGreg;
  rendicontazione: RendicontazioneEnteGreg;
  statoInitial: string;
  trasferimentiEnteBeforeSave: TrasferimentoGreg[] = [];
  causaliBeforeSave: CausaleGreg[] = [];
  trasferimentiComuneBeforeSave: TrasferimentoGreg[] = [];
  erroreModifica: string;
  erroreStato: string;
 datiEnte: DatiEnteGreg;
  ngAfterViewInit() {
    this.stepper._getIndicatorType = () => 'number';
  }

  constructor(private router: Router, private route: ActivatedRoute, public client: GregBOClient, 
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
		this.client.nomemodello=SECTION.CODMODELLOA2;
    forkJoin({
      // Recupero i comuni associati all'ente
      comuni: this.client.getComuniAssociati( this.rendicontazione.idSchedaEnte ,this.rendicontazione.annoEsercizio),
      // Recupero le causali per trasferimento da ente a comune (se precedentemente inserite)
      causali: this.client.getCausali(this.rendicontazione.idRendicontazioneEnte),
      // Recupero le causali per trasferimento da comune a ente
      causaliStatiche: this.client.getCausaliStatiche(),
      // Recupero i trasferimenti precedentemente inseriti
      trasferimenti: this.client.getTrasferimentiA2(this.rendicontazione.idRendicontazioneEnte),
      // Recupero le informazione della rendicontazione da visualizzare se l'utente e' operatore
      infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
      // Recupero l'intestazione del modello
      intestazione: this.client.getMsgInformativiPerCod("28"),
      tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOA2),
      erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
      erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
      erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
    }).subscribe(({ comuni, causali, causaliStatiche, trasferimenti, infoOperatore, intestazione, tranche, erroreexport, erroreModifica, erroreStato }) => {
        this.infoOperatore = infoOperatore;
        this.statoInitial = this.infoOperatore.statoRendicontazione.codStatoRendicontazione;
        this.listaComuniAssociati = comuni;
        this.listaCausali = causali;
        this.listaCausaliStatic = causaliStatiche;
        this.trasferimentiEnte = trasferimenti.trasferimentiEnteComune;
        this.trasferimentiComune = trasferimenti.trasferimentiComuneEnte;
        this.refillInitals();
        this.ricalcolaTabelle();
        this.tabtranche = tranche;
        if(this.tabtranche!=null){
          this.intestazione=this.tabtranche.desEstesaTab;
//          this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
       }
        this.erroreStato = erroreStato.testoMsgApplicativo;
        this.erroreModifica = erroreModifica.testoMsgApplicativo;
        
	      this.erroreexport = erroreexport.testoMsgApplicativo;
		    this.client.mostrabottoniera=true;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.mostrabottoniera=true;
        this.client.spinEmitter.emit(false);
    });
  }

  downloadReport() {
  }

  getTotaleTrasferimentiEnte() {
    return this.trasferimentiEnte.reduce((acc, cur) => acc*1 + cur.importo*1, 0);
  }

  getTotaleTrasferimentiComune() {
    return this.trasferimentiComune.reduce((acc, cur) => acc*1 + cur.importo*1, 0);
  }

  aggiungiCausale() {
    if(this.listaCausali.length == 5) {
      this.errorMessage.error.descrizione = 'Non e\' possibile inserire piu\' di 5 causali';
      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Non e\' possibile inserire piu\' di 5 causali' }))
    } else {
      for(let entry of this.listaCausali) {
        if(entry.descrizione.toUpperCase() == this.causaleInput.toUpperCase()) {
          this.errorMessage.error.descrizione = 'Causale gia\' inserita';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Causale gia\' inserita' }))
          return;
        }
      }
      this.currentCausale = new CausaleGreg();
      this.currentCausale.id = null;
      this.currentCausale.descrizione = this.causaleInput;
      this.listaCausali.push(this.currentCausale);
      this.causaliTable.renderRows();
    }
  }

  eliminaCausale(element: CausaleGreg) {
    const index: number = this.listaCausali.indexOf(element);
    if (index !== -1) {
        this.listaCausali.splice(index, 1);
    }      
    if(this.trasferimentiEnte.length > 0) {
      for(var i = this.trasferimentiEnte.length -1; i >= 0 ; i--){
        if(this.trasferimentiEnte[i].causale.descrizione == element.descrizione) {
          this.eliminaTrasferimentoEnte(this.trasferimentiEnte[i]);
        }
      }
    }
    this.causaliTable.renderRows();
  }

  nextCausali() {
    this.stepper.next();
  }

  
  transformAmountEnte(from: string) {  
    if(from == 'ente')
      this.trasfEnte = this.trasfEnte ? parseFloat(this.trasfEnte).toFixed(2) : this.trasfEnte;
    else if(from == 'comune')
      this.trasfComune = this.trasfComune ? parseFloat(this.trasfComune).toFixed(2) : this.trasfComune;
  }

  aggiungiTrasfEnte() {

    if(this.trasfEnte == null || this.trasfEnte == undefined || this.trasfEnte.length == 0 || this.trasfEnte == 'NaN') {
      this.errorMessage.error.descrizione = 'Inserire un importo';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Selezionare un importo' }))
          return;
    }

    if(this.comuneSelected == null) {
      this.errorMessage.error.descrizione = 'Selezionare un comune';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Selezionare un comune' }))
          return;
    }

    if(this.causaleSelected == null) {
      this.errorMessage.error.descrizione = 'Selezionare una causale';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Selezionare una causale' }))
          return;
    }

    if(this.trasferimentiEnte.findIndex(elem => elem.comune.idComune === this.comuneSelected.idComune && elem.causale.descrizione === this.causaleSelected.descrizione) != -1) {
      this.errorMessage.error.descrizione = 'Trasferimento verso comune selezionato gia\' presente';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Trasferimento verso comune selezionato gia\' presente' }))
          return;
    }

    let trasferimento = new TrasferimentoGreg();
    trasferimento.comune = this.comuneSelected;
    trasferimento.causale = this.causaleSelected;
    trasferimento.importo = parseFloat(this.trasfEnte);

    this.trasferimentiEnte.push(trasferimento);
    this.trasferimentiEnte.sort((a, b) => this.sortCausaleComune(a, b));
    this.totaleEnte = this.getTotaleTrasferimentiEnte();
    this.trasferimentiEnteTable.renderRows();
    
    let index = this.trasferimentiEnteFiltered.findIndex(elem => 
      elem.causale.descrizione === this.causaleSelected.descrizione);

    if(index != -1) {
      this.trasferimentiEnteFiltered[index].importo = (this.trasferimentiEnteFiltered[index].importo*1 + parseFloat(this.trasfEnte)*1);
    } else {
      trasferimento = new TrasferimentoGreg();
      trasferimento.comune = this.comuneSelected;
      trasferimento.causale = this.causaleSelected;
      trasferimento.importo = parseFloat(this.trasfEnte);
      this.trasferimentiEnteFiltered.push(trasferimento);
    }
    this.trasfEnte = null;
    this.trasferimentiEnteTableByCausale.renderRows();
    
  }

  aggiungiTrasfComune() {

    if(this.trasfComune == null || this.trasfComune == undefined || this.trasfComune.length == 0 || this.trasfComune == 'NaN') {
      this.errorMessage.error.descrizione = 'Inserire un importo';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Selezionare un importo' }))
          return;
    }

    if(this.comuneSelectedAlt == null) {
      this.errorMessage.error.descrizione = 'Selezionare un comune';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Selezionare un comune' }))
          return;
    }

    if(this.causaleSelectedAlt == null) {
      this.errorMessage.error.descrizione = 'Selezionare una causale';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Selezionare una causale' }))
          return;
    }

    if(this.trasferimentiComune.findIndex(elem => elem.comune.idComune === this.comuneSelectedAlt.idComune && elem.causale.id === this.causaleSelectedAlt.id) != -1) {
      this.errorMessage.error.descrizione = 'Trasferimento da comune selezionato gia\' presente';
          this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : 'Trasferimento da comune selezionato gia\' presente' }))
          return;
    }

    let trasferimento = new TrasferimentoGreg();
    trasferimento.comune = this.comuneSelectedAlt;
    trasferimento.causale = this.causaleSelectedAlt;
    trasferimento.importo = parseFloat(this.trasfComune);

    this.trasferimentiComune.push(trasferimento);
    this.trasferimentiComune.sort((a, b) => this.sortCausaleComune(a, b));
    this.totaleComune = this.getTotaleTrasferimentiComune();
    this.trasferimentiComuneTable.renderRows();
    
    
    let index = this.trasferimentiComuneFiltered.findIndex(elem => 
      elem.causale.id === this.causaleSelectedAlt.id);

    if(index != -1) {
      this.trasferimentiComuneFiltered[index].importo = (this.trasferimentiComuneFiltered[index].importo*1 + parseFloat(this.trasfComune)*1);
    } else {
      trasferimento = new TrasferimentoGreg();
      trasferimento.comune = this.comuneSelectedAlt;
      trasferimento.causale = this.causaleSelectedAlt;
      trasferimento.importo = parseFloat(this.trasfComune);
      this.trasferimentiComuneFiltered.push(trasferimento);
    }
    this.trasfComune = null;
    this.trasferimentiComuneTableByCausale.renderRows();
    
  }

  sortCausaleComune(a: TrasferimentoGreg, b: TrasferimentoGreg){
    if(a.comune.desComune<b.comune.desComune){
      return -1;
    } else if(a.comune.desComune==b.comune.desComune){
      if(a.causale.descrizione<b.causale.descrizione){
        return -1;
      } else if(a.causale.descrizione==b.causale.descrizione){
        return 0;
      } else {
        return 1;
      }
    } else {
      return 1;
    }
  }



  eliminaTrasferimentoEnte(element: TrasferimentoGreg) {
    const index: number = this.trasferimentiEnte.indexOf(element);
    if (index !== -1) {
        this.trasferimentiEnte.splice(index, 1);
    }      
    this.trasferimentiEnteTable.renderRows();
    
    const indT = this.trasferimentiEnte.findIndex(elem => 
      elem.causale.descrizione === element.causale.descrizione);
    const ind = this.trasferimentiEnteFiltered.findIndex(elem => 
      elem.causale.descrizione === element.causale.descrizione);
    if(indT !== -1) {
  
      if(ind !== -1) {
        this.trasferimentiEnteFiltered[ind].importo = (this.trasferimentiEnteFiltered[ind].importo*1 - element.importo*1);
      } else {
      //  console.log('ERROR');
      }
    } else {
      if(ind !== -1) {
        this.trasferimentiEnteFiltered.splice(ind, 1);
      } else {
      //  console.log('ERROR');
      }
    }
    this.totaleEnte = this.getTotaleTrasferimentiEnte();
    this.trasferimentiEnteTableByCausale.renderRows();
  }

  eliminaTrasferimentoComune(element: TrasferimentoGreg) {
    const index: number = this.trasferimentiComune.indexOf(element);
    if (index !== -1) {
        this.trasferimentiComune.splice(index, 1);
    }      
    this.trasferimentiComuneTable.renderRows();
    
    const indT = this.trasferimentiComune.findIndex(elem => 
      elem.causale.id === element.causale.id);
    const ind = this.trasferimentiComuneFiltered.findIndex(elem => 
      elem.causale.id === element.causale.id);
    if(indT !== -1) {
  
      if(ind !== -1) {
        this.trasferimentiComuneFiltered[ind].importo = (this.trasferimentiComuneFiltered[ind].importo*1 - element.importo*1);
      } else {
      //  console.log('ERROR');
      }
    } else {
      if(ind !== -1) {
        this.trasferimentiComuneFiltered.splice(ind, 1);
      } else {
      //  console.log('ERROR');
      }
    }
    this.totaleComune = this.getTotaleTrasferimentiComune();
    this.trasferimentiComuneTableByCausale.renderRows();
  }

  ricalcolaTabelle() {
    if(this.trasferimentiEnte.length > 0) {
      this.totaleEnte = this.getTotaleTrasferimentiEnte();
      // this.trasferimentiEnteTable.renderRows();
      
      for(let elem of this.trasferimentiEnte) {
        let index = this.trasferimentiEnteFiltered.findIndex(element => 
          element.causale.id == elem.causale.id);
  
        if(index != -1) {
          this.trasferimentiEnteFiltered[index].importo = (this.trasferimentiEnteFiltered[index].importo*1 + elem.importo*1);
        } else {
          let trasferimento = new TrasferimentoGreg();
          trasferimento.comune = elem.comune;
          trasferimento.causale = elem.causale;
          trasferimento.importo = elem.importo;
          this.trasferimentiEnteFiltered.push(trasferimento);
        }
      }
       this.trasferimentiEnteTableByCausale.renderRows();
    }

    if(this.trasferimentiComune.length > 0) {
      this.totaleComune = this.getTotaleTrasferimentiComune();
      // this.trasferimentiComuneTable.renderRows();
      
      for(let elem of this.trasferimentiComune) {
        let index = this.trasferimentiComuneFiltered.findIndex(element => 
          element.causale.id == elem.causale.id);
  
        if(index != -1) {
          this.trasferimentiComuneFiltered[index].importo = (this.trasferimentiComuneFiltered[index].importo*1 + elem.importo*1);
        } else {
          let trasferimento = new TrasferimentoGreg();
          trasferimento.comune = elem.comune;
          trasferimento.causale = elem.causale;
          trasferimento.importo = elem.importo;
          this.trasferimentiComuneFiltered.push(trasferimento);
        }
      }
      // this.trasferimentiComuneTableByCausale.renderRows();
    }
  }

  salvaModifiche(event) {
		this.client.nomemodello=SECTION.CODMODELLOA2;

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
            causali: this.client.getCausali(this.rendicontazione.idRendicontazioneEnte),
            trasferimenti: this.client.getTrasferimentiA2(this.rendicontazione.idRendicontazioneEnte),
          })
          .subscribe(({causali, trasferimenti}) => {
            this.causaliBeforeSave = causali;
            this.trasferimentiEnteBeforeSave = trasferimenti.trasferimentiEnteComune;
            this.trasferimentiComuneBeforeSave = trasferimenti.trasferimentiComuneEnte;
            if(this.causaliBeforeSave.length!=this.listaCausaliInitial.length){
              modify=true;
            } else{
              this.causaliBeforeSave.forEach((elem, index) => {
                if(elem.descrizione !== this.listaCausaliInitial[index].descrizione){
                  modify = true;
                }
              });
            }
            
            if(this.trasferimentiEnteBeforeSave.length !== this.trasferimentiEnteInitial.length){
              modify = true;
            } else {
              this.trasferimentiEnteBeforeSave.forEach((elem, index) => {
                if(elem.comune.desComune !=this.trasferimentiEnteInitial[index].comune.desComune){
                  modify = true;
                }else if(elem.causale.descrizione !=this.trasferimentiEnteInitial[index].causale.descrizione){
                  modify = true;
                } else if(elem.importo !=this.trasferimentiEnteInitial[index].importo){
                  modify = true;
                }
              });
            }

            if(this.trasferimentiComuneBeforeSave.length !== this.trasferimentiComuneInitial.length){
              modify = true;
            } else {
              this.trasferimentiComuneBeforeSave.forEach((elem, index) => {
                if(elem.comune.desComune !=this.trasferimentiComuneInitial[index].comune.desComune){
                  modify = true;
                }else if(elem.causale.descrizione !=this.trasferimentiComuneInitial[index].causale.descrizione){
                  modify = true;
                } else if(elem.importo !=this.trasferimentiComuneInitial[index].importo){
                  modify = true;
                }
              });
            }
            if(modify){
              this.errorMessage.error.descrizione  = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
              this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
              this.client.spinEmitter.emit(false);
              return;
            } else {
              //if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
				if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
				&& this.cronologiaMod.cronologia.notaEnte == null) {
                this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                this.client.spinEmitter.emit(false);
                return;
              } else {
                this.client.spinEmitter.emit(true);
                let salva: SalvaModelloA2Greg = new SalvaModelloA2Greg();
                salva.idEnte = this.rendicontazione.idSchedaEnte;
                salva.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                salva.causali = this.listaCausali;
                if(this.listaCausali.length > 0)
                  salva.trasferimentiEnteComune = this.trasferimentiEnte;
                else 
                  salva.trasferimentiEnteComune = [];
                salva.trasferimentiComuneEnte = this.trasferimentiComune;
                salva.cronologia = this.cronologiaMod.cronologia;
                salva.profilo = this.client.profilo;
                this.client.salvaModelloA2(salva).subscribe(data => {
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
//                    this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOA2);
                    this.refillInitals();
                    if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                      this.pulsanti.inviamodelli(this.rendicontazione.idRendicontazioneEnte);
                    }
                    else{
                      this.toastService.showSuccess({text: data.messaggio});
                      this.client.spinEmitter.emit(false);
                    }
                  }
                }, err => {
                  this.client.spinEmitter.emit(false);
                });
              }
            }
          });
        }
      });
  }

  @HostListener('document:changeTabEvent')
  checkEdited(event) {
      if(this.listaCausali.length !== this.listaCausaliInitial.length 
        || this.trasferimentiEnte.length !== this.trasferimentiEnteInitial.length
        || this.trasferimentiComune.length !== this.trasferimentiComuneInitial.length) {
          document.dispatchEvent(this.client.notSavedEvent);
          return;
      }

       this.ischanged();
      if(this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
      else        document.dispatchEvent(this.client.changeTabEmitter);
  }
  
  refillInitals() {
    this.listaCausaliInitial = [];
    this.trasferimentiEnteInitial = [];
    this.trasferimentiComuneInitial = [];
    this.listaCausali.forEach(elem => {this.listaCausaliInitial.push(elem)});
    this.trasferimentiEnte.forEach(elem => {this.trasferimentiEnteInitial.push(elem)});
    this.trasferimentiComune.forEach(elem => {this.trasferimentiComuneInitial.push(elem)});
  }

  esportaexcel() {
		this.ischanged();
		if (this.ischangedvar) {
			this.toastService.showError({ text: this.erroreexport });
		}
		else {
			this.client.spinEmitter.emit(true);
      let esporta: EsportaModelloA2Greg = new EsportaModelloA2Greg();
      esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
      esporta.causali = this.listaCausali;
      esporta.denominazioneEnte = this.datiEnte.denominazione;
      if(this.listaCausali.length > 0) {
        esporta.trasferimentiEnteComune = this.trasferimentiEnte;
        esporta.trasferimentiEnteComuneFiltered = this.trasferimentiEnteFiltered;
      } else {
        esporta.trasferimentiEnteComune = [];
        esporta.trasferimentiEnteComuneFiltered = [];
      }
      esporta.trasferimentiComuneEnte = this.trasferimentiComune;
      esporta.trasferimentiComuneEnteFiltered = this.trasferimentiComuneFiltered;
      
      this.client.esportaModelloA2(esporta).subscribe(res => {
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
      }, err => {
        this.client.spinEmitter.emit(false);
      });
		}
	}

	ischanged() {
		this.ischangedvar = false;
      this.listaCausali.forEach((elem, index) => {
        if(this.listaCausaliInitial[index] !== elem) {
          this.ischangedvar = true;
        }
      });
        
      this.trasferimentiEnte.forEach((elem, index) => {
        if(this.trasferimentiEnteInitial[index] !== elem) {
         this.ischangedvar = true;
        }
      });
      
      this.trasferimentiComune.forEach((elem, index) => {
        if(this.trasferimentiComuneInitial[index] !== elem) {
        this.ischangedvar = true;
        }
      });
	}
}


