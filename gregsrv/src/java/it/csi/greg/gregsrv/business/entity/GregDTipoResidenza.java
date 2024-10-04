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
 * The persistent class for the greg_d_tipo_residenza database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_residenza")
@NamedQuery(name="GregDTipoResidenza.findAll", query="SELECT g FROM GregDTipoResidenza g where g.dataCancellazione IS NULL")
public class GregDTipoResidenza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_RESIDENZA_IDTIPORESIDENZA_GENERATOR", sequenceName="GREG_D_TIPO_RESIDENZA_ID_TIPO_RESIDENZA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_RESIDENZA_IDTIPORESIDENZA_GENERATOR")
	@Column(name="id_tipo_residenza")
	private Integer idTipoResidenza;

	@Column(name="cod_tipo_residenza")
	private String codTipoResidenza;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_tipo_residenza")
	private String desTipoResidenza;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoResidenza")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDTipoResidenza() {
	}

	public Integer getIdTipoResidenza() {
		return this.idTipoResidenza;
	}

	public void setIdTipoResidenza(Integer idTipoResidenza) {
		this.idTipoResidenza = idTipoResidenza;
	}

	public String getCodTipoResidenza() {
		return this.codTipoResidenza;
	}

	public void setCodTipoResidenza(String codTipoResidenza) {
		this.codTipoResidenza = codTipoResidenza;
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

	public String getDesTipoResidenza() {
		return this.desTipoResidenza;
	}

	public void setDesTipoResidenza(String desTipoResidenza) {
		this.desTipoResidenza = desTipoResidenza;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregTNomenclatoreNazionale> getGregTNomenclatoreNazionales() {
		return this.gregTNomenclatoreNazionales;
	}

	public void setGregTNomenclatoreNazionales(Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales) {
		this.gregTNomenclatoreNazionales = gregTNomenclatoreNazionales;
	}

	public GregTNomenclatoreNazionale addGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().add(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDTipoResidenza(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDTipoResidenza(null);

		return gregTNomenclatoreNazionale;
	}

}