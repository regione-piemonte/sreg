import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListeComponent } from './components/tab-gestione-utenti/liste/liste.component';
import { ProfiliComponent } from './components/tab-gestione-utenti/profili/profili.component';
import { UtentiComponent } from './components/tab-gestione-utenti/utenti/utenti.component';


const routes: Routes = [
  {
    path: '',
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
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneUtentiRoutingModule { }