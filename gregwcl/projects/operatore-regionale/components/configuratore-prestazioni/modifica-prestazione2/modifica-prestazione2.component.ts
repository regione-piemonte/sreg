import { Component, Inject, OnInit } from "@angular/core";
import { FormControl } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { ListeConfiguratore } from "@greg-app/app/dto/ListeConfiguratore";
import { Nomenclatore } from "@greg-app/app/dto/Nomenclatore";
import { Prest1Prest2 } from "@greg-app/app/dto/Prest1Prest2";
import { Prest2PrestIstat } from "@greg-app/app/dto/Prest2PrestIstat";
import { PrestUtenza } from "@greg-app/app/dto/PrestUtenza";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { CancPopupComponent } from "../canc-popup/canc-popup.component";

@Component({
  selector: 'app-modifica-prestazione2',
  templateUrl: './modifica-prestazione2.component.html',
  styleUrls: ['./modifica-prestazione2.component.css']
})
export class ModificaPrestazione2Component implements OnInit {
  attenzione: string = '';
  tip: string = null;
  prest2: Prest1Prest2;
  prest2Initial: Prest1Prest2;
  ordinamento: number;
  codice: string;
  descrizione: string;
  nota: string;
  dal: Date;
  al: Date;
  dataPrest: Date;
  dataUtenza: Date;
  dataNomenclatore: Date;
  utenzeAssegnati: PrestUtenza[] = [];
  prestIstatAssegnate: Prest2PrestIstat[];
  utenza: number = 0;
  prestazioneIstat: number = 0;
  utenzaIstat: string = '0';
  titolo: string;
  tipologie: ListeConfiguratore[];
  utenze: PrestUtenza[];
  prestIstat: ListeConfiguratore[];
  utenzeIstat: ListeConfiguratore[];
  messaggio: string;
  nomenclatoreList: Nomenclatore[];
  nomenclatoreForm = new FormControl();
  nomenclatoreSelezionata: Nomenclatore = new Nomenclatore();
  nomenclatoreFiltered: Nomenclatore[];
  nomenclatore: Nomenclatore;
  nomenclatoreAssegnato: Nomenclatore[] = [];
  dettaglioNomenclatore: any;
  dataAttuale: Date;
  dalPrest1: Date;
  alPrest1: Date;
  constructor(public activeModal: NgbActiveModal, private dialog: MatDialog,
    public client: GregBOClient) { }

  ngOnInit() {
    this.dataAttuale = new Date();
    this.prest2.dal = new Date(this.prest2.dal);
    this.nomenclatoreFiltered = this.nomenclatoreList;
    this.nomenclatoreForm.valueChanges.subscribe(val => {
      if (val == "") {
        this.dettaglioNomenclatore = null;
      }
      this.nomenclatoreFiltered = this.filterValuesNomenclatore(val);
    })
    this.tip = this.prest2.codTipologia;
    let trovato = false;
    for (let p of this.prest2.prestIstat) {
      trovato = false;
      for (let u of p.utenzeMinConf) {
        u.cancellabile = false;
        if (u.al == null) {
          trovato = true;
        }
      }
      if (trovato) {
        this.prestazioneIstat = p.idPrestIstat;
      }
    }
    for (let p of this.prest2.nomenclatore) {
      p.cancellabile = false;
    }
  }

  onSelectionChangedTipologia(value: string) {
    this.tip = value;
  }

  onSelectionChangedUtenze(value: number) {
    this.utenza = value;
  }

