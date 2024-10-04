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
 * The persistent class for the greg_d_target_utenza database table.
 * 
 */
@Entity
@Table(name="greg_d_target_utenza")

@NamedQueries({
	@NamedQuery(name="GregDTargetUtenza.findAll", query="SELECT g FROM GregDTargetUtenza g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDTargetUtenza.findByIdUtenzaNotDeleted", query="SELECT g FROM GregDTargetUtenza g WHERE g.idTargetUtenza= :idUtenza AND g.dataCancellazione IS NULL")
})
public class GregDTargetUtenza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TARGET_UTENZA_IDTARGETUTENZA_GENERATOR", sequenceName="GREG_D_TARGET_UTENZA_ID_TARGET_UTENZA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TARGET_UTENZA_IDTARGETUTENZA_GENERATOR")
	@Column(name="id_target_utenza")
	private Integer idTargetUtenza;

	@Column(name="cod_utenza")
	private String codUtenza;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_utenza")
	private String desUtenza;					  

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDAree
	@ManyToOne
	@JoinColumn(name="id_area")
	private GregDAree gregDAree;

	//bi-directional many-to-one association to GregDTipoFlusso
	@ManyToOne
	@JoinColumn(name="id_tipo_flusso")
	private GregDTipoFlusso gregDTipoFlusso;

	//bi-directional many-to-one association to GregRAlgoritmoTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza1")
	private Set<GregRAlgoritmoTargetUtenza> gregRAlgoritmoTargetUtenzas1;

	//bi-directional many-to-one association to GregRAlgoritmoTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza2")
	private Set<GregRAlgoritmoTargetUtenza> gregRAlgoritmoTargetUtenzas2;

	//bi-directional many-to-one association to GregRCatUteVocePrestReg2Istat
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRCatUteVocePrestReg2Istat> gregRCatUteVocePrestReg2Istats;

	//bi-directional many-to-one association to GregRDisabilitaTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRDisabilitaTargetUtenza> gregRDisabilitaTargetUtenzas;

	//bi-directional many-to-one association to GregRPrestMinistUtenzeMinist
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRPrestMinistUtenzeMinist> gregRPrestMinistUtenzeMinists;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeModc
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRPrestReg1UtenzeModc> gregRPrestReg1UtenzeModcs;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRPrestReg1UtenzeRegionali1> gregRPrestReg1UtenzeRegionali1s;

	//bi-directional many-to-one association to GregRPrestReg2UtenzeRegionali2
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRPrestReg2UtenzeRegionali2> gregRPrestReg2UtenzeRegionali2s;

	//bi-directional many-to-one association to GregRTargetUtenzaAree
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRTargetUtenzaAree> gregRTargetUtenzaArees;

	//bi-directional many-to-one association to GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;

	//bi-directional many-to-one association to GregRFnpsUtenzaCalcolo
	@OneToMany(mappedBy="gregDTargetUtenza")
	private Set<GregRFnpsUtenzaCalcolo> gregRFnpsUtenzaCalcolos;

	public GregDTargetUtenza() {
	}

	public Integer getIdTargetUtenza() {
		return this.idTargetUtenza;
	}

	public void setIdTargetUtenza(Integer idTargetUtenza) {
		this.idTargetUtenza = idTargetUtenza;
	}

	public String getCodUtenza() {
		return this.codUtenza;
	}

	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
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

	public String getDesUtenza() {
		return this.desUtenza;
	}

	public void setDesUtenza(String desUtenza) {
		this.desUtenza = desUtenza;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDAree getGregDAree() {
		return this.gregDAree;
	}

	public void setGregDAree(GregDAree gregDAree) {
		this.gregDAree = gregDAree;
	}

	public GregDTipoFlusso getGregDTipoFlusso() {
		return this.gregDTipoFlusso;
	}

	public void setGregDTipoFlusso(GregDTipoFlusso gregDTipoFlusso) {
		this.gregDTipoFlusso = gregDTipoFlusso;
	}

	public Set<GregRAlgoritmoTargetUtenza> getGregRAlgoritmoTargetUtenzas1() {
		return this.gregRAlgoritmoTargetUtenzas1;
	}

	public void setGregRAlgoritmoTargetUtenzas1(Set<GregRAlgoritmoTargetUtenza> gregRAlgoritmoTargetUtenzas1) {
		this.gregRAlgoritmoTargetUtenzas1 = gregRAlgoritmoTargetUtenzas1;
	}

	public GregRAlgoritmoTargetUtenza addGregRAlgoritmoTargetUtenzas1(GregRAlgoritmoTargetUtenza gregRAlgoritmoTargetUtenzas1) {
		getGregRAlgoritmoTargetUtenzas1().add(gregRAlgoritmoTargetUtenzas1);
		gregRAlgoritmoTargetUtenzas1.setGregDTargetUtenza1(this);

		return gregRAlgoritmoTargetUtenzas1;
	}

	public GregRAlgoritmoTargetUtenza removeGregRAlgoritmoTargetUtenzas1(GregRAlgoritmoTargetUtenza gregRAlgoritmoTargetUtenzas1) {
		getGregRAlgoritmoTargetUtenzas1().remove(gregRAlgoritmoTargetUtenzas1);
		gregRAlgoritmoTargetUtenzas1.setGregDTargetUtenza1(null);

		return gregRAlgoritmoTargetUtenzas1;
	}

	public Set<GregRAlgoritmoTargetUtenza> getGregRAlgoritmoTargetUtenzas2() {
		return this.gregRAlgoritmoTargetUtenzas2;
	}

	public void setGregRAlgoritmoTargetUtenzas2(Set<GregRAlgoritmoTargetUtenza> gregRAlgoritmoTargetUtenzas2) {
		this.gregRAlgoritmoTargetUtenzas2 = gregRAlgoritmoTargetUtenzas2;
	}

	public GregRAlgoritmoTargetUtenza addGregRAlgoritmoTargetUtenzas2(GregRAlgoritmoTargetUtenza gregRAlgoritmoTargetUtenzas2) {
		getGregRAlgoritmoTargetUtenzas2().add(gregRAlgoritmoTargetUtenzas2);
		gregRAlgoritmoTargetUtenzas2.setGregDTargetUtenza2(this);

		return gregRAlgoritmoTargetUtenzas2;
	}

	public GregRAlgoritmoTargetUtenza removeGregRAlgoritmoTargetUtenzas2(GregRAlgoritmoTargetUtenza gregRAlgoritmoTargetUtenzas2) {
		getGregRAlgoritmoTargetUtenzas2().remove(gregRAlgoritmoTargetUtenzas2);
		gregRAlgoritmoTargetUtenzas2.setGregDTargetUtenza2(null);

		return gregRAlgoritmoTargetUtenzas2;
	}

	public Set<GregRCatUteVocePrestReg2Istat> getGregRCatUteVocePrestReg2Istats() {
		return this.gregRCatUteVocePrestReg2Istats;
	}

	public void setGregRCatUteVocePrestReg2Istats(Set<GregRCatUteVocePrestReg2Istat> gregRCatUteVocePrestReg2Istats) {
		this.gregRCatUteVocePrestReg2Istats = gregRCatUteVocePrestReg2Istats;
	}

	public GregRCatUteVocePrestReg2Istat addGregRCatUteVocePrestReg2Istat(GregRCatUteVocePrestReg2Istat gregRCatUteVocePrestReg2Istat) {
		getGregRCatUteVocePrestReg2Istats().add(gregRCatUteVocePrestReg2Istat);
		gregRCatUteVocePrestReg2Istat.setGregDTargetUtenza(this);

		return gregRCatUteVocePrestReg2Istat;
	}

	public GregRCatUteVocePrestReg2Istat removeGregRCatUteVocePrestReg2Istat(GregRCatUteVocePrestReg2Istat gregRCatUteVocePrestReg2Istat) {
		getGregRCatUteVocePrestReg2Istats().remove(gregRCatUteVocePrestReg2Istat);
		gregRCatUteVocePrestReg2Istat.setGregDTargetUtenza(null);

		return gregRCatUteVocePrestReg2Istat;
	}

	public Set<GregRDisabilitaTargetUtenza> getGregRDisabilitaTargetUtenzas() {
		return this.gregRDisabilitaTargetUtenzas;
	}

	public void setGregRDisabilitaTargetUtenzas(Set<GregRDisabilitaTargetUtenza> gregRDisabilitaTargetUtenzas) {
		this.gregRDisabilitaTargetUtenzas = gregRDisabilitaTargetUtenzas;
	}

	public GregRDisabilitaTargetUtenza addGregRDisabilitaTargetUtenza(GregRDisabilitaTargetUtenza gregRDisabilitaTargetUtenza) {
		getGregRDisabilitaTargetUtenzas().add(gregRDisabilitaTargetUtenza);
		gregRDisabilitaTargetUtenza.setGregDTargetUtenza(this);

		return gregRDisabilitaTargetUtenza;
	}

	public GregRDisabilitaTargetUtenza removeGregRDisabilitaTargetUtenza(GregRDisabilitaTargetUtenza gregRDisabilitaTargetUtenza) {
		getGregRDisabilitaTargetUtenzas().remove(gregRDisabilitaTargetUtenza);
		gregRDisabilitaTargetUtenza.setGregDTargetUtenza(null);

		return gregRDisabilitaTargetUtenza;
	}

	public Set<GregRPrestMinistUtenzeMinist> getGregRPrestMinistUtenzeMinists() {
		return this.gregRPrestMinistUtenzeMinists;
	}

	public void setGregRPrestMinistUtenzeMinists(Set<GregRPrestMinistUtenzeMinist> gregRPrestMinistUtenzeMinists) {
		this.gregRPrestMinistUtenzeMinists = gregRPrestMinistUtenzeMinists;
	}

	public GregRPrestMinistUtenzeMinist addGregRPrestMinistUtenzeMinist(GregRPrestMinistUtenzeMinist gregRPrestMinistUtenzeMinist) {
		getGregRPrestMinistUtenzeMinists().add(gregRPrestMinistUtenzeMinist);
		gregRPrestMinistUtenzeMinist.setGregDTargetUtenza(this);

		return gregRPrestMinistUtenzeMinist;
	}

	public GregRPrestMinistUtenzeMinist removeGregRPrestMinistUtenzeMinist(GregRPrestMinistUtenzeMinist gregRPrestMinistUtenzeMinist) {
		getGregRPrestMinistUtenzeMinists().remove(gregRPrestMinistUtenzeMinist);
		gregRPrestMinistUtenzeMinist.setGregDTargetUtenza(null);

		return gregRPrestMinistUtenzeMinist;
	}

	public Set<GregRPrestReg1UtenzeModc> getGregRPrestReg1UtenzeModcs() {
		return this.gregRPrestReg1UtenzeModcs;
	}

	public void setGregRPrestReg1UtenzeModcs(Set<GregRPrestReg1UtenzeModc> gregRPrestReg1UtenzeModcs) {
		this.gregRPrestReg1UtenzeModcs = gregRPrestReg1UtenzeModcs;
	}

	public GregRPrestReg1UtenzeModc addGregRPrestReg1UtenzeModc(GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc) {
		getGregRPrestReg1UtenzeModcs().add(gregRPrestReg1UtenzeModc);
		gregRPrestReg1UtenzeModc.setGregDTargetUtenza(this);

		return gregRPrestReg1UtenzeModc;
	}

	public GregRPrestReg1UtenzeModc removeGregRPrestReg1UtenzeModc(GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc) {
		getGregRPrestReg1UtenzeModcs().remove(gregRPrestReg1UtenzeModc);
		gregRPrestReg1UtenzeModc.setGregDTargetUtenza(null);

		return gregRPrestReg1UtenzeModc;
	}

	public Set<GregRPrestReg1UtenzeRegionali1> getGregRPrestReg1UtenzeRegionali1s() {
		return this.gregRPrestReg1UtenzeRegionali1s;
	}

	public void setGregRPrestReg1UtenzeRegionali1s(Set<GregRPrestReg1UtenzeRegionali1> gregRPrestReg1UtenzeRegionali1s) {
		this.gregRPrestReg1UtenzeRegionali1s = gregRPrestReg1UtenzeRegionali1s;
	}

	public GregRPrestReg1UtenzeRegionali1 addGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		getGregRPrestReg1UtenzeRegionali1s().add(gregRPrestReg1UtenzeRegionali1);
		gregRPrestReg1UtenzeRegionali1.setGregDTargetUtenza(this);

		return gregRPrestReg1UtenzeRegionali1;
	}

	public GregRPrestReg1UtenzeRegionali1 removeGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		getGregRPrestReg1UtenzeRegionali1s().remove(gregRPrestReg1UtenzeRegionali1);
		gregRPrestReg1UtenzeRegionali1.setGregDTargetUtenza(null);

		return gregRPrestReg1UtenzeRegionali1;
	}

	public Set<GregRPrestReg2UtenzeRegionali2> getGregRPrestReg2UtenzeRegionali2s() {
		return this.gregRPrestReg2UtenzeRegionali2s;
	}

	public void setGregRPrestReg2UtenzeRegionali2s(Set<GregRPrestReg2UtenzeRegionali2> gregRPrestReg2UtenzeRegionali2s) {
		this.gregRPrestReg2UtenzeRegionali2s = gregRPrestReg2UtenzeRegionali2s;
	}

	public GregRPrestReg2UtenzeRegionali2 addGregRPrestReg2UtenzeRegionali2(GregRPrestReg2UtenzeRegionali2 gregRPrestReg2UtenzeRegionali2) {
		getGregRPrestReg2UtenzeRegionali2s().add(gregRPrestReg2UtenzeRegionali2);
		gregRPrestReg2UtenzeRegionali2.setGregDTargetUtenza(this);

		return gregRPrestReg2UtenzeRegionali2;
	}

	public GregRPrestReg2UtenzeRegionali2 removeGregRPrestReg2UtenzeRegionali2(GregRPrestReg2UtenzeRegionali2 gregRPrestReg2UtenzeRegionali2) {
		getGregRPrestReg2UtenzeRegionali2s().remove(gregRPrestReg2UtenzeRegionali2);
		gregRPrestReg2UtenzeRegionali2.setGregDTargetUtenza(null);

		return gregRPrestReg2UtenzeRegionali2;
	}

	public Set<GregRTargetUtenzaAree> getGregRTargetUtenzaArees() {
		return this.gregRTargetUtenzaArees;
	}

	public void setGregRTargetUtenzaArees(Set<GregRTargetUtenzaAree> gregRTargetUtenzaArees) {
		this.gregRTargetUtenzaArees = gregRTargetUtenzaArees;
	}

	public GregRTargetUtenzaAree addGregRTargetUtenzaAree(GregRTargetUtenzaAree gregRTargetUtenzaAree) {
		getGregRTargetUtenzaArees().add(gregRTargetUtenzaAree);
		gregRTargetUtenzaAree.setGregDTargetUtenza(this);

		return gregRTargetUtenzaAree;
	}

	public GregRTargetUtenzaAree removeGregRTargetUtenzaAree(GregRTargetUtenzaAree gregRTargetUtenzaAree) {
		getGregRTargetUtenzaArees().remove(gregRTargetUtenzaAree);
		gregRTargetUtenzaAree.setGregDTargetUtenza(null);

		return gregRTargetUtenzaAree;
	}

	public Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits() {
		return this.gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;
	}

	public void setGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits(Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits) {
		this.gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits = gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;
	}

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit addGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits().add(gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit);
		gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.setGregDTargetUtenza(this);

		return gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit removeGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits().remove(gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit);
		gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.setGregDTargetUtenza(null);

		return gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}
	
	public Set<GregRFnpsUtenzaCalcolo> getGregRFnpsUtenzaCalcolos() {
		return this.gregRFnpsUtenzaCalcolos;
	}

	public void setGregRFnpsUtenzaCalcolos(Set<GregRFnpsUtenzaCalcolo> gregRFnpsUtenzaCalcolos) {
		this.gregRFnpsUtenzaCalcolos = gregRFnpsUtenzaCalcolos;
	}

	public GregRFnpsUtenzaCalcolo addGregRFnpsUtenzaCalcolo(GregRFnpsUtenzaCalcolo gregRFnpsUtenzaCalcolo) {
		getGregRFnpsUtenzaCalcolos().add(gregRFnpsUtenzaCalcolo);
		gregRFnpsUtenzaCalcolo.setGregDTargetUtenza(this);

		return gregRFnpsUtenzaCalcolo;
	}

	public GregRFnpsUtenzaCalcolo removeGregRFnpsUtenzaCalcolo(GregRFnpsUtenzaCalcolo gregRFnpsUtenzaCalcolo) {
		getGregRFnpsUtenzaCalcolos().remove(gregRFnpsUtenzaCalcolo);
		gregRFnpsUtenzaCalcolo.setGregDTargetUtenza(null);

		return gregRFnpsUtenzaCalcolo;
	}

}