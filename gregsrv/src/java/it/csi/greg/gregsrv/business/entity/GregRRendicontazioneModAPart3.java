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
 * The persistent class for the greg_r_rendicontazione_mod_a_part3 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_a_part3")
@NamedQueries({
@NamedQuery(name="GregRRendicontazioneModAPart3.findAll", query="SELECT g FROM GregRRendicontazioneModAPart3 g"),
@NamedQuery(name="GregRRendicontazioneModAPart3.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModAPart3 g "
		+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
		+ "and g.dataCancellazione is null")
})
public class GregRRendicontazioneModAPart3 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_A_PART3_IDRENDICONTAZIONEMODAPART3_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_A__ID_RENDICONTAZIONE_MOD_A_PAR_SEQ2", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_A_PART3_IDRENDICONTAZIONEMODAPART3_GENERATOR")
	@Column(name="id_rendicontazione_mod_a_part3")
	private Integer idRendicontazioneModAPart3;

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

	//bi-directional many-to-one association to GregDVoceModA
	@ManyToOne
	@JoinColumn(name="id_voce_mod_a")
	private GregDVoceModA gregDVoceModA;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModAPart3() {
	}

	public Integer getIdRendicontazioneModAPart3() {
		return this.idRendicontazioneModAPart3;
	}

	public void setIdRendicontazioneModAPart3(Integer idRendicontazioneModAPart3) {
		this.idRendicontazioneModAPart3 = idRendicontazioneModAPart3;
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

	public GregDVoceModA getGregDVoceModA() {
		return this.gregDVoceModA;
	}

	public void setGregDVoceModA(GregDVoceModA gregDVoceModA) {
		this.gregDVoceModA = gregDVoceModA;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}