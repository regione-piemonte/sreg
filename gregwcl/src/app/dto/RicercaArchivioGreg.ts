import { renameKeys } from "@greg-app/shared/util";

export class RicercaArchivioGreg {
    constructor(
        public statoRendicontazione: number,
        public denominazioneEnte: string,
        public comune: number,
        public tipoEnte: number,
        public partitaIva: string,
        public anno: number
    ){}
}