import { CampiMacroaggregatiRend } from "./CampiMacroaggregatiRend";

export class MacroaggregatiRend {
    public codMissione: string;
    public descrizioneMissione: string;
    public descrizioneProgramma: string;
    public campi: Array<CampiMacroaggregatiRend>;
    public idSpesaMissione: number;
    public ordinamento: number;

    public constructor() {}
}