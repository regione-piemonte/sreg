export class PrestazioneAssociataGreg {

     public annoFineValidita: number;
     public dataFineValidita: Date;
    constructor(
        public pkIdPrestazioneAssociata: number,
        public codicePrestazione: string,
        public desPrestazione: string,
        public idPrestazione: number,
        public prestFiglia: boolean
    ){}
    
}