  onSelectionChangedIstat(value: number) {
    this.prestazioneIstat = value;
  }
  onSelectionChangedUtenzeIstat(value: string) {
    this.utenzaIstat = value;
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

  aggiungiUtenza() {
    let ute: PrestUtenza = new PrestUtenza();
    this.client.spinEmitter.emit(true);
    this.utenze.forEach(element => {
      if (element.idUtenza == this.utenza) {
        ute.idUtenza = element.idUtenza;
        ute.codUtenza = element.codUtenza;
        ute.descUtenza = element.descUtenza;
        ute.dal = element.dal;
        ute.al = element.al;
        ute.dataMin = element.dataMin;
      }
    })
    this.prest2.utenzeConf.push(ute)
    this.prest2.utenzeConf.sort((a, b) =>
      this.sortUtenza(a, b)
    )
    this.utenzeIstat = [];
    this.client.getUtenzeIstatConf(this.prest2.utenzeConf).subscribe(element => {
      this.utenzeIstat = element;
      this.client.spinEmitter.emit(false);
    })
    this.utenza = 0;
    this.dataUtenza = null;
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


  disableUtenza() {
    if (this.utenza == 0) {
      return true;
    }
    let trovato: boolean = false;
    this.prest2.utenzeConf.forEach(e => {
      if (e.idUtenza == this.utenza && e.dataCancellazione == null) {
        trovato = true;
      }
    })
    if (trovato) {
      return true;
    }

    return false;
  }

  disabilitaAlUtenza(p: PrestUtenza) {
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.prest2.utenzeConf.forEach((e) => {
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
        pm = this.prest2.utenzeConf.length > 0 ? this.prest2.utenzeConf.find((e) => e.idUtenza == p.idUtenza && e.dataCancellazione == null) : null;
        this.prest2.utenzeConf.forEach((e) => {
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

  limitUtenza() {
    let ute: PrestUtenza;
    ute = this.prest2.utenzeConf.length > 0 ? this.prest2.utenzeConf.find((element) => element.idUtenza == this.utenza && element.dataCancellazione == null) : null;
    this.prest2.utenzeConf.forEach(e => {
      if (e.idUtenza == this.utenza && e.dataCancellazione == null) {
        if (ute.al < e.al) {
          ute = e;
        }
      }
    })
    if (ute != null && ute.al != undefined) {
      let dataMin = new Date(ute.al.getFullYear(), ute.al.getMonth(), ute.al.getDate() + 1);

      if (dataMin.getTime() > this.prest2.dal.getTime()) {
        return dataMin;
      } else {
        return this.prest2.dal;
      }

    } else {
      return this.prest2.dal;
    }

  }

  limitValorizzatoUtenza(u: PrestUtenza) {
    let ute: PrestUtenza;
    this.prest2.utenzeConf.forEach(e => {
      if (e == u && e.dataCancellazione == null) {
        ute = e;
      }
    })
    if (ute != null) {
      return ute.dataMin;
    }

  }

  eliminaUtenza(utenza: PrestUtenza) {
    let messaggio = this.messaggio.replace("ELIMINATA", 'utenza: ' + utenza.codUtenza).replace("PADRE", 'prestazione: ' + this.prest2.codPrest2);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.client.spinEmitter.emit(true);
        utenza.dataCancellazione = this.dataAttuale;
        this.client.getUtenzeIstatTransConf(utenza.codUtenza).subscribe((element: ListeConfiguratore) => {
          this.prest2.prestIstat.forEach(i => {
            let ute = i.utenzeMinConf.find((u) => u.idUtenza == element.id);
            if (ute != null && ute != undefined && ute.codUtenza != 'U21') {
              ute.al = this.dataAttuale;
            }

          });
          this.prest2.utenzeConf.sort((a, b) =>
            this.sortUtenza(a, b)
          )
          this.client.spinEmitter.emit(false);

        });

      }
    })

  }

  eliminaUtenzaIstat(utenza: PrestUtenza) {
    let messaggio = this.messaggio.replace("ELIMINATA", 'utenza: ' + utenza.codUtenza).replace("PADRE", 'prestazione: ' + this.prest2.codPrest2);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        utenza.dataCancellazione = this.dataAttuale;
        this.utenzeAssegnati.sort((a, b) =>
          this.sortUtenza(a, b)
        )
      }
    })

  }


  aggiungiIstat() {
    let istat = this.prestazioneIstat;
    let prest: Prest2PrestIstat = new Prest2PrestIstat();
    prest = this.prest2.prestIstat.find((e) => istat == e.idPrestIstat);
    if (prest != null) {
      let ute: PrestUtenza = new PrestUtenza();
      this.utenzeIstat.forEach(element => {
        if (element.codice == this.utenzaIstat) {
          ute.idUtenza = element.id;
          ute.codUtenza = element.codice;
          ute.descUtenza = element.descrizione;
        }
      })
      ute.dal = this.dataPrest;
      ute.dataMin = ute.dal;
      ute.cancellabile = true;
      prest.utenzeMinConf.push(ute);
      prest.utenzeMinConf.sort((a, b) =>
        this.sortUtenza(a, b)
      )
    } else {
      let newPrest: Prest2PrestIstat = new Prest2PrestIstat();
      let istat: ListeConfiguratore = new ListeConfiguratore();
      istat = this.prestIstat.find((e) => e.id == this.prestazioneIstat);
      newPrest.idPrestIstat = istat.id;
      newPrest.codPrestIstat = istat.codice;
      newPrest.descPrestIstat = istat.descrizione;
      let ute: PrestUtenza = new PrestUtenza();
      this.utenzeIstat.forEach(element => {
        if (element.codice == this.utenzaIstat) {
          ute.idUtenza = element.id;
          ute.codUtenza = element.codice;
          ute.descUtenza = element.descrizione;
        }
      })
      ute.dal = this.dataPrest;
      ute.dataMin = ute.dal;
      ute.cancellabile = true;
      newPrest.utenzeMinConf = [];
      newPrest.utenzeMinConf.push(ute);
      newPrest.utenzeMinConf.sort((a, b) =>
        this.sortUtenza(a, b)
      )
      this.prest2.prestIstat.push(newPrest);
      this.prest2.prestIstat.sort((a, b) =>
        this.sortIstat(a, b)
      )
    }
    this.utenzaIstat = "0";
    this.dataPrest = null;
  }

  sortIstat(a: Prest2PrestIstat, b: Prest2PrestIstat) {
    if (a.codPrestIstat > b.codPrestIstat) {
      return 1;
    } else if (a.codPrestIstat < b.codPrestIstat) {
      return -1;
    }
  }


  disableIstat() {
    let trovato: boolean = false;
    this.prest2.prestIstat.forEach((p) => {
      p.utenzeMinConf.forEach((u) => {
        if (u.al == null && u.dataCancellazione == null) {
          trovato = true;
        }
      })
    })
    if (trovato) {
      return true;
    }
    return false;
  }

  disableButtonIstat() {
    let dIU = this.disableUtenzaIstat()
    return this.utenzaIstat == '0' || dIU;
  }

  disableUteIstat() {
    let istat = this.prestazioneIstat;
    let esiste = false;
    if (istat == 0) {
      return true;
    }
    let trovato = true;
    if (istat != 0) {
      this.prest2.prestIstat.forEach(p => {
        p.utenzeMinConf.forEach(u => {
          if (u.al != null && u.dataCancellazione == null) {
            trovato = false;
            esiste = true;
          }
        })
      })
    }
    if (esiste) {
      return trovato;
    } else {
      return false;
    }

  }

  disableUtenzaIstat() {
    let istat = this.prestazioneIstat;
    let trovato: boolean = false;
    if (this.utenzaIstat == 'U21') {
      if (istat != 0) {
        this.prest2.prestIstat.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al == null) {
              trovato = true;
            } else {
              p.utenzeMinConf.forEach(u => {
                if (u.al == null && u.dataCancellazione == null) {
                  trovato = true;
                }
              })
            }
          }
        })
      }
    } else {
      if (istat != 0) {
        this.prest2.prestIstat.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al == null) {
              trovato = true;
            } else {
              p.utenzeMinConf.forEach(u => {
                if (u.codUtenza == this.utenzaIstat && u.al == null && u.dataCancellazione == null) {
                  trovato = true;
                }
              })
            }
          }
        })
      }
    }
    if (trovato) {
      return true;
    }
    return false;
  }

  disabilitaAlIstat(p: PrestUtenza, idPrestIstat: number) {
    if (p.al != null) {
      let valido: boolean = false;
      let dataMax: Date;
      this.prest2.prestIstat.forEach((t) => {
        if (t.idPrestIstat != idPrestIstat) {
          t.utenzeMinConf.forEach((e) => {
            if (e.al == null && e.dataCancellazione == null) {
              valido = true;
            } else if (e.al != null && e.dataCancellazione == null) {
              this.prest2.prestIstat.forEach((t) => {
                if (t.idPrestIstat == idPrestIstat) {
                  dataMax = t.utenzeMinConf.find((u) => u.al != null).al;
                  t.utenzeMinConf.forEach((e) => {
                    if (e.al != null && dataMax < e.al) {
                      dataMax = e.al;
                    }
                  })
                }
              })
              if (e.dal > dataMax) {
                valido = true;
              }
            }
          })
        }
      });
      if (valido) {
        return true;
      } else {
        let trovato: boolean = false;
        this.prest2.prestIstat.forEach((t) => {
          if (t.idPrestIstat == idPrestIstat) {
            t.utenzeMinConf.forEach((e) => {
              if (e.codUtenza == 'U21' && e.al == null && e.dataCancellazione == null) {
                trovato = true;
              } else if (p.idUtenza == e.idUtenza && e.dataCancellazione == null) {
                if (e.al == null) {
                  trovato = true;
                }
              }
            })
          }
        })
        if (trovato) {
          return true;
        } else {
          let pm: PrestUtenza = new PrestUtenza();
          let prest: Prest2PrestIstat = new Prest2PrestIstat();
          prest = this.prest2.prestIstat.length > 0 ? this.prest2.prestIstat.find((e) => e.idPrestIstat == idPrestIstat) : null;
          if (prest != null) {
            pm = prest.utenzeMinConf.length > 0 ? prest.utenzeMinConf.find((e) => e.idUtenza == p.idUtenza && e.dataCancellazione == null) : null;
            this.prest2.prestIstat.forEach((t) => {
              if (t.idPrestIstat == idPrestIstat) {
                t.utenzeMinConf.forEach((e) => {
                  if (p.idUtenza == e.idUtenza && e.dataCancellazione == null) {
                    if (pm != null && pm.dal < e.dal) {
                      pm = e;
                    }
                  }
                })
              }
            })
            if (p.dal < pm.dal) {
              return true;
            }
          } else {
            return false;
          }
        }
        return false;
      }
    } else if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.prest2.prestIstat.forEach((t) => {
        if (t.idPrestIstat == idPrestIstat) {
          t.utenzeMinConf.forEach((e) => {
            if (e.codUtenza == 'U21' && e.al == null && e.dataCancellazione == null) {
              trovato = true;
            } else if (p.idUtenza == e.idUtenza && e.dataCancellazione == null) {
              if (e.al == null) {
                trovato = true;
              }
            }
          })
        }
      })
      if (trovato) {
        return true;
      } else {
        let pm: PrestUtenza = new PrestUtenza();
        let prest: Prest2PrestIstat = new Prest2PrestIstat();
        prest = this.prest2.prestIstat.length > 0 ? this.prest2.prestIstat.find((e) => e.idPrestIstat == idPrestIstat) : null;
        if (prest != null) {
          pm = prest.utenzeMinConf.length > 0 ? prest.utenzeMinConf.find((e) => e.idUtenza == p.idUtenza && e.dataCancellazione == null) : null;
          this.prest2.prestIstat.forEach((t) => {
            if (t.idPrestIstat == idPrestIstat) {
              t.utenzeMinConf.forEach((e) => {
                if (p.idUtenza == e.idUtenza && e.dataCancellazione == null) {
                  if (pm != null && pm.dal < e.dal) {
                    pm = e;
                  }
                }
              })
            }
          })
          if (p.dal < pm.dal) {
            return true;
          }
        } else {
          return false;
        }
      }
      return false;
    }

  }

  limitIstat() {
    let istat = this.prestazioneIstat;
    let trovato: Date;
    if (this.utenzaIstat == 'U21') {
      if (istat != 0) {
        this.prest2.prestIstat.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al != null) {
              trovato = p.utenzeMinConf[0].al;
            } else {
              let c: number = 0;
              let t = p.utenzeMinConf[0].al;
              p.utenzeMinConf.forEach(u => {
                if (u.al != null && u.dataCancellazione == null) {
                  c++;
                }
                if (u.al > t && u.dataCancellazione == null) {
                  t = u.al;
                }
              })
              if (p.utenzeMinConf.length == c) {
                trovato = t;
              }
            }
          }
        })
      }
    } else {
      if (istat != 0) {
        this.prest2.prestIstat.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al != null) {
              trovato = p.utenzeMinConf[0].al;
            } else {
              p.utenzeMinConf.forEach(u => {
                if (u.codUtenza == this.utenzaIstat && u.al != null && u.dataCancellazione == null) {
                  if (trovato < u.al) {
                    trovato = u.al;
                  }

                }
              })
            }
          }
        })
      }
    }
    if (trovato != null) {

      let dataMin = new Date(trovato.getFullYear(), trovato.getMonth(), trovato.getDate() + 1);

      if (dataMin.getTime() > this.prest2.dal.getTime()) {
        return dataMin;
      } else {
        return this.prest2.dal;
      }

    } else {
      let t: Date = this.prest2.prestIstat.length > 0 ? this.prest2.prestIstat[0].utenzeMinConf.length > 0 ? this.prest2.prestIstat[0].utenzeMinConf[0].al : null : null;
      this.prest2.prestIstat.forEach(p => {
        p.utenzeMinConf.forEach(u => {
          if (t == null && u.dataCancellazione == null) {
            t = u.al;
          }
          if (u.al > t && u.dataCancellazione == null) {
            t = u.al;
          }
        })
      }
      )
      if (t != undefined) {
        let dataMin = new Date(t.getFullYear(), t.getMonth(), t.getDate() + 1);
        if (dataMin.getTime() > this.prest2.dal.getTime()) {
          return dataMin;
        } else {
          return this.prest2.dal;
        }

      } else {
        return this.prest2.dal;
      }

    }

  }

  limitValorizzatoIstat(u: PrestUtenza) {
    let ute: PrestUtenza;
    this.utenzeAssegnati.forEach(e => {
      if (e == u && e.dataCancellazione == null) {
        ute = e;
      }
    })
    if (ute != null) {
      return ute.dataMin;
    }

  }

  eliminaIstat(utenza: PrestUtenza, prestI: Prest2PrestIstat) {
    let messaggio = this.messaggio.replace("ELIMINATA", 'la prestazione istat: ' + prestI.codPrestIstat + ' con relativa utenza: ' + utenza.codUtenza).replace("PADRE", 'prestazione: ' + this.prest2.codPrest2);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        let prest: Prest2PrestIstat = this.prest2.prestIstat.find((e) => prestI.idPrestIstat == e.idPrestIstat);
        prest.utenzeMinConf = prest.utenzeMinConf.filter((element) => {
          return element.idUtenza != utenza.idUtenza;
        })
        prest.utenzeMinConf.sort((a, b) =>
          this.sortUtenza(a, b)
        )
        if (prest.utenzeMinConf.find((e) => e.dataCancellazione == null) != null) {
          this.prest2.prestIstat.filter((element) => {
            return element != prest;
          })
          this.prest2.prestIstat.sort((a, b) =>
            this.sortIstat(a, b)
          )
        }
      }
    })

  }

  optionNomenclatoreSelected(selectedOption: any) {
    const prest = selectedOption.value.split(' - ')[0];
    this.nomenclatoreSelezionata.codVoce = prest;
  }

  filterValuesNomenclatore(search: string) {
    if (search == "") {
      this.nomenclatoreSelezionata = new Nomenclatore();
    }
    if (search && this.nomenclatoreList && this.nomenclatoreList.length > 0) {
      return this.nomenclatoreList.filter(
        n =>
          n.codVoce.toUpperCase().trim().includes(search.toUpperCase().trim()) || n.descVoce.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.nomenclatoreList;
    }
  }


  disableNomenclatore() {
    if (this.nomenclatoreSelezionata == null || this.nomenclatoreSelezionata.codVoce == null) {
      return true;
    }
    let trovato: boolean = false;
    this.prest2.nomenclatore.forEach((e) => {
      if (e.al == null && e.dataCancellazione == null) {
        trovato = true;
      }
    })
    if (trovato) {
      return true;
    }
    return false;
  }

  limitNomenclatore() {
    let p: Nomenclatore = new Nomenclatore();
    p = this.prest2.nomenclatore.length > 0 ? this.prest2.nomenclatore[0] : null;
    let trovato: boolean = false;

    this.prest2.nomenclatore.forEach((e) => {
      if (e.al == null && e.dataCancellazione == null) {
        trovato = true;
      }
      if (p != null && p.al < e.al && e.dataCancellazione == null) {
        p = e;
      }
    })
    if (!trovato && p != null && p.al != undefined) {

      let dataMin = new Date(p.al.getFullYear(), p.al.getMonth(), p.al.getDate() + 1);
      if (dataMin.getTime() > this.prest2.dal.getTime()) {
        return dataMin;
      } else {
        return this.prest2.dal;
      }

    } else {
      return this.prest2.dal;
    }

  }

  aggiungiNomenclatore() {
    let nome: Nomenclatore = new Nomenclatore();
    this.nomenclatoreList.forEach(element => {
      if (element.codVoce == this.nomenclatoreSelezionata.codVoce) {
        nome.idNomenclatore = element.idNomenclatore;
        nome.codClassificazionePresidio = element.codClassificazionePresidio;
        nome.codFunzionePresidio = element.codFunzionePresidio;
        nome.codMacroArea = element.codMacroArea;
        nome.codNomenclatore = element.codNomenclatore;
        nome.codPresidio = element.codPresidio;
        nome.codSottoArea = element.codSottoArea;
        nome.codSottoAreaDet = element.codSottoAreaDet;
        nome.codSottoVoce = element.codSottoVoce;
        nome.codTipoResidenza = element.codTipoResidenza;
        nome.codVoce = element.codVoce;
        nome.descClassificazionePresidio = element.descClassificazionePresidio;
        nome.descFunzionePresidio = element.descFunzionePresidio;
        nome.descMacroArea = element.descMacroArea;
        nome.descPresidio = element.descPresidio;
        nome.descSottoArea = element.descSottoArea;
        nome.descSottoAreaDet = element.descSottoAreaDet;
        nome.descSottoVoce = element.descSottoVoce;
        nome.descTipoResidenza = element.descTipoResidenza;
        nome.descVoce = element.descVoce;
      }
    })
    nome.dal = this.dataNomenclatore;
    nome.cancellabile = true;
    this.prest2.nomenclatore.push(nome)
    this.prest2.nomenclatore.sort((a, b) =>
      this.sortNomenclatore(a, b)
    )
    this.nomenclatoreForm.reset();
    this.nomenclatoreSelezionata = new Nomenclatore();
    this.dataNomenclatore = null;
  }

  sortNomenclatore(a: Nomenclatore, b: Nomenclatore) {
    if (a.codVoce > b.codVoce) {
      return 1;
    } else if (a.codVoce < b.codVoce) {
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

  limitValorizzatoNomenclatore(n: Nomenclatore) {
    let p: Nomenclatore;
    this.prest2.nomenclatore.forEach((e) => {
      if (n == e && e.dataCancellazione == null) {
        p = e;
      }
    })
    if (p != null) {
      return p.dal;
    }
  }

  disabilitaAlNomenclatore(p: Nomenclatore) {
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.prest2.nomenclatore.forEach((e) => {
        if (e.al == null && e.dataCancellazione == null) {
          trovato = true;
        }
      })
      if (trovato) {
        return true;
      } else {
        let pm: Nomenclatore = new Nomenclatore();
        pm = this.prest2.nomenclatore.length > 0 ? this.prest2.nomenclatore.find((e) => e.dataCancellazione == null) : null;
        let trovato: boolean = false;
        this.prest2.nomenclatore.forEach((e) => {
          if (e.al == null && e.dataCancellazione == null) {
            trovato = true;
          }
          if (pm.dal < e.dal && e.dataCancellazione == null) {
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

  eliminaNomenclatore(n: Nomenclatore) {
    let messaggio = this.messaggio.replace("ELIMINATA", 'la voce del nomenclatore: ' + n.codVoce).replace("PADRE", 'prestazione: ' + this.prest2.codPrest2);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.prest2.nomenclatore = this.prest2.nomenclatore.filter((element) => {
          return element.idNomenclatore != n.idNomenclatore;
        })
        this.prest2.nomenclatore.sort((a, b) =>
          this.sortNomenclatore(a, b)
        )
      }
    })

  }

  conferma() {
    this.client.spinEmitter.emit(true);
    this.activeModal.close(this.prest2);
  }

  annulla() {
    this.prest2.al = this.prest2Initial.al;
    this.prest2.alRelazione = this.prest2Initial.alRelazione;
    this.prest2.codPrest2 = this.prest2Initial.codPrest2;
    this.prest2.codTipologia = this.prest2Initial.codTipologia;
    this.prest2.dal = this.prest2Initial.dal;
    this.prest2.dalRelazione = this.prest2Initial.dalRelazione;
    this.prest2.dataMin = this.prest2Initial.dataMin;
    this.prest2.descPrest2 = this.prest2Initial.descPrest2;
    this.prest2.idPrest2 = this.prest2Initial.idPrest2;
    this.prest2.idTipologia = this.prest2Initial.idTipologia;
    this.prest2.modificabile = this.prest2Initial.modificabile;
    this.prest2.nomenclatore = [];
    for (let p of this.prest2Initial.nomenclatore) {
      this.prest2.nomenclatore.push(p);
    }
    this.prest2.ordinamento = this.prest2Initial.ordinamento;
    this.prest2.prestIstat = [];
    for (let p of this.prest2Initial.prestIstat) {
      this.prest2.prestIstat.push(p);
    }
    this.prest2.utenzeConf = [];
    for (let p of this.prest2Initial.utenzeConf) {
      this.prest2.utenzeConf.push(p);
    }
    this.prest2.utilizzato = this.prest2Initial.utilizzato;
    this.activeModal.close(null);
  }
}
