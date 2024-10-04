export class DistrettoGreg {
    constructor(
        public idDistretto: number,
        public distretto: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ){}
}