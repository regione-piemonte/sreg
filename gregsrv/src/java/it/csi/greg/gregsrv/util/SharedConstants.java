/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.util;

public class SharedConstants {
	// Types
	public final static String STRING_TYPE = "STRING";
	public final static String NUMERIC_TYPE = "NUMERIC";
	
	// Casi Uso
	public static final String DATI_ENTE = "DATI ENTE";
	
	// Documenti
	public static final String DOC_INIZIALE = "Documento Iniziale";
	public static final String DOC_FINALE = "Documento Finale";

	// Messaggi
	public final static String MESSAGE_OK_ENTE_SALVA = "MSG01";
	public final static String MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI = "MSG02";
	public final static String MESSAGE_STANDARD_CAMBIO_STATO = "MSG03";
	public final static String MESSAGE_STANDARD_AGGIORNAMENTO_DATI = "MSG04";
	public final static String MESSAGE_OK_MODELLO_ESPORTA = "MSG10";
	public final static String MESSAGE_OK_MODELLO_SALVA_MACROAGGREGATI = "MSG11";
	
	
	// Errori
	public final static String ERRORE_LOGIN_MESSAGGIO = "ERR01";
	public final static String ERRORE_GENERICO = "ERR02";
	public final static String ERROR_MANCA_NOTA_ENTE = "ERR03";
	public final static String ERROR_CODE_DATI_OBBLIGATORI = "ERR04";
	public final static String ERROR_CODE_DATI_NONVALIDI = "ERR05";
	public final static String ERROR_CODE_DATI_NONCONGRUI = "ERR06";
	public final static String ERRORE_SALVATAGGIO_MODELLO = "ERR07";
	public final static String ERROR_TOTALE = "ERR08";
	public final static String ERROR_INVIO_GENERICO = "ERR09";
	public final static String ERROR_INVIO_STATO = "ERR10";
	public final static String ERRORE_MANCA_ALLEGATO_1 = "ERR11";
	public final static String ERRORE_MANCA_ALLEGATO_2 = "ERR12";
	public final static String ERROR_ANNO_CONTABILE = "ERR13";
	public final static String ERRORE_MODELLO_OBBLIGATORIO = "ERR14";
	public final static String ERROR_CONFERMA_RETTIFICA_DATI = "ERR15";
	public final static String ERROR_VALIDA_STORICIZZA_DATI = "ERR36";
	public final static String ERROR_DELETE_PRESTAZIONI = "ERR16";
	public final static String ERROR_INVIO_MAIL = "ERR17";
	public final static String ERROR_ASSENZA_MAIL = "ERR18";
	public final static String ERROR_INVIO_ALLEGATO_NO_MODELLISIDOC = "ERR19";
	public final static String ERROR_INVIO_ALLEGATO_NO_MODELLI = "ERR24";
	public final static String ERROR_UPDATE_DOC_ALLEGATI = "ERR20";
	public final static String ERROR_COMUNE_ALREADY_PRESENT = "ERR21";
	public final static String ERROR_PREST_ALREADY_PRESENT = "ERR22";
	public final static String ERROR_UPLOAD_ALLEGATI = "ERR23";
	public final static String MESSAGGIO_GENERICO_INVIA_KO = "MSG05";
	public final static String MESSAGGIO_GENERICO_INVIO_TRANCHE = "MSG07";
	public final static String MESSAGGIO_GENERICO_INVIO_OK = "MSG06";
	public final static String MESSAGGIO_PARZIALE_COMPILAZIONE = "MSG08";

	public final static String ERROR_INVIO_MODELLIA_08 = "ERR25";
	public final static String ERROR_INVIO_MODELLIA_NO08 = "ERR26";
	public final static String ERROR_INVIO_MODELLIA_06 = "ERR27";
	public final static String ERROR_INVIO_MODELLIA_NO06 = "ERR28";
	public final static String ERROR_INVIO_MODELLIA_07 = "ERR29";
	
	public final static String ERROR_INVIO_MODELLIMACRO_01 = "ERR37";
	public final static String ERROR_INVIO_MODELLIMACRO_02 = "ERR38";
	public final static String ERROR_INVIO_MODELLIMACRO_03 = "ERRMACROINVIO";
	public final static String ERROR_INVIO_MODELLIB_01 = "ERRB01INVIO";
	public final static String ERROR_INVIO_MODELLIMODC_01 = "ERR39";
	public final static String ERROR_INVIO_MODELLIMODC_02 = "ERR40";
	public final static String ERROR_INVIO_MODELLIMODC_03 = "ERR41";
	public final static String ERROR_INVIO_MODELLIMODC_04 = "ERR42";
	public final static String ERROR_INVIO_MODELLIMODC_05 = "ERR51";
	public final static String ERROR_INVIO_MODELLIMODC_06 = "ERR54";
	public final static String ERROR_INVIO_MODELLIMODF_01 = "ERR44";
	public final static String ERROR_INVIO_MODELLIMODB1_01 = "ERR45";
	public final static String ERROR_INVIO_MODELLIMODB1_02 = "ERR46";
	public final static String ERROR_INVIO_MODELLIMODB1_03 = "ERR47";
	public final static String ERROR_INVIO_MODELLIMODF_02 = "ERRF02INVIO";
	public final static String ERROR_INVIO_MODELLIMODF_03 = "ERRF03INVIO";
	
