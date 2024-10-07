/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class RicercaListaEntiDaunire {
    constructor(
        public statoEnte: boolean,
        public lista: number[],
        public dataMerge: Date,
		public codregionale: string
    ){}
}