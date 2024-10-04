import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Navigation, NavigationExtras, Router } from '@angular/router';
import { EnteGreg } from '@greg-app/app/dto/EnteGreg';
import { RicercaGregOutput } from '@greg-app/app/dto/RicercaGregOutput';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-multiente',
  templateUrl: './multiente.component.html',
  styleUrls: ['./multiente.component.css']
})
export class MultienteComponent implements OnInit, AfterViewInit {
  
  navigation: Navigation;
  idEnte : any[] = [];
  denominazioneEnte: EnteGreg;
  disabled: boolean;
  visible: boolean;
  displayedColumns: string[] = ['codiceRegionale', 'statoRendicontazione.descStatoRendicontazione', 'denominazione', 'comuneSedeLegale', 'tipoEnte', 'datiEnte'];
  dataListaRichieste: MatTableDataSource<RicercaGregOutput[]>;
  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  
  constructor(private router: Router, private route: ActivatedRoute, 
    public client: GregBOClient, private modalService: NgbModal, public toastService: AppToastService) {
      this.navigation = this.router.getCurrentNavigation();
      this.idEnte = this.navigation.extras? this.navigation.extras.state.idEnte : null;
    }

  getProperty = (obj, path) => (
    path.split('.').reduce((o, p) => o && o[p], obj)
  )

  ngOnInit() {
    this.dataListaRichieste = new MatTableDataSource<RicercaGregOutput[]>();
    this.client.idFeedback = null;
    this.client.messaggioFeedback = null;

    this.route.data.subscribe(v => this.visible = v.visible);
    this.route.data.subscribe(v => this.disabled = v.disabled);

    this.client.spinEmitter.emit(true);
    this.client.getSchedeMultiEntiGestori( this.idEnte ).subscribe( (response: RicercaGregOutput[]) => {
      this.client.spinEmitter.emit(false);
      this.dataListaRichieste.data = response as any;
    },
    err => {
      this.client.spinEmitter.emit(false);
    });

    this.dataListaRichieste.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    this.dataListaRichieste.sort = this.sort;
  }

  ngAfterViewInit() {
    this.dataListaRichieste.paginator = this.paginator;
    this.dataListaRichieste.sort = this.sort;
  }

  routeTo(content: RicercaGregOutput, path: string){
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route, 
      skipLocationChange: true,
      state: {
        idEnte: content.idSchedaEnteGestore,
      //  statoRendicontazione: content.statoRendicontazione.codStatoRendicontazione,
        tipoEnte: content.tipoEnte.codTipoEnte
      }
    };
    this.router.navigate([path], navigationExtras);
  }
}
