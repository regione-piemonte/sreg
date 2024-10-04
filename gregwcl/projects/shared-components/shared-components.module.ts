import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArchivioDatiRendicontazioneComponent } from './archivio-dati-rendicontazione/archivio-dati-rendicontazione.component';
import { DatiEnteComponent } from './dati-ente/dati-ente.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DatiRendicontazioneComponent } from './dati-rendicontazione/dati-rendicontazione.component';
import { DataTablesModule } from 'angular-datatables';
import { CronologiaComponent } from './cronologia/cronologia.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MaterialModule } from '@greg-app/app/material.module';
import { ModelloAComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloA/modelloA.component';
import { ModelloA1Component } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloA1/modelloA1.component';
import { ModelloA2Component } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloA2/modelloA2.component';
import { MacroaggregatiComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/macroaggregati/macroaggregati.component';
import { RouterModule } from '@angular/router';
import { TwoDigitDecimalNumberDirective } from '@greg-app/shared/directive/two-digit-decimal-number.directive';
import { ModelloDComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modello-d/modello-d.component';
import { CronologiaModelliComponent } from './cronologia-modelli/cronologia-modelli.component';
import { PulsantiFunzioniComponent } from './pulsanti-funzioni/pulsanti-funzioni.component';
import { CurrencyFormat } from '@greg-app/app/currencyFormatter';
import { ModelloB1Component } from './dati-rendicontazione/dati-rendicontazione-tabs/modello-b1/modello-b1.component';
import { MatDialogModule } from '@angular/material';
import { ChangeTabDialog } from './dati-rendicontazione/change-tab-dialog/change-tab-dialog.component';
import { ModelloBComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloB/modelloB.component';
import { ModelloCComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloC/modelloC.component';
import { MessaggioPopupComponent } from './dati-rendicontazione/messaggio-popup/messaggio-popup.component';
import { MotivazionePopupComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/motivazione-popup/motivazione-popup.component';
import { ModelloEComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloE/modelloE.component';
import { ConfermaCancellazionePopup } from './dati-rendicontazione/dati-rendicontazione-tabs/conferma-cancellazione-popup/conferma-cancellazione-popup.component';
import { InfoPopup } from './dati-rendicontazione/dati-rendicontazione-tabs/macroaggregati/info-popup/info-popup.component';
import { ModelloFComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloF/modelloF.component';
import { ModelloAllDComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloAllD/modelloAllD.component';

import { StatoEnteComponent } from './dati-anagrafici/stato-ente/stato-ente.component';
import { StoricoComponent } from '@greg-operatore/components/enti-gestori-attivi/Storico/storico.component';
import { DatiAnagraficiComponent } from './dati-anagrafici/dati-anagrafici.component';
import { DettaglioPrestazioneComponent } from './dati-ente/dettaglio-prestazione/dettaglio-prestazione.component';
import { MessaggioPopupCruscottoComponent } from '@greg-operatore/components/cruscotto/messaggio-popup-cruscotto/messaggio-popup-cruscotto.component';
import { CancPopupComponent } from '@greg-operatore/components/configuratore-prestazioni/canc-popup/canc-popup.component';
import { NuovaPrestazione2Component } from '@greg-operatore/components/configuratore-prestazioni/nuova-prestazione2/nuova-prestazione2component';
import { ModificaPrestazione2Component } from '@greg-operatore/components/configuratore-prestazioni/modifica-prestazione2/modifica-prestazione2.component';
import { DatiAnagraficiArchivioComponent } from 'projects/shared-components/archivio-dati-rendicontazione/dati-anagrafici-archivio/dati-anagrafici-archivio.component';
import { StoricoArchivioComponent } from './archivio-dati-rendicontazione/storico-archivio/storico-archivio.component';
import { DettaglioPrestazioneArchivioComponent } from './archivio-dati-rendicontazione/dati-ente-archivio/dettaglio-prestazione-archivio/dettaglio-prestazione-archivio.component';
import { DatiEnteArchivioComponent } from './archivio-dati-rendicontazione/dati-ente-archivio/dati-ente-archivio.component';
import { DatiRendicontazioneArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio.component';
import { MacroaggregatiArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/macroaggregati-archivio/macroaggregati-archivio.component';
import { ModelloAArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloA-archivio/modelloA-archivio.component';
import { ModelloA1ArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloA1-archivio/modelloA1-archivio.component';
import { ModelloA2ArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloA2-archivio/modelloA2-archivio.component';
import { ModelloAllDArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloAllD-archivio/modelloAllD-archivio.component';
import { ModelloBArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloB-archivio/modelloB-archivio.component';
import { ModelloB1ArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modello-b1-archivio/modello-b1-archivio.component';
import { ModelloCArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloC-archivio/modelloC-archivio.component';
import { ModelloDArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modello-d-archivio/modello-d-archivio.component';
import { ModelloEArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloE-archivio/modelloE-archivio.component';
import { ModelloFArchivioComponent } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloF-archivio/modelloF-archivio.component';
import { CronologiaModelliArchivioComponent } from './archivio-dati-rendicontazione/cronologia-modelli-archivio/cronologia-modelli-archivio.component';
import { StatoEnteArchivioComponent } from './archivio-dati-rendicontazione/dati-anagrafici-archivio/stato-ente-archivio/stato-ente-archivio.component';
import { ModificaUtentePopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/utenti/modifica-utente-popup/modifica-utente-popup.component';
import { CreaUtentePopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/utenti/crea-utente-popup/crea-utente-popup.component';
import { CreaProfiloPopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/profili/crea-profilo-popup/crea-profilo-popup.component';
import { ModificaProfiloPopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/profili/modifica-profilo-popup/modifica-profilo-popup.component';
import { ModificaListaPopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/liste/modifica-lista-popup/modifica-lista-popup.component';
import { CreaListaPopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/liste/crea-lista-popup/crea-lista-popup.component';
import { OpCambioStatoPopupComponent } from '@greg-operatore/components/op-cambio-stato-popup/op-cambio-stato-popup.component';
import { CancUtentePopupComponent } from '@greg-operatore/components/gestione-utenti/components/tab-gestione-utenti/utenti/canc-utente-popup/canc-utente-popup.component';
import { ModelloAlZero } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloAlZero/modelloAlZero.component';
import {MatCardModule} from '@angular/material/card';
import { ModelloAlZeroArchivio } from './archivio-dati-rendicontazione/dati-rendicontazione-archivio/dati-rendicontazione-archivio-tabs/modelloAlZero-archivio/modelloAlZero-archivio.component';
import { ConfermaValidazionePopupComponent } from './dati-rendicontazione/dati-rendicontazione-tabs/modelloAlZero/conferma-validazione-popup/conferma-validazione-popup';

@NgModule({
  declarations: [
    ArchivioDatiRendicontazioneComponent,
    CronologiaComponent,
    StoricoComponent,
    DatiEnteComponent,
    DatiAnagraficiComponent,
    DatiRendicontazioneComponent,
    ModelloAComponent,
    ModelloA1Component,
    ModelloA2Component,
    TwoDigitDecimalNumberDirective,
    CronologiaModelliComponent,
    PulsantiFunzioniComponent,
    ModelloDComponent,
    MacroaggregatiComponent,
    CronologiaModelliComponent,
    StatoEnteComponent,
    CurrencyFormat,
    ModelloBComponent,
    ModelloB1Component,
    ModelloAllDComponent,
    ModelloAlZero,
    ModelloEComponent,
    ModelloCComponent,
    ModelloFComponent,
    ChangeTabDialog,
    MessaggioPopupComponent,
    MessaggioPopupCruscottoComponent,
    MotivazionePopupComponent,
    ConfermaCancellazionePopup,
    InfoPopup,
    CancPopupComponent,
    CancUtentePopupComponent,
    OpCambioStatoPopupComponent,
    ModificaUtentePopupComponent,
    CreaUtentePopupComponent,
    ModificaProfiloPopupComponent,
    CreaProfiloPopupComponent,
    ModificaListaPopupComponent,
    CreaListaPopupComponent,
    NuovaPrestazione2Component,
    ModificaPrestazione2Component,
    DettaglioPrestazioneComponent,
    DatiAnagraficiArchivioComponent,
    StoricoArchivioComponent,
    DettaglioPrestazioneArchivioComponent,
    DatiEnteArchivioComponent,
    DatiRendicontazioneArchivioComponent,
    MacroaggregatiArchivioComponent,
    ModelloAArchivioComponent,
    ModelloA1ArchivioComponent,
    ModelloA2ArchivioComponent,
    ModelloAllDArchivioComponent,
    ModelloBArchivioComponent,
    ModelloB1ArchivioComponent,
    ModelloCArchivioComponent,
    ModelloDArchivioComponent,
    ModelloEArchivioComponent,
    ModelloFArchivioComponent,
    CronologiaModelliArchivioComponent,
    StatoEnteArchivioComponent,
    ModelloAlZeroArchivio,
    ConfermaValidazionePopupComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DataTablesModule,
    NgbModule,
    MaterialModule,
    RouterModule,
    MatDialogModule,
    MatCardModule
  ],
  exports: [
    ArchivioDatiRendicontazioneComponent,
    CronologiaComponent,
    StoricoComponent,
    DatiEnteComponent,
    DatiAnagraficiComponent,
    DatiRendicontazioneComponent,
    ModelloAComponent,
    ModelloA1Component,
    ModelloA2Component,
    ChangeTabDialog,
    ModelloBComponent,
    ModelloEComponent,
    MessaggioPopupComponent,
    MessaggioPopupCruscottoComponent,
    MotivazionePopupComponent,
    ConfermaCancellazionePopup,
    InfoPopup,
    CancPopupComponent,
    CancUtentePopupComponent,
    OpCambioStatoPopupComponent,
    ModificaUtentePopupComponent,
    CreaUtentePopupComponent,
    ModificaProfiloPopupComponent,
    CreaProfiloPopupComponent,
    ModificaListaPopupComponent,
    CreaListaPopupComponent,
    NuovaPrestazione2Component,
    ModificaPrestazione2Component,
    DettaglioPrestazioneComponent,
    DatiAnagraficiArchivioComponent,
    StoricoArchivioComponent,
    DettaglioPrestazioneArchivioComponent,
    DatiEnteArchivioComponent,
    DatiRendicontazioneArchivioComponent,
    ModelloAArchivioComponent,
    ModelloA1ArchivioComponent,
    ModelloA2ArchivioComponent,
    ModelloAllDArchivioComponent,
    ModelloBArchivioComponent,
    ModelloB1ArchivioComponent,
    ModelloCArchivioComponent,
    ModelloDArchivioComponent,
    ModelloEArchivioComponent,
    ModelloFArchivioComponent
  ],
  entryComponents: [
    CronologiaComponent,
    StoricoComponent,
    ChangeTabDialog,
    MessaggioPopupComponent,
    MessaggioPopupCruscottoComponent,
    MotivazionePopupComponent,
    ConfermaCancellazionePopup,
    InfoPopup,
    CancPopupComponent,
    CancUtentePopupComponent,
    OpCambioStatoPopupComponent,
    ModificaUtentePopupComponent,
    CreaUtentePopupComponent,
    ModificaProfiloPopupComponent,
    CreaProfiloPopupComponent,
    ModificaListaPopupComponent,
    CreaListaPopupComponent,
    NuovaPrestazione2Component,
    ModificaPrestazione2Component,
    DettaglioPrestazioneComponent,
    StoricoArchivioComponent,
    DettaglioPrestazioneArchivioComponent,
    DatiEnteArchivioComponent,
    ConfermaValidazionePopupComponent
  ]
})
export class SharedComponentModule { }
