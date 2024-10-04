import { DettagliUtenzeC } from "./DettagliUtenzeC";
import { DisabilitaC } from "./DisabilitaC";

export class TargetUtenzeC {
    public idTargetUtenze: number;
    public codTargetUtenze: string;
    public descTargetUtenze: string;
    public listaDettagliUtenze: Array<DettagliUtenzeC>;
    public listaDisabilita: Array<DisabilitaC>;
    public rotate?: string = 'default';
    public showDisabilita?: boolean = false;
    public valore: string;
    

    public constructor() {}
}