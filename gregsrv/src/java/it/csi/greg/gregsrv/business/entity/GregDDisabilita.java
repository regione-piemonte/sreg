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
 * The persistent class for the greg_d_disabilita database table.
 * 
 */
@Entity
@Table(name="greg_d_disabilita")
@NamedQuery(name="GregDDisabilita.findAll", query="SELECT g FROM GregDDisabilita g where g.dataCancellazione IS NULL")
public class GregDDisabilita implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_DISABILITA_IDDISABILITA_GENERATOR", sequenceName="GREG_D_DISABILITA_ID_DISABILITA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_DISABILITA_IDDISABILITA_GENERATOR")
	@Column(name="id_disabilita")
	private Integer idDisabilita;

	@Column(name="cod_disabilita")
	private String codDisabilita;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_disabilita")
	private String desDisabilita;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRDisabilitaTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregDDisabilita")
	private Set<GregRDisabilitaTargetUtenza> gregRDisabilitaTargetUtenzas;

	public GregDDisabilita() {
	}

	public Integer getIdDisabilita() {
		return this.idDisabilita;
	}

	public void setIdDisabilita(Integer idDisabilita) {
		this.idDisabilita = idDisabilita;
	}

	public String getCodDisabilita() {
		return this.codDisabilita;
	}

	public void setCodDisabilita(String codDisabilita) {
		this.codDisabilita = codDisabilita;
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

	public String getDesDisabilita() {
		return this.desDisabilita;
	}

	public void setDesDisabilita(String desDisabilita) {
		this.desDisabilita = desDisabilita;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRDisabilitaTargetUtenza> getGregRDisabilitaTargetUtenzas() {
		return this.gregRDisabilitaTargetUtenzas;
	}

	public void setGregRDisabilitaTargetUtenzas(Set<GregRDisabilitaTargetUtenza> gregRDisabilitaTargetUtenzas) {
		this.gregRDisabilitaTargetUtenzas = gregRDisabilitaTargetUtenzas;
	}

	public GregRDisabilitaTargetUtenza addGregRDisabilitaTargetUtenza(GregRDisabilitaTargetUtenza gregRDisabilitaTargetUtenza) {
		getGregRDisabilitaTargetUtenzas().add(gregRDisabilitaTargetUtenza);
		gregRDisabilitaTargetUtenza.setGregDDisabilita(this);

		return gregRDisabilitaTargetUtenza;
	}

	public GregRDisabilitaTargetUtenza removeGregRDisabilitaTargetUtenza(GregRDisabilitaTargetUtenza gregRDisabilitaTargetUtenza) {
		getGregRDisabilitaTargetUtenzas().remove(gregRDisabilitaTargetUtenza);
		gregRDisabilitaTargetUtenza.setGregDDisabilita(null);

		return gregRDisabilitaTargetUtenza;
	}

}