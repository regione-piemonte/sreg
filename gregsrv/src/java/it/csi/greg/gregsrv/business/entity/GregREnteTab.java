/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_ente_tab database table.
 * 
 */
@Entity
@Table(name="greg_r_ente_tab")
@NamedQuery(name="GregREnteTab.findAll", query="SELECT g FROM GregREnteTab g where g.dataCancellazione IS NULL")
public class GregREnteTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_ENTE_TAB_IDENTETAB_GENERATOR", sequenceName="GREG_R_ENTE_TAB_ID_ENTE_TAB_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_ENTE_TAB_IDENTETAB_GENERATOR")
	@Column(name="id_ente_tab")
	private Integer idEnteTab;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;
	
	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDObbligo
	@ManyToOne
	@JoinColumn(name="id_obbligo")
	private GregDObbligo gregDObbligo;

	//bi-directional many-to-one association to GregDTab
	@ManyToOne
	@JoinColumn(name="id_tab")
	private GregDTab gregDTab;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregREnteTab() {
	}

	public Integer getIdEnteTab() {
		return this.idEnteTab;
	}

	public void setIdEnteTab(Integer idEnteTab) {
		this.idEnteTab = idEnteTab;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public GregDObbligo getGregDObbligo() {
		return this.gregDObbligo;
	}

	public void setGregDObbligo(GregDObbligo gregDObbligo) {
		this.gregDObbligo = gregDObbligo;
	}

	public GregDTab getGregDTab() {
		return this.gregDTab;
	}

	public void setGregDTab(GregDTab gregDTab) {
		this.gregDTab = gregDTab;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}
	
	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}
}