	public final static String ERROR_INVIO_MODELLIMODB1_04 = "ERR48";
	public final static String ERROR_INVIO_MODELLIMODB1_05 = "ERR49";
	public final static String ERROR_INVIO_MODELLIMODB1_06 = "ERR50";
	public final static String ERROR_INVIO_MODELLIMODB1_07 = "ERR52";
	public final static String ERROR_INVIO_MODELLIMODB1_08 = "ERR53";
	public final static String ERROR_INVIO_MODELLIMODB1_09 = "ERR55";
	public final static String ERROR_INVIO_MODELLIMODB1_10 = "ERR56";
	public final static String ERROR_INVIO_MODELLIMODB1_11 = "ERRMODB1-2";
	public final static String ERRORE_SALVA_DATA_MANCANTE = "ERR60";
	public final static String ERROR_INVIO_MODELLIMODB1_12 = "ERRMODB1-ALZERO";

	public final static String ERROR_DELETE_PREST_ASS = "ERR30";
	public final static String ERROR_DELETE_COMUNE_ASS = "ERR31";
	public final static String ERROR_NO_MODELLO = "ERR32";
	public final static String ERROR_TITOLO2 = "ERR33";
	public final static String ERROR_TITOLO2IMPORTO = "ERR34";
	public final static String ERROR_DATIANAGRAFICI01 = "ERRDATIANAGRAFICI01";
	public final static String ERROR_DATIANAGRAFICI02 = "ERRDATIANAGRAFICI02";
	public final static String ERROR_CREAENTE01 = "ERRCREAENTE01";
	
	
	public static final String WARNING_MOD_A2_01 = "WARN01-A2";
	public static final String WARNING_MOD_B_01 = "WARN01-B";
	public static final String WARNING_MOD_B_02 = "WARN02-B";
	public static final String WARNING_MOD_B_03 = "WARN03-B";
	public static final String WARNING_MOD_D_01 = "WARN01-D";
	public static final String WARNING_MOD_D_02 = "WARN02-D";
	public static final String WARNING_MOD_D_03 = "WARN03-D";
	public static final String WARNING_MOD_D_04 = "WARN04-D";
	public static final String WARNING_MACROAGGREGATO_01 = "WARN01-MAC";
	public static final String WARNING_MACROAGGREGATO_02 = "WARN02-MAC";
	
	
	public static final String STATO_ENTE_IMPORTATA = "IMP";
	public static final String STATO_ENTE_DACONFERMARE= "DAC";
	public static final String STATO_ENTE_CONFERMATA = "CON";
	public static final String STATO_ENTE_CANCELLATA = "CAN";
	public static final String STATO_ENTE_ESPORTATA = "ESP";
	
	public static final String DIM_FILE_PDF = "DIM_FILE_PDF";	
	public static final String DIM_NOMEFILE_PDF = "DIM_NOMEFILE_PDF";	
		
	public static final String COMPONENT_NAME = "GREGSRV";
	public static final String GREG_ROOT_LOG_CATEGORY = "greg.gregsrv";
	
	public static final String OPERAZIONE_READ = "READ";
	public static final String OPERAZIONE_UPDATE = "UPDATE";
	public static final String OPERAZIONE_INSERT = "INSERT";
	public static final String OPERAZIONE_DELETE = "DELETE";
	
	public static final String KEY_OPER_OK = "OK";
    public static final String KEY_OPER_BAD_REQUEST = "BAD_REQUEST";
	public static final String KEY_OPER_UNAUTHORIZED = "UNAUTHORIZED";
	public static final String KEY_OPER_INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
	
	public static final String VALUE_SI = "SI";
	public static final String VALUE_NO = "NO";

