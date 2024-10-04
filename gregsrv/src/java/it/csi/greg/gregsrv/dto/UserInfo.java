/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfo implements Comparable<UserInfo>, Serializable {

	private String cognome;

	private String nome;

	private String codFisc;

	private String idAura;
	
	private ArrayList<ModelProfilo> listaprofili;
	
	public String getIdAura() {
		return idAura;
	}

	public void setIdAura(String idAura) {
		this.idAura = idAura;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codFisc == null) ? 0 : codFisc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (codFisc == null) {
			if (other.codFisc != null)
				return false;
		} else if (!codFisc.equals(other.codFisc))
			return false;
		return true;
	}

	@Override
	public int compareTo(UserInfo o) {
		if (this.equals(o))
			return 0;
		return 1;
	}

	
	public ArrayList<ModelProfilo> getListaprofili() {
		return listaprofili;
	}

	public void setListaprofili(ArrayList<ModelProfilo> listaprofili) {
		this.listaprofili = listaprofili;
	}

	@Override
	public String toString() {
		return "UserInfo [cognome=" + cognome + ", nome=" + nome + ", codFisc=" + codFisc + ", idAura=" + idAura
				+ ",  listaprofili=" + listaprofili + "]";
	}


}
