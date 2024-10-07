/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class PrestazioneGreg {
    constructor(
        public idPrestazione: number,
        public codicePrestazione: string,
        public desPrestazione: string,
        public prestFiglia: boolean,
        public prestazioniCollegate: PrestazioneGreg[],
    ){}
    
}
