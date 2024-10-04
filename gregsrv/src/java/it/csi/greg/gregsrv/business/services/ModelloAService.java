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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloADao;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregDVoceModA;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregREnteGestoreContatti;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;

import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart3;
import it.csi.greg.gregsrv.business.entity.GregRTitoloTipologiaVoceModA;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloAInput;
import it.csi.greg.gregsrv.dto.GenericResponseWarnCheckErr;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelDatiEnte;
import it.csi.greg.gregsrv.dto.ModelDatiEnteRendicontazione;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelPrestazioniModA;
import it.csi.greg.gregsrv.dto.ModelRendModAPart3;

import it.csi.greg.gregsrv.dto.ModelRendicontazioneModAPart3;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.SaveModelloAInput;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.dto.VociModelloA;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloAService")
public class ModelloAService {

	@Autowired
	protected ModelloADao modelloADao;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
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

	@Transactional
	public SaveModelloOutput saveModelloA(SaveModelloAInput body, UserInfo userInfo) throws Exception {
		SaveModelloOutput out = new SaveModelloOutput();
		out.setWarnings(new ArrayList<String>());
		out.setErrors(new ArrayList<String>());
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService
				.getRendicontazione(body.getIdRendicontazioneEnte());

		// verifico che nel caso di titpologia Altro 2 se c'e' importo deve esserci anche
		// la nota
		BigDecimal importo = null;
		String testo = null;
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase(SharedConstants.MODELLOA_02ALTRO)) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					if (tipologia.getCodTipologia().equalsIgnoreCase(SharedConstants.MODELLOA_TIPOALTRO)) {
						for (ModelVoceModA voce : tipologia.getListaVoci()) {
							if (voce.getCodVoce().equalsIgnoreCase(SharedConstants.MODELLO_A_31))
								importo = voce.getValoreNumb();
							else if (voce.getCodVoce().equalsIgnoreCase(SharedConstants.MODELLO_A_32))
								testo = voce.getValoreText();
						}
					}
				}

			}
		}
		if (importo != null) {
			if (!importo.equals(BigDecimal.ZERO)) {
				if (!Checker.isValorizzato(testo)) {
					out.setEsito("KO");
					out.setDescrizione(listeService.getMessaggio(SharedConstants.ERROR_TITOLO2).getTestoMessaggio());
					return out;
				}
			} else {
				out.setEsito("KO");
				out.setDescrizione(listeService.getMessaggio(SharedConstants.ERROR_TITOLO2IMPORTO).getTestoMessaggio());
				return out;
			}
		}

		if (Checker.isValorizzato(testo)) {
			if (importo == null) {
				out.setEsito("KO");
				out.setDescrizione(listeService.getMessaggio(SharedConstants.ERROR_TITOLO2IMPORTO).getTestoMessaggio());
				return out;
			}
		}

		String newNotaEnte = "";

		String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
		// MODIFICA STATO RENDICONTAZIONE
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
//				newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + body.getCronologia().getNotaEnte() : body.getCronologia().getNotaEnte();
				newNotaEnte = body.getCronologia().getNotaEnte();
			}

		} else {
//			newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + body.getCronologia().getNotaEnte() : body.getCronologia().getNotaEnte();
			newNotaEnte = body.getCronologia().getNotaEnte();
		}

		// SALVO NOTE DI CRONOLOGIA
		if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(body.getCronologia().getNotaInterna())) {
			GregTCronologia cronologia = new GregTCronologia();
			cronologia.setGregTRendicontazioneEnte(rendicontazione);
			cronologia.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
			cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
			cronologia.setModello("Mod. A");
			cronologia.setUtenteOperazione(userInfo.getCodFisc());
			cronologia.setNotaInterna(body.getCronologia().getNotaInterna());
			cronologia.setNotaPerEnte(newNotaEnte);
			cronologia.setDataOra(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataModifica(new Timestamp(System.currentTimeMillis()));
			datiRendicontazioneService.insertCronologia(cronologia);
		}

		// SALVO DATI RENDICONTAZIONE PART 3
		if (body.getRendicontazioneModAPart3().size() > 0) {

			List<GregRRendicontazioneModAPart3> listaRendPart3 = modelloADao
					.getAllRendModAPart3ByIdEnte(rendicontazione.getIdRendicontazioneEnte());
			List<ModelRendicontazioneModAPart3> listaRModAPart3ADD = new ArrayList<ModelRendicontazioneModAPart3>();

			for (ModelRendicontazioneModAPart3 rendPart3 : body.getRendicontazioneModAPart3()) {
				listaRModAPart3ADD.add(rendPart3);

				if (listaRendPart3.size() > 0) {
					for (GregRRendicontazioneModAPart3 rp3 : listaRendPart3) {
						if (rp3.getGregDVoceModA().getIdVoceModA() == rendPart3.getIdVoce()) {
							listaRModAPart3ADD.remove(rendPart3);
							if (rendPart3.getValore() != null) {
								// Aggiorno rend part 3 precedentemente inserita
								rp3.setDataCancellazione(null);
								rp3.setValore(rendPart3.getValore());
								rp3.setDataModifica(new Timestamp(System.currentTimeMillis()));
								rp3.setUtenteOperazione(userInfo.getCodFisc());
								rp3.setGregTRendicontazioneEnte(rendicontazione);
								modelloADao.saveRendicontazioneModAPart3(rp3);
							} else {
								// elimina record valore da db
								modelloADao.deleteRendModAPart3(rp3.getIdRendicontazioneModAPart3());
							}
						}
					}

					// insert nuova rend part 3
					for (ModelRendicontazioneModAPart3 newRp3Add : listaRModAPart3ADD) {
						if (newRp3Add.getValore() != null) {
							GregRRendicontazioneModAPart3 newRp3 = new GregRRendicontazioneModAPart3();
							GregDVoceModA voceModA = modelloADao.findGregDVoceModAByIdVoce(newRp3Add.getIdVoce());
							newRp3.setGregDVoceModA(voceModA);
							newRp3.setValore(newRp3Add.getValore());
							newRp3.setGregTRendicontazioneEnte(rendicontazione);
							newRp3.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
							newRp3.setDataCreazione(new Timestamp(System.currentTimeMillis()));
							newRp3.setDataModifica(new Timestamp(System.currentTimeMillis()));
							newRp3.setUtenteOperazione(userInfo.getCodFisc());
							modelloADao.saveRendicontazioneModAPart3(newRp3);
							listaRModAPart3ADD.remove(newRp3Add);
							if (listaRModAPart3ADD.size() == 0)
								break;
						}
					}
				} else {
					// insert nuova rend part 3
					if (rendPart3.getValore() != null) {
						GregRRendicontazioneModAPart3 newRp3 = new GregRRendicontazioneModAPart3();
						GregDVoceModA voceModA = modelloADao.findGregDVoceModAByIdVoce(rendPart3.getIdVoce());
						newRp3.setGregDVoceModA(voceModA);
						newRp3.setValore(rendPart3.getValore());
						newRp3.setGregTRendicontazioneEnte(rendicontazione);
						newRp3.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
						newRp3.setDataCreazione(new Timestamp(System.currentTimeMillis()));
						newRp3.setDataModifica(new Timestamp(System.currentTimeMillis()));
						newRp3.setUtenteOperazione(userInfo.getCodFisc());
						modelloADao.saveRendicontazioneModAPart3(newRp3);
					}
				}

			}

		}

		// SALVO DATI RENDICONTAZIONE PART 1
		if (body.getRendicontazioneModAPart1().getListaTitoli().size() > 0) {
			List<GregRRendicontazioneModAPart1> listaDBRendPart1 = modelloADao
					.getAllRendModAPart1ByIdEnte(rendicontazione.getIdRendicontazioneEnte());

			List<ModelVoceModA> listaVociADD = new ArrayList<ModelVoceModA>();
			List<ModelTipologiaModA> listaTipologieADD = new ArrayList<ModelTipologiaModA>();

			for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
				listaTipologieADD.clear();
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					listaTipologieADD.add(tipologia);
					listaVociADD.clear();
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						listaVociADD.add(voce);

						// controllo valori per VOCI - gia esistenti sul db
						if (listaDBRendPart1.size() > 0) {
							for (GregRRendicontazioneModAPart1 rp1 : listaDBRendPart1) {
								if (rp1.getGregRTitoloTipologiaVoceModA().getGregDVoceModA() != null
										&& rp1.getGregRTitoloTipologiaVoceModA().getGregDVoceModA()
												.getIdVoceModA() == voce.getIdVoce()) {
									listaVociADD.remove(voce);

									if (voce.getValoreNumb() != null || voce.getValoreText() != null) {
										// Aggiorno rend part 1 precedentemente inserita - valore VOCE
										GregRTitoloTipologiaVoceModA rttv = modelloADao.getRTitoloTipologiaVoceModA(
												titolo.getIdTitolo(), tipologia.getIdTipologia(), voce.getIdVoce());

										rp1.setDataCancellazione(null);
										rp1.setDataModifica(new Timestamp(System.currentTimeMillis()));
										rp1.setUtenteOperazione(userInfo.getCodFisc());
										if (voce.getValoreNumb() != null) {
											rp1.setValoreNumb(voce.getValoreNumb());
										} else if (voce.getValoreText() != null) {
											rp1.setValoreText(voce.getValoreText());
										}
										rp1.setGregTRendicontazioneEnte(rendicontazione);
										rp1.setGregRTitoloTipologiaVoceModA(rttv);
										modelloADao.saveRendicontazioneModAPart1(rp1);

										// RENDIONTAZIONE MOD A PART 2 - PRESTAZIONI / UTENZE
										if (voce.getPrestazioni() != null) {
											addOrUpdateRendicontazioneModAPart2(voce, rendicontazione, userInfo);
										}

									} else {
										// elimina record valore da db
										modelloADao.deleteRendModAPart1(rp1.getIdRendicontazioneModAPart1());
									}
								}
							}

							// se non ha trovato quella voce nel DB, quella voca va aggiunta
							// in questo punto deve inserire
							for (ModelVoceModA mv : listaVociADD) {
								if (mv.getValoreNumb() != null || mv.getValoreText() != null) {
									GregRRendicontazioneModAPart1 newRp1 = new GregRRendicontazioneModAPart1();
									GregRTitoloTipologiaVoceModA rttv = modelloADao.getRTitoloTipologiaVoceModA(
											titolo.getIdTitolo(), tipologia.getIdTipologia(), mv.getIdVoce());

									newRp1.setGregRTitoloTipologiaVoceModA(rttv);
									newRp1.setGregTRendicontazioneEnte(rendicontazione);
									if (mv.getValoreNumb() != null) {
										newRp1.setValoreNumb(mv.getValoreNumb());
									} else if (mv.getValoreText() != null) {
										newRp1.setValoreText(mv.getValoreText());
									}
									newRp1.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
									newRp1.setDataCreazione(new Timestamp(System.currentTimeMillis()));
									newRp1.setDataModifica(new Timestamp(System.currentTimeMillis()));
									newRp1.setUtenteOperazione(userInfo.getCodFisc());
									modelloADao.saveRendicontazioneModAPart1(newRp1);

									// RENDIONTAZIONE MOD A PART 2 - PRESTAZIONI / UTENZE
									if (voce.getPrestazioni() != null) {
										addOrUpdateRendicontazioneModAPart2(voce, rendicontazione, userInfo);
									}
								}
							}
							listaVociADD.remove(voce);

						} else {
							// listarendDB1 vuota - nuovi inserimenti su db
							// insert nuova rend part 1 - voce
							if (voce.getValoreNumb() != null || voce.getValoreText() != null) {
								GregRRendicontazioneModAPart1 newRp1 = new GregRRendicontazioneModAPart1();
								GregRTitoloTipologiaVoceModA rttv = modelloADao.getRTitoloTipologiaVoceModA(
										titolo.getIdTitolo(), tipologia.getIdTipologia(), voce.getIdVoce());

								newRp1.setGregRTitoloTipologiaVoceModA(rttv);
								newRp1.setGregTRendicontazioneEnte(rendicontazione);
								if (voce.getValoreNumb() != null) {
									newRp1.setValoreNumb(voce.getValoreNumb());
								} else if (voce.getValoreText() != null) {
									newRp1.setValoreText(voce.getValoreText());
								}
								newRp1.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
								newRp1.setDataCreazione(new Timestamp(System.currentTimeMillis()));
								newRp1.setDataModifica(new Timestamp(System.currentTimeMillis()));
								newRp1.setUtenteOperazione(userInfo.getCodFisc());
								modelloADao.saveRendicontazioneModAPart1(newRp1);

								// RENDIONTAZIONE MOD A PART 2 - PRESTAZIONI / UTENZE
								if (voce.getPrestazioni() != null) {
									addOrUpdateRendicontazioneModAPart2(voce, rendicontazione, userInfo);
								}
							}
						}
					}

					// controllo valori per TIPOLOGIE se non esistono voci
					if (tipologia.getListaVoci() == null || tipologia.getListaVoci().isEmpty()) {
						if (listaDBRendPart1.size() > 0) {
							for (GregRRendicontazioneModAPart1 rp1 : listaDBRendPart1) {

								if (rp1.getGregRTitoloTipologiaVoceModA().getGregDTipologiaModA()
										.getIdTipologiaModA() == tipologia.getIdTipologia()) {
									listaTipologieADD.remove(tipologia);

									if (tipologia.getValore() != null) {
										// Aggiorno rend part 1 precedentemente inserita - valore VOCE
										GregRTitoloTipologiaVoceModA rttv = modelloADao.getRTitoloTipologiaVoceModA(
												titolo.getIdTitolo(), tipologia.getIdTipologia(), null);

										rp1.setDataCancellazione(null);
										rp1.setDataModifica(new Timestamp(System.currentTimeMillis()));
										rp1.setUtenteOperazione(userInfo.getCodFisc());
										rp1.setValoreNumb(tipologia.getValore());
										rp1.setGregTRendicontazioneEnte(rendicontazione);
										rp1.setGregRTitoloTipologiaVoceModA(rttv);
										modelloADao.saveRendicontazioneModAPart1(rp1);
									} else {
										// elimina record valore
										modelloADao.deleteRendModAPart1(rp1.getIdRendicontazioneModAPart1());
									}
								}
							}

							// se non ha trovato quella voce, quella voca va aggiunta
							// in questo punto deve inserire
							for (ModelTipologiaModA mt : listaTipologieADD) {
								if (mt.getValore() != null) {
									GregRRendicontazioneModAPart1 newRp1 = new GregRRendicontazioneModAPart1();
									GregRTitoloTipologiaVoceModA rttv = modelloADao.getRTitoloTipologiaVoceModA(
											titolo.getIdTitolo(), mt.getIdTipologia(), null);

									newRp1.setGregRTitoloTipologiaVoceModA(rttv);
//									newRp1.setValore(tipologia.getValore().toString());
									newRp1.setValoreNumb(mt.getValore());
									newRp1.setGregTRendicontazioneEnte(rendicontazione);
									newRp1.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
									newRp1.setDataCreazione(new Timestamp(System.currentTimeMillis()));
									newRp1.setDataModifica(new Timestamp(System.currentTimeMillis()));
									newRp1.setUtenteOperazione(userInfo.getCodFisc());
									modelloADao.saveRendicontazioneModAPart1(newRp1);
								}
							}
							listaTipologieADD.remove(tipologia);

						} else {
							// insert nuova rend part 1 - tipologia
							if (tipologia.getValore() != null) {
								GregRRendicontazioneModAPart1 newRp1 = new GregRRendicontazioneModAPart1();
								GregRTitoloTipologiaVoceModA rttv = modelloADao.getRTitoloTipologiaVoceModA(
										titolo.getIdTitolo(), tipologia.getIdTipologia(), null);

								newRp1.setGregRTitoloTipologiaVoceModA(rttv);
								newRp1.setValoreNumb(tipologia.getValore());
								newRp1.setGregTRendicontazioneEnte(rendicontazione);
								newRp1.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
								newRp1.setDataCreazione(new Timestamp(System.currentTimeMillis()));
								newRp1.setDataModifica(new Timestamp(System.currentTimeMillis()));
								newRp1.setUtenteOperazione(userInfo.getCodFisc());
								modelloADao.saveRendicontazioneModAPart1(newRp1);
							}
						}
					}
				}
			}
		}

		// INVIO EMAIL
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
				.replace("oggettosalvo", "Modello A"));
		out.setEsito("OK");
		return out;
	}

	private void addOrUpdateRendicontazioneModAPart2(ModelVoceModA voce, GregTRendicontazioneEnte rendicontazione,
			UserInfo userInfo) {
		List<ModelTargetUtenza> listaUtenzeADD = new ArrayList<ModelTargetUtenza>();
		List<GregRRendicontazioneModAPart2> listaDBRendPart2 = modelloADao
				.getAllRendModAPart2ByIdEnte(rendicontazione.getIdRendicontazioneEnte());

		for (ModelPrestazioneUtenzaModA prestazioneSR : voce.getPrestazioni().getPrestazioniRS()) {
			listaUtenzeADD.clear();
			for (ModelTargetUtenza utenza : prestazioneSR.getListaTargetUtenza()) {
				if (utenza.isEntry()) {
					listaUtenzeADD.add(utenza);
					if (listaDBRendPart2.size() > 0) {
						for (GregRRendicontazioneModAPart2 rp2 : listaDBRendPart2) {
							if (rp2.getGregRPrestReg1UtenzeRegionali1().getGregTPrestazioniRegionali1()
									.getCodPrestReg1().equalsIgnoreCase(prestazioneSR.getCodPrestazione())
									&& rp2.getGregRPrestReg1UtenzeRegionali1().getGregDTargetUtenza().getCodUtenza()
											.equalsIgnoreCase(utenza.getCodTargetUtenza())) {
								listaUtenzeADD.remove(utenza);
								if (utenza.getValore() != null) {
									// Aggiorno rend part 2 precedentemente inserita
									GregRPrestReg1UtenzeRegionali1 rpru1 = modelloADao
											.getGregRPrestReg1UtenzeRegionali1(prestazioneSR.getCodPrestazione(),
													utenza.getCodTargetUtenza(), rendicontazione.getAnnoGestione());

									rp2.setDataCancellazione(null);
									rp2.setDataModifica(new Timestamp(System.currentTimeMillis()));
									rp2.setUtenteOperazione(userInfo.getCodFisc());
									rp2.setValore(utenza.getValore());
									rp2.setGregTRendicontazioneEnte(rendicontazione);
									rp2.setGregRPrestReg1UtenzeRegionali1(rpru1);
									modelloADao.saveRendicontazioneModAPart2(rp2);
								} else {
									// elimina record valore da db
									modelloADao.deleteRendModAPart2(rp2.getIdRendicontazioneModAPart2());
								}
							}

						}
					}

					// se non ha trovato quella voce, quella voca va aggiunta
					// in questo punto deve inserire
					for (ModelTargetUtenza u : listaUtenzeADD) {
						if (u.getValore() != null) {
							// aggiungo rend part 2 precedentemente inserita
							GregRRendicontazioneModAPart2 newRp2 = new GregRRendicontazioneModAPart2();
							GregRPrestReg1UtenzeRegionali1 rpru1 = modelloADao.getGregRPrestReg1UtenzeRegionali1(
									prestazioneSR.getCodPrestazione(), utenza.getCodTargetUtenza(),
									rendicontazione.getAnnoGestione());

							newRp2.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
							newRp2.setDataCreazione(new Timestamp(System.currentTimeMillis()));
							newRp2.setDataCancellazione(null);
							newRp2.setDataModifica(new Timestamp(System.currentTimeMillis()));
							newRp2.setUtenteOperazione(userInfo.getCodFisc());
							newRp2.setValore(u.getValore());
							newRp2.setGregTRendicontazioneEnte(rendicontazione);
							newRp2.setGregRPrestReg1UtenzeRegionali1(rpru1);
							modelloADao.saveRendicontazioneModAPart2(newRp2);
						}
					}
					listaUtenzeADD.remove(utenza);
				}
			}
		}

		for (ModelPrestazioneUtenzaModA prestazioneCD : voce.getPrestazioni().getPrestazioniCD()) {
			listaUtenzeADD.clear();
			for (ModelTargetUtenza utenza : prestazioneCD.getListaTargetUtenza()) {
				if (utenza.isEntry()) {
					listaUtenzeADD.add(utenza);
					if (listaDBRendPart2.size() > 0) {
						for (GregRRendicontazioneModAPart2 rp2 : listaDBRendPart2) {
							if (rp2.getGregRPrestReg1UtenzeRegionali1().getGregTPrestazioniRegionali1()
									.getCodPrestReg1().equalsIgnoreCase(prestazioneCD.getCodPrestazione())
									&& rp2.getGregRPrestReg1UtenzeRegionali1().getGregDTargetUtenza().getCodUtenza()
											.equalsIgnoreCase(utenza.getCodTargetUtenza())) {
								listaUtenzeADD.remove(utenza);
								if (utenza.getValore() != null) {
									// Aggiorno rend part 2 precedentemente inserita
									GregRPrestReg1UtenzeRegionali1 rpru1 = modelloADao
											.getGregRPrestReg1UtenzeRegionali1(prestazioneCD.getCodPrestazione(),
													utenza.getCodTargetUtenza(), rendicontazione.getAnnoGestione());

									rp2.setDataCancellazione(null);
									rp2.setDataModifica(new Timestamp(System.currentTimeMillis()));
									rp2.setUtenteOperazione(userInfo.getCodFisc());
									rp2.setValore(utenza.getValore());
									rp2.setGregTRendicontazioneEnte(rendicontazione);
									rp2.setGregRPrestReg1UtenzeRegionali1(rpru1);
									modelloADao.saveRendicontazioneModAPart2(rp2);
								} else {
									// elimina record valore da db
									modelloADao.deleteRendModAPart2(rp2.getIdRendicontazioneModAPart2());
								}
							}

						}
					}

					// se non ha trovato quella voce, quella voca va aggiunta
					// in questo punto deve inserire
					for (ModelTargetUtenza u : listaUtenzeADD) {
						if (u.getValore() != null) {
							// aggiungo rend part 2 precedentemente inserita
							GregRRendicontazioneModAPart2 newRp2 = new GregRRendicontazioneModAPart2();
							GregRPrestReg1UtenzeRegionali1 rpru1 = modelloADao.getGregRPrestReg1UtenzeRegionali1(
									prestazioneCD.getCodPrestazione(), utenza.getCodTargetUtenza(),
									rendicontazione.getAnnoGestione());

							newRp2.setDataInizioValidita(new Timestamp(System.currentTimeMillis()));
							newRp2.setDataCreazione(new Timestamp(System.currentTimeMillis()));
							newRp2.setDataCancellazione(null);
							newRp2.setDataModifica(new Timestamp(System.currentTimeMillis()));
							newRp2.setUtenteOperazione(userInfo.getCodFisc());
							newRp2.setValore(u.getValore());
							newRp2.setGregTRendicontazioneEnte(rendicontazione);
							newRp2.setGregRPrestReg1UtenzeRegionali1(rpru1);
							modelloADao.saveRendicontazioneModAPart2(newRp2);
						}
					}
					listaUtenzeADD.remove(utenza);
				}
			}
		}

	}

	public VociModelloA getListaVociModA(Integer idScheda) {

		// recupero informazioni rendicontazioneModAPart3 + lista voci
		List<GregRRendicontazioneModAPart3> listaRp3 = modelloADao.findAllValoriModAPart3ByEnte(idScheda);
		List<GregDVoceModA> listaVociModAPart3 = modelloADao.findVociBySezioneModello("sezione fondi");
		List<ModelRendModAPart3> listaVociPart3 = new ArrayList<ModelRendModAPart3>();

		// recupero informazioni rendicontazioneModAPart1 + lista voci
		List<GregRRendicontazioneModAPart1> listaRp1 = modelloADao.findAllValoriModAByEnte(idScheda);
		List<GregRTitoloTipologiaVoceModA> listaVociModA = modelloADao.findAllVociModA();
		List<ModelVoceModA> listaVoci = new ArrayList<ModelVoceModA>();
		List<ModelTipologiaModA> listaTipologie = new ArrayList<ModelTipologiaModA>();
		List<ModelTitoloModA> listaTitoli = new ArrayList<ModelTitoloModA>();

		// recupero informazioni rendicontazioneModAPart2 + lista voci
		List<GregRRendicontazioneModAPart2> listaRp2 = modelloADao.findAllValoriModAPart2ByEnte(idScheda);

		VociModelloA listaFinale = null;
		listaFinale = new VociModelloA();

		// popolo le voci della tabella rend mod a part 1
		if (listaVociModA.size() > 0) {
			for (GregRTitoloTipologiaVoceModA element : listaVociModA) {
				ModelTitoloModA titolo = new ModelTitoloModA();
				titolo.setListaTipologie(new ArrayList<ModelTipologiaModA>());
				titolo.setIdTitolo(element.getGregDTitoloModA().getIdTitoloModA());
				titolo.setCodTitolo(element.getGregDTitoloModA().getCodTitoloModA());
				titolo.setDescTitolo(element.getGregDTitoloModA().getDesTitoloModA());
				titolo.setMsgInformativo(element.getGregDTitoloModA().getMsgInformativo());
				titolo.setOrdinamento(element.getGregDTitoloModA().getOrdinamento());

				if (listaTitoli.contains(titolo)) {
					titolo = listaTitoli.get(listaTitoli.indexOf(titolo));
				} else {
					listaTitoli.add(titolo);
				}
				listaTipologie = titolo.getListaTipologie();

				ModelTipologiaModA tipologia = new ModelTipologiaModA();
				tipologia.setListaVoci(new ArrayList<ModelVoceModA>());
				tipologia.setIdTipologia(element.getGregDTipologiaModA().getIdTipologiaModA());
				tipologia.setCodTipologia(element.getGregDTipologiaModA().getCodTipologiaModA());
				tipologia.setDescCodTipologia(element.getGregDTipologiaModA().getDesCodTipologiaModA());
				tipologia.setDescTipologia(element.getGregDTipologiaModA().getDesTipologiaModA());
				tipologia.setMsgInformativo(element.getGregDTipologiaModA().getMsgInformativo());
				tipologia.setOrdinamento(element.getGregDTipologiaModA().getOrdinamento());

				if (listaTipologie.contains(tipologia)) {
					tipologia = listaTipologie.get(listaTipologie.indexOf(tipologia));
				} else {
					listaTipologie.add(tipologia);
				}
				listaVoci = tipologia.getListaVoci();

				ModelVoceModA voce = new ModelVoceModA();

				if (listaRp1.size() > 0) {
					for (GregRRendicontazioneModAPart1 rp1 : listaRp1) {
						if (rp1.getGregRTitoloTipologiaVoceModA().getIdTitoloTipologiaVoceModA() == element
								.getIdTitoloTipologiaVoceModA()) {

							if (element.getGregDVoceModA() != null) {
								voce.setValoreText(rp1.getValoreText());
								voce.setValoreNumb(rp1.getValoreNumb());
								if (rp1.getGregRTitoloTipologiaVoceModA().getGregDTipologiaQuota() != null) {
									voce.setCodTipologiaQuota(rp1.getGregRTitoloTipologiaVoceModA()
											.getGregDTipologiaQuota().getCodTipologiaQuota());
								}
							} else if (tipologia.getIdTipologia() != null) {
								tipologia.setValore(rp1.getValoreNumb());
							}
						}
					}
				}

				if (element.getGregDVoceModA() != null) {
					voce.setIdVoce(element.getGregDVoceModA().getIdVoceModA());
					voce.setCodVoce(element.getGregDVoceModA().getCodVoceModA());
					voce.setDescVoce(element.getGregDVoceModA().getDesVoceModA());
					voce.setMsgInformativo(element.getGregDVoceModA().getMsgInformativo());
					voce.setOrdinamento(element.getGregDVoceModA().getOrdinamento());

					if (element.getGregDTipologiaQuota() != null && (element.getGregDTipologiaQuota()
							.getCodTipologiaQuota().equalsIgnoreCase("02")
							|| element.getGregDTipologiaQuota().getCodTipologiaQuota().equalsIgnoreCase("03"))) {

						voce.setPrestazioni(getPrestazioniModA(idScheda,
								element.getGregDTipologiaQuota().getCodTipologiaQuota(), listaRp2));
					}

					listaVoci.add(voce);
				}

				tipologia.setListaVoci(listaVoci);
				titolo.setListaTipologie(listaTipologie);
			}

			listaFinale.setListaTitoli(listaTitoli);
		}

		// popolo le voci della tabella rend mod a part 3
		if (listaVociModAPart3.size() > 0) {
			for (GregDVoceModA element : listaVociModAPart3) {
				ModelRendModAPart3 rp3 = new ModelRendModAPart3();
				rp3.setIdVoce(element.getIdVoceModA());
				rp3.setCodVoce(element.getCodVoceModA());
				rp3.setDescVoce(element.getDesVoceModA());
				rp3.setMsgInformativo(element.getMsgInformativo());
				rp3.setOrdinamento(element.getOrdinamento());

				if (listaRp3.size() > 0) {
					for (GregRRendicontazioneModAPart3 lrp3 : listaRp3) {
						if (lrp3.getGregDVoceModA().getIdVoceModA() == element.getIdVoceModA()) {
							rp3.setValore(lrp3.getValore());
						}
					}
				}

				listaVociPart3.add(rp3);
			}

			listaFinale.setListaVociModAPart3(listaVociPart3);
		}

		return listaFinale;
	}

	@SuppressWarnings("rawtypes")
	public List<ModelValoriModA> getDatiModelloA(Integer idrendicontazione, String codVoce) {
		ArrayList<ModelValoriModA> elencovoci = new ArrayList<ModelValoriModA>();
		List<Object> entiresult = modelloADao.getDatiModelloAPerRendicontazioneECodVoce(idrendicontazione, codVoce);
		Iterator itr = entiresult.iterator();
//		boolean esci = false;
		try {
			while (itr.hasNext()) {
				ModelValoriModA voce = new ModelValoriModA();
				Object[] obj = (Object[]) itr.next();
				voce.setCodVoce(String.valueOf(obj[0]));
				voce.setDescVoce(String.valueOf(obj[1]));
				voce.setSezioneModello(String.valueOf(obj[2]));
				voce.setValore(new BigDecimal(String.valueOf(obj[3])));
				voce.setCodTipologia(String.valueOf(obj[4]));
				voce.setDescTipologia(String.valueOf(obj[5]));
				elencovoci.add(voce);
			}
			return elencovoci;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private ModelPrestazioniModA getPrestazioniModA(Integer idScheda, String codQuota,
			List<GregRRendicontazioneModAPart2> listaRp2) {
		ModelPrestazioniModA prestazioni = new ModelPrestazioniModA();
		List<ModelPrestazioneUtenzaModA> listaPrestRs = new ArrayList<ModelPrestazioneUtenzaModA>();
		List<ModelPrestazioneUtenzaModA> listaPrestCd = new ArrayList<ModelPrestazioneUtenzaModA>();
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idScheda);
		List<GregTPrestazioniRegionali1> prestMadriRS = modelloADao.getPrestazioniMadriModA(idScheda, "MA03", "01");
		List<GregTPrestazioniRegionali1> prestazioniFiglieRS = new ArrayList<GregTPrestazioniRegionali1>();
		for (GregTPrestazioniRegionali1 prest : prestMadriRS) {
			prestazioniFiglieRS.addAll(modelloADao.getPrestazioniFiglieModA(prest.getIdPrestReg1(), codQuota,
					rendicontazione.getAnnoGestione()));
		}

		List<GregTPrestazioniRegionali1> prestMadriCD = modelloADao.getPrestazioniMadriModA(idScheda, "MA03", "02");
		List<GregTPrestazioniRegionali1> prestazioniFiglieCD = new ArrayList<GregTPrestazioniRegionali1>();
		for (GregTPrestazioniRegionali1 prest : prestMadriCD) {
			prestazioniFiglieCD.addAll(modelloADao.getPrestazioniFiglieModA(prest.getIdPrestReg1(), codQuota,
					rendicontazione.getAnnoGestione()));
		}

		List<GregDTargetUtenza> targetUtenze = modelloADao.getTargetUtenzeByCodFlusso("REG",
				rendicontazione.getAnnoGestione());
		List<ModelTargetUtenza> listasubTotaliUt = new ArrayList<ModelTargetUtenza>();
		for (GregDTargetUtenza d : targetUtenze) {
			listasubTotaliUt.add(new ModelTargetUtenza(d));
		}
		prestazioni.setSubtotaliRS(listasubTotaliUt);
		prestazioni.setSubtotaliCD(listasubTotaliUt);
		prestazioni.setTotaliSRCD(listasubTotaliUt);

		for (GregTPrestazioniRegionali1 prest : prestazioniFiglieRS) {
			ModelPrestazioneUtenzaModA mPrest = new ModelPrestazioneUtenzaModA();
			mPrest.setIdPrestazione(prest.getIdPrestReg1());
			mPrest.setCodPrestazione(prest.getCodPrestReg1());
			mPrest.setDescPrestazione(prest.getDesPrestReg1());
			List<ModelTargetUtenza> listaUt = new ArrayList<ModelTargetUtenza>();
			for (GregDTargetUtenza d : targetUtenze) {
				listaUt.add(new ModelTargetUtenza(d));
			}
			for (GregRPrestReg1UtenzeRegionali1 prestUt : prest.getGregRPrestReg1UtenzeRegionali1s()) {
//				mPrest = new ModelPrestazioneUtenzaModA(prestUt);
				for (ModelTargetUtenza modelUt : listaUt) {
					if (prestUt.getGregDTargetUtenza() != null && prestUt.getGregDTargetUtenza().getCodUtenza()
							.equalsIgnoreCase(modelUt.getCodTargetUtenza())) {
						modelUt.setEntry(true);
						for (GregRRendicontazioneModAPart2 rend : listaRp2) {
							if (rend.getGregRPrestReg1UtenzeRegionali1().getIdPrestReg1UtenzaRegionale1()
									.equals(prestUt.getIdPrestReg1UtenzaRegionale1())) {
								modelUt.setValore(rend.getValore());
							}
						}
					}
				}
			}
			mPrest.setListaTargetUtenza(listaUt);
			listaPrestRs.add(mPrest);
		}

		for (GregTPrestazioniRegionali1 prest : prestazioniFiglieCD) {
			ModelPrestazioneUtenzaModA mPrest = new ModelPrestazioneUtenzaModA();
			mPrest.setIdPrestazione(prest.getIdPrestReg1());
			mPrest.setCodPrestazione(prest.getCodPrestReg1());
			mPrest.setDescPrestazione(prest.getDesPrestReg1());
			List<ModelTargetUtenza> listaUt = new ArrayList<ModelTargetUtenza>();
			for (GregDTargetUtenza d : targetUtenze) {
				listaUt.add(new ModelTargetUtenza(d));
			}
			for (GregRPrestReg1UtenzeRegionali1 prestUt : prest.getGregRPrestReg1UtenzeRegionali1s()) {
//				mPrest = new ModelPrestazioneUtenzaModA(prestUt);
				for (ModelTargetUtenza modelUt : listaUt) {
					if (prestUt.getGregDTargetUtenza() != null && prestUt.getGregDTargetUtenza().getCodUtenza()
							.equalsIgnoreCase(modelUt.getCodTargetUtenza())) {
						modelUt.setEntry(true);
						for (GregRRendicontazioneModAPart2 rend : listaRp2) {
							if (rend.getGregRPrestReg1UtenzeRegionali1().getIdPrestReg1UtenzaRegionale1()
									.equals(prestUt.getIdPrestReg1UtenzaRegionale1())) {
								modelUt.setValore(rend.getValore());
							}
						}
					}
				}
			}
			mPrest.setListaTargetUtenza(listaUt);
			listaPrestCd.add(mPrest);
		}

		prestazioni.setPrestazioniRS(listaPrestRs);
		prestazioni.setPrestazioniCD(listaPrestCd);
		return prestazioni;
	}

	@Transactional
	public String esportaModelloA(EsportaModelloAInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloA");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		DataFormat format = workbook.createDataFormat();
		Row row = sheet.createRow(rowCount);
		int maxcolumn = 0;

		// grigio chiaro
		HSSFPalette palette = workbook.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 218, (byte) 227, (byte) 243);

		// grigio scuro
		HSSFPalette palette1 = workbook.getCustomPalette();
		palette1.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index, (byte) 192, (byte) 192, (byte) 192);

		// verde chiaro
		HSSFPalette palette2 = workbook.getCustomPalette();
		palette2.setColorAtIndex(HSSFColor.LIGHT_GREEN.index, (byte) 197, (byte) 224, (byte) 180);
		// viola
		HSSFPalette palette3 = workbook.getCustomPalette();
		palette3.setColorAtIndex(HSSFColor.VIOLET.index, (byte) 153, (byte) 153, (byte) 255);

		// giallino
		HSSFPalette palette4 = workbook.getCustomPalette();
		palette4.setColorAtIndex(HSSFColor.LAVENDER.index, (byte) 255, (byte) 242, (byte) 204);

		// grigio not isentry
		HSSFPalette palette5 = workbook.getCustomPalette();
		palette5.setColorAtIndex(HSSFColor.AQUA.index, (byte) 221, (byte) 221, (byte) 221);

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
		cellStyle12b.setFont(font12b);
		cellStyle12b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 bold
		CellStyle cellStyle10b = sheet.getWorkbook().createCellStyle();
		cellStyle10b.setAlignment(CellStyle.ALIGN_CENTER);
		Font font10b = sheet.getWorkbook().createFont();
		font10b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font10b.setFontHeightInPoints((short) 10);
		font10b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle10b.setFont(font10b);
		cellStyle10b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 10 corsivo
		CellStyle cellStyle10c = sheet.getWorkbook().createCellStyle();
		cellStyle10c.setAlignment(CellStyle.ALIGN_RIGHT);
		Font font10c = sheet.getWorkbook().createFont();
		font10c.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10c.setFontHeightInPoints((short) 10);
		font10c.setFontName(HSSFFont.FONT_ARIAL);
		font10c.setItalic(true);
		cellStyle10c.setFont(font10c);
		// crea stili arial 10
		CellStyle cellStyle10 = sheet.getWorkbook().createCellStyle();
		cellStyle10.setAlignment(CellStyle.ALIGN_LEFT);
		Font font10 = sheet.getWorkbook().createFont();
		font10.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font10.setFontHeightInPoints((short) 10);
		font10.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle10.setFont(font10);
		cellStyle10.setWrapText(true);
		// crea stili arial 10 bold corsivo
		CellStyle cellStyle10bc = sheet.getWorkbook().createCellStyle();
		cellStyle10bc.setAlignment(CellStyle.ALIGN_RIGHT);
		Font font10bc = sheet.getWorkbook().createFont();
		font10bc.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font10bc.setFontHeightInPoints((short) 10);
		font10bc.setFontName(HSSFFont.FONT_ARIAL);
		font10bc.setItalic(true);
		cellStyle10bc.setFont(font10bc);
		// crea stile arial rigatitolo bordo sinistro
		CellStyle cellStyletitolos = sheet.getWorkbook().createCellStyle();
		cellStyletitolos.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolos.setFont(font12b);
		cellStyletitolos.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyletitolos.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyletitolos.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyletitolos.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// crea stile arial rigatitolo bordo top
		CellStyle cellStyletitolot = sheet.getWorkbook().createCellStyle();
		cellStyletitolot.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolot.setFont(font12b);
		cellStyletitolot.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyletitolot.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyletitolot.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyletitolot.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// crea stile arial rigatitolo bordo right
		CellStyle cellStyletitolor = sheet.getWorkbook().createCellStyle();
		cellStyletitolor.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolor.setFont(font12b);
		cellStyletitolor.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyletitolor.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyletitolor.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyletitolor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyletitolor.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// crea stili arial dati testo
		CellStyle cellStylecelletesto = sheet.getWorkbook().createCellStyle();
		cellStylecelletesto.setAlignment(CellStyle.ALIGN_LEFT);
		cellStylecelletesto.setFont(font10);
		cellStylecelletesto.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStylecelletesto.setBorderRight(CellStyle.BORDER_THIN);
		cellStylecelletesto.setBorderTop(CellStyle.BORDER_THIN);
		cellStylecelletesto.setBorderBottom(CellStyle.BORDER_THIN);
		cellStylecelletesto.setBorderLeft(CellStyle.BORDER_THIN);
		cellStylecelletesto.setWrapText(true);
		// crea stili arial dati importo
		CellStyle cellStylecelleimporto = sheet.getWorkbook().createCellStyle();
		cellStylecelleimporto.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStylecelleimporto.setFont(font10c);
		cellStylecelleimporto.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStylecelleimporto.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStylecelleimporto.setBorderTop(CellStyle.BORDER_THIN);
		cellStylecelleimporto.setBorderBottom(CellStyle.BORDER_THIN);
		cellStylecelleimporto.setBorderLeft(CellStyle.BORDER_THIN);
		cellStylecelleimporto.setWrapText(true);
		// crea stili arial valore
		CellStyle cellStylecellevalore = sheet.getWorkbook().createCellStyle();
		cellStylecellevalore.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStylecellevalore.setFont(font10c);
		cellStylecellevalore.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStylecellevalore.setBorderRight(CellStyle.BORDER_THIN);
		cellStylecellevalore.setBorderTop(CellStyle.BORDER_THIN);
		cellStylecellevalore.setBorderBottom(CellStyle.BORDER_THIN);
		cellStylecellevalore.setBorderLeft(CellStyle.BORDER_THIN);
		cellStylecellevalore.setWrapText(true);
		// totale
		CellStyle cellStyle12bTotale = sheet.getWorkbook().createCellStyle();
		cellStyle12bTotale.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12bTotale.setFont(font12b);
		cellStyle12bTotale.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle12bTotale.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle12bTotale.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// totale importo
		CellStyle cellStyle12bTotaleI = sheet.getWorkbook().createCellStyle();
		cellStyle12bTotaleI.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle12bTotaleI.setFont(font12b);
		cellStyle12bTotaleI.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle12bTotaleI.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyle12bTotaleI.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12bTotaleI.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyle12bTotaleI.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle12bTotaleI.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle12bTotaleI.setBorderLeft(CellStyle.BORDER_THIN);

		// crea stile arial rigatitolo bordo sinistro
		CellStyle cellStyletitolopart2 = sheet.getWorkbook().createCellStyle();
		cellStyletitolopart2.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyletitolopart2.setFont(fontb12I);
		cellStyletitolopart2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyletitolopart2.setBorderRight(CellStyle.BORDER_THIN);
		cellStyletitolopart2.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyletitolopart2.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyletitolopart2.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyletitolopart2.setWrapText(true);

		// totale residenziali
		CellStyle cellStyle10bY = sheet.getWorkbook().createCellStyle();
		cellStyle10bY.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle10bY.setFont(font10b);
		cellStyle10bY.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10bY.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		cellStyle10bY.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// totale residenziali
		CellStyle cellStyle10bL = sheet.getWorkbook().createCellStyle();
		cellStyle10bL.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle10bL.setFont(font10b);
		cellStyle10bL.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10bL.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyle10bL.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// crea stile arial bordo doppio
		CellStyle cellStyleSRB = sheet.getWorkbook().createCellStyle();
		cellStyleSRB.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleSRB.setFont(font10);
		cellStyleSRB.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleSRB.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleSRB.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleSRB.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleSRB.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyleSRB.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyleSRB.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleSRB.setWrapText(true);

		// crea stile arial bordo doppio bianco
		CellStyle cellStyleSRBW = sheet.getWorkbook().createCellStyle();
		cellStyleSRBW.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleSRBW.setFont(font10);
		cellStyleSRBW.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleSRBW.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleSRBW.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleSRBW.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleSRBW.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyleSRBW.setWrapText(true);

		// crea stile arial bordo doppio bold
		CellStyle cellStyleSRBB = sheet.getWorkbook().createCellStyle();
		cellStyleSRBB.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleSRBB.setFont(font10b);
		cellStyleSRBB.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleSRBB.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyleSRBB.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyleSRBB.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyleSRBB.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyleSRBB.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyleSRBB.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleSRBB.setWrapText(true);

		// crea stile arial utenze
		CellStyle cellStyleU = sheet.getWorkbook().createCellStyle();
		cellStyleU.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleU.setFont(font10);
		cellStyleU.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleU.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleU.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleU.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleU.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		cellStyleU.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// crea stili arial 10 corsivo
		CellStyle cellStyle10cw = sheet.getWorkbook().createCellStyle();
		cellStyle10cw.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10cw.setFont(font10c);
		cellStyle10cw.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10cw.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10cw.setBorderBottom(CellStyle.BORDER_THIN);
		// crea stili arial 10 corsivo
		CellStyle cellStyle10cv = sheet.getWorkbook().createCellStyle();
		cellStyle10cv.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10cv.setFont(font10c);
		cellStyle10cv.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10cv.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10cv.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10cv.setFillForegroundColor(HSSFColor.VIOLET.index);
		cellStyle10cv.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// crea stili arial 10 bold
		CellStyle cellStyle10bT = sheet.getWorkbook().createCellStyle();
		cellStyle10bT.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10bT.setFont(font10b);
		cellStyle10bT.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10bT.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10bT.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10bT.setBorderBottom(CellStyle.BORDER_THIN);

		// crea stili arial 10 bold
		CellStyle cellStyle10bTND = sheet.getWorkbook().createCellStyle();
		cellStyle10bTND.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10bTND.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10bTND.setBorderLeft(CellStyle.BORDER_THIN);
		// crea stili arial valore
		CellStyle cellStyle10bcT = sheet.getWorkbook().createCellStyle();
		cellStyle10bcT.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10bcT.setFont(font10bc);
		cellStyle10bcT.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10bcT.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10bcT.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10bcT.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10bcT.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10bcT.setWrapText(true);

		// crea stili arial valore
		CellStyle cellStyle10bcTG = sheet.getWorkbook().createCellStyle();
		cellStyle10bcTG.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle10bcTG.setFont(font10bc);
		cellStyle10bcTG.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10bcTG.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10bcTG.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10bcTG.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10bcTG.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle10bcTG.setWrapText(true);
		cellStyle10bcTG.setFillForegroundColor(HSSFColor.LAVENDER.index);
		cellStyle10bcTG.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// riga chiusura
		CellStyle cellStylechiusura = sheet.getWorkbook().createCellStyle();
		cellStylechiusura.setBorderBottom(CellStyle.BORDER_MEDIUM);
		// riga chiusura destra
		CellStyle cellStylechiusurad = sheet.getWorkbook().createCellStyle();
		cellStylechiusurad.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStylechiusurad.setBorderRight(CellStyle.BORDER_MEDIUM);
		// riga chiusura destra
		CellStyle cellStylechiusurar = sheet.getWorkbook().createCellStyle();
		cellStylechiusurar.setBorderRight(CellStyle.BORDER_MEDIUM);

		// crea stili isentry
		CellStyle cellStyle10entry = sheet.getWorkbook().createCellStyle();
		cellStyle10entry.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10entry.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle10entry.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10entry.setFillForegroundColor(HSSFColor.AQUA.index);
		cellStyle10entry.setFillPattern(CellStyle.SOLID_FOREGROUND);

		List<Integer> righespecifiche = new ArrayList<Integer>();
		List<Integer> righevuote = new ArrayList<Integer>();
		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_A);
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
		maxcolumn = columnCount;
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
		cell.getCellStyle().setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cell.getCellStyle().setFillPattern(CellStyle.SOLID_FOREGROUND);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - "
				+ body.getDenominazioneEnte());
		cell.setCellStyle(cellStyle12b);
		cell.getCellStyle().setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cell.getCellStyle().setFillPattern(CellStyle.SOLID_FOREGROUND);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
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
		cell.getCellStyle().setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cell.getCellStyle().setFillPattern(CellStyle.SOLID_FOREGROUND);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
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
		cell.setCellStyle(cellStyle12b);
		cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cell.getCellStyle().setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("COL1MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolos);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL2MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolot);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL3MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolot);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL4MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolot);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyletitolot);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL6MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolot);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL7MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolor);
		double valsomma = 0.0;
		double valsommacompetenza = 0.0;
		double valsommatitolo2e3 = 0.0;
		maxcolumn = columnCount;
		Integer contarighe = 0;
		for (ModelRendicontazioneModAPart3 voce : body.getRendicontazioneModAPart3()) {
			row = sheet.createRow(++rowCount);
			maxcolumn = columnCount;
			columnCount = 0;
			++contarighe;
			cell = row.createCell(columnCount);
			cell.setCellValue((String) contarighe.toString());
			cell.setCellStyle(cellStylecelletesto);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylecelletesto);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylecelletesto);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylecelletesto);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStylecelletesto);
			cell = row.createCell(++columnCount);
			cell.setCellValue((String) voce.getDescVoce());
			cell.setCellStyle(cellStylecelletesto);
			// importo
			cell = row.createCell(++columnCount);
			if (voce.getValore() != null) {
				cell.setCellValue((double) voce.getValore().doubleValue());
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			}
			cell.setCellStyle(cellStylecelleimporto);
			cell.getCellStyle().setDataFormat(format.getFormat(pattern));
		}
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 6, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellStyle(cellStyle10);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		righevuote.add(rowCount);
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
				if (tipologia.getListaVoci().size() == 0) {
					row = sheet.createRow(++rowCount);
					maxcolumn = columnCount;
					columnCount = 0;
					++contarighe;
					cell = row.createCell(columnCount);
					cell.setCellValue((String) contarighe.toString());
					cell.setCellStyle(cellStylecelletesto);
					cell = row.createCell(++columnCount);
					// titolo
					cell.setCellValue((String) titolo.getDescTitolo());
					cell.setCellStyle(cellStylecelletesto);
					// cod tipologia
					cell = row.createCell(++columnCount);
					cell.setCellValue((String) tipologia.getDescCodTipologia());
					cell.setCellStyle(cellStylecelletesto);
					// desc tipologia
					cell = row.createCell(++columnCount);
					cell.setCellValue((String) tipologia.getDescTipologia());
					cell.setCellStyle(cellStylecelletesto);
					cell = row.createCell(++columnCount);
					cell.setCellStyle(cellStylecelletesto);
					cell = row.createCell(++columnCount);
					cell.setCellStyle(cellStylecelletesto);
					// importo
					cell = row.createCell(++columnCount);
					if (tipologia.getValore() != null) {
						cell.setCellValue((double) Double.valueOf(tipologia.getValore().doubleValue()));
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						valsomma = valsomma + Double.valueOf(tipologia.getValore().doubleValue());
						valsommacompetenza = valsommacompetenza + Double.valueOf(tipologia.getValore().doubleValue());
						if (titolo.getCodTitolo().equalsIgnoreCase("02")
								|| titolo.getCodTitolo().equalsIgnoreCase("03")) {
							valsommatitolo2e3 = valsommatitolo2e3 + Double.valueOf(tipologia.getValore().doubleValue());
						}
					}
					cell.setCellStyle(cellStylecelleimporto);
					cell.getCellStyle().setDataFormat(format.getFormat(pattern));
				} else {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						// scrivo titolo
						if (!voce.getCodVoce().equalsIgnoreCase("32")) {
							row = sheet.createRow(++rowCount);
							maxcolumn = columnCount;
							columnCount = 0;
							++contarighe;
							cell = row.createCell(columnCount);
							cell.setCellValue((String) contarighe.toString());
							cell.setCellStyle(cellStylecelletesto);
							cell = row.createCell(++columnCount);
							// titolo
							cell.setCellValue((String) titolo.getDescTitolo());
							cell.setCellStyle(cellStylecelletesto);
							// cod tipologia
							cell = row.createCell(++columnCount);
							cell.setCellValue((String) tipologia.getDescCodTipologia());
							cell.setCellStyle(cellStylecelletesto);
							// desc tipologia
							cell = row.createCell(++columnCount);
							cell.setCellValue((String) tipologia.getDescTipologia());
							cell.setCellStyle(cellStylecelletesto);
							cell = row.createCell(++columnCount);
							cell.setCellStyle(cellStylecelletesto);
							cell = row.createCell(++columnCount);
							cell.setCellValue((String) voce.getDescVoce());
							cell.setCellStyle(cellStylecelletesto);
							// importo
							cell = row.createCell(++columnCount);
							if (voce.getValoreNumb() != null) {
								cell.setCellValue((Double) voce.getValoreNumb().doubleValue());
								cell.setCellStyle(cellStylecelleimporto);
								cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								valsomma = valsomma + Double.valueOf(voce.getValoreNumb().toString());
								valsommacompetenza = valsommacompetenza
										+ Double.valueOf(voce.getValoreNumb().toString());
								if (titolo.getCodTitolo().equalsIgnoreCase("02")
										|| titolo.getCodTitolo().equalsIgnoreCase("03")) {
									valsommatitolo2e3 = valsommatitolo2e3
											+ Double.valueOf(voce.getValoreNumb().toString());
								}

							} else {
								cell.setCellStyle(cellStylecelleimporto);
							}
						} else {
							// codice 32 campo testo
							row = sheet.createRow(++rowCount);
							maxcolumn = columnCount;
							columnCount = 0;
							cell = row.createCell(columnCount);
							row.createCell(++columnCount);
							row.createCell(++columnCount);
							row.createCell(++columnCount);
							row.createCell(++columnCount);
							cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
							sheet.addMergedRegion(cellRangeAddress);
							cell.setCellValue((String) voce.getDescVoce());
							cell.setCellStyle(cellStyle10);
							RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
									sheet.getWorkbook());
							RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
									sheet.getWorkbook());
							RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
									sheet.getWorkbook());
							RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet,
									sheet.getWorkbook());
							cell = row.createCell(++columnCount);
							cell.setCellValue((String) voce.getValoreText());
							cell.setCellStyle(cellStylecellevalore);
							cell = row.createCell(++columnCount);
							cell.setCellStyle(cellStylecelleimporto);
						}
					}
				}
			}
			// metto il totale del titolo
			row = sheet.createRow(++rowCount);
			maxcolumn = columnCount;
			columnCount = 0;
			cell = row.createCell(columnCount);
			cell.setCellStyle(cellStyle12bTotale);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyle12bTotale);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyle12bTotale);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyle12bTotale);
			cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyle12bTotale);
			cell = row.createCell(++columnCount);
			cell.setCellValue(titolo.getCodTitolo().equals("02-ALTRO")
					? (String) listeService.getParametro("TOTALESINA").getValtext() + " " + titolo.getDescTitolo()
							+ " - Tipologia ALTRO"
					: (String) listeService.getParametro("TOTALESINA").getValtext() + " " + titolo.getDescTitolo());
			cell.setCellStyle(cellStyle12bTotale);
			cell = row.createCell(++columnCount);
			cell.setCellValue((double) valsomma);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyle12bTotaleI);
			cell.getCellStyle().setDataFormat(format.getFormat(pattern));
			valsomma = 0.0;
		}
		// totale competenza
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) listeService.getParametro("TOTALECOM").getValtext());
		cell.setCellStyle(cellStyle12bTotale);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) valsommacompetenza);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyle12bTotaleI);
		cell.getCellStyle().setDataFormat(format.getFormat(pattern));
		// totale titolo2 e 3
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 5, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) listeService.getParametro("TOT23").getValtext());
		cell.setCellStyle(cellStyle12bTotale);
		RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cell.setCellValue((double) valsommatitolo2e3);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(cellStyle12bTotaleI);
		cell.getCellStyle().setDataFormat(format.getFormat(pattern));
		row = sheet.createRow(++rowCount);
		righevuote.add(rowCount);
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("COL8MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolopart2);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL9MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolopart2);
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
		cell.setCellValue((String) listeService.getParametro("COL10MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolopart2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		righespecifiche.add(rowCount);
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusurar);
		righevuote.add(rowCount);
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("STRUTTURESR").getValtext());
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		// prima sezione
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		int rowrs = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) listeService.getParametro("DESCSTRUTTURESR").getValtext());
		cell.setCellStyle(cellStyleSRBW);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// prendo i dati
		rowrs = rowCount;
		int i = 0;
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("02")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le SR
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniRS()) {
								if (i == 0) {
									for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
										cell = row.createCell(++columnCount);
										cell.setCellValue((String) utenza.getDescTargetUtenza());
										cell.setCellStyle(cellStyleU);
										++i;
									}
								}
								row = sheet.createRow(++rowCount);
								maxcolumn = columnCount;
								columnCount = 0;
								cell = row.createCell(columnCount);
								cell.setCellValue((String) prestazione.getCodPrestazione());
								cell.setCellStyle(cellStyleSRBB);
								cell = row.createCell(++columnCount);
								cell.setCellValue((String) prestazione.getDescPrestazione());
								cell.setCellStyle(cellStyleSRB);
							}
							break;
						}
					}
				}
			}
		}
		boolean isEntry = false;
		// prendo i valori
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("02")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le SR
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniRS()) {
								row = sheet.getRow(++rowrs);
								for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
									cell = row.createCell(++columnCount);
									if (utenza.getValore() != null) {
										cell.setCellValue((double) utenza.getValore().doubleValue());
										cell.setCellType(Cell.CELL_TYPE_NUMERIC);
										cell.setCellStyle(cellStyle10cw);
										cell.getCellStyle().setDataFormat(format.getFormat(pattern));
										isEntry = true;
									} else if (utenza.isEntry()) {
										cell.setCellStyle(cellStyle10cw);
										isEntry = true;
									} else if (!utenza.isEntry())
										cell.setCellStyle(cellStyle10cv);
								}
								// verifica se hai trovato almeno una isentry altrimenti riga grigia
								if (!isEntry) {
									row = sheet.getRow(rowrs);
									Iterator<Cell> cellIterator = row.cellIterator();
									while (cellIterator.hasNext()) {
										Cell cella = cellIterator.next();
										if (cella.getColumnIndex() >= 2) {
											cella.setCellStyle(cellStyle10entry);
										}
									}
								}
								isEntry = false;
								maxcolumn = columnCount;
								columnCount = 1;
							}
							break;
						}
					}
				}
			}
		}
		// metto i subtotali
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStyle10bTND);
		cell = row.createCell(++columnCount);
		// totale testo
		cell.setCellValue((String) listeService.getParametro("TOTALESINA").getValtext());
		cell.setCellStyle(cellStyle10bT);
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("02")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo i subtotali SR
							for (ModelTargetUtenza prestazione : voce.getPrestazioni().getSubtotaliRS()) {
								cell = row.createCell(++columnCount);
								if (prestazione.getValore() != null) {
									cell.setCellValue((double) prestazione.getValore().doubleValue());
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellStyle(cellStyle10bcT);
									cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								}
							}
							break;
						}
					}
				}
			}
		}

		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("STRUTTURECD").getValtext());
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bY);
		// prima sezione
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) listeService.getParametro("DESCSTRUTTURECD").getValtext());
		cell.setCellStyle(cellStyleSRBW);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// prendo i dati
		rowrs = rowCount;
		i = 0;
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("02")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le CD
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniCD()) {
								if (i == 0) {
									for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
										cell = row.createCell(++columnCount);
										cell.setCellValue((String) utenza.getDescTargetUtenza());
										cell.setCellStyle(cellStyleU);
										++i;
									}
								}
								row = sheet.createRow(++rowCount);
								maxcolumn = columnCount;
								columnCount = 0;
								cell = row.createCell(columnCount);
								cell.setCellValue((String) prestazione.getCodPrestazione());
								cell.setCellStyle(cellStyleSRBB);
								cell = row.createCell(++columnCount);
								cell.setCellValue((String) prestazione.getDescPrestazione());
								cell.setCellStyle(cellStyleSRB);
							}
							break;
						}
					}
				}
			}
		}
		isEntry = false;
		// prendo i valori
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("02")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le CD
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniCD()) {
								row = sheet.getRow(++rowrs);
								for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
									cell = row.createCell(++columnCount);
									if (utenza.getValore() != null) {
										cell.setCellValue((double) utenza.getValore().doubleValue());
										cell.setCellType(Cell.CELL_TYPE_NUMERIC);
										cell.setCellStyle(cellStyle10cw);
										cell.getCellStyle().setDataFormat(format.getFormat(pattern));
										isEntry = true;
									} else if (utenza.isEntry()) {
										cell.setCellStyle(cellStyle10cw);
										isEntry = true;
									} else if (!utenza.isEntry())
										cell.setCellStyle(cellStyle10cv);
								}
								// verifica se hai trovato almeno una isentry altrimenti riga grigia
								if (!isEntry) {
									row = sheet.getRow(rowrs);
									Iterator<Cell> cellIterator = row.cellIterator();
									while (cellIterator.hasNext()) {
										Cell cella = cellIterator.next();
										if (cella.getColumnIndex() >= 2) {
											cella.setCellStyle(cellStyle10entry);
										}
									}
								}
								isEntry = false;
								maxcolumn = columnCount;
								columnCount = 1;
							}
							break;
						}
					}
				}
			}
		}
		// metto i subtotali e totali
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStyle10bTND);
		cell = row.createCell(++columnCount);
		// totale testo
		cell.setCellValue((String) listeService.getParametro("TOTALESINA").getValtext());
		cell.setCellStyle(cellStyle10bT);
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("02")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo i subtotali SR
							for (ModelTargetUtenza prestazione : voce.getPrestazioni().getSubtotaliCD()) {
								cell = row.createCell(++columnCount);
								if (prestazione.getValore() != null) {
									cell.setCellValue((double) prestazione.getValore().doubleValue());
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellStyle(cellStyle10bcT);
									cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								}
							}
							row = sheet.createRow(++rowCount);
							maxcolumn = columnCount;
							columnCount = 0;
							cell = row.createCell(columnCount);
							cell.setCellStyle(cellStyle10bTND);
							cell = row.createCell(++columnCount);
							// totale srcd
							cell.setCellValue((String) listeService.getParametro("TOTALESRCD").getValtext());
							cell.setCellStyle(cellStyle10bT);
							// prendo i totali
							for (ModelTargetUtenza prestazione : voce.getPrestazioni().getTotaliSRCD()) {
								cell = row.createCell(++columnCount);
								if (prestazione.getValore() != null) {
									cell.setCellValue((double) prestazione.getValore().doubleValue());
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellStyle(cellStyle10bcTG);
									cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								}
							}
							break;
						}
					}
				}
			}
		}
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusurar);
		righevuote.add(rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusurar);
		righevuote.add(rowCount);
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("COL8MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolopart2);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) listeService.getParametro("COL11MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolopart2);
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
		cell.setCellValue((String) listeService.getParametro("COL12MODELLOA").getValtext());
		cell.setCellStyle(cellStyletitolopart2);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		righespecifiche.add(rowCount);
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusurar);
		righevuote.add(rowCount);
		;
		// seconda sezione
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("STRUTTURESR").getValtext());
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) listeService.getParametro("DESCSTRUTTURESR").getValtext());
		cell.setCellStyle(cellStyleSRBW);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// prendo i dati
		rowrs = rowCount;
		i = 0;
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("03")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le SR
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniRS()) {
								if (i == 0) {
									for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
										cell = row.createCell(++columnCount);
										cell.setCellValue((String) utenza.getDescTargetUtenza());
										cell.setCellStyle(cellStyleU);
										++i;
									}
								}
								row = sheet.createRow(++rowCount);
								maxcolumn = columnCount;
								columnCount = 0;
								cell = row.createCell(columnCount);
								cell.setCellValue((String) prestazione.getCodPrestazione());
								cell.setCellStyle(cellStyleSRBB);
								cell = row.createCell(++columnCount);
								cell.setCellValue((String) prestazione.getDescPrestazione());
								cell.setCellStyle(cellStyleSRB);
							}
							break;
						}
					}
				}
			}
		}
		isEntry = false;
		// prendo i valori
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("03")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le SR
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniRS()) {
								row = sheet.getRow(++rowrs);
								for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
									cell = row.createCell(++columnCount);
									if (utenza.getValore() != null) {
										cell.setCellValue((double) utenza.getValore().doubleValue());
										cell.setCellType(Cell.CELL_TYPE_NUMERIC);
										cell.setCellStyle(cellStyle10cw);
										cell.getCellStyle().setDataFormat(format.getFormat(pattern));
										isEntry = true;
									} else if (utenza.isEntry()) {
										cell.setCellStyle(cellStyle10cw);
										isEntry = true;
									} else if (!utenza.isEntry())
										cell.setCellStyle(cellStyle10cv);
								}
								// verifica se hai trovato almeno una isentry altrimenti riga grigia
								if (!isEntry) {
									row = sheet.getRow(rowrs);
									Iterator<Cell> cellIterator = row.cellIterator();
									while (cellIterator.hasNext()) {
										Cell cella = cellIterator.next();
										if (cella.getColumnIndex() >= 2) {
											cella.setCellStyle(cellStyle10entry);
										}
									}
								}
								isEntry = false;
								maxcolumn = columnCount;
								columnCount = 1;
							}
							break;
						}
					}
				}
			}
		}
		// metto i subtotali
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStyle10bTND);
		cell = row.createCell(++columnCount);
		// totale testo
		cell.setCellValue((String) listeService.getParametro("TOTALESINA").getValtext());
		cell.setCellStyle(cellStyle10bT);
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("03")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo i subtotali SR
							for (ModelTargetUtenza prestazione : voce.getPrestazioni().getSubtotaliRS()) {
								cell = row.createCell(++columnCount);
								if (prestazione.getValore() != null) {
									cell.setCellValue((double) prestazione.getValore().doubleValue());
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellStyle(cellStyle10bcT);
									cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								}
							}
							break;
						}
					}
				}
			}
		}
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellValue((String) listeService.getParametro("STRUTTURECD").getValtext());
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle10bL);

		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) listeService.getParametro("DESCSTRUTTURECD").getValtext());
		cell.setCellStyle(cellStyleSRBW);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// prendo i dati
		rowrs = rowCount;
		i = 0;
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("03")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le CD
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniCD()) {
								if (i == 0) {
									for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
										cell = row.createCell(++columnCount);
										cell.setCellValue((String) utenza.getDescTargetUtenza());
										cell.setCellStyle(cellStyleU);
										++i;
									}
								}
								row = sheet.createRow(++rowCount);
								maxcolumn = columnCount;
								columnCount = 0;
								cell = row.createCell(columnCount);
								cell.setCellValue((String) prestazione.getCodPrestazione());
								cell.setCellStyle(cellStyleSRBB);
								cell = row.createCell(++columnCount);
								cell.setCellValue((String) prestazione.getDescPrestazione());
								cell.setCellStyle(cellStyleSRB);
							}
							break;
						}
					}
				}
			}
		}
		isEntry = false;
		// prendo i valori
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("03")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo le CD
							for (ModelPrestazioneUtenzaModA prestazione : voce.getPrestazioni().getPrestazioniCD()) {
								row = sheet.getRow(++rowrs);
								for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
									cell = row.createCell(++columnCount);
									if (utenza.getValore() != null) {
										cell.setCellValue((double) utenza.getValore().doubleValue());
										cell.setCellType(Cell.CELL_TYPE_NUMERIC);
										cell.setCellStyle(cellStyle10cw);
										cell.getCellStyle().setDataFormat(format.getFormat(pattern));
										isEntry = true;
									} else if (utenza.isEntry()) {
										cell.setCellStyle(cellStyle10cw);
										isEntry = true;
									} else if (!utenza.isEntry())
										cell.setCellStyle(cellStyle10cv);
								}
								// verifica se hai trovato almeno una isentry altrimenti riga grigia
								if (!isEntry) {
									row = sheet.getRow(rowrs);
									Iterator<Cell> cellIterator = row.cellIterator();
									while (cellIterator.hasNext()) {
										Cell cella = cellIterator.next();
										if (cella.getColumnIndex() >= 2) {
											cella.setCellStyle(cellStyle10entry);
										}
									}
								}
								isEntry = false;
								maxcolumn = columnCount;
								columnCount = 1;
							}
							break;
						}
					}
				}
			}
		}
		// metto i subtotali e totali
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStyle10bTND);
		cell = row.createCell(++columnCount);
		// totale testo
		cell.setCellValue((String) listeService.getParametro("TOTALESINA").getValtext());
		cell.setCellStyle(cellStyle10bT);
		for (ModelTitoloModA titolo : body.getRendicontazioneModAPart1().getListaTitoli()) {
			if (titolo.getCodTitolo().equalsIgnoreCase("03")) {
				for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
					for (ModelVoceModA voce : tipologia.getListaVoci()) {
						if (voce.getPrestazioni() != null) {
							// prendo i subtotali SR
							for (ModelTargetUtenza prestazione : voce.getPrestazioni().getSubtotaliCD()) {
								cell = row.createCell(++columnCount);
								if (prestazione.getValore() != null) {
									cell.setCellValue((double) prestazione.getValore().doubleValue());
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellStyle(cellStyle10bcT);
									cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								}
							}
							row = sheet.createRow(++rowCount);
							maxcolumn = columnCount;
							columnCount = 0;
							cell = row.createCell(columnCount);
							cell.setCellStyle(cellStyle10bTND);
							cell = row.createCell(++columnCount);
							// totale srcd
							cell.setCellValue((String) listeService.getParametro("TOTALESRCD").getValtext());
							cell.setCellStyle(cellStyle10bT);
							// prendo i totali
							for (ModelTargetUtenza prestazione : voce.getPrestazioni().getTotaliSRCD()) {
								cell = row.createCell(++columnCount);
								if (prestazione.getValore() != null) {
									cell.setCellValue((double) prestazione.getValore().doubleValue());
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellStyle(cellStyle10bcTG);
									cell.getCellStyle().setDataFormat(format.getFormat(pattern));
								}
							}
							break;
						}
					}
				}
			}
		}
		row = sheet.createRow(++rowCount);
		maxcolumn = columnCount;
		columnCount = 0;
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusura);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStylechiusurad);
		righevuote.add(rowCount);
		for (int c = 0; c <= maxcolumn; c++) {
			setColumnToSize(sheet.getWorkbook(), c);
		}
		setRowToSize(sheet.getWorkbook(), righevuote, righespecifiche);
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloA_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));

	}

	public static void setColumnToSize(Workbook workbook, int column) {
		int numberOfSheets = workbook.getNumberOfSheets();
		int lunghezzarighe = 1;
		float dimens = 35;
		boolean totale = false;
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				sheet.setColumnWidth(column, 30 * 256);
				for (int j = 5; j < sheet.getPhysicalNumberOfRows(); j++) {
					Row row = sheet.getRow(j);
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							if (cell.getStringCellValue().startsWith("TOTALE")) {
								totale = true;
							} else {
								totale = false;
								lunghezzarighe = cell.getStringCellValue().length();
								lunghezzarighe = lunghezzarighe / 30 == 0 ? 1 : lunghezzarighe / 30;
								dimens = (float) (lunghezzarighe * 22);

							}
						}
						if (lunghezzarighe == 1 || totale)
							row.setHeightInPoints((float) 35);
						else {
							row.setHeightInPoints((float) dimens);

						}
					}
				}
			}
		}
	}

	public static void setRowToSize(Workbook workbook, List<Integer> righevuote, List<Integer> righespecifiche) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				for (int j = 0; j < 5; j++) {
					Row row = sheet.getRow(j);
					row.setHeightInPoints((float) 40);
				}
				for (int j = 5; j < sheet.getPhysicalNumberOfRows(); j++) {
					Row row = sheet.getRow(j);
					if (righevuote.contains(j))
						row.setHeightInPoints((float) 20);
					if (righespecifiche.contains(j))
						row.setHeightInPoints((float) 65);
				}
			}
		}
	}

	@Transactional
	public GenericResponseWarnErr checkModelloA(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());
		response.setObblMotivazione(false);
		boolean presenzaAttuale = false;
		boolean presenzaPassata = false;
		GregTRendicontazioneEnte rendicontazioneAttuale = datiRendicontazioneService
				.getRendicontazione(idRendicontazione);
		ModelDatiEnte datiAttuali = datiEnteService.findSchedaEntexAnno(
				rendicontazioneAttuale.getIdRendicontazioneEnte(), rendicontazioneAttuale.getAnnoGestione());
		GregTRendicontazioneEnte rendicontazionePassata = datiRendicontazioneService.getRendicontazionePassata(
				rendicontazioneAttuale.getGregTSchedeEntiGestori().getIdSchedaEnteGestore(),
				(rendicontazioneAttuale.getAnnoGestione() - 1));
		if (rendicontazionePassata != null) {
			ModelDatiEnte datiPassata = datiEnteService.findSchedaEntexAnno(
					rendicontazionePassata.getIdRendicontazioneEnte(), rendicontazionePassata.getAnnoGestione());
			VociModelloA vociModelloAAttuali = getListaVociModA(rendicontazioneAttuale.getIdRendicontazioneEnte());
			VociModelloA vociModelloAPassata = getListaVociModA(rendicontazionePassata.getIdRendicontazioneEnte());

			BigDecimal totaleTitoliAttuale = BigDecimal.ZERO;
			BigDecimal totaleTitoli2e3Attuale = BigDecimal.ZERO;
			BigDecimal titolo2AltroAttuale = BigDecimal.ZERO;
			BigDecimal entrateFigurativeAttuale = BigDecimal.ZERO;

			BigDecimal totaleTitoliPassata = BigDecimal.ZERO;
			BigDecimal totaleTitoli2e3Passata = BigDecimal.ZERO;
			BigDecimal titolo2AltroPassata = BigDecimal.ZERO;
			BigDecimal entrateFigurativePassata = BigDecimal.ZERO;

			for (ModelTitoloModA titolo : vociModelloAAttuali.getListaTitoli()) {
				for (ModelTipologiaModA tipologie : titolo.getListaTipologie()) {
					if (tipologie.getValore() != null) {
						totaleTitoliAttuale = totaleTitoliAttuale.add(tipologie.getValore());
						if (titolo.getCodTitolo().equals("02") || titolo.getCodTitolo().equals("03")) {
							totaleTitoli2e3Attuale = totaleTitoli2e3Attuale.add(tipologie.getValore());
						}
					}
					for (ModelVoceModA voce : tipologie.getListaVoci()) {
						if (voce.getValoreNumb() != null) {
							totaleTitoliAttuale = totaleTitoliAttuale.add(voce.getValoreNumb());
							if (datiAttuali.getTipoEnte().getCodTipoEnte().equals("COMUNE CAPOLUOGO")
									|| datiAttuali.getTipoEnte().getCodTipoEnte().equals("CONVENZIONE DI COMUNI")) {
								if (titolo.getCodTitolo().equals("02") && voce.getCodVoce().equals("08")) {
									entrateFigurativeAttuale = entrateFigurativeAttuale.add(voce.getValoreNumb());
								}
							}
							if (titolo.getCodTitolo().equals("02-ALTRO")) {
								titolo2AltroAttuale = voce.getValoreNumb();
								presenzaAttuale = true;
							}
							if (titolo.getCodTitolo().equals("02") || titolo.getCodTitolo().equals("03")) {
								totaleTitoli2e3Attuale = totaleTitoli2e3Attuale.add(voce.getValoreNumb());
							}
						}
					}
				}
			}

			for (ModelTitoloModA titolo : vociModelloAPassata.getListaTitoli()) {
				for (ModelTipologiaModA tipologie : titolo.getListaTipologie()) {
					if (tipologie.getValore() != null) {
						totaleTitoliPassata = totaleTitoliPassata.add(tipologie.getValore());
						if (titolo.getCodTitolo().equals("02") || titolo.getCodTitolo().equals("03")) {
							totaleTitoli2e3Passata = totaleTitoli2e3Passata.add(tipologie.getValore());
						}
					}
					for (ModelVoceModA voce : tipologie.getListaVoci()) {
						if (voce.getValoreNumb() != null) {
							totaleTitoliPassata = totaleTitoliPassata.add(voce.getValoreNumb());
							if (datiPassata.getTipoEnte().getCodTipoEnte().equals("COMUNE CAPOLUOGO")
									|| datiPassata.getTipoEnte().getCodTipoEnte().equals("CONVENZIONE DI COMUNI")) {
								if (titolo.getCodTitolo().equals("02") && voce.getCodVoce().equals("08")) {
									entrateFigurativePassata = entrateFigurativePassata.add(voce.getValoreNumb());
								}
							}
							if (titolo.getCodTitolo().equals("02-ALTRO")) {
								titolo2AltroPassata = voce.getValoreNumb();
								presenzaPassata = true;
							}
							if (titolo.getCodTitolo().equals("02") || titolo.getCodTitolo().equals("03")) {
								totaleTitoli2e3Passata = totaleTitoli2e3Passata.add(voce.getValoreNumb());
							}
						}
					}
				}
			}

			BigDecimal totaleTitoli25 = (totaleTitoliPassata.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal totaleTitoli2e325 = (totaleTitoli2e3Passata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal titoli2Altro25 = (titolo2AltroPassata.multiply(new BigDecimal(25))).divide(new BigDecimal(100),
					RoundingMode.HALF_DOWN);
			BigDecimal entrateFigurative25 = (entrateFigurativePassata.multiply(new BigDecimal(10)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);

			if ((totaleTitoliAttuale.setScale(2))
					.compareTo(totaleTitoliPassata.subtract(totaleTitoli25).setScale(2)) < 0
					|| (totaleTitoliAttuale.setScale(2))
							.compareTo(totaleTitoliPassata.add(totaleTitoli25).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLOA01).getTestoMessaggio()
								.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleTitoliAttuale))
								.replace("DATOPASSATO", Util.convertBigDecimalToString(totaleTitoliPassata)));
				response.setObblMotivazione(true);
			}
			if ((totaleTitoli2e3Attuale.setScale(2))
					.compareTo(totaleTitoli2e3Passata.subtract(totaleTitoli2e325).setScale(2)) < 0
					|| (totaleTitoli2e3Attuale.setScale(2))
							.compareTo(totaleTitoli2e3Passata.add(totaleTitoli2e325).setScale(2)) > 0) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLOA02).getTestoMessaggio()
								.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleTitoli2e3Attuale))
								.replace("DATOPASSATO", Util.convertBigDecimalToString(totaleTitoli2e3Passata)));
				response.setObblMotivazione(true);
			}

			if (datiAttuali.getTipoEnte().getCodTipoEnte().equals(datiPassata.getTipoEnte().getCodTipoEnte())) {
				if (datiAttuali.getTipoEnte().getCodTipoEnte().equals("COMUNE CAPOLUOGO")
						|| datiAttuali.getTipoEnte().getCodTipoEnte().equals("CONVENZIONE DI COMUNI")) {
					if ((entrateFigurativeAttuale.setScale(2))
							.compareTo(entrateFigurativePassata.subtract(entrateFigurative25).setScale(2)) < 0
							|| (entrateFigurativeAttuale.setScale(2))
									.compareTo(entrateFigurativePassata.add(entrateFigurative25).setScale(2)) > 0) {
						response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MODELLOA05)
								.getTestoMessaggio()
								.replace("DATOATTUALE", Util.convertBigDecimalToString(entrateFigurativeAttuale))
								.replace("DATOPASSATO", Util.convertBigDecimalToString(entrateFigurativePassata)));
						response.setObblMotivazione(true);
					}
				}
			}

			if ((presenzaAttuale && !presenzaPassata) || (!presenzaAttuale && presenzaPassata)) {
				response.getWarnings()
						.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLOA04).getTestoMessaggio());
				response.setObblMotivazione(true);
			} else if (presenzaAttuale && presenzaPassata) {
				if ((titolo2AltroAttuale.setScale(2))
						.compareTo(titolo2AltroPassata.subtract(titoli2Altro25).setScale(2)) < 0
						|| (titolo2AltroAttuale.setScale(2))
								.compareTo(titolo2AltroPassata.add(titoli2Altro25).setScale(2)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MODELLOA03).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(titolo2AltroAttuale))
									.replace("DATOPASSATO", Util.convertBigDecimalToString(titolo2AltroPassata)));
					response.setObblMotivazione(true);
				}
			}
			if (response.getWarnings().size() > 0) {
				List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_A,
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
		ModelStatoMod stato = modelloADao.getStatoModelloA(idRendicontazione);
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
	public GenericResponseWarnErr controlloModelloA(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);

		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_A);

		ModelDatiEnteRendicontazione contatti = datiEnteService
				.findDatiEntexAnno(rendicontazione.getIdRendicontazioneEnte(), rendicontazione.getAnnoGestione());
		List<ModelValoriModA> cella06 = new ArrayList<ModelValoriModA>();
		List<ModelValoriModA> cella07 = new ArrayList<ModelValoriModA>();
		List<ModelValoriModA> cella08 = new ArrayList<ModelValoriModA>();
		boolean facoltativo = false;
		boolean valorizzato = modelloADao.getValorizzatoModelloA(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO)
					.getTestoMessaggio().replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		if (!facoltativo) {
			// chiama funzione modello a
			cella06 = getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(), SharedConstants.MODELLO_A_06);
			// verifica se il campo avvalorato
			if (cella06.size() != 0) {
				cella06.get(0).getValore();
			}
			cella07 = getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(), SharedConstants.MODELLO_A_07);
			if (cella07.size() != 0) {
				cella07.get(0).getValore();
			}
			cella08 = getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(), SharedConstants.MODELLO_A_08);
			if (!(contatti.getCodTipoEnte().equals(SharedConstants.OPERAZIONE_COMUNE_CAPOLUOGO)
					|| contatti.getCodTipoEnte().equals(SharedConstants.OPERAZIONE_CONVENZIONE_COMUNI))) {
				if (cella08.size() != 0) {
					response.getErrors().add(
							listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_NO08).getTestoMessaggio());
				}
			}
		}

		return response;

	}
}
