/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ModelloA2Dao;
import it.csi.greg.gregsrv.business.entity.GregDCausaleComuneEnteModA2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneComuneEnteModA2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneEnteComuneModA2;
import it.csi.greg.gregsrv.business.entity.GregTCausaleEnteComuneModA2;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloA2Input;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.GetTrasferimentiA2Output;
import it.csi.greg.gregsrv.dto.ModelCausali;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelRendModAPart3;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelTrasferimentoA2;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.SaveModelloA2Input;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.dto.VociModelloA;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloA2Service")
public class ModelloA2Service {

	@Autowired
	protected ModelloA2Dao modelloA2Dao;
	@Autowired
	protected ModelloAService modelloAService;
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

	public List<ModelCausali> getCausali(Integer idEnte) {
		List<ModelCausali> listaCausali = new ArrayList<ModelCausali>();
		List<GregTCausaleEnteComuneModA2> causali = modelloA2Dao.getCausali(idEnte);
		for (GregTCausaleEnteComuneModA2 c : causali) {
			listaCausali.add(new ModelCausali(c));
		}
		return listaCausali;
	}

	public List<ModelCausali> getCausaliStatiche() {
		List<ModelCausali> listaCausali = new ArrayList<ModelCausali>();
		List<GregDCausaleComuneEnteModA2> causali = modelloA2Dao.getCausaliStatiche();
		for (GregDCausaleComuneEnteModA2 c : causali) {
			listaCausali.add(new ModelCausali(c));
		}
		return listaCausali;
	}

	public GetTrasferimentiA2Output getTrasferimentiModA2(Integer idEnte) {
		List<ModelTrasferimentoA2> trasfEnte = new ArrayList<ModelTrasferimentoA2>();
		List<GregRRendicontazioneEnteComuneModA2> trasferimentiEnteComune = modelloA2Dao
				.getTrasferimentiEnteComune(idEnte);
		for (GregRRendicontazioneEnteComuneModA2 t : trasferimentiEnteComune) {
			trasfEnte.add(new ModelTrasferimentoA2(t));
		}
		List<ModelTrasferimentoA2> trasfComuni = new ArrayList<ModelTrasferimentoA2>();
		List<GregRRendicontazioneComuneEnteModA2> trasferimentiComuneEnte = modelloA2Dao
				.getTrasferimentiComuneEnte(idEnte);
		for (GregRRendicontazioneComuneEnteModA2 t : trasferimentiComuneEnte) {
			trasfComuni.add(new ModelTrasferimentoA2(t));
		}

		return new GetTrasferimentiA2Output(trasfEnte, trasfComuni);
	}

