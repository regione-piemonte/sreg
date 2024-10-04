/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.dto.datatables.DataTableRequest;

public class DataTablesInput {
	private DataTableRequest dataTablesParameter;

	public DataTableRequest getDataTablesParameter() {
		return dataTablesParameter;
	}

	public void setDataTablesParameter(DataTableRequest dataTablesParameter) {
		this.dataTablesParameter = dataTablesParameter;
	}
}
