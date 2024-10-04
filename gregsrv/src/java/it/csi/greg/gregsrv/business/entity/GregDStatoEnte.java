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
 * The persistent class for the greg_d_stato_ente database table.
 * 
 */
@Entity
@Table(name="greg_d_stato_ente")

@NamedQueries({
	@NamedQuery(name="GregDStatoEnte.findAll", query="SELECT g FROM GregDStatoEnte g"),
	@NamedQuery(name="GregDStatoEnte.findAllNotDeleted", query="SELECT g FROM GregDStatoEnte g WHERE g.dataCancellazione IS NULL ORDER BY g.descStatoEnte"),
	@NamedQuery(name="GregDStatoEnte.findStatobyCod", query="SELECT g FROM GregDStatoEnte g WHERE g.codStatoEnte = :codStato and g.dataCancellazione IS NULL ORDER BY g.descStatoEnte")
})
public class GregDStatoEnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_STATO_ENTE_IDSTATOENTE_GENERATOR", sequenceName="GREG_D_STATO_ENTE_ID_STATO_ENTE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_STATO_ENTE_IDSTATOENTE_GENERATOR")
	@Column(name="id_stato_ente")
	private Integer idStatoEnte;

	@Column(name="cod_stato_ente")
	private String codStatoEnte;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_stato_ente")
	private String descStatoEnte;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregREnteGestoreStatoEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregDStatoEnte")
	private Set<GregREnteGestoreStatoEnte> gregREnteGestoreStatoEntes;

	public GregDStatoEnte() {
	}

	public Integer getIdStatoEnte() {
		return this.idStatoEnte;
	}

	public void setIdStatoEnte(Integer idStatoEnte) {
		this.idStatoEnte = idStatoEnte;
	}

	public String getCodStatoEnte() {
		return this.codStatoEnte;
	}

	public void setCodStatoEnte(String codStatoEnte) {
		this.codStatoEnte = codStatoEnte;
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

	public String getDescStatoEnte() {
		return this.descStatoEnte;
	}

	public void setDescStatoEnte(String descStatoEnte) {
		this.descStatoEnte = descStatoEnte;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregREnteGestoreStatoEnte> getGregREnteGestoreStatoEntes() {
		return this.gregREnteGestoreStatoEntes;
	}

	public void setGregREnteGestoreStatoEntes(Set<GregREnteGestoreStatoEnte> gregREnteGestoreStatoEntes) {
		this.gregREnteGestoreStatoEntes = gregREnteGestoreStatoEntes;
	}

	public GregREnteGestoreStatoEnte addGregREnteGestoreStatoEnte(GregREnteGestoreStatoEnte gregREnteGestoreStatoEnte) {
		getGregREnteGestoreStatoEntes().add(gregREnteGestoreStatoEnte);
		gregREnteGestoreStatoEnte.setGregDStatoEnte(this);

		return gregREnteGestoreStatoEnte;
	}

	public GregREnteGestoreStatoEnte removeGregREnteGestoreStatoEnte(GregREnteGestoreStatoEnte gregREnteGestoreStatoEnte) {
		getGregREnteGestoreStatoEntes().remove(gregREnteGestoreStatoEnte);
		gregREnteGestoreStatoEnte.setGregDStatoEnte(null);

		return gregREnteGestoreStatoEnte;
	}

}