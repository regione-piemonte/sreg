import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { StatoRendicontazioneGreg } from '@greg-app/app/dto/StatoRendicontazioneGreg';
import { RicercaGreg } from '@greg-app/app/dto/RicercaGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { TipoEnteGreg } from '@greg-app/app/dto/TipoEnteGreg';
import { ComuneGreg } from '@greg-app/app/dto/ComuneGreg';
import { CronologiaComponent } from '@greg-shared/cronologia/cronologia.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RicercaGregOutput } from '@greg-app/app/dto/RicercaGregOutput';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { ERRORS, SECTION } from '@greg-app/constants/greg-constants';
import { EnteGregWithComuniAss } from '@greg-app/app/dto/EnteGregWithComuniAss';
import { StatoEnte } from '@greg-app/app/dto/StatoEnte';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { SelectionModel } from '@angular/cdk/collections';
import { MatDialog } from '@angular/material';
import { ListaEntiAnno } from '@greg-app/app/dto/ListaEntiAnno';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { CancPopupComponent } from '../configuratore-prestazioni/canc-popup/canc-popup.component';
import { OpCambioStatoPopupComponent } from '../op-cambio-stato-popup/op-cambio-stato-popup.component';
import { UtenzeFnps } from '@greg-app/app/dto/UtenzeFnps';
import { ModelUtenzaAllD } from '@greg-app/app/dto/ModelUtenzaAllD';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { MsgApplicativo } from '@greg-app/app/dto/MsgApplicativo';

@Component({
  selector: 'app-configuratore-fnps',
  templateUrl: './configuratore-fnps.component.html',
  styleUrls: ['./configuratore-fnps.component.css']
})
export class ConfiguratoreFnpsComponent implements OnInit {

  tooltipRendicontazione: string = '';

  isChecked: boolean = false;
  isDisabled: boolean = true;

  searchForm: FormGroup;

