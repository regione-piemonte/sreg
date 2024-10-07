/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AllegatoGreg } from "./AllegatoGreg";
import { FileAllegatoToUploadGreg } from "./FileAllegatoToUploadGreg";
import { ProfiloGreg } from "./ProfiloGreg";

export interface DatiAllontanamentoZero {
    giustificativo: string | null,
    profilo: ProfiloGreg;
    lista: PrestazioniAllontanamentoZeroDTO[];
    fileAlZero: FileAllegatoToUploadGreg;
    fileAlZerodb: AllegatoGreg;
    notaEnte: string;
    notaInterna: string;
    confermaResponsabile: boolean | false;
}

export class PrestazioniAllontanamentoZeroDTO {
    public prestazioneId: number;
    public prestazioneCod: string;
    public prestazioneDesc: string;
    public valorePerFamiglieMinoriB1: number | null;
    public valorePerPrestazioneAlZero?: number | null;
    public valorePerPrestazioneAlZeroString?: string | null = null;
    public tooltipDesc: string | null;
    public constructor() {}
}