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

public class ModelRicercaEntiGestoriAttivi {
	
	private Integer idSchedaEnteGestore;
	private ModelTipoEnte tipoEnte;
	private String codiceRegionale;
//	private String codiceFiscale;
	private String denominazione;
//	private String partitaIva;
	private String comuneSedeLegale;
//	private String codiceIstatEnte;
//	private Integer provinciaSedeLegale;
//	private Integer idAsl;
//	private String eMail;
//	private String telefono;
//	private String pec;
//	private ModelResponsabileEnte responsabileEnte;
//	private BigDecimal fnps;
//	private BigDecimal vincoloFondo;
//	private Boolean strutturaResidenziale;
//	private Boolean strutturaSemiresidenziale;
	private ModelStatiEnte statoEnte;
	private List<ModelRendicontazioneEnte> rendicontazioni;
	private String notaEnte;
	private String notaInterna;
	private ModelProfilo profilo;
	
	public ModelRicercaEntiGestoriAttivi() { }
	
	public ModelRicercaEntiGestoriAttivi(Object[] obj) { 
		this.idSchedaEnteGestore = (Integer) obj[0];
		this.codiceRegionale = String.valueOf(obj[1]);
		this.denominazione = String.valueOf(obj[2]);
		this.comuneSedeLegale = String.valueOf(obj[3]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[3]);
		this.tipoEnte = new ModelTipoEnte(String.valueOf(obj[4]), String.valueOf(obj[5]));	
		this.statoEnte = new ModelStatiEnte(String.valueOf(obj[6]), String.valueOf(obj[7]));
	}
//	
//	public ModelRicercaEntiGestoriAttivi(Integer idScheda, String codiceRegionale, String denominazione, String comuneSedeLegale) { 
//		this.idSchedaEnteGestore = idScheda;
//		this.tipoEnte = null;
//		this.codiceRegionale = codiceRegionale;
//		this.denominazione = denominazione;
//		this.comuneSedeLegale = comuneSedeLegale;
//		this.statoRendicontazione = null;
//	}
	

//	public ModelRicercaEntiGestoriAttivi(GregTSchedeEntiGestori scheda) {
//		
//		List<GregTRendicontazioneEnte> listaRendicontazione = new ArrayList<GregTRendicontazioneEnte>();
//		listaRendicontazione = getListStatoRendicontazione(scheda);
//		
//		if (!listaRendicontazione.isEmpty()) {
//			for (GregTRendicontazioneEnte rendicontazione : listaRendicontazione) {
//				this.idSchedaEnteGestore = scheda.getIdSchedaEnteGestore();
//				this.tipoEnte = new ModelTipoEnte(scheda.getGregDTipoEnte());
//				this.codiceRegionale = scheda.getCodiceRegionale();
////				this.codiceFiscale = scheda.getCodiceFiscale();
//				this.denominazione = scheda.getDenominazione();
////				this.partitaIva = scheda.getPartitaIva();
//				this.comuneSedeLegale = scheda.getGregDComuni().getDesComune();
////				this.codiceIstatEnte = scheda.getCodIstatEnte();
////				this.provinciaSedeLegale = scheda.getGregDComuni().getGregDProvince().getIdProvincia();
////				this.idAsl = scheda.getGregDAsl().getIdAsl();
////				this.eMail = scheda.getEmail();
////				this.telefono = scheda.getTelefono();
////				this.pec = scheda.getPec();
////				this.responsabileEnte = new ModelResponsabileEnte(scheda.getGregTResponsabileEnteGestore());
////				this.fnps = scheda.getFnps();
////				this.vincoloFondo = scheda.getVincoloFondo();
////				this.strutturaResidenziale = scheda.getStrutturaResidenziale();
////				this.strutturaSemiresidenziale = scheda.getCentroDiurnoStruttSemires();				
//				this.statoRendicontazione = new ModelStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
////				this.statoRendicontazione = new ModelStatoRendicontazione();
//			}
//		}
//		else {
//			this.idSchedaEnteGestore = scheda.getIdSchedaEnteGestore();
//			this.tipoEnte = new ModelTipoEnte(scheda.getGregDTipoEnte());
//			this.codiceRegionale = scheda.getCodiceRegionale();
////			this.codiceFiscale = scheda.getCodiceFiscale();
//			this.denominazione = scheda.getDenominazione();
////			this.partitaIva = scheda.getPartitaIva();
//			this.comuneSedeLegale = scheda.getGregDComuni().getDesComune();
////			this.codiceIstatEnte = scheda.getCodIstatEnte();
////			this.provinciaSedeLegale = scheda.getGregDComuni().getGregDProvince().getIdProvincia();
////			this.idAsl = scheda.getGregDAsl().getIdAsl();
////			this.eMail = scheda.getEmail();
////			this.telefono = scheda.getTelefono();
////			this.pec = scheda.getPec();
////			this.responsabileEnte = new ModelResponsabileEnte(scheda.getGregTResponsabileEnteGestore());
////			this.fnps = scheda.getFnps();
////			this.vincoloFondo = scheda.getVincoloFondo();
////			this.strutturaResidenziale = scheda.getStrutturaResidenziale();
////			this.strutturaSemiresidenziale = scheda.getCentroDiurnoStruttSemires();
//			this.statoRendicontazione = new ModelStatoRendicontazione();
//		}
//	}

	
	private List<GregTRendicontazioneEnte> getListStatoRendicontazione(GregTSchedeEntiGestori scheda) {
		
		List<GregTRendicontazioneEnte> listaRendicontazione = new ArrayList<GregTRendicontazioneEnte>();
		for (GregTRendicontazioneEnte rendicontazione : scheda.getGregTRendicontazioneEntes()) {
			if (scheda.getIdSchedaEnteGestore() == rendicontazione.getGregTSchedeEntiGestori().getIdSchedaEnteGestore()) {
				listaRendicontazione.add(rendicontazione);
			}			
		}
		return listaRendicontazione;
	}
	
