/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ProvinciaGreg {
    constructor(
        public idProvincia: number,
        public codIstatProvincia: string,
        public desProvincia: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ){}    
}