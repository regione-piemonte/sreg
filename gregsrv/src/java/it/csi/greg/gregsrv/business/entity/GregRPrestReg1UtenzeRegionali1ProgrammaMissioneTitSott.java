/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_prest_reg1_utenze_regionali1_programma_missione_tit_sott database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_utenze_regionali1_programma_missione_tit_sott")
@NamedQuery(name="GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott.findAll", query="SELECT g FROM GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott g where g.dataCancellazione IS NULL")
public class GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_UTENZE_REGIONALI1_PROGRAMMA_MISSIONE_TIT_SOTT_IDRPRESTREG1UTENZEREGIONALI1PROGRAMMAMISSIONETITSOTTOT_GENERATOR", sequenceName="GREG_R_PREST_REG1_UTENZE_REGI_ID_R_PREST_REG1_UTENZE_REGION_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_UTENZE_REGIONALI1_PROGRAMMA_MISSIONE_TIT_SOTT_IDRPRESTREG1UTENZEREGIONALI1PROGRAMMAMISSIONETITSOTTOT_GENERATOR")
	@Column(name="id_r_prest_reg1_utenze_regionali1_programma_missione_tit_sottot")
	private Integer idRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_utenza_regionale1")
	private GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1;

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@ManyToOne
	@JoinColumn(name="id_programma_missione_tit_sottotit")
	private GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit;

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott() {
	}

	public Integer getIdRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot() {
		return this.idRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot;
	}

	public void setIdRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot(Integer idRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot) {
		this.idRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot = idRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSottot;
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

	public GregRPrestReg1UtenzeRegionali1 getGregRPrestReg1UtenzeRegionali1() {
		return this.gregRPrestReg1UtenzeRegionali1;
	}

	public void setGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		this.gregRPrestReg1UtenzeRegionali1 = gregRPrestReg1UtenzeRegionali1;
	}

	public GregRProgrammaMissioneTitSottotit getGregRProgrammaMissioneTitSottotit() {
		return this.gregRProgrammaMissioneTitSottotit;
	}

	public void setGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		this.gregRProgrammaMissioneTitSottotit = gregRProgrammaMissioneTitSottotit;
	}

}