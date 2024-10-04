import { Component, Inject, OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MatTableDataSource, MAT_DIALOG_DATA } from "@angular/material";
import { ListaEnte } from "@greg-app/app/dto/ListaEnte";
import { ListaLista } from "@greg-app/app/dto/ListaLista";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { AppToastService } from "@greg-shared/toast/app-toast.service";

@Component({
  selector: 'app-crea-lista-popup',
  templateUrl: './crea-lista-popup.component.html',
  styleUrls: ['./crea-lista-popup.component.css']
})
export class CreaListaPopupComponent implements OnInit {
  attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<CreaListaPopupComponent>, private fb: FormBuilder, public client: GregBOClient, public toastService: AppToastService,
    @Inject(MAT_DIALOG_DATA) public data: { titolo: string, liste: ListaLista[], entiDaAssegnare: ListaEnte[] }) { }

  listaForm: FormGroup = new FormGroup({});
  lista: ListaLista;
  enti: ListaEnte[] = [];
  ente: ListaEnte;
  dataEnti = new MatTableDataSource<ListaEnte>();
  enteRow: string[] = ['ente', 'cestino'];
  ngOnInit() {
    this.listaForm = this.fb.group({
      codLista: [''],
      descLista: [''],
    })
    this.ente = new ListaEnte();
    this.ente.idEnte = 0;
    this.initLista();
  }
  initLista() {
    this.listaForm = this.fb.group({
      codLista: ["", [Validators.required, this.noWhitespaceValidator]],
      descLista: ["", [Validators.required, this.noWhitespaceValidator]]
    })
  }
  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }

  conferma() {
    this.lista = new ListaLista();
    this.lista.codLista = this.listaForm.controls.codLista.value;
    this.lista.descLista = this.listaForm.controls.descLista.value;

    this.lista.enti = this.enti;
    this.lista.entiMancate = this.data.entiDaAssegnare;
    this.client.spinEmitter.emit(true);
    this.client.creaLista(this.lista).subscribe((r) => {
      this.toastService.showSuccess({ text: r.messaggio });
      this.lista.idLista = r.idEnte;
      this.dialogRef.close(this.lista);
      this.client.spinEmitter.emit(false);
    }, err => {
      this.client.spinEmitter.emit(false);
    })

  }

  annulla() {
    this.dialogRef.close(null);
  }

  VerificaValidita() {
    return !this.listaForm.valid;
  }

  verificaZeroEnti() {
    if (this.enti.length != 0) {
      return true;
    }
    return false;
  }

  onSelectionChangedListe(value: number) {
    if (value != 0) {
      let p: ListaLista = this.data.liste.find((e) => e.idLista == value);
      this.enti = p.enti;
      this.dataEnti.data = this.enti;
      for(let az of this.enti){
        this.data.entiDaAssegnare = this.data.entiDaAssegnare.filter((a) => a.idEnte != az.idEnte);
      }
    } else {
      this.enti = [];
    }
  }

  onSelectionChangedEnte(value: number) {
    this.ente = this.data.entiDaAssegnare.find((a) => a.idEnte == value);
  }

  disabilitaAggiungiEnti() {
    if (this.ente.idEnte == 0) {
      return true;
    }
    return false;
  }

  aggiungiEnte() {
    this.enti.push(this.ente);
    this.enti.sort((a, b) => this.sortEnti(a, b));
    this.dataEnti.data = this.enti;
    this.data.entiDaAssegnare = this.data.entiDaAssegnare.filter((a) => a.idEnte != this.ente.idEnte);
    this.ente = new ListaEnte();
    this.ente.idEnte = 0;
  }

  sortEnti(a: ListaEnte, b: ListaEnte) {
    if (a.codEnte < b.codEnte) {
      return -1;
    } else if (a.codEnte > b.codEnte) {
      return 1;
    } else {
      return 0;
    }
  }

  eliminaEnte(a: ListaEnte) {
    this.enti = this.enti.filter((az) => az.idEnte != a.idEnte);
    this.data.entiDaAssegnare.push(a);
    this.data.entiDaAssegnare.sort((a, b) => this.sortEnti(a, b))
    this.dataEnti.data = this.enti;
  }
}
