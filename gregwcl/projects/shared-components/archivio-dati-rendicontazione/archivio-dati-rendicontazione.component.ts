import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';
import { StatoRendicontazioneGreg } from '@greg-app/app/dto/StatoRendicontazioneGreg';
import { RicercaGreg } from '@greg-app/app/dto/RicercaGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ComuneGreg } from '@greg-app/app/dto/ComuneGreg';
import { TipoEnteGreg } from '@greg-app/app/dto/TipoEnteGreg';
import { CronologiaComponent } from '../cronologia/cronologia.component';
import { FormBuilder, FormGroup } from '@angular/forms';
import { RicercaGregOutput } from '@greg-app/app/dto/RicercaGregOutput';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { RicercaArchivioGreg } from '@greg-app/app/dto/RicercaArchivioGreg';
import { Validators } from '@angular/forms';
import { ERRORS, SECTION } from '@greg-app/constants/greg-constants';
import { EnteGregWithComuniAss } from '@greg-app/app/dto/EnteGregWithComuniAss';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { StoricoComponent } from '@greg-operatore/components/enti-gestori-attivi/Storico/storico.component';
import { StoricoArchivioComponent } from './storico-archivio/storico-archivio.component';
import { MatDialog } from '@angular/material';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { CancPopupComponent } from '@greg-operatore/components/configuratore-prestazioni/canc-popup/canc-popup.component';
import { OpCambioStatoPopupComponent } from '@greg-operatore/components/op-cambio-stato-popup/op-cambio-stato-popup.component';

@Component({
	selector: 'app-archivio-dati-rendicontazione',
	templateUrl: './archivio-dati-rendicontazione.component.html',
	styleUrls: ['./archivio-dati-rendicontazione.component.css']
})
export class ArchivioDatiRendicontazioneComponent implements OnInit {

	searchForm: FormGroup;

	//	listaStati: StatoRendicontazioneGreg[];
	//	listaComuni: ComuneGreg[];
	//	listaTipoEnti: TipoEnteGreg[];
	//	listaAnni: [];
	selezionaTutto:boolean = false;
	isChecked: boolean = false;
	isDisabled: boolean = true;
	tooltipRendicontazioneArchivio: string = '';

	//	columnsListaRichieste: string[] = ['codiceRegionale', 'statoRendicontazione', 'denominazioneEnte', 'comuneSedeLegale', 'tipoEnte', 'datiEnte', 'datiRendi', 'cronologia'];
	//	secondRowArchivio: string[] = ['rendicontazione', 'statoRendicontazione.descStatoRendicontazione', 'vuoto', 'datiEnte', 'vuoto2']

	columnsListaRichieste: string[] = ['checkArc', 'codiceRegionaleArchivio', 'denominazioneArchivio', 'comuneSedeLegaleArchivio', 'tipoEnteArchivio', 'dettaglioArchivio', 'statoEnteArchivio', 'storicoArchivio'];
	secondRowArchivio: string[] = ['rendicontazioneArchivio', 'statoRendicontazioneArchivio.descStatoRendicontazione', 'vuotoArchivio', 'datiEnteArchivio', 'vuoto2Archivio']
	dataListaRichiesteArchivio: MatTableDataSource<RicercaGregOutput>;
	dataSourceListaRichiesteArchivio: MatTableDataSource<RicercaGregOutput>;
	datiEnteArchivio: DatiEnteGreg;
	ricerca: RicercaGregOutput[];
	@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
	@ViewChild(MatSort, { static: true }) sort: MatSort;
	denomEnteFilteredArc: EnteGregWithComuniAss[];
	annoSelezionato: any;
	statoSelezionato: string;

	constructor(private fb: FormBuilder, private router: Router, private route: ActivatedRoute,
		private modalService: NgbModal, public client: GregBOClient, private dialog: MatDialog, public toastService: AppToastService) { }


