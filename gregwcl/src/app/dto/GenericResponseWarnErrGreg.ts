export class GenericResponseWarnErrGreg {
    constructor(
        public id : string,
        public descrizione: string,
        public ok: string[],
        public warnings: string[],
        public errors: string[],
        public obblMotivazione: boolean,
        public idEnte: number,
        public idPrestazione: number,
        public warningCheck: string,
        public dataCreazione: Date
    ) {}
}