/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CampiVociRendModD } from "./CampiVociRendModD";

export class VociRendModD {
    public descrizioneVoce: string;
    public operatore: string;
    public campi: Array<CampiVociRendModD>;
    public idVoce: number;
    public ordinamento: number;
	public codVoce: string;
	
    public constructor() {}
}