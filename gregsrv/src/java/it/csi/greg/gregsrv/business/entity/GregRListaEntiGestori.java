/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_lista_enti_gestori database table.
 * 
 */
@Entity
@Table(name="greg_r_lista_enti_gestori")
@NamedQuery(name="GregRListaEntiGestori.findAll", query="SELECT g FROM GregRListaEntiGestori g where g.dataCancellazione IS NULL")
public class GregRListaEntiGestori implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_LISTA_ENTI_GESTORI_IDLISTAENTIGESTORI_GENERATOR", sequenceName="GREG_R_LISTA_ENTI_GESTORI_ID_LISTA_ENTI_GESTORI_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_LISTA_ENTI_GESTORI_IDLISTAENTIGESTORI_GENERATOR")
	@Column(name="id_lista_enti_gestori")
	private Integer idListaEntiGestori;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTLista
	@ManyToOne
	@JoinColumn(name="id_lista")
	private GregTLista gregTLista;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_scheda_ente_gestore")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori;

	public GregRListaEntiGestori() {
	}

	public Integer getIdListaEntiGestori() {
		return this.idListaEntiGestori;
	}

	public void setIdListaEntiGestori(Integer idListaEntiGestori) {
		this.idListaEntiGestori = idListaEntiGestori;
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

	public GregTLista getGregTLista() {
		return this.gregTLista;
	}

	public void setGregTLista(GregTLista gregTLista) {
		this.gregTLista = gregTLista;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori() {
		return this.gregTSchedeEntiGestori;
	}

	public void setGregTSchedeEntiGestori(GregTSchedeEntiGestori gregTSchedeEntiGestori) {
		this.gregTSchedeEntiGestori = gregTSchedeEntiGestori;
	}

}