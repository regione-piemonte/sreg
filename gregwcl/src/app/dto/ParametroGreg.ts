export class ParametroGreg {
    constructor(
        public idParametro : number,
        public codParametro: string,
        public desParametro: string,
        public informativa: string,
        public valnum: number,
        public valInt: number,
        public valtext: string,
        public valBool: Boolean,
        public valDataDa: Date,
        public valDataA: Date,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}