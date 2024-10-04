import { DettagliDisabilitaC } from "./DettagliDisabilitaC";

export class DisabilitaC {
    public idDisabilita: number;
    public codDisabilita: string;
    public descDisabilita: string;
    public listaDettagliDisabilita: Array<DettagliDisabilitaC>;
    public valore: string;

    public constructor() {}
}