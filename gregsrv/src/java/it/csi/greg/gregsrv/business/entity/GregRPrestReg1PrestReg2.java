/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_prest_reg1_prest_reg2 database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_prest_reg2")
@NamedQuery(name="GregRPrestReg1PrestReg2.findAll", query="SELECT g FROM GregRPrestReg1PrestReg2 g where g.dataCancellazione IS NULL")
public class GregRPrestReg1PrestReg2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_PREST_REG2_IDPRESTREG1PRESTREG2_GENERATOR", sequenceName="GREG_R_PREST_REG1_PREST_REG2_ID_PREST_REG1_PREST_REG2_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_PREST_REG2_IDPRESTREG1PRESTREG2_GENERATOR")
	@Column(name="id_prest_reg1_prest_reg2")
	private Integer idPrestReg1PrestReg2;

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

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregTPrestazioniRegionali2
	@ManyToOne
	@JoinColumn(name="id_prest_reg_2")
	private GregTPrestazioniRegionali2 gregTPrestazioniRegionali2;

	public GregRPrestReg1PrestReg2() {
	}

	public Integer getIdPrestReg1PrestReg2() {
		return this.idPrestReg1PrestReg2;
	}

	public void setIdPrestReg1PrestReg2(Integer idPrestReg1PrestReg2) {
		this.idPrestReg1PrestReg2 = idPrestReg1PrestReg2;
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

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public GregTPrestazioniRegionali2 getGregTPrestazioniRegionali2() {
		return this.gregTPrestazioniRegionali2;
	}

	public void setGregTPrestazioniRegionali2(GregTPrestazioniRegionali2 gregTPrestazioniRegionali2) {
		this.gregTPrestazioniRegionali2 = gregTPrestazioniRegionali2;
	}

}