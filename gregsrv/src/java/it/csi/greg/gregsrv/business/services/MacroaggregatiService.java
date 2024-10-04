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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.MacroaggregatiDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.entity.GregDSpesaMissioneProgramma;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRProgrammaMissioneTitSottotit;
import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregRSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaMacroaggregatiInput;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelCampiMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliSpeseMissioni;
import it.csi.greg.gregsrv.dto.ModelRisultatiMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelSpesaMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelSpesaMissione;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTotaleMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelTotaleSpese;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("macroaggregatiService")
public class MacroaggregatiService {

	@Autowired
	protected MacroaggregatiDao macroaggregatiDao;
	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected ModelloBDao modelloBDao;

	public List<ModelMacroaggregati> getMacroaggregati() {
		List<GregTMacroaggregatiBilancio> macroaggregati = macroaggregatiDao.findAllMacroaggregati();

		List<ModelMacroaggregati> listaMacroaggregati = new ArrayList<ModelMacroaggregati>();
		for (GregTMacroaggregatiBilancio macroaggregato : macroaggregati) {
			listaMacroaggregati.add(new ModelMacroaggregati(macroaggregato));
		}
		return listaMacroaggregati;
	}

	public List<ModelSpesaMissione> getSpesaMissione() {
		List<GregDSpesaMissioneProgramma> speseMissioni = macroaggregatiDao.findAllSpesaMissione();

		List<ModelSpesaMissione> listaSpese = new ArrayList<ModelSpesaMissione>();
		for (GregDSpesaMissioneProgramma spesaMissione : speseMissioni) {
			listaSpese.add(new ModelSpesaMissione(spesaMissione));
		}
		return listaSpese;
	}

	public List<ModelSpesaMacroaggregati> getRSpesaMacro() {
		List<GregRSpesaMissioneProgrammaMacro> speseMacro = macroaggregatiDao.findAllRSpesaMacro();

		List<ModelSpesaMacroaggregati> listaSpeseMacro = new ArrayList<ModelSpesaMacroaggregati>();
		for (GregRSpesaMissioneProgrammaMacro spesaMacro : speseMacro) {
			GregTMacroaggregatiBilancio macroaggregato = macroaggregatiDao
					.findMacroaggregatoById(spesaMacro.getGregTMacroaggregatiBilancio().getIdMacroaggregatoBilancio());
			listaSpeseMacro.add(new ModelSpesaMacroaggregati(spesaMacro, macroaggregato));
		}
		return listaSpeseMacro;
	}

	public Boolean isRendicontazioneMacroaggregatiCompiledByIdScheda(Integer idScheda) {
		Boolean ret_value = false;
		List<Object> rendicontazioneEnte = macroaggregatiDao.findRendicontazioneEnteByIdScheda(idScheda);
		Iterator<Object> itr = rendicontazioneEnte.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();

			if (obj[7] != null && ((BigDecimal) obj[7]).compareTo(BigDecimal.ZERO) == 1) {
				ret_value = true;
				break;
			}
		}
		return ret_value;
	}

	public ModelRendicontazioneMacroaggregati getRendicontazioneMacroaggregatiByIdScheda(Integer idScheda) {
		List<Object> rendicontazioneEnte = macroaggregatiDao.findRendicontazioneEnteByIdScheda(idScheda);
		List<ModelRisultatiMacroaggregati> listaRisultati = new ArrayList<ModelRisultatiMacroaggregati>();
//		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService.getSchedaEnte(idScheda);

		Iterator<Object> itr = rendicontazioneEnte.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			ModelRisultatiMacroaggregati model = new ModelRisultatiMacroaggregati();
			model.setIdSpesaMacro((Integer) obj[0]);
			model.setCodSpesaMissione(String.valueOf(obj[1]));
			model.setCodMacroaggregato(String.valueOf(obj[2]));
			model.setDescrizioneMissioneCartacea(String.valueOf(obj[3]));
			model.setDescrizioneProgrammaCartaceo(String.valueOf(obj[4]));
			model.setMsgInformativo(String.valueOf(obj[5]));
			model.setDescrizioneMacroaggregato(String.valueOf(obj[6]));
			model.setValore((BigDecimal) obj[7]);
			model.setIdRendicontazioneEnte((Integer) obj[8]);
			model.setIdSchedaEnteGestore((Integer) obj[9]);
			model.setAnnoGestione((Integer) obj[10]);
			listaRisultati.add(model);
		}

		List<GregDSpesaMissioneProgramma> spesaMissione = macroaggregatiDao.findAllSpesaMissione();
		List<GregTMacroaggregatiBilancio> macroaggregati = macroaggregatiDao.findAllMacroaggregati();

		ModelRendicontazioneMacroaggregati listaElementi = new ModelRendicontazioneMacroaggregati();
