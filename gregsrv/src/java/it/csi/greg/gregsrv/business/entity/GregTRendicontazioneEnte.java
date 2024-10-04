/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_t_rendicontazione_ente database table.
 * 
 */
@Entity
@Table(name="greg_t_rendicontazione_ente")
@NamedQueries({
	@NamedQuery(name="GregTRendicontazioneEnte.findAll", query="SELECT g FROM GregTRendicontazioneEnte g"),
	@NamedQuery(name="GregTRendicontazioneEnte.findByIdScheda", query="SELECT g FROM GregTRendicontazioneEnte g WHERE g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idSchedaEnteGestore "
			+ "and g.annoGestione = :annoGestione"),
	@NamedQuery(name="GregTRendicontazioneEnte.findByIdSchedaNotDeleted", query="SELECT g FROM GregTRendicontazioneEnte g WHERE g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idSchedaEnteGestore "
			+ "and g.annoGestione = :annoGestione and g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTRendicontazioneEnte.findByIdRendNotDeleted", query="SELECT g FROM GregTRendicontazioneEnte g WHERE g.idRendicontazioneEnte = :idRendicontazioneEnte "
			+ "and g.dataCancellazione IS NULL")
})
public class GregTRendicontazioneEnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_RENDICONTAZIONE_ENTE_IDRENDICONTAZIONEENTE_GENERATOR", sequenceName="GREG_T_RENDICONTAZIONE_ENTE_ID_RENDICONTAZIONE_ENTE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_RENDICONTAZIONE_ENTE_IDRENDICONTAZIONEENTE_GENERATOR")
	@Column(name="id_rendicontazione_ente")
	private Integer idRendicontazioneEnte;

	@Column(name="anno_gestione")
	private Integer annoGestione;

	@Column(name="centro_diurno_strutt_semires")
	private Boolean centroDiurnoStruttSemires;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="struttura_residenziale")
	private Boolean strutturaResidenziale;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRCartaServiziPreg1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRCartaServiziPreg1> gregRCartaServiziPreg1s;

	//bi-directional many-to-one association to GregREnteTab
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregREnteTab> gregREnteTabs;

	//bi-directional many-to-one association to GregRParametroRendicontazioneEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRParametroRendicontazioneEnte> gregRParametroRendicontazioneEntes;

	//bi-directional many-to-one association to GregRRendMiProTitEnteGestoreModB
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendMiProTitEnteGestoreModB> gregRRendMiProTitEnteGestoreModBs;

	//bi-directional many-to-one association to GregRRendicontazioneComuneEnteModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneComuneEnteModA2> gregRRendicontazioneComuneEnteModA2s;

	//bi-directional many-to-one association to GregRRendicontazioneEnteComuneModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneEnteComuneModA2> gregRRendicontazioneEnteComuneModA2s;

	//bi-directional many-to-one association to GregRRendicontazioneModA1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModA1> gregRRendicontazioneModA1s;

	//bi-directional many-to-one association to GregRRendicontazioneModAPart1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModAPart1> gregRRendicontazioneModAPart1s;

	//bi-directional many-to-one association to GregRRendicontazioneModAPart2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModAPart2> gregRRendicontazioneModAPart2s;

	//bi-directional many-to-one association to GregRRendicontazioneModAPart3
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModAPart3> gregRRendicontazioneModAPart3s;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModCParte1> gregRRendicontazioneModCParte1s;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModCParte2> gregRRendicontazioneModCParte2s;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte3
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModCParte3> gregRRendicontazioneModCParte3s;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte4
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModCParte4> gregRRendicontazioneModCParte4s;

	//bi-directional many-to-one association to GregRRendicontazioneModD
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModD> gregRRendicontazioneModDs;

	//bi-directional many-to-one association to GregRRendicontazioneModE
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModE> gregRRendicontazioneModEs;

	//bi-directional many-to-one association to GregRRendicontazioneModFParte1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModFParte1> gregRRendicontazioneModFParte1s;

	//bi-directional many-to-one association to GregRRendicontazioneModFParte2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModFParte2> gregRRendicontazioneModFParte2s;

	//bi-directional many-to-one association to GregRRendicontazioneModuloFnps
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneModuloFnps> gregRRendicontazioneModuloFnps;

	//bi-directional many-to-one association to GregRRendicontazioneNonConformitaModB
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneNonConformitaModB> gregRRendicontazioneNonConformitaModBs;

	//bi-directional many-to-one association to GregRRendicontazionePreg1Macro
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazionePreg1Macro> gregRRendicontazionePreg1Macros;

	//bi-directional many-to-one association to GregRRendicontazionePreg1Utereg1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazionePreg1Utereg1> gregRRendicontazionePreg1Utereg1s;

	//bi-directional many-to-one association to GregRRendicontazionePreg2Utereg2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazionePreg2Utereg2> gregRRendicontazionePreg2Utereg2s;

	//bi-directional many-to-one association to GregRRendicontazioneReportReg1
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneReportReg1> gregRRendicontazioneReportReg1s;

	//bi-directional many-to-one association to GregRRendicontazioneSpesaMissioneProgrammaMacro
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneSpesaMissioneProgrammaMacro> gregRRendicontazioneSpesaMissioneProgrammaMacros;

	//bi-directional many-to-one association to GregTAllegatiRendicontazione
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregTAllegatiRendicontazione> gregTAllegatiRendicontaziones;

	//bi-directional many-to-one association to GregTCausaleEnteComuneModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregTCausaleEnteComuneModA2> gregTCausaleEnteComuneModA2s;

	//bi-directional many-to-one association to GregTCronologia
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregTCronologia> gregTCronologias;

	//bi-directional many-to-one association to GregDStatoRendicontazione
	@ManyToOne
	@JoinColumn(name="id_stato_rendicontazione")
	private GregDStatoRendicontazione gregDStatoRendicontazione;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_scheda_ente_gestore")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori;

	//bi-directional many-to-one association to GregRRendicontazioneGiustificazioneFnps
	@JsonIgnore
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneGiustificazioneFnps> gregRRendicontazioneGiustificazioneFnps;

	//bi-directional many-to-one association to GregRRendicontazioneFondi
	
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRRendicontazioneFondi> gregRRendicontazioneFondis;

	//bi-directional many-to-one association to GregRSpeseFnp
			
	@OneToMany(mappedBy="gregTRendicontazioneEnte")
	private Set<GregRSpeseFnps> gregRSpeseFnps;
	
	public GregTRendicontazioneEnte() {
	}

	public Integer getIdRendicontazioneEnte() {
		return this.idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getAnnoGestione() {
		return this.annoGestione;
	}

	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}

	public Boolean getCentroDiurnoStruttSemires() {
		return this.centroDiurnoStruttSemires;
	}

	public void setCentroDiurnoStruttSemires(Boolean centroDiurnoStruttSemires) {
		this.centroDiurnoStruttSemires = centroDiurnoStruttSemires;
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

	

	public Boolean getStrutturaResidenziale() {
		return this.strutturaResidenziale;
	}

	public void setStrutturaResidenziale(Boolean strutturaResidenziale) {
		this.strutturaResidenziale = strutturaResidenziale;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}


	public Set<GregRCartaServiziPreg1> getGregRCartaServiziPreg1s() {
		return this.gregRCartaServiziPreg1s;
	}

	public void setGregRCartaServiziPreg1s(Set<GregRCartaServiziPreg1> gregRCartaServiziPreg1s) {
		this.gregRCartaServiziPreg1s = gregRCartaServiziPreg1s;
	}

	public GregRCartaServiziPreg1 addGregRCartaServiziPreg1(GregRCartaServiziPreg1 gregRCartaServiziPreg1) {
		getGregRCartaServiziPreg1s().add(gregRCartaServiziPreg1);
		gregRCartaServiziPreg1.setGregTRendicontazioneEnte(this);

		return gregRCartaServiziPreg1;
	}

	public GregRCartaServiziPreg1 removeGregRCartaServiziPreg1(GregRCartaServiziPreg1 gregRCartaServiziPreg1) {
		getGregRCartaServiziPreg1s().remove(gregRCartaServiziPreg1);
		gregRCartaServiziPreg1.setGregTRendicontazioneEnte(null);

		return gregRCartaServiziPreg1;
	}

	public Set<GregREnteTab> getGregREnteTabs() {
		return this.gregREnteTabs;
	}

	public void setGregREnteTabs(Set<GregREnteTab> gregREnteTabs) {
		this.gregREnteTabs = gregREnteTabs;
	}

	public GregREnteTab addGregREnteTab(GregREnteTab gregREnteTab) {
		getGregREnteTabs().add(gregREnteTab);
		gregREnteTab.setGregTRendicontazioneEnte(this);

		return gregREnteTab;
	}

	public GregREnteTab removeGregREnteTab(GregREnteTab gregREnteTab) {
		getGregREnteTabs().remove(gregREnteTab);
		gregREnteTab.setGregTRendicontazioneEnte(null);

		return gregREnteTab;
	}

	public Set<GregRParametroRendicontazioneEnte> getGregRParametroRendicontazioneEntes() {
		return this.gregRParametroRendicontazioneEntes;
	}

	public void setGregRParametroRendicontazioneEntes(Set<GregRParametroRendicontazioneEnte> gregRParametroRendicontazioneEntes) {
		this.gregRParametroRendicontazioneEntes = gregRParametroRendicontazioneEntes;
	}

	public GregRParametroRendicontazioneEnte addGregRParametroRendicontazioneEnte(GregRParametroRendicontazioneEnte gregRParametroRendicontazioneEnte) {
		getGregRParametroRendicontazioneEntes().add(gregRParametroRendicontazioneEnte);
		gregRParametroRendicontazioneEnte.setGregTRendicontazioneEnte(this);

		return gregRParametroRendicontazioneEnte;
	}

	public GregRParametroRendicontazioneEnte removeGregRParametroRendicontazioneEnte(GregRParametroRendicontazioneEnte gregRParametroRendicontazioneEnte) {
		getGregRParametroRendicontazioneEntes().remove(gregRParametroRendicontazioneEnte);
		gregRParametroRendicontazioneEnte.setGregTRendicontazioneEnte(null);

		return gregRParametroRendicontazioneEnte;
	}

	public Set<GregRRendMiProTitEnteGestoreModB> getGregRRendMiProTitEnteGestoreModBs() {
		return this.gregRRendMiProTitEnteGestoreModBs;
	}

	public void setGregRRendMiProTitEnteGestoreModBs(Set<GregRRendMiProTitEnteGestoreModB> gregRRendMiProTitEnteGestoreModBs) {
		this.gregRRendMiProTitEnteGestoreModBs = gregRRendMiProTitEnteGestoreModBs;
	}

	public GregRRendMiProTitEnteGestoreModB addGregRRendMiProTitEnteGestoreModB(GregRRendMiProTitEnteGestoreModB gregRRendMiProTitEnteGestoreModB) {
		getGregRRendMiProTitEnteGestoreModBs().add(gregRRendMiProTitEnteGestoreModB);
		gregRRendMiProTitEnteGestoreModB.setGregTRendicontazioneEnte(this);

		return gregRRendMiProTitEnteGestoreModB;
	}

	public GregRRendMiProTitEnteGestoreModB removeGregRRendMiProTitEnteGestoreModB(GregRRendMiProTitEnteGestoreModB gregRRendMiProTitEnteGestoreModB) {
		getGregRRendMiProTitEnteGestoreModBs().remove(gregRRendMiProTitEnteGestoreModB);
		gregRRendMiProTitEnteGestoreModB.setGregTRendicontazioneEnte(null);

		return gregRRendMiProTitEnteGestoreModB;
	}

	public Set<GregRRendicontazioneComuneEnteModA2> getGregRRendicontazioneComuneEnteModA2s() {
		return this.gregRRendicontazioneComuneEnteModA2s;
	}

	public void setGregRRendicontazioneComuneEnteModA2s(Set<GregRRendicontazioneComuneEnteModA2> gregRRendicontazioneComuneEnteModA2s) {
		this.gregRRendicontazioneComuneEnteModA2s = gregRRendicontazioneComuneEnteModA2s;
	}

	public GregRRendicontazioneComuneEnteModA2 addGregRRendicontazioneComuneEnteModA2(GregRRendicontazioneComuneEnteModA2 gregRRendicontazioneComuneEnteModA2) {
		getGregRRendicontazioneComuneEnteModA2s().add(gregRRendicontazioneComuneEnteModA2);
		gregRRendicontazioneComuneEnteModA2.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneComuneEnteModA2;
	}

	public GregRRendicontazioneComuneEnteModA2 removeGregRRendicontazioneComuneEnteModA2(GregRRendicontazioneComuneEnteModA2 gregRRendicontazioneComuneEnteModA2) {
		getGregRRendicontazioneComuneEnteModA2s().remove(gregRRendicontazioneComuneEnteModA2);
		gregRRendicontazioneComuneEnteModA2.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneComuneEnteModA2;
	}

	public Set<GregRRendicontazioneEnteComuneModA2> getGregRRendicontazioneEnteComuneModA2s() {
		return this.gregRRendicontazioneEnteComuneModA2s;
	}

	public void setGregRRendicontazioneEnteComuneModA2s(Set<GregRRendicontazioneEnteComuneModA2> gregRRendicontazioneEnteComuneModA2s) {
		this.gregRRendicontazioneEnteComuneModA2s = gregRRendicontazioneEnteComuneModA2s;
	}

	public GregRRendicontazioneEnteComuneModA2 addGregRRendicontazioneEnteComuneModA2(GregRRendicontazioneEnteComuneModA2 gregRRendicontazioneEnteComuneModA2) {
		getGregRRendicontazioneEnteComuneModA2s().add(gregRRendicontazioneEnteComuneModA2);
		gregRRendicontazioneEnteComuneModA2.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneEnteComuneModA2;
	}

	public GregRRendicontazioneEnteComuneModA2 removeGregRRendicontazioneEnteComuneModA2(GregRRendicontazioneEnteComuneModA2 gregRRendicontazioneEnteComuneModA2) {
		getGregRRendicontazioneEnteComuneModA2s().remove(gregRRendicontazioneEnteComuneModA2);
		gregRRendicontazioneEnteComuneModA2.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneEnteComuneModA2;
	}

	public Set<GregRRendicontazioneModA1> getGregRRendicontazioneModA1s() {
		return this.gregRRendicontazioneModA1s;
	}

	public void setGregRRendicontazioneModA1s(Set<GregRRendicontazioneModA1> gregRRendicontazioneModA1s) {
		this.gregRRendicontazioneModA1s = gregRRendicontazioneModA1s;
	}

	public GregRRendicontazioneModA1 addGregRRendicontazioneModA1(GregRRendicontazioneModA1 gregRRendicontazioneModA1) {
		getGregRRendicontazioneModA1s().add(gregRRendicontazioneModA1);
		gregRRendicontazioneModA1.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModA1;
	}

	public GregRRendicontazioneModA1 removeGregRRendicontazioneModA1(GregRRendicontazioneModA1 gregRRendicontazioneModA1) {
		getGregRRendicontazioneModA1s().remove(gregRRendicontazioneModA1);
		gregRRendicontazioneModA1.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModA1;
	}

	public Set<GregRRendicontazioneModAPart1> getGregRRendicontazioneModAPart1s() {
		return this.gregRRendicontazioneModAPart1s;
	}

	public void setGregRRendicontazioneModAPart1s(Set<GregRRendicontazioneModAPart1> gregRRendicontazioneModAPart1s) {
		this.gregRRendicontazioneModAPart1s = gregRRendicontazioneModAPart1s;
	}

	public GregRRendicontazioneModAPart1 addGregRRendicontazioneModAPart1(GregRRendicontazioneModAPart1 gregRRendicontazioneModAPart1) {
		getGregRRendicontazioneModAPart1s().add(gregRRendicontazioneModAPart1);
		gregRRendicontazioneModAPart1.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModAPart1;
	}

	public GregRRendicontazioneModAPart1 removeGregRRendicontazioneModAPart1(GregRRendicontazioneModAPart1 gregRRendicontazioneModAPart1) {
		getGregRRendicontazioneModAPart1s().remove(gregRRendicontazioneModAPart1);
		gregRRendicontazioneModAPart1.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModAPart1;
	}

	public Set<GregRRendicontazioneModAPart2> getGregRRendicontazioneModAPart2s() {
		return this.gregRRendicontazioneModAPart2s;
	}

	public void setGregRRendicontazioneModAPart2s(Set<GregRRendicontazioneModAPart2> gregRRendicontazioneModAPart2s) {
		this.gregRRendicontazioneModAPart2s = gregRRendicontazioneModAPart2s;
	}

	public GregRRendicontazioneModAPart2 addGregRRendicontazioneModAPart2(GregRRendicontazioneModAPart2 gregRRendicontazioneModAPart2) {
		getGregRRendicontazioneModAPart2s().add(gregRRendicontazioneModAPart2);
		gregRRendicontazioneModAPart2.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModAPart2;
	}

	public GregRRendicontazioneModAPart2 removeGregRRendicontazioneModAPart2(GregRRendicontazioneModAPart2 gregRRendicontazioneModAPart2) {
		getGregRRendicontazioneModAPart2s().remove(gregRRendicontazioneModAPart2);
		gregRRendicontazioneModAPart2.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModAPart2;
	}

	public Set<GregRRendicontazioneModAPart3> getGregRRendicontazioneModAPart3s() {
		return this.gregRRendicontazioneModAPart3s;
	}

	public void setGregRRendicontazioneModAPart3s(Set<GregRRendicontazioneModAPart3> gregRRendicontazioneModAPart3s) {
		this.gregRRendicontazioneModAPart3s = gregRRendicontazioneModAPart3s;
	}

	public GregRRendicontazioneModAPart3 addGregRRendicontazioneModAPart3(GregRRendicontazioneModAPart3 gregRRendicontazioneModAPart3) {
		getGregRRendicontazioneModAPart3s().add(gregRRendicontazioneModAPart3);
		gregRRendicontazioneModAPart3.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModAPart3;
	}

	public GregRRendicontazioneModAPart3 removeGregRRendicontazioneModAPart3(GregRRendicontazioneModAPart3 gregRRendicontazioneModAPart3) {
		getGregRRendicontazioneModAPart3s().remove(gregRRendicontazioneModAPart3);
		gregRRendicontazioneModAPart3.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModAPart3;
	}

	public Set<GregRRendicontazioneModCParte1> getGregRRendicontazioneModCParte1s() {
		return this.gregRRendicontazioneModCParte1s;
	}

	public void setGregRRendicontazioneModCParte1s(Set<GregRRendicontazioneModCParte1> gregRRendicontazioneModCParte1s) {
		this.gregRRendicontazioneModCParte1s = gregRRendicontazioneModCParte1s;
	}

	public GregRRendicontazioneModCParte1 addGregRRendicontazioneModCParte1(GregRRendicontazioneModCParte1 gregRRendicontazioneModCParte1) {
		getGregRRendicontazioneModCParte1s().add(gregRRendicontazioneModCParte1);
		gregRRendicontazioneModCParte1.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModCParte1;
	}

	public GregRRendicontazioneModCParte1 removeGregRRendicontazioneModCParte1(GregRRendicontazioneModCParte1 gregRRendicontazioneModCParte1) {
		getGregRRendicontazioneModCParte1s().remove(gregRRendicontazioneModCParte1);
		gregRRendicontazioneModCParte1.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModCParte1;
	}

	public Set<GregRRendicontazioneModCParte2> getGregRRendicontazioneModCParte2s() {
		return this.gregRRendicontazioneModCParte2s;
	}

	public void setGregRRendicontazioneModCParte2s(Set<GregRRendicontazioneModCParte2> gregRRendicontazioneModCParte2s) {
		this.gregRRendicontazioneModCParte2s = gregRRendicontazioneModCParte2s;
	}

	public GregRRendicontazioneModCParte2 addGregRRendicontazioneModCParte2(GregRRendicontazioneModCParte2 gregRRendicontazioneModCParte2) {
		getGregRRendicontazioneModCParte2s().add(gregRRendicontazioneModCParte2);
		gregRRendicontazioneModCParte2.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModCParte2;
	}

	public GregRRendicontazioneModCParte2 removeGregRRendicontazioneModCParte2(GregRRendicontazioneModCParte2 gregRRendicontazioneModCParte2) {
		getGregRRendicontazioneModCParte2s().remove(gregRRendicontazioneModCParte2);
		gregRRendicontazioneModCParte2.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModCParte2;
	}

	public Set<GregRRendicontazioneModCParte3> getGregRRendicontazioneModCParte3s() {
		return this.gregRRendicontazioneModCParte3s;
	}

	public void setGregRRendicontazioneModCParte3s(Set<GregRRendicontazioneModCParte3> gregRRendicontazioneModCParte3s) {
		this.gregRRendicontazioneModCParte3s = gregRRendicontazioneModCParte3s;
	}

	public GregRRendicontazioneModCParte3 addGregRRendicontazioneModCParte3(GregRRendicontazioneModCParte3 gregRRendicontazioneModCParte3) {
		getGregRRendicontazioneModCParte3s().add(gregRRendicontazioneModCParte3);
		gregRRendicontazioneModCParte3.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModCParte3;
	}

	public GregRRendicontazioneModCParte3 removeGregRRendicontazioneModCParte3(GregRRendicontazioneModCParte3 gregRRendicontazioneModCParte3) {
		getGregRRendicontazioneModCParte3s().remove(gregRRendicontazioneModCParte3);
		gregRRendicontazioneModCParte3.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModCParte3;
	}

	public Set<GregRRendicontazioneModCParte4> getGregRRendicontazioneModCParte4s() {
		return this.gregRRendicontazioneModCParte4s;
	}

	public void setGregRRendicontazioneModCParte4s(Set<GregRRendicontazioneModCParte4> gregRRendicontazioneModCParte4s) {
		this.gregRRendicontazioneModCParte4s = gregRRendicontazioneModCParte4s;
	}

	public GregRRendicontazioneModCParte4 addGregRRendicontazioneModCParte4(GregRRendicontazioneModCParte4 gregRRendicontazioneModCParte4) {
		getGregRRendicontazioneModCParte4s().add(gregRRendicontazioneModCParte4);
		gregRRendicontazioneModCParte4.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModCParte4;
	}

	public GregRRendicontazioneModCParte4 removeGregRRendicontazioneModCParte4(GregRRendicontazioneModCParte4 gregRRendicontazioneModCParte4) {
		getGregRRendicontazioneModCParte4s().remove(gregRRendicontazioneModCParte4);
		gregRRendicontazioneModCParte4.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModCParte4;
	}

	public Set<GregRRendicontazioneModD> getGregRRendicontazioneModDs() {
		return this.gregRRendicontazioneModDs;
	}

	public void setGregRRendicontazioneModDs(Set<GregRRendicontazioneModD> gregRRendicontazioneModDs) {
		this.gregRRendicontazioneModDs = gregRRendicontazioneModDs;
	}

	public GregRRendicontazioneModD addGregRRendicontazioneModD(GregRRendicontazioneModD gregRRendicontazioneModD) {
		getGregRRendicontazioneModDs().add(gregRRendicontazioneModD);
		gregRRendicontazioneModD.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModD;
	}

	public GregRRendicontazioneModD removeGregRRendicontazioneModD(GregRRendicontazioneModD gregRRendicontazioneModD) {
		getGregRRendicontazioneModDs().remove(gregRRendicontazioneModD);
		gregRRendicontazioneModD.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModD;
	}

	public Set<GregRRendicontazioneModE> getGregRRendicontazioneModEs() {
		return this.gregRRendicontazioneModEs;
	}

	public void setGregRRendicontazioneModEs(Set<GregRRendicontazioneModE> gregRRendicontazioneModEs) {
		this.gregRRendicontazioneModEs = gregRRendicontazioneModEs;
	}

	public GregRRendicontazioneModE addGregRRendicontazioneModE(GregRRendicontazioneModE gregRRendicontazioneModE) {
		getGregRRendicontazioneModEs().add(gregRRendicontazioneModE);
		gregRRendicontazioneModE.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModE;
	}

	public GregRRendicontazioneModE removeGregRRendicontazioneModE(GregRRendicontazioneModE gregRRendicontazioneModE) {
		getGregRRendicontazioneModEs().remove(gregRRendicontazioneModE);
		gregRRendicontazioneModE.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModE;
	}

	public Set<GregRRendicontazioneModFParte1> getGregRRendicontazioneModFParte1s() {
		return this.gregRRendicontazioneModFParte1s;
	}

	public void setGregRRendicontazioneModFParte1s(Set<GregRRendicontazioneModFParte1> gregRRendicontazioneModFParte1s) {
		this.gregRRendicontazioneModFParte1s = gregRRendicontazioneModFParte1s;
	}

	public GregRRendicontazioneModFParte1 addGregRRendicontazioneModFParte1(GregRRendicontazioneModFParte1 gregRRendicontazioneModFParte1) {
		getGregRRendicontazioneModFParte1s().add(gregRRendicontazioneModFParte1);
		gregRRendicontazioneModFParte1.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModFParte1;
	}

	public GregRRendicontazioneModFParte1 removeGregRRendicontazioneModFParte1(GregRRendicontazioneModFParte1 gregRRendicontazioneModFParte1) {
		getGregRRendicontazioneModFParte1s().remove(gregRRendicontazioneModFParte1);
		gregRRendicontazioneModFParte1.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModFParte1;
	}

	public Set<GregRRendicontazioneModFParte2> getGregRRendicontazioneModFParte2s() {
		return this.gregRRendicontazioneModFParte2s;
	}

	public void setGregRRendicontazioneModFParte2s(Set<GregRRendicontazioneModFParte2> gregRRendicontazioneModFParte2s) {
		this.gregRRendicontazioneModFParte2s = gregRRendicontazioneModFParte2s;
	}

	public GregRRendicontazioneModFParte2 addGregRRendicontazioneModFParte2(GregRRendicontazioneModFParte2 gregRRendicontazioneModFParte2) {
		getGregRRendicontazioneModFParte2s().add(gregRRendicontazioneModFParte2);
		gregRRendicontazioneModFParte2.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModFParte2;
	}

	public GregRRendicontazioneModFParte2 removeGregRRendicontazioneModFParte2(GregRRendicontazioneModFParte2 gregRRendicontazioneModFParte2) {
		getGregRRendicontazioneModFParte2s().remove(gregRRendicontazioneModFParte2);
		gregRRendicontazioneModFParte2.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModFParte2;
	}

	public Set<GregRRendicontazioneModuloFnps> getGregRRendicontazioneModuloFnps() {
		return this.gregRRendicontazioneModuloFnps;
	}

	public void setGregRRendicontazioneModuloFnps(Set<GregRRendicontazioneModuloFnps> gregRRendicontazioneModuloFnps) {
		this.gregRRendicontazioneModuloFnps = gregRRendicontazioneModuloFnps;
	}

	public GregRRendicontazioneModuloFnps addGregRRendicontazioneModuloFnp(GregRRendicontazioneModuloFnps gregRRendicontazioneModuloFnp) {
		getGregRRendicontazioneModuloFnps().add(gregRRendicontazioneModuloFnp);
		gregRRendicontazioneModuloFnp.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneModuloFnp;
	}

	public GregRRendicontazioneModuloFnps removeGregRRendicontazioneModuloFnp(GregRRendicontazioneModuloFnps gregRRendicontazioneModuloFnp) {
		getGregRRendicontazioneModuloFnps().remove(gregRRendicontazioneModuloFnp);
		gregRRendicontazioneModuloFnp.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneModuloFnp;
	}

	public Set<GregRRendicontazioneNonConformitaModB> getGregRRendicontazioneNonConformitaModBs() {
		return this.gregRRendicontazioneNonConformitaModBs;
	}

	public void setGregRRendicontazioneNonConformitaModBs(Set<GregRRendicontazioneNonConformitaModB> gregRRendicontazioneNonConformitaModBs) {
		this.gregRRendicontazioneNonConformitaModBs = gregRRendicontazioneNonConformitaModBs;
	}

	public GregRRendicontazioneNonConformitaModB addGregRRendicontazioneNonConformitaModB(GregRRendicontazioneNonConformitaModB gregRRendicontazioneNonConformitaModB) {
		getGregRRendicontazioneNonConformitaModBs().add(gregRRendicontazioneNonConformitaModB);
		gregRRendicontazioneNonConformitaModB.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneNonConformitaModB;
	}

	public GregRRendicontazioneNonConformitaModB removeGregRRendicontazioneNonConformitaModB(GregRRendicontazioneNonConformitaModB gregRRendicontazioneNonConformitaModB) {
		getGregRRendicontazioneNonConformitaModBs().remove(gregRRendicontazioneNonConformitaModB);
		gregRRendicontazioneNonConformitaModB.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneNonConformitaModB;
	}

	public Set<GregRRendicontazionePreg1Macro> getGregRRendicontazionePreg1Macros() {
		return this.gregRRendicontazionePreg1Macros;
	}

	public void setGregRRendicontazionePreg1Macros(Set<GregRRendicontazionePreg1Macro> gregRRendicontazionePreg1Macros) {
		this.gregRRendicontazionePreg1Macros = gregRRendicontazionePreg1Macros;
	}

	public GregRRendicontazionePreg1Macro addGregRRendicontazionePreg1Macro(GregRRendicontazionePreg1Macro gregRRendicontazionePreg1Macro) {
		getGregRRendicontazionePreg1Macros().add(gregRRendicontazionePreg1Macro);
		gregRRendicontazionePreg1Macro.setGregTRendicontazioneEnte(this);

		return gregRRendicontazionePreg1Macro;
	}

	public GregRRendicontazionePreg1Macro removeGregRRendicontazionePreg1Macro(GregRRendicontazionePreg1Macro gregRRendicontazionePreg1Macro) {
		getGregRRendicontazionePreg1Macros().remove(gregRRendicontazionePreg1Macro);
		gregRRendicontazionePreg1Macro.setGregTRendicontazioneEnte(null);

		return gregRRendicontazionePreg1Macro;
	}

	public Set<GregRRendicontazionePreg1Utereg1> getGregRRendicontazionePreg1Utereg1s() {
		return this.gregRRendicontazionePreg1Utereg1s;
	}

	public void setGregRRendicontazionePreg1Utereg1s(Set<GregRRendicontazionePreg1Utereg1> gregRRendicontazionePreg1Utereg1s) {
		this.gregRRendicontazionePreg1Utereg1s = gregRRendicontazionePreg1Utereg1s;
	}

	public GregRRendicontazionePreg1Utereg1 addGregRRendicontazionePreg1Utereg1(GregRRendicontazionePreg1Utereg1 gregRRendicontazionePreg1Utereg1) {
		getGregRRendicontazionePreg1Utereg1s().add(gregRRendicontazionePreg1Utereg1);
		gregRRendicontazionePreg1Utereg1.setGregTRendicontazioneEnte(this);

		return gregRRendicontazionePreg1Utereg1;
	}

	public GregRRendicontazionePreg1Utereg1 removeGregRRendicontazionePreg1Utereg1(GregRRendicontazionePreg1Utereg1 gregRRendicontazionePreg1Utereg1) {
		getGregRRendicontazionePreg1Utereg1s().remove(gregRRendicontazionePreg1Utereg1);
		gregRRendicontazionePreg1Utereg1.setGregTRendicontazioneEnte(null);

		return gregRRendicontazionePreg1Utereg1;
	}

	public Set<GregRRendicontazionePreg2Utereg2> getGregRRendicontazionePreg2Utereg2s() {
		return this.gregRRendicontazionePreg2Utereg2s;
	}

	public void setGregRRendicontazionePreg2Utereg2s(Set<GregRRendicontazionePreg2Utereg2> gregRRendicontazionePreg2Utereg2s) {
		this.gregRRendicontazionePreg2Utereg2s = gregRRendicontazionePreg2Utereg2s;
	}

	public GregRRendicontazionePreg2Utereg2 addGregRRendicontazionePreg2Utereg2(GregRRendicontazionePreg2Utereg2 gregRRendicontazionePreg2Utereg2) {
		getGregRRendicontazionePreg2Utereg2s().add(gregRRendicontazionePreg2Utereg2);
		gregRRendicontazionePreg2Utereg2.setGregTRendicontazioneEnte(this);

		return gregRRendicontazionePreg2Utereg2;
	}

	public GregRRendicontazionePreg2Utereg2 removeGregRRendicontazionePreg2Utereg2(GregRRendicontazionePreg2Utereg2 gregRRendicontazionePreg2Utereg2) {
		getGregRRendicontazionePreg2Utereg2s().remove(gregRRendicontazionePreg2Utereg2);
		gregRRendicontazionePreg2Utereg2.setGregTRendicontazioneEnte(null);

		return gregRRendicontazionePreg2Utereg2;
	}

	public Set<GregRRendicontazioneReportReg1> getGregRRendicontazioneReportReg1s() {
		return this.gregRRendicontazioneReportReg1s;
	}

	public void setGregRRendicontazioneReportReg1s(Set<GregRRendicontazioneReportReg1> gregRRendicontazioneReportReg1s) {
		this.gregRRendicontazioneReportReg1s = gregRRendicontazioneReportReg1s;
	}

	public GregRRendicontazioneReportReg1 addGregRRendicontazioneReportReg1(GregRRendicontazioneReportReg1 gregRRendicontazioneReportReg1) {
		getGregRRendicontazioneReportReg1s().add(gregRRendicontazioneReportReg1);
		gregRRendicontazioneReportReg1.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneReportReg1;
	}

	public GregRRendicontazioneReportReg1 removeGregRRendicontazioneReportReg1(GregRRendicontazioneReportReg1 gregRRendicontazioneReportReg1) {
		getGregRRendicontazioneReportReg1s().remove(gregRRendicontazioneReportReg1);
		gregRRendicontazioneReportReg1.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneReportReg1;
	}

	public Set<GregRRendicontazioneSpesaMissioneProgrammaMacro> getGregRRendicontazioneSpesaMissioneProgrammaMacros() {
		return this.gregRRendicontazioneSpesaMissioneProgrammaMacros;
	}

	public void setGregRRendicontazioneSpesaMissioneProgrammaMacros(Set<GregRRendicontazioneSpesaMissioneProgrammaMacro> gregRRendicontazioneSpesaMissioneProgrammaMacros) {
		this.gregRRendicontazioneSpesaMissioneProgrammaMacros = gregRRendicontazioneSpesaMissioneProgrammaMacros;
	}

	public GregRRendicontazioneSpesaMissioneProgrammaMacro addGregRRendicontazioneSpesaMissioneProgrammaMacro(GregRRendicontazioneSpesaMissioneProgrammaMacro gregRRendicontazioneSpesaMissioneProgrammaMacro) {
		getGregRRendicontazioneSpesaMissioneProgrammaMacros().add(gregRRendicontazioneSpesaMissioneProgrammaMacro);
		gregRRendicontazioneSpesaMissioneProgrammaMacro.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneSpesaMissioneProgrammaMacro;
	}

	public GregRRendicontazioneSpesaMissioneProgrammaMacro removeGregRRendicontazioneSpesaMissioneProgrammaMacro(GregRRendicontazioneSpesaMissioneProgrammaMacro gregRRendicontazioneSpesaMissioneProgrammaMacro) {
		getGregRRendicontazioneSpesaMissioneProgrammaMacros().remove(gregRRendicontazioneSpesaMissioneProgrammaMacro);
		gregRRendicontazioneSpesaMissioneProgrammaMacro.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneSpesaMissioneProgrammaMacro;
	}

	public Set<GregTAllegatiRendicontazione> getGregTAllegatiRendicontaziones() {
		return this.gregTAllegatiRendicontaziones;
	}

	public void setGregTAllegatiRendicontaziones(Set<GregTAllegatiRendicontazione> gregTAllegatiRendicontaziones) {
		this.gregTAllegatiRendicontaziones = gregTAllegatiRendicontaziones;
	}

	public GregTAllegatiRendicontazione addGregTAllegatiRendicontazione(GregTAllegatiRendicontazione gregTAllegatiRendicontazione) {
		getGregTAllegatiRendicontaziones().add(gregTAllegatiRendicontazione);
		gregTAllegatiRendicontazione.setGregTRendicontazioneEnte(this);

		return gregTAllegatiRendicontazione;
	}

	public GregTAllegatiRendicontazione removeGregTAllegatiRendicontazione(GregTAllegatiRendicontazione gregTAllegatiRendicontazione) {
		getGregTAllegatiRendicontaziones().remove(gregTAllegatiRendicontazione);
		gregTAllegatiRendicontazione.setGregTRendicontazioneEnte(null);

		return gregTAllegatiRendicontazione;
	}

	public Set<GregTCausaleEnteComuneModA2> getGregTCausaleEnteComuneModA2s() {
		return this.gregTCausaleEnteComuneModA2s;
	}

	public void setGregTCausaleEnteComuneModA2s(Set<GregTCausaleEnteComuneModA2> gregTCausaleEnteComuneModA2s) {
		this.gregTCausaleEnteComuneModA2s = gregTCausaleEnteComuneModA2s;
	}

	public GregTCausaleEnteComuneModA2 addGregTCausaleEnteComuneModA2(GregTCausaleEnteComuneModA2 gregTCausaleEnteComuneModA2) {
		getGregTCausaleEnteComuneModA2s().add(gregTCausaleEnteComuneModA2);
		gregTCausaleEnteComuneModA2.setGregTRendicontazioneEnte(this);

		return gregTCausaleEnteComuneModA2;
	}

	public GregTCausaleEnteComuneModA2 removeGregTCausaleEnteComuneModA2(GregTCausaleEnteComuneModA2 gregTCausaleEnteComuneModA2) {
		getGregTCausaleEnteComuneModA2s().remove(gregTCausaleEnteComuneModA2);
		gregTCausaleEnteComuneModA2.setGregTRendicontazioneEnte(null);

		return gregTCausaleEnteComuneModA2;
	}

	public Set<GregTCronologia> getGregTCronologias() {
		return this.gregTCronologias;
	}

	public void setGregTCronologias(Set<GregTCronologia> gregTCronologias) {
		this.gregTCronologias = gregTCronologias;
	}

	public GregTCronologia addGregTCronologia(GregTCronologia gregTCronologia) {
		getGregTCronologias().add(gregTCronologia);
		gregTCronologia.setGregTRendicontazioneEnte(this);

		return gregTCronologia;
	}

	public GregTCronologia removeGregTCronologia(GregTCronologia gregTCronologia) {
		getGregTCronologias().remove(gregTCronologia);
		gregTCronologia.setGregTRendicontazioneEnte(null);

		return gregTCronologia;
	}

	public GregDStatoRendicontazione getGregDStatoRendicontazione() {
		return this.gregDStatoRendicontazione;
	}

	public void setGregDStatoRendicontazione(GregDStatoRendicontazione gregDStatoRendicontazione) {
		this.gregDStatoRendicontazione = gregDStatoRendicontazione;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori() {
		return this.gregTSchedeEntiGestori;
	}

	public void setGregTSchedeEntiGestori(GregTSchedeEntiGestori gregTSchedeEntiGestori) {
		this.gregTSchedeEntiGestori = gregTSchedeEntiGestori;
	}

	public Set<GregRRendicontazioneGiustificazioneFnps> getGregRRendicontazioneGiustificazioneFnps() {
		return this.gregRRendicontazioneGiustificazioneFnps;
	}

	public void setGregRRendicontazioneGiustificazioneFnps(Set<GregRRendicontazioneGiustificazioneFnps> gregRRendicontazioneGiustificazioneFnps) {
		this.gregRRendicontazioneGiustificazioneFnps = gregRRendicontazioneGiustificazioneFnps;
	}

	public GregRRendicontazioneGiustificazioneFnps addGregRRendicontazioneGiustificazioneFnp(GregRRendicontazioneGiustificazioneFnps gregRRendicontazioneGiustificazioneFnp) {
		getGregRRendicontazioneGiustificazioneFnps().add(gregRRendicontazioneGiustificazioneFnp);
		gregRRendicontazioneGiustificazioneFnp.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneGiustificazioneFnp;
	}

	public GregRRendicontazioneGiustificazioneFnps removeGregRRendicontazioneGiustificazioneFnp(GregRRendicontazioneGiustificazioneFnps gregRRendicontazioneGiustificazioneFnp) {
		getGregRRendicontazioneGiustificazioneFnps().remove(gregRRendicontazioneGiustificazioneFnp);
		gregRRendicontazioneGiustificazioneFnp.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneGiustificazioneFnp;
	}
	
	public Set<GregRRendicontazioneFondi> getGregRRendicontazioneFondis() {
		return this.gregRRendicontazioneFondis;
	}

	public void setGregRRendicontazioneFondis(Set<GregRRendicontazioneFondi> gregRRendicontazioneFondis) {
		this.gregRRendicontazioneFondis = gregRRendicontazioneFondis;
	}

	public GregRRendicontazioneFondi addGregRRendicontazioneFondi(GregRRendicontazioneFondi gregRRendicontazioneFondi) {
		getGregRRendicontazioneFondis().add(gregRRendicontazioneFondi);
		gregRRendicontazioneFondi.setGregTRendicontazioneEnte(this);

		return gregRRendicontazioneFondi;
	}

	public GregRRendicontazioneFondi removeGregRRendicontazioneFondi(GregRRendicontazioneFondi gregRRendicontazioneFondi) {
		getGregRRendicontazioneFondis().remove(gregRRendicontazioneFondi);
		gregRRendicontazioneFondi.setGregTRendicontazioneEnte(null);

		return gregRRendicontazioneFondi;
	}

	public Set<GregRSpeseFnps> getGregRSpeseFnps() {
		return this.gregRSpeseFnps;
	}

	public void setGregRSpeseFnps(Set<GregRSpeseFnps> gregRSpeseFnps) {
		this.gregRSpeseFnps = gregRSpeseFnps;
	}

	public GregRSpeseFnps addGregRSpeseFnps(GregRSpeseFnps gregRSpeseFnp) {
		getGregRSpeseFnps().add(gregRSpeseFnp);
		gregRSpeseFnp.setGregTRendicontazioneEnte(this);

		return gregRSpeseFnp;
	}

	public GregRSpeseFnps removeGregRSpeseFnps(GregRSpeseFnps gregRSpeseFnp) {
		getGregRSpeseFnps().remove(gregRSpeseFnp);
		gregRSpeseFnp.setGregTRendicontazioneEnte(null);

		return gregRSpeseFnp;
	}

}