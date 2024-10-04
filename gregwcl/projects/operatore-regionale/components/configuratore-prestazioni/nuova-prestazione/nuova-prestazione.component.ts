import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { DettaglioPrestazione } from '@greg-app/app/dto/DettaglioPrestazione';
import { DettaglioPrestazioneConf } from '@greg-app/app/dto/DettaglioPrestazioneConf';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { ListeConfiguratore } from '@greg-app/app/dto/ListeConfiguratore';
import { MacroaggregatiConf } from '@greg-app/app/dto/MacroaggregatiConf';
import { Prest1Prest2 } from '@greg-app/app/dto/Prest1Prest2';
import { Prest1PrestCollegate } from '@greg-app/app/dto/Prest1PrestCollegate';
import { Prest1PrestMin } from '@greg-app/app/dto/Prest1PrestMin';
import { PrestUtenza } from '@greg-app/app/dto/PrestUtenza';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { SECTION } from '@greg-app/constants/greg-constants';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { InfoPopup } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/macroaggregati/info-popup/info-popup.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { CancPopupComponent } from '../canc-popup/canc-popup.component';
import { ModificaPrestazione2Component } from '../modifica-prestazione2/modifica-prestazione2.component';
import { NuovaPrestazione2Component } from '../nuova-prestazione2/nuova-prestazione2component';

@Component({
  selector: 'app-nuova-prestazione',
  templateUrl: './nuova-prestazione.component.html',
  styleUrls: ['./nuova-prestazione.component.css']
})
export class NuovaPrestazioneComponent implements OnInit {

  dettaglioPrestazione: DettaglioPrestazioneConf;
  tipologie: ListeConfiguratore[];
  strutture: ListeConfiguratore[];
  quote: ListeConfiguratore[];
  prestColl: ListeConfiguratore[];
  macro: ListeConfiguratore[];
  utenze: ListeConfiguratore[];
  missioneProg: ListeConfiguratore[];
  spese: ListeConfiguratore[];
  prestazioni2: Prest1Prest2[];
  prestazioni2Form = new FormControl();
  prestazioni2Filtered: Prest1Prest2[] = [];
  prestazione2Selezionata: Prest1Prest2 = new Prest1Prest2();
  dettaglioPrestazione2: Prest1Prest2;
  prestazioniMin: Prest1PrestMin[];
  prestazioniMinForm = new FormControl();
  prestazioniMinFiltered: Prest1PrestMin[] = [];
  prestazioneMinSelezionata: Prest1PrestMin = new Prest1PrestMin();
  dettaglioPrestazioneMin: Prest1PrestMin;

