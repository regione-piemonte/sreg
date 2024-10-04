/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.util;

import javax.ws.rs.core.Response;

public class RestUtils {
	public static Response.ResponseBuilder getCorsedResponse() {

		Response.ResponseBuilder builder = Response.noContent();
		Response.ResponseBuilder header = builder.header("Access-Control-Allow-Origin", "*").header(
				"Access-Control-Allow-Headers",
				"Authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, HEAD, OPTIONS")
				.header("Access-Control-Max-Age", "1209600");

		return header;
	}
}
