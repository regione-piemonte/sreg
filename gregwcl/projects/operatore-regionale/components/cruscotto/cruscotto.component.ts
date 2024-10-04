import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { EnteGregWithComuniAss } from '@greg-app/app/dto/EnteGregWithComuniAss';
import { GenericResponseWarnCheckErrGreg } from '@greg-app/app/dto/GenericResponseWarnCheckErrGreg';
import { InfoModello } from '@greg-app/app/dto/InfoModello';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { ModelTabTrancheCruscotto } from '@greg-app/app/dto/ModelTabTrancheCruscotto';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { RicercaGregCruscotto } from '@greg-app/app/dto/RicercaGregCruscotto';
import { RicercaGregOutput } from '@greg-app/app/dto/RicercaGregOutput';
import { StatiCruscotto } from '@greg-app/app/dto/StatiCruscotto';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ERRORS, RENDICONTAZIONE_STATUS, SECTION, TRANCHE } from '@greg-app/constants/greg-constants';
import { forkJoin } from 'rxjs';
import { MessaggioPopupCruscottoComponent } from './messaggio-popup-cruscotto/messaggio-popup-cruscotto.component';

@Component({
  selector: 'app-cruscotto',
  templateUrl: './cruscotto.component.html',
  styleUrls: ['./cruscotto.component.css']
})
export class CruscottoComponent implements OnInit, AfterViewInit {

  searchForm: FormGroup;
  denomEnteFiltered: EnteGregWithComuniAss[];
  dataListaRichieste: MatTableDataSource<RicercaGregOutput[]>;
  ricerca: RicercaGregOutput[];
  isDisabled: boolean = true;
  annoEsercizio: number;
  displayedColumns: string[] = ['codiceRegionale', 'denominazione', 'vuoto1'];
  firstRow: string[] = ['codiceRegionaleFirst', 'denominazioneFirst', 'vuoto1First', 'primaTranche', 'vuoto2First', 'secondaTranche', 'vuoto3First', 'FNPS1First', 'FNPS2First', 'vuoto4First', 'ALZERO1First'];
  nTabPrimaTranche: number = 0;
  nTabSecondaTranche: number = 0;
  intestazioneColumns: string[] = ['Codice Regionale', 'Denominazione', ''];
  isChecked: boolean = false;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  datiEnte: any;
  tooltipRendicontazione: string = '';
  modelli: ModelTabTrancheCruscotto[];
  stati: StatiCruscotto[];
  annoSelezionato: number;
  warnings: string[];
  errors: string[];
  titolo: string;
  check: string[];
  messaggio: string;
  denomin: string;
  codObbl: string;
  val: boolean;


  constructor(private fb: FormBuilder, private router: Router, private route: ActivatedRoute,
    public client: GregBOClient, private dialog: MatDialog) { }



