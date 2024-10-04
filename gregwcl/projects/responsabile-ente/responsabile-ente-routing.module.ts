import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArchivioDatiRendicontazioneComponent } from '@greg-shared/archivio-dati-rendicontazione/archivio-dati-rendicontazione.component';
import { DatiEnteComponent } from '@greg-shared/dati-ente/dati-ente.component';
import { ModelloB1Component } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modello-b1/modello-b1.component';
import { MacroaggregatiComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/macroaggregati/macroaggregati.component';

import { ModelloDComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modello-d/modello-d.component';
import { ModelloAComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloA/modelloA.component';
import { ModelloA1Component } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloA1/modelloA1.component';
import { ModelloA2Component } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloA2/modelloA2.component';
import { ModelloBComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloB/modelloB.component';
import { ModelloCComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloC/modelloC.component';
import { DatiRendicontazioneComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione.component';
import { ModelloEComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloE/modelloE.component';
import { ModelloFComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloF/modelloF.component';
import { ModelloAllDComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloAllD/modelloAllD.component';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { DettaglioPrestazioneComponent } from '@greg-shared/dati-ente/dettaglio-prestazione/dettaglio-prestazione.component';
import { ModelloAlZero } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloAlZero/modelloAlZero.component';


const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dati-ente'
      },
      {
        path: 'dati-ente',
        component: DatiEnteComponent
      },
      {
        path: 'dettaglio-prestazione',
        component: DettaglioPrestazioneComponent
      },
      {
        path: 'dati-rendicontazione',
        component: DatiRendicontazioneComponent,
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'modello-a'
          },
          {
            path: 'modello-a',
            component: ModelloAComponent
          },
          {
            path: 'modello-a1',
            component: ModelloA1Component
          },
          {
            path: 'modello-a2',
            component: ModelloA2Component
          },
          {
            path: 'modello-d',
            component: ModelloDComponent
          },
          {
            path: 'modello-b1',
            component: ModelloB1Component
          },
          {
            path: 'macroaggregati',
            component: MacroaggregatiComponent
          },
          {
            path: 'modello-b',
            component: ModelloBComponent
          },
          {
            path: 'modello-e',
            component: ModelloEComponent
          },
          {
            path: 'modello-c',
            component: ModelloCComponent
          },
          {
            path: 'modello-f',
            component: ModelloFComponent
          }
        ]
      },
      {
        path: 'all-d',
        component: ModelloAllDComponent
      },
      {
        path: 'al-zero',
        component: ModelloAlZero
      },
      {
        path: 'archivio-dati-rendicontazione',
        component: ArchivioDatiRendicontazioneComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResponsabileEnteRoutingModule { }