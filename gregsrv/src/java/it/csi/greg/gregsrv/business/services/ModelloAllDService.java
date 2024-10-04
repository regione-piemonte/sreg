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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.ConfiguratoreUtenzeFnpsDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloAllDDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloAllontanamentoZeroDao;
import it.csi.greg.gregsrv.business.entity.GregDAree;
import it.csi.greg.gregsrv.business.entity.GregDMacroattivita;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRFnpsUtenzaCalcolo;
import it.csi.greg.gregsrv.business.entity.GregRPrestMinistUtenzeMinist;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneGiustificazioneFnps;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModuloFnps;
import it.csi.greg.gregsrv.business.entity.GregRSpeseFnps;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniMinisteriali;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.Aree;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModAllDInput;
import it.csi.greg.gregsrv.dto.ModelB1Macroaggregati;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg1;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg2;
import it.csi.greg.gregsrv.dto.ModelB1Voci;
import it.csi.greg.gregsrv.dto.ModelB1VociPrestReg2;
import it.csi.greg.gregsrv.dto.ModelFondi;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneAllD;
import it.csi.greg.gregsrv.dto.ModelRendAllDforRowB1;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModAllD;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelUtentiFnps;
import it.csi.greg.gregsrv.dto.ModelUtenzaAllD;
import it.csi.greg.gregsrv.dto.ModelVociAllD;
import it.csi.greg.gregsrv.dto.PrestazioniAlZeroPerFnpsDTO;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.springframework.transaction.annotation.Transactional;

@Service("modelloAllDService")
public class ModelloAllDService {

	@Autowired
	protected ModelloAllDDao modelloAllDDao;
	@Autowired
	protected ModelloB1Service modelloB1Service;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ConfiguratoreUtenzeFnpsService configuratoreUtenzeFnpsService;
	@Autowired
	protected ConfiguratoreUtenzeFnpsDao configuratoreUtenzeFnpsDao;
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
	@Autowired
	protected DatiEnteService dataEnteService;
	@Autowired
	protected ModelloAllontanamentoZeroDao mazd;

	public ModelVociAllD getVoci(Integer idScheda) {
		// AllD = ModuloFNPS
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idScheda);
		List<GregTPrestazioniMinisteriali> prestazioni = modelloAllDDao
				.findAllPrestazioni(rendicontazione.getAnnoGestione());
		List<GregDTargetUtenza> targetUtenze = modelloAllDDao.findAllTarget(rendicontazione.getAnnoGestione());
		List<GregRPrestMinistUtenzeMinist> prestUtenze = modelloAllDDao
				.findAllPrestazioniUtenza(rendicontazione.getAnnoGestione());
		List<GregDAree> aree = modelloAllDDao.findAllAree(rendicontazione.getAnnoGestione());

		ModelVociAllD voci = new ModelVociAllD();
		voci.setAree(new ArrayList<Aree>());
		voci.setListaPrestazione(new ArrayList<ModelPrestazioneAllD>());
		voci.setListaTargetUtenze(new ArrayList<ModelUtenzaAllD>());

		int c;
		for (GregDAree area : aree) {
			Aree a = new Aree();
			a.setArea(area.getDescArea());
			c = 0;
			for (GregDTargetUtenza utenza : targetUtenze) {
				if (area.getCodArea().equals(utenza.getGregDAree().getCodArea())) {
					c++;
				}
			}
			a.setNumeroUtenze(c);
			voci.getAree().add(a);
		}

		List<ModelPrestazioneAllD> listaPrestazioni = new ArrayList<ModelPrestazioneAllD>();
		List<ModelUtenzaAllD> tUtenze = new ArrayList<ModelUtenzaAllD>();
		List<ModelUtentiFnps> utenzeFnps = configuratoreUtenzeFnpsService
				.recuperaUtenzePerCalcoloByAnnoEsercizio(rendicontazione.getAnnoGestione());
		for (GregDAree area : aree) {
			for (GregDTargetUtenza utenza : targetUtenze) {
				if (utenza.getGregDAree().getCodArea().equals(area.getCodArea())) {
					ModelUtentiFnps u = utenzeFnps.stream()
							.filter(f -> f.getIdUtenza().equals(utenza.getIdTargetUtenza())).findFirst().orElse(null);
					ModelUtenzaAllD ute = new ModelUtenzaAllD();
					ute.setAree(utenza.getGregDAree().getCodArea());
					ute.setCodUtenza(utenza.getCodUtenza());
					ute.setDescUtenza(utenza.getDesUtenza());
					ute.setIdUtenza(utenza.getIdTargetUtenza());
					if (u != null) {
						ute.setValorePercentuale(BigDecimal.valueOf(u.getValorePercentuale()));
						ute.setUtilizzatoPerCalcolo(u.isUtilizzatoPerCalcolo());
					}
					tUtenze.add(ute);
				}
			}
			voci.setListaTargetUtenze(tUtenze);
		}

