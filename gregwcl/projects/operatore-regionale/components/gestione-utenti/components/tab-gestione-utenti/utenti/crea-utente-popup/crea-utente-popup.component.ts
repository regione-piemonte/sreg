import { Component, Inject, OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MatTableDataSource, MAT_DIALOG_DATA } from "@angular/material";
import { Abilitazioni } from "@greg-app/app/dto/Abilitazioni";
import { ListeConfiguratore } from "@greg-app/app/dto/ListeConfiguratore";
import { Utenti } from "@greg-app/app/dto/Utenti";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { PATTERN, STATO_ABILITAZIONE } from "@greg-app/constants/greg-constants";
import { AppToastService } from "@greg-shared/toast/app-toast.service";

@Component({
  selector: 'app-crea-utente-popup',
  templateUrl: './crea-utente-popup.component.html',
  styleUrls: ['./crea-utente-popup.component.css']
})
export class CreaUtentePopupComponent implements OnInit {
  attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<CreaUtentePopupComponent>, private fb: FormBuilder, public client: GregBOClient, public toastService: AppToastService,
    @Inject(MAT_DIALOG_DATA) public data: { titolo: string, profili: ListeConfiguratore[], liste: ListeConfiguratore[] }) { }
  displayedColumns: string[] = ['profilo', 'lista', 'cestino'];
  secondRow: string[] = ['titoloProfilo', 'titoloLista', 'aggiungi'];
  utenteForm: FormGroup = new FormGroup({});
  utente: Utenti;
  ngOnInit() {
    this.utente = new Utenti();
    this.utenteForm = this.fb.group({
      nome: [''],
      cognome: [''],
      codiceFiscale: [''],
      email: [''],
    })
    this.initUtente();
  }

  initUtente() {
    this.utenteForm = this.fb.group({
      nome: ["", [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator]],
      cognome: ["", [Validators.pattern(PATTERN.CHARS), Validators.maxLength(40), Validators.required, this.noWhitespaceValidator]],
      codiceFiscale: ["", [Validators.maxLength(16), Validators.pattern(PATTERN.CODFISC), Validators.required, this.noWhitespaceValidator]],
      email: ["", [Validators.email, Validators.required, this.noWhitespaceValidator]]
    })
    this.utente.abilitazioni = [];
  }
  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }
  conferma() {
    this.client.spinEmitter.emit(true);
    this.utente.nome = this.utenteForm.controls.nome.value.toUpperCase();
    this.utente.cognome = this.utenteForm.controls.cognome.value.toUpperCase();
    this.utente.codiceFiscale = this.utenteForm.controls.codiceFiscale.value.toUpperCase();
    this.utente.email = this.utenteForm.controls.email.value;
    this.client.creaUtente(this.utente).subscribe((r) => {
      this.toastService.showSuccess({ text: r.messaggio });
      this.utente.idUtente = r.idPrestazione;
      this.dialogRef.close(this.utente);
      this.client.spinEmitter.emit(false);
    }, err => {
      this.client.spinEmitter.emit(false);
    })

  }

  onSelectionChangedLista(value: number, i: number) {
    if (value == 0) {
      let newLista: ListeConfiguratore = new ListeConfiguratore();
      newLista.id = 0;
      this.utente.abilitazioni[i].lista = newLista;
    } else {
      let lista = this.data.liste.find((e) => e.id == value);
      this.utente.abilitazioni[i].lista = lista;
    }
  }

  onSelectionChangedProfilo(value: number, i: number) {
    if (value == 0) {
      let newProfilo: ListeConfiguratore = new ListeConfiguratore();
      newProfilo.id = 0;
      this.utente.abilitazioni[i].profilo = newProfilo;
    } else {
      let profilo = this.data.profili.find((e) => e.id == value);
      this.utente.abilitazioni[i].profilo = profilo;
    }
  }

  aggiungiAbilitazione() {
    let newProfilo: ListeConfiguratore = new ListeConfiguratore();
    let newLista: ListeConfiguratore = new ListeConfiguratore();
    newProfilo.id = 0;
    newLista.id = 0;
    let newAbilitazione: Abilitazioni = new Abilitazioni();
    newAbilitazione.lista = newLista;
    newAbilitazione.profilo = newProfilo;
    this.utente.abilitazioni.push(newAbilitazione);
  }

  eliminaAbilitazione(i: number) {
    this.utente.abilitazioni.splice(i, 1);
  }


  annulla() {
    this.dialogRef.close(null);
  }

  disabilitaSalvaAbilitazione(utente: Utenti) {
    let trovato: boolean = false;
    utente.abilitazioni.forEach((a) => {
      if (a.profilo.id == 0) {
        trovato = true;
      }
      if (a.lista.id == 0) {
        trovato = true;
      }
      if(a.dataInizioValidita == null){
        trovato = true;
      }
    })
    return trovato;
  }

  VerificaValidita() {
    return !this.utenteForm.valid || this.disabilitaSalvaAbilitazione(this.utente);
  }
}
