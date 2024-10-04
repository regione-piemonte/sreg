/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;

public class ModelPrestazioneUtenzaModA {

	private Integer idPrestazioneUtenza;
	private Integer idPrestazione;
	private String codPrestazione;
	private String descPrestazione;
	private List<ModelTargetUtenza> listaTargetUtenza;
	
	public ModelPrestazioneUtenzaModA() {}
	
	public ModelPrestazioneUtenzaModA(GregRPrestReg1UtenzeRegionali1 r) {
		if(r != null) {
			this.idPrestazioneUtenza = r.getIdPrestReg1UtenzaRegionale1();
			if(r.getGregTPrestazioniRegionali1() != null) {
				this.idPrestazione = r.getGregTPrestazioniRegionali1().getIdPrestReg1();
				this.codPrestazione = r.getGregTPrestazioniRegionali1().getCodPrestReg1();
				this.descPrestazione = r.getGregTPrestazioniRegionali1().getDesPrestReg1();
			}
		}
		this.listaTargetUtenza = new ArrayList<ModelTargetUtenza>();
	}
	
	public Integer getIdPrestazioneUtenza() {
		return idPrestazioneUtenza;
	}
	public void setIdPrestazioneUtenza(Integer idPrestazioneUtenza) {
		this.idPrestazioneUtenza = idPrestazioneUtenza;
	}
	public String getCodPrestazione() {
		return codPrestazione;
	}
	public void setCodPrestazione(String codPrestazione) {
		this.codPrestazione = codPrestazione;
	}
	public String getDescPrestazione() {
		return descPrestazione;
	}
	public void setDescPrestazione(String descPrestazione) {
		this.descPrestazione = descPrestazione;
	}
	public List<ModelTargetUtenza> getListaTargetUtenza() {
		return listaTargetUtenza;
	}
	public void setListaTargetUtenza(List<ModelTargetUtenza> targetUtenza) {
		this.listaTargetUtenza = targetUtenza;
	}

	public Integer getIdPrestazione() {
		return idPrestazione;
	}

	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
	}
	
	
}
