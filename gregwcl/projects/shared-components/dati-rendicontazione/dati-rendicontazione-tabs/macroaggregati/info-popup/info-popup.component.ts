import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
  selector: 'app-info-popup',
  templateUrl: './info-popup.component.html',
  styleUrls: ['./info-popup.component.css']
})
export class InfoPopup implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<InfoPopup>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, messaggio:string}) {}

  ngOnInit() {
  }

  confermaInfo(){
		this.dialogRef.close(this.data);
	}
}