//		listaElementi.setDenominazioneEnte(schedaEnte.getDenominazione());

		if (!listaRisultati.isEmpty()) {
			List<ModelValoriMacroaggregati> elemRiga = new ArrayList<ModelValoriMacroaggregati>();

			for (GregDSpesaMissioneProgramma riga : spesaMissione) {
				List<ModelCampiMacroaggregati> elemColonna = new ArrayList<ModelCampiMacroaggregati>();

				ModelValoriMacroaggregati valoriRendCorrente = new ModelValoriMacroaggregati();
				valoriRendCorrente.setIdSpesaMissione(riga.getIdSpesaMissioneProgramma());
				valoriRendCorrente.setCodMissione(riga.getCodSpesaMissioneProgramma());
				valoriRendCorrente.setOrdinamento(riga.getOrdinamento());
				valoriRendCorrente.setDescrizioneMissione(riga.getDescMissioneCartacea());
				valoriRendCorrente.setDescrizioneProgramma(riga.getDescProgrammaCartaceo());

				for (GregTMacroaggregatiBilancio colonna : macroaggregati) {

					ModelRisultatiMacroaggregati rendCorrente = listaRisultati.stream()
							.filter(rend -> rend.getCodSpesaMissione().equals(riga.getCodSpesaMissioneProgramma())
									&& rend.getCodMacroaggregato().equals(colonna.getCodMacroaggregatoBilancio()))
							.findFirst().orElse(null);
					if (rendCorrente != null) {

						if (rendCorrente.getAnnoGestione() != null) {
							listaElementi.setAnnoGestione(rendCorrente.getAnnoGestione());
						}
						if (rendCorrente.getIdRendicontazioneEnte() != null) {
							listaElementi.setIdRendicontazioneEnte(rendCorrente.getIdRendicontazioneEnte());
						}
						if (rendCorrente.getIdSchedaEnteGestore() != null) {
							listaElementi.setIdSchedaEnteGestore(rendCorrente.getIdSchedaEnteGestore());
						}

						ModelCampiMacroaggregati campoVoce = new ModelCampiMacroaggregati();
						campoVoce.setVoce(colonna.getDesMacroaggregatoBilancio());
						campoVoce.setCod(rendCorrente.getCodMacroaggregato());
						campoVoce.setValue(rendCorrente.getValore());
						campoVoce.setOrdinamento(colonna.getOrdinamento());

						elemColonna.add(campoVoce);
						valoriRendCorrente.setCampi(elemColonna);
					}
				}

				elemRiga.add(valoriRendCorrente);
				listaElementi.setValoriMacroaggregati(elemRiga);
			}
		}

		return listaElementi;

	}

	@Transactional
	public SaveModelloOutput saveMacroaggregati(ModelRendicontazioneMacroaggregati body, UserInfo userInfo,
			String notaEnte, String notaInterna) throws Exception {
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
			if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
//			if((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE)) 
//					|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {

				if (!Checker.isValorizzato(notaEnte)) {
					String msgUpdateDati = listeService
							.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI).getTestoMessaggio()
							.replace("OPERAZIONE", "SALVA");
					newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: newNotaEnte;
				} else {
//					newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + notaEnte : notaEnte;
					newNotaEnte = notaEnte;
				}

			} else {
//				newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + notaEnte : notaEnte;
				newNotaEnte = notaEnte;
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(notaInterna)) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendToUpdate);
				cronologia.setGregDStatoRendicontazione(rendToUpdate.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Mod. Macroaggregati");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(notaInterna);
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}

			List<GregDSpesaMissioneProgramma> spesaMissione = macroaggregatiDao.findAllSpesaMissione();
			List<GregTMacroaggregatiBilancio> macroaggregati = macroaggregatiDao.findAllMacroaggregati();

			// Recupero la lista delle voci da salvare o aggiornare
			List<ModelValoriMacroaggregati> listaToSaveOrUpdate = body.getValoriMacroaggregati();
			for (ModelValoriMacroaggregati valori : listaToSaveOrUpdate) {
				if (valori.getCampi() != null) {
					for (ModelCampiMacroaggregati valore : valori.getCampi()) {

						GregDSpesaMissioneProgramma spesaCorrente = spesaMissione.stream()
								.filter(voceC -> voceC.getCodSpesaMissioneProgramma().equals(valori.getCodMissione()))
								.findFirst().orElse(null);

						GregTMacroaggregatiBilancio macroaggregatoCorrente = macroaggregati.stream()
								.filter(tipo -> tipo.getCodMacroaggregatoBilancio().equals(valore.getCod())).findFirst()
								.orElse(null);

						GregRSpesaMissioneProgrammaMacro spesaMissioneMacro = macroaggregatiDao.findSpesaMacroaggregato(
								spesaCorrente.getIdSpesaMissioneProgramma(),
								macroaggregatoCorrente.getIdMacroaggregatoBilancio());

						GregRRendicontazioneSpesaMissioneProgrammaMacro rendicontazioneToUpdate = macroaggregatiDao
								.findRendicontazioneBySpesaMacroaggregatoEnte(
										spesaCorrente.getIdSpesaMissioneProgramma(),
										macroaggregatoCorrente.getIdMacroaggregatoBilancio(),
										rendToUpdate.getIdRendicontazioneEnte());
						if (rendicontazioneToUpdate != null) {
							if (valore.getValue() == null) {
								// Cancello il record
								macroaggregatiDao.deleteRendicontazioneSpesaMissioneProgrammaMacro(
										rendicontazioneToUpdate.getIdRendicontazioneSpesaMissioneProgrammaMacro());
							} else {
								// Effettuo l'aggiornamento del record
								rendicontazioneToUpdate.setValore(valore.getValue());
								rendicontazioneToUpdate.setDataModifica(dataModifica);
								macroaggregatiDao
										.updateRendicontazioneSpesaMissioneProgrammaMacro(rendicontazioneToUpdate);
							}

						} else {
							if (valore.getValue() != null) {
								// Effettuo l'inserimento a DB
								GregRRendicontazioneSpesaMissioneProgrammaMacro newRendicontazione = new GregRRendicontazioneSpesaMissioneProgrammaMacro();
								newRendicontazione.setGregTRendicontazioneEnte(rendToUpdate);
								newRendicontazione.setGregRSpesaMissioneProgrammaMacro(spesaMissioneMacro);
								newRendicontazione.setValore(valore.getValue());
								newRendicontazione.setDataInizioValidita(dataModifica);
								newRendicontazione.setUtenteOperazione(userInfo.getCodFisc());
								newRendicontazione.setDataCreazione(dataModifica);
								newRendicontazione.setDataModifica(dataModifica);
								macroaggregatiDao.insertRendicontazioneSpesaMissioneProgrammaMacro(newRendicontazione);
							}

						}
					}
				}
			}

			ModelRendicontazioneTotaliSpeseMissioni spesaMissioneB = getRendicontazioneTotaliSpesePerB(
					body.getIdRendicontazioneEnte());
			GregRRendMiProTitEnteGestoreModB rendProMissTitSottotit = modelloBDao
					.findRendModBbyMissProTitNoSottotit("04", "0406", "1", rendToUpdate.getIdRendicontazioneEnte());
			GregRProgrammaMissioneTitSottotit proMissTitSottotit = modelloBDao
					.findTitoliMissioneModBbyMissProTitNoSottotit("04", "0406", "1");

			if (rendProMissTitSottotit != null) {
				rendProMissTitSottotit.setValore(spesaMissioneB.getValoriSpese().get(2).getTotale());
				rendProMissTitSottotit.setDataModifica(dataModifica);
				modelloBDao.updateRendicontazioneProMissTitSottotit(rendProMissTitSottotit);
			} else {

				GregRRendMiProTitEnteGestoreModB newRend = new GregRRendMiProTitEnteGestoreModB();
				newRend.setGregTRendicontazioneEnte(rendToUpdate);
				newRend.setGregRProgrammaMissioneTitSottotit(proMissTitSottotit);
				newRend.setValore(spesaMissioneB.getValoriSpese().get(2).getTotale());
				newRend.setDataInizioValidita(dataModifica);
				newRend.setUtenteOperazione(userInfo.getCodFisc());
				newRend.setDataCreazione(dataModifica);
				newRend.setDataModifica(dataModifica);
				modelloBDao.insertRendicontazioneProMissTitSottotit(newRend);
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
				out.setId(HttpStatus.OK.toString());
				out.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				out.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			} else {
				out.setId(HttpStatus.OK.toString());
				out.setWarnings(null);
				out.setErrors(null);
			}
		}

		return out;
	}

	public ModelRendicontazioneTotaliSpeseMissioni getRendicontazioneTotaliSpesePerB(Integer idScheda) {
		ModelRendicontazioneMacroaggregati rend = getRendicontazioneMacroaggregatiByIdScheda(idScheda);
		ModelRendicontazioneTotaliSpeseMissioni totaliSpese = new ModelRendicontazioneTotaliSpeseMissioni();

		totaliSpese.setIdSchedaEnteGestore(rend.getIdSchedaEnteGestore());
		totaliSpese.setIdRendicontazioneEnte(rend.getIdRendicontazioneEnte());
		totaliSpese.setDenominazioneEnte(rend.getDenominazioneEnte());
		totaliSpese.setAnnoGestione(rend.getAnnoGestione());
		List<ModelTotaleSpese> arraySpese = new ArrayList<ModelTotaleSpese>();
		BigDecimal totale = null;

		for (ModelValoriMacroaggregati spese : rend.getValoriMacroaggregati()) {
			if (!spese.getCodMissione().equals("5")) {
				ModelTotaleSpese speseT = new ModelTotaleSpese();
				speseT.setDescMissione(spese.getDescrizioneMissione());
				speseT.setCodMissione(spese.getCodMissione());
				speseT.setOrdinamento(spese.getOrdinamento());
				if (spese.getCampi() != null) {
					for (ModelCampiMacroaggregati macro : spese.getCampi()) {
//						if (macro.getValue() != null) {
						totale = totale == null
								? BigDecimal.ZERO.add(macro.getValue() != null ? macro.getValue() : BigDecimal.ZERO)
								: totale.add(macro.getValue() != null ? macro.getValue() : BigDecimal.ZERO);

//							totale = totale==null ? BigDecimal.ZERO.add(macro.getValue()) : totale.add(macro.getValue());
//						}
					}
					speseT.setTotale(totale);
					if (!spese.getCodMissione().equals("2")) {
						totale = null;
						arraySpese.add(speseT);
					}
				}
			}
		}
		ModelTotaleSpese speseT = new ModelTotaleSpese();
		totale = null;
		for (ModelValoriMacroaggregati spese : rend.getValoriMacroaggregati()) {
			if (spese.getCampi() != null) {
				for (ModelCampiMacroaggregati macro : spese.getCampi()) {
//					if (macro.getValue() != null) {
//						totale = totale==null ? BigDecimal.ZERO.add(macro.getValue()) : totale.add(macro.getValue());
					totale = totale == null
							? BigDecimal.ZERO.add(macro.getValue() != null ? macro.getValue() : BigDecimal.ZERO)
							: totale.add(macro.getValue() != null ? macro.getValue() : BigDecimal.ZERO);
//					}
				}
			}
		}
		speseT.setDescMissione("TOTALE SPESA CORRENTE DI TUTTE LE MISSIONI");
		speseT.setCodMissione("6");
		speseT.setOrdinamento(6);
		speseT.setTotale(totale);
		arraySpese.add(speseT);
		totaliSpese.setValoriSpese(arraySpese);
		return totaliSpese;
	}

	public ModelRendicontazioneTotaliMacroaggregati getRendicontazioneTotaliMacroaggregatiPerB1(Integer idScheda) {
		ModelRendicontazioneMacroaggregati rend = getRendicontazioneMacroaggregatiByIdScheda(idScheda);
		ModelRendicontazioneTotaliMacroaggregati totaliMacroaggregati = new ModelRendicontazioneTotaliMacroaggregati();

		totaliMacroaggregati.setIdSchedaEnteGestore(rend.getIdSchedaEnteGestore());
		totaliMacroaggregati.setIdRendicontazioneEnte(rend.getIdRendicontazioneEnte());
		totaliMacroaggregati.setDenominazioneEnte(rend.getDenominazioneEnte());
		totaliMacroaggregati.setAnnoGestione(rend.getAnnoGestione());
		List<ModelTotaleMacroaggregati> arrayMacro = new ArrayList<ModelTotaleMacroaggregati>();
		BigDecimal totaleM1 = null;
		BigDecimal totaleM2 = null;
		BigDecimal totaleM3 = null;
		BigDecimal totaleM4 = null;
		BigDecimal totaleM5 = null;
		BigDecimal totaleM6 = null;
		BigDecimal totaleM7 = null;
		BigDecimal totale = null;
		for (ModelValoriMacroaggregati spese : rend.getValoriMacroaggregati()) {
			if (spese.getCampi() != null) {
				for (ModelCampiMacroaggregati macro : spese.getCampi()) {
					if (spese.getCodMissione().equals("2") || spese.getCodMissione().equals("3")
							|| spese.getCodMissione().equals("4")) {

						BigDecimal value = macro.getValue() != null ? macro.getValue() : BigDecimal.ZERO;

						switch (macro.getCod()) {
						case "1VB":
							totaleM1 = totaleM1 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM1.add(value != null ? value : BigDecimal.ZERO);
							break;
						case "2VB":
							totaleM2 = totaleM2 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM2.add(value != null ? value : BigDecimal.ZERO);
							break;
						case "3VB":
							totaleM3 = totaleM3 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM3.add(value != null ? value : BigDecimal.ZERO);
							break;
						case "4VB":
							totaleM4 = totaleM4 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM4.add(value != null ? value : BigDecimal.ZERO);
							break;
						case "5VB":
							totaleM5 = totaleM5 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM5.add(value != null ? value : BigDecimal.ZERO);
							break;
						case "6VB":
							totaleM6 = totaleM6 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM6.add(value != null ? value : BigDecimal.ZERO);
							break;
						case "7VB":
							totaleM7 = totaleM7 == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
									: totaleM7.add(value != null ? value : BigDecimal.ZERO);
							break;
						}
						totale = totale == null ? BigDecimal.ZERO.add(value != null ? value : BigDecimal.ZERO)
								: totale.add(value != null ? value : BigDecimal.ZERO);
					}
				}
			}
		}
		List<GregTMacroaggregatiBilancio> macro = macroaggregatiDao.findAllMacroaggregati();

		for (GregTMacroaggregatiBilancio valore : macro) {
			ModelTotaleMacroaggregati totaleMacro = new ModelTotaleMacroaggregati();
			totaleMacro.setCodMacroaggregati(valore.getCodMacroaggregatoBilancio());
			totaleMacro.setDescMacroaggregati(valore.getDesMacroaggregatoBilancio());
			totaleMacro.setOrdinamento(valore.getOrdinamento());
			switch (valore.getCodMacroaggregatoBilancio()) {
			case "1VB":
				totaleMacro.setTotale(totaleM1);
				break;
			case "2VB":
				totaleMacro.setTotale(totaleM2);
				break;
			case "3VB":
				totaleMacro.setTotale(totaleM3);
				break;
			case "4VB":
				totaleMacro.setTotale(totaleM4);
				break;
			case "5VB":
				totaleMacro.setTotale(totaleM5);
				break;
			case "6VB":
				totaleMacro.setTotale(totaleM6);
				break;
			case "7VB":
				totaleMacro.setTotale(totaleM7);
				break;
			}
			arrayMacro.add(totaleMacro);
		}
		ModelTotaleMacroaggregati totaleMacro = new ModelTotaleMacroaggregati();
		totaleMacro.setDescMacroaggregati("TOTALE");
		totaleMacro.setCodMacroaggregati("");
		totaleMacro.setOrdinamento(macro.size() + 1);
		totaleMacro.setTotale(totale);
		arrayMacro.add(totaleMacro);
		totaliMacroaggregati.setValoriMacroaggregati(arrayMacro);
		return totaliMacroaggregati;
	}

	public String esportaMacroaggregati(EsportaMacroaggregatiInput body) throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Macroaggregati");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		HSSFDataFormat format = workbook.createDataFormat();

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
		cellStyletitolo2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyletitolo2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo2.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo2.setFont(font12b);
		cellStyletitolo2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo3
		CellStyle cellStyletitolo3 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo3.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyletitolo3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo3.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo3.setFont(font10b);
		cellStyletitolo3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial titolo4
		CellStyle cellStyletitolo4 = sheet.getWorkbook().createCellStyle();
		cellStyletitolo4.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyletitolo4.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo4.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo4.setFont(font10);
		cellStyletitolo4.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyletitolo4.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyletitolo4.setBorderRight(CellStyle.BORDER_DOUBLE);
		cellStyletitolo4.setBorderBottom(CellStyle.BORDER_DOUBLE);
		cellStyletitolo4.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyletitolo4.setWrapText(true);

		// crea stili arial titolo4 bold
		CellStyle cellStyletitolo4b = sheet.getWorkbook().createCellStyle();
		cellStyletitolo4b.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyletitolo4b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyletitolo4b.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyletitolo4b.setFont(font12b);
		cellStyletitolo4b.setBorderTop(CellStyle.BORDER_DOUBLE);
		cellStyletitolo4b.setBorderLeft(CellStyle.BORDER_DOUBLE);
		cellStyletitolo4b.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyletitolo4b.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyletitolo4b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// crea stili arial Missioni
		CellStyle cellStyleMissione = sheet.getWorkbook().createCellStyle();
		cellStyleMissione.setFont(font10);
		cellStyleMissione.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyleMissione.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleMissione.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleMissione.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyleMissione.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleMissione.setWrapText(true);

		// crea stili arial Programmi
		CellStyle cellStyleProgramma = sheet.getWorkbook().createCellStyle();
		cellStyleProgramma.setFont(font10);
		cellStyleProgramma.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleProgramma.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleProgramma.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleProgramma.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleProgramma.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleProgramma.setWrapText(true);

		// crea stili arial Programmi giallo
		CellStyle cellStyleProgrammaY = sheet.getWorkbook().createCellStyle();
		cellStyleProgrammaY.setFont(font10);
		cellStyleProgrammaY.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyleProgrammaY.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleProgrammaY.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleProgrammaY.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleProgrammaY.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleProgrammaY.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleProgrammaY.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleProgrammaY.setWrapText(true);

		// crea stili arial Totale giallo
		CellStyle cellStyleTotaleY = sheet.getWorkbook().createCellStyle();
		cellStyleTotaleY.setFont(font10b);
		cellStyleTotaleY.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTotaleY.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyleTotaleY.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaleY.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotaleY.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleY.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTotaleY.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleY.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaleY.setWrapText(true);

		// crea stili arial Totale grigio
		CellStyle cellStyleTotaleG = sheet.getWorkbook().createCellStyle();
		cellStyleTotaleG.setFont(font10b);
		cellStyleTotaleG.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTotaleG.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleTotaleG.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaleG.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotaleG.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleG.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTotaleG.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleG.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaleG.setWrapText(true);

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
		cellStyle10c.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle10c.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle10c.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle10c.setDataFormat(format.getFormat(pattern));

		// totale green
		CellStyle cellStyleTotVal = sheet.getWorkbook().createCellStyle();
		cellStyleTotVal.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotVal.setFont(font10bc);
		cellStyleTotVal.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotVal.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTotVal.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotVal.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyleTotVal.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleTotVal.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotVal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotVal.setDataFormat(format.getFormat(pattern));

		// totale green 2
		CellStyle cellStyleTotVal2 = sheet.getWorkbook().createCellStyle();
		cellStyleTotVal2.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotVal2.setFont(font10bc);
		cellStyleTotVal2.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleTotVal2.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleTotVal2.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleTotVal2.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyleTotVal2.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleTotVal2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotVal2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotVal2.setDataFormat(format.getFormat(pattern));

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService
				.getRendicontazione(body.getDatiRendicontazione().getIdRendicontazioneEnte());
		ModelMsgInformativo intestazione = listeService.getMsgInformativiByCodice("34");
		ModelMsgInformativo header1 = listeService.getMsgInformativiByCodice("35");
		ModelMsgInformativo header2 = listeService.getMsgInformativiByCodice("36");
		ModelMsgInformativo header3 = listeService.getMsgInformativiByCodice("37");
		List<ModelMacroaggregati> macroaggregati = getMacroaggregati();

		// Row 1
		Row row = sheet.createRow(rowCount);
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
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - "
				+ body.getDatiRendicontazione().getDenominazioneEnte());
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
		cell.setCellValue((String) intestazione.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		columnCount = 0;

		// Row header1-2
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header1.getTestoMsgInformativo().replace("@anno@",
				String.valueOf(body.getDatiRendicontazione().getAnnoGestione() - 1)));
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());

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
		cell.setCellValue(header2.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo2);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());

		autoSizeColumnsAndMergedMacroaggregati(sheet.getWorkbook());
		columnCount = 0;

		// Row header 3
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount + 1, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(header3.getTestoMsgInformativo());
		cell.setCellStyle(cellStyletitolo3);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());

		int tmpColumnCount = columnCount;
		for (ModelMacroaggregati macro : macroaggregati) {
			cell = row.createCell(++tmpColumnCount);
			cell.setCellValue(macro.getDescMacroaggregati() + " " + macro.getAltraDescMacroaggregati());
			sheet.setColumnWidth(tmpColumnCount, 20 * 256);
			cell.setCellStyle(cellStyletitolo4);
		}
		cell = row.createCell(++tmpColumnCount);
		cell.setCellValue("Totale");
		cell.setCellStyle(cellStyletitolo3);
		row.setHeight((short) (15 * 256));

		row = sheet.createRow(++rowCount);
		tmpColumnCount = columnCount;
		for (ModelMacroaggregati macro : macroaggregati) {
			cell = row.createCell(++tmpColumnCount);
			cell.setCellValue(macro.getCodificaMacroaggregati());
			cell.setCellStyle(cellStyletitolo4b);
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		row.setHeight((short) (row.getHeight() * 2));

		columnCount = tmpColumnCount;
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		sheet.setColumnWidth(columnCount, 20 * 256);

		// missioni - programmi - valori
		List<ModelValoriMacroaggregati> valori = body.getDatiRendicontazione().getValoriMacroaggregati();
		for (int i = 0; i < valori.size(); i++) {
			columnCount = 0;
			row = sheet.createRow(++rowCount);
			if (valori.get(i).getDescrizioneMissione() != null) {
				row.setHeight((short) (5 * 256));
				cell = row.createCell(columnCount);
				cell.setCellStyle(cellStyleMissione);
				if (i > 0 && valori.get(i - 1).getDescrizioneMissione()
						.equalsIgnoreCase(valori.get(i).getDescrizioneMissione())) {
					cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
					sheet.addMergedRegion(cellRangeAddress);
					RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				} else {
					cell.setCellValue(valori.get(i).getDescrizioneMissione());
				}

				cell = row.createCell(++columnCount);
				if (valori.get(i).getCodMissione().equalsIgnoreCase("2")
						|| valori.get(i).getCodMissione().equalsIgnoreCase("3")
						|| valori.get(i).getCodMissione().equalsIgnoreCase("4")) {
					cell.setCellStyle(cellStyleProgrammaY);
				} else
					cell.setCellStyle(cellStyleProgramma);

				cell.setCellValue(valori.get(i).getDescrizioneProgramma());

				for (ModelCampiMacroaggregati campo : valori.get(i).getCampi()) {
					cell = row.createCell(++columnCount);
					if (campo.getValue() != null) {
						cell.setCellValue(campo.getValue().doubleValue());
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					}
					cell.setCellStyle(cellStyle10c);
				}

				cell = row.createCell(++columnCount);
				cell.setCellValue(Double.valueOf(body.getTotaliR().get(i).replaceAll("\\.", "").replace(",", ".")));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(cellStyleTotVal);
			} else {
				row.setHeight((short) (row.getHeight() * 2));
				cell = row.createCell(columnCount);
				row.createCell(++columnCount);
				cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
				sheet.addMergedRegion(cellRangeAddress);
				cell.setCellValue(valori.get(i).getDescrizioneProgramma());
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
				if (i == valori.size() - 1) {
					RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
					cell.setCellStyle(cellStyleTotaleY);
					for (String tot : body.getTotaliCS()) {
						cell = row.createCell(++columnCount);
						cell.setCellValue(Double.valueOf(tot.replaceAll("\\.", "").replace(",", ".")));
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(cellStyleTotVal2);
					}
					cell = row.createCell(++columnCount);
					cell.setCellValue(Double.valueOf(body.getTotaliR().get(i).replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellStyle(cellStyleTotVal);
				} else {
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					cell.setCellStyle(cellStyleTotaleG);

					for (String tot : body.getTotaliC()) {
						cell = row.createCell(++columnCount);
						cell.setCellValue(Double.valueOf(tot.replaceAll("\\.", "").replace(",", ".")));
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(cellStyleTotVal2);
					}
					cell = row.createCell(++columnCount);
					cell.setCellValue(Double.valueOf(body.getTotaliR().get(i).replaceAll("\\.", "").replace(",", ".")));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellStyle(cellStyleTotVal);
				}
			}
		}

		// END

		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportMacroaggregati_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));

	}

	private static void autoSizeColumnsAndMergedMacroaggregati(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
					Row row = sheet.getRow(j);
					if (j == 3 || j == 4)
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
	public GenericResponseWarnErr checkMacroaggregati(Integer idRendicontazione) throws Exception {
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
			ModelRendicontazioneMacroaggregati datiModelloAttuale = getRendicontazioneMacroaggregatiByIdScheda(
					rendicontazioneAttuale.getIdRendicontazioneEnte());
			ModelRendicontazioneMacroaggregati datiModelloPassata = getRendicontazioneMacroaggregatiByIdScheda(
					rendicontazionePassata.getIdRendicontazioneEnte());

			BigDecimal totaleMissione01NonAttuale = BigDecimal.ZERO;
			BigDecimal totaleMissione01NonPassata = BigDecimal.ZERO;
			BigDecimal totaleMissione01ParteAttuale = BigDecimal.ZERO;
			BigDecimal totaleMissione01PartePassata = BigDecimal.ZERO;
			BigDecimal totaleMissione12Attuale = BigDecimal.ZERO;
			BigDecimal totaleMissione12Passata = BigDecimal.ZERO;
			BigDecimal totaleMissione04Attuale = BigDecimal.ZERO;
			BigDecimal totaleMissione04Passata = BigDecimal.ZERO;
			BigDecimal totaleMissioneAltroAttuale = BigDecimal.ZERO;
			BigDecimal totaleMissioneAltroPassata = BigDecimal.ZERO;
			BigDecimal totaleMacroaggregatiAttuale = BigDecimal.ZERO;
			BigDecimal totaleMacroaggregatiPassata = BigDecimal.ZERO;

			for (ModelValoriMacroaggregati dato : datiModelloAttuale.getValoriMacroaggregati()) {
				if (dato.getCampi() != null) {
					for (ModelCampiMacroaggregati valore : dato.getCampi()) {
						if (valore.getValue() != null) {
							switch (dato.getCodMissione()) {
							case "1":
								totaleMissione01NonAttuale = totaleMissione01NonAttuale.add(valore.getValue());
								break;
							case "2":
								totaleMissione01ParteAttuale = totaleMissione01ParteAttuale.add(valore.getValue());
								break;
							case "3":
								totaleMissione12Attuale = totaleMissione12Attuale.add(valore.getValue());
								break;
							case "4":
								totaleMissione04Attuale = totaleMissione04Attuale.add(valore.getValue());
								break;
							case "5":
								totaleMissioneAltroAttuale = totaleMissioneAltroAttuale.add(valore.getValue());
								break;
							}
							totaleMacroaggregatiAttuale = totaleMacroaggregatiAttuale.add(valore.getValue());
						}
					}
				}
			}

			for (ModelValoriMacroaggregati dato : datiModelloPassata.getValoriMacroaggregati()) {
				if (dato.getCampi() != null) {
					for (ModelCampiMacroaggregati valore : dato.getCampi()) {
						if (valore.getValue() != null) {
							switch (dato.getCodMissione()) {
							case "1":
								totaleMissione01NonPassata = totaleMissione01NonPassata.add(valore.getValue());
								break;
							case "2":
								totaleMissione01PartePassata = totaleMissione01PartePassata.add(valore.getValue());
								break;
							case "3":
								totaleMissione12Passata = totaleMissione12Passata.add(valore.getValue());
								break;
							case "4":
								totaleMissione04Passata = totaleMissione04Passata.add(valore.getValue());
								break;
							case "5":
								totaleMissioneAltroPassata = totaleMissioneAltroPassata.add(valore.getValue());
								break;
							}
							totaleMacroaggregatiPassata = totaleMacroaggregatiPassata.add(valore.getValue());
						}
					}
				}
			}

			BigDecimal totaleMissione01Non25 = (totaleMissione01NonPassata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMissione01Parte25 = (totaleMissione01PartePassata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMissione1225 = (totaleMissione12Passata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMissione0425 = (totaleMissione04Passata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMissioneAltro25 = (totaleMissioneAltroPassata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
			BigDecimal totaleMacroaggregati25 = (totaleMacroaggregatiPassata.multiply(new BigDecimal(25)))
					.divide(new BigDecimal(100), RoundingMode.HALF_DOWN);

			if ((totaleMissione01NonAttuale.setScale(2))
					.compareTo(totaleMissione01NonPassata.subtract(totaleMissione01Non25).setScale(2)) < 0
					|| (totaleMissione01NonAttuale.setScale(2))
							.compareTo(totaleMissione01NonPassata.add(totaleMissione01Non25).setScale(2)) > 0) {
				if (((totaleMissione01NonAttuale.subtract(totaleMissione01NonPassata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MACROAGGREGATI01).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissione01NonAttuale))
									.replace("DATOPASSATO",
											Util.convertBigDecimalToString(totaleMissione01NonPassata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMissione01ParteAttuale.setScale(2))
					.compareTo(totaleMissione01PartePassata.subtract(totaleMissione01Parte25).setScale(2)) < 0
					|| (totaleMissione01ParteAttuale.setScale(2))
							.compareTo(totaleMissione01PartePassata.add(totaleMissione01Parte25).setScale(2)) > 0) {
				if (((totaleMissione01ParteAttuale.subtract(totaleMissione01PartePassata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_MACROAGGREGATI02)
							.getTestoMessaggio()
							.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissione01ParteAttuale))
							.replace("DATOPASSATO", Util.convertBigDecimalToString(totaleMissione01PartePassata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMissione12Attuale.setScale(2))
					.compareTo(totaleMissione12Passata.subtract(totaleMissione1225).setScale(2)) < 0
					|| (totaleMissione12Attuale.setScale(2))
							.compareTo(totaleMissione12Passata.add(totaleMissione1225).setScale(2)) > 0) {
				if (((totaleMissione12Attuale.subtract(totaleMissione12Passata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MACROAGGREGATI03).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissione12Attuale))
									.replace("DATOPASSATO", Util.convertBigDecimalToString(totaleMissione12Passata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMissione04Attuale.setScale(2))
					.compareTo(totaleMissione04Passata.subtract(totaleMissione0425).setScale(2)) < 0
					|| (totaleMissione04Attuale.setScale(2))
							.compareTo(totaleMissione04Passata.add(totaleMissione0425).setScale(2)) > 0) {
				if (((totaleMissione04Attuale.subtract(totaleMissione04Passata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MACROAGGREGATI04).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissione04Attuale))
									.replace("DATOPASSATO", Util.convertBigDecimalToString(totaleMissione04Passata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMissioneAltroAttuale.setScale(2))
					.compareTo(totaleMissioneAltroPassata.subtract(totaleMissioneAltro25).setScale(2)) < 0
					|| (totaleMissioneAltroAttuale.setScale(2))
							.compareTo(totaleMissioneAltroPassata.add(totaleMissioneAltro25).setScale(2)) > 0) {
				if (((totaleMissioneAltroAttuale.subtract(totaleMissioneAltroPassata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MACROAGGREGATI05).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMissioneAltroAttuale))
									.replace("DATOPASSATO",
											Util.convertBigDecimalToString(totaleMissioneAltroPassata)));
					response.setObblMotivazione(true);
				}
			}

			if ((totaleMacroaggregatiAttuale.setScale(2))
					.compareTo(totaleMacroaggregatiPassata.subtract(totaleMacroaggregati25).setScale(2)) < 0
					|| (totaleMacroaggregatiAttuale.setScale(2))
							.compareTo(totaleMacroaggregatiPassata.add(totaleMacroaggregati25).setScale(2)) > 0) {
				if (((totaleMacroaggregatiAttuale.subtract(totaleMacroaggregatiPassata)).abs())
						.compareTo(new BigDecimal(100000)) > 0) {
					response.getWarnings()
							.add(listeService.getMessaggio(SharedConstants.CHECK_MACROAGGREGATI06).getTestoMessaggio()
									.replace("DATOATTUALE", Util.convertBigDecimalToString(totaleMacroaggregatiAttuale))
									.replace("DATOPASSATO",
											Util.convertBigDecimalToString(totaleMacroaggregatiPassata)));
					response.setObblMotivazione(true);
				}
			}

			if (response.getWarnings().size() > 0) {
				List<GregRCheck> checkA = datiRendicontazioneService.getMotivazioniCheck(SharedConstants.MODELLO_MA,
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
		ModelStatoMod stato = macroaggregatiDao.getStatoModelloMA(idRendicontazione);
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
	public GenericResponseWarnErr controlloMacroaggregati(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);

		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_MA);

		boolean facoltativo = false;
		boolean valorizzato = macroaggregatiDao.getValorizzatoModelloMA(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors()
					.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
							.replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

		// Controlli Modello Macroaggregati
		if (!facoltativo) {
			Boolean trovataIncongRedditoAcquistoB = false;
			ModelRendicontazioneMacroaggregati rendicontazioneMacro = getRendicontazioneMacroaggregatiByIdScheda(
					rendicontazione.getIdRendicontazioneEnte());

			GregRRendicontazioneModAPart1 titolo2 = datiRendicontazioneDao.getRendicontazioneModAByIdRendicontazione(
					rendicontazione.getIdRendicontazioneEnte(), "02-ALTRO", "Tipo_altro", "31");
			BigDecimal totaleMissioneAltro = BigDecimal.ZERO.setScale(2);
			for (ModelValoriMacroaggregati missioneMacro : rendicontazioneMacro.getValoriMacroaggregati()) {
				BigDecimal valoreReddito = null;
				BigDecimal valoreAcquistoB = null;
				if (missioneMacro.getCampi() != null && missioneMacro.getCampi().size() > 0) {
					for (ModelCampiMacroaggregati campoMacro : missioneMacro.getCampi()) {
						if (campoMacro.getCod().equals("1VB") && campoMacro.getValue() != null) {
							valoreReddito = campoMacro.getValue();
						}
						if (campoMacro.getCod().equals("4VB") && campoMacro.getValue() != null) {
							valoreAcquistoB = campoMacro.getValue();
						}
						if (missioneMacro.getCodMissione().equals("5")) {
							if (campoMacro.getValue() != null) {
								totaleMissioneAltro = totaleMissioneAltro.add(campoMacro.getValue().setScale(2));
							}
						}
					}
					// verifico se per ogni missione e' valorizzato il campo acquisto B ma campo
					// reddito non e' valorizzato
					if (valoreReddito == null && valoreAcquistoB != null) {
						trovataIncongRedditoAcquistoB = true;
					}
				}
			}
			if (trovataIncongRedditoAcquistoB) {
				response.getErrors().add(
						listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_01).getTestoMessaggio());
			}
			if (titolo2 != null) {
				if (titolo2.getValoreNumb() != null && !titolo2.getValoreNumb().equals(BigDecimal.ZERO)) {
					if (totaleMissioneAltro == null || totaleMissioneAltro.equals(BigDecimal.ZERO.setScale(2))) {
						response.getErrors().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_03)
								.getTestoMessaggio());
					}
				}
			}
		}

		return response;

	}
}
