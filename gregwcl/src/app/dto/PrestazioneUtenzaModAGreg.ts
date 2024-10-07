/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { TargetUtenzaGreg } from "./TargetUtenzaGreg";

export class PrestazioneUtenzaModAGreg {
    idPrestazioneUtenza: number;
	idPrestazione: number;
	codPrestazione: string;
	descPrestazione: string;
	listaTargetUtenza: TargetUtenzaGreg[];
}