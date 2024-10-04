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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ModelloFDao;
import it.csi.greg.gregsrv.business.entity.GregDPersonaleEnte;
import it.csi.greg.gregsrv.business.entity.GregDProfiloProfessionale;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRConteggioMonteOreSett;
import it.csi.greg.gregsrv.business.entity.GregRConteggioPersonale;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModFParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModFParte2;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloFInput;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelCPrestazioni;
import it.csi.greg.gregsrv.dto.ModelCTargetUtenze;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelFConteggi;
import it.csi.greg.gregsrv.dto.ModelFConteggioOre;
import it.csi.greg.gregsrv.dto.ModelFConteggioPersonale;
import it.csi.greg.gregsrv.dto.ModelFPersonaleEnte;
import it.csi.greg.gregsrv.dto.ModelFProfiloProfessionale;
import it.csi.greg.gregsrv.dto.ModelFValori;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelRendModAPart3;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModC;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModF;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelTotalePrestazioniB1;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.MonteOre;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.dto.VociModelloA;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloFService")
public class ModelloFService {

	@Autowired
	protected ModelloFDao modelloFDao;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
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

	public ModelFConteggi getConteggi() {
		List<GregDProfiloProfessionale> profiloProf = modelloFDao.findAllProfiloProfessionale();
		List<GregDPersonaleEnte> personaleEnte = modelloFDao.findAllPersonaleEnte();
		List<GregRConteggioPersonale> conteggioPersonale = modelloFDao.findAllConteggioPersonale();
		List<GregRConteggioMonteOreSett> conteggioOre = modelloFDao.findAllConteggioMonteOreSett();

		ModelFConteggi conteggi = new ModelFConteggi();
		ModelFConteggioPersonale conteggioP = new ModelFConteggioPersonale();
		ModelFConteggioOre conteggioO = new ModelFConteggioOre();
		List<ModelFProfiloProfessionale> profilo = new ArrayList<ModelFProfiloProfessionale>();

		for (GregDProfiloProfessionale profiloProfessionale : profiloProf) {
			ModelFProfiloProfessionale prof = new ModelFProfiloProfessionale();
			prof.setCodProfiloProfessionale(profiloProfessionale.getCodProfiloProfessionale());
			prof.setDescProfiloProfessionale(profiloProfessionale.getDescProfiloProfessionale());
			profilo.add(prof);
		}

		conteggioP.setListaProfiloProfessionale(profilo);
		conteggioO.setListaProfiloProfessionale(profilo);
		List<ModelFPersonaleEnte> personaleContPers = new ArrayList<ModelFPersonaleEnte>();
		List<ModelFPersonaleEnte> personaleContOre = new ArrayList<ModelFPersonaleEnte>();

		for (GregDPersonaleEnte perso : personaleEnte) {
			ModelFPersonaleEnte personale = new ModelFPersonaleEnte();
			personale.setCodPersonaleEnte(perso.getCodPersonaleEnte());
			personale.setDescPersonaleEnte(perso.getDescPersonaleEnte());
			List<ModelFValori> valori = new ArrayList<ModelFValori>();

			for (GregDProfiloProfessionale profiloProfessionale : profiloProf) {
				ModelFValori val = new ModelFValori();
				if (perso.getConteggioPersonale()) {
					for (GregRConteggioPersonale conteP : conteggioPersonale) {
						if (profiloProfessionale.getCodProfiloProfessionale()
								.equals(conteP.getGregDProfiloProfessionale().getCodProfiloProfessionale())
								&& personale.getCodPersonaleEnte()
										.equals(conteP.getGregDPersonaleEnte().getCodPersonaleEnte())) {
							val.setCodProfiloProfessionale(profiloProfessionale.getCodProfiloProfessionale());
						}
					}
				} else {
					for (GregRConteggioMonteOreSett conteO : conteggioOre) {
						if (profiloProfessionale.getCodProfiloProfessionale()
								.equals(conteO.getGregDProfiloProfessionale().getCodProfiloProfessionale())
								&& personale.getCodPersonaleEnte()
										.equals(conteO.getGregDPersonaleEnte().getCodPersonaleEnte())) {
							val.setCodProfiloProfessionale(profiloProfessionale.getCodProfiloProfessionale());
						}
					}
				}
				valori.add(val);
			}
			personale.setListaValori(valori);
			if (perso.getConteggioPersonale()) {
				personaleContPers.add(personale);
			} else {
				personaleContOre.add(personale);
			}

		}

		conteggioP.setListaPersonaleEnte(personaleContPers);
		conteggioO.setListaPersonaleEnte(personaleContOre);

		conteggi.setConteggioOre(conteggioO);
		conteggi.setConteggioPersonale(conteggioP);

		return conteggi;
	}

	public ModelRendicontazioneModF getRendicontazioneModF(Integer idScheda) {
		List<GregRRendicontazioneModFParte1> rendicontazioni1 = modelloFDao
				.findAllRendicontazioneParte1ModFByEnte(idScheda);
		List<GregRRendicontazioneModFParte2> rendicontazioni2 = modelloFDao
				.findAllRendicontazioneParte2ModFByEnte(idScheda);
		GregTRendicontazioneEnte rendente = datiRendicontazioneService.getRendicontazione(idScheda);
		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService
				.getSchedaEnte(rendente.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());

		ModelFConteggi conteggi = getConteggi();
		ModelRendicontazioneModF rendCorrente = new ModelRendicontazioneModF();
		rendCorrente.setIdRendicontazioneEnte(rendente.getIdRendicontazioneEnte());
		rendCorrente.setIdSchedaEnteGestore(schedaEnte.getIdSchedaEnteGestore());
		rendCorrente.setConteggi(conteggi);

		for (ModelFPersonaleEnte personale : rendCorrente.getConteggi().getConteggioPersonale()
				.getListaPersonaleEnte()) {
			for (ModelFValori valori : personale.getListaValori()) {
				for (GregRRendicontazioneModFParte1 rendicontazione1 : rendicontazioni1) {
					if (rendicontazione1.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
							.equals(rendCorrente.getIdRendicontazioneEnte())
							&& rendicontazione1.getGregRConteggioPersonale().getGregDProfiloProfessionale()
									.getCodProfiloProfessionale().equals(valori.getCodProfiloProfessionale())
							&& rendicontazione1.getGregRConteggioPersonale().getGregDPersonaleEnte()
									.getCodPersonaleEnte().equals(personale.getCodPersonaleEnte())) {
						valori.setValore(rendicontazione1.getValore());
					}
				}
			}
		}

		for (ModelFPersonaleEnte personale : rendCorrente.getConteggi().getConteggioOre().getListaPersonaleEnte()) {
			for (ModelFValori valori : personale.getListaValori()) {
				for (GregRRendicontazioneModFParte2 rendicontazione2 : rendicontazioni2) {
					if (rendicontazione2.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
							.equals(rendCorrente.getIdRendicontazioneEnte())
							&& rendicontazione2.getGregRConteggioMonteOreSett().getGregDProfiloProfessionale()
									.getCodProfiloProfessionale().equals(valori.getCodProfiloProfessionale())
							&& rendicontazione2.getGregRConteggioMonteOreSett().getGregDPersonaleEnte()
									.getCodPersonaleEnte().equals(personale.getCodPersonaleEnte())) {
						valori.setValore(rendicontazione2.getValore());
					}
				}
			}
		}
		return rendCorrente;
	}

