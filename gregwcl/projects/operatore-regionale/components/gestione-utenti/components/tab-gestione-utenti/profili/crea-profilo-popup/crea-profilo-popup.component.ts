import { Component, Inject, OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MatTableDataSource, MAT_DIALOG_DATA } from "@angular/material";
import { ListaAzione } from "@greg-app/app/dto/ListaAzione";
import { ListaProfilo } from "@greg-app/app/dto/ListaProfilo";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { AppToastService } from "@greg-shared/toast/app-toast.service";

@Component({
  selector: 'app-crea-profilo-popup',
  templateUrl: './crea-profilo-popup.component.html',
  styleUrls: ['./crea-profilo-popup.component.css']
})
export class CreaProfiloPopupComponent implements OnInit {
  attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<CreaProfiloPopupComponent>, private fb: FormBuilder, public client: GregBOClient, public toastService: AppToastService,
    @Inject(MAT_DIALOG_DATA) public data: { titolo: string, profili: ListaProfilo[], azioniDaAssegnare: ListaAzione[] }) { }

  profiloForm: FormGroup = new FormGroup({});
  profilo: ListaProfilo;
  azioni: ListaAzione[] = [];
  azione: ListaAzione;
  dataAzioni = new MatTableDataSource<ListaAzione>();
  azioneRow: string[] = ['azione', 'cestino'];
  ngOnInit() {
    this.profiloForm = this.fb.group({
      codProfilo: [''],
      descProfilo: [''],
    })
    this.azione = new ListaAzione();
    this.azione.idAzione = 0;
    this.initProfilo();
  }
  initProfilo() {
    this.profiloForm = this.fb.group({
      codProfilo: ["", [Validators.required, this.noWhitespaceValidator]],
      descProfilo: ["", [Validators.required, this.noWhitespaceValidator]]
    })
  }
  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }

  conferma() {
    this.profilo = new ListaProfilo();
    this.profilo.codProfilo = this.profiloForm.controls.codProfilo.value;
    this.profilo.descProfilo = this.profiloForm.controls.descProfilo.value;

    this.profilo.azioni = this.azioni;
    this.profilo.azioniMancate = this.data.azioniDaAssegnare;
    this.client.spinEmitter.emit(true);
    this.client.creaProfilo(this.profilo).subscribe((r) => {
      this.toastService.showSuccess({ text: r.messaggio });
      this.profilo.idProfilo = r.idEnte;
      this.dialogRef.close(this.profilo);
      this.client.spinEmitter.emit(false);
    }, err => {
      this.client.spinEmitter.emit(false);
    })

  }

  annulla() {
    this.dialogRef.close(null);
  }

  VerificaValidita() {
    return !this.profiloForm.valid;
  }

  verificaZeroAzioni() {
    if (this.azioni.length != 0) {
      return true;
    }
    return false;
  }

  onSelectionChangedProfili(value: number) {
    if (value != 0) {
      let p: ListaProfilo = this.data.profili.find((e) => e.idProfilo == value);
      this.azioni = p.azioni;
      this.dataAzioni.data = this.azioni;
      for(let az of this.azioni){
        this.data.azioniDaAssegnare = this.data.azioniDaAssegnare.filter((a) => a.idAzione != az.idAzione);
      }
    } else {
      this.azioni = [];
    }
  }

  onSelectionChangedAzione(value: number) {
    this.azione = this.data.azioniDaAssegnare.find((a) => a.idAzione == value);
  }

  disabilitaAggiungiAzioni() {
    if (this.azione.idAzione == 0) {
      return true;
    }
    return false;
  }

  aggiungiAzione() {
    this.azioni.push(this.azione);
    this.azioni.sort((a, b) => this.sortAzioni(a, b));
    this.dataAzioni.data = this.azioni;
    this.data.azioniDaAssegnare = this.data.azioniDaAssegnare.filter((a) => a.idAzione != this.azione.idAzione);
    this.azione = new ListaAzione();
    this.azione.idAzione = 0;
  }

  sortAzioni(a: ListaAzione, b: ListaAzione) {
    if (a.codAzione < b.codAzione) {
      return -1;
    } else if (a.codAzione > b.codAzione) {
      return 1;
    } else {
      return 0;
    }
  }

  eliminaAzione(a: ListaAzione) {
    this.azioni = this.azioni.filter((az) => az.idAzione != a.idAzione);
    this.data.azioniDaAssegnare.push(a);
    this.data.azioniDaAssegnare.sort((a, b) => this.sortAzioni(a, b))
    this.dataAzioni.data = this.azioni;
  }
}
