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
 * The persistent class for the greg_d_mail database table.
 * 
 */
@Entity
@Table(name="greg_d_mail")
@NamedQueries({
	@NamedQuery(name="GregDMail.findAll", query="SELECT g FROM GregDMail g"),
	@NamedQuery(name="GregDMail.findByCodNotDeleted", query="SELECT g FROM GregDMail g WHERE g.dataCancellazione IS NULL AND g.codMail = :codMail")
})
public class GregDMail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_MAIL_IDMAIL_GENERATOR", sequenceName="GREG_D_MAIL_ID_MAIL_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_MAIL_IDMAIL_GENERATOR")
	@Column(name="id_mail")
	private Integer idMail;

	@Column(name="cod_mail")
	private String codMail;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_mail")
	private String descMail;

	private String oggetto;

	private String testo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRAzioneMail
	@JsonIgnore
	@OneToMany(mappedBy="gregDMail")
	private Set<GregRAzioneMail> gregRAzioneMails;

	public GregDMail() {
	}

	public Integer getIdMail() {
		return this.idMail;
	}

	public void setIdMail(Integer idMail) {
		this.idMail = idMail;
	}

	public String getCodMail() {
		return this.codMail;
	}

	public void setCodMail(String codMail) {
		this.codMail = codMail;
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

	public String getDescMail() {
		return this.descMail;
	}

	public void setDescMail(String descMail) {
		this.descMail = descMail;
	}

	public String getOggetto() {
		return this.oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRAzioneMail> getGregRAzioneMails() {
		return this.gregRAzioneMails;
	}

	public void setGregRAzioneMails(Set<GregRAzioneMail> gregRAzioneMails) {
		this.gregRAzioneMails = gregRAzioneMails;
	}

	public GregRAzioneMail addGregRAzioneMail(GregRAzioneMail gregRAzioneMail) {
		getGregRAzioneMails().add(gregRAzioneMail);
		gregRAzioneMail.setGregDMail(this);

		return gregRAzioneMail;
	}

	public GregRAzioneMail removeGregRAzioneMail(GregRAzioneMail gregRAzioneMail) {
		getGregRAzioneMails().remove(gregRAzioneMail);
		gregRAzioneMail.setGregDMail(null);

		return gregRAzioneMail;
	}

}