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
 * The persistent class for the greg_t_user database table.
 * 
 */
@Entity
@Table(name="greg_t_user")
@NamedQueries({
@NamedQuery(name="GregTUser.findAll", query="SELECT g FROM GregTUser g"),
@NamedQuery(name="GregTUser.findUtenteValido", query="SELECT g FROM GregTUser g where g.dataCancellazione is null and g.codiceFiscale = :codfiscale")
})
public class GregTUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_USER_IDUSER_GENERATOR", sequenceName="GREG_T_USER_ID_USER_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_USER_IDUSER_GENERATOR")
	@Column(name="id_user")
	private Integer idUser;

	@Column(name="codice_fiscale")
	private String codiceFiscale;

	private String cognome;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String email;

	private String nome;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRUserProfilo
	@JsonIgnore
	@OneToMany(mappedBy="gregTUser")
	private Set<GregRUserProfilo> gregRUserProfilos;

	public GregTUser() {
	}

	public Integer getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Set<GregRUserProfilo> getGregRUserProfilos() {
		return this.gregRUserProfilos;
	}

	public void setGregRUserProfilos(Set<GregRUserProfilo> gregRUserProfilos) {
		this.gregRUserProfilos = gregRUserProfilos;
	}

	public GregRUserProfilo addGregRUserProfilo(GregRUserProfilo gregRUserProfilo) {
		getGregRUserProfilos().add(gregRUserProfilo);
		gregRUserProfilo.setGregTUser(this);

		return gregRUserProfilo;
	}

	public GregRUserProfilo removeGregRUserProfilo(GregRUserProfilo gregRUserProfilo) {
		getGregRUserProfilos().remove(gregRUserProfilo);
		gregRUserProfilo.setGregTUser(null);

		return gregRUserProfilo;
	}

}