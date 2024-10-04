/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ConfiguratorePrestazioniDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloB1Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.entity.GregDMsgInformativo;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRCartaServiziPreg1;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1MacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestReg2;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1ProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg2UtenzeRegionali2;
import it.csi.greg.gregsrv.business.entity.GregRProgrammaMissioneTitSottotit;
import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Macro;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Utereg1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg2Utereg2;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg1;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg2;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloB1Input;
import it.csi.greg.gregsrv.dto.FondiEnteAllontanamentoZero;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelB1Lbl;
import it.csi.greg.gregsrv.dto.ModelB1Macroaggregati;
import it.csi.greg.gregsrv.dto.ModelB1ProgrammiMissioneTotali;
import it.csi.greg.gregsrv.dto.ModelB1Save;
import it.csi.greg.gregsrv.dto.ModelB1Voci;
import it.csi.greg.gregsrv.dto.ModelB1VociPrestReg2;
import it.csi.greg.gregsrv.dto.ModelFondi;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelTotaleMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelTotalePrestazioniB1;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloB1Service")
public class ModelloB1Service {

	@Autowired
	protected ModelloB1Dao modelloB1Dao;
	@Autowired
	protected ModelloAService modelloAService;
	@Autowired
	protected MacroaggregatiService macroaggregatiService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ModelloBDao modelloBDao;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ConfiguratorePrestazioniDao configuratorePrestazioniDao;
	@Autowired
	protected ConfiguratoreUtenzeFnpsService configuratoreUtenzeFnpsService;
	@Autowired
	protected ModelloAllontanamentoZeroService modelloAllontanamentoZeroService;

	public List<ModelB1Lbl> getLblMissioniProgramma() {
		List<GregDProgrammaMissione> missione_programmi = modelloB1Dao.findAllProgrammaMissione();

		List<ModelB1Lbl> listaMissioniProgramma = new ArrayList<ModelB1Lbl>();
		for (GregDProgrammaMissione missione_programma : missione_programmi) {
			ModelB1Lbl lbl = new ModelB1Lbl();
			lbl.setCodice(missione_programma.getCodProgrammaMissione());
			lbl.setDescrizione(missione_programma.getSiglaProgrammaMissione());
			lbl.setColore(missione_programma.getGregDColori().getRgb());
			lbl.setMsgInformativo(missione_programma.getMsgInformativo());
			listaMissioniProgramma.add(lbl);
		}
		return listaMissioniProgramma;
	}

	public List<ModelB1Lbl> getLblMacroaggregati(Integer idRendicontazione) {
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		List<GregTMacroaggregatiBilancio> macroaggregati = modelloB1Dao
				.findAllMacroaggregati(rendicontazione.getAnnoGestione());

		List<ModelB1Lbl> lista = new ArrayList<ModelB1Lbl>();
		for (GregTMacroaggregatiBilancio item : macroaggregati) {
			ModelB1Lbl lbl = new ModelB1Lbl();
			lbl.setCodice(item.getCodMacroaggregatoBilancio());
			lbl.setDescrizione(item.getDesMacroaggregatoBilancio());
			lbl.setColore("");
			lista.add(lbl);
		}
		return lista;
	}

	public List<ModelB1Lbl> getLblMsginformativi() {
		List<GregDMsgInformativo> l_msg = modelloB1Dao.findAllMsgInformativi();

		List<ModelB1Lbl> lista = new ArrayList<ModelB1Lbl>();
		for (GregDMsgInformativo item : l_msg) {
			ModelB1Lbl lbl = new ModelB1Lbl();
			lbl.setCodice(item.getCodMsgInformativo());
			lbl.setDescrizione(item.getTestoMsgInformativo());
			lbl.setColore("");
			lista.add(lbl);
		}
		return lista;
	}

	public List<ModelB1Lbl> getLblUtenze(int id_tipo_flusso, Integer idRendicontazione) {
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		List<GregDTargetUtenza> elenco_lbl = modelloB1Dao.findAllLabelByFlusso(id_tipo_flusso,
				rendicontazione.getAnnoGestione());

		List<ModelB1Lbl> lista = new ArrayList<ModelB1Lbl>();
		for (GregDTargetUtenza item : elenco_lbl) {
			ModelB1Lbl lbl = new ModelB1Lbl();
			lbl.setCodice(item.getCodUtenza());
			lbl.setDescrizione(item.getDesUtenza());
			lbl.setColore("");
			lista.add(lbl);
		}
		return lista;
	}

	@Transactional
	public List<ModelB1ProgrammiMissioneTotali> getProgrammiMissioneTotali(int id_scheda_ente) {
		List<ModelB1ProgrammiMissioneTotali> l_totali = new ArrayList<ModelB1ProgrammiMissioneTotali>();
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(id_scheda_ente);
		BigDecimal totali_pr01 = BigDecimal.ZERO;
		BigDecimal totali_pr02 = BigDecimal.ZERO;
		BigDecimal totali_pr03 = BigDecimal.ZERO;
		BigDecimal totali_pr04 = BigDecimal.ZERO;
		BigDecimal totali_pr05 = BigDecimal.ZERO;
		BigDecimal totali_pr06 = BigDecimal.ZERO;
		BigDecimal totali_pr07 = BigDecimal.ZERO;
		BigDecimal totali_pr08 = BigDecimal.ZERO;

		if (rendicontazione != null) {
			Iterator<GregRRendicontazionePreg1Utereg1> iterator = rendicontazione.getGregRRendicontazionePreg1Utereg1s()
					.iterator();
			while (iterator.hasNext()) {
				GregRRendicontazionePreg1Utereg1 item = iterator.next();
				if (!item.getGregRPrestReg1UtenzeRegionali1().getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
						.isEmpty()) {
					Iterator<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> iterator_pr = item
							.getGregRPrestReg1UtenzeRegionali1().getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
							.iterator();
					GregRPrestReg1UtenzeRegionali1ProgrammaMissione pr_mis = iterator_pr.next();
					Calendar cal = Calendar.getInstance();
					cal.setTime(pr_mis.getDataInizioValidita());
					int annoInizio = cal.get(Calendar.YEAR);
					int annoFine = 0;
					if (pr_mis.getDataFineValidita() != null) {
						cal.setTime(pr_mis.getDataFineValidita());
						annoFine = cal.get(Calendar.YEAR);
					}
					if ((rendicontazione.getAnnoGestione() - annoInizio) >= 0
							&& ((annoFine != 0 && (rendicontazione.getAnnoGestione() - annoFine) <= 0)
									|| pr_mis.getDataFineValidita() == null)) {
						switch (pr_mis.getGregDProgrammaMissione().getCodProgrammaMissione()) {
						case "1201":
							totali_pr01 = totali_pr01 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr01.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1202":
							totali_pr02 = totali_pr02 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr02.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1203":
							totali_pr03 = totali_pr03 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr03.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1204":
							totali_pr04 = totali_pr04 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr04.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1205":
							totali_pr05 = totali_pr05 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr05.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1206":
							totali_pr06 = totali_pr06 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr06.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1207":
							totali_pr07 = totali_pr07 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr07.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;
						case "1208":
							totali_pr08 = totali_pr08 == null
									? BigDecimal.ZERO.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO)
									: totali_pr08.add(item.getValore() != null ? item.getValore() : BigDecimal.ZERO);
							break;

						}
					}
				}
			}
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1201", totali_pr01));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1202", totali_pr02));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1203", totali_pr03));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1204", totali_pr04));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1205", totali_pr05));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1206", totali_pr06));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1207", totali_pr07));
			l_totali.add(new ModelB1ProgrammiMissioneTotali("1208", totali_pr08));
		}
		return l_totali;

	}

	@Transactional
	public List<ModelB1ProgrammiMissioneTotali> getSaveProgrammiMissioneTotali(List<ModelB1Voci> dati) {
		List<ModelB1ProgrammiMissioneTotali> l_totali = new ArrayList<ModelB1ProgrammiMissioneTotali>();
		BigDecimal totali_pr01 = BigDecimal.ZERO;
		BigDecimal totali_pr02 = BigDecimal.ZERO;
		BigDecimal totali_pr03 = BigDecimal.ZERO;
		BigDecimal totali_pr04 = BigDecimal.ZERO;
		BigDecimal totali_pr05 = BigDecimal.ZERO;
		BigDecimal totali_pr06 = BigDecimal.ZERO;
		BigDecimal totali_pr07 = BigDecimal.ZERO;
		BigDecimal totali_pr08 = BigDecimal.ZERO;

		for (ModelB1Voci voce : dati) {
			for (ModelB1UtenzaPrestReg1 utenza : voce.getUtenze()) {
				if (utenza.getCodiceProgrammaMissione() != null) {
					switch (utenza.getCodiceProgrammaMissione()) {
					case "1201":
						totali_pr01 = totali_pr01 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr01.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1202":
						totali_pr02 = totali_pr02 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr02.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1203":
						totali_pr03 = totali_pr03 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr03.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1204":
						totali_pr04 = totali_pr04 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr04.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1205":
						totali_pr05 = totali_pr05 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr05.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1206":
						totali_pr06 = totali_pr06 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr06.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1207":
						totali_pr07 = totali_pr07 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr07.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1208":
						totali_pr08 = totali_pr08 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr08.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;

					}
				}
			}
			for (ModelB1UtenzaPrestReg1 utenza : voce.getUtenzeCostoTotale()) {
				if (utenza.getCodiceProgrammaMissione() != null) {
					switch (utenza.getCodiceProgrammaMissione()) {
					case "1201":
						totali_pr01 = totali_pr01 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr01.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1202":
						totali_pr02 = totali_pr02 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr02.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1203":
						totali_pr03 = totali_pr03 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr03.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1204":
						totali_pr04 = totali_pr04 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr04.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1205":
						totali_pr05 = totali_pr05 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr05.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1206":
						totali_pr06 = totali_pr06 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr06.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1207":
						totali_pr07 = totali_pr07 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr07.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;
					case "1208":
						totali_pr08 = totali_pr08 == null
								? BigDecimal.ZERO.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO)
								: totali_pr08.add(
										utenza.getValore() != null ? Util.convertStringToBigDecimal(utenza.getValore())
												: BigDecimal.ZERO);
						break;

					}
				}
			}
		}

//		if (rendicontazione!=null) {
//			Iterator<GregRRendicontazionePreg1Utereg1> iterator = rendicontazione.getGregRRendicontazionePreg1Utereg1s().iterator();
//			while (iterator.hasNext()) {
//				GregRRendicontazionePreg1Utereg1 item = iterator.next();
//				if (!item.getGregRPrestReg1UtenzeRegionali1().getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().isEmpty()) {
//					Iterator<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> iterator_pr = item.getGregRPrestReg1UtenzeRegionali1().getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().iterator();
//					GregRPrestReg1UtenzeRegionali1ProgrammaMissione pr_mis = iterator_pr.next();
//					switch (pr_mis.getGregDProgrammaMissione().getCodProgrammaMissione()) {
//					case "1201":
//						totali_pr01=totali_pr01==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr01.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//					case "1202":
//						totali_pr02=totali_pr02==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr02.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;	
//					case "1203":
//						totali_pr03=totali_pr03==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr03.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//					case "1204":
//						totali_pr04=totali_pr04==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr04.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//					case "1205":
//						totali_pr05=totali_pr05==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr05.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//					case "1206":
//						totali_pr06=totali_pr06==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr06.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//					case "1207":
//						totali_pr07=totali_pr07==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr07.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//					case "1208":
//						totali_pr08=totali_pr08==null ? BigDecimal.ZERO.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO) : totali_pr08.add(item.getValore() != null? item.getValore() : BigDecimal.ZERO);
//						break;
//
//					}
//				}
//			}
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1201", totali_pr01));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1202", totali_pr02));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1203", totali_pr03));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1204", totali_pr04));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1205", totali_pr05));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1206", totali_pr06));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1207", totali_pr07));
		l_totali.add(new ModelB1ProgrammiMissioneTotali("1208", totali_pr08));
