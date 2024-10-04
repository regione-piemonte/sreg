import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
  selector: 'app-canc-popup',
  templateUrl: './canc-popup.component.html',
  styleUrls: ['./canc-popup.component.css']
})
export class CancPopupComponent implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<CancPopupComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, messaggio:string}) {}

  ngOnInit() {
  }

  conferma(){
		this.dialogRef.close(true);
	}

  annulla(){
		this.dialogRef.close(false);
	}
}
