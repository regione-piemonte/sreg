
import { Abilitazioni } from "./Abilitazioni";


export class Utenti {
    public idUtente:number;
    public nome: string;
    public cognome: string;
    public codiceFiscale: string;
    public email: string;
    public abilitazioni: Abilitazioni[];
    public utente: boolean;
    public maxData: Date;
    public dataFineValidita: Date;
    
    public constructor() {}
}