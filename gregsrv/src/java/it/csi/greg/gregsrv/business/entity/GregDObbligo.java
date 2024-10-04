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
 * The persistent class for the greg_d_obbligo database table.
 * 
 */
@Entity
@Table(name="greg_d_obbligo")
@NamedQuery(name="GregDObbligo.findAll", query="SELECT g FROM GregDObbligo g where g.dataCancellazione is null")
public class GregDObbligo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_OBBLIGO_IDOBBLIGO_GENERATOR", sequenceName="GREG_D_OBBLIGO_ID_OBBLIGO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_OBBLIGO_IDOBBLIGO_GENERATOR")
	@Column(name="id_obbligo")
	private Integer idObbligo;

	@Column(name="cod_obbligo")
	private String codObbligo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="des_obbligo")
	private String desObbligo;

	//bi-directional many-to-one association to GregREnteTab
	@JsonIgnore
	@OneToMany(mappedBy="gregDObbligo")
	private Set<GregREnteTab> gregREnteTabs;

	public GregDObbligo() {
	}

	public Integer getIdObbligo() {
		return this.idObbligo;
	}

	public void setIdObbligo(Integer idObbligo) {
		this.idObbligo = idObbligo;
	}

	public String getCodObbligo() {
		return this.codObbligo;
	}

	public void setCodObbligo(String codObbligo) {
		this.codObbligo = codObbligo;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public String getDesObbligo() {
		return this.desObbligo;
	}

	public void setDesObbligo(String desObbligo) {
		this.desObbligo = desObbligo;
	}

	public Set<GregREnteTab> getGregREnteTabs() {
		return this.gregREnteTabs;
	}

	public void setGregREnteTabs(Set<GregREnteTab> gregREnteTabs) {
		this.gregREnteTabs = gregREnteTabs;
	}

	public GregREnteTab addGregREnteTab(GregREnteTab gregREnteTab) {
		getGregREnteTabs().add(gregREnteTab);
		gregREnteTab.setGregDObbligo(this);

		return gregREnteTab;
	}

	public GregREnteTab removeGregREnteTab(GregREnteTab gregREnteTab) {
		getGregREnteTabs().remove(gregREnteTab);
		gregREnteTab.setGregDObbligo(null);

		return gregREnteTab;
	}

}