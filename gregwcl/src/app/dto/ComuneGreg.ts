import { ProvinciaGreg } from "./ProvinciaGreg";

export class ComuneGreg {
    constructor(
        public idComune: number,
        public codIstatComune: string,
        public desComune: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
        public provincia: ProvinciaGreg
    ){}
}