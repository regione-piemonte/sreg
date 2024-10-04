import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';
import { LinkModelli } from '@greg-app/app/dto/LinkModelli';
import { ModelTabTranche } from '@greg-app/app/dto/ModelTabTranche';
import { MsgApplicativo } from '@greg-app/app/dto/MsgApplicativo';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ERRORS, RENDICONTAZIONE_STATUS, STATO_ENTE } from '@greg-app/constants/greg-constants';

@Component({
  selector: 'app-tab-gestione-utenti',
  templateUrl: './tab-gestione-utenti.component.html',
  styleUrls: ['./tab-gestione-utenti.component.css']
})
export class TabGestioneUtentiComponent implements OnInit {

  links: { title, link, active,fragment?,azione}[];
  navigation: Navigation;
  tooltip: string = '';
  show: boolean;
  selectedLink: any;
	visibleTab : boolean = false;
	visibleTabEnti: boolean = false;
	visibleTabRendicontazioni: boolean = false;
	visibleTabFNPS: boolean = false;
  changeTabEvent = new CustomEvent('changeTabEvent', { bubbles: true });   
  datiEnte: DatiEnteGreg;
// modelli: ModelTabTranche[];
modelli: LinkModelli[];

rendicontazione:RendicontazioneEnteGreg;
  model: string;
  constructor(public route: ActivatedRoute, private router: Router, private client: GregBOClient) {
    this.navigation = this.router.getCurrentNavigation();

    }

  ngOnInit(): void {
        this.links = [
          {
            title: 'Utenti', link: 'utenti', active: true, fragment: null ,azione:'GestioneUtenti'
          },
          {
            title: 'Profili', link: 'profili', active: true, fragment: null, azione:'GestioneUtenti'
          },
          {
            title: 'Liste', link: 'liste', active: true, fragment: null, azione:'GestioneUtenti'
          }
        ]
  }


}



