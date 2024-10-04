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
 * The persistent class for the greg_t_prestazioni_regionali_2 database table.
 * 
 */
@Entity
@Table(name="greg_t_prestazioni_regionali_2")
@NamedQueries({
	@NamedQuery(name="GregTPrestazioniRegionali2.findAll", query="SELECT g FROM GregTPrestazioniRegionali2 g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTPrestazioniRegionali2.findByIdPrest2NotDeleted", query="SELECT g FROM GregTPrestazioniRegionali2 g WHERE g.idPrestReg2= :idPrestazione2 AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTPrestazioniRegionali2.findByIdPrest2NotDeletedDataInizio", query="SELECT g FROM GregTPrestazioniRegionali2 g WHERE g.idPrestReg2= :idPrestazione2 AND g.dataInizioValidita = :dataInizio AND g.dataCancellazione IS NULL")
})

public class GregTPrestazioniRegionali2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_PRESTAZIONI_REGIONALI_2_IDPRESTREG2_GENERATOR", sequenceName="GREG_T_PRESTAZIONI_REGIONALI_2_ID_PREST_REG_2_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_PRESTAZIONI_REGIONALI_2_IDPRESTREG2_GENERATOR")
	@Column(name="id_prest_reg_2")
	private Integer idPrestReg2;

	@Column(name="cod_prest_reg_2")
	private String codPrestReg2;

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

	@Column(name="des_prest_reg_2")
	private String desPrestReg2;

	private String informativa;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRCatUteVocePrestReg2Istat
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali2")
	private Set<GregRCatUteVocePrestReg2Istat> gregRCatUteVocePrestReg2Istats;

	//bi-directional many-to-one association to GregRNomencNazPrestReg2
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali2")
	private Set<GregRNomencNazPrestReg2> gregRNomencNazPrestReg2s;

	//bi-directional many-to-one association to GregRPrestReg1PrestReg2
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali2")
	private Set<GregRPrestReg1PrestReg2> gregRPrestReg1PrestReg2s;

	//bi-directional many-to-one association to GregRPrestReg2UtenzeRegionali2
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali2")
	private Set<GregRPrestReg2UtenzeRegionali2> gregRPrestReg2UtenzeRegionali2s;

	//bi-directional many-to-one association to GregDTipologia
	@ManyToOne
	@JoinColumn(name="id_tipologia")
	private GregDTipologia gregDTipologia;

	public GregTPrestazioniRegionali2() {
	}

	public Integer getIdPrestReg2() {
		return this.idPrestReg2;
	}

	public void setIdPrestReg2(Integer idPrestReg2) {
		this.idPrestReg2 = idPrestReg2;
	}

	public String getCodPrestReg2() {
		return this.codPrestReg2;
	}

	public void setCodPrestReg2(String codPrestReg2) {
		this.codPrestReg2 = codPrestReg2;
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

	public String getDesPrestReg2() {
		return this.desPrestReg2;
	}

	public void setDesPrestReg2(String desPrestReg2) {
		this.desPrestReg2 = desPrestReg2;
	}

	public String getInformativa() {
		return this.informativa;
	}

	public void setInformativa(String informativa) {
		this.informativa = informativa;
	}

	public Integer getOrdinamento() {
		return this.ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRCatUteVocePrestReg2Istat> getGregRCatUteVocePrestReg2Istats() {
		return this.gregRCatUteVocePrestReg2Istats;
	}

	public void setGregRCatUteVocePrestReg2Istats(Set<GregRCatUteVocePrestReg2Istat> gregRCatUteVocePrestReg2Istats) {
		this.gregRCatUteVocePrestReg2Istats = gregRCatUteVocePrestReg2Istats;
	}

	public GregRCatUteVocePrestReg2Istat addGregRCatUteVocePrestReg2Istat(GregRCatUteVocePrestReg2Istat gregRCatUteVocePrestReg2Istat) {
		getGregRCatUteVocePrestReg2Istats().add(gregRCatUteVocePrestReg2Istat);
		gregRCatUteVocePrestReg2Istat.setGregTPrestazioniRegionali2(this);

		return gregRCatUteVocePrestReg2Istat;
	}

	public GregRCatUteVocePrestReg2Istat removeGregRCatUteVocePrestReg2Istat(GregRCatUteVocePrestReg2Istat gregRCatUteVocePrestReg2Istat) {
		getGregRCatUteVocePrestReg2Istats().remove(gregRCatUteVocePrestReg2Istat);
		gregRCatUteVocePrestReg2Istat.setGregTPrestazioniRegionali2(null);

		return gregRCatUteVocePrestReg2Istat;
	}

	public Set<GregRNomencNazPrestReg2> getGregRNomencNazPrestReg2s() {
		return this.gregRNomencNazPrestReg2s;
	}

	public void setGregRNomencNazPrestReg2s(Set<GregRNomencNazPrestReg2> gregRNomencNazPrestReg2s) {
		this.gregRNomencNazPrestReg2s = gregRNomencNazPrestReg2s;
	}

	public GregRNomencNazPrestReg2 addGregRNomencNazPrestReg2(GregRNomencNazPrestReg2 gregRNomencNazPrestReg2) {
		getGregRNomencNazPrestReg2s().add(gregRNomencNazPrestReg2);
		gregRNomencNazPrestReg2.setGregTPrestazioniRegionali2(this);

		return gregRNomencNazPrestReg2;
	}

	public GregRNomencNazPrestReg2 removeGregRNomencNazPrestReg2(GregRNomencNazPrestReg2 gregRNomencNazPrestReg2) {
		getGregRNomencNazPrestReg2s().remove(gregRNomencNazPrestReg2);
		gregRNomencNazPrestReg2.setGregTPrestazioniRegionali2(null);

		return gregRNomencNazPrestReg2;
	}

	public Set<GregRPrestReg1PrestReg2> getGregRPrestReg1PrestReg2s() {
		return this.gregRPrestReg1PrestReg2s;
	}

	public void setGregRPrestReg1PrestReg2s(Set<GregRPrestReg1PrestReg2> gregRPrestReg1PrestReg2s) {
		this.gregRPrestReg1PrestReg2s = gregRPrestReg1PrestReg2s;
	}

	public GregRPrestReg1PrestReg2 addGregRPrestReg1PrestReg2(GregRPrestReg1PrestReg2 gregRPrestReg1PrestReg2) {
		getGregRPrestReg1PrestReg2s().add(gregRPrestReg1PrestReg2);
		gregRPrestReg1PrestReg2.setGregTPrestazioniRegionali2(this);

		return gregRPrestReg1PrestReg2;
	}

	public GregRPrestReg1PrestReg2 removeGregRPrestReg1PrestReg2(GregRPrestReg1PrestReg2 gregRPrestReg1PrestReg2) {
		getGregRPrestReg1PrestReg2s().remove(gregRPrestReg1PrestReg2);
		gregRPrestReg1PrestReg2.setGregTPrestazioniRegionali2(null);

		return gregRPrestReg1PrestReg2;
	}

	public Set<GregRPrestReg2UtenzeRegionali2> getGregRPrestReg2UtenzeRegionali2s() {
		return this.gregRPrestReg2UtenzeRegionali2s;
	}

	public void setGregRPrestReg2UtenzeRegionali2s(Set<GregRPrestReg2UtenzeRegionali2> gregRPrestReg2UtenzeRegionali2s) {
		this.gregRPrestReg2UtenzeRegionali2s = gregRPrestReg2UtenzeRegionali2s;
	}

	public GregRPrestReg2UtenzeRegionali2 addGregRPrestReg2UtenzeRegionali2(GregRPrestReg2UtenzeRegionali2 gregRPrestReg2UtenzeRegionali2) {
		getGregRPrestReg2UtenzeRegionali2s().add(gregRPrestReg2UtenzeRegionali2);
		gregRPrestReg2UtenzeRegionali2.setGregTPrestazioniRegionali2(this);

		return gregRPrestReg2UtenzeRegionali2;
	}

	public GregRPrestReg2UtenzeRegionali2 removeGregRPrestReg2UtenzeRegionali2(GregRPrestReg2UtenzeRegionali2 gregRPrestReg2UtenzeRegionali2) {
		getGregRPrestReg2UtenzeRegionali2s().remove(gregRPrestReg2UtenzeRegionali2);
		gregRPrestReg2UtenzeRegionali2.setGregTPrestazioniRegionali2(null);

		return gregRPrestReg2UtenzeRegionali2;
	}

	public GregDTipologia getGregDTipologia() {
		return this.gregDTipologia;
	}

	public void setGregDTipologia(GregDTipologia gregDTipologia) {
		this.gregDTipologia = gregDTipologia;
	}

}