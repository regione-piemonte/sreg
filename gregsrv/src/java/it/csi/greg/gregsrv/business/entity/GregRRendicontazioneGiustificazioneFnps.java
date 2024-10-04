/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_rendicontazione_giustificazione_fnps database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_giustificazione_fnps")
@NamedQuery(name="GregRRendicontazioneGiustificazioneFnps.findAll", query="SELECT g FROM GregRRendicontazioneGiustificazioneFnps g where g.dataCancellazione IS NULL")
public class GregRRendicontazioneGiustificazioneFnps implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_GIUSTIFICAZIONE_FNPS_IDRENDICONTAZIONEGIUSTIFNPS_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_GIUSTI_ID_RENDICONTAZIONE_GIUSTIFICA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_GIUSTIFICAZIONE_FNPS_IDRENDICONTAZIONEGIUSTIFNPS_GENERATOR")
	@Column(name="id_rendicontazione_giustificazione_fnps")
	private Integer idRendicontazioneGiustificazioneFnps;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String gisutificativo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneGiustificazioneFnps() {
	}

	public Integer getIdRendicontazioneGiustificazioneFnps() {
		return this.idRendicontazioneGiustificazioneFnps;
	}

	public void setIdRendicontazioneGiustificazioneFnps(Integer idRendicontazioneGiustificazioneFnps) {
		this.idRendicontazioneGiustificazioneFnps = idRendicontazioneGiustificazioneFnps;
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

	public String getGisutificativo() {
		return this.gisutificativo;
	}

	public void setGisutificativo(String gisutificativo) {
		this.gisutificativo = gisutificativo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}