/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDProfilo;
import it.csi.greg.gregsrv.business.entity.GregRProfiloAzione;

public class ModelListaProfilo {
	
	private Integer idProfilo;
	private String codProfilo;
	private String descProfilo;
	private String infoProfilo;
	private String tipoProfilo;
	private List<ModelListaAzione> azioni;
	private List<ModelListaAzione> azioniMancate;
	private List<ModelListaAzione> azioniDaCopiare;
	
	public ModelListaProfilo () {}


	public ModelListaProfilo(GregDProfilo profilo, List<GregRProfiloAzione> profiloAzione, List<ModelListaAzione> listaAzioni) {
		super();
		this.idProfilo = profilo.getIdProfilo();
		this.codProfilo = profilo.getCodProfilo();
		this.descProfilo = profilo.getDescProfilo();
		this.infoProfilo = profilo.getInfoProfilo();
		this.tipoProfilo = profilo.getTipoProfilo();
		this.azioni = new ArrayList<ModelListaAzione>();
		for(GregRProfiloAzione pa : profiloAzione) {
			ModelListaAzione a = new ModelListaAzione(pa);
			azioni.add(a);
		}
		this.azioniMancate = new ArrayList<ModelListaAzione>();
		for(ModelListaAzione a : listaAzioni) {
			boolean trovato = false;
			for(ModelListaAzione b : azioni) {
				if(a.getIdAzione().equals(b.getIdAzione())) {
					trovato = true;
				}
			}
			if(!trovato) {
				this.azioniMancate.add(a);
			}
		}
	}


	public Integer getIdProfilo() {
		return idProfilo;
	}


	public void setIdProfilo(Integer idProfilo) {
		this.idProfilo = idProfilo;
	}


	public String getCodProfilo() {
		return codProfilo;
	}


	public void setCodProfilo(String codProfilo) {
		this.codProfilo = codProfilo;
	}


	public String getDescProfilo() {
		return descProfilo;
	}


	public void setDescProfilo(String descProfilo) {
		this.descProfilo = descProfilo;
	}


	public String getInfoProfilo() {
		return infoProfilo;
	}


	public void setInfoProfilo(String infoProfilo) {
		this.infoProfilo = infoProfilo;
	}


	public String getTipoProfilo() {
		return tipoProfilo;
	}


	public void setTipoProfilo(String tipoProfilo) {
		this.tipoProfilo = tipoProfilo;
	}


	public List<ModelListaAzione> getAzioni() {
		return azioni;
	}


	public void setAzioni(List<ModelListaAzione> azioni) {
		this.azioni = azioni;
	}


	public List<ModelListaAzione> getAzioniMancate() {
		return azioniMancate;
	}


	public void setAzioniMancate(List<ModelListaAzione> azioniMancate) {
		this.azioniMancate = azioniMancate;
	}


	public List<ModelListaAzione> getAzioniDaCopiare() {
		return azioniDaCopiare;
	}


	public void setAzioniDaCopiare(List<ModelListaAzione> azioniDaCopiare) {
		this.azioniDaCopiare = azioniDaCopiare;
	}
	
}

	