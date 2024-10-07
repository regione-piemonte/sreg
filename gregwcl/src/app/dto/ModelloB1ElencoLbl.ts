/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelloB1ElencoLbl {
    public macroaggregati: ModelB1Lbl[];
    public missione_programma: ModelB1Lbl[];
    public utenze: ModelB1Lbl[];
    public msgInformativi: ModelB1Lbl[];

    public constructor() {}
}


export interface ModelB1Lbl {
    codice: string,
    descrizione: string,
    colore?: string,
    msgInformativo?: string
}

