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
 * The persistent class for the greg_r_rendicontazione_mod_a1 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_a1")
@NamedQueries({
@NamedQuery(name="GregRRendicontazioneModA1.findAll", query="SELECT g FROM GregRRendicontazioneModA1 g"),
@NamedQuery(name="GregRRendicontazioneModA1.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModA1 g "
		+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
		+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneModA1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_A1_IDRENDICONTAZIONEMODA1_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_A1_ID_RENDICONTAZIONE_MOD_A1_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_A1_IDRENDICONTAZIONEMODA1_GENERATOR")
	@Column(name="id_rendicontazione_mod_a1")
	private Integer idRendicontazioneModA1;

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

	//bi-directional many-to-one association to GregDComuni
	@ManyToOne
	@JoinColumn(name="id_comune")
	private GregDComuni gregDComuni;

	//bi-directional many-to-one association to GregDVoceModA1
	@ManyToOne
	@JoinColumn(name="id_voce_mod_a1")
	private GregDVoceModA1 gregDVoceModA1;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModA1() {
	}

	public Integer getIdRendicontazioneModA1() {
		return this.idRendicontazioneModA1;
	}

	public void setIdRendicontazioneModA1(Integer idRendicontazioneModA1) {
		this.idRendicontazioneModA1 = idRendicontazioneModA1;
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

	public GregDComuni getGregDComuni() {
		return this.gregDComuni;
	}

	public void setGregDComuni(GregDComuni gregDComuni) {
		this.gregDComuni = gregDComuni;
	}

	public GregDVoceModA1 getGregDVoceModA1() {
		return this.gregDVoceModA1;
	}

	public void setGregDVoceModA1(GregDVoceModA1 gregDVoceModA1) {
		this.gregDVoceModA1 = gregDVoceModA1;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}