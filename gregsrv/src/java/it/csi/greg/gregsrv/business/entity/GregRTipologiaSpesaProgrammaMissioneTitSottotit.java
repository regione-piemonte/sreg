/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_tipologia_spesa_programma_missione_tit_sottotit database table.
 * 
 */
@Entity
@Table(name="greg_r_tipologia_spesa_programma_missione_tit_sottotit")
@NamedQuery(name="GregRTipologiaSpesaProgrammaMissioneTitSottotit.findAll", query="SELECT g FROM GregRTipologiaSpesaProgrammaMissioneTitSottotit g where g.dataCancellazione IS NULL")
public class GregRTipologiaSpesaProgrammaMissioneTitSottotit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TIPOLOGIA_SPESA_PROGRAMMA_MISSIONE_TIT_SOTTOTIT_IDRTIPOLOGIASPESAPROGRAMMAMISSIONETITSOTTOTIT_GENERATOR", sequenceName="GREG_R_TIPOLOGIA_SPESA_PROGRA_ID_R_TIPOLOGIA_SPESA_PROGRAMM_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TIPOLOGIA_SPESA_PROGRAMMA_MISSIONE_TIT_SOTTOTIT_IDRTIPOLOGIASPESAPROGRAMMAMISSIONETITSOTTOTIT_GENERATOR")
	@Column(name="id_r_tipologia_spesa_programma_missione_tit_sottotit")
	private Integer idRTipologiaSpesaProgrammaMissioneTitSottotit;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTipologiaSpesa
	@ManyToOne
	@JoinColumn(name="id_tipologia_spesa")
	private GregDTipologiaSpesa gregDTipologiaSpesa;

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@ManyToOne
	@JoinColumn(name="id_programma_missione_tit_sottotit")
	private GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit;

	public GregRTipologiaSpesaProgrammaMissioneTitSottotit() {
	}

	public Integer getIdRTipologiaSpesaProgrammaMissioneTitSottotit() {
		return this.idRTipologiaSpesaProgrammaMissioneTitSottotit;
	}

	public void setIdRTipologiaSpesaProgrammaMissioneTitSottotit(Integer idRTipologiaSpesaProgrammaMissioneTitSottotit) {
		this.idRTipologiaSpesaProgrammaMissioneTitSottotit = idRTipologiaSpesaProgrammaMissioneTitSottotit;
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

	public GregDTipologiaSpesa getGregDTipologiaSpesa() {
		return this.gregDTipologiaSpesa;
	}

	public void setGregDTipologiaSpesa(GregDTipologiaSpesa gregDTipologiaSpesa) {
		this.gregDTipologiaSpesa = gregDTipologiaSpesa;
	}

	public GregRProgrammaMissioneTitSottotit getGregRProgrammaMissioneTitSottotit() {
		return this.gregRProgrammaMissioneTitSottotit;
	}

	public void setGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		this.gregRProgrammaMissioneTitSottotit = gregRProgrammaMissioneTitSottotit;
	}

}