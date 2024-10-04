import { Component, Inject, Input } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
  

@Component({
    selector: 'change-tab-dialog',
    templateUrl: 'change-tab-dialog.component.html',
  })
  export class ChangeTabDialog {
    constructor(
      public dialogRef: MatDialogRef<ChangeTabDialog>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string,messaggio:string}) {}
  
  }