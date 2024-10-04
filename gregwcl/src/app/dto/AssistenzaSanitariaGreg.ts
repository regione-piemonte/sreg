export class AssistenzaSanitariaGreg {
    constructor(
        public idAssistenzaSanitaria : number,
        public codAssistenzaSanitaria: number,
        public descAssistenzaSanitaria: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}