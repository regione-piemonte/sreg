import { Component, Input, OnInit } from '@angular/core';
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { CronologiaGreg } from "@greg-app/app/dto/CronologiaGreg";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material/table';
import { StoricoGreg } from '@greg-app/app/dto/StoricoGreg';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'app-storico-archivio',
  templateUrl: './storico-archivio.component.html',
  styleUrls: ['./storico-archivio.component.css']
})
export class StoricoArchivioComponent implements OnInit {

  @Input() public idSchedaEnteGestore: number;
  @Input() public denominazione: string;
  @Input() public archive: boolean;
  @Input() public statoEnte: string;

  constructor(public client: GregBOClient, public activeModal: NgbActiveModal, private route: ActivatedRoute, private router: Router ) { }

  displayedColumns: string[] = ['Dal', 'Al', 'Operatore', 'Dettaglio'];
  listaStorico: MatTableDataSource<StoricoGreg[]>;

  ngOnInit() {
      this.client.spinEmitter.emit(true);
    this.listaStorico = new MatTableDataSource<StoricoGreg[]>();

    // if(this.archive){
    //   this.client.getCronologiaArchivio(this.idRendicontazione).subscribe(
    //     (response: CronologiaGreg[]) => 
    //     this.listaCronologia.data = response as any
    //   )
    // }
    // else {
    this.client.getStorico(this.idSchedaEnteGestore).subscribe(
      (response: StoricoGreg[]) =>{
        this.listaStorico.data = response as any;
      this.client.spinEmitter.emit(false);
    }
    )
    // }

  }

  openStorico(content: StoricoGreg, path: string){
      const navigationExtras: NavigationExtras = {
        relativeTo: this.route, 
        skipLocationChange: true,
        state: {
          idSchedaEnteGestore: this.idSchedaEnteGestore,
          dataFineValidita: content.al,
          dataInizioValidita: content.dal,
          statoEnte: this.statoEnte,
		  numeroperiodi:  this.listaStorico.data.length
        }
      };
	if (this.archive){
		    let currentUrl = this.router.url;
		 this.router.navigateByUrl('/', navigationExtras).then(() => {
     	 this.router.navigate([currentUrl], navigationExtras);
  		});
	}
	else {
      this.router.navigate([path], navigationExtras);
}
  this.activeModal.close();
  }

}
