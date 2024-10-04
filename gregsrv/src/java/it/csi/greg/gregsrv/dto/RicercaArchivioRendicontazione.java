/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class RicercaArchivioRendicontazione {
	
	private Integer statoRendicontazione;

	private String denominazioneEnte;

	private Integer comune;

	private Integer tipoEnte;

	private String partitaIva;
	
	private Integer anno;

	public Integer getStatoRendicontazione() {
		return statoRendicontazione;
	}

	public void setStatoRendicontazione(Integer statoRendicontazione) {
		this.statoRendicontazione = statoRendicontazione;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public Integer getComune() {
		return comune;
	}

	public void setComune(Integer comune) {
		this.comune = comune;
	}

	public Integer getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(Integer tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	
	

}
