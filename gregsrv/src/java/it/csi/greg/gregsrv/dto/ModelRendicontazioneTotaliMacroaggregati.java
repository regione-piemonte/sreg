/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneSpesaMissioneProgrammaMacro;

public class ModelRendicontazioneTotaliMacroaggregati {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private String denominazioneEnte;
	private Integer annoGestione;
	private List<ModelTotaleMacroaggregati> valoriMacroaggregati;
	
	public ModelRendicontazioneTotaliMacroaggregati () {}
	
//	public ModelRendicontazioneTotaliMacroaggregati (GregRRendicontazioneSpesaMissioneProgrammaMacro rendicontazione) {
//		this.idRendicontazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte();
//		this.idSchedaEnteGestore = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
//		this.denominazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getDenominazione();
//		this.annoGestione = rendicontazione.getGregTRendicontazioneEnte().getAnnoGestione();
//		this.valoriMacroaggregati = new ArrayList<ModelTotaleMacroaggregati>();
//	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public Integer getAnnoGestione() {
		return annoGestione;
	}

	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}

	public List<ModelTotaleMacroaggregati> getValoriMacroaggregati() {
		return valoriMacroaggregati;
	}

	public void setValoriMacroaggregati(List<ModelTotaleMacroaggregati> valoriMacroaggregati) {
		this.valoriMacroaggregati = valoriMacroaggregati;
	}

	
}