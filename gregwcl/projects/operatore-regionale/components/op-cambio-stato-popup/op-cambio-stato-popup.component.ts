import { Component, Inject, OnInit, ViewChild } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { CronologiaGreg } from "@greg-app/app/dto/CronologiaGreg";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { GregError } from "@greg-app/shared/error/greg-error.model";
import { GregErrorService } from "@greg-app/shared/error/greg-error.service";
import { CronologiaModelliComponent } from "@greg-shared/cronologia-modelli/cronologia-modelli.component";

@Component({
  selector: 'app-op-cambio-stato-popup',
  templateUrl: './op-cambio-stato-popup.component.html',
  styleUrls: ['./op-cambio-stato-popup.component.css']
})
export class OpCambioStatoPopupComponent implements OnInit {
  attenzione: string = '';
  cronologia: CronologiaGreg = new CronologiaGreg();
  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }
  visibileSection: boolean = false;
  somePlaceholder: string;
  constructor(public dialogRef: MatDialogRef<OpCambioStatoPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { titolo: string, messaggio: string, operazione: boolean }, private gregError: GregErrorService, public client: GregBOClient) { }

  ngOnInit() {
    if (this.client.azioni.get("CronologiaRegionale")[1]) {
      this.visibileSection = true;
      this.somePlaceholder = "Inserisci qui la nota per l'Ente";
    }
    else if (this.client.azioni.get("CronologiaEnte")[1]) {
      this.visibileSection = false;
      this.somePlaceholder = "Inserisci qui la nota";
    }
  }

  conferma() {
    if (!this.data.operazione) {
      this.dialogRef.close(true);
    } else {
      if ((this.client.azioni.get("CronologiaRegionale")[1] && !this.client.azioni.get("CronologiaRegionale")[0])
        //if ((this.client.ruolo==ROLES.OPERATORE_REGIONALE || this.client.ruolo==ROLES.SUPERUSER)
        && (this.cronologia.notaEnte == null || this.cronologia.notaEnte == "")) {
        this.errorMessage.error.descrizione = "Inserire una nota per l'ente";
        this.gregError.handleWarning(GregError.toGregError({ ...this.errorMessage, errorDesc: "Inserire una nota per l'ente" }))
      } else {
        this.dialogRef.close(this.cronologia);
      }
    }
  }

  annulla() {
    this.dialogRef.close(false);
  }
}
