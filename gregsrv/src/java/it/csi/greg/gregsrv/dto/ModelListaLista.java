/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregTLista;

public class ModelListaLista {
	
	private Integer idLista;
	private String codLista;
	private String descLista;
	private String infoLista;
	private List<ModelListaEnti> enti;
	private List<ModelListaEnti> entiMancate;
	private List<ModelListaEnti> entiDaCopiare;
	
	public ModelListaLista () {}


	public ModelListaLista(GregTLista lista, List<GregRListaEntiGestori> listaEnte, List<ModelListaEnti> listaEnti) {
		super();
		this.idLista = lista.getIdLista();
		this.codLista = lista.getCodLista();
		this.descLista = lista.getDescLista();
		this.infoLista = lista.getInfoLista();
		this.enti = new ArrayList<ModelListaEnti>();
		for(GregRListaEntiGestori pa : listaEnte) {
			ModelListaEnti a = new ModelListaEnti(pa);
			enti.add(a);
		}
		this.entiMancate = new ArrayList<ModelListaEnti>();
		for(ModelListaEnti a : listaEnti) {
			boolean trovato = false;
			for(ModelListaEnti b : enti) {
				if(a.getCodEnte().equals(b.getCodEnte())) {
					trovato = true;
				}
			}
			if(!trovato) {
				this.entiMancate.add(a);
			}
		}
	}


	public Integer getIdLista() {
		return idLista;
	}


	public void setIdLista(Integer idLista) {
		this.idLista = idLista;
	}


	public String getCodLista() {
		return codLista;
	}


	public void setCodLista(String codLista) {
		this.codLista = codLista;
	}


	public String getDescLista() {
		return descLista;
	}


	public void setDescLista(String descLista) {
		this.descLista = descLista;
	}


	public String getInfoLista() {
		return infoLista;
	}


	public void setInfoLista(String infoLista) {
		this.infoLista = infoLista;
	}


	public List<ModelListaEnti> getEnti() {
		return enti;
	}


	public void setEnti(List<ModelListaEnti> enti) {
		this.enti = enti;
	}


	public List<ModelListaEnti> getEntiMancate() {
		return entiMancate;
	}


	public void setEntiMancate(List<ModelListaEnti> entiMancate) {
		this.entiMancate = entiMancate;
	}


	public List<ModelListaEnti> getEntiDaCopiare() {
		return entiDaCopiare;
	}


	public void setEntiDaCopiare(List<ModelListaEnti> entiDaCopiare) {
		this.entiDaCopiare = entiDaCopiare;
	}


	
	
}

	