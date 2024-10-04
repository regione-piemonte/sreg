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
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
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

import it.csi.greg.gregsrv.business.dao.impl.ModelloCDao;
import it.csi.greg.gregsrv.business.entity.GregDDettaglioDisabilita;
import it.csi.greg.gregsrv.business.entity.GregDDettaglioUtenze;
import it.csi.greg.gregsrv.business.entity.GregDDisabilita;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRPreg1DisabTargetUtenzaDettDisab;
import it.csi.greg.gregsrv.business.entity.GregRPreg1DisabilitaTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeModc;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeModcDettUtenze;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte3;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte4;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloCInput;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelBMissioni;
import it.csi.greg.gregsrv.dto.ModelBProgramma;
import it.csi.greg.gregsrv.dto.ModelBSottotitolo;
import it.csi.greg.gregsrv.dto.ModelBTitolo;
import it.csi.greg.gregsrv.dto.ModelCDettagliDisabilita;
import it.csi.greg.gregsrv.dto.ModelCDettagliUtenze;
import it.csi.greg.gregsrv.dto.ModelCDisabilita;
import it.csi.greg.gregsrv.dto.ModelCPrestazioni;
import it.csi.greg.gregsrv.dto.ModelCTargetUtenze;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelPrestazioniAssociate;
import it.csi.greg.gregsrv.dto.ModelRendModAPart3;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModB;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModC;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelTotalePrestazioniB1;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.dto.VociModelloA;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloCService")
public class ModelloCService {

	@Autowired
	protected ModelloCDao modelloCDao;
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
	@Autowired
	protected DatiEnteService dataEnteService;

	public List<ModelCPrestazioni> getPrestazioni(Integer idRend) {
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRend);
		List<GregTPrestazioniRegionali1> prestazioni = modelloCDao
				.findAllPrestazioni(rendicontazione.getAnnoGestione());
		List<GregDTargetUtenza> targetUtenze = modelloCDao.findAllTarget();
		List<GregRPrestReg1UtenzeModc> prestUtenze = modelloCDao
				.findAllPrestazioniUtenza(rendicontazione.getAnnoGestione());
		List<GregDDettaglioUtenze> dettagliUtenze = modelloCDao.findAllDettagliUtenze();
		List<GregRPrestReg1UtenzeModcDettUtenze> prestUtenzeDettagliUtenze = modelloCDao
				.findAllPrestazioniUtenzaDettagliUtenze(rendicontazione.getAnnoGestione());
		List<GregDDisabilita> disabilita = modelloCDao.findAllDisabilita();
		List<GregRPreg1DisabilitaTargetUtenza> prestDisabilitaTargetUtenze = modelloCDao
				.findAllTargetDisabilitaPrestazioni(rendicontazione.getAnnoGestione());
		List<GregDDettaglioDisabilita> dettagliDisabilita = modelloCDao.findAllDettaglioDisabilita();
		List<GregRPreg1DisabTargetUtenzaDettDisab> prestDisabilitaTargetUtenzeDettagliDisabilita = modelloCDao
				.findAllTargetDisabilitaPrestazioniDettaglioDisabilita(rendicontazione.getAnnoGestione());

		List<ModelCPrestazioni> listaPrestazioni = new ArrayList<ModelCPrestazioni>();

