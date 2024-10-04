import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CronologiaGreg } from '@greg-app/app/dto/CronologiaGreg';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Motivazioni } from '@greg-app/app/dto/Motivazioni';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ChiusuraEnte } from '@greg-app/app/dto/ChiusuraEnte';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { GregError } from '@greg-app/shared/error/greg-error.model';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { STATO_ENTE } from '@greg-app/constants/greg-constants';
import { forkJoin } from 'rxjs';



@Component({
  selector: 'app-ripristina-ente',
  templateUrl: './ripristina-ente.component.html',
  styleUrls: ['./ripristina-ente.component.css'],

})
export class RipristinaEnteComponent implements OnInit {

  cronologia: CronologiaGreg;
  listaMotivazioni: Motivazioni[] = [];
  dataSistema = new Date();
  dataChiusura: Date;
  ripristinoForm: FormGroup;

  @Input() public idSchedaEnteGestore: number;
  @Input() public denominazione: string;
  @Input() public email: string;
  @Input() public nome: string;
  @Input() public cognome: string;
  @Input() public emailResp: string;
  

  constructor(public client: GregBOClient, public activeModal: NgbActiveModal,
    private fb: FormBuilder, private gregError: GregErrorService, private router: Router , private route: ActivatedRoute) { }

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
    this.client.spinEmitter.emit(true);
    this.ripristinoForm = this.fb.group({
      dataRipristino: new FormControl(),
      motivazione: ['', [Validators.required]],
      notaEnte: ['', [Validators.required]],
      notaInterna: ['']
    });
    this.ripristinoForm.controls.dataRipristino.disable();
    this.listaMotivazioni = [];
    forkJoin({
      listaMotivazioni: this.client.getMotivazioneRipristino(),
      lastStato: this.client.getLastStato(this.idSchedaEnteGestore)
    }).subscribe(({listaMotivazioni, lastStato}) => {
      this.listaMotivazioni = listaMotivazioni;
      this.dataSistema = new Date(lastStato.dal);
      this.ripristinoForm.controls.dataRipristino.patchValue(this.dataSistema);
      this.client.spinEmitter.emit(false);
    },
    err => {
      this.client.spinEmitter.emit(false);
    });
  }

  conferma() {
    this.client.spinEmitter.emit(true);
    if(this.ripristinoForm.valid){
      let chiuso: ChiusuraEnte = new ChiusuraEnte();
      chiuso.dataChiusura = this.ripristinoForm.controls.dataRipristino.value;
      chiuso.idScheda = this.idSchedaEnteGestore;
      chiuso.motivazione = this.ripristinoForm.controls.motivazione.value;
      chiuso.notaEnte = this.ripristinoForm.controls.notaEnte.value;
      chiuso.notaInterna = this.ripristinoForm.controls.notaInterna.value;
      chiuso.profilo = this.client.profilo;
      chiuso.denominazione = this.denominazione;
      chiuso.email = this.email;
      chiuso.nome = this.nome;
      chiuso.cognome = this.cognome;
      chiuso.emailResp = this.emailResp;
      this.client.ripristinoEnte(chiuso).subscribe((result)=>{
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      }, err => {
        this.client.spinEmitter.emit(false);
      });
    } else {
      this.errorMessage.error.descrizione = "Bisogna compilare i campi obbligatori";
      this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: this.errorMessage.error.descrizione }))
      this.client.spinEmitter.emit(false);
    }
    
  }

  setValueDataCreazione() {
    this.dataChiusura = this.ripristinoForm.controls.dataChiusura.value;
}

}
