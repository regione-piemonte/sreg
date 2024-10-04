/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class RicercaEntiGestori {
	
	private String statoEnte;
	
	private String denominazioneEnte;
	private String statoRendicontazione;
	private String tipoEnte;
	private String comune;
	private Integer annoEsercizio;
	private List<Integer> lista;
	private String codiceRegionale;

	public String getStatoRendicontazione() {
		return statoRendicontazione;
	}

	public void setStatoRendicontazione(String statoRendicontazione) {
		this.statoRendicontazione = statoRendicontazione;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(String tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	public String getStatoEnte() {
		return statoEnte;
	}

	public void setStatoEnte(String statoEnte) {
		this.statoEnte = statoEnte;
	}

	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	public List<Integer> getLista() {
		return lista;
	}

	public void setLista(List<Integer> lista) {
		this.lista = lista;
	}

	public String getCodiceRegionale() {
		return codiceRegionale;
	}

	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
	}
	
}
