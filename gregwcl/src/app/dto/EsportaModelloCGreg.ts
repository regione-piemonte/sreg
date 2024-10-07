/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { PrestazioniC } from "./PrestazioniC";
import { RendicontazioneModC } from "./RendicontazioneModC";


export class EsportaModelloCGreg {
    public idEnte: number;
    public totaleUtenti: Array<string>;
    public totaleMinori: string;
    public totaleAdulti: string;
    public datiC: RendicontazioneModC;
    public prestazioniC: PrestazioniC[];
	public denominazioneEnte : string;
    public constructor() {}
}