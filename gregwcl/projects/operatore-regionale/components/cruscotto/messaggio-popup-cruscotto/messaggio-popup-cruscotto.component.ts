import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { MsgApplicativo } from "@greg-app/app/dto/MsgApplicativo";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { MESSAGES } from "@greg-app/constants/greg-constants";

@Component({
  selector: 'app-messaggio-popup',
  templateUrl: './messaggio-popup-cruscotto.component.html',
  styleUrls: ['./messaggio-popup-cruscotto.component.css']
})
export class MessaggioPopupCruscottoComponent implements OnInit {
  attenzione: string = '';
  tranche: string = '';
  messaggioCheck: string = '';
  
  constructor(private client: GregBOClient,
      public dialogRef: MatDialogRef<MessaggioPopupCruscottoComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string,warnings:string[],errors:string[],check:string[],chiudi:boolean, messaggio:string, denominazione: string, codObbl: string, val: boolean}) {}

  ngOnInit() {

  }

  controlloObbl(noWarn: boolean){
    if(this.data.warnings.length==0 && this.data.errors.length==0 && this.data.check.length==0 && (this.data.codObbl=='OB' || this.data.val) && noWarn){
      return true;
    } else if( this.data.errors.length==0 && this.data.check.length==0 && this.data.codObbl=='FA' && !noWarn && !this.data.val){
      return true;
    } 
    return false;
  }

	chiudiNo(data){
		this.data.chiudi=false;
		this.dialogRef.close(this.data);
	}
	chiudiSi(data){
		this.data.chiudi=true;
		this.dialogRef.close(this.data);
	}
}

