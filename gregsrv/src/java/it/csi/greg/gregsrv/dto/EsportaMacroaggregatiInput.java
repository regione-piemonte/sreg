/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaMacroaggregatiInput {

	private ModelRendicontazioneMacroaggregati datiRendicontazione;
	private List<String> totaliR;
	private List<String> totaliC;
	private List<String> totaliCS;
	
	public EsportaMacroaggregatiInput() { }

	public ModelRendicontazioneMacroaggregati getDatiRendicontazione() {
		return datiRendicontazione;
	}

	public void setDatiRendicontazione(ModelRendicontazioneMacroaggregati datiRendicontazione) {
		this.datiRendicontazione = datiRendicontazione;
	}

	public List<String> getTotaliR() {
		return totaliR;
	}

	public void setTotaliR(List<String> totaliR) {
		this.totaliR = totaliR;
	}

	public List<String> getTotaliC() {
		return totaliC;
	}

	public void setTotaliC(List<String> totaliC) {
		this.totaliC = totaliC;
	}

	public List<String> getTotaliCS() {
		return totaliCS;
	}

	public void setTotaliCS(List<String> totaliCS) {
		this.totaliCS = totaliCS;
	}
	
}