  tip: string;
  strutt: string;
  q: string;
  coll: number;
  ordinamento: string;
  codice: string;
  descrizione: string;
  dal: Date;
  al: Date;
  nota: string;
  macroAssegnati: MacroaggregatiConf[] = [];
  utenzeAssegnati: PrestUtenza[] = [];
  prestMinAssegnati: Prest1PrestMin[] = [];
  prest2Assegnati: Prest1Prest2[] = [];
  macroaggregato: number = 0;
  utenza: number = 0;
  missione: number = 0;
  spesa: number = 0;
  dataMacro: Date;
  dataUtenza: Date;
  dataPrest2: Date;
  dataPrestMin: Date;
  messaggioPopupElimina: string;
  messaggioPopupAnnulla: string;
  collegaPrestazioni = false;

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }
  salvataggioEffettuato: boolean = false;
  prestazioneSalvata: DettaglioPrestazioneConf = new DettaglioPrestazioneConf();


  constructor(private fb: FormBuilder, private router: Router, private route: ActivatedRoute, private modalService: NgbModal,
    public client: GregBOClient, private dialog: MatDialog, private gregError: GregErrorService, public toastService: AppToastService) {
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    forkJoin({
      tipologie: this.client.getTipologie(),
      strutture: this.client.getStrutture(),
      quote: this.client.getQuote(),
      prestColl: this.client.getPrestColl(),
      macro: this.client.getMacroaggregatiConf(),
      utenze: this.client.getUtenzeConf(),
      missioneProg: this.client.getMissioneProgConf(),
      spese: this.client.getSpeseConf(),
      prest2: this.client.getPrestazioni2Conf(),
      prestMin: this.client.getPrestazioniMinConf(),
      popUpElimina: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINA),
      popUpAnnulla: this.client.getMsgInformativi(SECTION.MESSAGGIOANNULLA),
    })
      .subscribe(({ tipologie, strutture, quote, prestColl, macro, utenze, missioneProg, spese, prest2, prestMin, popUpElimina, popUpAnnulla }) => {
        this.messaggioPopupElimina = popUpElimina[0].testoMsgInformativo;
        this.messaggioPopupAnnulla = popUpAnnulla[0].testoMsgInformativo;
        this.tipologie = tipologie;
        this.strutture = strutture;
        this.quote = quote;
        this.prestColl = prestColl;
        this.macro = macro;
        this.utenze = utenze;
        this.missioneProg = missioneProg;
        this.spese = spese;
        this.prestazioni2 = prest2;
        this.prestazioni2.forEach((e) => {
          e.dal = new Date(e.dal);
          e.al = e.al != null ? new Date(e.al) : null;
          if (e.al != null) {
            e.al.setHours(0, 0, 0, 0);
          }
          e.dataLastRelazione = e.dataLastRelazione != null ? new Date(e.dataLastRelazione) : null;
        })
        this.prestazioni2Filtered = this.prestazioni2;

        this.prestazioni2Form.valueChanges.subscribe(val => {
          if (val == "") {
            this.dettaglioPrestazione2 = null;
          }
          this.prestazioni2Filtered = this.filterValuesPrestazioni2(val);
        })

        this.prestazioniMin = prestMin;
        this.prestazioniMinFiltered = this.prestazioniMin;

        this.prestazioniMinForm.valueChanges.subscribe(val => {
          if (val == "") {
            this.dettaglioPrestazioneMin = null;
          }
          this.prestazioniMinFiltered = this.filterValuesPrestazioniMin(val);
        })

        this.client.spinEmitter.emit(false);
      }
      )
  }

  changePrest2(prest2: Prest1Prest2) {
    prest2.prest2 = !prest2.prest2
  }

  onSelectionChangedTipologia(value: string) {
    this.tip = value;
    this.strutt = null;
    this.q = null;
    this.coll = 0;
  }

  onSelectionChangedStruttura(value: string) {
    this.strutt = value;
  }

  onSelectionChangedQuota(value: string) {
    this.q = value;
  }

  onSelectionChangedCollegamento(value: number) {
    this.coll = value;
  }

  onSelectionChangedMacro(value: number) {
    this.macroaggregato = value;
  }

  onSelectionChangedUtenze(value: number) {
    this.utenza = value;
  }

  onSelectionChangedMissione(value: number) {
    this.missione = value;
  }

  onSelectionChangedSpese(value: number) {
    this.spesa = value;
  }

  onSelectionChangedMissioneUtenza(value: string, i: number) {
    let trovato: boolean = false;
    for (let m of this.missioneProg) {
      if (value == m.codice) {
        trovato = true;
        this.utenzeAssegnati[i].codMissioneProgramma = m.codice;
        this.utenzeAssegnati[i].descMissioneProgramma = m.descrizione;
        this.utenzeAssegnati[i].coloreMissioneProgramma = m.colore;
      }
    }
    if(!trovato){
      this.utenzeAssegnati[i].coloreMissioneProgramma = '#FFFFFFFF';
    }
  }

  onSelectionChangedSpeseUtenza(value: string, i: number) {
    for (let m of this.spese) {
      if (value == m.codice) {
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].codTipologiaSpesa = m.codice;
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].descTipologiaSpesa = m.descrizione;
      }
    }
  }

  optionPrestazioni2Selected(selectedOption: any) {
    let prest = selectedOption.value.split(' - ');
    let dataFine = prest.length > 2 ? prest[2].split('fino al: ')[1] : null;
    this.prestazione2Selezionata.codPrest2 = prest[0];
    let prestTrov: Prest1Prest2 = new Prest1Prest2();
    if (dataFine == null) {
      prestTrov = this.prestazioni2.find((e) => e.codPrest2 == this.prestazione2Selezionata.codPrest2 && e.al == null);
    } else {
      let data = dataFine.split('/');
      let dF = new Date(parseInt(data[2]), parseInt(data[1]) - 1, parseInt(data[0]));
      prestTrov = this.prestazioni2.find((e) => e.codPrest2 == this.prestazione2Selezionata.codPrest2 && e.al != null && e.al.getTime() == dF.getTime());
    }
    this.prestazione2Selezionata.idPrest2 = prestTrov.idPrest2;
  }

  filterValuesPrestazioni2(search: string) {
    if (search == "") {
      this.prestazione2Selezionata = new Prest1Prest2();
    }
    if (search && this.prestazioni2 && this.prestazioni2.length > 0) {
      return this.prestazioni2.filter(
        prest2 =>
          prest2.codPrest2.toUpperCase().trim().includes(search.toUpperCase().trim()) || prest2.descPrest2.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.prestazioni2;
    }
  }

  optionPrestazioniMinSelected(selectedOption: any) {
    const prest = selectedOption.value.split(' - ')[0];

    this.prestazioneMinSelezionata.idPrestMin = this.prestazioniMin.find((e) => e.codPrestMin == prest).idPrestMin;
  }

  filterValuesPrestazioniMin(search: string) {
    if (search == "") {
      this.prestazioneMinSelezionata = new Prest1PrestMin();
    }
    if (search && this.prestazioniMin && this.prestazioniMin.length > 0) {
      return this.prestazioniMin.filter(
        prestMin =>
          prestMin.codPrestMin.toUpperCase().trim().includes(search.toUpperCase().trim()) || prestMin.descPrestMin.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.prestazioniMin;
    }
  }

  getColoreMissioneProgramma(utenza: PrestUtenza, i: number) {
    let styles = {
      'border-top': '5px solid ' + utenza.coloreMissioneProgramma,
      'opacity': '1'
    }

    return styles;
  }

  disableMenu(menu: string) {
    if (this.tip == null || this.tip == undefined) {
      return true;
    }
    if (menu == 'strutt') {
      if (this.tip != 'MA03' && this.tip != 'MA05') {
        return true;
      }
    } else if (menu == 'quota') {
      if (this.tip != 'MA05') {
        return true;
      }
    }
    return false;
  }

  aggiungiMacro() {
    let macro: MacroaggregatiConf = new MacroaggregatiConf();
    this.macro.forEach(element => {
      if (element.id == this.macroaggregato) {
        macro.idMacroaggregati = element.id;
        macro.codMacroaggregati = element.codice;
        macro.descMacroaggregati = element.descrizione;
      }
    })
    macro.dal = this.dataMacro;
    macro.dataMin = macro.dal;
    this.macroAssegnati.push(macro)
    this.macroAssegnati.sort((a, b) =>
      this.sortMacro(a, b)
    )
    this.macroaggregato = 0;
    this.dataMacro = null;
  }

  aggiungiUtenza() {
    let ute: PrestUtenza = new PrestUtenza();
    this.utenze.forEach(element => {
      if (element.id == this.utenza) {
        ute.idUtenza = element.id;
        ute.codUtenza = element.codice;
        ute.descUtenza = element.descrizione;
      }
    })
    if (this.missione != 0) {
      this.missioneProg.forEach(element => {
        if (element.id == this.missione) {
          ute.codMissioneProgramma = element.codice;
          ute.descMissioneProgramma = element.descrizione;
          ute.coloreMissioneProgramma = element.colore;
        }
      })
    } else {
      ute.codMissioneProgramma = 'Vuoto';
    }
    if (this.spesa != 0) {
      this.spese.forEach(element => {
        if (element.id == this.spesa) {
          ute.codTipologiaSpesa = element.codice;
          ute.descTipologiaSpesa = element.descrizione;
        }
      })
    } else {
      ute.codTipologiaSpesa = 'Vuoto';
    }
    ute.dal = this.dataUtenza;
    ute.dataMin = ute.dal;
    this.utenzeAssegnati.push(ute)
    this.utenzeAssegnati.sort((a, b) =>
      this.sortUtenza(a, b)
    )
    this.utenza = 0;
    this.missione = 0;
    this.spesa = 0;
    this.dataUtenza = null;
  }

  aggiungiPrest2() {
    this.client.spinEmitter.emit(true);
    let pre2: Prest1Prest2 = new Prest1Prest2();
    this.client.getDettaglioPrestazioneReg2(this.prestazione2Selezionata.idPrest2).subscribe((element) => {
      pre2.idPrest2 = element.idPrest2;
      pre2.codPrest2 = element.codPrest2;
      pre2.descPrest2 = element.descPrest2;
      pre2.codTipologia = element.codTipologia;
      pre2.ordinamento = element.ordinamento;
      pre2.utenzeConf = element.utenzeConf;
      pre2.dal = element.dal;
      pre2.al = element.al;
      pre2.dataCreazione = element.dataCreazione;

      for (let e of pre2.utenzeConf) {
        if (e.al == null) {
          let utenza: PrestUtenza = new PrestUtenza();
          utenza = this.utenzeAssegnati.find((e) => e.codUtenza == e.codUtenza && e.dataCancellazione == null);
          this.utenzeAssegnati.forEach(u => {
            if (u.codUtenza == e.codUtenza && u.dataCancellazione == null) {
              if (utenza.dal < u.dal) {
                utenza = u;
              }
            }
          })

          e.dal = new Date(e.dal);
          if (utenza != null && utenza.dal.getTime() == e.dal.getTime()) {
            e.al = utenza.al;
          } else if (utenza != null) {
            let ute: PrestUtenza = new PrestUtenza();
            ute.idUtenza = utenza.idUtenza;
            ute.codUtenza = utenza.codUtenza;
            ute.descUtenza = utenza.descUtenza;
            ute.dal = utenza.dal;
            ute.al = utenza.al;
            ute.dataMin = utenza.dal;
            pre2.utenzeConf.push(ute);
            pre2.utenzeConf.sort((a, b) => this.sortUtenza(a, b));
          }
        }
      }
      pre2.prestIstat = element.prestIstat;
      pre2.nomenclatore = element.nomenclatore;
      pre2.dalRelazione = this.dataPrest2;
      pre2.dataMin = pre2.dalRelazione;
      this.prest2Assegnati.push(pre2)
      this.prest2Assegnati.sort((a, b) =>
        this.sortPrest2(a, b)
      )

      this.prestazioni2.forEach(element => {
        if (element.idPrest2 == this.prestazione2Selezionata.idPrest2) {
          element.utilizzato = true;
        }
      })

      pre2.idPrest1 = this.prestazioneSalvata.idPrestazione;
      this.client.modificaPrestazione2(pre2)
        .subscribe(
          (data: GenericResponseWarnErrGreg) => {
            if (data.id == "KO") {
              this.errorMessage.error.descrizione = data.descrizione;
              this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
              this.client.spinEmitter.emit(false);
            } else {
              pre2.dataCreazione = data.dataCreazione;
              this.dettaglioPrestazione.prest1Prest2.sort((a, b) => this.sortPrest2(a, b));
              this.client.spinEmitter.emit(false);
              this.toastService.showSuccess({ text: data.descrizione });
            }

          },
          err => {
            this.client.spinEmitter.emit(false);
          }
        );
      this.prestazioni2Form.reset()
      this.prestazione2Selezionata = new Prest1Prest2();
      this.dataPrest2 = null;
      this.client.spinEmitter.emit(false);
    })

  }

  aggiungiPrestMin() {
    let preMin: Prest1PrestMin = new Prest1PrestMin();
    this.prestazioniMin.forEach(element => {
      if (element.idPrestMin == this.prestazioneMinSelezionata.idPrestMin) {
        preMin.idPrestMin = element.idPrestMin;
        preMin.codPrestMin = element.codPrestMin;
        preMin.descPrestMin = element.descPrestMin;
      }
    })
    preMin.dal = this.dataPrestMin;
    preMin.dataMin = preMin.dal;
    this.prestMinAssegnati.push(preMin)
    this.prestMinAssegnati.sort((a, b) =>
      this.sortPrestMin(a, b)
    )
    this.prestazioniMinForm.reset();
    this.prestazioneMinSelezionata = new Prest1PrestMin();
    this.dataPrestMin = null;
  }

  sortMacro(a: MacroaggregatiConf, b: MacroaggregatiConf) {
    if (a.codMacroaggregati > b.codMacroaggregati) {
      return 1;
    } else if (a.codMacroaggregati < b.codMacroaggregati) {
      return -1;
    } else {
      if (a.al == null) {
        return -1;
      } else if (b.al == null) {
        return 1;
      } else if (a.al > b.al) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  sortUtenza(a: PrestUtenza, b: PrestUtenza) {
    if (a.codUtenza > b.codUtenza) {
      return 1;
    } else if (a.codUtenza < b.codUtenza) {
      return -1;
    } else {
      if (a.al == null) {
        return -1;
      } else if (b.al == null) {
        return 1;
      } else if (a.al > b.al) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  sortPrest2(a: Prest1Prest2, b: Prest1Prest2) {
    if (a.codPrest2 > b.codPrest2) {
      return 1;
    } else if (a.codPrest2 < b.codPrest2) {
      return -1;
    } else {
      if (a.al == null) {
        return -1;
      } else if (b.al == null) {
        return 1;
      } else if (a.al > b.al) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  sortPrestMin(a: Prest1PrestMin, b: Prest1PrestMin) {
    if (a.codPrestMin > b.codPrestMin) {
      return 1;
    } else if (a.codPrestMin < b.codPrestMin) {
      return -1;
    } else {
      if (a.al == null) {
        return -1;
      } else if (b.al == null) {
        return 1;
      } else if (a.al > b.al) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  disableMacro() {
    if (this.macroaggregato == 0) {
      return true;
    }
    let macro: MacroaggregatiConf;
    let trovato: boolean = false;
    this.macroAssegnati.forEach(e => {
      if (e.idMacroaggregati == this.macroaggregato) {
        if (e.al == null) {
          macro = e;
          trovato = true;
        }

      }
    })
    if (trovato) {

      return true;
    }
    return false;
  }

  disableUtenza() {
    if (this.utenza == 0) {
      return true;
    }
    let ute: PrestUtenza;
    let trovato: boolean = false;
    this.utenzeAssegnati.forEach(e => {
      if (e.idUtenza == this.utenza) {
        if (e.al == null) {
          ute = e;
          trovato = true;
        }

      }
    })
    if (trovato) {
      return true;
    }

    return false;
  }

  disablePrest2() {
    if (this.prestazione2Selezionata == null || this.prestazione2Selezionata.idPrest2 == null) {
      return true;
    }
    let pre2: Prest1Prest2;
    let trovato: boolean = false;
    this.prest2Assegnati.forEach(e => {
      if (e == this.prestazione2Selezionata) {
        if (e.al == null)
          pre2 = e;
        trovato = true;
      }
    })
    if (trovato) {
      return true;
    }
    return false;

  }

  disablePrestMin() {
    if (this.prestazioneMinSelezionata == null || this.prestazioneMinSelezionata.idPrestMin == null) {
      return true;
    }
    let trovato: boolean = false;
    this.prestMinAssegnati.forEach((e) => {
      if (e.al == null) {
        trovato = true;
      }
    })
    if (trovato) {
      return true;
    }
    return false;
  }

  disabilitaAlMacro(p: MacroaggregatiConf) {
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.macroAssegnati.forEach((e) => {
        if (p.idMacroaggregati == e.idMacroaggregati) {
          if (e.al == null) {
            trovato = true;
          }
        }

      })
      if (trovato) {
        return true;
      } else {
        let pm: MacroaggregatiConf = new MacroaggregatiConf();
        pm = this.macroAssegnati.length > 0 ? this.macroAssegnati.find((element) => element.idMacroaggregati == this.macroaggregato) : null;
        if (pm) {
          this.macroAssegnati.forEach((e) => {
            if (p.idMacroaggregati == e.idMacroaggregati) {
              if (pm.dal < e.dal) {
                pm = e;
              }
            }
          })
          if (p.dal < pm.dal) {
            return true;
          }
        }
      }
      return false;
    }

  }

  disabilitaAlUtenza(p: PrestUtenza) {
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.utenzeAssegnati.forEach((e) => {
        if (p.idUtenza == e.idUtenza) {
          if (e.al == null) {
            trovato = true;
          }
        }

      })
      if (trovato) {
        return true;
      } else {
        let pm: PrestUtenza = new PrestUtenza();
        pm = this.utenzeAssegnati.length > 0 ? this.utenzeAssegnati.find((element) => element.idUtenza == this.utenza) : null;
        if (pm) {
          this.utenzeAssegnati.forEach((e) => {
            if (p.idUtenza == e.idUtenza) {
              if (pm.dal < e.dal) {
                pm = e;
              }
            }
          })
          if (p.dal < pm.dal) {
            return true;
          }
        }
      }

      return false;
    }

  }

  disabilitaAlPrest2(p: Prest1Prest2) {
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.prest2Assegnati.forEach((e) => {
        if (p.idPrest2 == e.idPrest2) {
          if (e.al == null) {
            trovato = true;
          }
        }

      })
      if (trovato) {
        return true;
      } else {
        let pm: Prest1Prest2 = new Prest1Prest2();
        pm = this.prest2Assegnati.length > 0 ? this.prest2Assegnati.find((element) => element.idPrest2 == this.prestazione2Selezionata.idPrest2) : null;
        if (pm) {
          this.prest2Assegnati.forEach((e) => {
            if (p.idPrest2 == e.idPrest2) {
              if (pm.dal < e.dal) {
                pm = e;
              }
            }
          })
          if (p.dal < pm.dal) {
            return true;
          }
        }
      }
      return false;
    }

  }


  disabilitaAlMin(p: Prest1PrestMin) {
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.prestMinAssegnati.forEach((e) => {
        if (e.al == null) {
          trovato = true;
        }
      })
      if (trovato) {
        return true;
      } else {
        let pm: Prest1PrestMin = new Prest1PrestMin();
        pm = this.prestMinAssegnati.length > 0 ? this.prestMinAssegnati.find((element) => element.idPrestMin == this.prestazioneMinSelezionata.idPrestMin) : null;
        let trovato: boolean = false;
        if (pm) {
          this.prestMinAssegnati.forEach((e) => {
            if (e.dataMin == null) {
              trovato = true;
            }
            if (pm.dal < e.dal) {
              pm = e;
            }
          })
          if (p.dal < pm.dal) {
            return true;
          }
        }
      }
      return false;
    }

  }



  limitMacro() {
    let macro: MacroaggregatiConf;
    macro = this.macroAssegnati.length > 0 ? this.macroAssegnati.find((element) => element.idMacroaggregati == this.macroaggregato) : null;

    this.macroAssegnati.forEach(e => {
      if (e.idMacroaggregati == this.macroaggregato) {
        if (macro.al < e.al) {
          macro = e;
        }
      }
    })

    if (macro != null && macro.al != undefined) {
      let dataMin = new Date(macro.al.getFullYear(), macro.al.getMonth(), macro.al.getDate() + 1);
      if (dataMin.getTime() > this.dal.getTime()) {
        return dataMin;
      } else {
        return this.dal;
      }
    } else {
      return this.dal;
    }
  }

  limitUtenza() {
    let ute: PrestUtenza;
    ute = this.utenzeAssegnati.length > 0 ? this.utenzeAssegnati.find((element) => element.idUtenza == this.utenza) : null;
    this.utenzeAssegnati.forEach(e => {
      if (e.idUtenza == this.utenza) {
        if (ute.al < e.al) {
          ute = e;
        }
      }
    })
    if (ute != null && ute.al != undefined) {
      let dataMin = new Date(ute.al.getFullYear(), ute.al.getMonth(), ute.al.getDate() + 1);
      if (dataMin.getTime() > this.dal.getTime()) {
        return dataMin;
      } else {
        return this.dal;
      }
    } else {
      return this.dal;
    }

  }

  limitPrest2() {
    let pre2: Prest1Prest2;
    pre2 = this.prest2Assegnati.length > 0 ? this.prest2Assegnati.find((element) => element.idPrest2 == this.prestazione2Selezionata.idPrest2) : null;

    this.prest2Assegnati.forEach(e => {
      if (e.idPrest2 == this.prestazione2Selezionata.idPrest2) {
        if (e.al != null) {
          return e.al;
        }
        if (pre2.al < e.al) {
          pre2 = e;
        }
      }
    })

    if (pre2 != null && pre2.al != undefined) {
      let dataMin = new Date(pre2.alRelazione.getFullYear(), pre2.alRelazione.getMonth(), pre2.alRelazione.getDate() + 1);
      if (dataMin.getTime() > this.dal.getTime()) {
        if(pre2.dataLastRelazione==null){
          return dataMin;
        } else if (dataMin.getTime() > pre2.dataLastRelazione.getTime()) {
          return dataMin;
        } else {
          return pre2.dataLastRelazione;
        }
      } else if(pre2.dataLastRelazione==null){
        return this.dal;
      } else if (this.dal.getTime() > pre2.dataLastRelazione.getTime()) {
        return this.dal;
      } else {
        return pre2.dataLastRelazione;
      }

    } else {
      if(pre2 == null || pre2.dataLastRelazione==null){
        return this.dal;
      } else if (pre2 != null && this.dal.getTime() > pre2.dataLastRelazione.getTime()) {
        return this.dal;
      } else {
        return pre2.dataLastRelazione;
      }

    }

  }

  limitPrestMin() {
    let p: Prest1PrestMin = new Prest1PrestMin();
    p = this.prestMinAssegnati.length > 0 ? this.prestMinAssegnati[0] : null;
    let trovato: boolean = false;

    this.prestMinAssegnati.forEach((e) => {
      if (e.al == null) {
        trovato = true;
      }
      if (p != null && p.al < e.al) {
        p = e;
      }
    })

    if (!trovato && p != null && p.al != undefined) {
      let dataMin = new Date(p.al.getFullYear(), p.al.getMonth(), p.al.getDate() + 1);
      if(dataMin.getTime()>this.dal.getTime()){
        return dataMin;
      }else{
        return this.dal;
      }
    } else {
      return this.dal;
    }
  }

  eliminaMacro(macro: MacroaggregatiConf) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'macroaggregato: ' +macro.codMacroaggregati).replace("PADRE", 'prestazione: ' +this.codice);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.macroAssegnati = this.macroAssegnati.filter((element) => {
          return element != macro;
        })
        this.macroAssegnati.sort((a, b) =>
          this.sortMacro(a, b)
        )
      }
    })
  }

  eliminaUtenza(utenza: PrestUtenza) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'utenza: ' +utenza.codUtenza).replace("PADRE", 'prestazione: ' +this.codice);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.utenzeAssegnati = this.utenzeAssegnati.filter((element) => {
          return element != utenza;
        })
        this.utenzeAssegnati.sort((a, b) =>
          this.sortUtenza(a, b)
        )
      }
    })

  }

  eliminaPrest2(prest2: Prest1Prest2) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'prestazione: ' +prest2.codPrest2).replace("PADRE", 'prestazione: ' +this.codice);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.prest2Assegnati = this.prest2Assegnati.filter((element) => {
          return element != prest2;
        })
        this.prest2Assegnati.sort((a, b) =>
          this.sortPrest2(a, b)
        )
      }
    })
  }

  eliminaPrestMin(p: Prest1PrestMin) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'prestazione: ' +p.codPrestMin).replace("PADRE", 'prestazione: ' +this.codice);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.prestMinAssegnati = this.prestMinAssegnati.filter((element) => {
          return element != p;
        })
        this.prestMinAssegnati.sort((a, b) =>
          this.sortPrestMin(a, b)
        )
      }
    });
  }

  limitValorizzatoMacro(m: MacroaggregatiConf) {
    let macro: MacroaggregatiConf;
    this.macroAssegnati.forEach(e => {
      if (e == m) {
        macro = e;
      }
    })
    if (macro != null) {
      return macro.dataMin;
    }

  }

  limitValorizzatoUtenza(u: PrestUtenza) {
    let ute: PrestUtenza;
    this.utenzeAssegnati.forEach(e => {
      if (e == u) {
        ute = e;
      }
    })
    if (ute != null) {
      return ute.dataMin;
    }

  }

  limitValorizzatoPrest2(p: Prest1Prest2) {
    let pre2: Prest1Prest2;
    this.prest2Assegnati.forEach(e => {
      if (e == p) {
        pre2 = e;
      }
    })
    if (pre2 != null) {
      return pre2.dataMin;
    }

  }

  limitValorizzatoPrestMin(pre: Prest1PrestMin) {
    let p: Prest1PrestMin;
    this.prestMinAssegnati.forEach((e) => {
      if (pre == e) {
        p = e;
      }
    })
    if (p != null) {
      return p.dataMin;
    }
  }
  valorizzaAl(prest2: Prest1Prest2) {
    this.prestazioni2Form.reset();
    this.prestazione2Selezionata = new Prest1Prest2();
    this.dataPrest2 = null;
    this.prestazioni2Filtered.forEach((e) => {
      if (e.idPrest2 == prest2.idPrest2) {
        if (prest2.al == null) {
          e.utilizzato = true;
        } else {
          e.utilizzato = false;
        }

      }
    })
  }

  annullaModifica() {
    this.tip = null;
    this.strutt = null;
    this.q = null;
    this.coll = 0;
    this.codice = null;
    this.descrizione = null;
    this.dal = null;
    this.al = null;
    this.macroAssegnati = [];
    this.utenzeAssegnati = [];
    this.nota = null;
  }

  numberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    if (event.target.value.length <= 9 && (event.target.selectionEnd - event.target.selectionStart) > 0) {
      return true;
    }
    if (event.target.value.length >= 9) {
      return false;
    }

    return true;
  }

  attivaSalva() {
    if (this.codice == null ||
      this.descrizione == null ||
      this.dal == null ||
      this.ordinamento == null ||
      this.ordinamento == "" ||
      this.codice == "" ||
      this.descrizione == "" ||
      this.tip == null ||
      this.tip == "null"
      || this.codice.trim().length==0 
      || this.descrizione.trim().length==0) {
      return true;
    }
    if (this.tip == 'MA03') {
      if (this.strutt == null || this.strutt == "null") {
        return true;
      }
    }
    if (this.tip == 'MA05') {
      if (this.q == null || this.coll == null || this.q == "null" || this.coll == 0) {
        return true;
      }
    }
    return false;
  }

  collegaPrestazione() {
    
    this.client.spinEmitter.emit(true);
    if (this.salvataggioEffettuato) {
      this.dettaglioPrestazione.idPrestazione = this.prestazioneSalvata.idPrestazione;
    }
    this.dettaglioPrestazione = new DettaglioPrestazioneConf();
    this.dettaglioPrestazione.codTipologia = this.tip;
    this.dettaglioPrestazione.tipoStruttura = this.strutt;
    this.dettaglioPrestazione.tipoQuota = this.q;
    let prestazioneCollegata = new Prest1PrestCollegate();
    prestazioneCollegata.idPrestRegionale = this.coll;
    this.dettaglioPrestazione.prestazioniCollegate = [];
    this.dettaglioPrestazione.prestazioniCollegate.push(prestazioneCollegata);
    this.dettaglioPrestazione.ordinamento = parseInt(this.ordinamento);
    this.dettaglioPrestazione.codPrestRegionale = this.codice;
    this.dettaglioPrestazione.desPrestRegionale = this.descrizione;
    this.dettaglioPrestazione.dal = this.dal;
    this.dettaglioPrestazione.al = this.al;
    this.dettaglioPrestazione.notaPrestazione = this.nota;
    this.dettaglioPrestazione.macroaggregati = this.macroAssegnati;
    this.dettaglioPrestazione.targetUtenzaPrestReg1 = this.utenzeAssegnati;
    this.dettaglioPrestazione.prest1Prest2 = this.prest2Assegnati;
    this.dettaglioPrestazione.prest1PrestMin = this.prestMinAssegnati;

    if (!this.salvataggioEffettuato) {
      this.client.savePrestazione(this.dettaglioPrestazione)
        .subscribe(
          (data: GenericResponseWarnErrGreg) => {
            if (data.id == "KO") {
              this.errorMessage.error.descrizione = data.descrizione;
              this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
              this.client.spinEmitter.emit(false);
            } else {
              this.prestazioneSalvata.idPrestazione = data.idPrestazione;
              
              this.collegaPrestazioni = true;
              this.salvataggioEffettuato = true;
              this.toastService.showSuccess({ text: data.descrizione });
              this.client.spinEmitter.emit(false);
            }

          },
          err => {
            this.client.spinEmitter.emit(false);
          }
        );
    }
    
  }

  nuovaPrestazione2() {
    this.client.spinEmitter.emit(true);
    forkJoin({
      prestIstat: this.client.getIstatConf(),
      utenzeIstat: this.client.getUtenzeIstatConf(this.utenzeAssegnati),
      nomenclatore: this.client.getNomenclatoreConf()
    }).subscribe(({ prestIstat, utenzeIstat, nomenclatore }) => {
      let utenzeValide: PrestUtenza[] = [];
      this.utenzeAssegnati.forEach((e) => {
        if (e.al == null && e.dataCancellazione == null) {
          utenzeValide.push(e);
        }
      })
      const modalRef = this.modalService.open(NuovaPrestazione2Component, { size: 'lg', windowClass: 'my-class' });
      modalRef.componentInstance.titolo = 'Nuova Prestazione 2'
      modalRef.componentInstance.tipologie = this.tipologie;
      modalRef.componentInstance.utenze = utenzeValide;
      modalRef.componentInstance.prestIstat = prestIstat;
      modalRef.componentInstance.utenzeIstat = utenzeIstat;
      modalRef.componentInstance.nomenclatoreList = nomenclatore;
      modalRef.componentInstance.messaggio = this.messaggioPopupElimina;
      modalRef.componentInstance.dalPrest1 = this.dal;
      modalRef.componentInstance.alPrest1 = this.al;
      this.client.spinEmitter.emit(false);
      modalRef.result.then((result: Prest1Prest2) => {
        if (result != null) {
          result.idPrest1 = this.prestazioneSalvata.idPrestazione;
          this.client.savePrestazione2(result)
            .subscribe(
              (data: GenericResponseWarnErrGreg) => {
                if (data.id == "KO") {
                  this.errorMessage.error.descrizione = data.descrizione;
                  this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                  this.client.spinEmitter.emit(false);
                } else {
                  this.prest2Assegnati.push(result);
                  this.prest2Assegnati.sort((a, b) => this.sortPrest2(a, b));
                  this.toastService.showSuccess({ text: data.descrizione });
                  this.client.spinEmitter.emit(false);
                }

              },
              err => {
                this.client.spinEmitter.emit(false);
              }
            );

        }
      }).catch((res) => { });
    })

  }

  modificaPrestazione2(prest2: Prest1Prest2) {
    this.client.spinEmitter.emit(true);
    forkJoin({
      prestIstat: this.client.getIstatConf(),
      utenzeIstat: this.client.getUtenzeIstatConf(prest2.utenzeConf),
      nomenclatore: this.client.getNomenclatoreConf(),
      prest2I: this.client.getDettaglioPrestazioneReg2(prest2.idPrest2)
    }).subscribe(({ prestIstat, utenzeIstat, nomenclatore, prest2I}) => {
      let prest2Initial: Prest1Prest2 = new Prest1Prest2();
      prest2Initial = prest2I;
      prest2Initial.dalRelazione = prest2.dalRelazione;
      prest2Initial.utilizzato = prest2.utilizzato;
      let utenzeValide: PrestUtenza[] = [];
      this.utenzeAssegnati.forEach((e) => {
        if (e.al == null && e.dataCancellazione == null) {
          utenzeValide.push(e);
        }
      })
      const modalRef = this.modalService.open(ModificaPrestazione2Component, { size: 'lg', windowClass: 'my-class' });
      modalRef.componentInstance.titolo = 'Modifica Prestazione 2'
      modalRef.componentInstance.prest2 = prest2;
      modalRef.componentInstance.prest2Initial = prest2Initial;
      modalRef.componentInstance.tipologie = this.tipologie;
      modalRef.componentInstance.utenze = this.utenzeAssegnati;
      modalRef.componentInstance.prestIstat = prestIstat;
      modalRef.componentInstance.utenzeIstat = utenzeIstat;
      modalRef.componentInstance.nomenclatoreList = nomenclatore;
      modalRef.componentInstance.messaggio = this.messaggioPopupElimina;
      modalRef.componentInstance.dalPrest1 = this.dal;
      modalRef.componentInstance.alPrest1 = this.al;
      this.client.spinEmitter.emit(false);
      modalRef.result.then((result) => {
        if (result != null) {
          this.client.modificaPrestazione2(result)
            .subscribe(
              (data: GenericResponseWarnErrGreg) => {
                if (data.id == "KO") {
                  this.errorMessage.error.descrizione = data.descrizione;
                  this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                  this.client.spinEmitter.emit(false);
                } else {
                  
                  this.dettaglioPrestazione.prest1Prest2.sort((a, b) => this.sortPrest2(a, b));
                  
                  this.toastService.showSuccess({ text: data.descrizione });
                  this.client.spinEmitter.emit(false);
                }

              },
              err => {
                this.client.spinEmitter.emit(false);
              }
            );

        }
      }).catch((res) => { });
    })
  }

  salvaPrestazione1() {
    this.client.spinEmitter.emit(true);
   
    this.dettaglioPrestazione = new DettaglioPrestazioneConf(); 
    if (this.salvataggioEffettuato) {
      this.dettaglioPrestazione.idPrestazione = this.prestazioneSalvata.idPrestazione;
    }
    this.dettaglioPrestazione.codTipologia = this.tip;
    this.dettaglioPrestazione.tipoStruttura = this.strutt;
    this.dettaglioPrestazione.tipoQuota = this.q;
    let prestazioneCollegata = new Prest1PrestCollegate();
    prestazioneCollegata.idPrestRegionale = this.coll;
    this.dettaglioPrestazione.prestazioniCollegate = [];
    this.dettaglioPrestazione.prestazioniCollegate.push(prestazioneCollegata);
    this.dettaglioPrestazione.ordinamento = parseInt(this.ordinamento);
    this.dettaglioPrestazione.codPrestRegionale = this.codice;
    this.dettaglioPrestazione.desPrestRegionale = this.descrizione;
    this.dettaglioPrestazione.dal = this.dal;
    this.dettaglioPrestazione.al = this.al;
    this.dettaglioPrestazione.notaPrestazione = this.nota;
    this.dettaglioPrestazione.macroaggregati = this.macroAssegnati;
    this.dettaglioPrestazione.targetUtenzaPrestReg1 = this.utenzeAssegnati;
    this.dettaglioPrestazione.prest1Prest2 = this.prest2Assegnati;
    this.dettaglioPrestazione.prest1PrestMin = this.prestMinAssegnati;
    let utenzeComplete = true;
    if (this.dettaglioPrestazione.prest1Prest2.length > 0) {
      this.dettaglioPrestazione.prest1Prest2.forEach(p => {
        p.utenzeConf.forEach((e) => {
          if (e.al == null && e.dataCancellazione == null) {
            let u = this.dettaglioPrestazione.targetUtenzaPrestReg1.find(u => u.codUtenza == e.codUtenza && u.al == null && u.dataCancellazione == null)
            if (u != null && u.dal > e.dal) {
              utenzeComplete = false;
            }
          }
        })
      })
    }
    if (!utenzeComplete) {
      this.errorMessage.error.descrizione = "Le prestazioni 2 non hanno le stesse relazioni con le utenze rispetto alla prestazione 1;";
      this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: "Le prestazioni 2 non hanno le stesse relazioni con le utenze rispetto alla prestazione 1;" }));
      this.client.spinEmitter.emit(false);
    } else {
      if (!this.salvataggioEffettuato) {
        this.client.savePrestazione(this.dettaglioPrestazione)
          .subscribe(
            (data: GenericResponseWarnErrGreg) => {
              if (data.id == "KO") {
                this.errorMessage.error.descrizione = data.descrizione;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                this.client.spinEmitter.emit(false);
              } else {
                this.prestazioneSalvata.idPrestazione = data.idPrestazione;
                this.salvataggioEffettuato = true;
                this.toastService.showSuccess({ text: data.descrizione });
                this.client.spinEmitter.emit(false);

              }

            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
      } else {
        this.client.modificaPrestazione(this.dettaglioPrestazione)
          .subscribe(
            (data: GenericResponseWarnErrGreg) => {
              if (data.id == "KO") {
                this.errorMessage.error.descrizione = data.descrizione;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                this.client.spinEmitter.emit(false);
              } else {
                this.toastService.showSuccess({ text: data.descrizione });
                this.client.spinEmitter.emit(false);

              }

            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
      }
    }
  }

  limitPrest() {
    if (this.dal) {
      let dataMin = new Date(this.dal.getFullYear(), this.dal.getMonth(), this.dal.getDate() + 1);
      return dataMin;
    }

  }

  dataAl(prest: Prest1Prest2) {
    if (prest.al == null) {
      return true;
    }
    return false;
  }

}