		for (GregTPrestazioniRegionali1 prestazione : prestazioni) {
			if (prestazione.getCodPrestReg1().equals("R_A.1.1") || prestazione.getCodPrestReg1().equals("R_A.2.1")) {
				ModelCPrestazioni prestazioneC = new ModelCPrestazioni();
				prestazioneC.setIdPrestazione(prestazione.getIdPrestReg1());
				prestazioneC.setCodPrestazione(prestazione.getCodPrestReg1());
				prestazioneC.setDescPrestazione(prestazione.getDesPrestReg1());
				prestazioneC.setInformativa(prestazione.getInformativa());

				List<ModelCTargetUtenze> listaTargetUtenze = new ArrayList<ModelCTargetUtenze>();

				for (GregDTargetUtenza targetUtenza : targetUtenze) {
					for (GregRPrestReg1UtenzeModc prestUtenza : prestUtenze) {
						if (prestUtenza.getGregDTargetUtenza().getCodUtenza().equals(targetUtenza.getCodUtenza())
								&& prestUtenza.getGregTPrestazioniRegionali1().getCodPrestReg1()
										.equals(prestazioneC.getCodPrestazione())) {
							ModelCTargetUtenze targetUtenzeC = new ModelCTargetUtenze();
							targetUtenzeC.setIdTargetUtenze(targetUtenza.getIdTargetUtenza());
							targetUtenzeC.setCodTargetUtenze(targetUtenza.getCodUtenza());
							targetUtenzeC.setDescTargetUtenze(targetUtenza.getDesUtenza());

							List<ModelCDettagliUtenze> listaDettagliUtenze = new ArrayList<ModelCDettagliUtenze>();

							for (GregDDettaglioUtenze dettagliUtenza : dettagliUtenze) {
								for (GregRPrestReg1UtenzeModcDettUtenze prestUtenzeDettagliUtenza : prestUtenzeDettagliUtenze) {

									ModelCDettagliUtenze dettagliUtenzaC = null;

									if (prestUtenzeDettagliUtenza.getGregDDettaglioUtenze().getCodDettaglioUtenza()
											.equals(dettagliUtenza.getCodDettaglioUtenza())
											&& prestUtenzeDettagliUtenza.getGregRPrestReg1UtenzeModc()
													.getGregDTargetUtenza().getCodUtenza()
													.equals(targetUtenzeC.getCodTargetUtenze())
											&& prestUtenzeDettagliUtenza.getGregRPrestReg1UtenzeModc()
													.getGregTPrestazioniRegionali1().getCodPrestReg1()
													.equals(prestazioneC.getCodPrestazione())) {
										dettagliUtenzaC = new ModelCDettagliUtenze();
										dettagliUtenzaC.setIdDettagliUtenze(dettagliUtenza.getIdDettaglioUtenza());
										dettagliUtenzaC.setCodDettagliUtenze(dettagliUtenza.getCodDettaglioUtenza());
										dettagliUtenzaC.setDescDettagliUtenze(dettagliUtenza.getDescDettaglioUtenza());
										listaDettagliUtenze.add(dettagliUtenzaC);
									}
								}
							}

							targetUtenzeC.setListaDettagliUtenze(listaDettagliUtenze);

							List<ModelCDisabilita> listaDisabilita = new ArrayList<ModelCDisabilita>();

							for (GregDDisabilita disa : disabilita) {
								for (GregRPreg1DisabilitaTargetUtenza prestDisabilitaTargetUtenza : prestDisabilitaTargetUtenze) {

									ModelCDisabilita disabilitaC = null;

									if (prestDisabilitaTargetUtenza.getGregRDisabilitaTargetUtenza()
											.getGregDDisabilita().getCodDisabilita().equals(disa.getCodDisabilita())
											&& prestDisabilitaTargetUtenza.getGregRDisabilitaTargetUtenza()
													.getGregDTargetUtenza().getCodUtenza()
													.equals(targetUtenzeC.getCodTargetUtenze())
											&& prestDisabilitaTargetUtenza.getGregTPrestazioniRegionali1()
													.getCodPrestReg1().equals(prestazioneC.getCodPrestazione())) {
										disabilitaC = new ModelCDisabilita();
										disabilitaC.setIdDisabilita(disa.getIdDisabilita());
										disabilitaC.setCodDisabilita(disa.getCodDisabilita());
										disabilitaC.setDescDisabilita(disa.getDesDisabilita());

										List<ModelCDettagliDisabilita> listaDettagliDisabilita = new ArrayList<ModelCDettagliDisabilita>();

										for (GregDDettaglioDisabilita dettagliDisa : dettagliDisabilita) {
											for (GregRPreg1DisabTargetUtenzaDettDisab prestDisabilitaTargetUtenzeDettagliDisa : prestDisabilitaTargetUtenzeDettagliDisabilita) {

												ModelCDettagliDisabilita dettagliDisabilitaC = null;

												if (prestDisabilitaTargetUtenzeDettagliDisa
														.getGregDDettaglioDisabilita().getCodDettaglioDisabilita()
														.equals(dettagliDisa.getCodDettaglioDisabilita())
														&& prestDisabilitaTargetUtenzeDettagliDisa
																.getGregRPreg1DisabilitaTargetUtenza()
																.getGregTPrestazioniRegionali1().getCodPrestReg1()
																.equals(prestazioneC.getCodPrestazione())
														&& prestDisabilitaTargetUtenzeDettagliDisa
																.getGregRPreg1DisabilitaTargetUtenza()
																.getGregRDisabilitaTargetUtenza().getGregDTargetUtenza()
																.getCodUtenza()
																.equals(targetUtenzeC.getCodTargetUtenze())
														&& prestDisabilitaTargetUtenzeDettagliDisa
																.getGregRPreg1DisabilitaTargetUtenza()
																.getGregRDisabilitaTargetUtenza().getGregDDisabilita()
																.getCodDisabilita()
																.equals(disabilitaC.getCodDisabilita())) {
													dettagliDisabilitaC = new ModelCDettagliDisabilita();
													dettagliDisabilitaC.setIdDettagliDisabilita(
															dettagliDisa.getIdDettaglioDisabilita());
													dettagliDisabilitaC.setCodDettagliDisabilita(
															dettagliDisa.getCodDettaglioDisabilita());
													dettagliDisabilitaC.setDescDettagliDisabilita(
															dettagliDisa.getDescDettaglioDisabilita());
													listaDettagliDisabilita.add(dettagliDisabilitaC);
												}
											}
										}
										disabilitaC.setListaDettagliDisabilita(listaDettagliDisabilita);
										listaDisabilita.add(disabilitaC);
									}
								}
							}
							targetUtenzeC.setListaDisabilita(listaDisabilita);
							listaTargetUtenze.add(targetUtenzeC);
						}
					}
				}
				prestazioneC.setListaTargetUtenze(listaTargetUtenze);
				listaPrestazioni.add(prestazioneC);
			}
		}
		return listaPrestazioni;
	}

	public ModelRendicontazioneModC getRendicontazioneModC(Integer idScheda) {
		List<GregRRendicontazioneModCParte1> rendicontazioni1 = modelloCDao
				.findAllRendicontazioneParte1ModCByEnte(idScheda);
		List<GregRRendicontazioneModCParte2> rendicontazioni2 = modelloCDao
				.findAllRendicontazioneParte2ModCByEnte(idScheda);
		List<GregRRendicontazioneModCParte3> rendicontazioni3 = modelloCDao
				.findAllRendicontazioneParte3ModCByEnte(idScheda);
		List<GregRRendicontazioneModCParte4> rendicontazioni4 = modelloCDao
				.findAllRendicontazioneParte4ModCByEnte(idScheda);
		GregTRendicontazioneEnte rendente = datiRendicontazioneService.getRendicontazione(idScheda);
		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService
				.getSchedaEnte(rendente.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		List<ModelPrestazioniAssociate> cartaPrest = dataEnteService.findPrestazioniAssociate(idScheda);

		List<ModelCPrestazioni> prestazioni = getPrestazioni(rendente.getIdRendicontazioneEnte());
		if (prestazioni == null) {
			prestazioni = new ArrayList<ModelCPrestazioni>();
			ModelCPrestazioni p1 = new ModelCPrestazioni();
			p1.setFlag(false);
			ModelCPrestazioni p2 = new ModelCPrestazioni();
			p2.setFlag(false);
			prestazioni.add(p1);
			prestazioni.add(p2);
		}
		ModelRendicontazioneModC rendCorrente = new ModelRendicontazioneModC();
		rendCorrente.setIdRendicontazioneEnte(rendente.getIdRendicontazioneEnte());
		rendCorrente.setIdSchedaEnteGestore(rendente.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		rendCorrente.setListaPrestazioni(prestazioni);
		for (ModelCPrestazioni prestazione : rendCorrente.getListaPrestazioni()) {
			prestazione.setFlag(false);
			for (ModelPrestazioniAssociate carta : cartaPrest) {
				if (carta.getCodicePrestazione().equals(prestazione.getCodPrestazione())) {
					prestazione.setFlag(true);
				}
			}
			for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
				for (GregRRendicontazioneModCParte1 rendicontazione1 : rendicontazioni1) {
					if (rendicontazione1.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
							.equals(rendCorrente.getIdRendicontazioneEnte())
							&& rendicontazione1.getGregRPrestReg1UtenzeModc().getGregDTargetUtenza().getCodUtenza()
									.equals(targetUtenza.getCodTargetUtenze())
							&& rendicontazione1.getGregRPrestReg1UtenzeModc().getGregTPrestazioniRegionali1()
									.getCodPrestReg1().equals(prestazione.getCodPrestazione())) {
						targetUtenza.setValore(rendicontazione1.getValore());
					}
				}
				for (ModelCDettagliUtenze dettagliUtenza : targetUtenza.getListaDettagliUtenze()) {
					for (GregRRendicontazioneModCParte2 rendicontazione2 : rendicontazioni2) {
						if (rendicontazione2.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
								.equals(rendCorrente.getIdRendicontazioneEnte())
								&& rendicontazione2.getGregRPrestReg1UtenzeModcDettUtenze().getGregDDettaglioUtenze()
										.getCodDettaglioUtenza().equals(dettagliUtenza.getCodDettagliUtenze())
								&& rendicontazione2.getGregRPrestReg1UtenzeModcDettUtenze()
										.getGregRPrestReg1UtenzeModc().getGregDTargetUtenza().getCodUtenza()
										.equals(targetUtenza.getCodTargetUtenze())
								&& rendicontazione2.getGregRPrestReg1UtenzeModcDettUtenze()
										.getGregRPrestReg1UtenzeModc().getGregTPrestazioniRegionali1().getCodPrestReg1()
										.equals(prestazione.getCodPrestazione())) {
							dettagliUtenza.setValore(rendicontazione2.getValore());
						}
					}
				}
				for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
					for (GregRRendicontazioneModCParte3 rendicontazione3 : rendicontazioni3) {
						if (rendicontazione3.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
								.equals(rendCorrente.getIdRendicontazioneEnte())
								&& rendicontazione3.getGregRPreg1DisabilitaTargetUtenza()
										.getGregTPrestazioniRegionali1().getCodPrestReg1()
										.equals(prestazione.getCodPrestazione())
								&& rendicontazione3.getGregRPreg1DisabilitaTargetUtenza()
										.getGregRDisabilitaTargetUtenza().getGregDTargetUtenza().getCodUtenza()
										.equals(targetUtenza.getCodTargetUtenze())
								&& rendicontazione3.getGregRPreg1DisabilitaTargetUtenza()
										.getGregRDisabilitaTargetUtenza().getGregDDisabilita().getCodDisabilita()
										.equals(disabilita.getCodDisabilita())) {
							disabilita.setValore(rendicontazione3.getValore());
						}
					}

					for (ModelCDettagliDisabilita dettagliDisabilita : disabilita.getListaDettagliDisabilita()) {
						for (GregRRendicontazioneModCParte4 rendicontazione4 : rendicontazioni4) {
							if (rendicontazione4.getGregTRendicontazioneEnte().getIdRendicontazioneEnte()
									.equals(rendCorrente.getIdRendicontazioneEnte())
									&& rendicontazione4.getGregRPreg1DisabTargetUtenzaDettDisab()
											.getGregDDettaglioDisabilita().getCodDettaglioDisabilita()
											.equals(dettagliDisabilita.getCodDettagliDisabilita())
									&& rendicontazione4.getGregRPreg1DisabTargetUtenzaDettDisab()
											.getGregRPreg1DisabilitaTargetUtenza().getGregTPrestazioniRegionali1()
											.getCodPrestReg1().equals(prestazione.getCodPrestazione())
									&& rendicontazione4.getGregRPreg1DisabTargetUtenzaDettDisab()
											.getGregRPreg1DisabilitaTargetUtenza().getGregRDisabilitaTargetUtenza()
											.getGregDTargetUtenza().getCodUtenza()
											.equals(targetUtenza.getCodTargetUtenze())
									&& rendicontazione4.getGregRPreg1DisabTargetUtenzaDettDisab()
											.getGregRPreg1DisabilitaTargetUtenza().getGregRDisabilitaTargetUtenza()
											.getGregDDisabilita().getCodDisabilita()
											.equals(disabilita.getCodDisabilita())) {
								dettagliDisabilita.setValore(rendicontazione4.getValore());
							}
						}
					}
				}
			}
		}
		return rendCorrente;
	}

