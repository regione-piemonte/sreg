/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelDettaglioPrestRegionale {
	
	private String desTipologia;
	private String tipo;
	private Integer ordinamento;
	private String codPrestRegionale;
	private String desPrestRegionale;
	private List<ModelMacroaggregati> macroaggregati;
	private List<ModelPrestUtenza> targetUtenzaPrestReg1;
	private String notaPrestazione;
	private List<ModelPrest1Prest2> prest1Prest2;
	private ModelPrest1PrestMin prest1PrestMin;
	private List<ModelPrest1PrestCollegate> prestazioniCollegate;
	
	public ModelDettaglioPrestRegionale () {}

	public String getDesTipologia() {
		return desTipologia;
	}


	public void setDesTipologia(String desTipologia) {
		this.desTipologia = desTipologia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}


	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}


	public String getCodPrestRegionale() {
		return codPrestRegionale;
	}


	public void setCodPrestRegionale(String codPrestRegionale) {
		this.codPrestRegionale = codPrestRegionale;
	}


	public String getDesPrestRegionale() {
		return desPrestRegionale;
	}


	public void setDesPrestRegionale(String desPrestRegionale) {
		this.desPrestRegionale = desPrestRegionale;
	}


	public List<ModelMacroaggregati> getMacroaggregati() {
		return macroaggregati;
	}


	public void setMacroaggregati(List<ModelMacroaggregati> macroaggregati) {
		this.macroaggregati = macroaggregati;
	}


	public List<ModelPrestUtenza> getTargetUtenzaPrestReg1() {
		return targetUtenzaPrestReg1;
	}


	public void setTargetUtenzaPrestReg1(List<ModelPrestUtenza> targetUtenzaPrestReg1) {
		this.targetUtenzaPrestReg1 = targetUtenzaPrestReg1;
	}

	public String getNotaPrestazione() {
		return notaPrestazione;
	}

	public void setNotaPrestazione(String notaPrestazione) {
		this.notaPrestazione = notaPrestazione;
	}

	public List<ModelPrest1Prest2> getPrest1Prest2() {
		return prest1Prest2;
	}

	public void setPrest1Prest2(List<ModelPrest1Prest2> prest1Prest2) {
		this.prest1Prest2 = prest1Prest2;
	}

	public ModelPrest1PrestMin getPrest1PrestMin() {
		return prest1PrestMin;
	}

	public void setPrest1PrestMin(ModelPrest1PrestMin prest1PrestMin) {
		this.prest1PrestMin = prest1PrestMin;
	}

	public List<ModelPrest1PrestCollegate> getPrestazioniCollegate() {
		return prestazioniCollegate;
	}

	public void setPrestazioniCollegate(List<ModelPrest1PrestCollegate> prestazioniCollegate) {
		this.prestazioniCollegate = prestazioniCollegate;
	}
	
}