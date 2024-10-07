/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { EventEmitter, Injectable } from "@angular/core";
import { Observable, of, throwError } from "rxjs";
import { catchError } from 'rxjs/operators';
import { map } from "rxjs/internal/operators/map";
import * as uuid from 'uuid';
import { ConfigService } from "@greg-app/app/config.service";
import { UserInfoGreg } from "@greg-app/app/dto/UserInfoGreg";
import { RicercaGreg } from "@greg-app/app/dto/RicercaGreg";
import { CronologiaGreg } from "@greg-app/app/dto/CronologiaGreg";
import { ComuneAssociatoGreg } from "@greg-app/app/dto/ComuneAssociatoGreg";
import { PrestazioneAssociataGreg } from "@greg-app/app/dto/PrestazioneAssociataGreg";
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { GregError } from "@greg-app/shared/error/greg-error.model";
import { GregErrorService } from "@greg-app/shared/error/greg-error.service";
import { encodeAsHttpParams } from "@greg-app/shared/util";
import { StatoRendicontazioneGreg } from "@greg-app/app/dto/StatoRendicontazioneGreg";
import { ComuneGreg } from "@greg-app/app/dto/ComuneGreg";
import { TipoEnteGreg } from "@greg-app/app/dto/TipoEnteGreg";
import { ProvinciaGreg } from "@greg-app/app/dto/ProvinciaGreg";
import { PrestazioneGreg } from "@greg-app/app/dto/PrestazioneGreg";
import { RicercaGregOutput } from "@greg-app/app/dto/RicercaGregOutput";
import { RicercaArchivioGreg } from "@greg-app/app/dto/RicercaArchivioGreg";
import { EnteGreg } from "@greg-app/app/dto/EnteGreg";
import { AslGreg } from "@greg-app/app/dto/AslGreg";
import { AllegatoGreg } from "@greg-app/app/dto/AllegatoGreg";
import { CausaleGreg } from "@greg-app/app/dto/CausaleGreg";
import { VociModelloA1Greg } from "@greg-app/app/dto/VociModelloA1Greg";
import { DatiModelloA1Greg } from "@greg-app/app/dto/DatiModelloA1Greg";
import { GetTrasferimentiA2Output } from "@greg-app/app/dto/GetTrasferimentiA2Output";
import { SalvaModelloA2Greg } from "@greg-app/app/dto/SalvaModelloA2Greg";
import { TipoVoceD } from "@greg-app/app/dto/TipoVoceD";
import { VoceModD } from "@greg-app/app/dto/VoceModD";
import { RendicontazioneModD } from "@greg-app/app/dto/RendicontazioneModD";
import { SalvaModelloA1Greg } from "@greg-app/app/dto/SalvaModelloA1Greg";
import { MsgInformativo } from "@greg-app/app/dto/MsgInformativo";
import { MsgApplicativo } from "@greg-app/app/dto/MsgApplicativo";
import { SalvaModelloA } from '@greg-app/app/dto/SalvaModelloA';
import { VociModelloA } from '@greg-app/app/dto/VociModeloA';
import { Macroaggregati } from "@greg-app/app/dto/Macroaggregati";
import { SpesaMissione } from "@greg-app/app/dto/SpesaMissione";
import { InfoRendicontazioneOperatore } from "@greg-app/app/dto/InfoRendicontazioneOperatore";
import { GenericResponseWarnErrGreg } from "@greg-app/app/dto/GenericResponseWarnErrGreg";
import { DatiEnteToSave } from "@greg-app/app/dto/DatiEnte";
import { ResponseSalvaModelloGreg } from "@greg-app/app/dto/ResponseSalvaModelloGreg";
import { RendicontazioneMacroaggregati } from "./dto/RendicontazioneMacroaggregati";
import { InviaModelli } from "./dto/InviaModelli";
import { ModelPrestazioniB1, ModelRendicontazioneTotaliMacroaggregati } from "./dto/VociModelloB1";
import { MissioniB } from "./dto/MissioniB";
import { CookieService } from "ngx-cookie-service";
import { NavigationExtras, Router } from "@angular/router";
import { ModelTabTranche } from "./dto/ModelTabTranche";
import { ModelloB1ElencoLbl } from "./dto/ModelloB1ElencoLbl";
import { SalvaModelloB1 } from "./dto/SalvaModelloB1";
import { RendicontazioneModB } from "./dto/RendicontazioneModB";
import { RendicontazioneTotaliSpeseMissioni } from "./dto/RendicontazioneTotaliSpeseMissioni";
import { RendicontazioneTotaliMacroaggregati } from "./dto/RendicontazioneTotaliMacroaggregati";
import { ParametroGreg } from "./dto/ParametroGreg";
import { AttivitaSocioAssist } from "./dto/AttivitaSocioAssist";
import { EsportaModelloA1Greg } from "./dto/EsportaModelloA1Greg";
import { SalvaModelloEGreg } from "./dto/SalvaModelloEGreg";
import { ResponseEsportaModelliGreg } from "./dto/ResponseEsportaModelliGreg";
import { RendicontazioneModE } from "./dto/RendicontazioneModE";
import { EsportaModelloA2Greg } from "./dto/EsportaModelloA2Greg";
import { EsportaModelloA } from "./dto/EsportaModelloA";
import { EsportaModelloDGreg } from "./dto/EsportaModelloDGreg";
import { PrestazioniC } from "./dto/PrestazioniC";
import { RendicontazioneModC } from "./dto/RendicontazioneModC";
import { ProgrammiMissioneTotaliModB } from "./dto/ProgrammiMissioneTotaliModB";
import { EsportaMacroaggregatiGreg } from "./dto/EsportaMacroaggregatiGreg";
import { ConteggiF } from "./dto/ConteggiF";
import { RendicontazioneModF } from "./dto/RendicontazioneModF";
import { EnteGregWithComuniAss } from "./dto/EnteGregWithComuniAss";
import { GenericResponseGreg } from "./dto/GenericResponseGreg";
import { EsportaModelloBGreg } from "./dto/EsportaModelloBGreg";
import { EsportaModelloB1Greg } from "./dto/EsportaModelloB1Greg";
import { EsportaModelloEGreg } from "./dto/EsportaModelloEGreg";
import { EsportaModelloCGreg } from "./dto/EsportaModelloCGreg";
import { EsportaModelloFGreg } from "./dto/EsportaModelloFGreg";
import { VociAllD } from "./dto/VociAllD";
import { RendicontazioneModAllD } from "./dto/RendicontazioneModAllD";
import { EsportaModuloFnps } from "./dto/EsportaModuloFnps";
import { TIPO_PROFILO } from "@greg-app/constants/greg-constants";
import { ProfiloGreg } from "./dto/ProfiloGreg";
import { StatoEnte } from "./dto/StatoEnte";
import { RendicontazioneEnteGreg } from "./dto/RendicontazioneEnteGreg";
import { DatiEnteGreg } from "./dto/DatiEnteGreg";
import { ModelObbligo } from "./dto/ModelObbligo";
import { AnagraficaEnteGreg } from "./dto/AnagraficaEnteGreg";
import { DatiAnagraficiToSave } from "./dto/DatiAnagraficiToSave";
import { StoricoGreg } from "./dto/StoricoGreg";
import { Motivazioni } from "./dto/Motivazioni";
import { ChiusuraEnte } from "./dto/ChiusuraEnte";
import { StatoEnteGreg } from "./dto/StatoEnteGreg";
import { LinkModelli } from "./dto/LinkModelli";
import { CronologiaProfilo } from "./dto/CronologiaProfilo";
import { RicercaListaEntiDaunire } from "./dto/RicercaListaEntiDaunire";
import { UnisciEnte } from "./dto/UnisciEnte";
import { ListaGreg } from "./dto/ListaGreg";
import { SalvaMotivazioneCheck } from "./dto/SalvaMotivazioneCheck";
import { DettaglioPrestazione } from "./dto/DettaglioPrestazione";
import { RicercaGregCruscotto } from "./dto/RicercaGregCruscotto";
import { ModelTabTrancheCruscotto } from "./dto/ModelTabTrancheCruscotto";
import { StatiCruscotto } from "./dto/StatiCruscotto";
import { InfoModello } from "./dto/InfoModello";
import { GenericResponseWarnCheckErrGreg } from "./dto/GenericResponseWarnCheckErrGreg";
import { ListeConfiguratore } from "./dto/ListeConfiguratore";
import { DettaglioPrestazioneConf } from "./dto/DettaglioPrestazioneConf";
import { Prest1Prest2 } from "./dto/Prest1Prest2";
import { Prest1PrestMin } from "./dto/Prest1PrestMin";
import { Nomenclatore } from "./dto/Nomenclatore";
import { ListaEntiAnno } from "./dto/ListaEntiAnno";
import { PrestUtenza } from "./dto/PrestUtenza";
import { Utenti } from "./dto/Utenti";
import { RicercaUtenti } from "./dto/RicercaUtenti";
import { Abilitazioni } from "./dto/Abilitazioni";
import { RicercaProfilo } from "./dto/RicercaProfilo";
import { ListaProfilo } from "./dto/ListaProfilo";
import { ListaAzione } from "./dto/ListaAzione";
import { ListaLista } from "./dto/ListaLista";
import { ListaEnte } from "./dto/ListaEnte";
import { UtenzeFnps } from "./dto/UtenzeFnps";
import { ModelUtenzaAllD } from "./dto/ModelUtenzaAllD";
import { Fondi } from "./dto/Fondi";
import { DatiAllontanamentoZero, PrestazioniAllontanamentoZeroDTO } from "./dto/PrestazioniAllontanamentoZeroDTO";
import { FondiEnteAllontanamentoZeroDTO } from "./dto/FondiEnteAllontanamentoZeroDTO";
import { ValidazioneAlZero } from "./dto/ValidazioneAlZeroDTO";
import { PrestazioniAlZeroCSV_DTO } from "./dto/PrestazioniAlZeroCSV_DTO";
import { PrestazioniAlZeroFNPS } from "./dto/PrestazioniAllontanamentoZeroFnpsDTO";


