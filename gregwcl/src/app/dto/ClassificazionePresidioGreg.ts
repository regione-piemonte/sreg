export class ClassificazionePresidioGreg {
    constructor(
        public idClassificazionePresidio : number,
        public codClassificazionePresidio: number,
        public descClassificazionePresidio: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}