	@Transactional
	public SaveModelloOutput saveModelloA2(SaveModelloA2Input body, UserInfo userInfo) throws Exception {
		SaveModelloOutput out = new SaveModelloOutput();
		out.setWarnings(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService
				.getRendicontazione(body.getIdRendicontazioneEnte());

		// SALVO LE CAUSALI
		if (body.getCausali().size() > 0) {
			// Elimino le causali con id diverso da quelli restituiti da frontend
			modelloA2Dao.eliminaCausali(getListaIdCausali(body.getCausali()), body.getIdRendicontazioneEnte());

			List<GregTCausaleEnteComuneModA2> causaliDb = modelloA2Dao
					.getAllCausaliByIdEnte(rendicontazione.getIdRendicontazioneEnte());

			for (ModelCausali causale : body.getCausali()) {
				GregTCausaleEnteComuneModA2 aggiorna = null;
				if (causale.getId() == null) {
					if (causaliDb.size() > 0) {
						for (GregTCausaleEnteComuneModA2 cDb : causaliDb) {
							if (cDb.getDescCausaleEnteComuneModA2().equalsIgnoreCase(causale.getDescrizione())
									&& cDb.getDataCancellazione() != null) {
								aggiorna = cDb;
							}
						}

						if (aggiorna != null) {
							// Aggiorno causale precedentemente inserita e cancellata
							aggiorna.setDataCancellazione(null);
							aggiorna.setDataModifica(new Timestamp(System.currentTimeMillis()));
							aggiorna.setUtenteOperazione(userInfo.getCodFisc());
							modelloA2Dao.saveCausale(aggiorna);
						} else {
							// Inserisco nuova causale
							GregTCausaleEnteComuneModA2 nuovaCausale = new GregTCausaleEnteComuneModA2();
							nuovaCausale.setDescCausaleEnteComuneModA2(causale.getDescrizione());
							nuovaCausale.setGregTRendicontazioneEnte(rendicontazione);
							nuovaCausale.setUtenteOperazione(userInfo.getCodFisc());
							nuovaCausale.setDataCreazione(new Timestamp(System.currentTimeMillis()));
							nuovaCausale.setDataModifica(new Timestamp(System.currentTimeMillis()));
							modelloA2Dao.saveCausale(nuovaCausale);
						}
					} else {
						// Inserisco nuova causale
						GregTCausaleEnteComuneModA2 nuovaCausale = new GregTCausaleEnteComuneModA2();
						nuovaCausale.setDescCausaleEnteComuneModA2(causale.getDescrizione());
						nuovaCausale.setGregTRendicontazioneEnte(rendicontazione);
						nuovaCausale.setUtenteOperazione(userInfo.getCodFisc());
						nuovaCausale.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						nuovaCausale.setDataModifica(new Timestamp(System.currentTimeMillis()));
						modelloA2Dao.saveCausale(nuovaCausale);
					}
				}
			}

			// SALVO I TRASFERIMENTI DA ENTE A COMUNE

			// Elimino i trasferimenti con id diverso da quelli restituiti da frontend
			modelloA2Dao.eliminaTrasferimentiEnteComune(
					getListaIdTrasferimentiEnteComune(body.getTrasferimentiEnteComune()),
					body.getIdRendicontazioneEnte());

			List<GregRRendicontazioneEnteComuneModA2> trasfDb = modelloA2Dao
					.getAllTrasferimentiEnteByIdEnte(body.getIdRendicontazioneEnte());

			for (ModelTrasferimentoA2 trasf : body.getTrasferimentiEnteComune()) {
				GregRRendicontazioneEnteComuneModA2 aggiorna = null;
				if (trasf.getId() == null) {
					if (trasfDb.size() > 0) {
						for (GregRRendicontazioneEnteComuneModA2 tDb : trasfDb) {
							if (tDb.getGregDComuni().getCodIstatComune()
									.equalsIgnoreCase(trasf.getComune().getCodiceIstat())
									&& tDb.getGregTCausaleEnteComuneModA2().getDescCausaleEnteComuneModA2()
											.equalsIgnoreCase(trasf.getCausale().getDescrizione())
									&& tDb.getValore().equals(trasf.getImporto())
									&& tDb.getDataCancellazione() != null) {
								aggiorna = tDb;
							}
						}

						if (aggiorna != null) {
							// Aggiorno trasferimento precedentemente inserito e cancellato
							aggiorna.setDataCancellazione(null);
							aggiorna.setDataModifica(new Timestamp(System.currentTimeMillis()));
							aggiorna.setUtenteOperazione(userInfo.getCodFisc());
							modelloA2Dao.saveTrasferimentoEnteComune(aggiorna);
						} else {
							// Inserisco nuovo trasferimento
							GregRRendicontazioneEnteComuneModA2 nuovoTrasf = new GregRRendicontazioneEnteComuneModA2();
							nuovoTrasf.setValore(trasf.getImporto());
							nuovoTrasf.setGregTRendicontazioneEnte(rendicontazione);
							nuovoTrasf.setGregTCausaleEnteComuneModA2(modelloA2Dao.getGregTCausaleEnteComuneModA2(
									trasf.getCausale(), rendicontazione.getIdRendicontazioneEnte()));
							nuovoTrasf.setGregDComuni(modelloA2Dao.getComuneById(trasf.getComune().getIdComune()));
							nuovoTrasf.setUtenteOperazione(userInfo.getCodFisc());
							nuovoTrasf.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
							nuovoTrasf.setDataCreazione(new Timestamp(System.currentTimeMillis()));
							nuovoTrasf.setDataModifica(new Timestamp(System.currentTimeMillis()));
							modelloA2Dao.saveTrasferimentoEnteComune(nuovoTrasf);
						}
					} else {
						// Inserisco nuovo trasferimento
						GregRRendicontazioneEnteComuneModA2 nuovoTrasf = new GregRRendicontazioneEnteComuneModA2();
						nuovoTrasf.setValore(trasf.getImporto());
						nuovoTrasf.setGregTRendicontazioneEnte(rendicontazione);
						nuovoTrasf.setGregTCausaleEnteComuneModA2(modelloA2Dao.getGregTCausaleEnteComuneModA2(
								trasf.getCausale(), rendicontazione.getIdRendicontazioneEnte()));
						nuovoTrasf.setGregDComuni(modelloA2Dao.getComuneById(trasf.getComune().getIdComune()));
						nuovoTrasf.setUtenteOperazione(userInfo.getCodFisc());
						nuovoTrasf.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
						nuovoTrasf.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						nuovoTrasf.setDataModifica(new Timestamp(System.currentTimeMillis()));
						modelloA2Dao.saveTrasferimentoEnteComune(nuovoTrasf);
					}
				}
			}

		} else {

			// Elimino le causali con id diverso da quelli restituiti da frontend
			modelloA2Dao.eliminaCausali(getListaIdCausali(body.getCausali()), body.getIdRendicontazioneEnte());

			// Elimino i trasferimenti con id diverso da quelli restituiti da frontend
			modelloA2Dao.eliminaTrasferimentiEnteComune(
					getListaIdTrasferimentiEnteComune(body.getTrasferimentiEnteComune()),
					body.getIdRendicontazioneEnte());

			// Aggiungo un warning
			// out.getWarnings().add(listeService.getMessaggio(SharedConstants.WARNING_MOD_A2_01).getTestoMessaggio());
		}

		// SALVO I TRASFERIMENTI DA COMUNE A ENTE

		// Elimino i trasferimenti con id diverso da quelli restituiti da frontend
		modelloA2Dao.eliminaTrasferimentiComuneEnte(
				getListaIdTrasferimentiEnteComune(body.getTrasferimentiComuneEnte()), body.getIdRendicontazioneEnte());

		List<GregRRendicontazioneComuneEnteModA2> trasfDb = modelloA2Dao
				.getAllTrasferimentiComuneByIdEnte(body.getIdRendicontazioneEnte());

		for (ModelTrasferimentoA2 trasf : body.getTrasferimentiComuneEnte()) {
			GregRRendicontazioneComuneEnteModA2 aggiorna = null;
			if (trasf.getId() == null) {
				if (trasfDb.size() > 0) {
					for (GregRRendicontazioneComuneEnteModA2 tDb : trasfDb) {
						if (tDb.getGregDComuni().getCodIstatComune()
								.equalsIgnoreCase(trasf.getComune().getCodiceIstat())
								&& tDb.getGregDCausaleComuneEnteModA2().getIdCausaleComuneEnteModA2()
										.equals(trasf.getCausale().getId())
								&& tDb.getValore().equals(trasf.getImporto()) && tDb.getDataCancellazione() != null) {
							aggiorna = tDb;
						}
					}

					if (aggiorna != null) {
						// Aggiorno trasferimento precedentemente inserito e cancellato
						aggiorna.setDataCancellazione(null);
						aggiorna.setDataModifica(new Timestamp(System.currentTimeMillis()));
						aggiorna.setUtenteOperazione(userInfo.getCodFisc());
						modelloA2Dao.saveTrasferimentoComuneEnte(aggiorna);
					} else {
						// Inserisco nuovo trasferimento
						GregRRendicontazioneComuneEnteModA2 nuovoTrasf = new GregRRendicontazioneComuneEnteModA2();
						nuovoTrasf.setValore(trasf.getImporto());
						nuovoTrasf.setGregTRendicontazioneEnte(rendicontazione);
						nuovoTrasf.setGregDCausaleComuneEnteModA2(
								modelloA2Dao.getGregDCausaleComuneEnteModA2(trasf.getCausale().getId()));
						nuovoTrasf.setGregDComuni(modelloA2Dao.getComuneById(trasf.getComune().getIdComune()));
						nuovoTrasf.setUtenteOperazione(userInfo.getCodFisc());
						nuovoTrasf.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
						nuovoTrasf.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						nuovoTrasf.setDataModifica(new Timestamp(System.currentTimeMillis()));
						modelloA2Dao.saveTrasferimentoComuneEnte(nuovoTrasf);
					}
				} else {
					// Inserisco nuovo trasferimento
					GregRRendicontazioneComuneEnteModA2 nuovoTrasf = new GregRRendicontazioneComuneEnteModA2();
					nuovoTrasf.setValore(trasf.getImporto());
					nuovoTrasf.setGregTRendicontazioneEnte(rendicontazione);
					nuovoTrasf.setGregDCausaleComuneEnteModA2(
							modelloA2Dao.getGregDCausaleComuneEnteModA2(trasf.getCausale().getId()));
					nuovoTrasf.setGregDComuni(modelloA2Dao.getComuneById(trasf.getComune().getIdComune()));
					nuovoTrasf.setUtenteOperazione(userInfo.getCodFisc());
					nuovoTrasf.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
					nuovoTrasf.setDataCreazione(new Timestamp(System.currentTimeMillis()));
					nuovoTrasf.setDataModifica(new Timestamp(System.currentTimeMillis()));
					modelloA2Dao.saveTrasferimentoComuneEnte(nuovoTrasf);
				}
			}
		}

		String newNotaEnte = "";

		String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
		// Controllo e aggiorno lo stato della rendicontazione
		rendicontazione = datiRendicontazioneService.modificaStatoRendicontazione(rendicontazione, userInfo,
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

//		if((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE)) 
//				|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {
		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
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
			cronologia.setModello("Mod. A2");
			cronologia.setUtenteOperazione(userInfo.getCodFisc());
			cronologia.setNotaInterna(body.getCronologia().getNotaInterna());
			cronologia.setNotaPerEnte(newNotaEnte);
			cronologia.setDataOra(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataModifica(new Timestamp(System.currentTimeMillis()));
			datiRendicontazioneService.insertCronologia(cronologia);
		}

		// Controllo il totale trasferimenti comune->ente == totale Mod A cella G9
		// GregRRendicontazioneModAPart1 rendicontazioneModA =
		// datiRendicontazioneDao.getRendicontazioneModAByIdRendicontazione(rendicontazione.getIdRendicontazioneEnte());
		// BigDecimal totale = calcolaTotale(body.getTrasferimentiComuneEnte());
		// if(rendicontazioneModA == null ||
		// !rendicontazioneModA.getValore().equals(totale)) {
		// out.getWarnings().add("Il totale dei trasferimenti da comune a Ente Getstore
		// deve essere identico al valore indicato nel Modello A alla voce (contributi e
		// trasferimenti dai Comuni all'ente gestore per causali diverse dalla quota
		// procapite).");
		// }

		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdEnte());
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
		out.setMessaggio(listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio()
				.replace("oggettosalvo", "Modello A2"));
		out.setEsito("OK");
		return out;
	}

	@SuppressWarnings("unused")
	private BigDecimal calcolaTotale(List<ModelTrasferimentoA2> trasferimentiComuneEnte) {
		BigDecimal tot = new BigDecimal(0);
		for (ModelTrasferimentoA2 m : trasferimentiComuneEnte) {
			tot = tot.add(m.getImporto());
		}
		return tot;
	}

	private List<Integer> getListaIdCausali(List<ModelCausali> causali) {
		List<Integer> listaId = new ArrayList<Integer>();
		for (ModelCausali c : causali) {
			if (c.getId() != null)
				listaId.add(c.getId());
		}
		return listaId;
	}

	private List<Integer> getListaIdTrasferimentiEnteComune(List<ModelTrasferimentoA2> trasferimentiEnteComune) {
		List<Integer> listaId = new ArrayList<Integer>();
		for (ModelTrasferimentoA2 t : trasferimentiEnteComune) {
			if (t.getId() != null)
				listaId.add(t.getId());
		}
		return listaId;
	}

	public String esportaModelloA2(EsportaModelloA2Input body) throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloA2");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		HSSFDataFormat format = workbook.createDataFormat();

		// font arial 8 bold
		Font font8b = sheet.getWorkbook().createFont();
		font8b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font8b.setFontHeightInPoints((short) 8);
		font8b.setFontName(HSSFFont.FONT_ARIAL);

		// font arial 10
		Font font10 = sheet.getWorkbook().createFont();
		font10.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10.setFontHeightInPoints((short) 10);
		font10.setFontName(HSSFFont.FONT_ARIAL);

		// font arial 10 italic
		Font font10c = sheet.getWorkbook().createFont();
		font10c.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10c.setFontHeightInPoints((short) 10);
		font10c.setFontName(HSSFFont.FONT_ARIAL);
		font10c.setItalic(true);

		// font arial 10 bold
		Font font10b = sheet.getWorkbook().createFont();
		font10b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font10b.setFontHeightInPoints((short) 10);
		font10b.setFontName(HSSFFont.FONT_ARIAL);

		// font arial 10 bold italic
		Font font10bc = sheet.getWorkbook().createFont();
		font10bc.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font10bc.setFontHeightInPoints((short) 10);
		font10bc.setFontName(HSSFFont.FONT_ARIAL);
		font10bc.setItalic(true);

		// font arial 10 red
		Font font10r = sheet.getWorkbook().createFont();
		font10r.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10r.setFontHeightInPoints((short) 10);
		font10r.setFontName(HSSFFont.FONT_ARIAL);
		font10r.setColor(HSSFFont.COLOR_RED);

		// font arial 12 bold
		Font font12b = sheet.getWorkbook().createFont();
		font12b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font12b.setFontHeightInPoints((short) 12);
		font12b.setFontName(HSSFFont.FONT_ARIAL);

		// font arial 12 bold italic
		Font fontb12I = sheet.getWorkbook().createFont();
		fontb12I.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb12I.setFontHeightInPoints((short) 12);
		fontb12I.setFontName(HSSFFont.FONT_ARIAL);
		fontb12I.setItalic(true);

		// crea stili arial 12 bold italic
		CellStyle cellStyleb12I = sheet.getWorkbook().createCellStyle();
		cellStyleb12I.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleb12I.setFont(fontb12I);
		cellStyleb12I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 12 bold grigio
		CellStyle cellStyle12b = sheet.getWorkbook().createCellStyle();
		cellStyle12b.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12b.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle12b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12b.setFont(font12b);
		cellStyle12b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo
		CellStyle cellStyletitolo = sheet.getWorkbook().createCellStyle();
		cellStyletitolo.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo.setFont(font12b);
		cellStyletitolo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo2
		CellStyle cellStyletitolo2 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyletitolo2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo2.setFont(font12b);
		cellStyletitolo2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo3
		CellStyle cellStyletitolo3 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo3.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyletitolo3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo3.setFont(font10b);
		cellStyletitolo3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo4
		CellStyle cellStyletitolo4 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo4.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyletitolo4.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo4.setFont(font10b);
		cellStyletitolo4.setBorderTop(CellStyle.BORDER_THIN);
		cellStyletitolo4.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyletitolo4.setBorderRight(CellStyle.BORDER_THIN);
		cellStyletitolo4.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyletitolo4.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo5
		CellStyle cellStyletitolo5 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo5.setFont(font10b);
		cellStyletitolo5.setBorderTop(CellStyle.BORDER_THIN);
		cellStyletitolo5.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyletitolo5.setBorderRight(CellStyle.BORDER_THIN);
		cellStyletitolo5.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyletitolo5.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 8 corsivo
		CellStyle cellStyle10c = sheet.getWorkbook().createCellStyle();
		cellStyle10c.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10c.setFont(font10c);
		cellStyle10c.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10c.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10c.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyle10c.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10c.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10c.setDataFormat(format.getFormat(pattern));

		// crea stili arial
		CellStyle cellStyle10 = sheet.getWorkbook().createCellStyle();
		cellStyle10.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle10.setFont(font10);
		cellStyle10.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// totale
		CellStyle cellStyleTot = sheet.getWorkbook().createCellStyle();
		cellStyleTot.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTot.setFont(font10b);
		cellStyleTot.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTot.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTot.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTot.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTot.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// totale green
		CellStyle cellStyleTotVal = sheet.getWorkbook().createCellStyle();
		cellStyleTotVal.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotVal.setFont(font10bc);
		cellStyleTotVal.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotVal.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTotVal.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTotVal.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTotVal.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleTotVal.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotVal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotVal.setDataFormat(format.getFormat(pattern));

		// crea stili arial
		CellStyle cellStyle10r = sheet.getWorkbook().createCellStyle();
		cellStyle10r.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle10r.setFont(font10r);
		cellStyle10r.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10r.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10r.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10r.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10r.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stile bordo destro
		CellStyle cellStylebd = sheet.getWorkbook().createCellStyle();
		cellStylebd.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStylebd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stile bordo sinistro
		CellStyle cellStylebs = sheet.getWorkbook().createCellStyle();
		cellStylebs.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStylebs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_A2);

