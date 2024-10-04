/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the greg_d_fondi database table.
 * 
 */
@Entity
@Table(name="greg_d_fondi")
@NamedQuery(name="GregDFondi.findAll", query="SELECT g FROM GregDFondi g")
public class GregDFondi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_FONDI_IDFONDO_GENERATOR", sequenceName="GREG_D_FONDI_ID_FONDO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_FONDI_IDFONDO_GENERATOR")
	@Column(name="id_fondo")
	private Integer idFondo;

	@Column(name="altra_desc_fondo")
	private String altraDescFondo;

	@Column(name="cod_fondo")
	private String codFondo;

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

	@Column(name="desc_fondo")
	private String descFondo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTipologiaFondi
	@ManyToOne
	@JoinColumn(name="id_tipologia_fondo")
	private GregDTipologiaFondi gregDTipologiaFondi;

	//bi-directional many-to-one association to GregRRendicontazioneFondi
	@OneToMany(mappedBy="gregDFondi")
	private Set<GregRRendicontazioneFondi> gregRRendicontazioneFondis;

	//bi-directional many-to-one association to GregRSpeseFnp
	@OneToMany(mappedBy="gregDFondi")
	private Set<GregRSpeseFnps> gregRSpeseFnps;

	public GregDFondi() {
	}

	public Integer getIdFondo() {
		return this.idFondo;
	}

	public void setIdFondo(Integer idFondo) {
		this.idFondo = idFondo;
	}

	public String getAltraDescFondo() {
		return this.altraDescFondo;
	}

	public void setAltraDescFondo(String altraDescFondo) {
		this.altraDescFondo = altraDescFondo;
	}

	public String getCodFondo() {
		return this.codFondo;
	}

	public void setCodFondo(String codFondo) {
		this.codFondo = codFondo;
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

	public String getDescFondo() {
		return this.descFondo;
	}

	public void setDescFondo(String descFondo) {
		this.descFondo = descFondo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDTipologiaFondi getGregDTipologiaFondi() {
		return this.gregDTipologiaFondi;
	}

	public void setGregDTipologiaFondi(GregDTipologiaFondi gregDTipologiaFondi) {
		this.gregDTipologiaFondi = gregDTipologiaFondi;
	}

	public Set<GregRRendicontazioneFondi> getGregRRendicontazioneFondis() {
		return this.gregRRendicontazioneFondis;
	}

	public void setGregRRendicontazioneFondis(Set<GregRRendicontazioneFondi> gregRRendicontazioneFondis) {
		this.gregRRendicontazioneFondis = gregRRendicontazioneFondis;
	}

	public GregRRendicontazioneFondi addGregRRendicontazioneFondi(GregRRendicontazioneFondi gregRRendicontazioneFondi) {
		getGregRRendicontazioneFondis().add(gregRRendicontazioneFondi);
		gregRRendicontazioneFondi.setGregDFondi(this);

		return gregRRendicontazioneFondi;
	}

	public GregRRendicontazioneFondi removeGregRRendicontazioneFondi(GregRRendicontazioneFondi gregRRendicontazioneFondi) {
		getGregRRendicontazioneFondis().remove(gregRRendicontazioneFondi);
		gregRRendicontazioneFondi.setGregDFondi(null);

		return gregRRendicontazioneFondi;
	}

	public Set<GregRSpeseFnps> getGregRSpeseFnps() {
		return this.gregRSpeseFnps;
	}

	public void setGregRSpeseFnps(Set<GregRSpeseFnps> gregRSpeseFnps) {
		this.gregRSpeseFnps = gregRSpeseFnps;
	}

	public GregRSpeseFnps addGregRSpeseFnp(GregRSpeseFnps gregRSpeseFnp) {
		getGregRSpeseFnps().add(gregRSpeseFnp);
		gregRSpeseFnp.setGregDFondi(this);

		return gregRSpeseFnp;
	}

	public GregRSpeseFnps removeGregRSpeseFnp(GregRSpeseFnps gregRSpeseFnp) {
		getGregRSpeseFnps().remove(gregRSpeseFnp);
		gregRSpeseFnp.setGregDFondi(null);

		return gregRSpeseFnp;
	}

}