import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { MsgApplicativo } from "@greg-app/app/dto/MsgApplicativo";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { MESSAGES } from "@greg-app/constants/greg-constants";

@Component({
  selector: 'app-messaggio-popup',
  templateUrl: './messaggio-popup.component.html',
  styleUrls: ['./messaggio-popup.component.css']
})
export class MessaggioPopupComponent implements OnInit {
  attenzione: string = '';
  tranche: string = '';
  messaggioCheck: string = '';
  
  constructor(private client: GregBOClient,
      public dialogRef: MatDialogRef<MessaggioPopupComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {tranche: string, titolo:string,warnings:string[],errors:string[],messaggio:string,esito:string,nota:string,chiudi:boolean, obblMotivazione:boolean, warningCheck:string, check: boolean}) {}

  ngOnInit() {
	this.client.getMsgApplicativo(MESSAGES.MSG_INVIO_ATTENZIONE).subscribe((msg: MsgApplicativo) => {
        this.tranche=this.data.tranche;
        this.attenzione = msg.testoMsgApplicativo. replace("TRANCHE", this.tranche);
});
  }

  checkMotivazione(){
    if(this.data.obblMotivazione && (this.data.nota==null || this.data.nota=="" || this.data.nota==" ")){
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

