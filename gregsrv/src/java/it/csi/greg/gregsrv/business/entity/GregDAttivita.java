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
 * The persistent class for the greg_d_attivita database table.
 * 
 */
@Entity
@Table(name="greg_d_attivita")
@NamedQuery(name="GregDAttivita.findAll", query="SELECT g FROM GregDAttivita g where g.dataCancellazione IS NULL")
public class GregDAttivita implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_ATTIVITA_IDATTIVITA_GENERATOR", sequenceName="GREG_D_ATTIVITA_ID_ATTIVITA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_ATTIVITA_IDATTIVITA_GENERATOR")
	@Column(name="id_attivita")
	private Integer idAttivita;

	@Column(name="cod_attivita")
	private String codAttivita;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_attivita")
	private String descAttivita;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDFunzione
	@ManyToOne
	@JoinColumn(name="id_funzione")
	private GregDFunzione gregDFunzione;

	//bi-directional many-to-one association to GregRAttivitaProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDAttivita")
	private Set<GregRAttivitaProgrammaMissioneTitSottotit> gregRAttivitaProgrammaMissioneTitSottotits;

	public GregDAttivita() {
	}

	public Integer getIdAttivita() {
		return this.idAttivita;
	}

	public void setIdAttivita(Integer idAttivita) {
		this.idAttivita = idAttivita;
	}

	public String getCodAttivita() {
		return this.codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
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

	public String getDescAttivita() {
		return this.descAttivita;
	}

	public void setDescAttivita(String descAttivita) {
		this.descAttivita = descAttivita;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDFunzione getGregDFunzione() {
		return this.gregDFunzione;
	}

	public void setGregDFunzione(GregDFunzione gregDFunzione) {
		this.gregDFunzione = gregDFunzione;
	}

	public Set<GregRAttivitaProgrammaMissioneTitSottotit> getGregRAttivitaProgrammaMissioneTitSottotits() {
		return this.gregRAttivitaProgrammaMissioneTitSottotits;
	}

	public void setGregRAttivitaProgrammaMissioneTitSottotits(Set<GregRAttivitaProgrammaMissioneTitSottotit> gregRAttivitaProgrammaMissioneTitSottotits) {
		this.gregRAttivitaProgrammaMissioneTitSottotits = gregRAttivitaProgrammaMissioneTitSottotits;
	}

	public GregRAttivitaProgrammaMissioneTitSottotit addGregRAttivitaProgrammaMissioneTitSottotit(GregRAttivitaProgrammaMissioneTitSottotit gregRAttivitaProgrammaMissioneTitSottotit) {
		getGregRAttivitaProgrammaMissioneTitSottotits().add(gregRAttivitaProgrammaMissioneTitSottotit);
		gregRAttivitaProgrammaMissioneTitSottotit.setGregDAttivita(this);

		return gregRAttivitaProgrammaMissioneTitSottotit;
	}

	public GregRAttivitaProgrammaMissioneTitSottotit removeGregRAttivitaProgrammaMissioneTitSottotit(GregRAttivitaProgrammaMissioneTitSottotit gregRAttivitaProgrammaMissioneTitSottotit) {
		getGregRAttivitaProgrammaMissioneTitSottotits().remove(gregRAttivitaProgrammaMissioneTitSottotit);
		gregRAttivitaProgrammaMissioneTitSottotit.setGregDAttivita(null);

		return gregRAttivitaProgrammaMissioneTitSottotit;
	}

}