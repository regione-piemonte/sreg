import { Component, Input, OnInit } from '@angular/core';
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { CronologiaGreg } from "@greg-app/app/dto/CronologiaGreg";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-cronologia',
  templateUrl: './cronologia.component.html',
  styleUrls: ['./cronologia.component.css']
})
export class CronologiaComponent implements OnInit {

  @Input() public idRendicontazione: number;
  @Input() public archive: boolean;

  constructor( private client: GregBOClient, public activeModal: NgbActiveModal ) { }
  
  displayedColumns: string[] = ['dataOra', 'utente', 'modello', 'statoRendicontazione', 'notaEnte', 'notaInterna'];
  listaCronologia: MatTableDataSource<CronologiaGreg[]>;
          
  ngOnInit() {
	this.client.spinEmitter.emit(true);
    this.listaCronologia = new MatTableDataSource<CronologiaGreg[]>();
    if(this.archive){
      this.client.getCronologiaArchivio(this.idRendicontazione).subscribe(
        (response: CronologiaGreg[]) => {
        this.listaCronologia.data = response as any;
      this.client.spinEmitter.emit(false);
	}
      )
    }
    else {
      this.client.getCronologia(this.idRendicontazione).subscribe(
        (response: CronologiaGreg[]) => {
        this.listaCronologia.data = response as any;
      this.client.spinEmitter.emit(false);
     }
      )
    }

  }


}
