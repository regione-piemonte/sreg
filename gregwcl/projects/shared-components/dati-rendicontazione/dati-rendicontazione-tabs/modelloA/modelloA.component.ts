import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Navigation, Router} from '@angular/router';
import {EnteGreg} from '@greg-app/app/dto/EnteGreg';
import {VociModelloA1Greg} from '@greg-app/app/dto/VociModelloA1Greg';
import {DatiModelloA1Greg} from '@greg-app/app/dto/DatiModelloA1Greg';
import {CronologiaModelliComponent} from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import {GregBOClient} from '@greg-app/app/GregBOClient';
import {AppToastService} from '@greg-shared/toast/app-toast.service';
import {ERRORS, OPERAZIONE,   RENDICONTAZIONE_STATUS,   SECTION,  VOCE_IMPORTO_MOD_A, VOCE_TESTUALE_MOD_A} from '@greg-app/constants/greg-constants';
import {forkJoin} from 'rxjs';
import {TitoloModA} from '@greg-app/app/dto/TitoloModA';
import {MatDialog, MatTableDataSource} from '@angular/material';
import {CronologiaGreg} from '@greg-app/app/dto/CronologiaGreg';
import {VoceModA} from '@greg-app/app/dto/VoceModA';
import {DettaglioVoceModA} from '@greg-app/app/dto/DettaglioVoceModA';
import {VociRendModD} from '@greg-app/app/dto/VociRendModD';
import {GregError} from '@greg-app/shared/error/greg-error.model';
import {ResponseSalvaModelloDGreg} from '@greg-app/app/dto/ResponseSalvaModelloDGreg';
import {RendicontazioneModD} from '@greg-app/app/dto/RendicontazioneModD';
import {RendicontazioneModAPart3} from '@greg-app/app/dto/RendicontazioneModAPart3';
import {GregErrorService} from '@greg-app/shared/error/greg-error.service';
import {SalvaModelloA2Greg} from '@greg-app/app/dto/SalvaModelloA2Greg';
import {SalvaModelloA} from '@greg-app/app/dto/SalvaModelloA';
import {VociModelloA} from '@greg-app/app/dto/VociModeloA';
import {TipologiaModA} from '@greg-app/app/dto/TipologiaModA';
import {InfoRendicontazioneOperatore} from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import {VociRendModAPart3} from '@greg-app/app/dto/VociRendModAPart3';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { PrestazioniModAGreg } from '@greg-app/app/dto/PrestazioniModAGreg';
import { TargetUtenzaGreg } from '@greg-app/app/dto/TargetUtenzaGreg';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { PrestazioneUtenzaModAGreg } from '@greg-app/app/dto/PrestazioneUtenzaModAGreg';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { MotivazionePopupComponent } from '../motivazione-popup/motivazione-popup.component';
import { EsportaModelloA } from '@greg-app/app/dto/EsportaModelloA';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';

