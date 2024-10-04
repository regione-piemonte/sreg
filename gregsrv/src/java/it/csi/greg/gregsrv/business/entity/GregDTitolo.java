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
 * The persistent class for the greg_d_titolo database table.
 * 
 */
@Entity
@Table(name="greg_d_titolo")
@NamedQuery(name="GregDTitolo.findAll", query="SELECT g FROM GregDTitolo g where g.dataCancellazione IS NULL")
public class GregDTitolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TITOLO_IDTITOLO_GENERATOR", sequenceName="GREG_D_TITOLO_ID_TITOLO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TITOLO_IDTITOLO_GENERATOR")
	@Column(name="id_titolo")
	private Integer idTitolo;

	@Column(name="altra_desc")
	private String altraDesc;

	@Column(name="cod_titolo")
	private String codTitolo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_titolo")
	private String desTitolo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDTitolo")
	private Set<GregRProgrammaMissioneTitSottotit> gregRProgrammaMissioneTitSottotits;

	public GregDTitolo() {
	}

	public Integer getIdTitolo() {
		return this.idTitolo;
	}

	public void setIdTitolo(Integer idTitolo) {
		this.idTitolo = idTitolo;
	}

	public String getAltraDesc() {
		return this.altraDesc;
	}

	public void setAltraDesc(String altraDesc) {
		this.altraDesc = altraDesc;
	}

	public String getCodTitolo() {
		return this.codTitolo;
	}

	public void setCodTitolo(String codTitolo) {
		this.codTitolo = codTitolo;
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

	public String getDesTitolo() {
		return this.desTitolo;
	}

	public void setDesTitolo(String desTitolo) {
		this.desTitolo = desTitolo;
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
		gregRProgrammaMissioneTitSottotit.setGregDTitolo(this);

		return gregRProgrammaMissioneTitSottotit;
	}

	public GregRProgrammaMissioneTitSottotit removeGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		getGregRProgrammaMissioneTitSottotits().remove(gregRProgrammaMissioneTitSottotit);
		gregRProgrammaMissioneTitSottotit.setGregDTitolo(null);

		return gregRProgrammaMissioneTitSottotit;
	}

}