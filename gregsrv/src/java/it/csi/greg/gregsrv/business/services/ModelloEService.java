/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

import it.csi.greg.gregsrv.business.dao.impl.ModelloEDao;
import it.csi.greg.gregsrv.business.entity.GregDAttivitaSocioAssist;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModE;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloEInput;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelAttivitaSocioAssist;
import it.csi.greg.gregsrv.dto.ModelCampiMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelComuniAttivitaValoriModE;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelRendModAPart3;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneAttivitaModE;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModE;
import it.csi.greg.gregsrv.dto.ModelRisultatiModelloE;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.SaveModelloEInput;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.dto.VociModelloA;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloEService")
public class ModelloEService {

	@Autowired
	protected ModelloEDao modelloEDao;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected MacroaggregatiService macroaggregatiService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected MailService mailService;
	
	public List<ModelAttivitaSocioAssist> getAttivitaSocioAssist() {
		List<GregDAttivitaSocioAssist> attivitaSocioAssist = modelloEDao.getAttivitaSocioAssist();
		
		List<ModelAttivitaSocioAssist> listaAttivitaSocioAssist = new ArrayList<ModelAttivitaSocioAssist>();
		for(GregDAttivitaSocioAssist attivita : attivitaSocioAssist) {
			listaAttivitaSocioAssist.add(new ModelAttivitaSocioAssist(attivita));
		}
		return listaAttivitaSocioAssist;
	}
	
	public GregDAttivitaSocioAssist findAttivitaSocioAssistByCod(String codAttivita) {
		return modelloEDao.findAttivitaSocioAssistByCod(codAttivita);
	}


