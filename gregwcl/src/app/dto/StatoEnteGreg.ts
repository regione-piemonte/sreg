/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { StatoRendicontazioneGreg } from "./StatoRendicontazioneGreg";

export class StatoEnteGreg {
    public notaEnte: string;
    public notaInterna: string;
    public operatore: string;
    public stato: string;
    public dal: Date;
    public al: Date;
    public motivazione: string;

    constructor(){}
    
}