//		}
		return l_totali;

	}

	@Transactional
	public List<ModelB1Voci> getVoci(int id_scheda_ente) {

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(id_scheda_ente);
		List<GregRCartaServiziPreg1> elenco_prestazioni = modelloB1Dao.findAllPrestazioni(id_scheda_ente);

		List<ModelB1Voci> list_prestazioni = new ArrayList<ModelB1Voci>();
		if (!elenco_prestazioni.isEmpty() && rendicontazione != null) {
			for (GregRCartaServiziPreg1 prestazione : elenco_prestazioni) {
				ModelB1Voci p_tmp = new ModelB1Voci();
				List<ModelB1UtenzaPrestReg1> l_utenze = new ArrayList<ModelB1UtenzaPrestReg1>();
				p_tmp.setUtenze(l_utenze);
				List<ModelB1Macroaggregati> l_v_m = new ArrayList<ModelB1Macroaggregati>();
				p_tmp.setMacroaggregati(l_v_m);
				List<ModelB1VociPrestReg2> l_pr2 = new ArrayList<ModelB1VociPrestReg2>();
				p_tmp.setPrestazioniRegionali2(l_pr2);
				List<ModelB1UtenzaPrestReg1> l_utenze_costo_totale = new ArrayList<ModelB1UtenzaPrestReg1>();
				p_tmp.setUtenzeCostoTotale(l_utenze_costo_totale);
				List<ModelB1UtenzaPrestReg1> l_utenze_quota_socio_assistenziale = new ArrayList<ModelB1UtenzaPrestReg1>();
				p_tmp.setUtenzeQuotaSocioAssistenziale(l_utenze_quota_socio_assistenziale);

				p_tmp.setCodPrestazione(prestazione.getGregTPrestazioniRegionali1().getCodPrestReg1());
				p_tmp.setDescPrestazione(prestazione.getGregTPrestazioniRegionali1().getDesPrestReg1());
				p_tmp.setTipoPrestazione(
						prestazione.getGregTPrestazioniRegionali1().getGregDTipologia().getCodTipologia());
				p_tmp.setMsgInformativo(prestazione.getGregTPrestazioniRegionali1().getInformativa());

				List<GregRPrestReg1MacroaggregatiBilancio> lista_valori = modelloB1Dao.findAllVociMacroaggregati(
						prestazione.getGregTPrestazioniRegionali1().getIdPrestReg1(),
						rendicontazione.getAnnoGestione());

				if (!lista_valori.isEmpty()) {
					for (GregRPrestReg1MacroaggregatiBilancio item : lista_valori) {
						if (item != null && item.getDataCancellazione()==null) {
							ModelB1Macroaggregati o_tmp = new ModelB1Macroaggregati();
							o_tmp.setCodice(item.getGregTMacroaggregatiBilancio().getCodMacroaggregatoBilancio());
							o_tmp.setIDPrestReg1MacroaggregatoBilancio(item.getIdPrestReg1MacroaggregatiBilancio());
							Iterator<GregRRendicontazionePreg1Macro> iterator = rendicontazione
									.getGregRRendicontazionePreg1Macros().iterator();
							while (iterator.hasNext()) {
								GregRRendicontazionePreg1Macro m_tmp = iterator.next();
								if (m_tmp.getGregRPrestReg1MacroaggregatiBilancio()
										.getIdPrestReg1MacroaggregatiBilancio() == item
												.getIdPrestReg1MacroaggregatiBilancio()) {
									o_tmp.setValore(m_tmp.getValore().toString());
									o_tmp.setIDTabellaRiferimento(m_tmp.getIdRendicontazionePreg1Macro());
									break;
								}
							}
							l_v_m.add(o_tmp);
						}
					}
				}

				if (prestazione.getGregTPrestazioniRegionali1().getGregDTipologia().getCodTipologia().equals("MA03")) {
					// PRESTAZIONI TIPO STRUTTURA
					// VALORI SALVATI DELLE UTENZE REGIONALI 1
					List<GregRPrestReg1UtenzeRegionali1> l_u = new ArrayList<GregRPrestReg1UtenzeRegionali1>(
							prestazione.getGregTPrestazioniRegionali1().getGregRPrestReg1UtenzeRegionali1s());
					if (!l_u.isEmpty()) {
						l_u.sort((o1, o2) -> o1.getGregDTargetUtenza().getCodUtenza()
								.compareTo(o2.getGregDTargetUtenza().getCodUtenza()));

						for (GregRPrestReg1UtenzeRegionali1 utenza : l_u) {
							if (utenza != null && utenza.getDataCancellazione()==null) {
								Calendar calendarInizio = Calendar.getInstance();
								Calendar calendarFine = Calendar.getInstance();
								calendarInizio.setTime(new Date(utenza.getDataInizioValidita().getTime()));
								if (utenza.getDataFineValidita() != null) {
									calendarFine.setTime(new Date(utenza.getDataFineValidita().getTime()));
								} else {
									calendarFine = null;
								}
								if (calendarInizio.get(Calendar.YEAR) <= rendicontazione.getAnnoGestione()
										&& (calendarFine == null || calendarFine.get(Calendar.YEAR) >= rendicontazione
												.getAnnoGestione())) {
									ModelB1UtenzaPrestReg1 o_tmp = new ModelB1UtenzaPrestReg1();
									o_tmp.setCodice(utenza.getGregDTargetUtenza().getCodUtenza());
									o_tmp.setDescUtenza(utenza.getGregDTargetUtenza().getDesUtenza());
									if (utenza.getGregDColori() != null
											&& !utenza.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().isEmpty()
											&& utenza.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
													.size() == 1) {
										o_tmp.setColore(utenza.getGregDColori().getRgb());
										Iterator<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> iterator = utenza
												.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().iterator();
										GregRPrestReg1UtenzeRegionali1ProgrammaMissione pr_mis = iterator.next();
										Calendar cal = Calendar.getInstance();
										cal.setTime(pr_mis.getDataInizioValidita());
										int annoInizio = cal.get(Calendar.YEAR);
										if (pr_mis.getDataFineValidita() != null) {
											cal.setTime(pr_mis.getDataFineValidita());
										}
										int annoFine = 0;
										if (pr_mis.getDataFineValidita() != null) {
											cal.setTime(pr_mis.getDataFineValidita());
											annoFine = cal.get(Calendar.YEAR);
										}
										if ((rendicontazione.getAnnoGestione() - annoInizio) >= 0 && ((annoFine != 0
												&& (rendicontazione.getAnnoGestione() - annoFine) <= 0)
												|| pr_mis.getDataFineValidita() == null)) {
											o_tmp.setCodiceProgrammaMissione(
													pr_mis.getGregDProgrammaMissione().getCodProgrammaMissione());
										}
									}

									Iterator<GregRRendicontazionePreg1Utereg1> iterator = rendicontazione
											.getGregRRendicontazionePreg1Utereg1s().iterator();
									while (iterator.hasNext()) {
										GregRRendicontazionePreg1Utereg1 m_tmp = iterator.next();
										if (m_tmp.getGregRPrestReg1UtenzeRegionali1()
												.getIdPrestReg1UtenzaRegionale1() == utenza
														.getIdPrestReg1UtenzaRegionale1()) {
											o_tmp.setValore(m_tmp.getValore().toString());
											o_tmp.setIDTabellaRiferimento(m_tmp.getIdRendicontazionePreg1Utereg1());
											break;
										}
									}
									o_tmp.setIDPrestReg1UtenzaReg1(utenza.getIdPrestReg1UtenzaRegionale1());

									l_utenze_costo_totale.add(o_tmp);
								}
							}
						}
					}

					// UTENZE QUOTA SOCIO ASSISTENZIALE
					List<GregTPrestazioniRegionali1> l_nuova_prest_reg_1 = modelloB1Dao
							.findPrestReg1QuotaSocioAssistenziale(
									prestazione.getGregTPrestazioniRegionali1().getIdPrestReg1(),
									rendicontazione.getAnnoGestione());
					if (!l_nuova_prest_reg_1.isEmpty()) {
						Optional<GregTPrestazioniRegionali1> o_nuova_prest_reg_1 = l_nuova_prest_reg_1.stream()
								.filter(p -> p.getGregDTipologiaQuota().getCodTipologiaQuota().equals("01"))
								.findFirst();
						if (o_nuova_prest_reg_1.isPresent()) {
							GregTPrestazioniRegionali1 nuova_prest_reg_1 = o_nuova_prest_reg_1.get();
							p_tmp.setMsgInformativoQSA(nuova_prest_reg_1.getInformativa());

							Optional<GregTPrestazioniRegionali1> o_prest_reg_1_ASR = l_nuova_prest_reg_1.stream()
									.filter(p -> p.getGregDTipologiaQuota().getCodTipologiaQuota().equals("03"))
									.findFirst();
							GregTPrestazioniRegionali1 prest_reg_1_ASR = null;
							if (o_prest_reg_1_ASR.isPresent()) {
								prest_reg_1_ASR = o_prest_reg_1_ASR.get();
							}
							Optional<GregTPrestazioniRegionali1> o_prest_reg_1_UT = l_nuova_prest_reg_1.stream()
									.filter(p -> p.getGregDTipologiaQuota().getCodTipologiaQuota().equals("02"))
									.findFirst();
							GregTPrestazioniRegionali1 prest_reg_1_UT = null;
							if (o_prest_reg_1_UT.isPresent()) {
								prest_reg_1_UT = o_prest_reg_1_UT.get();
							}

							List<GregRPrestReg1UtenzeRegionali1> l_u_q_a = new ArrayList<GregRPrestReg1UtenzeRegionali1>(
									nuova_prest_reg_1.getGregRPrestReg1UtenzeRegionali1s());
							if (!l_u_q_a.isEmpty()) {
								l_u_q_a.sort((o1, o2) -> o1.getGregDTargetUtenza().getCodUtenza()
										.compareTo(o2.getGregDTargetUtenza().getCodUtenza()));

								for (GregRPrestReg1UtenzeRegionali1 utenza : l_u_q_a) {
									if (utenza != null && utenza.getDataCancellazione()==null) {
										Calendar calendarInizio = Calendar.getInstance();
										Calendar calendarFine = Calendar.getInstance();
										calendarInizio.setTime(new Date(utenza.getDataInizioValidita().getTime()));
										if (utenza.getDataFineValidita() != null) {
											calendarFine.setTime(new Date(utenza.getDataFineValidita().getTime()));
										} else {
											calendarFine = null;
										}
										if (calendarInizio.get(Calendar.YEAR) <= rendicontazione.getAnnoGestione()
												&& (calendarFine == null || calendarFine
														.get(Calendar.YEAR) >= rendicontazione.getAnnoGestione())) {
											ModelB1UtenzaPrestReg1 o_tmp = new ModelB1UtenzaPrestReg1();
											o_tmp.setCodice(utenza.getGregDTargetUtenza().getCodUtenza());
											if (utenza.getGregDColori() != null
													&& !utenza.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
															.isEmpty()
													&& utenza.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
															.size() == 1) {
												o_tmp.setColore(utenza.getGregDColori().getRgb());
												Iterator<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> iterator = utenza
														.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
														.iterator();
												GregRPrestReg1UtenzeRegionali1ProgrammaMissione pr_mis = iterator
														.next();
												Calendar cal = Calendar.getInstance();
												cal.setTime(pr_mis.getDataInizioValidita());
												int annoInizio = cal.get(Calendar.YEAR);
												cal.setTime(pr_mis.getDataFineValidita());
												int annoFine = 0;
												if (pr_mis.getDataFineValidita() != null) {
													cal.setTime(pr_mis.getDataFineValidita());
													annoFine = cal.get(Calendar.YEAR);
												}
												if ((rendicontazione.getAnnoGestione() - annoInizio) >= 0
														&& ((annoFine != 0
																&& (rendicontazione.getAnnoGestione() - annoFine) <= 0)
																|| pr_mis.getDataFineValidita() == null)) {
													o_tmp.setCodiceProgrammaMissione(pr_mis.getGregDProgrammaMissione()
															.getCodProgrammaMissione());
												}
											}

											Iterator<GregRRendicontazionePreg1Utereg1> iterator = rendicontazione
													.getGregRRendicontazionePreg1Utereg1s().iterator();
											while (iterator.hasNext()) {
												GregRRendicontazionePreg1Utereg1 m_tmp = iterator.next();
												if (m_tmp.getGregRPrestReg1UtenzeRegionali1()
														.getIdPrestReg1UtenzaRegionale1() == utenza
																.getIdPrestReg1UtenzaRegionale1()) {
													o_tmp.setValore(m_tmp.getValore().toString());
													o_tmp.setIDTabellaRiferimento(
															m_tmp.getIdRendicontazionePreg1Utereg1());
													break;
												}
											}

											BigDecimal somma_ut_asr_mod_A = new BigDecimal(0);
											boolean mandatory = false;

											Iterator<GregRRendicontazioneModAPart2> iterator_mod_A = rendicontazione
													.getGregRRendicontazioneModAPart2s().iterator();
											while (iterator_mod_A.hasNext()) {
												GregRRendicontazioneModAPart2 m_tmp = iterator_mod_A.next();
												if (prest_reg_1_ASR != null
														&& utenza.getGregDTargetUtenza().getCodUtenza()
																.equals(m_tmp.getGregRPrestReg1UtenzeRegionali1()
																		.getGregDTargetUtenza().getCodUtenza())
														&& m_tmp.getGregRPrestReg1UtenzeRegionali1()
																.getGregTPrestazioniRegionali1().getIdPrestReg1()
																.equals(prest_reg_1_ASR.getIdPrestReg1())) {
													if ((m_tmp.getGregRPrestReg1UtenzeRegionali1()
															.getGregTPrestazioniRegionali1().getGregDTipologiaQuota()
															.getCodTipologiaQuota().equals("02")
															|| m_tmp.getGregRPrestReg1UtenzeRegionali1()
																	.getGregTPrestazioniRegionali1()
																	.getGregDTipologiaQuota().getCodTipologiaQuota()
																	.equals("03"))
															&& m_tmp.getValore() != null && m_tmp.getValore().compareTo(BigDecimal.ZERO)!=0){
														somma_ut_asr_mod_A = somma_ut_asr_mod_A.add(m_tmp.getValore());
														mandatory = true;
													}
												}

												if (prest_reg_1_UT != null
														&& utenza.getGregDTargetUtenza().getCodUtenza()
																.equals(m_tmp.getGregRPrestReg1UtenzeRegionali1()
																		.getGregDTargetUtenza().getCodUtenza())
														&& m_tmp.getGregRPrestReg1UtenzeRegionali1()
																.getGregTPrestazioniRegionali1().getIdPrestReg1()
																.equals(prest_reg_1_UT.getIdPrestReg1())) {
													if ((m_tmp.getGregRPrestReg1UtenzeRegionali1()
															.getGregTPrestazioniRegionali1().getGregDTipologiaQuota()
															.getCodTipologiaQuota().equals("02")
															|| m_tmp.getGregRPrestReg1UtenzeRegionali1()
																	.getGregTPrestazioniRegionali1()
																	.getGregDTipologiaQuota().getCodTipologiaQuota()
																	.equals("03"))
															&& m_tmp.getValore() != null && m_tmp.getValore().compareTo(BigDecimal.ZERO)!=0) {
														somma_ut_asr_mod_A = somma_ut_asr_mod_A.add(m_tmp.getValore());
														mandatory = true;
													}
												}
											}

											if (mandatory) {
												ModelB1UtenzaPrestReg1 utenza_costo_totale = l_utenze_costo_totale
														.stream()
														.filter(u -> u.getCodice()
																.equals(utenza.getGregDTargetUtenza().getCodUtenza()))
														.findAny().orElse(null);
												if (utenza_costo_totale != null) {
													utenza_costo_totale.setMandatory(true);
												}

												if (o_tmp != null) {
													o_tmp.setMandatory(true);
												}
											}

											o_tmp.setSommaUTASRModA(somma_ut_asr_mod_A);

											o_tmp.setIDPrestReg1UtenzaReg1(utenza.getIdPrestReg1UtenzaRegionale1());
											l_utenze_quota_socio_assistenziale.add(o_tmp);
										}
									}
								}
							}
						}
					}
				} else {

					// VALORI SALVATI DELLE UTENZE REGIONALI 1
					List<GregRPrestReg1UtenzeRegionali1> l_u = new ArrayList<GregRPrestReg1UtenzeRegionali1>(
							prestazione.getGregTPrestazioniRegionali1().getGregRPrestReg1UtenzeRegionali1s());
					if (!l_u.isEmpty()) {
						l_u.sort((o1, o2) -> o1.getGregDTargetUtenza().getCodUtenza()
								.compareTo(o2.getGregDTargetUtenza().getCodUtenza()));
						for (GregRPrestReg1UtenzeRegionali1 utenza : l_u) {

							if (utenza != null && utenza.getDataCancellazione()==null) {
								Calendar calendarInizio = Calendar.getInstance();
								Calendar calendarFine = Calendar.getInstance();
								calendarInizio.setTime(new Date(utenza.getDataInizioValidita().getTime()));
								if (utenza.getDataFineValidita() != null) {
									calendarFine.setTime(new Date(utenza.getDataFineValidita().getTime()));
								} else {
									calendarFine = null;
								}
								if (calendarInizio.get(Calendar.YEAR) <= rendicontazione.getAnnoGestione()
										&& (calendarFine == null || calendarFine.get(Calendar.YEAR) >= rendicontazione
												.getAnnoGestione())) {
									ModelB1UtenzaPrestReg1 o_tmp = new ModelB1UtenzaPrestReg1();
									o_tmp.setCodice(utenza.getGregDTargetUtenza().getCodUtenza());
									o_tmp.setDescUtenza(utenza.getGregDTargetUtenza().getDesUtenza());
									if (utenza.getGregDColori() != null
											&& !utenza.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().isEmpty()
											&& utenza.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones()
													.size() == 1) {
										o_tmp.setColore(utenza.getGregDColori().getRgb());
										Iterator<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> iterator = utenza
												.getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().iterator();
										GregRPrestReg1UtenzeRegionali1ProgrammaMissione pr_mis = iterator.next();
										Calendar cal = Calendar.getInstance();
										cal.setTime(pr_mis.getDataInizioValidita());
										int annoInizio = cal.get(Calendar.YEAR);

										int annoFine = 0;
										if (pr_mis.getDataFineValidita() != null) {
											cal.setTime(pr_mis.getDataFineValidita());
											annoFine = cal.get(Calendar.YEAR);
										}
										if ((rendicontazione.getAnnoGestione() - annoInizio) >= 0 && ((annoFine != 0
												&& (rendicontazione.getAnnoGestione() - annoFine) <= 0)
												|| pr_mis.getDataFineValidita() == null)) {
											o_tmp.setCodiceProgrammaMissione(
													pr_mis.getGregDProgrammaMissione().getCodProgrammaMissione());
										}
									}
									Iterator<GregRRendicontazionePreg1Utereg1> iterator = rendicontazione
											.getGregRRendicontazionePreg1Utereg1s().iterator();
									while (iterator.hasNext()) {
										GregRRendicontazionePreg1Utereg1 m_tmp = iterator.next();
										if (m_tmp.getGregRPrestReg1UtenzeRegionali1()
												.getIdPrestReg1UtenzaRegionale1() == utenza
														.getIdPrestReg1UtenzaRegionale1()) {
											o_tmp.setValore(m_tmp.getValore().toString());
											o_tmp.setIDTabellaRiferimento(m_tmp.getIdRendicontazionePreg1Utereg1());
											break;
										}
									}
									o_tmp.setIDPrestReg1UtenzaReg1(utenza.getIdPrestReg1UtenzaRegionale1());
									l_utenze.add(o_tmp);
								}
							}
						}
					}
				}
				// ELENCO PRESTAZIONI REGIONALI 2
				List<GregRPrestReg1PrestReg2> l_prest_reg_2 = modelloB1Dao.findAllPrestReg2(
						prestazione.getGregTPrestazioniRegionali1().getIdPrestReg1(),
						rendicontazione.getAnnoGestione());
				if (!l_prest_reg_2.isEmpty()) {
					for (GregRPrestReg1PrestReg2 prest_reg_2 : l_prest_reg_2) {
						ModelB1VociPrestReg2 voce_prest_reg_2 = new ModelB1VociPrestReg2();
						voce_prest_reg_2
								.setCodPrestazione(prest_reg_2.getGregTPrestazioniRegionali2().getCodPrestReg2());
						voce_prest_reg_2
								.setDescPrestazione(prest_reg_2.getGregTPrestazioniRegionali2().getDesPrestReg2());
						List<ModelB1UtenzaPrestReg2> l_u_r_2 = new ArrayList<ModelB1UtenzaPrestReg2>();
						voce_prest_reg_2.setUtenze(l_u_r_2);

						// UTENZE PREST REG 2
						List<GregRPrestReg2UtenzeRegionali2> l_u_pr2 = modelloB1Dao.findAllUtenzePrestazioniRegionali2(
								prest_reg_2.getGregTPrestazioniRegionali2().getIdPrestReg2(),
								rendicontazione.getAnnoGestione());
						if (!l_u_pr2.isEmpty()) {
							for (GregRPrestReg2UtenzeRegionali2 utenza : l_u_pr2) {
								if (utenza != null && utenza.getDataCancellazione()==null) {
									ModelB1UtenzaPrestReg2 o_tmp = new ModelB1UtenzaPrestReg2();
									o_tmp.setCodice(utenza.getGregDTargetUtenza().getCodUtenza());
									Iterator<GregRRendicontazionePreg2Utereg2> iterator = rendicontazione
											.getGregRRendicontazionePreg2Utereg2s().iterator();
									while (iterator.hasNext()) {
										GregRRendicontazionePreg2Utereg2 m_tmp = iterator.next();
										if (m_tmp.getGregRPrestReg2UtenzeRegionali2()
												.getIdPrestReg2UtenzaRegionale2() == utenza
														.getIdPrestReg2UtenzaRegionale2()) {
											o_tmp.setValore(m_tmp.getValore().toString());
											o_tmp.setIDTabellaRiferimento(m_tmp.getIdRendicontazionePreg2Utereg2());
											break;
										}
									}
									o_tmp.setIDPrestReg2UtenzaReg2(utenza.getIdPrestReg2UtenzaRegionale2());
									l_u_r_2.add(o_tmp);
								}
							}
						}
						l_pr2.add(voce_prest_reg_2);
					}
				}
				list_prestazioni.add(p_tmp);
			}
		}
		return list_prestazioni;
	}

	@Transactional
	public SaveModelloOutput saveModello(ModelB1Save body, UserInfo userInfo) throws Exception {
		SaveModelloOutput response = new SaveModelloOutput();
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService
				.getRendicontazione(body.getIdRendicontazioneEnte());

		String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione();
		// Controllo e aggiorno lo stato della rendicontazione
		datiRendicontazioneService.modificaStatoRendicontazione(rendicontazione, userInfo,
				SharedConstants.OPERAZIONE_SALVA, body.getProfilo());
		String statoNew = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione();
		String newNotaEnte = "";
		if (!statoOld.equals(statoNew)) {
			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO).getTestoMessaggio()
					.replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
					.replace("STATONEW", "'" + statoNew + "'");
		}

		// Recupero eventuale ultima cronologia inserita

		GregTCronologia lastCrono = datiRendicontazioneService
				.findLastCronologiaEnte(rendicontazione.getIdRendicontazioneEnte());