	public ModelRendicontazioneModE getRendicontazioneModE(Integer idScheda) {
		List<Object> rendicontazioneModE = modelloEDao.getRendicontazioneModE(idScheda);
		List<GregDAttivitaSocioAssist> attivitaSocioAssist = modelloEDao.getAttivitaSocioAssist();
		List<ModelRisultatiModelloE> listaRisultati = new ArrayList<ModelRisultatiModelloE>();
		GregTRendicontazioneEnte rendicontazioneEnte = datiRendicontazioneService.getRendicontazione(idScheda);
		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService.getSchedaEnte(rendicontazioneEnte.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		List<GregRSchedeEntiGestoriComuni> comuniAssociati = datiEnteService.findAllComuniAssociati(schedaEnte.getIdSchedaEnteGestore(), rendicontazioneEnte.getAnnoGestione());
		
		if(rendicontazioneModE != null && !rendicontazioneModE.isEmpty()) {
			Iterator<Object> itr = rendicontazioneModE.iterator();
			while(itr.hasNext()){
				Object[] obj = (Object[]) itr.next();
				listaRisultati.add(new ModelRisultatiModelloE(obj));
			}
			
			ModelRendicontazioneModE rendModE = new ModelRendicontazioneModE();
			rendModE.setIdRendicontazioneEnte(rendicontazioneEnte.getIdRendicontazioneEnte());
			rendModE.setIdSchedaEnteGestore(schedaEnte.getIdSchedaEnteGestore());
//			rendModE.setDenominazioneEnte(schedaEnte.getDenominazione());
			rendModE.setAnnoGestione(rendicontazioneEnte.getAnnoGestione());
			rendModE.setComuniAttivitaValori(new ArrayList<ModelComuniAttivitaValoriModE>());
			
			for(GregRSchedeEntiGestoriComuni comuneAss : comuniAssociati) {
				ModelRisultatiModelloE comAssToRender = listaRisultati.stream().filter(risultato -> risultato.getCodIstatComune().equals(comuneAss.getGregDComuni().getCodIstatComune())).findFirst().orElse(null);
				
				if(comAssToRender != null) {
					ModelComuniAttivitaValoriModE rendElement = new ModelComuniAttivitaValoriModE();
					rendElement.setIdComune(comAssToRender.getIdComune());
					rendElement.setAttivita(new ArrayList<ModelRendicontazioneAttivitaModE>());
					
					for(GregDAttivitaSocioAssist attivita : attivitaSocioAssist) {
						ModelRisultatiModelloE attivitaComune = listaRisultati.stream().filter(risultato -> risultato.getCodIstatComune().equals(comuneAss.getGregDComuni().getCodIstatComune()) && 
								risultato.getCodAttivitaSocioAssist().equals(attivita.getCodAttivitaSocioAssist())).findFirst().orElse(null);
						if(attivitaComune != null) {
							ModelRendicontazioneAttivitaModE attivitaNew = new ModelRendicontazioneAttivitaModE();
							attivitaNew.setCodAttivitaSocioAssist(attivita.getCodAttivitaSocioAssist());
							attivitaNew.setDescAttivitaSocioAssist(attivita.getDescAttivitaSocioAssist());
							attivitaNew.setIdAttivitaSocioAssist(attivita.getIdAttivitaSocioAssist());
							attivitaNew.setValore(attivitaComune.getValore());
							rendElement.getAttivita().add(attivitaNew);
						}
						else {
							ModelRendicontazioneAttivitaModE attivitaNew = new ModelRendicontazioneAttivitaModE();
							attivitaNew.setCodAttivitaSocioAssist(attivita.getCodAttivitaSocioAssist());
							attivitaNew.setDescAttivitaSocioAssist(attivita.getDescAttivitaSocioAssist());
							attivitaNew.setIdAttivitaSocioAssist(attivita.getIdAttivitaSocioAssist());
							attivitaNew.setValore(null);
							rendElement.getAttivita().add(attivitaNew);
						}
					}
					rendElement.setCodIstatComune(comAssToRender.getCodIstatComune());
					rendElement.setDescComune(comAssToRender.getDesComune());
					rendModE.getComuniAttivitaValori().add(rendElement);
				}
			}			
			return rendModE;
		}
		else {
			ModelRendicontazioneModE rendModE = new ModelRendicontazioneModE();
			rendModE.setIdRendicontazioneEnte(rendicontazioneEnte.getIdRendicontazioneEnte());
			rendModE.setIdSchedaEnteGestore(schedaEnte.getIdSchedaEnteGestore());
//			rendModE.setDenominazioneEnte(schedaEnte.getDenominazione());
			rendModE.setAnnoGestione(rendicontazioneEnte.getAnnoGestione());
			rendModE.setComuniAttivitaValori(new ArrayList<ModelComuniAttivitaValoriModE>());
			return rendModE;
		}		
	}

	public SaveModelloOutput saveModelloE(SaveModelloEInput body, UserInfo userInfo) throws Exception {
		SaveModelloOutput out = new SaveModelloOutput();
		// Verifico la presenza della rendicontazione
		GregTRendicontazioneEnte rendToUpdate = datiRendicontazioneService.getRendicontazione(body.getIdRendicontazioneEnte());

		if (rendToUpdate == null) {
			throw new IntegritaException(Util.composeMessage(
					listeService.getMessaggio(SharedConstants.ERROR_ANNO_CONTABILE).getTestoMessaggio(), ""));
		} else {
			Timestamp dataModifica = new Timestamp(new Date().getTime());
			String newNotaEnte = "";

			String statoOld = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();

			rendToUpdate = datiRendicontazioneService.modificaStatoRendicontazione(rendToUpdate, userInfo,
					SharedConstants.OPERAZIONE_SALVA,body.getProfilo());

			String statoNew = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			if (!statoOld.equals(statoNew)) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
						.getTestoMessaggio().replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
						.replace("STATONEW", "'" + statoNew + "'");
			}

			// Recupero eventuale ultima cronologia inserita
			GregTCronologia lastCrono = datiRendicontazioneService.findLastCronologiaEnte(rendToUpdate.getIdRendicontazioneEnte());
			
//			if ((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE))
//					|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {
			if(body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
				if (!Checker.isValorizzato(body.getCronologia().getNotaEnte())) {
					String msgUpdateDati = listeService
							.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI).getTestoMessaggio()
							.replace("OPERAZIONE", "SALVA");
					newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: newNotaEnte;
				} else {
					newNotaEnte = body.getCronologia().getNotaEnte();
				}

			} else {
				newNotaEnte = body.getCronologia().getNotaEnte();
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(body.getCronologia().getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendToUpdate);
				cronologia.setGregDStatoRendicontazione(rendToUpdate.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Mod. E");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(body.getCronologia().getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}
			
			// Recupero la lista delle rendicontazioni da salvare o aggiornare
			List<ModelComuniAttivitaValoriModE> listaToSaveOrUpdate = body.getListaRendicontazione();
			
			//Recupero l'elenco dei comuni da FE
			List<Integer> elencoComuniFE = new ArrayList<Integer>();
			for (ModelComuniAttivitaValoriModE element : listaToSaveOrUpdate) {
				elencoComuniFE.add(element.getIdComune());
			}
			
			//Recupero l'elenco dei comuni da BE
			List<Integer> elencoComuniBE = modelloEDao.getAllComuniRendicontazioniModE(body.getIdRendicontazioneEnte());
			
			
			if(listaToSaveOrUpdate != null && listaToSaveOrUpdate.size() != 0) {
				for(ModelComuniAttivitaValoriModE rendModE : listaToSaveOrUpdate) {
					for(ModelRendicontazioneAttivitaModE attivita : rendModE.getAttivita()) {

						GregRRendicontazioneModE rendicontazioneToUpdate = 
								modelloEDao.findByIdAttIdComune(rendToUpdate.getIdRendicontazioneEnte(), attivita.getIdAttivitaSocioAssist(), rendModE.getIdComune());

						if(rendicontazioneToUpdate != null) {
							if(attivita.getValore() == null) {
								//Cancello il record
								modelloEDao.deleteRendicontazioneModelloE(rendicontazioneToUpdate.getIdRendicontazioneModE());
							}
							else {
								// Effettuo l'aggiornamento del record
								rendicontazioneToUpdate.setValore(attivita.getValore());
								rendicontazioneToUpdate.setDataModifica(dataModifica);
								modelloEDao.updateRendicontazioneModelloE(rendicontazioneToUpdate);
							}
						}
						else {
							if(attivita.getValore() != null) {
								// Effettuo l'inserimento a DB
								GregRRendicontazioneModE newRendicontazione = new GregRRendicontazioneModE();
								newRendicontazione.setGregTRendicontazioneEnte(rendToUpdate);
								newRendicontazione.setGregDAttivitaSocioAssist(findAttivitaSocioAssistByCod(attivita.getCodAttivitaSocioAssist()));
								newRendicontazione.setGregDComuni(datiEnteService.findComuneByIdNotDeleted(rendModE.getIdComune()));
								newRendicontazione.setValore(attivita.getValore());
								newRendicontazione.setDataInizioValidita(dataModifica);
								newRendicontazione.setUtenteOperazione(userInfo.getCodFisc());
								newRendicontazione.setDataCreazione(dataModifica);
								newRendicontazione.setDataModifica(dataModifica);
								modelloEDao.insertRendicontazioneModelloE(newRendicontazione);
							}
						}
					}
				}
				
				// Verifico eventuali rendicontazioni rimosse e le cancello
				for(Integer comuneBE : elencoComuniBE) {
					if(!elencoComuniFE.contains(comuneBE)) {
						List<Integer> listRendComune = modelloEDao.findRendicontazioniModEByComune(body.getIdRendicontazioneEnte(), comuneBE);
						for(Integer idRendComune : listRendComune) {
							modelloEDao.deleteRendicontazioneModelloE(idRendComune);							
						}
					}
					
				}
			}
			else {
				// Cancello eventuali rendicontazioni associate all'ente
				List<Integer> listRendicontazioni = modelloEDao.getAllRendicontazioniModE(body.getIdRendicontazioneEnte());
				if(listRendicontazioni != null && listRendicontazioni.size() != 0) {
					for(Integer idRendModE : listRendicontazioni) {
						modelloEDao.deleteRendicontazioneModelloE(idRendModE);
					}
					
				}
			}
		}
		
		if(body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1] ) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdEnte());
			//Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
			if (trovataemail) {
			EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),ultimoContatto.getDenominazione(),ultimoContatto.getResponsabileEnte(), SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
			out.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
			out.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		return out;
	}
	
	@Transactional
	public String esportaModelloE(EsportaModelloEInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloE");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		DataFormat format =workbook.createDataFormat();
		Row row = sheet.createRow(rowCount);

		//crea stili arial 12 bold italic
		CellStyle cellStyleb12I = sheet.getWorkbook().createCellStyle();
		cellStyleb12I.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb12I = sheet.getWorkbook().createFont();
		fontb12I.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb12I.setFontHeightInPoints((short) 12);
		fontb12I.setFontName(HSSFFont.FONT_ARIAL);
		fontb12I.setItalic(true);
		cellStyleb12I.setFont(fontb12I);
		cellStyleb12I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//crea stili arial 12 bold
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
		//crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b); 
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//crea stili arial titolo
		CellStyle cellStyleTitoloModelMissioni = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloModelMissioni.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTitoloModelMissioni.setFont(font12b); 
		cellStyleTitoloModelMissioni.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitoloModelMissioni.setWrapText(true);		
		// Crea Stile Headers Tabella
		Font fontHeaders = sheet.getWorkbook().createFont();
		fontHeaders.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontHeaders.setFontHeightInPoints((short) 8);
		fontHeaders.setFontName(HSSFFont.FONT_ARIAL);		
		CellStyle cellStyleHeaders = sheet.getWorkbook().createCellStyle();
		cellStyleHeaders.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleHeaders.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleHeaders.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleHeaders.setFont(fontHeaders);
		cellStyleHeaders.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleHeaders.setWrapText(true);
		cellStyleHeaders.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleHeaders.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleHeaders.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleHeaders.setBorderBottom(CellStyle.BORDER_THIN);		
		// Crea Stile Headers Tabella
		CellStyle cellStyleHeaderWithBorder = sheet.getWorkbook().createCellStyle();
		cellStyleHeaderWithBorder.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleHeaderWithBorder.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleHeaderWithBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleHeaderWithBorder.setFont(fontHeaders);
		cellStyleHeaderWithBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleHeaderWithBorder.setWrapText(true);
		cellStyleHeaderWithBorder.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleHeaderWithBorder.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleHeaderWithBorder.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleHeaderWithBorder.setBorderBottom(CellStyle.BORDER_THIN);		
		// Crea Stile Colonna Valore White
		CellStyle cellStyleValueWhite = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhite.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueWhite.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueWhite.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderBottom(CellStyle.BORDER_THIN);		
		// Crea Stile Colonna Valore CodIstat
		CellStyle cellStyleValueCodIstat = sheet.getWorkbook().createCellStyle();
		cellStyleValueCodIstat.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleValueCodIstat.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueCodIstat.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueCodIstat.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleValueCodIstat.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueCodIstat.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore Comune
		CellStyle cellStyleValueComune = sheet.getWorkbook().createCellStyle();
		cellStyleValueComune.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleValueComune.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueComune.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueComune.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleValueComune.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleValueComune.setBorderBottom(CellStyle.BORDER_THIN);		
		// Font Totali
		Font fontTotali = sheet.getWorkbook().createFont();
		fontTotali.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTotali.setFontHeightInPoints((short) 10);
		fontTotali.setFontName(HSSFFont.FONT_ARIAL);
		short indexColor = getColorExcel(workbook, "#E7E6E6").getIndex();
		// Crea Stile Totale Riga 
		CellStyle cellStyleTotaleRiga = sheet.getWorkbook().createCellStyle();
		cellStyleTotaleRiga.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotaleRiga.setFont(fontTotali);
		cellStyleTotaleRiga.setFillForegroundColor(indexColor);
		cellStyleTotaleRiga.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaleRiga.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaleRiga.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotaleRiga.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTotaleRiga.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleRiga.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Desc Totale 			
		CellStyle cellStyleDescTotaleColonna = sheet.getWorkbook().createCellStyle();
		cellStyleDescTotaleColonna.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleDescTotaleColonna.setFont(fontTotali);
		cellStyleDescTotaleColonna.setFillForegroundColor(indexColor);
		cellStyleDescTotaleColonna.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleDescTotaleColonna.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescTotaleColonna.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyleDescTotaleColonna.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleDescTotaleColonna.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleDescTotaleColonna.setBorderBottom(CellStyle.BORDER_MEDIUM);
		// Crea Stile Totale Colonna 
		CellStyle cellStyleTotaleColonna = sheet.getWorkbook().createCellStyle();
		cellStyleTotaleColonna.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotaleColonna.setFont(fontTotali);
		cellStyleTotaleColonna.setFillForegroundColor(indexColor);
		cellStyleTotaleColonna.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaleColonna.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaleColonna.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleColonna.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleColonna.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleColonna.setBorderBottom(CellStyle.BORDER_MEDIUM);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelMsgInformativo popoverModelE = listeService.findMsgInformativi(SharedConstants.SECTION_MODE_TOOLTIP).get(0);
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(), SharedConstants.MODELLO_E);
		List<String> columnsExcel = new ArrayList<String>();
		columnsExcel.add("Codice ISTAT (obbligatorio)");
		columnsExcel.add("Comuni");
		for(ModelAttivitaSocioAssist attivita : getAttivitaSocioAssist()) {
			columnsExcel.add(attivita.getTooltipDescAttivitaSocioAssist().toUpperCase());
		}
		columnsExcel.add("TOTALE SPESE DIRETTE");

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
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount-8, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);		
		// RIGA 1
		columnCount = 0;		
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress =new CellRangeAddress(rowCount, rowCount, columnCount-2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "ENTE GESTORE DENOMINAZIONE E NUMERO :");
		cell.setCellStyle(cellStyle12b);		
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress =new CellRangeAddress(rowCount, rowCount, columnCount-5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - " + body.getDenominazioneEnte());
		cell.setCellStyle(cellStyle12by);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
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
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress =new CellRangeAddress(rowCount, rowCount, columnCount-8, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,sheet.getWorkbook());
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
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount-11, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab() + "\n (" + popoverModelE.getTestoMsgInformativo() + ")");
		cell.setCellStyle(cellStyleTitoloModelMissioni);
		row.setHeight((short) (row.getHeight()*2));
		
		// RIGHE INTESTAZIONI TABELLA
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		row.setHeight((short) (row.getHeight()*4));
		int counter = 0;
		for(String header: columnsExcel) {
			cell = row.createCell(columnCount);
			cell.setCellValue((String) header);
			cell.setCellStyle(cellStyleHeaders);
			if(counter == 1) {
				cell.setCellStyle(cellStyleHeaderWithBorder);
			}
			columnCount++;
			counter++;
		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount-12, columnCount-1);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,sheet.getWorkbook());
		
		for(ModelComuniAttivitaValoriModE rend : body.getRendModE()) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellValue((String) rend.getCodIstatComune());
			cell.setCellStyle(cellStyleValueCodIstat);
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) rend.getDescComune());
			cell.setCellStyle(cellStyleValueComune);
			for(ModelRendicontazioneAttivitaModE attivita : rend.getAttivita()) {
				cell = row.createCell(++columnCount);
				if(attivita.getValore() != null) {
					cell.setCellValue((double) Double.valueOf(Util.convertBigDecimalToString(attivita.getValore()).replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				}
				cell.setCellStyle(cellStyleValueWhite);
				cellStyleValueWhite.setDataFormat(format.getFormat(pattern));
			}
			//Totali riga
			cell = row.createCell(++columnCount);
			if(body.getTotaleRiga().get(rowCount-5) != null) {
				cell.setCellValue((double) Double.valueOf(body.getTotaleRiga().get(rowCount-5).replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			}
			cell.setCellStyle(cellStyleTotaleRiga);
			cellStyleTotaleRiga.setDataFormat(format.getFormat(pattern));
		}
		
		// Totali Colonna
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellValue((String) "TOTALE");
		cell.setCellStyle(cellStyleDescTotaleColonna);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyleDescTotaleColonna);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount-1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		for(String value : body.getTotaleColonna()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) Double.valueOf(value.replaceAll("\\.", "").replace(",", ".")));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleTotaleColonna);
			cellStyleTotaleColonna.setDataFormat(format.getFormat(pattern));
		}

		Util.autoSizeColumns(sheet.getWorkbook());
		// Set dimensione colonne
		sheet.setColumnWidth(0, 15*256);
		sheet.setColumnWidth(1, 30*256);		
		sheet.setColumnWidth(2, 25*256);		
		sheet.setColumnWidth(3, 15*256);
		sheet.setColumnWidth(4, 15*256);
		sheet.setColumnWidth(5, 15*256);
		sheet.setColumnWidth(6, 15*256);
		sheet.setColumnWidth(7, 15*256);
		sheet.setColumnWidth(8, 15*256);
		sheet.setColumnWidth(9, 15*256);
		sheet.setColumnWidth(10, 15*256);
		sheet.setColumnWidth(11, 15*256);
		sheet.setColumnWidth(12, 15*256);
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloE_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream!=null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));
	}
	
	private HSSFColor getColorExcel(HSSFWorkbook workbook, String colore) {
		HSSFPalette palette = workbook.getCustomPalette();
		Integer[] rgb = Converter.hex2Rgb(colore);
		HSSFColor fColor = palette.findColor(rgb[0].byteValue(), rgb[1].byteValue(),rgb[2].byteValue());

		if(fColor != null) 	return fColor;

		HSSFColor sColor = palette.findSimilarColor(rgb[0].byteValue(), rgb[1].byteValue(),rgb[2].byteValue());
		if(sColor.getIndex() == HSSFColor.CORNFLOWER_BLUE.index) sColor = palette.getColor(sColor.getIndex()+1);
		palette.setColorAtIndex(sColor.getIndex(), rgb[0].byteValue(), rgb[1].byteValue(),rgb[2].byteValue());
		return palette.getColor(sColor.getIndex());
	}

	public String getStatoModelloEnte(Integer idRendicontazione, String codTranche, String codStatoRendicontazione) {
		ModelStatoMod stato = modelloEDao.getStatoModelloE(idRendicontazione);
		
		if(!stato.isValorizzato()) {
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
			if(codTranche.equals(SharedConstants.TRANCHEI)) {
				if(codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
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
			} else if(codTranche.equals(SharedConstants.TRANCHEII)) {
				if(codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
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
	public GenericResponseWarnErr controlloModelloE(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_E);
		boolean facoltativo = false;
		boolean valorizzato = modelloEDao.getValorizzatoModelloE(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors()
					.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
							.replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		return response;

	}
}