	public static final String OPERAZIONE_SALVAENTE = "SALVAENTE";
	public static final String OPERAZIONE_RICERCA_SCHEDE = "RICERCASCHEDE";
	public static final String OPERAZIONE_RICERCA_CRONOLOGIA = "RICERCACRONOLOGIA";
	public static final String OPERAZIONE_CONFERMAENTE = "CONFERMAENTE";
	public static final String OPERAZIONE_RIPRISTINAENTE = "RIPRISTINAENTE";
	public static final String OPERAZIONE_ELENCOENTE = "ELENCOENTE";
	public static final String OPERAZIONE_REPORTCSV = "REPORTCSV";
	public static final String OPERAZIONE_VERIFICAENTE = "VERIFICAENTE";
	public static final String OPERAZIONE_LOGIN = "LOGIN";
	public static final String OPERAZIONE_LOGOUT = "LOGOUT";
	public static final String OPERAZIONE_SCHEDAMULTIENTEGESTORE = "SCHEDAMULTIENTEGESTORE";
	public static final String OPERAZIONE_COMUNE = "COMUNE";
	public static final String OPERAZIONE_PROVINCIA = "PROVINCIA";
	public static final String OPERAZIONE_PARAMETRO = "PARAMETRO";
	public static final String OPERAZIONE_OPERATORE = "OPERATORE";
	public static final String OPERAZIONE_ALLEGATO = "ALLEGATO";
	public static final String OPERAZIONE_REQUEST = "request";
	public static final String OPERAZIONE_RESPONSE = "response";
	public static final String OPERAZIONE_GETCAUSALI = "GETCAUSALI";
	public static final String OPERAZIONE_GETVOCIA1 = "GETVOCIA1";
	public static final String OPERAZIONE_GETDATIA1 = "GETDATIA1";
	public static final String OPERAZIONE_GETCAUSALISTATICHE = "GETCAUSALISTATICHE";
	public static final String OPERAZIONE_GETTRASFERIMENTIMODA2 = "GETTRASFERIMENTIMODA2";
	public static final String OPERAZIONE_GETLISTASTATORENDICONTAZIONE = "GETLISTASTATORENDICONTAZIONE";
	public static final String OPERAZIONE_GETLISTASTATORENDICONTAZIONECONCLUSA = "GETLISTASTATORENDICONTAZIONECONCLUSA";
	public static final String OPERAZIONE_GETSTATIENTE = "GETSTATIENTE";
	public static final String OPERAZIONE_GETANNIESERCIZIO = "GETANNIESERCIZIO";
	public static final String OPERAZIONE_GETLISTACOMUNI = "GETLISTACOMUNI";
	public static final String OPERAZIONE_GETLISTATIPOENTE = "GETLISTATIPOENTE";	
	public static final String OPERAZIONE_PRESTAZIONI = "GETPRESTAZIONI";
	public static final String OPERAZIONE_PRESTAZIONIASSOCIATE = "GETPRESTAZIONIASSOCIATE";
	public static final String OPERAZIONE_PRESTAZIONIRESSEMIRES = "GETPRESTAZIONIRESSEMIRES";
	public static final String OPERAZIONE_GETASL = "GETASL";
	public static final String OPERAZIONE_GETALLEGATIASSOCIATI = "GETALLEGATIASSOCIATI";
	public static final String OPERAZIONE_GETALLEGATOTODOWNLOAD = "GETALLEGATOTODOWNLOAD";
	public static final String OPERAZIONE_GETLISTADENOMINAZIONI = "GETLISTADENOMINAZIONI";	
	public static final String OPERAZIONE_GETSCHEDAENTEGESTORE = "GETSCHEDAENTEGESTORE";
	public static final String OPERAZIONE_SAVEMODELLOA2 = "SAVEMODELLOA2";
	public static final String OPERAZIONE_SAVEMODELLOA1 = "SAVEMODELLOA1";
	public static final String OPERAZIONE_GETVOCIA1_I2 = "I2";
	public static final String OPERAZIONE_GETTIPOVOCID = "GETTIPOVOCID";
	public static final String OPERAZIONE_GETVOCEMODD = "GETVOCEMODD";
	public static final String OPERAZIONE_GETTIPOVOCIMACROAGGR = "GETTIPOVOCIMACROAGGR";
	public static final String OPERAZIONE_GETMSGINFO = "GETMSGINFO";
	public static final String OPERAZIONE_GETMSGAPP = "GETMSGAPP";
	public static final String OPERAZIONE_GETPARAMETRO = "GETPARAMETRO";
	public static final String OPERAZIONE_GETDESCSTATOREND = "GETDESCSTATOREND";
	public static final String OPERAZIONE_GETRTIPOVOCEMODD = "GETRTIPOVOCEMODD";
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODDBYIDSCHEDA = "GETRENDICONTAZIONEMODDBYIDSCHEDA";
	public static final String OPERAZIONE_SAVEMODELLOD = "SAVEMODELLOD";
	public static final String OPERAZIONE_GETMODELLOTRANCHE = "GETMODELLOTRANCHE";
	public static final String OPERAZIONE_INVIAMODELLI = "INVIAMODELLI";
	public static final String OPERAZIONE_CONFERMAINVIAMODELLI = "CONFERMAINVIAMODELLI";
	public static final String OPERAZIONE_CONFERMADATI1 = "CONFERMADATI1";
	public static final String OPERAZIONE_RICHIESTARETTIFICA1 = "RICHIESTARETTIFICA1";
	public static final String OPERAZIONE_CONFERMADATI2 = "CONFERMADATI2";
	public static final String OPERAZIONE_RICHIESTARETTIFICA2 = "RICHIESTARETTIFICA2";
	public static final String OPERAZIONE_OPERAZIONEVALIDA = "VALIDA";
	public static final String OPERAZIONE_OPERAZIONESTORICIZZA = "STORICIZZA";
	public static final String OPERAZIONE_GETINFORENDICONTAZIONEOPERATORE = "GETINFORENDICONTAZIONEOPERATORE";
	public static final String OPERAZIONE_GETMACROAGGREGATO = "GETMACROAGGREGATO";
	public static final String OPERAZIONE_GETSPESAMISSIONE = "GETSPESAMISSIONE";
	public static final String OPERAZIONE_GETRSPESAMACRO = "GETRSPESAMACRO";
	public static final String OPERAZIONE_GETRENDICONTAZIONESPESAMACROBYIDSCHEDA = "GETRENDICONTAZIONESPESAMACROBYIDSCHEDA";
	public static final String OPERAZIONE_GETRENDICONTAZIONETOTALESPESAPERMODB = "GETRENDICONTAZIONETOTALESPESAPERMODB";
	public static final String OPERAZIONE_GETRENDICONTAZIONETOTALEMACROAGGREGATIPERMODB1 = "GETRENDICONTAZIONETOTALEMACROAGGREGATIPERMODB1";
	public static final String OPERAZIONE_ISRENDICONTAZIONESPESAMACROCOMPILEDBYIDSCHEDA = "OPERAZIONE_ISRENDICONTAZIONESPESAMACROCOMPILEDBYIDSCHEDA";
	public static final String OPERAZIONE_SAVEMACROAGGREGATI = "SAVEMACROAGGREGATI"; 
	public static final String OPERAZIONE_SAVEMODELLOB = "SAVEMODELLOB";
	public static final String OPERAZIONE_SAVEMODELLOE = "SAVEMODELLOE"; 
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODB = "GETRENDICONTAZIONEMODB";
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODE = "GETRENDICONTAZIONEMODE";
	public static final String OPERAZIONE_GETMISSIONIMODB = "GETMISSIONIMODB"; 
	public static final String OPERAZIONE_GETCONTEGGIMODF = "GETCONTEGGIMODF"; 
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODF = "GETRENDICONTAZIONEMODF";
	public static final String OPERAZIONE_SAVEMODELLOF = "SAVEMODELLOF";
	public static final String OPERAZIONE_GETPRESTAZIONIMODC = "GETPRESTAZIONIMODC"; 
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODC = "GETRENDICONTAZIONEMODC";
	public static final String OPERAZIONE_SAVEMODELLOC = "SAVEMODELLOC";
	public static final String OPERAZIONE_GETVOCIALLD = "GETVOCIALLD"; 
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODALLD = "GETRENDICONTAZIONEMODALLD"; 
	public static final String OPERAZIONE_SAVEMODELLOALLD = "SAVEMODELLOALLD";
	public static final String OPERAZIONE_ESPORTAMODFNPS = "ESPORTAMODFNPS";
	public static final String OPERAZIONE_ISMODULOFNPSCOMPILED = "ISMODULOFNPSCOMPILED"; 
	public static final String OPERAZIONE_CANACTIVEMODB = "CANACTIVEMODB";
	public static final String OPERAZIONE_CANACTIVEMODFNPS = "CANACTIVEMODCANACTIVEMODFNPS";
	public static final String OPERAZIONE_COMUNE_CAPOLUOGO = "COMUNE CAPOLUOGO";
	public static final String OPERAZIONE_CONSORZIO_COMUNI = "CONSORZIO DI COMUNI";
	public static final String OPERAZIONE_CONVENZIONE_COMUNI = "CONVENZIONE DI COMUNI";
	public static final String OPERAZIONE_DELEGA_ASL = "DELEGA ASL";
	public static final String OPERAZIONE_UNIONE_COMUNI = "UNIONE DI COMUNI";
	public static final String OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_A = "LISTAPRESTVALORIZZATEMODA";
	public static final String OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_B1 = "LISTAPRESTVALORIZZATEMODB1";
	public static final String OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_C = "LISTAPRESTVALORIZZATEMODC";
	public static final String OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A1 = "LISTACOMUNIVALORIZZATIMODA1";
	public static final String OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A2 = "LISTACOMUNIVALORIZZATIMODA2";
	public static final String OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_E = "LISTACOMUNIVALORIZZATIMODE";
	public static final String OPERAZIONE_GETTRANCHEMODELLO = "GETTRANCHEMODELLO";
	public static final String OPERAZIONE_GETPARAMETROPERINFORMATIVA = "GETPARAMETROPERINFORMATIVA";
	public static final String OPERAZIONE_GETATTIVITASOCIOASSIST = "GETATTIVITASOCIOASSIST";
	public static final String OPERAZIONE_GETMSGINFOPERCOD = "GETMSGINFOPERCOD";
	public static final String OPERAZIONE_DATI_SCHEDA_ENTE = "DATISCHEDAENTE";
	public static final String OPERAZIONE_MODELLI = "MODELLI_ASSOCIATI";
	public static final String OPERAZIONE_OBBLIGO_MODELLI = "MODELLI_OBBLIGO";
	public static final String OPERAZIONE_VERIFICA_MODELLI_VUOTO = "VERIFICA_MODELLI_VUOTO";
	public static final String OPERAZIONE_GETSCHEDAANAGRAFICA = "GETSCHEDAANAGRAFICA";
	public static final String OPERAZIONE_GETSCHEDAANAGRAFICAUNIONE = "GETSCHEDAANAGRAFICAUNIONE";
	public static final String OPERAZIONE_UNIONEENTI = "UNIONEENTI";
	public static final String OPERAZIONE_GETPROVINCIACOMUNELIBERO = "GETPROVINCIACOMUNELIBERO";
	public static final String OPERAZIONE_GETPRESTAZIONEREGIONALE = "GETPRESTAZIONEREGIONALE";
	public static final String GETDATAINIZIOMAX = "GETDATAINIZIOMAX";
	public static final String OPERAZIONE_GETUTENTI = "GETUTENTI";
	public static final String OPERAZIONE_GETUTENZE = "GETUTENZE";
	public static final String OPERAZIONE_GETUTENZEANNOESERCIZIO = "GETUTENZEANNOESERCIZIO";
	public static final String OPERAZIONE_GETFONDIENTE = "GETFONDIENTE";
	public static final String OPERAZIONE_GETUTENZEFNPS = "GETUTENZEFNPS";
	public static final String OPERAZIONE_GETPROFILI = "GETPROFILI";
	public static final String OPERAZIONE_GETLISTE = "GETLISTE";
	public static final String OPERAZIONE_GETENTI = "GETENTI";
	public static final String OPERAZIONE_ELIMINAABILITAZIONE = "ELIMINAABILITAZIONE";
	public static final String OPERAZIONE_ELIMINAUTENTE = "ELIMINAUTENTE";
	public static final String OPERAZIONE_MODIFICAUTENTE = "MODIFICAUTENTE";
	public static final String OPERAZIONE_CREAUTENTE = "CREAUTENTE";
	public static final String OPERAZIONE_CREAUTENZEFNPS = "CREAUTENZEFNPS";
	public static final String OPERAZIONE_ELIMINAUTENZAFNPS = "ELIMINAUTENZAFNPS";
	public static final String OPERAZIONE_GETABILITAZIONI = "GETABILITAZIONI";
	public static final String OPERAZIONE_MODIFICAABILITAZIONI = "MODIFICAABILITAZIONI";
	public static final String OPERAZIONE_GETLISTAPROFILO = "GETLISTAPROFILO";
	public static final String OPERAZIONE_GETAZIONI = "GETAZIONI";
	public static final String OPERAZIONE_ELIMINAAZIONE = "ELIMINAAZIONE";
	public static final String OPERAZIONE_ELIMINAPROFILO = "ELIMINAPROFILO";
	public static final String OPERAZIONE_MODIFICAAZIONI = "MODIFICAAZIONI";
	public static final String OPERAZIONE_GETAZIONIPROFILO = "GETAZIONIPROFILO";
	public static final String OPERAZIONE_MODIFICAPROFILO = "MODIFICAPROFILO";
	public static final String OPERAZIONE_CREAPROFILO = "CREAPROFILO";
	public static final String OPERAZIONE_GETLISTA = "GETLISTA";
	public static final String OPERAZIONE_GETENTILISTA = "GETENTILISTA";
	public static final String OPERAZIONE_ELIMINALISTA = "ELIMINALISTA";
	public static final String OPERAZIONE_MODIFICALISTA = "MODIFICALISTA";
	public static final String OPERAZIONE_GETLISTAENTI = "GETLISTAENTI";
	public static final String OPERAZIONE_ELIMINAENTE = "ELIMINAENTE";
	public static final String OPERAZIONE_MODIFICAENTI = "MODIFICAENTI";
	public static final String OPERAZIONE_CREALISTA = "CREALISTA";
	
