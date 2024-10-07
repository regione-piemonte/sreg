/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ComuneAssociatoGreg {
    constructor(
        public pkIdSchedaComuneAssociato: number,
        public codiceIstat : string,
        public desComune: string,
        public idComune: number,
		public dal: string,
		public al: string
    ) {}
}
