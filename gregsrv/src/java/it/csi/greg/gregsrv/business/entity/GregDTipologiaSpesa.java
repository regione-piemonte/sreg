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
 * The persistent class for the greg_d_tipologia_spesa database table.
 * 
 */
@Entity
@Table(name="greg_d_tipologia_spesa")
@NamedQueries({
	@NamedQuery(name="GregDTipologiaSpesa.findAll", query="SELECT g FROM GregDTipologiaSpesa g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDTipologiaSpesa.findByCodSpesaNotDeleted", query="SELECT g FROM GregDTipologiaSpesa g WHERE g.codTipologiaSpesa= :codSpesa AND g.dataCancellazione IS NULL")
})

public class GregDTipologiaSpesa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPOLOGIA_SPESA_IDTIPOLOGIASPESA_GENERATOR", sequenceName="GREG_D_TIPOLOGIA_SPESA_ID_TIPOLOGIA_SPESA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPOLOGIA_SPESA_IDTIPOLOGIASPESA_GENERATOR")
	@Column(name="id_tipologia_spesa")
	private Integer idTipologiaSpesa;

	@Column(name="cod_tipologia_spesa")
	private String codTipologiaSpesa;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_tipologia_spesa")
	private String desTipologiaSpesa;

	@Column(name="regole_algoritmi")
	private String regoleAlgoritmi;

	private String report;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	@Column(name="valori_dal_modello")
	private String valoriDalModello;

	//bi-directional many-to-one association to GregRTipologiaSpesaPrestReg1
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologiaSpesa")
	private Set<GregRTipologiaSpesaPrestReg1> gregRTipologiaSpesaPrestReg1s;

	//bi-directional many-to-one association to GregRTipologiaSpesaProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologiaSpesa")
	private Set<GregRTipologiaSpesaProgrammaMissioneTitSottotit> gregRTipologiaSpesaProgrammaMissioneTitSottotits;

	public GregDTipologiaSpesa() {
	}

	public Integer getIdTipologiaSpesa() {
		return this.idTipologiaSpesa;
	}

	public void setIdTipologiaSpesa(Integer idTipologiaSpesa) {
		this.idTipologiaSpesa = idTipologiaSpesa;
	}

	public String getCodTipologiaSpesa() {
		return this.codTipologiaSpesa;
	}

	public void setCodTipologiaSpesa(String codTipologiaSpesa) {
		this.codTipologiaSpesa = codTipologiaSpesa;
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

	public String getDesTipologiaSpesa() {
		return this.desTipologiaSpesa;
	}

	public void setDesTipologiaSpesa(String desTipologiaSpesa) {
		this.desTipologiaSpesa = desTipologiaSpesa;
	}

	public String getRegoleAlgoritmi() {
		return this.regoleAlgoritmi;
	}

	public void setRegoleAlgoritmi(String regoleAlgoritmi) {
		this.regoleAlgoritmi = regoleAlgoritmi;
	}

	public String getReport() {
		return this.report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public String getValoriDalModello() {
		return this.valoriDalModello;
	}

	public void setValoriDalModello(String valoriDalModello) {
		this.valoriDalModello = valoriDalModello;
	}

	public Set<GregRTipologiaSpesaPrestReg1> getGregRTipologiaSpesaPrestReg1s() {
		return this.gregRTipologiaSpesaPrestReg1s;
	}

	public void setGregRTipologiaSpesaPrestReg1s(Set<GregRTipologiaSpesaPrestReg1> gregRTipologiaSpesaPrestReg1s) {
		this.gregRTipologiaSpesaPrestReg1s = gregRTipologiaSpesaPrestReg1s;
	}

	public GregRTipologiaSpesaPrestReg1 addGregRTipologiaSpesaPrestReg1(GregRTipologiaSpesaPrestReg1 gregRTipologiaSpesaPrestReg1) {
		getGregRTipologiaSpesaPrestReg1s().add(gregRTipologiaSpesaPrestReg1);
		gregRTipologiaSpesaPrestReg1.setGregDTipologiaSpesa(this);

		return gregRTipologiaSpesaPrestReg1;
	}

	public GregRTipologiaSpesaPrestReg1 removeGregRTipologiaSpesaPrestReg1(GregRTipologiaSpesaPrestReg1 gregRTipologiaSpesaPrestReg1) {
		getGregRTipologiaSpesaPrestReg1s().remove(gregRTipologiaSpesaPrestReg1);
		gregRTipologiaSpesaPrestReg1.setGregDTipologiaSpesa(null);

		return gregRTipologiaSpesaPrestReg1;
	}

	public Set<GregRTipologiaSpesaProgrammaMissioneTitSottotit> getGregRTipologiaSpesaProgrammaMissioneTitSottotits() {
		return this.gregRTipologiaSpesaProgrammaMissioneTitSottotits;
	}

	public void setGregRTipologiaSpesaProgrammaMissioneTitSottotits(Set<GregRTipologiaSpesaProgrammaMissioneTitSottotit> gregRTipologiaSpesaProgrammaMissioneTitSottotits) {
		this.gregRTipologiaSpesaProgrammaMissioneTitSottotits = gregRTipologiaSpesaProgrammaMissioneTitSottotits;
	}

	public GregRTipologiaSpesaProgrammaMissioneTitSottotit addGregRTipologiaSpesaProgrammaMissioneTitSottotit(GregRTipologiaSpesaProgrammaMissioneTitSottotit gregRTipologiaSpesaProgrammaMissioneTitSottotit) {
		getGregRTipologiaSpesaProgrammaMissioneTitSottotits().add(gregRTipologiaSpesaProgrammaMissioneTitSottotit);
		gregRTipologiaSpesaProgrammaMissioneTitSottotit.setGregDTipologiaSpesa(this);

		return gregRTipologiaSpesaProgrammaMissioneTitSottotit;
	}

	public GregRTipologiaSpesaProgrammaMissioneTitSottotit removeGregRTipologiaSpesaProgrammaMissioneTitSottotit(GregRTipologiaSpesaProgrammaMissioneTitSottotit gregRTipologiaSpesaProgrammaMissioneTitSottotit) {
		getGregRTipologiaSpesaProgrammaMissioneTitSottotits().remove(gregRTipologiaSpesaProgrammaMissioneTitSottotit);
		gregRTipologiaSpesaProgrammaMissioneTitSottotit.setGregDTipologiaSpesa(null);

		return gregRTipologiaSpesaProgrammaMissioneTitSottotit;
	}

}