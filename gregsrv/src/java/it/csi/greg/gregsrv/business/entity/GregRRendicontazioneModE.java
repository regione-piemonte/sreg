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
 * The persistent class for the greg_r_rendicontazione_mod_e database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_e")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneModE.findAll", query="SELECT g FROM GregRRendicontazioneModE g"),
	@NamedQuery(name="GregRRendicontazioneModE.findByIdAttIdComune", query="SELECT g FROM GregRRendicontazioneModE g WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendEnte AND g.gregDAttivitaSocioAssist.idAttivitaSocioAssist = :idAttivita AND g.gregDComuni.idComune = :idComune AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregRRendicontazioneModE.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModE g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneModE implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_E_IDRENDICONTAZIONEMODE_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_E_ID_RENDICONTAZIONE_MOD_E_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_E_IDRENDICONTAZIONEMODE_GENERATOR")
	@Column(name="id_rendicontazione_mod_e")
	private Integer idRendicontazioneModE;

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

	//bi-directional many-to-one association to GregDAttivitaSocioAssist
	@ManyToOne
	@JoinColumn(name="id_attivita_socio_assist")
	private GregDAttivitaSocioAssist gregDAttivitaSocioAssist;

	//bi-directional many-to-one association to GregDComuni
	@ManyToOne
	@JoinColumn(name="id_comune")
	private GregDComuni gregDComuni;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModE() {
	}

	public Integer getIdRendicontazioneModE() {
		return this.idRendicontazioneModE;
	}

	public void setIdRendicontazioneModE(Integer idRendicontazioneModE) {
		this.idRendicontazioneModE = idRendicontazioneModE;
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

	public GregDAttivitaSocioAssist getGregDAttivitaSocioAssist() {
		return this.gregDAttivitaSocioAssist;
	}

	public void setGregDAttivitaSocioAssist(GregDAttivitaSocioAssist gregDAttivitaSocioAssist) {
		this.gregDAttivitaSocioAssist = gregDAttivitaSocioAssist;
	}

	public GregDComuni getGregDComuni() {
		return this.gregDComuni;
	}

	public void setGregDComuni(GregDComuni gregDComuni) {
		this.gregDComuni = gregDComuni;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}