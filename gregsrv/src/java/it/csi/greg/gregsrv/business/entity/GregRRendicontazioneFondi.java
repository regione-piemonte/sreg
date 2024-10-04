/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_rendicontazione_fondi database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_fondi")
@NamedQuery(name="GregRRendicontazioneFondi.findAll", query="SELECT g FROM GregRRendicontazioneFondi g")
public class GregRRendicontazioneFondi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_FONDI_IDRENDICONTAZIONEFONDI_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_FONDI_ID_RENDICONTAZIONE_FONDI_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_FONDI_IDRENDICONTAZIONEFONDI_GENERATOR")
	@Column(name="id_rendicontazione_fondi")
	private Integer idRendicontazioneFondi;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	private BigDecimal valore;

	//bi-directional many-to-one association to GregCRegola
	@ManyToOne
	@JoinColumn(name="id_regola")
	private GregCRegola gregCRegola;

	//bi-directional many-to-one association to GregDFondi
	@ManyToOne
	@JoinColumn(name="id_fondo")
	private GregDFondi gregDFondi;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneFondi() {
	}

	public Integer getIdRendicontazioneFondi() {
		return this.idRendicontazioneFondi;
	}

	public void setIdRendicontazioneFondi(Integer idRendicontazioneFondi) {
		this.idRendicontazioneFondi = idRendicontazioneFondi;
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

	public BigDecimal getValore() {
		return this.valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public GregCRegola getGregCRegola() {
		return this.gregCRegola;
	}

	public void setGregCRegola(GregCRegola gregCRegola) {
		this.gregCRegola = gregCRegola;
	}

	public GregDFondi getGregDFondi() {
		return this.gregDFondi;
	}

	public void setGregDFondi(GregDFondi gregDFondi) {
		this.gregDFondi = gregDFondi;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}