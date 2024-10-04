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

import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloADao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.entity.GregDMissione;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRProgrammaMissioneTitSottotit;
import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneNonConformitaModB;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaIstatPreg1Preg2;
import it.csi.greg.gregsrv.dto.EsportaIstatSpese;
import it.csi.greg.gregsrv.dto.EsportaModelloBInput;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelB1Macroaggregati;
import it.csi.greg.gregsrv.dto.ModelB1ProgrammiMissioneTotali;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg1;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg2;
import it.csi.greg.gregsrv.dto.ModelB1Voci;
import it.csi.greg.gregsrv.dto.ModelB1VociPrestReg2;
import it.csi.greg.gregsrv.dto.ModelBMissioni;
import it.csi.greg.gregsrv.dto.ModelBProgramma;
import it.csi.greg.gregsrv.dto.ModelBSottotitolo;
import it.csi.greg.gregsrv.dto.ModelBTitolo;
import it.csi.greg.gregsrv.dto.ModelCampiMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModB;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliSpeseMissioni;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriMacroaggregati;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloBService")
public class ModelloBService {

	@Autowired
	protected ModelloBDao modelloBDao;
	@Autowired
	protected ModelloADao modelloADao;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
	@Autowired
	protected MacroaggregatiService macroaggregatiService;
	@Autowired
	protected ModelloAService modelloAService;
	@Autowired
	protected ModelloB1Service modelloB1Service;
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

	public List<ModelBMissioni> getMissioni() {
		List<GregDMissione> missioni = modelloBDao.findAllMissioni();
		List<GregDProgrammaMissione> programmi = modelloBDao.findAllProgrammaMissioni();
		List<GregRProgrammaMissioneTitSottotit> modB = modelloBDao.findAllTitoliMissioneModB();

		List<ModelBSottotitolo> listaSottotitoli = null;

		List<ModelBMissioni> listaMissioni = new ArrayList<ModelBMissioni>();

		String cod = null;
		String proMiss = null;

		for (GregDMissione missione : missioni) {

			ModelBMissioni missioneB = new ModelBMissioni();
			missioneB.setIdMissione(missione.getIdMissione());
			missioneB.setCodMissione(missione.getCodMissione());
			missioneB.setDescMissione(missione.getDesMissione());
			missioneB.setAltraDescMissione(missione.getAltraDesc());
			missioneB.setMsgInformativo(missione.getMsgInformativo());

			List<ModelBProgramma> listaProgramma = new ArrayList<ModelBProgramma>();

			for (GregDProgrammaMissione programma : programmi) {

				if (missione.getCodMissione().equals(programma.getGregDMissione().getCodMissione())) {

					ModelBProgramma programmaB = new ModelBProgramma();
					programmaB.setIdProgramma(programma.getIdProgrammaMissione());
					programmaB.setCodProgramma(programma.getCodProgrammaMissione());
					programmaB.setDescProgramma(programma.getDesProgrammaMissione());
					programmaB.setInformativa(programma.getInformativa());
					programmaB.setMsgInformativo(programma.getMsgInformativo());
					if (programma.getGregDColori() != null) {
						programmaB.setColore(programma.getGregDColori().getRgb());
					} else {
						programmaB.setColore(null);
					}

					List<ModelBTitolo> listaTitoli = new ArrayList<ModelBTitolo>();

					for (GregRProgrammaMissioneTitSottotit val : modB) {

						if (val.getGregDProgrammaMissione().getCodProgrammaMissione()
								.equals(programma.getCodProgrammaMissione())) {
							ModelBTitolo titoloB = null;

							if (cod == null || !cod.equals(val.getGregDTitolo().getCodTitolo()) || proMiss == null
									|| !proMiss.equals(programma.getCodProgrammaMissione())) {
								cod = val.getGregDTitolo().getCodTitolo();
								proMiss = programma.getCodProgrammaMissione();
								titoloB = new ModelBTitolo();
								titoloB.setIdTitolo(val.getGregDTitolo().getIdTitolo());
								titoloB.setCodTitolo(val.getGregDTitolo().getCodTitolo());
								titoloB.setDescTitolo(val.getGregDTitolo().getDesTitolo());
								titoloB.setAltraDescTitolo(val.getGregDTitolo().getAltraDesc());
								if (val.getMsgInformativo() != null && val.getGregDSottotitolo() == null) {
									titoloB.setMsgInformativo(val.getMsgInformativo());
								}
								listaSottotitoli = new ArrayList<ModelBSottotitolo>();
							}

							if (val.getGregDSottotitolo() != null) {

								ModelBSottotitolo sottotitoloB = new ModelBSottotitolo();
								sottotitoloB.setIdSottotitolo(val.getGregDSottotitolo().getIdSottotitolo());
								sottotitoloB.setCodSottotitolo(val.getGregDSottotitolo().getCodSottotitolo());
								sottotitoloB.setDescSottotitolo(val.getGregDSottotitolo().getDesSottotitolo());
								if (val.getMsgInformativo() != null) {
									sottotitoloB.setMsgInformativo(val.getMsgInformativo());
								}
								listaSottotitoli.add(sottotitoloB);

							}
							if (titoloB != null) {
								titoloB.setListaSottotitolo(listaSottotitoli);
								listaTitoli.add(titoloB);
							}
						}
					}
					programmaB.setListaTitolo(listaTitoli);
					listaProgramma.add(programmaB);
				}
			}
			missioneB.setListaProgramma(listaProgramma);
			listaMissioni.add(missioneB);
		}
		return listaMissioni;

	}

	public ModelRendicontazioneModB getRendicontazioneModB(Integer idScheda) {
		List<GregRRendMiProTitEnteGestoreModB> rendicontazioni = modelloBDao.findAllValoriModBByEnte(idScheda);
//		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService.getSchedaEnte(idScheda);
		GregTRendicontazioneEnte rendente = datiRendicontazioneService.getRendicontazione(idScheda);
		List<ModelBMissioni> missioni = getMissioni();
		ModelRendicontazioneModB rendCorrente = new ModelRendicontazioneModB();
		rendCorrente.setIdRendicontazioneEnte(rendente.getIdRendicontazioneEnte());
		rendCorrente.setIdSchedaEnteGestore(rendente.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		rendCorrente.setListaMissioni(missioni);
		for (ModelBMissioni missione : rendCorrente.getListaMissioni()) {
			for (ModelBProgramma programma : missione.getListaProgramma()) {
				if (programma.getListaTitolo() != null) {
					GregRRendicontazioneNonConformitaModB confModB = modelloBDao.findConformitabyProMissEnte(
							missione.getCodMissione(), programma.getCodProgramma(),
							rendCorrente.getIdRendicontazioneEnte());
					if (confModB != null && confModB.getMotivazione() != null) {
						programma.setMotivazione(confModB.getMotivazione());
						programma.setFlagConf(true);
					} else {
						programma.setFlagConf(false);
					}
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						for (GregRRendMiProTitEnteGestoreModB rendicontazione : rendicontazioni) {
							if (rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte().equals(rendCorrente
									.getIdRendicontazioneEnte())) {
								if (rendicontazione.getGregRProgrammaMissioneTitSottotit().getGregDProgrammaMissione()
										.getGregDMissione().getCodMissione().equals(missione.getCodMissione())) {
									if (rendicontazione.getGregRProgrammaMissioneTitSottotit()
											.getGregDProgrammaMissione().getCodProgrammaMissione()
											.equals(programma.getCodProgramma())) {
										if (rendicontazione.getGregRProgrammaMissioneTitSottotit().getGregDTitolo()
												.getCodTitolo().equals(titolo.getCodTitolo())) {
											if (rendicontazione.getGregRProgrammaMissioneTitSottotit()
													.getGregDSottotitolo() == null) {
												titolo.setValore(rendicontazione.getValore());
											}
											if (titolo.getListaSottotitolo() != null) {
												for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
													if (rendicontazione.getGregRProgrammaMissioneTitSottotit()
															.getGregDSottotitolo() != null
															&& rendicontazione.getGregRProgrammaMissioneTitSottotit()
																	.getGregDSottotitolo().getCodSottotitolo()
																	.equals(sottotitolo.getCodSottotitolo())) {
														sottotitolo.setValore(rendicontazione.getValore());
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return rendCorrente;
	}

	public List<String> controllaImportiModelloB(ModelRendicontazioneModB body) throws IntegritaException {
		List<String> listaWarnings = new ArrayList<String>();
		String warning = listeService.getMessaggio(SharedConstants.WARNING_MOD_B_01).getTestoMessaggio();

		ModelRendicontazioneTotaliSpeseMissioni totaliSpeseMissione = macroaggregatiService
				.getRendicontazioneTotaliSpesePerB(body.getIdRendicontazioneEnte());
		ModelRendicontazioneTotaliMacroaggregati totaliMacroaggregati = macroaggregatiService
				.getRendicontazioneTotaliMacroaggregatiPerB1(body.getIdRendicontazioneEnte());

		// 1 Verifico TotaleSpesaCorrente0412
		BigDecimal totaleSottotitoliMissione04 = BigDecimal.ZERO;
		// sommo i due sottotitoli
		totaleSottotitoliMissione04 = totaleSottotitoliMissione04
				.add(body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0)
						.getListaSottotitolo().get(0).getValore() != null
								? body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0)
										.getListaSottotitolo().get(0).getValore()
								: BigDecimal.ZERO)
				.add(body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0)
						.getListaSottotitolo().get(1).getValore() != null
								? body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0)
										.getListaSottotitolo().get(1).getValore()
								: BigDecimal.ZERO);
		BigDecimal totaleSpesaCorrente12 = BigDecimal.ZERO;
		List<ModelBProgramma> listaProgramma = body.getListaMissioni().get(2).getListaProgramma();
		for (ModelBProgramma programma : listaProgramma) {
			for (ModelBTitolo titolo : programma.getListaTitolo()) {
				if (titolo.getValore() != null && titolo.getCodTitolo().equals("1")) {
					totaleSpesaCorrente12 = totaleSpesaCorrente12.add(titolo.getValore());
				}
			}
		}
		BigDecimal totale1 = totaleSottotitoliMissione04.add(totaleSpesaCorrente12);
		if (totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale().compareTo(totale1) != 0) {
			String warning1 = warning.replace("QUALEIMPORTO",
					"TOTALE SPESA CORRENTE della MISSIONE 12 E della MISSIONE 04 (solo la parte concernente il Sostegno Socio Educativo Scolastico per minori stranieri e minori disabili - compreso Trasporto scolastico Disabili)")
					.replace("IMPORTO",
							Util.convertBigDecimalToString(
									totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale()))
					.replace("MODELLO", "Macroaggregati");
			listaWarnings.add(warning1);
		}

		// 2 Verifico TotaleSpesaCorrenteTutteMissioni
		BigDecimal totaleSpesaCorrenteTutteMissioni = BigDecimal.ZERO;
		List<ModelBMissioni> listaMissione = body.getListaMissioni();
		for (ModelBMissioni missione : listaMissione) {
			for (ModelBProgramma programma : missione.getListaProgramma()) {
				for (ModelBTitolo titolo : programma.getListaTitolo()) {
					if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
						totaleSpesaCorrenteTutteMissioni = totaleSpesaCorrenteTutteMissioni.add(titolo.getValore());
					}
				}
			}
		}
		if (totaleSpesaCorrenteTutteMissioni.compareTo(totaliSpeseMissione.getValoriSpese().get(3).getTotale()) != 0) {
			String warning2 = warning.replace("QUALEIMPORTO", "TOTALE SPESA CORRENTE DI TUTTE LE MISSIONI")
					.replace("IMPORTO",
							Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(3).getTotale()))
					.replace("MODELLO", "Macroaggregati");
			listaWarnings.add(warning2);
		}

		// 3 Verifica TotaleSpesaCorrenteMissione1
		BigDecimal totaleSpesaCorrenteMissione1 = BigDecimal.ZERO;
		for (ModelBProgramma programma : body.getListaMissioni().get(0).getListaProgramma()) {
			for (ModelBTitolo titolo : programma.getListaTitolo()) {
				if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
					totaleSpesaCorrenteMissione1 = totaleSpesaCorrenteMissione1.add(titolo.getValore());
				}
			}
		}
		if (totaleSpesaCorrenteMissione1.compareTo(totaliSpeseMissione.getValoriSpese().get(0).getTotale()) != 0) {
			String warning3 = warning.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 01")
					.replace("IMPORTO",
							Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(0).getTotale()))
					.replace("MODELLO", "Macroaggregati");
			listaWarnings.add(warning3);
		}

		// 4 Verifica Titolo1Missione04
		if (totaleSottotitoliMissione04.compareTo(totaliSpeseMissione.getValoriSpese().get(2).getTotale()) != 0) {
			String warning4 = warning.replace("QUALEIMPORTO", "valore di Missione04/Programma06/Titolo1")
					.replace("IMPORTO",
							Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(2).getTotale()))
					.replace("MODELLO", "Macroaggregati (Missione 04 - Istruzione e diritto allo studio)");
			listaWarnings.add(warning4);
		}

		// 5 Verifica TotaleSpesaCorrenteMissione12
		BigDecimal totaleSpesaCorrenteMissione12 = BigDecimal.ZERO;
		for (ModelBProgramma programma : body.getListaMissioni().get(2).getListaProgramma()) {
			for (ModelBTitolo titolo : programma.getListaTitolo()) {
				if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
					totaleSpesaCorrenteMissione12 = totaleSpesaCorrenteMissione12.add(titolo.getValore());
				}
			}
		}
		if (totaleSpesaCorrenteMissione12.compareTo(totaliSpeseMissione.getValoriSpese().get(1).getTotale()) != 0) {
			String warning5 = warning.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 12")
					.replace("IMPORTO",
							Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(1).getTotale()))
					.replace("MODELLO",
							"Macroaggregati (valore Missione 01 - parte spesa socio-assistenziale sommato a valore Missione 12)");
			listaWarnings.add(warning5);
		}

		return listaWarnings;
	}

