import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { CronologiaProfilo } from '@greg-app/app/dto/CronologiaProfilo';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { GenericResponseGreg } from '@greg-app/app/dto/GenericResponseGreg';
import { GenericResponseWarnErrGreg } from '@greg-app/app/dto/GenericResponseWarnErrGreg';
import { InviaModelli } from '@greg-app/app/dto/InviaModelli';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { RicercaGregOutput } from '@greg-app/app/dto/RicercaGregOutput';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { OPERAZIONE, SECTION, TRANCHE } from '@greg-app/constants/greg-constants';
import { RENDICONTAZIONE_STATUS } from '@greg-app/constants/greg-constants';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { CancPopupComponent } from '@greg-operatore/components/configuratore-prestazioni/canc-popup/canc-popup.component';
import { OpCambioStatoPopupComponent } from '@greg-operatore/components/op-cambio-stato-popup/op-cambio-stato-popup.component';
import { CronologiaModelliComponent } from '@greg-shared/cronologia-modelli/cronologia-modelli.component';
import { MessaggioPopupComponent } from '@greg-shared/dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';

@Component({
	selector: 'app-pulsanti-funzioni',
	templateUrl: './pulsanti-funzioni.component.html',
	styleUrls: ['./pulsanti-funzioni.component.css']
})

export class PulsantiFunzioniComponent implements OnInit {

	navigation: Navigation;
	//idEnte: any;
	ruolo: string;
	ente: EnteGreg;
	@Output() salvaEvent = new EventEmitter<string>();
	@Output() validaAlZeroEvent = new EventEmitter<string>();
	@Output() annullaValidazioneAlZeroEvent = new EventEmitter<string>();
	@Output() checkEvent = new EventEmitter<string>();
	@Input() mostrasalva: boolean;
	@Input() idRendicontazioneEnte;
	@Input() cronologiaMod;
	@Input() cronologia: string;
	@Input() modello: string;
	warnings: string[] = [];
	errors: string[] = [];
	// ruoloente: string;
	messaggio: string;
	titolo: string;
	esito: string;
	tranche: string;
	errorMessage = {
		error: { descrizione: '' },
		message: '',
		name: '',
		status: '',
		statusText: '',
		url: '',
		date: Date
	}
	datiEnte: DatiEnteGreg;
	rendicontazione: RendicontazioneEnteGreg;
	tabtranche: ModelTabTranche;
	obblMotivazione: boolean;

	// @ViewChild(CronologiaModelliComponent, { static: false }) cronologiaMod: CronologiaModelliComponent;
	warningCheck: string;
	messaggioPopup: any;

	constructor(private router: Router, private route: ActivatedRoute, private gregError: GregErrorService,
		public client: GregBOClient, public toastService: AppToastService, private dialog: MatDialog) {
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

	}

	salvaModifiche() {
		this.client.operazione = OPERAZIONE.SALVA;
		this.client.ricercaEnte = [];
		this.client.ricercaEnteCruscotto = [];
		this.salvaEvent.emit(null);
	}

	validaAlZero() {
		this.client.operazione = OPERAZIONE.VALIDAZIONE_AL_ZERO;
		this.client.ricercaEnte = [];
		this.client.ricercaEnteCruscotto = [];
		this.salvaEvent.emit(null);
		// this.validaAlZeroEvent.emit(null);
	}
	
	annullaValidazioneAlZero() {
		this.client.operazione = OPERAZIONE.ANNULLA_VALIDAZIONE_AL_ZERO;
		this.client.ricercaEnte = [];
		this.client.ricercaEnteCruscotto = [];
		this.annullaValidazioneAlZeroEvent.emit(null);
	}

	check() {
		this.client.operazione = OPERAZIONE.CHECK;
		this.client.ricercaEnte = [];
		this.client.ricercaEnteCruscotto = [];
		this.salvaEvent.emit(null);
	}

