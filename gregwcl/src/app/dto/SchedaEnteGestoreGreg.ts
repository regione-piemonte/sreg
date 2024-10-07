/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class SchedaEnteGestoreGreg {
    constructor(
        public idSchedaEnteGestore: number,
        public centroDiurnoStruttSemires: boolean,
        public codIstatEnte: string,
        public codSchedaEnteGestore: string,
        public codiceFiscale: string,
        public codiceRegionale: string,
        public dataCancellazione: Date,
        public dataCreazione: Date,
        public dataFineValidita: Date,
        public dataInizioValidita: Date,
        public dataModifica: Date,
        public denominazione: string,
        public email: string,
        public fnps: number,
        public indirizzo: string,
        public partitaIva: string,
        public pec: string,
        public strutturaResidenziale: boolean,
        public telefono: string,
        public utenteOperazione: string,
        public vincoloFondo: number,
		public elencoAzioni: string[]=[]
    ){}
}