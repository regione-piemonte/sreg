
import { MatTableDataSource } from "@angular/material";
import { ListaAzione } from "./ListaAzione";
import { ListaEnte } from "./ListaEnte";


export class ListaLista {
    public idLista:number;
    public codLista: string;
    public descLista: string;
    public infoLista: string;
    public TipoLista: string;
    public enti: ListaEnte[];
    public entiMancate: ListaEnte[];
    public ente: ListaEnte;
    public dataEnti: MatTableDataSource<ListaEnte>;
    public lista: boolean;
    public entiDaCopiare: ListaEnte[];
    
    public constructor() {}
}