	ngOnInit() {
		if (this.client.listaStatiSalvatoArchivio.length == 0) {
			this.client.spinEmitter.emit(true);
			forkJoin({
				statiEnteArchivio: this.client.getStatiEnte(),
				statoRendicontazioneArchivio: this.client.getStatoRendicontazioneConclusa(),
				listaComuniArchivio: this.client.getListaComuniArchivio(),
				listaTipoEntiArchivio: this.client.getListaTipoEnteArchivio(),
				listaDenominazioniEntiArchivio: this.client.getListaDenominazioni(),
				anniEsercizioArchivio: this.client.getAnniEsercizioArchivio(),
				msgApplicativo: this.client.getMsgApplicativo(ERRORS.ERROR_ANNO_CONTABILE),
				
			})
				.subscribe(({ statoRendicontazioneArchivio,
					listaComuniArchivio,
					listaTipoEntiArchivio, listaDenominazioniEntiArchivio,
					msgApplicativo, statiEnteArchivio, anniEsercizioArchivio }) => {
					this.client.statiEnteSalvatoArchivio = statiEnteArchivio;
					this.client.statoRendicontazioneConslusaArchivio = statoRendicontazioneArchivio;
					this.client.listaComuniSalvatoArchivio = listaComuniArchivio;
					this.client.listaComuniinitialArchivio = listaComuniArchivio;
					this.client.listaTipoEntiSalvatoArchivio = listaTipoEntiArchivio;
					this.client.listaDenominazioniEntiSalvatoArchivio = listaDenominazioniEntiArchivio;
					this.client.anniEsercizioSalvatoArchivio = anniEsercizioArchivio;
					this.client.tooltipRendicontazione = msgApplicativo.testoMsgApplicativo;
					//  this.client.spinEmitter.emit(false);
					this.avviaRicerca();
				},
					err => {
						this.client.spinEmitter.emit(false)
					}
				);
			this.dataListaRichiesteArchivio = new MatTableDataSource<RicercaGregOutput>();
			this.dataListaRichiesteArchivio.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
			this.dataListaRichiesteArchivio.sort = this.sort;

			this.searchForm = this.fb.group({
				statoEnteArc: [],
				denominazioneEnteArc: ['', [Validators.maxLength(300)]],
				statoRendicontazioneArc: '',
				tipoEnteArc: [],
				comuneArc: [],
				annoEsercizioArc: []
			});
		}
		else {
			this.client.listaComuniSalvatoArchivio = this.client.listaComuniinitialArchivio;
			if (this.client.ricercaEnteArchivio.length > 0) {
				this.dataListaRichiesteArchivio = new MatTableDataSource<RicercaGregOutput>();
				this.dataListaRichiesteArchivio.data = this.client.ricercaEnteArchivio as any;
				this.searchForm = this.fb.group({
					statoEnteArc: this.client.filtroEnteArchivio.statoEnte,
					denominazioneEnteArc: this.client.filtroEnteArchivio.denominazioneEnte,
					statoRendicontazioneArc: this.client.filtroEnteArchivio.statoRendicontazione,
					tipoEnteArc: this.client.filtroEnteArchivio.tipoEnte,
					comuneArc: this.client.filtroEnteArchivio.comune,
					annoEsercizioArc: this.client.filtroEnteArchivio.annoEsercizio
				});
				this.annoSelezionato = this.client.filtroEnteArchivio.annoEsercizio;
    			this.statoSelezionato = this.client.filtroEnteArchivio.statoRendicontazione;
			}
			else {
				this.dataListaRichiesteArchivio = new MatTableDataSource<RicercaGregOutput>();
				this.searchForm = this.fb.group({
					statoEnteArc: [],
					denominazioneEnteArc: ['', [Validators.maxLength(300)]],
					statoRendicontazioneArc: '',
					tipoEnteArc: [],
					comuneArc: [],
					annoEsercizioArc: []
				});
				this.avviaRicerca();
				this.dataListaRichiesteArchivio.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
				this.dataListaRichiesteArchivio.sort = this.sort;
			}
		}
	}

