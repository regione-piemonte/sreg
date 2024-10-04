
import { MatTableDataSource } from "@angular/material";
import { Abilitazioni } from "./Abilitazioni";
import { ListaAzione } from "./ListaAzione";


export class ListaProfilo {
    public idProfilo:number;
    public codProfilo: string;
    public descProfilo: string;
    public infoProfilo: string;
    public TipoProfilo: string;
    public azioni: ListaAzione[];
    public azioniMancate: ListaAzione[];
    public azione: ListaAzione;
    public dataAzioni: MatTableDataSource<ListaAzione>;
    public profilo: boolean;
    public azioniDaCopiare: ListaAzione[];
    
    public constructor() {}
}