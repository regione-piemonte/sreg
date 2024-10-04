/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the greg_r_check database table.
 * 
 */
@Entity
@Table(name = "greg_r_check")
@NamedQueries({
	@NamedQuery(name = "GregRCheck.findAll", query = "SELECT g FROM GregRCheck g where g.dataCancellazione IS NULL"),
	@NamedQuery(name = "GregRCheck.findMotivazioneByIdRendTab", query = "SELECT g FROM GregRCheck g where g.gregDTab.codTab=:codTab AND g.gregTCronologia.gregTRendicontazioneEnte.idRendicontazioneEnte=:idRendicontazione AND g.dataCancellazione IS NULL"),
})
public class GregRCheck implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "GREG_R_CHECK_IDCHECK_GENERATOR", sequenceName = "GREG_R_CHECK_ID_CHECK_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GREG_R_CHECK_IDCHECK_GENERATOR")
	@Column(name = "id_check")
	private Integer idCheck;

	@Column(name = "data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name = "data_creazione")
	private Timestamp dataCreazione;

	@Column(name = "data_modifica")
	private Timestamp dataModifica;
	
	@Column(name = "utente_operazione")
	private String utenteOperazione;

	// bi-directional many-to-one association to GregDTab
	@ManyToOne
	@JoinColumn(name = "id_tab")
	private GregDTab gregDTab;

	@OneToOne
	@JoinColumn(name = "id_cronologia")
	private GregTCronologia gregTCronologia;

	public GregRCheck() {
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

	public Integer getIdCheck() {
		return idCheck;
	}

	public void setIdCheck(Integer idCheck) {
		this.idCheck = idCheck;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public GregTCronologia getGregTCronologia() {
		return gregTCronologia;
	}

	public void setGregTCronologia(GregTCronologia gregTCronologia) {
		this.gregTCronologia = gregTCronologia;
	}

	public String getUtenteOperazione() {
		return utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

}