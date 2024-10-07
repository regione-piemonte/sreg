/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CronologiaGreg } from "./CronologiaGreg";
import { ProfiloGreg } from "./ProfiloGreg";
import { ModelPrestazioniB1 } from "./VociModelloB1";

export class SalvaModelloB1 {
    public idEnte: number;
    public idRendicontazioneEnte: number;
    public dati: ModelPrestazioniB1[];
    public cronologia : CronologiaGreg;
	public profilo: ProfiloGreg;
    public constructor() {}
}
