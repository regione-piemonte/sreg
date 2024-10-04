export const enum DATIENTE_INFO {    
    INFO_PARAM_TITLE = 'Informazioni Parametri',
    INFO_DOC_TITLE = 'Informazioni Documentazione',
    INFO_PREST_TITLE = 'Informazioni Prestazione',

    INFO_DOC_INIZIALI_TITLE = 'Informazioni Documenti Iniziali',
    INFO_DOC_FINALI_TITLE = 'Informazioni Documenti Finali',
}

/*export const enum ROLES {
    OPERATORE_REGIONALE = 'O-REG',
    RESPONSABILE_ENTE = 'EG',
}*/

export const enum PROFILES {
    EG = 'EG',
    O_REG = 'O-REG',
}

export const enum PROJECT_CONSTANTS {
    BACKOFFICE_TITLE = 'Backoffice Welfare',
    LOADING = 'Loading...',
    LOADING_TEXT = 'Caricamento del sistema in corso...'
}

export const enum FOOTER_CONSTANTS {
    ASSISTENZA = 'ASSISTENZA',
    HELP = 'Hai bisogno di aiuto?',
    WRITE = 'Scrivi a ',
    SPECIFIC = 'specificando:',
    INFO = 'il tuo recapito telefonico - il codice fiscale - il problema che hai rilevato',
    SISTEMA = '',
    SERVIZIO = 'Servizio a cura della Regione Piemonte',
    P_IVA = 'P.Iva-CF 80087670016',
    PORTAL = 'sisp',
    MAIL_ASSISTENZA = 'assistenza.entigestori@csi.it'
}

export const enum MENU {
    HELP = 'HELP',
}

export const enum PATTERN {
    CHARS = '^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.\'-]*$',
    others = '^[\/\\\*\^]$',
    CODFISC = '[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]'
}


export const enum RENDICONTAZIONE_STATUS {
    DA_COMPILARE_I = 'DACI',
    IN_COMPILAZIONE_I = 'INCI',
    IN_RIESAME_I = 'INRI',
    DA_COMPILARE_II = 'DACII',
    IN_COMPILAZIONE_II = 'INCII',
    IN_RIESAME_II = 'INRII',
    ATTESA_VALIDAZIONE = 'IAV',
    VALIDATA = 'VAL',
    STORICIZZATA = 'STO',
    ATTESA_RETTIFICA_I = 'IARI',
    ATTESA_RETTIFICA_II = 'IARII',
    CONCLUSA = 'CON',
}

export const enum AZIONE {
	EntiGestori	 =  'EntiGestori',
	Archivio	 =  'Archivio',
	ConfiguratorePrestazioni	 =  'ConfiguratorePrestazioni',
	Cruscotto	 =  'Cruscotto',
	DatiEnte	 =  'DatiEnte',
	DatiRendicontazione	 =  'DatiRendicontazione',
	EnteContatti	 =  'EnteContatti',
	EnteResponsabile	 =  'EnteResponsabile',
	EnteComuni	 =  'EnteComuni',
	DatiEntePrestazioni	 =  'DatiEntePrestazioni',
	DatiEnteAllegati	 =  'DatiEnteAllegati',
	DatiEnteParametri	 =  'DatiEnteParametri',
	ModelloD	 =  'ModelloD',
	EsportaModelloD	 =  'EsportaModelloD',
	ModelloMacroaggregati	 =  'ModelloMacroaggregati',
	EsportaModelloMacroaggregati	 =  'EsportaModelloMacroaggregati',
	ModelloB1	 =  'ModelloB1',
	EsportaModelloB1	 =  'EsportaModelloB1',
	ModelloB	 =  'ModelloB',
	EsportaModelloB	 =  'EsportaModelloB',
	ModelloC	 =  'ModelloC',
	EsportaModelloC	 =  'EsportaModelloC',
	ModelloE	 =  'ModelloE',
	EsportaModelloE	 =  'EsportaModelloE',
	ModelloF	 =  'ModelloF',
	EsportaModelloF	 =  'EsportaModelloF',
	ModelloFNPS	 =  'ModelloFNPS',
	EsportaModelloFNPS	 =  'EsportaModelloFNPS',
	CronologiaRegionale	 =  'CronologiaRegionale',
	CronologiaEnte	 =  'CronologiaEnte',
	SalvaDatiEnte	 =  'SalvaDatiEnte',
	SalvaModelliI	 =  'SalvaModelliI',
	InviaI	 =  'InviaI',
	InviaII	 =  'InviaII',
	RichiestaRettificaI	 =  'RichiestaRettificaI',
	ConfermaDatiI	 =  'ConfermaDatiI',
	RichiestaRettificaII	 =  'RichiestaRettificaII',
	ConfermaDatiII	 =  'ConfermaDatiII',
	Valida	 =  'Valida',
	Storicizza	 =  'Storicizza',
	ChiudiEnte	 =  'ChiudiEnte',
	RiprstinaEnte	 =  'RiprstinaEnte',
	InserisciNuovoEnte	 =  'InserisciNuovoEnte',
	UnisciEnti	 =  'UnisciEnti',
	CreaNuovoAnno	 =  'CreaNuovoAnno',
	ModelloA	 =  'ModelloA',
	EsportaModA	 =  'EsportaModA',
	ModelloA1	 =  'ModelloA1',
	EsportaModelloA1	 =  'EsportaModelloA1',
	ModelloA2	 =  'ModelloA2',
	EsportaModelloA2	 =  'EsportaModelloA2',
	PulsanteStoricoContatti	 =  'PulsanteStoricoContatti',
	SalvaModelliII	 =  'SalvaModelliII',
	SalvaModelliIII	 =  'SalvaModelliIII',
}

