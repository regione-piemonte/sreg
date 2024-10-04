/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_r_prest_reg2_utenze_regionali2 database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg2_utenze_regionali2")
@NamedQuery(name="GregRPrestReg2UtenzeRegionali2.findAll", query="SELECT g FROM GregRPrestReg2UtenzeRegionali2 g where g.dataCancellazione IS NULL")
public class GregRPrestReg2UtenzeRegionali2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG2_UTENZE_REGIONALI2_IDPRESTREG2UTENZAREGIONALE2_GENERATOR", sequenceName="GREG_R_PREST_REG2_UTENZE_REGI_ID_PREST_REG2_UTENZA_REGIONAL_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG2_UTENZE_REGIONALI2_IDPRESTREG2UTENZAREGIONALE2_GENERATOR")
	@Column(name="id_prest_reg2_utenza_regionale2")
	private Integer idPrestReg2UtenzaRegionale2;

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

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	//bi-directional many-to-one association to GregTPrestazioniRegionali2
	@ManyToOne
	@JoinColumn(name="id_prest_reg_2")
	private GregTPrestazioniRegionali2 gregTPrestazioniRegionali2;

	//bi-directional many-to-one association to GregRRendicontazionePreg2Utereg2
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg2UtenzeRegionali2")
	private Set<GregRRendicontazionePreg2Utereg2> gregRRendicontazionePreg2Utereg2s;

	public GregRPrestReg2UtenzeRegionali2() {
	}

	public Integer getIdPrestReg2UtenzaRegionale2() {
		return this.idPrestReg2UtenzaRegionale2;
	}

	public void setIdPrestReg2UtenzaRegionale2(Integer idPrestReg2UtenzaRegionale2) {
		this.idPrestReg2UtenzaRegionale2 = idPrestReg2UtenzaRegionale2;
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

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

	public GregTPrestazioniRegionali2 getGregTPrestazioniRegionali2() {
		return this.gregTPrestazioniRegionali2;
	}

	public void setGregTPrestazioniRegionali2(GregTPrestazioniRegionali2 gregTPrestazioniRegionali2) {
		this.gregTPrestazioniRegionali2 = gregTPrestazioniRegionali2;
	}

	public Set<GregRRendicontazionePreg2Utereg2> getGregRRendicontazionePreg2Utereg2s() {
		return this.gregRRendicontazionePreg2Utereg2s;
	}

	public void setGregRRendicontazionePreg2Utereg2s(Set<GregRRendicontazionePreg2Utereg2> gregRRendicontazionePreg2Utereg2s) {
		this.gregRRendicontazionePreg2Utereg2s = gregRRendicontazionePreg2Utereg2s;
	}

	public GregRRendicontazionePreg2Utereg2 addGregRRendicontazionePreg2Utereg2(GregRRendicontazionePreg2Utereg2 gregRRendicontazionePreg2Utereg2) {
		getGregRRendicontazionePreg2Utereg2s().add(gregRRendicontazionePreg2Utereg2);
		gregRRendicontazionePreg2Utereg2.setGregRPrestReg2UtenzeRegionali2(this);

		return gregRRendicontazionePreg2Utereg2;
	}

	public GregRRendicontazionePreg2Utereg2 removeGregRRendicontazionePreg2Utereg2(GregRRendicontazionePreg2Utereg2 gregRRendicontazionePreg2Utereg2) {
		getGregRRendicontazionePreg2Utereg2s().remove(gregRRendicontazionePreg2Utereg2);
		gregRRendicontazionePreg2Utereg2.setGregRPrestReg2UtenzeRegionali2(null);

		return gregRRendicontazionePreg2Utereg2;
	}

}