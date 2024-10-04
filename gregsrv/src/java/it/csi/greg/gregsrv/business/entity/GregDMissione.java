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
 * The persistent class for the greg_d_missione database table.
 * 
 */
@Entity
@Table(name="greg_d_missione")
@NamedQuery(name="GregDMissione.findAll", query="SELECT g FROM GregDMissione g where g.dataCancellazione IS NULL")
public class GregDMissione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_MISSIONE_IDMISSIONE_GENERATOR", sequenceName="GREG_D_MISSIONE_ID_MISSIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_MISSIONE_IDMISSIONE_GENERATOR")
	@Column(name="id_missione")
	private Integer idMissione;

	@Column(name="altra_desc")
	private String altraDesc;

	@Column(name="cod_missione")
	private String codMissione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_missione")
	private String desMissione;

	@Column(name="msg_informativo")
	private String msgInformativo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDProgrammaMissione
	@JsonIgnore
	@OneToMany(mappedBy="gregDMissione")
	private Set<GregDProgrammaMissione> gregDProgrammaMissiones;

	public GregDMissione() {
	}

	public Integer getIdMissione() {
		return this.idMissione;
	}

	public void setIdMissione(Integer idMissione) {
		this.idMissione = idMissione;
	}

	public String getAltraDesc() {
		return this.altraDesc;
	}

	public void setAltraDesc(String altraDesc) {
		this.altraDesc = altraDesc;
	}

	public String getCodMissione() {
		return this.codMissione;
	}

	public void setCodMissione(String codMissione) {
		this.codMissione = codMissione;
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

	public String getDesMissione() {
		return this.desMissione;
	}

	public void setDesMissione(String desMissione) {
		this.desMissione = desMissione;
	}

	public String getMsgInformativo() {
		return this.msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDProgrammaMissione> getGregDProgrammaMissiones() {
		return this.gregDProgrammaMissiones;
	}

	public void setGregDProgrammaMissiones(Set<GregDProgrammaMissione> gregDProgrammaMissiones) {
		this.gregDProgrammaMissiones = gregDProgrammaMissiones;
	}

	public GregDProgrammaMissione addGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		getGregDProgrammaMissiones().add(gregDProgrammaMissione);
		gregDProgrammaMissione.setGregDMissione(this);

		return gregDProgrammaMissione;
	}

	public GregDProgrammaMissione removeGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		getGregDProgrammaMissiones().remove(gregDProgrammaMissione);
		gregDProgrammaMissione.setGregDMissione(null);

		return gregDProgrammaMissione;
	}

}