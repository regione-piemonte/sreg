export class ResponseSalvaModelloGreg {
    public id : string;
    public esito: string;
	public messaggio:string;
    public descrizione: string;
    public warnings: string[];
    public errors: string[];
    public idEnte : number;
    public idPrestazione : number;

    public constructor() {}
}