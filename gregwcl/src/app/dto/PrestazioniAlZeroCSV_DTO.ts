import { AllegatoGreg } from "./AllegatoGreg";
import { FileAllegatoToUploadGreg } from "./FileAllegatoToUploadGreg";
import { ProfiloGreg } from "./ProfiloGreg";

export interface PrestazioniAlZeroCSV_DTO {
    giustificativo: string | null,
    lista: PrestazioniAllontanamentoZeroCSV_DTO[];
    fileAlZero: FileAllegatoToUploadGreg;
    fileAlZerodb: AllegatoGreg;
    fondoRegionale: string | null;
    quotaAlZero: string | null;
    residuo: string | null;
    totaleValoriAlZero: string | null;
    totaleValoriB1: string | null;
    annoEsercizio: number | null;
    denominazione: string | null;
}

export class PrestazioniAllontanamentoZeroCSV_DTO {
    public prestazioneId: number;
    public prestazioneCod: string;
    public prestazioneDesc: string;
    public valorePerFamiglieMinoriB1: string | null;
    public valorePerPrestazioneAlZero?: string | null;
    public tooltipDesc: string | null;
    public constructor() {}
}