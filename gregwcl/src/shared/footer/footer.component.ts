import { Component, OnInit } from '@angular/core';
import { FOOTER_CONSTANTS, SECTION } from '../../constants/greg-constants';
import { GregBOClient } from '@greg-app/app/GregBOClient';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  
  portal = null;

  assistenza: String = '';
  help: String = '';
  write: String = '';
  mailAssistenza: String = '';
  specific: String = '';
  info: String = '';
  sistema: String = '';
  servizio: String = '';
  pIva: String = '';


  constructor( public client: GregBOClient) { }

  ngOnInit() {
    this.client.getMsgInformativi(SECTION.FOOTERASSISTENZA).subscribe(r =>{
      this.assistenza = r[0].testoMsgInformativo;
    })
    // this.assistenza = FOOTER_CONSTANTS.ASSISTENZA;
    // this.help = FOOTER_CONSTANTS.HELP;
    // this.write = FOOTER_CONSTANTS.WRITE;
    // this.specific = FOOTER_CONSTANTS.SPECIFIC;
    // this.info = FOOTER_CONSTANTS.INFO;
    // this.sistema = FOOTER_CONSTANTS.SISTEMA;
    // this.servizio = FOOTER_CONSTANTS.SERVIZIO;
    // this.pIva = FOOTER_CONSTANTS.P_IVA;
    // this.mailAssistenza = FOOTER_CONSTANTS.MAIL_ASSISTENZA;
  }
}
