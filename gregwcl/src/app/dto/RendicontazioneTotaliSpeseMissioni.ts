/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { TotaleSpese } from "./TotaleSpese";

export class RendicontazioneTotaliSpeseMissioni {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: string;
    public valoriSpese: TotaleSpese[];

    public constructor() {}
}