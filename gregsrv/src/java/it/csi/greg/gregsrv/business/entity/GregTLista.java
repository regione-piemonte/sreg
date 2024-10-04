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
 * The persistent class for the greg_t_lista database table.
 * 
 */
@Entity
@Table(name="greg_t_lista")
@NamedQueries({
	@NamedQuery(name="GregTLista.findAll", query="SELECT g FROM GregTLista g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTLista.findByCod", query="SELECT g FROM GregTLista g where g.codLista=:codLista AND g.dataCancellazione IS NULL"),
})
public class GregTLista implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_LISTA_IDLISTA_GENERATOR", sequenceName="GREG_T_LISTA_ID_LISTA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_LISTA_IDLISTA_GENERATOR")
	@Column(name="id_lista")
	private Integer idLista;

	@Column(name="cod_lista")
	private String codLista;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_lista")
	private String descLista;

	@Column(name="info_lista")
	private String infoLista;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRListaEntiGestori
	@JsonIgnore
	@OneToMany(mappedBy="gregTLista")
	private Set<GregRListaEntiGestori> gregRListaEntiGestoris;

	//bi-directional many-to-one association to GregRUserProfilo
	@JsonIgnore
	@OneToMany(mappedBy="gregTLista")
	private Set<GregRUserProfilo> gregRUserProfilos;

	public GregTLista() {
	}

	public Integer getIdLista() {
		return this.idLista;
	}

	public void setIdLista(Integer idLista) {
		this.idLista = idLista;
	}

	public String getCodLista() {
		return this.codLista;
	}

	public void setCodLista(String codLista) {
		this.codLista = codLista;
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

	public String getDescLista() {
		return this.descLista;
	}

	public void setDescLista(String descLista) {
		this.descLista = descLista;
	}

	public String getInfoLista() {
		return this.infoLista;
	}

	public void setInfoLista(String infoLista) {
		this.infoLista = infoLista;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRListaEntiGestori> getGregRListaEntiGestoris() {
		return this.gregRListaEntiGestoris;
	}

	public void setGregRListaEntiGestoris(Set<GregRListaEntiGestori> gregRListaEntiGestoris) {
		this.gregRListaEntiGestoris = gregRListaEntiGestoris;
	}

	public GregRListaEntiGestori addGregRListaEntiGestori(GregRListaEntiGestori gregRListaEntiGestori) {
		getGregRListaEntiGestoris().add(gregRListaEntiGestori);
		gregRListaEntiGestori.setGregTLista(this);

		return gregRListaEntiGestori;
	}

	public GregRListaEntiGestori removeGregRListaEntiGestori(GregRListaEntiGestori gregRListaEntiGestori) {
		getGregRListaEntiGestoris().remove(gregRListaEntiGestori);
		gregRListaEntiGestori.setGregTLista(null);

		return gregRListaEntiGestori;
	}

	public Set<GregRUserProfilo> getGregRUserProfilos() {
		return this.gregRUserProfilos;
	}

	public void setGregRUserProfilos(Set<GregRUserProfilo> gregRUserProfilos) {
		this.gregRUserProfilos = gregRUserProfilos;
	}

	public GregRUserProfilo addGregRUserProfilo(GregRUserProfilo gregRUserProfilo) {
		getGregRUserProfilos().add(gregRUserProfilo);
		gregRUserProfilo.setGregTLista(this);

		return gregRUserProfilo;
	}

	public GregRUserProfilo removeGregRUserProfilo(GregRUserProfilo gregRUserProfilo) {
		getGregRUserProfilos().remove(gregRUserProfilo);
		gregRUserProfilo.setGregTLista(null);

		return gregRUserProfilo;
	}

}