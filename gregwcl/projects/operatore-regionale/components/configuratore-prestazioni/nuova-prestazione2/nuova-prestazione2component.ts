import { Component, Inject,  OnInit } from "@angular/core";
import { FormControl } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { ListeConfiguratore } from "@greg-app/app/dto/ListeConfiguratore";
import { Nomenclatore } from "@greg-app/app/dto/Nomenclatore";
import { Prest1Prest2 } from "@greg-app/app/dto/Prest1Prest2";
import { Prest2PrestIstat } from "@greg-app/app/dto/Prest2PrestIstat";
import { PrestUtenza } from "@greg-app/app/dto/PrestUtenza";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ignoreElements } from "rxjs/operators";
import { CancPopupComponent } from "../canc-popup/canc-popup.component";

@Component({
  selector: 'app-nuova-prestazione2',
  templateUrl: './nuova-prestazione2.component.html',
  styleUrls: ['./nuova-prestazione2.component.css']
})
export class NuovaPrestazione2Component implements OnInit {
attenzione: string = '';
  tip: string = null;
  ordinamento: string;
  codice: string;
  descrizione: string;
  nota: string;
  dal: Date;
  al: Date;
  dataPrest: Date;
  dataUtenza: Date;
  dataNomenclatore: Date;
  utenzeAssegnati: PrestUtenza[]=[];
  prestIstatAssegnate: Prest2PrestIstat[] = [];
  utenza: number = 0;
  prestazioneIstat: number = 0;
  utenzaIstat: string = '0';
  titolo:string; 
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
  dalPrest1: Date;
  alPrest1: Date;
  constructor(public activeModal: NgbActiveModal, private dialog: MatDialog, public client: GregBOClient ) {}

