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
 * The persistent class for the greg_t_prestazioni_regionali_1 database table.
 * 
 */
@Entity
@Table(name="greg_t_prestazioni_regionali_1")
@NamedQueries({
	@NamedQuery(name="GregTPrestazioniRegionali1.findByIdNotDeleted", query="SELECT g FROM GregTPrestazioniRegionali1 g WHERE g.idPrestReg1 = :idPrestazione AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTPrestazioniRegionali1.findAll", query="SELECT g FROM GregTPrestazioniRegionali1 g"),
	@NamedQuery(name="GregTPrestazioniRegionali1.findAllNotDeleted", query="SELECT g FROM GregTPrestazioniRegionali1 g WHERE g.dataCancellazione IS NULL"),
})
public class GregTPrestazioniRegionali1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_PRESTAZIONI_REGIONALI_1_IDPRESTREG1_GENERATOR", sequenceName="GREG_T_PRESTAZIONI_REGIONALI_1_ID_PREST_REG_1_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_PRESTAZIONI_REGIONALI_1_IDPRESTREG1_GENERATOR")
	@Column(name="id_prest_reg_1")
	private Integer idPrestReg1;

	@Column(name="cod_prest_reg_1")
	private String codPrestReg1;

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

	@Column(name="des_prest_reg_1")
	private String desPrestReg1;

	private String informativa;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRCartaServiziPreg1
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRCartaServiziPreg1> gregRCartaServiziPreg1s;

	//bi-directional many-to-one association to GregRPreg1DisabilitaTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPreg1DisabilitaTargetUtenza> gregRPreg1DisabilitaTargetUtenzas;

	//bi-directional many-to-one association to GregRPrestReg1MacroaggregatiBilancio
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPrestReg1MacroaggregatiBilancio> gregRPrestReg1MacroaggregatiBilancios;

	//bi-directional many-to-one association to GregRPrestReg1PrestMinist
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPrestReg1PrestMinist> gregRPrestReg1PrestMinists;

	//bi-directional many-to-one association to GregRPrestReg1PrestReg2
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPrestReg1PrestReg2> gregRPrestReg1PrestReg2s;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeModc
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPrestReg1UtenzeModc> gregRPrestReg1UtenzeModcs;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPrestReg1UtenzeRegionali1> gregRPrestReg1UtenzeRegionali1s;

	//bi-directional many-to-one association to GregRPrestazioniRegionali1TipoStruttura
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregRPrestazioniRegionali1TipoStruttura> gregRPrestazioniRegionali1TipoStrutturas;

	//bi-directional many-to-one association to GregDTipoStruttura
	@ManyToOne
	@JoinColumn(name="id_tipo_struttura")
	private GregDTipoStruttura gregDTipoStruttura;

	//bi-directional many-to-one association to GregDTipologia
	@ManyToOne
	@JoinColumn(name="id_tipologia")
	private GregDTipologia gregDTipologia;

	//bi-directional many-to-one association to GregDTipologiaQuota
	@ManyToOne
	@JoinColumn(name="id_tipologia_quota")
	private GregDTipologiaQuota gregDTipologiaQuota;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="collegato_a_id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniRegionali1")
	private Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s;

	public GregTPrestazioniRegionali1() {
	}

	public Integer getIdPrestReg1() {
		return this.idPrestReg1;
	}

	public void setIdPrestReg1(Integer idPrestReg1) {
		this.idPrestReg1 = idPrestReg1;
	}

	public String getCodPrestReg1() {
		return this.codPrestReg1;
	}

	public void setCodPrestReg1(String codPrestReg1) {
		this.codPrestReg1 = codPrestReg1;
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

	public String getDesPrestReg1() {
		return this.desPrestReg1;
	}

	public void setDesPrestReg1(String desPrestReg1) {
		this.desPrestReg1 = desPrestReg1;
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

	public Set<GregRCartaServiziPreg1> getGregRCartaServiziPreg1s() {
		return this.gregRCartaServiziPreg1s;
	}

	public void setGregRCartaServiziPreg1s(Set<GregRCartaServiziPreg1> gregRCartaServiziPreg1s) {
		this.gregRCartaServiziPreg1s = gregRCartaServiziPreg1s;
	}

	public GregRCartaServiziPreg1 addGregRCartaServiziPreg1(GregRCartaServiziPreg1 gregRCartaServiziPreg1) {
		getGregRCartaServiziPreg1s().add(gregRCartaServiziPreg1);
		gregRCartaServiziPreg1.setGregTPrestazioniRegionali1(this);

		return gregRCartaServiziPreg1;
	}

	public GregRCartaServiziPreg1 removeGregRCartaServiziPreg1(GregRCartaServiziPreg1 gregRCartaServiziPreg1) {
		getGregRCartaServiziPreg1s().remove(gregRCartaServiziPreg1);
		gregRCartaServiziPreg1.setGregTPrestazioniRegionali1(null);

		return gregRCartaServiziPreg1;
	}

	public Set<GregRPreg1DisabilitaTargetUtenza> getGregRPreg1DisabilitaTargetUtenzas() {
		return this.gregRPreg1DisabilitaTargetUtenzas;
	}

	public void setGregRPreg1DisabilitaTargetUtenzas(Set<GregRPreg1DisabilitaTargetUtenza> gregRPreg1DisabilitaTargetUtenzas) {
		this.gregRPreg1DisabilitaTargetUtenzas = gregRPreg1DisabilitaTargetUtenzas;
	}

	public GregRPreg1DisabilitaTargetUtenza addGregRPreg1DisabilitaTargetUtenza(GregRPreg1DisabilitaTargetUtenza gregRPreg1DisabilitaTargetUtenza) {
		getGregRPreg1DisabilitaTargetUtenzas().add(gregRPreg1DisabilitaTargetUtenza);
		gregRPreg1DisabilitaTargetUtenza.setGregTPrestazioniRegionali1(this);

		return gregRPreg1DisabilitaTargetUtenza;
	}

	public GregRPreg1DisabilitaTargetUtenza removeGregRPreg1DisabilitaTargetUtenza(GregRPreg1DisabilitaTargetUtenza gregRPreg1DisabilitaTargetUtenza) {
		getGregRPreg1DisabilitaTargetUtenzas().remove(gregRPreg1DisabilitaTargetUtenza);
		gregRPreg1DisabilitaTargetUtenza.setGregTPrestazioniRegionali1(null);

		return gregRPreg1DisabilitaTargetUtenza;
	}

	public Set<GregRPrestReg1MacroaggregatiBilancio> getGregRPrestReg1MacroaggregatiBilancios() {
		return this.gregRPrestReg1MacroaggregatiBilancios;
	}

	public void setGregRPrestReg1MacroaggregatiBilancios(Set<GregRPrestReg1MacroaggregatiBilancio> gregRPrestReg1MacroaggregatiBilancios) {
		this.gregRPrestReg1MacroaggregatiBilancios = gregRPrestReg1MacroaggregatiBilancios;
	}

	public GregRPrestReg1MacroaggregatiBilancio addGregRPrestReg1MacroaggregatiBilancio(GregRPrestReg1MacroaggregatiBilancio gregRPrestReg1MacroaggregatiBilancio) {
		getGregRPrestReg1MacroaggregatiBilancios().add(gregRPrestReg1MacroaggregatiBilancio);
		gregRPrestReg1MacroaggregatiBilancio.setGregTPrestazioniRegionali1(this);

		return gregRPrestReg1MacroaggregatiBilancio;
	}

	public GregRPrestReg1MacroaggregatiBilancio removeGregRPrestReg1MacroaggregatiBilancio(GregRPrestReg1MacroaggregatiBilancio gregRPrestReg1MacroaggregatiBilancio) {
		getGregRPrestReg1MacroaggregatiBilancios().remove(gregRPrestReg1MacroaggregatiBilancio);
		gregRPrestReg1MacroaggregatiBilancio.setGregTPrestazioniRegionali1(null);

		return gregRPrestReg1MacroaggregatiBilancio;
	}

	public Set<GregRPrestReg1PrestMinist> getGregRPrestReg1PrestMinists() {
		return this.gregRPrestReg1PrestMinists;
	}

	public void setGregRPrestReg1PrestMinists(Set<GregRPrestReg1PrestMinist> gregRPrestReg1PrestMinists) {
		this.gregRPrestReg1PrestMinists = gregRPrestReg1PrestMinists;
	}

	public GregRPrestReg1PrestMinist addGregRPrestReg1PrestMinist(GregRPrestReg1PrestMinist gregRPrestReg1PrestMinist) {
		getGregRPrestReg1PrestMinists().add(gregRPrestReg1PrestMinist);
		gregRPrestReg1PrestMinist.setGregTPrestazioniRegionali1(this);

		return gregRPrestReg1PrestMinist;
	}

	public GregRPrestReg1PrestMinist removeGregRPrestReg1PrestMinist(GregRPrestReg1PrestMinist gregRPrestReg1PrestMinist) {
		getGregRPrestReg1PrestMinists().remove(gregRPrestReg1PrestMinist);
		gregRPrestReg1PrestMinist.setGregTPrestazioniRegionali1(null);

		return gregRPrestReg1PrestMinist;
	}

	public Set<GregRPrestReg1PrestReg2> getGregRPrestReg1PrestReg2s() {
		return this.gregRPrestReg1PrestReg2s;
	}

	public void setGregRPrestReg1PrestReg2s(Set<GregRPrestReg1PrestReg2> gregRPrestReg1PrestReg2s) {
		this.gregRPrestReg1PrestReg2s = gregRPrestReg1PrestReg2s;
	}

	public GregRPrestReg1PrestReg2 addGregRPrestReg1PrestReg2(GregRPrestReg1PrestReg2 gregRPrestReg1PrestReg2) {
		getGregRPrestReg1PrestReg2s().add(gregRPrestReg1PrestReg2);
		gregRPrestReg1PrestReg2.setGregTPrestazioniRegionali1(this);

		return gregRPrestReg1PrestReg2;
	}

	public GregRPrestReg1PrestReg2 removeGregRPrestReg1PrestReg2(GregRPrestReg1PrestReg2 gregRPrestReg1PrestReg2) {
		getGregRPrestReg1PrestReg2s().remove(gregRPrestReg1PrestReg2);
		gregRPrestReg1PrestReg2.setGregTPrestazioniRegionali1(null);

		return gregRPrestReg1PrestReg2;
	}

	public Set<GregRPrestReg1UtenzeModc> getGregRPrestReg1UtenzeModcs() {
		return this.gregRPrestReg1UtenzeModcs;
	}

	public void setGregRPrestReg1UtenzeModcs(Set<GregRPrestReg1UtenzeModc> gregRPrestReg1UtenzeModcs) {
		this.gregRPrestReg1UtenzeModcs = gregRPrestReg1UtenzeModcs;
	}

	public GregRPrestReg1UtenzeModc addGregRPrestReg1UtenzeModc(GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc) {
		getGregRPrestReg1UtenzeModcs().add(gregRPrestReg1UtenzeModc);
		gregRPrestReg1UtenzeModc.setGregTPrestazioniRegionali1(this);

		return gregRPrestReg1UtenzeModc;
	}

	public GregRPrestReg1UtenzeModc removeGregRPrestReg1UtenzeModc(GregRPrestReg1UtenzeModc gregRPrestReg1UtenzeModc) {
		getGregRPrestReg1UtenzeModcs().remove(gregRPrestReg1UtenzeModc);
		gregRPrestReg1UtenzeModc.setGregTPrestazioniRegionali1(null);

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
		gregRPrestReg1UtenzeRegionali1.setGregTPrestazioniRegionali1(this);

		return gregRPrestReg1UtenzeRegionali1;
	}

	public GregRPrestReg1UtenzeRegionali1 removeGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		getGregRPrestReg1UtenzeRegionali1s().remove(gregRPrestReg1UtenzeRegionali1);
		gregRPrestReg1UtenzeRegionali1.setGregTPrestazioniRegionali1(null);

		return gregRPrestReg1UtenzeRegionali1;
	}

	public Set<GregRPrestazioniRegionali1TipoStruttura> getGregRPrestazioniRegionali1TipoStrutturas() {
		return this.gregRPrestazioniRegionali1TipoStrutturas;
	}

	public void setGregRPrestazioniRegionali1TipoStrutturas(Set<GregRPrestazioniRegionali1TipoStruttura> gregRPrestazioniRegionali1TipoStrutturas) {
		this.gregRPrestazioniRegionali1TipoStrutturas = gregRPrestazioniRegionali1TipoStrutturas;
	}

	public GregRPrestazioniRegionali1TipoStruttura addGregRPrestazioniRegionali1TipoStruttura(GregRPrestazioniRegionali1TipoStruttura gregRPrestazioniRegionali1TipoStruttura) {
		getGregRPrestazioniRegionali1TipoStrutturas().add(gregRPrestazioniRegionali1TipoStruttura);
		gregRPrestazioniRegionali1TipoStruttura.setGregTPrestazioniRegionali1(this);

		return gregRPrestazioniRegionali1TipoStruttura;
	}

	public GregRPrestazioniRegionali1TipoStruttura removeGregRPrestazioniRegionali1TipoStruttura(GregRPrestazioniRegionali1TipoStruttura gregRPrestazioniRegionali1TipoStruttura) {
		getGregRPrestazioniRegionali1TipoStrutturas().remove(gregRPrestazioniRegionali1TipoStruttura);
		gregRPrestazioniRegionali1TipoStruttura.setGregTPrestazioniRegionali1(null);

		return gregRPrestazioniRegionali1TipoStruttura;
	}

	public GregDTipoStruttura getGregDTipoStruttura() {
		return this.gregDTipoStruttura;
	}

	public void setGregDTipoStruttura(GregDTipoStruttura gregDTipoStruttura) {
		this.gregDTipoStruttura = gregDTipoStruttura;
	}

	public GregDTipologia getGregDTipologia() {
		return this.gregDTipologia;
	}

	public void setGregDTipologia(GregDTipologia gregDTipologia) {
		this.gregDTipologia = gregDTipologia;
	}

	public GregDTipologiaQuota getGregDTipologiaQuota() {
		return this.gregDTipologiaQuota;
	}

	public void setGregDTipologiaQuota(GregDTipologiaQuota gregDTipologiaQuota) {
		this.gregDTipologiaQuota = gregDTipologiaQuota;
	}

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public Set<GregTPrestazioniRegionali1> getGregTPrestazioniRegionali1s() {
		return this.gregTPrestazioniRegionali1s;
	}

	public void setGregTPrestazioniRegionali1s(Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s) {
		this.gregTPrestazioniRegionali1s = gregTPrestazioniRegionali1s;
	}

	public GregTPrestazioniRegionali1 addGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().add(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregTPrestazioniRegionali1(this);

		return gregTPrestazioniRegionali1;
	}

	public GregTPrestazioniRegionali1 removeGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().remove(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregTPrestazioniRegionali1(null);

		return gregTPrestazioniRegionali1;
	}

}