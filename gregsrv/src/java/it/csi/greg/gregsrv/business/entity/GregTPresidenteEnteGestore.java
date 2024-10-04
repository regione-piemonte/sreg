/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_t_presidente_ente_gestore database table.
 * 
 */
@Entity
@Table(name="greg_t_presidente_ente_gestore")
@NamedQuery(name="GregTPresidenteEnteGestore.findAll", query="SELECT g FROM GregTPresidenteEnteGestore g")
public class GregTPresidenteEnteGestore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_PRESIDENTE_ENTE_GESTORE_IDPRESIDENTEENTEGESTORE_GENERATOR", sequenceName="GREG_T_PRESIDENTE_ENTE_GESTORE_ID_PRESIDENTE_ENTE_GESTORE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_PRESIDENTE_ENTE_GESTORE_IDPRESIDENTEENTEGESTORE_GENERATOR")
	@Column(name="id_presidente_ente_gestore")
	private Integer idPresidenteEnteGestore;

	@Column(name="cod_presidente_ente_gestore")
	private String codPresidenteEnteGestore;

	@Column(name="codice_fiscale")
	private String codiceFiscale;

	private String cognome;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String nome;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregREnteGestoreContatti
	@OneToMany(mappedBy="gregTPresidenteEnteGestore")
	private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;

	public GregTPresidenteEnteGestore() {
	}

	public Integer getIdPresidenteEnteGestore() {
		return this.idPresidenteEnteGestore;
	}

	public void setIdPresidenteEnteGestore(Integer idPresidenteEnteGestore) {
		this.idPresidenteEnteGestore = idPresidenteEnteGestore;
	}

	public String getCodPresidenteEnteGestore() {
		return this.codPresidenteEnteGestore;
	}

	public void setCodPresidenteEnteGestore(String codPresidenteEnteGestore) {
		this.codPresidenteEnteGestore = codPresidenteEnteGestore;
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

	public Timestamp getDataCreazione() {
		return this.dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataFineValidita() {
		return this.dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Timestamp getDataInizioValidita() {
		return this.dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
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

	public Set<GregREnteGestoreContatti> getGregREnteGestoreContattis() {
		return this.gregREnteGestoreContattis;
	}

	public void setGregREnteGestoreContattis(Set<GregREnteGestoreContatti> gregREnteGestoreContattis) {
		this.gregREnteGestoreContattis = gregREnteGestoreContattis;
	}

	public GregREnteGestoreContatti addGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().add(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregTPresidenteEnteGestore(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregTPresidenteEnteGestore(null);

		return gregREnteGestoreContatti;
	}

}