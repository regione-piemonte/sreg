/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CronologiaGreg } from "./CronologiaGreg";
import { DatiModelloA1Greg } from "./DatiModelloA1Greg";
import { ProfiloGreg } from "./ProfiloGreg";


export class SalvaModelloA1Greg {
    public idEnte: number;
    public idRendicontazioneEnte: number;
    public datiA1: DatiModelloA1Greg[];
    public cronologia : CronologiaGreg;
    public profilo: ProfiloGreg;
    public constructor() {}
}