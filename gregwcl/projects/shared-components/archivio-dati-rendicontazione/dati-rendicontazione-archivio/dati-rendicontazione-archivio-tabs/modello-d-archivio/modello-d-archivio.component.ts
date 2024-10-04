import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { DettaglioVoceModD } from '@greg-app/app/dto/DettaglioVoceModD';
import { RendicontazioneModD } from '@greg-app/app/dto/RendicontazioneModD';
import { TipoVoceD } from '@greg-app/app/dto/TipoVoceD';
import { VoceModD } from '@greg-app/app/dto/VoceModD';
import { VociRendModD } from '@greg-app/app/dto/VociRendModD';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { forkJoin } from 'rxjs';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ERRORS, OPERAZIONE,  RENDICONTAZIONE_STATUS,  SECTION } from '@greg-app/constants/greg-constants';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { CampiVociRendModD } from '@greg-app/app/dto/CampiVociRendModD';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { EsportaModelloDGreg } from '@greg-app/app/dto/EsportaModelloDGreg';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { CronologiaModelliArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/cronologia-modelli-archivio/cronologia-modelli-archivio.component';


@Component({
  selector: 'app-modello-d-archivio',
  templateUrl: './modello-d-archivio.component.html',
  styleUrls: ['./modello-d-archivio.component.css']
})
export class ModelloDArchivioComponent implements OnInit {

  customRegexp: RegExp = new RegExp(/^(\-?\d{0,12})(\,?\d{0,2})$/g);

  navigation: Navigation;
  
  @ViewChild(CronologiaModelliArchivioComponent, {static: false}) cronologiaMod: CronologiaModelliArchivioComponent;
//  @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;

  // visible: boolean = false;
  idRendicontazioneEnte;

  modelDData: Array<VoceModD> = [];
  columnsDmodelData: Array<TipoVoceD>; 
  columnsDmodel: Array<string> = []; // Lista dei nomi delle colonne x tabella
  rowTable: Array<DettaglioVoceModD> = [];
  rendicontazioneModD: RendicontazioneModD;
  rendicontazioneModDExport: RendicontazioneModD;

  rowParteAccantonata: Array<DettaglioVoceModD> = [];
  thParteAccantonata: DettaglioVoceModD;
  columnsParteAccantonata: Array<string> = ['Titolo', 'Totale'];
  sumParteAccantonata: number = 0;

  rowParteVincolata: Array<DettaglioVoceModD> = [];
  thParteVincolata: DettaglioVoceModD;
  columnsParteVincolata: Array<string> = ['Titolo', 'Totale'];
  sumParteVincolata: number = 0;

  rowParteInvestimenti: Array<DettaglioVoceModD> = [];
  thParteInvestimenti: DettaglioVoceModD;
  columnsParteInvestimenti: Array<string> = ['Titolo', 'Totale'];
  sumParteInvestimenti: number = 0;

  TData: VociRendModD[] = []; // dati della tabella principale
  PAData: VociRendModD[] = []; // dati della tabella parte accantonata
  firstPAData: VociRendModD;
  PVData: VociRendModD[] = []; // dati della tabella parte vincolata
  firstPVData: VociRendModD;
  PIData: VociRendModD[] = []; // dati della tabella parte investimenti
  firstPIData: VociRendModD;
  tabtranche : ModelTabTranche;
  visibileSection: boolean;
  espansa:boolean = false;
  state: string = 'default';

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

  mostrasalva: boolean = true;
  flagNegativeT: boolean = false;
  flagNegativeP: boolean = false;

  rendicontazione: RendicontazioneEnteGreg;
  rendicontazioneModDInitial: RendicontazioneModD;  
  infoOperatore: RendicontazioneEnteGreg;
  titolo: string;
  infoOperatoreBeforeSave: RendicontazioneEnteGreg;
  statoInitial: string;
  rendicontazioneModDBeforeSave: RendicontazioneModD;
  erroreStato: string;
  erroreModifica: string;
  datiEnte: DatiEnteGreg;

  constructor(public client: GregBOClient, private router: Router, private route: ActivatedRoute, 
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
		this.client.nomemodello=SECTION.CODMODELLOD;
    forkJoin({
      tipoVoceD: this.client.getTipoVoceD(),
      voceModD: this.client.getVoceModD(),

      rendicontazione: this.client.getRendicontazioneModD(this.rendicontazione.idRendicontazioneEnte),
      infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
      tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOD),
      erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
      erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
      erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
    })
    .subscribe(({tipoVoceD, voceModD, rendicontazione, infoOperatore,tranche,erroreexport, erroreStato, erroreModifica}) => {
      this.infoOperatore = infoOperatore;
      this.statoInitial = infoOperatore.statoRendicontazione.codStatoRendicontazione;
      this.columnsDmodelData = tipoVoceD as TipoVoceD[];
      this.initColumns(this.columnsDmodelData);
      this.modelDData = voceModD as VoceModD[];
      this.initVociModD();
      this.rendicontazioneModD = rendicontazione;
      this.rendicontazioneModDExport = rendicontazione;
      this.initValueRendicontazioneIntables();
      this.initCalc();
      this.refillInitials();
      this.erroreexport = erroreexport.testoMsgApplicativo;
      this.tabtranche = tranche;
      if(this.tabtranche!=null){
        this.titolo=this.tabtranche.desEstesaTab;
//        this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
     }
      this.erroreStato = erroreStato.testoMsgApplicativo;
      this.erroreModifica = erroreModifica.testoMsgApplicativo;
     
			this.client.mostrabottoniera=true;
      this.client.spinEmitter.emit(false);
    },
    err => {this.client.mostrabottoniera=true;
    this.client.spinEmitter.emit(false)}
    );

  }

  // Inizializza le colonne della tabella principale del modello 
  initColumns(tipoVoce: TipoVoceD[]) {
    this.columnsDmodel.push('Titolo', 'Operatore')
        tipoVoce.forEach(element => {
          this.columnsDmodel.push(element.descTipoVoce)
        });
  }

  // Divide i dati per le tabelle del modello
  initVociModD() {
    let tabellaRow = this.modelDData.find(x => x.sezioneModello == "tabella");
    if (tabellaRow != undefined) {
      this.rowTable = tabellaRow.listaVoci;
    }

    let parteAccantonata = this.modelDData.find(x => x.sezioneModello == "parte accantonata");
    if (parteAccantonata != undefined) {
      this.thParteAccantonata = parteAccantonata.listaVoci[0];
      this.rowParteAccantonata = parteAccantonata.listaVoci;
      this.rowParteAccantonata.splice(0, 1);
    }

    let parteVincolata = this.modelDData.find(x => x.sezioneModello == "parte vincolata");
    if (parteVincolata != undefined) {
      this.thParteVincolata = parteVincolata.listaVoci[0];
      this.rowParteVincolata = parteVincolata.listaVoci;
      this.rowParteVincolata.splice(0, 1);
    }

    let parteInvestimenti = this.modelDData.find(x => x.sezioneModello == "parte destinata agli investimenti");
    if (parteInvestimenti != undefined) {
      this.thParteInvestimenti = parteInvestimenti.listaVoci[0];
      this.rowParteInvestimenti = parteInvestimenti.listaVoci;
      this.rowParteInvestimenti.splice(0, 1);
    }
    
  }

  // Ordina le righe e i dati delle righe per la proprieta ordinamento
  sortModelDTable() {
    this.TData.sort(function (a, b) {
      return - ( b.ordinamento - a.ordinamento );
    });
    this.rowTable.sort(function (a, b) {
      return - ( b.ordinamento - a.ordinamento );
    });
  }

  // Crea array dei valori per ogni tabella
  initValueRendicontazioneIntables() {
    this.TData = this.rendicontazioneModD.vociModello.slice(0, this.rowTable.length);
    this.sortModelDTable();

    let limitParteAccantonata = this.rowTable.length + this.rowParteAccantonata.length +1
    this.firstPAData = this.rendicontazioneModD.vociModello[this.rowTable.length];
    this.PAData = this.rendicontazioneModD.vociModello.slice(this.rowTable.length+1, limitParteAccantonata);

    let limitParteVincolata = limitParteAccantonata + this.rowParteVincolata.length +1
    this.firstPVData = this.rendicontazioneModD.vociModello[limitParteAccantonata];
    this.PVData = this.rendicontazioneModD.vociModello.slice(limitParteAccantonata+1, limitParteVincolata);

    let limitParteInvestimenti = limitParteVincolata + this.rowParteInvestimenti.length +1
    this.firstPIData = this.rendicontazioneModD.vociModello[limitParteVincolata];
    this.PIData = this.rendicontazioneModD.vociModello.slice(limitParteVincolata+1, limitParteInvestimenti);
  }

  // Calcola i valori al caricamento del modello
  initCalc() {
    this.rendicontazioneModD.vociModello.forEach(element => {
      element.campi.forEach(el => {
        if (el.value != null && el.value != undefined) {
          el.value = this.transform(parseFloat(el.value));
        }
      });
    });
    this.calcTableValue();
    this.calcTotParteAccantonata();
    this.calcTotParteVincolata();
  }

  // Calcola il totale per la tabella parte accantonata 
  calcTotParteAccantonata() {
    let tot: number = 0;
    for (let i = 0; i < this.rowParteAccantonata.length-1; i++) {
      tot += this.parsingFloat(this.PAData[i].campi[0].value);
    }
    
    this.PAData[this.PAData.length-1].campi[0].value = this.transform(tot);
    this.calcTotFinal();
  }

  // Calcola il totale per la tabella parte vincolata
  calcTotParteVincolata() {
    let tot: number = 0;
    for (let i = 0; i < this.rowParteAccantonata.length-1; i++) {
      tot += this.parsingFloat(this.PVData[i].campi[0].value);
    }
    this.PVData[this.PVData.length-1].campi[0].value = this.transform(tot);
    this.calcTotFinal();
  }

  // Calcoli effettuati ad ogni cambiamento dei valori
  calcTableValue() {
    // Oggetto fondo cassa 
    let fondoCassaTot;
    fondoCassaTot = this.TData.findIndex(el => el.codVoce == '01');
    fondoCassaTot = this.parsingFloat(this.TData[fondoCassaTot].campi[2].value);

    // Valori riga riscossioni e calcola il totale
    let riscossioniIndex = this.TData.findIndex(el => el.codVoce == '02');
    let riscossioniResidui = this.parsingFloat(this.TData[riscossioniIndex].campi[0].value);
    let riscossioniComp = this.parsingFloat(this.TData[riscossioniIndex].campi[1].value);
    let importototRis = riscossioniResidui + riscossioniComp;
    this.TData[riscossioniIndex].campi[2].value = this.transform(importototRis);

    // Valori della riga pagamenti e calcola il totale
    let pagamentiIndex = this.TData.findIndex(el => el.codVoce == '03');
    let pagamentiRes = this.parsingFloat(this.TData[pagamentiIndex].campi[0].value);
    let pagamanetiComp = this.parsingFloat(this.TData[pagamentiIndex].campi[1].value);
    let importototPag = pagamentiRes + pagamanetiComp;
    this.TData[pagamentiIndex].campi[2].value = this.transform(importototPag);

    // Valori saldo di cassa
    let saldoCassaIndex = this.TData.findIndex(el => el.codVoce == '04');

    // Calcola saldo di cassa al 31 dicembre
    this.saldoDiCassa(saldoCassaIndex, fondoCassaTot, importototRis, importototPag);

    // Valori della riga PAGAMENTI per azioni esecutive
    let pagPerAzioni;
    pagPerAzioni = this.TData.findIndex(el => el.codVoce == '05');
    let pagPerAzioniValue = this.parsingFloat(this.TData[pagPerAzioni].campi[2].value);

    // Valori della riga FONDO DI CASSA AL 31 DICEMBRE e calcola
    let fondoIndex = this.TData.findIndex(el => el.codVoce == '06');
    let saldoCassa = this.parsingFloat(this.TData[saldoCassaIndex].campi[2].value);
    let totFondoCassa = saldoCassa - pagPerAzioniValue;
    this.TData[fondoIndex].campi[2].value = this.transform(totFondoCassa);

    // Indice residui attivi e calcola il totale
    let residuiAttiviIndex = this.TData.findIndex(el => el.codVoce == '07');
    let residuiAttRes = this.parsingFloat(this.TData[residuiAttiviIndex].campi[0].value);
    let residuiAttComp = this.parsingFloat(this.TData[residuiAttiviIndex].campi[1].value);
    let totResAttivi = residuiAttRes + residuiAttComp;
    this.TData[residuiAttiviIndex].campi[2].value = this.transform(totResAttivi);

    //Indice accertamenti di tributi e calcola il totale della riga
    let accertamentiIndex = this.TData.findIndex(el => el.codVoce == '08');
    let accertamentiRes = this.parsingFloat(this.TData[accertamentiIndex].campi[0].value);
    let accertamentiComp = this.parsingFloat(this.TData[accertamentiIndex].campi[1].value);
    let totAcc =  accertamentiRes + accertamentiComp;
    this.TData[accertamentiIndex].campi[2].value = this.transform(totAcc);


    // Indice residui passivi e calcola il totale della riga
    let residuiPassiviIndex = this.TData.findIndex(el => el.codVoce == '09');
    let resPassRes = this.parsingFloat(this.TData[residuiPassiviIndex].campi[0].value);
    let resPassComp = this.parsingFloat(this.TData[residuiPassiviIndex].campi[1].value);
    let totResPassivi = resPassRes + resPassComp;
    this.TData[residuiPassiviIndex].campi[2].value = this.transform(totResPassivi);

    // Oggetto fondo pluriennale spese correnti
    let FLSpeseCorrenti;
    FLSpeseCorrenti = this.TData.findIndex(el => el.codVoce == '10');
    FLSpeseCorrenti = this.parsingFloat(this.TData[FLSpeseCorrenti].campi[2].value);

    // Oggetto fondo pluriennale in conto capitale
    let FLSpeseInContoCapitale;
    FLSpeseInContoCapitale = this.TData.findIndex(el => el.codVoce == '11');
    FLSpeseInContoCapitale = this.parsingFloat(this.TData[FLSpeseInContoCapitale].campi[2].value);
    // Oggetto risultato di amministrazione
    let risultatoIndex = this.TData.findIndex(el => el.codVoce == '12');
    let risultato;
    risultato = this.TData[risultatoIndex].campi[2].value;

    // Calcolo risultati di amministrazione
    risultato = 
        totFondoCassa +
        totResAttivi -
        totResPassivi -
        FLSpeseCorrenti -
        FLSpeseInContoCapitale;
    
    this.flagNegativeT = risultato < 0;
    
    this.TData[risultatoIndex].campi[2].value = this.transform(risultato);
    this.calcTotFinal();
  }

  // Calcola il saldo di cassa al 31 dicembre
  saldoDiCassa(saldoCassaIndex, fondoCassaTot, riscossioni, pagamenti) {
    let tot = fondoCassaTot + riscossioni - pagamenti;
    this.TData[saldoCassaIndex].campi[2].value = this.transform(tot);
  }

  parsingFloat(el) {
    if (el == '' || el == '-') el = null;
    el = el ? parseFloat(el.toString().replaceAll('.','').replace(',','.')) : el;
    return el;
  }


  // Calcola totale parte disponibile
  calcTotFinal() {
    let totInvestimenti;
    totInvestimenti = this.PIData.find(el => el.codVoce === '28');
    let totTData = this.TData[this.TData.length-1].campi[2].value ? this.parsingFloat(this.TData[this.TData.length-1].campi[2].value) : 0;
    let totPAD = this.PAData[this.PAData.length-1].campi[0].value ? this.parsingFloat(this.PAData[this.PAData.length-1].campi[0].value) : 0;
    let totPVD = this.PVData[this.PVData.length-1].campi[0].value ? this.parsingFloat(this.PVData[this.PVData.length-1].campi[0].value) : 0;
    totInvestimenti = totInvestimenti.campi[0].value ? this.parsingFloat(totInvestimenti.campi[0].value) : 0;
    let tot = 
              totTData -
              totPAD -
              totPVD - 
              totInvestimenti;
    
    this.flagNegativeP = tot < 0;
    this.PIData[this.PIData.length -1].campi[0].value = this.transform(tot);
  }

  changeKey(key:number ,valore: any,i:number, data:string) {
    switch(data) {
      case 'TData': {
        if(valore=="" || valore == null || valore ==="-"){
          this.TData[key].campi[i].value = null;
        }
        else {
          if(valore.indexOf('.') !== -1) {
            if(valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2)
                valore = [valore, '0'].join('');
                this.TData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));  
          }
          else
            this.TData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
        }
        break;
      }
      case 'PAData': {
        if(valore=="" || valore == null){
          this.PAData[key].campi[i].value = null;
        }
        else {
          if(valore.indexOf('.') !== -1) {
            if(valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2)
                valore = [valore, '0'].join('');
                this.PAData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.'))); 
          }
          else
            this.PAData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
        }
        break;
      }
      case 'PVData': {
        if(valore=="" || valore == null){
          this.PVData[key].campi[i].value = null;
        }
        else {
          if(valore.indexOf('.') !== -1) {
            if(valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2)
                valore = [valore, '0'].join('');
                this.PVData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.'))); 
          }
          else
            this.PVData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
        }
        break;
      }
      case 'PIData': {
        if(valore=="" || valore == null){
          this.PIData[key].campi[i].value = null;
        }
        else {
          if(valore.indexOf('.') !== -1) {
            if(valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',')+1, valore.length)).length != 2)
                valore = [valore, '0'].join('');
                this.PIData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));  
          }
          else
            this.PIData[key].campi[i].value = this.transform(parseFloat(valore.toString().replaceAll('.','').replace(',','.')));
        }
        break;
      }
    }
    
      
  }

  apricronologia(){
    if (this.espansa)
    this.espansa=false;
    else
    this.espansa=true;
    this.state = (this.state === 'default' ? 'rotated' : 'default');
  }

  salvaModifiche(event) {
	this.client.nomemodello=SECTION.CODMODELLOD;
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
          this.client.getRendicontazioneModD(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
              this.rendicontazioneModDBeforeSave = response;
              this.rendicontazioneBeforeSaveTransform();
              if(this.rendicontazioneModDBeforeSave && this.rendicontazioneModDBeforeSave != null) {
                this.rendicontazioneModDBeforeSave.vociModello.forEach((element, index) => {
                  element.campi.forEach((el, i) => {
                    if(el.show && el.value !== this.rendicontazioneModDInitial.vociModello[index].campi[i].value) {
                      modify = true;
                    }
                  });
                });
              }
              if(modify){
                this.errorMessage.error.descrizione  = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage , errorDesc :   this.errorMessage.error.descrizione }));
                this.client.spinEmitter.emit(false);
                return;
            } else {
 //             if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
				if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
				&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "")) {
                this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : "Inserire una nota per l'ente" }))
                this.client.spinEmitter.emit(false);
                return;
              } else {
                this.client.spinEmitter.emit(true);
                this.rendicontazioneModD.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                this.rendicontazioneModD.profilo = this.client.profilo;
                this.rendicontazioneModD.vociModello.forEach(element => {
                  element.campi.forEach(el => {
                    el.value = this.parsingFloat(el.value);
                  });
                });
                this.client.saveModD(this.rendicontazioneModD, this.cronologiaMod.cronologia.notaEnte, this.cronologiaMod.cronologia.notaInterna)
                .subscribe(
                  (data: ResponseSalvaModelloGreg) => {
                    if(data.warnings && data.warnings.length > 0 && this.client.operazione != OPERAZIONE.INVIAMODELLI){
                      for(let warn of data.warnings){
                      this.errorMessage.error.descrizione = warn;
                      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage , errorDesc : warn }))
                      }
                    }
                    if(data.errors && data.errors.length > 0 && this.client.operazione != OPERAZIONE.INVIAMODELLI){
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
//                    this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOD);
                    
                    this.initCalc();
                    this.refillInitials();
                    if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                      this.pulsanti.inviamodelli(this.rendicontazione.idRendicontazioneEnte);
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

  @HostListener('document:changeTabEvent')
  checkEdited(event) {
   this.ischanged();
    if(this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
    else        document.dispatchEvent(this.client.changeTabEmitter);
  }

  refillInitials() {
    this.rendicontazioneModDInitial = new RendicontazioneModD();
    this.rendicontazioneModDInitial.annoGestione = this.rendicontazioneModD.annoGestione;
    this.rendicontazioneModDInitial.denominazioneEnte = this.rendicontazioneModD.denominazioneEnte;
    this.rendicontazioneModDInitial.idRendicontazioneEnte = this.rendicontazioneModD.idRendicontazioneEnte;
    this.rendicontazioneModDInitial.idSchedaEnteGestore = this.rendicontazioneModD.idSchedaEnteGestore;
    this.rendicontazioneModDInitial.profilo = this.rendicontazioneModD.profilo;
    this.rendicontazioneModDInitial.vociModello = [];
    for(let voce of this.rendicontazioneModD.vociModello) {
      let newVoce = new VociRendModD();
      newVoce.idVoce = voce.idVoce;
	    newVoce.codVoce = voce.codVoce;
      newVoce.descrizioneVoce = voce.descrizioneVoce;
      newVoce.operatore = voce.operatore;
      newVoce.ordinamento = voce.ordinamento;
      newVoce.campi = [];
      for(let campo of voce.campi) {
        let newCampo = new CampiVociRendModD();
        newCampo.show = campo.show;
        newCampo.value = campo.value;
        newCampo.voce = campo.voce;
        newVoce.campi.push(newCampo);
      }
      this.rendicontazioneModDInitial.vociModello.push(newVoce);
    }
  }

    esportaexcel() {
		this.ischanged();
		if (this.ischangedvar) {
			this.toastService.showError({ text: this.erroreexport });
		}
		else {
		this.client.spinEmitter.emit(true);
		let esporta: EsportaModelloDGreg = new EsportaModelloDGreg();
		esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
    this.rendicontazioneModDExport.vociModello.forEach(element => {
      element.campi.forEach(el => {
        el.value = this.parsingFloat(el.value);
      });
    });
		this.rendicontazioneModDExport.denominazioneEnte = this.datiEnte.denominazione;
		esporta.datiD = this.rendicontazioneModDExport;
		esporta.vociD = this.columnsDmodelData;
		this.client.esportaModelloD(esporta)
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
            this.initCalc();
					}
					this.toastService.showSuccess({ text: messaggio });
					this.client.spinEmitter.emit(false);
				},
				err => {
          this.initCalc();
					this.client.spinEmitter.emit(false);
				}
			);
			}
	}

	ischanged() {
		this.ischangedvar = false;
    if(this.rendicontazioneModD && this.rendicontazioneModD != null) {
      this.rendicontazioneModD.vociModello.forEach((element, index) => {
        element.campi.forEach((el, i) => {
          if(el.show && el.value !== this.rendicontazioneModDInitial.vociModello[index].campi[i].value) {
            this.ischangedvar = true;
          }
        });
      });
    }
	}

  rendicontazioneBeforeSaveTransform() {
    for(let i=0; i<this.rendicontazioneModDBeforeSave.vociModello.length; i++){
      for(let j=0; j< this.rendicontazioneModDBeforeSave.vociModello[i].campi.length; j++){
        if (this.rendicontazioneModDBeforeSave.vociModello[i].campi[j].value != null && this.rendicontazioneModDBeforeSave.vociModello[i].campi[j].value != undefined) {
          this.rendicontazioneModDBeforeSave.vociModello[i].campi[j].value = this.transform(parseFloat(this.rendicontazioneModDBeforeSave.vociModello[i].campi[j].value));
        }
      }
    }
  }
}
