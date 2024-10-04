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
 * The persistent class for the greg_r_disabilita_target_utenza database table.
 * 
 */
@Entity
@Table(name="greg_r_disabilita_target_utenza")
@NamedQuery(name="GregRDisabilitaTargetUtenza.findAll", query="SELECT g FROM GregRDisabilitaTargetUtenza g where g.dataCancellazione IS NULL")
public class GregRDisabilitaTargetUtenza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_DISABILITA_TARGET_UTENZA_IDDISABILITATARGETUTENZA_GENERATOR", sequenceName="GREG_R_DISABILITA_TARGET_UTENZA_ID_DISABILITA_TARGET_UTENZA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_DISABILITA_TARGET_UTENZA_IDDISABILITATARGETUTENZA_GENERATOR")
	@Column(name="id_disabilita_target_utenza")
	private Integer idDisabilitaTargetUtenza;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String numero;

	@Column(name="numero_gravi")
	private String numeroGravi;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDDisabilita
	@ManyToOne
	@JoinColumn(name="id_disabilita")
	private GregDDisabilita gregDDisabilita;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	//bi-directional many-to-one association to GregRPreg1DisabilitaTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregRDisabilitaTargetUtenza")
	private Set<GregRPreg1DisabilitaTargetUtenza> gregRPreg1DisabilitaTargetUtenzas;

	public GregRDisabilitaTargetUtenza() {
	}

	public Integer getIdDisabilitaTargetUtenza() {
		return this.idDisabilitaTargetUtenza;
	}

	public void setIdDisabilitaTargetUtenza(Integer idDisabilitaTargetUtenza) {
		this.idDisabilitaTargetUtenza = idDisabilitaTargetUtenza;
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

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNumeroGravi() {
		return this.numeroGravi;
	}

	public void setNumeroGravi(String numeroGravi) {
		this.numeroGravi = numeroGravi;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDDisabilita getGregDDisabilita() {
		return this.gregDDisabilita;
	}

	public void setGregDDisabilita(GregDDisabilita gregDDisabilita) {
		this.gregDDisabilita = gregDDisabilita;
	}

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

	public Set<GregRPreg1DisabilitaTargetUtenza> getGregRPreg1DisabilitaTargetUtenzas() {
		return this.gregRPreg1DisabilitaTargetUtenzas;
	}

	public void setGregRPreg1DisabilitaTargetUtenzas(Set<GregRPreg1DisabilitaTargetUtenza> gregRPreg1DisabilitaTargetUtenzas) {
		this.gregRPreg1DisabilitaTargetUtenzas = gregRPreg1DisabilitaTargetUtenzas;
	}

	public GregRPreg1DisabilitaTargetUtenza addGregRPreg1DisabilitaTargetUtenza(GregRPreg1DisabilitaTargetUtenza gregRPreg1DisabilitaTargetUtenza) {
		getGregRPreg1DisabilitaTargetUtenzas().add(gregRPreg1DisabilitaTargetUtenza);
		gregRPreg1DisabilitaTargetUtenza.setGregRDisabilitaTargetUtenza(this);

		return gregRPreg1DisabilitaTargetUtenza;
	}

	public GregRPreg1DisabilitaTargetUtenza removeGregRPreg1DisabilitaTargetUtenza(GregRPreg1DisabilitaTargetUtenza gregRPreg1DisabilitaTargetUtenza) {
		getGregRPreg1DisabilitaTargetUtenzas().remove(gregRPreg1DisabilitaTargetUtenza);
		gregRPreg1DisabilitaTargetUtenza.setGregRDisabilitaTargetUtenza(null);

		return gregRPreg1DisabilitaTargetUtenza;
	}

}