		for (GregTPrestazioniMinisteriali prestazione : prestazioni) {
			ModelPrestazioneAllD prest = new ModelPrestazioneAllD();
			prest.setCodPrestazione(prestazione.getCodPrestazioneMinisteriale());
			prest.setDescPrestazione(prestazione.getDescPrestazioneMinisteriale());
			prest.setIdPrestazione(prestazione.getIdPrestazioneMinisteriale());
			prest.setMacroAttivita(prestazione.getGregDMacroattivita().getCodMacroattivita());
			List<ModelUtenzaAllD> utenze = new ArrayList<ModelUtenzaAllD>();
			for (GregDAree area : aree) {
				for (GregDTargetUtenza utenza : targetUtenze) {
					if (utenza.getGregDAree().getCodArea().equals(area.getCodArea())) {
						ModelUtentiFnps u = utenzeFnps.stream()
								.filter(f -> f.getIdUtenza().equals(utenza.getIdTargetUtenza())).findFirst()
								.orElse(null);
						ModelUtenzaAllD ute = new ModelUtenzaAllD();
						for (GregRPrestMinistUtenzeMinist prestUtenza : prestUtenze) {
							if (prestUtenza.getGregTPrestazioniMinisteriali().getCodPrestazioneMinisteriale()
									.equals(prest.getCodPrestazione())
									&& prestUtenza.getGregDTargetUtenza().getCodUtenza()
											.equals(utenza.getCodUtenza())) {
								ute.setAree(utenza.getGregDAree().getCodArea());
								ute.setCodUtenza(utenza.getCodUtenza());
								ute.setDescUtenza(utenza.getDesUtenza());
								ute.setIdUtenza(utenza.getIdTargetUtenza());
								if (u != null) {
									ute.setValorePercentuale(BigDecimal.valueOf(u.getValorePercentuale()));
									ute.setUtilizzatoPerCalcolo(u.isUtilizzatoPerCalcolo());
								}
								utenze.add(ute);
							}
						}
					}
				}
			}
			prest.setUtenze(utenze);
			listaPrestazioni.add(prest);
		}
		voci.setListaPrestazione(listaPrestazioni);

