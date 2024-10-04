/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

//import javax.persistence.TypedQuery;

public class Util {
	
	public static  <T> T getSingleResult(TypedQuery<T> query) {
		List<T> results = query.getResultList();
		if (!results.isEmpty())
			return (T) results.get(0);
		else
			return null;
	}

	
	private static String getFineValidita(String data, String ggMM) {

		String annoCorrente = Converter.getAnno(new Date());
		String ggmmaaaa = ggMM + "/" + annoCorrente;
		Date scadCandidate = Converter.getData(ggmmaaaa);
		Date today = Converter.getData(data);
		if (scadCandidate.compareTo(today) <= 0) {
			// torna scadCandidate+1anno
			scadCandidate = Converter.aggiungiAnnoAData(scadCandidate, 1);
			return Converter.getData(scadCandidate);
		} else {
			return ggmmaaaa;
		}
	}

	/**
	 * Validazione sintattica e semantica dei parametri di input di tutti i servizi rest
	 * @param xRequestId
	 * @param xForwardedFor
	 * @param xCodiceServizio
	 * @return
	 */
	public static Map<String, String> validate(String xRequestId, String xForwardedFor, String xCodiceServizio) {
		Map<String, String> errors = new HashMap<String, String>();
		if (xRequestId == null || xRequestId.trim().length()==0)
			errors.put("ERR09", "X-Request-ID");
		else if (xForwardedFor == null || xForwardedFor.trim().length()==0)
			errors.put("ERR09", "X-Forwarded-For");
		else if (xCodiceServizio == null || xCodiceServizio.trim().length()==0)
			errors.put("ERR09", "X-Codice-Servizio");
		else if (!Checker.isUuidValido(xRequestId))
			errors.put("ERR10", "X-Request-ID");
		return errors;
	}
	
	/**
	 * Costruzione onfly del messaggio di errore 
	 * @param messaggio messaggio registrato nel db (es. "Parametri obbligatori non presenti (SPECIFICARE)")
	 * @param dettaglio testo che prendera' il posto del "SPECIFICARE"
	 * @return messaggio di errore completo (es. "Parametri obbligatori non presenti (cognome))
	 */
	public static String composeMessage(String messaggio, String dettaglio) {
		if (Checker.isValorizzato(dettaglio)) {
			if (messaggio.contains("SPECIFICARE"))
				messaggio = messaggio.replaceAll("SPECIFICARE", dettaglio);
			else
				messaggio = messaggio+dettaglio;
		}
		return messaggio;
	}
	
	public static String caseUp(String s) {
		
		if(s!=null)
			return s.toUpperCase();
		
		return null;
		
	}
	
	public static String convertBigDecimalToString(BigDecimal value) {
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		String pattern = "###,##0.00";
		DecimalFormat df = new DecimalFormat(pattern, otherSymbols);		
		
		return df.format(value);

	}
		
	public static BigDecimal convertStringToBigDecimal(String value) {
		
		return new BigDecimal(value.replace(".", "").replace(",",".")).setScale(2,RoundingMode.HALF_UP);
	}
	
	public static void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	        	for (int j =0;j<sheet.getPhysicalNumberOfRows();j++) {
	            Row row = sheet.getRow(j);
	            if (j==3)
	            row.setHeight((short) (row.getHeight()*3));
	            else
	            row.setHeight((short) (row.getHeight()*2));	
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	            }
	        	}
	        }
	    }
	}
	
	public static void autoSizeColumnsAndMerged(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	        	for (int j =0;j<sheet.getPhysicalNumberOfRows();j++) {
	            Row row = sheet.getRow(j);
	            if (j==3)
	            row.setHeight((short) (row.getHeight()*3));
	            else
	            row.setHeight((short) (row.getHeight()*2));	
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex, true);
	            }
	        	}
	        }
	    }
	}


}