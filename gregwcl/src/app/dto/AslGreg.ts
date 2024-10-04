export class AslGreg {
    constructor(
        public idAsl : number,
        public codAsl: string,
        public desAsl: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}