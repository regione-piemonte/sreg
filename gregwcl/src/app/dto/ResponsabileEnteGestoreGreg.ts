export class ResponsabileEnteGestoreGreg {
    constructor(
        public idResponsabileEnteGestore: number,
        public codResponsabileEnteGestore: string,
        public cognome: string,
        public nome: string,
        public codFiscale: string,
        public cellulare: string,
        public telefono: string,
        public email: string,
        public dataInizioValidita: Date,
        public dataFineValidita: Date,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}