//	public List<String> controllaValoriModelloC(ModelRendicontazioneModC body) throws IntegritaException {
//		List<String> listaWarnings = new ArrayList<String>();
//		String warning = listeService.getMessaggio(SharedConstants.WARNING_MOD_B_01).getTestoMessaggio();
//		
//		BigDecimal totaleTarget = BigDecimal.ZERO;
//		BigDecimal totaleDettagliUtenze = BigDecimal.ZERO;
//		BigDecimal totaleDisabilita = BigDecimal.ZERO;
//		BigDecimal totaleDettagliDisabilita = BigDecimal.ZERO;
//		
//		
//		List<ModelCPrestazioni> prestazioni = body.getListaPrestazioni();
//		for(ModelCPrestazioni prestazione : prestazioni) {
//			for(ModelCTargetUtenze target : prestazione.getListaTargetUtenze()) {
//				totaleTarget = totaleTarget.add(target.getValore());
//				for(ModelCDettagliUtenze utenze : target.getListaDettagliUtenze()) {
//					totaleDettagliUtenze = totaleDettagliUtenze.add(utenze.getValore());
//				}
//				if(totaleDettagliUtenze.equals(target.getValore())) {
//					String warning1 = warning.replace("QUALEIMPORTO", "TOTALE SPESA CORRENTE della MISSIONE 12 E della MISSIONE 04 (solo la parte concernente il Sostegno Socio Educativo Scolastico per minori stranieri e minori disabili - compreso Trasporto scolastico Disabili)")
//							.replace("IMPORTO", Util.convertBigDecimalToString(totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale()))
//							.replace("MODELLO", "Macroaggregati");
//				}
//				
//			}
//			
//		}
//		
//		
//		//Controllo "di cui" target utenza
//		//Controllo "di cui" disabilita
//		//Controllo totale disabilita con target utenza
//		//Controllo nuclei familiari disabilita
//		//Controllo nuclei familiari target utenza
//		
//		
////		// Verifico TotaleSpesaCorrente0412
////		BigDecimal totaleSottotitoliMissione04 = BigDecimal.ZERO;
////		//sommo i due sottotitoli
////		totaleSottotitoliMissione04 = totaleSottotitoliMissione04.add(
////				body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0).getListaSottotitolo().get(0).getValore() != null?  
////						body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0).getListaSottotitolo().get(0).getValore() : BigDecimal.ZERO)
////				.add(body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0).getListaSottotitolo().get(1).getValore() != null? 
////						body.getListaMissioni().get(1).getListaProgramma().get(0).getListaTitolo().get(0).getListaSottotitolo().get(1).getValore() : BigDecimal.ZERO);
////		BigDecimal totaleSpesaCorrente12 = BigDecimal.ZERO;
////		List<ModelBProgramma> listaProgramma = body.getListaMissioni().get(1).getListaProgramma();
////		for(ModelBProgramma programma : listaProgramma) {
////			for(ModelBTitolo titolo : programma.getListaTitolo()) {
////				if(titolo.getValore() != null) {
////					totaleSpesaCorrente12 = totaleSpesaCorrente12.add(titolo.getValore());
////				}
////			}
////		}
////		BigDecimal totale1 = totaleSottotitoliMissione04.add(totaleSpesaCorrente12);
////		if(totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale() != totale1) {
////			String warning1 = warning.replace("QUALEIMPORTO", "TOTALE SPESA CORRENTE della MISSIONE 12 E della MISSIONE 04 (solo la parte concernente il Sostegno Socio Educativo Scolastico per minori stranieri e minori disabili - compreso Trasporto scolastico Disabili)")
////					.replace("IMPORTO", Util.convertBigDecimalToString(totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale()))
////					.replace("MODELLO", "Macroaggregati");
////			listaWarnings.add(warning1);
////		}
//		
//		return listaWarnings;
//	}

	public SaveModelloOutput saveModelloC(ModelRendicontazioneModC body, UserInfo userInfo, String notaEnte,
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
					newNotaEnte = notaEnte;
				}

			} else {
				newNotaEnte = notaEnte;
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(notaInterna)) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendToUpdate);
				cronologia.setGregDStatoRendicontazione(rendToUpdate.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Mod. C");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(notaInterna);
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}

			List<ModelCPrestazioni> listaToSaveOrUpdate = body.getListaPrestazioni();
			for (ModelCPrestazioni prestazione : listaToSaveOrUpdate) {
				for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
					GregRRendicontazioneModCParte1 rendicontazione1 = modelloCDao.findRendincontazione1byPrestTargUte(
							prestazione.getCodPrestazione(), targetUtenza.getCodTargetUtenze(),
							rendToUpdate.getIdRendicontazioneEnte());
					if (rendicontazione1 != null) {
						if (targetUtenza.getValore() == null) {
							modelloCDao.deleteRendicontazione1ModC(rendicontazione1.getIdRendicontazioneModCParte1());
						} else {
							rendicontazione1.setValore(targetUtenza.getValore());
							rendicontazione1.setDataModifica(dataModifica);
							modelloCDao.updateRendicontazione1ModC(rendicontazione1);
						}
					} else {
						if (targetUtenza.getValore() != null) {
							GregRRendicontazioneModCParte1 newRend = new GregRRendicontazioneModCParte1();
							GregRPrestReg1UtenzeModc prestUtenze = modelloCDao
									.findPrestReg1UtenzeModCbyPrestazioneUtenze(prestazione.getCodPrestazione(),
											targetUtenza.getCodTargetUtenze(), rendToUpdate.getAnnoGestione());
							newRend.setGregTRendicontazioneEnte(rendToUpdate);
							newRend.setGregRPrestReg1UtenzeModc(prestUtenze);
							newRend.setValore(targetUtenza.getValore());
							newRend.setDataInizioValidita(dataModifica);
							newRend.setUtenteOperazione(userInfo.getCodFisc());
							newRend.setDataCreazione(dataModifica);
							newRend.setDataModifica(dataModifica);
							modelloCDao.insertRendicontazione1ModC(newRend);
						}
					}

					for (ModelCDettagliUtenze dettagliUtenza : targetUtenza.getListaDettagliUtenze()) {
						GregRRendicontazioneModCParte2 rendicontazione2 = modelloCDao
								.findRendincontazione2byPrestTargUteDettUte(prestazione.getCodPrestazione(),
										targetUtenza.getCodTargetUtenze(), dettagliUtenza.getCodDettagliUtenze(),
										rendToUpdate.getIdRendicontazioneEnte());
						if (rendicontazione2 != null) {
							if (dettagliUtenza.getValore() == null) {
								modelloCDao
										.deleteRendicontazione2ModC(rendicontazione2.getIdRendicontazioneModCParte2());
							} else {
								rendicontazione2.setValore(dettagliUtenza.getValore());
								rendicontazione2.setDataModifica(dataModifica);
								modelloCDao.updateRendicontazione2ModC(rendicontazione2);
							}
						} else {
							if (dettagliUtenza.getValore() != null) {
								GregRRendicontazioneModCParte2 newRend = new GregRRendicontazioneModCParte2();
								GregRPrestReg1UtenzeModcDettUtenze prestUtenzeDettUtenze = modelloCDao
										.findPrestReg1UtenzeDettUtenzeModCbyPrestazioneUtenzeDettUtenze(
												prestazione.getCodPrestazione(), targetUtenza.getCodTargetUtenze(),
												dettagliUtenza.getCodDettagliUtenze(), rendToUpdate.getAnnoGestione());
								newRend.setGregTRendicontazioneEnte(rendToUpdate);
								newRend.setGregRPrestReg1UtenzeModcDettUtenze(prestUtenzeDettUtenze);
								newRend.setValore(dettagliUtenza.getValore());
								newRend.setDataInizioValidita(dataModifica);
								newRend.setUtenteOperazione(userInfo.getCodFisc());
								newRend.setDataCreazione(dataModifica);
								newRend.setDataModifica(dataModifica);
								modelloCDao.insertRendicontazione2ModC(newRend);
							}
						}

						for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
							GregRRendicontazioneModCParte3 rendicontazione3 = modelloCDao
									.findRendincontazione3byPrestTargUteDisa(prestazione.getCodPrestazione(),
											targetUtenza.getCodTargetUtenze(), disabilita.getCodDisabilita(),
											rendToUpdate.getIdRendicontazioneEnte());
							if (rendicontazione3 != null) {
								if (disabilita.getValore() == null) {
									modelloCDao.deleteRendicontazione3ModC(
											rendicontazione3.getIdRendicontazioneModCParte3());
								} else {
									rendicontazione3.setValore(disabilita.getValore());
									rendicontazione3.setDataModifica(dataModifica);
									modelloCDao.updateRendicontazione3ModC(rendicontazione3);
								}
							} else {
								if (disabilita.getValore() != null) {
									GregRRendicontazioneModCParte3 newRend = new GregRRendicontazioneModCParte3();
									GregRPreg1DisabilitaTargetUtenza prestDisabilitaTarget = modelloCDao
											.findPreg1DisabilitaTargetUtenzaModCbyPrestazioneUtenzeDisabilita(
													prestazione.getCodPrestazione(), targetUtenza.getCodTargetUtenze(),
													disabilita.getCodDisabilita(), rendToUpdate.getAnnoGestione());
									newRend.setGregTRendicontazioneEnte(rendToUpdate);
									newRend.setGregRPreg1DisabilitaTargetUtenza(prestDisabilitaTarget);
									newRend.setValore(disabilita.getValore());
									newRend.setDataInizioValidita(dataModifica);
									newRend.setUtenteOperazione(userInfo.getCodFisc());
									newRend.setDataCreazione(dataModifica);
									newRend.setDataModifica(dataModifica);
									modelloCDao.insertRendicontazione3ModC(newRend);
								}
							}

							for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
									.getListaDettagliDisabilita()) {
								GregRRendicontazioneModCParte4 rendicontazione4 = modelloCDao
										.findRendincontazione4byPrestTargUteDisaDettaglioDisa(
												prestazione.getCodPrestazione(), targetUtenza.getCodTargetUtenze(),
												disabilita.getCodDisabilita(),
												dettaglioDisabilita.getCodDettagliDisabilita(),
												rendToUpdate.getIdRendicontazioneEnte());
								if (rendicontazione4 != null) {
									if (dettaglioDisabilita.getValore() == null) {
										modelloCDao.deleteRendicontazione4ModC(
												rendicontazione4.getIdRendicontazioneModCParte4());
									} else {
										rendicontazione4.setValore(dettaglioDisabilita.getValore());
										rendicontazione4.setDataModifica(dataModifica);
										modelloCDao.updateRendicontazione4ModC(rendicontazione4);
									}
								} else {
									if (dettaglioDisabilita.getValore() != null) {
										GregRRendicontazioneModCParte4 newRend = new GregRRendicontazioneModCParte4();
										GregRPreg1DisabTargetUtenzaDettDisab prestDisabilitaTargetDettDisabilita = modelloCDao
												.findPreg1DisabTargetUtenzaDettDisabModCbyPrestazioneUtenzeDisabilitaDettagli(
														prestazione.getCodPrestazione(),
														targetUtenza.getCodTargetUtenze(),
														disabilita.getCodDisabilita(),
														dettaglioDisabilita.getCodDettagliDisabilita(),
														rendToUpdate.getAnnoGestione());
										newRend.setGregTRendicontazioneEnte(rendToUpdate);
										newRend.setGregRPreg1DisabTargetUtenzaDettDisab(
												prestDisabilitaTargetDettDisabilita);
										newRend.setValore(dettaglioDisabilita.getValore());
										newRend.setDataInizioValidita(dataModifica);
										newRend.setUtenteOperazione(userInfo.getCodFisc());
										newRend.setDataCreazione(dataModifica);
										newRend.setDataModifica(dataModifica);
										modelloCDao.insertRendicontazione4ModC(newRend);
									}
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
	public String esportaModelloC(EsportaModelloCInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloC");
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
		cellStyleb12I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold
		CellStyle cellStyle12bnc = sheet.getWorkbook().createCellStyle();
		cellStyle12bnc.setAlignment(CellStyle.ALIGN_LEFT);
		font12b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font12b.setFontHeightInPoints((short) 12);
		font12b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle12bnc.setFont(font12b);
		cellStyle12bnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold center
		CellStyle cellStyle12bCenter = sheet.getWorkbook().createCellStyle();
		cellStyle12bCenter.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle12bCenter.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle12bCenter.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12bCenter.setFont(font12b);
		cellStyle12bCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold italic
		CellStyle cellStyleb10I = sheet.getWorkbook().createCellStyle();
		cellStyleb10I.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb10I = sheet.getWorkbook().createFont();
		fontb10I.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb10I.setFontHeightInPoints((short) 10);
		fontb10I.setFontName(HSSFFont.FONT_ARIAL);
		fontb10I.setItalic(true);
		cellStyleb10I.setFont(fontb10I);
		cellStyleb10I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold
		CellStyle cellStyle10b = sheet.getWorkbook().createCellStyle();
		cellStyle10b.setAlignment(CellStyle.ALIGN_LEFT);
		Font font10b = sheet.getWorkbook().createFont();
		font10b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font10b.setFontHeightInPoints((short) 10);
		font10b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle10b.setFillForegroundColor(pattWhite);
		cellStyle10b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle10b.setFont(font10b);
		cellStyle10b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold
		CellStyle cellStyle10bnc = sheet.getWorkbook().createCellStyle();
		cellStyle10bnc.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle10bnc.setFont(font10b);
		cellStyle10bnc.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold center
		CellStyle cellStyle10bCenter = sheet.getWorkbook().createCellStyle();
		cellStyle10bCenter.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle10bCenter.setFont(font10b);
		cellStyle10bCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 bold italic
		CellStyle cellStyleb8I = sheet.getWorkbook().createCellStyle();
		cellStyleb8I.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb8I = sheet.getWorkbook().createFont();
		fontb8I.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb8I.setFontHeightInPoints((short) 10);
		fontb8I.setFontName(HSSFFont.FONT_ARIAL);
		fontb8I.setItalic(true);
		cellStyleb8I.setFont(fontb8I);
		cellStyleb8I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 bold
		CellStyle cellStyle8b = sheet.getWorkbook().createCellStyle();
		cellStyle8b.setAlignment(CellStyle.ALIGN_LEFT);
		Font font8b = sheet.getWorkbook().createFont();
		font8b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font8b.setFontHeightInPoints((short) 8);
		font8b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle8b.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle8b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle8b.setFont(font8b);
		cellStyle8b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 bold center
		CellStyle cellStyle8bCenter = sheet.getWorkbook().createCellStyle();
		cellStyle8bCenter.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle8bCenter.setFont(font8b);
		cellStyle8bCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 bold R
		CellStyle cellStyle8bR = sheet.getWorkbook().createCellStyle();
		cellStyle8bR.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle8bR.setFont(font8b);
		cellStyle8bR.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 8 bold center
		CellStyle cellStyle8bgCenter = sheet.getWorkbook().createCellStyle();
		cellStyle8bgCenter.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle8bgCenter.setFont(font8b);
		cellStyle8bgCenter.setFillForegroundColor(palIndex);
		cellStyle8bgCenter.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle8bgCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold right
		CellStyle cellStyle10bgCenter = sheet.getWorkbook().createCellStyle();
		cellStyle10bgCenter.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10bgCenter.setFont(font10b);
		cellStyle10bgCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo
		CellStyle cellStyleTitolo = sheet.getWorkbook().createCellStyle();
		cellStyleTitolo.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTitolo.setFont(font12b);
		cellStyleTitolo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial utenze
		CellStyle cellStyleUtenze = sheet.getWorkbook().createCellStyle();
		cellStyleUtenze.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleUtenze.setFillForegroundColor(patGrey);
		cellStyleUtenze.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleUtenze.setFont(font10b);
		cellStyleUtenze.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo parti
		CellStyle cellStyleTotaliParti = sheet.getWorkbook().createCellStyle();
		cellStyleTotaliParti.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleTotaliParti.setFont(font12b);
		cellStyleTotaliParti.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Valore Green
		CellStyle cellStyleTotaleParteValueGreen = sheet.getWorkbook().createCellStyle();
		cellStyleTotaleParteValueGreen.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotaleParteValueGreen.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleTotaleParteValueGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaleParteValueGreen.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaleParteValueGreen.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleParteValueGreen.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleParteValueGreen.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleParteValueGreen.setBorderBottom(CellStyle.BORDER_MEDIUM);
		// Crea Stile Vertical Align
		CellStyle cellStyleVocetabellaVerticalAlign = sheet.getWorkbook().createCellStyle();
		cellStyleVocetabellaVerticalAlign.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVocetabellaVerticalAlign = sheet.getWorkbook().createFont();
		fontVocetabellaVerticalAlign.setFontHeightInPoints((short) 10);
		fontVocetabellaVerticalAlign.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVocetabellaVerticalAlign.setFont(fontVocetabellaVerticalAlign);
		cellStyleVocetabellaVerticalAlign.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Voce tabella
		CellStyle cellStyleVocetabella = sheet.getWorkbook().createCellStyle();
		cellStyleVocetabella.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVocetabella = sheet.getWorkbook().createFont();
		fontVocetabella.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontVocetabella.setFontHeightInPoints((short) 10);
		fontVocetabella.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVocetabella.setFont(fontVocetabella);
		cellStyleVocetabella.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Operatore
		CellStyle cellStyleOperatore = sheet.getWorkbook().createCellStyle();
		cellStyleOperatore.setAlignment(CellStyle.ALIGN_CENTER);
		Font fontOperatore = sheet.getWorkbook().createFont();
		fontOperatore.setFontHeightInPoints((short) 10);
		fontOperatore.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleOperatore.setFont(fontOperatore);
		cellStyleOperatore.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Valore Not Show
		CellStyle cellStyleValueNotShow = sheet.getWorkbook().createCellStyle();
		cellStyleValueNotShow.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueNotShow.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyleValueNotShow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueNotShow.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueNotShow.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueNotShow.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueNotShow.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueNotShow.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore White
		CellStyle cellStyleValueWhite = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhite.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleValueWhite.setFillForegroundColor(pattWhite);
		cellStyleValueWhite.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueWhite.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Valore White with border left
		CellStyle cellStyleValueWhiteWithBorderLeft = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhiteWithBorderLeft.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueWhiteWithBorderLeft.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleValueWhiteWithBorderLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueWhiteWithBorderLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueWhiteWithBorderLeft.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueWhiteWithBorderLeft.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleValueWhiteWithBorderLeft.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueWhiteWithBorderLeft.setBorderBottom(CellStyle.BORDER_THIN);
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
		// Crea Stile Colonna Prestazione Azzurro
		CellStyle cellStylePrestAzz = sheet.getWorkbook().createCellStyle();
		cellStylePrestAzz.setAlignment(CellStyle.ALIGN_LEFT);
		cellStylePrestAzz.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		cellStylePrestAzz.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStylePrestAzz.setFont(font10b);
		cellStylePrestAzz.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Prestazione Verde
		CellStyle cellStylePrestVerd = sheet.getWorkbook().createCellStyle();
		cellStylePrestVerd.setAlignment(CellStyle.ALIGN_LEFT);
		cellStylePrestVerd.setFillForegroundColor(palIndex);
		cellStylePrestVerd.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStylePrestVerd.setFont(font10b);
		cellStylePrestVerd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Prestazione Verde
		CellStyle cellStylePrestVerd14 = sheet.getWorkbook().createCellStyle();
		cellStylePrestVerd14.setAlignment(CellStyle.ALIGN_CENTER);
		cellStylePrestVerd14.setFillForegroundColor(palIndex);
		cellStylePrestVerd14.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font14b = sheet.getWorkbook().createFont();
		font14b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font14b.setFontHeightInPoints((short) 14);
		font14b.setFontName(HSSFFont.FONT_ARIAL);
		cellStylePrestVerd14.setFont(font14b);
		cellStylePrestVerd14.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Prestazione Verde
		CellStyle cellStylePrestVerd9 = sheet.getWorkbook().createCellStyle();
		cellStylePrestVerd9.setAlignment(CellStyle.ALIGN_LEFT);
		cellStylePrestVerd9.setFillForegroundColor(palIndex);
		cellStylePrestVerd9.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font9b = sheet.getWorkbook().createFont();
		font9b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font9b.setFontHeightInPoints((short) 9);
		font9b.setFontName(HSSFFont.FONT_ARIAL);
		cellStylePrestVerd9.setFont(font9b);
		cellStylePrestVerd9.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Prestazione Verde
		CellStyle cellStylePrestVerd8 = sheet.getWorkbook().createCellStyle();
		cellStylePrestVerd8.setAlignment(CellStyle.ALIGN_LEFT);
		cellStylePrestVerd8.setFillForegroundColor(palIndex);
		cellStylePrestVerd8.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStylePrestVerd8.setFont(font8b);
		cellStylePrestVerd8.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTranche = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_C);

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
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 8, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);
		// RIGA 1
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
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
		// RIGA 2
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
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
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 9, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTranche.getDesEstesaTab());
		cell.setCellStyle(cellStyleTitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 4
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		// Tabelle
		for (ModelCPrestazioni prestazione : body.getDatiC().getListaPrestazioni()) {
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
			row.createCell(++columnCount);
			cell.setCellValue((String) prestazione.getDescPrestazione());
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 15, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) prestazione.getListaTargetUtenze().get(6).getDescTargetUtenze());
			cell.setCellStyle(cellStyleUtenze);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
			RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			for (ModelCTargetUtenze utenze : prestazione.getListaTargetUtenze()) {
				if (!utenze.getCodTargetUtenze().equals("U28")) {
					cell = row.createCell(++columnCount);
					cell.setCellValue((String) utenze.getDescTargetUtenze());
					cell.setCellStyle(cellStyle8bCenter);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
					RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					cell = row.createCell(++columnCount);
					if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
						cell.setCellStyle(cellStylePrestAzz);
					} else {
						cell.setCellStyle(cellStylePrestVerd);
					}
				}
			}
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) "Totale Numero Utenti");
			cell.setCellStyle(cellStyle8bCenter);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
			RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}

			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getListaTargetUtenze().get(6).getValore() != null) {
				cell.setCellValue(prestazione.getListaTargetUtenze().get(6).getValore().intValue());
			}
			cell.setCellStyle(cellStyleUtenze);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
			RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			for (ModelCTargetUtenze utenze : prestazione.getListaTargetUtenze()) {
				if (!utenze.getCodTargetUtenze().equals("U28")) {
					cell = row.createCell(++columnCount);
					if (utenze.getValore() != null) {
						cell.setCellValue(utenze.getValore().intValue());
					}
					if (prestazione.getCodPrestazione().equals("R_A.2.1") && (utenze.getCodTargetUtenze().equals("U23")
							|| utenze.getCodTargetUtenze().equals("U25"))) {
						CreationHelper factory = workbook.getCreationHelper();
						ClientAnchor anchor = factory.createClientAnchor();
						anchor.setCol1(6);
						anchor.setCol2(8);
						anchor.setRow1(13);
						anchor.setRow2(15);
						Drawing drawing = sheet.createDrawingPatriarch();
						Comment comment = drawing.createCellComment(anchor);
						comment.setString(factory.createRichTextString(
								messaggio.getTestoMsgInformativo().replace("TARGET1", utenze.getDescTargetUtenze())
										.replace("PRESTAZIONE", prestazione.getDescPrestazione())
										.replace("TARGET2", utenze.getDescTargetUtenze())));
						cell.setCellComment(comment);
					}
					cell.setCellStyle(cellStyle8bCenter);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
					RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					cell = row.createCell(++columnCount);
					if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
						cell.setCellStyle(cellStylePrestAzz);
					} else {
						cell.setCellStyle(cellStylePrestVerd);
					}
				}
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				if (body.getTotaleUtenti().get(0) != null) {
					cell.setCellValue(Integer.parseInt(body.getTotaleUtenti().get(0)));
				}
			} else {
				if (body.getTotaleUtenti().get(1) != null) {
					cell.setCellValue(Integer.parseInt(body.getTotaleUtenti().get(1)));
				}
			}

			cell.setCellStyle(cellStyle8bCenter);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
			RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			cell = row.createCell(columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			cell = row.createCell(++columnCount);
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cell.setCellStyle(cellStylePrestAzz);
			} else {
				cell.setCellStyle(cellStylePrestVerd);
			}
			if (prestazione.getCodPrestazione().equals("R_A.1.1")) {
				cellRangeAddress = new CellRangeAddress(rowCount - 3, rowCount, columnCount - 17, columnCount);
//				sheet.addMergedRegion(cellRangeAddress);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			} else {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(0).getListaDettagliUtenze().get(0)
						.getDescDettagliUtenze());
				cell.setCellStyle(cellStyle8bgCenter);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				if (prestazione.getListaTargetUtenze().get(6).getListaDettagliUtenze().get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(6).getListaDettagliUtenze().get(0)
							.getValore().intValue());
				}
				cell.setCellStyle(cellStyleUtenze);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				for (ModelCTargetUtenze utenze : prestazione.getListaTargetUtenze()) {
					if (!utenze.getCodTargetUtenze().equals("U28")) {
						cell = row.createCell(++columnCount);
						if (utenze.getListaDettagliUtenze().get(0).getValore() != null) {
							cell.setCellValue(utenze.getListaDettagliUtenze().get(0).getValore().intValue());
						}
						cell.setCellStyle(cellStyle8bCenter);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
						RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						cell = row.createCell(++columnCount);
						cell.setCellStyle(cellStylePrestVerd);
					}
				}
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(0).getListaDettagliUtenze().get(1)
						.getDescDettagliUtenze());
				cell.setCellStyle(cellStyle8bgCenter);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				if (prestazione.getListaTargetUtenze().get(6).getListaDettagliUtenze().get(1).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(6).getListaDettagliUtenze().get(1)
							.getValore().intValue());
				}
				cell.setCellStyle(cellStyleUtenze);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				for (ModelCTargetUtenze utenze : prestazione.getListaTargetUtenze()) {
					if (!utenze.getCodTargetUtenze().equals("U28")) {
						cell = row.createCell(++columnCount);
						if (utenze.getListaDettagliUtenze().get(1).getValore() != null) {
							cell.setCellValue(utenze.getListaDettagliUtenze().get(1).getValore().intValue());
						}
						cell.setCellStyle(cellStyle8bCenter);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
						RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						cell = row.createCell(++columnCount);
						cell.setCellStyle(cellStylePrestVerd);
					}
				}

				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(0).getListaDettagliUtenze().get(2)
						.getDescDettagliUtenze());
				cell.setCellStyle(cellStyle8bgCenter);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				if (prestazione.getListaTargetUtenze().get(6).getListaDettagliUtenze().get(2).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(6).getListaDettagliUtenze().get(2)
							.getValore().intValue());
				}
				cell.setCellStyle(cellStyleUtenze);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				for (ModelCTargetUtenze utenze : prestazione.getListaTargetUtenze()) {
					if (!utenze.getCodTargetUtenze().equals("U28")) {
						cell = row.createCell(++columnCount);
						if (utenze.getListaDettagliUtenze().get(2).getValore() != null) {
							cell.setCellValue(utenze.getListaDettagliUtenze().get(2).getValore().intValue());
						}
						cell.setCellStyle(cellStyle8bCenter);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
						RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						cell = row.createCell(++columnCount);
						cell.setCellStyle(cellStylePrestVerd);
					}
				}
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);
				cell = row.createCell(++columnCount);
				cell.setCellStyle(cellStylePrestVerd);

				cellRangeAddress = new CellRangeAddress(rowCount - 9, rowCount, columnCount - 17, columnCount);
