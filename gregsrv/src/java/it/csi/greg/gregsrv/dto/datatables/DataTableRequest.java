/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto.datatables;

import java.util.List;

public class DataTableRequest {

	private String draw;
	private int start;
	private int length;
	private List<DataTableColumnSpecs> columns;
	private List<OrderBy> order;
	Search search;

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public List<OrderBy> getOrder() {
		return order;
	}

	public void setOrder(List<OrderBy> order) {
		this.order = order;
	}

	public String getDraw() {
		return draw;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<DataTableColumnSpecs> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableColumnSpecs> columns) {
		this.columns = columns;
	}

}