const enum PathApi {
    login = '/loginController/login',
    logout = '/loginController/logout',
    selectprofilo = '/loginController/selectprofilo',
    // Messaggi
    getMsgApplicativo = '/liste/getMessaggioApplicativo',
    getMsgInformativi = '/liste/getMessaggiInformativi',
    getParametroPerInformativa = '/liste/getParametroPerInformativa',
    getMsgInformativiPerCod = '/liste/getMessaggiInformativipercodice',
    getParametro = '/liste/getParametro',

    // Ricerca Enti
    getListaStatoRendicontazione = '/entiGestoriAttivi/getListaStatoRendicontazione',
    getListaComuni = '/entiGestoriAttivi/getListaComuni',
    getListaTipoEnte = '/entiGestoriAttivi/getListaTipoEnte',
    getListaDenominazioni = '/entiGestoriAttivi/getListaDenominazioni',
    getListaDenominazioniWithComuniAssociati = '/entiGestoriAttivi/getListaDenominazioniWithComuniAssociati',
    searchSchedeEntiGestori = '/entiGestoriAttivi/searchSchedeEntiGestori',
    searchSchedeMultiEntiGestori = '/entiGestoriAttivi/searchSchedeMultiEntiGestori',
    getSchedaEnteGestore = '/entiGestoriAttivi/getSchedaEnteGestore',
    getCronologia = '/entiGestoriAttivi/getCronologia',
    getStorico = '/entiGestoriAttivi/getStorico',
    getStatiEnte = '/entiGestoriAttivi/getStatiEnte',
    getAnniEsercizio = '/entiGestoriAttivi/getAnniEsercizio',
    getCronologiaStato = '/datiEnte/getCronologiaStato',
    getLastStato = '/datiEnte/getLastStato',

    // Archivio Enti
    getListaStatoRendicontazioneArchivio = '/archivioDatiRendicontazione/getListaStatoRendicontazione',
    getListaComuniArchivio = '/archivioDatiRendicontazione/getListaComuni',
    getListaTipoEnteArchivio = '/archivioDatiRendicontazione/getListaTipoEnte',
    searchSchedeEntiGestoriArchivio = '/archivioDatiRendicontazione/searchArchivioRendicontazione',
    getCronologiaArchivio = '/archivioDatiRendicontazione/getCronologia',
    searchSchedeEntiGestoriRendicontazioneConclusa = '/archivioDatiRendicontazione/searchSchedeEntiGestoriRendicontazioneConclusa',
    getStatoRendicontazioneConclusa = '/archivioDatiRendicontazione/getStatoRendicontazioneConclusa',
    getAnniEsercizioArchivio = '/archivioDatiRendicontazione/getAnniEsercizio',
    // Dati Ente
    getListaProvince = '/datiEnte/getProvinciaSedeLegale',
    getSchedaEnte = '/datiEnte/getSchedaEnte',
    getSchedaAnagrafica = '/datiEnte/getSchedaAnagrafica',
    getSchedaAnagraficaStorico = '/datiEnte/getSchedaAnagraficaStorico',
    getPrestazioni = '/datiEnte/getPrestazioni',
    getPrestazioniResSemires = '/datiEnte/getPrestazioniResSemires',
    getComuniAssociati = '/datiEnte/getComuniAssociati',
    getComuniAnagraficaAssociati = '/datiEnte/getComuniAnagraficaAssociati',
    getComuniAnagraficaAssociatiStorico = '/datiEnte/getComuniAnagraficaAssociatiStorico',
    getPrestazioniAssociate = '/datiEnte/getPrestazioniAssociate',
    saveDatiEnte = '/datiEnte/saveDatiEnte',
    saveDatiAnagrafici = '/datiEnte/saveDatiAnagrafici',
    creaNuovoEnte = '/datiEnte/creaNuovoEnte',
    getAsl = '/datiEnte/getAsl',
    getDocumentiAssociati = '/datiEnte/getAllegatiAssociati',
    getAllegatoToDownload = '/datiEnte/getAllegatoToDownload',
    getListaPrestazioniValorizzateModA = '/datiEnte/getListaPrestazioniValorizzateModA',
    getListaPrestazioniValorizzateModB1 = '/datiEnte/getListaPrestazioniValorizzateModB1',
    getListaPrestazioniValorizzateModC = '/datiEnte/getListaPrestazioniValorizzateModC',
    getListaComuniValorizzatiModA1 = '/datiEnte/getListaComuniValorizzatiModA1',
    getListaComuniValorizzatiModA2 = '/datiEnte/getListaComuniValorizzatiModA2',
    getListaComuniValorizzatiModE = '/datiEnte/getListaComuniValorizzatiModE',
    getDatiEnteRendicontazione = '/datiEnte/getDatiEnteRendicontazione',
    getMotivazioniChiusura = '/datiEnte/getMotivazioniChiusura',
    closeEnte = '/datiEnte/closeEnte',
    ripristinoEnte = '/datiEnte/ripristinoEnte',
    getMotivazioniRipristino = '/datiEnte/getMotivazioniRipristino',
    getSchedaAnagraficaUnione = '/datiEnte/getEntiunione',
    unioneEnte = '/datiEnte/unioneEnte',
    getProvinciaComuneLibero = "/datiEnte/getProvinciaComuneLibero",
    getPrestazioneRegionale = "/datiEnte/getPrestazioneRegionale",
    getDataInizioMax = "/datiEnte/getDataInizioMax",


    // Rendicontazione
    getInfoRendicontazioneOperatore = '/datiRendicontazione/getInfoRendicontazioneOperatore',
    confermaDati1 = '/datiRendicontazione/confermaDati1',
    richiestaRettifica1 = '/datiRendicontazione/richiestaRettifica1',
    confermaDati2 = '/datiRendicontazione/confermaDati2',
    richiestaRettifica2 = '/datiRendicontazione/richiestaRettifica2',
    inviatranche = '/datiRendicontazione/inviaTranche',
    getmodellipertranche = '/datiRendicontazione/getModelliTranche',
    gettranchepermodello = '/datiRendicontazione/getTranchePerModello',
    valida = '/datiRendicontazione/valida',
    storicizza = '/datiRendicontazione/storicizza',
    getmodelliassociati = "/datiRendicontazione/getModelliAssociati",
    getmodelli = "/datiRendicontazione/getModelli",
    getAllObbligo = "/datiRendicontazione/getAllObbligo",
    getVerificaModelliVuoto = "/datiRendicontazione/getVerificaModelliVuoto",

    // Modello Allontanamento Zero
    getPrestazioniModelloAlZero = '/modelloAlZero/getTestiPrestazioniAlZero',
    getPrestazioniModelloAlZeroFNPS = '/modelloAlZero/getPrestazioniAdattamentoFnps',
    getFondiEnteModelloAlZero = '/modelloAlZero/getFondiEnteAlZero',
    getValidazioneModelloAlZero = '/modelloAlZero/getValidazioneAlZero',
    toggleValidazioneAlZero = '/modelloAlZero/toggleValidazioneAlZero',
    salvaDatiModelloAlZero = '/modelloAlZero/postModelloAlZero',
    esportaModuloAlZero = '/modelloAlZero/makeCsvAlZero',

    // Modello A1
    getVociModelloA1 = '/modelloA1/getVociModelloA1',
    getDatiModelloA1 = '/modelloA1/getDatiModelloA1',
    saveModelloA1 = '/modelloA1/saveModelloA1',
    esportaModelloA1 = '/modelloA1/esportaModelloA1',

    // Modello A2
    getCausali = '/modelloA2/getCausali',
    getCausaliStatiche = '/modelloA2/getCausaliStatiche',
    getTrasferimentiA2 = '/modelloA2/getTrasferimentiA2',
    saveModelloA2 = '/modelloA2/saveModelloA2',
    esportaModelloA2 = '/modelloA2/esportaModelloA2',

    // Modello D
    getTipoVoceD = '/modelloD/getTipoVoceD',
    getVoceModD = '/modelloD/getVociModD',
    getRendicontazioneModD = '/modelloD/getRendicontazioneModDByIdScheda',
    saveModelloD = '/modelloD/saveModelloD',
    esportaModelloD = '/modelloD/esportaModelloD',

    // Macroaggregati
    getMacroaggregatiTotali = '/macroaggregati/getRendicontazioneTotaliMacroaggregati',
    getMacroaggregati = '/macroaggregati/getMacroaggregati',
    getSpesaMissione = '/macroaggregati/getSpesaMissione',
    getRendicontazioneMacroaggregati = '/macroaggregati/getRendicontazioneMacroaggregatiByIdScheda',
    isMacroaggregatiCompiled = '/macroaggregati/isRendicontazioneTotaliMacroaggregatiCompiled',
    saveMacroaggregati = '/macroaggregati/saveMacroaggregati',
    esportaMacroaggregati = '/macroaggregati/esportaMacroaggregati',

    // Modello A
    getListaVociModA = '/modelloA/getListaVociModA',
    saveModelloA = '/modelloA/saveModelloA',
    esportaModelloA = '/modelloA/esportaModelloA',


    //Modello B1
    getPrestazioniModB1 = '/modellob1/getPrestazioni',
    getElencoLblModB1 = '/modellob1/getElencoLbl',
    saveModelloB1 = '/modellob1/saveModello',
    esportaModelloB1 = '/modellob1/esportaModelloB1',

    // ModelloB
    getMissioniModB = '/modelloB/getMissioniModB',
    getRendicontazioneModB = '/modelloB/getRendicontazioneModB',
    getRendicontazioneTotaliSpese = '/macroaggregati/getRendicontazioneTotaliSpese',
    getRendicontazioneTotaliMacroaggregati = '/macroaggregati/getRendicontazioneTotaliMacroaggregati',
    getProgrammiMissioneTotaliModB = '/modellob1/getProgrammiMissioneTotaliModB',
    saveModelloB = '/modelloB/saveModelloB',
    esportaModelloB = '/modelloB/esportaModelloB',
    esportaIstat = '/modelloB/esportaIstat',
    canActiveModB = '/modelloB/canActiveModB',

    // ModelloE
    getAttivitaSocioAssist = '/modelloE/getAttivitaSocioAssist',
    getRendicontazioneModE = '/modelloE/getRendicontazioneModE',
    saveModelloE = '/modelloE/saveModelloE',
    esportaModelloE = '/modelloE/esportaModelloE',

    // ModelloC
    getPrestazioniC = '/modelloC/getPrestazioniModC',
    getRendicontazioneModC = '/modelloC/getRendicontazioneModC',
    saveModelloC = '/modelloC/saveModelloC',
    esportaModelloC = '/modelloC/esportaModelloC',

    // ModelloF
    getConteggiF = '/modelloF/getConteggiModF',
    getRendicontazioneModF = '/modelloF/getRendicontazioneModF',
    saveModelloF = '/modelloF/saveModelloF',
    esportaModelloF = '/modelloF/esportaModelloF',

    // Modello All D
    getVociAllD = '/modelloAllD/getVociAllD',
    getRendicontazioneModAllD = '/modelloAllD/getRendicontazioneModAllD',
    compilabileModelloAllD = '/modelloAllD/compilabileModelloAllD',
    saveModelloAllD = '/modelloAllD/saveModelloAllD',
    canActiveModFnps = '/modelloAlZero/canActiveModFnps',
    esportaModuloFnps = '/modelloAllD/esportaModuloFnps',

    //check
    saveMotivazioneCheck = '/datiRendicontazione/saveMotivazioneCheck',
    checkModelloA = '/modelloA/checkModelloA',
    checkModelloA1 = '/modelloA1/checkModelloA1',
    checkMacroaggregati = '/macroaggregati/checkMacroaggregati',
    checkModelloB1 = '/modellob1/checkModelloB1',
    checkModelloB = '/modelloB/checkModelloB',
    checkModelloC = '/modelloC/checkModelloC',
    checkModelloF = '/modelloF/checkModelloF',


    //cruscotto
    searchSchedeEntiGestoriCruscotto = '/cruscotto/searchSchedeEntiGestori',
    getModelliCruscotto = "/cruscotto/getModelli",
    getStati = '/cruscotto/getStati',
    getInfoModello = '/cruscotto/getInfoModello',
    getMaxAnnoRendicontazione = '/cruscotto/getMaxAnnoRendicontazione',

    //Configuratore
    getPrestazioniReg1 = '/configuratore/getPrestazioni',
    getPrestazioneRegionale1 = '/configuratore/getPrestazioneRegionale1',
    getTipologie = '/configuratore/getTipologie',
    getStrutture = '/configuratore/getStrutture',
    getQuote = '/configuratore/getQuote',
    getPrestColl = '/configuratore/getPrestColl',
    getMacroaggregatiConf = '/configuratore/getMacroaggregati',
    getUtenzeConf = '/configuratore/getUtenze',
    getMissioneProgConf = '/configuratore/getMissioneProg',
    getSpeseConf = '/configuratore/getSpese',
    getPrestazioni2Conf = '/configuratore/getPrestazioni2',
    getPrestazioniMinConf = '/configuratore/getPrestazioniMin',
    savePrestazione = '/configuratore/savePrestazione',
    getIstatConf = '/configuratore/getIstatConf',
    getUtenzeIstatConf = '/configuratore/getUtenzeIstatConf',
    getNomenclatoreConf = '/configuratore/getNomenclatoreConf',
    savePrestazione2 = '/configuratore/savePrestazione2',
    modificaPrestazione2 = '/configuratore/modificaPrestazione2',
    modificaPrestazione = '/configuratore/modificaPrestazione',
    getPrestazioneRegionale2 = '/configuratore/getPrestazioneRegionale2',
    getUtenzeIstatTransConf = '/configuratore/getUtenzeIstatTransConf',

    //CreaAnno
    creaNuovoAnno = '/entiGestoriAttivi/creaNuovoAnno',
    //Concludi
    concludiRendicontazione = '/entiGestoriAttivi/concludiRendicontazione',
    ripristinaRendicontazione = '/entiGestoriAttivi/ripristinaRendicontazione',
    ripristinaCompilazione = '/entiGestoriAttivi/ripristinaCompilazione',

    //GestioneUtenti
    getUtenti = '/gestioneUtenti/getUtenti',
    getProfili = '/gestioneUtenti/getProfili',
    getListe = '/gestioneUtenti/getListe',
    getEnti = '/gestioneUtenti/getEnti',
    eliminaAbilitazione = '/gestioneUtenti/eliminaAbilitazione',
    eliminaUtente = '/gestioneUtenti/eliminaUtente',
    modificaUtente = '/gestioneUtenti/modificaUtente',
    modificaAbilitazioni = '/gestioneUtenti/modificaAbilitazioni',
    getAbilitazioni = '/gestioneUtenti/getAbilitazioni',
    creaUtente = '/gestioneUtenti/creaUtente',
    getListaProfili = '/gestioneUtenti/getListaProfili',
    getAzioni = '/gestioneUtenti/getAzioni',
    eliminaAzione = '/gestioneUtenti/eliminaAzione',
    eliminaProfilo = '/gestioneUtenti/eliminaProfilo',
    modificaAzioni = '/gestioneUtenti/modificaAzioni',
    getAzioniProfilo = '/gestioneUtenti/getAzioniProfilo',
    modificaProfilo = '/gestioneUtenti/modificaProfilo',
    creaProfilo = '/gestioneUtenti/creaProfilo',
    getLista = '/gestioneUtenti/getLista',
    eliminaLista = '/gestioneUtenti/eliminaLista',
    modificaLista = '/gestioneUtenti/modificaLista',
    getListaEnti = '/gestioneUtenti/getListaEnti',
    getEntiLista = '/gestioneUtenti/getEntiLista',
    eliminaEnte = '/gestioneUtenti/eliminaEnte',
    modificaEnti = '/gestioneUtenti/modificaEnti',
    creaLista = '/gestioneUtenti/creaLista',
    storicizzaMultiplo = '/entiGestoriAttivi/storicizzaMultiplo',

    //ConfiguratoreFnps
    getUtenzeFnps = '/configuratoreFnps/getUtenze',
    getUtenzeSelezionabiliFnps = '/configuratoreFnps/getUtenzeFnps',
    creaUtenzeFnps = '/configuratoreFnps/creaUtenzeFnps',
    eliminaUtenzaFnps = '/configuratoreFnps/eliminaUtenzaFnps',
    getUtenzeByAnno = '/configuratoreFnps/getUtenzeByAnno',
    getFondiEnte = '/configuratoreFnps/getFondiEnte',
    getFondiAnno = '/configuratoreFnps/getFondiAnno',
    getRegole = '/configuratoreFnps/getRegole',
    eliminaFondo = '/configuratoreFnps/eliminaFondo',
}

