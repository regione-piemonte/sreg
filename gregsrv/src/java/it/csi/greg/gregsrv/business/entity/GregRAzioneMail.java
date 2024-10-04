/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_azione_mail database table.
 * 
 */
@Entity
@Table(name="greg_r_azione_mail")
@NamedQueries({
	@NamedQuery(name="GregRAzioneMail.findAll", query="SELECT g FROM GregRAzioneMail g where g.dataCancellazione IS NULL")
})
public class GregRAzioneMail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_AZIONE_MAIL_IDAZIONEMAIL_GENERATOR", sequenceName="GREG_R_AZIONE_MAIL_ID_AZIONE_MAIL_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_AZIONE_MAIL_IDAZIONEMAIL_GENERATOR")
	@Column(name="id_azione_mail")
	private Integer idAzioneMail;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDAzione
	@ManyToOne
	@JoinColumn(name="id_azione")
	private GregDAzione gregDAzione;

	//bi-directional many-to-one association to GregDMail
	@ManyToOne
	@JoinColumn(name="id_mail")
	private GregDMail gregDMail;

	public GregRAzioneMail() {
	}

	public Integer getIdAzioneMail() {
		return this.idAzioneMail;
	}

	public void setIdAzioneMail(Integer idAzioneMail) {
		this.idAzioneMail = idAzioneMail;
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

	public GregDAzione getGregDAzione() {
		return this.gregDAzione;
	}

	public void setGregDAzione(GregDAzione gregDAzione) {
		this.gregDAzione = gregDAzione;
	}

	public GregDMail getGregDMail() {
		return this.gregDMail;
	}

	public void setGregDMail(GregDMail gregDMail) {
		this.gregDMail = gregDMail;
	}

}