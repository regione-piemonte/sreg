export class UserGreg {
    constructor(
        public idUser: number,
        public codiceFiscale: string,
        public nome: string,
        public cognome: string,
        public email: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ){}    
}