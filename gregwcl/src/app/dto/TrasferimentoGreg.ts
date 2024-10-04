import { CausaleGreg } from "./CausaleGreg";
import { ComuneAssociatoGreg } from "./ComuneAssociatoGreg";
import { ComuneGreg } from "./ComuneGreg";

export class TrasferimentoGreg {
    public id: number;
    public comune: ComuneAssociatoGreg;
    public causale: CausaleGreg;
    public importo: number;
    
    constructor(){}
}