	ngAfterViewInit() {
		if (this.client.paginaSalvata !== this.paginator.pageIndex) {
			setTimeout(() => {
				this.paginator.pageIndex = this.client.paginaSalvata;
				this.sort.active = this.client.ordinamentoSalvato;
				this.sort.direction = this.client.versoSalvato;
				this.dataListaRichiesteArchivio.paginator = this.paginator;
				this.dataListaRichiesteArchivio.sort = this.sort;
			}, 0);
		}
		else {
			this.dataListaRichiesteArchivio.paginator = this.paginator;
			this.dataListaRichiesteArchivio.sort = this.sort;
		}
	}

	avviaSpinner() {
		$("#btnSearch").css("display", "none");
		$("#btnLoading").css("display", "");
	}

	stopSpinner() {
		$("#btnSearch").css("display", "");
		$("#btnLoading").css("display", "none");
	}

	avviaRicerca() {
		this.client.spinEmitter.emit(true);
		const statoEnteArc = this.searchForm.controls.statoEnteArc.value && this.searchForm.controls.statoEnteArc.value != "" ? this.searchForm.controls.statoEnteArc.value : null;
		const denominazioneEnteArc = this.searchForm.controls.denominazioneEnteArc.value && this.searchForm.controls.denominazioneEnteArc.value.toUpperCase();
		const statoRendicontazioneArc = this.client.statoRendicontazioneConslusaArchivio.codStatoRendicontazione;
		const tipoEnteArc = this.searchForm.controls.tipoEnteArc.value && this.searchForm.controls.tipoEnteArc.value != "" ? this.searchForm.controls.tipoEnteArc.value : null;
		const comuneArc = this.searchForm.controls.comuneArc.value && this.searchForm.controls.comuneArc.value != "" ? this.searchForm.controls.comuneArc.value : null;
		const annoEsercizioArc = this.searchForm.controls.annoEsercizioArc.value && this.searchForm.controls.annoEsercizioArc.value != null ? this.searchForm.controls.annoEsercizioArc.value : null;
		const listaArc = this.client.listaenti;
		const ricercaArc = new RicercaGreg(statoEnteArc, denominazioneEnteArc, statoRendicontazioneArc, tipoEnteArc, comuneArc, annoEsercizioArc, listaArc);
		this.client.filtroEnteArchivio = ricercaArc;
		this.annoSelezionato = annoEsercizioArc;
    	this.statoSelezionato = statoRendicontazioneArc;
		this.client.getSchedeEntiGestoriRendicontazioneConclusa(ricercaArc).subscribe((response: RicercaGregOutput[]) => {
			this.client.spinEmitter.emit(false);
			this.dataListaRichiesteArchivio.data = response as any;
			this.ricerca = response;
			this.client.ricercaEnteArchivio = response as any;
		},
			err => {
				this.client.spinEmitter.emit(false);
			});

	}

