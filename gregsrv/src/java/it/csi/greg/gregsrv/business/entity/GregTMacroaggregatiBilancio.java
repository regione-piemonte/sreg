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
 * The persistent class for the greg_t_macroaggregati_bilancio database table.
 * 
 */
@Entity
@Table(name="greg_t_macroaggregati_bilancio")
@NamedQueries({
	@NamedQuery(name="GregTMacroaggregatiBilancio.findAll", query="SELECT g FROM GregTMacroaggregatiBilancio g"),
	@NamedQuery(name="GregTMacroaggregatiBilancio.findByIdMacroaggregatoBilancioNotDeleted", query="SELECT g FROM GregTMacroaggregatiBilancio g WHERE g.idMacroaggregatoBilancio= :idMacroaggregatoBilancio AND g.dataCancellazione IS NULL")
})
public class GregTMacroaggregatiBilancio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_MACROAGGREGATI_BILANCIO_IDMACROAGGREGATOBILANCIO_GENERATOR", sequenceName="GREG_T_MACROAGGREGATI_BILANCIO_ID_MACROAGGREGATO_BILANCIO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_MACROAGGREGATI_BILANCIO_IDMACROAGGREGATOBILANCIO_GENERATOR")
	@Column(name="id_macroaggregato_bilancio")
	private Integer idMacroaggregatoBilancio;

	@Column(name="altra_desc_macroaggregato_bilancio")
	private String altraDescMacroaggregatoBilancio;

	@Column(name="cod_macroaggregato_bilancio")
	private String codMacroaggregatoBilancio;

	@Column(name="codifica_macroaggregato_bilancio")
	private String codificaMacroaggregatoBilancio;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_macroaggregato_bilancio")
	private String desMacroaggregatoBilancio;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPrestReg1MacroaggregatiBilancio
	@JsonIgnore
	@OneToMany(mappedBy="gregTMacroaggregatiBilancio")
	private Set<GregRPrestReg1MacroaggregatiBilancio> gregRPrestReg1MacroaggregatiBilancios;

	//bi-directional many-to-one association to GregRSpesaMissioneProgrammaMacro
	@JsonIgnore
	@OneToMany(mappedBy="gregTMacroaggregatiBilancio")
	private Set<GregRSpesaMissioneProgrammaMacro> gregRSpesaMissioneProgrammaMacros;

	public GregTMacroaggregatiBilancio() {
	}

	public Integer getIdMacroaggregatoBilancio() {
		return this.idMacroaggregatoBilancio;
	}

	public void setIdMacroaggregatoBilancio(Integer idMacroaggregatoBilancio) {
		this.idMacroaggregatoBilancio = idMacroaggregatoBilancio;
	}

	public String getAltraDescMacroaggregatoBilancio() {
		return this.altraDescMacroaggregatoBilancio;
	}

	public void setAltraDescMacroaggregatoBilancio(String altraDescMacroaggregatoBilancio) {
		this.altraDescMacroaggregatoBilancio = altraDescMacroaggregatoBilancio;
	}

	public String getCodMacroaggregatoBilancio() {
		return this.codMacroaggregatoBilancio;
	}

	public void setCodMacroaggregatoBilancio(String codMacroaggregatoBilancio) {
		this.codMacroaggregatoBilancio = codMacroaggregatoBilancio;
	}

	public String getCodificaMacroaggregatoBilancio() {
		return this.codificaMacroaggregatoBilancio;
	}

	public void setCodificaMacroaggregatoBilancio(String codificaMacroaggregatoBilancio) {
		this.codificaMacroaggregatoBilancio = codificaMacroaggregatoBilancio;
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

	public String getDesMacroaggregatoBilancio() {
		return this.desMacroaggregatoBilancio;
	}

	public void setDesMacroaggregatoBilancio(String desMacroaggregatoBilancio) {
		this.desMacroaggregatoBilancio = desMacroaggregatoBilancio;
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

	public Set<GregRPrestReg1MacroaggregatiBilancio> getGregRPrestReg1MacroaggregatiBilancios() {
		return this.gregRPrestReg1MacroaggregatiBilancios;
	}

	public void setGregRPrestReg1MacroaggregatiBilancios(Set<GregRPrestReg1MacroaggregatiBilancio> gregRPrestReg1MacroaggregatiBilancios) {
		this.gregRPrestReg1MacroaggregatiBilancios = gregRPrestReg1MacroaggregatiBilancios;
	}

	public GregRPrestReg1MacroaggregatiBilancio addGregRPrestReg1MacroaggregatiBilancio(GregRPrestReg1MacroaggregatiBilancio gregRPrestReg1MacroaggregatiBilancio) {
		getGregRPrestReg1MacroaggregatiBilancios().add(gregRPrestReg1MacroaggregatiBilancio);
		gregRPrestReg1MacroaggregatiBilancio.setGregTMacroaggregatiBilancio(this);

		return gregRPrestReg1MacroaggregatiBilancio;
	}

	public GregRPrestReg1MacroaggregatiBilancio removeGregRPrestReg1MacroaggregatiBilancio(GregRPrestReg1MacroaggregatiBilancio gregRPrestReg1MacroaggregatiBilancio) {
		getGregRPrestReg1MacroaggregatiBilancios().remove(gregRPrestReg1MacroaggregatiBilancio);
		gregRPrestReg1MacroaggregatiBilancio.setGregTMacroaggregatiBilancio(null);

		return gregRPrestReg1MacroaggregatiBilancio;
	}

	public Set<GregRSpesaMissioneProgrammaMacro> getGregRSpesaMissioneProgrammaMacros() {
		return this.gregRSpesaMissioneProgrammaMacros;
	}

	public void setGregRSpesaMissioneProgrammaMacros(Set<GregRSpesaMissioneProgrammaMacro> gregRSpesaMissioneProgrammaMacros) {
		this.gregRSpesaMissioneProgrammaMacros = gregRSpesaMissioneProgrammaMacros;
	}

	public GregRSpesaMissioneProgrammaMacro addGregRSpesaMissioneProgrammaMacro(GregRSpesaMissioneProgrammaMacro gregRSpesaMissioneProgrammaMacro) {
		getGregRSpesaMissioneProgrammaMacros().add(gregRSpesaMissioneProgrammaMacro);
		gregRSpesaMissioneProgrammaMacro.setGregTMacroaggregatiBilancio(this);

		return gregRSpesaMissioneProgrammaMacro;
	}

	public GregRSpesaMissioneProgrammaMacro removeGregRSpesaMissioneProgrammaMacro(GregRSpesaMissioneProgrammaMacro gregRSpesaMissioneProgrammaMacro) {
		getGregRSpesaMissioneProgrammaMacros().remove(gregRSpesaMissioneProgrammaMacro);
		gregRSpesaMissioneProgrammaMacro.setGregTMacroaggregatiBilancio(null);

		return gregRSpesaMissioneProgrammaMacro;
	}

}