	//MODELLO ALLONTANAMENTO ZERO
	public static final String OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO = "OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO";
	public static final String OPERAZIONE_GET_FONDI_ENTE_AL_ZERO = "OPERAZIONE_GET_FONDI_ENTE_AL_ZERO";
	public static final String TOGGLE_VALIDAZIONE_AL_ZERO = "TOGGLE_VALIDAZIONE_AL_ZERO";
	public static final String GET_VALIDAZIONE_AL_ZERO = "GET_VALIDAZIONE_AL_ZERO";
	public static final String SALVA_MODELLO_AL_ZERO = "SALVA_MODELLO_AL_ZERO";
	public static final String GET_STATUS_MODELLO_AL_ZERO = "GET_STATUS_MODELLO_AL_ZERO";
	public static final String MAKE_CSV_AL_ZERO = "MAKE_CSV_AL_ZERO";
	public static final String GET_PRESTAZIONI_ADATTAMENTO_FNPS = "GET_PRESTAZIONI_ADATTAMENTO_FNPS";
	
	//MODELLO A
	public static final String OPERAZIONE_GETTITOLIMODA = "GETTITOLIMODA";
	public static final String OPERAZIONE_GETVOCIMODA = "GETVOCIMODA";
	public static final String OPERAZIONE_GETRENDICONTAZIONEMODA_PART3 = "GETRENDICONTAZIONEMODA_PART3";
	public static final String OPERAZIONE_GETLISTAVOCIMODA = "GETLISTAVOCIMODA";
	public static final String OPERAZIONE_SAVEMODELLOA = "SAVEMODELLOA";

	
	// Sezioni Voci Modello A
	public static final String MODELLO_A_SEZIONE_FONDI = "sezione fondi";
	
	
	//MODELLO B1
	public static final String OPERAZIONE_B1_GET_PRESTAZIONI = "GET_PRESTAZIONI";
	public static final String OPERAZIONE_B1_GET_ELENCO_LBL = "GET_ELENCO_LBL";
	public static final String OPERAZIONE_B1_GET_PROGRAMMI_MISSIONE_TOT_MODB = "GET_PROGRAMMI_MISSIONE_TOT_MODB";
	public static final String OPERAZIONE_B1_SAVE = "B1_SAVE";
	
	
	public static final String OPERAZIONE_SALVA = "SALVA";
	public static final String OPERAZIONE_INVIA = "INVIA";
	public static final String OPERAZIONE_CONFERMA_DATI_I = "CONFERMA-DATI-I";
	public static final String OPERAZIONE_RICHIESTA_RETTIFICA_I = "RICHIESTA-RETTIFICA-I";
	public static final String OPERAZIONE_CONFERMA_DATI_II = "CONFERMA-DATI-II";
	public static final String OPERAZIONE_RICHIESTA_RETTIFICA_II = "RICHIESTA-RETTIFICA-II";
	public static final String OPERAZIONE_VALIDA = "VALIDA";
	public static final String OPERAZIONE_STORICIZZA = "STORICIZZA";