	@SuppressWarnings("unused")
	private GregTRendicontazioneEnte getStatoRendicontazione(GregTSchedeEntiGestori scheda) {
		Iterator<GregTRendicontazioneEnte> iterator = scheda.getGregTRendicontazioneEntes().iterator();
		GregTRendicontazioneEnte rendicontazioneEnte = iterator.next();
		while(iterator.hasNext()) {
			GregTRendicontazioneEnte temp = iterator.next();
			if(temp.getGregTSchedeEntiGestori().getIdSchedaEnteGestore() == scheda.getIdSchedaEnteGestore())
				rendicontazioneEnte = temp;
		}
		return rendicontazioneEnte;
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

//	public String getCodiceFiscale() {
//		return codiceFiscale;
//	}
//
//	public void setCodiceFiscale(String codiceFiscale) {
//		this.codiceFiscale = codiceFiscale;
//	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

//	public String getPartitaIva() {
//		return partitaIva;
//	}
//
//	public void setPartitaIva(String partitaIva) {
//		this.partitaIva = partitaIva;
//	}

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

//	public String getCodiceIstatEnte() {
//		return codiceIstatEnte;
//	}
//
//	public void setCodiceIstatEnte(String codiceIstatEnte) {
//		this.codiceIstatEnte = codiceIstatEnte;
//	}
//
//	public Integer getProvinciaSedeLegale() {
//		return provinciaSedeLegale;
//	}
//
//	public void setProvinciaSedeLegale(Integer provinciaSedeLegale) {
//		this.provinciaSedeLegale = provinciaSedeLegale;
//	}
//
//	public Integer getIdAsl() {
//		return idAsl;
//	}
//
//	public void setIdAsl(Integer idAsl) {
//		this.idAsl = idAsl;
//	}
//
//	public String geteMail() {
//		return eMail;
//	}
//
//	public void seteMail(String eMail) {
//		this.eMail = eMail;
//	}
//
//	public String getTelefono() {
//		return telefono;
//	}
//
//	public void setTelefono(String telefono) {
//		this.telefono = telefono;
//	}
//
//	public String getPec() {
//		return pec;
//	}
//
//	public void setPec(String pec) {
//		this.pec = pec;
//	}
//
//	public ModelResponsabileEnte getResponsabileEnte() {
//		return responsabileEnte;
//	}
//
//	public void setResponsabileEnte(ModelResponsabileEnte responsabileEnte) {
//		this.responsabileEnte = responsabileEnte;
//	}
//
//	public BigDecimal getFnps() {
//		return fnps;
//	}
//
//	public void setFnps(BigDecimal fnps) {
//		this.fnps = fnps;
//	}
//
//	public BigDecimal getVincoloFondo() {
//		return vincoloFondo;
//	}
//
//	public void setVincoloFondo(BigDecimal vincoloFondo) {
//		this.vincoloFondo = vincoloFondo;
//	}
//
//	public Boolean getStrutturaResidenziale() {
//		return strutturaResidenziale;
//	}
//
//	public void setStrutturaResidenziale(Boolean strutturaResidenziale) {
//		this.strutturaResidenziale = strutturaResidenziale;
//	}
//
//	public Boolean getStrutturaSemiresidenziale() {
//		return strutturaSemiresidenziale;
//	}
//
//	public void setStrutturaSemiresidenziale(Boolean strutturaSemiresidenziale) {
//		this.strutturaSemiresidenziale = strutturaSemiresidenziale;
//	}

	public List<ModelRendicontazioneEnte> getRendicontazioni() {
		return rendicontazioni;
	}


	public void setRendicontazioni(List<ModelRendicontazioneEnte> rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}


	public ModelStatiEnte getStatoEnte() {
		return statoEnte;
	}

	public void setStatoEnte(ModelStatiEnte statoEnte) {
		this.statoEnte = statoEnte;

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

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}

	
	
}