export const enum DOC {
    DOC_INIZIALE = 'Documento Iniziale',
    DOC_FINALE = 'Documento Finale',
    DOC_AL_ZERO = 'Documento Allontanamento Zero',
}

export const enum SECTION {
    DATI_ENTE = 'DATI ENTE',
	MODELLOA1 = 'MODELLO A1',
	MODELLOA2 = 'MODELLO A2',
	MODELLOA = 'MODELLO A',
	MODELLOB = 'MODELLO B',
	MODELLOD = 'MODELLO D',
	MODELLOB1 = 'MODELLO B1',
	MODELLOMACRO = 'MODELLO MACRO',
    MACROAGGREGATITOOLTIP = 'MACROAGGREGATI TOOLTIP',
    MACROAGGREGATIIMGM = 'MACROAGGREGATI IMG M',
    MACROAGGREGATIPOPUP = 'MACROAGGREGATI POP UP',
    ALLD01 = 'ALLD01',
    ALLD02 = 'ALLD02',
    ALLD03 = 'ALLD03',
    ALLD04 = 'ALLD04',
    ALLD05 = 'ALLD05',
    ALLD06 = 'ALLD06',
    ALLD07 = 'ALLD07',
    ALLD08 = 'ALLD08',
    ALLD09 = 'ALLD09',
    MODELLOCINFO = 'MODELLO C INFO',
    MODELLOCINFODISABILITA = 'MODELLO C INFO DISABILITA',
    MODELLOCINFOTOTDISABILITA = 'MODELLO C INFO TOTDISABILITA',
    MODELLOCINFOTOTDISABILITAADULTI= 'MODELLO C INFO TOTDISABILITA ADULTI',
    MODELLOC_R_A_1_1 = 'MODELLO C R_A.1.1',
    MODELLOC_R_A_2_1 = 'MODELLO C R_A.2.1',
    MODELLOC_TAB= 'MODELLO C TAB',
	MODELLOE = 'MODELLO E',
    MODELLOE_POPOVER = 'MODELLO E TOOLTIP',
    MODELLOE_CANCELLAZIONE = 'MODELLO E CANCELLAZIONE',
	MODELLOF = 'MODELLO F',
    MODELLOFTITOLO = 'MODELLO F TITOLO',
    MODELLOFMSG1 = 'MODELLO F MSG1',
    MODELLOFMSG2 = 'MODELLO F MSG2',
    MODELLOFMSG3 = 'MODELLO F MSG3',
    MODELLOFMSG4 = 'MODELLO F MSG4',
    MODELLOFMSG5 = 'MODELLO F MSG5',
	MODELLOC = 'MODELLO C',
	CODMODELLOC = 'C',
	CODMODELLOA1 = 'A1',
	CODMODELLOA2 = 'A2',
	CODMODELLOA = 'A',
	CODMODELLOB = 'B',
	CODMODELLOD = 'D',
	CODMODELLOB1 = 'B1',
	CODMODELLOMACRO = 'MA',
	CODMODELLOE = 'E',
	CODMODELLOF = 'F',
	ALLEGATOD = 'Modulo MFNPS',
	CODALLEGATOD = 'MFNPS',
    MODELLOBTAB ='MODELLO B TAB',
    MODELLOBDESCTOTALI ='MODELLO B DESC TOTALI',
    MODELLOBDESCTOTALIMISSIONE = 'MODELLO B DESC TOTALI MISSIONE',
    SOGLIAFNPS = 'SOGLIAFNPS',
    MODATOOLTIP = 'MODATOOLTIP',
    MODFTOOLTIP = 'MODFTOOLTIP',
    ANNOESERCIZIO= 'ANNO_ESERCIZIO',
    MESSAGGIOELIMINA = 'MESSAGGIOELIMINA',
    MESSAGGIOANNULLA = 'MESSAGGIOANNULLA',
    MESSAGGIOCONCLUDISINGOLO = 'MESSAGGIOCONCLUDISINGOLO',
    MESSAGGIOCONCLUDIMULTIPLO = 'MESSAGGIOCONCLUDIMULTIPLO',
    MESSAGGIORIAPRISINGOLO = 'MESSAGGIORIAPRISINGOLO',
    MESSAGGIORIAPRIMULTIPLO = 'MESSAGGIORIAPRIMULTIPLO',
    MESSAGGIORIAPRICOMPILAZIONESINGOLO = 'MESSAGGIORIAPRICOMPILAZIONESINGOLO',
    MESSAGGIORIAPRICOMPILAZIONEMULTIPLO = 'MESSAGGIORIAPRICOMPILAZIONEMULTIPLO',
    MESSAGGIOSTORICIZZAMULTIPLO = 'MESSAGGIOSTORICIZZAMULTIPLO',
    MESSAGGIOSALVACONMODPASS = 'MESSAGGIOSALVACONMODPASS',
    MESSAGGIOELIMINAABILITAZIONE = 'MESSAGGIOELIMINAABILITAZIONE',
    MESSAGGIOELIMINAUTENTE = 'MESSAGGIOELIMINAUTENTE',
    CONFERMAAGGIORNAMENTOUTENTE = 'CONFERMAAGGIORNAMENTOUTENTE',
    MESSAGGIOELIMINAAZIONE = 'MESSAGGIOELIMINAAZIONE',
    CONFERMAAGGIORNAMENTOPROFILO = 'CONFERMAAGGIORNAMENTOPROFILO',
    MESSAGGIOELIMINAPROFILO = 'MESSAGGIOELIMINAPROFILO',
    MESSAGGIOELIMINAENTE = 'MESSAGGIOELIMINAENTE',
    MESSAGGIOELIMINALISTA = 'MESSAGGIOELIMINALISTA',
    CONFERMAAGGIORNAMENTOLISTA = 'CONFERMAAGGIORNAMENTOLISTA',
    PROBLEMAPRESTAZIONE = 'PROBLEMAPRESTAZIONE',
    MSGDATAVARIAZIONE = 'MSGDATAVARIAZIONE',
    MSGTITLEDATAVARIAZIONE = 'MSGTITLEDATAVARIAZIONE',
    MSGPOPUPDATAVARIAZIONE = 'MSGPOPUPDATAVARIAZIONE',
    MESSAGGIOELIMINAVINCOLO = 'MESSAGGIOELIMINAVINCOLO',
    MESSAGGIOELIMINAFONDO = 'MESSAGGIOELIMINAFONDO',
    CONFERMAELIMINAZIONEFONDI = 'CONFERMAELIMINAZIONEFONDI',
    LABELFNPS = 'LABELFNPS',
    MSGCALCOLO = 'MSGCALCOLO',
    FOOTERASSISTENZA = 'FOOTERASSISTENZA',
    ALZERO01 = 'ALZERO01',
    ALZERO02 = 'ALZERO02',
    ALZERO03 = 'ALZERO03',
    ALZERO04 = 'ALZERO04',
    ALZERO05 = 'ALZERO05',
    ALZERO06 = 'ALZERO06',
    ALZERO07 = 'ALZERO07',
    LABELALZERO = 'LABELALZERO',
    ALZEROGIUSTIFICAZIONE = 'ALZEROGIUSTIFICAZIONE',
    ALZEROFONDI = 'ALZEROFONDI',
}