  ngOnInit() {
    this.nomenclatoreFiltered = this.nomenclatoreList;
        this.nomenclatoreForm.valueChanges.subscribe(val => {
          if (val == "") {
            this.dettaglioNomenclatore = null;
          }
          this.nomenclatoreFiltered = this.filterValuesNomenclatore(val);
        })
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
    this.utenzeAssegnati.push(ute)
    this.utenzeAssegnati.sort((a, b) =>
      this.sortUtenza(a, b)
    )
    this.utenzeIstat = [];
    this.client.getUtenzeIstatConf(this.utenzeAssegnati).subscribe(element =>{
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
    this.utenzeAssegnati.forEach(e => {
      if (e.idUtenza == this.utenza) {
        if (e.al == null) {
          trovato = true;
        }

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
        pm = this.utenzeAssegnati.length > 0 ? this.utenzeAssegnati[0] : null;
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
      return false;
    }

  }

  limitUtenza() {
    let ute: PrestUtenza;
    ute = this.utenzeAssegnati.length > 0 ? this.utenzeAssegnati[0] : null;
      this.utenzeAssegnati.forEach(e => {
        if (e.idUtenza == this.utenza) {
          if (ute.al < e.al) {
            ute = e;
          }
        }
      })
    if (ute != null && ute.al!=undefined) {
        let dataMin = new Date(ute.al.getFullYear(), ute.al.getMonth(), ute.al.getDate()+1);
        if (dataMin.getTime() > this.dal.getTime()) {
          return dataMin;
        } else {
          return this.dal;
        }
  
      } else {
        return this.dal;
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

  eliminaUtenza(utenza: PrestUtenza) {
    let messaggio = this.messaggio.replace("ELIMINATA", 'utenza: ' +utenza.codUtenza).replace("PADRE", 'prestazione: ' +this.codice);
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
        this.client.getUtenzeIstatTransConf(utenza.codUtenza).subscribe((element: ListeConfiguratore) => {
          this.prestIstatAssegnate.forEach(i => {
            let ute = i.utenzeMinConf.find((u) => u.idUtenza == element.id);
            if (ute != null && ute != undefined && ute.codUtenza != 'U21') {
              ute.al = new Date();
            }

          });
          this.utenzeAssegnati.sort((a, b) =>
            this.sortUtenza(a, b)
          )
          this.client.spinEmitter.emit(false);

        });
      }
    })

  }

  aggiungiIstat() {
    let istat = this.prestazioneIstat;
    let prest: Prest2PrestIstat = new Prest2PrestIstat();
    prest = this.prestIstatAssegnate.find((e) => istat == e.idPrestIstat);
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
      ute.dataMin = new Date(ute.dal.getFullYear(), ute.dal.getMonth(), ute.dal.getDate()+1);
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
      this.prestIstatAssegnate.push(newPrest);
      this.prestIstatAssegnate.sort((a, b) =>
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
    this.prestIstatAssegnate.forEach((p) => {
      p.utenzeMinConf.forEach((u) => {
        if (u.al == null) {
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
    // let dI = this.disableIstat();
    let dIU = this.disableUtenzaIstat()
    return  dIU || this.utenzaIstat=='0';
  }

  disableUteIstat() {
    
    let istat = this.prestazioneIstat;
    let esiste = false;
    if(istat==0){
      return true;
    }
    let trovato = true;
    if (istat != 0) {
      this.prestIstatAssegnate.forEach(p => {
        p.utenzeMinConf.forEach(u => {
          if (u.al != null) {
            trovato = false;
            esiste = true;
          }
        })
      })
    }
    if(esiste){
       return trovato;
    }else{
      return false;
    }
   
  }

  disableUtenzaIstat() {
    let istat = this.prestazioneIstat;
    let trovato: boolean = false;
    if (this.utenzaIstat == 'U21') {
      if (istat != 0) {
        this.prestIstatAssegnate.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al == null) {
              trovato = true;
            } else {
              p.utenzeMinConf.forEach(u => {
                if (u.al == null) {
                  trovato = true;
                }
              })
            }
          }
        })
      }
    } else {
      if (istat != 0) {
        this.prestIstatAssegnate.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al == null) {
              trovato = true;
            } else {
              p.utenzeMinConf.forEach(u => {
                if (u.codUtenza == this.utenzaIstat && u.al == null) {
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
      this.prestIstatAssegnate.forEach((t) => {
        if (t.idPrestIstat != idPrestIstat) {
          t.utenzeMinConf.forEach((e) => {
            if (e.al == null && e.dataCancellazione == null) {
              valido = true;
            }else if (e.al != null && e.dataCancellazione == null) {
              this,this.prestIstatAssegnate.forEach((t) => {
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
        this.prestIstatAssegnate.forEach((t) => {
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
          prest = this.prestIstatAssegnate.length > 0 ? this.prestIstatAssegnate.find((e) => e.idPrestIstat == idPrestIstat) : null;
          if (prest != null) {
            pm = prest.utenzeMinConf.length > 0 ? prest.utenzeMinConf.find((e) => e.idUtenza == p.idUtenza && e.dataCancellazione == null) : null;
            this.prestIstatAssegnate.forEach((t) => {
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
      this.prestIstatAssegnate.forEach((t) => {
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
        prest = this.prestIstatAssegnate.length > 0 ? this.prestIstatAssegnate.find((e) => e.idPrestIstat == idPrestIstat) : null;
        if (prest != null) {
          pm = prest.utenzeMinConf.length > 0 ? prest.utenzeMinConf.find((e) => e.idUtenza == p.idUtenza && e.dataCancellazione == null) : null;
          this.prestIstatAssegnate.forEach((t) => {
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
        this.prestIstatAssegnate.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al != null) {
              trovato = p.utenzeMinConf[0].al;
            } else {
              let c: number = 0;
              let t = p.utenzeMinConf[0].al;
              p.utenzeMinConf.forEach(u => {
                if (u.al != null) {
                  c++;
                }
                if (u.al > t) {
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
        this.prestIstatAssegnate.forEach(p => {
          if (istat == p.idPrestIstat) {
            if (p.utenzeMinConf.length == 1 && p.utenzeMinConf[0].codUtenza == 'U21' && p.utenzeMinConf[0].al != null) {
              trovato = p.utenzeMinConf[0].al;
            } else {
              p.utenzeMinConf.forEach(u => {
                if (u.codUtenza == this.utenzaIstat && u.al != null) {
                  trovato = u.al;
                }
              })
            }
          }
        })
      }
    }
    if (trovato != null) {
      let dataMin = new Date(trovato.getFullYear(), trovato.getMonth(), trovato.getDate()+1);
      if (dataMin.getTime() > this.dal.getTime()) {
        return dataMin;
      } else {
        return this.dal;
      }
    } else {
      let t: Date =  this.prestIstatAssegnate.length>0 ? this.prestIstatAssegnate[0].utenzeMinConf.length>0 ? this.prestIstatAssegnate[0].utenzeMinConf[0].al : null : null;
      this.prestIstatAssegnate.forEach(p => {
        p.utenzeMinConf.forEach(u => {
          if(t==null){
            t = u.al;
          }
          if (u.al > t) {
            t = u.al;
          }
        })
      }
      )
      if(t!=null){

        let dataMin = new Date(t.getFullYear(), t.getMonth(), t.getDate()+1);
        if (dataMin.getTime() > this.dal.getTime()) {
          return dataMin;
        } else {
          return this.dal;
        }
  
      } else {
        return this.dal;
      }
    }
  }

  limitValorizzatoIstat(u: PrestUtenza) {
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

  eliminaIstat(utenza: PrestUtenza, prest: Prest2PrestIstat) {
    let messaggio = this.messaggio.replace("ELIMINATA", 'la prestazione istat: '+ prest.codPrestIstat + ' con relativa utenza: ' + utenza.codUtenza).replace("PADRE", 'prestazione: ' +this.codice);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        let prest: Prest2PrestIstat = this.prestIstatAssegnate.find((e) => prest.idPrestIstat == e.idPrestIstat);
        prest.utenzeMinConf = prest.utenzeMinConf.filter((element) => {
          return element != utenza;
        })
        prest.utenzeMinConf.sort((a, b) =>
          this.sortUtenza(a, b)
        )
        if (prest.utenzeMinConf.length == 0) {
          this.prestIstatAssegnate.filter((element) => {
            return element != prest;
          })
          this.prestIstatAssegnate.sort((a, b) =>
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
        n.codNomenclatore.toUpperCase().trim().includes(search.toUpperCase().trim()))
    } else {
      return this.nomenclatoreList;
    }
  }


  disableNomenclatore(){
    if (this.nomenclatoreSelezionata == null || this.nomenclatoreSelezionata.codVoce == null) {
      return true;
    }
    let trovato: boolean = false;
    this.nomenclatoreAssegnato.forEach((e) => {
      if (e.al == null) {
        trovato = true;
      }
    })
    if (trovato) {
      return true;
    }
    return false;
  }

  limitNomenclatore(){
    let p: Nomenclatore = new Nomenclatore();
    p = this.nomenclatoreAssegnato.length > 0 ? this.nomenclatoreAssegnato[0] : null;
    let trovato: boolean = false;
    
      this.nomenclatoreAssegnato.forEach((e) => {
        if (e.al == null) {
          trovato = true;
        }
        if (p != null && p.al < e.al) {
          p = e;
        }
      })
      if (!trovato && p != null && p.al!=undefined) {
        let dataMin = new Date(p.al.getFullYear(), p.al.getMonth(), p.al.getDate()+1);
        if (dataMin.getTime() > this.dal.getTime()) {
          return dataMin;
        } else {
          return this.dal;
        }
  
      } else {
        return this.dal;
      }

  }

  aggiungiNomenclatore(){
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
    this.nomenclatoreAssegnato.push(nome)
    this.nomenclatoreAssegnato.sort((a, b) =>
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

  limitValorizzatoNomenclatore(n: Nomenclatore){
  let p: Nomenclatore;
    this.nomenclatoreAssegnato.forEach((e) => {
      if (n == e) {
        p = e;
      }
    })
    if (p != null) {
      return p.dal;
    }
  }

  disabilitaAlNomenclatore(p: Nomenclatore){
    if (p.al == null) {
      return false;
    } else {
      let trovato: boolean = false;
      this.nomenclatoreAssegnato.forEach((e) => {
        if (e.al == null) {
          trovato = true;
        }
      })
      if (trovato) {
        return true;
      } else {
        let pm: Nomenclatore = new Nomenclatore();
        pm = this.nomenclatoreAssegnato.length > 0 ? this.nomenclatoreAssegnato[0] : null;
        let trovato: boolean = false;
        this.nomenclatoreAssegnato.forEach((e) => {
          if (e.al == null) {
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

  eliminaNomenclatore(n: Nomenclatore){
    let messaggio = this.messaggio.replace("ELIMINATA", 'la voce del nomenclatore: ' + n.codVoce).replace("PADRE", 'prestazione: ' +this.codice);
    const dialogRef = this.dialog.open(CancPopupComponent, {
      width: '650px',
      disableClose: true,
      autoFocus: true,
      data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
    });
    dialogRef.afterClosed().subscribe(r => {
      if (r) {
        this.nomenclatoreAssegnato = this.nomenclatoreAssegnato.filter((element) => {
          return element.idNomenclatore != n.idNomenclatore;
        })
        this.nomenclatoreAssegnato.sort((a, b) =>
          this.sortNomenclatore(a, b)
        )
      }
    })
  }

  conferma(){
    this.client.spinEmitter.emit(true);
    let prest2: Prest1Prest2 = new Prest1Prest2();
    prest2.codPrest2 = this.codice;
    prest2.descPrest2 = this.descrizione;
    let tipo: ListeConfiguratore = this.tipologie.find((e)=> e.codice == this.tip);
    prest2.idTipologia = tipo.id;
    prest2.codTipologia = tipo.codice;
    prest2.nomenclatore = this.nomenclatoreAssegnato;
    prest2.prestIstat = this.prestIstatAssegnate;
    prest2.utenzeConf = this.utenzeAssegnati;
    prest2.utilizzato = false;
    prest2.ordinamento = parseInt(this.ordinamento);
    prest2.al = this.al;
    prest2.dal = this.dal;
    prest2.dalRelazione = this.dal;
    prest2.nota = this.nota;
		this.activeModal.close(prest2);
	}

  disabilitaSalva(){
    if(this.codice==null || this.codice=='' || this.descrizione==null || this.descrizione=='' || this.tip=='0' || this.ordinamento==null || this.ordinamento=='' || this.codice.trim().length==0 || this.descrizione.trim().length==0){
      return true;
    }
    return false;
  }

  annulla(){
		this.activeModal.close(null);
	}
}
