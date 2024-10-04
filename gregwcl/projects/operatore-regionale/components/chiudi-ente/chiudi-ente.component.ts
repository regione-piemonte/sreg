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



@Component({
  selector: 'app-chiudi-ente',
  templateUrl: './chiudi-ente.component.html',
  styleUrls: ['./chiudi-ente.component.css'],

})
export class ChiudiEnteComponent implements OnInit {

  cronologia: CronologiaGreg;
  listaMotivazioni: Motivazioni[] = [];
  dataSistema =  new Date(new Date().getFullYear(), 0, 1);
  dataChiusura: Date;
  chiusuraForm: FormGroup;

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
    this.chiusuraForm = this.fb.group({
      dataChiusura: new FormControl(Validators.required),
      motivazione: ['', [Validators.required]],
      notaEnte: ['', [Validators.required]],
      notaInterna: ['']
    });
    this.listaMotivazioni = [];
    this.client.getMotivazioneChiusura().subscribe((result: Motivazioni[]) => {
      
      this.listaMotivazioni = result;
    });
    // this.chiusuraForm.controls.dataChiusura.valueChanges.subscribe((value:string) => {
    //   let valore = value;
    //   let v = valore.split("/");
    //   if(v.length==3){
    //     this.chiusuraForm.controls.dataChiusura.patchValue(v[1]+'/'+v[0]+'/'+v[2]);
    //   }
      
    // })
  }

  conferma() {
    this.client.spinEmitter.emit(true);
    if(this.chiusuraForm.valid){
      let chiuso: ChiusuraEnte = new ChiusuraEnte();
      chiuso.dataChiusura = this.chiusuraForm.controls.dataChiusura.value;
      chiuso.idScheda = this.idSchedaEnteGestore;
      chiuso.motivazione = this.chiusuraForm.controls.motivazione.value;
      chiuso.notaEnte = this.chiusuraForm.controls.notaEnte.value;
      chiuso.notaInterna = this.chiusuraForm.controls.notaInterna.value;
      chiuso.profilo = this.client.profilo;
      chiuso.denominazione = this.denominazione;
      chiuso.email = this.email;
      chiuso.nome = this.nome;
      chiuso.cognome = this.cognome;
      chiuso.emailResp = this.emailResp;
      this.client.closeEnte(chiuso).subscribe((result)=>{
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
    this.dataChiusura = this.chiusuraForm.controls.dataChiusura.value;
}

}
