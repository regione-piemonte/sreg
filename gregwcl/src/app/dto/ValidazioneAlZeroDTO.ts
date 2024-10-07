/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CronologiaGreg } from "./CronologiaGreg";
import { ProfiloGreg } from "./ProfiloGreg";

export class ValidazioneAlZero {
    public idRendicontazioneEnte: number;
	public toggle: boolean;
	public cronologia: CronologiaGreg;
	public profilo: ProfiloGreg;
	public notaEnte: string;
	public notaInterna: string;
	public modello: string;
    public constructor() {}
}