  /*  statiEnte: StatoEnte[];
    listaStati: StatoRendicontazioneGreg[];
    listaComuni: ComuneGreg[];
    listaTipoEnti: TipoEnteGreg[];
    listaDenominazioniEnti: EnteGreg[];
    listaDenomEnteWithComAss: EnteGregWithComuniAss[];*/

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }
  denomEnteFiltered: EnteGregWithComuniAss[];
  // anniEsercizio: number[];
  denomSelected: string;
  dataSistema = new Date();
  annoSelezionato: number = null;
  anniEsercizio: number[] = [];
  utenzeFnps: UtenzeFnps[] = [];
  utenzeSelezionabiliFnps: ModelUtenzaAllD[] = [];
  filtriUtenzeFnps: UtenzeFnps[] = [];
  utenzaSelezionata: UtenzeFnps = new UtenzeFnps();
  displayedColumns: string[] = ['check', 'codiceRegionale', 'denominazione', 'comuneSedeLegale', 'tipoEnte', 'dettaglio', 'statoEnte', 'storico'];
  secondRow: string[] = ['rendicontazione', 'statoRendicontazione.descStatoRendicontazione', 'vuoto', 'datiEnte', 'vuoto2']
  // displayedColumns: string[] = ['check', 'codiceRegionale', 'statoRendicontazione.descStatoRendicontazione', 'denominazione', 'comuneSedeLegale', 'tipoEnte', 'datiEnte', 'datiRendi', 'cronologia'];
  dataListaRichieste: MatTableDataSource<RicercaGregOutput>;
  ricerca: RicercaGregOutput[];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;


  idSelected: number;
  selezionaTutto: boolean = false;
  // Parametro per la renderizzazione del pulsante Home
  param: boolean = false;
  datiEnte: DatiEnteGreg;
  messaggioErrore: string;
  messaggioPopupElimina: any;
  messaggioErrorePercentuale: string;
  totale: number = 0;
  totaleString: string = "0,00"
  constructor(private fb: FormBuilder, private router: Router, private dialog: MatDialog, private route: ActivatedRoute, private gregError: GregErrorService,
    private modalService: NgbModal, public client: GregBOClient, public toastService: AppToastService) { }

  ngOnInit() {
    this.client.spinEmitter.emit(true);
    forkJoin({
      utenzeFnps: this.client.getUtenzeFnps(),
      utenzeSelezionabiliFnps: this.client.getUtenzeSelezionabiliFnps(),
      anniEsercizio: this.client.getAnniEsercizio(),
      msgApplicativo: this.client.getMsgApplicativo(ERRORS.ERRORE_SALVATAGGIO_FNPS_UTENZE_CALCOLO),
      messaggioPopupElimina: this.client.getMsgInformativi(SECTION.MESSAGGIOELIMINAVINCOLO),
      msgErrore: this.client.getMsgApplicativo(ERRORS.ERRVINCOLOFNPS),
    })
      .subscribe(({ anniEsercizio, utenzeFnps, utenzeSelezionabiliFnps, msgApplicativo, messaggioPopupElimina, msgErrore }) => {
        this.annoSelezionato = null;
        this.messaggioPopupElimina = messaggioPopupElimina[0].testoMsgInformativo;
        this.messaggioErrore = msgApplicativo.testoMsgApplicativo;
        this.messaggioErrorePercentuale = msgErrore.testoMsgApplicativo;
        this.anniEsercizio = anniEsercizio;
        this.utenzeFnps = utenzeFnps;
        for (let u of this.utenzeFnps) {
          u.valorePercentuale = this.transform(parseFloat(u.valorePercentuale.toString().replace(',', '.')));
        }
        this.filtriUtenzeFnps = JSON.parse(JSON.stringify(this.utenzeFnps));

        this.utenzeSelezionabiliFnps = utenzeSelezionabiliFnps;
        this.idSelected = this.utenzeSelezionabiliFnps[0].idUtenza;
        this.initUtenzaSelezionata();
        this.client.spinEmitter.emit(false);

      },
        err => {
          this.client.spinEmitter.emit(false)
        }
      )
  }

  initUtenzaSelezionata() {
    let ute = this.utenzeSelezionabiliFnps.find(u => u.idUtenza == this.idSelected);
    this.utenzaSelezionata.idUtenza = ute.idUtenza;
    this.utenzaSelezionata.codUtenza = ute.codUtenza;
    this.utenzaSelezionata.descUtenza = ute.descUtenza;
    this.utenzaSelezionata.valorePercentuale = null;
    this.utenzaSelezionata.annoInizioValidita = null;
    this.utenzaSelezionata.annoFineValidita = null;
    this.utenzaSelezionata.utilizzatoPerCalcolo = false;
  }

  onSelectionAnnoChanged(value: string) {
    this.annoSelezionato = parseInt(value);
    this.totale = 0;
    if (value != null && value != 'null') {
      this.filtriUtenzeFnps = JSON.parse(JSON.stringify(this.utenzeFnps.filter(f => parseInt(f.annoInizioValidita) <= parseInt(value) && (f.annoFineValidita == null || parseInt(f.annoFineValidita) >= parseInt(value)))));
      for(let u of this.filtriUtenzeFnps){
        this.totale += this.parsingFloat(u.valorePercentuale);
      }
      this.totaleString = this.transform(this.totale);
    }
    else {
      this.filtriUtenzeFnps = JSON.parse(JSON.stringify(this.utenzeFnps));
    }
  }

  onSelectionUtenzaChanged(value: number) {
    let ute = this.utenzeSelezionabiliFnps.find(u => u.idUtenza == value);
    this.utenzaSelezionata.idUtenza = ute.idUtenza;
    this.utenzaSelezionata.codUtenza = ute.codUtenza;
    this.utenzaSelezionata.descUtenza = ute.descUtenza;
    this.utenzaSelezionata.utilizzatoPerCalcolo = false;
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

  changeKeyIntAnnoInizio(valore: any, i: number) {
    if (valore == "" || valore == null || valore == undefined) {
      this.utenzeFnps[i].annoInizioValidita = null;
    } else {
      this.utenzeFnps[i].annoInizioValidita = parseInt(valore).toString();
    }

  }

  changeKeyIntAnnoFine(valore: any, i: number) {
    if (valore == "" || valore == null || valore == undefined) {
      this.utenzeFnps[i].annoFineValidita = null;
    } else {
      this.utenzeFnps[i].annoFineValidita = parseInt(valore).toString();
    }

  }

  changeKeyIntNewAnnoInizio(valore: any) {
    if (valore == "" || valore == null || valore == undefined) {
      this.utenzaSelezionata.annoInizioValidita = null;
    } else {
      this.utenzaSelezionata.annoInizioValidita = parseInt(valore).toString();
    }

  }

  changeKeyIntNewAnnoFine(valore: any) {
    if (valore == "" || valore == null || valore == undefined) {
      this.utenzaSelezionata.annoFineValidita = null;
    } else {
      this.utenzaSelezionata.annoFineValidita = parseInt(valore).toString();
    }

  }

  changeKey(valore: any) {
    if (valore == "" || valore == null || valore == undefined) {
      this.utenzaSelezionata.valorePercentuale = null;
    }
    else {
      if (valore.indexOf('.') != -1) {
        if (valore.indexOf(',') != -1 && (valore.slice(valore.indexOf(',') + 1, valore.length)).length != 2) {
          valore = [valore, '0'].join('');
        }
        if (valore > 100) {
          valore = 100;
        }
        this.utenzaSelezionata.valorePercentuale = this.transform(parseFloat(valore.toString().replaceAll('.', '').replace(',', '.')));
      }
      else {
        if (valore > 100) {
          valore = 100;
        }
        this.utenzaSelezionata.valorePercentuale = this.transform(parseFloat(valore.toString().replace(',', '.')));
      }
    }

  }
  parsingFloat(el) {
    if (el == '') el = null;
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
    let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
    if (this.isNumber(value)) {
      if (value != null && value != undefined) {
        let num = value.toFixed(Math.max(0, ~~decimalLength));
        return currencySign + (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter);
      }
    }
    return undefined;
  }

  eliminaUtenza(utenza: UtenzeFnps, i: number) {
    if (utenza.idUtenzaFnps == null) {
      this.client.spinEmitter.emit(true);
      this.utenzeFnps = this.utenzeFnps.filter((f, index) => index != i);
      this.client.spinEmitter.emit(false);
    } else {
      let messaggio = this.messaggioPopupElimina.replace("VINCOLO", utenza.descUtenza);
      const dialogRef = this.dialog.open(CancPopupComponent, {
        width: '650px',
        disableClose: true,
        autoFocus: true,
        data: { titolo: 'Conferma Eliminazione', messaggio: messaggio }
      });
      dialogRef.afterClosed().subscribe(r => {
        if (r) {
          this.utenzeFnps = this.utenzeFnps.filter((f, index) => index != i);
          this.client.spinEmitter.emit(true);
          utenza.valorePercentuale = this.parsingFloat(utenza.valorePercentuale);
          this.client.eliminaUtenzaFnps(utenza).subscribe(r => {
            this.toastService.showSuccess({ text: r.descrizione });
            this.onSelectionAnnoChanged(this.annoSelezionato ? this.annoSelezionato.toString() : "null");
            this.client.spinEmitter.emit(false);


          },
            err => {
              this.client.spinEmitter.emit(false)
            })
        }
      })

    }
  }

  disableUtenza() {
    if (this.utenzaSelezionata.annoInizioValidita != null && this.utenzaSelezionata.valorePercentuale != null) {
      return this.utenzeFnps.some(f => f.idUtenza == this.utenzaSelezionata.idUtenza && (f.annoFineValidita == null || parseInt(f.annoFineValidita) > parseInt(this.utenzaSelezionata.annoInizioValidita)));
    } else {
      return true;
    }
  }

  aggiungiUtenza() {
    this.client.spinEmitter.emit(true);
    this.utenzeFnps.push(JSON.parse(JSON.stringify(this.utenzaSelezionata)));
    this.utenzeFnps.sort((a, b) => this.sortUtenze(a, b));
    this.onSelectionAnnoChanged(this.annoSelezionato ? this.annoSelezionato.toString() : "null");
    this.initUtenzaSelezionata();
    this.client.spinEmitter.emit(false);
  }

  sortUtenze(a: UtenzeFnps, b: UtenzeFnps) {
    if (a.codUtenza < b.codUtenza) {
      return -1;
    } else if (a.codUtenza == b.codUtenza) {
      if (a.annoInizioValidita < b.annoInizioValidita) {
        return -1;
      } else {
        return 1;
      }
    } else {
      return 1;
    }
  }

  salvaUtenze() {
    this.client.spinEmitter.emit(true);
    let errore: boolean = false;
    for(let anno of this.anniEsercizio){
      let sommaPercentuale = 0;
      let utenze = this.utenzeFnps.filter(f => parseInt(f.annoInizioValidita) <= anno && (f.annoFineValidita == null || parseInt(f.annoFineValidita) >= anno));
      for(let u of utenze){
        sommaPercentuale += this.parsingFloat(u.valorePercentuale);
      }
      if(sommaPercentuale > 100){
        errore = true;
        this.errorMessage.error.descrizione = this.messaggioErrorePercentuale.replace("ANNO", anno.toString());
        this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.messaggioErrorePercentuale }));
        this.client.spinEmitter.emit(false)
      }
      sommaPercentuale = 0;
    }
    if(errore){
      return;
    }
    if (!this.utenzeFnps.some(f => parseInt(f.annoFineValidita) < parseInt(f.annoInizioValidita))) {
      for (let u of this.utenzeFnps) {
        u.valorePercentuale = this.parsingFloat(u.valorePercentuale);
      }
      this.client.creaUtenzeFnps(this.utenzeFnps).subscribe(r => {
        this.client.getUtenzeFnps().subscribe(utenze => {
          this.utenzeFnps = utenze;

          for (let u of this.utenzeFnps) {
            u.valorePercentuale = this.transform(parseFloat(u.valorePercentuale.toString().replace(',', '.')));
          }
          this.filtriUtenzeFnps = JSON.parse(JSON.stringify(this.utenzeFnps));
          this.toastService.showSuccess({ text: r.descrizione });
          this.client.spinEmitter.emit(false);
        },
          err => {
            this.client.spinEmitter.emit(false)
          })
      },
        err => {
          this.client.spinEmitter.emit(false)
        })
    } else {
      this.errorMessage.error.descrizione = this.messaggioErrore;
      this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: this.messaggioErrore }));
      this.client.spinEmitter.emit(false)
    }
  }
}