//				sheet.addMergedRegion(cellRangeAddress);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
			}

			columnCount = 0;
			row = sheet.createRow(++rowCount);
			// Fine tabelle
			if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell.setCellValue((String) "PER I DISABILI MINORI E ADULTI E PER I RELATIVI NUCLEI SPECIFICARE :");
				cell.setCellStyle(cellStyle12bnc);
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(1).getDescTargetUtenze());
				cell.setCellStyle(cellStylePrestVerd14);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(3).getDescTargetUtenze());
				cell.setCellStyle(cellStylePrestVerd14);
				// disabilita 1
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1)
						.getListaDettagliDisabilita().get(0).getDescDettagliDisabilita());
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1)
						.getListaDettagliDisabilita().get(0).getDescDettagliDisabilita());
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue(
						prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(0).getDescDisabilita());
				cell.setCellStyle(cellStyle10bgCenter);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(0).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(0).getListaDettagliDisabilita()
						.get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(0)
							.getListaDettagliDisabilita().get(0).getValore().intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(0).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(0).getListaDettagliDisabilita()
						.get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(0)
							.getListaDettagliDisabilita().get(0).getValore().intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				// disabilita 2
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1)
						.getListaDettagliDisabilita().get(0).getDescDettagliDisabilita());
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1)
						.getListaDettagliDisabilita().get(0).getDescDettagliDisabilita());
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue(
						prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1).getDescDisabilita());
				cell.setCellStyle(cellStyle10bgCenter);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1).getListaDettagliDisabilita()
						.get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1)
							.getListaDettagliDisabilita().get(0).getValore().intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1).getListaDettagliDisabilita()
						.get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1)
							.getListaDettagliDisabilita().get(0).getValore().intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				// disabilita 3
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(1)
						.getListaDettagliDisabilita().get(0).getDescDettagliDisabilita());
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(1)
						.getListaDettagliDisabilita().get(0).getDescDettagliDisabilita());
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue(
						prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(2).getDescDisabilita());
				cell.setCellStyle(cellStyle10bgCenter);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(2).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(2).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(2).getListaDettagliDisabilita()
						.get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(2)
							.getListaDettagliDisabilita().get(0).getValore().intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(2).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(2).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(2).getListaDettagliDisabilita()
						.get(0).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(2)
							.getListaDettagliDisabilita().get(0).getValore().intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				// disabilita 4
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
//				cell = row.createCell(columnCount);
//				cell.setCellValue("");
//				cell.setCellStyle(prova);
				row.createCell(++columnCount);
