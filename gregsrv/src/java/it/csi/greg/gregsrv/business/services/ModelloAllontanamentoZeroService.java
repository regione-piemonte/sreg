/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.DatiEnteDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloAllontanamentoZeroDao;
import it.csi.greg.gregsrv.business.entity.GregTAllegatiRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.DatiAllontanamentoZeroCSV_DTO;
import it.csi.greg.gregsrv.dto.DatiAllontanamentoZeroDTO;
import it.csi.greg.gregsrv.dto.FondiEnteAllontanamentoZero;
import it.csi.greg.gregsrv.dto.ModelAllegatiAssociati;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg1;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg2;
import it.csi.greg.gregsrv.dto.ModelB1Voci;
import it.csi.greg.gregsrv.dto.ModelB1VociPrestReg2;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.PrestazioniAlZeroPerFnpsDTO;
import it.csi.greg.gregsrv.dto.PrestazioniAllontanamentoZeroDTO;
import it.csi.greg.gregsrv.dto.ToggleValidazioneAllontanamentoZeroDTO;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloAllontanamentoZeroService")
public class ModelloAllontanamentoZeroService {

	@Autowired
	ModelloAllontanamentoZeroDao mazd;
	@Autowired
	protected ModelloB1Service modelloB1Service;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected DatiEnteDao datiEnteDao;

	public DatiAllontanamentoZeroDTO findAllPrestazioniAllontanamentoZero(Integer idRendEnte) {

		List<PrestazioniAllontanamentoZeroDTO> lista = mazd.findAllPrestazioniAllontanamentoZero(idRendEnte);
		String giustificativo = mazd.findGiustificativoAllontanamentoZero(idRendEnte);
		DatiAllontanamentoZeroDTO datiAlZero = new DatiAllontanamentoZeroDTO();
		datiAlZero.setGiustificativo(giustificativo);
		datiAlZero.setLista(lista);

		List<GregTAllegatiRendicontazione> allegatiNow = findAllAllegatiAssociati(idRendEnte);
		if (allegatiNow != null) {
			GregTAllegatiRendicontazione allegatoNew = allegatiNow.stream()
					.filter(f -> f.getTipoDocumentazione().equals(SharedConstants.AL_ZERO)).findFirst().orElse(null);
			if (allegatoNew != null) {
				ModelAllegatiAssociati fileResponse = new ModelAllegatiAssociati();
				fileResponse.setDimensione(allegatoNew.getFileSize());
				fileResponse.setNomeFile(allegatoNew.getNomeFile());
				fileResponse.setNoteFile(allegatoNew.getNoteFile());
				fileResponse.setNuovo(null);
				fileResponse.setPkAllegatoAssociato(allegatoNew.getIdAllegatiRendicontazione());
				fileResponse.setTipoDocumentazione(allegatoNew.getTipoDocumentazione());
				fileResponse.setUtenteOperazione(null);

				datiAlZero.setFileAlZerodb(fileResponse);
				datiAlZero.setFileAlZero(null);
			} else {
				datiAlZero.setFileAlZerodb(null);
				datiAlZero.setFileAlZero(null);
			}
		}
		return datiAlZero;
	}

	public boolean getValidazioneAlZero(Integer idRendicontazioneEnte) {

		return mazd.getValidazioneAlZero(idRendicontazioneEnte);
	}

	public void toggleValidazioneAlZero(ToggleValidazioneAllontanamentoZeroDTO toggle, UserInfo userInfo) {
		boolean validazione = mazd.getValidazioneAlZero(toggle.getIdRendicontazioneEnte());
		if (toggle.getToggle() != validazione) {
			mazd.toggleValidazioneAlZero(toggle);

			// CRONOLOGIA
			GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService
					.getRendicontazione(toggle.getIdRendicontazioneEnte());
//
//		String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione();
//		// Controllo e aggiorno lo stato della rendicontazione
//		datiRendicontazioneService.modificaStatoRendicontazione(rendicontazione, userInfo,
//				SharedConstants.OPERAZIONE_SALVA, body.getProfilo());
//		String statoNew = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione();
			String newNotaEnte = "";
//		if (!statoOld.equals(statoNew)) {
//			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO).getTestoMessaggio()
//					.replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
//					.replace("STATONEW", "'" + statoNew + "'");
//		}

			// Recupero eventuale ultima cronologia inserita

			GregTCronologia lastCrono = datiRendicontazioneService
					.findLastCronologiaEnte(toggle.getIdRendicontazioneEnte());

//		if((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE)) 
//
//				|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {
			String msgUpdateDati = "";
			if (toggle.getProfilo() != null && (toggle.getProfilo().getListaazioni().get("CronologiaEnte")[1]
					|| toggle.getProfilo().getListaazioni().get("CronologiaRegionale")[1])) {
				if (!Checker.isValorizzato(toggle.getCronologia().getNotaEnte())) {
					if (toggle.getToggle()) {
						msgUpdateDati = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI)
								.getTestoMessaggio().replace("OPERAZIONE", "CONFERMA RESPONSABILE");
					} else {
						msgUpdateDati = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI)
								.getTestoMessaggio().replace("OPERAZIONE", "CHE ANNULLA LA CONFERMA RESPONSABILE IN AL-ZERO");
					}
					newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: msgUpdateDati;
				} else {
					newNotaEnte = toggle.getCronologia().getNotaEnte();
				}
			} else {
				newNotaEnte = toggle.getCronologia().getNotaEnte();
			}

