/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


public class ModelTabTranche {
	
	private Integer idTab; 
	private String codTab; 
	private String desTab; 
	private String desEstesaTab; 
	private String redirect;
	private String codTranche;
	private String desTranche;
	private Integer idObbligo;
	private String codObbligo;
	private String desObbligo;
	private Integer idEnteTab;
	private Integer ordinamento;
	
	public ModelTabTranche () {}
	
	public ModelTabTranche(Object[] obj, boolean findmodelli) { 
		this.idTab =  (Integer) obj[0];
		this.codTab = String.valueOf(obj[1]);
		this.desTab = String.valueOf(obj[2]);
		this.desEstesaTab = String.valueOf(obj[3]);
		this.redirect = String.valueOf(obj[4]);
		this.ordinamento = (Integer) obj[5];
		if (findmodelli) {
			this.codTranche = String.valueOf(obj[6]);
			this.desTranche = String.valueOf(obj[7]);
			this.idObbligo =  (Integer) obj[8];
			this.codObbligo = String.valueOf(obj[9]);
			this.desObbligo = String.valueOf(obj[10]);
			this.idEnteTab = (Integer) obj[11];
		}
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

	public String getDesEstesaTab() {
		return desEstesaTab;
	}

	public void setDesEstesaTab(String desEstesaTab) {
		this.desEstesaTab = desEstesaTab;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getCodTranche() {
		return codTranche;
	}

	public void setCodTranche(String codTranche) {
		this.codTranche = codTranche;
	}

	public String getCodObbligo() {
		return codObbligo;
	}

	public void setCodObbligo(String codObbligo) {
		this.codObbligo = codObbligo;
	}

	public String getDesTranche() {
		return desTranche;
	}

	public void setDesTranche(String desTranche) {
		this.desTranche = desTranche;
	}

	public String getDesObbligo() {
		return desObbligo;
	}

	public void setDesObbligo(String desObbligo) {
		this.desObbligo = desObbligo;
	}

	public Integer getIdTab() {
		return idTab;
	}

	public void setIdTab(Integer idTab) {
		this.idTab = idTab;
	}

	public Integer getIdObbligo() {
		return idObbligo;
	}

	public void setIdObbligo(Integer idObbligo) {
		this.idObbligo = idObbligo;
	}

	public Integer getIdEnteTab() {
		return idEnteTab;
	}

	public void setIdEnteTab(Integer idEnteTab) {
		this.idEnteTab = idEnteTab;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}
