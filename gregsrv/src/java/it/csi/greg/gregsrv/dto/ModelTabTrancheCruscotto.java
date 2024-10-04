/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


public class ModelTabTrancheCruscotto {
	
	private String codTab; 
	private String desTab; 
	private String codTranche;
	private String path;
	private String azione;
	
	public ModelTabTrancheCruscotto () {}
	
	public ModelTabTrancheCruscotto(Object[] obj) { 
		this.codTab = String.valueOf(obj[0]);
		this.desTab = String.valueOf(obj[1]);
		this.codTranche = String.valueOf(obj[2]);
		this.path = String.valueOf(obj[3]);
	}

	public String getCodTab() {
		return codTab;
	}

	public void setCodTab(String codTab) {
		this.codTab = codTab;
	}

	public String getDesTab() {
		return desTab;
	}

	public void setDesTab(String desTab) {
		this.desTab = desTab;
	}

	
	public String getCodTranche() {
		return codTranche;
	}

	public void setCodTranche(String codTranche) {
		this.codTranche = codTranche;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}	
	
}