		return voci;
	}

	public ModelRendicontazioneModAllD getRendicontazioneModAllD(Integer idScheda) {
		List<GregRRendicontazioneModuloFnps> rendicontazioni = modelloAllDDao
				.findAllRendicontazioneModAllDByEnte(idScheda);
//		
		GregTRendicontazioneEnte rendente = datiRendicontazioneService.getRendicontazione(idScheda);
		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService
				.getSchedaEnte(rendente.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		GregRRendicontazioneGiustificazioneFnps giustificazione = modelloAllDDao
				.findGiustificazioneFnpsByEnte(idScheda);
		ModelVociAllD voci = getVoci(rendente.getIdRendicontazioneEnte());
		ModelRendicontazioneModAllD rendCorrente = new ModelRendicontazioneModAllD();
		rendCorrente.setIdRendicontazioneEnte(rendente.getIdRendicontazioneEnte());
		rendCorrente.setIdSchedaEnteGestore(rendente.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		rendCorrente.setVociAllD(voci);
		if (giustificazione != null) {
			rendCorrente.setGiustificazione(giustificazione.getGisutificativo());
			rendCorrente.setGiustificazionePerInf(true);
		} else {
			rendCorrente.setGiustificazione(null);
			rendCorrente.setGiustificazionePerInf(false);
		}
		for (ModelPrestazioneAllD prestazione : rendCorrente.getVociAllD().getListaPrestazione()) {
			for (ModelUtenzaAllD targetMin : prestazione.getUtenze()) {
				for (GregRRendicontazioneModuloFnps rendicontazione : rendicontazioni) {
					if (rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
							.equals(rendCorrente.getIdRendicontazioneEnte())
							&& rendicontazione.getGregRPrestMinistUtenzeMinist().getGregDTargetUtenza().getCodUtenza()
									.equals(targetMin.getCodUtenza())
							&& rendicontazione.getGregRPrestMinistUtenzeMinist().getGregTPrestazioniMinisteriali()
									.getCodPrestazioneMinisteriale().equals(prestazione.getCodPrestazione())) {
						targetMin.setValore(rendicontazione.getValore());
					}
				}

			}
		}

		List<Object> rendicontazioneForRowB1 = modelloAllDDao.findRendicontazioneforRowB1(idScheda);
		List<ModelRendAllDforRowB1> listaRisultati = new ArrayList<ModelRendAllDforRowB1>();
		Iterator<Object> itr = rendicontazioneForRowB1.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			ModelRendAllDforRowB1 model = new ModelRendAllDforRowB1();
			model.setCodPrestazione(String.valueOf(obj[0]));
			model.setCodUtenza(String.valueOf(obj[1]));
			model.setValore((BigDecimal) obj[2]);
			listaRisultati.add(model);
		}
		
		List<PrestazioniAlZeroPerFnpsDTO> listaPrestAlZero = mazd.getPrestazioniAdattamentoFnps(idScheda);
		
		for (ModelRendAllDforRowB1 risultato : listaRisultati) {
			for (ModelPrestazioneAllD prestazione : voci.getListaPrestazione()) {
				if (risultato.getCodPrestazione().equals(prestazione.getCodPrestazione())) {
					for (ModelUtenzaAllD utenza : prestazione.getUtenze()) {
						if (risultato.getCodUtenza().equals(utenza.getCodUtenza())) {
							utenza.setValoreB1(risultato.getValore());
							if (utenza.getCodUtenza().equals("U09")) {	// Se famiglia e minori
								PrestazioniAlZeroPerFnpsDTO prestAlZero = listaPrestAlZero.stream().filter(e -> e.getCodPrestMinisteriale().equals(risultato.getCodPrestazione())).findFirst().orElse(null);
								if (utenza.getValoreB1() == null) {
									utenza.setValoreB1(BigDecimal.ZERO);
								}
								if (prestAlZero != null) {
									utenza.setValoreB1(utenza.getValoreB1().subtract(prestAlZero.getSommaValore()));
								}
							}
						}
					}
				}
			}
		}
		for (ModelPrestazioneAllD prestazione : voci.getListaPrestazione()) {
			for (ModelUtenzaAllD utenza : prestazione.getUtenze()) {
				if (utenza.getValoreB1() == null) {
					utenza.setValoreB1(BigDecimal.ZERO);
				}
			}
		}

		return rendCorrente;
	}

	public boolean controlloCompilazioneAllD(Integer idScheda) {
		boolean compilabile = true;
//		GregTSchedeEntiGestori ente = dataEnteService.getSchedaEnte(idScheda);
//		GregTRendicontazioneEnte ente = datiRendicontazioneService.getRendicontazione(idScheda);
		List<ModelFondi> fondi = configuratoreUtenzeFnpsService.findFondiByidRendicontazione(idScheda);
		ModelFondi fnps = fondi.stream().filter(f -> f.getCodTipologiaFondo().equals(SharedConstants.FNPS)).findFirst()
				.orElse(null);
		if (fnps == null || (fnps != null && fnps.getValore().equals(BigDecimal.ZERO))) {
			compilabile = false;
		}
//		if (ente.getVincoloFondo() == null) {
//			compilabile = false;
//		}
		return compilabile;
	}

	public SaveModelloOutput saveModelloAllD(ModelRendicontazioneModAllD body, UserInfo userInfo, String notaEnte,
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
//			rendToUpdate = datiRendicontazioneService.modificaStatoRendicontazione(rendToUpdate, userInfo,
//					SharedConstants.OPERAZIONE_SALVA);
			String statoNew = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			if (!statoOld.equals(statoNew)) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
						.getTestoMessaggio().replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'"
								+ statoOld
								+ "'")
						.replace("STATONEW", "'"
								+ statoNew
								+ "'");
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
					newNotaEnte = notaEnte;
				}

			} else {
				newNotaEnte = notaEnte;
			}

			if (body.isAzzeramentoGiustificativo()) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_AZZERA_FNPS).getTestoMessaggio()
						.replace("NOTA", newNotaEnte);
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(notaInterna)) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendToUpdate);
				cronologia.setGregDStatoRendicontazione(rendToUpdate.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome()
						+ " "
						+ userInfo.getCognome());
				cronologia.setModello("Mod. FNPS");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(notaInterna);
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}

			if (body.isGiustificazionePerInf()) {
				GregRRendicontazioneGiustificazioneFnps giustificazione = modelloAllDDao
						.findGiustificazioneFnpsByEnte(rendToUpdate.getIdRendicontazioneEnte());
				if (giustificazione != null) {
					if (body.getGiustificazione() == null || body.getGiustificazione().isEmpty()) {
						giustificazione.setDataModifica(dataModifica);
						giustificazione.setDataCancellazione(dataModifica);
						modelloAllDDao.updateRendicontazioneGiustificazioneFnps(giustificazione);
					} else {
						giustificazione.setGisutificativo(body.getGiustificazione());
						giustificazione.setDataModifica(dataModifica);
						modelloAllDDao.updateRendicontazioneGiustificazioneFnps(giustificazione);
					}
				} else {
					if (body.getGiustificazione() != null) {
						GregRRendicontazioneGiustificazioneFnps newRend = new GregRRendicontazioneGiustificazioneFnps();
						newRend.setGregTRendicontazioneEnte(rendToUpdate);
						newRend.setGisutificativo(body.getGiustificazione());
						newRend.setUtenteOperazione(userInfo.getCodFisc());
						newRend.setDataCreazione(dataModifica);
						newRend.setDataModifica(dataModifica);
						modelloAllDDao.insertRendicontazioneGiustificazioneFnps(newRend);
					}
				}
			} else {
				GregRRendicontazioneGiustificazioneFnps giustificazione = modelloAllDDao
						.findGiustificazioneFnpsByEnte(rendToUpdate.getIdRendicontazioneEnte());
				if (giustificazione != null) {
					if (body.getGiustificazione() == null || body.getGiustificazione().isEmpty()) {
						giustificazione.setDataModifica(dataModifica);
						giustificazione.setDataCancellazione(dataModifica);
						modelloAllDDao.updateRendicontazioneGiustificazioneFnps(giustificazione);
					}
				}
			}

			List<ModelPrestazioneAllD> listaToSaveOrUpdate = body.getVociAllD().getListaPrestazione();
			for (ModelPrestazioneAllD prestazione : listaToSaveOrUpdate) {
				for (ModelUtenzaAllD targetUtenza : prestazione.getUtenze()) {
					GregRRendicontazioneModuloFnps rendicontazione = modelloAllDDao
							.findRendincontazioneFnpsbyPrestTargUte(prestazione.getCodPrestazione(),
									targetUtenza.getCodUtenza(), rendToUpdate.getIdRendicontazioneEnte());
					if (rendicontazione != null) {
						if (targetUtenza.getValore() == null) {
							modelloAllDDao
									.deleteRendicontazioneModuloFnps(rendicontazione.getIdRendicontazioneModuloFnps());
						} else {
							rendicontazione.setValore(targetUtenza.getValore());
							rendicontazione.setDataModifica(dataModifica);
							modelloAllDDao.updateRendicontazioneModuloFnps(rendicontazione);
						}
					} else {
						if (targetUtenza.getValore() != null) {
							GregRRendicontazioneModuloFnps newRend = new GregRRendicontazioneModuloFnps();
							GregRPrestMinistUtenzeMinist prestUtenze = modelloAllDDao
									.findPrestMinistUtenzeModFnpsbyPrestazioneUtenze(prestazione.getCodPrestazione(),
											targetUtenza.getCodUtenza());
							newRend.setGregTRendicontazioneEnte(rendToUpdate);
							newRend.setGregRPrestMinistUtenzeMinist(prestUtenze);
							newRend.setValore(targetUtenza.getValore());
							newRend.setDataInizioValidita(dataModifica);
							newRend.setUtenteOperazione(userInfo.getCodFisc());
							newRend.setDataCreazione(dataModifica);
							newRend.setDataModifica(dataModifica);
							modelloAllDDao.insertRendicontazioneModuloFnps(newRend);
						}
					}
				}
			}
			for (ModelFondi azione : body.getAzioniSistema()) {
				GregRSpeseFnps speseFnps = modelloAllDDao.findSpesaFnpsByEnte(rendToUpdate.getIdRendicontazioneEnte(),
						azione.getIdFondo());
				if (speseFnps != null) {
					if (azione.getValoreSpesaFnps() == null) {
						modelloAllDDao.deleteSpeseFnps(speseFnps.getIdSpeseFnps());
					} else {
						speseFnps.setValore(azione.getValoreSpesaFnps());
						speseFnps.setDataModifica(dataModifica);
						modelloAllDDao.updateSpeseFnps(speseFnps);
					}
				} else {
					if (azione.getValoreSpesaFnps() != null) {
						GregRSpeseFnps newRend = new GregRSpeseFnps();
						newRend.setGregDFondi(configuratoreUtenzeFnpsDao.findFondobyId(azione.getIdFondo()));
						newRend.setGregTRendicontazioneEnte(rendToUpdate);
						newRend.setValore(azione.getValoreSpesaFnps());
						newRend.setUtenteOperazione(userInfo.getCodFisc());
						newRend.setDataCreazione(dataModifica);
						newRend.setDataModifica(dataModifica);
						modelloAllDDao.insertSpeseFnps(newRend);
					}
				}
			}
			for (ModelFondi quote : body.getQuote()) {
				GregRSpeseFnps speseFnps = modelloAllDDao.findSpesaFnpsByEnte(rendToUpdate.getIdRendicontazioneEnte(),
						quote.getIdFondo());
				if (speseFnps != null) {
					if (quote.getValoreSpesaFnps() == null) {
						modelloAllDDao.deleteSpeseFnps(speseFnps.getIdSpeseFnps());
					} else {
						speseFnps.setValore(quote.getValoreSpesaFnps());
						speseFnps.setDataModifica(dataModifica);
						modelloAllDDao.updateSpeseFnps(speseFnps);
					}
				} else {
					if (quote.getValoreSpesaFnps() != null) {
						GregRSpeseFnps newRend = new GregRSpeseFnps();
						newRend.setGregDFondi(configuratoreUtenzeFnpsDao.findFondobyId(quote.getIdFondo()));
						newRend.setGregTRendicontazioneEnte(rendToUpdate);
						newRend.setValore(quote.getValoreSpesaFnps());
						newRend.setUtenteOperazione(userInfo.getCodFisc());
						newRend.setDataCreazione(dataModifica);
						newRend.setDataModifica(dataModifica);
						modelloAllDDao.insertSpeseFnps(newRend);
					}
				}
			}
		}
		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdSchedaEnteGestore());
			// Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_FNPS);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
						ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
						SharedConstants.MAIL_MODIFICA_DATI_FNPS);
				out.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				out.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		return out;
	}

	

	@Transactional
	public String esportaModuloFnps(EsportaModAllDInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModuloFnps");
		int rowCount = 0;
		int columnCount = 0;
		Row row = sheet.createRow(rowCount);

		HSSFColor myColor = getColorExcel(workbook, "#E2EFDA");
		short palIndex = myColor.getIndex();
		HSSFColor myColor2 = getColorExcel(workbook, "#F2F2F2");
		short patGrey = myColor2.getIndex();
		HSSFColor myColor3 = getColorExcel(workbook, "#FFFFFF");
		short pattWhite = myColor3.getIndex();

		ModelMsgInformativo messaggio = listeService.getMsgInformativiByCodice("65");

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
		// crea stili arial 14 bold
		CellStyle cellStyleb14 = sheet.getWorkbook().createCellStyle();
		cellStyleb14.setAlignment(CellStyle.ALIGN_CENTER);
		Font fontb14 = sheet.getWorkbook().createFont();
		fontb14.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb14.setFontHeightInPoints((short) 14);
		fontb14.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleb14.setFont(fontb14);
		cellStyleb14.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold center
		CellStyle cellStyleb12c = sheet.getWorkbook().createCellStyle();
		cellStyleb12c.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleb12c.setFont(fontb12Y);
		cellStyleb12c.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10
		CellStyle cellStyle10 = sheet.getWorkbook().createCellStyle();
		cellStyle10.setAlignment(CellStyle.ALIGN_LEFT);
		Font font10 = sheet.getWorkbook().createFont();
		font10.setFontHeightInPoints((short) 10);
		font10.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle10.setFont(font10);
		cellStyle10.setWrapText(true);
		cellStyle10.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 9
		CellStyle cellStyle9 = sheet.getWorkbook().createCellStyle();
		cellStyle9.setAlignment(CellStyle.ALIGN_CENTER);
		Font font9 = sheet.getWorkbook().createFont();
		font9.setFontHeightInPoints((short) 9);
		font9.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle9.setFont(font9);
		cellStyle9.setWrapText(true);
		cellStyle9.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 Center
		CellStyle cellStyle10c = sheet.getWorkbook().createCellStyle();
		cellStyle10c.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle10c.setFont(font10);
		cellStyle10c.setWrapText(true);
		cellStyle10c.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold
		CellStyle cellStyle10b = sheet.getWorkbook().createCellStyle();
		cellStyle10b.setAlignment(CellStyle.ALIGN_LEFT);
		Font font10b = sheet.getWorkbook().createFont();
		font10b.setFontHeightInPoints((short) 10);
		font10b.setFontName(HSSFFont.FONT_ARIAL);
		font10b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle10b.setFont(font10b);
		cellStyle10b.setWrapText(true);
		cellStyle10b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		List<GregDMacroattivita> macro = modelloAllDDao.findAllMacroAttivita();

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
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 1
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "ENTE GESTORE DENOMINAZIONE E NUMERO :");
		cell.setCellStyle(cellStyleb12G);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale()
				+ " - "
				+ body.getDenominazioneEnte());
		cell.setCellStyle(cellStyleb12Y);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
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
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
//		String annoRilevazione = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")).toString();
		String annoRilevazione = String.valueOf(Integer.parseInt(body.getAnnoGestione()) + 1);
		cell.setCellValue((String) "ANNO DI RIFERIMENTO : "
				+ body.getAnnoGestione()
				+ " - RILEVAZIONE : "
				+ annoRilevazione);
		cell.setCellStyle(cellStyleb12G);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
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
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 10, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "Modulo FNPS - Prospetto ministeriale utilizzo delle risorse FNPS : ("
				+ body.getFnps()
				+ ")");
		cell.setCellStyle(cellStyleb12);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 4
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		// RIGA 5
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "Attivita'");
		cell.setCellStyle(cellStyleb14);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		int col = 0;
		for (Aree area : body.getAreeUtenze()) {
			for (int j = 0; j < area.getNumeroUtenze(); j++) {
				row.createCell(++columnCount);
			}
			col += area.getNumeroUtenze();
		}
		cell.setCellValue((String) "Area assistenziale");
		cell.setCellStyle(cellStyleb14);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - col, columnCount - 1);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 6
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + 1, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "MacroAttivita''");
		cell.setCellStyle(cellStyleb12c);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + 1, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "Interventi e servizi sociali");
		cell.setCellStyle(cellStyleb12c);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		int[] numUtenzeArea = new int[body.getAreeUtenze().size()];
		int i = 0;
		for (Aree area : body.getAreeUtenze()) {
			cell = row.createCell(++columnCount);
			for (int j = 0; j < area.getNumeroUtenze() - 1; j++) {
				row.createCell(++columnCount);
			}
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - (area.getNumeroUtenze() - 1),
					columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue((String) "Area " + area.getArea());
			cell.setCellStyle(cellStyleb12c);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			i++;
		}
		// RIGA 6
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());

		for (Aree area : body.getAreeUtenze()) {
			i = 0;
			int j = 0;
			for (ModelUtenzaAllD utenza : body.getUtenzeFnps()) {
				if (area.getArea().equals(utenza.getAree())) {
					cell = row.createCell(++columnCount);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
					cell.setCellValue((String) utenza.getDescUtenza());
					cell.setCellStyle(cellStyle9);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					if (j < numUtenzeArea[i]) {
						RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					} else {
						RegionUtil.setBorderLeft(CellStyle.BORDER_DOTTED, cellRangeAddress, sheet, sheet.getWorkbook());
					}
					if (j < numUtenzeArea[i]) {
						RegionUtil.setBorderRight(CellStyle.BORDER_DOTTED, cellRangeAddress, sheet,
								sheet.getWorkbook());
					} else {
						RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					}
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					j++;
				}
			}
			i++;
		}
		// Prestazioni
		int[] numPrestMacro = new int[macro.size()];
		i = 0;
		for (GregDMacroattivita macroA : macro) {
			numPrestMacro[i] = 0;
			for (ModelPrestazioneAllD prest : body.getPrestazioniFnps()) {
				if (prest.getMacroAttivita().equals(macroA.getCodMacroattivita())) {
					numPrestMacro[i]++;
				}
			}
			numPrestMacro[i] -= 1;
			i++;
		}
		i = 0;
		for (GregDMacroattivita macroA : macro) {
			int j = 0;
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount + numPrestMacro[i], columnCount, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue((String) macroA.getCodMacroattivita()
					+ ".");
			cell.setCellStyle(cellStyle10c);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			cell = row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount + numPrestMacro[i], columnCount, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue((String) macroA.getDescMacroattivita());
			cell.setCellStyle(cellStyle10);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			for (ModelPrestazioneAllD prest : body.getDatiFnps().getVociAllD().getListaPrestazione()) {
				if (prest.getMacroAttivita().equals(macroA.getCodMacroattivita())) {
					cell = row.createCell(++columnCount);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
					cell.setCellValue((String) prest.getCodPrestazione());
					cell.setCellStyle(cellStyle10);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					cell = row.createCell(++columnCount);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
					cell.setCellValue((String) prest.getDescPrestazione());
					cell.setCellStyle(cellStyle10);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_DOTTED, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					for (Aree area : body.getAreeUtenze()) {
						int k = 0;
						int l = 0;
						for (ModelUtenzaAllD utenza : prest.getUtenze()) {
							if (utenza.getAree().equals(area.getArea())) {
								cell = row.createCell(++columnCount);
								cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
								if (utenza.getValore() != null) {
									cell.setCellValue((String) Util.convertBigDecimalToString(utenza.getValore()));
								}
								cell.setCellStyle(cellStyle10c);
								RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
										sheet.getWorkbook());
								if (l < numUtenzeArea[k]) {
									RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
											sheet.getWorkbook());
								} else {
									RegionUtil.setBorderLeft(CellStyle.BORDER_DOTTED, cellRangeAddress, sheet,
											sheet.getWorkbook());
								}
								if (l < numUtenzeArea[k]) {
									RegionUtil.setBorderRight(CellStyle.BORDER_DOTTED, cellRangeAddress, sheet,
											sheet.getWorkbook());
								} else {
									RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
											sheet.getWorkbook());
								}
								RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
										sheet.getWorkbook());
								l++;
							}
						}
						k++;
					}
					if (j < numPrestMacro[i]) {
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						cell = row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
						cell.setCellStyle(cellStyleb12c);
						RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						cell = row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
						cell.setCellStyle(cellStyleb12c);
						RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					} else {
						j = 3;
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						cell = row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
//						for(int p=0; p<numUtenzeArea.length; p++) {
//							row.createCell(++columnCount);
//							row.createCell(++columnCount);
//							j+=2;
//						}
						for (Aree area : body.getAreeUtenze()) {
							for (int p = 0; p < area.getNumeroUtenze(); p++) {
								row.createCell(++columnCount);
							}
							j += area.getNumeroUtenze();
						}
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - j, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell.setCellStyle(cellStyleb12G);
						RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					}
					j++;
				}
			}
			i++;
		}
		// Totali
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
		cell.setCellValue((String) "TOTALE");
		cell.setCellStyle(cellStyle10);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		for (BigDecimal totale : body.getTotaleUtenze()) {
			cell = row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
			cell.setCellValue((String) Util.convertBigDecimalToString(totale));
			cell.setCellStyle(cellStyle10c);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		}

		// TOTALE RENDICONTAZIONE Modulo FNPS
		row = sheet.createRow(++rowCount);
