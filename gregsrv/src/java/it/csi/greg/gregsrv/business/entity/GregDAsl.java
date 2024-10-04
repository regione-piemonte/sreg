/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.csi.greg.gregsrv.business.entity.GregDRegione;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_asl database table.
 * 
 */
@Entity
@Table(name="greg_d_asl")
@NamedQueries({
	@NamedQuery(name="GregDAsl.findAll", query="SELECT g FROM GregDAsl g"),
	@NamedQuery(name="GregDAsl.findByIdNotDeleted", query="SELECT g FROM GregDAsl g WHERE g.idAsl = :idAsl AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDAsl.findAllNotDeleted", query="SELECT g FROM GregDAsl g WHERE g.gregDRegione.codRegione = :codregione "
			+ "and ((g.dataInizioValidita <= :dataValidita and g.dataFineValidita >= :dataValidita) or "
			+ "(g.dataInizioValidita <= :dataValidita and g.dataFineValidita is null)) "
			+ "and g.dataCancellazione IS NULL ORDER BY g.desAsl"),
	@NamedQuery(name="GregDAsl.findAllStorico", query="SELECT g FROM GregDAsl g WHERE "
			+ "g.dataCancellazione IS NULL ORDER BY g.desAsl"),
	@NamedQuery(name="GregDAsl.findByCod", query="SELECT g FROM GregDAsl g WHERE g.codAsl = :codAsl "
			+ "AND g.dataCancellazione IS NULL")
})

public class GregDAsl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_ASL_IDASL_GENERATOR", sequenceName="GREG_D_ASL_ID_ASL_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_ASL_IDASL_GENERATOR")
	@Column(name="id_asl")
	private Integer idAsl;

	@Column(name="cod_asl")
	private String codAsl;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_asl")
	private String desAsl;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	//bi-directional many-to-one association to GregDRegione
	@ManyToOne
	@JoinColumn(name="id_regione")
	private GregDRegione gregDRegione;
		
	//bi-directional many-to-one association to GregDDistretti
	@JsonIgnore
	@OneToMany(mappedBy="gregDAsl")
	private Set<GregDDistretti> gregDDistrettis;

	//bi-directional many-to-one association to GregREnteGestoreContatti
	@JsonIgnore
	@OneToMany(mappedBy="gregDAsl")
	private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;

	public GregDAsl() {
	}

	public Integer getIdAsl() {
		return this.idAsl;
	}

	public void setIdAsl(Integer idAsl) {
		this.idAsl = idAsl;
	}

	public String getCodAsl() {
		return this.codAsl;
	}

	public void setCodAsl(String codAsl) {
		this.codAsl = codAsl;
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

	public String getDesAsl() {
		return this.desAsl;
	}

	public void setDesAsl(String desAsl) {
		this.desAsl = desAsl;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDDistretti> getGregDDistrettis() {
		return this.gregDDistrettis;
	}

	public void setGregDDistrettis(Set<GregDDistretti> gregDDistrettis) {
		this.gregDDistrettis = gregDDistrettis;
	}

	public GregDDistretti addGregDDistretti(GregDDistretti gregDDistretti) {
		getGregDDistrettis().add(gregDDistretti);
		gregDDistretti.setGregDAsl(this);

		return gregDDistretti;
	}

	public GregDDistretti removeGregDDistretti(GregDDistretti gregDDistretti) {
		getGregDDistrettis().remove(gregDDistretti);
		gregDDistretti.setGregDAsl(null);

		return gregDDistretti;
	}

	public Set<GregREnteGestoreContatti> getGregREnteGestoreContattis() {
		return this.gregREnteGestoreContattis;
	}

	public void setGregREnteGestoreContattis(Set<GregREnteGestoreContatti> gregREnteGestoreContattis) {
		this.gregREnteGestoreContattis = gregREnteGestoreContattis;
	}

	public GregREnteGestoreContatti addGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().add(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDAsl(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDAsl(null);

		return gregREnteGestoreContatti;
	}

	public GregDRegione getGregDRegione() {
		return this.gregDRegione;
	}

	public void setGregDRegione(GregDRegione gregDRegione) {
		this.gregDRegione = gregDRegione;
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
}