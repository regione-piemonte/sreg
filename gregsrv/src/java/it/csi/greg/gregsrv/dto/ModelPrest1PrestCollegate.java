/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;

public class ModelPrest1PrestCollegate {

	private String desTipologia;
	private String tipo;
	private Integer idPrestRegionale;
	private String codPrestRegionale;
	private String desPrestRegionale;
	private Timestamp al;
	
	public ModelPrest1PrestCollegate () {}

	public Integer getIdPrestRegionale() {
		return idPrestRegionale;
	}

	public void setIdPrestRegionale(Integer idPrestRegionale) {
		this.idPrestRegionale = idPrestRegionale;
	}

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

	public Timestamp getAl() {
		return al;
	}

	public void setAl(Timestamp al) {
		this.al = al;
	}

}
