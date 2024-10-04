export class VociModelloB1 {
    public macroaggregati: ModelRendicontazioneTotaliMacroaggregati;
    public elencoPrestazioni : ModelPrestazioniB1[];

    public constructor() {}
}

export interface ModelRendicontazioneTotaliMacroaggregati {
    idRendicontazioneEnte?: number;
	idSchedaEnteGestore?: number;
	denominazioneEnte?: string;
	annoGestione?: number;
	valoriMacroaggregati?: ModelTotaleMacroaggregati[];
}

export interface ModelTotaleMacroaggregati {
    descMacroaggregati?: string;
	codMacroaggregati?: string;
	totale?: string;
	ordinamento?: number;
}

export interface ModelPrestazioniB1 {
    codPrestazione: string,
    descPrestazione: string,
    tipoPrestazione: string,
    msgInformativo: string,
    msgInformativoQSA: string,
    macroaggregati: ModelB1VoceMacroaggregati[],
    utenze: ModelB1VocePrestazioniRegionali1Utenza[],
    prestazioniRegionali2: ModelB1VocePrestReg2[],
    utenzeCostoTotale: ModelB1VocePrestazioniRegionali1Utenza[],
    utenzeQuotaSocioAssistenziale: ModelB1VocePrestazioniRegionali1Utenza[]
}
export interface ModelB1VocePrestazioniRegionali1Utenza {
    codice: string,
    colore: string,
    codiceProgrammaMissione: string,
    idprestReg1UtenzaReg1: number,
    idtabellaRiferimento: number,
    valore: string,
    sommaUTASRModA: string,
    limiteMassimo: string,
    error: boolean,
    mandatory: boolean,
    disabled: boolean
}

export interface ModelB1VoceMacroaggregati {
    codice: string,
    idprestReg1MacroaggregatoBilancio: number,
    idtabellaRiferimento: number,
    valore: string,
    disabled: boolean,
}

export interface ModelB1VocePrestReg2 {
    codPrestazione: string,
    descPrestazione: string,
    utenze: ModelB1VocePrestazioniRegionali2Utenza[]
    
}

export interface ModelB1VocePrestazioniRegionali2Utenza {
    codice: string,
    idprestReg2UtenzaReg2: number,
    idtabellaRiferimento: number,
    valore: string,
    error: boolean
}

export interface ModelB1Totali {
    codice: string,
    totale: string
}