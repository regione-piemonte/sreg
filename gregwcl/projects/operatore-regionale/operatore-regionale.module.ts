import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OperatoreRegionaleRoutingModule } from './operatore-regionale-routing.module';
import { ContainerOperatoreComponent } from './components/container-operatore.component';
import { TabOperatoreComponent } from './components/tab-operatore/tab-operatore.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EntiGestoriAttiviComponent } from './components/enti-gestori-attivi/enti-gestori-attivi.component';
import { SharedComponentModule } from '@greg-shared/shared-components.module';
import { ConfiguratorePrestazioniComponent } from './components/configuratore-prestazioni/configuratore-prestazioni.component';
import { CruscottoComponent } from './components/cruscotto/cruscotto.component';
import { DataTablesModule } from 'angular-datatables';
import { ChiudiEnteComponent } from './components/chiudi-ente/chiudi-ente.component';
import { MaterialModule } from '@greg-app/app/material.module';
import { DateAdapter, MatDatepicker, MatDatepickerModule, MatNativeDateModule, MatSelectModule, MAT_DATE_LOCALE } from '@angular/material';
import { RipristinaEnteComponent } from './components/ripristina-ente/ripristina-ente.component';
import { UnisciEnteComponent } from './components/unisci-ente/unisci-ente.component';
import { NuovoEnteComponent } from './components/nuovo-ente/nuovo-ente.component';
import { NuovaPrestazioneComponent } from './components/configuratore-prestazioni/nuova-prestazione/nuova-prestazione.component';
import { CustomDateAdapter } from '@greg-app/app/format-datepicker';
import { SelezioneAnnoComponent } from './components/enti-gestori-attivi/selezione-anno/selezione-anno.component';
import { MessaggioPopupCreaAnnoComponent } from './components/enti-gestori-attivi/messaggio-popup-crea-anno/messaggio-popup-crea-anno.component';
import { ContainerGestioneUtentiComponent } from './components/gestione-utenti/components/container-gestione-utenti.component';
import { GestioneUtentiModule } from './components/gestione-utenti/gestione-utenti.module';
import { ConfiguratoreFnpsComponent } from './components/configuratore-fnps/configuratore-fnps.component';
import { TwoDigitDecimalNumberFnpsDirective } from '@greg-app/shared/directive/two-digit-decimal-number-fnps.directive';




@NgModule({
  declarations: [
    ContainerOperatoreComponent,
    TabOperatoreComponent,
    EntiGestoriAttiviComponent,
    ConfiguratoreFnpsComponent,
    ConfiguratorePrestazioniComponent,
    // ContainerGestioneUtentiComponent,
    CruscottoComponent,
    ChiudiEnteComponent,
    RipristinaEnteComponent,
    UnisciEnteComponent,
    NuovoEnteComponent,
    NuovaPrestazioneComponent,
    SelezioneAnnoComponent,
    MessaggioPopupCreaAnnoComponent,
    TwoDigitDecimalNumberFnpsDirective

  ],
  imports: [
    CommonModule,
    OperatoreRegionaleRoutingModule,
    GestioneUtentiModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    SharedComponentModule,
    DataTablesModule,
    MaterialModule
  ],
  exports: [
    TabOperatoreComponent,
    SelezioneAnnoComponent,
    MessaggioPopupCreaAnnoComponent,
    // ContainerGestioneUtentiComponent
  ],
  entryComponents: [
    ChiudiEnteComponent,
    RipristinaEnteComponent,
    UnisciEnteComponent,
    NuovoEnteComponent,
    NuovaPrestazioneComponent,
    SelezioneAnnoComponent,
    MessaggioPopupCreaAnnoComponent
  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    {provide: DateAdapter, useClass: CustomDateAdapter }
], 
})
export class OperatoreRegionaleModule { }
