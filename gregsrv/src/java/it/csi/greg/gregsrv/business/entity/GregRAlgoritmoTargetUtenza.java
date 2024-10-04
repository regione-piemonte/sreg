/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_algoritmo_target_utenza database table.
 * 
 */
@Entity
@Table(name="greg_r_algoritmo_target_utenza")
@NamedQuery(name="GregRAlgoritmoTargetUtenza.findAll", query="SELECT g FROM GregRAlgoritmoTargetUtenza g where g.dataCancellazione IS NULL")
public class GregRAlgoritmoTargetUtenza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_ALGORITMO_TARGET_UTENZA_IDALGORITMOTARGETUTENZA_GENERATOR", sequenceName="GREG_R_ALGORITMO_TARGET_UTENZA_ID_ALGORITMO_TARGET_UTENZA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_ALGORITMO_TARGET_UTENZA_IDALGORITMOTARGETUTENZA_GENERATOR")
	@Column(name="id_algoritmo_target_utenza")
	private Integer idAlgoritmoTargetUtenza;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza_padre")
	private GregDTargetUtenza gregDTargetUtenza1;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza_figlio")
	private GregDTargetUtenza gregDTargetUtenza2;

	public GregRAlgoritmoTargetUtenza() {
	}

	public Integer getIdAlgoritmoTargetUtenza() {
		return this.idAlgoritmoTargetUtenza;
	}

	public void setIdAlgoritmoTargetUtenza(Integer idAlgoritmoTargetUtenza) {
		this.idAlgoritmoTargetUtenza = idAlgoritmoTargetUtenza;
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

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDTargetUtenza getGregDTargetUtenza1() {
		return this.gregDTargetUtenza1;
	}

	public void setGregDTargetUtenza1(GregDTargetUtenza gregDTargetUtenza1) {
		this.gregDTargetUtenza1 = gregDTargetUtenza1;
	}

	public GregDTargetUtenza getGregDTargetUtenza2() {
		return this.gregDTargetUtenza2;
	}

	public void setGregDTargetUtenza2(GregDTargetUtenza gregDTargetUtenza2) {
		this.gregDTargetUtenza2 = gregDTargetUtenza2;
	}

}