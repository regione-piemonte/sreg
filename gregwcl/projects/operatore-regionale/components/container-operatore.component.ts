import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { UserInfoGreg } from '@greg-app/app/dto/UserInfoGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';

@Component({
  selector: 'app-container-operatore',
  templateUrl: './container-operatore.component.html',
  styleUrls: ['./container-operatore.component.css']
})
export class ContainerOperatoreComponent implements OnInit {

  disableButton: boolean;
  navigation: Navigation;
  utente: UserInfoGreg;
  constructor(private router: Router, private route: ActivatedRoute, public client: GregBOClient) {
  }

  ngOnInit(): void {
    this.client.selectprofiloazione().subscribe(result => {
      this.utente = result;
      this.client.buttonViewEmitter.subscribe((value: boolean) => {
        this.disableButton = value;
      });
    }
    )
  }

  backConfiguratore() {
    this.client.nuovaPrestazione = false;
    this.client.goToConfiguratore = true;
    this.client.goToNuovaPrestazione = false;
    this.client.goToArchivio = false;
    this.client.redirecttab(this.client.azioni, false, true);
  }

  backButton() {
    this.client.ricercaEnte = [];
    this.client.ricercaEnteCruscotto = []
    this.client.filtroEnte = null;
    this.client.filtroEnteCruscotto = null;
    this.client.listaStatiSalvato = [];
    this.client.listaStatiSalvatoCruscotto = [];
    this.client.paginaSalvataCruscotto = 0;
    this.client.inviaIFatto = false;
    this.client.inviaIIFatto = false;
    this.client.goToCruscotto = false;
    this.client.goToConfiguratore = false;
    this.client.goToNuovaPrestazione = false;
    this.client.goToArchivio = false;
    this.client.selectprofiloazione().subscribe(result => {
      this.utente = result;
      const navigationExtras: NavigationExtras = {
        relativeTo: this.route,
        skipLocationChange: true,
        state: {
          utente: this.utente
        }
      };
      this.router.navigate(["../selezione-profilo-applicativo"], navigationExtras);
    });
  }

  onActivate(event) {
    if (event.param != undefined) {
      this.disableButton = event.param;
    }
    else {
      this.disableButton = !event.param;
    }
  }

}
