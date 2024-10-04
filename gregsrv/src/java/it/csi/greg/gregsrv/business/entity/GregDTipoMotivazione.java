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
 * The persistent class for the greg_d_tipo_motivazione database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_motivazione")
@NamedQuery(name="GregDTipoMotivazione.findAll", query="SELECT g FROM GregDTipoMotivazione g where g.dataCancellazione IS NULL")
public class GregDTipoMotivazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_MOTIVAZIONE_IDTIPOMOTIVAZIONE_GENERATOR", sequenceName="GREG_D_TIPO_MOTIVAZIONE_ID_TIPO_MOTIVAZIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_MOTIVAZIONE_IDTIPOMOTIVAZIONE_GENERATOR")
	@Column(name="id_tipo_motivazione")
	private Integer idTipoMotivazione;

	@Column(name="cod_tipo_motivazione")
	private String codTipoMotivazione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_tipo_motivazione")
	private String descTipoMotivazione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDMotivazione
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoMotivazione")
	private Set<GregDMotivazione> gregDMotivaziones;

	public GregDTipoMotivazione() {
	}

	public Integer getIdTipoMotivazione() {
		return this.idTipoMotivazione;
	}

	public void setIdTipoMotivazione(Integer idTipoMotivazione) {
		this.idTipoMotivazione = idTipoMotivazione;
	}

	public String getCodTipoMotivazione() {
		return this.codTipoMotivazione;
	}

	public void setCodTipoMotivazione(String codTipoMotivazione) {
		this.codTipoMotivazione = codTipoMotivazione;
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

	public String getDescTipoMotivazione() {
		return this.descTipoMotivazione;
	}

	public void setDescTipoMotivazione(String descTipoMotivazione) {
		this.descTipoMotivazione = descTipoMotivazione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDMotivazione> getGregDMotivaziones() {
		return this.gregDMotivaziones;
	}

	public void setGregDMotivaziones(Set<GregDMotivazione> gregDMotivaziones) {
		this.gregDMotivaziones = gregDMotivaziones;
	}

	public GregDMotivazione addGregDMotivazione(GregDMotivazione gregDMotivazione) {
		getGregDMotivaziones().add(gregDMotivazione);
		gregDMotivazione.setGregDTipoMotivazione(this);

		return gregDMotivazione;
	}

	public GregDMotivazione removeGregDMotivazione(GregDMotivazione gregDMotivazione) {
		getGregDMotivaziones().remove(gregDMotivazione);
		gregDMotivazione.setGregDTipoMotivazione(null);

		return gregDMotivazione;
	}

}