	public static final String STATO_RENDICONTAZIONE_DA_COMPILARE_I = "DACI";
	public static final String STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I = "INCI";
	public static final String STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I = "IARI";
	public static final String STATO_RENDICONTAZIONE_IN_RIESAME_I = "INRI";
	public static final String STATO_RENDICONTAZIONE_DA_COMPILARE_II = "DACII";
	public static final String STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II = "INCII";
	public static final String STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II = "IARII";
	public static final String STATO_RENDICONTAZIONE_IN_RIESAME_II = "INRII";
	public static final String STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE = "IAV";
	public static final String STATO_RENDICONTAZIONE_VALIDATA = "VAL";
	public static final String STATO_RENDICONTAZIONE_STORICIZZATA = "STO";
	public static final String STATO_RENDICONTAZIONE_CONCLUSA = "CON";
	
	public static final String TRANCHEI = "PT";
	public static final String TRANCHEII = "ST";
	public static final String AL_ZERO = "ALZ";
	public static final String PRIMA_TRANCHE = "Prima Tranche";	
	public static final String SECONDA_TRANCHE = "Seconda Tranche";
	
	//Modelli
	public static final String MODELLO_A ="A";
	public static final String MODELLO_A1 ="A1";
	public static final String MODELLO_A2 ="A2";
	public static final String MODELLO_AllD ="AllD";
	public static final String MODELLO_B ="B";
	public static final String MODELLO_B1 ="B1";
	public static final String MODELLO_C ="C";
	public static final String MODELLO_D ="D";
	public static final String MODELLO_E ="E";
	public static final String MODELLO_F ="F";
	public static final String MODELLO_MA ="MA";
	public static final String MACROAGGREGATI ="MACROAGGREGATI";
	
