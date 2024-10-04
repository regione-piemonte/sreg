/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_carta_servizi_preg1 database table.
 * 
 */
@Entity
@Table(name="greg_r_carta_servizi_preg1")
@NamedQueries({
	@NamedQuery(name="GregRCartaServiziPreg1.findAll", query="SELECT g FROM GregRCartaServiziPreg1 g"),
	@NamedQuery(name="GregRCartaServiziPreg1.findByIdSchedaEnte", query="SELECT g FROM GregRCartaServiziPreg1 g WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda"),
	@NamedQuery(name="GregRCartaServiziPreg1.findByIdSchedaEnteNotDeleted", query="SELECT g FROM GregRCartaServiziPreg1 g WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda AND "
			+ "g.dataCancellazione IS NULL ORDER BY g.gregTPrestazioniRegionali1.codPrestReg1")
})
public class GregRCartaServiziPreg1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_CARTA_SERVIZI_PREG1_IDCARTASERVIZIPREG1_GENERATOR", sequenceName="GREG_R_CARTA_SERVIZI_PREG1_ID_CARTA_SERVIZI_PREG1_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_CARTA_SERVIZI_PREG1_IDCARTASERVIZIPREG1_GENERATOR")
	@Column(name="id_carta_servizi_preg1")
	private Integer idCartaServiziPreg1;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRCartaServiziPreg1() {
	}

	public Integer getIdCartaServiziPreg1() {
		return this.idCartaServiziPreg1;
	}

	public void setIdCartaServiziPreg1(Integer idCartaServiziPreg1) {
		this.idCartaServiziPreg1 = idCartaServiziPreg1;
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

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}
	
	public GregRCartaServiziPreg1(Timestamp dataCancellazione, Timestamp dataCreazione,
			Timestamp dataModifica, String utenteOperazione,
			GregTPrestazioniRegionali1 gregTPrestazioniRegionali1, GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.dataCancellazione = dataCancellazione;
		this.dataCreazione = dataCreazione;
		this.dataModifica = dataModifica;
		this.utenteOperazione = utenteOperazione;
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}
	
	public GregRCartaServiziPreg1(GregRCartaServiziPreg1 carta) {
		this.dataCancellazione = carta.getDataCancellazione();
		this.dataCreazione = carta.getDataCreazione();
		this.dataModifica = carta.getDataModifica();
		this.utenteOperazione = carta.getUtenteOperazione();
		this.gregTPrestazioniRegionali1 = carta.getGregTPrestazioniRegionali1();
		this.gregTRendicontazioneEnte = carta.getGregTRendicontazioneEnte();
	}


}