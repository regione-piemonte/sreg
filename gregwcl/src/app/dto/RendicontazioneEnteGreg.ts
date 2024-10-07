/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { StatoRendicontazioneGreg } from "./StatoRendicontazioneGreg";

export class RendicontazioneEnteGreg {
    constructor(
        public idRendicontazioneEnte: number,  
        public annoEsercizio: number,
        public idSchedaEnte: number,
        public statoRendicontazione: StatoRendicontazioneGreg,
        public strutturaResidenziale: boolean,
        public centroDiurnoStruttSemires: boolean,
        public fnps: number,
        public vincoloFondo: number,
        public pippi: number
    ) {}
}