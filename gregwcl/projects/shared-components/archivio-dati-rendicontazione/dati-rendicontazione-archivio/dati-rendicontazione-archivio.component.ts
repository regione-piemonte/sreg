import { Component, HostListener, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ThemePalette } from '@angular/material/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { MSG, RENDICONTAZIONE_STATUS } from '@greg-app/constants/greg-constants';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { LinkModelli } from '@greg-app/app/dto/LinkModelli';

@Component({
	selector: 'app-dati-rendicontazione-archivio',
	templateUrl: './dati-rendicontazione-archivio.component.html',
	styleUrls: ['./dati-rendicontazione-archivio.component.css']
})
export class DatiRendicontazioneArchivioComponent implements OnInit {

	// links: { title, link, active, tooltip, fragment?,azione,sigla}[];

	background: ThemePalette = undefined;
	linkActive;
	navigation: Navigation;
	ente: EnteGreg;
	visible: boolean;
	changeTabEvent = new CustomEvent('changeTabEvent', { bubbles: true });
	selectedLink: any;
	msgPopup: string;
	msgTab: string;
	idRendicontazioneEnte;
	datiEnte: DatiEnteGreg;
	rendicontazione: RendicontazioneEnteGreg;
	modelli: ModelTabTranche[];
	links: LinkModelli[];
	model: string;

	constructor(private router: Router, public route: ActivatedRoute,
		public client: GregBOClient, public toastService: AppToastService,
		public dialog: MatDialog) {
		this.navigation = this.router.getCurrentNavigation();
		let enteValues: string[] = [];
		this.route.fragment.subscribe((frag: string) => {
			enteValues.push(frag);
		});
		this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
		this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : enteValues[0][1];
		this.idRendicontazioneEnte = this.rendicontazione.idRendicontazioneEnte;
		this.model = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.model : enteValues[0][2];
	}

	ngOnInit() {
		forkJoin({
			messaggioPopup: this.client.getMsgApplicativo(MSG.POPUP_MODELLI_RENDICONTAZIONE_EDIT),
			messaggioTab: this.client.getMsgApplicativo(MSG.WARN_TAB),
			//	  modelli: this.client.getModelliassociati( this.rendicontazione.idRendicontazioneEnte),
			modellitab: this.client.getModelliPerTranche(this.idRendicontazioneEnte, this.client.profilo.codProfilo)
		}).subscribe(({ messaggioPopup, messaggioTab, modellitab }) => {
			this.msgPopup = messaggioPopup.testoMsgApplicativo;
			this.msgTab = messaggioTab.testoMsgApplicativo;
			//  this.modelli = modelli;
			this.links = modellitab;
			this.aggiornaLinkArchivio();
			/*  this.links = [
				{
				  title: 'Mod A (I)', link: 'modello-a', active: true, tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloA',sigla: 'A'
		  
				},
				{
				  title: 'Mod A1 (I)', link: 'modello-a1', active: this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO', tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloA1',sigla: 'A1'
				},
				{
				  title: 'Mod A2 (I)', link: 'modello-a2', active: this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO',  tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloA2',sigla: 'A2'
				},
				{
				  title: 'Mod D (I)', link: 'modello-d', active: true,  tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloD',sigla: 'D'
				},
				{
				  title: 'Macroaggregati (II)', link: 'macroaggregati', active: this.rendicontazione.statoRendicontazione.codStatoRendicontazione != RENDICONTAZIONE_STATUS.DA_COMPILARE_I, tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloMacroaggregati',sigla: 'MA'
				},
				{
				  title: 'Mod B1 (II)', link: 'modello-b1', active: true,  tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloB1',sigla: 'B1'
				},
				{
				  title: 'Mod B (II)', link: 'modello-b', active: true, tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloB',sigla: 'B'
				},
				{
				  title: 'Mod C (II)', link: 'modello-c', active: true,  tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloC',sigla: 'C'
				},
				{
				  title: 'Mod E (II)', link: 'modello-e', active: this.datiEnte.codTipoEnte != 'COMUNE CAPOLUOGO', tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloE',sigla: 'E'
				},
				{
				  title: 'Mod F (II)', link: 'modello-f', active: true,  tooltip:'',fragment: [ this.datiEnte, this.idRendicontazioneEnte],azione: 'ModelloF',sigla: 'F'
				}
			  ]
		  */
			const navigationExtras: NavigationExtras = {
				relativeTo: this.route,
				skipLocationChange: true,
				state: {
					rendicontazione: this.rendicontazione,
					datiEnte: this.datiEnte
				}
			};
			let trovato = false;
			this.linkActive = this.links[0];
			if (this.model) {
				for (let link of this.links) {
					if (link.link == this.model) {
						this.linkActive = link;
						this.router.navigate([link.link], navigationExtras);
						trovato = true;
						break;
					}
				}
			}
			else {

				this.linkActive = this.links[0];
				for (let link of this.links) {
					if (!trovato) {
						this.linkActive = link;
						this.router.navigate([link.link], navigationExtras);
						trovato = true;
						break;
					}
				}
			}
			if (!trovato)
				this.router.navigate(['/redirect-page'], navigationExtras);
		});
	}

	onTabChange(link) {
		this.selectedLink = link;
		document.dispatchEvent(this.changeTabEvent);
	}

	//	  @HostListener('document:notSavedEvent')
	//	  openDialog(): void {
	//	      const dialogRef = this.dialog.open(ChangeTabDialog, {
	//	        width: '400px',
	//	        data: {messaggio: this.msgPopup}
	//	      });
	//	  
	//	      dialogRef.afterClosed().subscribe(result => {
	//	        if(result) this.changeTab();
	//	      });
	//	
	//	  }

	@HostListener('document:changeTabEmitter')
	changeTab() {
		this.linkActive = this.selectedLink;
		const navigationExtras: NavigationExtras = {
			relativeTo: this.route,
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				datiEnte: this.datiEnte
			}
		};
		this.router.navigate([this.selectedLink.link], navigationExtras);
	}

	callTooltip(i: number) {
		// if(!this.links[i].active){
		//   return this.links[i].tooltip;
		// } else {
		return '';
		// }
	}

	routeTo(path: string) {
		const navigationExtras: NavigationExtras = {
			relativeTo: this.route,
			skipLocationChange: true,
			state: {
				rendicontazione: this.rendicontazione,
				datiEnte: this.datiEnte
			}
		};
		this.router.navigate([path], navigationExtras);
	}

	aggiornaLinkArchivio() {
		if(this.links.length > 0){
			for (let i = 0; i < this.links.length; i++) {
			if (this.links[i].link == 'modello-a')
				this.links[i].link = 'modello-a-archivio';
			if(this.links[i].link == 'modello-d')
				this.links[i].link = 'modello-d-archivio';
			if(this.links[i].link == 'macroaggregati')
				this.links[i].link = 'macroaggregati-archivio';
			if(this.links[i].link == 'modello-b1')
				this.links[i].link = 'modello-b1-archivio';
			if(this.links[i].link == 'modello-b')
				this.links[i].link = 'modello-b-archivio';
			if(this.links[i].link == 'modello-c')
				this.links[i].link = 'modello-c-archivio';
			if(this.links[i].link == 'modello-f')
				this.links[i].link = 'modello-f-archivio';
			if(this.links[i].link == 'modello-a1')
				this.links[i].link = 'modello-a1-archivio';
			if(this.links[i].link == 'modello-a2')
				this.links[i].link = 'modello-a2-archivio';
			if(this.links[i].link == 'modello-e')
				this.links[i].link = 'modello-e-archivio';
		}
		}
		
	}

}