	concludi() {
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
			this.client.spinEmitter.emit(false);
			return;
		} else {
			this.client.spinEmitter.emit(true);
			this.client.ricercaEnte = [];
			this.client.ricercaEnteCruscotto = [];
			let enti: RicercaGregOutput[] = [];
			let rend: RendicontazioneEnteGreg[] = [];
			rend.push(this.rendicontazione);
			let e: RicercaGregOutput = new RicercaGregOutput(null, null, null, this.datiEnte.denominazione, null, null,
				rend, null, null, null, null, null);
				e.notaEnte = this.cronologiaMod.cronologia.notaEnte;
				e.notaInterna = this.cronologiaMod.cronologia.notaInterna;
				e.profilo = this.client.profilo;
			enti.push(e);
			this.client.getMsgInformativi(SECTION.MESSAGGIOCONCLUDISINGOLO).subscribe((t) => {
				let messaggio = t[0].testoMsgInformativo.replace("ANNO", this.rendicontazione.annoEsercizio.toString()).replace("ENTE", this.datiEnte.denominazione);
				const dialogRef = this.dialog.open(OpCambioStatoPopupComponent, {
					width: '650px',
					disableClose: true,
					autoFocus: true,
					data: { titolo: 'Conferma Conclusione Rendicontazione', messaggio: messaggio, operazione: false }
				});
				this.client.spinEmitter.emit(false);
				dialogRef.afterClosed().subscribe(r => {
					if (r) {
						this.client.spinEmitter.emit(true);
						this.client.concludiRendicontazione(enti).subscribe((r) => {
							this.rendicontazione.statoRendicontazione.codStatoRendicontazione = RENDICONTAZIONE_STATUS.CONCLUSA;
							this.abilitapulsanti();
							this.cronologiaMod.ngOnInit();
                                this.cronologiaMod.espansa=true;
                                this.cronologiaMod.state='rotated';
                                this.cronologiaMod.apricronologia();
                                this.cronologiaMod.cronologia.notaEnte = null;
                                this.cronologiaMod.cronologia.notaInterna = null;
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

	riapri() {
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.cronologiaMod.cronologia.notaEnte == null || this.cronologiaMod.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
			this.client.spinEmitter.emit(false);
			return;
		} else {
			this.client.spinEmitter.emit(true);
			this.client.ricercaEnte = [];
			this.client.ricercaEnteCruscotto = [];
			let enti: RicercaGregOutput[] = [];
			let rend: RendicontazioneEnteGreg[] = [];
			rend.push(this.rendicontazione);
			let e: RicercaGregOutput = new RicercaGregOutput(null, null, null, this.datiEnte.denominazione, null, null,
				rend, null, null, null, null, null);
				e.notaEnte = this.cronologiaMod.cronologia.notaEnte;
				e.notaInterna = this.cronologiaMod.cronologia.notaInterna;
				e.profilo = this.client.profilo;
			enti.push(e);
			this.client.getMsgInformativi(SECTION.MESSAGGIORIAPRICOMPILAZIONESINGOLO).subscribe((t) => {
				let messaggio = t[0].testoMsgInformativo.replace("ANNO", this.rendicontazione.annoEsercizio.toString()).replace("ENTE", this.datiEnte.denominazione);
				const dialogRef = this.dialog.open(OpCambioStatoPopupComponent, {
					width: '650px',
					disableClose: true,
					autoFocus: true,
					data: { titolo: 'Conferma Riapertura Rendicontazione', messaggio: messaggio, operazione: false }
				});
				this.client.spinEmitter.emit(false);
				dialogRef.afterClosed().subscribe(r => {
					if (r) {
						this.client.spinEmitter.emit(true);
						this.client.ripristinaCompilazione(enti).subscribe((r) => {
							this.rendicontazione.statoRendicontazione.codStatoRendicontazione = RENDICONTAZIONE_STATUS.IN_RIESAME_II;
							this.abilitaPulsantiFromModello(TRANCHE.III_TRANCHE);
							this.cronologiaMod.ngOnInit();
                                this.cronologiaMod.espansa=true;
                                this.cronologiaMod.state='rotated';
                                this.cronologiaMod.apricronologia();
                                this.cronologiaMod.cronologia.notaEnte = null;
                                this.cronologiaMod.cronologia.notaInterna = null;
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

	abilitaPulsantiExternal(modello: string) {

		forkJoin({
			ente: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
			utente: this.client.utente,
			tranche: this.client.getTranchePerModello(this.rendicontazione.idRendicontazioneEnte, modello)
		})
			.subscribe(({ ente, utente, tranche }) => {
				this.rendicontazione.statoRendicontazione.codStatoRendicontazione = ente.statoRendicontazione.codStatoRendicontazione;
				this.abilitapulsanti();
				if (modello == 'MFNPS') {
					this.tabtranche = new ModelTabTranche();
					this.tabtranche.codTranche = TRANCHE.III_TRANCHE;
				} else {
					this.tabtranche = tranche;
				}

				if (this.tabtranche.codTranche == TRANCHE.I_TRANCHE) {
					this.client.SalvaModelliII = false;
					this.client.InviaII = false;
					this.client.ConfermaDatiII = false;
					this.client.RichiestaRettificaII = false;
					this.client.SalvaModelliIII = false;
					this.client.Concludi = false;
					this.client.RiapriRendicontazione = false;
				}
				else if (this.tabtranche.codTranche == TRANCHE.II_TRANCHE) {
					this.client.SalvaModelliI = false;
					this.client.InviaI = false;
					this.client.ConfermaDatiI = false;
					this.client.RichiestaRettificaI = false;
					this.client.SalvaModelliIII = false;
					this.client.Concludi = false;
					this.client.RiapriRendicontazione = false;
				}
				else if (this.tabtranche.codTranche == TRANCHE.III_TRANCHE) {
					this.client.SalvaModelliIII = true && this.client.azioni.get("SalvaModelliIII")[1];
					this.client.SalvaModelliI = false;
					this.client.InviaI = false;
					this.client.ConfermaDatiI = false;
					this.client.RichiestaRettificaI = false;
					this.client.SalvaModelliII = false;
					this.client.InviaII = false;
					this.client.ConfermaDatiII = false;
					this.client.RichiestaRettificaII = false;
					this.client.SalvaModelliI = false;
					this.client.InviaI = false;
					this.client.ConfermaDatiI = false;
					this.client.RichiestaRettificaI = false;
					this.client.Valida = false;
					this.client.Storicizza = false;
				}
			})
	}

	abilitaPulsantiFromModello(codTranche: string) {

		forkJoin({
			ente: this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte),
			utente: this.client.utente
		})
			.subscribe(({ ente, utente }) => {
				this.rendicontazione.statoRendicontazione.codStatoRendicontazione = ente.statoRendicontazione.codStatoRendicontazione;
				this.abilitapulsanti();
				if (codTranche == TRANCHE.I_TRANCHE) {
					this.client.SalvaModelliII = false;
					this.client.InviaII = false;
					this.client.ConfermaDatiII = false;
					this.client.RichiestaRettificaII = false;
					this.client.SalvaModelliIII = false;
					this.client.Concludi = false;
					this.client.RiapriRendicontazione = false;
				}
				else if (codTranche == TRANCHE.II_TRANCHE) {
					this.client.SalvaModelliI = false;
					this.client.InviaI = false;
					this.client.ConfermaDatiI = false;
					this.client.RichiestaRettificaI = false;
					this.client.SalvaModelliIII = false;
					this.client.Concludi = false;
					this.client.RiapriRendicontazione = false;
				} else if (codTranche == TRANCHE.III_TRANCHE) {
					this.client.SalvaModelliIII = this.client.azioni.get("SalvaModelliIII")[1];
					this.client.SalvaModelliII = false;
					this.client.InviaII = false;
					this.client.ConfermaDatiII = false;
					this.client.RichiestaRettificaII = false;
					this.client.SalvaModelliI = false;
					this.client.InviaI = false;
					this.client.ConfermaDatiI = false;
					this.client.RichiestaRettificaI = false;
					this.client.Valida = false;
					this.client.Storicizza = false;
				}
			})
	}


	abilitapulsanti() {
		if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione != null) {
			//manca controllo disabilita campi tra tranche 1 e 2 e bottone conferma tra moduli di tranche differente
			if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.DA_COMPILARE_I) {
				this.client.SalvaModelliI = this.client.azioni.get("SalvaModelliI")[1];
				this.client.SalvaModelliII = false;
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = false;
				this.client.readOnlyII = true;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_I) {
				this.client.SalvaModelliI = this.client.azioni.get("SalvaModelliI")[1];
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = this.client.azioni.get("InviaI")[1];
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = false;
				this.client.readOnlyII = false;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = this.client.inviaIFatto && this.client.azioni.get("CheckI")[1];
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.IN_RIESAME_I) {
				this.client.SalvaModelliI = this.client.azioni.get("SalvaModelliI")[1] && this.client.azioni.get("SalvaInRiesameI")[1];
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1];
				this.client.ConfermaDatiI = this.client.azioni.get("ConfermaDatiI")[1];
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = this.client.azioni.get("RichiestaRettificaI")[1];;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				//se sei abilitato a salvainriesame1 vuol dire che puoi modificare i modelli e devi vedere il pulsante salva 
				this.client.readOnly = !this.client.azioni.get("SalvaInRiesameI")[1];
				this.client.readOnlyII = false;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.ATTESA_RETTIFICA_I) {
				this.client.SalvaModelliI = this.client.azioni.get("SalvaModelliI")[1];
				this.client.SalvaModelliII = false;
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = false;
				this.client.readOnlyII = true;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = this.client.inviaIFatto && this.client.azioni.get("CheckI")[1];
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.DA_COMPILARE_II) {
				this.client.SalvaModelliI = this.client.azioni.get("SalvaModelliI")[1] && this.client.azioni.get("SalvaDaCompilareII")[1];
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				//se ente true false
				this.client.readOnly = !this.client.azioni.get("SalvaDaCompilareII")[1];
				this.client.readOnlyII = false;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_II) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = this.client.azioni.get("InviaII")[1];
				this.client.RichiestaRettificaI = this.client.azioni.get("RichiestaRettificaI")[1];
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = true;
				this.client.readOnlyII = false;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = this.client.inviaIIFatto && this.client.azioni.get("CheckII")[1];
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.IN_RIESAME_II) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1] && this.client.azioni.get("SalvaInRiesameII")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = this.client.azioni.get("ConfermaDatiII")[1];
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = this.client.azioni.get("RichiestaRettificaI")[1];
				this.client.RichiestaRettificaII = this.client.azioni.get("RichiestaRettificaII")[1];
				this.client.Storicizza = false;
				this.client.Valida = false;
				//se ente true true
				this.client.readOnly = true;
				this.client.readOnlyII = !this.client.azioni.get("SalvaInRiesameII")[1];
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.ATTESA_RETTIFICA_II) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = true;
				this.client.readOnlyII = false;
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = this.client.inviaIIFatto && this.client.azioni.get("CheckII")[1];
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.ATTESA_VALIDAZIONE) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1] && this.client.azioni.get("SalvaInAttesaValidazione")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = this.client.azioni.get("Valida")[1];
				//se ente true true
				this.client.readOnly = true;
				this.client.readOnlyII = !this.client.azioni.get("SalvaInAttesaValidazione")[1];
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			else if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.VALIDATA) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = this.client.azioni.get("SalvaModelliII")[1] && this.client.azioni.get("SalvaValidata")[1];
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = this.client.azioni.get("Storicizza")[1];
				this.client.Valida = false;
				//se ente true true
				this.client.readOnly = true;
				this.client.readOnlyII = !this.client.azioni.get("SalvaValidata")[1];
				this.client.readOnlyIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.STORICIZZATA) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = false;
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = true;
				this.client.readOnlyII = true;
				this.client.readOnlyIII = false;
				this.client.Concludi = this.client.azioni.get("ConcludiRendicontazioneSingola")[1];
				this.client.RiapriRendicontazione = this.client.azioni.get("RiapriRendicontazione")[1];
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
			if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.CONCLUSA) {
				this.client.SalvaModelliI = false;
				this.client.SalvaModelliII = false;
				this.client.ConfermaDatiI = false;
				this.client.ConfermaDatiII = false;
				this.client.InviaI = false;
				this.client.InviaII = false;
				this.client.RichiestaRettificaI = false;
				this.client.RichiestaRettificaII = false;
				this.client.Storicizza = false;
				this.client.Valida = false;
				this.client.readOnly = true;
				this.client.readOnlyII = true;
				this.client.readOnlyIII = true;
				this.client.SalvaModelliIII = false;
				this.client.Concludi = false;
				this.client.RiapriRendicontazione = false;
				this.client.CheckI = false;
				this.client.CheckII = false;
				return;
			}
		}
		else {
			this.client.SalvaModelliI = false;
			this.client.SalvaModelliII = false;
			this.client.ConfermaDatiI = false;
			this.client.ConfermaDatiII = false;
			this.client.InviaI = false;
			this.client.InviaII = false;
			this.client.RichiestaRettificaI = false;
			this.client.RichiestaRettificaII = false;
			this.client.Storicizza = false;
			this.client.Valida = false;
			this.client.readOnly = false;
			this.client.readOnlyII = false;
			this.client.CheckI = false;
			this.client.CheckII = false;
			return;
		}
	}

	richiestaRettifica1() {
		//se cronologia regionale visibile e abilitata obbligo a mettere una nota
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			//if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
			&& (this.client.cronologia.notaEnte == null || this.client.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
		} else {
			this.client.spinEmitter.emit(true);
			let cronologiaprofilo = new CronologiaProfilo();
			cronologiaprofilo.cronologia = this.client.cronologia;
			cronologiaprofilo.profilo = this.client.profilo;
			cronologiaprofilo.modello = this.modello;
			this.client.richiestaRettifica1(this.rendicontazione.idRendicontazioneEnte, cronologiaprofilo)
				.subscribe(
					(data: GenericResponseWarnErrGreg) => {
						if (data.warnings && data.warnings.length > 0) {
							for (let warn of data.warnings) {
								this.errorMessage.error.descrizione = warn;
								this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
							}
						}
						if (data.errors && data.errors.length > 0) {
							for (let err of data.errors) {
								this.errorMessage.error.descrizione = err;
								this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
							}
						}
						this.abilitaPulsantiExternal(this.client.nomemodello);
						this.client.ricercaEnte = [];
						this.client.ricercaEnteCruscotto = [];
						document.dispatchEvent(this.client.updateCronoEmitter);
						this.client.spinEmitter.emit(false);
						this.toastService.showSuccess({ text: data.descrizione });
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
		}
	}

	confermaDati1() {
		//if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.client.cronologia.notaEnte == null || this.client.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
		} else {
			this.client.spinEmitter.emit(true);
			let cronologiaprofilo = new CronologiaProfilo();
			cronologiaprofilo.cronologia = this.client.cronologia;
			cronologiaprofilo.profilo = this.client.profilo;
			cronologiaprofilo.modello = this.modello;
			this.client.confermaDati1(this.rendicontazione.idRendicontazioneEnte, cronologiaprofilo)
				.subscribe(
					(data: GenericResponseWarnErrGreg) => {
						if (data.warnings && data.warnings.length > 0) {
							for (let warn of data.warnings) {
								this.errorMessage.error.descrizione = warn;
								this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
							}
						}
						if (data.errors && data.errors.length > 0) {
							for (let err of data.errors) {
								this.errorMessage.error.descrizione = err;
								this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
							}
						}
						this.abilitaPulsantiExternal(this.client.nomemodello);
						this.client.ricercaEnte = [];
						this.client.ricercaEnteCruscotto = [];
						document.dispatchEvent(this.client.updateCronoEmitter);
						this.client.spinEmitter.emit(false);
						this.toastService.showSuccess({ text: data.descrizione });
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
		}
	}

	richiestaRettifica2() {
		//if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.client.cronologia.notaEnte == null || this.client.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
		} else {
			this.client.spinEmitter.emit(true);
			let cronologiaprofilo = new CronologiaProfilo();
			cronologiaprofilo.cronologia = this.client.cronologia;
			cronologiaprofilo.profilo = this.client.profilo;
			cronologiaprofilo.modello = this.modello;
			this.client.richiestaRettifica2(this.rendicontazione.idRendicontazioneEnte, cronologiaprofilo)
				.subscribe(
					(data: GenericResponseWarnErrGreg) => {
						if (data.warnings && data.warnings.length > 0) {
							for (let warn of data.warnings) {
								this.errorMessage.error.descrizione = warn;
								this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
							}
						}
						if (data.errors && data.errors.length > 0) {
							for (let err of data.errors) {
								this.errorMessage.error.descrizione = err;
								this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
							}
						}
						this.abilitaPulsantiExternal(this.client.nomemodello);
						this.client.ricercaEnte = [];
						this.client.ricercaEnteCruscotto = [];
						document.dispatchEvent(this.client.updateCronoEmitter);
						this.client.spinEmitter.emit(false);
						this.toastService.showSuccess({ text: data.descrizione });
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
		}
	}

	confermaDati2() {
		//if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER) 
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.client.cronologia.notaEnte == null || this.client.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
		} else {
			this.client.spinEmitter.emit(true);
			let cronologiaprofilo = new CronologiaProfilo();
			cronologiaprofilo.cronologia = this.client.cronologia;
			cronologiaprofilo.profilo = this.client.profilo;
			cronologiaprofilo.modello = this.modello;
			this.client.confermaDati2(this.rendicontazione.idRendicontazioneEnte, cronologiaprofilo)
				.subscribe(
					(data: GenericResponseWarnErrGreg) => {
						if (data.warnings && data.warnings.length > 0) {
							for (let warn of data.warnings) {
								this.errorMessage.error.descrizione = warn;
								this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
							}
						}
						if (data.errors && data.errors.length > 0) {
							for (let err of data.errors) {
								this.errorMessage.error.descrizione = err;
								this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
							}
						}
						this.abilitaPulsantiExternal(this.client.nomemodello);
						this.client.ricercaEnte = [];
						this.client.ricercaEnteCruscotto = [];
						document.dispatchEvent(this.client.updateCronoEmitter);
						this.client.spinEmitter.emit(false);
						this.toastService.showSuccess({ text: data.descrizione });
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
		}
	}

	inviaI() {
		//chiama salva
		if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_I) {
			this.client.operazione = OPERAZIONE.INVIAMODELLI;
			this.client.modello =  this.modello;
			this.salvaEvent.emit(null);
		}
	}

	inviaII() {
		//chiama salva
		if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione === RENDICONTAZIONE_STATUS.IN_COMPILAZIONE_II) {
			this.client.operazione = OPERAZIONE.INVIAMODELLI;
			this.client.modello = this.modello;
			this.salvaEvent.emit(null);
		}
	}

	inviamodelli(idRendicontazioneEnte) {
		this.client.operazione = OPERAZIONE.INVIAMODELLI;
		// esito ok salva posso chiamare invia
		// this.client.spinEmitter.emit(true);
		this.rendicontazione.idRendicontazioneEnte = idRendicontazioneEnte;
		let inviaModelli: InviaModelli = new InviaModelli();
		inviaModelli.tranche = TRANCHE.I_TRANCHE;
		inviaModelli.idEnte = this.rendicontazione.idRendicontazioneEnte;
		inviaModelli.operazione = OPERAZIONE.INVIAMODELLI;
		inviaModelli.esito = null;
		inviaModelli.nota = null;
		inviaModelli.profilo = this.client.profilo;
		inviaModelli.tranchestesa = TRANCHE.PRIMA_TRANCHE;
		this.client.inviotranche(inviaModelli).subscribe(
			(data: GenericResponseWarnErrGreg) => {
				this.tranche = inviaModelli.tranchestesa;
				this.warnings = data.warnings;
				this.errors = data.errors;
				this.messaggio = data.descrizione;
				this.titolo = "INVIA";
				this.esito = data.id;
				this.obblMotivazione = data.obblMotivazione;
				this.warningCheck = data.warningCheck;
				if (this.warningCheck != null) {
					this.client.inviaIFatto = true;
					this.abilitaPulsantiExternal(this.client.nomemodello);
				}
				this.openDialog(this.client.modello);
				this.client.spinEmitter.emit(false);
			},
			err => {
				this.client.spinEmitter.emit(false);
			}
		)
	}

	valida() {
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.client.cronologia.notaEnte == null || this.client.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota" }))
		} else {
			this.client.spinEmitter.emit(true);
			let cronologiaprofilo = new CronologiaProfilo();
			cronologiaprofilo.cronologia = this.client.cronologia;
			cronologiaprofilo.profilo = this.client.profilo;
			cronologiaprofilo.modello = this.modello;
			this.client.valida(this.rendicontazione.idRendicontazioneEnte, cronologiaprofilo)
				.subscribe(
					(data: GenericResponseGreg) => {
						this.abilitaPulsantiExternal(this.client.nomemodello);
						this.client.ricercaEnte = [];
						this.client.ricercaEnteCruscotto = [];
						document.dispatchEvent(this.client.updateCronoEmitter);
						this.client.spinEmitter.emit(false);
						this.toastService.showSuccess({ text: data.descrizione });
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
		}
	}

	storicizza() {
		if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
			&& (this.client.cronologia.notaEnte == null || this.client.cronologia.notaEnte == "")) {
			this.errorMessage.error.descrizione = "Inserire una nota";
			this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota" }))
		} else {
			this.client.spinEmitter.emit(true);
			let cronologiaprofilo = new CronologiaProfilo();
			cronologiaprofilo.cronologia = this.client.cronologia;
			cronologiaprofilo.profilo = this.client.profilo;
			cronologiaprofilo.modello = this.modello;
			this.client.storicizza(this.rendicontazione.idRendicontazioneEnte, cronologiaprofilo)
				.subscribe(
					(data: GenericResponseGreg) => {
						this.abilitaPulsantiExternal(this.client.nomemodello);
						this.client.ricercaEnte = [];
						this.client.ricercaEnteCruscotto = [];
						document.dispatchEvent(this.client.updateCronoEmitter);
						this.client.spinEmitter.emit(false);
						this.toastService.showSuccess({ text: data.descrizione });
					},
					err => {
						this.client.spinEmitter.emit(false);
					}
				);
		}
	}


	openDialog(modello: string): void {
		const dialogRef = this.dialog.open(MessaggioPopupComponent, {
			width: '70%',
			disableClose: true,
			autoFocus: true,
			data: { tranche: this.tranche, titolo: this.titolo, warnings: this.warnings, errors: this.errors, messaggio: this.messaggio, esito: this.esito, nota: "", chiudi: null, obblMotivazione: this.obblMotivazione, warningCheck: this.warningCheck }
		});
		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				if (result.chiudi) {
					//devi scrivere solo la nota
					this.client.spinEmitter.emit(true);
					let inviaModelli: InviaModelli = new InviaModelli();
					inviaModelli.tranche = TRANCHE.I_TRANCHE;
					inviaModelli.profilo = this.client.profilo;
					inviaModelli.idEnte = this.rendicontazione.idRendicontazioneEnte;
					inviaModelli.operazione = OPERAZIONE.CONFERMAINVIAMODELLI;
					if (result.chiudi)
						inviaModelli.esito = 'OK';
					else
						inviaModelli.esito = 'KO';
					inviaModelli.nota = result.nota;
					inviaModelli.profilo = this.client.profilo;
					inviaModelli.modello = modello;
					inviaModelli.tranchestesa = TRANCHE.PRIMA_TRANCHE;
					this.client.inviotranche(inviaModelli).subscribe(
						(data: GenericResponseWarnErrGreg) => {
							if (data.warnings && data.warnings.length > 0) {
								for (let warn of data.warnings) {
									this.errorMessage.error.descrizione = warn;
									this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
								}
							}
							if (data.errors && data.errors.length > 0) {
								for (let err of data.errors) {
									this.errorMessage.error.descrizione = err;
									this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
								}
							}
							//this.ngOnInit();
							this.client.ricercaEnte = [];
							this.client.ricercaEnteCruscotto = [];
							this.abilitaPulsantiExternal(this.client.nomemodello);
							document.dispatchEvent(this.client.updateCronoEmitter);
							if (result.chiudi) {
								this.client.componinote = false;
							}
							if (data.descrizione != null)
								this.toastService.showSuccess({ text: data.descrizione });
							this.client.spinEmitter.emit(false);
						},
						err => {
							this.client.spinEmitter.emit(false);
						}
					)
				}
			}
		});
	}

	inviamodelliII(idRendicontazioneEnte) {
		this.client.operazione = OPERAZIONE.INVIAMODELLI;
		// esito ok salva posso chiamare invia
		// this.client.spinEmitter.emit(true);
		this.rendicontazione.idRendicontazioneEnte = idRendicontazioneEnte;
		let inviaModelli: InviaModelli = new InviaModelli();
		inviaModelli.tranche = TRANCHE.II_TRANCHE;
		inviaModelli.idEnte = this.rendicontazione.idRendicontazioneEnte;
		inviaModelli.operazione = OPERAZIONE.INVIAMODELLI;
		inviaModelli.esito = null;
		inviaModelli.nota = null;
		inviaModelli.profilo = this.client.profilo;
		inviaModelli.tranchestesa = TRANCHE.SECONDA_TRANCHE;
		this.client.inviotranche(inviaModelli).subscribe(
			(data: GenericResponseWarnErrGreg) => {
				this.warnings = data.warnings;
				this.errors = data.errors;
				this.messaggio = data.descrizione;
				this.titolo = "INVIA";
				this.esito = data.id;
				this.tranche = inviaModelli.tranchestesa;
				this.obblMotivazione = data.obblMotivazione;
				this.warningCheck = data.warningCheck;
				if (this.warningCheck != null) {
					this.client.inviaIIFatto = true;
					this.abilitaPulsantiExternal(this.client.nomemodello);
				}
				this.openDialogII(this.client.modello);
				this.client.spinEmitter.emit(false);
			},
			err => {
				this.client.spinEmitter.emit(false);
			}
		)
	}

	openDialogII(modello: string): void {
		const dialogRef = this.dialog.open(MessaggioPopupComponent, {
			width: '70%',
			disableClose: true,
			autoFocus: true,
			data: { tranche: this.tranche, titolo: this.titolo, warnings: this.warnings, errors: this.errors, messaggio: this.messaggio, esito: this.esito, nota: "", chiudi: null, obblMotivazione: this.obblMotivazione, warningCheck: this.warningCheck }
		});
		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				if (result.chiudi) {
					//devi scrivere solo la nota
					this.client.spinEmitter.emit(true);
					let inviaModelli: InviaModelli = new InviaModelli();
					inviaModelli.tranche = TRANCHE.II_TRANCHE;
					inviaModelli.profilo = this.client.profilo;
					inviaModelli.idEnte = this.rendicontazione.idRendicontazioneEnte;
					inviaModelli.operazione = OPERAZIONE.CONFERMAINVIAMODELLI;
					if (result.chiudi)
						inviaModelli.esito = 'OK';
					else
						inviaModelli.esito = 'KO';
					inviaModelli.nota = result.nota;
					inviaModelli.modello = modello;
					inviaModelli.tranchestesa = TRANCHE.SECONDA_TRANCHE;
					inviaModelli.profilo = this.client.profilo;
					this.client.inviotranche(inviaModelli).subscribe(
						(data: GenericResponseWarnErrGreg) => {
							if (data.warnings && data.warnings.length > 0) {
								for (let warn of data.warnings) {
									this.errorMessage.error.descrizione = warn;
									this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: warn }))
								}
							}
							if (data.errors && data.errors.length > 0) {
								for (let err of data.errors) {
									this.errorMessage.error.descrizione = err;
									this.gregError.handleError(GregError.toGregError({ ...this.errorMessage, errorDesc: err }))
								}
							}
							//this.ngOnInit();
							this.abilitaPulsantiExternal(this.client.nomemodello);
							this.client.ricercaEnte = [];
							this.client.ricercaEnteCruscotto = [];
							document.dispatchEvent(this.client.updateCronoEmitter);
							if (result.chiudi) {
								this.client.componinote = false;
							}
							if (data.descrizione != null)
								this.toastService.showSuccess({ text: data.descrizione });
							this.client.spinEmitter.emit(false);
						},
						err => {
							this.client.spinEmitter.emit(false);
						}
					)
				}
			}
		});
	}

	attivaCheckI() {
		if (this.client.nomemodello == "A" || this.client.nomemodello == "A1") {
			return true;
		}
		return false;
	}

	attivaCheckII() {
		if (this.client.nomemodello == "MA" || this.client.nomemodello == "B1"
			|| this.client.nomemodello == "B" || this.client.nomemodello == "C" || this.client.nomemodello == "F") {
			return true;
		}
		return false;
	}
}
