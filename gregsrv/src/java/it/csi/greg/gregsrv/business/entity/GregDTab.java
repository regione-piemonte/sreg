/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_tab database table.
 * 
 */
@Entity
@Table(name="greg_d_tab")
@NamedQueries({
	@NamedQuery(name="GregDTab.findAll", query="SELECT g FROM GregDTab g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDTab.findByCod", query="SELECT g FROM GregDTab g where g.codTab = :codTab AND g.dataCancellazione IS NULL"),	
})
public class GregDTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TAB_IDTAB_GENERATOR", sequenceName="GREG_D_TAB_ID_TAB_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TAB_IDTAB_GENERATOR")
	@Column(name="id_tab")
	private Integer idTab;

	@Column(name="cod_tab")
	private String codTab;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="des_estesa_tab")
	private String desEstesaTab;

	@Column(name="des_tab")
	private String desTab;

	private Integer ordinamento;

	private String redirect;

	private String fragment;

	//bi-directional many-to-one association to GregREnteTab
	@JsonIgnore
	@OneToMany(mappedBy="gregDTab")
	private Set<GregREnteTab> gregREnteTabs;

	//bi-directional many-to-one association to GregRTabTranche
	@JsonIgnore
	@OneToMany(mappedBy="gregDTab")
	private Set<GregRTabTranche> gregRTabTranches;
	
	@JsonIgnore
	@OneToMany(mappedBy="gregDTab")
	private Set<GregRCheck> gregRChecks;

	public GregDTab() {
	}

	public Integer getIdTab() {
		return this.idTab;
	}

	public void setIdTab(Integer idTab) {
		this.idTab = idTab;
	}

	public String getCodTab() {
		return this.codTab;
	}

	public void setCodTab(String codTab) {
		this.codTab = codTab;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public String getDesEstesaTab() {
		return this.desEstesaTab;
	}

	public void setDesEstesaTab(String desEstesaTab) {
		this.desEstesaTab = desEstesaTab;
	}

	public String getDesTab() {
		return this.desTab;
	}

	public void setDesTab(String desTab) {
		this.desTab = desTab;
	}

	public Integer getOrdinamento() {
		return this.ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getRedirect() {
		return this.redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getFragment() {
		return this.fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}
	
	public Set<GregREnteTab> getGregREnteTabs() {
		return this.gregREnteTabs;
	}

	public void setGregREnteTabs(Set<GregREnteTab> gregREnteTabs) {
		this.gregREnteTabs = gregREnteTabs;
	}

	public GregREnteTab addGregREnteTab(GregREnteTab gregREnteTab) {
		getGregREnteTabs().add(gregREnteTab);
		gregREnteTab.setGregDTab(this);

		return gregREnteTab;
	}

	public GregREnteTab removeGregREnteTab(GregREnteTab gregREnteTab) {
		getGregREnteTabs().remove(gregREnteTab);
		gregREnteTab.setGregDTab(null);

		return gregREnteTab;
	}

	public Set<GregRTabTranche> getGregRTabTranches() {
		return this.gregRTabTranches;
	}

	public void setGregRTabTranches(Set<GregRTabTranche> gregRTabTranches) {
		this.gregRTabTranches = gregRTabTranches;
	}

	public GregRTabTranche addGregRTabTranch(GregRTabTranche gregRTabTranch) {
		getGregRTabTranches().add(gregRTabTranch);
		gregRTabTranch.setGregDTab(this);

		return gregRTabTranch;
	}

	public GregRTabTranche removeGregRTabTranch(GregRTabTranche gregRTabTranch) {
		getGregRTabTranches().remove(gregRTabTranch);
		gregRTabTranch.setGregDTab(null);

		return gregRTabTranch;
	}

	public Set<GregRCheck> getGregRChecks() {
		return gregRChecks;
	}

	public void setGregRChecks(Set<GregRCheck> gregRChecks) {
		this.gregRChecks = gregRChecks;
	}
	
	public GregRCheck addGregRCheck(GregRCheck gregRCheck) {
		getGregRChecks().add(gregRCheck);
		gregRCheck.setGregDTab(this);

		return gregRCheck;
	}

	public GregRCheck removeGregRCheck(GregRCheck gregRCheck) {
		getGregRChecks().remove(gregRCheck);
		gregRCheck.setGregDTab(null);

		return gregRCheck;
	}

}