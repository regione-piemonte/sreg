import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UnisciEnteComponent } from '../unisci-ente/unisci-ente.component';

@Component({
  selector: 'app-tab-operatore',
  templateUrl: './tab-operatore.component.html',
  styleUrls: ['./tab-operatore.component.css']
})
export class TabOperatoreComponent implements OnInit {

  links = [
    {
      title: 'Enti', link: 'enti-gestori-attivi', active: true, azione: 'EntiGestori'
    },
    {
//      title: 'Archivio', link: 'archivio-dati-rendicontazione', active: false, azione: 'Archivio'
       title: 'Archivio', link: 'archivio-dati-rendicontazione', active: true, azione:'Archivio'
    },
    {
      title: 'Configuratore Prestazioni', link: 'configuratore-prestazioni', active: true, azione: 'ConfiguratorePrestazioni'
    },
    {
      title: 'Configurazione FNPS Utenze', link: 'configuratore-fnps', active: true, azione: 'ConfiguratoreUtenzeFnps'
    },
    {
      title: 'Cruscotto', link: 'cruscotto', active: true, azione: 'Cruscotto'
    },
    {
      title: 'Gestione Utenti', link: 'gestione-utenti', active: true, azione: 'GestioneUtenti'
    }
  ]
  navigation: Navigation;
  cruscotto: boolean;
  active: number = 0;

  constructor(public route: ActivatedRoute, public client: GregBOClient, private modalService: NgbModal, public toastService: AppToastService, private router: Router) {
    this.navigation = this.router.getCurrentNavigation();
    this.cruscotto = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.cruscotto : false;
  }

  ngOnInit(): void {
    if (this.cruscotto) {
      this.links.forEach((element, index) => {
        if (element.link == 'cruscotto') {
          this.active = index;
        }
      });
      this.router.navigate(['operatore-regionale/cruscotto'], { skipLocationChange: true });
    }
  }





  inserisciEnte() {
    this.router.navigate(['/nuovo-ente'], { relativeTo: this.route, skipLocationChange: true });
  }



  unisciEnte() {
    // this.modalService.open(ChiudiEnteComponent, { size: 'lg' });
    // Serve a passare eventuali dati alla modale
    const modalRef = this.modalService.open(UnisciEnteComponent, { size: 'lg', windowClass: 'my-class' });
    modalRef.result.then((result) => {
      if (result) {
        this.toastService.showSuccess({ text: 'Unione Avvenuta con successo' });
        this.onReloadPage();


      }
    }).catch((res) => { });

  }

  onReloadPage() {

    let currentUrl = this.router.url;
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      skipLocationChange: true,

    };
    this.router.navigateByUrl('/', navigationExtras).then(() => {
      this.router.navigate([currentUrl], navigationExtras);
    });
  }

  goToCruscotto(link: any) {
    if (link.link == 'cruscotto') {
      this.client.goToCruscotto = true;
      this.client.goToConfiguratore = false;
      this.client.nuovaPrestazione = false;
      this.client.goToNuovaPrestazione = false;
      this.client.goToArchivio = false;
      this.client.goToFnps = false;
    } else if (link.link == 'configuratore-prestazioni') {
      this.client.goToCruscotto = false;
      this.client.goToConfiguratore = true;
      this.client.nuovaPrestazione = false;
      this.client.goToNuovaPrestazione = false;
      this.client.goToArchivio = false;
      this.client.goToFnps = false;
    } else if (link.link == 'configuratore-fnps') {
      this.client.goToCruscotto = false;
      this.client.goToConfiguratore = false;
      this.client.nuovaPrestazione = false;
      this.client.goToNuovaPrestazione = false;
      this.client.goToArchivio = false;
      this.client.goToFnps = true;
    } else if (link.link == 'archivio-dati-rendicontazione'){
	  this.client.goToCruscotto = false;
      this.client.goToConfiguratore = false;
      this.client.nuovaPrestazione = false;
      this.client.goToNuovaPrestazione = false;
      this.client.goToArchivio = true;
      this.client.goToFnps = false;
	} else if (link.link == 'gestione-utenti'){
	    this.client.goToCruscotto = true;
      this.client.goToConfiguratore = false;
      this.client.nuovaPrestazione = false;
      this.client.goToNuovaPrestazione = false;
      this.client.goToArchivio = false;
      this.client.goToFnps = false;
	}
	else{
      this.client.goToCruscotto = false;
      this.client.goToConfiguratore = false;
      this.client.nuovaPrestazione = false;
      this.client.goToNuovaPrestazione = false;
      this.client.goToArchivio = false;
      this.client.goToFnps = false;
    }
  }

  nuovaPrestazione1() {
    this.client.nuovaPrestazione = true;
    this.client.goToNuovaPrestazione = true;
    this.router.navigate(['../operatore-regionale/nuova-prestazione'], { relativeTo: this.route, skipLocationChange: true });
  }

}
