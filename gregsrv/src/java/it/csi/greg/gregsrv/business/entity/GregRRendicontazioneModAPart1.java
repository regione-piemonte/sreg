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
 * The persistent class for the greg_r_rendicontazione_mod_a_part1 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_a_part1")
@NamedQueries({
@NamedQuery(name="GregRRendicontazioneModAPart1.findAll", query="SELECT g FROM GregRRendicontazioneModAPart1 g"),
@NamedQuery(name="GregRRendicontazioneModAPart1.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModAPart1 g "
		+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
		+ "and g.dataCancellazione is null AND g.valoreNumb is not null and g.valoreNumb > 0")
})
public class GregRRendicontazioneModAPart1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_A_PART1_IDRENDICONTAZIONEMODAPART1_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_A__ID_RENDICONTAZIONE_MOD_A_PART_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_A_PART1_IDRENDICONTAZIONEMODAPART1_GENERATOR")
	@Column(name="id_rendicontazione_mod_a_part1")
	private Integer idRendicontazioneModAPart1;

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

	@Column(name="valore_numb")
	private BigDecimal valoreNumb;

	@Column(name="valore_text")
	private String valoreText;

	//bi-directional many-to-one association to GregRTitoloTipologiaVoceModA
	@ManyToOne
	@JoinColumn(name="id_titolo_tipologia_voce_mod_a")
	private GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModAPart1() {
	}

	public Integer getIdRendicontazioneModAPart1() {
		return this.idRendicontazioneModAPart1;
	}

	public void setIdRendicontazioneModAPart1(Integer idRendicontazioneModAPart1) {
		this.idRendicontazioneModAPart1 = idRendicontazioneModAPart1;
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

	public BigDecimal getValoreNumb() {
		return this.valoreNumb;
	}

	public void setValoreNumb(BigDecimal valoreNumb) {
		this.valoreNumb = valoreNumb;
	}

	public String getValoreText() {
		return this.valoreText;
	}

	public void setValoreText(String valoreText) {
		this.valoreText = valoreText;
	}

	public GregRTitoloTipologiaVoceModA getGregRTitoloTipologiaVoceModA() {
		return this.gregRTitoloTipologiaVoceModA;
	}

	public void setGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		this.gregRTitoloTipologiaVoceModA = gregRTitoloTipologiaVoceModA;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}