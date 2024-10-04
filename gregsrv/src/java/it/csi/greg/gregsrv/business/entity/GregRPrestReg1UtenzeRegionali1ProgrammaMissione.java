/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_prest_reg1_utenze_regionali1_programma_missione database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_utenze_regionali1_programma_missione")
@NamedQuery(name="GregRPrestReg1UtenzeRegionali1ProgrammaMissione.findAll", query="SELECT g FROM GregRPrestReg1UtenzeRegionali1ProgrammaMissione g where g.dataCancellazione IS NULL")
public class GregRPrestReg1UtenzeRegionali1ProgrammaMissione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_UTENZE_REGIONALI1_PROGRAMMA_MISSIONE_IDPRESTREG1UTENZEREGIONALI1PROGRAMMAMISSIONE_GENERATOR", sequenceName="GREG_R_PREST_REG1_UTENZE_REGI_ID_PREST_REG1_UTENZE_REGIONAL_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_UTENZE_REGIONALI1_PROGRAMMA_MISSIONE_IDPRESTREG1UTENZEREGIONALI1PROGRAMMAMISSIONE_GENERATOR")
	@Column(name="id_prest_reg1_utenze_regionali1_programma_missione")
	private Integer idPrestReg1UtenzeRegionali1ProgrammaMissione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	//bi-directional many-to-one association to GregDProgrammaMissione
	@ManyToOne
	@JoinColumn(name="id_programma_missione")
	private GregDProgrammaMissione gregDProgrammaMissione;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_utenza_regionale1")
	private GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1;

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione() {
	}

	public Integer getIdPrestReg1UtenzeRegionali1ProgrammaMissione() {
		return this.idPrestReg1UtenzeRegionali1ProgrammaMissione;
	}

	public void setIdPrestReg1UtenzeRegionali1ProgrammaMissione(Integer idPrestReg1UtenzeRegionali1ProgrammaMissione) {
		this.idPrestReg1UtenzeRegionali1ProgrammaMissione = idPrestReg1UtenzeRegionali1ProgrammaMissione;
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

	public GregDProgrammaMissione getGregDProgrammaMissione() {
		return this.gregDProgrammaMissione;
	}

	public void setGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		this.gregDProgrammaMissione = gregDProgrammaMissione;
	}

	public GregRPrestReg1UtenzeRegionali1 getGregRPrestReg1UtenzeRegionali1() {
		return this.gregRPrestReg1UtenzeRegionali1;
	}

	public void setGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		this.gregRPrestReg1UtenzeRegionali1 = gregRPrestReg1UtenzeRegionali1;
	}

}