@Component({
    selector: 'app-modelloA',
    templateUrl: './modelloA.component.html',
    styleUrls: ['./modelloA.component.css'],
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
export class ModelloAComponent implements OnInit {

    navigation: Navigation;
    ente: EnteGreg;
    a: any[];

    listaTipologie: Array<TipologiaModA>;
    tabtranche : ModelTabTranche;
    idRendicontazioneEnte;
    visibileSection: boolean;
    espansa: boolean;
    state = 'default';
    statoRendicontazione: string;
    totaleTitoli = '';
    totaleTitoliSelezionati = '';
    // visible: boolean;
    infoOperatore: RendicontazioneEnteGreg;
    tipoEnte: any;
    showVoceInput: boolean;
    columnsTitleTotaliParziali: Array<TitoloModA>; // Lista dei titoli delle colonne per tabella totale parziali
    modelAData: VociModelloA;
    modelADataInitial: VociModelloA;
    rendicontazioneModAPart3: Array<VociRendModAPart3>;
  ischangedvar:boolean;
   erroreexport :string;
    errorMessage = {
        error: {descrizione: ''},
        message: '',
        name: '',
        status: '',
        statusText: '',
        url: '',
        date: Date
    };

    @ViewChild(CronologiaModelliComponent, {static: false}) cronologiaMod: CronologiaModelliComponent;
    @ViewChild(PulsantiFunzioniComponent, {static: false}) pulsantiMod: PulsantiFunzioniComponent;

    mostrasalva = true;
    private salvaModelloA: SalvaModelloA;
    importoavvalorato : boolean;
    titolo: string;
    rendicontazioneModABeforeSave: VociModelloA;
    infoOperatoreBeforeSave: RendicontazioneEnteGreg;
    statoInitial: string;
    erroreModifica: string;
    erroreStato: string;
    tooltip: string;

    modelloA: boolean = false;
  datiEnte: DatiEnteGreg;
rendicontazione:RendicontazioneEnteGreg;
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
	    this.client.nomemodello=SECTION.CODMODELLOA;
        forkJoin({
            listaVociModA: this.client.getListaVociModA(this.rendicontazione.idRendicontazioneEnte),
            infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
            tranche:this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte,SECTION.CODMODELLOA),
            erroreexport:this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
            erroreStato:this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
            erroreModifica:this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
            tooltip: this.client.getMsgInformativi(SECTION.MODATOOLTIP)
            })
            .subscribe(({listaVociModA, tranche,erroreexport, erroreStato, erroreModifica, infoOperatore, tooltip}) => {
                this.modelloA = this.client.azioni.get('ModelloA')[0];
                this.infoOperatore = infoOperatore;
                this.statoInitial = this.infoOperatore.statoRendicontazione.codStatoRendicontazione;
                this.modelAData = listaVociModA as VociModelloA;
                this.tooltip = tooltip[0].testoMsgInformativo;
                this.ordinaModel(); // ordina elementi del modello  
                this.transformAll(); // converte cifre del modello
                this.calcolaSubtotali(); // calcola tutti i subtotali
			    //verifica se il campo importo titolo altro 2 pieno abilita la voce testuale
				for (const titolo of this.modelAData.listaTitoli) {
           			 for (const tipologia of titolo.listaTipologie) {
                		for (const voce of tipologia.listaVoci) {
							if (titolo.codTitolo == VOCE_IMPORTO_MOD_A.COD_TITOLO
								&& tipologia.codTipologia == VOCE_IMPORTO_MOD_A.COD_TIPOLOGIA
								&& voce.codVoce == VOCE_IMPORTO_MOD_A.COD_VOCE) {
									if (voce.valoreNumb !== null && voce.valoreNumb!=="0,00" && voce.valoreNumb!="" )
									this.importoavvalorato = true;
									else
									this.importoavvalorato = false;
							}
						}
					}
				}
                this.refillInitials(); // crea copia dati iniziali
				this.tabtranche = tranche;
                if(this.tabtranche!=null){
                    this.titolo=this.tabtranche.desEstesaTab;
                    this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
                 }
                
                this.erroreStato = erroreStato.testoMsgApplicativo;
                this.erroreModifica = erroreModifica.testoMsgApplicativo;
				this.erroreexport = erroreexport.testoMsgApplicativo;
                this.rendicontazioneModAPart3 = this.modelAData.listaVociModAPart3;
 				this.client.mostrabottoniera=true;
                this.client.spinEmitter.emit(false);
            },
            err => {
				this.client.mostrabottoniera=true;
                this.client.spinEmitter.emit(false);
            }
            );

    }

    calcolaSubtotali() {
        for (const titolo of this.modelAData.listaTitoli) {
            for (const tipologia of titolo.listaTipologie) {
                for (const voce of tipologia.listaVoci) {
                    if (voce.prestazioni != null ) {
                        if (voce.prestazioni.prestazioniRS.length > 0) {
                            voce.prestazioni.prestazioniRS.forEach((rs, i) => {
                                this.calcolaSubtotalePrest(voce, i, 'sr', titolo);
                            });
                        }
                        if (voce.prestazioni.prestazioniCD.length > 0) {
                            voce.prestazioni.prestazioniCD.forEach((cd, i) => {
                                this.calcolaSubtotalePrest(voce, i, 'cd', titolo);
                            });
                        }
                    }
                }
            }
            this.calcolaSubtotale(titolo);
        }

    }

    downloadReport() {
    }

    salvaModifiche(event) {
		this.client.nomemodello=SECTION.CODMODELLOA;

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
                this.client.getListaVociModA(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {
                    this.rendicontazioneModABeforeSave = response;
                    this.ordinaModelBeforeSave();
                    this.rendicontazioneBeforeSaveTransform();
                    if (this.rendicontazioneModABeforeSave && this.rendicontazioneModABeforeSave != null) {
                        this.rendicontazioneModABeforeSave.listaTitoli.forEach((titolo, i) => {
                            titolo.listaTipologie.forEach((tipologia, j) => {
                                if (tipologia.valore !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].valore) {
                                    modify = true;
                                }
                                if (tipologia.listaVoci != null || tipologia.listaVoci.length > 0) {
                                    tipologia.listaVoci.forEach((voce, k) => {
                                        if (voce.valoreNumb !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb) {
                                            if(!(voce.valoreNumb==null && this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb=="0,00")){
                                                modify = true;
                                            }
                                        }
                                        if(voce.valoreText!=null){
                                            if (voce.valoreText !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreText) {
                                                modify = true;
                                            }
                                        }
                                        if (voce.prestazioni != null) {
                                            voce.prestazioni.prestazioniRS.forEach((prestRs, r) => {
                                                prestRs.listaTargetUtenza.forEach((ut, u) => {
                                                    if (ut.valore !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[r].listaTargetUtenza[u].valore) {
                                                        modify = true;
                                                    }
                                                });
                                            });
            
                                            voce.prestazioni.prestazioniCD.forEach((prestCd, c) => {
                                                prestCd.listaTargetUtenza.forEach((ut, u) => {
                                                    if (ut.valore !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[c].listaTargetUtenza[u].valore) {
                                                        modify = true;
                                                    }
                                                });
                                            });
            
                                        }
            
                                    });
                                }
                            });
                        });
            
                        this.rendicontazioneModABeforeSave.listaVociModAPart3.forEach((element, i) => {
                            if(element.valore !== this.modelADataInitial.listaVociModAPart3[i].valore) {
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
//                    if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
					if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0]) 
						&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length==0)) {
                        this.errorMessage.error.descrizione = 'Inserire una nota per l\'ente';
                        this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: 'Inserire una nota per l\'ente' }));
                        this.client.spinEmitter.emit(false);
                        return;
                    } else {
                        this.salvaModelloA = new SalvaModelloA();
                        this.salvaModelloA.idEnte = this.rendicontazione.idSchedaEnte;
                        this.salvaModelloA.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                        this.salvaModelloA.rendicontazioneModAPart3 = this.rendicontazioneModAPart3;
                        this.salvaModelloA.rendicontazioneModAPart1 = this.modelAData;
                        this.salvaModelloA.cronologia = this.cronologiaMod.cronologia;
            
                        for (const elem of this.salvaModelloA.rendicontazioneModAPart3) {
                            elem.valore = this.parsingFloat(elem.valore);
                        }
                        for (const titolo of this.salvaModelloA.rendicontazioneModAPart1.listaTitoli) {
                            for (const tipologia of titolo.listaTipologie) {
                                tipologia.valore = this.parsingFloat(tipologia.valore);
                                for (const voce of tipologia.listaVoci) {
                                    if(!(titolo.codTitolo == VOCE_TESTUALE_MOD_A.COD_TITOLO 
                                        && tipologia.codTipologia == VOCE_TESTUALE_MOD_A.COD_TIPOLOGIA 
                                        && voce.codVoce == VOCE_TESTUALE_MOD_A.COD_VOCE)) {
                                            voce.valoreNumb = this.parsingFloat(voce.valoreNumb);
                                    }
                                    if (voce.prestazioni != null) {
                                        for (const prestRs of voce.prestazioni.prestazioniRS) {
                                            for (const ut of prestRs.listaTargetUtenza) {
                                                ut.valore = this.parsingFloat(ut.valore);
                                            }
                                        }
                                        for (const prestCd of voce.prestazioni.prestazioniCD) {
                                            for (const ut of prestCd.listaTargetUtenza) {
                                                ut.valore = this.parsingFloat(ut.valore);
                                            }
                                        }
                                        for (const subRs of voce.prestazioni.subtotaliRS) {
                                            subRs.valore = this.parsingFloat(subRs.valore);
                                        }
                                        for (const subCd of voce.prestazioni.subtotaliCD) {
                                            subCd.valore = this.parsingFloat(subCd.valore);
                                        }
                                        for (const totRsCd of voce.prestazioni.totaliSRCD) {
                                            totRsCd.valore = this.parsingFloat(totRsCd.valore);
                                        }
                                    }
                                }
                            }
                        }
                        this.salvaModelloA.profilo = this.client.profilo;
                        this.client.spinEmitter.emit(true);
                        this.client.saveModelloA(this.salvaModelloA)
                            .subscribe(
                                (data: ResponseSalvaModelloGreg) => {
                                    this.transformAll();
                                    this.refillInitials();
                                    this.calcolaSubtotali(); 
                                    if (data.warnings && data.warnings.length > 0) {
                                        for (const warn of data.warnings) {
                                            this.errorMessage.error.descrizione = warn;
                                            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }));
                                        }
                                    }
                                    if (data.errors && data.errors.length > 0) {
                                        for (const err of data.errors) {
                                            this.errorMessage.error.descrizione = err;
                                            this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }));
                                        }
                                    }
                                    this.cronologiaMod.ngOnInit();
                                    this.cronologiaMod.espansa = false;
                                    this.cronologiaMod.state = 'rotated';
                                    this.cronologiaMod.apricronologia();
                                    this.cronologiaMod.cronologia.notaEnte = null;
                                    this.cronologiaMod.cronologia.notaInterna = null;
            
                                    //ricalcolo pulsantiera in caso di cambio stato
                                    this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOA);
            
                                    if (this.client.operazione == OPERAZIONE.INVIAMODELLI){
                                        this.pulsanti.inviamodelli(this.rendicontazione.idRendicontazioneEnte);
                                   }else if(this.client.operazione == OPERAZIONE.CHECK){
                                        this.check();
                                   } else{
                                        if(data.messaggio!=null && data.messaggio!=""){
                                            this.toastService.showSuccess({text: data.messaggio});
                                        }
                                        this.client.spinEmitter.emit(false);
                                    }
                                },
                                err => {
                                    this.transformAll();
                                    this.calcolaSubtotali(); 
                                    this.client.spinEmitter.emit(false);
                                }
                            );
                    }
                  }
                });
            }
        });
    }

    espandiPart2(voce: VoceModA) {
        voce.showPrestazioni = !voce.showPrestazioni;
        voce.rotate = (voce.rotate === 'default' ? 'rotated' : 'default');
    }

    calcolaSubtotale(titolo: TitoloModA) {
        let somma = 0;
        for (const tipologia of titolo.listaTipologie) {
            somma = somma * 1 + this.parsingFloat(tipologia.valore) * 1;
            for (const voce of tipologia.listaVoci) {
                // Escludo il campo testuale dal calcolo
                if(!(titolo.codTitolo == VOCE_TESTUALE_MOD_A.COD_TITOLO 
                    && tipologia.codTipologia == VOCE_TESTUALE_MOD_A.COD_TIPOLOGIA 
                    && voce.codVoce == VOCE_TESTUALE_MOD_A.COD_VOCE)) {
                        somma = somma * 1 + this.parsingFloat(voce.valoreNumb) * 1;
				if (titolo.codTitolo == VOCE_IMPORTO_MOD_A.COD_TITOLO
								&& tipologia.codTipologia == VOCE_IMPORTO_MOD_A.COD_TIPOLOGIA
								&& voce.codVoce == VOCE_IMPORTO_MOD_A.COD_VOCE) {
                                    if (voce.valoreNumb !== null && voce.valoreNumb!=="0,00" && voce.valoreNumb!="" )
									this.importoavvalorato = true;
									else 
									this.importoavvalorato = false;
							}
                }
				else {
					if (this.importoavvalorato == false)
					voce.valoreText=null;
				}
            }
        }
        titolo.totale = this.transform(somma);
        this.calcolaTotaleTuttiITitoli();
        this.calcolaTotaleTitoliSelezionati();
    }

    calcolaTotaleTuttiITitoli() {
        let somma = 0;
        for (const titolo of this.modelAData.listaTitoli) {
            somma = somma * 1 + this.parsingFloat(titolo.totale) * 1;
        }
        this.totaleTitoli = this.transform(somma);
    }

    calcolaTotaleTitoliSelezionati() {
        let somma = 0;
        for (const titolo of this.modelAData.listaTitoli) {
            if (titolo.codTitolo === '02' || titolo.codTitolo === '03') {
            somma = somma * 1 + this.parsingFloat(titolo.totale) * 1;
            }
        }
        this.totaleTitoliSelezionati = this.transform(somma);
    }

    calcolaSubtotalePrest(voce: VoceModA, u: number, sezione: string, titolo: TitoloModA) {
        switch (sezione) {
            case 'sr': {
                voce.prestazioni.prestazioniRS[u].listaTargetUtenza.forEach((el, i) =>{
                    let somma = 0;
                    for (const prest of voce.prestazioni.prestazioniRS) {
                        somma = somma * 1 + this.parsingFloat(prest.listaTargetUtenza[i].valore) * 1;
                    }
                    voce.prestazioni.subtotaliRS[i].valore = this.transform(somma);
                });
                break;
            }
            case 'cd': {
                voce.prestazioni.prestazioniCD[u].listaTargetUtenza.forEach((el, i) =>{
                    let somma = 0;
                    for (const prest of voce.prestazioni.prestazioniCD) {
                        somma = somma * 1 + this.parsingFloat(prest.listaTargetUtenza[i].valore) * 1;
                    }
                    voce.prestazioni.subtotaliCD[i].valore = this.transform(somma);
                });
                break;
            }
        }
        this.calcolaTotalePrest(voce, u, titolo);
    }

    calcolaTotalePrest(voce: VoceModA, u: number, titolo: TitoloModA) {
        voce.prestazioni.totaliSRCD.forEach((tot, j) => {         
            let somma = 0;
            for (const prest of voce.prestazioni.prestazioniRS) {
                somma = somma * 1 + this.parsingFloat(prest.listaTargetUtenza[j].valore) * 1;
            }
            for (const prest of voce.prestazioni.prestazioniCD) {
                somma = somma * 1 + this.parsingFloat(prest.listaTargetUtenza[j].valore) * 1;
            }
            voce.prestazioni.totaliSRCD[j].valore = this.transform(somma);
        });

        let sommaVoce = 0;
        for (let element of voce.prestazioni.totaliSRCD) {
            sommaVoce = sommaVoce * 1 + this.parsingFloat(element.valore) * 1;
        }
        voce.valoreNumb = this.transform(sommaVoce);
        this.calcolaSubtotale(titolo);
    }

    parsingFloat(el) {
        if (el == undefined || el == '') { el = null; }
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
        const result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
        if (this.isNumber(value)) {
            if (value != null && value != undefined) {
            const num = value.toFixed(Math.max(0, ~~decimalLength));
            return currencySign + (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
          }
        }
        return undefined;
      }

    changeKeyPrest(utenza: TargetUtenzaGreg, valore: any) {
        if (valore == '' || valore == null) {
            utenza.valore = null;
        } else {
            if (valore.indexOf('.') !== -1) {
                if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
                    valore = [valore, '0'].join('');
                }
                utenza.valore = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
            } else {
                utenza.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
            }
        }
    }

    changeKeyTipologia(tipologia: TipologiaModA, valore: any) {
        if (valore == '' || valore == null) {
            tipologia.valore = null;
        } else {
            if (valore.indexOf('.') !== -1) {
                if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
                    valore = [valore, '0'].join('');
                }
                    tipologia.valore = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
            } else {
                tipologia.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
            }
        }
    }

    changeKeyVoce(voce: VoceModA, valore: any) {
        if (valore == '' || valore == null) {
            voce.valoreNumb = null;
        } else {
            if (valore.indexOf('.') !== -1) {
                if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
                    valore = [valore, '0'].join('');
                }
                    voce.valoreNumb = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
            } else {
                voce.valoreNumb = this.transform(parseFloat(valore.toString().replace(',', '.')));
            }
        }
    }

    changeKeyPart3(element: VociRendModAPart3, valore: any) {
        if (valore == '' || valore == null) {
            element.valore = null;
        } else {
            if (valore.indexOf('.') !== -1) {
                if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
                    valore = [valore, '0'].join('');
                }
                    element.valore = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
            } else {
                element.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
            }
        }
    }

    transformAll() {
        for (const elem of this.modelAData.listaVociModAPart3) {
            if(elem.valore != null && elem.valore != undefined && elem.valore !== '')
                elem.valore = this.transform(parseFloat(elem.valore));
        }
        for (const titolo of this.modelAData.listaTitoli) {
            for (const tipologia of titolo.listaTipologie) {
                if(tipologia.valore != null && tipologia.valore != undefined && tipologia.valore !== '')
                    tipologia.valore = this.transform(parseFloat(tipologia.valore));
                for (const voce of tipologia.listaVoci) {
                    if(!(titolo.codTitolo == VOCE_TESTUALE_MOD_A.COD_TITOLO 
                        && tipologia.codTipologia == VOCE_TESTUALE_MOD_A.COD_TIPOLOGIA 
                        && voce.codVoce == VOCE_TESTUALE_MOD_A.COD_VOCE)) {
                            if(voce.valoreNumb!==null)
                                voce.valoreNumb = this.transform(parseFloat(voce.valoreNumb));
                    }
                    if (voce.prestazioni != null) {
                        for (const prestRs of voce.prestazioni.prestazioniRS) {
                            for (const ut of prestRs.listaTargetUtenza) {
                                if(ut.valore != null && ut.valore != undefined && ut.valore !== '')
                                ut.valore = this.transform(parseFloat(ut.valore));
                            }
                        }
                        for (const prestCd of voce.prestazioni.prestazioniCD) {
                            for (const ut of prestCd.listaTargetUtenza) {
                                if(ut.valore != null && ut.valore != undefined && ut.valore !== '')
                                ut.valore = this.transform(parseFloat(ut.valore));
                            }
                        }
                        for (const subRs of voce.prestazioni.subtotaliRS) {
                            if(subRs.valore != null && subRs.valore != undefined && subRs.valore !== '')
                            subRs.valore = this.transform(parseFloat(subRs.valore));
                        }
                        for (const subCd of voce.prestazioni.subtotaliCD) {
                            if(subCd.valore != null && subCd.valore != undefined && subCd.valore !== '')
                            subCd.valore = this.transform(parseFloat(subCd.valore));
                        }
                        for (const totRsCd of voce.prestazioni.totaliSRCD) {
                            if(totRsCd.valore != null && totRsCd.valore != undefined && totRsCd.valore !== '')
                            totRsCd.valore = this.transform(parseFloat(totRsCd.valore));
                        }
                    }
                }
            }
        }
    }

    openDialogMotivazione(voce: VoceModA){
        const dialogRef = this.dialog.open(MotivazionePopupComponent, {
            width: '600px',
            disableClose : true,
            autoFocus : true,
            data: {titolo: 'Compila area testuale', motivazione: voce.valoreText, placeholder: "Inserire qui il testo"}
         });
           dialogRef.afterClosed().subscribe(result => {
          if(result) {
              voce.valoreText = result.motivazione;
          }
        });
    }


    @HostListener('document:changeTabEvent')
    checkEdited(event) {
        this.ischanged();
        if (this.ischangedvar) { document.dispatchEvent(this.client.notSavedEvent); } else { document.dispatchEvent(this.client.changeTabEmitter); }
    }

    refillInitials() {
        this.modelADataInitial = new VociModelloA();
        // copia part3
        this.modelADataInitial.listaVociModAPart3 = [];
        for(let element of this.modelAData.listaVociModAPart3) {
            let newEl = new VociRendModAPart3();
            newEl.idVoce = element.idVoce;
            newEl.codVoce = element.codVoce;
            newEl.descVoce = element.descVoce;
            newEl.msgInformativo = element.msgInformativo;
            newEl.ordinamento = element.ordinamento;
            newEl.valore = element.valore;
            this.modelADataInitial.listaVociModAPart3.push(newEl);
        }
        
        // copia part1
        this.modelADataInitial.listaTitoli = [];
        for(let titolo of this.modelAData.listaTitoli) {
            let newTit = new TitoloModA();
            newTit.idTitolo = titolo.idTitolo;
            newTit.codTitolo = titolo.codTitolo;
            newTit.descTitolo = titolo.descTitolo;
            newTit.msgInformativo = titolo.msgInformativo;
            newTit.totale = titolo.totale;
            newTit.listaTipologie = [];
            for(let tipologia of titolo.listaTipologie) {
                let newTip = new TipologiaModA();
                newTip.idTipologia = tipologia.idTipologia;
                newTip.codTipologia = tipologia.codTipologia;
                newTip.descTipologia = tipologia.descTipologia;
                newTip.descCodTipologia = tipologia.descCodTipologia;
                newTip.msgInformativo = tipologia.msgInformativo;
                newTip.ordinamento = tipologia.ordinamento;
                newTip.valore = tipologia.valore;
                newTip.listaVoci = [];
                for(let voce of tipologia.listaVoci) {
                    let newVoce = new VoceModA();
                    newVoce.idVoce = voce.idVoce;
                    newVoce.codVoce = voce.codVoce;
                    newVoce.descVoce = voce.descVoce;
                    newVoce.msgInformativo = voce.msgInformativo;
                    newVoce.ordinamento = voce.ordinamento;
                    newVoce.rotate = voce.rotate;
                    newVoce.showPrestazioni = voce.showPrestazioni;
                    newVoce.valoreNumb = voce.valoreNumb;
                    newVoce.valoreText = voce.valoreText;
                    // copia part 2
                    if(voce.prestazioni != null) {
                        newVoce.prestazioni = new PrestazioniModAGreg();
                        newVoce.prestazioni.prestazioniRS = [];
                        for(let prest of voce.prestazioni.prestazioniRS) {
                            let newPr = new PrestazioneUtenzaModAGreg();
                            newPr.idPrestazioneUtenza = prest.idPrestazioneUtenza;
                            newPr.idPrestazione = prest.idPrestazione;
                            newPr.codPrestazione = prest.codPrestazione;
                            newPr.descPrestazione = prest.descPrestazione;
                            newPr.listaTargetUtenza = [];
                            for(let utenza of prest.listaTargetUtenza) {
                                let newUt = new TargetUtenzaGreg();
                                newUt.idTargetUtenza = utenza.idTargetUtenza;
                                newUt.codTargetUtenza = utenza.codTargetUtenza;
                                newUt.descTargetUtenza = utenza.descTargetUtenza;
                                newUt.entry = utenza.entry;
                                newUt.valore = utenza.valore;
                                newPr.listaTargetUtenza.push(newUt);
                            }
                            newVoce.prestazioni.prestazioniRS.push(newPr);
                        }
                        newVoce.prestazioni.prestazioniCD = [];
                        for(let prest of voce.prestazioni.prestazioniCD) {
                            let newPr = new PrestazioneUtenzaModAGreg();
                            newPr.idPrestazioneUtenza = prest.idPrestazioneUtenza;
                            newPr.idPrestazione = prest.idPrestazione;
                            newPr.codPrestazione = prest.codPrestazione;
                            newPr.descPrestazione = prest.descPrestazione;
                            newPr.listaTargetUtenza = [];
                            for(let utenza of prest.listaTargetUtenza) {
                                let newUt = new TargetUtenzaGreg();
                                newUt.idTargetUtenza = utenza.idTargetUtenza;
                                newUt.codTargetUtenza = utenza.codTargetUtenza;
                                newUt.descTargetUtenza = utenza.descTargetUtenza;
                                newUt.entry = utenza.entry;
                                newUt.valore = utenza.valore;
                                newPr.listaTargetUtenza.push(newUt);
                            }
                            newVoce.prestazioni.prestazioniCD.push(newPr);
                        }
                    }
                    newTip.listaVoci.push(newVoce);
                }
                newTit.listaTipologie.push(newTip);
            }
            this.modelADataInitial.listaTitoli.push(newTit);
        }
        this.ordinaInitialModel();
    }

    ordinaModel() {
        // Ordinamento campi
        this.modelAData.listaTitoli.sort((titoloA, titoloB) =>
            titoloA.ordinamento - titoloB.ordinamento
        );
        for (const titolo of this.modelAData.listaTitoli) {
            titolo.listaTipologie.sort((tipologia1, tipologia2) =>
                tipologia1.codTipologia.localeCompare(tipologia2.codTipologia));
            for (const tipologia of titolo.listaTipologie) {
                tipologia.listaVoci.sort((voce1, voce2) =>
                    voce1.codVoce.localeCompare(voce2.codVoce));
                for (const voce of tipologia.listaVoci) {
                    if(voce.prestazioni != null){
                        if(voce.prestazioni.prestazioniRS != null && voce.prestazioni.prestazioniRS.length > 0){
                            voce.prestazioni.prestazioniRS.sort((prest1,prest2) =>
                             prest1.codPrestazione.localeCompare(prest2.codPrestazione)
                            );
                            for(const utenza of voce.prestazioni.prestazioniRS){
                                utenza.listaTargetUtenza.sort((ute1,ute2) => 
                                ute1.codTargetUtenza.localeCompare(ute2.codTargetUtenza)
                                );
                            }
                        }
                        if(voce.prestazioni.prestazioniCD != null && voce.prestazioni.prestazioniCD.length > 0){
                            voce.prestazioni.prestazioniCD.sort((prest1,prest2) =>
                                prest1.codPrestazione.localeCompare(prest2.codPrestazione)
                            );
                            for(const utenza of voce.prestazioni.prestazioniCD){
                                utenza.listaTargetUtenza.sort((ute1,ute2) => 
                                ute1.codTargetUtenza.localeCompare(ute2.codTargetUtenza)
                                );
                            }
                        }
                    }

                }
            }
        }
    }

    ordinaInitialModel() {
        // Ordinamento campi
        this.modelADataInitial.listaTitoli.sort((titoloA, titoloB) =>
            titoloA.ordinamento - titoloB.ordinamento
        );
        for (const titolo of this.modelADataInitial.listaTitoli) {
            titolo.listaTipologie.sort((tipologia1, tipologia2) =>
                tipologia1.codTipologia.localeCompare(tipologia2.codTipologia));
            for (const tipologia of titolo.listaTipologie) {
                tipologia.listaVoci.sort((voce1, voce2) =>
                    voce1.codVoce.localeCompare(voce2.codVoce));
            }
        }
    }


	checkShowVoceInput(titolo: TitoloModA, tipologia: TipologiaModA, voce: VoceModA) {
			if (titolo.codTitolo == '02' && tipologia.codTipologia == 'Tipo_101_2' && (voce.codVoce == '06' || voce.codVoce == '07')) {
				return this.datiEnte.codTipoEnte == 'COMUNE CAPOLUOGO';
			} else if (titolo.codTitolo == '02' && tipologia.codTipologia == 'Tipo_101_2' && voce.codVoce == '08') {
				return !((this.datiEnte.codTipoEnte == 'COMUNE CAPOLUOGO' || this.datiEnte.codTipoEnte == 'CONVENZIONE DI COMUNI'));
			}
            return false;
	}
	
	esportaexcel(){
		 this.ischanged();
		 if(this.ischangedvar){
			this.toastService.showError({ text: this.erroreexport }); 
			}
		else {
		this.client.spinEmitter.emit(true);
		let esporta: EsportaModelloA = new EsportaModelloA();
		esporta.idEnte = this.rendicontazione.idRendicontazioneEnte;
        esporta.denominazioneEnte = this.datiEnte.denominazione;
		esporta.rendicontazioneModAPart1 = this.modelAData;
		esporta.rendicontazioneModAPart3 = this.rendicontazioneModAPart3;
		            for (const elem of esporta.rendicontazioneModAPart3) {
                elem.valore = this.parsingFloat(elem.valore);
            }
            for (const titolo of esporta.rendicontazioneModAPart1.listaTitoli) {
                for (const tipologia of titolo.listaTipologie) {
                    tipologia.valore = this.parsingFloat(tipologia.valore);
                    for (const voce of tipologia.listaVoci) {
                        if(!(titolo.codTitolo == VOCE_TESTUALE_MOD_A.COD_TITOLO 
                            && tipologia.codTipologia == VOCE_TESTUALE_MOD_A.COD_TIPOLOGIA 
                            && voce.codVoce == VOCE_TESTUALE_MOD_A.COD_VOCE)) {
                                voce.valoreNumb = this.parsingFloat(voce.valoreNumb);
                        }
                        if (voce.prestazioni != null) {
                            for (const prestRs of voce.prestazioni.prestazioniRS) {
                                for (const ut of prestRs.listaTargetUtenza) {
                                    ut.valore = this.parsingFloat(ut.valore);
                                }
                            }
                            for (const prestCd of voce.prestazioni.prestazioniCD) {
                                for (const ut of prestCd.listaTargetUtenza) {
                                    ut.valore = this.parsingFloat(ut.valore);
                                }
                            }
                            for (const subRs of voce.prestazioni.subtotaliRS) {
                                subRs.valore = this.parsingFloat(subRs.valore);
                            }
                            for (const subCd of voce.prestazioni.subtotaliCD) {
                                subCd.valore = this.parsingFloat(subCd.valore);
                            }
                            for (const totRsCd of voce.prestazioni.totaliSRCD) {
                                totRsCd.valore = this.parsingFloat(totRsCd.valore);
                            }
                        }
                    }
                }
            }
		this.client.esportaModelloA(esporta)
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
					this.transformAll();
					this.toastService.showSuccess({ text: messaggio });
					this.client.spinEmitter.emit(false);
				},
				err => {
					this.transformAll();
					this.client.spinEmitter.emit(false);
				}
			);
		}
		}
		
		ischanged(){
		this.ischangedvar = false;
        if (this.modelAData && this.modelAData != null) {
            this.modelAData.listaTitoli.forEach((titolo, i) => {
                titolo.listaTipologie.forEach((tipologia, j) => {
                    if (tipologia.valore !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].valore) {
                        this.ischangedvar = true;
                    }
                    if (tipologia.listaVoci != null || tipologia.listaVoci.length > 0) {
                        tipologia.listaVoci.forEach((voce, k) => {
                            if (voce.valoreNumb !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb) {
                              this.ischangedvar = true;
                            }
                            if(voce.valoreText!=null){
                                if (voce.valoreText !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreText) {
                                    this.ischangedvar = true;
                                }
                            }
                           
                            if (voce.prestazioni != null) {
                                voce.prestazioni.prestazioniRS.forEach((prestRs, r) => {
                                    prestRs.listaTargetUtenza.forEach((ut, u) => {
                                        if (ut.valore !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[r].listaTargetUtenza[u].valore) {
                                       this.ischangedvar = true;
                                        }
                                    });
                                });

                                voce.prestazioni.prestazioniCD.forEach((prestCd, c) => {
                                    prestCd.listaTargetUtenza.forEach((ut, u) => {
                                        if (ut.valore !== this.modelADataInitial.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[c].listaTargetUtenza[u].valore) {
                                            this.ischangedvar = true;
                                        }
                                    });
                                });

                            }

                        });
                    }
                });
            });

            this.modelAData.listaVociModAPart3.forEach((element, i) => {
                if(element.valore !== this.modelADataInitial.listaVociModAPart3[i].valore) {
                     this.ischangedvar = true;
                }
            });
        }
		}

        rendicontazioneBeforeSaveTransform() {
            for(let i=0; i<this.rendicontazioneModABeforeSave.listaTitoli.length; i++){
                for(let j=0; j<this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie.length; j++){
                    for(let k=0; k<this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci.length; k++){
                        if(this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni !==null){
                            for(let l=0; l<this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS.length; l++){
                                for(let r=0; r<this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[l].listaTargetUtenza.length; r++){
                                     if (this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[l].listaTargetUtenza[r].valore != null && this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[l].listaTargetUtenza[r].valore != undefined) {
                                         this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[l].listaTargetUtenza[r].valore = this.transform(parseFloat(this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniRS[l].listaTargetUtenza[r].valore));
                                     }
                                }
                             }
                             for(let l=0; l<this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD.length; l++){
                                 for(let r=0; r<this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[l].listaTargetUtenza.length; r++){
                                      if (this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[l].listaTargetUtenza[r].valore != null && this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[l].listaTargetUtenza[r].valore != undefined) {
                                          this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[l].listaTargetUtenza[r].valore = this.transform(parseFloat(this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].prestazioni.prestazioniCD[l].listaTargetUtenza[r].valore));
                                      }
                                 }
                            }
                        }
                        if (this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb != null && this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb != undefined) {
                            if(!(i==7 && j==0 && k==1)){
                                this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb = this.transform(parseFloat(this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].listaVoci[k].valoreNumb));
                            }
                        }
                    } 
                    if (this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].valore != null && this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].valore != undefined) {
                        this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].valore = this.transform(parseFloat(this.rendicontazioneModABeforeSave.listaTitoli[i].listaTipologie[j].valore));
                    }
                }
            }
            for(let i=0; i<this.rendicontazioneModABeforeSave.listaVociModAPart3.length; i++){
                if (this.rendicontazioneModABeforeSave.listaVociModAPart3[i].valore != null && this.rendicontazioneModABeforeSave.listaVociModAPart3[i].valore != undefined) {
                    this.rendicontazioneModABeforeSave.listaVociModAPart3[i].valore = this.transform(parseFloat(this.rendicontazioneModABeforeSave.listaVociModAPart3[i].valore));
                }
            }
        }

        ordinaModelBeforeSave() {
            // Ordinamento campi
        //     this.rendicontazioneModABeforeSave.listaTitoli.sort((titoloA, titoloB) =>
        //         titoloA.ordinamento - titoloB.ordinamento
        //     );
        //     for (const titolo of this.rendicontazioneModABeforeSave.listaTitoli) {
        //         titolo.listaTipologie.sort((tipologia1, tipologia2) =>
        //             tipologia1.codTipologia.localeCompare(tipologia2.codTipologia));
        //         for (const tipologia of titolo.listaTipologie) {
        //             tipologia.listaVoci.sort((voce1, voce2) =>
        //                 voce1.codVoce.localeCompare(voce2.codVoce));
        //         }
        //     }

            this.rendicontazioneModABeforeSave.listaTitoli.sort((titoloA, titoloB) =>
            titoloA.ordinamento - titoloB.ordinamento
        );
        for (const titolo of this.rendicontazioneModABeforeSave.listaTitoli) {
            titolo.listaTipologie.sort((tipologia1, tipologia2) =>
                tipologia1.codTipologia.localeCompare(tipologia2.codTipologia));
            for (const tipologia of titolo.listaTipologie) {
                tipologia.listaVoci.sort((voce1, voce2) =>
                    voce1.codVoce.localeCompare(voce2.codVoce));
                for (const voce of tipologia.listaVoci) {
                    if(voce.prestazioni != null){
                        if(voce.prestazioni.prestazioniRS != null && voce.prestazioni.prestazioniRS.length > 0){
                            voce.prestazioni.prestazioniRS.sort((prest1,prest2) =>
                             prest1.codPrestazione.localeCompare(prest2.codPrestazione)
                            );
                            for(const utenza of voce.prestazioni.prestazioniRS){
                                utenza.listaTargetUtenza.sort((ute1,ute2) => 
                                ute1.codTargetUtenza.localeCompare(ute2.codTargetUtenza)
                                );
                            }
                        }
                        if(voce.prestazioni.prestazioniCD != null && voce.prestazioni.prestazioniCD.length > 0){
                            voce.prestazioni.prestazioniCD.sort((prest1,prest2) =>
                                prest1.codPrestazione.localeCompare(prest2.codPrestazione)
                            );
                            for(const utenza of voce.prestazioni.prestazioniCD){
                                utenza.listaTargetUtenza.sort((ute1,ute2) => 
                                ute1.codTargetUtenza.localeCompare(ute2.codTargetUtenza)
                                );
                            }
                        }
                    }

                }
            }
        }
        }

        check(){
            this.client.checkModelloA(this.idRendicontazioneEnte).subscribe(
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
                    motivazione.codModello = SECTION.CODMODELLOA;
                    motivazione.idRendicontazione = this.idRendicontazioneEnte;
                    motivazione.nota = result.nota;
                    motivazione.modello = 'Mod. A';
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
