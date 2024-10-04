/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class SaveMotivazioneCheck {
	private Integer idRendicontazione;
	private String codModello;
	private String nota;
	private ModelProfilo profilo;
	private String modello;
	
	public Integer getIdRendicontazione() {
		return idRendicontazione;
	}
	public void setIdRendicontazione(Integer idRendicontazione) {
		this.idRendicontazione = idRendicontazione;
	}
	public String getCodModello() {
		return codModello;
	}
	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public ModelProfilo getProfilo() {
		return profilo;
	}
	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}
	public String getModello() {
		return modello;
	}
	public void setModello(String modello) {
		this.modello = modello;
	}
	
	
	
}
