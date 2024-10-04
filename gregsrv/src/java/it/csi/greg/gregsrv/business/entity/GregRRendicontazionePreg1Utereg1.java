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
 * The persistent class for the greg_r_rendicontazione_preg1_utereg1 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_preg1_utereg1")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazionePreg1Utereg1.findAll", query="SELECT g FROM GregRRendicontazionePreg1Utereg1 g"),
	@NamedQuery(name="GregRRendicontazionePreg1Utereg1.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazionePreg1Utereg1 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazionePreg1Utereg1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_PREG1_UTEREG1_IDRENDICONTAZIONEPREG1UTEREG1_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_PREG1__ID_RENDICONTAZIONE_PREG1_UTER_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_PREG1_UTEREG1_IDRENDICONTAZIONEPREG1UTEREG1_GENERATOR")
	@Column(name="id_rendicontazione_preg1_utereg1")
	private Integer idRendicontazionePreg1Utereg1;

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

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_utenza_regionale1")
	private GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazionePreg1Utereg1() {
	}

	public Integer getIdRendicontazionePreg1Utereg1() {
		return this.idRendicontazionePreg1Utereg1;
	}

	public void setIdRendicontazionePreg1Utereg1(Integer idRendicontazionePreg1Utereg1) {
		this.idRendicontazionePreg1Utereg1 = idRendicontazionePreg1Utereg1;
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

	public GregRPrestReg1UtenzeRegionali1 getGregRPrestReg1UtenzeRegionali1() {
		return this.gregRPrestReg1UtenzeRegionali1;
	}

	public void setGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		this.gregRPrestReg1UtenzeRegionali1 = gregRPrestReg1UtenzeRegionali1;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}