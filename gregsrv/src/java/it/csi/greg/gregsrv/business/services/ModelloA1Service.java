/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ModelloA1Dao;
import it.csi.greg.gregsrv.business.entity.GregDVoceModA1;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModA1;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloA1Input;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelVociA1;
import it.csi.greg.gregsrv.dto.SaveModelloA1Input;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.apache.commons.io.FileUtils;

@Service("modelloA1Service")
public class ModelloA1Service {

	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected ModelloA1Dao modelloA1Dao;
	@Autowired
	protected ModelloAService modelloAService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected MailService mailService;

	public List<ModelVociA1> getIntestazioniModA1(Integer idEnte) {
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idEnte);
		int annorend = rendicontazione.getAnnoGestione();
		int annoattuale = rendicontazione.getAnnoGestione() + 1;
		List<GregDVoceModA1> intestazioni = modelloA1Dao.findAllVociModelloA1();
		List<ModelVociA1> intestazionifinali = new ArrayList<ModelVociA1>();
		for (GregDVoceModA1 intvoce : intestazioni) {
			if (!intvoce.getCodVoceModA1().equalsIgnoreCase("I3")) {
				intvoce.setDescVoceModA1(intvoce.getDescVoceModA1().replace("anno", String.valueOf(annorend)));
			} else if (intvoce.getCodVoceModA1().equalsIgnoreCase("I3")) {
				intvoce.setDescVoceModA1(intvoce.getDescVoceModA1().replace("anno", String.valueOf(annoattuale)));
			}
			intestazionifinali.add(new ModelVociA1(intvoce));
		}
		return intestazionifinali;
	}

	@SuppressWarnings("rawtypes")
	public List<ModelDatiA1> getDatiModelloA1(Integer idente) {
		ArrayList<ModelDatiA1> elencovoci = new ArrayList<ModelDatiA1>();
		List<GregDVoceModA1> voci = modelloA1Dao.findSoloImporti();
		List<Object> entiresult = modelloA1Dao.getDatiModelloA1(idente);

		Iterator itr = entiresult.iterator();
		Integer primocomune = null;
		BigDecimal somma = BigDecimal.ZERO;
		boolean esci = false;
		try {
			while (itr.hasNext()) {
				ModelDatiA1 voce = new ModelDatiA1();
				Object[] obj = (Object[]) itr.next();
				// prendo il comune e poi riempio i dati
				Integer codcomune = ((Integer) obj[7]);
				if (primocomune != null) {
					if (primocomune.equals(codcomune))
						esci = true;
					else {
						primocomune = codcomune;
						esci = false;
					}
				} else {
					primocomune = codcomune;
					esci = false;
				}
				if (!esci) {
					if (Checker.isValorizzato(String.valueOf(obj[2])))
						voce.setCodcomune(String.valueOf(obj[2]));
					else
						voce.setCodcomune(null);
					if (Checker.isValorizzato(String.valueOf(obj[3])))
						voce.setDesccomune(String.valueOf(obj[3]));
					else
						voce.setDesccomune(null);
					if (obj[5] != null) {
						voce.setDataInizioValidita((Date) obj[5]);
					} else {
						voce.setDataInizioValidita(null);
					}
					if (obj[6] != null) {
						voce.setDataFineValidita((Date) obj[6]);
					} else {
						voce.setDataFineValidita(null);
					}
					if (obj[7] != null) {
						voce.setIdComune((Integer) obj[7]);
					} else {
						voce.setIdComune(null);
					}
					// ciclo ancora sul vettore
					Iterator itr1 = entiresult.iterator();
					Map<String, String> listavalori = new HashMap<String, String>();
					while (itr1.hasNext()) {
						Object[] obj1 = (Object[]) itr1.next();
						if (codcomune.equals(((Integer) obj1[7]))) {
							if (Checker.isValorizzato(String.valueOf(obj1[4]))) {
								if (String.valueOf(obj1[1]).equalsIgnoreCase("I2")) {
									somma = somma.add(
											new BigDecimal(String.valueOf(obj1[4])).setScale(2, RoundingMode.HALF_UP));
								}
								listavalori.put(String.valueOf(obj1[1]),
										Util.convertBigDecimalToString(new BigDecimal(String.valueOf(obj1[4]))));
							} else
								listavalori.put(String.valueOf(obj1[1]), null);
						}
					}
					// aggiungi valori
					Map<String, String> listavaloritutti = new HashMap<String, String>();
					for (GregDVoceModA1 colimporto : voci) {
						if (!listavalori.containsKey(colimporto.getCodVoceModA1())) {
							listavaloritutti.put(colimporto.getCodVoceModA1(), null);
						} else {
							listavaloritutti.put(colimporto.getCodVoceModA1(),
									listavalori.get(colimporto.getCodVoceModA1()));
						}
					}
					voce.setValore(listavaloritutti);
					elencovoci.add(voce);
				}
			}
			Object totale = modelloA1Dao.getDatiModelloA1totale(idente);
			ModelDatiA1 vocetotale = new ModelDatiA1();
			vocetotale.setCodcomune(null);
			vocetotale.setDesccomune(null);
			Map<String, String> totaleval = new HashMap<String, String>();
			Object[] objtot = (Object[]) totale;
			if (Checker.isValorizzato(String.valueOf(objtot[1])))
				totaleval.put(String.valueOf(objtot[0]),
						Util.convertBigDecimalToString(new BigDecimal(String.valueOf(objtot[1]))));
			else
				totaleval.put(String.valueOf(objtot[0]), null);
			vocetotale.setValore(totaleval);
			elencovoci.add(vocetotale);
			// aggiungo totate I2
			vocetotale = new ModelDatiA1();
			totaleval = new HashMap<String, String>();
			vocetotale.setCodcomune("Somma");
			vocetotale.setDesccomune(null);
			totaleval.put("I2", Util.convertBigDecimalToString(somma));
			vocetotale.setValore(totaleval);
			elencovoci.add(vocetotale);
			return elencovoci;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public SaveModelloOutput saveModelloA1(SaveModelloA1Input body, UserInfo userInfo) throws Exception {
		SaveModelloOutput response = new SaveModelloOutput();
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService
				.getRendicontazione(body.getIdRendicontazioneEnte());

		String newNotaEnte = "";

		String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
		// Controllo e aggiorno lo stato della rendicontazione
		datiRendicontazioneService.modificaStatoRendicontazione(rendicontazione, userInfo,
				SharedConstants.OPERAZIONE_SALVA, body.getProfilo());
		String statoNew = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
		if (!statoOld.equals(statoNew)) {
			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO).getTestoMessaggio()
					.replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
					.replace("STATONEW", "'" + statoNew + "'");
		}

		// Recupero eventuale ultima cronologia inserita
		GregTCronologia lastCrono = datiRendicontazioneService
				.findLastCronologiaEnte(rendicontazione.getIdRendicontazioneEnte());

		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
//		if ((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE))
//				|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {

			if (!Checker.isValorizzato(body.getCronologia().getNotaEnte())) {
				String msgUpdateDati = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI)
						.getTestoMessaggio().replace("OPERAZIONE", "SALVA");
				newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
						: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
								? msgUpdateDati
								: newNotaEnte;
			} else {
				// newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " +
				// body.getCronologia().getNotaEnte() : body.getCronologia().getNotaEnte();
				newNotaEnte = body.getCronologia().getNotaEnte();
			}

		} else {
			// newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " +
			// body.getCronologia().getNotaEnte() : body.getCronologia().getNotaEnte();
			newNotaEnte = body.getCronologia().getNotaEnte();
		}

		// SALVO NOTE DI CRONOLOGIA
		if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(body.getCronologia().getNotaInterna())) {
			GregTCronologia cronologia = new GregTCronologia();
			cronologia.setGregTRendicontazioneEnte(rendicontazione);
			cronologia.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
			cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
			cronologia.setModello("Mod. A1");
			cronologia.setUtenteOperazione(userInfo.getCodFisc());
			cronologia.setNotaInterna(body.getCronologia().getNotaInterna());
			cronologia.setNotaPerEnte(newNotaEnte);
			cronologia.setDataOra(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataModifica(new Timestamp(System.currentTimeMillis()));
			datiRendicontazioneService.insertCronologia(cronologia);
		}

		// inserisco gli importi
		for (ModelDatiA1 dati : body.getDatiA1()) {
			if (Checker.isValorizzato(dati.getCodcomune()) && !dati.getCodcomune().equalsIgnoreCase("Somma")) {
				for (Map.Entry<String, String> entry : dati.getValore().entrySet()) {
					boolean trovato = false;
					Iterator<GregRRendicontazioneModA1> iterator = rendicontazione.getGregRRendicontazioneModA1s()
							.iterator();
					while (iterator.hasNext()) {
						GregRRendicontazioneModA1 temp = iterator.next();
						if (temp.getGregDVoceModA1().getCodVoceModA1().equalsIgnoreCase(entry.getKey())
								&& temp.getGregDComuni().getIdComune().equals(dati.getIdComune())) {
							if (entry.getValue() != null) { // aggiorna
								temp.setValore(new BigDecimal(entry.getValue().replace(".", "").replace(",", ".")));
								temp.setDataModifica(new Timestamp(System.currentTimeMillis()));
								temp.setUtenteOperazione(userInfo.getCodFisc());
								modelloA1Dao.updateModelloA1(temp);
							} else {// cancella
								modelloA1Dao.deleteVoceModelloA1(temp.getIdRendicontazioneModA1());
							}
							trovato = true;
						}
					}
					if (!trovato && entry.getValue() != null) {
						// se non trovato nella rendicontazione se valore nullo non fare nulla
						// altrimenti inserisci
						GregRRendicontazioneModA1 insval = new GregRRendicontazioneModA1();
						insval.setGregDComuni(
								modelloA1Dao.getComuneById(dati.getIdComune()));
//						insval.setGregDComuni(
//								modelloA1Dao.getComuneByCod(dati.getCodcomune()));
						insval.setGregDVoceModA1(modelloA1Dao.getVoceByCodVoce(entry.getKey()));
						insval.setGregTRendicontazioneEnte(rendicontazione);
						insval.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						insval.setValore(new BigDecimal(entry.getValue().replace(".", "").replace(",", ".")));
						insval.setDataModifica(new Timestamp(System.currentTimeMillis()));
						insval.setUtenteOperazione(userInfo.getCodFisc());
						insval.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
						modelloA1Dao.insertModelloA1(insval);
					}
				}
			} else if (!Checker.isValorizzato(dati.getCodcomune())) {

				for (Map.Entry<String, String> entry : dati.getValore().entrySet()) {
					boolean trovato = false;
					Iterator<GregRRendicontazioneModA1> iterator = rendicontazione.getGregRRendicontazioneModA1s()
							.iterator();
					while (iterator.hasNext()) {
						GregRRendicontazioneModA1 temp = iterator.next();
						if (temp.getGregDVoceModA1().getCodVoceModA1().equalsIgnoreCase(entry.getKey())) {
							if (entry.getValue() != null) { // aggiorna
								temp.setValore(new BigDecimal(entry.getValue().replace(".", "").replace(",", ".")));
								temp.setDataModifica(new Timestamp(System.currentTimeMillis()));
								temp.setUtenteOperazione(userInfo.getCodFisc());
								modelloA1Dao.updateModelloA1(temp);
							} else {// cancella
								modelloA1Dao.deleteVoceModelloA1(temp.getIdRendicontazioneModA1());
							}
							trovato = true;
						}
					}
					if (!trovato && entry.getValue() != null) {
						// se non trovato nella rendicontazione se valore nullo non fare nulla
						// altrimenti inserisci
						GregRRendicontazioneModA1 insval = new GregRRendicontazioneModA1();
						insval.setGregDComuni(null);
						insval.setGregDVoceModA1(modelloA1Dao.getVoceByCodVoce(entry.getKey()));
						insval.setGregTRendicontazioneEnte(rendicontazione);
						insval.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						insval.setValore(new BigDecimal(entry.getValue().replace(".", "").replace(",", ".")));
						insval.setDataModifica(new Timestamp(System.currentTimeMillis()));
						insval.setUtenteOperazione(userInfo.getCodFisc());
						insval.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
						modelloA1Dao.insertModelloA1(insval);
					}
				}
			}
		}
		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdEnte());
			// Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
						ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
						SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
				response.setId(HttpStatus.OK.toString());
				response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			} else {
				response.setId(HttpStatus.OK.toString());
				response.setWarnings(null);
				response.setErrors(null);
			}
		}
		return response;
	}

	public boolean controllaImportoModA1(List<ModelDatiA1> datimodel) throws IntegritaException {
		// controllo tutti i campi di tipo importo not null
		BigDecimal somma = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totale = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		for (ModelDatiA1 dati : datimodel) {
			for (Map.Entry<String, String> entry : dati.getValore().entrySet()) {
				controlloService.numberDecimalValidate(
						entry.getValue() == null ? "0" : entry.getValue().replace(".", ""),
						"Importo " + entry.getKey() + " " + dati.getDesccomune(), 14, 2);
				// controllo somma
				if (entry.getKey().equalsIgnoreCase(SharedConstants.OPERAZIONE_GETVOCIA1_I2)) {
					if (dati.getCodcomune() != null && !dati.getCodcomune().equalsIgnoreCase("Somma")) {
						if (entry.getValue() != null)
							somma = somma.add(new BigDecimal(entry.getValue().replace(".", "").replace(",", "."))
									.setScale(2, RoundingMode.HALF_UP));
					} else if (dati.getCodcomune().equalsIgnoreCase("Somma")) {
						if (entry.getValue() != null) {
							totale = new BigDecimal(entry.getValue().replace(".", "").replace(",", ".")).setScale(2,
									RoundingMode.HALF_UP);
						}
					}
				}
			}
		}
		return somma.equals(totale);
	}

	@Transactional
	public String esportaModelloA1(EsportaModelloA1Input body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloA1");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		DataFormat format = workbook.createDataFormat();
		Row row = sheet.createRow(rowCount);
		// crea stili arial 12 bold italic
		CellStyle cellStyleb12I = sheet.getWorkbook().createCellStyle();
		cellStyleb12I.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb12I = sheet.getWorkbook().createFont();
		fontb12I.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb12I.setFontHeightInPoints((short) 12);
		fontb12I.setFontName(HSSFFont.FONT_ARIAL);
		fontb12I.setItalic(true);
		cellStyleb12I.setFont(fontb12I);
		cellStyleb12I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 12 bold
		CellStyle cellStyle12b = sheet.getWorkbook().createCellStyle();
		cellStyle12b.setAlignment(CellStyle.ALIGN_LEFT);
		Font font12b = sheet.getWorkbook().createFont();
		font12b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		;
		font12b.setFontHeightInPoints((short) 12);
		font12b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle12b.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle12b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12b.setFont(font12b);
		cellStyle12b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo
		CellStyle cellStyletitolo = sheet.getWorkbook().createCellStyle();
		cellStyletitolo.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo.setFont(font12b);
		cellStyletitolo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial totale
		CellStyle cellStyletotale = sheet.getWorkbook().createCellStyle();
		cellStyletotale.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletotale.setFont(font12b);
		cellStyletotale.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyletotale.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletotale.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 bold
		CellStyle cellStyle8b = sheet.getWorkbook().createCellStyle();
		cellStyle8b.setAlignment(CellStyle.ALIGN_CENTER);
		Font font8b = sheet.getWorkbook().createFont();
		font8b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font8b.setFontHeightInPoints((short) 8);
		font8b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle8b.setFont(font8b);
		cellStyle8b.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle8b.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle8b.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle8b.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle8b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// stile grigio
		// crea stili arial 8 bold
		CellStyle cellStyle8bg = sheet.getWorkbook().createCellStyle();
		cellStyle8bg.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle8bg.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle8bg.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle8bg.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle8bg.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle8bg.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle8bg.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle8bg.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 corsivo
		CellStyle cellStyle10c = sheet.getWorkbook().createCellStyle();
		cellStyle10c.setAlignment(CellStyle.ALIGN_RIGHT);
		Font font10c = sheet.getWorkbook().createFont();
		font10c.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10c.setFontHeightInPoints((short) 10);
		font10c.setFontName(HSSFFont.FONT_ARIAL);
		font10c.setItalic(true);
		cellStyle10c.setFont(font10c);
		cellStyle10c.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10c.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10c.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyle10c.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10c.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial
		CellStyle cellStyle10 = sheet.getWorkbook().createCellStyle();
		cellStyle10.setAlignment(CellStyle.ALIGN_LEFT);
		Font font10 = sheet.getWorkbook().createFont();
		font10.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10.setFontHeightInPoints((short) 10);
		font10.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle10.setFont(font10);
		cellStyle10.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold corsivo
		CellStyle cellStyle10bc = sheet.getWorkbook().createCellStyle();
		cellStyle10bc.setAlignment(CellStyle.ALIGN_RIGHT);
		Font font10bc = sheet.getWorkbook().createFont();
		font10bc.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font10bc.setFontHeightInPoints((short) 10);
		font10bc.setFontName(HSSFFont.FONT_ARIAL);
		font10bc.setItalic(true);
		cellStyle10bc.setFont(font10bc);
		cellStyle10bc.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyle10bc.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyle10bc.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyle10bc.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyle10bc.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle10bc.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle10bc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// totale green
		CellStyle cellStyleTotVal = sheet.getWorkbook().createCellStyle();
		cellStyleTotVal.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotVal.setFont(font10bc);
		cellStyleTotVal.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleTotVal.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleTotVal.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotVal.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleTotVal.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleTotVal.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotVal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stile bordo destro
		CellStyle cellStylebd = sheet.getWorkbook().createCellStyle();
		cellStylebd.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStylebd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stile bordo sinistro
		CellStyle cellStylebs = sheet.getWorkbook().createCellStyle();
		cellStylebs.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStylebs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stile bordo bottom
		CellStyle cellStylebb = sheet.getWorkbook().createCellStyle();
		cellStylebb.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStylebb.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stile bordo bottom sin
		CellStyle cellStylebbss = sheet.getWorkbook().createCellStyle();
		cellStylebbss.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStylebbss.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStylebbss.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stile bordo bottom sin
		CellStyle cellStylebbdd = sheet.getWorkbook().createCellStyle();
		cellStylebbdd.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStylebbdd.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStylebbdd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_A1);
		Cell cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "ENTE GESTORE DENOMINAZIONE E NUMERO :");
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - "
				+ body.getDenominazioneEnte());
		cell.setCellStyle(cellStyle12by);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyletitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("COL1MODELLOA1").getValtext());
		cell.setCellStyle(cellStyle8b);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL2MODELLOA1").getValtext());
		cell.setCellStyle(cellStyle8b);
		String descsomma = null;
		String valsomma = null;
		String valtotale = null;
		for (ModelVociA1 voce : body.getVociA1()) {
			if (!voce.getCodvoce().equalsIgnoreCase("T1")) {
				cell = row.createCell(++columnCount);
				if (voce.getCodvoce().equalsIgnoreCase("I3")) {
					cell.setCellValue((String) voce.getDescVoceModA1().replace("anno", annoril.toString()));
					cell.setCellStyle(cellStyle8bg);
				} else
					cell.setCellValue((String) voce.getDescVoceModA1().replace("anno",
							rendicontazione.getAnnoGestione().toString()));
				cell.setCellStyle(cellStyle8bg);
			} else
				descsomma = voce.getDescVoceModA1().replace("anno", annoril.toString());
		}
		// inserisco gli importi
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		for (ModelDatiA1 dati : body.getDatiA1()) {
			// scrivo il comune
			if (Checker.isValorizzato(dati.getCodcomune()) && !dati.getCodcomune().equalsIgnoreCase("Somma")) {
				cell.setCellValue((String) dati.getCodcomune());
				cell.setCellStyle(cellStyle10);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) dati.getDesccomune());
				cell.setCellStyle(cellStyle10);
			}
			for (ModelVociA1 voce : body.getVociA1()) {
				for (Map.Entry<String, String> entry : dati.getValore().entrySet()) {
					if (voce.getCodvoce().equalsIgnoreCase(entry.getKey())) {
						if (Checker.isValorizzato(dati.getCodcomune())
								&& !dati.getCodcomune().equalsIgnoreCase("Somma")) {
							cell = row.createCell(++columnCount);
							if (Checker.isValorizzato(entry.getValue())) {
								cell.setCellValue((double) Double
										.valueOf(entry.getValue().replaceAll("\\.", "").replace(",", ".")));
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							}
							cell.setCellStyle(cellStyle10c);
							cell.getCellStyle().setDataFormat(format.getFormat(pattern));
						} else if (Checker.isValorizzato(dati.getCodcomune())
								&& dati.getCodcomune().equalsIgnoreCase("Somma")) {
							valtotale = entry.getValue();
						} else if (!Checker.isValorizzato(dati.getCodcomune())) {
							valsomma = entry.getValue();
						}
					}
				}
			}
			if (Checker.isValorizzato(dati.getCodcomune()) && !dati.getCodcomune().equalsIgnoreCase("Somma")) {
				row = sheet.createRow(++rowCount);
				columnCount = 0;
				cell = row.createCell(columnCount);
			}
		}
		row.createCell(++columnCount);
		cell.setCellValue((String) "TOTALE");
		cell.setCellStyle(cellStyletotale);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bc);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bc);
		cell.setCellValue((double) Double.valueOf(valtotale.replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleTotVal);
		cell.getCellStyle().setDataFormat(format.getFormat(pattern));
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bc);
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) descsomma);
		cell.setCellStyle(cellStyle8bg);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebbss);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebb);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebb);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebb);
		if(valsomma!=null) {
			cell.setCellValue((double) Double.valueOf(valsomma.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else {
			cell.setCellValue("");
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		cell.setCellStyle(cellStyle10c);
		cell.getCellStyle().setDataFormat(format.getFormat(pattern));
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebbdd);
		Util.autoSizeColumns(sheet.getWorkbook());
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloA1_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));

	}

	@Transactional
	public GenericResponseWarnErr checkModelloA1(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());
		response.setObblMotivazione(false);
		boolean presenzaAttuale = false;
		boolean presenzaPassata = false;
		GregTRendicontazioneEnte rendicontazioneAttuale = datiRendicontazioneService
				.getRendicontazione(idRendicontazione);
		GregTRendicontazioneEnte rendicontazionePassata = datiRendicontazioneService.getRendicontazionePassata(
				rendicontazioneAttuale.getGregTSchedeEntiGestori().getIdSchedaEnteGestore(),
				(rendicontazioneAttuale.getAnnoGestione() - 1));
		if (rendicontazionePassata != null) {
			List<ModelDatiA1> datiModelloAttuale = getDatiModelloA1(rendicontazioneAttuale.getIdRendicontazioneEnte());
			List<ModelDatiA1> datiModelloPassata = getDatiModelloA1(rendicontazionePassata.getIdRendicontazioneEnte());

			BigDecimal totaleAttuale = BigDecimal.ZERO;
			BigDecimal totalePassata = BigDecimal.ZERO;

			for (ModelDatiA1 dato : datiModelloAttuale) {
				if (dato.getCodcomune() != null && dato.getCodcomune().equals("Somma")) {
					if (dato.getValore().get("I2") != null) {
						totaleAttuale = totaleAttuale.add(Util.convertStringToBigDecimal(dato.getValore().get("I2"))
								.setScale(2, RoundingMode.HALF_UP));
					}
				}
			}

			for (ModelDatiA1 dato : datiModelloPassata) {
				if (dato.getCodcomune() != null && dato.getCodcomune().equals("Somma")) {
					if (dato.getValore().get("I2") != null) {
						totalePassata = totalePassata.add(Util.convertStringToBigDecimal(dato.getValore().get("I2"))
								.setScale(2, RoundingMode.HALF_UP));
					}
				}
			}

			BigDecimal totale25 = (totalePassata.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);

			if ((totaleAttuale.setScale(2)).compareTo(totalePassata.subtract(totale25).setScale(2)) < 0
					|| (totaleAttuale.setScale(2)).compareTo(totalePassata.add(totale25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLOA101).getTestoMessaggio()
								.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleAttuale))
								.replace("DATOPASSATO", Util.convertBigDecimalToString(totalePassata)));
				response.setObblMotivazione(true);
			}

			if (response.getWarnings().size() > 0) {
				List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_A1,
						rendicontazioneAttuale.getIdRendicontazioneEnte());
				if (checkA.size() > 0) {
					response.getWarnings().add(
							listeService.getMessaggio(SharedConstants.CHECK_MOTIVAZIONE_ESISTE).getTestoMessaggio());
				}
			}

			if (response.isObblMotivazione()) {
				response.setDescrizione("");
			} else {
				response.setDescrizione(listeService.getMessaggio(SharedConstants.CHECK_OK).getTestoMessaggio());
				response.setId("OK");
			}
		}
		return response;
	}

	public String getStatoModelloEnte(Integer idRendicontazione, String codTranche, String codStatoRendicontazione) {

		ModelStatoMod stato = modelloA1Dao.getStatoModelloA1(idRendicontazione);
		if (!stato.isValorizzato()) {

			if (!codTranche.equals(SharedConstants.TRANCHEI)) {
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)) {
					return SharedConstants.NON_DISPONIBILE;
				} else if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)) {
					return SharedConstants.NON_COMPILATO;
				} else if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.NON_DISPONIBILE;
				}
			} else {
				if (!codTranche.equals(SharedConstants.TRANCHEII)) {
					if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
							|| codStatoRendicontazione
									.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
						return SharedConstants.NON_DISPONIBILE;
					}
				}
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)) {
					return SharedConstants.NON_COMPILATO;
				} else if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.COMPILATO;
				}
			}

		} else {
			if (codTranche.equals(SharedConstants.TRANCHEI)) {
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.COMPILATO;
				} else {
					return stato.getStato();
				}
			} else if (codTranche.equals(SharedConstants.TRANCHEII)) {
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.COMPILATO;
				} else {
					return stato.getStato();
				}
			}
		}
		return null;
	}

	@Transactional
	public GenericResponseWarnErr controlloModelloA1(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		List<ModelValoriModA> cella06 = new ArrayList<ModelValoriModA>();

		BigDecimal valorecella06 = null;

		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_A1);
		boolean facoltativo = false;
		boolean valorizzato = modelloA1Dao.getValorizzatoModelloA1(idRendicontazione);
		if (!valorizzato) {
			response.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO)
					.getTestoMessaggio().replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}
		if (!facoltativo) {
			// chiama funzione modello a
			cella06 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
					SharedConstants.MODELLO_A_06);
			// verifica se il campo avvalorato
			if (cella06.size() != 0) {
				valorecella06 = cella06.get(0).getValore();
			}

			// chiama funzione modello a1
			// prendo i dati del modello A1
			int contavalorimodelloA1 = 0;
			// if (!comunecapoluogo) {
			List<ModelDatiA1> modelloa1 = getDatiModelloA1(rendicontazione.getIdRendicontazioneEnte());
			for (ModelDatiA1 datimodel : modelloa1) {
				if (datimodel.getCodcomune() != null) {
					if (!datimodel.getCodcomune().equalsIgnoreCase("Somma")) {
						// se tutti i dati sono obbligatori
						// verificare se il campo puo' essere 0
						for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
							if (entry.getValue() != null) {
								contavalorimodelloA1 = contavalorimodelloA1 + 1;
								String warning = controlloService.numberDecimalValidate(
										entry.getValue() == null ? "0" : entry.getValue().replace(".", ""),
										"Importo " + entry.getKey() + " " + datimodel.getDesccomune(), 14, 2);
								if (warning != null) {
									response.getErrors().add(warning);
								}
							}
						}
					} else {
						// campo somma da controllare uguale al valore del modello A se presente
						for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
							if (entry.getValue() != null && !entry.getValue().equalsIgnoreCase(",00")) {
								if (valorecella06 != null) {
									if (!valorecella06.setScale(2, RoundingMode.HALF_UP)
											.equals(new BigDecimal(entry.getValue().replace(".", "").replace(",", "."))
													.setScale(2, RoundingMode.HALF_UP))) {
										// valori differenti
										response.getErrors()
												.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_06)
														.getTestoMessaggio());
									} else if (valorecella06.setScale(2, RoundingMode.HALF_UP)
											.equals(new BigDecimal(entry.getValue().replace(".", "").replace(",", "."))
													.setScale(2, RoundingMode.HALF_UP))
											&& valorecella06.setScale(2, RoundingMode.HALF_UP)
													.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))) {
										// uguale zero
										response.getErrors()
												.add(listeService
														.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_NO06)
														.getTestoMessaggio());
									}
								} else {
									response.getErrors().add(listeService
											.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_06).getTestoMessaggio());
								}
							} else {
								if (valorecella06 != null && !valorecella06.equals(BigDecimal.ZERO)) {
									// valori differenti
									response.getErrors().add(listeService
											.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_06).getTestoMessaggio());
								} else {
									response.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_NO06)
													.getTestoMessaggio());
								}
							}
						}

					}
				} else {
					// caso totale
					for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
						if (entry.getValue() != null) {
							contavalorimodelloA1 = contavalorimodelloA1 + 1;
							String warning = controlloService.numberDecimalValidate(
									entry.getValue() == null ? "0" : entry.getValue().replace(".", ""),
									"Importo " + entry.getKey() + " " + datimodel.getDesccomune(), 12, 2);
							if (warning != null) {
								response.getErrors().add(warning);
							}
						} else {
							if (modello.getCodObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)) {

								if (entry.getKey().equalsIgnoreCase("T1")) {
									response.getErrors().add(listeService
											.getMessaggio(SharedConstants.ERROR_CODE_DATI_OBBLIGATORI)
											.getTestoMessaggio().replace("MODELLODEF", "A1")
											.replace("SPECIFICARE", modelloA1Dao.getVoceByCodVoce(entry.getKey())
													.getDescVoceModA1().replace("anno",
															String.valueOf(rendicontazione.getAnnoGestione()))));
								}
							}
						}
					}
				}
			}
		}

		return response;

	}

}
