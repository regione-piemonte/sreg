import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { LinkModelli } from '@greg-app/app/dto/LinkModelli';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { RENDICONTAZIONE_STATUS } from '@greg-app/constants/greg-constants';

@Component({
	selector: 'app-container-responsabile-archivio',
	templateUrl: './container-responsabile-archivio.component.html',
	styleUrls: ['./container-responsabile-archivio.component.css']
})
export class ContainerResponsabileArchivioComponent implements OnInit {

	navigation: Navigation;
	disableButton: boolean;
	datiEnte: DatiEnteGreg;
	rendicontazione: RendicontazioneEnteGreg;
	//modelli: ModelTabTranche[];
	modelli: LinkModelli[];
	dettaglioPrestazione: boolean;
	model: string;

	constructor(private router: Router, private route: ActivatedRoute, public client: GregBOClient) {
		this.navigation = this.router.getCurrentNavigation();
		let enteValues: string[] = [];
		this.route.fragment.subscribe((frag: string) => {
			enteValues.push(frag);
		});
		this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
		this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : enteValues[0][1];
		this.dettaglioPrestazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.dettaglioPrestazione : enteValues[0][1];
		this.model = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.model : null;
	}

	ngOnInit(): void {
		//this.client.getModelliassociati( this.rendicontazione.idRendicontazioneEnte).
		this.client.getModelliPerTranche(this.rendicontazione.idRendicontazioneEnte, this.client.profilo.codProfilo).
			subscribe(modelli => {
				this.modelli = modelli;
				this.redirecttab(this.client.azioni);
			});
		//    if (this.client.listaenti.length>1) {
		//       this.disableButton = true;
		//     }

	}

	redirecttab(azioni: Map<string, boolean[]>) {
		const navigationExtras: NavigationExtras = {
			// relativeTo: this.route, 
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				datiEnte: this.datiEnte,
				model: this.model
			}
		};
		if (this.model == 'all-d-archivio') {
			this.router.navigate(['../../responsabile-ente-archivio/all-d-archivio'], navigationExtras);
		} else if (this.model) {
			this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/' + this.model], navigationExtras);
		}
		else if (azioni.get("DatiEnteTab")[1])
			this.router.navigate(['../../responsabile-ente-archivio'], navigationExtras);
		else if (azioni.get("DatiRendicontazioneTab")[1]) {
			if (this.modelli.find(modello => modello.sigla == 'A'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio'], navigationExtras);
			else if (
				// this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO' && 
				this.modelli.find(modello => modello.sigla == 'A1'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-a1-archivio'], navigationExtras);
			else if (
				// this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO' && 
				this.modelli.find(modello => modello.sigla == 'A2'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-a2-archivio'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'D'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-d-archivio'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'MA')
				&& this.rendicontazione.statoRendicontazione.codStatoRendicontazione != RENDICONTAZIONE_STATUS.DA_COMPILARE_I)
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/macroaggregati-archivio'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'B1'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-b1-archivio'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'B'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-b-archivio'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'C'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-c-archivio'], navigationExtras);
			else if (
				// this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO' && 
				this.modelli.find(modello => modello.sigla == 'E'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-e-archivio'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'F'))
				this.router.navigate(['../../responsabile-ente-archivio/dati-rendicontazione-archivio/modello-f-archivio'], navigationExtras);
		}
		else if (azioni.get("ModelloFNPS")[1])
			this.router.navigate(['../../responsabile-ente-archivio/all-d-archivio'], navigationExtras);
		else
			this.router.navigate(['/redirect-page'], { skipLocationChange: true });
	}

	backButton() {
		this.client.inviaIFatto = false;
		this.client.inviaIIFatto = false;
		// this.client.redirecttab(this.client.azioni, false, false);
		this.router.navigate(["/operatore-regionale/archivio-dati-rendicontazione"], { relativeTo: this.route, skipLocationChange: true });
	}

	backDatiEnte(path: string) {
		this.client.dettaglioPrestazione = false;
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

	backCruscotto(path: string) {
		this.client.cruscotto = false;
		this.client.redirecttab(this.client.azioni, true, false);
	}


}
