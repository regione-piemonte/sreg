export class RicercaGregCruscotto {
    constructor(
        public codiceRegionale: string,
        public denominazioneEnte: string,
        public statoRendicontazione: number,
        public tipoEnte: number,
        public comune: number, 
        public annoEsercizio: number,
        public lista: number[]
    ){}
}