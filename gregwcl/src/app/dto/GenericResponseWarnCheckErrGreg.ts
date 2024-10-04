export class GenericResponseWarnCheckErrGreg {
    constructor(
        public id : string,
        public descrizione: string,
        public warnings: string[],
        public errors: string[],
        public check: string[],
        public valorizzato: boolean
    ) {}
}