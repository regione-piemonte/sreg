/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class BytesSourceUtil implements DataSource {
	private final ByteArrayOutputStream _bos = new ByteArrayOutputStream();
	private String contentType = "application/pdf"; // content-type

	public BytesSourceUtil(final String contentType) {
		this.contentType = contentType;
	}

	public void write(final byte[] byteArray) throws IOException {
		_bos.write(byteArray);
	}

	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(_bos.toByteArray());
	}

	public String getName() { return ""; }

	public OutputStream getOutputStream() throws IOException {
		return _bos;
	}

	public String getContentType() {
		return contentType;
	}
}
