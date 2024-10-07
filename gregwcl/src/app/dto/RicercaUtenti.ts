/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class RicercaUtenti {
    constructor(
        public nome: string,
        public cognome: string,
        public codiceFiscale: string,
        public email: string,
        public profilo: number, 
        public lista: number,
        public ente: number,
        public attivo: boolean
    ){}
}