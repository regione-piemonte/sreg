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
 * The persistent class for the greg_d_spesa_missione_programma database table.
 * 
 */
@Entity
@Table(name="greg_d_spesa_missione_programma")
@NamedQuery(name="GregDSpesaMissioneProgramma.findAll", query="SELECT g FROM GregDSpesaMissioneProgramma g where g.dataCancellazione IS NULL")
public class GregDSpesaMissioneProgramma implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_SPESA_MISSIONE_PROGRAMMA_IDSPESAMISSIONEPROGRAMMA_GENERATOR", sequenceName="GREG_D_SPESA_MISSIONE_PROGRAMMA_ID_SPESA_MISSIONE_PROGRAMMA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_SPESA_MISSIONE_PROGRAMMA_IDSPESAMISSIONEPROGRAMMA_GENERATOR")
	@Column(name="id_spesa_missione_programma")
	private Integer idSpesaMissioneProgramma;

	@Column(name="cod_spesa_missione_programma")
	private String codSpesaMissioneProgramma;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_missione_cartacea")
	private String descMissioneCartacea;

	@Column(name="desc_programma_cartaceo")
	private String descProgrammaCartaceo;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRSpesaMissioneProgrammaMacro
	@JsonIgnore
	@OneToMany(mappedBy="gregDSpesaMissioneProgramma")
	private Set<GregRSpesaMissioneProgrammaMacro> gregRSpesaMissioneProgrammaMacros;

	public GregDSpesaMissioneProgramma() {
	}

	public Integer getIdSpesaMissioneProgramma() {
		return this.idSpesaMissioneProgramma;
	}

	public void setIdSpesaMissioneProgramma(Integer idSpesaMissioneProgramma) {
		this.idSpesaMissioneProgramma = idSpesaMissioneProgramma;
	}

	public String getCodSpesaMissioneProgramma() {
		return this.codSpesaMissioneProgramma;
	}

	public void setCodSpesaMissioneProgramma(String codSpesaMissioneProgramma) {
		this.codSpesaMissioneProgramma = codSpesaMissioneProgramma;
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

	public String getDescMissioneCartacea() {
		return this.descMissioneCartacea;
	}

	public void setDescMissioneCartacea(String descMissioneCartacea) {
		this.descMissioneCartacea = descMissioneCartacea;
	}

	public String getDescProgrammaCartaceo() {
		return this.descProgrammaCartaceo;
	}

	public void setDescProgrammaCartaceo(String descProgrammaCartaceo) {
		this.descProgrammaCartaceo = descProgrammaCartaceo;
	}

	public String getMsgInformativo() {
		return this.msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public Integer getOrdinamento() {
		return this.ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRSpesaMissioneProgrammaMacro> getGregRSpesaMissioneProgrammaMacros() {
		return this.gregRSpesaMissioneProgrammaMacros;
	}

	public void setGregRSpesaMissioneProgrammaMacros(Set<GregRSpesaMissioneProgrammaMacro> gregRSpesaMissioneProgrammaMacros) {
		this.gregRSpesaMissioneProgrammaMacros = gregRSpesaMissioneProgrammaMacros;
	}

	public GregRSpesaMissioneProgrammaMacro addGregRSpesaMissioneProgrammaMacro(GregRSpesaMissioneProgrammaMacro gregRSpesaMissioneProgrammaMacro) {
		getGregRSpesaMissioneProgrammaMacros().add(gregRSpesaMissioneProgrammaMacro);
		gregRSpesaMissioneProgrammaMacro.setGregDSpesaMissioneProgramma(this);

		return gregRSpesaMissioneProgrammaMacro;
	}

	public GregRSpesaMissioneProgrammaMacro removeGregRSpesaMissioneProgrammaMacro(GregRSpesaMissioneProgrammaMacro gregRSpesaMissioneProgrammaMacro) {
		getGregRSpesaMissioneProgrammaMacros().remove(gregRSpesaMissioneProgrammaMacro);
		gregRSpesaMissioneProgrammaMacro.setGregDSpesaMissioneProgramma(null);

		return gregRSpesaMissioneProgrammaMacro;
	}

}