//		if((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE)) 
//
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
				newNotaEnte = body.getCronologia().getNotaEnte();
			}

		} else {
			newNotaEnte = body.getCronologia().getNotaEnte();
		}

		// SALVO NOTE DI CRONOLOGIA
		if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(body.getCronologia().getNotaInterna())) {
			GregTCronologia cronologia = new GregTCronologia();
			cronologia.setGregTRendicontazioneEnte(rendicontazione);
			cronologia.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
			cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
			cronologia.setUtenteOperazione(userInfo.getCodFisc());
			cronologia.setModello("Mod. B1");
			cronologia.setNotaInterna(body.getCronologia().getNotaInterna());
			cronologia.setNotaPerEnte(newNotaEnte);
			cronologia.setDataOra(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataModifica(new Timestamp(System.currentTimeMillis()));
			datiRendicontazioneService.insertCronologia(cronologia);
		}

		// Set<GregRRendicontazionePreg1Macro> rend_macroaggregati =
		// rendicontazione.getGregRRendicontazionePreg1Macros();
		// SALVO I DATI
		for (ModelB1Voci voce : body.getDati()) {
			// MACROAGGREGATI
			for (ModelB1Macroaggregati item : voce.getMacroaggregati()) {
				if (item.getIDTabellaRiferimento() > 0) {
					Iterator<GregRRendicontazionePreg1Macro> iterator = rendicontazione
							.getGregRRendicontazionePreg1Macros().iterator();
					while (iterator.hasNext()) {
						GregRRendicontazionePreg1Macro m_tmp = iterator.next();
						if (m_tmp.getIdRendicontazionePreg1Macro() == item.getIDTabellaRiferimento()) {
							if (item.getValore() != null) {
								// UPDATE
								m_tmp.setValore(new BigDecimal(item.getValore().replace(".", "").replace(",", ".")));
								m_tmp.setDataModifica(new Timestamp(System.currentTimeMillis()));
								m_tmp.setUtenteOperazione(userInfo.getCodFisc());
								modelloB1Dao.updateMacroaggregati(m_tmp);
							} else {
								// DELETE
								modelloB1Dao.deleteMacroaggregati(m_tmp.getIdRendicontazionePreg1Macro());
							}
							break;
						}
					}

				} else if (item.getValore() != null) {
					// INSERT
					GregRRendicontazionePreg1Macro insval = new GregRRendicontazionePreg1Macro();
					insval.setGregRPrestReg1MacroaggregatiBilancio(modelloB1Dao.findRPrestReg1MacroaggregatiBilancio(
							item.getIDPrestReg1MacroaggregatoBilancio(), rendicontazione.getAnnoGestione()));
					insval.setGregTRendicontazioneEnte(rendicontazione);
					insval.setDataCreazione(new Timestamp(System.currentTimeMillis()));
					insval.setValore(new BigDecimal(item.getValore().replace(".", "").replace(",", ".")));
					insval.setDataModifica(new Timestamp(System.currentTimeMillis()));
					insval.setUtenteOperazione(userInfo.getCodFisc());
					insval.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
					modelloB1Dao.insertMacroaggregati(insval);
				}
			}

			// UTENZE PREST REG 1
			savePrestReg1(voce.getUtenze(), rendicontazione, userInfo);
			savePrestReg1(voce.getUtenzeCostoTotale(), rendicontazione, userInfo);
			savePrestReg1(voce.getUtenzeQuotaSocioAssistenziale(), rendicontazione, userInfo);

			// UTENZE PREST REG 2 (ISTAT)
			for (ModelB1VociPrestReg2 pres_reg_2_voce : voce.getPrestazioniRegionali2()) {
				for (ModelB1UtenzaPrestReg2 item : pres_reg_2_voce.getUtenze()) {
					if (item.getIDTabellaRiferimento() > 0) {
						Iterator<GregRRendicontazionePreg2Utereg2> iterator = rendicontazione
								.getGregRRendicontazionePreg2Utereg2s().iterator();
						while (iterator.hasNext()) {
							GregRRendicontazionePreg2Utereg2 m_tmp = iterator.next();
							if (m_tmp.getIdRendicontazionePreg2Utereg2() == item.getIDTabellaRiferimento()) {
								if (item.getValore() != null) {
									// UPDATE
									m_tmp.setValore(
											new BigDecimal(item.getValore().replace(".", "").replace(",", ".")));
									m_tmp.setDataModifica(new Timestamp(System.currentTimeMillis()));
									m_tmp.setUtenteOperazione(userInfo.getCodFisc());
									modelloB1Dao.updatePreg2Utereg2(m_tmp);
								} else {
									// DELETE
									modelloB1Dao.deletePreg2Utereg2(m_tmp.getIdRendicontazionePreg2Utereg2());
								}
								break;
							}
						}

					} else if (item.getValore() != null) {
						// INSERT
						GregRRendicontazionePreg2Utereg2 insval = new GregRRendicontazionePreg2Utereg2();
						insval.setGregRPrestReg2UtenzeRegionali2(modelloB1Dao.findRPrestReg2Utereg2(
								item.getIDPrestReg2UtenzaReg2(), rendicontazione.getAnnoGestione()));
						insval.setGregTRendicontazioneEnte(rendicontazione);
						insval.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						insval.setValore(new BigDecimal(item.getValore().replace(".", "").replace(",", ".")));
						insval.setDataModifica(new Timestamp(System.currentTimeMillis()));
						insval.setUtenteOperazione(userInfo.getCodFisc());
						insval.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
						modelloB1Dao.insertPreg2Utereg2(insval);
					}
				}
			}
		}

//		List<ModelB1ProgrammiMissioneTotali> missione12 = getProgrammiMissioneTotali(body.getIdEnte(), Integer.parseInt(Converter.getAnno(new Date()))-1);
		List<ModelB1ProgrammiMissioneTotali> missione12 = getSaveProgrammiMissioneTotali(body.getDati());
		GregRRendMiProTitEnteGestoreModB rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12",
				"1201", "1", rendicontazione.getIdRendicontazioneEnte());
		GregRProgrammaMissioneTitSottotit proMissTitSottotit = modelloBDao
				.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1201", "1");
		GregRRendMiProTitEnteGestoreModB rendProMissTitSottotit04Sott1 = modelloBDao
				.findRendModBbyMissProTitSottotit("04", "0406", "1", "11", rendicontazione.getIdRendicontazioneEnte());

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(0).getValore()
					.subtract(rendProMissTitSottotit04Sott1 != null ? rendProMissTitSottotit04Sott1.getValore()
							: BigDecimal.ZERO));
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(0).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1202", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1202", "1");
		GregRRendMiProTitEnteGestoreModB rendProMissTitSottotit04Sott2 = modelloBDao
				.findRendModBbyMissProTitSottotit("04", "0406", "1", "12", rendicontazione.getIdRendicontazioneEnte());

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(1).getValore()
					.subtract(rendProMissTitSottotit04Sott2 != null ? rendProMissTitSottotit04Sott2.getValore()
							: BigDecimal.ZERO));
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(1).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1203", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1203", "1");

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(2).getValore());
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(2).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1204", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1204", "1");

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(3).getValore());
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(3).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1205", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1205", "1");

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(4).getValore());
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(4).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1206", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1206", "1");

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(5).getValore());
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(5).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1207", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1207", "1");

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(6).getValore());
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(6).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		rendProMissTitSottotit = modelloBDao.findRendModBbyMissProTitNoSottotit("12", "1208", "1",
				rendicontazione.getIdRendicontazioneEnte());
		proMissTitSottotit = modelloBDao.findTitoliMissioneModBbyMissProTitNoSottotit("12", "1208", "1");

		if (rendProMissTitSottotit != null) {
			rendProMissTitSottotit.setValore(missione12.get(7).getValore());
			rendProMissTitSottotit.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
		} else {

			GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
			newRend.setGregTRendicontazioneEnte(rendicontazione);
			newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
			newRend.setValore(missione12.get(7).getValore());
			newRend.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
			newRend.setUtenteOperazione(userInfo.getCodFisc());
			newRend.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			newRend.setDataModifica(new Timestamp(System.currentTimeMillis()));
			modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
		}

		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdEnte());
			// Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
						ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
						SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
				response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		return response;
	}

	public List<String> controllaWarningsPrestNotValModelloB1(List<ModelB1Voci> prestazioniModB1) {
		List<String> listaWarnings = new ArrayList<String>();

		// Verifica valorizzazioni per Macroaggregati e Utenze, per ogni Prestazione
		String prestNotVal = "";
		for (ModelB1Voci prestazione : prestazioniModB1) {
			Boolean macroValorizzato = false;
			Boolean utenzaValorizzata = false;
			// Valorizzato Macroaggregato
			for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
				if (macroaggregato.getValore() != null) {
					macroValorizzato = true;
				}
			}
			// Valorizzata Utenza
			if (prestazione.getTipoPrestazione().equals("MA03")) {
				for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
					if (utenza.getValore() != null) {
						utenzaValorizzata = true;
					}
				}
			} else {
				for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
					if (utenza.getValore() != null) {
						utenzaValorizzata = true;
					}
				}
			}

			if (!macroValorizzato && !utenzaValorizzata) {
				prestNotVal = prestNotVal + prestazione.getCodPrestazione() + ", ";
			}
		}
		if (!prestNotVal.equals("")) {
			listaWarnings.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_09).getTestoMessaggio()
					.replace("PRESTAZIONI", prestNotVal.substring(0, prestNotVal.length() - 2)));
		}
		return listaWarnings;
	}

	private void savePrestReg1(List<ModelB1UtenzaPrestReg1> list, GregTRendicontazioneEnte rendicontazione,
			UserInfo userInfo) {
		for (ModelB1UtenzaPrestReg1 item : list) {
			if (item.getIDTabellaRiferimento() > 0) {
				Iterator<GregRRendicontazionePreg1Utereg1> iterator = rendicontazione
						.getGregRRendicontazionePreg1Utereg1s().iterator();
				while (iterator.hasNext()) {
					GregRRendicontazionePreg1Utereg1 m_tmp = iterator.next();
					if (m_tmp.getIdRendicontazionePreg1Utereg1() == item.getIDTabellaRiferimento()) {
						if (item.getValore() != null) {
							// UPDATE
							m_tmp.setValore(new BigDecimal(item.getValore().replace(".", "").replace(",", ".")));
							m_tmp.setDataModifica(new Timestamp(System.currentTimeMillis()));
							m_tmp.setUtenteOperazione(userInfo.getCodFisc());
							modelloB1Dao.updatePreg1Utereg1(m_tmp);
						} else {
							// DELETE
							modelloB1Dao.deletePreg1Utereg1(m_tmp.getIdRendicontazionePreg1Utereg1());
						}
						break;
					}
				}

			} else if (item.getValore() != null) {
				// INSERT
				GregRRendicontazionePreg1Utereg1 insval = new GregRRendicontazionePreg1Utereg1();
				insval.setGregRPrestReg1UtenzeRegionali1(modelloB1Dao
						.findRPrestReg1Utereg1(item.getIDPrestReg1UtenzaReg1(), rendicontazione.getAnnoGestione()));
				insval.setGregTRendicontazioneEnte(rendicontazione);
				insval.setDataCreazione(new Timestamp(System.currentTimeMillis()));
				insval.setValore(new BigDecimal(item.getValore().replace(".", "").replace(",", ".")));
				insval.setDataModifica(new Timestamp(System.currentTimeMillis()));
				insval.setUtenteOperazione(userInfo.getCodFisc());
				insval.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
				modelloB1Dao.insertPreg1Utereg1(insval);
			}
		}
	}

	public String esportaModelloB1(EsportaModelloB1Input body) throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Modello B1");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		HSSFDataFormat format = workbook.createDataFormat();

		Font font8 = sheet.getWorkbook().createFont();
		font8.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font8.setFontHeightInPoints((short) 8);
		font8.setFontName(HSSFFont.FONT_ARIAL);

		Font font8b = sheet.getWorkbook().createFont();
		font8b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font8b.setFontHeightInPoints((short) 8);
		font8b.setFontName(HSSFFont.FONT_ARIAL);

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
		cellStyletitolo.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyletitolo.setFont(font12b);
		cellStyletitolo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo2
		CellStyle cellStyletitolo2 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyletitolo2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo2.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo2.setFont(font8b);
		cellStyletitolo2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyletitolo2.setWrapText(true);

		// crea stili arial titolo3
		CellStyle cellStyletitolo3 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo3.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyletitolo3.setFont(font8b);
		cellStyletitolo3.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyletitolo3.setBorderRight(CellStyle.BORDER_THIN);
		cellStyletitolo3.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyletitolo3.setWrapText(true);

		// crea stili arial titolo3Tot
		CellStyle cellStyletitolo3Tot = sheet.getWorkbook().createCellStyle();
		cellStyletitolo3Tot.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyletitolo3Tot.setFont(font8b);
		cellStyletitolo3Tot.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyletitolo3Tot.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyletitolo3Tot.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyletitolo3Tot.setWrapText(true);

		// crea stili arial codice prestazione
		CellStyle cellStyleCodPrest = sheet.getWorkbook().createCellStyle();
		cellStyleCodPrest.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleCodPrest.setFont(font8b);
		cellStyleCodPrest.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleCodPrest.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleCodPrest.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleCodPrest.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleCodPrest.setWrapText(true);

		// crea stili arial descrizione prestazione
		CellStyle cellStyleDescPrest = sheet.getWorkbook().createCellStyle();
		cellStyleDescPrest.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleDescPrest.setFont(font8);
		cellStyleDescPrest.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyleDescPrest.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleDescPrest.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleDescPrest.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescPrest.setWrapText(true);

		// crea stili arial value
		CellStyle cellStyleValue = sheet.getWorkbook().createCellStyle();
		cellStyleValue.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValue.setFont(font8);
		cellStyleValue.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleValue.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValue.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValue.setDataFormat(format.getFormat(pattern));

		// crea stili arial value end
		CellStyle cellStyleValueEnd = sheet.getWorkbook().createCellStyle();
		cellStyleValueEnd.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueEnd.setFont(font8);
		cellStyleValueEnd.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleValueEnd.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleValueEnd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueEnd.setDataFormat(format.getFormat(pattern));

		short reg2Pal = getColorExcel(workbook, "#EFEFEF").getIndex();

		// crea stili arial codice prestazione 2
		CellStyle cellStyleCodPrest2 = sheet.getWorkbook().createCellStyle();
		cellStyleCodPrest2.setFillForegroundColor(reg2Pal);
		cellStyleCodPrest2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleCodPrest2.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleCodPrest2.setFont(font8b);
		cellStyleCodPrest2.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleCodPrest2.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleCodPrest2.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleCodPrest2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleCodPrest2.setWrapText(true);

		// crea stili arial descrizione prestazione 2
		CellStyle cellStyleDescPrest2 = sheet.getWorkbook().createCellStyle();
		cellStyleDescPrest2.setFillForegroundColor(reg2Pal);
		cellStyleDescPrest2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleDescPrest2.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleDescPrest2.setFont(font8);
		cellStyleDescPrest2.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyleDescPrest2.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleDescPrest2.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleDescPrest2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDescPrest2.setWrapText(true);

		// crea stili arial value 2
		CellStyle cellStyleValue2 = sheet.getWorkbook().createCellStyle();
		cellStyleValue2.setFillForegroundColor(reg2Pal);
		cellStyleValue2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValue2.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValue2.setFont(font8);
		cellStyleValue2.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleValue2.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValue2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValue2.setDataFormat(format.getFormat(pattern));

		// crea stili arial value end 2
		CellStyle cellStyleValueEnd2 = sheet.getWorkbook().createCellStyle();
		cellStyleValueEnd2.setFillForegroundColor(reg2Pal);
		cellStyleValueEnd2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueEnd2.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueEnd2.setFont(font8);
		cellStyleValueEnd2.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleValueEnd2.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleValueEnd2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueEnd2.setDataFormat(format.getFormat(pattern));

		// crea stili arial value empty
		CellStyle cellStyleValueEmpty = sheet.getWorkbook().createCellStyle();
		cellStyleValueEmpty.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueEmpty.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		cellStyleValueEmpty.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueEmpty.setFont(font8);
		cellStyleValueEmpty.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleValueEmpty.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueEmpty.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial value empty end
		CellStyle cellStyleValueEmptyEnd = sheet.getWorkbook().createCellStyle();
		cellStyleValueEmptyEnd.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueEmptyEnd.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		cellStyleValueEmptyEnd.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueEmptyEnd.setFont(font8);
		cellStyleValueEmptyEnd.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleValueEmptyEnd.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleValueEmptyEnd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// Mappatura missioni - stili colori
		Map<String, CellStyle> missionStyles = new HashMap<String, CellStyle>();
		for (ModelB1Lbl missione : body.getLabels().getMissione_programma()) {
			HSSFColor myColor = getColorExcel(workbook, missione.getColore());
			short palIndex = myColor.getIndex();

			CellStyle missionStyle = workbook.createCellStyle();
			missionStyle.setFillForegroundColor(palIndex);
			missionStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			missionStyle.setAlignment(CellStyle.ALIGN_CENTER);
			missionStyle.setFont(font8);
			missionStyle.setBorderBottom(CellStyle.BORDER_THIN);
			missionStyle.setBorderRight(CellStyle.BORDER_THIN);
			missionStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			CellStyle missionStyleEnd = workbook.createCellStyle();
			missionStyleEnd.setFillForegroundColor(palIndex);
			missionStyleEnd.setFillPattern(CellStyle.SOLID_FOREGROUND);
			missionStyleEnd.setAlignment(CellStyle.ALIGN_CENTER);
			missionStyleEnd.setFont(font8);
			missionStyleEnd.setBorderBottom(CellStyle.BORDER_THIN);
			missionStyleEnd.setBorderRight(CellStyle.BORDER_MEDIUM);
			missionStyleEnd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			missionStyles.put(missione.getCodice(), missionStyle);
			missionStyles.put(missione.getCodice() + "_END", missionStyleEnd);
		}

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		List<GregTMacroaggregatiBilancio> macroaggregati = modelloB1Dao
				.findAllMacroaggregati(rendicontazione.getAnnoGestione());

		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_B1);
		ModelMsgInformativo header1 = listeService.getMsgInformativiByCodice("47");
		ModelMsgInformativo header2 = listeService.getMsgInformativiByCodice("48");
		ModelMsgInformativo header3 = listeService.getMsgInformativiByCodice("49");
		ModelMsgInformativo header4 = listeService.getMsgInformativiByCodice("50");
		ModelMsgInformativo header5 = listeService.getMsgInformativiByCodice("51");

		// Row 1
		Row row = sheet.createRow(rowCount);
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

		columnCount = 0;

		// Row 4
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		for (int i = 0; i <= body.getLabels().getMacroaggregati().size()
				+ ((body.getLabels().getUtenze().size()) * 2); i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyletitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		autoSizeColumnsAndMergedB1Meta(sheet.getWorkbook());
		columnCount = 0;

		// Row headers
//		sheet.setColumnWidth(columnCount, sheet.getColumnWidth(columnCount)/2);
		row = sheet.createRow(++rowCount);
		sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("Tipo prestazione");
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		cell = row.createCell(++columnCount);
		sheet.setColumnWidth(columnCount, sheet.getColumnWidth(columnCount) / 2);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header1.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		sheet.setColumnWidth(columnCount - 1, sheet.getColumnWidth(columnCount));

		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(StringEscapeUtils.unescapeJava(header2.getTestoMsgInformativo()));
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		sheet.setColumnWidth(columnCount, 60 * 256);

		cell = row.createCell(++columnCount);
		for (int i = 0; i < body.getLabels().getMacroaggregati().size(); i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount - 1,
				columnCount - (body.getLabels().getMacroaggregati().size()), columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(StringEscapeUtils.unescapeJava(header3.getTestoMsgInformativo()));
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		cell = row.createCell(++columnCount);
		for (int i = 0; i < body.getLabels().getUtenze().size() - 1; i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount - 1,
				columnCount - (body.getLabels().getUtenze().size() - 1), columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header4.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		cell = row.createCell(++columnCount);
		for (int i = 0; i < body.getLabels().getUtenze().size() - 1; i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount - 1,
				columnCount - (body.getLabels().getUtenze().size() - 1), columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header5.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		row.setHeight((short) (row.getHeight() * 3));

		row = sheet.getRow(rowCount);
		columnCount = 2;

		// Row value descs
		for (GregTMacroaggregatiBilancio lblMacro : macroaggregati) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(
					lblMacro.getDesMacroaggregatoBilancio() + " " + lblMacro.getAltraDescMacroaggregatoBilancio());
			cell.setCellStyle(cellStyletitolo3);
			sheet.setColumnWidth(columnCount, 20 * 256);
		}
		cell = row.createCell(++columnCount);
		cell.setCellValue("Totale");
		cell.setCellStyle(cellStyletitolo3Tot);
		sheet.setColumnWidth(columnCount, 20 * 256);

		for (ModelB1Lbl lblUtenze : body.getLabels().getUtenze()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(lblUtenze.getDescrizione());
			cell.setCellStyle(cellStyletitolo3);
			sheet.setColumnWidth(columnCount, 20 * 256);
		}
		cell.setCellStyle(cellStyletitolo3Tot);

		for (ModelB1Lbl lblUtenze : body.getLabels().getUtenze()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(lblUtenze.getDescrizione());
			cell.setCellStyle(cellStyletitolo3);
			sheet.setColumnWidth(columnCount, 20 * 256);
		}
		cell.setCellStyle(cellStyletitolo3Tot);
		row.setHeight((short) (row.getHeight() * 20));

		// rows prestazioni
		for (ModelB1Voci voce : body.getDatiB1()) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellValue("REG.1");
			cell.setCellStyle(cellStyleCodPrest);

			cell = row.createCell(++columnCount);
			cell.setCellValue(voce.getCodPrestazione());
			cell.setCellStyle(cellStyleCodPrest);

			cell = row.createCell(++columnCount);
			cell.setCellValue(voce.getDescPrestazione());
			cell.setCellStyle(cellStyleDescPrest);

			boolean found = false;
			Double total = Double.valueOf(0);
			for (ModelB1Lbl macro : body.getLabels().getMacroaggregati()) {
				cell = row.createCell(++columnCount);
				found = false;
				for (ModelB1Macroaggregati macroVal : voce.getMacroaggregati()) {
					if (macro.getCodice().equalsIgnoreCase(macroVal.getCodice())) {
						found = true;
						if (macroVal.getValore() != null) {
							cell.setCellValue(
									Double.valueOf(macroVal.getValore().replaceAll("\\.", "").replace(",", ".")));
							total = total
									+ Double.valueOf(macroVal.getValore().replaceAll("\\.", "").replace(",", "."));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					}
				}
				cell.setCellStyle(found ? cellStyleValue : cellStyleValueEmpty);
			}
			cell = row.createCell(++columnCount);
			cell.setCellValue(total);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleValueEnd);

			List<ModelB1UtenzaPrestReg1> utenze = voce.getUtenze() == null || voce.getUtenze().isEmpty()
					? voce.getUtenzeCostoTotale()
					: voce.getUtenze();
			for (ModelB1Lbl utenza : body.getLabels().getUtenze()) {
				cell = row.createCell(++columnCount);
				found = false;
				for (ModelB1UtenzaPrestReg1 utVal : utenze) {
					if (utenza.getCodice().equalsIgnoreCase(utVal.getCodice())) {
						found = true;
						if (utVal.getValore() != null) {
							cell.setCellValue(
									Double.valueOf(utVal.getValore().replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					}
				}
				cell.setCellStyle(found ? cellStyleValue : cellStyleValueEmpty);
			}
			cell.setCellStyle(found ? cellStyleValueEnd : cellStyleValueEmptyEnd);

			for (ModelB1Lbl utenza : body.getLabels().getUtenze()) {
				cell = row.createCell(++columnCount);
				found = false;
				for (ModelB1UtenzaPrestReg1 utVal : utenze) {
					if (utenza.getCodice().equalsIgnoreCase(utVal.getCodice())) {
						found = true;
						for (ModelB1Lbl missione : body.getLabels().getMissione_programma()) {
							if (utVal.getCodiceProgrammaMissione() == null) {
								cell.setCellStyle(body.getLabels().getUtenze()
										.indexOf(utenza) == body.getLabels().getUtenze().size() - 1 ? cellStyleValueEnd
												: cellStyleValue);
							} else if (missione.getCodice().equalsIgnoreCase(utVal.getCodiceProgrammaMissione())) {
								cell.setCellValue(missione.getDescrizione());
								cell.setCellStyle(body.getLabels().getUtenze()
										.indexOf(utenza) == body.getLabels().getUtenze().size() - 1
												? missionStyles.get(missione.getCodice() + "_END")
												: missionStyles.get(missione.getCodice()));
							}
						}
					}
				}
				if (!found)
					cell.setCellStyle(cellStyleValueEmpty);
			}
			if (!found)
				cell.setCellStyle(cellStyleValueEmptyEnd);

			row.setHeight((short) (row.getHeight() * 3));

			// QS
			if (voce.getUtenzeQuotaSocioAssistenziale() != null && !voce.getUtenzeQuotaSocioAssistenziale().isEmpty()) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				GregRPrestReg1UtenzeRegionali1 rP = modelloB1Dao.findRPrestReg1Utereg1(
						voce.getUtenzeQuotaSocioAssistenziale().get(0).getIDPrestReg1UtenzaReg1(),
						rendicontazione.getAnnoGestione());

				cell = row.createCell(columnCount);
				cell.setCellValue("REG.1");
				cell.setCellStyle(cellStyleCodPrest);

				cell = row.createCell(++columnCount);
				cell.setCellValue(rP.getGregTPrestazioniRegionali1().getCodPrestReg1());
				cell.setCellStyle(cellStyleCodPrest);

				cell = row.createCell(++columnCount);
				cell.setCellValue(rP.getGregTPrestazioniRegionali1().getDesPrestReg1());
				cell.setCellStyle(cellStyleDescPrest);

				total = Double.valueOf(0);
				for (@SuppressWarnings("unused")
				ModelB1Lbl macro : body.getLabels().getMacroaggregati()) {
					cell = row.createCell(++columnCount);
					cell.setCellStyle(cellStyleValueEmpty);
				}
				cell = row.createCell(++columnCount);
				cell.setCellValue(total);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleValueEnd);

				for (ModelB1Lbl utenza : body.getLabels().getUtenze()) {
					cell = row.createCell(++columnCount);
					found = false;
					for (ModelB1UtenzaPrestReg1 utPr : voce.getUtenzeQuotaSocioAssistenziale()) {
						if (utenza.getCodice().equalsIgnoreCase(utPr.getCodice())) {
							found = true;
							if (utPr.getValore() != null) {
								cell.setCellValue(
										Double.valueOf(utPr.getValore().replaceAll("\\.", "").replace(",", ".")));
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							}
						}
					}
					cell.setCellStyle(found ? cellStyleValue : cellStyleValueEmpty);
				}
				cell.setCellStyle(found ? cellStyleValueEnd : cellStyleValueEmptyEnd);

				Iterator<ModelB1Lbl> iterator = body.getLabels().getUtenze().iterator();
				while (iterator.hasNext()) {
					iterator.next();
					cell = row.createCell(++columnCount);
					cell.setCellStyle(cellStyleValueEmpty);
				}
				cell.setCellStyle(cellStyleValueEmptyEnd);

				row.setHeight((short) (row.getHeight() * 3));
			}

		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, columnCount);
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// END

		sheet = workbook.createSheet("Prestazioni Reg1-Reg2");
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

		columnCount = 0;

		// Row 4
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		for (int i = 0; i <= body.getLabels().getMacroaggregati().size()
				+ ((body.getLabels().getUtenze().size())); i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyletitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		autoSizeColumnsAndMergedB1(sheet.getWorkbook());
		columnCount = 0;

		// Row headers
//		sheet.setColumnWidth(columnCount, sheet.getColumnWidth(columnCount)/2);
		row = sheet.createRow(++rowCount);
		sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue("Tipo prestazione");
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		cell = row.createCell(++columnCount);
		sheet.setColumnWidth(columnCount, sheet.getColumnWidth(columnCount) / 2);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header1.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		sheet.setColumnWidth(columnCount - 1, sheet.getColumnWidth(columnCount));

		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(StringEscapeUtils.unescapeJava(header2.getTestoMsgInformativo()));
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		sheet.setColumnWidth(columnCount, 60 * 256);

		cell = row.createCell(++columnCount);
		for (int i = 0; i < body.getLabels().getMacroaggregati().size(); i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount - 1,
				columnCount - (body.getLabels().getMacroaggregati().size()), columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(StringEscapeUtils.unescapeJava(header3.getTestoMsgInformativo()));
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		cell = row.createCell(++columnCount);
		for (int i = 0; i < body.getLabels().getUtenze().size() - 1; i++) {
			row.createCell(++columnCount);
		}
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount - 1,
				columnCount - (body.getLabels().getUtenze().size() - 1), columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header4.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		cell = row.createCell(++columnCount);
		for (int i = 0; i < body.getLabels().getUtenze().size() - 1; i++) {
			row.createCell(++columnCount);
		}

		row.setHeight((short) (row.getHeight() * 3));

		row = sheet.getRow(rowCount);
		columnCount = 2;

		// Row value descs
		for (GregTMacroaggregatiBilancio lblMacro : macroaggregati) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(
					lblMacro.getDesMacroaggregatoBilancio() + " " + lblMacro.getAltraDescMacroaggregatoBilancio());
			cell.setCellStyle(cellStyletitolo3);
			sheet.setColumnWidth(columnCount, 20 * 256);
		}
		cell = row.createCell(++columnCount);
		cell.setCellValue("Totale");
		cell.setCellStyle(cellStyletitolo3Tot);
		sheet.setColumnWidth(columnCount, 20 * 256);

		for (ModelB1Lbl lblUtenze : body.getLabels().getUtenze()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(lblUtenze.getDescrizione());
			cell.setCellStyle(cellStyletitolo3);
			sheet.setColumnWidth(columnCount, 20 * 256);
		}
		cell.setCellStyle(cellStyletitolo3Tot);

		row.setHeight((short) (row.getHeight() * 20));

		// rows prestazioni
		for (ModelB1Voci voce : body.getDatiB1()) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			cell.setCellValue("REG.1");
			cell.setCellStyle(cellStyleCodPrest);

			cell = row.createCell(++columnCount);
			cell.setCellValue(voce.getCodPrestazione());
			cell.setCellStyle(cellStyleCodPrest);

			cell = row.createCell(++columnCount);
			cell.setCellValue(voce.getDescPrestazione());
			cell.setCellStyle(cellStyleDescPrest);

			boolean found = false;
			Double total = Double.valueOf(0);
			for (ModelB1Lbl macro : body.getLabels().getMacroaggregati()) {
				cell = row.createCell(++columnCount);
				found = false;
				for (ModelB1Macroaggregati macroVal : voce.getMacroaggregati()) {
					if (macro.getCodice().equalsIgnoreCase(macroVal.getCodice())) {
						found = true;
						if (macroVal.getValore() != null) {
							cell.setCellValue(
									Double.valueOf(macroVal.getValore().replaceAll("\\.", "").replace(",", ".")));
							total = total
									+ Double.valueOf(macroVal.getValore().replaceAll("\\.", "").replace(",", "."));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					}
				}
				cell.setCellStyle(found ? cellStyleValue : cellStyleValueEmpty);
			}
			cell = row.createCell(++columnCount);
			cell.setCellValue(total);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleValueEnd);

			List<ModelB1UtenzaPrestReg1> utenze = voce.getUtenze() == null || voce.getUtenze().isEmpty()
					? voce.getUtenzeCostoTotale()
					: voce.getUtenze();
			for (ModelB1Lbl utenza : body.getLabels().getUtenze()) {
				cell = row.createCell(++columnCount);
				found = false;
				for (ModelB1UtenzaPrestReg1 utVal : utenze) {
					if (utenza.getCodice().equalsIgnoreCase(utVal.getCodice())) {
						found = true;
						if (utVal.getValore() != null) {
							cell.setCellValue(
									Double.valueOf(utVal.getValore().replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					}
				}
				cell.setCellStyle(found ? cellStyleValue : cellStyleValueEmpty);
			}
			cell.setCellStyle(found ? cellStyleValueEnd : cellStyleValueEmptyEnd);

			row.setHeight((short) (row.getHeight() * 3));

			// REG.2
			if (voce.getPrestazioniRegionali2() != null && !voce.getPrestazioniRegionali2().isEmpty()) {
				for (ModelB1VociPrestReg2 reg2 : voce.getPrestazioniRegionali2()) {
					columnCount = 0;
					row = sheet.createRow(++rowCount);

					cell = row.createCell(columnCount);
					cell.setCellValue("REG.2");
					cell.setCellStyle(cellStyleCodPrest2);

					cell = row.createCell(++columnCount);
					cell.setCellValue(reg2.getCodPrestazione());
					cell.setCellStyle(cellStyleCodPrest2);

					cell = row.createCell(++columnCount);
					cell.setCellValue(reg2.getDescPrestazione());
					cell.setCellStyle(cellStyleDescPrest2);

					total = Double.valueOf(0);
					for (@SuppressWarnings("unused")
					ModelB1Lbl macro : body.getLabels().getMacroaggregati()) {
						cell = row.createCell(++columnCount);
						cell.setCellStyle(cellStyleValueEmpty);
					}
					cell = row.createCell(++columnCount);
					cell.setCellValue(total);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellStyle(cellStyleValueEnd);

					for (ModelB1Lbl utenza : body.getLabels().getUtenze()) {
						cell = row.createCell(++columnCount);
						found = false;
						for (ModelB1UtenzaPrestReg2 utPr : reg2.getUtenze()) {
							if (utenza.getCodice().equalsIgnoreCase(utPr.getCodice())) {
								found = true;
								if (utPr.getValore() != null) {
									cell.setCellValue(
											Double.valueOf(utPr.getValore().replaceAll("\\.", "").replace(",", ".")));
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								}
							}
						}
						cell.setCellStyle(found ? cellStyleValue2 : cellStyleValueEmpty);
					}
					cell.setCellStyle(found ? cellStyleValueEnd2 : cellStyleValueEmptyEnd);

					row.setHeight((short) (row.getHeight() * 3));
				}
			}

			// QS
			if (voce.getUtenzeQuotaSocioAssistenziale() != null && !voce.getUtenzeQuotaSocioAssistenziale().isEmpty()) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				GregRPrestReg1UtenzeRegionali1 rP = modelloB1Dao.findRPrestReg1Utereg1(
						voce.getUtenzeQuotaSocioAssistenziale().get(0).getIDPrestReg1UtenzaReg1(),
						rendicontazione.getAnnoGestione());

				cell = row.createCell(columnCount);
				cell.setCellValue("REG.1");
				cell.setCellStyle(cellStyleCodPrest);

				cell = row.createCell(++columnCount);
				cell.setCellValue(rP.getGregTPrestazioniRegionali1().getCodPrestReg1());
				cell.setCellStyle(cellStyleCodPrest);

				cell = row.createCell(++columnCount);
				cell.setCellValue(rP.getGregTPrestazioniRegionali1().getDesPrestReg1());
				cell.setCellStyle(cellStyleDescPrest);

				total = Double.valueOf(0);
				for (@SuppressWarnings("unused")
				ModelB1Lbl macro : body.getLabels().getMacroaggregati()) {
					cell = row.createCell(++columnCount);
					cell.setCellStyle(cellStyleValueEmpty);
				}
				cell = row.createCell(++columnCount);
				cell.setCellValue(total);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleValueEnd);

				for (ModelB1Lbl utenza : body.getLabels().getUtenze()) {
					cell = row.createCell(++columnCount);
					found = false;
					for (ModelB1UtenzaPrestReg1 utPr : voce.getUtenzeQuotaSocioAssistenziale()) {
						if (utenza.getCodice().equalsIgnoreCase(utPr.getCodice())) {
							found = true;
							if (utPr.getValore() != null) {
								cell.setCellValue(
										Double.valueOf(utPr.getValore().replaceAll("\\.", "").replace(",", ".")));
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							}
						}
					}
					cell.setCellStyle(found ? cellStyleValue : cellStyleValueEmpty);
				}
				cell.setCellStyle(found ? cellStyleValueEnd : cellStyleValueEmptyEnd);

				row.setHeight((short) (row.getHeight() * 3));
			}

		}
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, 0, columnCount);
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		// END

		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloB1_" + nomefile, ".xls");
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

	private static void autoSizeColumnsAndMergedB1(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			if (i != 0) {
				Sheet sheet = workbook.getSheetAt(i);
				if (sheet.getPhysicalNumberOfRows() > 0) {
					for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
						Row row = sheet.getRow(j);
						if (j == 3)
							row.setHeight((short) (row.getHeight() * 3));
						else
							row.setHeight((short) (row.getHeight() * 2));
						if (j < 4) {
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
	}

	private static void autoSizeColumnsAndMergedB1Meta(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
					Row row = sheet.getRow(j);
					if (j == 3)
						row.setHeight((short) (row.getHeight() * 3));
					else
						row.setHeight((short) (row.getHeight() * 2));
					if (j < 4) {
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

	@Transactional
	public GenericResponseWarnErr checkModelloB1(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());
		response.setObblMotivazione(false);
		String prestaz = "";
		GregTRendicontazioneEnte rendicontazioneAttuale = datiRendicontazioneService
				.getRendicontazione(idRendicontazione);
		GregTRendicontazioneEnte rendicontazionePassata = datiRendicontazioneService.getRendicontazionePassata(
				rendicontazioneAttuale.getGregTSchedeEntiGestori().getIdSchedaEnteGestore(),
				(rendicontazioneAttuale.getAnnoGestione() - 1));
		if (rendicontazionePassata != null) {
			List<ModelB1Voci> datiModelloAttuale = getVoci(rendicontazioneAttuale.getIdRendicontazioneEnte());
			List<ModelB1Voci> datiModelloPassata = getVoci(rendicontazionePassata.getIdRendicontazioneEnte());

			List<ModelTotalePrestazioniB1> prestazioniAttuali = new ArrayList<ModelTotalePrestazioniB1>();
			List<ModelTotalePrestazioniB1> prestazioniPassate = new ArrayList<ModelTotalePrestazioniB1>();

			for (ModelB1Voci dato : datiModelloAttuale) {
				ModelTotalePrestazioniB1 pre = new ModelTotalePrestazioniB1();
				pre.setCodPrestazione(dato.getCodPrestazione());
				prestazioniAttuali.add(pre);
			}
			for (ModelB1Voci dato : datiModelloPassata) {
				ModelTotalePrestazioniB1 pre = new ModelTotalePrestazioniB1();
				pre.setCodPrestazione(dato.getCodPrestazione());
				prestazioniPassate.add(pre);
			}

			for (ModelTotalePrestazioniB1 pre : prestazioniAttuali) {
				BigDecimal totale = BigDecimal.ZERO;
				for (ModelB1Voci dato : datiModelloAttuale) {
					if (pre.getCodPrestazione().equals(dato.getCodPrestazione())) {
						for (ModelB1Macroaggregati valore : dato.getMacroaggregati()) {
							if (valore.getValore() != null) {
								totale = totale.add(BigDecimal.valueOf(Double.parseDouble(valore.getValore())));
							}
						}
					}
				}
				pre.setTotale(totale);
			}

			for (ModelTotalePrestazioniB1 pre : prestazioniPassate) {
				BigDecimal totale = BigDecimal.ZERO;
				for (ModelB1Voci dato : datiModelloPassata) {
					if (pre.getCodPrestazione().equals(dato.getCodPrestazione())) {
						for (ModelB1Macroaggregati valore : dato.getMacroaggregati()) {
							if (valore.getValore() != null) {
								totale = totale.add(BigDecimal.valueOf(Double.parseDouble(valore.getValore())));
							}
						}
					}
				}
				pre.setTotale(totale);
			}

			for (ModelTotalePrestazioniB1 prePass : prestazioniPassate) {
				for (ModelTotalePrestazioniB1 preAtt : prestazioniAttuali) {
					if (prePass.getCodPrestazione().equals(preAtt.getCodPrestazione())) {
						BigDecimal totale25 = (prePass.getTotale().multiply(new BigDecimal(25)))
								.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
						if (((preAtt.getTotale().subtract(prePass.getTotale())).abs())
								.compareTo(new BigDecimal(100000)) > 0) {
							if ((preAtt.getTotale().setScale(2))
									.compareTo(prePass.getTotale().subtract(totale25).setScale(2)) < 0
									|| (preAtt.getTotale().setScale(2))
											.compareTo(prePass.getTotale().add(totale25).setScale(2)) > 0) {

								prestaz += preAtt.getCodPrestazione() + ", ";
								response.setObblMotivazione(true);
							}
						}
					}
				}
			}

			if (response.isObblMotivazione()) {
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_B101)
						.getTestoMessaggio().replace("PRESTAZIONI", prestaz));
				if (response.getWarnings().size() > 0) {
					List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_B1,
							rendicontazioneAttuale.getIdRendicontazioneEnte());
					if (checkA.size() > 0) {
						response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MOTIVAZIONE_ESISTE)
								.getTestoMessaggio());
					}
				}

				response.setDescrizione("");
			} else {
				response.setDescrizione(listeService.getMessaggio(SharedConstants.CHECK_OK).getTestoMessaggio());
				response.setId("OK");
			}
		}
		return response;
	}

	public String getStatoModelloEnte(Integer idRendicontazione, String codTranche, String codStatoRendicontazione) {
		ModelStatoMod stato = modelloB1Dao.getStatoModelloB1(idRendicontazione);
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
	public GenericResponseWarnErr controlloModelloB1(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_B1);
		boolean facoltativo = false;
		boolean valorizzato = modelloB1Dao.getValorizzatoModelloB1(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO)
					.getTestoMessaggio().replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		// Controlli Modello B1
		List<ModelB1Voci> rendModB1 = getVoci(rendicontazione.getIdRendicontazioneEnte());

		// CONTROLLO 1, per ogni Prestazione il campo acquisto beni B deve essere
		// valorizzato se campo reddito e' valorizzato
		for (ModelB1Voci prestazione : rendModB1) {
			BigDecimal valReddito = null;
			BigDecimal valAcquistoB = null;
			for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
				if (macroaggregato.getCodice().equals("1VB") && macroaggregato.getValore() != null) {
					valReddito = Util.convertStringToBigDecimal(macroaggregato.getValore().replace(".", ","));
				}
				if (macroaggregato.getCodice().equals("4VB") && macroaggregato.getValore() != null) {
					valAcquistoB = Util.convertStringToBigDecimal(macroaggregato.getValore().replace(".", ","));
				}
			}
			if (valReddito == null && valAcquistoB != null) {
				response.getErrors().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_01)
						.getTestoMessaggio().replace("PRESTAZIONE",
								"'" + prestazione.getCodPrestazione() + " " + prestazione.getDescPrestazione() + "'"));
			}
		}

		// CONTROLLO 2, verifica Totali Progressivi Automatici coincidano con Totali
		// Macroaggregati
		List<ModelTotaleMacroaggregati> totaliMacro = macroaggregatiService
				.getRendicontazioneTotaliMacroaggregatiPerB1(rendicontazione.getIdRendicontazioneEnte())
				.getValoriMacroaggregati();
		BigDecimal totaleRedditoDipendente = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleAltreImposte = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleAcquistoBeniA = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleAcquistoBeniB = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleTrasferimentiCorrenti = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleInteressiPassivi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleAltreSpese = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

		for (ModelB1Voci prestazione : rendModB1) {
			for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
				if (macroaggregato.getCodice().equals("1VB") && macroaggregato.getValore() != null) {
					totaleRedditoDipendente = totaleRedditoDipendente.add(new BigDecimal(macroaggregato.getValore())
							.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				}
				if (macroaggregato.getCodice().equals("2VB") && macroaggregato.getValore() != null) {
					totaleAltreImposte = totaleAltreImposte.add(new BigDecimal(macroaggregato.getValore())
							.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				}
				if (macroaggregato.getCodice().equals("3VB") && macroaggregato.getValore() != null) {
					totaleAcquistoBeniA = totaleAcquistoBeniA.add(new BigDecimal(macroaggregato.getValore())
							.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				}
				if (macroaggregato.getCodice().equals("4VB") && macroaggregato.getValore() != null) {
					totaleAcquistoBeniB = totaleAcquistoBeniB.add(new BigDecimal(macroaggregato.getValore())
							.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				}
				if (macroaggregato.getCodice().equals("5VB") && macroaggregato.getValore() != null) {
					totaleTrasferimentiCorrenti = totaleTrasferimentiCorrenti
							.add(new BigDecimal(macroaggregato.getValore()).setScale(2, RoundingMode.HALF_UP)
									.stripTrailingZeros());
				}
				if (macroaggregato.getCodice().equals("6VB") && macroaggregato.getValore() != null) {
					totaleInteressiPassivi = totaleInteressiPassivi.add(new BigDecimal(macroaggregato.getValore())
							.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				}
				if (macroaggregato.getCodice().equals("7VB") && macroaggregato.getValore() != null) {
					totaleAltreSpese = totaleAltreSpese.add(new BigDecimal(macroaggregato.getValore())
							.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				}
			}
		}
		if (!facoltativo) {
			if (!totaleRedditoDipendente.toPlainString()
					.equals(totaliMacro.get(0).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
					|| !totaleAltreImposte.toPlainString()
							.equals(totaliMacro.get(1).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
					|| !totaleAcquistoBeniA.toPlainString()
							.equals(totaliMacro.get(2).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
					|| !totaleAcquistoBeniB.toPlainString()
							.equals(totaliMacro.get(3).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
					|| !totaleTrasferimentiCorrenti.toPlainString()
							.equals(totaliMacro.get(4).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
					|| !totaleInteressiPassivi.toPlainString()
							.equals(totaliMacro.get(5).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
					|| !totaleAltreSpese.toPlainString()
							.equals(totaliMacro.get(6).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())) {
				response.getErrors().add(
						listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_02).getTestoMessaggio());

			}
		}

		// CONTROLLO 3, verifica Totali Prestazioni coincidente con Totali Utenze
		for (ModelB1Voci prestazione : rendModB1) {
			BigDecimal totalePrestazione = BigDecimal.ZERO;
			BigDecimal totaleUtenza = BigDecimal.ZERO;
			// Totale Prestazione
			for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
				if (macroaggregato.getValore() != null) {
					totalePrestazione = totalePrestazione
							.add(Util.convertStringToBigDecimal(macroaggregato.getValore().replace(".", ",")));
				}
			}
			// Totale Utenza
			if (prestazione.getTipoPrestazione().equals("MA03")) {
				for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
					if (utenza.getValore() != null) {
						totaleUtenza = totaleUtenza
								.add(Util.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
					}
				}
			} else {
				for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
					if (utenza.getValore() != null) {
						totaleUtenza = totaleUtenza
								.add(Util.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
					}
				}
			}
			if (!facoltativo) {
				if (!totalePrestazione.equals(totaleUtenza)) {
					response.getErrors()
							.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_03)
									.getTestoMessaggio().replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione()
											+ " " + prestazione.getDescPrestazione() + "'"));
				}
			}
		}
		
		for (ModelB1Voci prestazione : rendModB1) {
			if(prestazione.getUtenzeQuotaSocioAssistenziale().size()>0) {
				for(int i=0; i<prestazione.getUtenzeQuotaSocioAssistenziale().size(); i++) {
					if(prestazione.getUtenzeCostoTotale().get(i).getValore()!=null &&
							(prestazione.getUtenzeQuotaSocioAssistenziale().get(i).getValore()==null || Util.convertStringToBigDecimal(prestazione.getUtenzeQuotaSocioAssistenziale().get(i).getValore() != null
									? prestazione.getUtenzeQuotaSocioAssistenziale().get(i).getValore().replace(".", ",")
									: "0,00").equals(BigDecimal.ZERO.setScale(2)))) {
						response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_11)
								.getTestoMessaggio().replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione()
										+ " " + prestazione.getDescPrestazione() + "'").replace("TARGET", "'" + prestazione.getUtenzeCostoTotale().get(i).getCodice()
										+ " " + prestazione.getUtenzeCostoTotale().get(i).getDescUtenza() + "'"));
					}
				}
			}
			
		}

		// Verifica somma utenze prestazioni regionali 2 con utenze prestazioni
		// regionali 1
		List<ModelListeConfiguratore> utenze = configuratorePrestazioniDao.findUtenza();
		for (ModelB1Voci prestazione : rendModB1) {
			if (prestazione.getUtenze().size() > 0) {
				ModelTotalePrestazioniB1[] totaliUtenzePrest2 = new ModelTotalePrestazioniB1[utenze.size()];
				for (int i = 0; i < utenze.size(); i++) {
					totaliUtenzePrest2[i] = new ModelTotalePrestazioniB1();
					totaliUtenzePrest2[i].setCodPrestazione(utenze.get(i).getCodice());
					totaliUtenzePrest2[i].setTotale(BigDecimal.ZERO.setScale(2));
				}
				for (ModelB1VociPrestReg2 prest2 : prestazione.getPrestazioniRegionali2()) {
					for (ModelB1UtenzaPrestReg2 utenza : prest2.getUtenze()) {
						for (int i = 0; i < utenze.size(); i++) {
							if (totaliUtenzePrest2[i].getCodPrestazione().equals(utenza.getCodice())) {
								totaliUtenzePrest2[i]
										.setTotale(totaliUtenzePrest2[i].getTotale()
												.add(Util.convertStringToBigDecimal(utenza.getValore() != null
														? utenza.getValore().replace(".", ",")
														: "0,00")));
							}
						}
					}
				}

				for (ModelB1UtenzaPrestReg1 u : prestazione.getUtenze()) {
					for (int i = 0; i < utenze.size(); i++) {
						if (u.getCodice().equals(totaliUtenzePrest2[i].getCodPrestazione())) {
							if (!totaliUtenzePrest2[i].getTotale().equals(BigDecimal.ZERO.setScale(2))
									&& totaliUtenzePrest2[i] != null
									&& !Util.convertStringToBigDecimal(
											u.getValore() != null ? u.getValore().replace(".", ",") : "0,00")
											.equals(totaliUtenzePrest2[i].getTotale())) {
								response.getWarnings()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_10)
												.getTestoMessaggio()
												.replace("PRESTAZIONE",
														"'" + prestazione.getCodPrestazione() + " "
																+ prestazione.getDescPrestazione() + "'")
												.replace("UTENZA", "'" + u.getDescUtenza() + "'"));
							}
						}
					}
				}
			} else if (prestazione.getUtenzeCostoTotale().size() > 0) {
				ModelTotalePrestazioniB1[] totaliUtenzePrest2 = new ModelTotalePrestazioniB1[utenze.size()];
				for (int i = 0; i < utenze.size(); i++) {
					totaliUtenzePrest2[i] = new ModelTotalePrestazioniB1();
					totaliUtenzePrest2[i].setCodPrestazione(utenze.get(i).getCodice());
					totaliUtenzePrest2[i].setTotale(BigDecimal.ZERO.setScale(2));
				}
				for (ModelB1VociPrestReg2 prest2 : prestazione.getPrestazioniRegionali2()) {
					for (ModelB1UtenzaPrestReg2 utenza : prest2.getUtenze()) {
						for (int i = 0; i < utenze.size(); i++) {
							if (totaliUtenzePrest2[i].getCodPrestazione().equals(utenza.getCodice())) {
								totaliUtenzePrest2[i]
										.setTotale(totaliUtenzePrest2[i].getTotale()
												.add(Util.convertStringToBigDecimal(utenza.getValore() != null
														? utenza.getValore().replace(".", ",")
														: "0,00")));
							}
						}
					}
				}
				for (ModelB1UtenzaPrestReg1 u : prestazione.getUtenzeCostoTotale()) {
					for (int i = 0; i < utenze.size(); i++) {
						if (u.getCodice().equals(totaliUtenzePrest2[i].getCodPrestazione())) {
							if (!totaliUtenzePrest2[i].getTotale().equals(BigDecimal.ZERO.setScale(2))
									&& totaliUtenzePrest2[i] != null
									&& !Util.convertStringToBigDecimal(
											u.getValore() != null ? u.getValore().replace(".", ",") : "0,00")
											.equals(totaliUtenzePrest2[i].getTotale())) {
								response.getWarnings()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_10)
												.getTestoMessaggio()
												.replace("PRESTAZIONE",
														"'" + prestazione.getCodPrestazione() + " "
																+ prestazione.getDescPrestazione() + "'")
												.replace("UTENZA", "'" + u.getDescUtenza() + "'"));
							}
						}
					}
				}
			}
		}

		// CONTROLLO 4, verifica tra Entrate (ASR,UT) e Spese (QS) tra Modello A e
		// Modello B1
		Map<String, Map<String, BigDecimal>> prestUteValModA = new HashMap<String, Map<String, BigDecimal>>();
		Map<String, BigDecimal> uteValModA = null;
		// Dal Modello A calcolo le entrate (ASR e UT) per ogni utenza
		List<ModelTitoloModA> titoliModA = modelloAService.getListaVociModA(rendicontazione.getIdRendicontazioneEnte())
				.getListaTitoli();
		for (ModelTitoloModA titolo : titoliModA) {
			for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
				for (ModelVoceModA voce : tipologia.getListaVoci()) {
					if (voce.getPrestazioni() != null) {
						// Considero le Prestazioni ASR
						List<ModelPrestazioneUtenzaModA> prestazioniASR = voce.getPrestazioni().getPrestazioniRS();
						for (ModelPrestazioneUtenzaModA prestazione : prestazioniASR) {
							String codPrestASR = prestazione.getCodPrestazione().replace("_ASR", "").replace("_UT", "");
							// Creo o prelevo la Mappa <CodUtenza, Valore> e ciclo sulle utenze
							if (prestUteValModA.get(codPrestASR) != null) {
								uteValModA = prestUteValModA.get(codPrestASR);
							} else {
								uteValModA = new HashMap<String, BigDecimal>();
							}
							for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
								if (utenza.getValore() != null) {
									if (uteValModA.get(utenza.getCodTargetUtenza()) != null) {
										BigDecimal oldValue = uteValModA.get(utenza.getCodTargetUtenza());
										BigDecimal newValue = utenza.getValore();
										uteValModA.replace(utenza.getCodTargetUtenza(), oldValue.add(newValue));
									} else {
										uteValModA.put(utenza.getCodTargetUtenza(), utenza.getValore());
									}
								}
							}
							if (prestUteValModA.get(codPrestASR) != null) {
								prestUteValModA.replace(codPrestASR, uteValModA);
							} else {
								prestUteValModA.put(codPrestASR, uteValModA);
							}
						}
						// Considero le Prestazioni UT
						List<ModelPrestazioneUtenzaModA> prestazioniUT = voce.getPrestazioni().getPrestazioniCD();
						for (ModelPrestazioneUtenzaModA prestazione : prestazioniUT) {
							String codPrestUT = prestazione.getCodPrestazione().replace("_ASR", "").replace("_UT", "");
							// Creo o prelevo la Mappa <CodUtenza, Valore> e ciclo sulle utenze
							if (prestUteValModA.get(codPrestUT) != null) {
								uteValModA = prestUteValModA.get(codPrestUT);
							} else {
								uteValModA = new HashMap<String, BigDecimal>();
							}
							for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
								if (utenza.getValore() != null) {
									if (uteValModA.get(utenza.getCodTargetUtenza()) != null) {
										BigDecimal oldValue = uteValModA.get(utenza.getCodTargetUtenza());
										BigDecimal newValue = utenza.getValore();
										uteValModA.replace(utenza.getCodTargetUtenza(), oldValue.add(newValue));
									} else {
										uteValModA.put(utenza.getCodTargetUtenza(), utenza.getValore());
									}
								}
							}
							if (prestUteValModA.get(codPrestUT) != null) {
								prestUteValModA.replace(codPrestUT, uteValModA);
							} else {
								prestUteValModA.put(codPrestUT, uteValModA);
							}
						}
					}
				}
			}
		}

		for (String uteMapKey : prestUteValModA.keySet()) {
			Map<String, BigDecimal> mappa = prestUteValModA.get(uteMapKey);
			ModelB1Voci prestModB1 = new ModelB1Voci();
			for (ModelB1Voci prestazione : rendModB1) {
				if (prestazione.getCodPrestazione().equals(uteMapKey)) {
					prestModB1 = prestazione;
				}
			}
			// Ciclo sui valori ed effettuo il controllo
			// Ciclo sulle entrate del Mod.A
			Boolean entrateVal = false;
			BigDecimal entrateModA = BigDecimal.ZERO;
			for (BigDecimal value : mappa.values()) {
				if (value != null) {
					entrateVal = true;
					entrateModA = entrateModA.add(value);
				}
			}
			// Ciclo sulle spese del Mod.B1
			Boolean usciteVal = false;
			Boolean usciteObbligVal = true;
			BigDecimal usciteModB1 = BigDecimal.ZERO;
			if (prestModB1.getTipoPrestazione().equals("MA03")) {
				for (ModelB1UtenzaPrestReg1 entryValue : prestModB1.getUtenzeCostoTotale()) {
					if (entryValue.getValore() != null) {
						if (entryValue.isMandatory()) {
							usciteObbligVal = usciteObbligVal && true;
						}
						usciteVal = true;
						usciteModB1 = usciteModB1
								.add(Util.convertStringToBigDecimal(entryValue.getValore().replace(".", ",")));
					} else {
						if (entryValue.isMandatory()) {
							usciteObbligVal = usciteObbligVal && false;
						}
					}
				}
			} else {
				for (ModelB1UtenzaPrestReg1 entryValue : prestModB1.getUtenze()) {
					if (entryValue.getValore() != null) {
						usciteVal = true;
						usciteModB1 = usciteModB1
								.add(Util.convertStringToBigDecimal(entryValue.getValore().replace(".", ",")));
					}
				}
			}
			if (!facoltativo) {
				// CONTROLLO 4.1
				if (!usciteObbligVal) {
					response.getErrors()
							.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_04)
									.getTestoMessaggio().replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione()
											+ " " + prestModB1.getDescPrestazione() + "'"));
				}

				// CONTROLLO 4.2
				if (!entrateModA.equals(BigDecimal.ZERO) && !usciteModB1.equals(BigDecimal.ZERO)
						&& usciteModB1.compareTo(entrateModA) == -1) {
					response.getErrors()
							.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_05)
									.getTestoMessaggio().replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione()
											+ " " + prestModB1.getDescPrestazione() + "'"));
				}

				// CONTROLLO 4.3
				BigDecimal diffUsciteEntrate = usciteModB1.subtract(entrateModA).setScale(2, RoundingMode.HALF_UP);
				if (!entrateModA.equals(BigDecimal.ZERO) && !usciteModB1.equals(BigDecimal.ZERO)
						&& diffUsciteEntrate.compareTo(BigDecimal.ZERO) == -1) {
					response.getErrors()
							.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_06)
									.getTestoMessaggio().replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione()
											+ " " + prestModB1.getDescPrestazione() + "'"));
				}

			}
			// CONTROLLO 4.4 WARNING
			if (!entrateVal && usciteVal) {
				response.getWarnings().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_08)
						.getTestoMessaggio().replace("PRESTAZIONE",
								"'" + prestModB1.getCodPrestazione() + " " + prestModB1.getDescPrestazione() + "'"));
			}
		}

		// CONTROLLO 5, Controllo su Spese Prestazioni e 1% del Totale Macroaggregati
		// Calcolo 1% del Totale Macroaggregati
		BigDecimal percentValue = BigDecimal.ZERO;
		List<ModelTotaleMacroaggregati> totaliMacroaggregati = macroaggregatiService
				.getRendicontazioneTotaliMacroaggregatiPerB1(rendicontazione.getIdRendicontazioneEnte())
				.getValoriMacroaggregati();
		for (ModelTotaleMacroaggregati totale : totaliMacroaggregati) {
			if (totale.getCodMacroaggregati().equals("") && totale.getDescMacroaggregati().equals("TOTALE")) {
				percentValue = totale.getTotale().multiply(new BigDecimal(0.01)).setScale(2, RoundingMode.HALF_UP);
			}
		}
		for (ModelB1Voci prestazione : rendModB1) {
			if (prestazione.getCodPrestazione().equals("R_A.2.3") || prestazione.getCodPrestazione().equals("R_B.8.2")
					|| prestazione.getCodPrestazione().equals("R_C.3.4")
					|| prestazione.getCodPrestazione().equals("R_D.1.2")
					|| prestazione.getCodPrestazione().equals("R_E.4.2")) {

				BigDecimal totaleUtenza = BigDecimal.ZERO;

				if (prestazione.getTipoPrestazione().equals("MA03")) {
					for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
						if (utenza.getValore() != null) {
							totaleUtenza = totaleUtenza
									.add(Util.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
						}
					}
				} else {
					for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
						if (utenza.getValore() != null) {
							totaleUtenza = totaleUtenza
									.add(Util.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
						}
					}
				}

				if (totaleUtenza.compareTo(percentValue) == 1) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_07)
									.getTestoMessaggio().replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione()
											+ " " + prestazione.getDescPrestazione() + "'"));
				}
			}
		}

		// CONTROLLO 6, verifica valorizzazioni per Macroaggregati e Utenze, per ogni
		// Prestazione
		if (!facoltativo) {
			String prestNotVal = "";
			for (ModelB1Voci prestazione : rendModB1) {
				Boolean macroValorizzato = false;
				Boolean utenzaValorizzata = false;
				// Valorizzato Macroaggregato
				for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
					if (macroaggregato.getValore() != null) {
						macroValorizzato = true;
					}
				}
				// Valorizzata Utenza
				if (prestazione.getTipoPrestazione().equals("MA03")) {
					for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
						if (utenza.getValore() != null) {
							utenzaValorizzata = true;
						}
					}
				} else {
					for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
						if (utenza.getValore() != null) {
							utenzaValorizzata = true;
						}
					}
				}

				if (!macroValorizzato && !utenzaValorizzata) {
					prestNotVal = prestNotVal + prestazione.getCodPrestazione() + ", ";
				}
			}
			if (!prestNotVal.equals("")) {
				response.getErrors()
						.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_09).getTestoMessaggio()
								.replace("PRESTAZIONI", prestNotVal.substring(0, prestNotVal.length() - 2)));
			}
		}
		
		// CONTROLLO 7 SFU V.05
		BigDecimal sommaValoriB1TargetFamiglia = BigDecimal.ZERO;
		for (ModelB1Voci prestazione : rendModB1) {
			for (int i = 0; i < prestazione.getUtenze().size(); i++) {
				if(prestazione.getUtenze().get(i).getCodice().equals("U01")) {
					String valore = prestazione.getUtenze().get(i).getValore();
					if (valore != null) {
						sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia.add(Util.convertStringToBigDecimal(valore.replace(".", ",")));											
					}
				}
			}
			for (int i = 0; i < prestazione.getUtenzeCostoTotale().size(); i++) {
				if(prestazione.getUtenzeCostoTotale().get(i).getCodice().equals("U01")) {
					String valore = prestazione.getUtenzeCostoTotale().get(i).getValore();
					if (valore != null) {
						sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia.add(Util.convertStringToBigDecimal(valore.replace(".", ",")));											
					}
				}
			}
		}
		BigDecimal fondoFNPS = BigDecimal.ZERO;
		List<ModelFondi> fondi = configuratoreUtenzeFnpsService.findFondiByidRendicontazione(rendicontazione.getIdRendicontazioneEnte());
		ModelFondi fondo = fondi.stream().filter(item -> item.getCodTipologiaFondo().equals("FONDO")).findFirst().orElse(null);
		if (fondo == null || (fondo != null && fondo.getValore().equals(BigDecimal.ZERO))) {
			fondoFNPS = BigDecimal.ZERO;
		} else {
			fondoFNPS = fondo.getValore().divide(new BigDecimal(2));
		}
		FondiEnteAllontanamentoZero fondiAlZero = modelloAllontanamentoZeroService.findFondiEnteAllontanamentoZero(BigInteger.valueOf(rendicontazione.getIdRendicontazioneEnte()));
		BigDecimal quotaAlZero = fondiAlZero.getQuotaAllontanamentoZero();
		// GET VOCI MODELLO A
		List<ModelTitoloModA> titoliModAControllo7 = modelloAService.getListaVociModA(rendicontazione.getIdRendicontazioneEnte())
				.getListaTitoli();
		for (ModelTitoloModA titolo : titoliModAControllo7) {
			for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
				for (ModelVoceModA voce : tipologia.getListaVoci()) {
					if(voce.getPrestazioni() != null) {
						for (ModelPrestazioneUtenzaModA prestazioneCD : voce.getPrestazioni().getPrestazioniCD()) {
							for (ModelTargetUtenza targetUtenza : prestazioneCD.getListaTargetUtenza()) {
								if(targetUtenza.getCodTargetUtenza().equals("U01")) {
									if (targetUtenza.getValore() != null) {
										sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia.subtract(targetUtenza.getValore());															
									}
								}
							}
						}
						for (ModelPrestazioneUtenzaModA prestazioneRS : voce.getPrestazioni().getPrestazioniRS()) {
							for (ModelTargetUtenza targetUtenza : prestazioneRS.getListaTargetUtenza()) {
								if(targetUtenza.getCodTargetUtenza().equals("U01")) {
									if (targetUtenza.getValore() != null) {
										sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia.subtract(targetUtenza.getValore());															
									}
								}
							}
						}
					}
				}
			}
		}
		// CONTROLLO
		if (quotaAlZero != null) {
			if (!quotaAlZero.equals(BigDecimal.ZERO) && !fondoFNPS.equals(BigDecimal.ZERO)) {
				BigDecimal sommaFNPSALZERO = fondoFNPS.add(quotaAlZero);
				if(sommaValoriB1TargetFamiglia.compareTo(sommaFNPSALZERO) < 0) {
					response.getWarnings()
					.add(listeService
							.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_12)
							.getTestoMessaggio()
							);
				}									
			}
		}
		
		return response;
	}
}