@Injectable()
export class GregBOClient {

    public myUUIDV4 = uuid.v4();
    public messaggioFeedback: string = null;
    public idFeedback: number = null;

    baseUrl = ConfigService.getBEServer();

    public spinEmitter: EventEmitter<boolean> = new EventEmitter();
    public buttonViewEmitter: EventEmitter<boolean> = new EventEmitter();
    public utente: Observable<UserInfoGreg>;
    public listaenti: any[] = [];
    public listaEntiArc: any[] = [];
    public cronologia: CronologiaGreg = new CronologiaGreg();
    public notSavedEvent = new CustomEvent('notSavedEvent', { bubbles: true });
    public changeTabEmitter = new CustomEvent('changeTabEmitter', { bubbles: true });
    public updateCronoEmitter = new CustomEvent('updateCronoEmitter', { bubbles: true });
    public operazione: string = "";
    public readOnly: boolean;
    public readOnlyII: boolean;
    public readOnlyIII: boolean;
    public mostrabottoniera: boolean;
    public nomemodello: string;
    public componinote: boolean;
    public statorendicontazione: string;
    public utenteloggato: UserInfoGreg;
    // sezione azioni booleani
    public SalvaModelliI: boolean;
    public InviaI: boolean;
    public InviaII: boolean;
    public RichiestaRettificaI: boolean;
    public ConfermaDatiI: boolean;
    public RichiestaRettificaII: boolean;
    public ConfermaDatiII: boolean;
    public Valida: boolean;
    public Storicizza: boolean;
    public SalvaModelliII: boolean;
    public SalvaModelliIII: boolean;

    // ALLONTANAMENTO ZERO
    public SalvaModelliIV: boolean;
    public ValidaAlZero: boolean;
    public AnnullaValidazioneAlZero: boolean;
    // --------------------

