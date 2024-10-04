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
import { StoricoComponent } from './Storico/storico.component';
import { SelectionModel } from '@angular/cdk/collections';
import { MatDialog } from '@angular/material';
import { SelezioneAnnoComponent } from './selezione-anno/selezione-anno.component';
import { ListaEntiAnno } from '@greg-app/app/dto/ListaEntiAnno';
import { MessaggioPopupCreaAnnoComponent } from './messaggio-popup-crea-anno/messaggio-popup-crea-anno.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { CancPopupComponent } from '../configuratore-prestazioni/canc-popup/canc-popup.component';
import { OpCambioStatoPopupComponent } from '../op-cambio-stato-popup/op-cambio-stato-popup.component';

@Component({
  selector: 'app-enti-gestori-attivi',
  templateUrl: './enti-gestori-attivi.component.html',
  styleUrls: ['./enti-gestori-attivi.component.css']
})
export class EntiGestoriAttiviComponent implements OnInit, AfterViewInit {

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
  denomEnteFiltered: EnteGregWithComuniAss[];
 // anniEsercizio: number[];
  denomSelected: string;
 dataSistema = new Date();
  annoSelezionato: number = null;
  statoSelezionato: string = null;

  displayedColumns: string[] = ['check', 'codiceRegionale', 'denominazione', 'comuneSedeLegale', 'tipoEnte', 'dettaglio', 'statoEnte', 'storico'];
  secondRow: string[] = ['rendicontazione', 'statoRendicontazione.descStatoRendicontazione', 'vuoto', 'datiEnte', 'vuoto2']
  // displayedColumns: string[] = ['check', 'codiceRegionale', 'statoRendicontazione.descStatoRendicontazione', 'denominazione', 'comuneSedeLegale', 'tipoEnte', 'datiEnte', 'datiRendi', 'cronologia'];
  dataListaRichieste: MatTableDataSource<RicercaGregOutput>;
  ricerca: RicercaGregOutput[];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;



