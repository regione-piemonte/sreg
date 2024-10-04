import { Component, Input, OnInit } from '@angular/core';
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { DettaglioPrestazione } from '@greg-app/app/dto/DettaglioPrestazione';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { PrestUtenza } from '@greg-app/app/dto/PrestUtenza';
import { Prest1Prest2 } from '@greg-app/app/dto/Prest1Prest2';

@Component({
	selector: 'app-dettaglio-prestazione-archivio',
	templateUrl: './dettaglio-prestazione-archivio.component.html',
	styleUrls: ['./dettaglio-prestazione-archivio.component.css']
})
export class DettaglioPrestazioneArchivioComponent implements OnInit {

	rendicontazione: RendicontazioneEnteGreg;
	codPrest: string;
	dettaglioPrestazione: DettaglioPrestazione;
	datiEnte: DatiEnteGreg;
	navigation: Navigation;
	prestazione: boolean = false;
	notaPrestazione: boolean = false;
	prestazioneMinisteriale: boolean = false;

	constructor(public client: GregBOClient, private route: ActivatedRoute, private router: Router) {
		this.navigation = this.router.getCurrentNavigation();
		let enteValues: string[] = [];
		this.route.fragment.subscribe((frag: string) => {
			enteValues.push(frag);
		});
		this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
		this.codPrest = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.codPrestazione : enteValues[0][1];
	}

	ngOnInit() {
		this.client.spinEmitter.emit(true);
		this.client.getDettaglioPrestazione(this.codPrest, this.rendicontazione.annoEsercizio).subscribe((res) => {
			this.dettaglioPrestazione = res;
			for (let i = 0; i < this.dettaglioPrestazione.prest1Prest2.length; i++) {
				this.dettaglioPrestazione.prest1Prest2[i].prest2 = false;
				this.dettaglioPrestazione.prest1Prest2[i].x = 10;
			}
			this.client.spinEmitter.emit(false);
		}, err => {
			this.client.spinEmitter.emit(false);
		});
	}

	returnDatiEnte(path: string) {
		this.client.getDatiEnteRendicontazione(this.rendicontazione.idRendicontazioneEnte, this.rendicontazione.annoEsercizio).subscribe((response: DatiEnteGreg) => {
			this.datiEnte = response as any;
			const navigationExtras: NavigationExtras = {
				relativeTo: this.route,
				skipLocationChange: true,
				state: {
					rendicontazione: this.rendicontazione,
					datiEnte: this.datiEnte
				}
			};
			this.router.navigate([path], navigationExtras);
		});
	}

	controlloLunghezza(prest2: Prest1Prest2) {
		if (prest2.utenze.length >= (prest2.utenzeMin.length + 1)) {
			return true;
		}
		return false;
	}

	changePrestazione() {
		this.prestazione = !this.prestazione;
	}

	changeNotaPrestazione() {
		this.notaPrestazione = !this.notaPrestazione;
	}

	changePrestazioneMinisteriale() {
		this.prestazioneMinisteriale = !this.prestazioneMinisteriale;
		for (let i = 0; i < this.dettaglioPrestazione.prest1Prest2.length; i++) {
			this.dettaglioPrestazione.prest1Prest2[i].x = 10;
		}
	}

	getColoreMissioneProgramma(utenza: PrestUtenza, i: number) {
		let styles = {
			'border-top': '5px solid ' + utenza.coloreMissioneProgramma,
			'opacity': '1'
		}

		return styles;
	}

	changePrestazione2(j: number) {
		for (let i = 0; i < this.dettaglioPrestazione.prest1Prest2.length; i++) {
			if (i == j) {
				this.dettaglioPrestazione.prest1Prest2[i].prest2 = !this.dettaglioPrestazione.prest1Prest2[i].prest2;
			}
			this.dettaglioPrestazione.prest1Prest2[i].x = 10;
		}
	}

	spostamento(prest2: Prest1Prest2, i: number) {
		if (i > 0) {
			prest2.x += 10;
		}
		let styles = {
			'padding-left': prest2.x + 'px',
			'padding-top': '10px',
			'padding-bottom': '10px',
		}

		return styles;
	}

	vai(codPrest: string) {
		this.client.spinEmitter.emit(true);
		this.client.getDatiEnteRendicontazione(this.rendicontazione.idRendicontazioneEnte, this.rendicontazione.annoEsercizio).subscribe((response: DatiEnteGreg) => {
			this.datiEnte = response as any;
			let currentUrl = this.router.url;
			const navigationExtras: NavigationExtras = {
				relativeTo: this.route,
				skipLocationChange: true,
				state: {
					rendicontazione: this.rendicontazione,
					datiEnte: this.datiEnte,
					codPrestazione: codPrest
				}
			};
			this.router.navigateByUrl('/responsabile-ente', navigationExtras).then(() => {
				this.router.navigate([currentUrl], navigationExtras);
			});
		});
	}

	controlloNota(nota: string) {
		if (nota != " ") {
			return true;
		}
		return false;
	}

}
