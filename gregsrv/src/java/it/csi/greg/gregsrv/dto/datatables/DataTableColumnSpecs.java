/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto.datatables;

public class DataTableColumnSpecs {

	String data;
	String name;
	boolean searchable;
	boolean orderable;
	Search search;

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public String getData() {
		return data;
	}

	public String getName() {
		return name;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}
}
