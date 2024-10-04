import { Component, ElementRef, Input, OnInit, QueryList, ViewChildren, ViewEncapsulation } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CronologiaGreg } from '@greg-app/app/dto/CronologiaGreg';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Motivazioni } from '@greg-app/app/dto/Motivazioni';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { AnagraficaEnteGreg } from '@greg-app/app/dto/AnagraficaEnteGreg';
import { forkJoin } from 'rxjs';
import { RicercaListaEntiDaunire } from '@greg-app/app/dto/RicercaListaEntiDaunire';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { UnisciEnte } from '@greg-app/app/dto/UnisciEnte';


@Component({
  selector: 'app-unisci-ente',
  templateUrl: './unisci-ente.component.html',
  styleUrls: ['./unisci-ente.component.css'],
  animations: [
    // Each unique animation requires its own trigger. The first argument of the trigger function is the name
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in'))
    ])
  ],
  encapsulation: ViewEncapsulation.None
})
export class UnisciEnteComponent implements OnInit {

  cronologia: CronologiaGreg;
  listaMotivazioni: Motivazioni[] = [];
  dataSistema = new Date();
  dataChiusura: Date;
  enteForm: FormGroup = new FormGroup({});
  stato: boolean = true;
  entidestinazione: AnagraficaEnteGreg[] = [];
  entidaUnire: AnagraficaEnteGreg[] = [];
  enteDaUnire: string;
  dataMerge = new Date(new Date().getFullYear(), 0, 1);
  enteDest: string;
  ricercaEntiDest: RicercaListaEntiDaunire = new RicercaListaEntiDaunire(true, null, null, null);
  ricercaEntiDaunire: RicercaListaEntiDaunire = new RicercaListaEntiDaunire(false, null, null, null);
  enableDropDest: boolean = true;
  enableDropSlave: boolean = true;
  disabledUnisci: boolean = true;
  enableUnisciDest: boolean = false;
  enableUnisciDaUnire: boolean = false;
  unisciEnte: UnisciEnte;
  idEnteDest: string;
  idEntedaUnire: string;
  listacheckeds: Map<String, boolean>[] = [];

  // @ViewChild(StatoEnteComponent, { static: false }) cronologiaStatoMod: StatoEnteComponent;



  @Input() public idSchedaEnteGestore: number;
  @Input() public denominazione: string;
  @Input() public email: string;
  @Input() public nome: string;
  @Input() public cognome: string;
  @Input() public emailResp: string;
  @ViewChildren("checkboxes") checkboxes: QueryList<ElementRef>


