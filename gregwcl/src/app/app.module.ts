/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgxLoadingModule } from 'ngx-loading';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule, Routes } from "@angular/router";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DataTablesModule } from 'angular-datatables';
import { NgbDateParserFormatter } from "@ng-bootstrap/ng-bootstrap";
import { MomentDateFormatter } from "@greg-app/app/MomentDateFormatter";
import { GregBOClient } from "@greg-app/app/GregBOClient";
import { CookieService } from "ngx-cookie-service";
import { BackofficeToolbarComponent } from '../shared/backoffice-toolbar/backoffice-toolbar.component';
import { ProjectToolbarComponent } from '../shared/project-toolbar/project-toolbar.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { OperatoreRegionaleModule } from '@greg-operatore/operatore-regionale.module';
import { ResponsabileEnteModule } from '@greg-responsabile/responsabile-ente.module';
import { HttpClientModule } from '@angular/common/http';
import { GregErrorService } from '@greg-app/shared/error/greg-error.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { ToastsContainerComponent } from '@greg-shared/toast/toasts-container.component';
import { CurrencyFormat } from './currencyFormatter';
import { PulsantiFunzioniComponent } from '@greg-shared/pulsanti-funzioni/pulsanti-funzioni.component';
import { RedirectPageComponent } from '@greg-shared/redirect-page/redirect-page.component';
import { SelezioneProfiloApplicativoComponent } from './pages/selezione-profilo-applicativo/selezione-profilo-applicativo.component';
import { AppDateAdapter, CustomDateAdapter } from './format-datepicker';
import { DateAdapter, MAT_DATE_LOCALE } from '@angular/material/core';
import { ResponsabileEnteArchivioModule } from '@greg-shared/archivio-dati-rendicontazione/responsabile-ente-archivio/responsabile-ente-archivio.module';
import { GestioneUtentiModule } from '@greg-operatore/components/gestione-utenti/gestione-utenti.module';
import {MatCardModule} from '@angular/material/card';



//import { TwoDigitDecimalNumberDirective } from '@greg-app/shared/directive/two-digit-decimal-number.directive';

const routes: Routes = [];


@NgModule( {
    declarations: [
        AppComponent,
        BackofficeToolbarComponent,
        ProjectToolbarComponent,
        FooterComponent,
        ToastsContainerComponent,
        RedirectPageComponent,
        SelezioneProfiloApplicativoComponent
  //      TwoDigitDecimalNumberDirective
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        DataTablesModule,
        NgbModule,
        AppRoutingModule,
        NgxLoadingModule.forRoot( {} ),
        OperatoreRegionaleModule,
        ResponsabileEnteModule,
		ResponsabileEnteArchivioModule,
        GestioneUtentiModule,
        BrowserAnimationsModule,
        MaterialModule,
        RouterModule,
        MatCardModule
    ],
    providers: [GregBOClient,
        { 
            provide: NgbDateParserFormatter, 
            useClass: MomentDateFormatter 
        },  
        CookieService,
        GregErrorService,
		PulsantiFunzioniComponent,
        CurrencyFormat,
        { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
        {provide: DateAdapter, useClass: CustomDateAdapter }
    ], 
    bootstrap: [AppComponent]
} )

export class AppModule { }
