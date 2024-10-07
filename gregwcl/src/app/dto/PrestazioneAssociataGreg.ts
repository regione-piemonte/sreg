/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class PrestazioneAssociataGreg {

     public annoFineValidita: number;
     public dataFineValidita: Date;
    constructor(
        public pkIdPrestazioneAssociata: number,
        public codicePrestazione: string,
        public desPrestazione: string,
        public idPrestazione: number,
        public prestFiglia: boolean
    ){}
    
}