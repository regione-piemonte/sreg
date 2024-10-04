/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_rendicontazione_mod_c_parte4 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_c_parte4")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneModCParte4.findAll", query="SELECT g FROM GregRRendicontazioneModCParte4 g"),
	@NamedQuery(name="GregRRendicontazioneModCParte4.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModCParte4 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneModCParte4 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_C_PARTE4_IDRENDICONTAZIONEMODCPARTE4_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_C__ID_RENDICONTAZIONE_MOD_C_PAR_SEQ3", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_C_PARTE4_IDRENDICONTAZIONEMODCPARTE4_GENERATOR")
	@Column(name="id_rendicontazione_mod_c_parte4")
	private Integer idRendicontazioneModCParte4;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	private BigDecimal valore;

	//bi-directional many-to-one association to GregRPreg1DisabTargetUtenzaDettDisab
	@ManyToOne
	@JoinColumn(name="id_preg1_disab_target_utenza_dett_disab")
	private GregRPreg1DisabTargetUtenzaDettDisab gregRPreg1DisabTargetUtenzaDettDisab;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModCParte4() {
	}

	public Integer getIdRendicontazioneModCParte4() {
		return this.idRendicontazioneModCParte4;
	}

	public void setIdRendicontazioneModCParte4(Integer idRendicontazioneModCParte4) {
		this.idRendicontazioneModCParte4 = idRendicontazioneModCParte4;
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

	public BigDecimal getValore() {
		return this.valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public GregRPreg1DisabTargetUtenzaDettDisab getGregRPreg1DisabTargetUtenzaDettDisab() {
		return this.gregRPreg1DisabTargetUtenzaDettDisab;
	}

	public void setGregRPreg1DisabTargetUtenzaDettDisab(GregRPreg1DisabTargetUtenzaDettDisab gregRPreg1DisabTargetUtenzaDettDisab) {
		this.gregRPreg1DisabTargetUtenzaDettDisab = gregRPreg1DisabTargetUtenzaDettDisab;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}