export const enum ERRORS {
    ERROR_ANNO_CONTABILE = 'ERR13',
    ERROR_DELETE_PRESTAZIONI = 'ERR16',
    ERROR_UPDATE_DOC_ALLEGATI = 'ERR20',
    ERROR_COMUNE_ALREADY_PRESENT = 'ERR21',
    ERROR_PREST_ALREADY_PRESENT = 'ERR22',
    ERROR_UPLOAD_ALLEGATI = 'ERR23',
    ERROR_DELETE_PREST_ASS = 'ERR30',
    ERROR_DELETE_PREST_ASS_MODC = 'ERR43',
    ERROR_DELETE_COMUNE_ASS = 'ERR31',
    ERROR_EXPORT = 'ERR35',
    ERROR_MODB1 = 'ERRMODB1',
    ERROR_MODC01 = 'ERRMODC01',
    ERROR_MODC02 = 'ERRMODC02',
    ERROR_MODC03 = 'ERRMODC03',
    ERROR_MODC04 = 'ERRMODC04',
    ERROR_MODC05 = 'ERRMODC05',
    ERROR_MODC06 = 'ERRMODC06',
    ERROR_MODC07 = 'ERRMODC07',
    ERROR_MODC08 = 'ERRMODC08',
    ERROR_MODC09 = 'ERRMODC09',
    ERROR_MODC10 = 'ERRMODC10',
    ERROR_MODF01 = 'ERRMODF01',
    ERROR_FNPS01 = 'ERRFNPS01',
    ERROR_FNPS02 = 'ERRFNPS02',
    ERROR_FNPS03 = 'ERRFNPS03',
    ERROR_FNPS04 = 'ERRFNPS04',
    ERROR_FNPS05 = 'ERRFNPS05',
    ERROR_FNPS06 = 'ERRFNPS06',
    ERROR_PIPPI_FNPS = 'ERRPIPPIFNPS',
    ERROR_STATO_REND = 'ERRSTATOREND',
    ERROR_MODIF_MOD = 'ERRMODIFMOD',
    ERROR_MODELLO_ALREADY_PRESENT = 'ERR58',
    ERROR_DELETE_MODELLI_ASS = 'ERR59',
    ERRORE_SALVATAGGIO_FNPS_UTENZE_CALCOLO = 'ERRORE_SALVATAGGIO_FNPS_UTENZE_CALCOLO',
    ERRVINCOLOFNPS = 'ERRVINCOLOFNPS',
}

