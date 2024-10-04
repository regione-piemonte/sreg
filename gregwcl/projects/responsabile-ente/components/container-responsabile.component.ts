import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { LinkModelli } from '@greg-app/app/dto/LinkModelli';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { RENDICONTAZIONE_STATUS } from '@greg-app/constants/greg-constants';
import { ModelloAllDComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloAllD/modelloAllD.component';

@Component({
	selector: 'app-container-responsabile',
	templateUrl: './container-responsabile.component.html',
	styleUrls: ['./container-responsabile.component.css']
})
export class ContainerResponsabileComponent implements OnInit {

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
		if (this.model == 'all-d') {
			this.router.navigate(['../../responsabile-ente/all-d'], navigationExtras);
		} else if (this.model == 'al-zero') {
			this.router.navigate(['../../responsabile-ente/al-zero'], navigationExtras);
		} else if (this.model) {
			this.router.navigate(['../../responsabile-ente/dati-rendicontazione/' + this.model], navigationExtras);
		}

		else if (azioni.get("DatiEnteTab")[1])
			this.router.navigate(['../../responsabile-ente'], navigationExtras);
		else if (azioni.get("DatiRendicontazioneTab")[1]) {
			if (this.modelli.find(modello => modello.sigla == 'A'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione'], navigationExtras);
			else if (
				// this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO' && 
				this.modelli.find(modello => modello.sigla == 'A1'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-a1'], navigationExtras);
			else if (
				// this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO' && 
				this.modelli.find(modello => modello.sigla == 'A2'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-a2'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'D'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-d'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'MA')
				&& this.rendicontazione.statoRendicontazione.codStatoRendicontazione != RENDICONTAZIONE_STATUS.DA_COMPILARE_I)
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/macroaggregati'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'B1'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-b1'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'B'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-b'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'C'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-c'], navigationExtras);
			else if (
				// this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO' && 
				this.modelli.find(modello => modello.sigla == 'E'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-e'], navigationExtras);
			else if (this.modelli.find(modello => modello.sigla == 'F'))
				this.router.navigate(['../../responsabile-ente/dati-rendicontazione/modello-f'], navigationExtras);
		}
		else if (azioni.get("ModelloFNPS")[1])
			this.router.navigate(['../../responsabile-ente/all-d'], navigationExtras);
		else if (azioni.get("ModelloAlZero")[1])
			this.router.navigate(['../../responsabile-ente/al-zero'], navigationExtras);
		else
			this.router.navigate(['/redirect-page'], { skipLocationChange: true });
	}

	backButton() {
		this.client.inviaIFatto = false;
		this.client.inviaIIFatto = false;
		this.client.readOnlyIII = false;
		this.client.redirecttab(this.client.azioni, false, false);
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
