/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ResponsabileEnteGreg {
    constructor(
        public idResponsabile: number,
        public cognome: string,
        public nome: string,
        public cellulare: string,
        public telefono: string,
        public codiceFiscale: string,        
        public eMail: string
    ) {}
}