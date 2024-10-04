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
 * The persistent class for the greg_d_dettaglio_utenze database table.
 * 
 */
@Entity
@Table(name="greg_d_dettaglio_utenze")
@NamedQuery(name="GregDDettaglioUtenze.findAll", query="SELECT g FROM GregDDettaglioUtenze g where g.dataCancellazione IS NULL")
public class GregDDettaglioUtenze implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_DETTAGLIO_UTENZE_IDDETTAGLIOUTENZA_GENERATOR", sequenceName="GREG_D_DETTAGLIO_UTENZE_ID_DETTAGLIO_UTENZA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_DETTAGLIO_UTENZE_IDDETTAGLIOUTENZA_GENERATOR")
	@Column(name="id_dettaglio_utenza")
	private Integer idDettaglioUtenza;

	@Column(name="cod_dettaglio_utenza")
	private String codDettaglioUtenza;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_dettaglio_utenza")
	private String descDettaglioUtenza;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeModcDettUtenze
	@JsonIgnore
	@OneToMany(mappedBy="gregDDettaglioUtenze")
	private Set<GregRPrestReg1UtenzeModcDettUtenze> gregRPrestReg1UtenzeModcDettUtenzes;

	public GregDDettaglioUtenze() {
	}

	public Integer getIdDettaglioUtenza() {
		return this.idDettaglioUtenza;
	}

	public void setIdDettaglioUtenza(Integer idDettaglioUtenza) {
		this.idDettaglioUtenza = idDettaglioUtenza;
	}

	public String getCodDettaglioUtenza() {
		return this.codDettaglioUtenza;
	}

	public void setCodDettaglioUtenza(String codDettaglioUtenza) {
		this.codDettaglioUtenza = codDettaglioUtenza;
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

	public String getDescDettaglioUtenza() {
		return this.descDettaglioUtenza;
	}

	public void setDescDettaglioUtenza(String descDettaglioUtenza) {
		this.descDettaglioUtenza = descDettaglioUtenza;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRPrestReg1UtenzeModcDettUtenze> getGregRPrestReg1UtenzeModcDettUtenzes() {
		return this.gregRPrestReg1UtenzeModcDettUtenzes;
	}

	public void setGregRPrestReg1UtenzeModcDettUtenzes(Set<GregRPrestReg1UtenzeModcDettUtenze> gregRPrestReg1UtenzeModcDettUtenzes) {
		this.gregRPrestReg1UtenzeModcDettUtenzes = gregRPrestReg1UtenzeModcDettUtenzes;
	}

	public GregRPrestReg1UtenzeModcDettUtenze addGregRPrestReg1UtenzeModcDettUtenze(GregRPrestReg1UtenzeModcDettUtenze gregRPrestReg1UtenzeModcDettUtenze) {
		getGregRPrestReg1UtenzeModcDettUtenzes().add(gregRPrestReg1UtenzeModcDettUtenze);
		gregRPrestReg1UtenzeModcDettUtenze.setGregDDettaglioUtenze(this);

		return gregRPrestReg1UtenzeModcDettUtenze;
	}

	public GregRPrestReg1UtenzeModcDettUtenze removeGregRPrestReg1UtenzeModcDettUtenze(GregRPrestReg1UtenzeModcDettUtenze gregRPrestReg1UtenzeModcDettUtenze) {
		getGregRPrestReg1UtenzeModcDettUtenzes().remove(gregRPrestReg1UtenzeModcDettUtenze);
		gregRPrestReg1UtenzeModcDettUtenze.setGregDDettaglioUtenze(null);

		return gregRPrestReg1UtenzeModcDettUtenze;
	}

}