  ngOnInit() {
    this.client.spinEmitter.emit(true);
    this.searchForm = this.fb.group({
      codiceRegionale: [],
      denominazioneEnte: ['', [Validators.maxLength(300)]],
      statoRendicontazione: [],
      tipoEnte: [],
      comune: [],
      annoEsercizio: [Validators.required]
    });
    if (this.client.listaStatiSalvatoCruscotto.length == 0) {
      let r: RicercaGregCruscotto;
      this.client.getMaxAnnoRendicontazione().subscribe(element => {
        this.annoEsercizio = element;
        r = new RicercaGregCruscotto(null, null, null, null, null, this.annoEsercizio, this.client.listaenti);

        forkJoin({
          listaStati: this.client.getListaStatoRendicontazione(),
          listaComuni: this.client.getListaComuni(null),
          listaTipoEnti: this.client.getListaTipoEnte(),
          listaDenominazioniEnti: this.client.getListaDenominazioni(),
          anniEsercizio: this.client.getAnniEsercizio(),
          msgApplicativo: this.client.getMsgApplicativo(ERRORS.ERROR_ANNO_CONTABILE),
          modelli: this.client.getModelliCruscotto(),
          stati: this.client.getStati(r)
        })
          .subscribe(({ listaStati,
            listaComuni,
            listaTipoEnti, listaDenominazioniEnti,
            msgApplicativo, anniEsercizio, modelli, stati }) => {
            this.modelli = modelli;
            this.client.modelliSalvatoCruscotto = modelli;
            this.modelli.forEach(modello => {

              if (modello.codTranche == TRANCHE.I_TRANCHE) {
                this.displayedColumns.push(modello.codTab);
                this.intestazioneColumns.push(modello.desTab);
                this.nTabPrimaTranche++;
              }
            });
            this.stati = stati;
            this.client.statiRendicontazioneCruscotto = stati;
            this.displayedColumns.push('vuoto2');
            this.intestazioneColumns.push('');
            this.modelli.forEach(modello => {
              if (modello.codTranche == TRANCHE.II_TRANCHE) {
                this.displayedColumns.push(modello.codTab);
                this.intestazioneColumns.push(modello.desTab);
                this.nTabSecondaTranche++;
              }
            });
            this.displayedColumns.push('vuoto3');
            this.intestazioneColumns.push('');
            this.displayedColumns.push('FNPS1');
            this.displayedColumns.push('FNPS2');
            this.intestazioneColumns.push('FNPS ');
            this.intestazioneColumns.push('FNPS ');
            this.searchForm.controls.annoEsercizio.patchValue(this.annoEsercizio);

            // MODELLO AL ZERO

            this.displayedColumns.push('vuoto4');
            this.intestazioneColumns.push('');
            this.displayedColumns.push('ALZERO1');
            this.intestazioneColumns.push('Al-Zero ');


            this.client.listaStatiSalvatoCruscotto = listaStati;
            this.client.listaComuniSalvatoCruscotto = listaComuni;
            this.client.listaComuniinitialCruscotto = listaComuni;
            this.client.listaTipoEntiSalvatoCruscotto = listaTipoEnti;
            this.client.listaDenominazioniEntiSalvatoCruscotto = listaDenominazioniEnti;
            this.client.anniEsercizioSalvatoCruscotto = anniEsercizio;
            this.client.tooltipRendicontazione = msgApplicativo.testoMsgApplicativo;

            //  this.client.spinEmitter.emit(false);
            this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput[]>();
            this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
            this.dataListaRichieste.sort = this.sort;


            this.avviaRicerca();
          },
            err => {
              this.client.spinEmitter.emit(false)
            }
          );
      })
    } else {
      this.client.listaComuniSalvatoCruscotto = this.client.listaComuniinitialCruscotto;
      if (this.client.ricercaEnteCruscotto.length > 0) {
        this.modelli = this.client.modelliSalvatoCruscotto;
        this.stati = this.client.statiRendicontazioneCruscotto;
        this.annoSelezionato = this.client.filtroEnteCruscotto.annoEsercizio;
        this.modelli.forEach(modello => {

          if (modello.codTranche == TRANCHE.I_TRANCHE) {
            this.displayedColumns.push(modello.codTab);
            this.intestazioneColumns.push(modello.desTab);
            this.nTabPrimaTranche++;
          }
        });
        this.displayedColumns.push('vuoto2');
        this.intestazioneColumns.push('');
        this.modelli.forEach(modello => {
          if (modello.codTranche == TRANCHE.II_TRANCHE) {
            this.displayedColumns.push(modello.codTab);
            this.intestazioneColumns.push(modello.desTab);
            this.nTabSecondaTranche++;
          }
        });
        this.displayedColumns.push('vuoto3');
        this.intestazioneColumns.push('');
        this.displayedColumns.push('FNPS1');
        this.displayedColumns.push('FNPS2');
        this.intestazioneColumns.push('FNPS ' + this.annoSelezionato);
        this.intestazioneColumns.push('FNPS ' + (this.annoSelezionato - 1));
        // MODELLO AL ZERO
        this.displayedColumns.push('vuoto4');
        this.intestazioneColumns.push('');
        this.displayedColumns.push('ALZERO1');
        this.intestazioneColumns.push('Al-Zero ' + this.annoSelezionato);

        this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput[]>();
        this.dataListaRichieste.data = this.client.ricercaEnteCruscotto as any;
        this.dataListaRichieste.paginator = this.paginator;
        this.searchForm = this.fb.group({
          codiceRegionale: this.client.filtroEnteCruscotto.codiceRegionale,
          denominazioneEnte: this.client.filtroEnteCruscotto.denominazioneEnte,
          statoRendicontazione: this.client.filtroEnteCruscotto.statoRendicontazione,
          tipoEnte: this.client.filtroEnteCruscotto.tipoEnte,
          comune: this.client.filtroEnteCruscotto.comune,
          annoEsercizio: this.client.filtroEnteCruscotto.annoEsercizio
        });
        this.client.spinEmitter.emit(false);
      }
      else {
        this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput[]>();
        this.modelli = this.client.modelliSalvatoCruscotto;
        this.stati = this.client.statiRendicontazioneCruscotto;
        this.annoSelezionato = this.client.filtroEnteCruscotto.annoEsercizio;
        this.modelli.forEach(modello => {

          if (modello.codTranche == TRANCHE.I_TRANCHE) {
            this.displayedColumns.push(modello.codTab);
            this.intestazioneColumns.push(modello.desTab);
            this.nTabPrimaTranche++;
          }
        });
        this.displayedColumns.push('vuoto2');
        this.intestazioneColumns.push('');
        this.modelli.forEach(modello => {
          if (modello.codTranche == TRANCHE.II_TRANCHE) {
            this.displayedColumns.push(modello.codTab);
            this.intestazioneColumns.push(modello.desTab);
            this.nTabSecondaTranche++;
          }
        });
        this.displayedColumns.push('vuoto3');
        this.intestazioneColumns.push('');
        this.displayedColumns.push('FNPS1');
        this.displayedColumns.push('FNPS2');
        this.intestazioneColumns.push('FNPS ' + this.annoSelezionato);
        this.intestazioneColumns.push('FNPS ' + (this.annoSelezionato - 1));
        // MODELLO AL ZERO
        this.displayedColumns.push('vuoto4');
        this.intestazioneColumns.push('');
        this.displayedColumns.push('ALZERO1');
        this.intestazioneColumns.push('Al-Zero ' + this.annoSelezionato);

        this.searchForm = this.fb.group({
          codiceRegionale: this.client.filtroEnteCruscotto.codiceRegionale,
          denominazioneEnte: this.client.filtroEnteCruscotto.denominazioneEnte,
          statoRendicontazione: this.client.filtroEnteCruscotto.statoRendicontazione,
          tipoEnte: this.client.filtroEnteCruscotto.tipoEnte,
          comune: this.client.filtroEnteCruscotto.comune,
          annoEsercizio: this.client.filtroEnteCruscotto.annoEsercizio
        });
        this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
        this.dataListaRichieste.sort = this.sort;

        this.avviaRicerca();
      }
    }
  }

  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    const codiceRegionale = this.searchForm.controls.codiceRegionale.value && this.searchForm.controls.codiceRegionale.value != "" ? this.searchForm.controls.codiceRegionale.value : null;
    const denominazioneEnte = this.searchForm.controls.denominazioneEnte.value && this.searchForm.controls.denominazioneEnte.value.toUpperCase();
    const statoRendicontazione = this.searchForm.controls.statoRendicontazione.value && this.searchForm.controls.statoRendicontazione.value != "" ? this.searchForm.controls.statoRendicontazione.value : null;
    const tipoEnte = this.searchForm.controls.tipoEnte.value && this.searchForm.controls.tipoEnte.value != "" ? this.searchForm.controls.tipoEnte.value : null;
    const comune = this.searchForm.controls.comune.value && this.searchForm.controls.comune.value != "" ? this.searchForm.controls.comune.value : null;
    const annoEsercizio = this.searchForm.controls.annoEsercizio.value && this.searchForm.controls.annoEsercizio.value != null ? this.searchForm.controls.annoEsercizio.value : null;
    const lista = this.client.listaenti;