			// SALVO NOTE DI CRONOLOGIA
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(toggle.getCronologia().getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendicontazione);
				cronologia.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello(toggle.getModello());
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(toggle.getCronologia().getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(new Timestamp(System.currentTimeMillis()));
				cronologia.setDataCreazione(new Timestamp(System.currentTimeMillis()));
				cronologia.setDataModifica(new Timestamp(System.currentTimeMillis()));
				datiRendicontazioneService.insertCronologia(cronologia);
			}
		}
	}

	public FondiEnteAllontanamentoZero findFondiEnteAllontanamentoZero(BigInteger idEnte) {
		GregTRendicontazioneEnte rendicontazioneEnte = datiRendicontazioneService.getRendicontazione(idEnte.intValue());

		FondiEnteAllontanamentoZero fondi = mazd.findFondiEnteAllontanamentoZero(idEnte,
				rendicontazioneEnte.getAnnoGestione());

		return fondi;
	}

	public Boolean canActiveModFnps(Integer idScheda) {
		Boolean canActiveModFnpsForB1 = false;

		List<ModelB1Voci> rendB1 = modelloB1Service.getVoci(idScheda);
		for (ModelB1Voci voceB1 : rendB1) {
//			for (ModelB1Macroaggregati modelB1Macro : voceB1.getMacroaggregati()) {
//				if (modelB1Macro.getValore() != null
//						&& Util.convertStringToBigDecimal(modelB1Macro.getValore()).compareTo(BigDecimal.ZERO) == 1) {
//					canActiveModFnpsForB1 = true;
//					break;
//				}
//			}
//			if (canActiveModFnpsForB1) {
//				break;
//			}

			for (ModelB1UtenzaPrestReg1 modelB1Utenza : voceB1.getUtenze()) {
				// Only if cod is equals to U01(Minori e famiglia)
				if (modelB1Utenza.getValore() != null
						&& Util.convertStringToBigDecimal(modelB1Utenza.getValore()).compareTo(BigDecimal.ZERO) == 1
						&& modelB1Utenza.getCodice().equals("U01")) {
					canActiveModFnpsForB1 = true;
					break;
				}
			}
			if (canActiveModFnpsForB1) {
				break;
			}

			for (ModelB1VociPrestReg2 modelB1VocePrest : voceB1.getPrestazioniRegionali2()) {
				for (ModelB1UtenzaPrestReg2 prestReg : modelB1VocePrest.getUtenze()) {
					if (prestReg.getValore() != null
							&& Util.convertStringToBigDecimal(prestReg.getValore()).compareTo(BigDecimal.ZERO) == 1
							&& prestReg.getCodice().equals("U01")) {
						canActiveModFnpsForB1 = true;
						break;
					}
				}
				if (canActiveModFnpsForB1) {
					break;
				}

			}
			if (canActiveModFnpsForB1) {
				break;
			}

			for (ModelB1UtenzaPrestReg1 modelB1UtenzaPrestCosto : voceB1.getUtenzeCostoTotale()) {
				if (modelB1UtenzaPrestCosto.getValore() != null && Util
						.convertStringToBigDecimal(modelB1UtenzaPrestCosto.getValore()).compareTo(BigDecimal.ZERO) == 1
						&& modelB1UtenzaPrestCosto.getCodice().equals("U01")) {
					canActiveModFnpsForB1 = true;
					break;
				}
			}
			if (canActiveModFnpsForB1) {
				break;
			}

			for (ModelB1UtenzaPrestReg1 modelB1UtenzaPrestQuota : voceB1.getUtenzeQuotaSocioAssistenziale()) {
				if (modelB1UtenzaPrestQuota.getValore() != null && Util
						.convertStringToBigDecimal(modelB1UtenzaPrestQuota.getValore()).compareTo(BigDecimal.ZERO) == 1
						&& modelB1UtenzaPrestQuota.getCodice().equals("U01")) {
					canActiveModFnpsForB1 = true;
					break;
				}
			}
			if (canActiveModFnpsForB1) {
				break;
			}
		}

		return canActiveModFnpsForB1;
	}

	public void postModelloAlZero(DatiAllontanamentoZeroDTO dati, UserInfo userInfo, Integer idRendicontazioneEnte) {

		GregTRendicontazioneEnte rendicontazioneEnte = datiRendicontazioneService
				.getRendicontazione(idRendicontazioneEnte);

		// INSERIMENTO NOTA
		// Recupero eventuale ultima cronologia inserita
		if (!dati.isConfermaResponsabile()) {
			GregTCronologia lastCrono = datiRendicontazioneService.findLastCronologiaEnte(idRendicontazioneEnte);
			String newNotaEnte = "";
			if (dati.getProfilo() != null && dati.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
				if (!Checker.isValorizzato(dati.getNotaEnte())) {
					String msgUpdateDati = listeService
							.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI).getTestoMessaggio()
							.replace("OPERAZIONE", "SALVA");
					newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: newNotaEnte;
				} else {
					newNotaEnte = dati.getNotaEnte();
				}

			} else {
				newNotaEnte = dati.getNotaEnte();
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(dati.getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendicontazioneEnte);
				cronologia.setGregDStatoRendicontazione(rendicontazioneEnte.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Mod. Al-Zero");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(dati.getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(new Timestamp(new Date().getTime()));
				cronologia.setDataCreazione(new Timestamp(new Date().getTime()));
				cronologia.setDataModifica(new Timestamp(new Date().getTime()));
				datiRendicontazioneService.insertCronologia(cronologia);
			}
		}
		List<GregTAllegatiRendicontazione> listAllegatiNew = new ArrayList<>();
		GregTAllegatiRendicontazione newAllegatoIniziale = new GregTAllegatiRendicontazione();

		// Esisteva gia' un file e ne e' stato caricato un'altro
		if (dati.getFileAlZero() != null && dati.getFileAlZerodb() == null) {
			// Costruisco nuovo file
			byte[] fileInizialeBytes = java.util.Base64.getDecoder().decode(dati.getFileAlZero().getFileBytes());
			newAllegatoIniziale.setFileAllegato(fileInizialeBytes);
			newAllegatoIniziale.setFileSize(dati.getFileAlZero().getDimensione());
			newAllegatoIniziale.setNomeFile(dati.getFileAlZero().getNomeFile());
			newAllegatoIniziale.setNoteFile(
					Checker.isValorizzato(dati.getFileAlZero().getNoteFile()) ? dati.getFileAlZero().getNoteFile()
							: null);
			newAllegatoIniziale.setTipoDocumentazione(SharedConstants.AL_ZERO);
			newAllegatoIniziale.setUtenteOperazione(userInfo.getCodFisc());
			newAllegatoIniziale.setGregTRendicontazioneEnte(rendicontazioneEnte);
			listAllegatiNew.add(newAllegatoIniziale);
			// Ottengo file vecchio e lo cancello logicamente
			List<GregTAllegatiRendicontazione> allegatiNow = findAllAllegatiAssociati(idRendicontazioneEnte);
			GregTAllegatiRendicontazione allegatoNew = allegatiNow.stream()
					.filter(f -> f.getTipoDocumentazione().equals(SharedConstants.AL_ZERO)).findFirst().orElse(null);
			if (allegatoNew != null && allegatiNow != null) {
				allegatoNew.setDataModifica(new Timestamp(new Date().getTime()));
				allegatoNew.setDataCancellazione(new Timestamp(new Date().getTime()));
				datiEnteDao.updateAllegatoAssociato(allegatoNew);
			}
			// Inserisco il nuovo file
			newAllegatoIniziale.setDataCreazione(new Timestamp(new Date().getTime()));
			newAllegatoIniziale.setDataModifica(new Timestamp(new Date().getTime()));
			datiEnteDao.insertAllegato(newAllegatoIniziale);
		}
		if (dati.getFileAlZero() == null && dati.getFileAlZerodb() == null) {
			// Check se esisteva un file da cancellare
			List<GregTAllegatiRendicontazione> allegatiNow = findAllAllegatiAssociati(idRendicontazioneEnte);
			GregTAllegatiRendicontazione allegatoNew = allegatiNow.stream()
					.filter(f -> f.getTipoDocumentazione().equals(SharedConstants.AL_ZERO)).findFirst().orElse(null);
			if (allegatoNew != null && allegatiNow != null) {
				allegatoNew.setDataModifica(new Timestamp(new Date().getTime()));
				allegatoNew.setDataCancellazione(new Timestamp(new Date().getTime()));
				datiEnteDao.updateAllegatoAssociato(allegatoNew);
			}
		}

		// Prestazioni e valore associato
		// Check if exist every record
		for (PrestazioniAllontanamentoZeroDTO prestazione : dati.getLista()) {
			if (mazd.checkExistsRecordPrestazione(prestazione.getPrestazioneId(), idRendicontazioneEnte)) {
				if (prestazione.getValorePerPrestazioneAlZero() == null) {
					mazd.deleteRecordPrestazione(prestazione.getPrestazioneId(), idRendicontazioneEnte);
					continue;
				}
				mazd.updateRecordPrestazione(prestazione.getPrestazioneId(),
						prestazione.getValorePerPrestazioneAlZero(), userInfo.getCodFisc(), idRendicontazioneEnte);
			} else {
				if (prestazione.getValorePerPrestazioneAlZero() != null) {
					mazd.insertRecordPrestazione(prestazione.getPrestazioneId(), idRendicontazioneEnte,
							prestazione.getValorePerPrestazioneAlZero(), userInfo.getCodFisc());
				}
			}
		}
		// Giustificazione
		String giustificativoOld = mazd.checkExistsRecordGiustificazione(idRendicontazioneEnte);
		String notaGiustificativo = "";
		if (giustificativoOld != null) {
			// If giustificazione is null
			if (dati.getGiustificativo() == null) {

				notaGiustificativo = listeService.getMessaggio(SharedConstants.MESSAGE_AZZERA_ALZERO)
						.getTestoMessaggio().replace("GIUSTIFICATIVO", giustificativoOld);
				// Inserimento nuova cronologia
				if (Checker.isValorizzato(notaGiustificativo)) {
					GregTCronologia cronologia = new GregTCronologia();
					cronologia.setGregTRendicontazioneEnte(rendicontazioneEnte);
					cronologia.setGregDStatoRendicontazione(rendicontazioneEnte.getGregDStatoRendicontazione());
					cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
					cronologia.setModello("Mod. Al-Zero");
					cronologia.setUtenteOperazione(userInfo.getCodFisc());
					cronologia.setNotaInterna("");
					cronologia.setNotaPerEnte(notaGiustificativo);
					cronologia.setDataOra(new Timestamp(new Date().getTime()));
					cronologia.setDataCreazione(new Timestamp(new Date().getTime()));
					cronologia.setDataModifica(new Timestamp(new Date().getTime()));
					datiRendicontazioneService.insertCronologia(cronologia);
				}

				mazd.deleteLogicGiustificativo(idRendicontazioneEnte, userInfo.getCodFisc());
			} else {
				mazd.updateRecordGiustificativo(idRendicontazioneEnte, dati.getGiustificativo(), userInfo.getCodFisc());
			}
		} else {
			if (dati.getGiustificativo() != null) {
				mazd.insertRecordGiustificativo(idRendicontazioneEnte, dati.getGiustificativo(), userInfo.getCodFisc());
			}
		}

	}

	public List<GregTAllegatiRendicontazione> findAllAllegatiAssociati(Integer idScheda) {
		List<GregTAllegatiRendicontazione> resultList = new ArrayList<GregTAllegatiRendicontazione>();
		resultList = datiEnteDao.findAllAllegatiAssociati(idScheda);
		return resultList;
	}

	public String getStatusModelloAlZero(Integer idRendicontazioneEnte) {
		return mazd.getStatusModelloAlZero(idRendicontazioneEnte,
				datiRendicontazioneService.getRendicontazione(idRendicontazioneEnte).getAnnoGestione());
	}

	public String makeCsvAlZero(DatiAllontanamentoZeroCSV_DTO dati, Integer idRendicontazioneEnte) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModuloAlZero");
		int rowCount = 0;
		int columnCount = 0;
		Row row = sheet.createRow(rowCount);

		HSSFColor azzurroChiaro = getColorExcel(workbook, "#DBEEF7");
		short azzurroChiaroValue = azzurroChiaro.getIndex();
		HSSFColor blue = getColorExcel(workbook, "#006CB4");
		short blueValue = blue.getIndex();

		List<ModelMsgInformativo> listaMsgInformativi = listeService.findMsgInformativi("ALZERO01");
		String infoAlZero01 = listaMsgInformativi.get(0).getTestoMsgInformativo();
		listaMsgInformativi = listeService.findMsgInformativi("ALZERO03");
		String infoAlZero03 = listaMsgInformativi.get(0).getTestoMsgInformativo();
		listaMsgInformativi = listeService.findMsgInformativi("ALZERO04");
		String infoAlZero04 = listaMsgInformativi.get(0).getTestoMsgInformativo();

		// ----FONT---------------------------------------------------------------------
		// font size 12, wrapped, bold, align left
		CellStyle text_12_wrapped_bold_left = sheet.getWorkbook().createCellStyle();
		text_12_wrapped_bold_left.setAlignment(CellStyle.ALIGN_LEFT);
		Font font_text_12_wrapped_bold_left = sheet.getWorkbook().createFont();
		font_text_12_wrapped_bold_left.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font_text_12_wrapped_bold_left.setFontHeightInPoints((short) 12);
		font_text_12_wrapped_bold_left.setFontName(HSSFFont.FONT_ARIAL);
		font_text_12_wrapped_bold_left.setItalic(true);
		text_12_wrapped_bold_left.setWrapText(true);
		text_12_wrapped_bold_left.setFont(font_text_12_wrapped_bold_left);
		text_12_wrapped_bold_left.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// font size 12, wrapped, bold, align left
		CellStyle text_12_wrapped_bold_left_white = sheet.getWorkbook().createCellStyle();
		text_12_wrapped_bold_left.setAlignment(CellStyle.ALIGN_LEFT);
		Font font_text_12_wrapped_bold_left_white = sheet.getWorkbook().createFont();
		font_text_12_wrapped_bold_left_white.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font_text_12_wrapped_bold_left_white.setFontHeightInPoints((short) 12);
		font_text_12_wrapped_bold_left_white.setFontName(HSSFFont.FONT_ARIAL);
		font_text_12_wrapped_bold_left_white.setItalic(true);
		font_text_12_wrapped_bold_left_white.setColor(HSSFColor.WHITE.index);
		text_12_wrapped_bold_left_white.setWrapText(true);
		text_12_wrapped_bold_left_white.setFont(font_text_12_wrapped_bold_left_white);
		text_12_wrapped_bold_left_white.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// font size 12, wrapped, bold, align center
		CellStyle text_12_wrapped_bold_center = sheet.getWorkbook().createCellStyle();
		text_12_wrapped_bold_center.setAlignment(CellStyle.ALIGN_CENTER);
		Font font_text_12_wrapped_bold_center = sheet.getWorkbook().createFont();
		font_text_12_wrapped_bold_center.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font_text_12_wrapped_bold_center.setFontHeightInPoints((short) 12);
		font_text_12_wrapped_bold_center.setFontName(HSSFFont.FONT_ARIAL);
		font_text_12_wrapped_bold_center.setItalic(true);
		text_12_wrapped_bold_center.setWrapText(true);
		text_12_wrapped_bold_center.setFont(font_text_12_wrapped_bold_center);
		text_12_wrapped_bold_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// font size 12, wrapped, not bold, align left
		CellStyle text_12_wrapped_not_bold_left = sheet.getWorkbook().createCellStyle();
		text_12_wrapped_not_bold_left.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb_text_12_wrapped_not_bold_left = sheet.getWorkbook().createFont();
		fontb_text_12_wrapped_not_bold_left.setFontHeightInPoints((short) 12);
		fontb_text_12_wrapped_not_bold_left.setFontName(HSSFFont.FONT_ARIAL);
		fontb_text_12_wrapped_not_bold_left.setItalic(true);
		text_12_wrapped_not_bold_left.setWrapText(true);
		text_12_wrapped_not_bold_left.setFont(fontb_text_12_wrapped_not_bold_left);
		text_12_wrapped_not_bold_left.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// font size 12, wrapped, not bold, align center
		CellStyle text_12_wrapped_not_bold_center = sheet.getWorkbook().createCellStyle();
		text_12_wrapped_not_bold_center.setAlignment(CellStyle.ALIGN_CENTER);
		Font fontb_text_12_wrapped_not_bold_center = sheet.getWorkbook().createFont();
		fontb_text_12_wrapped_not_bold_center.setFontHeightInPoints((short) 12);
		fontb_text_12_wrapped_not_bold_center.setFontName(HSSFFont.FONT_ARIAL);
		fontb_text_12_wrapped_not_bold_center.setItalic(true);
		text_12_wrapped_not_bold_center.setWrapText(true);
		text_12_wrapped_not_bold_center.setFont(fontb_text_12_wrapped_not_bold_center);
		text_12_wrapped_not_bold_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// font size 12, wrapped, not bold, align center
		CellStyle text_12_wrapped_not_bold_center_white = sheet.getWorkbook().createCellStyle();
		text_12_wrapped_not_bold_center_white.setAlignment(CellStyle.ALIGN_CENTER);
		Font fontb_text_12_wrapped_not_bold_center_white = sheet.getWorkbook().createFont();
		fontb_text_12_wrapped_not_bold_center_white.setFontHeightInPoints((short) 12);
		fontb_text_12_wrapped_not_bold_center_white.setFontName(HSSFFont.FONT_ARIAL);
		fontb_text_12_wrapped_not_bold_center_white.setItalic(true);
		fontb_text_12_wrapped_not_bold_center_white.setColor(HSSFColor.WHITE.index);
		text_12_wrapped_not_bold_center_white.setWrapText(true);
		text_12_wrapped_not_bold_center_white.setFont(fontb_text_12_wrapped_not_bold_center_white);
		text_12_wrapped_not_bold_center_white.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

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
		// crea stili arial 12 bold Yellow
		CellStyle cellStyleb12Y = sheet.getWorkbook().createCellStyle();
		cellStyleb12Y.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb12Y = sheet.getWorkbook().createFont();
		fontb12Y.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb12Y.setFontHeightInPoints((short) 12);
		fontb12Y.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleb12Y.setFont(fontb12I);
		cellStyleb12Y.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleb12Y.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyleb12Y.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// crea stili arial 12 bold
		CellStyle cellStyleb12 = sheet.getWorkbook().createCellStyle();
		cellStyleb12.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleb12.setFont(fontb12Y);
		cellStyleb12.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold Grey
		CellStyle cellStyleb12G = sheet.getWorkbook().createCellStyle();
		cellStyleb12G.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleb12G.setFont(fontb12I);
		cellStyleb12G.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleb12G.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyleb12G.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// ----------------------------------------------------------------------------------

		// RIGA 0
		ModelParametro parametro = listeService.getParametro("EXP");
		Cell cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 14, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(0);
		row.setHeight((short) (row.getHeight() * 2));
		// RIGA 1
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazioneEnte);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 8, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "ENTE GESTORE DENOMINAZIONE E NUMERO :");
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - "
				+ dati.getDenominazione());
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(1);
		row.setHeight((short) (row.getHeight() * 2));
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// RIGA 2
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 14, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
//		String annoRilevazione = String.valueOf(Integer.parseInt(body.getAnnoGestione()) + 1);
		cell.setCellValue((String) "ANNO DI RIFERIMENTO : " + Integer.toString(dati.getAnnoEsercizio())
				+ " - RILEVAZIONE : " + Integer.toString(dati.getAnnoEsercizio() + 1));
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(2);
		row.setHeight((short) (row.getHeight() * 2));
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// RIGA 3
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 14, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "Modulo Allontanamento Zero - " + infoAlZero01);
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(3);
		row.setHeight((short) (row.getHeight() * 2));
		// RIGA 4
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		// RIGA 5/6
		text_12_wrapped_not_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_not_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		FondiEnteAllontanamentoZero fondi = this