  constructor(public client: GregBOClient, public activeModal: NgbActiveModal,
    private fb: FormBuilder, private gregError: GregErrorService, private router: Router, private route: ActivatedRoute) { }

  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }

  ngOnInit() {
    this.ricercaEntiDest.lista = this.client.listaenti;
    this.ricercaEntiDaunire.lista = this.client.listaenti;
    this.enteForm = this.fb.group({
      codiceRegionale: ['',],
      codiceFiscale: [''],
      denominazione: [''],
      partitaIva: [''],
      tipoEnte: [''],
      comune: [''],
      codiceIstat: [''],
      asl: [''],
      email: [''],
      telefono: [''],
      pec: [''],
      codiceRegionale2: [''],
      codiceFiscale2: [''],
      denominazione2: [''],
      partitaIva2: [''],
      tipoEnte2: [''],
      comune2: [''],
      codiceIstat2: [''],
      asl2: [''],
      email2: [''],
      telefono2: [''],
      pec2: [''],
      dataMerge: ['']
    })
    this.enteForm.disable();
    this.enteForm.get('dataMerge').enable();
  }


  checkedEvnt() {
    this.checkboxes.forEach((element) => {
      element.nativeElement.checked = false;
    });
  }
  azzeradati() {
    this.enteForm.get('codiceRegionale2').patchValue('');
    this.enteForm.get('codiceFiscale2').patchValue('');
    this.enteForm.get('denominazione2').patchValue('');
    this.enteForm.get('partitaIva2').patchValue('');
    this.enteForm.get('tipoEnte2').patchValue('');
    this.enteForm.get('comune2').patchValue('');
    this.enteForm.get('codiceIstat2').patchValue('');
    this.enteForm.get('asl2').patchValue('');
    this.enteForm.get('email2').patchValue('');
    this.enteForm.get('telefono2').patchValue('');
    this.enteForm.get('pec2').patchValue('');
    this.enteForm.get('codiceRegionale').patchValue('');
    this.enteForm.get('codiceFiscale').patchValue('');
    this.enteForm.get('denominazione').patchValue('');
    this.enteForm.get('partitaIva').patchValue('');
    this.enteForm.get('tipoEnte').patchValue('');
    this.enteForm.get('comune').patchValue('');
    this.enteForm.get('codiceIstat').patchValue('');
    this.enteForm.get('asl').patchValue('');
    this.enteForm.get('email').patchValue('');
    this.enteForm.get('telefono').patchValue('');
    this.enteForm.get('pec').patchValue('');
    this.enteDaUnire = undefined;
    this.enteDest = undefined;
    this.disabledUnisci = true;
    this.checkedEvnt();
    //pulisci check

  }

  cambiaValueDaunire() {
    let ente: AnagraficaEnteGreg;
    this.entidaUnire.filter(elem => {
      if (elem.codiceRegionale == this.enteDaUnire)
        ente = elem;
    });
    if (this.enteDaUnire != undefined) {
      this.enteForm.get('codiceRegionale2').patchValue(ente.codiceRegionale);
      this.enteForm.get('codiceFiscale2').patchValue(ente.codiceFiscale);
      this.enteForm.get('denominazione2').patchValue(ente.denominazione);
      this.enteForm.get('partitaIva2').patchValue(ente.partitaIva);
      this.enteForm.get('tipoEnte2').patchValue(ente.tipoEnte.codTipoEnte);
      this.enteForm.get('comune2').patchValue(ente.comune.desComune);
      this.enteForm.get('codiceIstat2').patchValue(ente.comune.codIstatComune);
      this.enteForm.get('asl2').patchValue(ente.asl.codAsl);
      this.enteForm.get('email2').patchValue(ente.email);
      this.enteForm.get('telefono2').patchValue(ente.telefono);
      this.enteForm.get('pec2').patchValue(ente.pec);
      this.idEntedaUnire = ente.codiceRegionale;
      this.enableUnisciDaUnire = true;
      if (this.enableUnisciDaUnire == true && this.enableUnisciDest == true) {
        this.disabledUnisci = false;
      }
      if (this.idEnteDest == this.idEntedaUnire) {
        this.disabledUnisci = true;
      }
      if (this.enteDest == undefined || this.enteDaUnire == undefined) {
        this.disabledUnisci = true;
      }
    }
  }

  cambiaValueDest() {
    let ente: AnagraficaEnteGreg;
    //cancella i dati del da unire
    this.enteForm.get('codiceRegionale2').patchValue('');
    this.enteForm.get('codiceFiscale2').patchValue('');
    this.enteForm.get('denominazione2').patchValue('');
    this.enteForm.get('partitaIva2').patchValue('');
    this.enteForm.get('tipoEnte2').patchValue('');
    this.enteForm.get('comune2').patchValue('');
    this.enteForm.get('codiceIstat2').patchValue('');
    this.enteForm.get('asl2').patchValue('');
    this.enteForm.get('email2').patchValue('');
    this.enteForm.get('telefono2').patchValue('');
    this.enteForm.get('pec2').patchValue('');
    this.checkedEvnt();
    this.enteDaUnire = undefined;
    this.entidestinazione.filter(elem => {
      if (elem.codiceRegionale == this.enteDest)
        ente = elem;
    });
    if (this.enteDest != undefined) {
      this.enteForm.get('codiceRegionale').patchValue(ente.codiceRegionale);
      this.enteForm.get('codiceFiscale').patchValue(ente.codiceFiscale);
      this.enteForm.get('denominazione').patchValue(ente.denominazione);
      this.enteForm.get('partitaIva').patchValue(ente.partitaIva);
      this.enteForm.get('tipoEnte').patchValue(ente.tipoEnte.codTipoEnte);
      this.enteForm.get('comune').patchValue(ente.comune.desComune);
      this.enteForm.get('codiceIstat').patchValue(ente.comune.codIstatComune);
      this.enteForm.get('asl').patchValue(ente.asl.codAsl);
      this.enteForm.get('email').patchValue(ente.email);
      this.enteForm.get('telefono').patchValue(ente.telefono);
      this.enteForm.get('pec').patchValue(ente.pec);
      this.idEnteDest = ente.codiceRegionale;
      this.enableUnisciDest = true;
      if (this.enableUnisciDaUnire == true && this.enableUnisciDest == true) {
        this.disabledUnisci = false;
      }
      if (this.idEnteDest == this.idEntedaUnire) {
        this.disabledUnisci = true;
      }
      if (this.enteDest == undefined || this.enteDaUnire == undefined) {
        this.disabledUnisci = true;
      }
      this.ricercaEntiDaunire.dataMerge = this.enteForm.get('dataMerge').value;
      this.ricercaEntiDaunire.codregionale = ente.codiceRegionale;
      forkJoin({
        entidaunire: this.client.getSchedaAnagraficaunione(this.ricercaEntiDaunire)
      }).subscribe(({ entidaunire }) => {
        this.entidaUnire = entidaunire;
      })
    }
    if (this.enteDest != undefined) {
      this.enableDropDest = false;
      this.enableDropSlave = false;
    } else {
      this.enableDropDest = false;
      this.enableDropSlave = true;
    }

  }

  onDateChange() {
    if (this.enteForm.get('dataMerge').value != null && this.enteForm.get('dataMerge').value != '') {
      this.client.spinEmitter.emit(true);
      this.azzeradati();
      this.ricercaEntiDest.dataMerge = this.enteForm.get('dataMerge').value;
      forkJoin({
        entidestinazione: this.client.getSchedaAnagraficaunione(this.ricercaEntiDest)
      }).subscribe(({ entidestinazione }) => {
        this.entidestinazione = entidestinazione;
      })
      if (this.enteForm.get('dataMerge').value != null) {
        this.enableDropDest = false;
        this.enableDropSlave = true;
      } else {
        this.enableDropDest = true;
        this.enableDropSlave = true;
      }
      this.client.spinEmitter.emit(false);
    }
  }

  changeValueUnion(id: number, event: any, selected: string) {
    var appoggio = new Map<String, boolean>();
    if (event.target.checked) {
      appoggio[selected] = true;
      this.listacheckeds[id] = appoggio;
    }
    else {
      appoggio[selected] = false;
      this.listacheckeds[id] = appoggio;
    }
  }

  unisci() {
    this.client.spinEmitter.emit(true);
    this.unisciEnte = new UnisciEnte();
    this.unisciEnte.dataMerge = this.enteForm.get('dataMerge').value;
    this.unisciEnte.codiceRegionaleEnteDaUnire = this.enteDaUnire;
    this.unisciEnte.codiceRegionaleEnteDest = this.enteDest;
    this.unisciEnte.listacheckeds = this.listacheckeds;
    this.unisciEnte.lista = this.client.listaenti;
    this.unisciEnte.denominazioneEnteDaUnire = this.enteForm.get('denominazione2').value;
    this.unisciEnte.denominazioneEnteDest = this.enteForm.get('denominazione').value;

    this.client.unisciEnte(this.unisciEnte).subscribe(res => {

      if (res) {
        //console.log('ok');
        this.client.spinEmitter.emit(false);
        this.client.ricercaEnte = [];
        this.client.ricercaEnteCruscotto = [];
        this.activeModal.close(true);

      }

    },
      err => {
        this.client.spinEmitter.emit(false);

      }
    )
    //this.client.spinEmitter.emit(false); 

  }

}
