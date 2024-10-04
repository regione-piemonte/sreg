import { Component, Inject,  OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { Utenti } from "@greg-app/app/dto/Utenti";
import { PATTERN } from "@greg-app/constants/greg-constants";

@Component({
  selector: 'app-modifica-utente-popup',
  templateUrl: './modifica-utente-popup.component.html',
  styleUrls: ['./modifica-utente-popup.component.css']
})
export class ModificaUtentePopupComponent implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<ModificaUtentePopupComponent>, private fb: FormBuilder,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, utente:Utenti}) {}

  utenteForm: FormGroup = new FormGroup({});
  utente: Utenti;
  ngOnInit() {
    this.utenteForm = this.fb.group({
      nome: [''],
      cognome: [''],
      codiceFiscale: [''],
      email: [''],
    })
    this.initUtente(this.data.utente);
  }

  initUtente(utente: Utenti) {
    this.utenteForm = this.fb.group({
      nome: [utente.nome, [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator]],
      cognome: [utente.cognome, [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator]],
      codiceFiscale: [utente.codiceFiscale, [Validators.maxLength(16), Validators.pattern(PATTERN.CODFISC), Validators.required, this.noWhitespaceValidator]],
      email: [utente.email, [Validators.email, Validators.required, this.noWhitespaceValidator]]
    })
  }
  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }
  conferma(){
    this.utente = new Utenti();
    this.utente.nome = this.utenteForm.controls.nome.value.toUpperCase();
    this.utente.cognome = this.utenteForm.controls.cognome.value.toUpperCase();
    this.utente.codiceFiscale = this.utenteForm.controls.codiceFiscale.value.toUpperCase();
    this.utente.email = this.utenteForm.controls.email.value;
		this.dialogRef.close(this.utente);
	}

  annulla(){
		this.dialogRef.close(null);
	}

  VerificaValidita(){
    return !this.utenteForm.valid;
  }
}
