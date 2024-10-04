/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;

public class ModelRicercaCruscotto {
	
	private Integer idSchedaEnteGestore;
	private ModelTipoEnte tipoEnte;
	private String codiceRegionale;
	private String denominazione;
	private String comuneSedeLegale;
	private ModelStatiEnte statoEnte;
	private ModelRendicontazioneEnte rendicontazioni;
	private List<ModelStatoModelli> modelli;
	private String statoFnpsAttuale;
	private String statoFnpsPrecedente;
	private Integer idRendicontazionePassata;
	private String statoAlZero;
		
	public ModelRicercaCruscotto() { }

	public ModelRicercaCruscotto(Object[] obj) { 
		this.idSchedaEnteGestore = (Integer) obj[0];
		this.codiceRegionale = String.valueOf(obj[1]);
		this.denominazione = String.valueOf(obj[2]);
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public String getCodiceRegionale() {
		return codiceRegionale;
	}

	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public ModelTipoEnte getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(ModelTipoEnte tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	public String getComuneSedeLegale() {
		return comuneSedeLegale;
	}

	public void setComuneSedeLegale(String comuneSedeLegale) {
		this.comuneSedeLegale = comuneSedeLegale;
	}

	public ModelRendicontazioneEnte getRendicontazioni() {
		return rendicontazioni;
	}

	public void setRendicontazioni(ModelRendicontazioneEnte rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}

	public ModelStatiEnte getStatoEnte() {
		return statoEnte;
	}

	public void setStatoEnte(ModelStatiEnte statoEnte) {
		this.statoEnte = statoEnte;

	}

	public List<ModelStatoModelli> getModelli() {
		return modelli;
	}

	public void setModelli(List<ModelStatoModelli> modelli) {
		this.modelli = modelli;
	}

	public String getStatoFnpsAttuale() {
		return statoFnpsAttuale;
	}

	public void setStatoFnpsAttuale(String statoFnpsAttuale) {
		this.statoFnpsAttuale = statoFnpsAttuale;
	}

	public String getStatoFnpsPrecedente() {
		return statoFnpsPrecedente;
	}

	public void setStatoFnpsPrecedente(String statoFnpsPrecedente) {
		this.statoFnpsPrecedente = statoFnpsPrecedente;
	}

	public Integer getIdRendicontazionePassata() {
		return idRendicontazionePassata;
	}

	public void setIdRendicontazionePassata(Integer idRendicontazionePassata) {
		this.idRendicontazionePassata = idRendicontazionePassata;
	}

	public String getStatoAlZero() {
		return statoAlZero;
	}

	public void setStatoAlZero(String statoAlZero) {
		this.statoAlZero = statoAlZero;
	}
	
	
}