	public static final String MODELLOA_02ALTRO ="02-ALTRO";
	public static final String MODELLOA_TIPOALTRO ="Tipo_altro";

	public static final String OBBLIGATORIO ="OB";
	public static final String FACOLTATIVO ="FA";
	//Parametri
	public static final String MAIL_FROM = "MAIL_FROM";
	
	// Mail
	public static final String MAIL_GENERA_NUOVO_ANNO_CONTABILE = "GENERA_NUOVO_ANNO_CONTABILE";
	public static final String MAIL_MODIFICA_DATI_ANAG_ENTE = "MODIFICA_DATI_ANAG_ENTE";
	public static final String MAIL_MODIFICA_DATI_RENDICONTAZIONE = "MODIFICA_DATI_RENDICONTAZIONE";
	public static final String MAIL_CANCELLA_ENTE_GESTORE = "CANCELLA_ENTE_GESTORE";
	public static final String MAIL_GENERA_NUOVO_ENTE_GESTORE = "GENERA_NUOVO_ENTE_GESTORE";
	public static final String MAIL_CONFERMA_DATI_REND_I_TRANCHE = "CONFERMA_DATI_REND_I_TRANCHE";
	public static final String MAIL_RICHIESTA_RETTIFICA_I_TRANCHE = "RICHIESTA_RETTIFICA_I_TRANCHE";
	public static final String MAIL_CONFERMA_DATI_REND_II_TRANCHE = "CONFERMA_DATI_REND_II_TRANCHE";
	public static final String MAIL_RICHIESTA_RETTIFICA_II_TRANCHE = "RICHIESTA_RETTIFICA_II_TRANCHE";
	public static final String MAIL_INVIA_I_TRANCHE = "INVIA_I_TRANCHE";
	public static final String MAIL_INVIA_II_TRANCHE = "INVIA_II_TRANCHE";
	public static final String MAIL_MODIFICA_DATI_ENTE_STORICO = "MODIFICA_DATI_ENTE_STORICO";
	public static final String MAIL_CHIUSURA_ENTE = "CHIUSURA_ENTE";
	public static final String MAIL_RIPRISTINO_ENTE = "RIPRISTINO_ENTE";
	public static final String MAIL_MODIFICA_DATI_FNPS = "MODIFICA_DATI_FNPS";
	
