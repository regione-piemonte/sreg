import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestioneUtentiRoutingModule } from './gestione-utenti-routing.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentModule } from '@greg-shared/shared-components.module';
import { DataTablesModule } from 'angular-datatables';
import { MaterialModule } from '@greg-app/app/material.module';
import { ContainerGestioneUtentiComponent } from './components/container-gestione-utenti.component';
import { TabGestioneUtentiComponent } from './components/tab-gestione-utenti/tab-gestione-utenti.component';
import { UtentiComponent } from './components/tab-gestione-utenti/utenti/utenti.component';
import { ProfiliComponent } from './components/tab-gestione-utenti/profili/profili.component';
import { ListeComponent } from './components/tab-gestione-utenti/liste/liste.component';

@NgModule({
  declarations: [
    ContainerGestioneUtentiComponent,
    TabGestioneUtentiComponent,
    UtentiComponent,
    ProfiliComponent,
    ListeComponent
  ],
  imports: [
    CommonModule,
    GestioneUtentiRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    SharedComponentModule,
    DataTablesModule,
    MaterialModule
  ],
  exports: [
    TabGestioneUtentiComponent
  ]
})
export class GestioneUtentiModule { }
