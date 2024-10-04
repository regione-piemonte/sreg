/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {

	/**
	 * Verifica che date (se valorizzata) sia una data valida nel formato passato come pattern nello specifico locale
	 * Se la data non e' valida return false.
	 * 
	 * @param data
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static boolean isData(String data, String pattern, Locale locale) {
		boolean value = true;
		if (!Checker.isValorizzato(data))
			return true;
		SimpleDateFormat sdf;
		if(locale!=null)  sdf = new SimpleDateFormat(pattern, locale);
		else sdf = new SimpleDateFormat(pattern);
		
		sdf.setLenient(false);

		Date dataLetta = null;
		try {
			dataLetta = sdf.parse(data);
		} catch (ParseException pe) {
			value = false;
		}

		if (value == true) {
			String dataFormattata = sdf.format(dataLetta);
			value = data.equals(dataFormattata);
		}

		return value;
	}

	/**
	 * Controllo di valorizzazione su di una stringa.
	 * Se null o vuoto ritorna false.
	 * @param stringa
	 * @return
	 */
	public static boolean isValorizzato(String stringa) {
		if (stringa == null || stringa.equalsIgnoreCase("null"))
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}

	public static boolean isValorizzatoRicercaPer(String stringa) {

		if (stringa == null)
			return true;

		if (stringa.trim().length() == 0)
			return true;

		if (stringa.equalsIgnoreCase("99999999"))
			return false;

		return true;
	}

	/**
	 * Controllo di valorizzazione di un long. Se e' zero return false.
	 * 
	 * @param num
	 *            num
	 * @return boolean
	 */
	public static boolean isValorizzato(long num) {
		if (num == 0)
			return false;
		return true;
	}

	/**
	 * Verifica che il codice fiscale (se valorizzato) sia corretto. 
	 * Se e' errato
	 * return false.
	 * 
	 * @param codice
	 * @return boolean
	 */
	public static boolean isCodiceFiscale(String codice) {

		if (!isValorizzato(codice))
			return false;

		if (codice.length() != 16)
			return false;

		if ((!isAlfabeticString(codice.substring(0, 6))) || (!isAlfabeticString(codice.substring(8, 9))) || (!isAlfabeticString(codice.substring(11, 12)))
				|| (!isAlfabeticString(codice.substring(15, 16)))) {
			return false;
		}
		//controllo codice fiscale mascherato esteso a esenred 1
			if (!isCodiceFiscaleCorretto(codice)) {
				return false;
			}
		
		return true;
	}

	/**
	 * Verifica che la stringa data in input sia una stringa Alfabetica. Se si
	 * tratta di stringa numerica ritorna true , altrimenti ritorna false.
	 * 
	 * @param string
	 *            String
	 * @return boolean valore di verita'
	 */
	public static boolean isAlfabeticString(String string) {
		boolean matchFound = false;
		String patternStr = "^[a-zA-Z]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}
	
	public static boolean isAlfanumericcString(String string) {
		boolean matchFound = false;
		String patternStr = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}

	public static boolean isAlfabeticStringLettera(String string) {
		boolean matchFound = false;
		String patternStr = "^[A-Za-z0-9 .,\\\\/\\u20AC]*$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}
	/**
	 * Controllo del check-digit del codice fiscale
	 * 
	 * @param codice
	 *            (codice fiscale)
	 * @return boolean
	 */
	private static boolean isCodiceFiscaleCorretto(String codice) {
		String codiceFiscale = codice.toUpperCase();

		// creo una Hashtable con i valori assunti dai caratteri pari del codice
		// fiscale
		Hashtable<String, String> caratterePari = new Hashtable<String, String>();

		caratterePari.put("0", new String("0"));
		caratterePari.put("1", new String("1"));
		caratterePari.put("2", new String("2"));
		caratterePari.put("3", new String("3"));
		caratterePari.put("4", new String("4"));
		caratterePari.put("5", new String("5"));
		caratterePari.put("6", new String("6"));
		caratterePari.put("7", new String("7"));
		caratterePari.put("8", new String("8"));
		caratterePari.put("9", new String("9"));
		caratterePari.put("A", new String("0"));
		caratterePari.put("B", new String("1"));
		caratterePari.put("C", new String("2"));
		caratterePari.put("D", new String("3"));
		caratterePari.put("E", new String("4"));
		caratterePari.put("F", new String("5"));
		caratterePari.put("G", new String("6"));
		caratterePari.put("H", new String("7"));
		caratterePari.put("I", new String("8"));
		caratterePari.put("J", new String("9"));
		caratterePari.put("K", new String("10"));
		caratterePari.put("L", new String("11"));
		caratterePari.put("M", new String("12"));
		caratterePari.put("N", new String("13"));
		caratterePari.put("O", new String("14"));
		caratterePari.put("P", new String("15"));
		caratterePari.put("Q", new String("16"));
		caratterePari.put("R", new String("17"));
		caratterePari.put("S", new String("18"));
		caratterePari.put("T", new String("19"));
		caratterePari.put("U", new String("20"));
		caratterePari.put("V", new String("21"));
		caratterePari.put("W", new String("22"));
		caratterePari.put("X", new String("23"));
		caratterePari.put("Y", new String("24"));
		caratterePari.put("Z", new String("25"));

		// creo una Hashtable con i valori assunti dai caratteri dispari del
		// codice fiscale
		Hashtable<String, String> carattereDispari = new Hashtable<String, String>();

		carattereDispari.put("0", new String("1"));
		carattereDispari.put("1", new String("0"));
		carattereDispari.put("2", new String("5"));
		carattereDispari.put("3", new String("7"));
		carattereDispari.put("4", new String("9"));
		carattereDispari.put("5", new String("13"));
		carattereDispari.put("6", new String("15"));
		carattereDispari.put("7", new String("17"));
		carattereDispari.put("8", new String("19"));
		carattereDispari.put("9", new String("21"));
		carattereDispari.put("A", new String("1"));
		carattereDispari.put("B", new String("0"));
		carattereDispari.put("C", new String("5"));
		carattereDispari.put("D", new String("7"));
		carattereDispari.put("E", new String("9"));
		carattereDispari.put("F", new String("13"));
		carattereDispari.put("G", new String("15"));
		carattereDispari.put("H", new String("17"));
		carattereDispari.put("I", new String("19"));
		carattereDispari.put("J", new String("21"));
		carattereDispari.put("K", new String("2"));
		carattereDispari.put("L", new String("4"));
		carattereDispari.put("M", new String("18"));
		carattereDispari.put("N", new String("20"));
		carattereDispari.put("O", new String("11"));
		carattereDispari.put("P", new String("3"));
		carattereDispari.put("Q", new String("6"));
		carattereDispari.put("R", new String("8"));
		carattereDispari.put("S", new String("12"));
		carattereDispari.put("T", new String("14"));
		carattereDispari.put("U", new String("16"));
		carattereDispari.put("V", new String("10"));
		carattereDispari.put("W", new String("22"));
		carattereDispari.put("X", new String("25"));
		carattereDispari.put("Y", new String("24"));
		carattereDispari.put("Z", new String("23"));

		// creo una Hashtable con i valori possibili del carattere di controllo
		Hashtable<String, String> carattereControllo = new Hashtable<String, String>();

		carattereControllo.put("0", new String("A"));
		carattereControllo.put("1", new String("B"));
		carattereControllo.put("2", new String("C"));
		carattereControllo.put("3", new String("D"));
		carattereControllo.put("4", new String("E"));
		carattereControllo.put("5", new String("F"));
		carattereControllo.put("6", new String("G"));
		carattereControllo.put("7", new String("H"));
		carattereControllo.put("8", new String("I"));
		carattereControllo.put("9", new String("J"));
		carattereControllo.put("10", new String("K"));
		carattereControllo.put("11", new String("L"));
		carattereControllo.put("12", new String("M"));
		carattereControllo.put("13", new String("N"));
		carattereControllo.put("14", new String("O"));
		carattereControllo.put("15", new String("P"));
		carattereControllo.put("16", new String("Q"));
		carattereControllo.put("17", new String("R"));
		carattereControllo.put("18", new String("S"));
		carattereControllo.put("19", new String("T"));
		carattereControllo.put("20", new String("U"));
		carattereControllo.put("21", new String("V"));
		carattereControllo.put("22", new String("W"));
		carattereControllo.put("23", new String("X"));
		carattereControllo.put("24", new String("Y"));
		carattereControllo.put("25", new String("Z"));

		// aggiunto da Davide Diomede - inizio
		// controllo della correttezza nell'eventualita' dell'omocodia
		if (!chkCodiceFiscaleInOmocodia(codiceFiscale))
			return false;
		// aggiunto da Davide Diomede - inizio

		// Controllo la correttezza del check-digit del codice fiscale.

		// Prendo i valori dei campi dispari e di quelli pari sommandoli in
		// totValori
		if (codiceFiscale.equals("")) {
			return false;
		}
		boolean pari = false;
		int totValori = 0;
		int numLoop = codiceFiscale.length() - 1;

		for (int i = 0; i < numLoop; i++) {
			if (!pari) {
				pari = true;
				String numeroDispari = (String) carattereDispari.get(String.valueOf(codiceFiscale.charAt(i)));
				try {
					int valoreDaSommare = Integer.parseInt(numeroDispari);
					totValori = totValori + valoreDaSommare;
				} catch (NumberFormatException nfe) {
					return false;
				}
			} else {
				pari = false;
				try {
					String numeroPari = (String) caratterePari.get(String.valueOf(codiceFiscale.charAt(i)));
					int valoreDaSommare = Integer.parseInt(numeroPari);
					totValori = totValori + valoreDaSommare;
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
		}
		// Decodifico il resto della divisione (totValori:26) per ottenere il
		// check-digit
		// del codice fiscale
		int resto = totValori % 26;
		Integer valoreDaDecodificare = resto;
		String carattereDiControllo = valoreDaDecodificare.toString();
		String checkDigit = (String) carattereControllo.get(carattereDiControllo);

		// Controllo l'uguaglianza del check-digit calcolato con quello del
		// codice fiscale in input
		if (!codiceFiscale.substring(15, 16).equals(checkDigit)) {
			return false;
		}
		return true;

	}

	/**
	 * Verifica se le posizioni destinate ai caratteri numerici del codice
	 * fiscale contengano cifre o i caratteri alfabetici previsti in caso di
	 * omocodia.
	 * 
	 * @param codiceFiscale
	 *            Codice Fiscale da verificare
	 * @return vero se il codice fiscale (in caso di omocodia) e' corretto
	 *         limitatamente alle posizioni per le cifre, false altrimenti.
	 */

	private static boolean chkCodiceFiscaleInOmocodia(String codiceFiscale) {

		if ((!isNumericString(codiceFiscale.substring(6, 8))) || (!isNumericString(codiceFiscale.substring(9, 11))) || (!isNumericString(codiceFiscale.substring(12, 15)))) {

			StringBuffer nuovoCodiceFiscale = new StringBuffer();
			nuovoCodiceFiscale.append(codiceFiscale.substring(0, 6));

			if (!isNumericString(codiceFiscale.substring(6, 8))) {
				// C'e' un carattere alfabetico nell'anno del Codice Fiscale
				String annoCF = codiceFiscale.substring(6, 8);
				for (int i = 0; i < annoCF.length(); i++) {
					if (!isNumericString(String.valueOf(annoCF.charAt(i)))) {
						String numeroAnno = (String) getCaratterePerOmocodia(String.valueOf(annoCF.charAt(i)));
						if (numeroAnno == null) {
							return false;
						}
					}
				}
			}

			if (!isNumericString(codiceFiscale.substring(9, 11))) {
				// C'e' un carattere alfabetico nel giorno del Codice Fiscale
				String giornoCF = codiceFiscale.substring(9, 11);
				for (int i = 0; i < giornoCF.length(); i++) {
					if (!isNumericString(String.valueOf(giornoCF.charAt(i)))) {
						String numeroGiorno = (String) getCaratterePerOmocodia(String.valueOf(giornoCF.charAt(i)));
						if (numeroGiorno == null) {
							return false;
						}
					}
				}
			}

			if (!isNumericString(codiceFiscale.substring(12, 15))) {
				// C'e' un carattere alfabetico nel comune del Codice Fiscale
				String comuneCF = codiceFiscale.substring(12, 15);
				for (int i = 0; i < comuneCF.length(); i++) {
					if (!isNumericString(String.valueOf(comuneCF.charAt(i)))) {
						String numeroComune = (String) getCaratterePerOmocodia(String.valueOf(comuneCF.charAt(i)));
						if (numeroComune == null) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Verifica che la stringa data in input sia una stringa numerica. Se si
	 * tratta di stringa numerica ritorna true , altrimenti ritorna false.
	 * 
	 * @param string
	 *            String
	 * @return boolean
	 */
	public static boolean isNumericString(String string) {
		boolean matchFound = false;
		String patternStr = "^[0-9]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Metodo che realizza la conversione dei caratteri alfabetici in cifre ai
	 * fini del codice fiscale in caso di omocodia. Dato il carattere alfabetico
	 * in formato String restituisce la cifra corrispondente in formato String.
	 * 
	 * @param key
	 *            carattere alfabetico da convertire
	 * @return String - la cifra risultato della conversione
	 * 
	 * @version 0.1
	 */
	private static String getCaratterePerOmocodia(String key) {
		Hashtable<String, String> carattereOmocodia = new Hashtable<String, String>();

		carattereOmocodia.put("L", new String("0"));
		carattereOmocodia.put("M", new String("1"));
		carattereOmocodia.put("N", new String("2"));
		carattereOmocodia.put("P", new String("3"));
		carattereOmocodia.put("Q", new String("4"));
		carattereOmocodia.put("R", new String("5"));
		carattereOmocodia.put("S", new String("6"));
		carattereOmocodia.put("T", new String("7"));
		carattereOmocodia.put("U", new String("8"));
		carattereOmocodia.put("V", new String("9"));

		return carattereOmocodia.get(key);
	}

	/**
	 * Questo metodo controlla la coerenza tra il codice fiscale (anche in caso
	 * di omocodia) passato come parametro e i dati anagrafici del soggetto. Nel
	 * caso in cui il codice fiscale sia coerente con i dati anagrafici passati,
	 * ritorna true. Esegue preliminarmente un controllo della validita'
	 * sintattica del codice fiscale inserito. Se il codice fiscale passato non
	 * e' valido sintatticamente, il metodo ritorna false. Se uno dei parametri
	 * in input e' null, stringa vuota o non valido il metodo restituisce false.
	 * Nell'ordine i parametri da passare sono: codice fiscale (String), cognome
	 * (String), nome (String), sesso (String), data di nascita (Date) e codice
	 * catastale del comune di nascita (String).
	 * 
	 * @param codiceFiscale
	 *            codice fiscale della persona
	 * @param cognome
	 *            cognome della persona
	 * @param nome
	 *            nome della persona
	 * @param sesso
	 *            sesso della persona M o F
	 * @param data
	 *            data di nascita della persona
	 * @param comune
	 *            codice catastale del comune di nascita della persona
	 * @return esito del controllo
	 * 
	 * @version 0.1
	 */
	public static boolean isCodiceFiscaleCoerente(String codiceFiscale, String cognome, String nome) {
		//controllo codice fiscale mascherato esteso a esenred 1
		if (isCodiceFiscale(codiceFiscale)) {
			codiceFiscale = getCodiceFiscaleDecodificato(codiceFiscale);
			String codiceFiscaleGenerato = getCodiceFiscale(cognome, nome);
			if (codiceFiscaleGenerato.length() != 6)
				return false;
			if (codiceFiscale.substring(0, 5).equalsIgnoreCase(codiceFiscaleGenerato.substring(0, 5)))
				return true;
			return false;
		} else
			return false;
	}

	/**
	 * Metodo privato che decodifica opportunamente il codice fiscale passato
	 * come parametro in caso di omocodia.
	 * 
	 * @param codiceFiscale
	 * @return codiceFiscaleDecodificato
	 */
	private static String getCodiceFiscaleDecodificato(String codiceFiscale) {

		// creo uno StringBuffer nel quale costruire' il nuovo codice fiscale.
		// Nell'eventualita'
		// in cui uno dei sette caratteri numerici del codice fiscale contenga
		// delle lettere,
		// decodifico la lettera in numero. In caso contrario il metodo
		// restituisce
		// il codice fiscale passato come input

		if ((!isNumericString(codiceFiscale.substring(6, 8))) || (!isNumericString(codiceFiscale.substring(9, 11))) || (!isNumericString(codiceFiscale.substring(12, 15)))) {

			codiceFiscale = codiceFiscale.toUpperCase();

			StringBuffer nuovoCodiceFiscale = new StringBuffer();
			nuovoCodiceFiscale.append(codiceFiscale.substring(0, 6));

			if (!isNumericString(codiceFiscale.substring(6, 8))) {
				// C'e' un carattere alfabetico nell'anno del Codice Fiscale
				String annoCF = codiceFiscale.substring(6, 8);
				for (int i = 0; i < annoCF.length(); i++) {
					if (!isNumericString(String.valueOf(annoCF.charAt(i)))) {
						String numeroAnno = (String) getCaratterePerOmocodia(String.valueOf(annoCF.charAt(i)));
						if (numeroAnno == null) {
							numeroAnno = " ";
						}
						nuovoCodiceFiscale.append(numeroAnno);
					} else {
						nuovoCodiceFiscale.append(annoCF.charAt(i));
					}
				}
			} else {
				nuovoCodiceFiscale.append(codiceFiscale.substring(6, 8));
			}

			nuovoCodiceFiscale.append(codiceFiscale.substring(8, 9));

			if (!isNumericString(codiceFiscale.substring(9, 11))) {
				// C'e' un carattere alfabetico nel giorno del Codice Fiscale
				String giornoCF = codiceFiscale.substring(9, 11);
				for (int i = 0; i < giornoCF.length(); i++) {
					if (!isNumericString(String.valueOf(giornoCF.charAt(i)))) {
						String numeroGiorno = (String) getCaratterePerOmocodia(String.valueOf(giornoCF.charAt(i)));
						if (numeroGiorno == null) {
							numeroGiorno = " ";
						}
						nuovoCodiceFiscale.append(numeroGiorno);
					} else {
						nuovoCodiceFiscale.append(giornoCF.charAt(i));
					}
				}
			} else {
				nuovoCodiceFiscale.append(codiceFiscale.substring(9, 11));
			}

			nuovoCodiceFiscale.append(codiceFiscale.substring(11, 12));

			if (!isNumericString(codiceFiscale.substring(12, 15))) {
				// C'e' un carattere alfabetico nel comune del Codice Fiscale
				String comuneCF = codiceFiscale.substring(12, 15);
				for (int i = 0; i < comuneCF.length(); i++) {
					if (!isNumericString(String.valueOf(comuneCF.charAt(i)))) {
						String numeroComune = (String) getCaratterePerOmocodia(String.valueOf(comuneCF.charAt(i)));
						if (numeroComune == null) {
							numeroComune = " ";
						}
						nuovoCodiceFiscale.append(numeroComune);
					} else {
						nuovoCodiceFiscale.append(comuneCF.charAt(i));
					}
				}
			} else {
				nuovoCodiceFiscale.append(codiceFiscale.substring(12, 15));
			}

			nuovoCodiceFiscale.append(codiceFiscale.substring(15, 16));
			codiceFiscale = nuovoCodiceFiscale.toString();
		}

		return codiceFiscale;
	}

	/**
	 * Metodo che calcola il codice fiscale in base ai dati anagrafici passati.
	 * Se uno dei parametri in input e' null, stringa vuota o non valido il
	 * metodo restituisce una stringa vuota. Nell'ordine i parametri da passare
	 * sono: cognome (String), nome (String), sesso (String), data di nascita
	 * (Date) e codice catastale del comune di nascita (String).
	 * 
	 * @param cognome
	 *            cognome della persona
	 * @param nome
	 *            nome della persona
	 * @param sesso
	 *            sesso della persona M o F
	 * @param data
	 *            data di nascita della persona
	 * @param comune
	 *            codice catastale del comune di nascita della persona
	 * @return String - il codice fiscale calcolato in base a parametri in input
	 * 
	 */
	public static String getCodiceFiscale(String cognome, String nome) {

		// controllo gli argomenti
		String[] args = new String[] { cognome, nome};
		for (String s : args) {
			if (s == null || s.trim().length() == 0) {
				return "";
			}
		}

		nome = nome.toUpperCase().trim();
		// Controllo la sintassi della stringa nome
		String pattern = "[A-Z" + String.valueOf((char) 0xC0) + String.valueOf((char) 0xC8) + String.valueOf((char) 0xC9) + String.valueOf((char) 0xCC) + String.valueOf((char) 0xD2)
		+ String.valueOf((char) 0xD9) + "'\\s\\-]+";
		if (!Pattern.compile(pattern).matcher(nome).matches()) {
			return "";
		} else {
			nome = nome.replaceAll("[" + String.valueOf((char) 0xC0) + "]", "A");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xC9) + "]", "E");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xC8) + "]", "E");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xCC) + "]", "I");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xD2) + "]", "O");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xD9) + "]", "U");
			nome = nome.replaceAll("['\\s\\-]", "");
		}

		cognome = cognome.toUpperCase().trim();
		// Controllo la sintassi della stringa cognome
		if (!Pattern.compile(pattern).matcher(cognome).matches()) {
			return "";
		} else {
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xC0) + "]", "A");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xC9) + "]", "E");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xC8) + "]", "E");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xCC) + "]", "I");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xD2) + "]", "O");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xD9) + "]", "U");
			cognome = cognome.replaceAll("['\\s\\-]", "");
		}


		StringBuilder codfisc = new StringBuilder();

		// Cognome
		StringBuilder cogn = new StringBuilder();
		cogn.append(ottieniConsVoc(cognome, true) + ottieniConsVoc(cognome, false));

		if (cogn.length() > 3)
			cogn = new StringBuilder(cogn.substring(0, 3));

		for (int i = cogn.length(); i < 3; i++)
			cogn.append("X");

		codfisc.append(cogn);

		// Nome
		StringBuilder codice = new StringBuilder(ottieniConsVoc(nome, true));
		if (codice.length() >= 4)
			codice = codice.delete(1, 2);
		codice.append(ottieniConsVoc(nome, false));

		if (codice.length() > 3)
			codice = codice.replace(0, codice.length(), codice.substring(0, 3));

		for (int i = codice.length(); i < 3; i++)
			codice.append("X");

		codfisc.append(codice);

		return codfisc.toString();
	}

	/**
	 * Passata una stringa in input, ritorna le sole consonanti o vocali delle
	 * stringa ai fini del calcolo del codice fiscale.
	 * <p>
	 * Per ottenere le vocali, passare conson = false <br/> Per ottenere le
	 * consonanti passare conson = true
	 * 
	 * @param stringa
	 *            La stringa per la quale si vogliono ottenere le sole
	 *            consonanti o vocali
	 * @param cod
	 *            puo' essere false o true a seconda che si vogliano ottenere le
	 *            vocali o le consonanti della stringa
	 * @return String - La stringa contente le solo vocali o consonanti della
	 *         stringa passata in input
	 */
	private static String ottieniConsVoc(String stringa, boolean conson) {
		StringBuilder testo = new StringBuilder();
		int i = 0;
		char[] valChar = stringa.toCharArray();
		for (i = 0; i < valChar.length; i++) {
			if (Pattern.compile("[AEIOU]").matcher(String.valueOf((valChar[i]))).matches() ^ conson) {
				testo.append(valChar[i]);
			}
		}
		return testo.toString();
	}

	/**
	 * Restituisce il codice del mese ai fini del calcolo del codice fiscale.
	 * L'indice mese va da 0 a 11. 0 Gennaio, 1 Febbraio, ..., 11 Dicembre.
	 * 
	 * @param mese -
	 *            intero da 0 a 11
	 * @return char - carattere associato al mese
	 * 
	 */
	private static char getCodiceMese(int mese) {
		char[] codiciMesi = { 'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T' };
		return codiciMesi[mese];
	}

	/**
	 * Converte il carattere in base al carattere stesso e alla posizione ai
	 * fini del calcolo del codice fiscale.
	 * 
	 * @param pari
	 * @param dispari
	 * @return int
	 * 
	 */
	private static int convertiCaratteriPosizioneDispari(int pari, int dispari) {
		int[][] caratteri = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 },
				{ 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 } };
		return caratteri[pari][dispari];
	}

	/**
	 * Questo metodo restituisce la lunghezza del codice IBAN prevista dal paese
	 * (aderente al REGOLAMENTO (CE) N. 2560/2001) la cui sigla e' rappresentata
	 * dai primi due caratteri alfabetici del codice IBAN stesso. Nel caso in
	 * cui questi due caratteri non corrispondano ad una sigla valida il metodo
	 * restituisce zero.
	 * 
	 * @param key
	 *            - sigla del paese (primi due caratteri del codice IBAN)
	 * @return int
	 * 
	 */
	public static int getLunghezzaCodiceIBANPerNazione(String key) {

		HashMap<String, Integer> paesi = new HashMap<String, Integer>();
		// Hashmap che contiene le sigle di tutti i paesi aderenti e per
		// ciascuna di essi la lunghezza prevista da quel paese per il codice
		// IBAN
		paesi.put("AD", 24); // Andorra
		paesi.put("AT", 20); // Austria
		paesi.put("BE", 16); // Belgio
		paesi.put("BA", 20); // Bosnia Hercegovina
		paesi.put("BG", 22); // Bulgaria
		paesi.put("CY", 28); // Cipro
		paesi.put("HR", 21); // Croazia
		paesi.put("DK", 18); // Danimarca
		paesi.put("EE", 20); // Estonia
		paesi.put("FI", 18); // Finlandia
		paesi.put("FR", 27); // Francia
		paesi.put("DE", 22); // Germania
		paesi.put("GI", 23); // Gibilterra
		paesi.put("GB", 22); // Gran Bretagna
		paesi.put("GR", 27); // Grecia
		paesi.put("IE", 22); // Irlanda
		paesi.put("IS", 26); // Islanda
		paesi.put("IL", 23); // Israele
		paesi.put("IT", 27); // Italia
		paesi.put("LV", 21); // Lettonia
		paesi.put("LI", 21); // Liechtenstein
		paesi.put("LT", 20); // Lituania
		paesi.put("LU", 20); // Lussemburgo
		paesi.put("MK", 19); // Macedonia
		paesi.put("MT", 31); // Malta
		paesi.put("MU", 30); // Mauritius
		paesi.put("MC", 27); // Monaco
		paesi.put("ME", 22); // Montenegro
		paesi.put("NL", 18); // Olanda
		paesi.put("NO", 15); // Norvegia
		paesi.put("PL", 28); // Polonia
		paesi.put("PT", 25); // Portogallo
		paesi.put("CZ", 24); // Repubblica Ceca
		paesi.put("RO", 24); // Romania
		paesi.put("SM", 27); // San Marino
		paesi.put("RS", 22); // Serbia
		paesi.put("SK", 24); // Slovacchia
		paesi.put("SI", 19); // Slovenia
		paesi.put("ES", 24); // Spagna
		paesi.put("SE", 24); // Svezia
		paesi.put("CH", 21); // Svizzera
		paesi.put("TN", 24); // Tunisia
		paesi.put("TR", 26); // Turchia
		paesi.put("HU", 28); // Ungheria

		if (paesi.containsKey(key))
			// key denota un paese valido
			return paesi.get(key).intValue();
		else
			// key non corrisponde a nessun paese aderente
			return 0;
	}

	/**
	 * Verifica che l'indirizzo email passato come parametro sia sintatticamente
	 * corretto.
	 * 
	 * @param eMail
	 *            indirizzo email da verificare
	 * @return boolean valore di verita'
	 */
	public static boolean isValidEmail(String eMail) {
		boolean matchFound = false;
		String patternStr = "^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(eMail);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica che l'url passata come parametro sia sintatticamente corretta.
	 * 
	 * @param url
	 *            indirizzo url da verificare
	 * @return boolean valore di verita'
	 */
	public static boolean isValidUrl(String url) {
		boolean matchFound = false;
		String patternStr = "^((http|https|ftp)\\://|www\\.)[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,4}(/[a-zA-Z0-9\\-\\._\\?=\\,\\'\\+%\\$#~]*[^\\.\\,\\)\\(\\s])*$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(url);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica che l'anno passato come parametro sia sintatticamente corretto.
	 * 
	 * @param year
	 *            anno da verificare
	 * @return boolean valore di verita'
	 */
	public static boolean isValidYear(String year) {
		boolean matchFound = false;
		String patternStr = "^([0-9]{4})$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(year);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica che il path passato come parametro sia sintatticamente corretto.
	 * 
	 * @param path
	 *            percorso da verificare
	 * @return boolean valore di verita'
	 */
	public static boolean isValidPath(String path) {
		boolean matchFound = false;
		String patternStr = "([A-Z]:\\[^/:\\*\\?<>\\|]+\\.\\w{2,6})|(\\{2}[^/:\\*\\?<>\\|]+\\.\\w{2,6})";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(path);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Restituisce vero se la partita IVA e' corretta, false altrimenti. Ritorna
	 * false anche se la lunghezza e' diversa da 11.
	 * 
	 * @param partitaIVA
	 *            - la partita IVA da controllare.
	 * @return vero se corretta, false altrimenti.
	 */
	public static boolean isPartitaIVA(String partitaIVA) {
		int i, c, s;
		if (partitaIVA.length() != 11)
			return false;
		for (i = 0; i < 11; i++) {
			if (partitaIVA.charAt(i) < '0' || partitaIVA.charAt(i) > '9')
				return false;
		}
		s = 0;
		for (i = 0; i <= 9; i += 2)
			s += partitaIVA.charAt(i) - '0';
		for (i = 1; i <= 9; i += 2) {
			c = 2 * (partitaIVA.charAt(i) - '0');
			if (c > 9)
				c = c - 9;
			s += c;
		}
		if ((10 - s % 10) % 10 != partitaIVA.charAt(10) - '0')
			return false;
		else
			return true;
	}

	/**
	 * Verifica che la stringa contenga i caratteri validi per un nome o un
	 * cognome o altre stringhe del genere quindi non valgono i numeri ne' i
	 * simboli eccetto l'apice ed il trattino (es: DELL'OLIO oppure DI-GEGLIE).
	 * I caratteri accentati non sono validi (es: calo' va scritto calo') Il
	 * blank e' valido (es: EDOARDO FILIPPO). Non e' un controllo di
	 * obbligatorieta' quindi se non valorizzato return true.
	 * 
	 * @param string
	 *            String
	 * @return boolean
	 */
	public static boolean isNoSymbolNoNumberString(String string) {
		if (!isValorizzato(string))
			return true;
		return string.matches("[a-zA-Z\\s'-]+");
	}

	/**
	 * Verifica che string sia un intero.
	 * 
	 * @param string
	 *            string
	 * @return true se string e' un intero, false altrimenti
	 */
	public static boolean isInt(String string) {
		if (!isValorizzato(string))
			return false;

		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static boolean isNumber15_3(BigDecimal bigDecimal) {
		return isNumberM_N(bigDecimal, 15, 3);
	}

	public static boolean isNumber15_3(Double d) {
		return isNumberM_N(BigDecimal.valueOf(d), 15, 3);
	}

	public static boolean isNumberM_N(Double d, int precision, int scale) {
		return isNumberM_N(BigDecimal.valueOf(d), precision, scale);
	}

	public static boolean isNumberM_N(BigDecimal bigDecimal, int precision, int scale) {
		if (bigDecimal == null) {
			return true;
		}

		bigDecimal.stripTrailingZeros();
		return bigDecimal.precision() <= precision && bigDecimal.scale() <= scale;
	}

	public static boolean isXForwardedForValido(String xForwardedFor) {
		if (!xForwardedFor.contains(","))
			return isIpAddressValido(xForwardedFor);
		String[] split = xForwardedFor.split(Pattern.quote(","));
		for (String s : split) {
			s = s.trim();
			if (!isIpAddressValido(s)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isIpAddressValido(String ip) {
		String[] split = ip.split(Pattern.quote("."));
		if (split.length != 4)
			return false;
		for (String s : split) {
			if (!isNumericString(s))
				return false;
		}
		return true;
	}

	public static boolean isUuidValido(String uuid) {
		if (uuid == null)
			return false;
		if (uuid.length() != 36)
			return false;
		String[] split = uuid.split(Pattern.quote("-"));
		if (split.length != 5)
			return false;
		if (split[0].length() != 8 || split[1].length() != 4 || split[2].length() != 4 || split[3].length() != 4 || split[4].length() != 12)
			return false;
		return true;
	}

	public static boolean isDataItalian(String data) {
		return isData(data, "dd/MM/yyyy", null);
	}
	
	/**
	 * Verifica la valorizzazione dei valori di un hashmap. Al primo non valorizzato
	 * si esce restituendo la chiave.
	 * @param inputAsMap
	 * @return
	 */
	public static String checkObbligatori(HashMap<Object, Object> inputAsMap) {
		for (Object key : inputAsMap.keySet()) {
			Object value = inputAsMap.get(key);
			if (value instanceof String) {
				if (value == null ||  ((String)value).trim().length()==0)
					return key.toString();
			} else if (value == null) //per payload passati vuoti.
				return key.toString();
		}
		return null;
	}
	
	public static String checkObbligatoriNoShib(HashMap<Object, Object> inputAsMap) {
		for (Object key : inputAsMap.keySet()) {
			Object value = inputAsMap.get(key);
			if (!key.toString().equalsIgnoreCase("Shib-Identita-CodiceFiscale")) {
			if (value instanceof String) {
				if (value == null ||  ((String)value).trim().length()==0)
					return key.toString();
			} else if (value == null) //per payload passati vuoti.
				return key.toString();
			}
		}
		return null;
	}
	
	public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
	
	public static boolean isMaggiorenne(Date birthDate) {
		LocalDate dataNascita = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return calculateAge(dataNascita, today) >= 18;
	}
	
	
	public static boolean  isCodiceENI(String string) {
		
		if(string.length() != 16)
			return false;
		
		string = string.toUpperCase();
		boolean matchFound = false;
		String patternStr = "^ENI[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
		
	}
	
	public static boolean  isCodiceSTP(String string) {
		
		if(string.length() != 16)
			return false;
		
		string=string.toUpperCase();
		boolean matchFound = false;
		String patternStr = "^STP[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
		
	}
	
	public static boolean isCodiceFiscaleCoerente(String codicefiscale,String nome,String cognome,String sesso, Date dataNascita) {
		
		codicefiscale = getCodiceFiscaleDecodificato(codicefiscale);
		codicefiscale = codicefiscale.substring(0, 11).toUpperCase();
		
		
		nome = nome.toUpperCase().trim();
		// Controllo la sintassi della stringa nome
		String pattern = "[A-Z" + String.valueOf((char) 0xC0) + String.valueOf((char) 0xC8) + String.valueOf((char) 0xC9) + String.valueOf((char) 0xCC) + String.valueOf((char) 0xD2)
		+ String.valueOf((char) 0xD9) + "'\\s\\-]+";
		if (!Pattern.compile(pattern).matcher(nome).matches()) {
			return false;
		} else {
			nome = nome.replaceAll("[" + String.valueOf((char) 0xC0) + "]", "A");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xC9) + "]", "E");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xC8) + "]", "E");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xCC) + "]", "I");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xD2) + "]", "O");
			nome = nome.replaceAll("[" + String.valueOf((char) 0xD9) + "]", "U");
			nome = nome.replaceAll("['\\s\\-]", "");
		}
		
		
		cognome = cognome.toUpperCase().trim();
		// Controllo la sintassi della stringa cognome
		if (!Pattern.compile(pattern).matcher(cognome).matches()) {
			return false;
		} else {
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xC0) + "]", "A");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xC9) + "]", "E");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xC8) + "]", "E");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xCC) + "]", "I");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xD2) + "]", "O");
			cognome = cognome.replaceAll("[" + String.valueOf((char) 0xD9) + "]", "U");
			cognome = cognome.replaceAll("['\\s\\-]", "");
		}
		
		if (dataNascita == null) {
			return false;
		}

		sesso = sesso.toUpperCase().trim();
		if (!("M".equals(sesso) || "F".equals(sesso))) {
			return false;
		}
		
		
		StringBuilder codfisc = new StringBuilder();

		// Cognome
		StringBuilder cogn = new StringBuilder();
		cogn.append(ottieniConsVoc(cognome, true) + ottieniConsVoc(cognome, false));

		if (cogn.length() > 3)
			cogn = new StringBuilder(cogn.substring(0, 3));

		for (int i = cogn.length(); i < 3; i++)
			cogn.append("X");

		codfisc.append(cogn);

		// Nome
		StringBuilder codice = new StringBuilder(ottieniConsVoc(nome, true));
		if (codice.length() >= 4)
			codice = codice.delete(1, 2);
		codice.append(ottieniConsVoc(nome, false));

		if (codice.length() > 3)
			codice = codice.replace(0, codice.length(), codice.substring(0, 3));

		for (int i = codice.length(); i < 3; i++)
			codice.append("X");

		codfisc.append(codice);
		
		StringBuilder cod = new StringBuilder();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dataNascita);

		int giorno = cal.get(GregorianCalendar.DAY_OF_MONTH);
		int mese = cal.get(GregorianCalendar.MONTH);
		int anno = cal.get(GregorianCalendar.YEAR);

		if (Integer.toString(anno).length() != 4)
			return false; // anno di nascita non valido

		cod.append((anno + "").substring(2, 4));
		cod.append(getCodiceMese(mese));

		if (sesso.equals("M")) {
			cod.append(String.format("%02d", giorno));
		} else {
			giorno += 40;
			cod.append(giorno + "");
		}

		codfisc.append(cod);
		
		if(!codicefiscale.equals(codfisc.toString()))
			return false;
		
//		String codDecodificato = getCodiceFiscaleDecodificato(codicefiscale);
//		if (codicefiscale.substring(0, 11).equalsIgnoreCase(codDecodificato.substring(0, 15)))
//			return true;
		
		return true;
		
	}
	
	public static boolean ValidaCfEnte(String cf)
	{
		if( ! cf.matches("^[0-9]{11}$") )
			return false;
		int s = 0;
		for(int i = 0; i < 11; i++){
			int n = cf.charAt(i) - '0';
			if( (i & 1) == 1 ){
				n *= 2;
				if( n > 9 )
					n -= 9;
			}
			s += n;
		}
		if( s % 10 != 0 )
			return false;
		return true;
	}
}