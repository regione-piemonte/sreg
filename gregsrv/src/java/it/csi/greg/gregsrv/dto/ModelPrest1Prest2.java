/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.util.List;

public class ModelPrest1Prest2 {

	private Integer idPrest2;
	private String codPrest2;
	private String descPrest2;
	private Integer idTipologia;
	private String codTipologia;
	private String codPrestIstat;
	private String descPrestIstat;
	private List<String> utenze;
	private List<String> utenzeMin;
	private List<ModelPrestUtenza> utenzeConf;
	private List<ModelPrestUtenza> utenzeMinConf;
	private List<ModelPrest2PrestIstat> prestIstat;
	private String macroArea;
	private String sottoArea;
	private String sottoAreaDet;
	private String presidio;
	private String classificazionePresidio;
	private String funzionePresidio;
	private String voce;
	private String sottoVoce;
	private String tipoResidenza;
	private String nota;
	private List<ModelNomenclatore> nomenclatore;
	private Timestamp dal;
	private Timestamp al;
	private Timestamp dalRelazione;
	private Timestamp alRelazione;
	private Timestamp dataMin;
	private boolean modificabile;
	private boolean utilizzato;
	private Integer ordinamento;
	private Timestamp dataCancellazione;
	private Integer idPrest1;
	private Timestamp dataLastRelazione;
	private Timestamp dataCreazione;
	
	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public ModelPrest1Prest2() {
	}

	public Integer getIdPrest2() {
		return idPrest2;
	}

	public void setIdPrest2(Integer idPrest2) {
		this.idPrest2 = idPrest2;
	}

	public String getCodPrest2() {
		return codPrest2;
	}

	public void setCodPrest2(String codPrest2) {
		this.codPrest2 = codPrest2;
	}

	public String getDescPrest2() {
		return descPrest2;
	}

	public void setDescPrest2(String descPrest2) {
		this.descPrest2 = descPrest2;
	}

	public List<String> getUtenze() {
		return utenze;
	}

	public void setUtenze(List<String> utenze) {
		this.utenze = utenze;
	}

	public String getMacroArea() {
		return macroArea;
	}

	public void setMacroArea(String macroArea) {
		this.macroArea = macroArea;
	}

	public String getSottoArea() {
		return sottoArea;
	}

	public void setSottoArea(String sottoArea) {
		this.sottoArea = sottoArea;
	}

	public String getSottoAreaDet() {
		return sottoAreaDet;
	}

	public void setSottoAreaDet(String sottoAreaDet) {
		this.sottoAreaDet = sottoAreaDet;
	}

	public String getPresidio() {
		return presidio;
	}

	public void setPresidio(String presidio) {
		this.presidio = presidio;
	}

	public String getClassificazionePresidio() {
		return classificazionePresidio;
	}

	public void setClassificazionePresidio(String classificazionePresidio) {
		this.classificazionePresidio = classificazionePresidio;
	}

	public String getFunzionePresidio() {
		return funzionePresidio;
	}

	public void setFunzionePresidio(String funzionePresidio) {
		this.funzionePresidio = funzionePresidio;
	}

	public String getVoce() {
		return voce;
	}

	public void setVoce(String voce) {
		this.voce = voce;
	}

	public String getSottoVoce() {
		return sottoVoce;
	}

	public void setSottoVoce(String sottoVoce) {
		this.sottoVoce = sottoVoce;
	}

	public String getTipoResidenza() {
		return tipoResidenza;
	}

	public void setTipoResidenza(String tipoResidenza) {
		this.tipoResidenza = tipoResidenza;
	}

	public Timestamp getDal() {
		return dal;
	}

	public void setDal(Timestamp dal) {
		this.dal = dal;
	}

	public Timestamp getAl() {
		return al;
	}

	public void setAl(Timestamp al) {
		this.al = al;
	}

	public List<ModelPrestUtenza> getUtenzeConf() {
		return utenzeConf;
	}

	public void setUtenzeConf(List<ModelPrestUtenza> utenzeConf) {
		this.utenzeConf = utenzeConf;
	}
	
	

	public List<ModelNomenclatore> getNomenclatore() {
		return nomenclatore;
	}

	public void setNomenclatore(List<ModelNomenclatore> nomenclatore) {
		this.nomenclatore = nomenclatore;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}

	public Timestamp getDataMin() {
		return dataMin;
	}

	public void setDataMin(Timestamp dataMin) {
		this.dataMin = dataMin;
	}

	public boolean isUtilizzato() {
		return utilizzato;
	}

	public void setUtilizzato(boolean utilizzato) {
		this.utilizzato = utilizzato;
	}

	public Integer getIdTipologia() {
		return idTipologia;
	}

	public void setIdTipologia(Integer idTipologia) {
		this.idTipologia = idTipologia;
	}

	public String getCodTipologia() {
		return codTipologia;
	}

	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}

	public List<ModelPrest2PrestIstat> getPrestIstat() {
		return prestIstat;
	}

	public void setPrestIstat(List<ModelPrest2PrestIstat> prestIstat) {
		this.prestIstat = prestIstat;
	}

	public String getCodPrestIstat() {
		return codPrestIstat;
	}

	public void setCodPrestIstat(String codPrestIstat) {
		this.codPrestIstat = codPrestIstat;
	}

	public String getDescPrestIstat() {
		return descPrestIstat;
	}

	public void setDescPrestIstat(String descPrestIstat) {
		this.descPrestIstat = descPrestIstat;
	}

	public List<String> getUtenzeMin() {
		return utenzeMin;
	}

	public void setUtenzeMin(List<String> utenzeMin) {
		this.utenzeMin = utenzeMin;
	}

	public List<ModelPrestUtenza> getUtenzeMinConf() {
		return utenzeMinConf;
	}

	public void setUtenzeMinConf(List<ModelPrestUtenza> utenzeMinConf) {
		this.utenzeMinConf = utenzeMinConf;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public Timestamp getDalRelazione() {
		return dalRelazione;
	}

	public void setDalRelazione(Timestamp dalRelazione) {
		this.dalRelazione = dalRelazione;
	}

	public Timestamp getAlRelazione() {
		return alRelazione;
	}

	public void setAlRelazione(Timestamp alRelazione) {
		this.alRelazione = alRelazione;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public Integer getIdPrest1() {
		return idPrest1;
	}

	public void setIdPrest1(Integer idPrest1) {
		this.idPrest1 = idPrest1;
	}

	public Timestamp getDataLastRelazione() {
		return dataLastRelazione;
	}

	public void setDataLastRelazione(Timestamp dataLastRelazione) {
		this.dataLastRelazione = dataLastRelazione;
	}

}
