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
 * The persistent class for the greg_t_responsabile_ente_gestore database table.
 * 
 */
@Entity
@Table(name="greg_t_responsabile_ente_gestore")
@NamedQueries({
	@NamedQuery(name="GregTResponsabileEnteGestore.findAll", query="SELECT g FROM GregTResponsabileEnteGestore g"),
	@NamedQuery(name="GregTResponsabileEnteGestore.findById", query="SELECT g FROM GregTResponsabileEnteGestore g WHERE g.idResponsabileEnteGestore = :resId"),
	@NamedQuery(name="GregTResponsabileEnteGestore.findByIdNotDeleted", query="SELECT g FROM GregTResponsabileEnteGestore g WHERE g.idResponsabileEnteGestore = :resId AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTResponsabileEnteGestore.findByCodNotDeleted", query="SELECT g FROM GregTResponsabileEnteGestore g WHERE g.codiceFiscale = :codiceFiscale AND g.dataCancellazione IS NULL")
})

public class GregTResponsabileEnteGestore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_RESPONSABILE_ENTE_GESTORE_IDRESPONSABILEENTEGESTORE_GENERATOR", sequenceName="GREG_T_RESPONSABILE_ENTE_GESTO_ID_RESPONSABILE_ENTE_GESTORE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_RESPONSABILE_ENTE_GESTORE_IDRESPONSABILEENTEGESTORE_GENERATOR")
	@Column(name="id_responsabile_ente_gestore")
	private Integer idResponsabileEnteGestore;

	@Column(name="cod_responsabile_ente_gestore")
	private String codResponsabileEnteGestore;

	@Column(name="codice_fiscale")
	private String codiceFiscale;

	private String cognome;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String nome;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRResponsabileContatti
	@JsonIgnore
	@OneToMany(mappedBy="gregTResponsabileEnteGestore")
	private Set<GregRResponsabileContatti> gregRResponsabileContattis;

	public GregTResponsabileEnteGestore() {
	}

	public Integer getIdResponsabileEnteGestore() {
		return this.idResponsabileEnteGestore;
	}

	public void setIdResponsabileEnteGestore(Integer idResponsabileEnteGestore) {
		this.idResponsabileEnteGestore = idResponsabileEnteGestore;
	}

	public String getCodResponsabileEnteGestore() {
		return this.codResponsabileEnteGestore;
	}

	public void setCodResponsabileEnteGestore(String codResponsabileEnteGestore) {
		this.codResponsabileEnteGestore = codResponsabileEnteGestore;
	}

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
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

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRResponsabileContatti> getGregRResponsabileContattis() {
		return this.gregRResponsabileContattis;
	}

	public void setGregRResponsabileContattis(Set<GregRResponsabileContatti> gregRResponsabileContattis) {
		this.gregRResponsabileContattis = gregRResponsabileContattis;
	}

	public GregRResponsabileContatti addGregRResponsabileContatti(GregRResponsabileContatti gregRResponsabileContatti) {
		getGregRResponsabileContattis().add(gregRResponsabileContatti);
		gregRResponsabileContatti.setGregTResponsabileEnteGestore(this);

		return gregRResponsabileContatti;
	}

	public GregRResponsabileContatti removeGregRResponsabileContatti(GregRResponsabileContatti gregRResponsabileContatti) {
		getGregRResponsabileContattis().remove(gregRResponsabileContatti);
		gregRResponsabileContatti.setGregTResponsabileEnteGestore(null);

		return gregRResponsabileContatti;
	}

}