/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the greg_d_tipologia_fondi database table.
 * 
 */
@Entity
@Table(name="greg_d_tipologia_fondi")
@NamedQuery(name="GregDTipologiaFondi.findAll", query="SELECT g FROM GregDTipologiaFondi g")
public class GregDTipologiaFondi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPOLOGIA_FONDI_IDTIPOLOGIAFONDO_GENERATOR", sequenceName="GREG_D_TIPOLOGIA_FONDI_ID_TIPOLOGIA_FONDO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPOLOGIA_FONDI_IDTIPOLOGIAFONDO_GENERATOR")
	@Column(name="id_tipologia_fondo")
	private Integer idTipologiaFondo;

	@Column(name="cod_tipologia_fondo")
	private String codTipologiaFondo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_tipologia_fondo")
	private String descTipologiaFondo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDFondi
	@OneToMany(mappedBy="gregDTipologiaFondi")
	private List<GregDFondi> gregDFondis;

	public GregDTipologiaFondi() {
	}

	public Integer getIdTipologiaFondo() {
		return this.idTipologiaFondo;
	}

	public void setIdTipologiaFondo(Integer idTipologiaFondo) {
		this.idTipologiaFondo = idTipologiaFondo;
	}

	public String getCodTipologiaFondo() {
		return this.codTipologiaFondo;
	}

	public void setCodTipologiaFondo(String codTipologiaFondo) {
		this.codTipologiaFondo = codTipologiaFondo;
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

	public String getDescTipologiaFondo() {
		return this.descTipologiaFondo;
	}

	public void setDescTipologiaFondo(String descTipologiaFondo) {
		this.descTipologiaFondo = descTipologiaFondo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public List<GregDFondi> getGregDFondis() {
		return this.gregDFondis;
	}

	public void setGregDFondis(List<GregDFondi> gregDFondis) {
		this.gregDFondis = gregDFondis;
	}

	public GregDFondi addGregDFondi(GregDFondi gregDFondi) {
		getGregDFondis().add(gregDFondi);
		gregDFondi.setGregDTipologiaFondi(this);

		return gregDFondi;
	}

	public GregDFondi removeGregDFondi(GregDFondi gregDFondi) {
		getGregDFondis().remove(gregDFondi);
		gregDFondi.setGregDTipologiaFondi(null);

		return gregDFondi;
	}

}