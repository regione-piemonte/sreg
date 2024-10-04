import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
  selector: 'app-motivazione-popup',
  templateUrl: './motivazione-popup.component.html',
  styleUrls: ['./motivazione-popup.component.css']
})
export class MotivazionePopupComponent implements OnInit {
attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<MotivazionePopupComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, motivazione:string, placeholder: string, maxlength: number}) {}

  ngOnInit() {
  }

	chiudiMotivazione(){
		this.dialogRef.close(this.data);
	}
}
