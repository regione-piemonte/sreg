import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NuovaPrestazioneComponent } from '@greg-operatore/components/configuratore-prestazioni/nuova-prestazione/nuova-prestazione.component';
import { ContainerOperatoreComponent } from '@greg-operatore/components/container-operatore.component';
import { NuovoEnteComponent } from '@greg-operatore/components/nuovo-ente/nuovo-ente.component';
import { ContainerResponsabileComponent } from '@greg-responsabile/components/container-responsabile.component';
import { MultienteComponent } from '@greg-responsabile/components/multiente/multiente.component';
import { DatiAnagraficiComponent } from '@greg-shared/dati-anagrafici/dati-anagrafici.component';
import { RedirectPageComponent } from '@greg-shared/redirect-page/redirect-page.component';
import { SelezioneProfiloApplicativoComponent } from './pages/selezione-profilo-applicativo/selezione-profilo-applicativo.component';
import { DatiAnagraficiArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/dati-anagrafici-archivio/dati-anagrafici-archivio.component';
import { ContainerResponsabileArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/responsabile-ente-archivio/components/container-responsabile-archivio.component';
import { ContainerGestioneUtentiComponent } from '@greg-operatore/components/gestione-utenti/components/container-gestione-utenti.component';


const routes: Routes = [
    {
        path: '',
        children: [
            //{
            //    path: '',
            //    pathMatch: 'full',
            //    redirectTo: 'operatore-regionale'
            //},
            {
                path: 'operatore-regionale',
                component: ContainerOperatoreComponent,
                loadChildren: () => import('@greg-operatore/operatore-regionale.module').then(m => m.OperatoreRegionaleModule),
                
            },
            {
                path: 'responsabile-ente',
                component: ContainerResponsabileComponent,
                loadChildren: () => import('@greg-responsabile/responsabile-ente.module').then(m => m.ResponsabileEnteModule)
            },
			{
                path: 'responsabile-ente-archivio',
                component: ContainerResponsabileArchivioComponent,
                loadChildren: () => import('@greg-shared/archivio-dati-rendicontazione/responsabile-ente-archivio/responsabile-ente-archivio.module').then(m => m.ResponsabileEnteArchivioModule)
            },
            {
                path: 'responsabile-multiente',
                component: MultienteComponent
            },
            {
                path: 'redirect-page',
                component: RedirectPageComponent
            },
			{
                path: 'selezione-profilo-applicativo',
                component: SelezioneProfiloApplicativoComponent
            },
            {
                path: 'dati-anagrafici',
                component: DatiAnagraficiComponent
            },
            {
                path: 'nuovo-ente',
                component: NuovoEnteComponent
            },
            {
                path: 'nuova-prestazione',
                component: NuovaPrestazioneComponent
            },
			{
                path: 'dati-anagrafici-archivio',
                component: DatiAnagraficiArchivioComponent
            }
            // {
            //     path: '**',
            //     pathMatch: 'full',
            //     redirectTo: 'operatore-regionale'
            // }
        ]
    }    
];

@NgModule( {

    imports: [RouterModule.forRoot( routes, {enableTracing: false} )],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
