/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_nomenc_naz_prest_reg2 database table.
 * 
 */
@Entity
@Table(name="greg_r_nomenc_naz_prest_reg2")
@NamedQuery(name="GregRNomencNazPrestReg2.findAll", query="SELECT g FROM GregRNomencNazPrestReg2 g where g.dataCancellazione IS NULL")
public class GregRNomencNazPrestReg2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_NOMENC_NAZ_PREST_REG2_IDPRESTREG1MACROAGGREGATIBILANCIO_GENERATOR", sequenceName="GREG_R_NOMENC_NAZ_PREST_REG2_ID_PREST_REG1_MACROAGGREGATI_B_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_NOMENC_NAZ_PREST_REG2_IDPRESTREG1MACROAGGREGATIBILANCIO_GENERATOR")
	@Column(name="id_prest_reg1_macroaggregati_bilancio")
	private Integer idPrestReg1MacroaggregatiBilancio;

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

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@ManyToOne
	@JoinColumn(name="id_nomenclatore_nazionale")
	private GregTNomenclatoreNazionale gregTNomenclatoreNazionale;

	//bi-directional many-to-one association to GregTPrestazioniRegionali2
	@ManyToOne
	@JoinColumn(name="id_prest_reg_2")
	private GregTPrestazioniRegionali2 gregTPrestazioniRegionali2;

	public GregRNomencNazPrestReg2() {
	}

	public Integer getIdPrestReg1MacroaggregatiBilancio() {
		return this.idPrestReg1MacroaggregatiBilancio;
	}

	public void setIdPrestReg1MacroaggregatiBilancio(Integer idPrestReg1MacroaggregatiBilancio) {
		this.idPrestReg1MacroaggregatiBilancio = idPrestReg1MacroaggregatiBilancio;
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

	public GregTNomenclatoreNazionale getGregTNomenclatoreNazionale() {
		return this.gregTNomenclatoreNazionale;
	}

	public void setGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		this.gregTNomenclatoreNazionale = gregTNomenclatoreNazionale;
	}

	public GregTPrestazioniRegionali2 getGregTPrestazioniRegionali2() {
		return this.gregTPrestazioniRegionali2;
	}

	public void setGregTPrestazioniRegionali2(GregTPrestazioniRegionali2 gregTPrestazioniRegionali2) {
		this.gregTPrestazioniRegionali2 = gregTPrestazioniRegionali2;
	}

}