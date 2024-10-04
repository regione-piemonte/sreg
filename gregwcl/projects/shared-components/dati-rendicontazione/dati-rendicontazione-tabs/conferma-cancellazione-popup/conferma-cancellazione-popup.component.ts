import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
  selector: 'app-conferma-cancellazione-popup',
  templateUrl: './conferma-cancellazione-popup.component.html',
  styleUrls: ['./conferma-cancellazione-popup.component.css']
})
export class ConfermaCancellazionePopup implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<ConfermaCancellazionePopup>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, messaggio:string, delete:boolean}) {}

  ngOnInit() {
  }

	annullaCancellazione(){
    this.data.delete = false;
		this.dialogRef.close(this.data);
	}

  confermaCancellazione(){
    this.data.delete = true;
		this.dialogRef.close(this.data);
	}
}
