import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { MsgApplicativo } from "@greg-app/app/dto/MsgApplicativo";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { MESSAGES } from "@greg-app/constants/greg-constants";

@Component({
  selector: 'app-messaggio-popup-crea-anno',
  templateUrl: './messaggio-popup-crea-anno.component.html',
  styleUrls: ['./messaggio-popup-crea-anno.component.css']
})
export class MessaggioPopupCreaAnnoComponent implements OnInit {

  
  constructor(private client: GregBOClient,
      public dialogRef: MatDialogRef<MessaggioPopupCreaAnnoComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string,ok:string[],errors:string[],chiudi:boolean}) {}

  ngOnInit() {

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