	routeTo(content: RicercaGregOutput, path: string) {
		this.client.paginaSalvata = this.paginator.pageIndex
		this.client.ordinamentoSalvato = this.sort.active;
		this.client.versoSalvato = this.sort.direction;
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

//	openCronologia(content: RicercaGregOutput) {
//		const modalRef = this.modalService.open(CronologiaComponent, { size: 'xl' });
//		modalRef.componentInstance.archive = true;
//		modalRef.componentInstance.idRendicontazione = content.idRendicontazioneEnte;
//	}

	openCronologia(content: RendicontazioneEnteGreg) {
    const modalRef = this.modalService.open(CronologiaComponent, { size: 'xl' });
    modalRef.componentInstance.archive = false;
    modalRef.componentInstance.idRendicontazione = content.idRendicontazioneEnte;
  }

	getProperty = (obj, path) => (
		path.split('.').reduce((o, p) => o && o[p], obj)
	)

	annullaRicerca() {
		this.searchForm.reset();
		this.denomEnteFilteredArc = [];
		this.dataListaRichiesteArchivio.data = [];
	}

	checkUncheckAll() {
		let c: number = 0;
    this.dataListaRichiesteArchivio.data.forEach(element=>{
      if(element.checked){
        c++;
      }
    })
    if(c<this.dataListaRichiesteArchivio.data.length){
      this.dataListaRichiesteArchivio.data.forEach(element=>{
        element.checked=true;
      })
    } else {
      this.dataListaRichiesteArchivio.data.forEach(element=>{
        element.checked=!element.checked;
      })
    }
	}

	openStorico(content: RicercaGregOutput) {
		this.client.paginaSalvata = this.paginator.pageIndex
		this.client.ordinamentoSalvato = this.sort.active;
		this.client.versoSalvato = this.sort.direction;
		const modalRef = this.modalService.open(StoricoArchivioComponent, { size: 'lg' });
		modalRef.componentInstance.archive = false;
		modalRef.componentInstance.idSchedaEnteGestore = content.idSchedaEnteGestore;
		modalRef.componentInstance.denominazione = content.denominazione;
		modalRef.componentInstance.statoEnte = content.statoEnte.codStatoEnte;
	}


	routeToDatiEnte(rend: RendicontazioneEnteGreg, path: string) {
		this.client.paginaSalvata = this.paginator.pageIndex
		this.client.ordinamentoSalvato = this.sort.active;
		this.client.versoSalvato = this.sort.direction;
		this.client.getDatiEnteRendicontazione(rend.idRendicontazioneEnte, rend.annoEsercizio).subscribe((response: DatiEnteGreg) => {
			this.datiEnteArchivio = response as any;
			const navigationExtras: NavigationExtras = {
				relativeTo: this.route,
				skipLocationChange: true,
				state: {
					rendicontazione: rend,
					datiEnte: this.datiEnteArchivio
				}
			};
			this.router.navigate([path], navigationExtras);
		});
	}

	onSelectionAnnoChanged(value: string) {
		if (value != '') {
			this.client.spinEmitter.emit(true);
			this.searchForm.get('comuneArc').reset();
			this.client.getListaComuni(value + '-01-01').subscribe(listaComuni => {
				this.client.listaComuniSalvatoArchivio = listaComuni;
				this.client.spinEmitter.emit(false);
			});
		}
		else {
			this.client.listaComuniSalvatoArchivio = this.client.listaComuniinitialArchivio;
		}
	}

	checkSelezione(){
		let c: number = 0;
		this.dataListaRichiesteArchivio.data.forEach(element=>{
		  if(element.checked){
			c++;
		  }
		})
		if(c==this.dataListaRichiesteArchivio.data.length){
		  this.selezionaTutto=true;
		}else {
		  this.selezionaTutto=false;
		}
	  }

	controlloRiapriSelezione(){
		let c: number = 0;
		let trovato: boolean = true;
		  this.dataListaRichiesteArchivio.data.forEach(element=>{
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
		  if(this.statoSelezionato!='CON'){
			trovato = false;
		  }
		
		  return trovato;
		}
	  
		ripristinaRendicontazione(){
		 this.client.spinEmitter.emit(true);
		  let enti: RicercaGregOutput[]=[];
		  let enteSelezionato: string = '<ul>';
		  this.dataListaRichiesteArchivio.data.forEach(element =>{
			if(element.checked){
				element.profilo = this.client.profilo;
			  enti.push(element);
			  enteSelezionato += '<li>' + element.denominazione + '; </li>';
			}
		  })
		  enteSelezionato += '</ul>' 
		  
		  this.client.getMsgInformativi(SECTION.MESSAGGIORIAPRIMULTIPLO).subscribe((t)=>{
				  let messaggio = t[0].testoMsgInformativo.replace("ENTE", enteSelezionato).replace("ANNO", this.annoSelezionato.toString());
				  const dialogRef = this.dialog.open(OpCambioStatoPopupComponent, {
					  width: '650px',
					  disableClose: true,
					  autoFocus: true,
					  data: { titolo: 'Conferma Riapertura FNPS', messaggio: messaggio, operazione: true }
				  });
				  this.client.spinEmitter.emit(false);
				  dialogRef.afterClosed().subscribe(r => {
					  if (r) {
						this.client.spinEmitter.emit(true);
						enti.forEach(e => {
							e.notaEnte = r.notaEnte;
							e.notaInterna = r.notaInterna;
						  });
						  this.selezionaTutto = false;
						  this.client.ripristinaRendicontazione(enti).subscribe((r) => {
								this.avviaRicerca();
								this.client.listaStatiSalvato = [];
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
	  

}