  selezionaTutto:boolean = false;
  // Parametro per la renderizzazione del pulsante Home
  param: boolean = false;
  datiEnte: DatiEnteGreg;
  constructor(private fb: FormBuilder, private router: Router, private dialog: MatDialog, private route: ActivatedRoute, 
    private modalService: NgbModal, public client: GregBOClient, public toastService: AppToastService) {}

  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  checkUncheckAll() {
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element=>{
      if(element.checked){
        c++;
      }
    })
    if(c<this.dataListaRichieste.data.length){
      this.dataListaRichieste.data.forEach(element=>{
        element.checked=true;
      })
    } else {
      this.dataListaRichieste.data.forEach(element=>{
        element.checked=!element.checked;
      })
    }
    
  }

  ngOnInit() {
if (this.client.listaStatiSalvato.length==0){
	    this.client.spinEmitter.emit(true);
	forkJoin({
      statiEnte: this.client.getStatiEnte(),
      listaStati: this.client.getListaStatoRendicontazione(),
      listaComuni: this.client.getListaComuni(null),
      listaTipoEnti: this.client.getListaTipoEnte(),
      listaDenominazioniEnti: this.client.getListaDenominazioni(),
      anniEsercizio: this.client.getAnniEsercizio(),
      msgApplicativo: this.client.getMsgApplicativo(ERRORS.ERROR_ANNO_CONTABILE)
    })
    .subscribe(({listaStati, 
      listaComuni, 
      listaTipoEnti, listaDenominazioniEnti, 
       msgApplicativo, statiEnte, anniEsercizio}) => {
      this.client.statiEnteSalvato = statiEnte;
      this.client.listaStatiSalvato = listaStati;
      this.client.listaComuniSalvato = listaComuni;
      this.client.listaComuniinitial = listaComuni;
      this.client.listaTipoEntiSalvato = listaTipoEnti;
      this.client.listaDenominazioniEntiSalvato = listaDenominazioniEnti;
      this.client.anniEsercizioSalvato = anniEsercizio;
      this.client.tooltipRendicontazione = msgApplicativo.testoMsgApplicativo;
    //  this.client.spinEmitter.emit(false);
      this.avviaRicerca();
    },
    err => {
      this.client.spinEmitter.emit(false)
      }
    );
    this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput>();
    this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    this.dataListaRichieste.sort = this.sort;
  
    this.searchForm = this.fb.group({
      statoEnte: [],
      denominazioneEnte: ['', [Validators.maxLength(300)]],
      statoRendicontazione: [],
      tipoEnte: [],
      comune: [],
      annoEsercizio: []
    });
}
else{
	this.client.listaComuniSalvato = this.client.listaComuniinitial;
	 if (this.client.ricercaEnte.length>0){
	    this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput>();
      this.dataListaRichieste.data = this.client.ricercaEnte  as any;
 this.searchForm = this.fb.group({
      statoEnte: this.client.filtroEnte.statoEnte,
      denominazioneEnte: this.client.filtroEnte.denominazioneEnte,
      statoRendicontazione: this.client.filtroEnte.statoRendicontazione,
      tipoEnte: this.client.filtroEnte.tipoEnte,
      comune: this.client.filtroEnte.comune,
      annoEsercizio: this.client.filtroEnte.annoEsercizio
    });
    this.annoSelezionato = this.client.filtroEnte.annoEsercizio;
    this.statoSelezionato = this.client.filtroEnte.statoRendicontazione;
}
else {
 this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput>();
 this.searchForm = this.fb.group({
      statoEnte: [],
      denominazioneEnte: ['', [Validators.maxLength(300)]],
      statoRendicontazione: [],
      tipoEnte: [],
      comune: [],
      annoEsercizio: []
    });
  this.avviaRicerca();
    this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    this.dataListaRichieste.sort = this.sort; 
}
}
  }



  ngAfterViewInit() {
	if (this.client.paginaSalvata !== this.paginator.pageIndex) {
            setTimeout(() => {
                this.paginator.pageIndex = this.client.paginaSalvata;
				this.sort.active = this.client.ordinamentoSalvato;
    			this.sort.direction = this.client.versoSalvato;
				this.dataListaRichieste.paginator = this.paginator;
    			this.dataListaRichieste.sort = this.sort;
            }, 0);
        }
	else {
	this.dataListaRichieste.paginator = this.paginator;
    this.dataListaRichieste.sort = this.sort;
}
  }

  onSelectionComuneChanged(value: string) {
//    this.searchForm.get('denominazioneEnte').reset();
//    this.denomEnteFiltered = value == ''? this.listaDenomEnteWithComAss : this.listaDenomEnteWithComAss.filter(ente => ente.comuniAssociati.includes(parseFloat(value)));
  }

  onSelectionAnnoChanged(value: string) {
	if (value!=''){
	this.client.spinEmitter.emit(true);
    this.searchForm.get('comune').reset();
    this.client.getListaComuni(value + '-01-01').subscribe(listaComuni=> {
	this.client.listaComuniSalvato = listaComuni;
	this.client.spinEmitter.emit(false);
	});
	}
	else {
		this.client.listaComuniSalvato = this.client.listaComuniinitial; 
	}
  }

  avviaRicerca() {
    this.client.spinEmitter.emit(true);
    const statoEnte = this.searchForm.controls.statoEnte.value && this.searchForm.controls.statoEnte.value!="" ? this.searchForm.controls.statoEnte.value : null;
    const denominazioneEnte = this.searchForm.controls.denominazioneEnte.value && this.searchForm.controls.denominazioneEnte.value.toUpperCase();
    const statoRendicontazione = this.searchForm.controls.statoRendicontazione.value && this.searchForm.controls.statoRendicontazione.value!="" ? this.searchForm.controls.statoRendicontazione.value : null;
    const tipoEnte = this.searchForm.controls.tipoEnte.value && this.searchForm.controls.tipoEnte.value!="" ? this.searchForm.controls.tipoEnte.value : null;
    const comune = this.searchForm.controls.comune.value && this.searchForm.controls.comune.value!="" ? this.searchForm.controls.comune.value : null;
    const annoEsercizio = this.searchForm.controls.annoEsercizio.value && this.searchForm.controls.annoEsercizio.value!=null ? this.searchForm.controls.annoEsercizio.value : null;
    const lista = this.client.listaenti;
    this.annoSelezionato = annoEsercizio;
    this.statoSelezionato = statoRendicontazione;
    const ricerca = new RicercaGreg(statoEnte, denominazioneEnte, statoRendicontazione, tipoEnte, comune, annoEsercizio, lista);
    this.client.filtroEnte = ricerca;
    this.client.getSchedeEntiGestori( ricerca ).subscribe( (response: RicercaGregOutput[]) => {
      this.client.spinEmitter.emit(false);
      response.forEach(element=>{
        element.checked=false;
      })
      this.dataListaRichieste.data = response as any;
      this.ricerca = response;
	  this.client.ricercaEnte = response as any;
    },
    err => {
      this.client.spinEmitter.emit(false);
    });
  }

  
  routeTo(content: RicercaGregOutput, path: string){
		this.client.paginaSalvata = this.paginator.pageIndex
    this.client.ordinamentoSalvato = this.sort.active;
  this.client.versoSalvato = this.sort.direction ;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route, 
      skipLocationChange: true,
      state: {
        idSchedaEnteGestore: content.idSchedaEnteGestore,
        statoEnte: content.statoEnte.codStatoEnte
      }
    };
    this.router.navigate([path], navigationExtras);
  }



  routeToDatiEnte(rend:RendicontazioneEnteGreg, path: string){
	this.client.paginaSalvata = this.paginator.pageIndex
    this.client.ordinamentoSalvato = this.sort.active;
    this.client.versoSalvato = this.sort.direction ;
	this.client.getDatiEnteRendicontazione( rend.idRendicontazioneEnte,rend.annoEsercizio ).subscribe( (response: DatiEnteGreg) => {
		 this.datiEnte = response as any;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route, 
      skipLocationChange: true,
      state: {
        rendicontazione: rend,
		datiEnte: this.datiEnte
		}
    };
    this.router.navigate([path], navigationExtras);
 });
  }


  annullaRicerca() {
    this.searchForm.reset();
    this.denomEnteFiltered = [];
    this.dataListaRichieste.data = [];
  }

  openCronologia(content: RendicontazioneEnteGreg) {
    const modalRef = this.modalService.open(CronologiaComponent, { size: 'xl' });
    modalRef.componentInstance.archive = false;
    modalRef.componentInstance.idRendicontazione = content.idRendicontazioneEnte;
  }




  openStorico(content: RicercaGregOutput) {
	this.client.paginaSalvata = this.paginator.pageIndex
    this.client.ordinamentoSalvato = this.sort.active;
  this.client.versoSalvato = this.sort.direction ;
    const modalRef = this.modalService.open(StoricoComponent, { size: 'lg' });
    modalRef.componentInstance.archive = false;
    modalRef.componentInstance.idSchedaEnteGestore = content.idSchedaEnteGestore;
    modalRef.componentInstance.denominazione = content.denominazione;
    modalRef.componentInstance.statoEnte = content.statoEnte.codStatoEnte;
  }

  checkSelezione(){
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element=>{
      if(element.checked){
        c++;
      }
    })
    if(c==this.dataListaRichieste.data.length){
      this.selezionaTutto=true;
    }else {
      this.selezionaTutto=false;
    }
  }

  controlloSelezione(){
    let c: number = 0;
    this.dataListaRichieste.data.forEach(element=>{
      if(element.checked){
        c++;
      }
    })
    if(c==0){
      return true;
    }
    return false;
  }

  creaNuovoAnnoContabile() {
    let anno = new Date();
    let anni: number[] = [];
    for(let i=anno.getFullYear()-1; i<=anno.getFullYear()+3; i++){
      anni.push(i);
    }
    let enti: RicercaGregOutput[]=[];
    this.dataListaRichieste.data.forEach(element =>{
      if(element.checked){
        enti.push(element);
      }
    })
    this.openDialogMotivazione(anni, enti);
  }

  openDialogMotivazione(anni: number[], enti: RicercaGregOutput[]){
    const dialogRef = this.dialog.open(SelezioneAnnoComponent, {
        width: '600px',
        disableClose : true,
        autoFocus : true,
        data: {titolo: 'Seleziona anno da generare', anni}
     });
       dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.selezionaTutto=false;
        this.client.spinEmitter.emit(true);
          let creaAnno: ListaEntiAnno = new ListaEntiAnno();
          creaAnno.anno=result;
          creaAnno.enti = enti;
          creaAnno.profilo = this.client.profilo;
          this.client.creaNuovoAnno(creaAnno).subscribe((r)=>{
            const dialogRef = this.dialog.open(MessaggioPopupCreaAnnoComponent, {
              width: '70%',
              disableClose: true,
              autoFocus: true,
              data: { titolo: 'Risultato della creazione dell\'anno', ok: r.ok, errors: r.errors, chiudi: null}
            });
            this.avviaRicerca();
            this.client.spinEmitter.emit(false);
          },
          err => {
            this.client.spinEmitter.emit(false)
            })
      }
    });
}

