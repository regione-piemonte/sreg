import { StatoRendicontazioneGreg } from "./StatoRendicontazioneGreg";

export class CronologiaGreg {
    public idCronologia: number;
    public notaEnte: string;
    public notaInterna: string;
    public utente: string;
    public modello: string;
    public dataOra: Date;
    public utenteOperazione: string;
    public dataCreazione: Date;
    public dataModifica: Date;
    public dataCancellazione: Date;
    public statoRendicontazione: StatoRendicontazioneGreg
    constructor(){}
    
}