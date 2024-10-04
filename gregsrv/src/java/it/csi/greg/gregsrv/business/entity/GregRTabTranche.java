/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_tab_tranche database table.
 * 
 */
@Entity
@Table(name="greg_r_tab_tranche")
@NamedQuery(name="GregRTabTranche.findAll", query="SELECT g FROM GregRTabTranche g where g.dataCancellazione IS NULL")
public class GregRTabTranche implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TAB_TRANCHE_IDTABTRANCHE_GENERATOR", sequenceName="GREG_R_TAB_TRANCHE_ID_TAB_TRANCHE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TAB_TRANCHE_IDTABTRANCHE_GENERATOR")
	@Column(name="id_tab_tranche")
	private Integer idTabTranche;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	//bi-directional many-to-one association to GregDTab
	@ManyToOne
	@JoinColumn(name="id_tab")
	private GregDTab gregDTab;

	//bi-directional many-to-one association to GregDTranche
	@ManyToOne
	@JoinColumn(name="id_tranche")
	private GregDTranche gregDTranche;

	public GregRTabTranche() {
	}

	public Integer getIdTabTranche() {
		return this.idTabTranche;
	}

	public void setIdTabTranche(Integer idTabTranche) {
		this.idTabTranche = idTabTranche;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public GregDTab getGregDTab() {
		return this.gregDTab;
	}

	public void setGregDTab(GregDTab gregDTab) {
		this.gregDTab = gregDTab;
	}

	public GregDTranche getGregDTranche() {
		return this.gregDTranche;
	}

	public void setGregDTranche(GregDTranche gregDTranche) {
		this.gregDTranche = gregDTranche;
	}

}