export const enum PREST_RES_SEMIRES {
    COD_TIPOLOGIA = 'MA03',
    TIPO_STRUTT_RES = '01',
    TIPO_STRUTT_CD = '02',
}

export const enum MSG {
    POPUP_MODELLI_RENDICONTAZIONE_EDIT = 'POPUP01',
    WARN_TAB = 'WARN-TAB',
    WARNDATIANAGRAFICI = 'WARNDATIANAGRAFICI'
}

export const enum TRANCHE {
    I_TRANCHE = 'PT',
    II_TRANCHE = 'ST',
    III_TRANCHE = 'TT',
    PRIMA_TRANCHE = "prima tranche",
    SECONDA_TRANCHE = "seconda tranche",
    TERZA_TRANCHE = "terza tranche",
}

export const enum OPERAZIONE {
    INVIAMODELLI = 'INVIAMODELLI',
    CONFERMAINVIAMODELLI = 'CONFERMAINVIAMODELLI',
 	SALVA = 'SALVA',
    CHECK = 'CHECK',
    VALIDAZIONE_AL_ZERO = 'VALIDAZIONE_AL_ZERO',
    ANNULLA_VALIDAZIONE_AL_ZERO = 'ANNULLA_VALIDAZIONE_AL_ZERO',
}

export const enum VOCE_TESTUALE_MOD_A {
    COD_TITOLO = '02-ALTRO',
    COD_TIPOLOGIA = 'Tipo_altro',
    COD_VOCE = '32'
}

export const enum VOCE_IMPORTO_MOD_A {
    COD_TITOLO = '02-ALTRO',
    COD_TIPOLOGIA = 'Tipo_altro',
    COD_VOCE = '31'
}

export const enum MESSAGES {
    MSG_INVIO_ATTENZIONE = 'MSG09',
}

export const enum TIPO_PROFILO {
    HOME_REGIONALE = 'OP-HomeRegionale',
    HOME_ENTE = 'OP-HomeEnte',
}

export const enum STATO_ENTE {
    APERTO = 'APE',
    CHIUSO = 'CHI',
}

export const enum STATO_ENTE_DESC_LUNGA {
    APERTO = 'APERTO',
    CHIUSO = 'CHIUSO',
}

export const enum STATO_ABILITAZIONE {
    ESISTENTE = 'esistente',
    NUOVO = 'nuovo',
    MODIFICATO = 'modificato'
}

export const enum TIPOLOGIA_FONDI {
    FNPS = 'FONDO',
    ALTREQUOTE = 'ALQ',
    AZIONISISTEMA = 'AZIO',
    ALTRO = 'ALT'
}