//				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
//				sheet.addMergedRegion(cellRangeAddress);
//				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM_DASHED, cellRangeAddress, sheet,sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue("non vedenti");
				cell.setCellStyle(cellStyle8bR);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(3).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(3).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(3).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(3).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				// disabilita 5
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM_DASHED, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM_DASHED, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue("non udenti");
				cell.setCellStyle(cellStyle8bR);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(4).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(4).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(4).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(4).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				// disabilita 6
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell.setCellValue("Disabilita' sensoriale");
				cell.setCellStyle(cellStyle10bnc);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM_DASHED, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(5)
						.getDescDisabilita());
				cell.setCellStyle(cellStyle8bR);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(5).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(5).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(5).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(5).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				// disabilita 7
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM_DASHED, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
//				cell = row.createCell(columnCount);
//				cell.setCellValue("");
//				cell.setCellStyle(prova1);
				row.createCell(++columnCount);

//				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
//				sheet.addMergedRegion(cellRangeAddress);
//				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM_DASHED, cellRangeAddress, sheet,sheet.getWorkbook());

				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(6)
						.getDescDisabilita());
				cell.setCellStyle(cellStyle8bR);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(6).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(6).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(6).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(6).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd8);
				// Totali
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell.setCellValue("Totale Minori disabili");
				cell.setCellStyle(cellStyle10b);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell.setCellValue("Totale Adulti disabili");
				cell.setCellStyle(cellStyle10b);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);

				cell.setCellValue(Integer.parseInt(body.getTotaleMinori()));
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStyleValueWhite);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStyleValueWhite);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellValue(Integer.parseInt(body.getTotaleAdulti()));
				cell.setCellStyle(cellStyle10bCenter);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStyleValueWhite);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStyleValueWhite);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell.setCellStyle(cellStyle10b);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell.setCellStyle(cellStyle10b);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				// disabilita 8
				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(7)
						.getDescDisabilita());
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue((String) prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(7)
						.getDescDisabilita());
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				if (prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(7).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(1).getListaDisabilita().get(7).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyleUtenze);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
				if (prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(7).getValore() != null) {
					cell.setCellValue(prestazione.getListaTargetUtenze().get(3).getListaDisabilita().get(7).getValore()
							.intValue());
				}
				cell.setCellStyle(cellStyleUtenze);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);
				row.createCell(++columnCount);
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStylePrestVerd9);

				columnCount = 0;
				row = sheet.createRow(++rowCount);
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
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount - 22, rowCount, columnCount - 17, columnCount);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

				HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
				HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 32, (short) 2, 31);
				HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
				shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
				shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_LONGDASHGEL);

				HSSFPatriarch patriarch1 = (HSSFPatriarch) sheet.createDrawingPatriarch();
				HSSFClientAnchor a1 = new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 37, (short) 2, 38);
				HSSFSimpleShape shape2 = patriarch1.createSimpleShape(a1);
				shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
				shape2.setLineStyle(HSSFSimpleShape.LINESTYLE_LONGDASHGEL);
			}
		}

		Util.autoSizeColumns(sheet.getWorkbook());
		// Set dimensione colonne importi
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 3 * 256);
		sheet.setColumnWidth(2, 25 * 256);
		sheet.setColumnWidth(3, 3 * 256);
		sheet.setColumnWidth(4, 25 * 256);
		sheet.setColumnWidth(5, 3 * 256);
		sheet.setColumnWidth(6, 25 * 256);
		sheet.setColumnWidth(7, 3 * 256);
		sheet.setColumnWidth(8, 25 * 256);
		sheet.setColumnWidth(9, 3 * 256);
		sheet.setColumnWidth(10, 25 * 256);
		sheet.setColumnWidth(11, 3 * 256);
		sheet.setColumnWidth(12, 25 * 256);
		sheet.setColumnWidth(13, 3 * 256);
		sheet.setColumnWidth(14, 25 * 256);
		sheet.setColumnWidth(15, 3 * 256);
		sheet.setColumnWidth(16, 25 * 256);
		sheet.setColumnWidth(17, 3 * 256);
		for (int i = 0; i < 44; i++) {
			sheet.getRow(i).setHeight((short) (row.getHeight() / 1.5));
		}