	//Note Cronologia
	public static final String NOTA_CAMBIO_STATO_REDICONTAZIONE = "Cambio stato rendicontazione";
	public static final String NOTA_AGGIORNAMENTO_DATI = "Effettuato aggiornamento dati";

	//controllo campi invio
	public static final String MODELLO_A_06 = "06";
	public static final String MODELLO_A_07 = "07";
	public static final String MODELLO_A_08 = "08";
	public static final String MODELLO_A_31 = "31";
	public static final String MODELLO_A_32 = "32";

	//export modelli
	public static final String OPERAZIONE_ESPORTAMODELLOA1 = "ESPORTAMODELLOA1";
	public static final String OPERAZIONE_ESPORTAMODELLOA= "ESPORTAMODELLOA";
	public static final String OPERAZIONE_ESPORTAMODELLOD = "ESPORTAMODELLOD";
	public static final String OPERAZIONE_ESPORTAMODELLOA2 = "ESPORTAMODELLOA2";
	public static final String OPERAZIONE_ESPORTAMACROAGGREGATI = "ESPORTAMACROAGGREGATI";
	public static final String OPERAZIONE_ESPORTAMODELLOB = "ESPORTAMODELLOB";
	public static final String OPERAZIONE_ESPORTAMODELLOE = "ESPORTAMODELLOE";
	public static final String OPERAZIONE_ESPORTAMODELLOF = "ESPORTAMODELLOF";
	public static final String OPERAZIONE_ESPORTAMODELLOB1 = "ESPORTAMODELLOB1";
	public static final String OPERAZIONE_ESPORTAMODELLOC = "ESPORTAMODELLOC";
	public static final String OPERAZIONE_ESPORTAISTAT = "ESPORTAESPORTAISTAT";
	
	//Section Msg Informativi
	public static final String SECTION_MODB_DESC_TOTALI = "MODELLO B DESC TOTALI";
	public static final String SECTION_MODB_DESC_TOTALI_MISSIONE = "MODELLO B DESC TOTALI MISSIONE";
	public static final String SECTION_MODE_TOOLTIP = "MODELLO E TOOLTIP";
	
	//Sessione
	public static final String MESSAGE_SESSION_DOWN = "Caduta di Sessione";
	
	//pte2
		public static final String ERRORE_LOGIN_PROFILO = "ERR57";
		public static final String ANNODAGENERARE = "ANNODAGENERARE";
		
	//stato
	public static final String CHIUSO = "CHI";
	public static final String APERTO = "APE";
	//regione
	public static final String CODREGIONE = "CODREGIONE";
	
	//Motivazioni chiusura
	public static final String MOTIVAZIONEUNIONEENTE="03";
	
	//Check
	public static final String OPERAZIONE_CHECKMODELLOA = "CHECKMODELLOA";
	public static final String OPERAZIONE_CHECKMODELLOA1 = "CHECKMODELLOA1";
	public static final String OPERAZIONE_CHECKMACROAGGREGATI = "CHECKMACROAGGREGATI";
	public static final String OPERAZIONE_CHECKMODELLOB1 = "CHECKCHECKMODELLOB1";
	public static final String OPERAZIONE_CHECKMODELLOB = "CHECKMODELLOB";
	public static final String OPERAZIONE_SAVEMOTIVAZIONECHECK = "SAVEMOTIVAZIONECHECK";
	public static final String CHECK_OK="CHECKOK";
	public static final String CHECK_OK_MOTIVAZIONE="CHECKOK_MOTIVAZIONE";
	public static final String CHECK_MOTIVAZIONE_ESISTE_CON_ERRORI="CHECK_MOTIVAZIONE_ESISTE_CON_ERRORI";
	public static final String CHECK_BLOCCANTE="CHECK_BLOCCANTE";
	public static final String CHECK_MODELLOA01="CHECK_MODELLOA01";
	public static final String CHECK_MODELLOA02="CHECK_MODELLOA02";
	public static final String CHECK_MODELLOA03="CHECK_MODELLOA03";
	public static final String CHECK_MODELLOA04="CHECK_MODELLOA04";
	public static final String CHECK_MODELLOA05="CHECK_MODELLOA05";
	public static final String CHECK_MOTIVAZIONE_ESISTE = "CHECK_MOTIVAZIONE_ESISTE";
	public static final String INSERIMENTO_MOTIVAZIONE="INSERIMENTO_MOTIVAZIONE";
	public static final String CHECK_MODELLOA101="CHECK_MODELLOA101";
	public static final String CHECK_MACROAGGREGATI01="CHECK_MACROAGGREGATI01";
	public static final String CHECK_MACROAGGREGATI02="CHECK_MACROAGGREGATI02";
	public static final String CHECK_MACROAGGREGATI03="CHECK_MACROAGGREGATI03";
	public static final String CHECK_MACROAGGREGATI04="CHECK_MACROAGGREGATI04";
	public static final String CHECK_MACROAGGREGATI05="CHECK_MACROAGGREGATI05";
	public static final String CHECK_MACROAGGREGATI06="CHECK_MACROAGGREGATI06";
	public static final String CHECK_MODELLO_B101="CHECK_MODELLO_B101";
	public static final String CHECK_MODELLO_B01="CHECK_MODELLO_B01";
	public static final String CHECK_MODELLO_B02="CHECK_MODELLO_B02";
	public static final String CHECK_MODELLO_B03="CHECK_MODELLO_B03";
	public static final String CHECK_MODELLO_B04="CHECK_MODELLO_B04";
	public static final String CHECK_MODELLO_B05="CHECK_MODELLO_B05";
	public static final String CHECK_MODELLO_B06="CHECK_MODELLO_B06";
	public static final String CHECK_MODELLO_B07="CHECK_MODELLO_B07";
	public static final String CHECK_MODELLO_C01="CHECK_MODELLO_C01";
	public static final String CHECK_MODELLO_F01="CHECK_MODELLO_F01";
	public static final String CHECK_MODELLO_F02="CHECK_MODELLO_F02";
	
