/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_prest_reg1_prest_minist database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_prest_minist")
@NamedQuery(name="GregRPrestReg1PrestMinist.findAll", query="SELECT g FROM GregRPrestReg1PrestMinist g where g.dataCancellazione IS NULL")
public class GregRPrestReg1PrestMinist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_PREST_MINIST_IDPRESTREG1PRESTMINIST_GENERATOR", sequenceName="GREG_R_PREST_REG1_PREST_MINIST_ID_PREST_REG1_PREST_MINIST_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_PREST_MINIST_IDPRESTREG1PRESTMINIST_GENERATOR")
	@Column(name="id_prest_reg1_prest_minist")
	private Integer idPrestReg1PrestMinist;

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

	//bi-directional many-to-one association to GregCRegola
	@ManyToOne
	@JoinColumn(name="id_regola")
	private GregCRegola gregCRegola;

	//bi-directional many-to-one association to GregTPrestazioniMinisteriali
	@ManyToOne
	@JoinColumn(name="id_prest_minist")
	private GregTPrestazioniMinisteriali gregTPrestazioniMinisteriali;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	public GregRPrestReg1PrestMinist() {
	}

	public Integer getIdPrestReg1PrestMinist() {
		return this.idPrestReg1PrestMinist;
	}

	public void setIdPrestReg1PrestMinist(Integer idPrestReg1PrestMinist) {
		this.idPrestReg1PrestMinist = idPrestReg1PrestMinist;
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

	public GregCRegola getGregCRegola() {
		return this.gregCRegola;
	}

	public void setGregCRegola(GregCRegola gregCRegola) {
		this.gregCRegola = gregCRegola;
	}

	public GregTPrestazioniMinisteriali getGregTPrestazioniMinisteriali() {
		return this.gregTPrestazioniMinisteriali;
	}

	public void setGregTPrestazioniMinisteriali(GregTPrestazioniMinisteriali gregTPrestazioniMinisteriali) {
		this.gregTPrestazioniMinisteriali = gregTPrestazioniMinisteriali;
	}

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

}