		// Row 1
		Row row = sheet.createRow(rowCount);
		Cell cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);

		columnCount = 0;

		// Row 2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
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

		columnCount = 0;

		// Row 3
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row 4
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyletitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row trasf ente comune
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("TRASFERIMENTO DA ENTE GESTORE A COMUNE");
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row space
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row title tab 1
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("Totali trasferimenti ai Comuni");
		cell.setCellStyle(cellStyletitolo3);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		columnCount = 0;

		// Row subtitle tab 1
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Cod. Istat");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Comune");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Causale");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Importo");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		// Row tab 1
		BigDecimal totEnte = BigDecimal.ZERO;
		for (ModelTrasferimentoA2 element : body.getTrasferimentiEnteComune()) {
			totEnte = totEnte.add(element.getImporto());

			columnCount = 0;

			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellStyle(cellStylebs);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getComune().getCodiceIstat());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getComune().getDesComune());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getCausale().getDescrizione());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getImporto().doubleValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyle10c);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylebd);
		}
		columnCount = 0;

		// row total
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("TOTALE");
		cell.setCellStyle(cellStyleTot);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellValue(totEnte.doubleValue());
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleTotVal);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		columnCount = 0;

		// Row space
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row space
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// Row tab 1 filtered
		boolean first = true;
		for (ModelTrasferimentoA2 element : body.getTrasferimentiEnteComuneFiltered()) {
			columnCount = 0;

			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellStyle(cellStylebs);

			cell = row.createCell(++columnCount);
			if (first) {
				cell.setCellValue("Totali per causale");
				cell.setCellStyle(cellStyletitolo5);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStyle10);
				first = false;
			} else {
				row.createCell(++columnCount);
			}

			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getCausale().getDescrizione());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getImporto().doubleValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyle10c);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylebd);
		}

		columnCount = 0;

		// row total tab 1 filtered
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue("TOTALE");
		cell.setCellStyle(cellStyleTot);
		cell = row.createCell(++columnCount);
		cell.setCellValue(totEnte.doubleValue());
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleTotVal);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		columnCount = 0;

		// Row space bottom
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row trasf comune ente
		sheet.createRow(++rowCount);
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("TRASFERIMENTO DA COMUNE A ENTE GESTORE");
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row space
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row title tab 2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("Totali trasferimenti all'ente");
		cell.setCellStyle(cellStyletitolo3);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		columnCount = 0;

		// Row subtitle tab 2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Cod. Istat");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Comune");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Causale");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Importo");
		cell.setCellStyle(cellStyletitolo4);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		// Row tab 2
		BigDecimal totComune = BigDecimal.ZERO;
		for (ModelTrasferimentoA2 element : body.getTrasferimentiComuneEnte()) {
			totComune = totComune.add(element.getImporto());

			columnCount = 0;

			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellStyle(cellStylebs);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getComune().getCodiceIstat());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getComune().getDesComune());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getCausale().getDescrizione());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getImporto().doubleValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyle10c);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylebd);
		}
		columnCount = 0;

		// row total tab 2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("TOTALE");
		cell.setCellStyle(cellStyleTot);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellValue(totComune.doubleValue());
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleTotVal);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		columnCount = 0;

		// Row space
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row space
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// Row tab 2 filtered
		first = true;
		for (ModelTrasferimentoA2 element : body.getTrasferimentiComuneEnteFiltered()) {
			columnCount = 0;

			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellStyle(cellStylebs);

			cell = row.createCell(++columnCount);
			if (first) {
				cell.setCellValue("Totali per causale");
				cell.setCellStyle(cellStyletitolo5);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStyle10);
				first = false;
			} else {
				row.createCell(++columnCount);
			}

			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getCausale().getDescrizione());
			cell.setCellStyle(cellStyle10);
			cell = row.createCell(++columnCount);
			cell.setCellValue(element.getImporto().doubleValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyle10c);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylebd);
		}

		columnCount = 0;

		// row total tab 2 filtered
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylebs);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellValue("TOTALE");
		cell.setCellStyle(cellStyleTot);
		cell = row.createCell(++columnCount);
		cell.setCellValue(totComune.doubleValue());
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyleTotVal);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylebd);

		columnCount = 0;

		// Row space bottom
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// END

		Util.autoSizeColumnsAndMerged(sheet.getWorkbook());
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloA2_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));

	}

	public String getStatoModelloEnte(Integer idRendicontazione, String codTranche, String codStatoRendicontazione) {
		ModelStatoMod stato = modelloA2Dao.getStatoModelloA2(idRendicontazione);

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
	public GenericResponseWarnErr controlloModelloA2(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);

		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_A2);

		boolean facoltativo = false;
		boolean valorizzato = modelloA2Dao.getValorizzatoModelloA2(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors()
					.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
							.replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		List<ModelValoriModA> cella07 = new ArrayList<ModelValoriModA>();
		BigDecimal valorecella07 = null;

		cella07 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
				SharedConstants.MODELLO_A_07);
		if (cella07.size() != 0) {
			valorecella07 = cella07.get(0).getValore();
		}

		List<GregRRendicontazioneEnteComuneModA2> entecomune = modelloA2Dao
				.getPerInvioModelloA2EnteComune(rendicontazione.getIdRendicontazioneEnte());
		List<GregRRendicontazioneComuneEnteModA2> comuneente = modelloA2Dao
				.getPerInvioModelloA2ComuneEnte(rendicontazione.getIdRendicontazioneEnte());
		// if (!comunecapoluogo) {
		// chiama funzione modello a2
		if (entecomune.size() == 0) {
			response.getWarnings()
					.add(listeService.getMessaggio(SharedConstants.WARNING_MOD_A2_01).getTestoMessaggio());
		}
		if (!facoltativo) {
			// Controllo il totale trasferimenti comune->ente == totale Mod A cella G9
			if (comuneente.size() != 0 && (valorecella07 == null || valorecella07.compareTo(BigDecimal.ZERO)==0)) {
//			ERROR_INVIO_MODELLIA_NO06
				response.getErrors()
						.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
			} else {
				if (comuneente.size() == 0 && (valorecella07 != null && !valorecella07.equals(BigDecimal.ZERO))) {
					response.getErrors().add(
							listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
				} else if (comuneente.size() != 0 && valorecella07!=null && !valorecella07.equals(BigDecimal.ZERO)) {
					// calcolo il totale
					BigDecimal totale = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
					for (GregRRendicontazioneComuneEnteModA2 val : comuneente) {
						totale = totale.add(val.getValore());
					}
					if (!valorecella07.setScale(2, RoundingMode.HALF_UP).equals(totale)) {
						response.getErrors().add(
								listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
					}
				}
			}
		}

		return response;

	}
}
