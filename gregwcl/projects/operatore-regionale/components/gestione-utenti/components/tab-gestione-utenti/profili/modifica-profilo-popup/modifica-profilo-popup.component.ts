import { Component, Inject,  OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { ListaAzione } from "@greg-app/app/dto/ListaAzione";
import { ListaProfilo } from "@greg-app/app/dto/ListaProfilo";
import { Utenti } from "@greg-app/app/dto/Utenti";
import { PATTERN } from "@greg-app/constants/greg-constants";

@Component({
  selector: 'app-modifica-profilo-popup',
  templateUrl: './modifica-profilo-popup.component.html',
  styleUrls: ['./modifica-profilo-popup.component.css']
})
export class ModificaProfiloPopupComponent implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<ModificaProfiloPopupComponent>, private fb: FormBuilder,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, profilo:ListaProfilo, profili: ListaProfilo[]}) {}

  profiloForm: FormGroup = new FormGroup({});
  profilo: ListaProfilo;
  azioni: ListaAzione[] = [];
  ngOnInit() {
    this.profiloForm = this.fb.group({
      codProfilo: [''],
      descProfilo: [''],
    })
    this.initProfilo(this.data.profilo);
    this.data.profili = this.data.profili.filter((p)=>p.codProfilo!=this.data.profilo.codProfilo);
  }

  initProfilo(profilo: ListaProfilo) {
    this.profiloForm = this.fb.group({
      codProfilo: [profilo.codProfilo, [Validators.required, this.noWhitespaceValidator]],
      descProfilo: [profilo.descProfilo, [Validators.required, this.noWhitespaceValidator]]
    })
    this.profiloForm.controls.codProfilo.disable();
  }
  noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length? null : { 'whitespace': true };       
  }
  conferma(){
    this.profilo = new ListaProfilo();
    this.profilo.codProfilo = this.profiloForm.controls.codProfilo.value;
    this.profilo.descProfilo = this.profiloForm.controls.descProfilo.value;
    if(this.azioni.length!=0){
      this.profilo.azioniDaCopiare = this.azioni;
    }
		this.dialogRef.close(this.profilo);
	}

  annulla(){
		this.dialogRef.close(null);
	}

  VerificaValidita(){
    return !this.profiloForm.valid;
  }

  verificaZeroAzioni(){
    if(this.data.profilo.azioni.length!=0){
      return true;
    }
    return false;
  }

  onSelectionChangedProfili(value:number){
    if(value!=0){
      let p:ListaProfilo = this.data.profili.find((e)=> e.idProfilo == value);
      this.azioni = p.azioni;
    }else{
      this.azioni = [];
    }
  }
}