    public CheckI: boolean;
    public CheckII: boolean;
    public Concludi: boolean;
    public RiapriRendicontazione: boolean;
    public inviaIIFatto: boolean = false;
    public inviaIFatto: boolean = false;
    public azioni = new Map<string, boolean[]>();
    public profilo: ProfiloGreg;
    public listaSelezionata: ListaGreg;
    public profiloSelezionato: ProfiloGreg;
    public selectedLinkRend: any;
    public ricercaEnte: RicercaGregOutput[] = [];
    public ricercaEnteArchivio: RicercaGregOutput[] = [];
    public filtroEnte: RicercaGreg;
    public filtroEnteArchivio: RicercaGreg;
    public paginaSalvata: number = 0;
    public ordinamentoSalvato: string = null;
    public versoSalvato = null;
    public statiEnteSalvato: StatoEnte[] = [];
    public statiEnteSalvatoArchivio: StatoEnte[] = [];
    public listaStatiSalvato: StatoRendicontazioneGreg[] = [];
    public statoRendicontazioneConslusaArchivio: StatoRendicontazioneGreg;
    public listaStatiSalvatoArchivio: StatoRendicontazioneGreg[] = [];
    public listaComuniSalvato: ComuneGreg[] = [];
    public listaComuniSalvatoArchivio: ComuneGreg[] = [];
    public listaTipoEntiSalvato: TipoEnteGreg[] = [];
    public listaTipoEntiSalvatoArchivio: TipoEnteGreg[] = [];
    public listaDenominazioniEntiSalvato: EnteGreg[] = [];
    public listaDenominazioniEntiSalvatoArchivio: EnteGreg[] = [];
    public anniEsercizioSalvato: number[] = [];
    public anniEsercizioSalvatoArchivio: number[] = [];
    public tooltipRendicontazione: string = null;
    public listaComuniinitial: ComuneGreg[] = [];
    public listaComuniinitialArchivio: ComuneGreg[] = [];
    public listaComuniinitialCruscotto: ComuneGreg[] = [];
    public dettaglioPrestazione: boolean = false;
    public ricercaEnteCruscotto: RicercaGregOutput[] = [];
    public filtroEnteCruscotto: RicercaGregCruscotto;
    public paginaSalvataCruscotto: number = 0;
    public statiEnteSalvatoCruscotto: StatoEnte[] = [];
    public listaStatiSalvatoCruscotto: StatoRendicontazioneGreg[] = [];
    public listaComuniSalvatoCruscotto: ComuneGreg[] = [];
    public listaTipoEntiSalvatoCruscotto: TipoEnteGreg[] = [];
    public listaDenominazioniEntiSalvatoCruscotto: EnteGreg[] = [];
    public anniEsercizioSalvatoCruscotto: number[] = [];
    public modelliSalvatoCruscotto: ModelTabTrancheCruscotto[] = [];
    public statiRendicontazioneCruscotto: StatiCruscotto[] = [];
    public cruscotto: boolean = false;
    public goToCruscotto: boolean = false;
    public goToConfiguratore: boolean = false;
    public goToNuovaPrestazione: boolean = false;
    public nuovaPrestazione: boolean = false;
    public goToArchivio: boolean = false;
  goToFnps: boolean;
	modello: string;
    constructor(private http: HttpClient, private gregError: GregErrorService, private cookieService: CookieService, private router: Router) { }