//				.findFondiEnteAllontanamentoZero(BigInteger.valueOf(idRendicontazioneEnte));
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(infoAlZero03);
		cell.setCellStyle(text_12_wrapped_not_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		if (dati.getFondoRegionale() != null) {
			cell.setCellValue(dati.getFondoRegionale());
		} else {
			cell.setCellValue("0,00");
		}
		cell.setCellStyle(text_12_wrapped_not_bold_center);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(5);
		row.setHeight((short) (row.getHeight() * 4));
		text_12_wrapped_not_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_not_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);

		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_bold_center.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		text_12_wrapped_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(infoAlZero04);
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		if (dati.getQuotaAlZero() != null) {
			cell.setCellValue(dati.getQuotaAlZero());
		} else {
			cell.setCellValue("0,00");
		}
		cell.setCellStyle(text_12_wrapped_bold_center);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(6);
		row.setHeight((short) (row.getHeight() * 6));
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_bold_center.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		text_12_wrapped_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// RIGA 7
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		// RIGA 8
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 14, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "Modulo Allontanamento Zero - " + infoAlZero01);
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(8);
		row.setHeight((short) (row.getHeight() * 2));
		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// RIGHE PRESTAZIONI
		for (int i = 0; i < dati.getLista().size(); i++) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue(dati.getLista().get(i).getPrestazioneCod());
			cell.setCellStyle(text_12_wrapped_bold_center);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			cell = row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue(dati.getLista().get(i).getPrestazioneDesc());
			cell.setCellStyle(text_12_wrapped_bold_left);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);

			// Spazio
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue("");
			cell.setCellStyle(text_12_wrapped_not_bold_left);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			// Descrizione Massimale B1
			text_12_wrapped_not_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			text_12_wrapped_not_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
			text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell = row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue("Massimale corrispondente alla spesa da B1");
			cell.setCellStyle(text_12_wrapped_not_bold_left);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			// Valore massimale B1
			cell = row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			if (dati.getLista().get(i).getValorePerFamiglieMinoriB1() != null) {
				cell.setCellValue(dati.getLista().get(i).getValorePerFamiglieMinoriB1());
			} else {
				cell.setCellValue("0,00");
			}
			cell.setCellStyle(text_12_wrapped_not_bold_center);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			text_12_wrapped_not_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			text_12_wrapped_not_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
			text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// Spazio
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue("");
			cell.setCellStyle(text_12_wrapped_not_bold_left);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			// Descrizione Spesa Allontanamento Zero
			text_12_wrapped_not_bold_left.setFillForegroundColor(azzurroChiaroValue);
			text_12_wrapped_not_bold_center.setFillForegroundColor(azzurroChiaroValue);
			text_12_wrapped_not_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
			text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell = row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue("Spesa rendicontata Mod. Al. Zero");
			cell.setCellStyle(text_12_wrapped_not_bold_left);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			// Valore Spesa Allontanamento Zero
			cell = row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			if (dati.getLista().get(i).getValorePerPrestazioneAlZero() != null) {
				cell.setCellValue(dati.getLista().get(i).getValorePerPrestazioneAlZero());
			} else {
				cell.setCellValue("0,00");
			}
			cell.setCellStyle(text_12_wrapped_not_bold_center);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			text_12_wrapped_not_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			text_12_wrapped_not_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
			text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);

			// RIGA GRIGIA
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 14, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue("");
			cell.setCellStyle(cellStyleb12G);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());

			// GESTIONE ALTEZZA RIGHE
			if (i == 0) {
				row = sheet.getRow(i + 9);
				row.setHeight((short) (row.getHeight() * 3));
				row = sheet.getRow(i + 9 + 1);
				row.setHeight((short) (row.getHeight() * 2));
				row = sheet.getRow(i + 9 + 2);
				row.setHeight((short) (row.getHeight() * 2));
			} else {
				row = sheet.getRow((i * 4) + 9);
				row.setHeight((short) (row.getHeight() * 3));
				row = sheet.getRow((i * 4) + 10);
				row.setHeight((short) (row.getHeight() * 2));
				row = sheet.getRow((i * 4) + 11);
				row.setHeight((short) (row.getHeight() * 2));
			}

		}

		// RIGA vuota
		columnCount = 0;
		row = sheet.createRow(++rowCount);

		listaMsgInformativi = listeService.findMsgInformativi("LABELALZERO"); // GET MESAGGI
		String info0 = listaMsgInformativi.get(0).getTestoMsgInformativo();
		String info1 = listaMsgInformativi.get(1).getTestoMsgInformativo();
		String info2 = listaMsgInformativi.get(2).getTestoMsgInformativo();
		String info3 = listaMsgInformativi.get(3).getTestoMsgInformativo();

		// TOTALE MASSIMALE B1
