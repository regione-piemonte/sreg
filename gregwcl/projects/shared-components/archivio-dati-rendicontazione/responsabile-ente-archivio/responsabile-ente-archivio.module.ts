import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentModule } from '@greg-shared/shared-components.module';
import { DataTablesModule } from 'angular-datatables';
import { MaterialModule } from '@greg-app/app/material.module';
import { ContainerResponsabileArchivioComponent } from './components/container-responsabile-archivio.component';
import { TabResponsabileArchivioComponent } from './components/tab-responsabile-archivio/tab-responsabile-archivio.component';
import { MultienteArchivioComponent } from './components/multiente-archivio/multiente-archivio.component';
import { ResponsabileEnteArchivioRoutingModule } from './responsabile-ente-archivio-routing.module';

@NgModule({
  declarations: [
    ContainerResponsabileArchivioComponent,
    TabResponsabileArchivioComponent,
    MultienteArchivioComponent
  ],
  imports: [
    CommonModule,
    ResponsabileEnteArchivioRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    SharedComponentModule,
    DataTablesModule,
    MaterialModule
  ],
  exports: [
    TabResponsabileArchivioComponent
  ]
})
export class ResponsabileEnteArchivioModule { }
