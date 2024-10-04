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
 * The persistent class for the greg_r_rendicontazione_preg2_utereg2 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_preg2_utereg2")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazionePreg2Utereg2.findAll", query="SELECT g FROM GregRRendicontazionePreg2Utereg2 g"),
	@NamedQuery(name="GregRRendicontazionePreg2Utereg2.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazionePreg2Utereg2 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazionePreg2Utereg2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_PREG2_UTEREG2_IDRENDICONTAZIONEPREG2UTEREG2_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_PREG2__ID_RENDICONTAZIONE_PREG2_UTER_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_PREG2_UTEREG2_IDRENDICONTAZIONEPREG2UTEREG2_GENERATOR")
	@Column(name="id_rendicontazione_preg2_utereg2")
	private Integer idRendicontazionePreg2Utereg2;

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

	//bi-directional many-to-one association to GregRPrestReg2UtenzeRegionali2
	@ManyToOne
	@JoinColumn(name="id_prest_reg2_utenza_regionale2")
	private GregRPrestReg2UtenzeRegionali2 gregRPrestReg2UtenzeRegionali2;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazionePreg2Utereg2() {
	}

	public Integer getIdRendicontazionePreg2Utereg2() {
		return this.idRendicontazionePreg2Utereg2;
	}

	public void setIdRendicontazionePreg2Utereg2(Integer idRendicontazionePreg2Utereg2) {
		this.idRendicontazionePreg2Utereg2 = idRendicontazionePreg2Utereg2;
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

	public GregRPrestReg2UtenzeRegionali2 getGregRPrestReg2UtenzeRegionali2() {
		return this.gregRPrestReg2UtenzeRegionali2;
	}

	public void setGregRPrestReg2UtenzeRegionali2(GregRPrestReg2UtenzeRegionali2 gregRPrestReg2UtenzeRegionali2) {
		this.gregRPrestReg2UtenzeRegionali2 = gregRPrestReg2UtenzeRegionali2;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}