import { Component, Inject,  OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { ListaAzione } from "@greg-app/app/dto/ListaAzione";
import { ListaEnte } from "@greg-app/app/dto/ListaEnte";
import { ListaLista } from "@greg-app/app/dto/ListaLista";
import { ListaProfilo } from "@greg-app/app/dto/ListaProfilo";
import { Utenti } from "@greg-app/app/dto/Utenti";
import { PATTERN } from "@greg-app/constants/greg-constants";

@Component({
  selector: 'app-modifica-lista-popup',
  templateUrl: './modifica-lista-popup.component.html',
  styleUrls: ['./modifica-lista-popup.component.css']
})
export class ModificaListaPopupComponent implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<ModificaListaPopupComponent>, private fb: FormBuilder,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, lista:ListaLista, liste: ListaLista[]}) {}

  listaForm: FormGroup = new FormGroup({});
  lista: ListaLista;
  enti: ListaEnte[] = [];
  ngOnInit() {
    this.listaForm = this.fb.group({
      codLista: [''],
      descLista: [''],
    })
    this.initLista(this.data.lista);
    this.data.liste = this.data.liste.filter((p)=>p.codLista!=this.data.lista.codLista);
  }

  initLista(lista: ListaLista) {
    this.listaForm = this.fb.group({
      codLista: [lista.codLista, [Validators.required, this.noWhitespaceValidator]],
      descLista: [lista.descLista, [Validators.required, this.noWhitespaceValidator]]
    })
    this.listaForm.controls.codLista.disable();
  }
  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }
  conferma(){
    this.lista = new ListaLista();
    this.lista.codLista = this.listaForm.controls.codLista.value;
    this.lista.descLista = this.listaForm.controls.descLista.value;
    if(this.enti.length!=0){
      this.lista.entiDaCopiare = this.enti;
    }
		this.dialogRef.close(this.lista);
	}

  annulla(){
		this.dialogRef.close(null);
	}

  VerificaValidita(){
    return !this.listaForm.valid;
  }

  verificaZeroEnti(){
    if(this.data.lista.enti.length!=0){
      return true;
    }
    return false;
  }

  onSelectionChangedListe(value:number){
    if(value!=0){
      let p:ListaLista = this.data.liste.find((e)=> e.idLista == value);
      this.enti = p.enti;
    }else{
      this.enti = [];
    }
  }
}
