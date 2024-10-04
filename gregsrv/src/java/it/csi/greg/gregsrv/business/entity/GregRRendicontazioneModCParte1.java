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
 * The persistent class for the greg_r_rendicontazione_mod_c_parte1 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_c_parte1")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneModCParte1.findAll", query="SELECT g FROM GregRRendicontazioneModCParte1 g"),
	@NamedQuery(name="GregRRendicontazioneModCParte1.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModCParte1 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneModCParte1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_C_PARTE1_IDRENDICONTAZIONEMODCPARTE1_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_C__ID_RENDICONTAZIONE_MOD_C_PART_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_C_PARTE1_IDRENDICONTAZIONEMODCPARTE1_GENERATOR")
	@Column(name="id_rendicontazione_mod_c_parte1")
	private Integer idRendicontazioneModCParte1;

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

	//bi-directional many-to-one association to GregRPrestReg1UtenzeModc
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_utenza_modc")
	private GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModCParte1() {
	}

	public Integer getIdRendicontazioneModCParte1() {
		return this.idRendicontazioneModCParte1;
	}

	public void setIdRendicontazioneModCParte1(Integer idRendicontazioneModCParte1) {
		this.idRendicontazioneModCParte1 = idRendicontazioneModCParte1;
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

	public GregRPrestReg1UtenzeModc getGregRPrestReg1UtenzeModc() {
		return this.gregRPrestReg1UtenzeModc;
	}

	public void setGregRPrestReg1UtenzeModc(GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc) {
		this.gregRPrestReg1UtenzeModc = gregRPrestReg1UtenzeModc;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}