//		row = sheet.createRow(++rowCount);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);

		int lepsAzioni = 0;
		for (ModelFondi azione : body.getDatiFnps().getAzioniSistema()) {
			if (azione.isLeps()) {
				lepsAzioni++;
			}
		}
		int lepsQuote = 0;
		for (ModelFondi quote : body.getDatiFnps().getQuote()) {
			if (quote.isLeps()) {
				lepsQuote++;
			}
		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + lepsAzioni, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "TOTALE RENDICONTAZIONE Modulo FNPS");
		cell.setCellStyle(cellStyle10b);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + lepsAzioni, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) body.getTotaloneUtenze());
		cell.setCellStyle(cellStyle10c);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		if (lepsAzioni > 0) {
			row.createCell(++columnCount);
			row.createCell(++columnCount);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell.setCellValue((String) "Azione di sistema e altro (su importo FNPS)");
			cell.setCellStyle(cellStyle10b);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		}
		for (ModelFondi azione : body.getDatiFnps().getAzioniSistema()) {
			if (azione.isLeps()) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue((String) azione.getDescFondo());
				cell.setCellStyle(cellStyle10b);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				cell = row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue(azione.getValoreSpesaFnps() != null
						? (String) Util.convertBigDecimalToString(azione.getValoreSpesaFnps())
						: (String) "");
				cell.setCellStyle(cellStyle10c);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			}
		}

		// RESIDUO DA RENDICONTARE
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		if (lepsQuote == 0) {
			lepsQuote = 1;
		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + (lepsQuote - 1), columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "Residuo FNPS da Rendicontare per Interventi E Servizi Sociali e per Azioni di Sistema/Altro");
		cell.setCellStyle(cellStyle10b);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + (lepsQuote - 1), columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) body.getResiduo());
		cell.setCellStyle(cellStyle10c);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		int k = 0;
		for (ModelFondi quote : body.getDatiFnps().getQuote()) {
			if (quote.isLeps()) {
				cell = row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue((String) quote.getDescFondo());
				cell.setCellStyle(cellStyle10b);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				cell = row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue(quote.getValoreSpesaFnps() != null
						? (String) Util.convertBigDecimalToString(quote.getValoreSpesaFnps())
						: (String) "");
				cell.setCellStyle(cellStyle10c);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				k++;
				if (k < lepsQuote) {
					columnCount = 0;
					row = sheet.createRow(++rowCount);
					cell = row.createCell(++columnCount);
					row.createCell(++columnCount);
					row.createCell(++columnCount);
					row.createCell(++columnCount);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
					cell.setCellStyle(cellStyle10b);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					cell = row.createCell(++columnCount);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
					cell.setCellStyle(cellStyle10c);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					row.createCell(++columnCount);
					row.createCell(++columnCount);
				}
			}
		}

		// giustificazione
		row = sheet.createRow(++rowCount);
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "Giustificazione mancato raggiungimento totale fondo");
		cell.setCellStyle(cellStyle10b);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) body.getGiustificazione());
		cell.setCellStyle(cellStyle10);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());

		sheet.setColumnWidth(0, 3 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		for (i = 5; i < 11; i++) {
			sheet.setColumnWidth(i, 15 * 256);
		}
		for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
			row = sheet.getRow(j);
			if (j == 3)
				row.setHeight((short) (row.getHeight() * 3));
			else
				row.setHeight((short) (row.getHeight() * 2));
		}

		row = sheet.getRow(7);
		row.setHeight((short) (row.getHeight() * 3));

		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModAllD_"
				+ nomefile, ".xls");
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

	public String getStatoFNPS(Integer idRendicontazione) {
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		ModelFondi fnps = configuratoreUtenzeFnpsService.findFondiByidRendicontazione(idRendicontazione).stream()
				.filter(f -> f.getCodTipologiaFondo().equalsIgnoreCase(SharedConstants.FNPS)).findFirst().orElse(null);
		if (fnps == null
				|| !checkRendicontazione(rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione())) {
			return SharedConstants.NON_COMPILATO;
		}
		ModelStatoMod stato = modelloAllDDao.getStatoModelloFNPS(idRendicontazione);
		return stato.getStato();
	}

	private boolean checkRendicontazione(String codStato) {
		switch (codStato) {
		case SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II:
		case SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE:
		case SharedConstants.STATO_RENDICONTAZIONE_VALIDATA:
		case SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA:
		case SharedConstants.STATO_RENDICONTAZIONE_CONCLUSA:
			return true;
		}
		return false;
	}
}
