/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class RicercaGreg {
    constructor(
        public statoEnte: string,
        public denominazioneEnte: string,
        public statoRendicontazione: string,
        public tipoEnte: number,
        public comune: number, 
        public annoEsercizio: number,
        public lista: number[]
    ){}
}