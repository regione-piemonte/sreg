/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import it.csi.greg.gregsrv.business.entity.GregDAsl;
import it.csi.greg.gregsrv.business.entity.GregDProvince;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.util.Checker;

public class ModelDatiEnte {
	
	private Integer idSchedaEntegestore;
	private String codiceRegionale;
	private String codiceFiscale;
	private String denominazione;
	private String partitaIva;
	private ModelTipoEnte tipoEnte;
	private ModelComune comune;
	private String codiceIstat;
	private ModelDatiAsl asl;
	private String email;
	private String telefono;
	private String pec;
	private ModelResponsabileEnte responsabileEnte;
	private Boolean strutturaResidenziale;
	private Boolean centroDiurnoStruttSemires;
	private BigDecimal fnps;
	private BigDecimal vincoloFondo;
	private BigDecimal pippi;
	private ModelStatoRendicontazione rendicontazione;

	
	public ModelDatiEnte() { }
	
	public ModelDatiEnte(Object[] obj) {
		this.idSchedaEntegestore = (Integer) obj[0];
		this.codiceRegionale = String.valueOf(obj[1]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[1]);
		this.codiceFiscale = String.valueOf(obj[2]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[2]);
		this.denominazione =  String.valueOf(obj[3]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[3]);
		this.partitaIva =  String.valueOf(obj[4]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[4]);
		this.email =  String.valueOf(obj[5]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[5]);
		this.telefono =  String.valueOf(obj[6]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[6]);
		this.pec =  String.valueOf(obj[7]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[7]);
		this.codiceIstat = String.valueOf(obj[8]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[8]);
		this.responsabileEnte = new ModelResponsabileEnte((Integer) obj[9],String.valueOf(obj[10]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[10]),
				String.valueOf(obj[11]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[11]),
				String.valueOf(obj[12]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[12]),
				String.valueOf(obj[13]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[13]),
				String.valueOf(obj[14]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[14]),
				String.valueOf(obj[15]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[15]));
		GregDProvince provincia = new GregDProvince();
		provincia.setDesProvincia(String.valueOf(obj[19]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[19]));
		provincia.setIdProvincia((Integer) obj[20]);
		provincia.setCodIstatProvincia(String.valueOf(obj[21]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[21]));
		this.comune = new ModelComune((Integer) obj[16],
				String.valueOf(obj[17]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[17]),
				String.valueOf(obj[18]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[18]),provincia);
//		this.pippi = ((BigDecimal) obj[22]);
//		this.fnps =  ((BigDecimal) obj[23]);
//		this.vincoloFondo =  ((BigDecimal) obj[24]);
//		this.strutturaResidenziale =  ((Boolean) obj[25]);
//		this.centroDiurnoStruttSemires = ((Boolean) obj[26]);
//		this.tipoEnte = new ModelTipoEnte((Integer) obj[27],String.valueOf(obj[28]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[28])
//				,String.valueOf(obj[29]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[29]));
//		GregDAsl asl = new GregDAsl();
//		asl.setIdAsl((Integer) obj[30]);
//		asl.setCodAsl(String.valueOf(obj[31]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[31]));
//		asl.setDesAsl(String.valueOf(obj[32]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[32]));
//		this.asl = new ModelDatiAsl(asl);
//		this.rendicontazione = new ModelStatoRendicontazione((Integer) obj[33],
//				String.valueOf(obj[34]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[34]),
//				String.valueOf(obj[35]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[35]));
		this.strutturaResidenziale =  ((Boolean) obj[22]);
		this.centroDiurnoStruttSemires = ((Boolean) obj[23]);
		this.tipoEnte = new ModelTipoEnte((Integer) obj[24],String.valueOf(obj[25]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[25])
				,String.valueOf(obj[26]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[26]));
		GregDAsl asl = new GregDAsl();
		asl.setIdAsl((Integer) obj[27]);
		asl.setCodAsl(String.valueOf(obj[28]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[28]));
		asl.setDesAsl(String.valueOf(obj[29]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[29]));
		this.asl = new ModelDatiAsl(asl);
		this.rendicontazione = new ModelStatoRendicontazione((Integer) obj[30],
				String.valueOf(obj[31]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[31]),
				String.valueOf(obj[32]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[32]));
	}
	
