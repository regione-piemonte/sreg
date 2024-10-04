/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;

public class ModelliInvio implements Comparable<ModelliInvio> {
	private String modello;
	private String obbligo;
	private Integer ordinamento;
	//altri modelli
	public ModelliInvio () {}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getObbligo() {
		return obbligo;
	}

	public void setObbligo(String obbligo) {
		this.obbligo = obbligo;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	@Override		
		public int compareTo(ModelliInvio o) {
			Integer parametroSort = this.ordinamento;
			Integer parametroSortC = o.ordinamento;
			return parametroSort.compareTo(parametroSortC);
	}	
	
}