	public Boolean canActiveModB(Integer idScheda) {
		Boolean canActiveModBForMacro = false;
		Boolean canActiveModBForB1 = false;

		ModelRendicontazioneMacroaggregati rendMacro = macroaggregatiService
				.getRendicontazioneMacroaggregatiByIdScheda(idScheda);
		for (ModelValoriMacroaggregati valoriMacro : rendMacro.getValoriMacroaggregati()) {
			if (valoriMacro.getCampi() != null) {
				for (ModelCampiMacroaggregati campo : valoriMacro.getCampi()) {
					if (campo.getValue() != null && campo.getValue().compareTo(BigDecimal.ZERO) == 1) {
						canActiveModBForMacro = true;
						break;
					}
				}
			}
		}

		List<ModelB1Voci> rendB1 = modelloB1Service.getVoci(idScheda);
		for (ModelB1Voci voceB1 : rendB1) {
			for (ModelB1Macroaggregati modelB1Macro : voceB1.getMacroaggregati()) {
				if (modelB1Macro.getValore() != null
						&& Util.convertStringToBigDecimal(modelB1Macro.getValore()).compareTo(BigDecimal.ZERO) == 1) {
					canActiveModBForB1 = true;
					break;
				}
			}
			if (canActiveModBForB1) {
				break;
			}

			for (ModelB1UtenzaPrestReg1 modelB1Utenza : voceB1.getUtenze()) {
				if (modelB1Utenza.getValore() != null
						&& Util.convertStringToBigDecimal(modelB1Utenza.getValore()).compareTo(BigDecimal.ZERO) == 1) {
					canActiveModBForB1 = true;
					break;
				}
			}
			if (canActiveModBForB1) {
				break;
			}

			for (ModelB1VociPrestReg2 modelB1VocePrest : voceB1.getPrestazioniRegionali2()) {
				for (ModelB1UtenzaPrestReg2 prestReg : modelB1VocePrest.getUtenze()) {
					if (prestReg.getValore() != null
							&& Util.convertStringToBigDecimal(prestReg.getValore()).compareTo(BigDecimal.ZERO) == 1) {
						canActiveModBForB1 = true;
						break;
					}
				}
				if (canActiveModBForB1) {
					break;
				}

			}
			if (canActiveModBForB1) {
				break;
			}

			for (ModelB1UtenzaPrestReg1 modelB1UtenzaPrestCosto : voceB1.getUtenzeCostoTotale()) {
				if (modelB1UtenzaPrestCosto.getValore() != null
						&& Util.convertStringToBigDecimal(modelB1UtenzaPrestCosto.getValore())
								.compareTo(BigDecimal.ZERO) == 1) {
					canActiveModBForB1 = true;
					break;
				}
			}
			if (canActiveModBForB1) {
				break;
			}

			for (ModelB1UtenzaPrestReg1 modelB1UtenzaPrestQuota : voceB1.getUtenzeQuotaSocioAssistenziale()) {
				if (modelB1UtenzaPrestQuota.getValore() != null
						&& Util.convertStringToBigDecimal(modelB1UtenzaPrestQuota.getValore())
								.compareTo(BigDecimal.ZERO) == 1) {
					canActiveModBForB1 = true;
					break;
				}
			}
			if (canActiveModBForB1) {
				break;
			}
		}

		return canActiveModBForMacro && canActiveModBForB1;
	}

