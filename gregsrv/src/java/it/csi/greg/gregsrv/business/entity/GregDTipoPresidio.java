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
 * The persistent class for the greg_d_tipo_presidio database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_presidio")
@NamedQuery(name="GregDTipoPresidio.findAll", query="SELECT g FROM GregDTipoPresidio g where g.dataCancellazione IS NULL")
public class GregDTipoPresidio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_PRESIDIO_IDTIPOPRESIDIO_GENERATOR", sequenceName="GREG_D_TIPO_PRESIDIO_ID_TIPO_PRESIDIO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_PRESIDIO_IDTIPOPRESIDIO_GENERATOR")
	@Column(name="id_tipo_presidio")
	private Integer idTipoPresidio;

	@Column(name="cod_tipo_presidio")
	private String codTipoPresidio;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_tipo_presidio")
	private String desTipoPresidio;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoPresidio")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDTipoPresidio() {
	}

	public Integer getIdTipoPresidio() {
		return this.idTipoPresidio;
	}

	public void setIdTipoPresidio(Integer idTipoPresidio) {
		this.idTipoPresidio = idTipoPresidio;
	}

	public String getCodTipoPresidio() {
		return this.codTipoPresidio;
	}

	public void setCodTipoPresidio(String codTipoPresidio) {
		this.codTipoPresidio = codTipoPresidio;
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

	public String getDesTipoPresidio() {
		return this.desTipoPresidio;
	}

	public void setDesTipoPresidio(String desTipoPresidio) {
		this.desTipoPresidio = desTipoPresidio;
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
		gregTNomenclatoreNazionale.setGregDTipoPresidio(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDTipoPresidio(null);

		return gregTNomenclatoreNazionale;
	}

}