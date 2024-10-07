/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CausaleGreg } from "./CausaleGreg";
import { CronologiaGreg } from "./CronologiaGreg";
import { TrasferimentoGreg } from "./TrasferimentoGreg";

export class EsportaModelloA2Greg {
    public idEnte: number;
    public causali: CausaleGreg[];
    public trasferimentiEnteComune: TrasferimentoGreg[];
    public trasferimentiComuneEnte: TrasferimentoGreg[];
    public trasferimentiEnteComuneFiltered: TrasferimentoGreg[];
    public trasferimentiComuneEnteFiltered: TrasferimentoGreg[];
	public denominazioneEnte : string;
    public constructor() {}
}