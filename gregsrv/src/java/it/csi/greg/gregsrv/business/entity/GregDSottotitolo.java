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
 * The persistent class for the greg_d_sottotitolo database table.
 * 
 */
@Entity
@Table(name="greg_d_sottotitolo")
@NamedQuery(name="GregDSottotitolo.findAll", query="SELECT g FROM GregDSottotitolo g where g.dataCancellazione IS NULL")
public class GregDSottotitolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_SOTTOTITOLO_IDSOTTOTITOLO_GENERATOR", sequenceName="GREG_D_SOTTOTITOLO_ID_SOTTOTITOLO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_SOTTOTITOLO_IDSOTTOTITOLO_GENERATOR")
	@Column(name="id_sottotitolo")
	private Integer idSottotitolo;

	@Column(name="cod_sottotitolo")
	private String codSottotitolo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_sottotitolo")
	private String desSottotitolo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottotitolo")
	private Set<GregRProgrammaMissioneTitSottotit> gregRProgrammaMissioneTitSottotits;

	public GregDSottotitolo() {
	}

	public Integer getIdSottotitolo() {
		return this.idSottotitolo;
	}

	public void setIdSottotitolo(Integer idSottotitolo) {
		this.idSottotitolo = idSottotitolo;
	}

	public String getCodSottotitolo() {
		return this.codSottotitolo;
	}

	public void setCodSottotitolo(String codSottotitolo) {
		this.codSottotitolo = codSottotitolo;
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

	public String getDesSottotitolo() {
		return this.desSottotitolo;
	}

	public void setDesSottotitolo(String desSottotitolo) {
		this.desSottotitolo = desSottotitolo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRProgrammaMissioneTitSottotit> getGregRProgrammaMissioneTitSottotits() {
		return this.gregRProgrammaMissioneTitSottotits;
	}

	public void setGregRProgrammaMissioneTitSottotits(Set<GregRProgrammaMissioneTitSottotit> gregRProgrammaMissioneTitSottotits) {
		this.gregRProgrammaMissioneTitSottotits = gregRProgrammaMissioneTitSottotits;
	}

	public GregRProgrammaMissioneTitSottotit addGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		getGregRProgrammaMissioneTitSottotits().add(gregRProgrammaMissioneTitSottotit);
		gregRProgrammaMissioneTitSottotit.setGregDSottotitolo(this);

		return gregRProgrammaMissioneTitSottotit;
	}

	public GregRProgrammaMissioneTitSottotit removeGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		getGregRProgrammaMissioneTitSottotits().remove(gregRProgrammaMissioneTitSottotit);
		gregRProgrammaMissioneTitSottotit.setGregDSottotitolo(null);

		return gregRProgrammaMissioneTitSottotit;
	}

}