/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_tranche database table.
 * 
 */
@Entity
@Table(name="greg_d_tranche")
@NamedQuery(name="GregDTranche.findAll", query="SELECT g FROM GregDTranche g where g.dataCancellazione IS NULL")
public class GregDTranche implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TRANCHE_IDTRANCHE_GENERATOR", sequenceName="GREG_D_TRANCHE_ID_TRANCHE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TRANCHE_IDTRANCHE_GENERATOR")
	@Column(name="id_tranche")
	private Integer idTranche;

	@Column(name="cod_tranche")
	private String codTranche;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Temporal(TemporalType.DATE)
	@Column(name="data_scadenza")
	private Date dataScadenza;

	@Column(name="des_tranche")
	private String desTranche;

	//bi-directional many-to-one association to GregRTabTranche
	@JsonIgnore
	@OneToMany(mappedBy="gregDTranche")
	private Set<GregRTabTranche> gregRTabTranches;

	public GregDTranche() {
	}

	public Integer getIdTranche() {
		return this.idTranche;
	}

	public void setIdTranche(Integer idTranche) {
		this.idTranche = idTranche;
	}

	public String getCodTranche() {
		return this.codTranche;
	}

	public void setCodTranche(String codTranche) {
		this.codTranche = codTranche;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public Date getDataScadenza() {
		return this.dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getDesTranche() {
		return this.desTranche;
	}

	public void setDesTranche(String desTranche) {
		this.desTranche = desTranche;
	}

	public Set<GregRTabTranche> getGregRTabTranches() {
		return this.gregRTabTranches;
	}

	public void setGregRTabTranches(Set<GregRTabTranche> gregRTabTranches) {
		this.gregRTabTranches = gregRTabTranches;
	}

	public GregRTabTranche addGregRTabTranch(GregRTabTranche gregRTabTranch) {
		getGregRTabTranches().add(gregRTabTranch);
		gregRTabTranch.setGregDTranche(this);

		return gregRTabTranch;
	}

	public GregRTabTranche removeGregRTabTranch(GregRTabTranche gregRTabTranch) {
		getGregRTabTranches().remove(gregRTabTranch);
		gregRTabTranch.setGregDTranche(null);

		return gregRTabTranch;
	}

}