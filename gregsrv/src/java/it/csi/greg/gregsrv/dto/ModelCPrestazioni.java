/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;

public class ModelCPrestazioni {
	private Integer idPrestazione;
	private String codPrestazione;
	private String descPrestazione;
	private String informativa;
	private boolean flag;
	private List<ModelCTargetUtenze> listaTargetUtenze;
	
	public ModelCPrestazioni () {}
	
	public ModelCPrestazioni (GregTPrestazioniRegionali1 prestazione) {
		this.idPrestazione = prestazione.getIdPrestReg1();
		this.codPrestazione = prestazione.getCodPrestReg1();
		this.descPrestazione = prestazione.getDesPrestReg1();
		this.informativa=prestazione.getInformativa();
		this.listaTargetUtenze = new ArrayList<ModelCTargetUtenze>();
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Integer getIdPrestazione() {
		return idPrestazione;
	}

	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
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

	public String getInformativa() {
		return informativa;
	}

	public void setInformativa(String informativa) {
		this.informativa = informativa;
	}

	public List<ModelCTargetUtenze> getListaTargetUtenze() {
		return listaTargetUtenze;
	}

	public void setListaTargetUtenze(List<ModelCTargetUtenze> listaTargetUtenze) {
		this.listaTargetUtenze = listaTargetUtenze;
	}

	
	
}
