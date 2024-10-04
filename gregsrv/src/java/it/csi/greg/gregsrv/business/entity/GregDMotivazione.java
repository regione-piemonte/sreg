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
 * The persistent class for the greg_d_motivazione database table.
 * 
 */
@Entity
@Table(name="greg_d_motivazione")
@NamedQueries({
	@NamedQuery(name="GregDMotivazione.findAll", query="SELECT g FROM GregDMotivazione g"),
	@NamedQuery(name="GregDMotivazione.findAllChiusura", query="SELECT g FROM GregDMotivazione g  where g.gregDTipoMotivazione.codTipoMotivazione = 'CHI' and g.dataCancellazione is null Order by g.codMotivazione"),
	@NamedQuery(name="GregDMotivazione.findMotivazioneByCod", query="SELECT g FROM GregDMotivazione g  where g.codMotivazione = :codMotivazione and g.dataCancellazione is null"),
	@NamedQuery(name="GregDMotivazione.findAllRipristino", query="SELECT g FROM GregDMotivazione g  where g.gregDTipoMotivazione.codTipoMotivazione = 'RIP' and g.dataCancellazione is null Order by g.codMotivazione")
})
public class GregDMotivazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_MOTIVAZIONE_IDMOTIVAZIONE_GENERATOR", sequenceName="GREG_D_MOTIVAZIONE_ID_MOTIVAZIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_MOTIVAZIONE_IDMOTIVAZIONE_GENERATOR")
	@Column(name="id_motivazione")
	private Integer idMotivazione;

	@Column(name="cod_motivazione")
	private String codMotivazione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_motivazione")
	private String descMotivazione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTipoMotivazione
	@ManyToOne
	@JoinColumn(name="id_tipo_motivazione")
	private GregDTipoMotivazione gregDTipoMotivazione;

	//bi-directional many-to-one association to GregREnteGestoreStatoEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregDMotivazione")
	private Set<GregREnteGestoreStatoEnte> gregREnteGestoreStatoEntes;

	public GregDMotivazione() {
	}

	public Integer getIdMotivazione() {
		return this.idMotivazione;
	}

	public void setIdMotivazione(Integer idMotivazione) {
		this.idMotivazione = idMotivazione;
	}

	public String getCodMotivazione() {
		return this.codMotivazione;
	}

	public void setCodMotivazione(String codMotivazione) {
		this.codMotivazione = codMotivazione;
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

	public String getDescMotivazione() {
		return this.descMotivazione;
	}

	public void setDescMotivazione(String descMotivazione) {
		this.descMotivazione = descMotivazione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDTipoMotivazione getGregDTipoMotivazione() {
		return this.gregDTipoMotivazione;
	}

	public void setGregDTipoMotivazione(GregDTipoMotivazione gregDTipoMotivazione) {
		this.gregDTipoMotivazione = gregDTipoMotivazione;
	}

	public Set<GregREnteGestoreStatoEnte> getGregREnteGestoreStatoEntes() {
		return this.gregREnteGestoreStatoEntes;
	}

	public void setGregREnteGestoreStatoEntes(Set<GregREnteGestoreStatoEnte> gregREnteGestoreStatoEntes) {
		this.gregREnteGestoreStatoEntes = gregREnteGestoreStatoEntes;
	}

	public GregREnteGestoreStatoEnte addGregREnteGestoreStatoEnte(GregREnteGestoreStatoEnte gregREnteGestoreStatoEnte) {
		getGregREnteGestoreStatoEntes().add(gregREnteGestoreStatoEnte);
		gregREnteGestoreStatoEnte.setGregDMotivazione(this);

		return gregREnteGestoreStatoEnte;
	}

	public GregREnteGestoreStatoEnte removeGregREnteGestoreStatoEnte(GregREnteGestoreStatoEnte gregREnteGestoreStatoEnte) {
		getGregREnteGestoreStatoEntes().remove(gregREnteGestoreStatoEnte);
		gregREnteGestoreStatoEnte.setGregDMotivazione(null);

		return gregREnteGestoreStatoEnte;
	}

}