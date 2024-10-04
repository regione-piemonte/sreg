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
 * The persistent class for the greg_t_nomenclatore_nazionale database table.
 * 
 */
@Entity
@Table(name="greg_t_nomenclatore_nazionale")
@NamedQuery(name="GregTNomenclatoreNazionale.findAll", query="SELECT g FROM GregTNomenclatoreNazionale g where g.dataCancellazione IS NULL")
public class GregTNomenclatoreNazionale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_NOMENCLATORE_NAZIONALE_IDNOMENCLATORENAZIONALE_GENERATOR", sequenceName="GREG_T_NOMENCLATORE_NAZIONALE_ID_NOMENCLATORE_NAZIONALE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_NOMENCLATORE_NAZIONALE_IDNOMENCLATORENAZIONALE_GENERATOR")
	@Column(name="id_nomenclatore_nazionale")
	private Integer idNomenclatoreNazionale;

	@Column(name="cod_nomenclatore_nazionale")
	private String codNomenclatoreNazionale;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="flag_presidio_residenziale")
	private String flagPresidioResidenziale;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRNomencNazPrestReg2
	@JsonIgnore
	@OneToMany(mappedBy="gregTNomenclatoreNazionale")
	private Set<GregRNomencNazPrestReg2> gregRNomencNazPrestReg2s;

	//bi-directional many-to-one association to GregDAssistenzaSanitaria
	@ManyToOne
	@JoinColumn(name="id_assistenza_sanitaria")
	private GregDAssistenzaSanitaria gregDAssistenzaSanitaria;

	//bi-directional many-to-one association to GregDClassificazionePresidio
	@ManyToOne
	@JoinColumn(name="id_classificazione_presidio")
	private GregDClassificazionePresidio gregDClassificazionePresidio;

	//bi-directional many-to-one association to GregDFunzionePresidio
	@ManyToOne
	@JoinColumn(name="id_funzione_presidio")
	private GregDFunzionePresidio gregDFunzionePresidio;

	//bi-directional many-to-one association to GregDMacroaree
	@ManyToOne
	@JoinColumn(name="id_macroarea")
	private GregDMacroaree gregDMacroaree;

	//bi-directional many-to-one association to GregDSottoaree
	@ManyToOne
	@JoinColumn(name="id_sottoarea")
	private GregDSottoaree gregDSottoaree;

	//bi-directional many-to-one association to GregDSottoareeDet
	@ManyToOne
	@JoinColumn(name="id_sottoarea_det")
	private GregDSottoareeDet gregDSottoareeDet;

	//bi-directional many-to-one association to GregDSottovoci
	@ManyToOne
	@JoinColumn(name="id_sottovoce")
	private GregDSottovoci gregDSottovoci;

	//bi-directional many-to-one association to GregDTipoPresidio
	@ManyToOne
	@JoinColumn(name="id_tipo_presidio")
	private GregDTipoPresidio gregDTipoPresidio;

	//bi-directional many-to-one association to GregDTipoResidenza
	@ManyToOne
	@JoinColumn(name="id_tipo_residenza")
	private GregDTipoResidenza gregDTipoResidenza;

	//bi-directional many-to-one association to GregDVoci
	@ManyToOne
	@JoinColumn(name="id_voce")
	private GregDVoci gregDVoci;

	public GregTNomenclatoreNazionale() {
	}

	public Integer getIdNomenclatoreNazionale() {
		return this.idNomenclatoreNazionale;
	}

	public void setIdNomenclatoreNazionale(Integer idNomenclatoreNazionale) {
		this.idNomenclatoreNazionale = idNomenclatoreNazionale;
	}

	public String getCodNomenclatoreNazionale() {
		return this.codNomenclatoreNazionale;
	}

	public void setCodNomenclatoreNazionale(String codNomenclatoreNazionale) {
		this.codNomenclatoreNazionale = codNomenclatoreNazionale;
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

	public String getFlagPresidioResidenziale() {
		return this.flagPresidioResidenziale;
	}

	public void setFlagPresidioResidenziale(String flagPresidioResidenziale) {
		this.flagPresidioResidenziale = flagPresidioResidenziale;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRNomencNazPrestReg2> getGregRNomencNazPrestReg2s() {
		return this.gregRNomencNazPrestReg2s;
	}

	public void setGregRNomencNazPrestReg2s(Set<GregRNomencNazPrestReg2> gregRNomencNazPrestReg2s) {
		this.gregRNomencNazPrestReg2s = gregRNomencNazPrestReg2s;
	}

	public GregRNomencNazPrestReg2 addGregRNomencNazPrestReg2(GregRNomencNazPrestReg2 gregRNomencNazPrestReg2) {
		getGregRNomencNazPrestReg2s().add(gregRNomencNazPrestReg2);
		gregRNomencNazPrestReg2.setGregTNomenclatoreNazionale(this);

		return gregRNomencNazPrestReg2;
	}

	public GregRNomencNazPrestReg2 removeGregRNomencNazPrestReg2(GregRNomencNazPrestReg2 gregRNomencNazPrestReg2) {
		getGregRNomencNazPrestReg2s().remove(gregRNomencNazPrestReg2);
		gregRNomencNazPrestReg2.setGregTNomenclatoreNazionale(null);

		return gregRNomencNazPrestReg2;
	}

	public GregDAssistenzaSanitaria getGregDAssistenzaSanitaria() {
		return this.gregDAssistenzaSanitaria;
	}

	public void setGregDAssistenzaSanitaria(GregDAssistenzaSanitaria gregDAssistenzaSanitaria) {
		this.gregDAssistenzaSanitaria = gregDAssistenzaSanitaria;
	}

	public GregDClassificazionePresidio getGregDClassificazionePresidio() {
		return this.gregDClassificazionePresidio;
	}

	public void setGregDClassificazionePresidio(GregDClassificazionePresidio gregDClassificazionePresidio) {
		this.gregDClassificazionePresidio = gregDClassificazionePresidio;
	}

	public GregDFunzionePresidio getGregDFunzionePresidio() {
		return this.gregDFunzionePresidio;
	}

	public void setGregDFunzionePresidio(GregDFunzionePresidio gregDFunzionePresidio) {
		this.gregDFunzionePresidio = gregDFunzionePresidio;
	}

	public GregDMacroaree getGregDMacroaree() {
		return this.gregDMacroaree;
	}

	public void setGregDMacroaree(GregDMacroaree gregDMacroaree) {
		this.gregDMacroaree = gregDMacroaree;
	}

	public GregDSottoaree getGregDSottoaree() {
		return this.gregDSottoaree;
	}

	public void setGregDSottoaree(GregDSottoaree gregDSottoaree) {
		this.gregDSottoaree = gregDSottoaree;
	}

	public GregDSottoareeDet getGregDSottoareeDet() {
		return this.gregDSottoareeDet;
	}

	public void setGregDSottoareeDet(GregDSottoareeDet gregDSottoareeDet) {
		this.gregDSottoareeDet = gregDSottoareeDet;
	}

	public GregDSottovoci getGregDSottovoci() {
		return this.gregDSottovoci;
	}

	public void setGregDSottovoci(GregDSottovoci gregDSottovoci) {
		this.gregDSottovoci = gregDSottovoci;
	}

	public GregDTipoPresidio getGregDTipoPresidio() {
		return this.gregDTipoPresidio;
	}

	public void setGregDTipoPresidio(GregDTipoPresidio gregDTipoPresidio) {
		this.gregDTipoPresidio = gregDTipoPresidio;
	}

	public GregDTipoResidenza getGregDTipoResidenza() {
		return this.gregDTipoResidenza;
	}

	public void setGregDTipoResidenza(GregDTipoResidenza gregDTipoResidenza) {
		this.gregDTipoResidenza = gregDTipoResidenza;
	}

	public GregDVoci getGregDVoci() {
		return this.gregDVoci;
	}

	public void setGregDVoci(GregDVoci gregDVoci) {
		this.gregDVoci = gregDVoci;
	}

}