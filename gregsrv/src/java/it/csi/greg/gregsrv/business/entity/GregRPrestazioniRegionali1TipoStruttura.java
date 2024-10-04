/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_prestazioni_regionali_1_tipo_struttura database table.
 * 
 */
@Entity
@Table(name="greg_r_prestazioni_regionali_1_tipo_struttura")
@NamedQuery(name="GregRPrestazioniRegionali1TipoStruttura.findAll", query="SELECT g FROM GregRPrestazioniRegionali1TipoStruttura g where g.dataCancellazione IS NULL")
public class GregRPrestazioniRegionali1TipoStruttura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PRESTAZIONI_REGIONALI_1_TIPO_STRUTTURA_IDPRESTAZIONIREGIONALI1TIPOSTRUTTURA_GENERATOR", sequenceName="GREG_R_PRESTAZIONI_REGIONALI__ID_PRESTAZIONI_REGIONALI_1_TI_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PRESTAZIONI_REGIONALI_1_TIPO_STRUTTURA_IDPRESTAZIONIREGIONALI1TIPOSTRUTTURA_GENERATOR")
	@Column(name="id_prestazioni_regionali_1_tipo_struttura")
	private Integer idPrestazioniRegionali1TipoStruttura;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTipoStruttura
	@ManyToOne
	@JoinColumn(name="id_tipo_struttura")
	private GregDTipoStruttura gregDTipoStruttura;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	public GregRPrestazioniRegionali1TipoStruttura() {
	}

	public Integer getIdPrestazioniRegionali1TipoStruttura() {
		return this.idPrestazioniRegionali1TipoStruttura;
	}

	public void setIdPrestazioniRegionali1TipoStruttura(Integer idPrestazioniRegionali1TipoStruttura) {
		this.idPrestazioniRegionali1TipoStruttura = idPrestazioniRegionali1TipoStruttura;
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

	public GregDTipoStruttura getGregDTipoStruttura() {
		return this.gregDTipoStruttura;
	}

	public void setGregDTipoStruttura(GregDTipoStruttura gregDTipoStruttura) {
		this.gregDTipoStruttura = gregDTipoStruttura;
	}

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

}