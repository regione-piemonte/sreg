import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResponsabileEnteRoutingModule } from './responsabile-ente-routing.module';
import { ContainerResponsabileComponent } from './components/container-responsabile.component';
import { TabResponsabileComponent } from './components/tab-responsabile/tab-responsabile.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentModule } from '@greg-shared/shared-components.module';
import { MultienteComponent } from './components/multiente/multiente.component';
import { DataTablesModule } from 'angular-datatables';
import { MaterialModule } from '@greg-app/app/material.module';

@NgModule({
  declarations: [
    ContainerResponsabileComponent,
    TabResponsabileComponent,
    MultienteComponent
  ],
  imports: [
    CommonModule,
    ResponsabileEnteRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    SharedComponentModule,
    DataTablesModule,
    MaterialModule
  ],
  exports: [
    TabResponsabileComponent
  ]
})
export class ResponsabileEnteModule { }
