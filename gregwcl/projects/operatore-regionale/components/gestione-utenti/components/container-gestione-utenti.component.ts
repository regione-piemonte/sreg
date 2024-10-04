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
  selector: 'app-container-gestione-utenti',
  templateUrl: './container-gestione-utenti.component.html',
  styleUrls: ['./container-gestione-utenti.component.css']
})
export class ContainerGestioneUtentiComponent implements OnInit {

  navigation: Navigation;
  disableButton: boolean;
  datiEnte: DatiEnteGreg;
  rendicontazione:RendicontazioneEnteGreg;
  //modelli: ModelTabTranche[];
  modelli: LinkModelli[];
  dettaglioPrestazione: boolean;
	model: string;

  constructor(private router: Router, private route: ActivatedRoute, public client: GregBOClient) { 
    this.navigation = this.router.getCurrentNavigation();
	let enteValues: string[] = [];
	this.route.fragment.subscribe( (frag: string) => {
	  enteValues.push(frag);
	});
}

  ngOnInit(): void {


  }



}
