/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { ParametroGreg } from '@greg-app/app/dto/ParametroGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { MENU } from '@greg-app/constants/greg-constants';
import { environment } from '@greg-app/environments/environment';


@Component({
  selector: 'app-project-toolbar',
  templateUrl: './project-toolbar.component.html',
  styleUrls: ['./project-toolbar.component.css']
})
export class ProjectToolbarComponent implements OnInit {


  projectTitle: String = 'Rendicontazione Enti Gestori';
  parametriperhelp:	ParametroGreg[];
  constructor(private client: GregBOClient) { }

  ngOnInit() {
      this.client.getParametroPerInformativa(MENU.HELP).subscribe((msg: ParametroGreg[]) => {
        this.parametriperhelp = msg;
      });
  }

  goToParametro(url:string){
    window.open(url, "_blank");
  }

}
