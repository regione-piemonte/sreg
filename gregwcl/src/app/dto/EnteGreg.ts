/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AslGreg } from "./AslGreg";
import { ComuneGreg } from "./ComuneGreg";
import { ResponsabileEnteGreg } from "./ResponsabileEnteGreg";
import { StatoRendicontazioneGreg } from "./StatoRendicontazioneGreg";
import { TipoEnteGreg } from "./TipoEnteGreg";

export class EnteGreg {
    constructor(
        public idSchedaEntegestore: number,
        public codiceRegionale: string,
        public codiceFiscale: string,
        public denominazione: string,
        public partitaIva: string,
        public tipoEnte: TipoEnteGreg,
        public comune: ComuneGreg,
        public codiceIstat: string,
        public asl: AslGreg,
        public email: string,
        public telefono: string,
        public pec: string,
        public responsabileEnte: ResponsabileEnteGreg,
        public strutturaResidenziale: boolean,
        public centroDiurnoStruttSemires: boolean,
        public fnps: number,
        public vincoloFondo: number,
        public pippi: number,
        public rendicontazione: StatoRendicontazioneGreg
    ) {}
}

