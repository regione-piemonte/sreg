export class PrestazioneGreg {
    constructor(
        public idPrestazione: number,
        public codicePrestazione: string,
        public desPrestazione: string,
        public prestFiglia: boolean,
        public prestazioniCollegate: PrestazioneGreg[],
    ){}
    
}
