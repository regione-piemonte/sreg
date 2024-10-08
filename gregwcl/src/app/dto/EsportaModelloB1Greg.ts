/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelloB1ElencoLbl } from "./ModelloB1ElencoLbl";
import { ModelPrestazioniB1 } from "./VociModelloB1";


export class EsportaModelloB1Greg {
    public idEnte: number;
    public datiB1: ModelPrestazioniB1[];
    public labels: ModelloB1ElencoLbl;
	public denominazioneEnte : string;
    public constructor() {}
}