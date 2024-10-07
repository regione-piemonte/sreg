/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserInfoGreg } from '@greg-app/app/dto/UserInfoGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { PROJECT_CONSTANTS} from '../../constants/greg-constants';
import { GregError } from '../error/greg-error.model';
import { GregErrorService } from '../error/greg-error.service';
import { environment } from '@greg-app/environments/environment';

@Component({
  selector: 'app-backoffice-toolbar',
  templateUrl: './backoffice-toolbar.component.html',
  styleUrls: ['./backoffice-toolbar.component.css']
})
export class BackofficeToolbarComponent implements OnInit {

  titlePage: String = PROJECT_CONSTANTS.BACKOFFICE_TITLE;
  userLogged: UserInfoGreg;

   constructor(public client: GregBOClient, private router: Router, private gregError: GregErrorService, private route: ActivatedRoute) { }

  ngOnInit() {
    /* this.client.login().subscribe(
      (response: UserInfoGreg ) => {
          this.userLogged = response;
          if (response.ruolo == ROLES.OPERATORE_REGIONALE || response.ruolo == ROLES.SUPERUSER){
            this.client.ruolo = response.ruolo;
            this.router.navigate( ['operatore-regionale'], { skipLocationChange: true } );
          }
          else  if (response.ruolo == ROLES.RESPONSABILE_ENTE){
            this.client.ruolo = response.ruolo;
            //prendo l'ente dalla map
            let map = new Map(Object.entries(this.userLogged.enteprofilo));
            for (let entry of map.entries()) {
                  this.router.navigate( ['responsabile-ente'], { skipLocationChange: true, state: { idEnte: entry[0]} } );
          }
          }
          else  if (response.ruolo == ROLES.RESPONSABILE_MULTIENTE){
            this.client.ruolo = response.ruolo;
            this.client.listaenti = this.userLogged.enteprofilo;
            this.router.navigate( ['responsabile-multiente'], { skipLocationChange: true, state: { idEnte: this.client.listaenti} } );
          }
          else{
            this.gregError.handleError(GregError.toGregError({ ...Error, errorDesc : 'Errore nel ruolo' }))
          }
      },
      err => {}
    )*/
      
     this.client.selectprofiloazione().subscribe(
      (response: UserInfoGreg ) => {
          this.userLogged = response;
		  this.client.verificaprofilo(response);
      },
      err => {}
    )
  }

  goToPaginaPersonale() {
    window.open(environment.profilo, "_blank");
  }

  goLogout() {
	 this.client.logout().subscribe(
      (response: UserInfoGreg ) => {
	  window.open(environment.esci, "_self");
	 },
      err => {}
  )
  }


}
