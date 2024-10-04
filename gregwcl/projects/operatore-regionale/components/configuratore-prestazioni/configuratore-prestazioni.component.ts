import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
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
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { CancPopupComponent } from './canc-popup/canc-popup.component';
import { ModificaPrestazione2Component } from './modifica-prestazione2/modifica-prestazione2.component';
import { NuovaPrestazione2Component } from './nuova-prestazione2/nuova-prestazione2component';

@Component({
  selector: 'app-configuratore-prestazioni',
  templateUrl: './configuratore-prestazioni.component.html',
  styleUrls: ['./configuratore-prestazioni.component.css']
})
export class ConfiguratorePrestazioniComponent implements OnInit {

  prestazioni: DettaglioPrestazioneConf[] = [];
  prestazioniFiltered: DettaglioPrestazioneConf[] = [];
  prestazioniForm = new FormControl();

  prestazioneSelezionata: DettaglioPrestazioneConf = new DettaglioPrestazioneConf();
  dettaglioPrestazione: DettaglioPrestazioneConf;
  dettaglioPrestazioneInitial: DettaglioPrestazioneConf;
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
  navigation: Navigation;
  codPrest: string;
  dataAttuale: Date;
  prestazioneColl: Prest1PrestCollegate = null;
  haMoltePrestColl: boolean = false;

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }
  modificaAlPassato: boolean;
  messaggioPopupSalva: string;

  constructor(private fb: FormBuilder, private router: Router, private route: ActivatedRoute, private gregError: GregErrorService,
    public client: GregBOClient, private dialog: MatDialog, private modalService: NgbModal, public toastService: AppToastService) {
    this.navigation = this.router.getCurrentNavigation();
    this.codPrest = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.prestazione : null;
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    forkJoin({
      prestazioni: this.client.getPrestazioniReg1(),
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
      popUpSalva: this.client.getMsgInformativi(SECTION.MESSAGGIOSALVACONMODPASS),
    })
      .subscribe(({ prestazioni, tipologie, strutture, quote, prestColl, macro, utenze, missioneProg, spese, prest2, prestMin, popUpElimina, popUpAnnulla, popUpSalva }) => {

        this.dataAttuale = new Date();
        this.messaggioPopupElimina = popUpElimina[0].testoMsgInformativo;
        this.messaggioPopupAnnulla = popUpAnnulla[0].testoMsgInformativo;
        this.messaggioPopupSalva = popUpSalva[0].testoMsgInformativo;
        this.prestazioni = prestazioni;
        this.prestazioni.forEach((e) => {
          e.dal = new Date(e.dal);
          e.al = e.al != null ? new Date(e.al) : null;
          if (e.al != null) {
            e.al.setHours(0, 0, 0, 0);
          }
        })
        this.tipologie = tipologie;
        this.strutture = strutture;
        this.quote = quote;
        this.prestColl = prestColl;
        this.macro = macro;
        this.utenze = utenze;
        this.missioneProg = missioneProg;
        this.spese = spese;
        this.prestazioniFiltered = this.prestazioni;
        this.prestazioniForm.valueChanges.subscribe(val => {
          if (val == "") {
            this.dettaglioPrestazione = null;
          }
          this.prestazioniFiltered = this.filterValuesPrestazioni(val);
        })
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
        if (this.codPrest != null) {
          this.prestazioniForm.patchValue(this.codPrest);
          this.optionPrestazioniSelected(this.prestazioniForm);
        }
        this.client.spinEmitter.emit(false);
      }
      )
  }

  changePrest2(prest2: Prest1Prest2) {
    prest2.prest2 = !prest2.prest2
  }

  onSelectionChangedTipologia(value: string) {
    this.tip = value;
    this.dettaglioPrestazione.codTipologia = this.tip;
    this.strutt = null;
    this.q = null;
    this.coll = 0;
  }

  onSelectionChangedStruttura(value: string) {
    this.strutt = value;
    this.dettaglioPrestazione.tipoStruttura = this.strutt;
  }

  onSelectionChangedQuota(value: string) {
    this.q = value;
    this.dettaglioPrestazione.tipoQuota = this.q;
  }

  onSelectionChangedCollegamento(value: number) {
    this.coll = value;
    let p = this.prestazioni.find(e => e.idPrestazione == this.coll);
    if (this.dettaglioPrestazione.prestazioniCollegate.length = 1) {
      if(p != null){
        let prestC: Prest1PrestCollegate = new Prest1PrestCollegate();
        prestC.idPrestRegionale = p.idPrestazione;
        prestC.codPrestRegionale = p.codPrestRegionale;
        prestC.desPrestRegionale = p.desPrestRegionale;
        this.dettaglioPrestazione.prestazioniCollegate[0] = prestC;
      } else {
        let prestC: Prest1PrestCollegate = new Prest1PrestCollegate();
        prestC.idPrestRegionale = 0;
        this.dettaglioPrestazione.prestazioniCollegate[0] = prestC;
      }
    }

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
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].codMissioneProgramma = m.codice;
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].descMissioneProgramma = m.descrizione;
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].coloreMissioneProgramma = m.colore;
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].modificabile) {
          this.modificaAlPassato = true;
        }
      }
    }
    if (!trovato) {
      this.dettaglioPrestazione.targetUtenzaPrestReg1[i].coloreMissioneProgramma = '#FFFFFFFF';
    }

  }

  onSelectionChangedSpeseUtenza(value: string, i: number) {
    for (let m of this.spese) {
      if (value == m.codice) {
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].codTipologiaSpesa = m.codice;
        this.dettaglioPrestazione.targetUtenzaPrestReg1[i].descTipologiaSpesa = m.descrizione;
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].modificabile) {
          this.modificaAlPassato = true;
        }
      }
    }
  }

  optionPrestazioniSelected(selectedOption: any) {
    this.client.spinEmitter.emit(true);
    this.prestazioneColl = null;
    let prest = selectedOption.value.split(' - ');
    let dataFine = prest.length > 2 ? prest[2].split('fino al: ')[1] : null;
    this.prestazioneSelezionata.codPrestRegionale = prest[0];
    let prestTrov: DettaglioPrestazioneConf = new DettaglioPrestazioneConf();
    if (dataFine == null) {
      prestTrov = this.prestazioni.find((e) => e.codPrestRegionale == this.prestazioneSelezionata.codPrestRegionale && e.al == null);
    } else {
      let data = dataFine.split('/');
      let dF = new Date(parseInt(data[2]), parseInt(data[1]) - 1, parseInt(data[0]));
      prestTrov = this.prestazioni.find((e) => e.codPrestRegionale == this.prestazioneSelezionata.codPrestRegionale && e.al != null && e.al.getTime() == (dF.getTime()));
    }

    forkJoin({
      prest: this.client.getDettaglioPrestazioneReg1(prestTrov.idPrestazione),
      prestInitial: this.client.getDettaglioPrestazioneReg1(prestTrov.idPrestazione),
    }).subscribe(({ prest, prestInitial }) => {
      this.dettaglioPrestazione = prest;
      this.tip = this.dettaglioPrestazione.codTipologia;
      this.strutt = this.dettaglioPrestazione.tipoStruttura;
      this.q = this.dettaglioPrestazione.tipoQuota;
      this.dettaglioPrestazione.dal = new Date(this.dettaglioPrestazione.dal);
      this.dettaglioPrestazione.al = this.dettaglioPrestazione.al != null ? new Date(this.dettaglioPrestazione.al) : null;
      this.dettaglioPrestazione.dataMin = new Date(this.dettaglioPrestazione.dataMin);
      for (let m of this.dettaglioPrestazione.macroaggregati) {
        m.dal = new Date(m.dal);
        m.al = m.al != null ? new Date(m.al) : null;
        m.dataMin = new Date(m.dataMin);
      }
      for (let u of this.dettaglioPrestazione.targetUtenzaPrestReg1) {
        u.dal = new Date(u.dal);
        u.al = u.al != null ? new Date(u.al) : null;
        u.dataMin = new Date(u.dataMin);
      }
      for (let u of this.dettaglioPrestazione.targetUtenzaPrestReg1) {
        u.dal = new Date(u.dal);
        u.al = u.al != null ? new Date(u.al) : null;
        u.dataMin = new Date(u.dataMin);
      }
      for (let p of this.dettaglioPrestazione.prest1Prest2) {
        p.dal = new Date(p.dal);
        p.al = p.al != null ? new Date(p.al) : null;
        p.dataMin = new Date(p.dataMin);
        p.dalRelazione = new Date(p.dalRelazione);
        p.alRelazione = p.alRelazione != null ? new Date(p.alRelazione) : null;
      }
      for (let p of this.dettaglioPrestazione.prest1PrestMin) {
        p.dal = new Date(p.dal);
        p.al = p.al != null ? new Date(p.al) : null;
        p.dataMin = new Date(p.dataMin);
      }
      if (this.dettaglioPrestazione.prestazioniCollegate.length == 1) {
        this.coll = this.dettaglioPrestazione.prestazioniCollegate[0].idPrestRegionale;
        this.prestazioneColl = this.dettaglioPrestazione.prestazioniCollegate[0];
      } else {
        if(this.dettaglioPrestazione.prestazioniCollegate.length > 1) {
          this.haMoltePrestColl = true;
          this.coll = 0;
        }else {
          this.coll = 0;
        }  
      }
      this.dettaglioPrestazione.prestazioniCollegate.forEach((p)=>{
        p.al = p.al != null ? new Date(p.al) : null;
        p.dataFine = p.al !=null ? p.al.getDate() + '/' + (p.al.getMonth() + 1) + '/' + p.al.getFullYear() : null;
      })
      
      for (let u of this.dettaglioPrestazione.prest1Prest2) {
        u.prest2 = false;
      }
      this.dettaglioPrestazioneInitial = prestInitial;
      this.dettaglioPrestazioneInitial.dal = new Date(this.dettaglioPrestazioneInitial.dal);
      this.dettaglioPrestazioneInitial.al = this.dettaglioPrestazioneInitial.al != null ? new Date(this.dettaglioPrestazioneInitial.al) : null;
      this.dettaglioPrestazioneInitial.dataMin = new Date(this.dettaglioPrestazioneInitial.dataMin);
      for (let m of this.dettaglioPrestazioneInitial.macroaggregati) {
        m.dal = new Date(m.dal);
        m.al = m.al != null ? new Date(m.al) : null;
        m.dataMin = new Date(m.dataMin);
      }
      for (let u of this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1) {
        u.dal = new Date(u.dal);
        u.al = u.al != null ? new Date(u.al) : null;
        u.dataMin = new Date(u.dataMin);
      }
      for (let u of this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1) {
        u.dal = new Date(u.dal);
        u.al = u.al != null ? new Date(u.al) : null;
        u.dataMin = new Date(u.dataMin);
      }
      for (let p of this.dettaglioPrestazioneInitial.prest1Prest2) {
        p.dal = new Date(p.dal);
        p.al = p.al != null ? new Date(p.al) : null;
        p.dataMin = new Date(p.dataMin);
        p.dalRelazione = new Date(p.dalRelazione);
        p.alRelazione = p.alRelazione != null ? new Date(p.alRelazione) : null;
      }
      for (let p of this.dettaglioPrestazioneInitial.prest1PrestMin) {
        p.dal = new Date(p.dal);
        p.al = p.al != null ? new Date(p.al) : null;
        p.dataMin = new Date(p.dataMin);

      }
      if (this.dettaglioPrestazioneInitial.prestazioniCollegate.length == 1) {
        this.coll = this.dettaglioPrestazioneInitial.prestazioniCollegate[0].idPrestRegionale;
      } else {
        this.coll = 0;
      }
      for (let u of this.dettaglioPrestazioneInitial.prest1Prest2) {
        u.prest2 = false;
      }
      this.dettaglioPrestazione.macroaggregati.sort((a, b) => this.sortMacro(a, b));
      this.dettaglioPrestazione.targetUtenzaPrestReg1.sort((a, b) => this.sortUtenza(a, b));
      this.dettaglioPrestazione.prest1Prest2.sort((a, b) => this.sortPrest2(a, b));
      this.dettaglioPrestazione.prest1PrestMin.sort((a, b) => this.sortPrestMin(a, b));
      this.dettaglioPrestazioneInitial.macroaggregati.sort((a, b) => this.sortMacro(a, b));
      this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1.sort((a, b) => this.sortUtenza(a, b));
      this.dettaglioPrestazioneInitial.prest1Prest2.sort((a, b) => this.sortPrest2(a, b));
      this.dettaglioPrestazioneInitial.prest1PrestMin.sort((a, b) => this.sortPrestMin(a, b));


      this.client.spinEmitter.emit(false);
    },
      err => {
        this.client.spinEmitter.emit(false)
      });
  }

  filterValuesPrestazioni(search: string) {
    if (search == "") {
      this.prestazioneSelezionata = new DettaglioPrestazioneConf();
    }
    if (search && this.prestazioni && this.prestazioni.length > 0) {
      return this.prestazioni.filter(
        prest =>
          prest.codPrestRegionale.toUpperCase().trim().includes(search.toUpperCase().trim()) || prest.desPrestRegionale.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.prestazioni;
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
    this.dettaglioPrestazione.macroaggregati.push(macro)
    this.dettaglioPrestazione.macroaggregati.sort((a, b) =>
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
    this.dettaglioPrestazione.targetUtenzaPrestReg1.push(ute)
    this.dettaglioPrestazione.targetUtenzaPrestReg1.sort((a, b) =>
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
          utenza = this.dettaglioPrestazione.targetUtenzaPrestReg1.find((u) => u.codUtenza == e.codUtenza && u.dataCancellazione == null);
          this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach(u => {
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
      // pre2.utenzeConf.forEach(e => {

      // })
      pre2.prestIstat = element.prestIstat;
      pre2.nomenclatore = element.nomenclatore;
      pre2.dalRelazione = this.dataPrest2;
      pre2.dataMin = pre2.dalRelazione;
      this.dettaglioPrestazione.prest1Prest2.push(pre2)
      this.dettaglioPrestazione.prest1Prest2.sort((a, b) =>
        this.sortPrest2(a, b)
      )

      this.prestazioni2.forEach(element => {
        if (element.idPrest2 == this.prestazione2Selezionata.idPrest2) {
          element.utilizzato = true;
        }
      })
      pre2.idPrest1 = this.dettaglioPrestazione.idPrestazione;
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

              this.prestazioni2Form.reset()
              this.prestazione2Selezionata = new Prest1Prest2();
              this.dataPrest2 = null;
              this.client.spinEmitter.emit(false);
              this.toastService.showSuccess({ text: data.descrizione });
            }

          },
          err => {
            this.client.spinEmitter.emit(false);
          }
        );


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
    this.dettaglioPrestazione.prest1PrestMin.push(preMin)
    this.dettaglioPrestazione.prest1PrestMin.sort((a, b) =>
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
      if (a.alRelazione == null) {
        return -1;
      } else if (b.alRelazione == null) {
        return 1;
      } else if (a.alRelazione > b.alRelazione) {
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
    this.dettaglioPrestazione.macroaggregati.forEach(e => {
      if (e.idMacroaggregati == this.macroaggregato && e.dataCancellazione == null) {
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
    this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach(e => {
      if (e.idUtenza == this.utenza && e.dataCancellazione == null) {
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
    this.dettaglioPrestazione.prest1Prest2.forEach(e => {
      if (e == this.prestazione2Selezionata && e.dataCancellazione == null) {
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
    this.dettaglioPrestazione.prest1PrestMin.forEach((e) => {
      if (e.al == null && e.dataCancellazione == null) {
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
      this.dettaglioPrestazione.macroaggregati.forEach((e) => {
        if (p.idMacroaggregati == e.idMacroaggregati && e.dataCancellazione == null) {
          if (e.al == null) {
            trovato = true;
          }
        }

      })
      if (trovato) {
        return true;
      } else {
        let pm: MacroaggregatiConf = new MacroaggregatiConf();
        pm = this.dettaglioPrestazione.macroaggregati.length > 0 ?
          this.dettaglioPrestazione.macroaggregati.find((element) => element.idMacroaggregati == p.idMacroaggregati && element.dataCancellazione == null) : null;
        let trovato: boolean = false;
        this.dettaglioPrestazione.macroaggregati.forEach((e) => {
          if (p.idMacroaggregati == e.idMacroaggregati && e.dataCancellazione == null) {
            if (pm.dal < e.dal) {
              pm = e;
            }
          }
        })
        if (p.dal < pm.dal) {
          return true;
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
      this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach((e) => {
        if (p.idUtenza == e.idUtenza && e.dataCancellazione == null) {
          if (e.al == null) {
            trovato = true;
          }
        }

      })
      if (trovato) {
        return true;
      } else {
        let pm: PrestUtenza = new PrestUtenza();
        pm = this.dettaglioPrestazione.targetUtenzaPrestReg1.length > 0 ? this.dettaglioPrestazione.targetUtenzaPrestReg1.find((element) => element.idUtenza == p.idUtenza && element.dataCancellazione == null) : null;
        let trovato: boolean = false;
        this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach((e) => {
          if (p.idUtenza == e.idUtenza && e.dataCancellazione == null) {
            if (pm.dal < e.dal) {
              pm = e;
            }
          }
        })
        if (p.dal < pm.dal) {
          return true;
        }
      }
      return false;
    }

  }

  disabilitaAlPrest2(p: Prest1Prest2) {
    if (p.alRelazione == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.dettaglioPrestazione.prest1Prest2.forEach((e) => {
        if (p.idPrest2 == e.idPrest2 && e.dataCancellazione == null) {
          if (e.alRelazione == null) {
            trovato = true;
          }
        }

      })
      if (trovato) {
        return true;
      } else {
        let pm: Prest1Prest2 = new Prest1Prest2();
        pm = this.dettaglioPrestazione.prest1Prest2.length > 0 ? this.dettaglioPrestazione.prest1Prest2.find((element) => element.idPrest2 == p.idPrest2 && element.dataCancellazione == null) : null;
        let trovato: boolean = false;
        this.dettaglioPrestazione.prest1Prest2.forEach((e) => {
          if (p.idPrest2 == e.idPrest2 && e.dataCancellazione == null) {
            if (pm.dalRelazione < e.dalRelazione) {
              pm = e;
            }
          }
        })
        if (p.dalRelazione < pm.dalRelazione) {
          return true;
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
      this.dettaglioPrestazione.prest1PrestMin.forEach((e) => {
        if (e.al == null && e.dataCancellazione == null) {
          trovato = true;
        }
      })
      if (trovato) {
        return true;
      } else {
        let pm: Prest1PrestMin = new Prest1PrestMin();
        pm = this.dettaglioPrestazione.prest1PrestMin.length > 0 ? this.dettaglioPrestazione.prest1PrestMin.find((e) => e.dataCancellazione == null) : null;
        let trovato: boolean = false;
        this.dettaglioPrestazione.prest1PrestMin.forEach((e) => {
          if (e.al == null && e.dataCancellazione == null) {
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
      return false;
    }

  }

  limitMacro() {
    let macro: MacroaggregatiConf;
    macro = this.dettaglioPrestazione.macroaggregati.length > 0 ?
      this.dettaglioPrestazione.macroaggregati.find((element) => element.idMacroaggregati == this.macroaggregato && element.dataCancellazione == null) : null;
    if (macro) {
      this.dettaglioPrestazione.macroaggregati.forEach(e => {
        if (e.idMacroaggregati == this.macroaggregato && e.dataCancellazione == null) {
          if (macro != null && macro.al < e.al) {
            macro = e;
          }
        }
      })
      if (macro != null && macro.al != undefined) {
        let dataMin = new Date();
        dataMin = new Date(macro.al.getFullYear(), macro.al.getMonth(), macro.al.getDate() + 1);
        if (dataMin.getTime() > this.dettaglioPrestazione.dal.getTime()) {
          return dataMin;
        } else {
          return this.dettaglioPrestazione.dal;
        }

      } else {
        return this.dettaglioPrestazione.dal;
      }
    } else {
      return this.dettaglioPrestazione.dal;
    }

  }

  limitUtenza() {
    let ute: PrestUtenza;
    ute = this.dettaglioPrestazione.targetUtenzaPrestReg1.length > 0 ? this.dettaglioPrestazione.targetUtenzaPrestReg1.find((element) => element.idUtenza == this.utenza && element.dataCancellazione == null) : null;
    if (ute) {
      this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach(e => {
        if (e.idUtenza == this.utenza && e.dataCancellazione == null) {
          if (ute != null && ute.al < e.al) {
            ute = e;
          }
        }
      })
      if (ute != null && ute.al != undefined) {
        let dataMin = new Date();
        dataMin = new Date(ute.al.getFullYear(), ute.al.getMonth(), ute.al.getDate() + 1);
        if (dataMin.getTime() > this.dettaglioPrestazione.dal.getTime()) {
          return dataMin;
        } else {
          return this.dettaglioPrestazione.dal;
        }

      } else {
        return this.dettaglioPrestazione.dal;
      }
    } else {
      return this.dettaglioPrestazione.dal;
    }
  }

  limitPrest2() {
    let pre2: Prest1Prest2;
    pre2 = this.dettaglioPrestazione.prest1Prest2.length > 0 ? this.dettaglioPrestazione.prest1Prest2.find((element) => element.idPrest2 == this.prestazione2Selezionata.idPrest2 && element.dataCancellazione == null) : null;
    if (pre2) {
      this.dettaglioPrestazione.prest1Prest2.forEach(e => {
        if (e.idPrest2 == this.prestazione2Selezionata.idPrest2 && e.dataCancellazione == null) {
          // if (e.alRelazione != null) {
          //   return e.alRelazione;
          // }
          if (pre2.alRelazione != null && pre2.alRelazione < e.alRelazione) {
            pre2 = e;
          }
        }
      })
      if (pre2 != null && pre2.alRelazione != undefined) {
        let dataMin = new Date();
        dataMin = new Date(pre2.alRelazione.getFullYear(), pre2.alRelazione.getMonth(), pre2.alRelazione.getDate() + 1);
        if (dataMin.getTime() > this.dettaglioPrestazione.dal.getTime()) {
          if (pre2.dataLastRelazione == null) {
            return dataMin;
          } else if (dataMin.getTime() > pre2.dataLastRelazione.getTime()) {
            return dataMin;
          } else {
            return pre2.dataLastRelazione;
          }
        } else if (pre2.dataLastRelazione == null) {
          return this.dettaglioPrestazione.dal;
        } else if (this.dettaglioPrestazione.dal.getTime() > pre2.dataLastRelazione.getTime()) {
          return this.dettaglioPrestazione.dal;
        } else {
          return pre2.dataLastRelazione;
        }

      } else {
        if (pre2 == null || pre2.dataLastRelazione == null) {
          return this.dettaglioPrestazione.dal;
        } else if (pre2 != null && this.dettaglioPrestazione.dal.getTime() > pre2.dataLastRelazione.getTime()) {
          return this.dettaglioPrestazione.dal;
        } else {
          return pre2.dataLastRelazione;
        }

      }
    } else {
      return this.dettaglioPrestazione.dal;
    }
  }

  limitPrestMin() {
    let p: Prest1PrestMin = new Prest1PrestMin();
    p = this.dettaglioPrestazione.prest1PrestMin.length > 0 ? this.dettaglioPrestazione.prest1PrestMin.find((e) => e.dataCancellazione == null) : null;
    let trovato: boolean = false;

    this.dettaglioPrestazione.prest1PrestMin.forEach((e) => {
      if (e.al == null && e.dataCancellazione == null) {
        trovato = true;
      }
      if (p != null && p.al < e.al && e.dataCancellazione == null) {
        p = e;
      }
    })
    if (!trovato && p != null && p.al != undefined) {
      let dataMin = new Date();
      dataMin = new Date(p.al.getFullYear(), p.al.getMonth(), p.al.getDate() + 1);

      if (dataMin.getTime() > this.dettaglioPrestazione.dal.getTime()) {
        return dataMin;
      } else {
        return this.dettaglioPrestazione.dal;
      }

    } else {
      return this.dettaglioPrestazione.dal;
    }

  }

  eliminaMacro(macro: MacroaggregatiConf) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'macroaggregato: ' + macro.codMacroaggregati).replace("PADRE", 'prestazione: ' + this.dettaglioPrestazione.codPrestRegionale);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        macro.dataCancellazione = this.dataAttuale;
        this.dettaglioPrestazione.macroaggregati.sort((a, b) =>
          this.sortMacro(a, b)
        )
      }
    })
  }

  eliminaUtenza(utenza: PrestUtenza) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'utenza: ' + utenza.codUtenza).replace("PADRE", 'prestazione: ' + this.dettaglioPrestazione.codPrestRegionale);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        utenza.dataCancellazione = this.dataAttuale;
        this.dettaglioPrestazione.targetUtenzaPrestReg1.sort((a, b) =>
          this.sortUtenza(a, b)
        )
      }
    })

  }

  eliminaPrest2(prest2: Prest1Prest2) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'prestazione: ' + prest2.codPrest2).replace("PADRE", 'prestazione: ' + this.dettaglioPrestazione.codPrestRegionale);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        prest2.dataCancellazione = this.dataAttuale;
        this.prestazioni2.find((e) => e.idPrest2 == prest2.idPrest2).utilizzato = false;
        this.dettaglioPrestazione.prest1Prest2.sort((a, b) =>
          this.sortPrest2(a, b)
        )
      }
    })
  }

  eliminaPrestMin(p: Prest1PrestMin) {
    let messaggio = this.messaggioPopupElimina.replace("ELIMINATA", 'prestazione: ' + p.codPrestMin).replace("PADRE", 'prestazione: ' + this.dettaglioPrestazione.codPrestRegionale);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        p.dataCancellazione = this.dataAttuale;
        this.dettaglioPrestazione.prest1PrestMin.sort((a, b) =>
          this.sortPrestMin(a, b)
        )
      }
    });
  }

  limitValorizzatoMacro(m: MacroaggregatiConf) {
    let macro: MacroaggregatiConf;
    this.dettaglioPrestazione.macroaggregati.forEach(e => {
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
    this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach(e => {
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
    this.dettaglioPrestazione.prest1Prest2.forEach(e => {
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
    this.dettaglioPrestazione.prest1PrestMin.forEach((e) => {
      if (pre == e) {
        p = e;
      }
    })
    if (p != null) {

      return p.dataMin;
    }
  }

  vai(codPrest: string) {
    this.client.spinEmitter.emit(true);
    let currentUrl = this.router.url;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      skipLocationChange: true,
      state: {
        prestazione: codPrest,
      }
    };
    this.router.navigateByUrl('/configuratore-prestazioni', navigationExtras).then(() => {
      this.router.navigate([currentUrl], navigationExtras);
    });
  }

  valorizzaAl(prest2: Prest1Prest2) {
    prest2.utenzeConf.forEach((u) => {
      if (u.al == null && prest2.alRelazione != null) {
        u.al = prest2.alRelazione;
      }
      if (prest2.alRelazione == null) {
        u.al = prest2.alRelazione;
        this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach((t) => {
          if (t.idUtenza == u.idUtenza && t.al != null) {
            u.al = t.al;
          }
        })
      }
    })
    this.prestazioni2Form.reset();
    this.prestazione2Selezionata = new Prest1Prest2();
    this.dataPrest2 = null;
    this.prestazioni2Filtered.forEach((e) => {
      if (e.idPrest2 == prest2.idPrest2) {
        if (prest2.alRelazione == null) {
          e.utilizzato = true;
        }
        // else {
        //   e.utilizzato = false;
        // }

      }
    })
  }

  annullaModifica() {
    let messaggio = this.messaggioPopupAnnulla.replace("PRESTAZIONE", this.dettaglioPrestazione.codPrestRegionale);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Annulla Modifiche', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.client.spinEmitter.emit(true);
        this.client.getDettaglioPrestazioneReg1(this.dettaglioPrestazione.idPrestazione).subscribe((result) => {
          this.dettaglioPrestazione = result;
          this.tip = this.dettaglioPrestazione.codTipologia;
          this.strutt = this.dettaglioPrestazione.tipoStruttura;
          this.q = this.dettaglioPrestazione.tipoQuota;
          this.dettaglioPrestazione.dal = new Date(this.dettaglioPrestazione.dal);
          this.dettaglioPrestazione.al = this.dettaglioPrestazione.al != null ? new Date(this.dettaglioPrestazione.al) : null;
          this.dettaglioPrestazione.dataMin = new Date(this.dettaglioPrestazione.dataMin);
          for (let m of this.dettaglioPrestazione.macroaggregati) {
            m.dal = new Date(m.dal);
            m.al = m.al != null ? new Date(m.al) : null;
            m.dataMin = new Date(m.dataMin);
          }
          for (let u of this.dettaglioPrestazione.targetUtenzaPrestReg1) {
            u.dal = new Date(u.dal);
            u.al = u.al != null ? new Date(u.al) : null;
            u.dataMin = new Date(u.dataMin);
          }
          for (let u of this.dettaglioPrestazione.targetUtenzaPrestReg1) {
            u.dal = new Date(u.dal);
            u.al = u.al != null ? new Date(u.al) : null;
            u.dataMin = new Date(u.dataMin);
          }
          for (let p of this.dettaglioPrestazione.prest1Prest2) {
            p.dal = new Date(p.dal);
            p.al = p.al != null ? new Date(p.al) : null;
            p.dalRelazione = new Date(p.dalRelazione);
            p.alRelazione = p.alRelazione != null ? new Date(p.alRelazione) : null;
            p.dataMin = new Date(p.dataMin);
          }
          for (let p of this.dettaglioPrestazione.prest1PrestMin) {
            p.dal = new Date(p.dal);
            p.al = p.al != null ? new Date(p.al) : null;
            p.dataMin = new Date(p.dataMin);
          }
          if (this.dettaglioPrestazione.prestazioniCollegate.length == 1) {
            this.coll = this.dettaglioPrestazione.prestazioniCollegate[0].idPrestRegionale;
          }
          for (let u of this.dettaglioPrestazione.prest1Prest2) {
            u.prest2 = false;
          }
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false)
          });
      }
    })
  }

  nuovaPrestazione2() {
    this.client.spinEmitter.emit(true);
    forkJoin({
      prestIstat: this.client.getIstatConf(),
      utenzeIstat: this.client.getUtenzeIstatConf(this.dettaglioPrestazione.targetUtenzaPrestReg1),
      nomenclatore: this.client.getNomenclatoreConf()
    }).subscribe(({ prestIstat, utenzeIstat, nomenclatore }) => {
      let utenzeValide: PrestUtenza[] = [];
      this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach((e) => {
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
      modalRef.componentInstance.dalPrest1 = this.dettaglioPrestazione.dal;
      modalRef.componentInstance.alPrest1 = this.dettaglioPrestazione.al;
      modalRef.componentInstance.titolo = 'Modifica Prestazione 2'
      this.client.spinEmitter.emit(false);
      modalRef.result.then((result: Prest1Prest2) => {
        if (result != null) {
          result.idPrest1 = this.dettaglioPrestazione.idPrestazione;
          this.client.savePrestazione2(result)
            .subscribe(
              (data: GenericResponseWarnErrGreg) => {
                if (data.id == "KO") {
                  this.errorMessage.error.descrizione = data.descrizione;
                  this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                  this.client.spinEmitter.emit(false);
                } else {
                  this.dettaglioPrestazione.prest1Prest2.push(result);
                  this.dettaglioPrestazione.prest1Prest2.sort((a, b) => this.sortPrest2(a, b));
                  this.client.spinEmitter.emit(false);
                  this.toastService.showSuccess({ text: data.descrizione });
                }

              },
              err => {
                this.client.spinEmitter.emit(false);
              }
            );

        }
      }).catch((res) => {

      });
    })

  }

  modificaPrestazione2(prest2: Prest1Prest2) {
    this.client.spinEmitter.emit(true);
    forkJoin({
      prestIstat: this.client.getIstatConf(),
      utenzeIstat: this.client.getUtenzeIstatConf(prest2.utenzeConf),
      nomenclatore: this.client.getNomenclatoreConf(),
      prest2I: this.client.getDettaglioPrestazioneReg2(prest2.idPrest2),

    }).subscribe(({ prestIstat, utenzeIstat, nomenclatore, prest2I }) => {
      let prest2Initial: Prest1Prest2 = new Prest1Prest2();
      prest2Initial = prest2I;
      prest2Initial.dalRelazione = prest2.dalRelazione;
      prest2Initial.utilizzato = prest2.utilizzato;
      let utenzeValide: PrestUtenza[] = [];
      this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach((e) => {
        if (e.al == null && e.dataCancellazione == null) {
          utenzeValide.push(e);
        }
      })
      const modalRef = this.modalService.open(ModificaPrestazione2Component, { size: 'lg', windowClass: 'my-class' });
      modalRef.componentInstance.titolo = 'Modifica Prestazione 2'
      modalRef.componentInstance.prest2 = prest2;
      modalRef.componentInstance.prest2Initial = prest2Initial;
      modalRef.componentInstance.tipologie = this.tipologie;
      modalRef.componentInstance.utenze = utenzeValide;
      modalRef.componentInstance.prestIstat = prestIstat;
      modalRef.componentInstance.utenzeIstat = utenzeIstat;
      modalRef.componentInstance.nomenclatoreList = nomenclatore;
      modalRef.componentInstance.messaggio = this.messaggioPopupElimina;
      modalRef.componentInstance.dalPrest1 = this.dettaglioPrestazione.dal;
      modalRef.componentInstance.alPrest1 = this.dettaglioPrestazione.al;
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
                  this.client.spinEmitter.emit(false);
                  this.toastService.showSuccess({ text: data.descrizione });
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

  valorizzaUtenzaAl(p: PrestUtenza) {
    this.client.spinEmitter.emit(true);
    if (this.dettaglioPrestazione.prest1Prest2.length > 0) {
      this.dettaglioPrestazione.prest1Prest2.forEach((e) => {
        let utenza = e.utenzeConf.find((u) => u.idUtenza == p.idUtenza)
        if (utenza != null) {
          utenza.al = p.al;
          this.client.getUtenzeIstatTransConf(utenza.codUtenza).subscribe((element: ListeConfiguratore) => {
            e.prestIstat.forEach(i => {
              let ute = i.utenzeMinConf.find((u) => u.idUtenza == element.id);
              if (ute != null && ute != undefined && ute.codUtenza != 'U21') {
                ute.al = p.al;
              }

            });
            this.client.spinEmitter.emit(false);
          })
        } else {
          this.client.spinEmitter.emit(false);
        }
      });
    } else {
      this.client.spinEmitter.emit(false);
    }


  }

  disableSave() {
    let trovato: boolean = true;
    let lunghezzaDiversa: boolean = false;
    if (this.dettaglioPrestazione.al != null && this.dettaglioPrestazione.al.getTime() < this.dettaglioPrestazione.dataMin.getTime()) {
      trovato = true;
    }
    if (this.dettaglioPrestazione.al != null && this.dettaglioPrestazioneInitial.al == null) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.al == null && this.dettaglioPrestazioneInitial.al != null) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.al != null && this.dettaglioPrestazioneInitial.al != null && this.dettaglioPrestazione.al.getDate() != this.dettaglioPrestazioneInitial.al.getDate()) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.codPrestRegionale != this.dettaglioPrestazioneInitial.codPrestRegionale) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.codTipologia != this.dettaglioPrestazioneInitial.codTipologia) {
      trovato = false;
    }
    if(this.dettaglioPrestazione.prestazioniCollegate.length == 1 && this.dettaglioPrestazioneInitial.prestazioniCollegate.length == 0){
      trovato = false;
    }
    if(this.dettaglioPrestazione.prestazioniCollegate.length == 0 && this.dettaglioPrestazioneInitial.prestazioniCollegate.length == 1){
      trovato = false;
    }
    if (this.dettaglioPrestazione.prestazioniCollegate.length == 1 && this.dettaglioPrestazioneInitial.prestazioniCollegate.length == 1) {
      if(this.dettaglioPrestazione.prestazioniCollegate[0].idPrestRegionale != this.dettaglioPrestazioneInitial.prestazioniCollegate[0].idPrestRegionale){
        trovato = false;
      };
    } 
    if (this.dettaglioPrestazione.dal.getTime() != this.dettaglioPrestazioneInitial.dal.getTime()) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.desPrestRegionale != this.dettaglioPrestazioneInitial.desPrestRegionale) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.idPrestazione != this.dettaglioPrestazioneInitial.idPrestazione) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.tipoQuota != this.dettaglioPrestazioneInitial.tipoQuota) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.tipoStruttura != this.dettaglioPrestazioneInitial.tipoStruttura) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.codTipologia != this.dettaglioPrestazioneInitial.codTipologia) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.notaPrestazione != this.dettaglioPrestazioneInitial.notaPrestazione) {
      trovato = false;
    }
    if (this.dettaglioPrestazione.ordinamento != this.dettaglioPrestazioneInitial.ordinamento) {
      trovato = false;
    }

    lunghezzaDiversa = false;
    let c: number = 0;
    this.dettaglioPrestazione.macroaggregati.forEach(e => {
      if (e.dataCancellazione == null) {
        c++;
      }
    })
    if (c != this.dettaglioPrestazioneInitial.macroaggregati.length) {
      trovato = false;
      lunghezzaDiversa = true;
    }
    if (!lunghezzaDiversa) {
      for (let i = 0; i < this.dettaglioPrestazione.macroaggregati.length; i++) {
        if (this.dettaglioPrestazione.macroaggregati[i].idMacroaggregati != this.dettaglioPrestazioneInitial.macroaggregati[i].idMacroaggregati) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.macroaggregati[i].dal.getTime() != this.dettaglioPrestazioneInitial.macroaggregati[i].dal.getTime()) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.macroaggregati[i].al == null && this.dettaglioPrestazioneInitial.macroaggregati[i].al != null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.macroaggregati[i].al != null && this.dettaglioPrestazioneInitial.macroaggregati[i].al == null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.macroaggregati[i].al != null && this.dettaglioPrestazioneInitial.macroaggregati[i].al != null && this.dettaglioPrestazione.macroaggregati[i].al.getTime() != this.dettaglioPrestazioneInitial.macroaggregati[i].al.getTime()) {
          trovato = false;
        }
      }
    }
    lunghezzaDiversa = false;
    c = 0;
    this.dettaglioPrestazione.prest1Prest2.forEach(e => {
      if (e.dataCancellazione == null) {
        c++;
      }
    })
    if (c != this.dettaglioPrestazioneInitial.prest1Prest2.length) {
      trovato = false;
      lunghezzaDiversa = true;
    }
    if (!lunghezzaDiversa) {
      for (let i = 0; i < this.dettaglioPrestazione.prest1Prest2.length; i++) {
        if (this.dettaglioPrestazione.prest1Prest2[i].idPrest2 != this.dettaglioPrestazioneInitial.prest1Prest2[i].idPrest2) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.prest1Prest2[i].dalRelazione.getTime() != this.dettaglioPrestazioneInitial.prest1Prest2[i].dalRelazione.getTime()) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.prest1Prest2[i].alRelazione == null && this.dettaglioPrestazioneInitial.prest1Prest2[i].alRelazione != null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.prest1Prest2[i].alRelazione != null && this.dettaglioPrestazioneInitial.prest1Prest2[i].alRelazione == null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.prest1Prest2[i].alRelazione != null && this.dettaglioPrestazioneInitial.prest1Prest2[i].alRelazione != null && this.dettaglioPrestazione.prest1Prest2[i].alRelazione.getTime() != this.dettaglioPrestazioneInitial.prest1Prest2[i].alRelazione.getTime()) {
          trovato = false;
        }
      }
    }
    lunghezzaDiversa = false;
    c = 0;
    this.dettaglioPrestazione.prest1PrestMin.forEach(e => {
      if (e.dataCancellazione == null) {
        c++;
      }
    })
    if (c != this.dettaglioPrestazioneInitial.prest1PrestMin.length) {
      trovato = false;
      lunghezzaDiversa = true;
    }
    if (!lunghezzaDiversa) {
      for (let i = 0; i < this.dettaglioPrestazione.prest1PrestMin.length; i++) {
        if (this.dettaglioPrestazione.prest1PrestMin[i].idPrestMin != this.dettaglioPrestazioneInitial.prest1PrestMin[i].idPrestMin) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.prest1PrestMin[i].dal.getTime() != this.dettaglioPrestazioneInitial.prest1PrestMin[i].dal.getTime()) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.prest1PrestMin[i].al == null && this.dettaglioPrestazioneInitial.prest1PrestMin[i].al != null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.prest1PrestMin[i].al != null && this.dettaglioPrestazioneInitial.prest1PrestMin[i].al == null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.prest1PrestMin[i].al != null && this.dettaglioPrestazioneInitial.prest1PrestMin[i].al != null && this.dettaglioPrestazione.prest1PrestMin[i].al.getTime() != this.dettaglioPrestazioneInitial.prest1PrestMin[i].al.getTime()) {
          trovato = false;
        }
      }
    }
    lunghezzaDiversa = false;
    c = 0;
    this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach(e => {
      if (e.dataCancellazione == null) {
        c++;
      }
    })
    if (c != this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1.length) {
      trovato = false;
      lunghezzaDiversa = true;
    }
    if (!lunghezzaDiversa) {
      for (let i = 0; i < this.dettaglioPrestazione.targetUtenzaPrestReg1.length; i++) {
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].idUtenza != this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].idUtenza) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].dal.getTime() != this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].dal.getTime()) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].al == null && this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].al != null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].al != null && this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].al == null) {
          trovato = false;
        } else if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].al != null && this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].al != null && this.dettaglioPrestazione.targetUtenzaPrestReg1[i].al.getTime() != this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].al.getTime()) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].codMissioneProgramma != this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].codMissioneProgramma) {
          trovato = false;
        }
        if (this.dettaglioPrestazione.targetUtenzaPrestReg1[i].codTipologiaSpesa != this.dettaglioPrestazioneInitial.targetUtenzaPrestReg1[i].codTipologiaSpesa) {
          trovato = false;
        }
      }
    }
    if (this.dettaglioPrestazione.al != null && this.dettaglioPrestazione.al.getTime() < this.dettaglioPrestazione.dataMin.getTime()) {
      trovato = true;
    }
    this.dettaglioPrestazione.macroaggregati.forEach(e => {
      if (e.al != null && this.dettaglioPrestazione.al != null && e.al < this.limitValorizzatoMacro(e) || e.al > this.dettaglioPrestazione.al) {
        trovato = true;
      }
    })
    this.dettaglioPrestazione.prest1Prest2.forEach(e => {
      if (e.al != null && this.dettaglioPrestazione.al != null && e.al < this.limitValorizzatoPrest2(e) || e.al > this.dettaglioPrestazione.al) {
        trovato = true;
      }
    })
    this.dettaglioPrestazione.prest1PrestMin.forEach(e => {
      if (e.al != null && this.dettaglioPrestazione.al != null && e.al < this.limitValorizzatoPrestMin(e) || e.al > this.dettaglioPrestazione.al) {
        trovato = true;
      }
    })
    this.dettaglioPrestazione.targetUtenzaPrestReg1.forEach(e => {
      if (e.al != null && this.dettaglioPrestazione.al != null && e.al < this.limitValorizzatoUtenza(e) || e.al > this.dettaglioPrestazione.al) {
        trovato = true;
      }
    })
    return trovato;
  }

  dataAl(prest: Prest1Prest2) {
    if (prest.al == null) {
      return true;
    }
    return false;
  }

  dataAlPReg1(prest: DettaglioPrestazioneConf) {
    if (prest.al == null) {
      return true;
    }
    return false;
  }

  dataAlPRegColl(prest: Prest1PrestCollegate) {
    if (prest.al == null) {
      return true;
    }
    return false;
  }


  salvaPrestazione1() {
    this.client.spinEmitter.emit(true);
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
      if (this.modificaAlPassato) {
        let messaggio = this.messaggioPopupSalva.replace("PRESTAZIONE", this.dettaglioPrestazione.codPrestRegionale);
        const dialogRef = this.dialog.open(CancPopupComponent, {
          width: '650px',
          disableClose: true,
          autoFocus: true,
          data: { titolo: 'Salva Modifiche', messaggio: messaggio }
        });
        this.client.spinEmitter.emit(false);
        dialogRef.afterClosed().subscribe(r => {
          if (r) {
            this.client.spinEmitter.emit(true);
            this.client.modificaPrestazione(this.dettaglioPrestazione)
              .subscribe(
                (data: GenericResponseWarnErrGreg) => {
                  if (data.id == "KO") {
                    this.errorMessage.error.descrizione = data.descrizione;
                    this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                    this.client.spinEmitter.emit(false);
                  } else {
                    this.prestazioni.find((e) => e.codPrestRegionale == this.dettaglioPrestazioneInitial.codPrestRegionale && (e != null && e.al == null || (e.al.getTime() == (this.dettaglioPrestazioneInitial.al.getTime())))).al = this.dettaglioPrestazione.al;
                    let dataFine = null;
                    if (this.dettaglioPrestazione.al != null) {
                      dataFine = this.dettaglioPrestazione.al.getDate() + '/' + (this.dettaglioPrestazione.al.getMonth() + 1) + '/' + this.dettaglioPrestazione.al.getFullYear();
                    }
                    this.dettaglioPrestazioneInitial.tipoQuota = this.dettaglioPrestazione.tipoQuota;
                    this.dettaglioPrestazioneInitial.tipoStruttura = this.dettaglioPrestazione.tipoStruttura;
                    this.dettaglioPrestazioneInitial.codTipologia = this.dettaglioPrestazione.codTipologia;
                    if (this.dettaglioPrestazione.prestazioniCollegate.length == 1) {
                      this.dettaglioPrestazioneInitial.prestazioniCollegate[0] = this.dettaglioPrestazione.prestazioniCollegate[0];
                    }
                    this.prestazioniForm.patchValue(this.dettaglioPrestazione.al == null ? this.dettaglioPrestazione.codPrestRegionale + " - " + this.dettaglioPrestazione.desPrestRegionale : this.dettaglioPrestazione.codPrestRegionale + " - " + this.dettaglioPrestazione.desPrestRegionale + ' - fino al: ' + dataFine);
                    this.optionPrestazioniSelected(this.prestazioniForm);
                    this.toastService.showSuccess({ text: data.descrizione });
                  }

                },
                err => {
                  this.client.spinEmitter.emit(false);
                }
              );
          } else {
            this.client.spinEmitter.emit(false);
          }
        });
      } else {
        this.client.modificaPrestazione(this.dettaglioPrestazione)
          .subscribe(
            (data: GenericResponseWarnErrGreg) => {
              if (data.id == "KO") {
                this.errorMessage.error.descrizione = data.descrizione;
                this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: data.descrizione }));
                this.client.spinEmitter.emit(false);
              } else {
                // this.ngOnInit();
                this.prestazioni.find((e) => e.codPrestRegionale == this.dettaglioPrestazioneInitial.codPrestRegionale && ((e.al == null && this.dettaglioPrestazioneInitial.al==null) || (e.al != null && this.dettaglioPrestazioneInitial.al!=null && e.al.getTime() == this.dettaglioPrestazioneInitial.al.getTime()))).al = this.dettaglioPrestazione.al;
                let dataFine = null;
                if (this.dettaglioPrestazione.al != null) {
                  dataFine = this.dettaglioPrestazione.al.getDate() + '/' + (this.dettaglioPrestazione.al.getMonth() + 1) + '/' + this.dettaglioPrestazione.al.getFullYear();
                }
                this.dettaglioPrestazioneInitial.tipoQuota = this.dettaglioPrestazione.tipoQuota;
                this.dettaglioPrestazioneInitial.tipoStruttura = this.dettaglioPrestazione.tipoStruttura;
                this.dettaglioPrestazioneInitial.codTipologia = this.dettaglioPrestazione.codTipologia;
                if (this.dettaglioPrestazione.prestazioniCollegate.length == 1) {
                  this.dettaglioPrestazioneInitial.prestazioniCollegate[0] = this.dettaglioPrestazione.prestazioniCollegate[0];
                }
                this.dettaglioPrestazioneInitial.codTipologia = this.dettaglioPrestazione.codTipologia;
                this.prestazioneColl = null;
                this.prestazioniForm.patchValue(this.dettaglioPrestazione.al == null ? this.dettaglioPrestazione.codPrestRegionale + " - " + this.dettaglioPrestazione.desPrestRegionale : this.dettaglioPrestazione.codPrestRegionale + " - " + this.dettaglioPrestazione.desPrestRegionale + ' - fino al: ' + dataFine);
                this.optionPrestazioniSelected(this.prestazioniForm);
                this.toastService.showSuccess({ text: data.descrizione });
              }

            },
            err => {
              this.client.spinEmitter.emit(false);
            }
          );
      }
    }
  }

}
