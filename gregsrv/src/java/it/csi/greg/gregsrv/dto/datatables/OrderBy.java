/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto.datatables;
public class OrderBy {
	private int column;
	private String dir;
	
	public int getColumn() {
		return column;
	}
	public String getDir() {
		return dir;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
}
