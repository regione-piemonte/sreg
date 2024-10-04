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
 * The persistent class for the greg_d_azione database table.
 * 
 */
@Entity
@Table(name="greg_d_azione")
@NamedQuery(name="GregDAzione.findAll", query="SELECT g FROM GregDAzione g where g.dataCancellazione IS NULL")
public class GregDAzione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_AZIONE_IDAZIONE_GENERATOR", sequenceName="GREG_D_AZIONE_ID_AZIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_AZIONE_IDAZIONE_GENERATOR")
	@Column(name="id_azione")
	private Integer idAzione;

	@Column(name="cod_azione")
	private String codAzione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_azione")
	private String descAzione;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="modifica")
	private Boolean modifica;
	
	@Column(name="visualizza")
	private Boolean visualizza;
	
	@Column(name="visibile")
	private Boolean visibile;

	//bi-directional many-to-one association to GregRAzioneMail
	@JsonIgnore
	@OneToMany(mappedBy="gregDAzione")
	private Set<GregRAzioneMail> gregRAzioneMails;

	//bi-directional many-to-one association to GregRProfiloAzione
	@JsonIgnore
	@OneToMany(mappedBy="gregDAzione")
	private Set<GregRProfiloAzione> gregRProfiloAziones;

	public GregDAzione() {
	}

	public Integer getIdAzione() {
		return this.idAzione;
	}

	public void setIdAzione(Integer idAzione) {
		this.idAzione = idAzione;
	}

	public String getCodAzione() {
		return this.codAzione;
	}

	public void setCodAzione(String codAzione) {
		this.codAzione = codAzione;
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

	public String getDescAzione() {
		return this.descAzione;
	}

	public void setDescAzione(String descAzione) {
		this.descAzione = descAzione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Boolean getModifica() {
		return modifica;
	}

	public void setModifica(Boolean modifica) {
		this.modifica = modifica;
	}

	public Boolean getVisualizza() {
		return visualizza;
	}

	public void setVisualizza(Boolean visualizza) {
		this.visualizza = visualizza;
	}

	public Boolean getVisibile() {
		return visibile;
	}

	public void setVisibile(Boolean visibile) {
		this.visibile = visibile;
	}
	
	public Set<GregRAzioneMail> getGregRAzioneMails() {
		return this.gregRAzioneMails;
	}

	public void setGregRAzioneMails(Set<GregRAzioneMail> gregRAzioneMails) {
		this.gregRAzioneMails = gregRAzioneMails;
	}

	public GregRAzioneMail addGregRAzioneMail(GregRAzioneMail gregRAzioneMail) {
		getGregRAzioneMails().add(gregRAzioneMail);
		gregRAzioneMail.setGregDAzione(this);

		return gregRAzioneMail;
	}

	public GregRAzioneMail removeGregRAzioneMail(GregRAzioneMail gregRAzioneMail) {
		getGregRAzioneMails().remove(gregRAzioneMail);
		gregRAzioneMail.setGregDAzione(null);

		return gregRAzioneMail;
	}

	public Set<GregRProfiloAzione> getGregRProfiloAziones() {
		return this.gregRProfiloAziones;
	}

	public void setGregRProfiloAziones(Set<GregRProfiloAzione> gregRProfiloAziones) {
		this.gregRProfiloAziones = gregRProfiloAziones;
	}

	public GregRProfiloAzione addGregRProfiloAzione(GregRProfiloAzione gregRProfiloAzione) {
		getGregRProfiloAziones().add(gregRProfiloAzione);
		gregRProfiloAzione.setGregDAzione(this);

		return gregRProfiloAzione;
	}

	public GregRProfiloAzione removeGregRProfiloAzione(GregRProfiloAzione gregRProfiloAzione) {
		getGregRProfiloAziones().remove(gregRProfiloAzione);
		gregRProfiloAzione.setGregDAzione(null);

		return gregRProfiloAzione;
	}
	
}