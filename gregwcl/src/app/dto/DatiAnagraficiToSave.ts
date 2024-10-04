import { AnagraficaEnteGreg } from "./AnagraficaEnteGreg";
import { ComuneAssociatoGreg } from "./ComuneAssociatoGreg";
import { ListaGreg } from "./ListaGreg";
import { ProfiloGreg } from "./ProfiloGreg";

export class DatiAnagraficiToSave {
    datiEnte: AnagraficaEnteGreg;
    comuniAssociati: ComuneAssociatoGreg[];
    modificaEnte: boolean;
    modificaResp: boolean;
    modificaComune: boolean;
    profilo: ProfiloGreg;
    dataModifica: Date;
    dataApertura: Date;
    listaSelezionata: ListaGreg;
    sameData: boolean;
}
