/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


public class SaveModelloBInput {

	private Integer idEnte;
	private ModelCronologiaEnte cronologia;
	
	public SaveModelloBInput() { }
	

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public ModelCronologiaEnte getCronologia() {
		return cronologia;
	}

	public void setCronologia(ModelCronologiaEnte cronologia) {
		this.cronologia = cronologia;
	}

	
}

