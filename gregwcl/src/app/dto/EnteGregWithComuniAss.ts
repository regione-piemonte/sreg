/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class EnteGregWithComuniAss {
    constructor(
        public idSchedaEnteGestore: number,
        public denominazione: string,
		public comuniAssociati: number[]
    ) {}
}

