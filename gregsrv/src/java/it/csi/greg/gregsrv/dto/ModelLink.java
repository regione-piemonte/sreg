/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;

public class ModelLink {

	private String title; 
	private String link; 
	private String tooltip; 
	private String[] fragment;
	private String azione;
	private String sigla;

	
	public ModelLink () {}
	
	public ModelLink(Object[] obj) { 
		this.title = String.valueOf(obj[0]);
		this.link = String.valueOf(obj[1]);
		this.tooltip = String.valueOf(obj[2]);
		this.azione = String.valueOf(obj[3]);
		this.sigla = String.valueOf(obj[4]);
	
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String[] getFragment() {
		return fragment;
	}

	public void setFragment(String[] fragment) {
		this.fragment = fragment;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	
}
