/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloAInput {

	private Integer idEnte;
	private List<ModelRendicontazioneModAPart3> rendicontazioneModAPart3;
	private VociModelloA rendicontazioneModAPart1;
	private String denominazioneEnte;
	
	public EsportaModelloAInput() { }
	

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public List<ModelRendicontazioneModAPart3> getRendicontazioneModAPart3() {
		return rendicontazioneModAPart3;
	}



	public void setRendicontazioneModAPart3(List<ModelRendicontazioneModAPart3> rendicontazioneModAPart3) {
		this.rendicontazioneModAPart3 = rendicontazioneModAPart3;
	}


	public VociModelloA getRendicontazioneModAPart1() {
		return rendicontazioneModAPart1;
	}


	public void setRendicontazioneModAPart1(VociModelloA rendicontazioneModAPart1) {
		this.rendicontazioneModAPart1 = rendicontazioneModAPart1;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}


	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}
	
}