//		sheet.getRow(13).setHeight((short) (row.getHeight()*2));		
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloC_" + nomefile, ".xls");
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
	public GenericResponseWarnErr checkModelloC(Integer idRendicontazione) throws Exception {
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
			ModelRendicontazioneModC datiModelloAttuale = getRendicontazioneModC(
					rendicontazioneAttuale.getIdRendicontazioneEnte());
			ModelRendicontazioneModC datiModelloPassata = getRendicontazioneModC(
					rendicontazionePassata.getIdRendicontazioneEnte());

			List<ModelTotalePrestazioniB1> totaleAttuale = new ArrayList<ModelTotalePrestazioniB1>();
			List<ModelTotalePrestazioniB1> totalePassato = new ArrayList<ModelTotalePrestazioniB1>();

			for (ModelCPrestazioni prestazionePas : datiModelloPassata.getListaPrestazioni()) {
				ModelTotalePrestazioniB1 pre = new ModelTotalePrestazioniB1();
				pre.setCodPrestazione(prestazionePas.getCodPrestazione());
				pre.setDescPrestazione(prestazionePas.getDescPrestazione());
				BigDecimal totale = BigDecimal.ZERO;
				for (ModelCTargetUtenze utenzaPas : prestazionePas.getListaTargetUtenze()) {
					if (utenzaPas.getValore() != null && !utenzaPas.getCodTargetUtenze().equals("U28")) {
						totale = totale.add(utenzaPas.getValore());
					}
				}
				pre.setTotale(totale);
				totalePassato.add(pre);
			}

			for (ModelCPrestazioni prestazione : datiModelloAttuale.getListaPrestazioni()) {
				ModelTotalePrestazioniB1 pre = new ModelTotalePrestazioniB1();
				pre.setCodPrestazione(prestazione.getCodPrestazione());
				pre.setDescPrestazione(prestazione.getDescPrestazione());
				BigDecimal totale = BigDecimal.ZERO;
				for (ModelCTargetUtenze utenza : prestazione.getListaTargetUtenze()) {
					if (utenza.getValore() != null && !utenza.getCodTargetUtenze().equals("U28")) {
						totale = totale.add(utenza.getValore());
					}
				}
				pre.setTotale(totale);
				totaleAttuale.add(pre);
			}

			for (ModelCPrestazioni prestazione : datiModelloAttuale.getListaPrestazioni()) {
				for (ModelCTargetUtenze utenza : prestazione.getListaTargetUtenze()) {
					BigDecimal valoreAttuale = utenza.getValore() != null ? utenza.getValore() : BigDecimal.ZERO;
					for (ModelCPrestazioni prestazionePas : datiModelloPassata.getListaPrestazioni()) {
						if (prestazione.getCodPrestazione().equals(prestazionePas.getCodPrestazione())) {
							for (ModelCTargetUtenze utenzaPas : prestazionePas.getListaTargetUtenze()) {
								if (utenza.getCodTargetUtenze().equals(utenzaPas.getCodTargetUtenze())) {
									BigDecimal valorePassato = utenzaPas.getValore() != null ? utenzaPas.getValore()
											: BigDecimal.ZERO;
									BigDecimal target25 = (valorePassato.multiply(new BigDecimal(25)))
											.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
									if ((valoreAttuale.setScale(2))
											.compareTo(valorePassato.subtract(target25).setScale(2)) < 0
											|| (valoreAttuale.setScale(2))
													.compareTo(valorePassato.add(target25).setScale(2)) > 0) {
										response.getWarnings()
												.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_C01)
														.getTestoMessaggio()
														.replace("UTENZA", utenza.getDescTargetUtenze())
														.replace("PRESTAZIONE", prestazione.getDescPrestazione())
														.replace("DATOATTUALE", valoreAttuale.setScale(0).toString())
														.replace("DATOPASSATO", valorePassato.setScale(0).toString()));
										response.setObblMotivazione(true);
									}
								}
							}
						}
					}
				}
			}

			for (ModelTotalePrestazioniB1 attuale : totaleAttuale) {
				for (ModelTotalePrestazioniB1 passato : totalePassato) {
					if (attuale.getCodPrestazione().equals(passato.getCodPrestazione())) {
						BigDecimal totale25 = (passato.getTotale().multiply(new BigDecimal(25)))
								.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
						if ((attuale.getTotale().setScale(2))
								.compareTo(passato.getTotale().subtract(totale25).setScale(2)) < 0
								|| (attuale.getTotale().setScale(2))
										.compareTo(passato.getTotale().add(totale25).setScale(2)) > 0) {
							response.getWarnings()
									.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLO_C01)
											.getTestoMessaggio().replace("UTENZA", "Totale num. Utenti")
											.replace("PRESTAZIONE", attuale.getDescPrestazione())
											.replace("DATOATTUALE", attuale.getTotale().setScale(0).toString())
											.replace("DATOPASSATO", passato.getTotale().setScale(0).toString()));
							response.setObblMotivazione(true);
						}
					}
				}
			}

			if (response.getWarnings().size() > 0) {
				List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_C,
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

		ModelStatoMod stato = modelloCDao.getStatoModelloC(idRendicontazione);

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
	public GenericResponseWarnErr controlloModelloC(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);

		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_C);
		boolean facoltativo = false;
		boolean valorizzato = modelloCDao.getValorizzatoModelloC(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors()
					.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
							.replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		// Controlli Modello C
		ModelRendicontazioneModC rendModC = getRendicontazioneModC(rendicontazione.getIdRendicontazioneEnte());
		String errorMessageC1 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_01)
				.getTestoMessaggio();
		String errorMessageC2 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_02)
				.getTestoMessaggio();
		String errorMessageC3 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_03)
				.getTestoMessaggio();
		String errorMessageC4 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_04)
				.getTestoMessaggio();
		String errorMessageC5 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_05)
				.getTestoMessaggio();
		String errorMessageC6 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_06)
				.getTestoMessaggio();

		if (!facoltativo) {
			// CONTROLLO 1, Controllo NucleiFamiliari non deve superare somma target utenza
			for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
				BigDecimal numNucleiFamiliari = BigDecimal.ZERO;
				BigDecimal totaleTargetUtenze = BigDecimal.ZERO;
				for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
					if (!targetUtenza.getCodTargetUtenze().equals("U28") && targetUtenza.getValore() != null) {
						totaleTargetUtenze = totaleTargetUtenze.add(targetUtenza.getValore());
					} else if (targetUtenza.getCodTargetUtenze().equals("U28") && targetUtenza.getValore() != null) {
						numNucleiFamiliari = numNucleiFamiliari.add(targetUtenza.getValore());
					}
				}
				if (!numNucleiFamiliari.equals(BigDecimal.ZERO) && !totaleTargetUtenze.equals(BigDecimal.ZERO)
						&& numNucleiFamiliari.compareTo(totaleTargetUtenze) == 1) {
					String errMsgC1 = errorMessageC1;
					response.getErrors().add(errMsgC1.replace("PRESTAZIONE", prestazione.getDescPrestazione()));
				}
			}

			// CONTROLLO 2, Controllo somma valori di cui inferiore al totale di
			// riferimento, Prest. Serv. Soc. Professionale
			for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
				if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
					for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
						BigDecimal totaleTargetUtenza = targetUtenza.getValore() != null ? targetUtenza.getValore()
								: BigDecimal.ZERO;
						for (ModelCDettagliUtenze dettaglioUtenza : targetUtenza.getListaDettagliUtenze()) {
							if (dettaglioUtenza.getValore() != null
									&& dettaglioUtenza.getValore().compareTo(totaleTargetUtenza) == 1) {
								String errMsgC2 = errorMessageC2;
								response.getErrors()
										.add(errMsgC2.replace("DICUI", dettaglioUtenza.getDescDettagliUtenze())
												.replace("UTENZA", targetUtenza.getDescTargetUtenze()));
							}

						}
					}
				}
			}

			// CONTROLLO 3, Controllo TotaleMinoriDisabili con TotalePerDisabilita
			for (ModelCPrestazioni prestazione1 : rendModC.getListaPrestazioni()) {
				if (prestazione1.getCodPrestazione().equals("R_A.2.1")) {
					for (ModelCTargetUtenze targetUtenza1 : prestazione1.getListaTargetUtenze()) {
						if (targetUtenza1.getCodTargetUtenze().equals("U23")) {
							BigDecimal totaleTargetUtenzaDisabilitaMinori = targetUtenza1.getValore() != null
									? targetUtenza1.getValore()
									: BigDecimal.ZERO;
							BigDecimal totaleSpecificaDisabilitaMinori = BigDecimal.ZERO;
							for (ModelCDisabilita disabilita1 : targetUtenza1.getListaDisabilita()) {
								if (!disabilita1.getCodDisabilita().equals("DIS08")
										&& disabilita1.getValore() != null) {
									totaleSpecificaDisabilitaMinori = totaleSpecificaDisabilitaMinori
											.add(disabilita1.getValore());
								}
							}
							if (!totaleSpecificaDisabilitaMinori.equals(BigDecimal.ZERO)
									&& !totaleTargetUtenzaDisabilitaMinori.equals(BigDecimal.ZERO)
									&& !totaleSpecificaDisabilitaMinori.equals(totaleTargetUtenzaDisabilitaMinori)) {
								String errMsgC3 = errorMessageC3;
								response.getErrors().add(errMsgC3.replaceAll("MINADUL", "Minori").replace("PRESTAZIONE",
										prestazione1.getDescPrestazione()));
							}
						}
					}
				}
			}

			// CONTROLLO 4, Controllo TotaleAdultiDisabili con TotalePerDisabilita
			for (ModelCPrestazioni prestazione2 : rendModC.getListaPrestazioni()) {
				if (prestazione2.getCodPrestazione().equals("R_A.2.1")) {
					for (ModelCTargetUtenze targetUtenza2 : prestazione2.getListaTargetUtenze()) {
						if (targetUtenza2.getCodTargetUtenze().equals("U25")) {
							BigDecimal totaleTargetUtenzaDisabilitaAdulti = targetUtenza2.getValore() != null
									? targetUtenza2.getValore()
									: BigDecimal.ZERO;
							BigDecimal totaleSpecificaDisabilitaAdulti = BigDecimal.ZERO;
							for (ModelCDisabilita disabilita2 : targetUtenza2.getListaDisabilita()) {
								if (!disabilita2.getCodDisabilita().equals("DIS09")
										&& disabilita2.getValore() != null) {
									totaleSpecificaDisabilitaAdulti = totaleSpecificaDisabilitaAdulti
											.add(disabilita2.getValore());
								}
							}
							if (!totaleSpecificaDisabilitaAdulti.equals(BigDecimal.ZERO)
									&& !totaleTargetUtenzaDisabilitaAdulti.equals(BigDecimal.ZERO)
									&& !totaleSpecificaDisabilitaAdulti.equals(totaleTargetUtenzaDisabilitaAdulti)) {

								String errMsgC4 = errorMessageC3;
								response.getErrors().add(errMsgC4.replaceAll("MINADUL", "Adulti").replace("PRESTAZIONE",
										prestazione2.getDescPrestazione()));
							}
						}
					}
				}
			}

			// CONTROLLO 5, Controllo NucleiFamiliari con MinoriDisabili per disabilita
			for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
				if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
					for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
						if (targetUtenza.getCodTargetUtenze().equals("U23")) {
							BigDecimal nucleiFamiliariConMinoriDisabili = BigDecimal.ZERO;
							BigDecimal totSpecDisabMinori = BigDecimal.ZERO;
							for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
								if (!disabilita.getCodDisabilita().equals("DIS08") && disabilita.getValore() != null) {
									totSpecDisabMinori = totSpecDisabMinori.add(disabilita.getValore());
								}
								if (disabilita.getCodDisabilita().equals("DIS08") && disabilita.getValore() != null) {
									nucleiFamiliariConMinoriDisabili = nucleiFamiliariConMinoriDisabili
											.add(disabilita.getValore());
								}
							}
							if ((!nucleiFamiliariConMinoriDisabili.equals(BigDecimal.ZERO)
									&& !totSpecDisabMinori.equals(BigDecimal.ZERO)
									&& nucleiFamiliariConMinoriDisabili.compareTo(totSpecDisabMinori) > 0)
									|| nucleiFamiliariConMinoriDisabili.compareTo(totSpecDisabMinori) > 0) {
								String errMsgC5 = errorMessageC4;
								response.getErrors().add(errMsgC5.replaceAll("MINADUL", "Minori"));
							}
						}
					}
				}
			}

			// CONTROLLO 6, Controllo NucleiFamiliari con AdultiDisabili per disabilita
			for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
				if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
					for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
						if (targetUtenza.getCodTargetUtenze().equals("U25")) {
							BigDecimal nucleiFamiliariConAdultiDisabili = BigDecimal.ZERO;
							BigDecimal totSpecDisabAdulti = BigDecimal.ZERO;
							for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
								if (!disabilita.getCodDisabilita().equals("DIS09") && disabilita.getValore() != null) {
									totSpecDisabAdulti = totSpecDisabAdulti.add(disabilita.getValore());
								}
								if (disabilita.getCodDisabilita().equals("DIS09") && disabilita.getValore() != null) {
									nucleiFamiliariConAdultiDisabili = nucleiFamiliariConAdultiDisabili
											.add(disabilita.getValore());
								}
							}
							if ((!nucleiFamiliariConAdultiDisabili.equals(BigDecimal.ZERO)
									&& !totSpecDisabAdulti.equals(BigDecimal.ZERO)
									&& nucleiFamiliariConAdultiDisabili.compareTo(totSpecDisabAdulti) > 0)
									|| nucleiFamiliariConAdultiDisabili.compareTo(totSpecDisabAdulti) > 0) {
								String errMsgC6 = errorMessageC4;
								response.getErrors().add(errMsgC6.replaceAll("MINADUL", "Adulti"));
							}
						}
					}
				}
			}

			// CONTROLLO 7, Controllo NucleiFamiliari minore di somma target utenza
			for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
				if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
					for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
						// di cui Minori disabili
						if (targetUtenza.getCodTargetUtenze().equals("U23")) {
							for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
								if (disabilita.getCodDisabilita().equals("DIS01")
										|| disabilita.getCodDisabilita().equals("DIS02")
										|| disabilita.getCodDisabilita().equals("DIS03")) {
									BigDecimal valueDisabilitaMinori = disabilita.getValore() != null
											? disabilita.getValore()
											: BigDecimal.ZERO;
									for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
											.getListaDettagliDisabilita()) {
										if (dettaglioDisabilita.getValore() != null && dettaglioDisabilita.getValore()
												.compareTo(valueDisabilitaMinori) == 1) {
											String errMsgC7 = errorMessageC5;
											response.getErrors().add(errMsgC7
													.replace("PRESTAZIONE", prestazione.getDescPrestazione())
													.replace("DICUI", dettaglioDisabilita.getDescDettagliDisabilita())
													.replace("DISABILITA", disabilita.getDescDisabilita())
													.replace("MINADUL", "Minori"));
										}
									}
								}
							}
						}
						// di cui Adulti disabili
						if (targetUtenza.getCodTargetUtenze().equals("U25")) {
							for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
								if (disabilita.getCodDisabilita().equals("DIS01")
										|| disabilita.getCodDisabilita().equals("DIS02")
										|| disabilita.getCodDisabilita().equals("DIS03")) {
									BigDecimal valueDisabilitaAdulti = disabilita.getValore() != null
											? disabilita.getValore()
											: BigDecimal.ZERO;
									for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
											.getListaDettagliDisabilita()) {
										if (dettaglioDisabilita.getValore() != null && dettaglioDisabilita.getValore()
												.compareTo(valueDisabilitaAdulti) == 1) {
											String errMsgC8 = errorMessageC5;
											response.getErrors().add(errMsgC8
													.replace("PRESTAZIONE", prestazione.getDescPrestazione())
													.replace("DICUI", dettaglioDisabilita.getDescDettagliDisabilita())
													.replace("DISABILITA", disabilita.getDescDisabilita())
													.replace("MINADUL", "Adulti"));
										}
									}
								}
							}
						}
					}
				}
			}

			// CONTROLLO 8, Nuclei Totali < (Nuclei Totali Adulti + Nuclei Totali Minori)
			for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
				if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
					BigDecimal totaleNucleiFamiliari = BigDecimal.ZERO;
					BigDecimal totaleNucleiDisabiliAdultiMinori = BigDecimal.ZERO;

					for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
						if (targetUtenza.getCodTargetUtenze().equals("U28")) {
							totaleNucleiFamiliari = targetUtenza.getValore() != null ? targetUtenza.getValore()
									: BigDecimal.ZERO;
						}
						if (targetUtenza.getCodTargetUtenze().equals("U23")
								|| targetUtenza.getCodTargetUtenze().equals("U25")) {
							for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
								if (disabilita.getCodDisabilita().equals("DIS08")
										|| disabilita.getCodDisabilita().equals("DIS09")) {
									totaleNucleiDisabiliAdultiMinori = totaleNucleiDisabiliAdultiMinori.add(
											disabilita.getValore() != null ? disabilita.getValore() : BigDecimal.ZERO);
								}
							}
						}
					}
					if (!totaleNucleiFamiliari.equals(BigDecimal.ZERO)
							&& !totaleNucleiDisabiliAdultiMinori.equals(BigDecimal.ZERO)
							&& totaleNucleiDisabiliAdultiMinori.compareTo(totaleNucleiFamiliari) == 1) {
						response.getErrors().add(errorMessageC6);
					}
				}
			}
		}
		return response;

	}

}
