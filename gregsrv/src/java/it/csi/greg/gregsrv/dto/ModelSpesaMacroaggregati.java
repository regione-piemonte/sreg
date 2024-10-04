/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregRSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;

public class ModelSpesaMacroaggregati {
	private ModelMacroaggregati macroaggregato;
	private ModelSpesaMissione spesaMissione;
	
	public ModelSpesaMacroaggregati () {}
	
	public ModelSpesaMacroaggregati (GregRSpesaMissioneProgrammaMacro spesaMacro, GregTMacroaggregatiBilancio macroaggregato) {
		this.macroaggregato=new ModelMacroaggregati(macroaggregato);
		this.spesaMissione=new ModelSpesaMissione(spesaMacro.getGregDSpesaMissioneProgramma());
	}

	public ModelMacroaggregati getMacroaggregato() {
		return macroaggregato;
	}

	public void setMacroaggregato(ModelMacroaggregati macroaggregato) {
		this.macroaggregato = macroaggregato;
	}

	public ModelSpesaMissione getSpesaMissione() {
		return spesaMissione;
	}

	public void setSpesaMissione(ModelSpesaMissione spesaMissione) {
		this.spesaMissione = spesaMissione;
	}

	
}
