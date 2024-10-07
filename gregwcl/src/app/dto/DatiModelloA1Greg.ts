/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

	export class DatiModelloA1Greg {
    constructor(
        public valore : any[][] = [],
        public codcomune: string,
        public desccomune: string,
        public dataInizioValidita: Date,
        public dataFineValidita: Date,
        public idComune: number

    ) {}
}