import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { EsportaModelloB1Greg } from '@greg-app/app/dto/EsportaModelloB1Greg';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { InfoRendicontazioneOperatore } from '@greg-app/app/dto/InfoRendicontazioneOperatore';
import { ModelloB1ElencoLbl } from '@greg-app/app/dto/ModelloB1ElencoLbl';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { ResponseSalvaModelloGreg } from '@greg-app/app/dto/ResponseSalvaModelloGreg';
import { SalvaModelloB1 } from '@greg-app/app/dto/SalvaModelloB1';
import { SalvaMotivazioneCheck } from '@greg-app/app/dto/SalvaMotivazioneCheck';
import { ValidazioneAlZero } from '@greg-app/app/dto/ValidazioneAlZeroDTO';
import { ModelB1Totali, ModelB1VoceMacroaggregati, ModelB1VocePrestazioniRegionali1Utenza, ModelB1VocePrestazioniRegionali2Utenza, ModelB1VocePrestReg2, ModelPrestazioniB1, ModelRendicontazioneTotaliMacroaggregati, VociModelloB1 } from '@greg-app/app/dto/VociModelloB1';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ERRORS, OPERAZIONE, RENDICONTAZIONE_STATUS, SECTION, TRANCHE } from '@greg-app/constants/greg-constants';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-modello-b1',
  templateUrl: './modello-b1.component.html',
  styleUrls: ['./modello-b1.component.css'],
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
export class ModelloB1Component implements OnInit {

  @ViewChild(CronologiaModelliComponent, { static: false }) cronologiaMod: CronologiaModelliComponent;
  @ViewChild(PulsantiFunzioniComponent, { static: false }) pulsantiMod: PulsantiFunzioniComponent;
  state: string[] = [];

