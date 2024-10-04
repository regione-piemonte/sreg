import { TargetUtenzeC } from "./TargetUtenzeC";

export class PrestazioniC {
    public idPrestazione: number;
    public codPrestazione: string;
    public descPrestazione: string;
    public informativa: string;
    public flag: boolean;
    public listaTargetUtenze: Array<TargetUtenzeC>;

    public constructor() {}
}