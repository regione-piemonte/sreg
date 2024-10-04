import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArchivioDatiRendicontazioneComponent } from '@greg-shared/archivio-dati-rendicontazione/archivio-dati-rendicontazione.component';

import { DatiEnteArchivioComponent } from '../dati-ente-archivio/dati-ente-archivio.component';
import { DettaglioPrestazioneArchivioComponent } from '../dati-ente-archivio/dettaglio-prestazione-archivio/dettaglio-prestazione-archivio.component';
import { MacroaggregatiArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/macroaggregati-archivio/macroaggregati-archivio.component';
import { ModelloB1ArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modello-b1-archivio/modello-b1-archivio.component';
import { ModelloDArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modello-d-archivio/modello-d-archivio.component';
import { ModelloAArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloA-archivio/modelloA-archivio.component';
import { ModelloA1ArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloA1-archivio/modelloA1-archivio.component';
import { ModelloA2ArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloA2-archivio/modelloA2-archivio.component';
import { ModelloAllDArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloAllD-archivio/modelloAllD-archivio.component';
import { ModelloAlZeroArchivio } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloAlZero-archivio/modelloAlZero-archivio.component';
import { ModelloBArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloB-archivio/modelloB-archivio.component';
import { ModelloCArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloC-archivio/modelloC-archivio.component';
import { ModelloEArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloE-archivio/modelloE-archivio.component';
import { ModelloFArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloF-archivio/modelloF-archivio.component';
import { DatiRendicontazioneArchivioComponent } from '../dati-rendicontazione-archivio/dati-rendicontazione-archivio.component';


const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dati-ente-archivio'
      },
      {
        path: 'dati-ente-archivio',
        component: DatiEnteArchivioComponent
      },
      {
        path: 'dettaglio-prestazione-archivio',
        component: DettaglioPrestazioneArchivioComponent
      },
      {
        path: 'dati-rendicontazione-archivio',
        component: DatiRendicontazioneArchivioComponent,
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'modello-a-archivio'
          },
          {
            path: 'modello-a-archivio',
            component: ModelloAArchivioComponent
          },
          {
            path: 'modello-a1-archivio',
            component: ModelloA1ArchivioComponent
          },
          {
            path: 'modello-a2-archivio',
            component: ModelloA2ArchivioComponent
          },
          {
            path: 'modello-d-archivio',
            component: ModelloDArchivioComponent
          },
          {
            path: 'modello-b1-archivio',
            component: ModelloB1ArchivioComponent
          },
          {
            path: 'macroaggregati-archivio',
            component: MacroaggregatiArchivioComponent
          },
          {
            path: 'modello-b-archivio',
            component: ModelloBArchivioComponent
          },
          {
            path: 'modello-e-archivio',
            component: ModelloEArchivioComponent
          },
          {
            path: 'modello-c-archivio',
            component: ModelloCArchivioComponent
          },
          {
            path: 'modello-f-archivio',
            component: ModelloFArchivioComponent
          }
        ]
      },
      {
        path: 'all-d-archivio',
        component: ModelloAllDArchivioComponent
      },
      {
        path: 'al-zero-archivio',
        component: ModelloAlZeroArchivio
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
export class ResponsabileEnteArchivioRoutingModule { }