import { Component, Inject,  OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";

@Component({
  selector: 'app-selezione-anno',
  templateUrl: './selezione-anno.component.html',
  styleUrls: ['./selezione-anno.component.css']
})
export class SelezioneAnnoComponent implements OnInit {
attenzione: string = '';

anno: number;
  constructor(public dialogRef: MatDialogRef<SelezioneAnnoComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, anni: number[]}) {}

  ngOnInit() {
    this.anno=this.data.anni[0];
  }

	chiudiMotivazione(){
		this.dialogRef.close(null);
	}

  confermoMotivazione(){
		this.dialogRef.close(this.anno);
	}

  onSelectionAnnoChanged(value: number){
    this.anno = value;
  }
}
