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
 * The persistent class for the greg_r_rendicontazione_modulo_fnps database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_modulo_fnps")
@NamedQuery(name="GregRRendicontazioneModuloFnps.findAll", query="SELECT g FROM GregRRendicontazioneModuloFnps g where g.dataCancellazione IS NULL")
public class GregRRendicontazioneModuloFnps implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MODULO_FNPS_IDRENDICONTAZIONEMODULOFNPS_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MODULO_ID_RENDICONTAZIONE_MODULO_FNP_SEQ" , initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MODULO_FNPS_IDRENDICONTAZIONEMODULOFNPS_GENERATOR")
	@Column(name="id_rendicontazione_modulo_fnps")
	private Integer idRendicontazioneModuloFnps;

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

	//bi-directional many-to-one association to GregRPrestMinistUtenzeMinist
	@ManyToOne
	@JoinColumn(name="id_prest_minist_utenze_minist")
	private GregRPrestMinistUtenzeMinist gregRPrestMinistUtenzeMinist;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModuloFnps() {
	}

	public Integer getIdRendicontazioneModuloFnps() {
		return this.idRendicontazioneModuloFnps;
	}

	public void setIdRendicontazioneModuloFnps(Integer idRendicontazioneModuloFnps) {
		this.idRendicontazioneModuloFnps = idRendicontazioneModuloFnps;
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

	public GregRPrestMinistUtenzeMinist getGregRPrestMinistUtenzeMinist() {
		return this.gregRPrestMinistUtenzeMinist;
	}

	public void setGregRPrestMinistUtenzeMinist(GregRPrestMinistUtenzeMinist gregRPrestMinistUtenzeMinist) {
		this.gregRPrestMinistUtenzeMinist = gregRPrestMinistUtenzeMinist;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}