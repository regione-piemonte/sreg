/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class DatiAllontanamentoZeroCSV_DTO {
	private String giustificativo;
	private List<PrestazioniAllontanamentoZeroCSV_DTO> lista;
	private ModelFileAllegatoToUpload fileAlZero;
	private ModelAllegatiAssociati fileAlZerodb;
	private String quotaAlZero;
	private String fondoRegionale;
	private String totaleValoriB1;
	private String totaleValoriAlZero;
	private String residuo;
	private Integer annoEsercizio;
	private String denominazione;
	
	public String getGiustificativo() {
		return giustificativo;
	}
	public void setGiustificativo(String giustificativo) {
		this.giustificativo = giustificativo;
	}
	
	public List<PrestazioniAllontanamentoZeroCSV_DTO> getLista() {
		return lista;
	}
	public void setLista(List<PrestazioniAllontanamentoZeroCSV_DTO> lista) {
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
	public String getQuotaAlZero() {
		return quotaAlZero;
	}
	public void setQuotaAlZero(String quotaAlZero) {
		this.quotaAlZero = quotaAlZero;
	}
	public String getFondoRegionale() {
		return fondoRegionale;
	}
	public void setFondoRegionale(String fondoRegionale) {
		this.fondoRegionale = fondoRegionale;
	}
	public String getTotaleValoriB1() {
		return totaleValoriB1;
	}
	public void setTotaleValoriB1(String totaleValoriB1) {
		this.totaleValoriB1 = totaleValoriB1;
	}
	public String getTotaleValoriAlZero() {
		return totaleValoriAlZero;
	}
	public void setTotaleValoriAlZero(String totaleValoriAlZero) {
		this.totaleValoriAlZero = totaleValoriAlZero;
	}
	public String getResiduo() {
		return residuo;
	}
	public void setResiduo(String residuo) {
		this.residuo = residuo;
	}
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
}

