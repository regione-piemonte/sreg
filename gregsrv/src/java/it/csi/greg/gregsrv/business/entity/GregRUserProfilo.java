/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_user_profilo database table.
 * 
 */
@Entity
@Table(name="greg_r_user_profilo")
@NamedQuery(name="GregRUserProfilo.findAll", query="SELECT g FROM GregRUserProfilo g where g.dataCancellazione IS NULL")
public class GregRUserProfilo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_USER_PROFILO_IDUSERPROFILO_GENERATOR", sequenceName="GREG_R_USER_PROFILO_ID_USER_PROFILO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_USER_PROFILO_IDUSERPROFILO_GENERATOR")
	@Column(name="id_user_profilo")
	private Integer idUserProfilo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	//bi-directional many-to-one association to GregDProfilo
	@ManyToOne
	@JoinColumn(name="id_profilo")
	private GregDProfilo gregDProfilo;

	//bi-directional many-to-one association to GregTLista
	@ManyToOne
	@JoinColumn(name="id_lista")
	private GregTLista gregTLista;

	//bi-directional many-to-one association to GregTUser
	@ManyToOne
	@JoinColumn(name="id_user")
	private GregTUser gregTUser;

	public GregRUserProfilo() {
	}

	public Integer getIdUserProfilo() {
		return this.idUserProfilo;
	}

	public void setIdUserProfilo(Integer idUserProfilo) {
		this.idUserProfilo = idUserProfilo;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public Timestamp getDataCreazione() {
		return this.dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return this.dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDProfilo getGregDProfilo() {
		return this.gregDProfilo;
	}

	public void setGregDProfilo(GregDProfilo gregDProfilo) {
		this.gregDProfilo = gregDProfilo;
	}

	public GregTLista getGregTLista() {
		return this.gregTLista;
	}

	public void setGregTLista(GregTLista gregTLista) {
		this.gregTLista = gregTLista;
	}

	public GregTUser getGregTUser() {
		return this.gregTUser;
	}

	public void setGregTUser(GregTUser gregTUser) {
		this.gregTUser = gregTUser;
	}

	public Timestamp getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Timestamp getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

}