/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProfiloGreg } from "./ProfiloGreg";
import { RendicontazioneEnteGreg } from "./RendicontazioneEnteGreg";
import { StatoEnte } from "./StatoEnte";
import { StatoModelloEnte } from "./StatoModelloEnte";
import { TipoEnteGreg } from "./TipoEnteGreg";

export class RicercaGregOutput {
    checked: boolean;
    notaEnte: string;
    notaInterna: string; 
    profilo: ProfiloGreg;
    constructor(
        public idSchedaEnteGestore: number,
        public tipoEnte: TipoEnteGreg,
        public codiceRegionale: string,
        public denominazione: string,
        public comuneSedeLegale: string,
        public statoEnte: StatoEnte,
        public rendicontazioni: RendicontazioneEnteGreg[],
        public modelli: StatoModelloEnte,
        public statoFnpsAttuale: string,
        public statoFnpsPrecedente: string,
        public idRendicontazionePassata: number,
        public statoModelstatoAlZero: string
    ){}
}