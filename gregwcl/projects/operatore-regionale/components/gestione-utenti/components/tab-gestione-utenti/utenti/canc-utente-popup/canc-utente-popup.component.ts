import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { Utenti } from "@greg-app/app/dto/Utenti";

@Component({
  selector: 'app-canc-popup',
  templateUrl: './canc-utente-popup.component.html',
  styleUrls: ['./canc-utente-popup.component.css']
})
export class CancUtentePopupComponent implements OnInit {
  attenzione: string = '';
  constructor(public dialogRef: MatDialogRef<CancUtentePopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { titolo: string, messaggio: string, utente: Utenti }) { }

  ngOnInit() {
  }

  conferma() {
      this.dialogRef.close(this.data.utente);
  }

  annulla() {
    this.dialogRef.close(false);
  }

  controlData() {
    if (this.data.utente.maxData != null) {
      if (this.data.utente.dataFineValidita == null) {
        return true;
      }
    }
    return false;
  }

  checkMaxData(){
    if (this.data.utente.maxData != null) {
      return true;
    }
    return false;
  }
}