	public SaveModelloOutput saveModelloF(ModelRendicontazioneModF body, UserInfo userInfo, String notaEnte,
			String notaInterna) throws Exception {
		SaveModelloOutput out = new SaveModelloOutput();
		// Verifico la presenza della rendicontazione
		GregTRendicontazioneEnte rendToUpdate = datiRendicontazioneService
				.getRendicontazione(body.getIdRendicontazioneEnte());

		if (rendToUpdate == null) {
			throw new IntegritaException(Util.composeMessage(
					listeService.getMessaggio(SharedConstants.ERROR_ANNO_CONTABILE).getTestoMessaggio(), ""));
		} else {
			Timestamp dataModifica = new Timestamp(new Date().getTime());
			String newNotaEnte = "";

			String statoOld = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			rendToUpdate = datiRendicontazioneService.modificaStatoRendicontazione(rendToUpdate, userInfo,
					SharedConstants.OPERAZIONE_SALVA, body.getProfilo());
			String statoNew = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			if (!statoOld.equals(statoNew)) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
						.getTestoMessaggio().replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
						.replace("STATONEW", "'" + statoNew + "'");
			}

			// Recupero eventuale ultima cronologia inserita
			GregTCronologia lastCrono = datiRendicontazioneService
					.findLastCronologiaEnte(rendToUpdate.getIdRendicontazioneEnte());

//			if ((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE))
//					|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {
			if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
				if (!Checker.isValorizzato(notaEnte)) {
					String msgUpdateDati = listeService
							.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI).getTestoMessaggio()
							.replace("OPERAZIONE", "SALVA");
					newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: newNotaEnte;
				} else {
					// newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " +
					// notaEnte : notaEnte;
					newNotaEnte = notaEnte;
				}

			} else {
				// newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " +
				// notaEnte : notaEnte;
				newNotaEnte = notaEnte;
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(notaInterna)) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendToUpdate);
				cronologia.setGregDStatoRendicontazione(rendToUpdate.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Mod. F");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(notaInterna);
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}

			List<ModelFPersonaleEnte> listaToSaveOrUpdate1 = body.getConteggi().getConteggioPersonale()
					.getListaPersonaleEnte();
			for (ModelFPersonaleEnte personale : listaToSaveOrUpdate1) {
				for (ModelFValori valori : personale.getListaValori()) {
					GregRRendicontazioneModFParte1 rendicontazione1 = modelloFDao
							.findRendincontazione1byProfProfPersEnte(valori.getCodProfiloProfessionale(),
									personale.getCodPersonaleEnte(), rendToUpdate.getIdRendicontazioneEnte());
					if (rendicontazione1 != null) {
						if (valori.getValore() == null) {
							modelloFDao.deleteRendicontazione1ModF(rendicontazione1.getIdRendicontazioneModFParte1());
						} else {
							rendicontazione1.setValore(valori.getValore());
							rendicontazione1.setDataModifica(dataModifica);
							modelloFDao.updateRendicontazione1ModF(rendicontazione1);
						}
					} else {
						if (valori.getValore() != null) {
							GregRRendicontazioneModFParte1 newRend = new GregRRendicontazioneModFParte1();
							GregRConteggioPersonale conteggioPersonale = modelloFDao
									.findConteggioPersonaleModFbyProfProfPers(valori.getCodProfiloProfessionale(),
											personale.getCodPersonaleEnte());
							newRend.setGregTRendicontazioneEnte(rendToUpdate);
							newRend.setGregRConteggioPersonale(conteggioPersonale);
							newRend.setValore(valori.getValore());
							newRend.setDataInizioValidita(dataModifica);
							newRend.setUtenteOperazione(userInfo.getCodFisc());
							newRend.setDataCreazione(dataModifica);
							newRend.setDataModifica(dataModifica);
							modelloFDao.insertRendicontazione1ModF(newRend);
						}
					}
				}
			}

			List<ModelFPersonaleEnte> listaToSaveOrUpdate2 = body.getConteggi().getConteggioOre()
					.getListaPersonaleEnte();
			for (ModelFPersonaleEnte personale : listaToSaveOrUpdate2) {
				for (ModelFValori valori : personale.getListaValori()) {
					GregRRendicontazioneModFParte2 rendicontazione2 = modelloFDao
							.findRendincontazione2byProfProfPersEnte(valori.getCodProfiloProfessionale(),
									personale.getCodPersonaleEnte(), rendToUpdate.getIdRendicontazioneEnte());
					if (rendicontazione2 != null) {
						if (valori.getValore() == null) {
							modelloFDao.deleteRendicontazione2ModF(rendicontazione2.getIdRendicontazioneModFParte2());
						} else {
							rendicontazione2.setValore(valori.getValore());
							rendicontazione2.setDataModifica(dataModifica);
							modelloFDao.updateRendicontazione2ModF(rendicontazione2);
						}
					} else {
						if (valori.getValore() != null) {
							GregRRendicontazioneModFParte2 newRend = new GregRRendicontazioneModFParte2();
							GregRConteggioMonteOreSett conteggioOre = modelloFDao
									.findConteggioMonteOreSettModFbyProfProfPers(valori.getCodProfiloProfessionale(),
											personale.getCodPersonaleEnte());
							newRend.setGregTRendicontazioneEnte(rendToUpdate);
							newRend.setGregRConteggioMonteOreSett(conteggioOre);
							newRend.setValore(valori.getValore());
							newRend.setDataInizioValidita(dataModifica);
							newRend.setUtenteOperazione(userInfo.getCodFisc());
							newRend.setDataCreazione(dataModifica);
							newRend.setDataModifica(dataModifica);
							modelloFDao.insertRendicontazione2ModF(newRend);
						}
					}
				}
			}

		}
		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdSchedaEnteGestore());
			// Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
						ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
						SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
				out.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				out.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		return out;
	}

	@Transactional
	public String esportaModelloF(EsportaModelloFInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloF");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###";
		DataFormat format = workbook.createDataFormat();
		Row row = sheet.createRow(rowCount);

		HSSFPalette palette = workbook.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 231, (byte) 230, (byte) 230);

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
		font12b.setFontHeightInPoints((short) 12);
		font12b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle12b.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle12b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12b.setFont(font12b);
		cellStyle12b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo
		CellStyle cellStyleTitoloModel = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloModel.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTitoloModel.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleTitoloModel.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTitoloModel.setFont(font12b);
		cellStyleTitoloModel.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stile label tabella personale/monte ore
		CellStyle cellStyleLabel = sheet.getWorkbook().createCellStyle();
		cellStyleLabel.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontLabel = sheet.getWorkbook().createFont();
		fontLabel.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontLabel.setFontHeightInPoints((short) 11);
		fontLabel.setFontName(HSSFFont.FONT_ARIAL);
		fontLabel.setItalic(true);
		cellStyleLabel.setFont(fontLabel);
		cellStyleLabel.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleLabel.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleLabel.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyleLabel.setWrapText(true);
		// crea stile cella sfondo bianco
		CellStyle cellWhiteBackground = sheet.getWorkbook().createCellStyle();
		cellWhiteBackground.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellWhiteBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// crea stile profilo professionale
		CellStyle cellStyleProfile = sheet.getWorkbook().createCellStyle();
		cellStyleProfile.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleProfile.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleProfile.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font fontProfile = sheet.getWorkbook().createFont();
		fontProfile.setFontHeightInPoints((short) 11);
		fontProfile.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleProfile.setFont(fontProfile);
		cellStyleProfile.setWrapText(true);
		// Crea Stile Headers Tabella
		Font fontHeaders = sheet.getWorkbook().createFont();
		fontHeaders.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontHeaders.setFontHeightInPoints((short) 9);
		fontHeaders.setFontName(HSSFFont.FONT_ARIAL);
		// Crea Stile Headers Tabella
		CellStyle cellStyleHeaderWithBorder = sheet.getWorkbook().createCellStyle();
		cellStyleHeaderWithBorder.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleHeaderWithBorder.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleHeaderWithBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleHeaderWithBorder.setFont(fontHeaders);
		cellStyleHeaderWithBorder.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyleHeaderWithBorder.setWrapText(true);
		cellStyleHeaderWithBorder.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyleHeaderWithBorder.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleHeaderWithBorder.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleHeaderWithBorder.setBorderBottom(CellStyle.BORDER_MEDIUM);
		// Crea Stile Headers Tabella
		CellStyle cellStyleHeaderTotaleWithBorder = sheet.getWorkbook().createCellStyle();
		cellStyleHeaderTotaleWithBorder.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleHeaderTotaleWithBorder.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleHeaderTotaleWithBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleHeaderTotaleWithBorder.setFont(fontHeaders);
		cellStyleHeaderTotaleWithBorder.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyleHeaderTotaleWithBorder.setWrapText(true);
		cellStyleHeaderTotaleWithBorder.setBorderTop(CellStyle.BORDER_THICK);
		cellStyleHeaderTotaleWithBorder.setBorderLeft(CellStyle.BORDER_THICK);
		cellStyleHeaderTotaleWithBorder.setBorderRight(CellStyle.BORDER_THICK);
		cellStyleHeaderTotaleWithBorder.setBorderBottom(CellStyle.BORDER_THICK);
		// Crea stile label Personale
		CellStyle cellStyleLabelPersonale = sheet.getWorkbook().createCellStyle();
		cellStyleLabelPersonale.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleLabelPersonale.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleLabelPersonale.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font fontLabelPersonale = sheet.getWorkbook().createFont();
		fontLabelPersonale.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontLabelPersonale.setFontHeightInPoints((short) 12);
		fontLabelPersonale.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleLabelPersonale.setFont(fontLabelPersonale);
		cellStyleLabelPersonale.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font fontCells = sheet.getWorkbook().createFont();
		fontCells.setFontHeightInPoints((short) 10);
		fontCells.setFontName(HSSFFont.FONT_ARIAL);
		// Crea Stile celle Tabella Bordered
		CellStyle cellStyleBorderedAlignLeft = sheet.getWorkbook().createCellStyle();
		cellStyleBorderedAlignLeft.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleBorderedAlignLeft.setFont(fontCells);
		cellStyleBorderedAlignLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleBorderedAlignLeft.setWrapText(true);
		cellStyleBorderedAlignLeft.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleBorderedAlignLeft.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleBorderedAlignLeft.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleBorderedAlignLeft.setBorderBottom(CellStyle.BORDER_THIN);
		// crea stile di cui totale operatori italic
		CellStyle cellStyleBorderedAlignRightItalic = sheet.getWorkbook().createCellStyle();
		cellStyleBorderedAlignRightItalic.setAlignment(CellStyle.ALIGN_RIGHT);
		Font fontCellsItalic = sheet.getWorkbook().createFont();
		fontCellsItalic.setFontHeightInPoints((short) 10);
		fontCellsItalic.setFontName(HSSFFont.FONT_ARIAL);
		fontCellsItalic.setItalic(true);
		cellStyleBorderedAlignRightItalic.setFont(fontCellsItalic);
		cellStyleBorderedAlignRightItalic.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleBorderedAlignRightItalic.setWrapText(true);
		cellStyleBorderedAlignRightItalic.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleBorderedAlignRightItalic.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleBorderedAlignRightItalic.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleBorderedAlignRightItalic.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile celle Tabella Bordered
		CellStyle cellStyleBordered = sheet.getWorkbook().createCellStyle();
		cellStyleBordered.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleBordered.setFont(fontCells);
		cellStyleBordered.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleBordered.setWrapText(true);
		cellStyleBordered.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleBordered.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleBordered.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleBordered.setBorderBottom(CellStyle.BORDER_THIN);
		Font fontCellsTotaleBold = sheet.getWorkbook().createFont();
		fontCellsTotaleBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontCellsTotaleBold.setFontHeightInPoints((short) 10);
		fontCellsTotaleBold.setFontName(HSSFFont.FONT_ARIAL);
		// Crea Stile celle Tabella Bordered
		CellStyle cellStyleBorderedBold = sheet.getWorkbook().createCellStyle();
		cellStyleBorderedBold.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleBorderedBold.setFont(fontCellsTotaleBold);
		cellStyleBorderedBold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleBorderedBold.setWrapText(true);
		cellStyleBorderedBold.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleBorderedBold.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleBorderedBold.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleBorderedBold.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile celle Tabella Bordered
		CellStyle cellStyleBorderedBoldAlignLeft = sheet.getWorkbook().createCellStyle();
		cellStyleBorderedBoldAlignLeft.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleBorderedBoldAlignLeft.setFont(fontCellsTotaleBold);
		cellStyleBorderedBoldAlignLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleBorderedBoldAlignLeft.setWrapText(true);
		cellStyleBorderedBoldAlignLeft.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleBorderedBoldAlignLeft.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleBorderedBoldAlignLeft.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleBorderedBoldAlignLeft.setBorderBottom(CellStyle.BORDER_THIN);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_F);

		// RIGA 0
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
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);
		// RIGA 1
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
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 7, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - "
				+ body.getDenominazioneEnte());
		cell.setCellStyle(cellStyle12by);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 2
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
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
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
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyleTitoloModel);
		row.setHeight((short) (row.getHeight() * 2));

		// PARTE 1: CONTEGGIO PERSONALE
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellValue((String) "Conteggio Personale");
		cell.setCellStyle(cellStyleLabel);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyleLabel);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Profilo Professionale");
		cell.setCellStyle(cellStyleProfile);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		row.setHeight((short) (row.getHeight() * 2));
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		for (ModelFProfiloProfessionale conteggioP : body.getRendModF().getConteggi().getConteggioPersonale()
				.getListaProfiloProfessionale()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) conteggioP.getDescProfiloProfessionale());
			cell.setCellStyle(cellStyleHeaderWithBorder);
		}
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "TOTALE");
		cell.setCellStyle(cellStyleHeaderTotaleWithBorder);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Personale dipendente dell'Ente");
		cell.setCellStyle(cellStyleLabelPersonale);
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
		row.createCell(++columnCount);
		int counterP = 0;
		for (ModelFPersonaleEnte personale : body.getRendModF().getConteggi().getConteggioPersonale()
				.getListaPersonaleEnte()) {
			if (personale.getCodPersonaleEnte().equals("01") || personale.getCodPersonaleEnte().equals("02")
					|| personale.getCodPersonaleEnte().equals("03") || personale.getCodPersonaleEnte().equals("04")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellWhiteBackground);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) personale.getDescPersonaleEnte());
				cell.setCellStyle(cellStyleBorderedAlignLeft);
				for (ModelFValori valore : personale.getListaValori()) {
					cell = row.createCell(++columnCount);
					if (valore.getValore() != null) {
						cell.setCellValue((int) valore.getValore().intValue());
						cell.setCellType(Cell.CELL_TYPE_STRING);
					}
					cell.setCellStyle(cellStyleBordered);
					cellStyleBordered.setDataFormat(format.getFormat(pattern));
				}
				// Totale Riga
				cell = row.createCell(++columnCount);
				cell.setCellValue(Integer.parseInt(body.getTotaleRP().get(counterP)));
				cell.setCellStyle(cellStyleBorderedBold);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellWhiteBackground);
			}
			counterP++;
		}
		// Totale Personale Dipendente
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Totale");
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		for (String valueTot : body.getTotaleCP()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) Double.valueOf(valueTot.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleTotCP().replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Personale non dipendente dell'Ente");
		cell.setCellStyle(cellStyleLabelPersonale);
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
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);
		for (ModelFPersonaleEnte personale : body.getRendModF().getConteggi().getConteggioPersonale()
				.getListaPersonaleEnte()) {
			if (personale.getCodPersonaleEnte().equals("05")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellWhiteBackground);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) personale.getDescPersonaleEnte());
				cell.setCellStyle(cellStyleBorderedAlignLeft);
				for (ModelFValori valore : personale.getListaValori()) {
					cell = row.createCell(++columnCount);
					if (valore.getValore() != null) {
						cell.setCellValue((int) valore.getValore().intValue());
						cell.setCellType(Cell.CELL_TYPE_STRING);
					}
					cell.setCellStyle(cellStyleBordered);
					cellStyleBordered.setDataFormat(format.getFormat(pattern));
				}
				// Totale Riga
				cell = row.createCell(++columnCount);
				cell.setCellValue(
						(double) Double.valueOf(body.getTotaleRP().get(4).replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleBorderedBold);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellWhiteBackground);
			}
		}

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		// Totale Operatore
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Totale operatori (dipendente + non dipendente) ");
		row.setHeight((short) (row.getHeight() * 2));
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		cell.getCellStyle().setWrapText(true);
		for (String valueTot : body.getTotaleCOpP()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) Double.valueOf(valueTot.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleTotCOpP().replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);
		for (ModelFPersonaleEnte personale : body.getRendModF().getConteggi().getConteggioPersonale()
				.getListaPersonaleEnte()) {
			if (personale.getCodPersonaleEnte().equals("06") || personale.getCodPersonaleEnte().equals("07")
					|| personale.getCodPersonaleEnte().equals("08")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellWhiteBackground);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) personale.getDescPersonaleEnte());
				cell.setCellStyle(cellStyleBorderedAlignRightItalic);
				for (ModelFValori valore : personale.getListaValori()) {
					cell = row.createCell(++columnCount);
					if (valore.getValore() != null) {
						cell.setCellValue((int) valore.getValore().intValue());
						cell.setCellType(Cell.CELL_TYPE_STRING);
					}
					cell.setCellStyle(cellStyleBordered);
					cellStyleBordered.setDataFormat(format.getFormat(pattern));
				}
				// Totale Riga
				cell = row.createCell(++columnCount);
				cell.setCellValue((double) Double
						.valueOf(body.getTotaleRP().get(counterP - 3).replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleBorderedBold);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellWhiteBackground);
				counterP++;
			}
		}
		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		cellRangeAddress = new CellRangeAddress(4, rowCount, 0, 12);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		// PARTE 2: CONTEGGIO MONTE ORE SETTIMANALE
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellValue((String) "Conteggio Monte ore settimanale");
		cell.setCellStyle(cellStyleLabel);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyleLabel);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Profilo Professionale");
		cell.setCellStyle(cellStyleProfile);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 9, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell = row.createCell(++columnCount);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		row.setHeight((short) (row.getHeight() * 2));
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		for (ModelFProfiloProfessionale conteggioP : body.getRendModF().getConteggi().getConteggioOre()
				.getListaProfiloProfessionale()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) conteggioP.getDescProfiloProfessionale());
			cell.setCellStyle(cellStyleHeaderWithBorder);
		}
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "TOTALE");
		cell.setCellStyle(cellStyleHeaderTotaleWithBorder);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Personale dipendente dell'Ente");
		cell.setCellStyle(cellStyleLabelPersonale);
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
		row.createCell(++columnCount);
		int counterPC = 0;
		for (ModelFPersonaleEnte personale : body.getRendModF().getConteggi().getConteggioOre()
				.getListaPersonaleEnte()) {
			if (personale.getCodPersonaleEnte().equals("09") || personale.getCodPersonaleEnte().equals("10")
					|| personale.getCodPersonaleEnte().equals("11") || personale.getCodPersonaleEnte().equals("12")
					|| personale.getCodPersonaleEnte().equals("13")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellWhiteBackground);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) personale.getDescPersonaleEnte());
				cell.setCellStyle(cellStyleBorderedAlignLeft);
				for (ModelFValori valore : personale.getListaValori()) {
					cell = row.createCell(++columnCount);
					if (valore.getValore() != null) {
						cell.setCellValue((int) valore.getValore().intValue());
						cell.setCellType(Cell.CELL_TYPE_STRING);
					}
					cell.setCellStyle(cellStyleBordered);
					cellStyleBordered.setDataFormat(format.getFormat(pattern));
				}
				// Totale Riga
				cell = row.createCell(++columnCount);
				cell.setCellValue((double) Double
						.valueOf(body.getTotaleRO().get(counterPC).replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleBorderedBold);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellWhiteBackground);

				counterPC++;
			}
		}
		// Totale Monte Ore Settimanale
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "TOTALE Monte ore settimanale per area di attivita' trasversali e utenza");
		row.setHeight((short) (row.getHeight() * 2));
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		cell.getCellStyle().setWrapText(true);
		for (String valueTot : body.getTotaleCO()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) Double.valueOf(valueTot.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleTotCO().replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);

		// Attivita Amministrative
		for (ModelFPersonaleEnte personale : body.getRendModF().getConteggi().getConteggioOre()
				.getListaPersonaleEnte()) {
			if (personale.getCodPersonaleEnte().equals("14")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellWhiteBackground);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) personale.getDescPersonaleEnte());
				cell.setCellStyle(cellStyleBorderedAlignLeft);
				for (ModelFValori valore : personale.getListaValori()) {
					cell = row.createCell(++columnCount);
					if (valore.getValore() != null) {
						cell.setCellValue((int) valore.getValore().intValue());
						cell.setCellType(Cell.CELL_TYPE_STRING);
					}
					cell.setCellStyle(cellStyleBordered);
					cellStyleBordered.setDataFormat(format.getFormat(pattern));
				}
			}
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleRO().get(5).replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		// Totale Monte Ore Settimanale Attivita' trasversali e amministrative
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue(
				(String) "TOTALE  Monte ore settimanale per area di attivita' trasversali e utenza e Monte ore settimanale per attivita' amministrative");
		row.setHeight((short) (row.getHeight() * 2));
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		cell.getCellStyle().setWrapText(true);
		for (String valueTot : body.getTotaleCOpO()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) Double.valueOf(valueTot.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleTotCOpO().replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Personale non dipendente dell'Ente");
		cell.setCellStyle(cellStyleLabelPersonale);
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
		row.createCell(++columnCount);
		for (ModelFPersonaleEnte personale : body.getRendModF().getConteggi().getConteggioOre()
				.getListaPersonaleEnte()) {
			if (personale.getCodPersonaleEnte().equals("15")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellWhiteBackground);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) personale.getDescPersonaleEnte());
				cell.setCellStyle(cellStyleBorderedAlignLeft);
				for (ModelFValori valore : personale.getListaValori()) {
					cell = row.createCell(++columnCount);
					if (valore.getValore() != null) {
						cell.setCellValue((int) valore.getValore().intValue());
						cell.setCellType(Cell.CELL_TYPE_STRING);
					}
					cell.setCellStyle(cellStyleBordered);
					cellStyleBordered.setDataFormat(format.getFormat(pattern));
				}
				// Totale Riga
				cell = row.createCell(++columnCount);
				cell.setCellValue(
						(double) Double.valueOf(body.getTotaleRO().get(6).replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleBorderedBold);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellWhiteBackground);
			}
		}

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		// Totale Monte Ore Settimanale Attivita' trasversali e amministrative
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Totale Monte ore settimanale (personale dipendente + personale non dipendente)");
		row.setHeight((short) (row.getHeight() * 2));
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		cell.getCellStyle().setWrapText(true);
		for (String valueTot : body.getTotaleCOpAttO()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) Double.valueOf(valueTot.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleTotCOpAttO().replaceAll("\\.", "").replace(",", ".")));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);

		// Riga separatrice
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		cellRangeAddress = new CellRangeAddress(23, rowCount, 0, 12);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Profilo Professionale");
		cell.setCellStyle(cellStyleProfile);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 9, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell = row.createCell(++columnCount);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		row.setHeight((short) (row.getHeight() * 2));
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		for (ModelFProfiloProfessionale conteggioP : body.getRendModF().getConteggi().getConteggioOre()
				.getListaProfiloProfessionale()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) conteggioP.getDescProfiloProfessionale());
			cell.setCellStyle(cellStyleHeaderWithBorder);
		}
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "TOTALE");
		cell.setCellStyle(cellStyleHeaderTotaleWithBorder);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellWhiteBackground);
		
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Monte ore settimanale pro capite del personale dipendente");
		row.setHeight((short) (row.getHeight() * 2));
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		cell.getCellStyle().setWrapText(true);
		for (String valueTot : body.getMonteOreSettInterno()) {
			cell = row.createCell(++columnCount);
			if(!valueTot.isEmpty()) {
				cell.setCellValue((double) Double.valueOf(valueTot));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			}else {
				cell.setCellValue("");
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
			
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleMonteOreSettInterno()));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "Monte ore settimanale pro capite del personale esternalizzato");
		row.setHeight((short) (row.getHeight() * 2));
		cell.setCellStyle(cellStyleBorderedBoldAlignLeft);
		cell.getCellStyle().setWrapText(true);
		for (String valueTot : body.getMonteOreSettEsterno()) {
			cell = row.createCell(++columnCount);
			if(!valueTot.isEmpty()) {
				cell.setCellValue((double) Double.valueOf(valueTot));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			}else {
				cell.setCellValue("");
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
			cell.setCellStyle(cellStyleBorderedBold);
		}
		// Totale Riga
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) Double.valueOf(body.getTotaleMonteOreSettEsterno()));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleBorderedBold);

		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellWhiteBackground);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 12, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		
		cellRangeAddress = new CellRangeAddress(42, rowCount, 0, 12);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		
		
		Util.autoSizeColumns(sheet.getWorkbook());
		// Set dimensione colonne
		sheet.setColumnWidth(0, 8 * 256);
		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 12 * 256);
		sheet.setColumnWidth(4, 12 * 256);
		sheet.setColumnWidth(5, 12 * 256);
		sheet.setColumnWidth(6, 12 * 256);
		sheet.setColumnWidth(7, 12 * 256);
		sheet.setColumnWidth(8, 12 * 256);
		sheet.setColumnWidth(9, 12 * 256);
		sheet.setColumnWidth(10, 12 * 256);
		sheet.setColumnWidth(11, 12 * 256);
		sheet.setColumnWidth(12, 8 * 256);
		// Set dimensione righe
		sheet.getRow(9).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(10).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(18).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(19).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(20).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(26).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(27).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(32).setHeight((short) (row.getHeight() * 0.8));
		sheet.getRow(37).setHeight((short) (row.getHeight() * 0.8));
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloF_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));
	}

	@Transactional
	public GenericResponseWarnErr checkModelloF(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());
		response.setObblMotivazione(false);

		GregTRendicontazioneEnte rendicontazioneAttuale = datiRendicontazioneService
				.getRendicontazione(idRendicontazione);
		GregTRendicontazioneEnte rendicontazionePassata = datiRendicontazioneService.getRendicontazionePassata(
				rendicontazioneAttuale.getGregTSchedeEntiGestori().getIdSchedaEnteGestore(),
				(rendicontazioneAttuale.getAnnoGestione() - 1));
		if (rendicontazionePassata != null) {
			ModelRendicontazioneModF datiModelloAttuale = getRendicontazioneModF(
					rendicontazioneAttuale.getIdRendicontazioneEnte());
			ModelRendicontazioneModF datiModelloPassata = getRendicontazioneModF(
					rendicontazionePassata.getIdRendicontazioneEnte());

			BigDecimal assistenteT = BigDecimal.ZERO;
			BigDecimal assistenteEsternoT = BigDecimal.ZERO;
			BigDecimal educatoreT = BigDecimal.ZERO;
			BigDecimal educatoreEsternoT = BigDecimal.ZERO;
			BigDecimal ossT = BigDecimal.ZERO;
			BigDecimal ossEsternoT = BigDecimal.ZERO;
			BigDecimal amministrativoT = BigDecimal.ZERO;
			BigDecimal amministrativoEsternoT = BigDecimal.ZERO;
			BigDecimal totaleT = BigDecimal.ZERO;
			BigDecimal totaleEsternoT = BigDecimal.ZERO;

			for (ModelFPersonaleEnte personale : datiModelloAttuale.getConteggi().getConteggioPersonale()
					.getListaPersonaleEnte()) {
				for (ModelFValori valori : personale.getListaValori()) {
					if (valori.getValore() != null) {
						if (!personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							totaleT = totaleT.add(valori.getValore());
						}
						if (personale.getCodPersonaleEnte().equals("05")) {
							totaleEsternoT = totaleEsternoT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							assistenteT = assistenteT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							educatoreT = educatoreT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							ossT = ossT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")
								&& personale.getCodPersonaleEnte().equals("05")) {
							assistenteEsternoT = assistenteEsternoT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							amministrativoT = amministrativoT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& personale.getCodPersonaleEnte().equals("05")) {
							amministrativoEsternoT = amministrativoEsternoT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& personale.getCodPersonaleEnte().equals("05")) {
							educatoreEsternoT = educatoreEsternoT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& personale.getCodPersonaleEnte().equals("05")) {
							ossEsternoT = ossEsternoT.add(valori.getValore());
						}
					}
				}
			}

			BigDecimal assistenteTP = BigDecimal.ZERO;
			BigDecimal assistenteEsternoTP = BigDecimal.ZERO;
			BigDecimal educatoreTP = BigDecimal.ZERO;
			BigDecimal educatoreEsternoTP = BigDecimal.ZERO;
			BigDecimal ossTP = BigDecimal.ZERO;
			BigDecimal ossEsternoTP = BigDecimal.ZERO;
			BigDecimal amministrativoTP = BigDecimal.ZERO;
			BigDecimal amministrativoEsternoTP = BigDecimal.ZERO;
			BigDecimal totaleTP = BigDecimal.ZERO;
			BigDecimal totaleEsternoTP = BigDecimal.ZERO;

			for (ModelFPersonaleEnte personale : datiModelloPassata.getConteggi().getConteggioPersonale()
					.getListaPersonaleEnte()) {
				for (ModelFValori valori : personale.getListaValori()) {
					if (valori.getValore() != null) {
						if (!personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							totaleTP = totaleTP.add(valori.getValore());
						}
						if (personale.getCodPersonaleEnte().equals("05")) {
							totaleEsternoTP = totaleEsternoTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							assistenteTP = assistenteTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							educatoreTP = educatoreTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							ossTP = ossTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& !personale.getCodPersonaleEnte().equals("05")
								&& !personale.getCodPersonaleEnte().equals("06")
								&& !personale.getCodPersonaleEnte().equals("07")
								&& !personale.getCodPersonaleEnte().equals("08")) {
							amministrativoTP = amministrativoTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& personale.getCodPersonaleEnte().equals("05")) {
							amministrativoEsternoTP = amministrativoEsternoTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")
								&& personale.getCodPersonaleEnte().equals("05")) {
							assistenteEsternoTP = assistenteEsternoTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& personale.getCodPersonaleEnte().equals("05")) {
							educatoreEsternoTP = educatoreEsternoTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& personale.getCodPersonaleEnte().equals("05")) {
							ossEsternoTP = ossEsternoTP.add(valori.getValore());
						}
					}
				}
			}

			BigDecimal assistenteTP25 = (assistenteTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal assistenteEsternoTP25 = (assistenteEsternoTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal educatoreTP25 = (educatoreTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal educatoreEsternoTP25 = (educatoreEsternoTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal ossTP25 = (ossTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal ossEsternoTP25 = (ossEsternoTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal amministrativoTP25 = (amministrativoTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal amministrativoEsternoTP25 = (amministrativoEsternoTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal totaleTP25 = (totaleTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal totaleEsternoTP25 = (totaleEsternoTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);

			if ((assistenteT.setScale(2)).compareTo(assistenteTP.subtract(assistenteTP25).setScale(2)) < 0
					|| (assistenteT.setScale(2)).compareTo(assistenteTP.add(assistenteTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("01").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale dipendente dell'Ente")
								.replace("DATOATTUALE", assistenteT.setScale(0).toString())
								.replace("DATOPASSATO", assistenteTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((assistenteEsternoT.setScale(2))
					.compareTo(assistenteEsternoTP.subtract(assistenteEsternoTP25).setScale(2)) < 0
					|| (assistenteEsternoT.setScale(2))
							.compareTo(assistenteEsternoTP.add(assistenteEsternoTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("01").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale non dipendente dell'Ente")
								.replace("DATOATTUALE", assistenteEsternoT.setScale(0).toString())
								.replace("DATOPASSATO", assistenteEsternoTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}

			if ((educatoreT.setScale(2)).compareTo(educatoreTP.subtract(educatoreTP25).setScale(2)) < 0
					|| (educatoreT.setScale(2)).compareTo(educatoreTP.add(educatoreTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("02").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale dipendente dell'Ente")
								.replace("DATOATTUALE", educatoreT.setScale(0).toString())
								.replace("DATOPASSATO", educatoreTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((educatoreEsternoT.setScale(2))
					.compareTo(educatoreEsternoTP.subtract(educatoreEsternoTP25).setScale(2)) < 0
					|| (educatoreEsternoT.setScale(2))
							.compareTo(educatoreEsternoTP.add(educatoreEsternoTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("02").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale non dipendente dell'Ente")
								.replace("DATOATTUALE", educatoreEsternoT.setScale(0).toString())
								.replace("DATOPASSATO", educatoreEsternoTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}

			if ((ossT.setScale(2)).compareTo(ossTP.subtract(ossTP25).setScale(2)) < 0
					|| (ossT.setScale(2)).compareTo(ossTP.add(ossTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("07").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale dipendente dell'Ente")
								.replace("DATOATTUALE", ossT.setScale(0).toString())
								.replace("DATOPASSATO", ossTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((ossEsternoT.setScale(2)).compareTo(ossEsternoTP.subtract(ossEsternoTP25).setScale(2)) < 0
					|| (ossEsternoT.setScale(2)).compareTo(ossEsternoTP.add(ossEsternoTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("07").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale non dipendente dell'Ente")
								.replace("DATOATTUALE", ossEsternoT.setScale(0).toString())
								.replace("DATOPASSATO", ossEsternoTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			
			if ((amministrativoT.setScale(2)).compareTo(amministrativoTP.subtract(amministrativoTP25).setScale(2)) < 0
					|| (amministrativoT.setScale(2)).compareTo(amministrativoTP.add(amministrativoTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("10").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale dipendente dell'Ente")
								.replace("DATOATTUALE", amministrativoT.setScale(0).toString())
								.replace("DATOPASSATO", amministrativoTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((amministrativoEsternoT.setScale(2)).compareTo(amministrativoEsternoTP.subtract(amministrativoEsternoTP25).setScale(2)) < 0
					|| (amministrativoEsternoT.setScale(2)).compareTo(amministrativoEsternoTP.add(amministrativoEsternoTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("10").getDescProfiloProfessionale())
								.replace("PERSONALE", "Personale non dipendente dell'Ente")
								.replace("DATOATTUALE", amministrativoEsternoT.setScale(0).toString())
								.replace("DATOPASSATO", amministrativoEsternoTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}

			if ((totaleT.setScale(2)).compareTo(totaleT.subtract(totaleTP25).setScale(2)) < 0
					|| (totaleT.setScale(2)).compareTo(totaleT.add(totaleTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F02).getTestoMessaggio()
								.replace("TOTALE", "Totale operatori dipendenti")
								.replace("DATOATTUALE", totaleT.setScale(0).toString())
								.replace("DATOPASSATO", totaleTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((totaleEsternoT.setScale(2)).compareTo(totaleEsternoTP.subtract(totaleEsternoTP25).setScale(2)) < 0
					|| (totaleEsternoT.setScale(2)).compareTo(totaleEsternoTP.add(totaleEsternoTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F02).getTestoMessaggio()
								.replace("TOTALE", "Totale operatori non dipendenti")
								.replace("DATOATTUALE", totaleEsternoT.setScale(0).toString())
								.replace("DATOPASSATO", totaleEsternoTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}

			BigDecimal assistenteOT = BigDecimal.ZERO;
			BigDecimal assistenteEsternoOT = BigDecimal.ZERO;
			BigDecimal assistentePersEsternoOT = BigDecimal.ZERO;
			BigDecimal educatoreOT = BigDecimal.ZERO;
			BigDecimal educatoreEsternoOT = BigDecimal.ZERO;
			BigDecimal educatorePersEsternoOT = BigDecimal.ZERO;
			BigDecimal ossOT = BigDecimal.ZERO;
			BigDecimal ossEsternoOT = BigDecimal.ZERO;
			BigDecimal ossPersEsternoOT = BigDecimal.ZERO;
			BigDecimal amministrativoOT = BigDecimal.ZERO;
			BigDecimal amministrativoEsternoOT = BigDecimal.ZERO;
			BigDecimal amministrativoPersEsternoOT = BigDecimal.ZERO;
			BigDecimal totaleOT = BigDecimal.ZERO;
			BigDecimal totaleEsternoOT = BigDecimal.ZERO;
			BigDecimal totalePersEsternoOT = BigDecimal.ZERO;

			for (ModelFPersonaleEnte personale : datiModelloAttuale.getConteggi().getConteggioOre()
					.getListaPersonaleEnte()) {
				for (ModelFValori valori : personale.getListaValori()) {
					if (valori.getValore() != null) {
						if (!personale.getCodPersonaleEnte().equals("15")) {
							totaleOT = totaleOT.add(valori.getValore());
						}
						if (personale.getCodPersonaleEnte().equals("15")) {
							totalePersEsternoOT = totalePersEsternoOT.add(valori.getValore());
						}
						totaleEsternoOT = totaleEsternoOT.add(valori.getValore());
						if (valori.getCodProfiloProfessionale().equals("01")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							assistenteOT = assistenteOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							educatoreOT = educatoreOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							ossOT = ossOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")
								&& personale.getCodPersonaleEnte().equals("15")) {
							assistenteEsternoOT = assistenteEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							amministrativoOT = amministrativoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& personale.getCodPersonaleEnte().equals("15")) {
							amministrativoEsternoOT = amministrativoEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& personale.getCodPersonaleEnte().equals("15")) {
							educatoreEsternoOT = educatoreEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& personale.getCodPersonaleEnte().equals("15")) {
							ossEsternoOT = ossEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")) {
							assistentePersEsternoOT = assistentePersEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")) {
							amministrativoPersEsternoOT = amministrativoPersEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")) {
							educatorePersEsternoOT = educatorePersEsternoOT.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")) {
							ossPersEsternoOT = ossPersEsternoOT.add(valori.getValore());
						}
					}
				}
			}

			BigDecimal assistenteOTP = BigDecimal.ZERO;
			BigDecimal assistenteEsternoOTP = BigDecimal.ZERO;
			BigDecimal assistentePersEsternoOTP = BigDecimal.ZERO;
			BigDecimal educatoreOTP = BigDecimal.ZERO;
			BigDecimal educatoreEsternoOTP = BigDecimal.ZERO;
			BigDecimal educatorePersEsternoOTP = BigDecimal.ZERO;
			BigDecimal ossOTP = BigDecimal.ZERO;
			BigDecimal ossEsternoOTP = BigDecimal.ZERO;
			BigDecimal ossPersEsternoOTP = BigDecimal.ZERO;
			BigDecimal amministrativoOTP = BigDecimal.ZERO;
			BigDecimal amministrativoEsternoOTP = BigDecimal.ZERO;
			BigDecimal amministrativoPersEsternoOTP = BigDecimal.ZERO;
			BigDecimal totaleOTP = BigDecimal.ZERO;
			BigDecimal totaleEsternoOTP = BigDecimal.ZERO;
			BigDecimal totalePersEsternoOTP = BigDecimal.ZERO;

			for (ModelFPersonaleEnte personale : datiModelloPassata.getConteggi().getConteggioOre()
					.getListaPersonaleEnte()) {
				for (ModelFValori valori : personale.getListaValori()) {
					if (valori.getValore() != null) {
						if (!personale.getCodPersonaleEnte().equals("15")) {
							totaleOTP = totaleOTP.add(valori.getValore());
						}
						if (personale.getCodPersonaleEnte().equals("15")) {
							totalePersEsternoOTP = totalePersEsternoOTP.add(valori.getValore());
						}
						totaleEsternoOTP = totaleEsternoOTP.add(valori.getValore());
						if (valori.getCodProfiloProfessionale().equals("01")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							assistenteOTP = assistenteOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							educatoreOTP = educatoreOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							ossOTP = ossOTP.add(valori.getValore());
						}
						
						if (valori.getCodProfiloProfessionale().equals("10")
								&& !personale.getCodPersonaleEnte().equals("15")) {
							amministrativoOTP = amministrativoOTP.add(valori.getValore());
						}
						
						if (valori.getCodProfiloProfessionale().equals("01")
								&& personale.getCodPersonaleEnte().equals("15")) {
							assistenteEsternoOTP = assistenteEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")
								&& personale.getCodPersonaleEnte().equals("15")) {
							amministrativoEsternoOTP = amministrativoEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")
								&& personale.getCodPersonaleEnte().equals("15")) {
							educatoreEsternoOTP = educatoreEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")
								&& personale.getCodPersonaleEnte().equals("15")) {
							ossEsternoOTP = ossEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("01")) {
							assistentePersEsternoOTP = assistentePersEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("10")) {
							amministrativoPersEsternoOTP = amministrativoPersEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("02")) {
							educatorePersEsternoOTP = educatorePersEsternoOTP.add(valori.getValore());
						}
						if (valori.getCodProfiloProfessionale().equals("07")) {
							ossPersEsternoOTP = ossPersEsternoOTP.add(valori.getValore());
						}
					}
				}
			}

			BigDecimal assistenteOTP25 = (assistenteOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal assistenteEsternoOTP25 = (assistenteEsternoOTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal assistentePersEsternoOTP25 = (assistentePersEsternoOTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal educatoreOTP25 = (educatoreOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal educatoreEsternoOTP25 = (educatoreEsternoOTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal educatorePersEsternoOTP25 = (educatorePersEsternoOTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal ossOTP25 = (ossOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal amministrativoOTP25 = (amministrativoOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal amministrativoPersEsternoOTP25 = (amministrativoPersEsternoOTP.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal ossEsternoOTP25 = (ossEsternoOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal ossPersEsternoOTP25 = (ossPersEsternoOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal amministrativoEsternoOTP25 = (amministrativoEsternoOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal totaleOTP25 = (totaleOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal totaleEsternoOTP25 = (totaleEsternoOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal totalePersEsternoOTP25 = (totalePersEsternoOTP.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);

			if ((assistenteOT.setScale(2)).compareTo(assistenteOTP.subtract(assistenteOTP25).setScale(2)) < 0
					|| (assistenteOT.setScale(2)).compareTo(assistenteOTP.add(assistenteOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("01").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente dell'ente")
								.replace("DATOATTUALE", assistenteOT.setScale(0).toString())
								.replace("DATOPASSATO", assistenteOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((assistenteEsternoOT.setScale(2))
					.compareTo(assistenteEsternoOTP.subtract(assistenteEsternoOTP25).setScale(2)) < 0
					|| (assistenteEsternoOT.setScale(2))
							.compareTo(assistenteEsternoOTP.add(assistenteEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("01").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale esternalizzato dell'ente")
								.replace("DATOATTUALE", assistenteEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", assistenteEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((assistentePersEsternoOT.setScale(2))
					.compareTo(assistentePersEsternoOTP.subtract(assistentePersEsternoOTP25).setScale(2)) < 0
					|| (assistentePersEsternoOT.setScale(2))
							.compareTo(assistentePersEsternoOTP.add(assistentePersEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("01").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente + non dipendente dell'ente")
								.replace("DATOATTUALE", assistentePersEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", assistentePersEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}

			if ((educatoreOT.setScale(2)).compareTo(educatoreOTP.subtract(educatoreOTP25).setScale(2)) < 0
					|| (educatoreOT.setScale(2)).compareTo(educatoreOTP.add(educatoreOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("02").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente dell'ente")
								.replace("DATOATTUALE", educatoreOT.setScale(0).toString())
								.replace("DATOPASSATO", educatoreOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((educatoreEsternoOT.setScale(2))
					.compareTo(educatoreEsternoOTP.subtract(educatoreEsternoOTP25).setScale(2)) < 0
					|| (educatoreEsternoOT.setScale(2))
							.compareTo(educatoreEsternoOTP.add(educatoreEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("02").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale esternalizzato dell'ente")
								.replace("DATOATTUALE", educatoreEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", educatoreEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((educatorePersEsternoOT.setScale(2))
					.compareTo(educatorePersEsternoOTP.subtract(educatorePersEsternoOTP25).setScale(2)) < 0
					|| (educatorePersEsternoOT.setScale(2))
							.compareTo(educatorePersEsternoOTP.add(educatorePersEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("02").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente + non dipendente dell'ente")
								.replace("DATOATTUALE", educatorePersEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", educatorePersEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}

			if ((ossOT.setScale(2)).compareTo(ossOTP.subtract(ossOTP25).setScale(2)) < 0
					|| (ossOT.setScale(2)).compareTo(ossOTP.add(ossOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("07").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente dell'ente")
								.replace("DATOATTUALE", ossOT.setScale(0).toString())
								.replace("DATOPASSATO", ossOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((ossEsternoOT.setScale(2)).compareTo(ossEsternoOTP.subtract(ossEsternoOTP25).setScale(2)) < 0
					|| (ossEsternoOT.setScale(2)).compareTo(ossEsternoOTP.add(ossEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("07").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale esternalizzato dell'ente")
								.replace("DATOATTUALE", ossEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", ossEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((ossPersEsternoOT.setScale(2)).compareTo(ossPersEsternoOTP.subtract(ossPersEsternoOTP25).setScale(2)) < 0
					|| (ossPersEsternoOT.setScale(2)).compareTo(ossPersEsternoOTP.add(ossPersEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("07").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente + non dipendente dell'ente")
								.replace("DATOATTUALE", ossPersEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", ossPersEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			
			if ((amministrativoOT.setScale(2)).compareTo(amministrativoOTP.subtract(amministrativoOTP25).setScale(2)) < 0
					|| (amministrativoOT.setScale(2)).compareTo(amministrativoOTP.add(amministrativoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("10").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente dell'ente")
								.replace("DATOATTUALE", amministrativoOT.setScale(0).toString())
								.replace("DATOPASSATO", amministrativoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((amministrativoEsternoOT.setScale(2)).compareTo(amministrativoEsternoOTP.subtract(amministrativoEsternoOTP25).setScale(2)) < 0
					|| (amministrativoEsternoOT.setScale(2)).compareTo(amministrativoEsternoOTP.add(amministrativoEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("10").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale esternalizzato dell'ente")
								.replace("DATOATTUALE", amministrativoEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", amministrativoEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((amministrativoPersEsternoOT.setScale(2)).compareTo(amministrativoPersEsternoOTP.subtract(amministrativoPersEsternoOTP25).setScale(2)) < 0
					|| (amministrativoPersEsternoOT.setScale(2)).compareTo(amministrativoPersEsternoOTP.add(amministrativoPersEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F01).getTestoMessaggio()
								.replace("PROFILO",
										modelloFDao.findProfiloProfessionaleByCod("10").getDescProfiloProfessionale())
								.replace("PERSONALE", "Monte ore settimanale del personale dipendente + non dipendente dell'ente")
								.replace("DATOATTUALE", amministrativoPersEsternoOT.setScale(0).toString())
								.replace("DATOPASSATO", amministrativoPersEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}


			if ((totaleOT.setScale(2)).compareTo(totaleOTP.subtract(totaleOTP25).setScale(2)) < 0
					|| (totaleOT.setScale(2)).compareTo(totaleOTP.add(totaleOTP25).setScale(2)) > 0) {
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F02)
						.getTestoMessaggio()
						.replace("TOTALE",
								"Totale monte ore settimanale del personale dipendente per area di attivita' trasversale utenza e attivita' amministrative")
						.replace("DATOATTUALE", totaleOT.setScale(0).toString())
						.replace("DATOPASSATO", totaleOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((totalePersEsternoOT.setScale(2)).compareTo(totalePersEsternoOTP.subtract(totalePersEsternoOTP25).setScale(2)) < 0
					|| (totalePersEsternoOT.setScale(2))
							.compareTo(totalePersEsternoOTP.add(totalePersEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F02)
						.getTestoMessaggio()
						.replace("TOTALE",
								"Totale monte ore settimanale personale esternalizzato")
						.replace("DATOATTUALE", totalePersEsternoOT.setScale(0).toString())
						.replace("DATOPASSATO", totalePersEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			if ((totaleEsternoOT.setScale(2)).compareTo(totaleEsternoOTP.subtract(totaleEsternoOTP25).setScale(2)) < 0
					|| (totaleEsternoOT.setScale(2))
							.compareTo(totaleEsternoOTP.add(totaleEsternoOTP25).setScale(2)) > 0) {
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_F02)
						.getTestoMessaggio()
						.replace("TOTALE",
								"Totale monte ore settimanale (personale dipendente + personale non dipendente)")
						.replace("DATOATTUALE", totaleEsternoOT.setScale(0).toString())
						.replace("DATOPASSATO", totaleEsternoOTP.setScale(0).toString()));
				response.setObblMotivazione(true);
			}
			

			if (response.getWarnings().size() > 0) {
				List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_F,
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

		ModelStatoMod stato = modelloFDao.getStatoModelloF(idRendicontazione);

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
	public GenericResponseWarnErr controlloModelloF(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);

		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_F);
		boolean facoltativo = false;
		boolean valorizzato = modelloFDao.getValorizzatoModelloF(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors()
					.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
							.replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		// Controlli Modello F
		ModelRendicontazioneModF rendModF = getRendicontazioneModF(rendicontazione.getIdRendicontazioneEnte());
		ModelFConteggioPersonale conteggioPersonale = rendModF.getConteggi().getConteggioPersonale();
		ModelFConteggioOre conteggioOre = rendModF.getConteggi().getConteggioOre();
		String errorMessageF = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_01)
				.getTestoMessaggio();
		if (!facoltativo) {
			// CONTROLLO 1, Controllo NucleiFamiliari minore di somma target utenza
			BigDecimal totaleAssistenzaSociale = BigDecimal.ZERO;
			BigDecimal totaleEducatore = BigDecimal.ZERO;
			BigDecimal totaleMediatoreCulturale = BigDecimal.ZERO;
			BigDecimal totalePsicologo = BigDecimal.ZERO;
			BigDecimal totalePedagogista = BigDecimal.ZERO;
			BigDecimal totaleSociologo = BigDecimal.ZERO;
			BigDecimal totaleOssAdbOta = BigDecimal.ZERO;
			BigDecimal totaleInfermiere = BigDecimal.ZERO;
			BigDecimal totaleAltro = BigDecimal.ZERO;
			BigDecimal totaleAmministrativo = BigDecimal.ZERO;

			for (ModelFPersonaleEnte personaleEnte : conteggioPersonale.getListaPersonaleEnte()) {
				if (personaleEnte.getCodPersonaleEnte().equals("01") || personaleEnte.getCodPersonaleEnte().equals("02")
						|| personaleEnte.getCodPersonaleEnte().equals("03")
						|| personaleEnte.getCodPersonaleEnte().equals("04")
						|| personaleEnte.getCodPersonaleEnte().equals("05")) {
					for (ModelFValori valore : personaleEnte.getListaValori()) {
						if (valore.getCodProfiloProfessionale().equals("01")) {
							totaleAssistenzaSociale = totaleAssistenzaSociale
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("02")) {
							totaleEducatore = totaleEducatore
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("03")) {
							totaleMediatoreCulturale = totaleMediatoreCulturale
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("04")) {
							totalePsicologo = totalePsicologo
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("05")) {
							totalePedagogista = totalePedagogista
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("06")) {
							totaleSociologo = totaleSociologo
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("07")) {
							totaleOssAdbOta = totaleOssAdbOta
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("08")) {
							totaleInfermiere = totaleInfermiere
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("09")) {
							totaleAltro = totaleAltro
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
						if (valore.getCodProfiloProfessionale().equals("10")) {
							totaleAmministrativo = totaleAmministrativo
									.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
						}
					}
				}
				// di cui
				else if (personaleEnte.getCodPersonaleEnte().equals("06")
						|| personaleEnte.getCodPersonaleEnte().equals("07")
						|| personaleEnte.getCodPersonaleEnte().equals("08")) {
					for (ModelFValori valore : personaleEnte.getListaValori()) {
						if (valore.getCodProfiloProfessionale().equals("01")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleAssistenzaSociale.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleAssistenzaSociale) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("01")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC6 = errorMessageF;
								response.getErrors().add(errMsgC6.replace("DICUI", personaleEnte.getDescPersonaleEnte())
										.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("02")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleEducatore.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleEducatore) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("02")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC7 = errorMessageF;
								response.getErrors().add(errMsgC7.replace("DICUI", personaleEnte.getDescPersonaleEnte())
										.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("03")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleMediatoreCulturale.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleMediatoreCulturale) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("03")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC8 = errorMessageF;
								response.getErrors().add(errMsgC8.replace("DICUI", personaleEnte.getDescPersonaleEnte())
										.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("04")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totalePsicologo.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totalePsicologo) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("04")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC9 = errorMessageF;
								response.getErrors().add(errMsgC9.replace("DICUI", personaleEnte.getDescPersonaleEnte())
										.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("05")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totalePedagogista.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totalePedagogista) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("05")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC10 = errorMessageF;
								response.getErrors()
										.add(errMsgC10.replace("DICUI", personaleEnte.getDescPersonaleEnte())
												.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("06")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleSociologo.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleSociologo) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("06")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC11 = errorMessageF;
								response.getErrors()
										.add(errMsgC11.replace("DICUI", personaleEnte.getDescPersonaleEnte())
												.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("07")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleOssAdbOta.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleOssAdbOta) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("07")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC12 = errorMessageF;
								response.getErrors()
										.add(errMsgC12.replace("DICUI", personaleEnte.getDescPersonaleEnte())
												.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("08")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleInfermiere.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleInfermiere) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("08")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC13 = errorMessageF;
								response.getErrors()
										.add(errMsgC13.replace("DICUI", personaleEnte.getDescPersonaleEnte())
												.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("09")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleAltro.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleAltro) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("09")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC13 = errorMessageF;
								response.getErrors()
										.add(errMsgC13.replace("DICUI", personaleEnte.getDescPersonaleEnte())
												.replace("PROFILO", descProfiloProfessionale));
							}
						}
						if (valore.getCodProfiloProfessionale().equals("10")) {
							String descProfiloProfessionale = "";
							if (valore.getValore() != null && !totaleAltro.equals(BigDecimal.ZERO)
									&& valore.getValore().compareTo(totaleAmministrativo) == 1) {
								for (ModelFProfiloProfessionale profilo : conteggioPersonale
										.getListaProfiloProfessionale()) {
									if (profilo.getCodProfiloProfessionale().equals("10")) {
										descProfiloProfessionale = profilo.getDescProfiloProfessionale();
									}
								}
								String errMsgC13 = errorMessageF;
								response.getErrors()
										.add(errMsgC13.replace("DICUI", personaleEnte.getDescPersonaleEnte())
												.replace("PROFILO", descProfiloProfessionale));
							}
						}
					}
				}
			}
		}
		List<MonteOre> monteOreDip = new ArrayList<MonteOre>();
		List<MonteOre> monteOreNonDip = new ArrayList<MonteOre>();

		List<GregDProfiloProfessionale> profili = modelloFDao.findAllProfiloProfessionale();

		for (GregDProfiloProfessionale profilo : profili) {
			MonteOre monteD = new MonteOre();
			monteD.setCodProfessionale(profilo.getCodProfiloProfessionale());
			monteOreDip.add(monteD);
			MonteOre monteND = new MonteOre();
			monteND.setCodProfessionale(profilo.getCodProfiloProfessionale());
			monteOreNonDip.add(monteND);
		}

		BigDecimal[] monteDip = new BigDecimal[profili.size()];
		BigDecimal[] monteNonDip = new BigDecimal[profili.size()];
		for (int i = 0; i < profili.size(); i++) {
			monteDip[i] = BigDecimal.ZERO;
			monteNonDip[i] = BigDecimal.ZERO;
		}
		for (int i = 0; i < conteggioPersonale.getListaPersonaleEnte().size(); i++) {
			for (int j = 0; j < conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().size(); j++) {
				if (conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("01")
						|| conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("02")
						|| conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("03")
						|| conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("04")) {
					if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().get(j)
							.getCodProfiloProfessionale().equals(monteOreDip.get(j).getCodProfessionale())) {
						if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().get(j)
								.getValore() != null) {
							monteDip[j] = monteDip[j].add(conteggioPersonale.getListaPersonaleEnte().get(i)
									.getListaValori().get(j).getValore());
						}
					}
				} else if (conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("05")) {
					if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().get(j)
							.getCodProfiloProfessionale().equals(monteOreDip.get(j).getCodProfessionale())) {
						if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().get(j)
								.getValore() != null) {
							monteNonDip[j] = monteNonDip[j].add(conteggioPersonale.getListaPersonaleEnte().get(i)
									.getListaValori().get(j).getValore());
						}
					}
				}

			}
		}

		for (int i = 0; i < monteOreDip.size(); i++) {
			monteOreDip.get(i).setNumeroPersonale(monteDip[i]);
			monteOreNonDip.get(i).setNumeroPersonale(monteNonDip[i]);
		}

		for (int i = 0; i < profili.size(); i++) {
			monteDip[i] = BigDecimal.ZERO;
			monteNonDip[i] = BigDecimal.ZERO;
		}
		for (int i = 0; i < conteggioOre.getListaPersonaleEnte().size(); i++) {
			for (int j = 0; j < conteggioOre.getListaPersonaleEnte().get(i).getListaValori().size(); j++) {
				if (conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("09")
						|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("10")
						|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("11")
						|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("12")
						|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("13")
						|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("14")) {
					if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j).getCodProfiloProfessionale()
							.equals(monteOreDip.get(j).getCodProfessionale())) {
						if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j).getValore() != null) {
							monteDip[j] = monteDip[j].add(
									conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j).getValore());
						}
					}
				} else if (conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("15")) {
					if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j).getCodProfiloProfessionale()
							.equals(monteOreDip.get(j).getCodProfessionale())) {
						if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j).getValore() != null) {
							monteNonDip[j] = monteNonDip[j].add(
									conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j).getValore());
						}
					}
				}

			}
		}

		for (int i = 0; i < monteOreDip.size(); i++) {
			monteOreDip.get(i).setNumeroOre(monteDip[i]);
			monteOreNonDip.get(i).setNumeroOre(monteNonDip[i]);
		}
		boolean persDip = false;
		boolean persNonDip = false;
		boolean persDip40 = false;
		boolean persNonDip40 = false;
		for (MonteOre monte : monteOreDip) {
			if (!facoltativo) {
				if (((monte.getNumeroOre().equals(BigDecimal.ZERO)
						&& monte.getNumeroPersonale().compareTo(BigDecimal.ZERO) > 0)
						|| (monte.getNumeroOre().compareTo(BigDecimal.ZERO) > 0
								&& monte.getNumeroPersonale().equals(BigDecimal.ZERO)))
						&& !persDip) {
					response.getErrors().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_03)
							.getTestoMessaggio().replace("PERSONALE", "Personale dipendente"));
					persDip = true;
				}
			}
			BigDecimal mon = !monte.getNumeroPersonale().equals(BigDecimal.ZERO)
					? monte.getNumeroOre().divide(monte.getNumeroPersonale(), 2, RoundingMode.UP)
					: BigDecimal.ZERO;
			if (mon.compareTo(new BigDecimal(40)) > 0 && !persDip40) {
				persDip40 = true;
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_02)
						.getTestoMessaggio().replace("PERSONALE", "Personale dipendente"));
			}
		}

		for (MonteOre monte : monteOreNonDip) {
			if (!facoltativo) {
				if (((monte.getNumeroOre().equals(BigDecimal.ZERO)
						&& monte.getNumeroPersonale().compareTo(BigDecimal.ZERO) > 0)
						|| (monte.getNumeroOre().compareTo(BigDecimal.ZERO) > 0
								&& monte.getNumeroPersonale().equals(BigDecimal.ZERO)))
						&& !persNonDip) {
					response.getErrors().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_03)
							.getTestoMessaggio().replace("PERSONALE", "Personale esternalizzato"));
					persNonDip = true;
				}
			}
			BigDecimal mon = !monte.getNumeroPersonale().equals(BigDecimal.ZERO)
					? monte.getNumeroOre().divide(monte.getNumeroPersonale(), 2, RoundingMode.UP)
					: BigDecimal.ZERO;
			if (mon.compareTo(new BigDecimal(40)) > 0 && !persNonDip40) {
				persNonDip40 = true;
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_02)
						.getTestoMessaggio().replace("PERSONALE", "Personale esternalizzato"));
			}
		}

		return response;

	}
}
