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
 * The persistent class for the greg_d_tipo_ente database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_ente")
@NamedQueries({
	@NamedQuery(name="GregDTipoEnte.findAll", query="SELECT g FROM GregDTipoEnte g ORDER BY g.desTipoEnte"),
	@NamedQuery(name="GregDTipoEnte.findByIdNotDeleted", query="SELECT g FROM GregDTipoEnte g WHERE g.idTipoEnte = :idTipoEnte AND g.dataCancellazione IS NULL ORDER BY g.desTipoEnte"),
	@NamedQuery(name="GregDTipoEnte.findAllNotDeleted", query="SELECT g FROM GregDTipoEnte g WHERE g.dataCancellazione IS NULL ORDER BY g.desTipoEnte"),
	@NamedQuery(name="GregDTipoEnte.findByCodNotDeleted", query="SELECT g FROM GregDTipoEnte g WHERE g.codTipoEnte = :codTipoEnte AND g.dataCancellazione IS NULL ORDER BY g.desTipoEnte")
})
public class GregDTipoEnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_ENTE_IDTIPOENTE_GENERATOR", sequenceName="GREG_D_TIPO_ENTE_ID_TIPO_ENTE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_ENTE_IDTIPOENTE_GENERATOR")
	@Column(name="id_tipo_ente")
	private Integer idTipoEnte;

	@Column(name="cod_tipo_ente")
	private String codTipoEnte;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_tipo_ente")
	private String desTipoEnte;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregREnteGestoreContatti
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoEnte")
	private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;

	public GregDTipoEnte() {
	}

	public Integer getIdTipoEnte() {
		return this.idTipoEnte;
	}

	public void setIdTipoEnte(Integer idTipoEnte) {
		this.idTipoEnte = idTipoEnte;
	}

	public String getCodTipoEnte() {
		return this.codTipoEnte;
	}

	public void setCodTipoEnte(String codTipoEnte) {
		this.codTipoEnte = codTipoEnte;
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

	public String getDesTipoEnte() {
		return this.desTipoEnte;
	}

	public void setDesTipoEnte(String desTipoEnte) {
		this.desTipoEnte = desTipoEnte;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregREnteGestoreContatti> getGregREnteGestoreContattis() {
		return this.gregREnteGestoreContattis;
	}

	public void setGregREnteGestoreContattis(Set<GregREnteGestoreContatti> gregREnteGestoreContattis) {
		this.gregREnteGestoreContattis = gregREnteGestoreContattis;
	}

	public GregREnteGestoreContatti addGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().add(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDTipoEnte(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDTipoEnte(null);

		return gregREnteGestoreContatti;
	}

}