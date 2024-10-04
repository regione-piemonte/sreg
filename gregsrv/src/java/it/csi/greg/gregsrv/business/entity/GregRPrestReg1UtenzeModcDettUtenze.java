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
 * The persistent class for the greg_r_prest_reg1_utenze_modc_dett_utenze database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_utenze_modc_dett_utenze")
@NamedQuery(name="GregRPrestReg1UtenzeModcDettUtenze.findAll", query="SELECT g FROM GregRPrestReg1UtenzeModcDettUtenze g where g.dataCancellazione IS NULL")
public class GregRPrestReg1UtenzeModcDettUtenze implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_UTENZE_MODC_DETT_UTENZE_IDPRESTREG1UTENZEMODCDETTUTENZE_GENERATOR", sequenceName="GREG_R_PREST_REG1_UTENZE_MODC_DETT_UTENZE_ID_PREST_REG1_UTENZE_MODC_DETT_UTENZE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_UTENZE_MODC_DETT_UTENZE_IDPRESTREG1UTENZEMODCDETTUTENZE_GENERATOR")
	@Column(name="id_prest_reg1_utenze_modc_dett_utenze")
	private Integer idPrestReg1UtenzeModcDettUtenze;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDDettaglioUtenze
	@ManyToOne
	@JoinColumn(name="id_dettaglio_utenza")
	private GregDDettaglioUtenze gregDDettaglioUtenze;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeModc
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_utenza_modc")
	private GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte2
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeModcDettUtenze")
	private Set<GregRRendicontazioneModCParte2> gregRRendicontazioneModCParte2s;

	public GregRPrestReg1UtenzeModcDettUtenze() {
	}

	public Integer getIdPrestReg1UtenzeModcDettUtenze() {
		return this.idPrestReg1UtenzeModcDettUtenze;
	}

	public void setIdPrestReg1UtenzeModcDettUtenze(Integer idPrestReg1UtenzeModcDettUtenze) {
		this.idPrestReg1UtenzeModcDettUtenze = idPrestReg1UtenzeModcDettUtenze;
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

	public GregDDettaglioUtenze getGregDDettaglioUtenze() {
		return this.gregDDettaglioUtenze;
	}

	public void setGregDDettaglioUtenze(GregDDettaglioUtenze gregDDettaglioUtenze) {
		this.gregDDettaglioUtenze = gregDDettaglioUtenze;
	}

	public GregRPrestReg1UtenzeModc getGregRPrestReg1UtenzeModc() {
		return this.gregRPrestReg1UtenzeModc;
	}

	public void setGregRPrestReg1UtenzeModc(GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc) {
		this.gregRPrestReg1UtenzeModc = gregRPrestReg1UtenzeModc;
	}

	public Set<GregRRendicontazioneModCParte2> getGregRRendicontazioneModCParte2s() {
		return this.gregRRendicontazioneModCParte2s;
	}

	public void setGregRRendicontazioneModCParte2s(Set<GregRRendicontazioneModCParte2> gregRRendicontazioneModCParte2s) {
		this.gregRRendicontazioneModCParte2s = gregRRendicontazioneModCParte2s;
	}

	public GregRRendicontazioneModCParte2 addGregRRendicontazioneModCParte2(GregRRendicontazioneModCParte2 gregRRendicontazioneModCParte2) {
		getGregRRendicontazioneModCParte2s().add(gregRRendicontazioneModCParte2);
		gregRRendicontazioneModCParte2.setGregRPrestReg1UtenzeModcDettUtenze(this);

		return gregRRendicontazioneModCParte2;
	}

	public GregRRendicontazioneModCParte2 removeGregRRendicontazioneModCParte2(GregRRendicontazioneModCParte2 gregRRendicontazioneModCParte2) {
		getGregRRendicontazioneModCParte2s().remove(gregRRendicontazioneModCParte2);
		gregRRendicontazioneModCParte2.setGregRPrestReg1UtenzeModcDettUtenze(null);

		return gregRRendicontazioneModCParte2;
	}

}