    getHeader() {
        return new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            //'Access-Control-Allow-Origin': '*',
            // responseType: 'text',
            // Authorization: 'my-auth-token',
            // 'Authorization': 'Bearer my-token', 
            // 'My-Custom-Header': 'foobar',
            'XSRF-TOKEN': this.cookieService.get("XSRF-TOKEN"),
            'X-XSRF-TOKEN': this.cookieService.get("XSRF-TOKEN"),
            // 'X-Request-ID': uuid()
        })
    }

    login(): Observable<UserInfoGreg> {
        return this.http.get(this.baseUrl + PathApi.login
            // , {headers: this.getHeader()}
        ).pipe(
            map((response: any) => {
                this.utente = of(response as UserInfoGreg);
                return response as UserInfoGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    logout(): Observable<UserInfoGreg> {
        return this.http.get(this.baseUrl + PathApi.logout
        ).pipe(
            map((response: any) => {
                this.utente = of(response as UserInfoGreg);
                return response as UserInfoGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaStatoRendicontazione(): Observable<StatoRendicontazioneGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getListaStatoRendicontazione
        ).pipe(
            map((response: any) => {
                return response as StatoRendicontazioneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaComuni(dataValidita: string): Observable<ComuneGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getListaComuni, { ...encodeAsHttpParams({ dataValidita: dataValidita }) }
        ).pipe(
            map((response: any) => {
                return response as ComuneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaTipoEnte(): Observable<TipoEnteGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getListaTipoEnte
        ).pipe(
            map((response: any) => {
                return response as TipoEnteGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaDenominazioni(): Observable<EnteGreg[]> {

        return this.http.get<EnteGreg>(this.baseUrl + PathApi.getListaDenominazioni
        ).pipe(
            map((response: any) => {
                return response as EnteGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaDenominazioniWithComuniAssociati(): Observable<EnteGregWithComuniAss[]> {

        return this.http.get<EnteGregWithComuniAss[]>(this.baseUrl + PathApi.getListaDenominazioniWithComuniAssociati
        ).pipe(
            map((response: any) => {
                return response as EnteGregWithComuniAss[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getSchedeEntiGestori(ricerca: RicercaGreg): Observable<RicercaGregOutput[]> {

        return this.http.post<RicercaGreg>(this.baseUrl + PathApi.searchSchedeEntiGestori, ricerca
        ).pipe(
            map((response: any) => {
                return response as RicercaGregOutput[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getSchedeEntiGestoriRendicontazioneConclusa(ricerca: RicercaGreg): Observable<RicercaGregOutput[]> {

        return this.http.post<RicercaGreg>(this.baseUrl + PathApi.searchSchedeEntiGestoriRendicontazioneConclusa, ricerca
        ).pipe(
            map((response: any) => {
                return response as RicercaGregOutput[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getSchedeMultiEntiGestori(listaenti: any[][] = []): Observable<RicercaGregOutput[]> {

        return this.http.post<RicercaGreg>(this.baseUrl + PathApi.searchSchedeMultiEntiGestori, listaenti
        ).pipe(
            map((response: any) => {
                return response as RicercaGregOutput[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaStatoRendicontazioneArchivio(): Observable<StatoRendicontazioneGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getListaStatoRendicontazioneArchivio
        ).pipe(
            map((response: any) => {
                return response as StatoRendicontazioneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getStatoRendicontazioneConclusa(): Observable<StatoRendicontazioneGreg> {

        return this.http.get(this.baseUrl + PathApi.getStatoRendicontazioneConclusa
        ).pipe(
            map((response: any) => {
                return response as StatoRendicontazioneGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaComuniArchivio(): Observable<ComuneGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getListaComuniArchivio
        ).pipe(
            map((response: any) => {
                return response as ComuneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaTipoEnteArchivio(): Observable<TipoEnteGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getListaTipoEnteArchivio
        ).pipe(
            map((response: any) => {
                return response as TipoEnteGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getSchedeEntiGestoriArchivio(ricerca: RicercaArchivioGreg): Observable<RicercaGregOutput[]> {

        return this.http.post<RicercaGreg>(this.baseUrl + PathApi.searchSchedeEntiGestoriArchivio, ricerca
        ).pipe(
            map((response: any) => {
                return response as RicercaGregOutput[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getCronologia(idRendicontazione: number): Observable<CronologiaGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getCronologia, { ...encodeAsHttpParams({ idRendicontazione: idRendicontazione }) }
        ).pipe(
            map((response: any) => {
                return response as CronologiaGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getCronologiaArchivio(idScheda: number): Observable<CronologiaGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getCronologiaArchivio, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as CronologiaGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getDatiEnteProvince(dataValidita: string): Observable<ProvinciaGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getListaProvince, { ...encodeAsHttpParams({ dataValidita: dataValidita }) }).pipe(
            map((response: any) => {
                return response as ProvinciaGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDatiEnteRendicontazione(idRendicontazioneEnte: number, anno: number): Observable<DatiEnteGreg> {
        return this.http.get(this.baseUrl + PathApi.getDatiEnteRendicontazione, { ...encodeAsHttpParams({ idRendicontazioneEnte: idRendicontazioneEnte, annoGestione: anno }) }).pipe(
            map((response: any) => {
                return response as DatiEnteGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getSchedaEnte(id: number, anno: number): Observable<EnteGreg> {
        return this.http.get(this.baseUrl + PathApi.getSchedaEnte, { ...encodeAsHttpParams({ idScheda: id, annoGestione: anno }) }).pipe(
            map((response: any) => {
                this.statorendicontazione = response.rendicontazione.codStatoRendicontazione;
                return response as EnteGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getSchedaAnagrafica(id: number, dataValidita: string): Observable<AnagraficaEnteGreg> {
        return this.http.get(this.baseUrl + PathApi.getSchedaAnagrafica, { ...encodeAsHttpParams({ idScheda: id, dataValidita: dataValidita }) }).pipe(
            map((response: any) => {
                return response as AnagraficaEnteGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioni(anno: number): Observable<PrestazioneGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioni, { ...encodeAsHttpParams({ anno: anno }) }).pipe(
            map((response: any) => {
                return response as PrestazioneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getComuniAssociati(id: number, annoGestione: number): Observable<ComuneAssociatoGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getComuniAssociati, { ...encodeAsHttpParams({ id: id, annoGestione: annoGestione }) }).pipe(
            map((response: any) => {
                return response as ComuneAssociatoGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getComuniAnagraficaAssociati(id: number, dataValidita: string): Observable<ComuneAssociatoGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getComuniAnagraficaAssociati, { ...encodeAsHttpParams({ id: id, dataValidita: dataValidita }) }).pipe(
            map((response: any) => {
                return response as ComuneAssociatoGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioniAssociate(id: number): Observable<PrestazioneAssociataGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniAssociate, { ...encodeAsHttpParams({ id: id }) }).pipe(
            map((response: any) => {
                return response as PrestazioneAssociataGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getAsl(dataValidita: string): Observable<AslGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getAsl, { ...encodeAsHttpParams({ dataValidita: dataValidita }) }).pipe(
            map((response: any) => {
                return response as AslGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDocAssociati(id: number): Observable<AllegatoGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getDocumentiAssociati, { ...encodeAsHttpParams({ idScheda: id }) }).pipe(
            map((response: any) => {
                return response as AllegatoGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getAllegatoToDownload(idAllegato: number): Observable<AllegatoGreg> {
        return this.http.get(this.baseUrl + PathApi.getAllegatoToDownload, { ...encodeAsHttpParams({ idAllegato: idAllegato }) }).pipe(
            map((response: any) => {
                return response as AllegatoGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveDatiEnte(datiEnteToSave: DatiEnteToSave): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.saveDatiEnte, datiEnteToSave).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveDatiAnagrafici(datiEnteToSave: DatiAnagraficiToSave): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.saveDatiAnagrafici, datiEnteToSave).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveModelloA1(datiModelloA1: SalvaModelloA1Greg): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloA1, datiModelloA1).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getCausaliStatiche(): Observable<CausaleGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getCausaliStatiche).pipe(
            map((response: any) => {
                return response as CausaleGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getCausali(idEnte: number): Observable<CausaleGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getCausali, { ...encodeAsHttpParams({ id_ente: idEnte }) }).pipe(
            map((response: any) => {
                return response as CausaleGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getVociModelloA1(idEnte: number): Observable<VociModelloA1Greg[]> {
        return this.http.get(this.baseUrl + PathApi.getVociModelloA1, { ...encodeAsHttpParams({ id_ente: idEnte }) }).pipe(
            map((response: any) => {
                return response as VociModelloA1Greg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getTrasferimentiA2(idEnte: number): Observable<GetTrasferimentiA2Output> {
        return this.http.get(this.baseUrl + PathApi.getTrasferimentiA2, { ...encodeAsHttpParams({ id_ente: idEnte }) }).pipe(
            map((response: any) => {
                return response as GetTrasferimentiA2Output;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDatiModelloA1(idEnte: number): Observable<DatiModelloA1Greg[]> {
        return this.http.get(this.baseUrl + PathApi.getDatiModelloA1, { ...encodeAsHttpParams({ id_ente: idEnte }) }).pipe(
            map((response: any) => {
                return response as DatiModelloA1Greg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    /*   getSchedaEnteGestore(idEnte: number,ruolo: string):Observable<RicercaGregOutput>  {
           return this.http.get(this.baseUrl + PathApi.getSchedaEnteGestore,  {...encodeAsHttpParams({ idEnte: idEnte, ruolo:ruolo })}
               ).pipe(
               map((response: any) => {
                   this.statorendicontazione = response.statoRendicontazione.codStatoRendicontazione;
                   return response as RicercaGregOutput;
               }),
               catchError((error: HttpErrorResponse) => {
                   if(!error.error || error.error == null || error.error.id == null) {
                       this.spinEmitter.emit(false);
                       this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                       return;
                   }
                   return throwError(
                      this.gregError.handleError(GregError.toGregError({ ...error, errorDesc : error.error.descrizione }))
                   )
               }
               )
           );
       }*/

    salvaModelloA2(salva: SalvaModelloA2Greg): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloA2, salva).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    salvaModelloE(salva: SalvaModelloEGreg): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloE, salva).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getTipoVoceD(): Observable<TipoVoceD[]> {
        return this.http.get(this.baseUrl + PathApi.getTipoVoceD).pipe(
            map((response: any) => {
                return response as TipoVoceD[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getVoceModD(): Observable<VoceModD[]> {
        return this.http.get(this.baseUrl + PathApi.getVoceModD).pipe(
            map((response: any) => {
                return response as VoceModD[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneModD(idScheda: number): Observable<RendicontazioneModD> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneModD, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as RendicontazioneModD;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    saveModD(rendicontazioneModD: RendicontazioneModD, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloD, rendicontazioneModD, { ...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna }) }
        ).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneModE(idScheda: number): Observable<RendicontazioneModE> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneModE, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as RendicontazioneModE;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getMacroaggregati(): Observable<Macroaggregati[]> {
        return this.http.get(this.baseUrl + PathApi.getMacroaggregati).pipe(
            map((response: any) => {
                return response as Macroaggregati[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getSpesaMissione(): Observable<SpesaMissione[]> {
        return this.http.get(this.baseUrl + PathApi.getSpesaMissione).pipe(
            map((response: any) => {
                return response as SpesaMissione[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneMacroaggregati(idScheda): Observable<RendicontazioneMacroaggregati> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneMacroaggregati, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as RendicontazioneMacroaggregati;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    saveMacroaggregati(rendicontazioneMacroaggregati: RendicontazioneMacroaggregati, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveMacroaggregati, rendicontazioneMacroaggregati, { ...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna }) }
        ).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveModelloB(rendicontazioneModB: RendicontazioneModB, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloB, rendicontazioneModB, { ...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna }) }
        ).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMissioniModB(): Observable<MissioniB[]> {
        return this.http.get(this.baseUrl + PathApi.getMissioniModB).pipe(
            map((response: any) => {
                return response as MissioniB[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getProgrammiMissioneTotaliModB(idScheda: number): Observable<ProgrammiMissioneTotaliModB[]> {
        return this.http.get(this.baseUrl + PathApi.getProgrammiMissioneTotaliModB, { ...encodeAsHttpParams({ idSchedaEnte: idScheda }) }).pipe(
            map((response: any) => {
                return response as ProgrammiMissioneTotaliModB[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneModB(idScheda: number): Observable<RendicontazioneModB> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneModB, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((rendicontazione: any) => {
                return rendicontazione as RendicontazioneModB;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneTotaliSpese(idScheda: number): Observable<RendicontazioneTotaliSpeseMissioni> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneTotaliSpese, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((rendicontazione: any) => {
                return rendicontazione as RendicontazioneTotaliSpeseMissioni;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneTotaliMacroaggregati(idScheda: number): Observable<RendicontazioneTotaliMacroaggregati> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneTotaliMacroaggregati, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((rendicontazione: any) => {
                return rendicontazione as RendicontazioneTotaliMacroaggregati;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioniModC(idScheda: number): Observable<PrestazioniC[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniC, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((response: any) => {
                return response as PrestazioniC[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneModC(idScheda: number): Observable<RendicontazioneModC> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneModC, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((rendicontazione: any) => {
                return rendicontazione as RendicontazioneModC;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveModelloC(rendicontazioneModC: RendicontazioneModC, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloC, rendicontazioneModC, { ...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna }) }
        ).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getConteggiModF(): Observable<ConteggiF> {
        return this.http.get(this.baseUrl + PathApi.getConteggiF).pipe(
            map((response: any) => {
                return response as ConteggiF;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRendicontazioneModF(idScheda: number): Observable<RendicontazioneModF> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneModF, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((rendicontazione: any) => {
                return rendicontazione as RendicontazioneModF;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveModelloF(rendicontazioneModF: RendicontazioneModF, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloF, rendicontazioneModF, { ...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna }) }
        ).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMsgInformativi(section: string): Observable<MsgInformativo[]> {

        return this.http.get(this.baseUrl + PathApi.getMsgInformativi, { ...encodeAsHttpParams({ section: section }) }
        ).pipe(
            map((response: any) => {
                return response as MsgInformativo[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getMsgApplicativo(param: string): Observable<MsgApplicativo> {

        return this.http.get(this.baseUrl + PathApi.getMsgApplicativo, { ...encodeAsHttpParams({ param: param }) }
        ).pipe(
            map((response: any) => {
                return response as MsgApplicativo;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }


    getListaVociModA(idScheda): Observable<VociModelloA> {
        return this.http.get(this.baseUrl + PathApi.getListaVociModA, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as VociModelloA;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    saveModelloA(salvaModelloA: SalvaModelloA): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloA, salvaModelloA).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getPrestazioniResSemires(codTipologia: string, codTipoStruttura: string, anno: number): Observable<PrestazioneGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getPrestazioniResSemires, { ...encodeAsHttpParams({ codTipologia: codTipologia, codTipoStruttura: codTipoStruttura, anno: anno }) }
        ).pipe(
            map((response: any) => {
                return response as PrestazioneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    confermaDati1(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrGreg> {

        return this.http.post(this.baseUrl + PathApi.confermaDati1, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    richiestaRettifica1(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrGreg> {

        return this.http.post(this.baseUrl + PathApi.richiestaRettifica1, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    confermaDati2(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrGreg> {

        return this.http.post(this.baseUrl + PathApi.confermaDati2, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    richiestaRettifica2(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrGreg> {

        return this.http.post(this.baseUrl + PathApi.richiestaRettifica2, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    valida(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseGreg> {

        return this.http.post(this.baseUrl + PathApi.valida, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as GenericResponseGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    storicizza(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseGreg> {

        return this.http.post(this.baseUrl + PathApi.storicizza, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as GenericResponseGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getInfoRendicontazioneOperatore(idEnte: any): Observable<RendicontazioneEnteGreg> {
        return this.http.get(this.baseUrl + PathApi.getInfoRendicontazioneOperatore, { ...encodeAsHttpParams({ id_ente: idEnte }) }
        ).pipe(
            map((response: any) => {
                return response as RendicontazioneEnteGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaPrestazioniValorizzateModA(idSchedaEnte: number): Observable<string[]> {
        return this.http.get(this.baseUrl + PathApi.getListaPrestazioniValorizzateModA, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as string[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaPrestazioniValorizzateModB1(idSchedaEnte: number): Observable<string[]> {
        return this.http.get(this.baseUrl + PathApi.getListaPrestazioniValorizzateModB1, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as string[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaPrestazioniValorizzateModC(idSchedaEnte: number): Observable<string[]> {
        return this.http.get(this.baseUrl + PathApi.getListaPrestazioniValorizzateModC, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as string[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaComuniValorizzatiModA1(idSchedaEnte: number): Observable<string[]> {
        return this.http.get(this.baseUrl + PathApi.getListaComuniValorizzatiModA1, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as string[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaComuniValorizzatiModA2(idSchedaEnte: number): Observable<string[]> {
        return this.http.get(this.baseUrl + PathApi.getListaComuniValorizzatiModA2, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as string[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaComuniValorizzatiModE(idSchedaEnte: number): Observable<string[]> {
        return this.http.get(this.baseUrl + PathApi.getListaComuniValorizzatiModE, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }
        ).pipe(
            map((response: any) => {
                return response as string[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getAttivitaSocioAssist(): Observable<AttivitaSocioAssist[]> {
        return this.http.get(this.baseUrl + PathApi.getAttivitaSocioAssist
        ).pipe(
            map((response: any) => {
                return response as AttivitaSocioAssist[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    //MODELLO B1
    isMacroaggregatiCompiled(idSchedaEnte: number): Observable<boolean> {
        return this.http.get(this.baseUrl + PathApi.isMacroaggregatiCompiled, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    canActiveModB(idSchedaEnte: number): Observable<boolean> {
        return this.http.get(this.baseUrl + PathApi.canActiveModB, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMacroaggregatiTotali(idSchedaEnte: number): Observable<ModelRendicontazioneTotaliMacroaggregati> {
        return this.http.get(this.baseUrl + PathApi.getMacroaggregatiTotali, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }).pipe(
            map((response: any) => {
                return response as ModelRendicontazioneTotaliMacroaggregati;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioniModB1(idSchedaEnte: number): Observable<ModelPrestazioniB1[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniModB1, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte }) }).pipe(
            map((response: any) => {
                return response as ModelPrestazioniB1[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getElencoLblModB1(idSchedaEnte: number, anno?: number): Observable<ModelloB1ElencoLbl> {
        return this.http.get(this.baseUrl + PathApi.getElencoLblModB1, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte, anno: anno }) }).pipe(
            map((response: any) => {
                return response as ModelloB1ElencoLbl;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }


    saveModelloB1(dati: SalvaModelloB1): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloB1, dati).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }


    //invio tranche

    inviotranche(inviaModelli: InviaModelli): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.inviatranche, inviaModelli).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    //MODELLO tab per tranche
    getModelliPerTranche(idRendicontazione: number, codProfilo: string): Observable<LinkModelli[]> {
        return this.http.get(this.baseUrl + PathApi.getmodellipertranche, { ...encodeAsHttpParams({ idRendicontazione: idRendicontazione, codProfilo: codProfilo }) }).pipe(
            map((response: any) => {
                return response as LinkModelli[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    //MODELLO tab per tranche
    getTranchePerModello(idSchedaEnte: number, modello: string): Observable<ModelTabTranche> {
        return this.http.get(this.baseUrl + PathApi.gettranchepermodello, { ...encodeAsHttpParams({ id_ente: idSchedaEnte, modello: modello }) }).pipe(
            map((response: any) => {
                return response as ModelTabTranche;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getParametroPerInformativa(informativa: string): Observable<ParametroGreg[]> {

        return this.http.get(this.baseUrl + PathApi.getParametroPerInformativa, { ...encodeAsHttpParams({ informativa: informativa }) }
        ).pipe(
            map((response: any) => {
                return response as ParametroGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getParametro(codParam: string): Observable<ParametroGreg> {

        return this.http.get(this.baseUrl + PathApi.getParametro, { ...encodeAsHttpParams({ codParam: codParam }) }
        ).pipe(
            map((response: any) => {
                return response as ParametroGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    esportaModelloA1(datiModelloA1: EsportaModelloA1Greg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloA1, datiModelloA1).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    esportaModelloD(datiModelloD: EsportaModelloDGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloD, datiModelloD).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    esportaModelloB(datiModelloB: EsportaModelloBGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloB, datiModelloB).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    esportaIstat(datiModelloB: EsportaModelloBGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaIstat, datiModelloB).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    esportaModelloE(datiModelloE: EsportaModelloEGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloE, datiModelloE).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    esportaModelloC(datiModelloC: EsportaModelloCGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloC, datiModelloC).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    esportaModelloF(datiModelloF: EsportaModelloFGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloF, datiModelloF).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    getMsgInformativiPerCod(codice: string): Observable<MsgInformativo> {

        return this.http.get(this.baseUrl + PathApi.getMsgInformativiPerCod, { ...encodeAsHttpParams({ codice: codice }) }
        ).pipe(
            map((response: any) => {
                return response as MsgInformativo;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );

    }

    esportaModelloA2(datiModelloA2: EsportaModelloA2Greg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloA2, datiModelloA2).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    esportaModelloA(datiModelloA: EsportaModelloA): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloA, datiModelloA).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    esportaMacroaggregati(datiMacroaggregati: EsportaMacroaggregatiGreg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaMacroaggregati, datiMacroaggregati).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    esportaModelloB1(datiModelloB1: EsportaModelloB1Greg): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModelloB1, datiModelloB1).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    getVociAllD(idScheda: number): Observable<VociAllD> {
        return this.http.get(this.baseUrl + PathApi.getVociAllD, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as VociAllD;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getRendicontazioneModAllD(idScheda: number): Observable<RendicontazioneModAllD> {
        return this.http.get(this.baseUrl + PathApi.getRendicontazioneModAllD, { ...encodeAsHttpParams({ idScheda: idScheda }) }).pipe(
            map((rendicontazione: any) => {
                return rendicontazione as RendicontazioneModAllD;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    isModuloFnpsIsCompiled(idSchedaEnte: number): Observable<boolean> {
        return this.http.get(this.baseUrl + PathApi.compilabileModelloAllD, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    saveModelloAllD(rendicontazioneAllD: RendicontazioneModAllD, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveModelloAllD, rendicontazioneAllD, { ...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna }) }
        ).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkB1EntryForAlZero(idSchedaEnte: number): Observable<boolean> {
        return this.http.get(this.baseUrl + PathApi.canActiveModFnps, { ...encodeAsHttpParams({ idScheda: idSchedaEnte }) }).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    esportaModuloFnps(datiModuloFnps: EsportaModuloFnps): Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModuloFnps, datiModuloFnps).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            })
        )
    }

    selectprofiloazione(): Observable<UserInfoGreg> {
        return this.http.get(this.baseUrl + PathApi.selectprofilo
            // , {headers: this.getHeader()}
        ).pipe(
            map((response: any) => {
                this.utente = of(response as UserInfoGreg);
                return response as UserInfoGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    verificaprofilo(response: UserInfoGreg) {
        //se esiste un solo profilo ed una sola lista devo vedere se operatore regionale o operatore ente dal tipo profilo
        //se ente devo vedere quanti enti ha la lista per indirizzarlo sul singolo ente o su multiente
        this.utenteloggato = response;
        let tipoprofilo = response.listaprofili[0].tipoProfilo;
        if (response.listaprofili.length == 1) { //un solo profilo
            if (response.listaprofili[0].listaenti.length == 1) {//una sola lista
                this.profilo = response.listaprofili[0];
                this.profiloSelezionato = response.listaprofili[0];
                this.listaSelezionata = response.listaprofili[0].listaenti[0];
                this.azioni = new Map(Object.entries(response.listaprofili[0].listaazioni));
                this.listaenti = response.listaprofili[0].listaenti[0].idente;
                this.redirecttab(this.azioni, false, false);
            }
            else {
                //aggiungere il routing alla selezione-profilo-applicativo
                this.profiloSelezionato = response.listaprofili[0];
                this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true, state: { utente: response } });
            }
        }
        else {
            //aggiungere il routing alla selezione-profilo-applicativo
            this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true, state: { utente: response } });
        }
    }

    redirecttab(azioni: Map<string, boolean[]>, cruscotto: boolean, configuratore: boolean) {

        if (cruscotto) {
            this.spinEmitter.emit(true);
            const navigationExtras: NavigationExtras = {
                skipLocationChange: true,
                state: {
                    cruscotto: true
                }
            }
            this.router.navigate(['operatore-regionale/cruscotto'], navigationExtras);
        } else if (configuratore) {
            this.spinEmitter.emit(true);
            const navigationExtras: NavigationExtras = {
                skipLocationChange: true
            }
            this.router.navigate(['operatore-regionale/configuratore-prestazioni'], navigationExtras);
        } else
            if (azioni.get("EntiGestori")[1])
                this.router.navigate(['operatore-regionale'], { skipLocationChange: true });
            else if (azioni.get("Archivio")[1])
                this.router.navigate(['operatore-regionale/archivio-dati-rendicontazione'], { skipLocationChange: true });
            else if (azioni.get("ConfiguratorePrestazioni")[1])
                this.router.navigate(['operatore-regionale/configuratore-prestazioni'], { skipLocationChange: true });
            else if (azioni.get("ConfiguratoreUtenzeFnps")[1])
                this.router.navigate(['operatore-regionale/configuratore-fnps'], { skipLocationChange: true });
            else if (azioni.get("Cruscotto")[1])
                this.router.navigate(['operatore-regionale/cruscotto'], { skipLocationChange: true });
            else if (azioni.get("GestioneUtenti")[1])
                this.router.navigate(['operatore-regionale/gestione-utenti'], { skipLocationChange: true });
            else
                this.router.navigate(['/redirect-page'], { skipLocationChange: true });
    }

    getStatiEnte(): Observable<StatoEnte[]> {

        return this.http.get(this.baseUrl + PathApi.getStatiEnte
        ).pipe(
            map((response: any) => {
                return response as StatoEnte[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getAnniEsercizio(): Observable<number[]> {

        return this.http.get(this.baseUrl + PathApi.getAnniEsercizio
        ).pipe(
            map((response: any) => {
                return response as number[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getAnniEsercizioArchivio(): Observable<number[]> {

        return this.http.get(this.baseUrl + PathApi.getAnniEsercizioArchivio
        ).pipe(
            map((response: any) => {
                return response as number[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getStorico(idScheda: number): Observable<StoricoGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getStorico, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as StoricoGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    //MODELLO associato
    getModelliassociati(idRendicontazione: number): Observable<ModelTabTranche[]> {
        return this.http.get(this.baseUrl + PathApi.getmodelliassociati, { ...encodeAsHttpParams({ idRendicontazione: idRendicontazione }) }).pipe(
            map((response: any) => {
                return response as ModelTabTranche[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }
    //MODELLO tutti ma atetnzione deve esserci la tranche
    getModelli(): Observable<ModelTabTranche[]> {
        return this.http.get(this.baseUrl + PathApi.getmodelli).pipe(
            map((response: any) => {
                return response as ModelTabTranche[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getAllObbligo(): Observable<ModelObbligo[]> {
        return this.http.get(this.baseUrl + PathApi.getAllObbligo).pipe(
            map((response: any) => {
                return response as ModelObbligo[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }
    getSchedaAnagraficaStorico(id: number, dataFineValidita: string): Observable<AnagraficaEnteGreg> {
        return this.http.get(this.baseUrl + PathApi.getSchedaAnagraficaStorico, { ...encodeAsHttpParams({ idScheda: id, dataFineValidita: dataFineValidita }) }).pipe(
            map((response: any) => {
                return response as AnagraficaEnteGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getVerificaModelliVuoto(modello: string, idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.get(this.baseUrl + PathApi.getVerificaModelliVuoto, { ...encodeAsHttpParams({ modello: modello, idRendicontazione: idRendicontazione }) }).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getComuniAnagraficaAssociatiStorico(id: number, dataFineValidita: string, dataInizioValidita: string): Observable<ComuneAssociatoGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getComuniAnagraficaAssociatiStorico, { ...encodeAsHttpParams({ id: id, dataFineValidita: dataFineValidita, dataInizioValidita: dataInizioValidita }) }).pipe(
            map((response: any) => {
                return response as ComuneAssociatoGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMotivazioneChiusura(): Observable<Motivazioni[]> {
        return this.http.get(this.baseUrl + PathApi.getMotivazioniChiusura).pipe(
            map((response: any) => {
                return response as Motivazioni[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getCronologiaStato(idScheda: number): Observable<CronologiaGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getCronologiaStato, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as CronologiaGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    closeEnte(chiuso: ChiusuraEnte): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.closeEnte, chiuso).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getLastStato(idScheda: number): Observable<StatoEnteGreg> {
        return this.http.get(this.baseUrl + PathApi.getLastStato, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as StatoEnteGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    ripristinoEnte(chiuso: ChiusuraEnte): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.ripristinoEnte, chiuso).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getMotivazioneRipristino(): Observable<Motivazioni[]> {
        return this.http.get(this.baseUrl + PathApi.getMotivazioniRipristino).pipe(
            map((response: any) => {
                return response as Motivazioni[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getSchedaAnagraficaunione(ricerca: RicercaListaEntiDaunire): Observable<AnagraficaEnteGreg[]> {
        return this.http.post(this.baseUrl + PathApi.getSchedaAnagraficaUnione, ricerca).pipe(
            map((response: any) => {
                return response as AnagraficaEnteGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    unisciEnte(unisci: UnisciEnte): Observable<boolean> {
        return this.http.post(this.baseUrl + PathApi.unioneEnte, unisci).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getProvinceComuniLiberi(dataValidita: string): Observable<ComuneGreg[]> {
        return this.http.get(this.baseUrl + PathApi.getProvinciaComuneLibero, { ...encodeAsHttpParams({ dataValidita: dataValidita }) }).pipe(
            map((response: any) => {
                return response as ComuneGreg[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    creaNuovoEnte(datiEnteToSave: DatiAnagraficiToSave): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.creaNuovoEnte, datiEnteToSave).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkModelloA(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkModelloA, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkModelloA1(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkModelloA1, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkMacroaggregati(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkMacroaggregati, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkModelloB1(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkModelloB1, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkModelloB(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkModelloB, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkModelloC(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkModelloC, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    checkModelloF(idRendicontazione: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.checkModelloF, idRendicontazione).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }


    saveMotivazioneCheck(datiMotivazione: SalvaMotivazioneCheck): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.saveMotivazioneCheck, datiMotivazione).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDettaglioPrestazione(codPrestazione: string, annoGestione: number): Observable<DettaglioPrestazione> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioneRegionale, { ...encodeAsHttpParams({ codPrestRegionale: codPrestazione, annoGestione: annoGestione }) }).pipe(
            map((response: any) => {
                return response as DettaglioPrestazione;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getSchedeEntiGestoriCruscotto(ricerca: RicercaGregCruscotto): Observable<RicercaGregOutput[]> {

        return this.http.post<RicercaGreg>(this.baseUrl + PathApi.searchSchedeEntiGestoriCruscotto, ricerca
        ).pipe(
            map((response: any) => {
                return response as RicercaGregOutput[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getModelliCruscotto(): Observable<ModelTabTrancheCruscotto[]> {
        return this.http.get(this.baseUrl + PathApi.getModelliCruscotto).pipe(
            map((response: any) => {
                return response as ModelTabTrancheCruscotto[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getStati(ricerca: RicercaGregCruscotto): Observable<StatiCruscotto[]> {

        return this.http.post<RicercaGreg>(this.baseUrl + PathApi.getStati, ricerca
        ).pipe(
            map((response: any) => {
                return response as StatiCruscotto[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    infoModello(infoModello: InfoModello): Observable<GenericResponseWarnCheckErrGreg> {
        return this.http.post(this.baseUrl + PathApi.getInfoModello, infoModello).pipe(
            map((response: any) => {
                return response as GenericResponseWarnCheckErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioniReg1(): Observable<DettaglioPrestazioneConf[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniReg1).pipe(
            map((response: any) => {
                return response as DettaglioPrestazioneConf[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDettaglioPrestazioneReg1(idPrestazione: number): Observable<DettaglioPrestazioneConf> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioneRegionale1, { ...encodeAsHttpParams({ idPrestazione: idPrestazione }) }).pipe(
            map((response: any) => {
                return response as DettaglioPrestazioneConf;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getTipologie(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getTipologie).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getStrutture(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getStrutture).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getQuote(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getQuote).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestColl(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestColl).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMacroaggregatiConf(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getMacroaggregatiConf).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getUtenzeConf(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getUtenzeConf).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMissioneProgConf(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getMissioneProgConf).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getSpeseConf(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getSpeseConf).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getIstatConf(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getIstatConf).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getUtenzeIstatConf(utenze: PrestUtenza[]): Observable<ListeConfiguratore[]> {
        return this.http.post(this.baseUrl + PathApi.getUtenzeIstatConf, utenze).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getNomenclatoreConf(): Observable<Nomenclatore[]> {
        return this.http.get(this.baseUrl + PathApi.getNomenclatoreConf).pipe(
            map((response: any) => {
                return response as Nomenclatore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioni2Conf(): Observable<Prest1Prest2[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioni2Conf).pipe(
            map((response: any) => {
                return response as Prest1Prest2[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioniMinConf(): Observable<Prest1PrestMin[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniMinConf).pipe(
            map((response: any) => {
                return response as Prest1PrestMin[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    savePrestazione(dettaglioPrestazione: DettaglioPrestazioneConf): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.savePrestazione, dettaglioPrestazione
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    modificaPrestazione(dettaglioPrestazione: DettaglioPrestazioneConf): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaPrestazione, dettaglioPrestazione
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    savePrestazione2(prest2: Prest1Prest2): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.savePrestazione2, prest2
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    modificaPrestazione2(prest2: Prest1Prest2): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaPrestazione2, prest2
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDettaglioPrestazioneReg2(idPrestazione: number): Observable<Prest1Prest2> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioneRegionale2, { ...encodeAsHttpParams({ idPrestazione: idPrestazione }) }).pipe(
            map((response: any) => {
                return response as Prest1Prest2;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    creaNuovoAnno(creaAnno: ListaEntiAnno): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.creaNuovoAnno, creaAnno
        ).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getMaxAnnoRendicontazione(): Observable<number> {
        return this.http.get(this.baseUrl + PathApi.getMaxAnnoRendicontazione).pipe(
            map((response: any) => {
                return response as number;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getUtenzeIstatTransConf(codUtenza: string): Observable<ListeConfiguratore> {
        return this.http.get(this.baseUrl + PathApi.getUtenzeIstatTransConf, { ...encodeAsHttpParams({ codUtenza: codUtenza }) }).pipe(
            map((response: any) => {
                return response as ListeConfiguratore;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    concludiRendicontazione(enti: RicercaGregOutput[]): Observable<GenericResponseWarnCheckErrGreg> {
        return this.http.post(this.baseUrl + PathApi.concludiRendicontazione, enti).pipe(
            map((response: any) => {
                return response as GenericResponseWarnCheckErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    ripristinaRendicontazione(enti: RicercaGregOutput[]): Observable<GenericResponseWarnCheckErrGreg> {
        return this.http.post(this.baseUrl + PathApi.ripristinaRendicontazione, enti).pipe(
            map((response: any) => {
                return response as GenericResponseWarnCheckErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    ripristinaCompilazione(enti: RicercaGregOutput[]): Observable<GenericResponseWarnCheckErrGreg> {
        return this.http.post(this.baseUrl + PathApi.ripristinaCompilazione, enti).pipe(
            map((response: any) => {
                return response as GenericResponseWarnCheckErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getUtenti(ricerca: RicercaUtenti): Observable<Utenti[]> {

        return this.http.post(this.baseUrl + PathApi.getUtenti, ricerca
        ).pipe(
            map((response: any) => {
                return response as Utenti[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getProfili(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getProfili).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getListe(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getListe).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getEnti(): Observable<ListeConfiguratore[]> {
        return this.http.get(this.baseUrl + PathApi.getEnti).pipe(
            map((response: any) => {
                return response as ListeConfiguratore[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    eliminaAbilitazione(abilitazioni: Abilitazioni): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaAbilitazione, abilitazioni).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    eliminaUtente(utente: Utenti): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaUtente, utente).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    selectprofiloazioneDaGestioneUtenti(): Observable<UserInfoGreg> {
        return this.http.get(this.baseUrl + PathApi.selectprofilo
            // , {headers: this.getHeader()}
        ).pipe(
            map((response: any) => {
                this.utente = of(response as UserInfoGreg);
                return response as UserInfoGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleErrorDaGestioneUtenti(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    modificaUtente(utente: Utenti): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaUtente, utente).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    modificaAbilitazione(utente: Utenti): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaAbilitazioni, utente).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getAbilitazioni(utente: Utenti): Observable<Abilitazioni[]> {

        return this.http.post(this.baseUrl + PathApi.getAbilitazioni, utente
        ).pipe(
            map((response: any) => {
                return response as Abilitazioni[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    creaUtente(utente: Utenti): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.creaUtente, utente).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaProfilo(ricerca: RicercaProfilo): Observable<ListaProfilo[]> {

        return this.http.post(this.baseUrl + PathApi.getListaProfili, ricerca
        ).pipe(
            map((response: any) => {
                return response as ListaProfilo[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getAzioni(): Observable<ListaAzione[]> {
        return this.http.get(this.baseUrl + PathApi.getAzioni).pipe(
            map((response: any) => {
                return response as ListaAzione[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    eliminaAzione(azione: ListaAzione): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaAzione, azione).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    eliminaProfilo(profilo: ListaProfilo): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaProfilo, profilo).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    modificaAzioni(profilo: ListaProfilo): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaAzioni, profilo).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getAzioniProfilo(profilo: ListaProfilo): Observable<ListaAzione[]> {
        return this.http.post(this.baseUrl + PathApi.getAzioniProfilo, profilo).pipe(
            map((response: any) => {
                return response as ListaAzione[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    modificaProfilo(profilo: ListaProfilo): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaProfilo, profilo).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    creaProfilo(profilo: ListaProfilo): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.creaProfilo, profilo).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getLista(ricerca: RicercaProfilo): Observable<ListaLista[]> {

        return this.http.post(this.baseUrl + PathApi.getLista, ricerca
        ).pipe(
            map((response: any) => {
                return response as ListaLista[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    eliminaLista(lista: ListaLista): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaLista, lista).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    modificaLista(lista: ListaLista): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaLista, lista).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getListaEnti(lista: ListaLista): Observable<ListaEnte[]> {
        return this.http.post(this.baseUrl + PathApi.getListaEnti, lista).pipe(
            map((response: any) => {
                return response as ListaEnte[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getEntiLista(): Observable<ListaEnte[]> {
        return this.http.get(this.baseUrl + PathApi.getEntiLista).pipe(
            map((response: any) => {
                return response as ListaEnte[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    eliminaEnte(ente: ListaEnte): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaEnte, ente).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    modificaEnti(lista: ListaLista): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.modificaEnti, lista).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    creaLista(lista: ListaLista): Observable<ResponseSalvaModelloGreg> {
        return this.http.post(this.baseUrl + PathApi.creaLista, lista).pipe(
            map((response: any) => {
                return response as ResponseSalvaModelloGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    storicizzaMultiplo(enti: RicercaGregOutput[]): Observable<GenericResponseWarnCheckErrGreg> {
        return this.http.post(this.baseUrl + PathApi.storicizzaMultiplo, enti).pipe(
            map((response: any) => {
                return response as GenericResponseWarnCheckErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getDataInizioMax(idScheda: number): Observable<Abilitazioni> {

        return this.http.get(this.baseUrl + PathApi.getDataInizioMax, { ...encodeAsHttpParams({ idScheda: idScheda }) }
        ).pipe(
            map((response: any) => {
                return response as Abilitazioni;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getUtenzeFnps(): Observable<UtenzeFnps[]> {
        return this.http.get(this.baseUrl + PathApi.getUtenzeFnps).pipe(
            map((response: any) => {
                return response as UtenzeFnps[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getUtenzeSelezionabiliFnps(): Observable<ModelUtenzaAllD[]> {
        return this.http.get(this.baseUrl + PathApi.getUtenzeSelezionabiliFnps).pipe(
            map((response: any) => {
                return response as ModelUtenzaAllD[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    creaUtenzeFnps(utenze: UtenzeFnps[]): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.creaUtenzeFnps, utenze).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    eliminaUtenzaFnps(utenza: UtenzeFnps): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaUtenzaFnps, utenza).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getUtenzeFnpsByAnno(annoEsercizio: number): Observable<UtenzeFnps[]> {
        return this.http.get(this.baseUrl + PathApi.getUtenzeByAnno, { ...encodeAsHttpParams({ annoEsercizio: annoEsercizio }) }).pipe(
            map((response: any) => {
                return response as UtenzeFnps[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getFondiEnte(idRendicontazione: number): Observable<Fondi[]> {
        return this.http.get(this.baseUrl + PathApi.getFondiEnte, { ...encodeAsHttpParams({ idRendicontazione: idRendicontazione }) }).pipe(
            map((response: any) => {
                return response as Fondi[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getFondiAnno(annoEsercizio: number): Observable<Fondi[]> {
        return this.http.get(this.baseUrl + PathApi.getFondiAnno, { ...encodeAsHttpParams({ annoEsercizio: annoEsercizio }) }).pipe(
            map((response: any) => {
                return response as Fondi[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getRegole(): Observable<Fondi[]> {
        return this.http.get(this.baseUrl + PathApi.getRegole).pipe(
            map((response: any) => {
                return response as Fondi[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    eliminaFondo(fondo: Fondi): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.eliminaFondo, fondo).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    getPrestazioniModelloAlZero(idEnte: number): Observable<DatiAllontanamentoZero> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniModelloAlZero, { ...encodeAsHttpParams({ idScheda: idEnte }) }).pipe(
            map((response: any) => {
                return response as DatiAllontanamentoZero;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getPrestazioniModelloAlZeroFNPS(idRendicontazioneEnte: number): Observable<PrestazioniAlZeroFNPS[]> {
        return this.http.get(this.baseUrl + PathApi.getPrestazioniModelloAlZeroFNPS, { ...encodeAsHttpParams({ idRendicontazioneEnte: idRendicontazioneEnte }) }).pipe(
            map((response: any) => {
                return response as PrestazioniAlZeroFNPS[];
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getFondiEnteModelloAlZero(idEnte: number): Observable<FondiEnteAllontanamentoZeroDTO> {
        return this.http.get(this.baseUrl + PathApi.getFondiEnteModelloAlZero,  { ...encodeAsHttpParams({ idEnte: idEnte }) }).pipe(
            map((response: any) => {
                return response as FondiEnteAllontanamentoZeroDTO;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    getValidazioneModelloAlZero(idRendicontazioneEnte: number): Observable<boolean> {
        return this.http.get(this.baseUrl + PathApi.getValidazioneModelloAlZero, { ...encodeAsHttpParams({ idRendicontazioneEnte: idRendicontazioneEnte }) }).pipe(
            map((response: any) => {
                return response as boolean;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        );
    }

    toggleValidazioneAlZero(payload: ValidazioneAlZero): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.toggleValidazioneAlZero, payload).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    salvaDatiModelloAlZero(data: DatiAllontanamentoZero, idRendicontazioneEnte: number): Observable<GenericResponseWarnErrGreg> {
        return this.http.post(this.baseUrl + PathApi.salvaDatiModelloAlZero, data,  { ...encodeAsHttpParams({ idRendicontazioneEnte: idRendicontazioneEnte }) }).pipe(
            map((response: any) => {
                return response as GenericResponseWarnErrGreg;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

    esportaModuloAlZero(data: PrestazioniAlZeroCSV_DTO, idRendicontazioneEnte: number):  Observable<Response | any> {
        var estratto: ResponseEsportaModelliGreg;
        return this.http.post(this.baseUrl + PathApi.esportaModuloAlZero, data,  { ...encodeAsHttpParams({ idRendicontazioneEnte: idRendicontazioneEnte }) }).pipe(
            map((response: any) => {
                estratto = response as ResponseEsportaModelliGreg;
                let m = new Map<string, any>();
                let name = estratto.messaggio;
                let messaggio = estratto.descrizione;
                m.set("name", name);
                m.set("messaggio", messaggio);
                m.set("file", estratto.excel);
                let stato = estratto.esito;
                m.set("status", stato);
                return m;
            }),
            catchError((error: HttpErrorResponse) => {
                if (!error.error || error.error == null || error.error.id == null) {
                    this.spinEmitter.emit(false);
                    this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                    return;
                }
                return throwError(
                    this.gregError.handleError(GregError.toGregError({ ...error, errorDesc: error.error.descrizione }))
                )
            }
            )
        )
    }

}
