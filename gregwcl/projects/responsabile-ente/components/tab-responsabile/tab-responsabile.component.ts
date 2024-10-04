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
  selector: 'app-tab-responsabile',
  templateUrl: './tab-responsabile.component.html',
  styleUrls: ['./tab-responsabile.component.css']
})
export class TabResponsabileComponent implements OnInit {

  links: { title, link, active, fragment?, azione }[];
  navigation: Navigation;
  tooltip: string = '';
  show: boolean;
  selectedLink: any;
  visibleTab: boolean = false;
  visibleTabEnti: boolean = false;
  visibleTabRendicontazioni: boolean = false;
  visibleTabFNPS: boolean = false;
  visibleTabAlZero: boolean = false;
  changeTabEvent = new CustomEvent('changeTabEvent', { bubbles: true });
  datiEnte: DatiEnteGreg;
  // modelli: ModelTabTranche[];
  modelli: LinkModelli[];

  rendicontazione: RendicontazioneEnteGreg;
  model: string;
  constructor(public route: ActivatedRoute, private router: Router, private client: GregBOClient) {
    this.navigation = this.router.getCurrentNavigation();
    this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : null;
    this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : null;
    this.model = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.model : null;
  }

  ngOnInit(): void {

    this.tooltip = '';
    this.client.getModelliPerTranche(this.rendicontazione.idRendicontazioneEnte, this.client.profilo.codProfilo).
      //this.client.getModelliassociati( this.rendicontazione.idRendicontazioneEnte).
      subscribe(modelli => {
        this.modelli = modelli;
        //prelevo modelli associati per renderli o meno visibili
        this.visibilitaTab('dati-rendicontazione');
        this.visibilitaTab('dati-ente');
        this.visibilitaTab('all-d');
        if (this.rendicontazione.statoRendicontazione.codStatoRendicontazione == null || this.datiEnte.codTipoEnte == null) {
          this.client.getMsgApplicativo(ERRORS.ERROR_ANNO_CONTABILE).subscribe((msg: MsgApplicativo) => {
            this.tooltip = msg.testoMsgApplicativo;
          });
          this.client.getInfoRendicontazioneOperatore(this.rendicontazione.idRendicontazioneEnte).subscribe(data => {
            this.show = data.statoRendicontazione.codStatoRendicontazione != null ? true : false;
            this.links = [
              {
                title: 'Dati Ente', link: 'dati-ente', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'DatiEnteTab'
              },
              {
                title: 'Dati Rendicontazione', link: 'dati-rendicontazione', active: this.show, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'DatiRendicontazioneTab'
              },
              {
                title: 'Modulo Al-Zero', link: 'al-zero', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'ModelloAlZero'
              },
              {
                title: 'Modulo FNPS', link: 'all-d', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'ModelloFNPS'
              },
            ]
            // Nascondo tab AlZero prima dell'anno 2023
            if (this.rendicontazione.annoEsercizio < 2023) {
              this.links = this.links.filter(link => link.title !== 'Modulo Al-Zero');
            }
          })
        } else {
          this.links = [
            {
              title: 'Dati Ente', link: 'dati-ente', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'DatiEnteTab'
            },
            {
              title: 'Dati Rendicontazione', link: this.modelli[0] != null ? 'dati-rendicontazione/' + this.modelli[0].link : 'dati-rendicontazione', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'DatiRendicontazioneTab'
            },
            {
              title: 'Modulo Al-Zero', link: 'al-zero', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'ModelloAlZero'
            },
            {
              title: 'Modulo FNPS', link: 'all-d', active: true, fragment: [this.rendicontazione, this.datiEnte, this.model], azione: 'ModelloFNPS'
            },
          ]
          // Nascondo tab AlZero prima dell'anno 2023
          if (this.rendicontazione.annoEsercizio < 2023) {
            this.links = this.links.filter(link => link.title !== 'Modulo Al-Zero');
          }
        }
        // this.client.dettaglioPrestazione = false;
        const navigationExtras: NavigationExtras = {
          relativeTo: this.route,
          skipLocationChange: true,
          state: {
            rendicontazione: this.rendicontazione,
            datiEnte: this.datiEnte,
            model: this.model
          }
        };

        if (this.visibleTabRendicontazioni == false && this.visibleTabEnti == false && this.visibleTabFNPS == false && this.visibleTabAlZero == false) {
          this.router.navigate(['/redirect-page'], navigationExtras);
        }
      });
  }

  visibilitaTab(link) {
    if ((this.modelli.length > 0 && link == ('dati-rendicontazione/' + this.modelli[0].link)) || (this.modelli.length == 0 && link == 'dati-rendicontazione')) {
      if (this.client.azioni.get("DatiRendicontazioneTab")[1] &&
        this.modelli.length > 0
        && !(this.modelli[0].sigla == 'MA' && this.rendicontazione.statoRendicontazione.codStatoRendicontazione == RENDICONTAZIONE_STATUS.DA_COMPILARE_I)) {
        this.visibleTab = true;
        this.visibleTabRendicontazioni = true;
      }
      else {
        this.visibleTab = false;
        this.visibleTabRendicontazioni = false;
      }
    }
    else if (link == 'dati-ente') {
      if (this.client.azioni.get("DatiEnteTab")[1] &&
        (this.client.azioni.get("EnteContatti")[1] ||
          this.client.azioni.get("EnteResponsabile")[1] ||
          this.client.azioni.get("EnteComuni")[1] ||
          this.client.azioni.get("DatiEntePrestazioni")[1] ||
          this.client.azioni.get("DatiEnteAllegati")[1] ||
          this.client.azioni.get("DatiEnteParametri")[1] ||
          this.client.azioni.get("AssociaModelli")[1])) {
        this.visibleTab = true;
        this.visibleTabEnti = true;
      }
      else {
        this.visibleTab = false;
        this.visibleTabEnti = false;
      }
    }
    else if (link == 'all-d') {
      if (this.client.azioni.get("ModelloFNPS") && this.client.azioni.get("ModelloFNPS")[1]) {
        this.visibleTab = true;
        this.visibleTabFNPS = true;
      }
      else {
        this.visibleTab = false;
        this.visibleTabFNPS = false;
      }
    }
    else if (link == 'al-zero') {
      // TODO Aggiungere azione giusta
      if (this.client.azioni.get("ModelloAlZero") && this.client.azioni.get("ModelloAlZero")[1]) {
        this.visibleTab = true;
        this.visibleTabAlZero = true;
      }
      else {
        this.visibleTab = false;
        this.visibleTabAlZero = false;
      }
    }
    return this.visibleTab;
  }

  disattivaDettaglioPrestazione() {
    this.client.dettaglioPrestazione = false;
  }

}