	//StatoModello
	public static final String NON_ASSEGNATO="NON_ASSEGNATO";
	public static final String NON_DISPONIBILE="NON_DISPONIBILE";
	public static final String NON_COMPILATO="NON_COMPILATO";
	public static final String NON_CONFORME="NON_CONFORME";
	public static final String COMPILATO_PARZIALE="COMPILATO_PARZIALE";
	public static final String COMPILATO="COMPILATO";
	
	//Cruscotto
	public static final String MSG01CRUSCOTTO="MSG01CRUSCOTTO";
	public static final String MSG02CRUSCOTTO="MSG02CRUSCOTTO";
	
	//Configuratore
	public static final String MSG01CONFIGURATORE="79";
	public static final String MSG02CONFIGURATORE="80";
	
	//CreaAnno
	public static final String CREANUOVOANNO_ERRORE="CREANUOVOANNO_ERRORE";
	public static final String CREANUOVOANNO_OK="CREANUOVOANNO_OK";
	
	//Concludi
	public static final String CONCLUDI_MULTIPLO_OK="CONCLUDI_MULTIPLO_OK";
	public static final String CONCLUDI_SINGOLO_OK="CONCLUDI_SINGOLO_OK";
	public static final String RIAPRI_MULTIPLO_OK="RIAPRI_MULTIPLO_OK";
	public static final String RIAPRI_SINGOLO_OK="RIAPRI_SINGOLO_OK";
	public static final String RIAPRI_COMPILAZIONE_MULTIPLO_OK="RIAPRI_COMPILAZIONE_MULTIPLO_OK";
	public static final String RIAPRI_COMPILAZIONE_SINGOLO_OK="RIAPRI_COMPILAZIONE_SINGOLO_OK";
	public static final String STORICIZZA_MULTIPLO_OK="STORICIZZA_MULTIPLO_OK";
	public static final String STORICIZZA_SINGOLO_OK="STORICIZZA_SINGOLO_OK";
	//GestioneUtenti
	public static final String ESISTENTE="esistente";
	public static final String NUOVO="nuovo";
	public static final String MODIFICATO="modificato";
	public static final String CONFERMAAGGIORNAMENTOUTENTE = "90";
	public static final String CONFERMAELIMINAZIONEUTENTE = "91";
	public static final String CODICEFISCALEESISTENTE = "92";
	public static final String ABILITAZIONEESISTENTE = "93";
	public static final String CONFERMACREAUTENTE = "94";
	public static final String CONFERMAAGGIORNAMENTOPROFILO = "96";
	public static final String CONFERMAELIMINAZIONEPROFILO = "97";
	public static final String CONFERMACREAPROFILO = "99";
	public static final String CODICEPROFILOESISTENTE = "100";
	public static final String CONFERMAELIMINAZIONELISTA = "103";
	public static final String CONFERMAAGGIORNAMENTOLISTA = "102";
	public static final String CONFERMACREALISTA = "105";
	public static final String CODICELISTAESISTENTE = "106";
	
	//Configuratore FNPS
	public static final String SALVATAGGIO_FNPS_UTENZE_CALCOLO = "SALVATAGGIO_FNPS_UTENZE_CALCOLO";
	public static final String ELIMINAZIONE_FNPS_UTENZE_CALCOLO = "ELIMINAZIONE_FNPS_UTENZE_CALCOLO";
	public static final String AZIONESISTEMA = "AZIO";
	public static final String ALTREQUOTE = "ALQ";
	public static final String FNPS = "FONDO";
	public final static String MESSAGE_AZZERA_FNPS = "MSGFNPSAZZERA";
	public final static String MESSAGE_AZZERA_ALZERO = "MSGALZEROAZZERA";
}