	public SaveModelloOutput saveModelloB(ModelRendicontazioneModB body, UserInfo userInfo, String notaEnte,
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
				cronologia.setModello("Mod. B");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(notaInterna);
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}
			List<ModelBMissioni> listaToSaveOrUpdate = body.getListaMissioni();
			for (ModelBMissioni missione : listaToSaveOrUpdate) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					if (programma.getListaTitolo() != null) {
						GregRRendicontazioneNonConformitaModB confModB = modelloBDao.findConformitabyProMissEnte(
								missione.getCodMissione(), programma.getCodProgramma(),
								rendToUpdate.getIdRendicontazioneEnte());
						GregDProgrammaMissione proMiss = modelloBDao.findProgrammaMissionibyProgrammaMissione(
								missione.getCodMissione(), programma.getCodProgramma());
						if (confModB != null) {
							if (programma.getMotivazione() == null || programma.getMotivazione().isEmpty()) {
								modelloBDao.deleteRendicontazioneConformita(
										confModB.getIdRendicontazioneNonConformitaModB());
							} else {
								confModB.setMotivazione(programma.getMotivazione());
								confModB.setDataModifica(dataModifica);
								modelloBDao.updateRendicontazioneConformita(confModB);
							}
						} else {
							if (programma.getMotivazione() != null) {
								GregRRendicontazioneNonConformitaModB newConf = new GregRRendicontazioneNonConformitaModB();
								newConf.setGregTRendicontazioneEnte(rendToUpdate);
								newConf.setGregDProgrammaMissione(proMiss);
								newConf.setMotivazione(programma.getMotivazione());
								newConf.setDataInizioValidita(dataModifica);
								newConf.setUtenteOperazione(userInfo.getCodFisc());
								newConf.setDataCreazione(dataModifica);
								newConf.setDataModifica(dataModifica);
								modelloBDao.insertRendicontazioneConformita(newConf);
							}
						}
						for (ModelBTitolo titolo : programma.getListaTitolo()) {
							if (titolo.getListaSottotitolo() != null) {
								for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
									GregRRendMiProTitEnteGestoreModB rendProMissTitSottotit = modelloBDao
											.findRendModBbyMissProTitSottotit(missione.getCodMissione(),
													programma.getCodProgramma(), titolo.getCodTitolo(),
													sottotitolo.getCodSottotitolo(),
													rendToUpdate.getIdRendicontazioneEnte());
									GregRProgrammaMissioneTitSottotit proMissTitSottotit = modelloBDao
											.findTitoliMissioneModBbyMissProTitSottotit(missione.getCodMissione(),
													programma.getCodProgramma(), titolo.getCodTitolo(),
													sottotitolo.getCodSottotitolo());
									if (rendProMissTitSottotit != null) {
										if (sottotitolo.getValore() == null) {
											modelloBDao.deleteRendicontazioneProMissTitSottotit(
													rendProMissTitSottotit.getIdRendMiProTitEnteGestoreModB());
										} else {
											rendProMissTitSottotit.setValore(sottotitolo.getValore());
											rendProMissTitSottotit.setDataModifica(dataModifica);
											modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
										}
									} else {
										if (sottotitolo.getValore() != null) {
											GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
											newRend.setGregTRendicontazioneEnte(rendToUpdate);
											newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
											newRend.setValore(sottotitolo.getValore());
											newRend.setDataInizioValidita(dataModifica);
											newRend.setUtenteOperazione(userInfo.getCodFisc());
											newRend.setDataCreazione(dataModifica);
											newRend.setDataModifica(dataModifica);
											modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
										}
									}
								}
							}
							GregRRendMiProTitEnteGestoreModB rendProMissTitSottotit = modelloBDao
									.findRendModBbyMissProTitNoSottotit(missione.getCodMissione(),
											programma.getCodProgramma(), titolo.getCodTitolo(),
											rendToUpdate.getIdRendicontazioneEnte());
							GregRProgrammaMissioneTitSottotit proMissTitSottotit = modelloBDao
									.findTitoliMissioneModBbyMissProTitNoSottotit(missione.getCodMissione(),
											programma.getCodProgramma(), titolo.getCodTitolo());

							if (rendProMissTitSottotit != null) {
								if (titolo.getValore() == null) {
									modelloBDao.deleteRendicontazioneProMissTitSottotit(
											rendProMissTitSottotit.getIdRendMiProTitEnteGestoreModB());
								} else {
									rendProMissTitSottotit.setValore(titolo.getValore());
									rendProMissTitSottotit.setDataModifica(dataModifica);
									modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
								}
							} else {
								if (titolo.getValore() != null) {
									GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
									newRend.setGregTRendicontazioneEnte(rendToUpdate);
									newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
									newRend.setValore(titolo.getValore());
									newRend.setDataInizioValidita(dataModifica);
									newRend.setUtenteOperazione(userInfo.getCodFisc());
									newRend.setDataCreazione(dataModifica);
									newRend.setDataModifica(dataModifica);
									modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
								}
							}
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
	public String esportaModelloB(EsportaModelloBInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloB");
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
		CellStyle cellStyleTitoloModelMissioni = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloModelMissioni.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTitoloModelMissioni.setFont(font12b);
		cellStyleTitoloModelMissioni.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Voce Tabella
		CellStyle cellStyleVociTotali = sheet.getWorkbook().createCellStyle();
		cellStyleVociTotali.setAlignment(CellStyle.ALIGN_CENTER);
		Font fontVoceTotale = sheet.getWorkbook().createFont();
		fontVoceTotale.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontVoceTotale.setFontHeightInPoints((short) 9);
		fontVoceTotale.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVociTotali.setFont(fontVoceTotale);
		cellStyleVociTotali.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleVociTotali.setWrapText(true);
		cellStyleVociTotali.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleVociTotali.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleVociTotali.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleVociTotali.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Voce Tabella Border Double
		CellStyle cellStyleVoceTabellaWithBorderDouble = sheet.getWorkbook().createCellStyle();
		cellStyleVoceTabellaWithBorderDouble.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVoceTabellaWithBorder = sheet.getWorkbook().createFont();
		fontVoceTabellaWithBorder.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontVoceTabellaWithBorder.setFontHeightInPoints((short) 11);
		fontVoceTabellaWithBorder.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVoceTabellaWithBorderDouble.setFont(fontVoceTabellaWithBorder);
		cellStyleVoceTabellaWithBorderDouble.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleVoceTabellaWithBorderDouble.setWrapText(true);
		cellStyleVoceTabellaWithBorderDouble.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleVoceTabellaWithBorderDouble.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleVoceTabellaWithBorderDouble.setBorderRight(CellStyle.BORDER_THIN);
		// Crea Stile Voce Tabella Border Double
		CellStyle cellStyleVoceOnlyBorderDouble = sheet.getWorkbook().createCellStyle();
		cellStyleVoceOnlyBorderDouble.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVoceTabellaOnlyBorder = sheet.getWorkbook().createFont();
		fontVoceTabellaOnlyBorder.setFontHeightInPoints((short) 11);
		fontVoceTabellaOnlyBorder.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVoceOnlyBorderDouble.setFont(fontVoceTabellaOnlyBorder);
		cellStyleVoceOnlyBorderDouble.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleVoceOnlyBorderDouble.setWrapText(true);
		cellStyleVoceOnlyBorderDouble.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleVoceOnlyBorderDouble.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleVoceOnlyBorderDouble.setBorderLeft(CellStyle.BORDER_THIN);
		// Crea Stile Voce Tabella Border Double With Bold
		CellStyle cellStyleVoceOnlyBorderDoubleWithBold = sheet.getWorkbook().createCellStyle();
		cellStyleVoceOnlyBorderDoubleWithBold.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVoceTabellaOnlyBorderWithBold = sheet.getWorkbook().createFont();
		fontVoceTabellaOnlyBorderWithBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontVoceTabellaOnlyBorderWithBold.setFontHeightInPoints((short) 11);
		fontVoceTabellaOnlyBorderWithBold.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVoceOnlyBorderDoubleWithBold.setFont(fontVoceTabellaOnlyBorderWithBold);
		cellStyleVoceOnlyBorderDoubleWithBold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleVoceOnlyBorderDoubleWithBold.setWrapText(true);
		cellStyleVoceOnlyBorderDoubleWithBold.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleVoceOnlyBorderDoubleWithBold.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleVoceOnlyBorderDoubleWithBold.setBorderLeft(CellStyle.BORDER_THIN);
		// Crea Stile Conforme
		CellStyle cellStyleConforme = sheet.getWorkbook().createCellStyle();
		cellStyleConforme.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontConforme = sheet.getWorkbook().createFont();
		fontConforme.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontConforme.setFontHeightInPoints((short) 9);
		fontConforme.setFontName(HSSFFont.FONT_ARIAL);
		fontConforme.setItalic(true);
		cellStyleConforme.setFont(fontConforme);
		cellStyleConforme.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyleConforme.setWrapText(true);
		cellStyleConforme.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleConforme.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleConforme.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleConforme.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Descrizione Motivazione
		CellStyle cellStyleDescMotivazione = sheet.getWorkbook().createCellStyle();
		cellStyleDescMotivazione.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDescMotivazione = sheet.getWorkbook().createFont();
		fontDescMotivazione.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDescMotivazione.setFontHeightInPoints((short) 9);
		fontDescMotivazione.setFontName(HSSFFont.FONT_ARIAL);
		fontDescMotivazione.setItalic(true);
		cellStyleDescMotivazione.setFont(fontDescMotivazione);
		cellStyleDescMotivazione.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyleDescMotivazione.setWrapText(true);
		cellStyleDescMotivazione.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDescMotivazione.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDescMotivazione.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleDescMotivazione.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Testo Motivazione
		CellStyle cellStyleTestoMotivazione = sheet.getWorkbook().createCellStyle();
		cellStyleTestoMotivazione.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTestoMotivazione = sheet.getWorkbook().createFont();
		fontTestoMotivazione.setFontHeightInPoints((short) 9);
		fontTestoMotivazione.setFontName(HSSFFont.FONT_ARIAL);
		fontTestoMotivazione.setItalic(true);
		cellStyleTestoMotivazione.setFont(fontTestoMotivazione);
		cellStyleTestoMotivazione.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyleTestoMotivazione.setWrapText(true);
		cellStyleTestoMotivazione.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTestoMotivazione.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTestoMotivazione.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTestoMotivazione.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Totali
		CellStyle cellStyleTotaliMissione = sheet.getWorkbook().createCellStyle();
		cellStyleTotaliMissione.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotaliMissione.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleTotaliMissione.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaliMissione.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaliMissione.setWrapText(true);
		cellStyleTotaliMissione.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotaliMissione.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTotaliMissione.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotaliMissione.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Testo Titolo1 Programma
		CellStyle cellStyleTitolo1Programma = sheet.getWorkbook().createCellStyle();
		cellStyleTitolo1Programma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitolo1Programma = sheet.getWorkbook().createFont();
		fontTitolo1Programma.setFontHeightInPoints((short) 11);
		fontTitolo1Programma.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleTitolo1Programma.setFont(fontTitolo1Programma);
		cellStyleTitolo1Programma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitolo1Programma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitolo1Programma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitolo1Programma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitolo1Programma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitolo1Programma.setWrapText(true);
		// Crea Stile Testo Titolo2 Programma
		CellStyle cellStyleTitolo2Programma = sheet.getWorkbook().createCellStyle();
		cellStyleTitolo2Programma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitolo2Programma = sheet.getWorkbook().createFont();
		fontTitolo2Programma.setFontHeightInPoints((short) 10);
		fontTitolo2Programma.setFontName(HSSFFont.FONT_ARIAL);
		fontTitolo2Programma.setItalic(true);
		cellStyleTitolo2Programma.setFont(fontTitolo2Programma);
		cellStyleTitolo2Programma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitolo2Programma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitolo2Programma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitolo2Programma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitolo2Programma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitolo2Programma.setWrapText(true);
		// Crea Stile Testo Titolo3 Programma
		CellStyle cellStyleTitolo3Programma = sheet.getWorkbook().createCellStyle();
		cellStyleTitolo3Programma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitolo3Programma = sheet.getWorkbook().createFont();
		fontTitolo3Programma.setFontHeightInPoints((short) 8);
		fontTitolo3Programma.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleTitolo3Programma.setFont(fontTitolo3Programma);
		cellStyleTitolo3Programma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitolo3Programma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitolo3Programma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitolo3Programma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitolo3Programma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitolo3Programma.setWrapText(true);
		// Crea Stile Testo TitoloAltro1 Programma
		CellStyle cellStyleTitoloAltro1Programma = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloAltro1Programma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitoloAltro1Programma = sheet.getWorkbook().createFont();
		fontTitoloAltro1Programma.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitoloAltro1Programma.setFontHeightInPoints((short) 11);
		fontTitoloAltro1Programma.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleTitoloAltro1Programma.setFont(fontTitoloAltro1Programma);
		cellStyleTitoloAltro1Programma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitoloAltro1Programma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1Programma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1Programma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1Programma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1Programma.setWrapText(true);
		// Crea Stile Testo TitoloAltro1 Programma NoBold
		CellStyle cellStyleTitoloAltro1ProgrammaNoBold = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloAltro1ProgrammaNoBold.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitoloAltro1ProgrammaNoBold = sheet.getWorkbook().createFont();
		fontTitoloAltro1ProgrammaNoBold.setFontHeightInPoints((short) 11);
		fontTitoloAltro1ProgrammaNoBold.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleTitoloAltro1ProgrammaNoBold.setFont(fontTitoloAltro1ProgrammaNoBold);
		cellStyleTitoloAltro1ProgrammaNoBold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitoloAltro1ProgrammaNoBold.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1ProgrammaNoBold.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1ProgrammaNoBold.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1ProgrammaNoBold.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro1ProgrammaNoBold.setWrapText(true);
		// Crea Stile Testo TitoloAltro2 Programma
		CellStyle cellStyleTitoloAltro2Programma = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloAltro2Programma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitoloAltro2Programma = sheet.getWorkbook().createFont();
		fontTitoloAltro2Programma.setFontHeightInPoints((short) 10);
		fontTitoloAltro2Programma.setFontName(HSSFFont.FONT_ARIAL);
		fontTitoloAltro2Programma.setItalic(true);
		cellStyleTitoloAltro2Programma.setFont(fontTitoloAltro2Programma);
		cellStyleTitoloAltro2Programma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitoloAltro2Programma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro2Programma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro2Programma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro2Programma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro2Programma.setWrapText(true);
		// Crea Stile Testo TitoloAltro3 Programma
		CellStyle cellStyleTitoloAltro3Programma = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloAltro3Programma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitoloAltro3Programma = sheet.getWorkbook().createFont();
		fontTitoloAltro3Programma.setFontHeightInPoints((short) 8);
		fontTitoloAltro3Programma.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleTitoloAltro3Programma.setFont(fontTitoloAltro3Programma);
		cellStyleTitoloAltro3Programma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitoloAltro3Programma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro3Programma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro3Programma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro3Programma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitoloAltro3Programma.setWrapText(true);
		// // Crea Stile Testo TitoloAltro Programma
		CellStyle cellStyleTitoloAltroProgramma = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloAltroProgramma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontTitoloAltroProgramma = sheet.getWorkbook().createFont();
		fontTitoloAltroProgramma.setFontHeightInPoints((short) 10);
		fontTitoloAltroProgramma.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleTitoloAltroProgramma.setFont(fontTitoloAltroProgramma);
		cellStyleTitoloAltroProgramma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTitoloAltroProgramma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTitoloAltroProgramma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTitoloAltroProgramma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTitoloAltroProgramma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleTitoloAltroProgramma.setWrapText(true);
		// Crea Stile Testo Dicui Titolo Programma
		CellStyle cellStyleDicuiTitoloProgramma = sheet.getWorkbook().createCellStyle();
		cellStyleDicuiTitoloProgramma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDicuiTitoloProgramma = sheet.getWorkbook().createFont();
		fontDicuiTitoloProgramma.setFontHeightInPoints((short) 9);
		fontDicuiTitoloProgramma.setFontName(HSSFFont.FONT_ARIAL);
		fontDicuiTitoloProgramma.setItalic(true);
		cellStyleDicuiTitoloProgramma.setFont(fontDicuiTitoloProgramma);
		cellStyleDicuiTitoloProgramma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDicuiTitoloProgramma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDicuiTitoloProgramma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDicuiTitoloProgramma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleDicuiTitoloProgramma.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Testo Dicui Titolo Programma
		CellStyle cellStyleDicuiAltroTitoloProgramma = sheet.getWorkbook().createCellStyle();
		cellStyleDicuiAltroTitoloProgramma.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDicuiAltroTitoloProgramma = sheet.getWorkbook().createFont();
		fontDicuiAltroTitoloProgramma.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDicuiAltroTitoloProgramma.setFontHeightInPoints((short) 9);
		fontDicuiAltroTitoloProgramma.setFontName(HSSFFont.FONT_ARIAL);
		fontDicuiAltroTitoloProgramma.setItalic(true);
		cellStyleDicuiAltroTitoloProgramma.setFont(fontDicuiAltroTitoloProgramma);
		cellStyleDicuiAltroTitoloProgramma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDicuiAltroTitoloProgramma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTitoloProgramma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTitoloProgramma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTitoloProgramma.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Testo Dicui Titolo1 Programma06 Missione 04 Green
		CellStyle cellStyleDicuiAltroTit1Prog06Mis04Green = sheet.getWorkbook().createCellStyle();
		cellStyleDicuiAltroTit1Prog06Mis04Green.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleDicuiAltroTit1Prog06Mis04Green.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font fontDicuiAltroTit1Prog06Mis04Green = sheet.getWorkbook().createFont();
		fontDicuiAltroTit1Prog06Mis04Green.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDicuiAltroTit1Prog06Mis04Green.setFontHeightInPoints((short) 9);
		fontDicuiAltroTit1Prog06Mis04Green.setFontName(HSSFFont.FONT_ARIAL);
		fontDicuiAltroTit1Prog06Mis04Green.setItalic(true);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setFont(fontDicuiAltroTit1Prog06Mis04Green);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTit1Prog06Mis04Green.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Testo Dicui Titolo1 Programma06 Missione 04 Yellow
		CellStyle cellStyleDicuiAltroTit1Prog06Mis04Yellow = sheet.getWorkbook().createCellStyle();
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font fontDicuiAltroTit1Prog06Mis04Yellow = sheet.getWorkbook().createFont();
		fontDicuiAltroTit1Prog06Mis04Yellow.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDicuiAltroTit1Prog06Mis04Yellow.setFontHeightInPoints((short) 9);
		fontDicuiAltroTit1Prog06Mis04Yellow.setFontName(HSSFFont.FONT_ARIAL);
		fontDicuiAltroTit1Prog06Mis04Yellow.setItalic(true);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setFont(fontDicuiAltroTit1Prog06Mis04Yellow);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleDicuiAltroTit1Prog06Mis04Yellow.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore White
		CellStyle cellStyleValueWhite = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhite.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueWhite.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleValueWhite.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueWhite.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueWhite.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore White with border right
		CellStyle cellStyleValueWhiteWithBorderRight = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhiteWithBorderRight.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueWhiteWithBorderRight.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleValueWhiteWithBorderRight.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueWhiteWithBorderRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueWhiteWithBorderRight.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueWhiteWithBorderRight.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueWhiteWithBorderRight.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleValueWhiteWithBorderRight.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore Green
		CellStyle cellStyleValueGreen = sheet.getWorkbook().createCellStyle();
		cellStyleValueGreen.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueGreen.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleValueGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueGreen.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueGreen.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueGreen.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueGreen.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueGreen.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore Green with border right
		CellStyle cellStyleValueGreenWithBorderRight = sheet.getWorkbook().createCellStyle();
		cellStyleValueGreenWithBorderRight.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueGreenWithBorderRight.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleValueGreenWithBorderRight.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueGreenWithBorderRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueGreenWithBorderRight.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueGreenWithBorderRight.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueGreenWithBorderRight.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleValueGreenWithBorderRight.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore Grey
		CellStyle cellStyleValueGrey = sheet.getWorkbook().createCellStyle();
		cellStyleValueGrey.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueGrey.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleValueGrey.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueGrey.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueGrey.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueGrey.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueGrey.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueGrey.setBorderBottom(CellStyle.BORDER_THIN);

		// Mappatura Colori e Stile Programmi Missione 12
		Map<String, CellStyle> programmaMissioneStyles = new HashMap<String, CellStyle>();
		Font fontProgrammaStyle = sheet.getWorkbook().createFont();
		fontProgrammaStyle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontProgrammaStyle.setFontHeightInPoints((short) 11);
		fontProgrammaStyle.setFontName(HSSFFont.FONT_ARIAL);
		for (ModelBProgramma programma : body.getMissioniB().get(2).getListaProgramma()) {
			HSSFColor programmaColor = getColorExcel(workbook, programma.getColore());
			short indexColor = programmaColor.getIndex();

			CellStyle programmaDescStyle = sheet.getWorkbook().createCellStyle();
			programmaDescStyle.setAlignment(CellStyle.ALIGN_LEFT);
			programmaDescStyle.setFillForegroundColor(indexColor);
			programmaDescStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			programmaDescStyle.setFont(fontProgrammaStyle);
			programmaDescStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			programmaDescStyle.setWrapText(true);
			programmaDescStyle.setBorderTop(CellStyle.BORDER_DOUBLE);
			programmaDescStyle.setBorderBottom(CellStyle.BORDER_DOUBLE);
			programmaDescStyle.setBorderRight(CellStyle.BORDER_THIN);

			CellStyle programmaInfoStyle = sheet.getWorkbook().createCellStyle();
			programmaInfoStyle.setAlignment(CellStyle.ALIGN_LEFT);
			programmaInfoStyle.setFillForegroundColor(indexColor);
			programmaInfoStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			programmaInfoStyle.setFont(fontProgrammaStyle);
			programmaInfoStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			programmaInfoStyle.setWrapText(true);
			programmaInfoStyle.setBorderTop(CellStyle.BORDER_DOUBLE);
			programmaInfoStyle.setBorderBottom(CellStyle.BORDER_DOUBLE);
			programmaInfoStyle.setBorderLeft(CellStyle.BORDER_THIN);

			programmaMissioneStyles.put(programma.getCodProgramma() + "_DESC", programmaDescStyle);
			programmaMissioneStyles.put(programma.getCodProgramma() + "_INFO", programmaInfoStyle);
		}

		// Crea Stile Descrizioni Totali
		// Totale Spesa Corrente
		CellStyle cellStyleDescTotaleSpesaCorrente = sheet.getWorkbook().createCellStyle();
		cellStyleDescTotaleSpesaCorrente.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDescTotaleSpesaCorrente = sheet.getWorkbook().createFont();
		fontDescTotaleSpesaCorrente.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDescTotaleSpesaCorrente.setFontHeightInPoints((short) 11);
		fontDescTotaleSpesaCorrente.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleDescTotaleSpesaCorrente.setFont(fontDescTotaleSpesaCorrente);
		cellStyleDescTotaleSpesaCorrente.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		cellStyleDescTotaleSpesaCorrente.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleDescTotaleSpesaCorrente.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescTotaleSpesaCorrente.setWrapText(true);
		cellStyleDescTotaleSpesaCorrente.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDescTotaleSpesaCorrente.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDescTotaleSpesaCorrente.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleDescTotaleSpesaCorrente.setBorderBottom(CellStyle.BORDER_THIN);
		// Totale Spesa Conto Capitale
		CellStyle cellStyleDescTotaleSpesaContoCapitale = sheet.getWorkbook().createCellStyle();
		cellStyleDescTotaleSpesaContoCapitale.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDescTotaleSpesaContoCapitale = sheet.getWorkbook().createFont();
		fontDescTotaleSpesaContoCapitale.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDescTotaleSpesaContoCapitale.setFontHeightInPoints((short) 10);
		fontDescTotaleSpesaContoCapitale.setFontName(HSSFFont.FONT_ARIAL);
		fontDescTotaleSpesaContoCapitale.setItalic(true);
		cellStyleDescTotaleSpesaContoCapitale.setFont(fontDescTotaleSpesaContoCapitale);
		cellStyleDescTotaleSpesaContoCapitale.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		cellStyleDescTotaleSpesaContoCapitale.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleDescTotaleSpesaContoCapitale.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescTotaleSpesaContoCapitale.setWrapText(true);
		cellStyleDescTotaleSpesaContoCapitale.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDescTotaleSpesaContoCapitale.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDescTotaleSpesaContoCapitale.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleDescTotaleSpesaContoCapitale.setBorderBottom(CellStyle.BORDER_THIN);
		// Totale Spesa Incremento Attivita Finanziaria
		CellStyle cellStyleDescTotaleIncrementoAttivitaFinanziaria = sheet.getWorkbook().createCellStyle();
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDescTotaleIncrementoAttivitaFinanziaria = sheet.getWorkbook().createFont();
		fontDescTotaleIncrementoAttivitaFinanziaria.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDescTotaleIncrementoAttivitaFinanziaria.setFontHeightInPoints((short) 8);
		fontDescTotaleIncrementoAttivitaFinanziaria.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setFont(fontDescTotaleIncrementoAttivitaFinanziaria);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria
				.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setWrapText(true);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleDescTotaleIncrementoAttivitaFinanziaria.setBorderBottom(CellStyle.BORDER_THIN);
		// Totale Missione
		CellStyle cellStyleDescTotaleMissione = sheet.getWorkbook().createCellStyle();
		cellStyleDescTotaleMissione.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontDescTotaleMissione = sheet.getWorkbook().createFont();
		fontDescTotaleMissione.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontDescTotaleMissione.setFontHeightInPoints((short) 12);
		fontDescTotaleMissione.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleDescTotaleMissione.setFont(fontDescTotaleMissione);
		cellStyleDescTotaleMissione.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		cellStyleDescTotaleMissione.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleDescTotaleMissione.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescTotaleMissione.setWrapText(true);
		cellStyleDescTotaleMissione.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleDescTotaleMissione.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleDescTotaleMissione.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleDescTotaleMissione.setBorderBottom(CellStyle.BORDER_THIN);

		List<ModelMsgInformativo> listaDescTotaliMissione = listeService
				.findMsgInformativi(SharedConstants.SECTION_MODB_DESC_TOTALI_MISSIONE);
		// Mappatura Colori e Stile Totali Missione
		Map<String, CellStyle> descTotaliMissioneStyles = new HashMap<String, CellStyle>();
		descTotaliMissioneStyles.put(listaDescTotaliMissione.get(0).getCodMsgInformativo(),
				cellStyleDescTotaleSpesaCorrente);
		descTotaliMissioneStyles.put(listaDescTotaliMissione.get(1).getCodMsgInformativo(),
				cellStyleDescTotaleSpesaContoCapitale);
		descTotaliMissioneStyles.put(listaDescTotaliMissione.get(2).getCodMsgInformativo(),
				cellStyleDescTotaleIncrementoAttivitaFinanziaria);
		descTotaliMissioneStyles.put(listaDescTotaliMissione.get(3).getCodMsgInformativo(),
				cellStyleDescTotaleMissione);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_B);
		List<ModelMsgInformativo> listaMsgInformativi = listeService
				.findMsgInformativi(SharedConstants.SECTION_MODB_DESC_TOTALI);

		// RIGA 0
		Cell cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);
		// RIGA 1
		columnCount = 0;
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
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
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
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
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
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyleTitoloModelMissioni);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGHE DESC TOTALI
		for (int i = 0; i < 4; i++) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellStyle(cellStyleVociTotali);
			if (i == 0) {
				cell.setCellValue((String) listaMsgInformativi.get(i).getTestoMsgInformativo()
						+ (String) listaMsgInformativi.get(i + 1).getTestoMsgInformativo());
			}
			if (i == 1) {
				cell.setCellValue((String) listaMsgInformativi.get(i + 1).getTestoMsgInformativo() + " "
						+ (String) listaMsgInformativi.get(i + 2).getTestoMsgInformativo()
						+ (String) listaMsgInformativi.get(i + 3).getTestoMsgInformativo());
			}
			if (i == 2) {
				cell.setCellValue((String) listaMsgInformativi.get(i + 3).getTestoMsgInformativo());
			}
			if (i == 3) {
				cell.setCellValue((String) listaMsgInformativi.get(i + 3).getTestoMsgInformativo());
			}
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyleVociTotali);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyleVociTotali);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			cell = row.createCell(++columnCount);
			if (i == 0) {
				if (body.getTotaleSpesaTutteMissioni() != null) {
					cell.setCellValue(
							(double) Double.valueOf(Util.convertBigDecimalToString(body.getTotaleSpesaTutteMissioni())
									.replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				}
			}
			if (i == 1) {
				if (body.getTotaleSpesaCorrenteMis1204() != null) {
					cell.setCellValue(
							(double) Double.valueOf(Util.convertBigDecimalToString(body.getTotaleSpesaCorrenteMis1204())
									.replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				}
			}
			if (i == 2) {
				if (body.getTotaleSpesaCorrenteMis01041215() != null) {
					cell.setCellValue((double) Double
							.valueOf(Util.convertBigDecimalToString(body.getTotaleSpesaCorrenteMis01041215())
									.replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				}
			}
			if (i == 3) {
				if (body.getTotaleSpesaCorrenteTutteMissioni() != null) {
					cell.setCellValue((double) Double
							.valueOf(Util.convertBigDecimalToString(body.getTotaleSpesaCorrenteTutteMissioni())
									.replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				}
			}
			if (i == 0 || i == 2) {
				cell.setCellStyle(cellStyleValueGrey);
				cellStyleValueGrey.setDataFormat(format.getFormat(pattern));
			} else {
				cell.setCellStyle(cellStyleValueGreen);
				cellStyleValueGreen.setDataFormat(format.getFormat(pattern));
			}
			cell = row.createCell(++columnCount);
			row.createCell(++columnCount);
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount - 3, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// MISSIONI
		int currentMission = 0;
		for (ModelBMissioni missione : body.getDatiB().getListaMissioni()) {
			int initRowCount = rowCount;
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			if (missione.getCodMissione().equals("20") || missione.getCodMissione().equals("50")
					|| missione.getCodMissione().equals("60") || missione.getCodMissione().equals("99")
					|| missione.getCodMissione().equals("ALT")) {
				row.setHeight((short) (row.getHeight() * 2));
			}
			cell = row.createCell(columnCount);
			cell.setCellValue((String) missione.getDescMissione());
			cell.setCellStyle(cellStyleTitoloModelMissioni);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyleTitoloModelMissioni);
			row.createCell(++columnCount);

			if (missione.getCodMissione().equals("ALT")) {
				cell = row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);

				cell = row.createCell(++columnCount);
				cell.setCellValue((String) "Conforme rendiconto della gestione: "
						+ (missione.getListaProgramma().get(0).isFlagConf() ? (String) "NO" : (String) "SI"));
				cell.setCellStyle(cellStyleConforme);
				cell.getCellStyle().setWrapText(true);

				cell = row.createCell(++columnCount);
				cell.setCellValue((String) "Motivazione se non conforme:");
				cell.getCellStyle().setWrapText(true);
				cell.setCellStyle(cellStyleDescMotivazione);

				cell = row.createCell(++columnCount);
				cell.setCellValue((String) missione.getListaProgramma().get(0).getMotivazione());
				cell.getCellStyle().setWrapText(true);
				cell.setCellStyle(cellStyleTestoMotivazione);

				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			} else {
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				if (!missione.getCodMissione().equals("ALT")) {
					cell.setCellValue((String) missione.getAltraDescMissione());
				}
				cell.setCellStyle(cellStyleTitoloModelMissioni);
				cell.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
				cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
				cell.getCellStyle().setFillPattern(CellStyle.SOLID_FOREGROUND);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			}

			// Programmi
			for (ModelBProgramma programma : missione.getListaProgramma()) {

				if (programma.getListaTitolo().size() > 0) {
					if (!missione.getCodMissione().equals("ALT")) {
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						row.setHeight((short) (row.getHeight() * 2));
						cell = row.createCell(columnCount);
						if (!(missione.getCodMissione().equals("ALT")
								&& programma.getCodProgramma().equals("ALTALT"))) {
							cell.setCellValue((String) programma.getDescProgramma());
						}
						if (missione.getCodMissione().equals("12")) {
							cell.setCellStyle(programmaMissioneStyles.get(programma.getCodProgramma() + "_DESC"));
						} else {
							cell.setCellStyle(cellStyleVoceTabellaWithBorderDouble);
						}
						cell = row.createCell(++columnCount);
						if (!(missione.getCodMissione().equals("ALT")
								&& programma.getCodProgramma().equals("ALTALT"))) {
							cell.setCellValue((String) programma.getInformativa());
						}
						if (missione.getCodMissione().equals("12")) {
							cell.setCellStyle(programmaMissioneStyles.get(programma.getCodProgramma() + "_INFO"));
						} else if (missione.getCodMissione().equals("15") || missione.getCodMissione().equals("20")
								|| missione.getCodMissione().equals("50") || missione.getCodMissione().equals("60")
								|| missione.getCodMissione().equals("99") || missione.getCodMissione().equals("ALT")) {
							cell.setCellStyle(cellStyleVoceOnlyBorderDoubleWithBold);
						} else {
							cell.setCellStyle(cellStyleVoceOnlyBorderDouble);
						}
						cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_CENTER);
						cell = row.createCell(++columnCount);
						row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
						sheet.addMergedRegion(cellRangeAddress);

						cell = row.createCell(++columnCount);
						cell.setCellValue((String) "Conforme rendiconto della gestione: "
								+ (programma.isFlagConf() ? (String) "NO" : (String) "SI"));
						cell.setCellStyle(cellStyleConforme);
						cell.getCellStyle().setWrapText(true);

						cell = row.createCell(++columnCount);
						cell.setCellValue((String) "Motivazione se non conforme:");
						cell.getCellStyle().setWrapText(true);
						cell.setCellStyle(cellStyleDescMotivazione);

						cell = row.createCell(++columnCount);
						cell.setCellValue((String) programma.getMotivazione());
						cell.getCellStyle().setWrapText(true);
						cell.setCellStyle(cellStyleTestoMotivazione);
					}

					// Titoli
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellValue((String) titolo.getDescTitolo());
						if (missione.getCodMissione().equals("ALT")
								&& (titolo.getCodTitolo().equals("3") || titolo.getCodTitolo().equals("4")
										|| titolo.getCodTitolo().equals("5") || titolo.getCodTitolo().equals("7"))) {
							cell.setCellStyle(cellStyleTitolo3Programma);
						} else {
							cell.setCellStyle(titolo.getCodTitolo().equals("1") ? cellStyleTitolo1Programma
									: titolo.getCodTitolo().equals("2") ? cellStyleTitolo2Programma
											: titolo.getCodTitolo().equals("3") ? cellStyleTitolo3Programma
													: cellStyleTitolo1Programma);
						}
						cell = row.createCell(++columnCount);
						cell.setCellValue((String) titolo.getAltraDescTitolo());
						if (missione.getCodMissione().equals("15") || missione.getCodMissione().equals("20")
								|| missione.getCodMissione().equals("50") || missione.getCodMissione().equals("60")
								|| missione.getCodMissione().equals("99") || missione.getCodMissione().equals("ALT")) {
							if (missione.getCodMissione().equals("ALT") && (titolo.getCodTitolo().equals("3")
									|| titolo.getCodTitolo().equals("4") || titolo.getCodTitolo().equals("5")
									|| titolo.getCodTitolo().equals("7"))) {
								cell.setCellStyle(cellStyleTitolo3Programma);
							} else {
								if (titolo.getCodTitolo().equals("1")) {
									cell.setCellStyle(cellStyleTitoloAltro1ProgrammaNoBold);
								} else {
									cell.setCellStyle(titolo.getCodTitolo().equals("2") ? cellStyleTitoloAltro2Programma
											: titolo.getCodTitolo().equals("3") ? cellStyleTitoloAltro3Programma
													: cellStyleTitoloAltroProgramma);
								}
							}
						} else {
							cell.setCellStyle(titolo.getCodTitolo().equals("1") ? cellStyleTitoloAltro1Programma
									: titolo.getCodTitolo().equals("2") ? cellStyleTitoloAltro2Programma
											: titolo.getCodTitolo().equals("3") ? cellStyleTitoloAltro3Programma
													: cellStyleTitoloAltroProgramma);
						}
						cell = row.createCell(++columnCount);
						if (titolo.getValore() != null) {
							cell.setCellValue((double) Double.valueOf(Util.convertBigDecimalToString(titolo.getValore())
									.replaceAll("\\.", "").replace(",", ".")));
						}
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						if (missione.getCodMissione().equals("12")
								&& (programma.getCodProgramma().equals("1201")
										|| programma.getCodProgramma().equals("1202"))
								&& titolo.getCodTitolo().equals("1")) {
							cell.setCellStyle(cellStyleValueWhiteWithBorderRight);
							cellStyleValueWhiteWithBorderRight.setDataFormat(format.getFormat(pattern));
						} else if (missione.getCodMissione().equals("12")
								&& (programma.getCodProgramma().equals("1203")
										|| programma.getCodProgramma().equals("1204")
										|| programma.getCodProgramma().equals("1205")
										|| programma.getCodProgramma().equals("1206")
										|| programma.getCodProgramma().equals("1207")
										|| programma.getCodProgramma().equals("1208"))
								&& titolo.getCodTitolo().equals("1")) {
							cell.setCellStyle(cellStyleValueGreenWithBorderRight);
							cellStyleValueGreenWithBorderRight.setDataFormat(format.getFormat(pattern));
						} else {
							cell.setCellStyle(cellStyleValueWhite);
						}
						cellStyleValueWhite.setDataFormat(format.getFormat(pattern));

						// Sottotitoli
						for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
							columnCount = 0;
							row = sheet.createRow(++rowCount);
							cell = row.createCell(columnCount);
							if (!(missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
									&& titolo.getCodTitolo().equals("1"))) {
								cell.setCellValue((String) "di cui " + titolo.getDescTitolo());
							}
							if (missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
									&& titolo.getCodTitolo().equals("1")) {
								row.setHeight((short) (row.getHeight() * 2));
							}
							cell.setCellStyle(cellStyleDicuiTitoloProgramma);
							cell = row.createCell(++columnCount);
							cell.setCellValue((String) sottotitolo.getDescSottotitolo());
							if (missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
									&& titolo.getCodTitolo().equals("1")
									&& sottotitolo.getCodSottotitolo().equals("11")) {
								cell.setCellStyle(cellStyleDicuiAltroTit1Prog06Mis04Yellow);
							} else if (missione.getCodMissione().equals("04")
									&& programma.getCodProgramma().equals("0406") && titolo.getCodTitolo().equals("1")
									&& sottotitolo.getCodSottotitolo().equals("12")) {
								cell.setCellStyle(cellStyleDicuiAltroTit1Prog06Mis04Green);
							} else {
								cell.setCellStyle(cellStyleDicuiAltroTitoloProgramma);
							}
							cell.getCellStyle().setWrapText(true);
							cell = row.createCell(++columnCount);
							if (sottotitolo.getValore() != null) {
								cell.setCellValue(
										(double) Double.valueOf(Util.convertBigDecimalToString(sottotitolo.getValore())
												.replaceAll("\\.", "").replace(",", ".")));
							}
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell.setCellStyle(cellStyleValueWhite);
							cellStyleValueWhite.setDataFormat(format.getFormat(pattern));
						}
					}
				}
			}

			// Bordo Double Missione
			cellRangeAddress = new CellRangeAddress(initRowCount + 2, rowCount, 0, 1);
			RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());

			// Totali Missione
			for (ModelMsgInformativo descTotale : listaDescTotaliMissione) {
				// Missione 20 Senza Totale Incremento Attivita Finanziarie
				if (missione.getCodMissione().equals("20")) {
					if (!descTotale.getCodMsgInformativo().equals("63")) {
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellStyle(descTotaliMissioneStyles.get(descTotale.getCodMsgInformativo()));
						cell.setCellValue(
								(String) missione.getDescMissione() + " " + descTotale.getTestoMsgInformativo());
						cell = row.createCell(++columnCount);
						cell.setCellStyle(descTotaliMissioneStyles.get(descTotale.getCodMsgInformativo()));
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell = row.createCell(++columnCount);

						if (descTotale.getCodMsgInformativo().equals("61")) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleSpeseCorrenti()
									.get(currentMission).replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						} else if (descTotale.getCodMsgInformativo().equals("62")) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleSpeseContoCapitale()
									.get(currentMission).replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						} else if (descTotale.getCodMsgInformativo().equals("64")) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleMissione().get(currentMission)
									.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						if (missione.getCodMissione().equals("12") && descTotale.getCodMsgInformativo().equals("61")) {
							cell.setCellStyle(cellStyleValueGreenWithBorderRight);
						} else {
							cell.setCellStyle(cellStyleTotaliMissione);
						}
						cellStyleTotaliMissione.setDataFormat(format.getFormat(pattern));
					}
				}
				// Missionei 50, 60, 99, ALTRE solo Totale Missione
				else if (missione.getCodMissione().equals("50") || missione.getCodMissione().equals("60")
						|| missione.getCodMissione().equals("99") || missione.getCodMissione().equals("ALT")) {
					if (descTotale.getCodMsgInformativo().equals("64")) {
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellStyle(descTotaliMissioneStyles.get(descTotale.getCodMsgInformativo()));
						cell.setCellValue(
								(String) missione.getDescMissione() + " " + descTotale.getTestoMsgInformativo());
						cell = row.createCell(++columnCount);
						cell.setCellStyle(descTotaliMissioneStyles.get(descTotale.getCodMsgInformativo()));
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell = row.createCell(++columnCount);

						cell.setCellValue((double) Double.valueOf(body.getRowTotaleMissione().get(currentMission)
								.replaceAll("\\.", "").replace(",", ".")));
						// cell.setCellValue((double)
						// Double.valueOf(Util.convertBigDecimalToString(BigDecimal.ONE).replaceAll("\\.",
						// "").replace(",", ".")));
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);

						if (missione.getCodMissione().equals("12") && descTotale.getCodMsgInformativo().equals("61")) {
							cell.setCellStyle(cellStyleValueGreenWithBorderRight);
						} else {
							cell.setCellStyle(cellStyleTotaliMissione);
						}
						cellStyleTotaliMissione.setDataFormat(format.getFormat(pattern));

					}
				} else {
					columnCount = 0;
					row = sheet.createRow(++rowCount);
					cell = row.createCell(columnCount);
					cell.setCellStyle(descTotaliMissioneStyles.get(descTotale.getCodMsgInformativo()));
					cell.setCellValue((String) missione.getDescMissione() + " " + descTotale.getTestoMsgInformativo());
					cell = row.createCell(++columnCount);
					cell.setCellStyle(descTotaliMissioneStyles.get(descTotale.getCodMsgInformativo()));
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
					sheet.addMergedRegion(cellRangeAddress);
					cell = row.createCell(++columnCount);

					if (descTotale.getCodMsgInformativo().equals("61")) {
						if (body.getRowTotaleSpeseCorrenti().get(currentMission) != null) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleSpeseCorrenti()
									.get(currentMission).replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					} else if (descTotale.getCodMsgInformativo().equals("62")) {
						if (body.getRowTotaleSpeseContoCapitale().get(currentMission) != null) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleSpeseContoCapitale()
									.get(currentMission).replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					} else if (descTotale.getCodMsgInformativo().equals("63")) {
						if (body.getRowTotaleSpeseIncrementoAttFinanz().get(currentMission) != null) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleSpeseIncrementoAttFinanz()
									.get(currentMission).replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					} else if (descTotale.getCodMsgInformativo().equals("64")) {
						if (body.getRowTotaleMissione().get(currentMission) != null) {
							cell.setCellValue((double) Double.valueOf(body.getRowTotaleMissione().get(currentMission)
									.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					}

					if (missione.getCodMissione().equals("12") && descTotale.getCodMsgInformativo().equals("61")) {
						cell.setCellStyle(cellStyleValueGreenWithBorderRight);
					} else {
						cell.setCellStyle(cellStyleTotaliMissione);
					}
					cellStyleTotaliMissione.setDataFormat(format.getFormat(pattern));
				}
			}
			// Bordo Double Totale Spesa Missione
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, 1);
			RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());

			currentMission++;
		}

		// Bordo Esterno Documento Finale
		Integer rowsSheet = sheet.getWorkbook().getSheetAt(0).getPhysicalNumberOfRows();
		cellRangeAddress = new CellRangeAddress(3, rowsSheet - 1, 0, 6);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		Util.autoSizeColumns(sheet.getWorkbook());
		// Set dimensione colonne
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 45 * 256);
		sheet.setColumnWidth(2, 25 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 15 * 256);
		sheet.setColumnWidth(5, 15 * 256);
		sheet.setColumnWidth(6, 15 * 256);
		sheet.getRow(5).setHeight((short) (row.getHeight() * 2));
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloB_" + nomefile, ".xls");
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

	@Transactional
	public GenericResponseWarnErr checkModelloB(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());
		response.setObblMotivazione(false);
		boolean presenzaDisabilitAttuale = false;
		boolean presenzaDisabilitPassata = false;
		boolean presenzaStranieriAttuale = false;
		boolean presenzaStranieriPassata = false;
		GregTRendicontazioneEnte rendicontazioneAttuale = datiRendicontazioneService
				.getRendicontazione(idRendicontazione);
		GregTRendicontazioneEnte rendicontazionePassata = datiRendicontazioneService.getRendicontazionePassata(
				rendicontazioneAttuale.getGregTSchedeEntiGestori().getIdSchedaEnteGestore(),
				(rendicontazioneAttuale.getAnnoGestione() - 1));
		if (rendicontazionePassata != null) {
			ModelRendicontazioneModB datiModelloAttuale = getRendicontazioneModB(
					rendicontazioneAttuale.getIdRendicontazioneEnte());
			ModelRendicontazioneModB datiModelloPassata = getRendicontazioneModB(
					rendicontazionePassata.getIdRendicontazioneEnte());

			BigDecimal totaleTutteMissioniAttuale = BigDecimal.ZERO;
			BigDecimal totaleTutteMissioniPassata = BigDecimal.ZERO;
			BigDecimal totaleMissione12e04Attuale = BigDecimal.ZERO;
			BigDecimal totaleMissione12e04Passata = BigDecimal.ZERO;
			BigDecimal totaleMissione01041215Attuale = BigDecimal.ZERO;
			BigDecimal totaleMissione01041215Passata = BigDecimal.ZERO;

			for (ModelBMissioni missione : datiModelloAttuale.getListaMissioni()) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						if (titolo.getValore() != null && !titolo.getValore().equals(BigDecimal.ZERO)) {
							if (missione.getCodMissione().equals("01") || missione.getCodMissione().equals("04")
									|| missione.getCodMissione().equals("12")
									|| missione.getCodMissione().equals("15")) {
								totaleMissione01041215Attuale = totaleMissione01041215Attuale.add(titolo.getValore());
							}
							if (missione.getCodMissione().equals("12") || (missione.getCodMissione().equals("04")
									&& programma.getCodProgramma().equals("0406")
									&& titolo.getCodTitolo().equals("1"))) {
								totaleMissione12e04Attuale = totaleMissione12e04Attuale.add(titolo.getValore());
							}
							totaleTutteMissioniAttuale = totaleTutteMissioniAttuale.add(titolo.getValore());
						}
						for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
							if (sottotitolo.getValore() != null && !sottotitolo.getValore().equals(BigDecimal.ZERO)) {
								if (missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
										&& titolo.getCodTitolo().equals("1")
										&& sottotitolo.getCodSottotitolo().equals("11")) {
									presenzaStranieriAttuale = true;
								}
								if (missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
										&& titolo.getCodTitolo().equals("1")
										&& sottotitolo.getCodSottotitolo().equals("12")) {
									presenzaDisabilitAttuale = true;
								}
							}
						}
					}
				}
			}

			for (ModelBMissioni missione : datiModelloPassata.getListaMissioni()) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						if (titolo.getValore() != null) {
							if (missione.getCodMissione().equals("01") || missione.getCodMissione().equals("04")
									|| missione.getCodMissione().equals("12")
									|| missione.getCodMissione().equals("15")) {
								totaleMissione01041215Passata = totaleMissione01041215Passata.add(titolo.getValore());
							}
							if (missione.getCodMissione().equals("12") || (missione.getCodMissione().equals("04")
									&& programma.getCodProgramma().equals("0406")
									&& titolo.getCodTitolo().equals("1"))) {
								totaleMissione12e04Passata = totaleMissione12e04Passata.add(titolo.getValore());
							}
							totaleTutteMissioniPassata = totaleTutteMissioniPassata.add(titolo.getValore());
						}
						for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
							if (sottotitolo.getValore() != null && !sottotitolo.getValore().equals(BigDecimal.ZERO)) {
								if (missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
										&& titolo.getCodTitolo().equals("1")
										&& sottotitolo.getCodSottotitolo().equals("11")) {
									presenzaStranieriPassata = true;
								}
								if (missione.getCodMissione().equals("04") && programma.getCodProgramma().equals("0406")
										&& titolo.getCodTitolo().equals("1")
										&& sottotitolo.getCodSottotitolo().equals("12")) {
									presenzaDisabilitPassata = true;
								}
							}
						}
					}
				}
			}

			BigDecimal totaleTutteMissioni25 = (totaleTutteMissioniPassata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMissione12e0425 = (totaleMissione12e04Passata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMissione0104121525 = (totaleMissione01041215Passata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);

			if ((totaleTutteMissioniAttuale.setScale(2))
					.compareTo(totaleTutteMissioniPassata.subtract(totaleTutteMissioni25).setScale(2)) < 0
					|| (totaleTutteMissioniAttuale.setScale(2))
							.compareTo(totaleTutteMissioniPassata.add(totaleTutteMissioni25).setScale(2)) > 0) {
				if (((totaleTutteMissioniAttuale.subtract(totaleTutteMissioniPassata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B01).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleTutteMissioniAttuale))
									.replace("DATOPASSATO",
											Util.convertBigDecimalToString(totaleTutteMissioniPassata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMissione12e04Attuale.setScale(2))
					.compareTo(totaleMissione12e04Passata.subtract(totaleMissione12e0425).setScale(2)) < 0
					|| (totaleMissione12e04Attuale.setScale(2))
							.compareTo(totaleMissione12e04Passata.add(totaleMissione12e0425).setScale(2)) > 0) {
				if (((totaleMissione12e04Attuale.subtract(totaleMissione12e04Passata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B02).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissione12e04Attuale))
									.replace("DATOPASSATO",
											Util.convertBigDecimalToString(totaleMissione12e04Passata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMissione01041215Attuale.setScale(2))
					.compareTo(totaleMissione01041215Passata.subtract(totaleMissione0104121525).setScale(2)) < 0
					|| (totaleMissione01041215Attuale.setScale(2))
							.compareTo(totaleMissione01041215Passata.add(totaleMissione0104121525).setScale(2)) > 0) {
				if (((totaleMissione01041215Attuale.subtract(totaleMissione01041215Passata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B03)
							.getTestoMessaggio()
							.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissione01041215Attuale))
							.replace("DATOPASSATO", Util.convertBigDecimalToString(totaleMissione01041215Passata)));
					response.setObblMotivazione(true);
				}
			}

			if ((presenzaDisabilitAttuale && !presenzaDisabilitPassata)
					|| (!presenzaDisabilitAttuale && presenzaDisabilitPassata)) {
				if (presenzaDisabilitPassata) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B06).getTestoMessaggio());
				} else {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B07).getTestoMessaggio());
				}

				response.setObblMotivazione(true);
			}
			if ((presenzaStranieriAttuale && !presenzaStranieriPassata)
					|| (!presenzaStranieriAttuale && presenzaStranieriPassata)) {
				if (presenzaStranieriPassata) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B04).getTestoMessaggio());
				} else {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B05).getTestoMessaggio());
				}
				response.setObblMotivazione(true);
			}

			if (response.getWarnings().size() > 0) {
				List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_B,
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
		ModelStatoMod stato = modelloBDao.getStatoModelloB(idRendicontazione);
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
	public GenericResponseWarnErr controlloModelloB(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_B);
		boolean facoltativo = false;
		boolean valorizzato = modelloBDao.getValorizzatoModelloB(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors()
					.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
							.replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		// Controlli Modello B
		ModelRendicontazioneModB rendModB = getRendicontazioneModB(rendicontazione.getIdRendicontazioneEnte());
		ModelRendicontazioneTotaliMacroaggregati totaliMacroaggregati = macroaggregatiService
				.getRendicontazioneTotaliMacroaggregatiPerB1(rendicontazione.getIdRendicontazioneEnte());
		ModelRendicontazioneTotaliSpeseMissioni totaliSpeseMissione = macroaggregatiService
				.getRendicontazioneTotaliSpesePerB(rendicontazione.getIdRendicontazioneEnte());
		List<GregRRendicontazioneModAPart1> rendModA = modelloADao
				.findAllValoriModAByEnte(rendicontazione.getIdRendicontazioneEnte());
		BigDecimal totaleTitolo2e3 = BigDecimal.ZERO.setScale(2);
		BigDecimal totaleMissione01041215 = BigDecimal.ZERO.setScale(2);
		String errorMessageB = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_02)
				.getTestoMessaggio();
		// Controllo Totale Titolo 02 e 03 ModA e totale Missione 01, 04, 12, 15 modB
		for (GregRRendicontazioneModAPart1 rend : rendModA) {
			if (rend.getGregRTitoloTipologiaVoceModA().getGregDTitoloModA().getCodTitoloModA().equals("02")
					|| rend.getGregRTitoloTipologiaVoceModA().getGregDTitoloModA().getCodTitoloModA().equals("03")) {
				totaleTitolo2e3 = totaleTitolo2e3.add(rend.getValoreNumb().setScale(2));
			}
		}
		for (ModelBMissioni missione : rendModB.getListaMissioni()) {
			if (missione.getCodMissione().equals("01") || missione.getCodMissione().equals("04")
					|| missione.getCodMissione().equals("12") || missione.getCodMissione().equals("15")) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						if (titolo.getCodTitolo().equals("1")) {
							if (titolo.getValore() != null) {
								totaleMissione01041215 = totaleMissione01041215.add(titolo.getValore().setScale(2));
							}
						}
					}
				}
			}
		}
		BigDecimal diff = totaleTitolo2e3.subtract(totaleMissione01041215).abs().setScale(2);
		if (diff.compareTo(new BigDecimal(100000).setScale(2)) > 0) {
			BigDecimal fraz = diff.divide(totaleTitolo2e3, 2, RoundingMode.UP).setScale(2);
			float r = (float) 0.20;
			BigDecimal res = new BigDecimal(r).setScale(2, RoundingMode.HALF_DOWN);
			if (fraz.compareTo(res) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.WARNING_MOD_B_03).getTestoMessaggio());
				response.setObblMotivazione(true);
			}
		}

		// Controllo missione Altro
		GregRRendicontazioneModAPart1 titolo2 = datiRendicontazioneDao.getRendicontazioneModAByIdRendicontazione(
				rendicontazione.getIdRendicontazioneEnte(), "02-ALTRO", "Tipo_altro", "31");
		BigDecimal totaleAltro = BigDecimal.ZERO.setScale(2);
		for (ModelBMissioni missione : rendModB.getListaMissioni()) {
			if (missione.getCodMissione().equals("ALT")) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						if (titolo.getValore() != null) {
							totaleAltro = totaleAltro.add(titolo.getValore().setScale(2));
						}
					}
				}
			}
		}
		if (!facoltativo) {
			if (titolo2 != null) {
				if (titolo2.getValoreNumb() != null && !titolo2.getValoreNumb().equals(BigDecimal.ZERO)) {
					if (totaleAltro == null || totaleAltro.equals(BigDecimal.ZERO.setScale(2))) {
						response.getErrors().add(
								listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIB_01).getTestoMessaggio());
					}
				}
			}
		}
		// CONTROLLO 1, Verifica TotaleSpesaCorrenteMis12 e Mis04(dicui)
		// prelevo gli importi di cui, Missione 04 Programma 06 Titolo 1
		BigDecimal sommaDicuiMiss04Prog06Tit1 = BigDecimal.ZERO.setScale(2);
		for (ModelBMissioni missione : rendModB.getListaMissioni()) {
			if (missione.getCodMissione().equals("04")) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					if (programma.getCodProgramma().equals("0406")) {
						for (ModelBTitolo titolo : programma.getListaTitolo()) {
							if (titolo.getCodTitolo().equals("1")) {
								for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
									if (sottotitolo.getCodSottotitolo().equals("11")
											|| sottotitolo.getCodSottotitolo().equals("12")) {
										if (sottotitolo.getValore() != null) {
											sommaDicuiMiss04Prog06Tit1 = sommaDicuiMiss04Prog06Tit1
													.add(sottotitolo.getValore());
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// Calcolo Totale Missione 12, partendo dagli importi del Modello B1 (devo poi
		// sottrarre i due di cui come come nella webapp per Prog1/Tit1 e Prog2/Tit1)
		BigDecimal totaleMissione12 = BigDecimal.ZERO.setScale(2);
		List<ModelB1ProgrammiMissioneTotali> importiMissione12FromModB1 = modelloB1Service
				.getProgrammiMissioneTotali(rendicontazione.getIdRendicontazioneEnte());
		for (ModelB1ProgrammiMissioneTotali importoMissione : importiMissione12FromModB1) {
			if (importoMissione.getValore() != null) {
				totaleMissione12 = totaleMissione12.add(importoMissione.getValore());
			}
		}
		totaleMissione12 = totaleMissione12.subtract(sommaDicuiMiss04Prog06Tit1).setScale(2, RoundingMode.HALF_UP);

		// Calcolo il TotaleSpesaCorrenteMis12 e lo confronto con il
		// TotaleMacroaggregati
		BigDecimal totaleSpesaCorrenteMis12Mis04Dicui = BigDecimal.ZERO.setScale(2);
		totaleSpesaCorrenteMis12Mis04Dicui = totaleMissione12.add(sommaDicuiMiss04Prog06Tit1);
		// Prelevo il totale Macroaggregati
		BigDecimal totaleMissioniMacro = BigDecimal.ZERO.setScale(2);
		totaleMissioniMacro = totaleMissioniMacro.add(macroaggregatiService
				.getRendicontazioneTotaliMacroaggregatiPerB1(rendicontazione.getIdRendicontazioneEnte())
				.getValoriMacroaggregati().get(7).getTotale());
		// verifico che il totaleMissioni Macro sia uguale al (totaleSpesaCorrenteMis12
		// + di cui Miss04)
		if (!facoltativo) {
			if (!totaleSpesaCorrenteMis12Mis04Dicui.equals(totaleMissioniMacro)) {
				String errMsgB1 = errorMessageB;
				response.getErrors().add(errMsgB1.replace("QUALEIMPORTO",
						"TOTALE SPESA CORRENTE della MISSIONE 12 E della MISSIONE 04 (solo la parte concernente il Sostegno Socio Educativo Scolastico per minori stranieri e minori disabili - compreso Trasporto scolastico Disabili)")
						.replace("IMPORTO",
								Util.convertBigDecimalToString(
										totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale()))
						.replace("MODELLO", "Macroaggregati"));
			}
		}
		// CONTROLLO 2, Verifico TotaleSpesaCorrenteTutteMissioni
		BigDecimal totaleSpesaCorrenteTutteMissioni = BigDecimal.ZERO.setScale(2);
		for (ModelBMissioni missione : rendModB.getListaMissioni()) {
			for (ModelBProgramma programma : missione.getListaProgramma()) {
				for (ModelBTitolo titolo : programma.getListaTitolo()) {
					if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
						totaleSpesaCorrenteTutteMissioni = totaleSpesaCorrenteTutteMissioni.add(titolo.getValore());
					}
				}
			}
		}
		BigDecimal totaliTutteMissioniMacroaggregati = totaliSpeseMissione.getValoriSpese().get(3).getTotale()
				.setScale(2);
		if (!facoltativo) {
			if (!totaleSpesaCorrenteTutteMissioni.equals(totaliTutteMissioniMacroaggregati)) {
				String errMsgB2 = errorMessageB;
				response.getErrors().add(errMsgB2.replace("QUALEIMPORTO", "TOTALE SPESA CORRENTE DI TUTTE LE MISSIONI")
						.replace("IMPORTO",
								Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(3).getTotale()))
						.replace("MODELLO", "Macroaggregati"));
			}
		}

		// CONTROLLO 3, Verifica TotaleSpesaCorrenteMissione01
		BigDecimal totaleSpesaCorrenteMissione1 = BigDecimal.ZERO.setScale(2);
		for (ModelBMissioni missione : rendModB.getListaMissioni()) {
			if (missione.getCodMissione().equals("01")) {
				for (ModelBProgramma programma : missione.getListaProgramma()) {
					for (ModelBTitolo titolo : programma.getListaTitolo()) {
						if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
							totaleSpesaCorrenteMissione1 = totaleSpesaCorrenteMissione1.add(titolo.getValore());
						}
					}
				}
			}
		}
		BigDecimal totaleMissione1Macroaggregati = totaliSpeseMissione.getValoriSpese().get(0).getTotale().setScale(2);
		if (!facoltativo) {
			if (!totaleSpesaCorrenteMissione1.equals(totaleMissione1Macroaggregati)) {
				String errMsgB3 = errorMessageB;
				response.getErrors().add(errMsgB3.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 01")
						.replace("IMPORTO",
								Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(0).getTotale()))
						.replace("MODELLO", "Macroaggregati"));
			}

			// CONTROLLO 4, Verifica Missione04Programma06Titolo1
			BigDecimal totaleSpesaMissione = BigDecimal.ZERO.setScale(2);
			totaleSpesaMissione = totaleSpesaMissione
					.add(totaliSpeseMissione.getValoriSpese().get(2).getTotale().setScale(2));
			if (!sommaDicuiMiss04Prog06Tit1.equals(totaleSpesaMissione)) {
				String errMsgB4 = errorMessageB;
				response.getErrors().add(errMsgB4.replace("QUALEIMPORTO", "valore di Missione04/Programma06/Titolo1")
						.replace("IMPORTO",
								Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(2).getTotale()))
						.replace("MODELLO", "Macroaggregati"));
			}

			// CONTROLLO 5, Verifica TotaleSpesaCorrenteMissione12
			BigDecimal totaleSpesaCorrenteMissione12 = BigDecimal.ZERO.setScale(2);
			for (ModelBMissioni missione : rendModB.getListaMissioni()) {
				if (missione.getCodMissione().equals("12")) {
					for (ModelBProgramma programma : missione.getListaProgramma()) {
						for (ModelBTitolo titolo : programma.getListaTitolo()) {
							if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
								totaleSpesaCorrenteMissione12 = totaleSpesaCorrenteMissione12.add(titolo.getValore());
							}
						}
					}
				}

			}
			BigDecimal totaleMissione12Macroaggregati = totaliSpeseMissione.getValoriSpese().get(1).getTotale()
					.setScale(2);
			if (!totaleSpesaCorrenteMissione12.equals(totaleMissione12Macroaggregati)) {
				String errMsgB5 = errorMessageB;
				response.getErrors().add(errMsgB5.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 12")
						.replace("IMPORTO",
								Util.convertBigDecimalToString(totaliSpeseMissione.getValoriSpese().get(1).getTotale()))
						.replace("MODELLO",
								"Macroaggregati (valore Missione 01 - parte spesa socio-assistenziale sommato a valore Missione 12)"));
			}
		}

		return response;

	}

	@Transactional
	public String esportaIstat(EsportaModelloBInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Spesa ISTAT B_B1");
		int rowCount = 0;
		int columnCount = 0;
		Row row = sheet.createRow(rowCount);
		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());

		List<GregDTargetUtenza> utenzeIstat = modelloBDao.findAllTargetIstat(rendicontazione.getAnnoGestione());
		List<EsportaIstatSpese> istatSpese = modelloBDao.findSpeseIstat(rendicontazione.getIdRendicontazioneEnte(),
				utenzeIstat);

		HSSFColor myColor = getColorExcel(workbook, "#E2EFDA");
		short palIndex = myColor.getIndex();

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

		// crea stili arial 11 bold grigio chiaro
		CellStyle cellStyle11bg = sheet.getWorkbook().createCellStyle();
		cellStyle11bg.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle11bg.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle11bg.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font11b = sheet.getWorkbook().createFont();
		font11b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font11b.setFontHeightInPoints((short) 11);
		font11b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle11bg.setFont(font11b);
		cellStyle11bg.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 11 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 11 left
		CellStyle cellStyle11l = sheet.getWorkbook().createCellStyle();
		cellStyle11l.setAlignment(CellStyle.ALIGN_LEFT);
		Font font11 = sheet.getWorkbook().createFont();
		font11.setFontHeightInPoints((short) 11);
		font11.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle11l.setFont(font11);
		cellStyle11l.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 11 right
		CellStyle cellStyle11r = sheet.getWorkbook().createCellStyle();
		cellStyle11r.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle11r.setFont(font11);
		cellStyle11r.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 11 bold center
		CellStyle cellStyle11bgR = sheet.getWorkbook().createCellStyle();
		cellStyle11bgR.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle11bgR.setFont(font11);
		cellStyle11bgR.setFillForegroundColor(palIndex);
		cellStyle11bgR.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle11bgR.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 11 bold center
		CellStyle cellStyle11bgL = sheet.getWorkbook().createCellStyle();
		cellStyle11bgL.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle11bgL.setFont(font11);
		cellStyle11bgL.setFillForegroundColor(palIndex);
		cellStyle11bgL.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle11bgL.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// Row 1
		row = sheet.createRow(rowCount);
		Cell cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);

		columnCount = 0;

		// Row 2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
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
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
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
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// Row 4
		columnCount = 0;
		row = sheet.createRow(++rowCount);

		// Row 5
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellValue("Modello");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Codice Cat. ISTAT");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Descrizione Cat. ISTAT");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Codice. ISTAT");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Descrizione ISTAT");
		cell.setCellStyle(cellStyle11bg);
		for (GregDTargetUtenza utenza : utenzeIstat) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(utenza.getDesUtenza());
			cell.setCellStyle(cellStyle11bg);
		}

		for (EsportaIstatSpese spesa : istatSpese) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellValue(spesa.getModello());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(spesa.getCodiceCatIstat());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(spesa.getDescrizioneCatIstat());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(spesa.getCodiceIstat());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(spesa.getDescrizioneIstat());
			cell.setCellStyle(cellStyle11l);
			for (BigDecimal utenza : spesa.getUtenzeMinisteriali()) {
				cell = row.createCell(++columnCount);
				cell.setCellValue((double) Double.valueOf(Util.convertBigDecimalToString(utenza)
						.replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyle11r);
			}
		}

		Util.autoSizeColumns(sheet.getWorkbook());

		List<GregDTargetUtenza> utenze = modelloBDao.findAllTarget(rendicontazione.getAnnoGestione());

		List<EsportaIstatPreg1Preg2> istatPreg1Preg2 = modelloBDao.findPreg1Preg2Istat(
				rendicontazione.getIdRendicontazioneEnte(), rendicontazione.getAnnoGestione(), utenzeIstat, utenze);

		sheet = workbook.createSheet("Preg1_Preg2_Spesa ISTAT_B1");
		rowCount = 0;
		columnCount = 0;

		// Row 1
		row = sheet.createRow(rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);

		columnCount = 0;

		// Row 2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
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
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
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
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// Row 4
		columnCount = 0;
		row = sheet.createRow(++rowCount);

		// Row 5
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellValue("Codice Preg 1");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Descrizione Preg 1");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Codice Preg 2");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Descrizione Preg 2");
		cell.setCellStyle(cellStyle11bg);
		for (GregDTargetUtenza utenza : utenze) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(utenza.getDesUtenza());
			cell.setCellStyle(cellStyle11bg);
		}
		cell = row.createCell(++columnCount);
		cell.setCellValue("Codice Cat. ISTAT");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Descrizione Cat. ISTAT");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Codice. ISTAT");
		cell.setCellStyle(cellStyle11bg);
		cell = row.createCell(++columnCount);
		cell.setCellValue("Descrizione ISTAT");
		cell.setCellStyle(cellStyle11bg);
		for (GregDTargetUtenza utenza : utenzeIstat) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(utenza.getDesUtenza());
			cell.setCellStyle(cellStyle11bg);
		}

		for (EsportaIstatPreg1Preg2 preg1Preg2 : istatPreg1Preg2) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellValue(preg1Preg2.getCodPreg1());
			cell.setCellStyle(cellStyle11bgL);
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getDesPreg1());
			cell.setCellStyle(cellStyle11bgL);
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getCodPreg2());
			cell.setCellStyle(cellStyle11bgL);
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getDesPreg2());
			cell.setCellStyle(cellStyle11bgL);
			for (BigDecimal utenza : preg1Preg2.getUtenzeRegionali()) {
				cell = row.createCell(++columnCount);
				cell.setCellValue(Util.convertBigDecimalToString(utenza));
				cell.setCellStyle(cellStyle11bgR);
			}
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getCodiceCatIstat());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getDescrizioneCatIstat());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getCodiceIstat());
			cell.setCellStyle(cellStyle11l);
			cell = row.createCell(++columnCount);
			cell.setCellValue(preg1Preg2.getDescrizioneIstat());
			cell.setCellStyle(cellStyle11l);
			for (BigDecimal utenza : preg1Preg2.getUtenzeMinisteriali()) {
				cell = row.createCell(++columnCount);
				cell.setCellValue((double) Double.valueOf(Util.convertBigDecimalToString(utenza)
						.replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyle11r);
			}
		}

		Util.autoSizeColumns(sheet.getWorkbook());

		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloB_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));
	}
}