controlloConcludiSelezione(){
  let c: number = 0;
  let trovato: boolean = true;
    this.dataListaRichieste.data.forEach(element=>{
      if(element.checked){
        c++;
      }
    })
    if(c==0){
      trovato = false;
    }
    if(this.annoSelezionato==null){
      trovato = false;
    }
    if(this.statoSelezionato!='STO'){
      trovato = false;
    }
  
    return trovato;
  }

  concludiRendicontazione(){
    this.client.spinEmitter.emit(true);
    let enti: RicercaGregOutput[]=[];
    let enteSelezionato: string = '<ul>';
    this.dataListaRichieste.data.forEach(element =>{
      if(element.checked){
        element.profilo = this.client.profilo;
        enti.push(element);
        enteSelezionato += '<li>' + element.denominazione + '; </li>';
      }
    })
    enteSelezionato += '</ul>'
    
    this.client.getMsgInformativi(SECTION.MESSAGGIOCONCLUDIMULTIPLO).subscribe((t)=>{
			let messaggio = t[0].testoMsgInformativo.replace("ENTE", enteSelezionato).replace("ANNO", this.annoSelezionato.toString());
			const dialogRef = this.dialog.open(OpCambioStatoPopupComponent, {
				width: '650px',
				disableClose: true,
				autoFocus: true,
				data: { titolo: 'Conferma Conclusione Rendicontazione', messaggio: messaggio, operazione: true }
			});
      this.client.spinEmitter.emit(false);
			dialogRef.afterClosed().subscribe(r => {
				if (r) {
          this.client.spinEmitter.emit(true);
          enti.forEach(e => {
            e.notaEnte = r.notaEnte;
            e.notaInterna = r.notaInterna;
          });
					this.client.concludiRendicontazione(enti).subscribe((r) => {
            this.selezionaTutto = false;
						  this.avviaRicerca();
              this.client.listaStatiSalvatoArchivio = [];
						this.toastService.showSuccess({ text: r.descrizione });
						this.client.spinEmitter.emit(false);
					},
						err => {
							this.client.spinEmitter.emit(false)
						})
				} else {
          this.client.spinEmitter.emit(false)
        }
			}
			);
		})
  }

  riapriFNPS(){
    this.client.spinEmitter.emit(true);
    let enti: RicercaGregOutput[]=[];
    let enteSelezionato: string = '<ul>';
    this.dataListaRichieste.data.forEach(element =>{
      if(element.checked){
        element.profilo = this.client.profilo;
        enti.push(element);
        enteSelezionato += '<li>' + element.denominazione + '; </li>';
      }
    })
    enteSelezionato += '</ul>'
    
    this.client.getMsgInformativi(SECTION.MESSAGGIORIAPRICOMPILAZIONEMULTIPLO).subscribe((t)=>{
			let messaggio = t[0].testoMsgInformativo.replace("ENTE", enteSelezionato).replace("ANNO", this.annoSelezionato.toString());
			const dialogRef = this.dialog.open(OpCambioStatoPopupComponent, {
				width: '650px',
				disableClose: true,
				autoFocus: true,
				data: { titolo: 'Conferma Riapertura Rendicontazione', messaggio: messaggio, operazione: true }
			});
      this.client.spinEmitter.emit(false);
			dialogRef.afterClosed().subscribe(r => {
				if (r) {
          this.client.spinEmitter.emit(true);
          enti.forEach(e => {
            e.notaEnte = r.notaEnte;
            e.notaInterna = r.notaInterna;
          });
					this.client.ripristinaCompilazione(enti).subscribe((r) => {
            this.selezionaTutto = false;
						  this.avviaRicerca();
              this.client.listaStatiSalvatoArchivio = [];
						this.toastService.showSuccess({ text: r.descrizione });
						this.client.spinEmitter.emit(false);
					},
						err => {
							this.client.spinEmitter.emit(false)
						})
				} else {
          this.client.spinEmitter.emit(false)
        }
			}
			);
		})
  }


  storicizzaMultiplo(){
    this.client.spinEmitter.emit(true);
    let enti: RicercaGregOutput[]=[];
    let enteSelezionato: string = '<ul>';
    this.dataListaRichieste.data.forEach(element =>{
      if(element.checked){
        element.profilo = this.client.profilo;
        enti.push(element);
        enteSelezionato += '<li>' + element.denominazione + '; </li>';
      }
    })
    enteSelezionato += '</ul>'
    
    this.client.getMsgInformativi(SECTION.MESSAGGIOSTORICIZZAMULTIPLO).subscribe((t)=>{
			let messaggio = t[0].testoMsgInformativo.replace("ENTE", enteSelezionato).replace("ANNO", this.annoSelezionato.toString());
			const dialogRef = this.dialog.open(OpCambioStatoPopupComponent, {
				width: '650px',
				disableClose: true,
				autoFocus: true,
				data: { titolo: 'Conferma Storicizza Rendicontazione', messaggio: messaggio, operazione: true }
			});
      this.client.spinEmitter.emit(false);
			dialogRef.afterClosed().subscribe(r => {
				if (r) {
          this.client.spinEmitter.emit(true);
          enti.forEach(e => {
            e.notaEnte = r.notaEnte;
            e.notaInterna = r.notaInterna;
          });
					this.client.storicizzaMultiplo(enti).subscribe((r) => {
            this.selezionaTutto = false;
						  this.avviaRicerca();
              this.client.listaStatiSalvatoArchivio = [];
						this.toastService.showSuccess({ text: r.descrizione });
						this.client.spinEmitter.emit(false);
					},
						err => {
							this.client.spinEmitter.emit(false)
						})
				} else {
          this.client.spinEmitter.emit(false)
        }
			}
			);
		})
  }

  controlloStoricizzaSelezione(){
    let c: number = 0;
    let trovato: boolean = true;
      this.dataListaRichieste.data.forEach(element=>{
        if(element.checked){
          c++;
        }
      })
      if(c==0){
        trovato = false;
      }
      if(this.annoSelezionato==null){
        trovato = false;
      }
      if(this.statoSelezionato!='VAL'){
        trovato = false;
      }
    
      return trovato;
    }
  
}