//		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//		text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		columnCount = 0;
//		row = sheet.createRow(++rowCount);
//		cell = row.createCell(columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 9, columnCount);
//		sheet.addMergedRegion(cellRangeAddress);
//		cell.setCellValue(info0);
//		cell.setCellStyle(text_12_wrapped_bold_left);
//		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
//		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
//		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
//		cell = row.createCell(++columnCount);
//		row.createCell(++columnCount);
//		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
//		sheet.addMergedRegion(cellRangeAddress);
//		cell.setCellValue(dati.getTotaleValoriB1());
//		cell.setCellStyle(text_12_wrapped_not_bold_center);
//		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
//		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
//		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
//		row = sheet.getRow(8 + (dati.getLista().size() * 4) + 2);
//		row.setHeight((short) (row.getHeight() * 3));
//		text_12_wrapped_bold_left.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//		text_12_wrapped_not_bold_center.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//		text_12_wrapped_bold_left.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		text_12_wrapped_not_bold_center.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// TOTALE RENDICONTAZIONE AL ZERO
		text_12_wrapped_bold_left_white.setFillForegroundColor(blueValue);
		text_12_wrapped_not_bold_center_white.setFillForegroundColor(blueValue);
		text_12_wrapped_bold_left_white.setFillPattern(CellStyle.SOLID_FOREGROUND);
		text_12_wrapped_not_bold_center_white.setFillPattern(CellStyle.SOLID_FOREGROUND);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 9, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(info3);
		cell.setCellStyle(text_12_wrapped_bold_left_white);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(dati.getTotaleValoriAlZero());
		cell.setCellStyle(text_12_wrapped_not_bold_center_white);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(8 + (dati.getLista().size() * 4) + 2);
		row.setHeight((short) (row.getHeight() * 3));
		text_12_wrapped_bold_left_white.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_not_bold_center_white.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		text_12_wrapped_bold_left_white.setFillPattern(CellStyle.SOLID_FOREGROUND);
		text_12_wrapped_not_bold_center_white.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// RESIDUO
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 9, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(info1);
		cell.setCellStyle(text_12_wrapped_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(dati.getResiduo());
		cell.setCellStyle(text_12_wrapped_not_bold_center);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(8 + (dati.getLista().size() * 4) + 3);
		row.setHeight((short) (row.getHeight() * 3));

		// RIGA vuota
		columnCount = 0;
		row = sheet.createRow(++rowCount);

		// GIUSTIFICAZIONE testo
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 11, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(info2);
		cell.setCellStyle(text_12_wrapped_bold_center);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(8 + (dati.getLista().size() * 4) + 5);
		row.setHeight((short) (row.getHeight() * 3));
		// GIUSTIFICAZIONE valore
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 11, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		if (dati.getGiustificativo() != null) {
			cell.setCellValue(dati.getGiustificativo());
		} else {
			cell.setCellValue("");
		}
		cell.setCellStyle(text_12_wrapped_not_bold_left);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.getRow(8 + (dati.getLista().size() * 4) + 6);
		row.setHeight((short) (row.getHeight() * 6));

		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModAlZero_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));
	}

	private HSSFColor getColorExcel(HSSFWorkbook workbook, String colore) {
		HSSFPalette palette = workbook.getCustomPalette();
		Integer[] rgb = Converter.hex2Rgb(colore);
		HSSFColor fColor = palette.findColor(rgb[0].byteValue(), rgb[1].byteValue(), rgb[2].byteValue());

		if (fColor != null)
			return fColor;

		HSSFColor sColor = palette.findSimilarColor(rgb[0].byteValue(), rgb[1].byteValue(), rgb[2].byteValue());
		if (sColor.getIndex() == HSSFColor.CORNFLOWER_BLUE.index)
			sColor = palette.getColor(sColor.getIndex() + 1);
		palette.setColorAtIndex(sColor.getIndex(), rgb[0].byteValue(), rgb[1].byteValue(), rgb[2].byteValue());
		return palette.getColor(sColor.getIndex());
	}

	public List<PrestazioniAlZeroPerFnpsDTO> getPrestazioniAdattamentoFnps(Integer idRendicontazioneEnte) {
		List<PrestazioniAlZeroPerFnpsDTO> resultList = new ArrayList<PrestazioniAlZeroPerFnpsDTO>();
		resultList = mazd.getPrestazioniAdattamentoFnps(idRendicontazioneEnte);
		return resultList;
	}
}