  modello_b1_data: VociModelloB1 = new VociModelloB1();
  modello_b1_data_initial: string = "";
  modello_b1_elenco_lbl: ModelloB1ElencoLbl = new ModelloB1ElencoLbl();
  enteValues: string[] = [];
  navigation: Navigation;
  idEnte;
  infoOperatore: RendicontazioneEnteGreg;
  tabtranche: ModelTabTranche;
  ///
  mostrasalva = true;
  is_module_enabled = false;
  ///
  titolo: string;

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }
  ischangedvar: boolean;
  erroreexport: string;

  info_costo_totale: string = "";

  totali_macroaggregati: ModelB1Totali[] = [];
  totali_programma_missione: ModelB1Totali[] = [];

  totali_macroaggregati_prest_reg1: string[] = [];
  totali_utenze_prest_reg1: string[] = [];
  totali_utenze_prest_reg2: any[] = [];
  totali_costo_totale_prest_reg1: string[] = [];
  totali_quota_socio_assistenziale_prest_reg1: string[] = [];
  infoOperatoreBeforeSave: RendicontazioneEnteGreg;
  statoInitial: string;
  modello_b1_data_BeforeSave: string = "";
  modello_b1_data_BS: VociModelloB1 = new VociModelloB1();
  erroreStato: string;
  erroreModifica: string;
  errB1: string;
  idRendicontazioneEnte;
  datiEnte: DatiEnteGreg;
  rendicontazione: RendicontazioneEnteGreg;

  warnings: string[] = [];
  errors: string[] = [];
  // ruoloente: string;
  messaggio: string;
  titoloPopUp: string;
  esito: string;
  obblMotivazione: boolean;
  warningCheck: any;

  constructor(public client: GregBOClient, private router: Router, private dialog: MatDialog, private route: ActivatedRoute, public pulsanti: PulsantiFunzioniComponent, public toastService: AppToastService, private gregError: GregErrorService) {
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
    this.client.spinEmitter.emit(true);
    this.client.nomemodello = SECTION.CODMODELLOB1;

    this.client.isMacroaggregatiCompiled(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {

      this.is_module_enabled = response;
      if (this.is_module_enabled) {
        this.get_all(true);
      } else {

        this.client.getElencoLblModB1(this.rendicontazione.idRendicontazioneEnte).subscribe(response => {

          this.modello_b1_elenco_lbl = response as ModelloB1ElencoLbl;
          this.client.mostrabottoniera = false;
          this.toastService.showError({ text: this.find_msg_informativo("11") });
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.mostrabottoniera = true;
            this.client.spinEmitter.emit(false)
          });

      }
    });
  }


  get_all(disattivaSpinner: boolean) {
    this.client.spinEmitter.emit(true);
    forkJoin({

      MacroaggregatiTotali: this.client.getMacroaggregatiTotali(this.rendicontazione.idRendicontazioneEnte),
      Prestazioni: this.client.getPrestazioniModB1(this.rendicontazione.idRendicontazioneEnte),
      ElencoLbl: this.client.getElencoLblModB1(this.rendicontazione.idRendicontazioneEnte),
      infoOperatore: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
      tranche: this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte, SECTION.CODMODELLOB1),

      erroreexport: this.client.getMsgApplicativo(ERRORS.ERROR_EXPORT),
      erroreStato: this.client.getMsgApplicativo(ERRORS.ERROR_STATO_REND),
      erroreModifica: this.client.getMsgApplicativo(ERRORS.ERROR_MODIF_MOD),
      errB1: this.client.getMsgApplicativo(ERRORS.ERROR_MODB1),
    })
      .subscribe(({ MacroaggregatiTotali, Prestazioni, ElencoLbl, infoOperatore, tranche, erroreexport, erroreStato, erroreModifica, errB1 }) => {
        this.infoOperatore = infoOperatore;
        this.statoInitial = this.infoOperatore.statoRendicontazione.codStatoRendicontazione;
        this.modello_b1_data.macroaggregati = MacroaggregatiTotali as ModelRendicontazioneTotaliMacroaggregati;
        this.modello_b1_data.elencoPrestazioni = Prestazioni as ModelPrestazioniB1[];
        this.modello_b1_elenco_lbl = ElencoLbl as ModelloB1ElencoLbl;

        this.info_costo_totale = this.find_msg_informativo("30");

        this.modello_b1_elenco_lbl.macroaggregati.forEach(item => {
          this.totali_macroaggregati.push({
            codice: item.codice,
            totale: "0,00"
          });
        });
        this.modello_b1_elenco_lbl.missione_programma.forEach(item => {
          this.totali_programma_missione.push({
            codice: item.codice,
            totale: "0,00"
          });
        });
        this.modello_b1_data.macroaggregati.valoriMacroaggregati.forEach(m => {
          m.totale = this.transform(parseFloat(m.totale));
        });
        this.totali_macroaggregati_prest_reg1 = new Array(this.modello_b1_data.elencoPrestazioni.length);
        this.totali_utenze_prest_reg1 = new Array(this.modello_b1_data.elencoPrestazioni.length);
        this.totali_utenze_prest_reg2 = new Array(this.modello_b1_data.elencoPrestazioni.length);
        this.totali_costo_totale_prest_reg1 = new Array(this.modello_b1_data.elencoPrestazioni.length);
        this.totali_quota_socio_assistenziale_prest_reg1 = new Array(this.modello_b1_data.elencoPrestazioni.length);
        this.init_totali();

        this.modello_b1_data_initial = JSON.stringify(this.modello_b1_data);

        this.state = new Array(this.modello_b1_data.elencoPrestazioni.length);
        this.state.fill("default");

        this.tabtranche = tranche;
        if (this.tabtranche != null) {
          this.titolo = this.tabtranche.desEstesaTab;
          this.pulsantiMod.abilitaPulsantiFromModello(this.tabtranche.codTranche);
        }

        this.erroreexport = erroreexport.testoMsgApplicativo;
        this.erroreStato = erroreStato.testoMsgApplicativo;
        this.erroreModifica = erroreModifica.testoMsgApplicativo;
        this.errB1 = errB1.testoMsgApplicativo;

        this.client.mostrabottoniera = true;
        if (disattivaSpinner) {
          this.client.spinEmitter.emit(false);
        }
      },
        err => {
          this.client.mostrabottoniera = true;
          this.client.spinEmitter.emit(false)
        }
      );
  }

  reset_objects() {
    this.modello_b1_data = new VociModelloB1();
    this.modello_b1_elenco_lbl = new ModelloB1ElencoLbl();

    this.totali_macroaggregati = [];
    this.totali_programma_missione = [];

    this.totali_macroaggregati_prest_reg1 = [];
    this.totali_utenze_prest_reg1 = [];
    this.totali_utenze_prest_reg2 = [];
    this.totali_costo_totale_prest_reg1 = [];
    this.totali_quota_socio_assistenziale_prest_reg1 = [];
    this.state = [];
  }

  init_totali() {
    this.modello_b1_data.elencoPrestazioni.forEach((p, index) => {
      //MACROAGGREGATI
      let totale_riga = 0;
      let reddito_da_lavoro_compilato = false;
      if (p.macroaggregati && p.macroaggregati.length > 0) {
        p.macroaggregati.forEach(element => {
          element.disabled = false;
          if (!reddito_da_lavoro_compilato && element.codice == "4VB") {
            element.disabled = true;
          }

          if (element.valore != undefined && element.valore != "") {
            if (element.codice == "1VB") {
              reddito_da_lavoro_compilato = true;
            }
            let v = parseFloat(element.valore);
            if (!isNaN(v)) {
              element.valore = this.transform(v);
              totale_riga += v;
            }

            //MACROAGGREGATI COLONNE
            let col = this.totali_macroaggregati.find(m => m.codice == element.codice);
            if (col != undefined) {
              let old_value = this.parsingFloat(col.totale);
              if (old_value != null) {
                let totale_tmp = v + old_value;
                col.totale = this.transform(totale_tmp);
              }

            }
          }
        });
      }
      this.totali_macroaggregati_prest_reg1[index] = this.transform(totale_riga);

      if (p.tipoPrestazione != "MA03") {
        //UTENZE
        totale_riga = 0;
        if (p.utenze && p.utenze.length > 0) {
          p.utenze.forEach(element => {
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
                totale_riga += v;
              }

              //PROGRAMMA MISSIONE COLONNE
              let col = this.totali_programma_missione.find(m => m.codice == element.codiceProgrammaMissione);
              if (col != undefined) {
                let old_value = this.parsingFloat(col.totale);
                if (old_value != null) {
                  col.totale = this.transform(v + old_value);
                }
              }
            }
          });
        }
        this.totali_utenze_prest_reg1[index] = this.transform(totale_riga);


      } else {
        //COSTO TOTALE
        totale_riga = 0;
        if (p.utenzeCostoTotale && p.utenzeCostoTotale.length > 0) {
          p.utenzeCostoTotale.forEach(element => {
            this.calcola_limite_massimo_quota_socio_assistenziale(element, p.utenzeQuotaSocioAssistenziale);
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
                totale_riga += v;
              }

              //PROGRAMMA MISSIONE COLONNE
              let col = this.totali_programma_missione.find(m => m.codice == element.codiceProgrammaMissione);
              if (col != undefined) {
                let old_value = this.parsingFloat(col.totale);
                if (old_value != null) {
                  col.totale = this.transform(v + old_value);
                }
              }
            }
          });
        }
        this.totali_costo_totale_prest_reg1[index] = this.transform(totale_riga);

        //QUOTA SOCIO ASSISTENZIALE
        totale_riga = 0;
        if (p.utenzeQuotaSocioAssistenziale && p.utenzeQuotaSocioAssistenziale.length > 0) {
          p.utenzeQuotaSocioAssistenziale.forEach(element => {
            element.error = false;
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
                totale_riga += v;
                let v2 = this.parsingFloat(element.limiteMassimo);
                if (v2 != null) {
                  element.error = v > v2;
                }
              }

              //PROGRAMMA MISSIONE COLONNE
              let col = this.totali_programma_missione.find(m => m.codice == element.codiceProgrammaMissione);
              if (col != undefined) {
                let old_value = this.parsingFloat(col.totale);
                if (old_value != null) {
                  col.totale = this.transform(v + old_value);
                }
              }
            }
          });
        }
        this.totali_quota_socio_assistenziale_prest_reg1[index] = this.transform(totale_riga);
      }
      //PRESTAZIONI REGIONALI 2
      this.totali_utenze_prest_reg2[index] = new Array(p.prestazioniRegionali2.length);
      p.prestazioniRegionali2.forEach((p2, i2) => {
        totale_riga = 0;
        if (p2.utenze && p2.utenze.length > 0) {
          p2.utenze.forEach(element => {
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
                totale_riga += v;
              }
            }
          });
          this.totali_utenze_prest_reg2[index][i2] = this.transform(totale_riga);
        }
      });

      this.check_totali_utenze_prest_reg2_per_colonna(index);
    });
  }

  calcola_limite_massimo_quota_socio_assistenziale(utenza_costo_totale: ModelB1VocePrestazioniRegionali1Utenza, utenze_quota_socio_assistenziale: ModelB1VocePrestazioniRegionali1Utenza[]) {
    let valore_totale = 0;
    let quota_disabled: boolean = true;
    if (utenza_costo_totale.valore != undefined && utenza_costo_totale.valore != "") {
      let v = parseFloat(utenza_costo_totale.valore);
      if (!isNaN(v)) {
        valore_totale = v;
        quota_disabled = false;
      }
    }


    let quota_socio_assistenziale: ModelB1VocePrestazioniRegionali1Utenza = utenze_quota_socio_assistenziale.find(qsa => qsa.codice == utenza_costo_totale.codice);
    if (quota_socio_assistenziale != undefined) {
      quota_socio_assistenziale.disabled = quota_disabled;
      let v = parseFloat(quota_socio_assistenziale.sommaUTASRModA);
      if (!isNaN(v)) {
        let limite = valore_totale - v;
        if (limite > 0) {
          quota_socio_assistenziale.limiteMassimo = this.transform(limite);
        }
      }
    }
  }

  check_macroaggregati_reddito_serviziB(index: number) {
    let macroaggregati: ModelB1VoceMacroaggregati[] = this.modello_b1_data.elencoPrestazioni[index].macroaggregati;
    let reddito_da_lavoro_compilato = false;
    let show_warning = false;
    let valore_da_aggiungere = 0;
    if (macroaggregati && macroaggregati.length > 0) {
      macroaggregati.forEach(element => {
        if (element.codice == "1VB") { //REDDITO DAL LAVORO DIPENDENTE
          reddito_da_lavoro_compilato = element.valore != undefined && element.valore != "" && this.parsingFloat(element.valore) != null && this.parsingFloat(element.valore) !== 0;
        } else if (element.codice == "4VB") { //ACQUISTO E BENI SERVIZI B
          element.disabled = false;
          if (!reddito_da_lavoro_compilato) {
            element.disabled = true;
            if (element.valore != undefined && element.valore != "") {
              let v = this.parsingFloat(element.valore);
              if (v != null) {
                valore_da_aggiungere = v;
                let valorizzato = this.parsingFloat(element.valore) != null && this.parsingFloat(element.valore) !== 0;
                element.valore = null;
                if (valorizzato) {
                  show_warning = true;
                }
              }
            }
          }
        } else if (element.codice == "7VB") { //ALTRE SPESE CORRENTI
          if (show_warning) {
            let t = valore_da_aggiungere;
            if (element.valore != undefined && element.valore != "") {
              let v = this.parsingFloat(element.valore);
              if (v != null) {
                t += v;
              }
            }
            element.valore = this.transform(t);
            this.errorMessage.error.descrizione = this.find_msg_informativo("17").replace("%1", this.transform(valore_da_aggiungere));
            this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: 'Altre spese' }))
          }
        }
      });
    }
  }

  calcola_totali_macroaggregati(codice: string, index: number) {
    this.check_macroaggregati_reddito_serviziB(index);
    let macroaggregati: ModelB1VoceMacroaggregati[] = this.modello_b1_data.elencoPrestazioni[index].macroaggregati;
    let totale_riga = 0;

    if (macroaggregati && macroaggregati.length > 0) {
      macroaggregati.forEach(element => {
        if (element.valore != undefined && element.valore != "") {
          let v = this.parsingFloat(element.valore);
          if (v != null) {
            totale_riga += v;
          }
        }
      });
    }
    this.totali_macroaggregati_prest_reg1[index] = this.transform(totale_riga);

    //TOTALI PER COLONNA
    let totale_colonna = 0;
    this.modello_b1_data.elencoPrestazioni.forEach(prestazione => {
      let macroaggregato = prestazione.macroaggregati.find(m => m.codice == codice);
      if (macroaggregato != undefined && macroaggregato.valore != undefined && macroaggregato.valore != "") {
        let v = this.parsingFloat(macroaggregato.valore);
        if (v != null) {
          totale_colonna += v;
        }
      }
    });

    let tot_mac = this.totali_macroaggregati.find(item => item.codice == codice);
    if (tot_mac != undefined) {
      tot_mac.totale = this.transform(totale_colonna);
    }
  }

  calcola_totali_utenze_prest_reg1(codiceProgrammaMissione: string, index: number) {
    let utenze: ModelB1VocePrestazioniRegionali1Utenza[] = this.modello_b1_data.elencoPrestazioni[index].utenze;
    let totale_riga = 0;
    if (utenze && utenze.length > 0) {
      utenze.forEach(element => {
        if (element.valore != undefined && element.valore != "") {
          let v = this.parsingFloat(element.valore);
          if (v != null) {
            totale_riga += v;
          }
        }

      });
    }
    this.totali_utenze_prest_reg1[index] = this.transform(totale_riga);

    //TOTALI PER COLONNA PROGRAMMA MISSIONE
    let tot_progr_mis = this.totali_programma_missione.find(item => item.codice == codiceProgrammaMissione);
    if (tot_progr_mis != undefined) {
      tot_progr_mis.totale = this.transform(this.calcola_totali_programma_missione(codiceProgrammaMissione));
    }

    this.check_totali_utenze_prest_reg2_per_colonna(index);
  }

  calcola_totali_costo_totale(codiceProgrammaMissione: string, index: number) {
    let utenze: ModelB1VocePrestazioniRegionali1Utenza[] = this.modello_b1_data.elencoPrestazioni[index].utenzeCostoTotale;
    let utenze_quota_socio_assistenziale: ModelB1VocePrestazioniRegionali1Utenza[] = this.modello_b1_data.elencoPrestazioni[index].utenzeQuotaSocioAssistenziale;
    let totale_riga = 0;
    let quota_disabled: boolean;
    if (utenze && utenze.length > 0) {
      utenze.forEach(element => {
        let valore_totale = 0;
        quota_disabled = true;
        if (element.valore != undefined && element.valore != "") {
          let v = this.parsingFloat(element.valore);
          if (v != null) {
            valore_totale = v;
            quota_disabled = false;
          }
          totale_riga += valore_totale;
        }

        //CALCOLO LIMITE MASSIMO QUOTA SOCIO ASSISTENZIALE
        let quota_socio_assistenziale: ModelB1VocePrestazioniRegionali1Utenza = utenze_quota_socio_assistenziale.find(qsa => qsa.codice == element.codice);
        if (quota_socio_assistenziale != undefined) {
          quota_socio_assistenziale.limiteMassimo = undefined;
          quota_socio_assistenziale.error = false;
          quota_socio_assistenziale.disabled = quota_disabled;
          if (quota_disabled) {
            quota_socio_assistenziale.valore = null;
            this.calcola_totali_quota_socio_assistenziale(codiceProgrammaMissione, index);
          }
          let v = parseFloat(quota_socio_assistenziale.sommaUTASRModA);
          if (!isNaN(v)) {
            let limite = valore_totale - v;
            if (limite >= 0) {
              quota_socio_assistenziale.limiteMassimo = this.transform(limite);
              quota_socio_assistenziale.error = false;
              let v2 = this.parsingFloat(quota_socio_assistenziale.valore);
              if (v2 != null) {
                quota_socio_assistenziale.error = v2 > limite;
              }
            }
          }
        }
      });
    }
    this.totali_costo_totale_prest_reg1[index] = this.transform(totale_riga);

    //TOTALI PER COLONNA PROGRAMMA MISSIONE
    let tot_progr_mis = this.totali_programma_missione.find(item => item.codice == codiceProgrammaMissione);
    if (tot_progr_mis != undefined) {
      tot_progr_mis.totale = this.transform(this.calcola_totali_programma_missione(codiceProgrammaMissione));
    }

    this.check_totali_utenze_prest_reg2_per_colonna(index);
  }

  calcola_totali_quota_socio_assistenziale(codiceProgrammaMissione: string, index: number) {
    let utenze: ModelB1VocePrestazioniRegionali1Utenza[] = this.modello_b1_data.elencoPrestazioni[index].utenzeQuotaSocioAssistenziale;
    let totale_riga = 0;
    if (utenze && utenze.length > 0) {
      utenze.forEach(element => {
        element.error = false;
        if (element.valore != undefined && element.valore != "") {
          let v = this.parsingFloat(element.valore);
          let v2 = this.parsingFloat(element.limiteMassimo);
          element.error = false;
          if (v != null) {
            totale_riga += v;
            if (v2 != null) {
              element.error = v > v2;
            }
          }
        }
      });
    }
    this.totali_quota_socio_assistenziale_prest_reg1[index] = this.transform(totale_riga);

    //TOTALI PER COLONNA PROGRAMMA MISSIONE
    let tot_progr_mis = this.totali_programma_missione.find(item => item.codice == codiceProgrammaMissione);
    if (tot_progr_mis != undefined) {
      tot_progr_mis.totale = this.transform(this.calcola_totali_programma_missione(codiceProgrammaMissione));
    }
  }

  calcola_totali_utenze_prest_reg2(index: number, index_prestazione_regionale_2: number) {
    let utenze: ModelB1VocePrestazioniRegionali2Utenza[] = this.modello_b1_data.elencoPrestazioni[index].prestazioniRegionali2[index_prestazione_regionale_2].utenze;
    let totale_riga = 0;
    if (utenze && utenze.length > 0) {
      utenze.forEach(element => {
        if (element.valore != undefined && element.valore != "") {
          let v = this.parsingFloat(element.valore);
          if (v != null) {
            totale_riga += v;
          }
        }
      });
    }

    this.totali_utenze_prest_reg2[index][index_prestazione_regionale_2] = this.transform(totale_riga);

    this.check_totali_utenze_prest_reg2_per_colonna(index);
  }

  calcola_totali_programma_missione(codiceProgrammaMissione: string): number {
    let totale_colonna = 0;
    this.modello_b1_data.elencoPrestazioni.forEach(prestazione => {
      prestazione.utenze.forEach(utenza => {
        if (utenza.valore != undefined && utenza.valore != "" && utenza.codiceProgrammaMissione == codiceProgrammaMissione) {
          let v = this.parsingFloat(utenza.valore);
          if (v != null) {
            totale_colonna += v;
          }
        }
      });

      prestazione.utenzeQuotaSocioAssistenziale.forEach(utenza => {
        if (utenza.valore != undefined && utenza.valore != "" && utenza.codiceProgrammaMissione == codiceProgrammaMissione) {
          let v = this.parsingFloat(utenza.valore);
          if (v != null) {
            totale_colonna += v;
          }
        }
      });

      prestazione.utenzeCostoTotale.forEach(utenza => {
        if (utenza.valore != undefined && utenza.valore != "" && utenza.codiceProgrammaMissione == codiceProgrammaMissione) {
          let v = this.parsingFloat(utenza.valore);
          if (v != null) {
            totale_colonna += v;
          }
        }
      });


    });

    return totale_colonna;
  }

  check_totali_utenze_prest_reg2_per_colonna(index: number) {
    let utenze_pr1: ModelB1VocePrestazioniRegionali1Utenza[] = this.modello_b1_data.elencoPrestazioni[index].utenze;
    let utenza_cs: ModelB1VocePrestazioniRegionali1Utenza[] = this.modello_b1_data.elencoPrestazioni[index].utenzeCostoTotale;
    if (utenze_pr1 && utenze_pr1.length > 0) {
      utenze_pr1.forEach((upr1, i) => {
        upr1.error = false;
        let v_upr1 = this.parsingFloat(upr1.valore);
        let totale_utenze_colonna_pr2: number;
        totale_utenze_colonna_pr2 = null;
        let arr_tmp: ModelB1VocePrestazioniRegionali2Utenza[] = [];

        let elenco_pr2: ModelB1VocePrestReg2[] = this.modello_b1_data.elencoPrestazioni[index].prestazioniRegionali2;
        if (elenco_pr2 && elenco_pr2.length > 0) {
          elenco_pr2.forEach(pr2 => {
            let upr2: ModelB1VocePrestazioniRegionali2Utenza = pr2.utenze.find(e => e.codice == upr1.codice);
            if (upr2 != undefined) {
              upr2.error = false;
              arr_tmp.push(upr2);
              let v = this.parsingFloat(upr2.valore);
              if (v != null) {
                if (totale_utenze_colonna_pr2 == null) {
                  totale_utenze_colonna_pr2 = v;
                } else {
                  totale_utenze_colonna_pr2 += v;
                }
              }
            }
          });
        }
        if (totale_utenze_colonna_pr2 != null) {
          let reg1 = v_upr1 != null ? v_upr1 : 0.00;
          if (reg1.toFixed(2) != totale_utenze_colonna_pr2.toFixed(2)) {
            upr1.error = true;
            arr_tmp.forEach(pr2 => {
              pr2.error = true;
            });
          }
        }


      });
    } else if (utenza_cs && utenza_cs.length > 0) {
      utenza_cs.forEach((upr1, i) => {
        upr1.error = false;
        let v_upr1 = this.parsingFloat(upr1.valore);
        let totale_utenze_colonna_pr2: number;
        totale_utenze_colonna_pr2 = null;
        let arr_tmp: ModelB1VocePrestazioniRegionali2Utenza[] = [];

        let elenco_pr2: ModelB1VocePrestReg2[] = this.modello_b1_data.elencoPrestazioni[index].prestazioniRegionali2;
        if (elenco_pr2 && elenco_pr2.length > 0) {
          elenco_pr2.forEach(pr2 => {
            let upr2: ModelB1VocePrestazioniRegionali2Utenza = pr2.utenze.find(e => e.codice == upr1.codice);
            if (upr2 != undefined) {
              upr2.error = false;
              arr_tmp.push(upr2);
              let v = this.parsingFloat(upr2.valore);
              if (v != null) {
                if (totale_utenze_colonna_pr2 == null) {
                  totale_utenze_colonna_pr2 = v;
                } else {
                  totale_utenze_colonna_pr2 += v;
                }
              }
            }
          });
        }
        if (totale_utenze_colonna_pr2 != null) {
          let reg1 = v_upr1 != null ? v_upr1 : 0.00;
          if (reg1.toFixed(2) != totale_utenze_colonna_pr2.toFixed(2)) {
            upr1.error = true;
            arr_tmp.forEach(pr2 => {
              pr2.error = true;
            });
          }
        }


      });
    }
  }

  parsingFloat(el) {
    if (el == undefined || el == '') {
      return null;
    }
    let value = parseFloat(el.toString().replaceAll('.', '').replace(',', '.'));
    if (isNaN(value)) {
      return null;
    }
    return value;
  }

  transform(value: number, currencySign: string = '', decimalLength: number = 2, chunkDelimiter: string = '.', decimalDelimiter: string = ',', chunkLength: number = 3): string {
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
    return ((value != null) && (value !== '') && !isNaN(Number(value.toString().replace(',', '.'))));
  }

  changeKey(voce: any, valore: any) {
    if (valore == '' || valore == null) {
      voce.valore = null;
    } else {
      if (valore.indexOf('.') !== -1) {
        if (valore.indexOf(',') !== -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
          valore = [valore, '0'].join('');
        }
        voce.valore = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
      } else {
        voce.valore = this.transform(parseFloat(valore.toString().replace(',', '.')));
      }
    }
  }

  apriISTAT(index: number) {
    this.state[index] = (this.state[index] === 'default' ? 'rotated' : 'default');
  }

  find_msg_informativo(codice: string): string {
    let output = "Errore generico";
    if (this.modello_b1_elenco_lbl.msgInformativi) {
      let msg = this.modello_b1_elenco_lbl.msgInformativi.find(m => m.codice == codice);
      if (msg != undefined) {
        output = msg.descrizione;
      }
    }
    return output
  }

  salvaModifiche(event) {
    //SALVATAGGIO
    this.client.nomemodello = SECTION.CODMODELLOB1;
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

        forkJoin({
          MacroaggregatiTotali: this.client.getMacroaggregatiTotali(this.rendicontazione.idRendicontazioneEnte),
          Prestazioni: this.client.getPrestazioniModB1(this.rendicontazione.idRendicontazioneEnte)
        })
          .subscribe(({ MacroaggregatiTotali, Prestazioni }) => {
            this.modello_b1_data_BS.macroaggregati = MacroaggregatiTotali as ModelRendicontazioneTotaliMacroaggregati;
            this.modello_b1_data_BS.elencoPrestazioni = Prestazioni as ModelPrestazioniB1[];

            this.modello_b1_data_BS.macroaggregati.valoriMacroaggregati.forEach(m => {
              m.totale = this.transform(parseFloat(m.totale));
            });
            this.init_totaliBeforeSave();
            this.modello_b1_data_BeforeSave = JSON.stringify(this.modello_b1_data_BS);
            let modelloB1Initial: VociModelloB1 = new VociModelloB1();
            modelloB1Initial = JSON.parse(this.modello_b1_data_initial);
            if (this.modello_b1_data_BS !== undefined && this.modello_b1_data_initial !== "") {
              for (let i = 0; i < this.modello_b1_data_BS.elencoPrestazioni.length; i++) {
                for (let j = 0; j < this.modello_b1_data_BS.elencoPrestazioni[i].macroaggregati.length; j++) {
                  if (this.modello_b1_data_BS.elencoPrestazioni[i].macroaggregati[j].valore !== modelloB1Initial.elencoPrestazioni[i].macroaggregati[j].valore) {
                    modify = true;
                  }
                }
                for (let j = 0; j < this.modello_b1_data_BS.elencoPrestazioni[i].prestazioniRegionali2.length; j++) {
                  for (let k = 0; k < this.modello_b1_data_BS.elencoPrestazioni[i].prestazioniRegionali2[j].utenze.length; k++) {
                    if (this.modello_b1_data_BS.elencoPrestazioni[i].prestazioniRegionali2[j].utenze[k].valore !== modelloB1Initial.elencoPrestazioni[i].prestazioniRegionali2[j].utenze[k].valore) {
                      modify = true;
                    }
                  }
                }
                for (let j = 0; j < this.modello_b1_data_BS.elencoPrestazioni[i].utenze.length; j++) {
                  if (this.modello_b1_data_BS.elencoPrestazioni[i].utenze[j].valore !== modelloB1Initial.elencoPrestazioni[i].utenze[j].valore) {
                    modify = true;
                  }
                }
                for (let j = 0; j < this.modello_b1_data_BS.elencoPrestazioni[i].utenzeCostoTotale.length; j++) {
                  if (this.modello_b1_data_BS.elencoPrestazioni[i].utenzeCostoTotale[j].valore !== modelloB1Initial.elencoPrestazioni[i].utenzeCostoTotale[j].valore) {
                    modify = true;
                  }
                }
                for (let j = 0; j < this.modello_b1_data_BS.elencoPrestazioni[i].utenzeQuotaSocioAssistenziale.length; j++) {
                  if (this.modello_b1_data_BS.elencoPrestazioni[i].utenzeQuotaSocioAssistenziale[j].valore !== modelloB1Initial.elencoPrestazioni[i].utenzeQuotaSocioAssistenziale[j].valore) {
                    modify = true;
                  }
                }
              }
            }
            if (modify) {
              this.errorMessage.error.descrizione = this.erroreModifica.replace('MODELLO', this.client.nomemodello);
              this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
              this.reset_objects();
              this.get_all(true);
              this.client.spinEmitter.emit(false);
              return;
            } else {
              // if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
              if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
                && (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "" || this.cronologiaMod.cronologia.notaEnte.trim().length == 0)) {
                this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
                this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
                this.client.spinEmitter.emit(false);
                return;
              } else {
                for (let i = 0; i < this.modello_b1_data.elencoPrestazioni.length; i++) {
                  for (let j = 0; j < this.modello_b1_data.elencoPrestazioni[i].utenzeQuotaSocioAssistenziale.length; j++) {
                    if (this.modello_b1_data.elencoPrestazioni[i].utenzeQuotaSocioAssistenziale[j].error) {
                      this.errorMessage.error.descrizione = this.errB1;
                      this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }));
                      this.client.spinEmitter.emit(false);
                      return;
                    }
                  }
                }
                let salva: SalvaModelloB1 = new SalvaModelloB1();
                salva.idEnte = this.rendicontazione.idSchedaEnte;
                salva.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
                salva.cronologia = this.cronologiaMod.cronologia;
                salva.dati = this.modello_b1_data.elencoPrestazioni;
                salva.profilo = this.client.profilo;
                this.client.spinEmitter.emit(true);
                this.client.saveModelloB1(salva).subscribe((data: ResponseSalvaModelloGreg) => {

                  if (data.warnings && data.warnings.length > 0 && this.client.operazione != OPERAZIONE.INVIAMODELLI) {
                    for (let warn of data.warnings) {
                      this.errorMessage.error.descrizione = warn;
                      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
                    }
                  }
                  if (data.errors && data.errors.length > 0 && this.client.operazione != OPERAZIONE.INVIAMODELLI) {
                    for (let err of data.errors) {
                      this.errorMessage.error.descrizione = err;
                      this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
                    }
                  }
                  this.cronologiaMod.ngOnInit();
                  this.cronologiaMod.espansa = false;
                  this.cronologiaMod.state = 'rotated';
                  this.cronologiaMod.apricronologia();
                  this.cronologiaMod.cronologia.notaEnte = null;
                  this.cronologiaMod.cronologia.notaInterna = null;

                  // ANNULLA VALIDAZIONE ALLONTANAMENTO ZERO
                  let payload: ValidazioneAlZero = {
                    idRendicontazioneEnte: this.rendicontazione.idRendicontazioneEnte,
                    toggle: false,
                    cronologia: this.cronologiaMod.cronologia,
                    profilo: this.client.profilo,
                    notaEnte: null,
                    notaInterna: null,
                    modello: 'Mod. B1'
                  }
                  forkJoin({
                    response: this.client.toggleValidazioneAlZero(payload),
                  }).subscribe(({ response }) => {
                    this.cronologiaMod.ngOnInit();
                    this.cronologiaMod.espansa = true;
                    this.cronologiaMod.state = 'rotated';
                    this.cronologiaMod.apricronologia();
                    this.cronologiaMod.cronologia.notaEnte = null;
                    this.cronologiaMod.cronologia.notaInterna = null;
                  }, err => {
                    this.client.spinEmitter.emit(false);
                  });

                  //ricalcolo pulsantiera in caso di cambio stato
                  this.pulsantiMod.abilitaPulsantiExternal(SECTION.CODMODELLOB1);
                  // this.client.spinEmitter.emit(false);


                  // this.client.spinEmitter.emit(false);
                  if (this.client.operazione == OPERAZIONE.INVIAMODELLI) {
                    // this.refillInitialsFromInvia();
                    this.reset_objects();
                    this.get_all(false);
                    this.pulsanti.inviamodelliII(this.rendicontazione.idRendicontazioneEnte);
                  } else if (this.client.operazione == OPERAZIONE.CHECK) {
                    this.check();
                  }
                  else {
                    this.reset_objects();
                    this.get_all(true);
                    this.toastService.showSuccess({ text: data.descrizione });
                  }
                  // -------------------------------------------------------------
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

  updateCrono(event) {
    this.cronologiaMod.ngOnInit();
    this.cronologiaMod.espansa = false;
    this.cronologiaMod.state = 'rotated';
    this.cronologiaMod.apricronologia();
    this.cronologiaMod.cronologia.notaEnte = null;
    this.cronologiaMod.cronologia.notaInterna = null;
  }

  @HostListener('document:changeTabEvent')
  checkEdited(event) {
    this.ischanged();
    if (this.ischangedvar) document.dispatchEvent(this.client.notSavedEvent);
    else document.dispatchEvent(this.client.changeTabEmitter);
  }

  esportaexcel() {
    this.ischanged();
    if (this.ischangedvar) {
      this.toastService.showError({ text: this.erroreexport });
    }
    else {
      this.client.spinEmitter.emit(true);
      let datiExport = new EsportaModelloB1Greg();
      datiExport.idEnte = this.rendicontazione.idRendicontazioneEnte;
      datiExport.datiB1 = this.modello_b1_data.elencoPrestazioni;
      datiExport.labels = this.modello_b1_elenco_lbl;
      datiExport.denominazioneEnte = this.datiEnte.denominazione;
      this.client.esportaModelloB1(datiExport).subscribe(res => {
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
        });
    }
  }

  ischanged() {
    if (this.modello_b1_data !== undefined && this.modello_b1_data_initial !== "") {
      this.ischangedvar = JSON.stringify(this.modello_b1_data) !== this.modello_b1_data_initial;
    }
  }

  refillInitialsFromInvia() {
    this.modello_b1_data = new VociModelloB1();
    forkJoin({
      MacroaggregatiTotali: this.client.getMacroaggregatiTotali(this.rendicontazione.idRendicontazioneEnte),
      Prestazioni: this.client.getPrestazioniModB1(this.rendicontazione.idRendicontazioneEnte)
    })
      .subscribe(({ MacroaggregatiTotali, Prestazioni }) => {
        this.modello_b1_data.macroaggregati = MacroaggregatiTotali as ModelRendicontazioneTotaliMacroaggregati;
        this.modello_b1_data.elencoPrestazioni = Prestazioni as ModelPrestazioniB1[];

        this.modello_b1_data.macroaggregati.valoriMacroaggregati.forEach(m => {
          m.totale = this.transform(parseFloat(m.totale));
        });

        this.modello_b1_data_initial = JSON.stringify(this.modello_b1_data);

      });
  }

  init_totaliBeforeSave() {
    this.modello_b1_data_BS.elencoPrestazioni.forEach((p, index) => {
      //MACROAGGREGATI

      let reddito_da_lavoro_compilato = false;
      if (p.macroaggregati && p.macroaggregati.length > 0) {
        p.macroaggregati.forEach(element => {
          element.disabled = false;
          if (!reddito_da_lavoro_compilato && element.codice == "4VB") {
            element.disabled = true;
          }

          if (element.valore != undefined && element.valore != "") {
            if (element.codice == "1VB") {
              reddito_da_lavoro_compilato = true;
            }
            let v = parseFloat(element.valore);
            if (!isNaN(v)) {
              element.valore = this.transform(v);
            }
          }
        });
      }

      if (p.tipoPrestazione != "MA03") {
        //UTENZE
        if (p.utenze && p.utenze.length > 0) {
          p.utenze.forEach(element => {
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
              }
            }
          });
        }


      } else {
        //COSTO TOTALE
        if (p.utenzeCostoTotale && p.utenzeCostoTotale.length > 0) {
          p.utenzeCostoTotale.forEach(element => {
            this.calcola_limite_massimo_quota_socio_assistenziale(element, p.utenzeQuotaSocioAssistenziale);
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
              }

            }
          });
        }

        //QUOTA SOCIO ASSISTENZIALE
        if (p.utenzeQuotaSocioAssistenziale && p.utenzeQuotaSocioAssistenziale.length > 0) {
          p.utenzeQuotaSocioAssistenziale.forEach(element => {
            element.error = false;
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
                let v2 = this.parsingFloat(element.limiteMassimo);
                if (v2 != null) {
                  element.error = v > v2;
                }
              }
            }
          });
        }
      }
      //PRESTAZIONI REGIONALI 2
      this.totali_utenze_prest_reg2[index] = new Array(p.prestazioniRegionali2.length);
      p.prestazioniRegionali2.forEach((p2, i2) => {
        if (p2.utenze && p2.utenze.length > 0) {
          p2.utenze.forEach(element => {
            if (element.valore != undefined && element.valore != "") {
              let v = parseFloat(element.valore);
              if (!isNaN(v)) {
                element.valore = this.transform(v);
              }
            }
          });
        }
      });
    });
  }


  check() {
    this.client.checkModelloB1(this.idRendicontazioneEnte).subscribe(
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

  openDialog() {
    const dialogRef = this.dialog.open(MessaggioPopupComponent, {
      width: '70%',
      disableClose: true,
      autoFocus: true,
      data: { titolo: this.titoloPopUp, warnings: this.warnings, errors: this.errors, messaggio: this.messaggio, esito: this.esito, nota: "", chiudi: null, obblMotivazione: this.obblMotivazione, warningCheck: this.warningCheck, check: true }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result.chiudi) {
        this.client.spinEmitter.emit(true);
        let motivazione: SalvaMotivazioneCheck = new SalvaMotivazioneCheck();
        motivazione.codModello = SECTION.CODMODELLOB1;
        motivazione.idRendicontazione = this.idRendicontazioneEnte;
        motivazione.nota = result.nota;
        motivazione.modello = 'Mod. B1';
        this.client.saveMotivazioneCheck(motivazione).subscribe((result) => {
          this.toastService.showSuccess({ text: result.messaggio });
          this.cronologiaMod.ngOnInit();
          this.cronologiaMod.espansa = false;
          this.cronologiaMod.state = 'rotated';
          this.cronologiaMod.apricronologia();
          this.cronologiaMod.cronologia.notaEnte = null;
          this.cronologiaMod.cronologia.notaInterna = null;
          this.client.spinEmitter.emit(false);
        }, err => {
          this.client.spinEmitter.emit(false);
        });
      }
    })
  }


}
