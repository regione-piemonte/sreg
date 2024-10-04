
import { ModelUtenzaAllD } from "./ModelUtenzaAllD";
import { TargetUtenzeC } from "./TargetUtenzeC";

export class ModelPrestazioneAllD {
    public idPrestazione: number;
    public codPrestazione: string;
    public descPrestazione: string;
    public macroAttivita: string;
    public utenze: Array<ModelUtenzaAllD>
    

    public constructor() {}
}