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
 * The persistent class for the greg_d_dettaglio_disabilita database table.
 * 
 */
@Entity
@Table(name="greg_d_dettaglio_disabilita")
@NamedQuery(name="GregDDettaglioDisabilita.findAll", query="SELECT g FROM GregDDettaglioDisabilita g where g.dataCancellazione IS NULL")
public class GregDDettaglioDisabilita implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_DETTAGLIO_DISABILITA_IDDETTAGLIODISABILITA_GENERATOR", sequenceName="GREG_D_DETTAGLIO_DISABILITA_ID_DETTAGLIO_DISABILITA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_DETTAGLIO_DISABILITA_IDDETTAGLIODISABILITA_GENERATOR")
	@Column(name="id_dettaglio_disabilita")
	private Integer idDettaglioDisabilita;

	@Column(name="cod_dettaglio_disabilita")
	private String codDettaglioDisabilita;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_dettaglio_disabilita")
	private String descDettaglioDisabilita;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPreg1DisabTargetUtenzaDettDisab
	@JsonIgnore
	@OneToMany(mappedBy="gregDDettaglioDisabilita")
	private Set<GregRPreg1DisabTargetUtenzaDettDisab> gregRPreg1DisabTargetUtenzaDettDisabs;

	public GregDDettaglioDisabilita() {
	}

	public Integer getIdDettaglioDisabilita() {
		return this.idDettaglioDisabilita;
	}

	public void setIdDettaglioDisabilita(Integer idDettaglioDisabilita) {
		this.idDettaglioDisabilita = idDettaglioDisabilita;
	}

	public String getCodDettaglioDisabilita() {
		return this.codDettaglioDisabilita;
	}

	public void setCodDettaglioDisabilita(String codDettaglioDisabilita) {
		this.codDettaglioDisabilita = codDettaglioDisabilita;
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

	public String getDescDettaglioDisabilita() {
		return this.descDettaglioDisabilita;
	}

	public void setDescDettaglioDisabilita(String descDettaglioDisabilita) {
		this.descDettaglioDisabilita = descDettaglioDisabilita;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRPreg1DisabTargetUtenzaDettDisab> getGregRPreg1DisabTargetUtenzaDettDisabs() {
		return this.gregRPreg1DisabTargetUtenzaDettDisabs;
	}

	public void setGregRPreg1DisabTargetUtenzaDettDisabs(Set<GregRPreg1DisabTargetUtenzaDettDisab> gregRPreg1DisabTargetUtenzaDettDisabs) {
		this.gregRPreg1DisabTargetUtenzaDettDisabs = gregRPreg1DisabTargetUtenzaDettDisabs;
	}

	public GregRPreg1DisabTargetUtenzaDettDisab addGregRPreg1DisabTargetUtenzaDettDisab(GregRPreg1DisabTargetUtenzaDettDisab gregRPreg1DisabTargetUtenzaDettDisab) {
		getGregRPreg1DisabTargetUtenzaDettDisabs().add(gregRPreg1DisabTargetUtenzaDettDisab);
		gregRPreg1DisabTargetUtenzaDettDisab.setGregDDettaglioDisabilita(this);

		return gregRPreg1DisabTargetUtenzaDettDisab;
	}

	public GregRPreg1DisabTargetUtenzaDettDisab removeGregRPreg1DisabTargetUtenzaDettDisab(GregRPreg1DisabTargetUtenzaDettDisab gregRPreg1DisabTargetUtenzaDettDisab) {
		getGregRPreg1DisabTargetUtenzaDettDisabs().remove(gregRPreg1DisabTargetUtenzaDettDisab);
		gregRPreg1DisabTargetUtenzaDettDisab.setGregDDettaglioDisabilita(null);

		return gregRPreg1DisabTargetUtenzaDettDisab;
	}

}