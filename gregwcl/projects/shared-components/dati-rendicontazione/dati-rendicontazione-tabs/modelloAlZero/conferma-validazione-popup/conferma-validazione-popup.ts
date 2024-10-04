import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
  selector: 'conferma-validazione-popup',
  templateUrl: './conferma-validazione-popup.html',
  styleUrls: ['./conferma-validazione-popup.css']
})
export class ConfermaValidazionePopupComponent implements OnInit {
  giustificativo: string = '';

  constructor(public dialogRef: MatDialogRef<ConfermaValidazionePopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data) { 
      ({ giustificativo: this.giustificativo } = data);
    }

  ngOnInit() {
  }

  chiudiMotivazione(conferma: boolean = false) {
    conferma ? this.dialogRef.close('validazione') : this.dialogRef.close('');
  }
}
