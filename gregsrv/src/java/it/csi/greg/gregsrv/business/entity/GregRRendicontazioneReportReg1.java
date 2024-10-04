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
 * The persistent class for the greg_r_rendicontazione_report_reg1 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_report_reg1")
@NamedQuery(name="GregRRendicontazioneReportReg1.findAll", query="SELECT g FROM GregRRendicontazioneReportReg1 g where g.dataCancellazione IS NULL")
public class GregRRendicontazioneReportReg1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_REPORT_REG1_IDRENDICONTAZIONEREPORTREG1_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_REPORT_ID_RENDICONTAZIONE_REPORT_REG_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_REPORT_REG1_IDRENDICONTAZIONEREPORTREG1_GENERATOR")
	@Column(name="id_rendicontazione_report_reg1")
	private Integer idRendicontazioneReportReg1;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	private BigDecimal valore;

	//bi-directional many-to-one association to GregRTipologiaSpesaPrestReg1
	@ManyToOne
	@JoinColumn(name="id_tipologia_spesa_prest_reg1")
	private GregRTipologiaSpesaPrestReg1 gregRTipologiaSpesaPrestReg1;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneReportReg1() {
	}

	public Integer getIdRendicontazioneReportReg1() {
		return this.idRendicontazioneReportReg1;
	}

	public void setIdRendicontazioneReportReg1(Integer idRendicontazioneReportReg1) {
		this.idRendicontazioneReportReg1 = idRendicontazioneReportReg1;
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

	public GregRTipologiaSpesaPrestReg1 getGregRTipologiaSpesaPrestReg1() {
		return this.gregRTipologiaSpesaPrestReg1;
	}

	public void setGregRTipologiaSpesaPrestReg1(GregRTipologiaSpesaPrestReg1 gregRTipologiaSpesaPrestReg1) {
		this.gregRTipologiaSpesaPrestReg1 = gregRTipologiaSpesaPrestReg1;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}