    const ricerca = new RicercaGregCruscotto(codiceRegionale, denominazioneEnte, statoRendicontazione, tipoEnte, comune, annoEsercizio, lista);
    this.client.filtroEnteCruscotto = ricerca;
    this.client.getSchedeEntiGestoriCruscotto(ricerca).subscribe((response: RicercaGregOutput[]) => {
      this.client.getStati(ricerca).subscribe((element) => {
        this.client.spinEmitter.emit(false);
        this.annoSelezionato = ricerca.annoEsercizio;
        this.intestazioneColumns.pop();
        this.intestazioneColumns.pop();
        // if (this.annoSelezionato >= 2023) {
        this.intestazioneColumns.pop();
        this.intestazioneColumns.pop();
        // }
        this.intestazioneColumns.push('FNPS ' + this.annoSelezionato);
        this.intestazioneColumns.push('FNPS ' + (this.annoSelezionato - 1));
        // MODELLO AL ZERO
        this.intestazioneColumns.push('');
        this.intestazioneColumns.push('Al-Zero ' + this.annoSelezionato);
        this.dataListaRichieste.data = response as any;
        this.ricerca = response;
        this.client.ricercaEnteCruscotto = response as any;
        if (this.client.paginaSalvataCruscotto !== this.paginator.pageIndex) {
          setTimeout(() => {
            this.paginator.pageIndex = this.client.paginaSalvataCruscotto;
            this.dataListaRichieste.paginator = this.paginator;
            this.dataListaRichieste.sort = this.sort;
          }, 0);
        } else {
          this.dataListaRichieste.paginator = this.paginator;
          this.dataListaRichieste.sort = this.sort;
        }
        this.stati = element;
        this.client.statiRendicontazioneCruscotto = this.stati;
      },
        err => {
          this.client.spinEmitter.emit(false);
        });
    },
      err => {
        this.client.spinEmitter.emit(false);
      });
  }

  creaNuovoAnnoContabile() {
  }

  annullaRicerca() {
    this.searchForm.reset();
    this.searchForm.controls.annoEsercizio.patchValue(this.annoSelezionato);
    this.denomEnteFiltered = [];
    this.dataListaRichieste.data = [];
  }

  onSelectionComuneChanged(value: string) {
    //    this.searchForm.get('denominazioneEnte').reset();
    //    this.denomEnteFiltered = value == ''? this.listaDenomEnteWithComAss : this.listaDenomEnteWithComAss.filter(ente => ente.comuniAssociati.includes(parseFloat(value)));
  }

  onSelectionAnnoChanged(value: string) {
    if (value != '') {
      this.client.spinEmitter.emit(true);
      this.searchForm.get('comune').reset();
      this.client.getListaComuni(value + '-01-01').subscribe(listaComuni => {
        this.client.listaComuniSalvatoCruscotto = listaComuni;
        this.client.spinEmitter.emit(false);
      });
    }
    else {
      this.client.listaComuniSalvatoCruscotto = this.client.listaComuniinitial;
    }
  }


  routeToModello(rend: RendicontazioneEnteGreg, path: string) {
    let azioneAttiva: boolean = false;
    switch (path) {
      case 'modello-a':
        if (this.client.azioni.get('ModelloA')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-a1':
        if (this.client.azioni.get('ModelloA1')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-a2':
        if (this.client.azioni.get('ModelloA2')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-d':
        if (this.client.azioni.get('ModelloD')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'macroaggregati':
        if (this.client.azioni.get('ModelloMacroaggregati')[1] && rend.statoRendicontazione.codStatoRendicontazione != RENDICONTAZIONE_STATUS.DA_COMPILARE_I) {
          azioneAttiva = true;
        }
        break;
      case 'modello-b1':
        if (this.client.azioni.get('ModelloB1')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-b':
        if (this.client.azioni.get('ModelloB')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-c':
        if (this.client.azioni.get('ModelloC')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-e':
        if (this.client.azioni.get('ModelloE')[1]) {
          azioneAttiva = true;
        }
        break;
      case 'modello-f':
        if (this.client.azioni.get('ModelloF')[1]) {
          azioneAttiva = true;
        }
        break;
    }
    if (azioneAttiva) {
      this.client.paginaSalvataCruscotto = this.paginator.pageIndex;
      this.client.cruscotto = true;
      this.client.getDatiEnteRendicontazione(rend.idRendicontazioneEnte, rend.annoEsercizio).subscribe((response: DatiEnteGreg) => {
        this.datiEnte = response as any;
        const navigationExtras: NavigationExtras = {
          relativeTo: this.route,
          skipLocationChange: true,
          state: {
            rendicontazione: rend,
            datiEnte: this.datiEnte,
            model: path
          }
        };
        this.router.navigate(['../../responsabile-ente/dati-rendicontazione/' + path], navigationExtras);
      });
    }

  }

  routeToFnps(idRendicontazione: number) {
    if (this.client.azioni.get('ModelloFNPS')[1]) {
      this.client.paginaSalvataCruscotto = this.paginator.pageIndex;
      this.client.cruscotto = true;
      this.client.getInfoRendicontazioneOperatore(idRendicontazione).subscribe(rend => {
        this.client.getDatiEnteRendicontazione(rend.idRendicontazioneEnte, rend.annoEsercizio).subscribe((response: DatiEnteGreg) => {
          this.datiEnte = response as any;
          if (rend.statoRendicontazione.codStatoRendicontazione !== 'CON') {
            const navigationExtras: NavigationExtras = {
              relativeTo: this.route,
              skipLocationChange: true,
              state: {
                rendicontazione: rend,
                datiEnte: this.datiEnte,
                model: 'all-d'
              }
            };
            this.router.navigate(['../../responsabile-ente/all-d'], navigationExtras)
          } else {
            const navigationExtras: NavigationExtras = {
              relativeTo: this.route,
              skipLocationChange: true,
              state: {
                rendicontazione: rend,
                datiEnte: this.datiEnte,
                model: 'all-d-archivio'
              }
            };
            this.router.navigate(['../../responsabile-ente-archivio/all-d-archivio'], navigationExtras)
          }
        });
      })
    }
  }

  routeToAlZeero(idRendicontazione: number) {
    if (this.client.azioni.get('ModelloAlZero')[1]) {
      this.client.paginaSalvataCruscotto = this.paginator.pageIndex;
      this.client.cruscotto = true;
      this.client.getInfoRendicontazioneOperatore(idRendicontazione).subscribe(rend => {
        this.client.getDatiEnteRendicontazione(rend.idRendicontazioneEnte, rend.annoEsercizio).subscribe((response: DatiEnteGreg) => {
          this.datiEnte = response as any;
          if (rend.statoRendicontazione.codStatoRendicontazione !== 'CON') {
            const navigationExtras: NavigationExtras = {
              relativeTo: this.route,
              skipLocationChange: true,
              state: {
                rendicontazione: rend,
                datiEnte: this.datiEnte,
                model: 'al-zero'
              }
            };
            this.router.navigate(['../../responsabile-ente/al-zero'], navigationExtras)
          } else {
            const navigationExtras: NavigationExtras = {
              relativeTo: this.route,
              skipLocationChange: true,
              state: {
                rendicontazione: rend,
                datiEnte: this.datiEnte,
                model: 'al-zero-archivio'
              }
            };
            this.router.navigate(['../../responsabile-ente-archivio/al-zero-archivio'], navigationExtras)
          }
        });
      })
    }
  }

  controlloRendicontazione(codStatoRendicontazione: string) {
    if (codStatoRendicontazione == RENDICONTAZIONE_STATUS.ATTESA_RETTIFICA_I ||
      codStatoRendicontazione == RENDICONTAZIONE_STATUS.DA_COMPILARE_I ||
      codStatoRendicontazione == RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_I ||
      codStatoRendicontazione == RENDICONTAZIONE_STATUS.IN_RIESAME_I
    ) {
      return true;
    }
    return false;
  }

  controlloColoreHeader(i: number) {
    if (i < 2) {
      return 'grey';
    }
    if (i > (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1)) && i < (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1)) + 3) {
      return 'fnps';
    }
    if (i > (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1)) + 3) {
      return 'fnps';
    }
    if (i == 2 || i == (2 + (this.nTabPrimaTranche + 1)) || i == (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1)) || i == (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1)) + 3) {
      return 'white';
    }
    if (i > 2 && i < (2 + (this.nTabPrimaTranche + 1))) {
      return 'primaTranche';
    }
    if (i > (2 + (this.nTabPrimaTranche + 1)) && i < (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1))) {
      return 'secondaTranche';
    }
  }

  controlloColore(i: number) {
    if (i > 2 && i < (2 + (this.nTabPrimaTranche + 1))) {
      return 'primaTranche';
    }
    if (i > (2 + (this.nTabPrimaTranche + 1)) && i < (2 + (this.nTabPrimaTranche + 1) + (this.nTabSecondaTranche + 1))) {
      return 'secondaTranche';
    }
    return 'white';
  }


  ngAfterViewInit() {
    if (this.client.paginaSalvataCruscotto !== this.paginator.pageIndex) {
      setTimeout(() => {
        this.paginator.pageIndex = this.client.paginaSalvataCruscotto;
        this.dataListaRichieste.paginator = this.paginator;
        this.dataListaRichieste.sort = this.sort;
      }, 0);
    }
    // else {
    //     this.dataListaRichieste.paginator = this.paginator;
    //     this.dataListaRichieste.sort = this.sort;
    // }
  }

  info(codModello: string, idRendicontazione: number, desModello: string, denominazione: string) {
    this.client.spinEmitter.emit(true);
    let modello: ModelTabTranche;
    this.client.getTranchePerModello(idRendicontazione, codModello).subscribe((r) => {
      modello = r;

      let infoModello: InfoModello = new InfoModello();
      infoModello.codModello = codModello;
      infoModello.idRendicontazione = idRendicontazione;
      infoModello.desModello = desModello;
      this.client.infoModello(infoModello).subscribe(
        (data: GenericResponseWarnCheckErrGreg) => {
          this.warnings = data.warnings;
          this.errors = data.errors;
          this.check = data.check;
          this.titolo = "INFO " + desModello;
          this.messaggio = data.descrizione;
          this.denomin = denominazione;
          this.codObbl = modello.codObbligo;
          this.val = data.valorizzato;
          this.openDialog();
          this.client.spinEmitter.emit(false);
        },
        err => {
          this.client.spinEmitter.emit(false);
        }
      );
    })
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(MessaggioPopupCruscottoComponent, {
      width: '70%',
      disableClose: true,
      autoFocus: true,
      data: { titolo: this.titolo, warnings: this.warnings, errors: this.errors, check: this.check, chiudi: null, messaggio: this.messaggio, denominazione: this.denomin, codObbl: this.codObbl, val: this.val }
    });
  }

  colonnaFnps(colonna: string) {
    if (colonna == 'FNPS1' || colonna == 'FNPS2') {
      return true;
    }
    return false;
  }

  attivaPointer(modello: string, stato: string, rendicontazione: string) {
    if (modello == "ModelloMacroaggregati" && rendicontazione != RENDICONTAZIONE_STATUS.DA_COMPILARE_I) {
      if (stato == 'COMPILATO' && this.client.azioni.get(modello)[1]) {
        return true;
      }
      if (stato == 'NON_COMPILATO' && this.client.azioni.get(modello)[1]) {
        return true;
      }
      if (stato == 'COMPILATO_PARZIALE' && this.client.azioni.get(modello)[1]) {
        return true;
      }
      if (stato == 'NON_DISPONIBILE' && this.client.azioni.get(modello)[1]) {
        return true;
      }
    } else if (modello != "ModelloMacroaggregati") {
      if (stato == 'COMPILATO' && this.client.azioni.get(modello)[1]) {
        return true;
      }
      if (stato == 'NON_COMPILATO' && this.client.azioni.get(modello)[1]) {
        return true;
      }
      if (stato == 'COMPILATO_PARZIALE' && this.client.azioni.get(modello)[1]) {
        return true;
      }
      if (stato == 'NON_DISPONIBILE' && this.client.azioni.get(modello)[1]) {
        return true;
      }
    }
    return false;
  }
}
