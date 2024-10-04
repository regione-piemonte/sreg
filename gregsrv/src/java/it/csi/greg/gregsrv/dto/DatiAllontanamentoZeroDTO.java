/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class DatiAllontanamentoZeroDTO {
	
	private String giustificativo;
	private List<PrestazioniAllontanamentoZeroDTO> lista;
	private ModelFileAllegatoToUpload fileAlZero;
	private ModelAllegatiAssociati fileAlZerodb;
	private ModelProfilo profilo;
	private String notaEnte;
	private String notaInterna;
	private boolean ConfermaResponsabile;
	
	public String getGiustificativo() {
		return giustificativo;
	}
	public void setGiustificativo(String giustificativo) {
		this.giustificativo = giustificativo;
	}
	public List<PrestazioniAllontanamentoZeroDTO> getLista() {
		return lista;
	}
	public void setLista(List<PrestazioniAllontanamentoZeroDTO> lista) {
		this.lista = lista;
	}
	public ModelFileAllegatoToUpload getFileAlZero() {
		return fileAlZero;
	}
	public void setFileAlZero(ModelFileAllegatoToUpload fileAlZero) {
		this.fileAlZero = fileAlZero;
	}
	public ModelAllegatiAssociati getFileAlZerodb() {
		return fileAlZerodb;
	}
	public void setFileAlZerodb(ModelAllegatiAssociati fileAlZerodb) {
		this.fileAlZerodb = fileAlZerodb;
	}
	public ModelProfilo getProfilo() {
		return profilo;
	}
	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}
	public String getNotaEnte() {
		return notaEnte;
	}
	public void setNotaEnte(String notaEnte) {
		this.notaEnte = notaEnte;
	}
	public String getNotaInterna() {
		return notaInterna;
	}
	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}
	public boolean isConfermaResponsabile() {
		return ConfermaResponsabile;
	}
	public void setConfermaResponsabile(boolean confermaResponsabile) {
		ConfermaResponsabile = confermaResponsabile;
	}
	
	
}
