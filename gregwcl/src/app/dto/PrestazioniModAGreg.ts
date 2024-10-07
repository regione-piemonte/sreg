/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { PrestazioneUtenzaModAGreg } from "./PrestazioneUtenzaModAGreg";
import { TargetUtenzaGreg } from "./TargetUtenzaGreg";

export class PrestazioniModAGreg {
    prestazioniRS: PrestazioneUtenzaModAGreg[];
    subtotaliRS: TargetUtenzaGreg[];
	prestazioniCD: PrestazioneUtenzaModAGreg[];
	subtotaliCD: TargetUtenzaGreg[];
    totaliSRCD: TargetUtenzaGreg[];
}