	public ModelDatiEnte(GregTSchedeEntiGestori scheda) {
		this.idSchedaEntegestore = scheda.getIdSchedaEnteGestore();
		this.codiceRegionale = scheda.getCodiceRegionale();
		this.codiceFiscale = scheda.getCodiceFiscale();
//		this.denominazione = scheda.getDenominazione();
//		this.partitaIva = scheda.getPartitaIva();
//		this.tipoEnte = new ModelTipoEnte(scheda.getGregDTipoEnte());
//		this.comune = new ModelComune(scheda.getGregDComuni());
//		this.codiceIstat = scheda.getCodIstatEnte();
//		this.asl = new ModelDatiAsl(scheda.getGregDAsl());
//		this.email = scheda.getEmail();
//		this.telefono = scheda.getTelefono();
//		this.pec = scheda.getPec();
//		this.responsabileEnte = new ModelResponsabileEnte(scheda.getGregTResponsabileEnteGestore());
//		this.strutturaResidenziale = scheda.getStrutturaResidenziale();
//		this.centroDiurnoStruttSemires = scheda.getCentroDiurnoStruttSemires();
//		this.fnps = scheda.getFnps();
//		this.vincoloFondo = scheda.getVincoloFondo();
//		this.pippi = scheda.getPippi();
		this.rendicontazione = getStatoRendicontazione(scheda)!= null ? 
				new ModelStatoRendicontazione(getStatoRendicontazione(scheda).getGregDStatoRendicontazione()) : null;
	}
	
	private GregTRendicontazioneEnte getStatoRendicontazione(GregTSchedeEntiGestori scheda) {
		GregTRendicontazioneEnte rendicontazioneEnte = null;
		if(scheda.getGregTRendicontazioneEntes() != null) {
			Iterator<GregTRendicontazioneEnte> iterator = scheda.getGregTRendicontazioneEntes().iterator();
			rendicontazioneEnte = iterator.hasNext()? iterator.next() : null;
			while(iterator.hasNext()) {
				GregTRendicontazioneEnte temp = iterator.next();
				if(temp.getGregTSchedeEntiGestori().getIdSchedaEnteGestore() == scheda.getIdSchedaEnteGestore())
					rendicontazioneEnte = temp;
			}
		}
		return rendicontazioneEnte;
	}
	
	// Costruttore utilizzato per ListaDenominazioniByComune in Enti Gestori Attivi a FE
	public ModelDatiEnte(GregRSchedeEntiGestoriComuni scheda) {
		this.idSchedaEntegestore = Checker.isValorizzato(scheda.getIdSchedaEnteGestoreComune())? scheda.getIdSchedaEnteGestoreComune() : null;
//		this.denominazione = scheda.getGregTSchedeEntiGestori().getDenominazione();
	}

	public Integer getIdSchedaEntegestore() {
		return idSchedaEntegestore;
	}

	public void setIdSchedaEntegestore(Integer idSchedaEntegestore) {
		this.idSchedaEntegestore = idSchedaEntegestore;
	}

	public String getCodiceRegionale() {
		return codiceRegionale;
	}

	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public ModelTipoEnte getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(ModelTipoEnte tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	public ModelComune getComune() {
		return comune;
	}

	public void setComune(ModelComune comune) {
		this.comune = comune;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public ModelDatiAsl getAsl() {
		return asl;
	}

	public void setAsl(ModelDatiAsl asl) {
		this.asl = asl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public ModelResponsabileEnte getResponsabileEnte() {
		return responsabileEnte;
	}

	public void setResponsabileEnte(ModelResponsabileEnte responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}

	public Boolean getStrutturaResidenziale() {
		return strutturaResidenziale;
	}

	public void setStrutturaResidenziale(Boolean strutturaResidenziale) {
		this.strutturaResidenziale = strutturaResidenziale;
	}

	public Boolean getCentroDiurnoStruttSemires() {
		return centroDiurnoStruttSemires;
	}

	public void setCentroDiurnoStruttSemires(Boolean centroDiurnoStruttSemires) {
		this.centroDiurnoStruttSemires = centroDiurnoStruttSemires;
	}

	public BigDecimal getFnps() {
		return fnps;
	}

	public void setFnps(BigDecimal fnps) {
		this.fnps = fnps;
	}

	public BigDecimal getVincoloFondo() {
		return vincoloFondo;
	}

	public void setVincoloFondo(BigDecimal vincoloFondo) {
		this.vincoloFondo = vincoloFondo;
	}

	public ModelStatoRendicontazione getRendicontazione() {
		return rendicontazione;
	}

	public void setRendicontazione(ModelStatoRendicontazione rendicontazione) {
		this.rendicontazione = rendicontazione;
	}

//	public ArrayList<String> getElencoAzioni() {
//		return elencoAzioni;
//	}
//
//	public void setElencoAzioni(ArrayList<String> elencoAzioni) {
//		this.elencoAzioni = elencoAzioni;
//	}

	public BigDecimal getPippi() {
		return pippi;
	}

	public void setPippi(BigDecimal pippi) {
		this.pippi = pippi;
	}
	
}