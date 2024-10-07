/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ColoreGreg {
    constructor(
        public idColore : number,
        public descColore: string,
        public rgb: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}