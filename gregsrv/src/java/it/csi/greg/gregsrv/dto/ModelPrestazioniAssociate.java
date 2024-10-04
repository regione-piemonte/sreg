/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.csi.greg.gregsrv.business.entity.GregRCartaServiziPreg1;


public class ModelPrestazioniAssociate {
	
	private Integer pkIdPrestazioneAssociata;
	private String codicePrestazione;
	private String desPrestazione;
	private Integer idPrestazione;
	private Boolean prestFiglia;
	private Integer annoFineValidita;
	private Timestamp dataFineValidita;
	
	
	public ModelPrestazioniAssociate() { }
	
	public ModelPrestazioniAssociate(GregRCartaServiziPreg1 presAssociata) {
		this.pkIdPrestazioneAssociata = presAssociata.getIdCartaServiziPreg1();
		this.codicePrestazione = presAssociata.getGregTPrestazioniRegionali1().getCodPrestReg1();
		this.desPrestazione = presAssociata.getGregTPrestazioniRegionali1().getDesPrestReg1();
		this.idPrestazione = presAssociata.getGregTPrestazioniRegionali1().getIdPrestReg1();
		this.prestFiglia = presAssociata.getGregTPrestazioniRegionali1().getGregTPrestazioniRegionali1() == null? false : true;
		SimpleDateFormat f = new SimpleDateFormat("yyyy");
		this.annoFineValidita = presAssociata.getGregTPrestazioniRegionali1().getDataFineValidita()!=null ? Integer.valueOf(f.format(new Date(presAssociata.getGregTPrestazioniRegionali1().getDataFineValidita().getTime()))) : null;
		this.dataFineValidita = presAssociata.getGregTPrestazioniRegionali1().getDataFineValidita()!=null ? presAssociata.getGregTPrestazioniRegionali1().getDataFineValidita() : null;
	}

	public Integer getPkIdPrestazioneAssociata() {
		return pkIdPrestazioneAssociata;
	}

	public void setPkIdPrestazioneAssociata(Integer pkIdPrestazioneAssociata) {
		this.pkIdPrestazioneAssociata = pkIdPrestazioneAssociata;
	}

	public String getCodicePrestazione() {
		return codicePrestazione;
	}

	public void setCodicePrestazione(String codicePrestazione) {
		this.codicePrestazione = codicePrestazione;
	}

	public String getDesPrestazione() {
		return desPrestazione;
	}

	public void setDesPrestazione(String desPrestazione) {
		this.desPrestazione = desPrestazione;
	}

	public Integer getIdPrestazione() {
		return idPrestazione;
	}

	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
	}

	public Boolean getPrestFiglia() {
		return prestFiglia;
	}

	public void setPrestFiglia(Boolean prestFiglia) {
		this.prestFiglia = prestFiglia;
	}

	public Integer getAnnoFineValidita() {
		return annoFineValidita;
	}

	public void setAnnoFineValidita(Integer annoFineValidita) {
		this.annoFineValidita = annoFineValidita;
	}

	public Timestamp getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	
}