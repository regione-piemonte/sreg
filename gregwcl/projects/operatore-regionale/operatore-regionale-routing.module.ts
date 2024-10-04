import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArchivioDatiRendicontazioneComponent } from '@greg-shared/archivio-dati-rendicontazione/archivio-dati-rendicontazione.component';
import { DatiEnteComponent } from '@greg-shared/dati-ente/dati-ente.component';
import { MacroaggregatiComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/macroaggregati/macroaggregati.component';
import { ModelloB1Component } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modello-b1/modello-b1.component';
import { ModelloDComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modello-d/modello-d.component';
import { ModelloAComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloA/modelloA.component';
import { ModelloA1Component } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloA1/modelloA1.component';
import { ModelloA2Component } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloA2/modelloA2.component';
import { ModelloBComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloB/modelloB.component';
import { ModelloEComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloE/modelloE.component';
import { ModelloCComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloC/modelloC.component';
import { DatiRendicontazioneComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione.component';
import { ConfiguratorePrestazioniComponent } from './components/configuratore-prestazioni/configuratore-prestazioni.component';
import { CruscottoComponent } from './components/cruscotto/cruscotto.component';
import { EntiGestoriAttiviComponent } from './components/enti-gestori-attivi/enti-gestori-attivi.component';
import { ModelloFComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloF/modelloF.component';
import { ModelloAllDComponent } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloAllD/modelloAllD.component';
import { DatiAnagraficiComponent } from '@greg-shared/dati-anagrafici/dati-anagrafici.component';
import { DettaglioPrestazione } from '@greg-app/app/dto/DettaglioPrestazione';
import { DettaglioPrestazioneComponent } from '@greg-shared/dati-ente/dettaglio-prestazione/dettaglio-prestazione.component';
import { NuovaPrestazioneComponent } from './components/configuratore-prestazioni/nuova-prestazione/nuova-prestazione.component';
import { DatiAnagraficiArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/dati-anagrafici-archivio/dati-anagrafici-archivio.component';
import { DettaglioPrestazioneArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/dati-ente-archivio/dettaglio-prestazione-archivio/dettaglio-prestazione-archivio.component';
import { DatiEnteArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/dati-ente-archivio/dati-ente-archivio.component';
import { ContainerGestioneUtentiComponent } from './components/gestione-utenti/components/container-gestione-utenti.component';
import { UtentiComponent } from './components/gestione-utenti/components/tab-gestione-utenti/utenti/utenti.component';
import { ProfiliComponent } from './components/gestione-utenti/components/tab-gestione-utenti/profili/profili.component';
import { ListeComponent } from './components/gestione-utenti/components/tab-gestione-utenti/liste/liste.component';
import { ConfiguratoreFnpsComponent } from './components/configuratore-fnps/configuratore-fnps.component';
import { ModelloAlZero } from '@greg-shared/dati-rendicontazione/dati-rendicontazione-tabs/modelloAlZero/modelloAlZero.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'enti-gestori-attivi'
        // redirectTo: 'gestione-utenti'
      },
      {
        path: 'enti-gestori-attivi',
        component: EntiGestoriAttiviComponent
      },
      {
        path: 'dati-ente',
        component: DatiEnteComponent
      },
      {
        path: 'gestione-utenti',
        component: ContainerGestioneUtentiComponent,
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'utenti'
          },
          {
            path: 'utenti',
            component: UtentiComponent
          },
          {
            path: 'profili',
            component: ProfiliComponent,
          },
          {
            path: 'liste',
            component: ListeComponent
          }
        ]
      },
      {
        path: 'dati-ente-archivio',
        component: DatiEnteArchivioComponent
      },
      {
        path: 'dettaglio-prestazione',
        component: DettaglioPrestazioneComponent
      },
      {
        path: 'dettaglio-prestazione-archivio',
        component: DettaglioPrestazioneArchivioComponent
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
            path: 'macroaggregati',
            component: MacroaggregatiComponent
          },
          {
            path: 'modello-b',
            component: ModelloBComponent
          },
          {
            path: 'modello-b1',
            component: ModelloB1Component
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
          },
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
      },
      {
        path: 'configuratore-prestazioni',
        component: ConfiguratorePrestazioniComponent,
      },
      {
        path: 'configuratore-fnps',
        component: ConfiguratoreFnpsComponent,
      },
      {
        path: 'cruscotto',
        component: CruscottoComponent,
      },
      {
        path: 'nuova-prestazione',
        component: NuovaPrestazioneComponent
      },
      {
        path: 'dati-anagrafici-archivio',
        component: DatiAnagraficiArchivioComponent
      }


    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OperatoreRegionaleRoutingModule { }
