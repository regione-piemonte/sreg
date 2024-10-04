/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_ambiti_territoriali database table.
 * 
 */
@Entity
@Table(name="greg_d_ambiti_territoriali")
@NamedQuery(name="GregDAmbitiTerritoriali.findAll", query="SELECT g FROM GregDAmbitiTerritoriali g")
public class GregDAmbitiTerritoriali implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_AMBITI_TERRITORIALI_IDAMBITOTERRITORIALE_GENERATOR", sequenceName="GREG_D_AMBITI_TERRITORIALI_ID_AMBITO_TERRITORIALE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_AMBITI_TERRITORIALI_IDAMBITOTERRITORIALE_GENERATOR")
	@Column(name="id_ambito_territoriale")
	private Integer idAmbitoTerritoriale;

	@Column(name="cod_sioss_registro_ambito_territoriali")
	private String codSiossRegistroAmbitoTerritoriali;

	@Column(name="codice_ambito_territoriale")
	private String codiceAmbitoTerritoriale;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	@Column(name="denominazione_sioss_registro_ambito_territoriali")
	private String denominazioneSiossRegistroAmbitoTerritoriali;

	@Column(name="descrizione_ambito_territoriale")
	private String descrizioneAmbitoTerritoriale;

	@Column(name="id_sioss_registro_ambito_territoriali")
	private Integer idSiossRegistroAmbitoTerritoriali;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregREnteGestoreContatti
	@OneToMany(mappedBy="gregDAmbitiTerritoriali")
	private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;

	public GregDAmbitiTerritoriali() {
	}

	public Integer getIdAmbitoTerritoriale() {
		return this.idAmbitoTerritoriale;
	}

	public void setIdAmbitoTerritoriale(Integer idAmbitoTerritoriale) {
		this.idAmbitoTerritoriale = idAmbitoTerritoriale;
	}

	public String getCodSiossRegistroAmbitoTerritoriali() {
		return this.codSiossRegistroAmbitoTerritoriali;
	}

	public void setCodSiossRegistroAmbitoTerritoriali(String codSiossRegistroAmbitoTerritoriali) {
		this.codSiossRegistroAmbitoTerritoriali = codSiossRegistroAmbitoTerritoriali;
	}

	public String getCodiceAmbitoTerritoriale() {
		return this.codiceAmbitoTerritoriale;
	}

	public void setCodiceAmbitoTerritoriale(String codiceAmbitoTerritoriale) {
		this.codiceAmbitoTerritoriale = codiceAmbitoTerritoriale;
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

	public String getDenominazioneSiossRegistroAmbitoTerritoriali() {
		return this.denominazioneSiossRegistroAmbitoTerritoriali;
	}

	public void setDenominazioneSiossRegistroAmbitoTerritoriali(String denominazioneSiossRegistroAmbitoTerritoriali) {
		this.denominazioneSiossRegistroAmbitoTerritoriali = denominazioneSiossRegistroAmbitoTerritoriali;
	}

	public String getDescrizioneAmbitoTerritoriale() {
		return this.descrizioneAmbitoTerritoriale;
	}

	public void setDescrizioneAmbitoTerritoriale(String descrizioneAmbitoTerritoriale) {
		this.descrizioneAmbitoTerritoriale = descrizioneAmbitoTerritoriale;
	}

	public Integer getIdSiossRegistroAmbitoTerritoriali() {
		return this.idSiossRegistroAmbitoTerritoriali;
	}

	public void setIdSiossRegistroAmbitoTerritoriali(Integer idSiossRegistroAmbitoTerritoriali) {
		this.idSiossRegistroAmbitoTerritoriali = idSiossRegistroAmbitoTerritoriali;
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
		gregREnteGestoreContatti.setGregDAmbitiTerritoriali(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDAmbitiTerritoriali(null);

		return gregREnteGestoreContatti;
	}

}