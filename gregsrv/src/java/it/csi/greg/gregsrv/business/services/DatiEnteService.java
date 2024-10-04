/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.DatiEnteDao;
import it.csi.greg.gregsrv.business.entity.GregDAsl;
import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDMotivazione;
import it.csi.greg.gregsrv.business.entity.GregDObbligo;
import it.csi.greg.gregsrv.business.entity.GregDProvince;
import it.csi.greg.gregsrv.business.entity.GregDStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregDTab;
import it.csi.greg.gregsrv.business.entity.GregRCartaServiziPreg1;
import it.csi.greg.gregsrv.business.entity.GregREnteTab;
import it.csi.greg.gregsrv.business.entity.GregRMergeEnti;
import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregREnteGestoreContatti;
import it.csi.greg.gregsrv.business.entity.GregREnteGestoreStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregRResponsabileContatti;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.business.entity.GregTAllegatiRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTLista;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTResponsabileEnteGestore;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelAllegatiAssociati;
import it.csi.greg.gregsrv.dto.ModelChiusura;
import it.csi.greg.gregsrv.dto.ModelComune;
import it.csi.greg.gregsrv.dto.ModelComuniAssociati;
import it.csi.greg.gregsrv.dto.ModelCronogiaStato;
import it.csi.greg.gregsrv.dto.ModelDatiAnagrafici;
import it.csi.greg.gregsrv.dto.ModelDatiAnagraficiToSave;
import it.csi.greg.gregsrv.dto.ModelDatiAsl;
import it.csi.greg.gregsrv.dto.ModelDatiEnte;
import it.csi.greg.gregsrv.dto.ModelDatiEnteRendicontazione;
import it.csi.greg.gregsrv.dto.ModelDatiEnteToSave;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestRegionale;
import it.csi.greg.gregsrv.dto.ModelMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelMotivazioni;
import it.csi.greg.gregsrv.dto.ModelPrest1Prest2;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestCollegate;
import it.csi.greg.gregsrv.dto.ModelPrestUtenza;
import it.csi.greg.gregsrv.dto.ModelPrestazioni;
import it.csi.greg.gregsrv.dto.ModelPrestazioniAssociate;
import it.csi.greg.gregsrv.dto.ModelProvincia;
import it.csi.greg.gregsrv.dto.ModelResponsabileEnte;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.RicercaListaEntiDaunire;
import it.csi.greg.gregsrv.dto.UnioneEnte;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("DatiEnte")
public class DatiEnteService {

	@Autowired
	protected DatiEnteDao datiEnteDao;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ConfiguratoreUtenzeFnpsService configuratoreUtenzeFnpsService;

	public ModelDatiAnagrafici findSchedaEnte(Integer idScheda, String codregione, String dataValidita) {
		Date d = null;
		if (Checker.isValorizzato(dataValidita))
			d = Converter.getDataEnglish(dataValidita);
		ModelDatiAnagrafici lista = datiEnteDao.findSchedaEnte(idScheda, codregione, d);

		return lista;
	}

	public ModelDatiEnte findSchedaEntexAnno(Integer idRendicontazione, Integer annoGestione) {
		ModelDatiEnte lista = new ModelDatiEnte();

		lista = datiEnteDao.findSchedaEntexAnno(idRendicontazione, annoGestione);

		return lista;
	}

	public ModelDatiEnteRendicontazione findDatiEntexAnno(Integer idRendicontazione, Integer annoGestione) {
		ModelDatiEnteRendicontazione lista = new ModelDatiEnteRendicontazione();

		lista = datiEnteDao.findDatiEntexAnno(idRendicontazione, annoGestione);

		return lista;
	}

//	public GregTSchedeEntiGestori getSchedaEnte (Integer idScheda) {
//		GregTSchedeEntiGestori schedaEnte = new GregTSchedeEntiGestori();
//		schedaEnte = datiEnteDao.findSchedaEnte(idScheda);
//		
//		return schedaEnte;
//	}

//	public GregTResponsabileEnteGestore findResponsabileEnte(Integer resId) {
//
//		GregTResponsabileEnteGestore result = new GregTResponsabileEnteGestore();
//
//		result = datiEnteDao.findResponsabileEnte(resId);
//
//		return result;
//	}

	public List<ModelProvincia> findAllProvince(String codregione, String dataValidita) {

		List<GregDProvince> resultList = new ArrayList<GregDProvince>();
		Date d = null;
		if (Checker.isValorizzato(dataValidita))
			d = Converter.getDataEnglish(dataValidita);

		resultList = datiEnteDao.findAllProvince(codregione, d);

		List<ModelProvincia> listaProvince = new ArrayList<ModelProvincia>();

		for (GregDProvince pres : resultList) {
			ModelProvincia elemento = new ModelProvincia(pres);
			listaProvince.add(elemento);
		}

		return listaProvince;
	}

	public List<ModelPrestazioni> findAllPrestazioniMadre(Integer anno) {

		List<GregTPrestazioniRegionali1> resultList = new ArrayList<GregTPrestazioniRegionali1>();

		resultList = datiEnteDao.findAllPrestazioniMadre(anno);

		List<ModelPrestazioni> listaPrestazioni = new ArrayList<ModelPrestazioni>();

		for (GregTPrestazioniRegionali1 prestazioneMadre : resultList) {
			List<ModelPrestazioni> prestazioniFiglie = findPrestazioniFiglie(prestazioneMadre.getIdPrestReg1(), anno);
			ModelPrestazioni elemento = new ModelPrestazioni(prestazioneMadre, prestazioniFiglie, false);
			listaPrestazioni.add(elemento);
		}

		return listaPrestazioni;
	}

	public List<ModelPrestazioni> findPrestazioniFiglie(Integer idPrestazioneMadre, Integer anno) {

		List<GregTPrestazioniRegionali1> resultList = new ArrayList<GregTPrestazioniRegionali1>();

		resultList = datiEnteDao.findPrestazioniFiglie(idPrestazioneMadre, anno);

		List<ModelPrestazioni> listaPrestazioni = new ArrayList<ModelPrestazioni>();

		for (GregTPrestazioniRegionali1 pres : resultList) {
			ModelPrestazioni elemento = new ModelPrestazioni(pres, true);
			listaPrestazioni.add(elemento);
		}

		return listaPrestazioni;
	}

	public List<ModelPrestazioni> findPrestazioniResSemires(String codTipologia, String codTipoStruttura,
			Integer anno) {

		List<GregTPrestazioniRegionali1> resultList = new ArrayList<GregTPrestazioniRegionali1>();

		resultList = datiEnteDao.findPrestazioniResSemires(codTipologia, codTipoStruttura);

		List<ModelPrestazioni> listaPrestazioni = new ArrayList<ModelPrestazioni>();

		for (GregTPrestazioniRegionali1 prest : resultList) {
			List<ModelPrestazioni> prestazioniFiglie = findPrestazioniFiglie(prest.getIdPrestReg1(), anno);
			ModelPrestazioni elemento = new ModelPrestazioni(prest, prestazioniFiglie, false);
			listaPrestazioni.add(elemento);
		}

		return listaPrestazioni;
	}

	public List<ModelComuniAssociati> findComuniAssociati(Integer idScheda, Integer annoGestione) {

		List<GregRSchedeEntiGestoriComuni> resultList = new ArrayList<GregRSchedeEntiGestoriComuni>();
		resultList = datiEnteDao.findComuniAssociati(idScheda, annoGestione);

		List<ModelComuniAssociati> listaComuniAssociati = new ArrayList<ModelComuniAssociati>();
		for (GregRSchedeEntiGestoriComuni comAssociati : resultList) {
			ModelComuniAssociati elemento = new ModelComuniAssociati(comAssociati, annoGestione);
			listaComuniAssociati.add(elemento);
		}

		return listaComuniAssociati;
	}

	public List<GregRSchedeEntiGestoriComuni> findAllComuniAssociati(Integer idScheda, Integer annoGestione) {

		List<GregRSchedeEntiGestoriComuni> resultList = new ArrayList<GregRSchedeEntiGestoriComuni>();
		resultList = datiEnteDao.findComuniAssociati(idScheda, annoGestione);

		return resultList;
	}

	public List<ModelPrestazioniAssociate> findPrestazioniAssociate(Integer idScheda) {

		List<GregRCartaServiziPreg1> resultList = new ArrayList<GregRCartaServiziPreg1>();
		resultList = datiEnteDao.findPrestazioniAssociate(idScheda);

		List<ModelPrestazioniAssociate> listaPrestazioniAssociate = new ArrayList<ModelPrestazioniAssociate>();
		for (GregRCartaServiziPreg1 presAssociate : resultList) {
			ModelPrestazioniAssociate elemento = new ModelPrestazioniAssociate(presAssociate);
			listaPrestazioniAssociate.add(elemento);
		}

		return listaPrestazioniAssociate;
	}

	public List<GregRCartaServiziPreg1> findAllPrestazioniAssociate(Integer idScheda) {

		List<GregRCartaServiziPreg1> resultList = new ArrayList<GregRCartaServiziPreg1>();
		resultList = datiEnteDao.findPrestazioniAssociate(idScheda);

		return resultList;
	}

	public GregDComuni findComuneByIdNotDeleted(Integer idComune) {
		return datiEnteDao.findComuneByIdNotDeleted(idComune);
	}

	public List<GregTAllegatiRendicontazione> findAllAllegatiAssociati(Integer idScheda) {

		List<GregTAllegatiRendicontazione> resultList = new ArrayList<GregTAllegatiRendicontazione>();
		resultList = datiEnteDao.findAllAllegatiAssociati(idScheda);

		return resultList;
	}

	public List<ModelComuniAssociati> findComuniAnagraficaAssociati(Integer idScheda, String dataValidita) {

		List<ModelComuniAssociati> lista = new ArrayList<ModelComuniAssociati>();
		Date d = null;
		if (Checker.isValorizzato(dataValidita)) {
			d = Converter.getDataEnglish(dataValidita);
			lista = datiEnteDao.findComuniAnagraficaAssociatiEliminati(idScheda, Converter.getAnno(d));
		} else {
			lista = datiEnteDao.findComuniAnagraficaAssociati(idScheda);
		}
		return lista;
	}

	public List<GregRSchedeEntiGestoriComuni> findGregRComuniAnagraficaAssociati(Integer idScheda) {

		List<GregRSchedeEntiGestoriComuni> lista = new ArrayList<GregRSchedeEntiGestoriComuni>();
		lista = datiEnteDao.findGregRComuniAssegnati(idScheda);

		return lista;
	}

	public GenericResponseWarnErr saveDatiEnte(ModelDatiEnteToSave datiEnte,
			List<GregTAllegatiRendicontazione> listAllegatiNew, UserInfo userInfo) throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();

		// Verifico la presenza della rendicontazione

		GregTRendicontazioneEnte rendicontazioneEnte = datiRendicontazioneService
				.getRendicontazione(datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
		if (rendicontazioneEnte.getIdRendicontazioneEnte() == null) {

			throw new IntegritaException(Util.composeMessage(
					listeService.getMessaggio(SharedConstants.ERROR_ANNO_CONTABILE).getTestoMessaggio(), ""));
		} else {

			Timestamp dataModifica = new Timestamp(new Date().getTime());
//			rendicontazioneEnte.setFnps(datiEnte.getRendicontazioneEnte().getFnps());
//			rendicontazioneEnte.setVincoloFondo(datiEnte.getRendicontazioneEnte().getVincoloFondo());
//			rendicontazioneEnte.setPippi(datiEnte.getRendicontazioneEnte().getPippi());
			rendicontazioneEnte.setStrutturaResidenziale(datiEnte.getRendicontazioneEnte().getStrutturaResidenziale());
			rendicontazioneEnte
					.setCentroDiurnoStruttSemires(datiEnte.getRendicontazioneEnte().getCentroDiurnoStruttSemires());

			rendicontazioneEnte.setUtenteOperazione(userInfo.getCodFisc());
			rendicontazioneEnte.setDataModifica(dataModifica);

			// Prestazioni Associate
			List<GregRCartaServiziPreg1> prestAssNow = findAllPrestazioniAssociate(
					datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
			List<ModelPrestazioniAssociate> prestAssNew = datiEnte.getPrestazioniAssociate();
			// Inserisco le nuove Prestazioni Associate (quelle senza PK)
			for (ModelPrestazioniAssociate prestazione : prestAssNew) {
				if (prestazione.getPkIdPrestazioneAssociata() == null) {
					GregTPrestazioniRegionali1 nuovaPrestazione = datiEnteDao
							.findPrestazione(prestazione.getIdPrestazione());
					Timestamp dataCancellazione = null;
					Timestamp dataCreazione = dataModifica;
					Timestamp dataFineValidita = null;
					String utenteOperazione = userInfo.getCodFisc();

					GregRCartaServiziPreg1 newPrestazioneAssociata = new GregRCartaServiziPreg1(dataCancellazione,
							dataCreazione, dataModifica, utenteOperazione, nuovaPrestazione, rendicontazioneEnte);
					datiEnteDao.saveNewPrestazioneAssociata(newPrestazioneAssociata);
				}
			}
			// Effettuo operazioni su associazioni eventualmente modificate
			List<Integer> listPkPrestazioniAss = new ArrayList<>();
			for (GregRCartaServiziPreg1 prest : prestAssNow) {
				listPkPrestazioniAss.add(prest.getIdCartaServiziPreg1());
			}
			List<Integer> listPkPrestazioniNew = new ArrayList<>();
			for (ModelPrestazioniAssociate prest : prestAssNew) {
				if (prest.getPkIdPrestazioneAssociata() != null) {
					listPkPrestazioniNew.add(prest.getPkIdPrestazioneAssociata());
				}
			}
			for (Integer pkPrestAss : listPkPrestazioniAss) {
				if (!listPkPrestazioniNew.contains(pkPrestAss)) {
					GregRCartaServiziPreg1 prestAssToUpdate = prestAssNow.stream()
							.filter(prest -> prest.getIdCartaServiziPreg1() == pkPrestAss).findFirst().orElse(null);
					if (prestAssToUpdate != null) {
						prestAssToUpdate.setDataModifica(dataModifica);
						prestAssToUpdate.setDataCancellazione(dataModifica);
						datiEnteDao.updatePrestazioneAssociata(prestAssToUpdate);
					}
				}
			}

			List<GregTAllegatiRendicontazione> allegatiNow = findAllAllegatiAssociati(
					datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
			
			List<ModelAllegatiAssociati> allegatiNew = datiEnte.getAllegatiAssociati();

			// Salvataggio Allegati
			// Nuovi Allegati
			for (GregTAllegatiRendicontazione allegato : listAllegatiNew) {
				allegato.setDataCreazione(dataModifica);
				allegato.setDataModifica(dataModifica);
				allegato.setGregTRendicontazioneEnte(rendicontazioneEnte);
				datiEnteDao.insertAllegato(allegato);
			}
			// Effettuo operazioni su Allegati eventualmente rimossi
			List<Integer> listPkAllegatiAss = new ArrayList<>();
			for (GregTAllegatiRendicontazione allegato : allegatiNow) {
				if(!allegato.getTipoDocumentazione().equals(SharedConstants.AL_ZERO)) {
					listPkAllegatiAss.add(allegato.getIdAllegatiRendicontazione());
				}
			}
			List<Integer> listPkAllegatiNew = new ArrayList<>();
			for (ModelAllegatiAssociati allegato : allegatiNew) {
				if (allegato.getPkAllegatoAssociato() != null) {
					listPkAllegatiNew.add(allegato.getPkAllegatoAssociato());
				}
			}
			for (Integer pkAllegatoAss : listPkAllegatiAss) {
				if (!listPkAllegatiNew.contains(pkAllegatoAss)) {
					GregTAllegatiRendicontazione allegatoAssToUpdate = allegatiNow.stream()
							.filter(allegato -> allegato.getIdAllegatiRendicontazione() == pkAllegatoAss).findFirst()
							.orElse(null);
					if (allegatoAssToUpdate != null) {
						allegatoAssToUpdate.setDataModifica(dataModifica);
						allegatoAssToUpdate.setDataCancellazione(dataModifica);
						datiEnteDao.updateAllegatoAssociato(allegatoAssToUpdate);
					}
				}
			}

			String notaEnte = "";

			String statoOld = rendicontazioneEnte.getGregDStatoRendicontazione().getDesStatoRendicontazione()
					.toUpperCase();

			// Controllo e aggiorno lo stato della rendicontazione
			datiRendicontazioneService.modificaStatoRendicontazione(rendicontazioneEnte, userInfo,
					SharedConstants.OPERAZIONE_SALVA, datiEnte.getProfilo());
			String statoNew = rendicontazioneEnte.getGregDStatoRendicontazione().getDesStatoRendicontazione()
					.toUpperCase();
			if (!statoOld.equals(statoNew)) {
				notaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO).getTestoMessaggio()
						.replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
						.replace("STATONEW", "'" + statoNew + "'");
			}

			// Recupero eventuale ultima cronologia inserita

			GregTCronologia lastCrono = datiRendicontazioneService
					.findLastCronologiaEnte(rendicontazioneEnte.getIdRendicontazioneEnte());

			if (datiEnte.getProfilo() != null && datiEnte.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {

				if (!Checker.isValorizzato(datiEnte.getCronologia().getNotaEnte())) {
					String msgUpdateDati = listeService
							.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI).getTestoMessaggio()

							.replace("OPERAZIONE", "SALVA");
					notaEnte = !notaEnte.equals("") ? notaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: notaEnte;
				} else {
//					notaEnte = Checker.isValorizzato(notaEnte)? notaEnte + " " + datiEnte.getCronologia().getNotaEnte() : datiEnte.getCronologia().getNotaEnte();
					notaEnte = datiEnte.getCronologia().getNotaEnte();
				}

			} else {
//				notaEnte = Checker.isValorizzato(notaEnte)? notaEnte + " " + datiEnte.getCronologia().getNotaEnte() : datiEnte.getCronologia().getNotaEnte();
				notaEnte = datiEnte.getCronologia().getNotaEnte();
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(notaEnte) || Checker.isValorizzato(datiEnte.getCronologia().getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendicontazioneEnte);
				cronologia.setGregDStatoRendicontazione(rendicontazioneEnte.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Dati ente");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(datiEnte.getCronologia().getNotaInterna());
				cronologia.setNotaPerEnte(notaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiEnteDao.insertCronologia(cronologia);
			}
			// Salvataggio
			datiEnteDao.saveRendicontazioneEnte(rendicontazioneEnte);
			// salvataggio dei modelli associati
			// prelevo la lista dei modelli associati nuovi e confronto con i vecchi
			boolean trovato = false;
			ArrayList<ModelTabTranche> modelliold = datiRendicontazioneService
					.findModelliAssociati(datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
			for (ModelTabTranche modelloold : modelliold) {
				trovato = false;
				for (ModelTabTranche modellonew : datiEnte.getModelli()) {
					if (modellonew.getIdTab() == modelloold.getIdTab()) {
						if (modellonew.getIdObbligo() != modelloold.getIdObbligo()) {
							// fare update di obbligo
							GregREnteTab updatemodello = new GregREnteTab();
							GregDTab tab = new GregDTab();
							GregTRendicontazioneEnte rend = new GregTRendicontazioneEnte();
							GregDObbligo obbligo = new GregDObbligo();
							obbligo.setIdObbligo(modellonew.getIdObbligo());
							rend.setIdRendicontazioneEnte(datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
							tab.setIdTab(modelloold.getIdTab());
							updatemodello.setGregDTab(tab);
							updatemodello.setGregTRendicontazioneEnte(rend);
							updatemodello.setIdEnteTab(modelloold.getIdEnteTab());
							updatemodello.setGregDObbligo(obbligo);
							updatemodello.setUtenteOperazione(userInfo.getCodFisc());
							datiEnteDao.updateModelloAssociato(updatemodello);
						}
						trovato = true;
						break;
					}
				}
				if (!trovato) {
					// cancello logicamente da tabella vecchio
					GregREnteTab updatemodello = new GregREnteTab();
					GregDTab tab = new GregDTab();
					GregTRendicontazioneEnte rend = new GregTRendicontazioneEnte();
					GregDObbligo obbligo = new GregDObbligo();
					obbligo.setIdObbligo(modelloold.getIdObbligo());
					rend.setIdRendicontazioneEnte(datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
					tab.setIdTab(modelloold.getIdTab());
					updatemodello.setGregDTab(tab);
					updatemodello.setGregTRendicontazioneEnte(rend);
					updatemodello.setGregDObbligo(obbligo);
					updatemodello.setIdEnteTab(modelloold.getIdEnteTab());
					updatemodello.setDataCancellazione(dataModifica);
					updatemodello.setUtenteOperazione(userInfo.getCodFisc());
					datiEnteDao.updateModelloAssociato(updatemodello);
				}
			}
			for (ModelTabTranche modellonew : datiEnte.getModelli()) {
				trovato = false;
				for (ModelTabTranche modelloold : modelliold) {
					if (modellonew.getIdTab() == modelloold.getIdTab()) {
						trovato = true;
						break;
					}
				}
				if (!trovato) {
					// inserisci record in tabella
					GregREnteTab insertmodello = new GregREnteTab();
					GregDTab tab = new GregDTab();
					GregTRendicontazioneEnte rend = new GregTRendicontazioneEnte();
					GregDObbligo obbligo = new GregDObbligo();
					obbligo.setIdObbligo(modellonew.getIdObbligo());
					rend.setIdRendicontazioneEnte(datiEnte.getRendicontazioneEnte().getIdRendicontazioneEnte());
					tab.setIdTab(modellonew.getIdTab());
					insertmodello.setGregDTab(tab);
					insertmodello.setGregTRendicontazioneEnte(rend);
					insertmodello.setGregDObbligo(obbligo);
					insertmodello.setDataCancellazione(null);
					insertmodello.setUtenteOperazione(userInfo.getCodFisc());
					datiEnteDao.insertModello(insertmodello);
				}
			}
			configuratoreUtenzeFnpsService.salvaFondi(datiEnte.getFondi(), userInfo, rendicontazioneEnte);
			// datiEnteDao.saveResponsabileEnte(respToUpdate);
			// prelevo i dati dell'ultimo responsabile e ente
			ModelUltimoContatto ultimoContatto = mailService
					.findDatiUltimoContatto(datiEnte.getRendicontazioneEnte().getIdSchedaEnte());
			// Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_ENTE_STORICO);
			if (trovataemail) {

				if (datiEnte.getProfilo() != null && datiEnte.getProfilo().getListaazioni().get("InviaEmail")[1]) {
					// Invio mail
					EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
							ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
							SharedConstants.MAIL_MODIFICA_DATI_ANAG_ENTE);
					response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
					response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());

				}
			}
			response.setId(HttpStatus.OK.toString());
			String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio()
					.replace("oggettosalvo", "Ente");
			response.setDescrizione(message);
			return response;
		}
	}

	public List<ModelDatiAsl> findAllAsl(String codregione, String dataValidita) {

		List<GregDAsl> resultList = new ArrayList<GregDAsl>();
		Date d = null;
		if (Checker.isValorizzato(dataValidita))
			d = Converter.getDataEnglish(dataValidita);
		resultList = datiEnteDao.findAllAsl(codregione, d);

		List<ModelDatiAsl> lista = new ArrayList<ModelDatiAsl>();
		for (GregDAsl asl : resultList) {
			ModelDatiAsl elemento = new ModelDatiAsl(asl);
			lista.add(elemento);
		}

		return lista;
	}

	public List<ModelAllegatiAssociati> findAllegatiAssociati(Integer idScheda) {
		List<Object> resultList = new ArrayList<Object>();
		resultList = datiEnteDao.findAllegatiAssociati(idScheda);

		List<ModelAllegatiAssociati> listaAllegatiAssociati = new ArrayList<ModelAllegatiAssociati>();

		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			listaAllegatiAssociati.add(new ModelAllegatiAssociati(obj));
		}

		return listaAllegatiAssociati;
	}

	public ModelAllegatiAssociati getAllegatoToDownload(Integer idAllegato) {

		GregTAllegatiRendicontazione allegatoToDownload = datiEnteDao.getAllegatoToDownload(idAllegato);

		return new ModelAllegatiAssociati(allegatoToDownload.getNomeFile(), allegatoToDownload.getFileAllegato());
	}

	public List<String> getListaPrestazioniValorizzateModA(Integer idScheda) {
		// Verifica presenza eventuali prestazioni associate Modello A
		return datiEnteDao.getListaPrestazioniValorizzateModA(idScheda);
	}

	public List<String> getListaPrestazioniValorizzateModB1(Integer idScheda) {
		// Verifica presenza eventuali prestazioni associate Modello B1
		return datiEnteDao.getListaPrestazioniValorizzateModB1(idScheda);
	}

	public List<String> getListaPrestazioniValorizzateModC(Integer idScheda) {
		// Verifica presenza eventuali prestazioni associate Modello C
		return datiEnteDao.getListaPrestazioniValorizzateModC(idScheda);
	}

	public List<String> getListaComuniValorizzatiModA1(Integer idScheda) {
		// Verifica presenza eventuali comuni associati Modello A1
		return datiEnteDao.getListaComuniValorizzatiModA1(idScheda);
	}

	public List<String> getListaComuniValorizzatiModA2(Integer idScheda) {
		// Verifica presenza eventuali comuni associati Modello A2
		return datiEnteDao.getListaComuniValorizzatiModA2(idScheda);
	}

	public List<String> getListaComuniValorizzatiModE(Integer idScheda) {
		// Verifica presenza eventuali comuni associati Modello E
		return datiEnteDao.getListaComuniValorizzatiModE(idScheda);
	}

	public GenericResponseWarnErr saveDatiAnagrafica(ModelDatiAnagraficiToSave datiEnte, UserInfo userInfo)
			throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();
		// Timestamp dataAttuale = new Timestamp(new Date().getTime());
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		Timestamp datafinevalidita = new Timestamp(datiEnte.getDataModifica().getTime());
		Timestamp datafine = new Timestamp(Converter.aggiungiGiorniAData(datiEnte.getDataModifica(), -1).getTime());
		// Recupero i dati delle entity da aggiornare
		GregTSchedeEntiGestori schedaToUpdate = datiEnteDao.findScheda(datiEnte.getDatiEnte().getIdSchedaEntegestore());
		GregREnteGestoreContatti contattiToUpdate = datiEnteDao
				.findLastContatti(datiEnte.getDatiEnte().getIdSchedaEntegestore());
		GregTResponsabileEnteGestore respToUpdate = datiEnteDao
				.findResponsabileEnte(datiEnte.getDatiEnte().getResponsabileEnte().getCodiceFiscale());
		GregRResponsabileContatti respContattiToUpdate = new GregRResponsabileContatti();
		if (respToUpdate != null) {
			respContattiToUpdate = datiEnteDao.findResponsabileContattiByCod(
					contattiToUpdate.getGregRResponsabileContatti().getIdResponsabileContatti());
		}
		ModelResponsabileEnte responsabilemail = new ModelResponsabileEnte();
		responsabilemail.seteMail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
		responsabilemail.setNome(datiEnte.getDatiEnte().getResponsabileEnte().getNome());
		responsabilemail.setCognome(datiEnte.getDatiEnte().getResponsabileEnte().getCognome());
		GregRResponsabileContatti newContattiResp = new GregRResponsabileContatti();
		if (datiEnte.isModificaResp()) {
			if (respToUpdate != null) {
				// esiste responsabile devo creare solo i contatti
				if (!respToUpdate.getNome().equalsIgnoreCase(datiEnte.getDatiEnte().getResponsabileEnte().getNome())
						|| !respToUpdate.getCognome()
								.equalsIgnoreCase(datiEnte.getDatiEnte().getResponsabileEnte().getCognome())) {

					response.setId(HttpStatus.INTERNAL_SERVER_ERROR.toString());
					response.setDescrizione(
							listeService.getMessaggio(SharedConstants.ERROR_DATIANAGRAFICI01).getTestoMessaggio());
					return response;
				} else {
					if (!datiEnte.isSameData()) {
						respToUpdate.setDataModifica(dataAttuale);
						respToUpdate = datiEnteDao.saveResponsabileEnte(respToUpdate);
						respContattiToUpdate.setDataFineValidita(datafine);
						datiEnteDao.saveResponsabileContatti(respContattiToUpdate);
						contattiToUpdate.setDataFineValidita(datafine);
						datiEnteDao.saveDatiAnagrafici(contattiToUpdate);
						newContattiResp.setGregTResponsabileEnteGestore(respToUpdate);
						newContattiResp.setCellulare(datiEnte.getDatiEnte().getResponsabileEnte().getCellulare());
						newContattiResp.setTelefono(datiEnte.getDatiEnte().getResponsabileEnte().getTelefono());
						newContattiResp.setEmail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
						newContattiResp.setDataCreazione(dataAttuale);
						newContattiResp.setDataInizioValidita(datafinevalidita);
						newContattiResp.setUtenteOperazione(userInfo.getCodFisc());
						newContattiResp = datiEnteDao.saveResponsabileContatti(newContattiResp);
						// inserisco il contatto nuovo nei contatti dellente perche ente responsabile
						// uguale cambio contatti

						GregREnteGestoreContatti newContatti = new GregREnteGestoreContatti();
						newContatti.setGregTSchedeEntiGestori(schedaToUpdate);
						newContatti.setCodSchedaEnteGestore(contattiToUpdate.getCodSchedaEnteGestore());
						newContatti.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
						newContatti.setIndirizzo(contattiToUpdate.getIndirizzo());
						newContatti.setPartitaIva(datiEnte.getDatiEnte().getPartitaIva() == null ? null
								: datiEnte.getDatiEnte().getPartitaIva());
						newContatti.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
						newContatti.setEmail(datiEnte.getDatiEnte().getEmail());
						newContatti.setTelefono(datiEnte.getDatiEnte().getTelefono());
						newContatti.setPec(datiEnte.getDatiEnte().getPec());
						newContatti.setUtenteOperazione(userInfo.getCodFisc());
						newContatti.setDataCreazione(dataAttuale);
						newContatti.setDataInizioValidita(datafinevalidita);
						newContatti.setGregRResponsabileContatti(newContattiResp);
						newContatti.setGregDComuni(datiEnteDao
								.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
						newContatti.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
						newContatti.setGregDTipoEnte(
								datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));
						datiEnteDao.saveDatiAnagrafici(newContatti);
					} else {
						respContattiToUpdate.setGregTResponsabileEnteGestore(respToUpdate);
						respContattiToUpdate.setCellulare(datiEnte.getDatiEnte().getResponsabileEnte().getCellulare());
						respContattiToUpdate.setTelefono(datiEnte.getDatiEnte().getResponsabileEnte().getTelefono());
						respContattiToUpdate.setEmail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
						respContattiToUpdate.setDataCreazione(dataAttuale);
						respContattiToUpdate.setDataInizioValidita(datafinevalidita);
						respContattiToUpdate.setUtenteOperazione(userInfo.getCodFisc());
						datiEnteDao.saveResponsabileContatti(respContattiToUpdate);
						contattiToUpdate.setGregTSchedeEntiGestori(schedaToUpdate);
						contattiToUpdate.setCodSchedaEnteGestore(contattiToUpdate.getCodSchedaEnteGestore());
						contattiToUpdate.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
						contattiToUpdate.setIndirizzo(contattiToUpdate.getIndirizzo());
						contattiToUpdate.setPartitaIva(datiEnte.getDatiEnte().getPartitaIva() == null ? null
								: datiEnte.getDatiEnte().getPartitaIva());
						contattiToUpdate.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
						contattiToUpdate.setEmail(datiEnte.getDatiEnte().getEmail());
						contattiToUpdate.setTelefono(datiEnte.getDatiEnte().getTelefono());
						contattiToUpdate.setPec(datiEnte.getDatiEnte().getPec());
						contattiToUpdate.setUtenteOperazione(userInfo.getCodFisc());
						contattiToUpdate.setDataCreazione(dataAttuale);
						contattiToUpdate.setDataInizioValidita(datafinevalidita);
						contattiToUpdate.setGregRResponsabileContatti(respContattiToUpdate);
						contattiToUpdate.setGregDComuni(datiEnteDao
								.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
						contattiToUpdate
								.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
						contattiToUpdate.setGregDTipoEnte(
								datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));
						datiEnteDao.saveDatiAnagrafici(contattiToUpdate);
					}
				}
			} else {
				// non esiste responsabile va creato responsabile contatti responsabile e
				// contatti ente
				GregTResponsabileEnteGestore newResp = new GregTResponsabileEnteGestore();
				newResp.setCodiceFiscale(datiEnte.getDatiEnte().getResponsabileEnte().getCodiceFiscale());
				newResp.setNome(datiEnte.getDatiEnte().getResponsabileEnte().getNome());
				newResp.setCognome(datiEnte.getDatiEnte().getResponsabileEnte().getCognome());
				newResp.setCodResponsabileEnteGestore(newResp.getNome() + " " + newResp.getCognome());
				newResp.setUtenteOperazione(userInfo.getCodFisc());
				newResp.setDataCreazione(dataAttuale);
				newResp.setDataModifica(dataAttuale);
				newResp = datiEnteDao.saveResponsabileEnte(newResp);
				newContattiResp.setGregTResponsabileEnteGestore(newResp);
				newContattiResp.setCellulare(datiEnte.getDatiEnte().getResponsabileEnte().getCellulare());
				newContattiResp.setTelefono(datiEnte.getDatiEnte().getResponsabileEnte().getTelefono());
				newContattiResp.setEmail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
				newContattiResp.setDataCreazione(dataAttuale);
				newContattiResp.setDataInizioValidita(datafinevalidita);
				newContattiResp.setUtenteOperazione(userInfo.getCodFisc());
				responsabilemail.seteMail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
				responsabilemail.setNome(datiEnte.getDatiEnte().getResponsabileEnte().getNome());
				responsabilemail.setCognome(datiEnte.getDatiEnte().getResponsabileEnte().getCognome());
				newContattiResp = datiEnteDao.saveResponsabileContatti(newContattiResp);
				if (!datiEnte.isSameData()) {
					contattiToUpdate.setDataFineValidita(datafine);
					datiEnteDao.saveDatiAnagrafici(contattiToUpdate);
					GregREnteGestoreContatti newContatti = new GregREnteGestoreContatti();

					newContatti.setGregTSchedeEntiGestori(schedaToUpdate);
					newContatti.setCodSchedaEnteGestore(contattiToUpdate.getCodSchedaEnteGestore());
					newContatti.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
					newContatti.setIndirizzo(contattiToUpdate.getIndirizzo());
					newContatti.setPartitaIva(datiEnte.getDatiEnte().getPartitaIva() == null ? null
							: datiEnte.getDatiEnte().getPartitaIva());
					newContatti.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
					newContatti.setEmail(datiEnte.getDatiEnte().getEmail());
					newContatti.setTelefono(datiEnte.getDatiEnte().getTelefono());
					newContatti.setPec(datiEnte.getDatiEnte().getPec());
					newContatti.setUtenteOperazione(userInfo.getCodFisc());
					newContatti.setDataCreazione(dataAttuale);
					newContatti.setDataInizioValidita(datafinevalidita);
					newContatti.setGregRResponsabileContatti(newContattiResp);

					newContatti.setGregDComuni(datiEnteDao
							.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
					newContatti.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
					newContatti.setGregDTipoEnte(
							datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));
					datiEnteDao.saveDatiAnagrafici(newContatti);
				} else {
					contattiToUpdate.setGregTSchedeEntiGestori(schedaToUpdate);
					contattiToUpdate.setCodSchedaEnteGestore(contattiToUpdate.getCodSchedaEnteGestore());
					contattiToUpdate.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
					contattiToUpdate.setIndirizzo(contattiToUpdate.getIndirizzo());
					contattiToUpdate.setPartitaIva(datiEnte.getDatiEnte().getPartitaIva() == null ? null
							: datiEnte.getDatiEnte().getPartitaIva());
					contattiToUpdate.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
					contattiToUpdate.setEmail(datiEnte.getDatiEnte().getEmail());
					contattiToUpdate.setTelefono(datiEnte.getDatiEnte().getTelefono());
					contattiToUpdate.setPec(datiEnte.getDatiEnte().getPec());
					contattiToUpdate.setUtenteOperazione(userInfo.getCodFisc());
					contattiToUpdate.setDataCreazione(dataAttuale);
					contattiToUpdate.setDataInizioValidita(datafinevalidita);
					contattiToUpdate.setGregRResponsabileContatti(newContattiResp);

					contattiToUpdate.setGregDComuni(datiEnteDao
							.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
					contattiToUpdate.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
					contattiToUpdate.setGregDTipoEnte(
							datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));
					datiEnteDao.saveDatiAnagrafici(contattiToUpdate);
				}
			}
		}

		if (datiEnte.isModificaEnte() && !datiEnte.isModificaResp()) {
			if (!datiEnte.isSameData()) {
			contattiToUpdate.setDataFineValidita(datafine);

			datiEnteDao.saveDatiAnagrafici(contattiToUpdate);
			GregREnteGestoreContatti newContatti = new GregREnteGestoreContatti();

			newContatti.setGregTSchedeEntiGestori(schedaToUpdate);
			newContatti.setCodSchedaEnteGestore(contattiToUpdate.getCodSchedaEnteGestore());
			newContatti.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
			newContatti.setIndirizzo(contattiToUpdate.getIndirizzo());
			newContatti.setPartitaIva(
					datiEnte.getDatiEnte().getPartitaIva() == null ? null : datiEnte.getDatiEnte().getPartitaIva());
			newContatti.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
			newContatti.setEmail(datiEnte.getDatiEnte().getEmail());
			newContatti.setTelefono(datiEnte.getDatiEnte().getTelefono());
			newContatti.setPec(datiEnte.getDatiEnte().getPec());
			newContatti.setUtenteOperazione(userInfo.getCodFisc());
			newContatti.setDataCreazione(dataAttuale);
			newContatti.setDataInizioValidita(datafinevalidita);
			newContatti.setGregRResponsabileContatti(respContattiToUpdate);
			newContatti.setGregDComuni(
					datiEnteDao.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
			newContatti.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
			newContatti.setGregDTipoEnte(
					datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));
			datiEnteDao.saveDatiAnagrafici(newContatti);
			} else {
				contattiToUpdate.setGregTSchedeEntiGestori(schedaToUpdate);
				contattiToUpdate.setCodSchedaEnteGestore(contattiToUpdate.getCodSchedaEnteGestore());
				contattiToUpdate.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
				contattiToUpdate.setIndirizzo(contattiToUpdate.getIndirizzo());
				contattiToUpdate.setPartitaIva(
						datiEnte.getDatiEnte().getPartitaIva() == null ? null : datiEnte.getDatiEnte().getPartitaIva());
				contattiToUpdate.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
				contattiToUpdate.setEmail(datiEnte.getDatiEnte().getEmail());
				contattiToUpdate.setTelefono(datiEnte.getDatiEnte().getTelefono());
				contattiToUpdate.setPec(datiEnte.getDatiEnte().getPec());
				contattiToUpdate.setUtenteOperazione(userInfo.getCodFisc());
				contattiToUpdate.setDataCreazione(dataAttuale);
				contattiToUpdate.setDataInizioValidita(datafinevalidita);
				contattiToUpdate.setGregRResponsabileContatti(respContattiToUpdate);
				contattiToUpdate.setGregDComuni(
						datiEnteDao.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
				contattiToUpdate.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
				contattiToUpdate.setGregDTipoEnte(
						datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));

				datiEnteDao.saveDatiAnagrafici(contattiToUpdate);
			}

		}

		if (datiEnte.isModificaComune()) {
			List<String> comuniAssegnati = datiEnteDao.findComuniAssegnati(
					datiEnte.getDatiEnte().getIdSchedaEntegestore(), datiEnte.getComuniAssociati());
			if (comuniAssegnati.size() > 0) {
				response.setId(HttpStatus.INTERNAL_SERVER_ERROR.toString());
				response.setDescrizione(
						listeService.getMessaggio(SharedConstants.ERROR_DATIANAGRAFICI02).getTestoMessaggio());
				return response;
			}
			// Comuni Associati
			List<GregRSchedeEntiGestoriComuni> comAssNow = findGregRComuniAnagraficaAssociati(
					datiEnte.getDatiEnte().getIdSchedaEntegestore());
			List<ModelComuniAssociati> comAssNew = datiEnte.getComuniAssociati();
			// Inserisco i nuovi Comuni Associati (quelli senza PK)
			for (ModelComuniAssociati comune : comAssNew) {
				if (comune.getPkIdSchedaComuneAssociato() == null) {
					GregDComuni nuovoComune = datiEnteDao.findComuneByIdNotDeleted(comune.getIdComune());
					GregRSchedeEntiGestoriComuni newComuneAssociato = new GregRSchedeEntiGestoriComuni();
					newComuneAssociato.setGregDComuni(nuovoComune);
					newComuneAssociato.setGregTSchedeEntiGestori(schedaToUpdate);
					newComuneAssociato.setUtenteOperazione(userInfo.getCodFisc());
					newComuneAssociato.setDataCreazione(dataAttuale);
					newComuneAssociato.setDataModifica(dataAttuale);
					newComuneAssociato.setDataInizioValidita(datafinevalidita);
					datiEnteDao.saveNewComuneAssociato(newComuneAssociato);
				}
			}
			// Effettuo operazioni su associazioni eventualmente modificate
			List<String> listPkComuniAss = new ArrayList<>();
			for (GregRSchedeEntiGestoriComuni com : comAssNow) {
				listPkComuniAss.add(com.getGregDComuni().getCodIstatComune());
			}
			List<String> listPkComuniNew = new ArrayList<>();
			for (ModelComuniAssociati com : comAssNew) {
				if (com.getCodiceIstat() != null) {
					listPkComuniNew.add(com.getCodiceIstat());
				}
			}
			for (String pkComAss : listPkComuniAss) {
				if (!listPkComuniNew.contains(pkComAss)) {
					GregRSchedeEntiGestoriComuni comAssToUpdate = comAssNow.stream()
							.filter(com -> com.getGregDComuni().getCodIstatComune() == pkComAss).findFirst()
							.orElse(null);
					if (comAssToUpdate != null) {
						comAssToUpdate.setDataModifica(dataAttuale);
						comAssToUpdate.setDataFineValidita(datafine);
						comAssToUpdate.setUtenteOperazione(userInfo.getCodFisc());
						datiEnteDao.updateComuneAssociato(comAssToUpdate);
					}
				}
			}
		}

		if (datiEnte.getProfilo() != null && datiEnte.getProfilo().getListaazioni().get("InviaEmail")[1]) {

			// Invio mail
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_ANAG_ENTE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(datiEnte.getDatiEnte().getEmail(),
						datiEnte.getDatiEnte().getDenominazione(), responsabilemail,
						SharedConstants.MAIL_MODIFICA_DATI_ANAG_ENTE);
				response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		response.setId(HttpStatus.OK.toString());
		String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio()
				.replace("oggettosalvo", "Ente");
		response.setDescrizione(message);
		return response;

	}

	public ModelDatiAnagrafici findSchedaAnagraficaStorico(Integer idScheda, String dataFineValidita)
			throws ParseException {
		Date d = new Date(Long.parseLong(dataFineValidita));

		ModelDatiAnagrafici lista = datiEnteDao.findSchedaAnagraficaStorico(idScheda, d);

		return lista;
	}

	public List<ModelComuniAssociati> findComuniAnagraficaAssociatiStorico(Integer idScheda, String dataFineValidita,
			String dataInizioValidita) {

		List<ModelComuniAssociati> lista = new ArrayList<ModelComuniAssociati>();
		Date dlong = new Date(Long.parseLong(dataFineValidita));
		if (Checker.isValorizzato(dataInizioValidita)) {
			Date d = new Date(Long.parseLong(dataFineValidita));
			Date di = new Date(Long.parseLong(dataInizioValidita));
			lista = datiEnteDao.findComuniAnagraficaEliminatiStorico(idScheda, Converter.getAnno(di),
					Converter.getAnno(d), dlong);
		} else {
			lista = datiEnteDao.findComuniAnagraficaAssociatiStorico(idScheda, dlong);
		}
		return lista;
	}

	public List<ModelMotivazioni> getMotivazioniChiusura() {

		List<ModelMotivazioni> lista = datiEnteDao.getMotivazioniChiusura();

		return lista;
	}

	public List<ModelCronogiaStato> getCronologiaStato(Integer idScheda) {

		List<ModelCronogiaStato> lista = datiEnteDao.getCronologiaStato(idScheda);

		return lista;
	}

	public GregREnteGestoreStatoEnte findLastStato(Integer idScheda) {

		GregREnteGestoreStatoEnte stato = datiEnteDao.findLastStato(idScheda);

		return stato;
	}

	public GregDMotivazione findMotivazioneByCod(String codMotivazione) {
		GregDMotivazione motivazione = datiEnteDao.findMotivazioneByCod(codMotivazione);
		return motivazione;
	}

	public GregDStatoEnte findStatoByCod(String codStato) {
		GregDStatoEnte stato = datiEnteDao.findStatoByCod(codStato);
		return stato;
	}

	public GenericResponseWarnErr closeEnte(ModelChiusura chiuso, UserInfo userInfo) throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(new Date().getTime());
		Timestamp dataChiusura = new Timestamp(chiuso.getDataChiusura().getTime());
		Timestamp dataChiusuraggprev = new Timestamp(
				Converter.aggiungiGiorniAData(chiuso.getDataChiusura(), -1).getTime());

		GregTSchedeEntiGestori schedaToUpdate = datiEnteDao.findScheda(chiuso.getIdScheda());
		GregDMotivazione motivazione = findMotivazioneByCod(chiuso.getMotivazione());
		GregDStatoEnte stato = findStatoByCod(SharedConstants.CHIUSO);
		GregREnteGestoreStatoEnte statoEnte = findLastStato(chiuso.getIdScheda());
		// Recupero i dati delle entity da aggiornare
		statoEnte.setDataFineValidita(dataChiusuraggprev);
		statoEnte.setDataModifica(dataAttuale);
		datiEnteDao.saveEnteStatoEnte(statoEnte);
		GregREnteGestoreStatoEnte newStato = new GregREnteGestoreStatoEnte();
		newStato.setDataCreazione(dataAttuale);
		newStato.setDataInizioValidita(dataChiusura);
		newStato.setDataModifica(dataAttuale);
		newStato.setGregDMotivazione(motivazione);
		newStato.setGregDStatoEnte(stato);
		newStato.setGregTSchedeEntiGestori(schedaToUpdate);
		newStato.setNotaPerEnte(chiuso.getNotaEnte());
		newStato.setNotaInterna(chiuso.getNotaInterna());
		newStato.setUtenteOperazione(userInfo.getCodFisc());
		datiEnteDao.saveEnteStatoEnte(newStato);
		ModelResponsabileEnte responsabilemail = new ModelResponsabileEnte();
		responsabilemail.setNome(chiuso.getNome());
		responsabilemail.setCognome(chiuso.getCognome());
		responsabilemail.seteMail(chiuso.getEmailResp());
		if (chiuso.getProfilo() != null && chiuso.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			// Invio mail
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_CHIUSURA_ENTE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(chiuso.getEmail(), chiuso.getDenominazione(),
						responsabilemail, SharedConstants.MAIL_CHIUSURA_ENTE);
				response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}

		response.setId(HttpStatus.OK.toString());
		String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio()
				.replace("oggettosalvo", "Ente");
		response.setDescrizione(message);
		return response;

	}

	public ModelCronogiaStato findLastStatoEnte(Integer idScheda) {

		ModelCronogiaStato stato = datiEnteDao.findLastStatoEnte(idScheda);

		return stato;
	}

	public GenericResponseWarnErr ripristinoEnte(ModelChiusura chiuso, UserInfo userInfo) {

		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(new Date().getTime());
		Timestamp dataChiusura = new Timestamp(chiuso.getDataChiusura().getTime());

		GregTSchedeEntiGestori schedaToUpdate = datiEnteDao.findScheda(chiuso.getIdScheda());
		GregDMotivazione motivazione = findMotivazioneByCod(chiuso.getMotivazione());
		GregDStatoEnte stato = findStatoByCod(SharedConstants.APERTO);
		GregREnteGestoreStatoEnte statoEnte = findLastStato(chiuso.getIdScheda());
		GregREnteGestoreStatoEnte lastAperto = findLastStatoAperto(chiuso.getIdScheda());
		// Recupero i dati delle entity da aggiornare
		statoEnte.setDataFineValidita(dataChiusura);
		statoEnte.setDataModifica(dataAttuale);
		datiEnteDao.saveEnteStatoEnte(statoEnte);
		GregREnteGestoreStatoEnte newStato = new GregREnteGestoreStatoEnte();
		newStato.setDataCreazione(dataAttuale);
		newStato.setDataInizioValidita(lastAperto.getDataInizioValidita());
		newStato.setDataModifica(dataAttuale);
		newStato.setGregDMotivazione(motivazione);
		newStato.setGregDStatoEnte(stato);
		newStato.setGregTSchedeEntiGestori(schedaToUpdate);
		newStato.setNotaPerEnte(chiuso.getNotaEnte());
		newStato.setNotaInterna(chiuso.getNotaInterna());
		newStato.setUtenteOperazione(userInfo.getCodFisc());
		datiEnteDao.saveEnteStatoEnte(newStato);

		ModelResponsabileEnte responsabilemail = new ModelResponsabileEnte();
		responsabilemail.setNome(chiuso.getNome());
		responsabilemail.setCognome(chiuso.getCognome());
		responsabilemail.seteMail(chiuso.getEmailResp());
		if (chiuso.getProfilo() != null && chiuso.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			// Invio mail
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_RIPRISTINO_ENTE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(chiuso.getEmail(), chiuso.getDenominazione(),
						responsabilemail, SharedConstants.MAIL_RIPRISTINO_ENTE);
				response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		response.setId(HttpStatus.OK.toString());
		String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio()
				.replace("oggettosalvo", "Ente");
		response.setDescrizione(message);
		return response;

	}

	public List<ModelMotivazioni> getMotivazioniRipristino() {

		List<ModelMotivazioni> lista = datiEnteDao.getMotivazioniRipristino();

		return lista;
	}

	public GregREnteGestoreStatoEnte findLastStatoAperto(Integer idScheda) {

		GregREnteGestoreStatoEnte stato = datiEnteDao.findLastStatoAperto(idScheda);

		return stato;
	}

	public GenericResponseWarnErr creaNuovoEnte(ModelDatiAnagraficiToSave datiEnte, UserInfo userInfo)
			throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataApertura = new Timestamp(datiEnte.getDataApertura().getTime());
		Timestamp dataAttuale = new Timestamp(new Date().getTime());
		// Recupero i dati delle entity da aggiornare
		GregTSchedeEntiGestori newScheda = new GregTSchedeEntiGestori();
		newScheda.setCodiceFiscale(datiEnte.getDatiEnte().getCodiceFiscale());
		GregTSchedeEntiGestori codiceFiscale = datiEnteDao
				.findSchedabyCodFisc(datiEnte.getDatiEnte().getCodiceFiscale());
		if (codiceFiscale != null) {
			response.setId(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			response.setDescrizione(listeService.getMessaggio(SharedConstants.ERROR_CREAENTE01).getTestoMessaggio());
			return response;
		}
		newScheda.setCodiceRegionale(datiEnte.getDatiEnte().getCodiceRegionale());
		GregTSchedeEntiGestori codiceRegionale = datiEnteDao
				.findSchedabyCodReg(datiEnte.getDatiEnte().getCodiceRegionale());
		if (codiceRegionale != null) {
			response.setId(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			response.setDescrizione(listeService.getMessaggio(SharedConstants.ERROR_CREAENTE01).getTestoMessaggio());
			return response;
		}

		newScheda.setDataCreazione(dataAttuale);
		newScheda.setDataModifica(dataAttuale);
		newScheda.setUtenteOperazione(userInfo.getCodFisc());
		newScheda = datiEnteDao.saveDatiEnte(newScheda);
		List<String> comuniAssegnati = datiEnteDao.findComuniAssegnati(newScheda.getIdSchedaEnteGestore(),
				datiEnte.getComuniAssociati());
		if (comuniAssegnati.size() > 0) {
			response.setId(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			response.setDescrizione(
					listeService.getMessaggio(SharedConstants.ERROR_DATIANAGRAFICI02).getTestoMessaggio());
			datiEnteDao.deleteSchedaEnteGestore(newScheda.getIdSchedaEnteGestore());
			return response;
		}
		GregTResponsabileEnteGestore respToUpdate = datiEnteDao
				.findResponsabileEnte(datiEnte.getDatiEnte().getResponsabileEnte().getCodiceFiscale());
		GregTResponsabileEnteGestore newResp = new GregTResponsabileEnteGestore();

		ModelResponsabileEnte responsabilemail = new ModelResponsabileEnte();
		responsabilemail.seteMail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
		responsabilemail.setNome(datiEnte.getDatiEnte().getResponsabileEnte().getNome());
		responsabilemail.setCognome(datiEnte.getDatiEnte().getResponsabileEnte().getCognome());
		GregRResponsabileContatti newContattiResp = new GregRResponsabileContatti();
		if (respToUpdate != null) {
			if (!respToUpdate.getNome().equalsIgnoreCase(datiEnte.getDatiEnte().getResponsabileEnte().getNome())
					|| !respToUpdate.getCognome()
							.equalsIgnoreCase(datiEnte.getDatiEnte().getResponsabileEnte().getCognome())) {

				response.setId(HttpStatus.INTERNAL_SERVER_ERROR.toString());
				response.setDescrizione(
						listeService.getMessaggio(SharedConstants.ERROR_DATIANAGRAFICI01).getTestoMessaggio());
				datiEnteDao.deleteSchedaEnteGestore(newScheda.getIdSchedaEnteGestore());
				return response;
			} else {
				respToUpdate.setDataModifica(dataAttuale);
				respToUpdate = datiEnteDao.saveResponsabileEnte(respToUpdate);
				GregRResponsabileContatti respContattiToUpdate = datiEnteDao
						.findResponsabileContatti(datiEnte.getDatiEnte().getResponsabileEnte().getCodiceFiscale());
				respContattiToUpdate.setDataFineValidita(dataAttuale);
				datiEnteDao.saveResponsabileContatti(respContattiToUpdate);
				newContattiResp.setGregTResponsabileEnteGestore(respToUpdate);
				newContattiResp.setCellulare(datiEnte.getDatiEnte().getResponsabileEnte().getCellulare());
				newContattiResp.setTelefono(datiEnte.getDatiEnte().getResponsabileEnte().getTelefono());
				newContattiResp.setEmail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
				newContattiResp.setDataCreazione(dataAttuale);
				newContattiResp.setDataInizioValidita(dataApertura);
				newContattiResp.setUtenteOperazione(userInfo.getCodFisc());
				newContattiResp = datiEnteDao.saveResponsabileContatti(newContattiResp);
			}
		} else {
			newResp.setCodiceFiscale(datiEnte.getDatiEnte().getResponsabileEnte().getCodiceFiscale());
			newResp.setNome(datiEnte.getDatiEnte().getResponsabileEnte().getNome());
			newResp.setCognome(datiEnte.getDatiEnte().getResponsabileEnte().getCognome());
			newResp.setCodResponsabileEnteGestore(newResp.getNome() + " " + newResp.getCognome());
			newResp.setUtenteOperazione(userInfo.getCodFisc());
			newResp.setDataCreazione(dataAttuale);
			newResp.setDataModifica(dataAttuale);
			newResp = datiEnteDao.saveResponsabileEnte(newResp);
			newContattiResp.setGregTResponsabileEnteGestore(newResp);
			newContattiResp.setCellulare(datiEnte.getDatiEnte().getResponsabileEnte().getCellulare());
			newContattiResp.setTelefono(datiEnte.getDatiEnte().getResponsabileEnte().getTelefono());
			newContattiResp.setEmail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
			newContattiResp.setDataCreazione(dataAttuale);
			newContattiResp.setDataInizioValidita(dataApertura);
			newContattiResp.setUtenteOperazione(userInfo.getCodFisc());
			responsabilemail.seteMail(datiEnte.getDatiEnte().getResponsabileEnte().geteMail());
			responsabilemail.setNome(datiEnte.getDatiEnte().getResponsabileEnte().getNome());
			responsabilemail.setCognome(datiEnte.getDatiEnte().getResponsabileEnte().getCognome());
			newContattiResp = datiEnteDao.saveResponsabileContatti(newContattiResp);
		}

		GregREnteGestoreContatti newContatti = new GregREnteGestoreContatti();

		newContatti.setGregTSchedeEntiGestori(newScheda);
		newContatti.setCodSchedaEnteGestore(datiEnte.getDatiEnte().getCodiceIstat());
		newContatti.setDenominazione(datiEnte.getDatiEnte().getDenominazione());
		newContatti.setPartitaIva(
				datiEnte.getDatiEnte().getPartitaIva() == null ? null : datiEnte.getDatiEnte().getPartitaIva());
		newContatti.setCodIstatEnte(datiEnte.getDatiEnte().getCodiceIstat());
		newContatti.setEmail(datiEnte.getDatiEnte().getEmail());
		newContatti.setTelefono(datiEnte.getDatiEnte().getTelefono());
		newContatti.setPec(datiEnte.getDatiEnte().getPec());
		newContatti.setUtenteOperazione(userInfo.getCodFisc());
		newContatti.setDataCreazione(dataAttuale);
		newContatti.setDataInizioValidita(dataApertura);
		newContatti.setGregRResponsabileContatti(newContattiResp);
		newContatti.setGregDComuni(
				datiEnteDao.findComuneByCodNotDeleted(datiEnte.getDatiEnte().getComune().getCodIstatComune()));
		newContatti.setGregDAsl(datiEnteDao.findAslbyCod(datiEnte.getDatiEnte().getAsl().getCodAsl()));
		newContatti
				.setGregDTipoEnte(datiEnteDao.findTipoEntebyCod(datiEnte.getDatiEnte().getTipoEnte().getCodTipoEnte()));
		datiEnteDao.saveDatiAnagrafici(newContatti);

		// Comuni Associati
		List<GregRSchedeEntiGestoriComuni> comAssNow = findGregRComuniAnagraficaAssociati(
				newScheda.getIdSchedaEnteGestore());
		List<ModelComuniAssociati> comAssNew = datiEnte.getComuniAssociati();
		// Inserisco i nuovi Comuni Associati (quelli senza PK)
		for (ModelComuniAssociati comune : comAssNew) {
			if (comune.getPkIdSchedaComuneAssociato() == null) {
				GregDComuni nuovoComune = datiEnteDao.findComuneByIdNotDeleted(comune.getIdComune());
				GregRSchedeEntiGestoriComuni newComuneAssociato = new GregRSchedeEntiGestoriComuni();
				newComuneAssociato.setGregDComuni(nuovoComune);
				newComuneAssociato.setGregTSchedeEntiGestori(newScheda);
				newComuneAssociato.setUtenteOperazione(userInfo.getCodFisc());
				newComuneAssociato.setDataCreazione(dataAttuale);
				newComuneAssociato.setDataModifica(dataAttuale);
				newComuneAssociato.setDataInizioValidita(dataApertura);
				datiEnteDao.saveNewComuneAssociato(newComuneAssociato);
			}
		}
		// Effettuo operazioni su associazioni eventualmente modificate
		List<String> listPkComuniAss = new ArrayList<>();
		for (GregRSchedeEntiGestoriComuni com : comAssNow) {
			listPkComuniAss.add(com.getGregDComuni().getCodIstatComune());
		}
		List<String> listPkComuniNew = new ArrayList<>();
		for (ModelComuniAssociati com : comAssNew) {
			if (com.getCodiceIstat() != null) {
				listPkComuniNew.add(com.getCodiceIstat());
			}
		}
		for (String pkComAss : listPkComuniAss) {
			if (!listPkComuniNew.contains(pkComAss)) {
				GregRSchedeEntiGestoriComuni comAssToUpdate = comAssNow.stream()
						.filter(com -> com.getGregDComuni().getCodIstatComune() == pkComAss).findFirst().orElse(null);
				if (comAssToUpdate != null) {
					comAssToUpdate.setDataModifica(dataAttuale);
					comAssToUpdate.setDataFineValidita(dataApertura);
					datiEnteDao.updateComuneAssociato(comAssToUpdate);
				}
			}
		}

		GregREnteGestoreStatoEnte statoEnte = new GregREnteGestoreStatoEnte();
		statoEnte.setDataCreazione(dataAttuale);
		statoEnte.setDataInizioValidita(dataApertura);
		statoEnte.setDataModifica(dataAttuale);
		statoEnte.setGregDMotivazione(null);
		statoEnte.setGregDStatoEnte(findStatoByCod(SharedConstants.APERTO));
		statoEnte.setGregTSchedeEntiGestori(newScheda);
		statoEnte.setUtenteOperazione(userInfo.getCodFisc());

		statoEnte = datiEnteDao.saveEnteStatoEnte(statoEnte);

		GregTLista lista = datiEnteDao.findListabyCod(datiEnte.getListaSelezionata().getCodLista());

		GregRListaEntiGestori listaEnte = new GregRListaEntiGestori();
		listaEnte.setGregTLista(lista);
		listaEnte.setGregTSchedeEntiGestori(newScheda);
		listaEnte.setUtenteOperazione(userInfo.getCodFisc());
		listaEnte.setDataCreazione(dataAttuale);
		listaEnte.setDataModifica(dataAttuale);
		listaEnte = datiEnteDao.updateListaEnte(listaEnte);

		if (datiEnte.getProfilo() != null && datiEnte.getProfilo().getListaazioni().get("InviaEmail")[1]) {

			// Invio mail
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_GENERA_NUOVO_ENTE_GESTORE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(datiEnte.getDatiEnte().getEmail(),
						datiEnte.getDatiEnte().getDenominazione(), responsabilemail,
						SharedConstants.MAIL_GENERA_NUOVO_ENTE_GESTORE);
				response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}
		response.setId(HttpStatus.OK.toString());
		String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio()
				.replace("oggettosalvo", "Ente");
		response.setDescrizione(message);
		response.setIdEnte(newScheda.getIdSchedaEnteGestore());
		return response;

	}

	public List<ModelComune> findProvinceComuniLiberi(String codregione, String dataValidita) {

		Date d = null;
		if (Checker.isValorizzato(dataValidita))
			d = Converter.getDataEnglish(dataValidita);

		return datiEnteDao.findProvicieComuniLiberi(codregione, d);

	}

	public List<ModelDatiAnagrafici> findSchedaEnteUnione(RicercaListaEntiDaunire stato) {

		List<ModelDatiAnagrafici> lista = datiEnteDao.findSchedaEnteApertoeChiuso(stato);

		return lista;
	}

	public Boolean unioneEnti(UnioneEnte stato, UserInfo userinfo) throws Exception {

		// Recupero entity interesse
		GregTSchedeEntiGestori enteSlave = datiEnteDao.findSchedaByCod(stato.getCodiceRegionaleEnteDaUnire());
		GregTSchedeEntiGestori enteDest = datiEnteDao.findSchedaByCod(stato.getCodiceRegionaleEnteDest());
		GregREnteGestoreContatti enteSlaveAnag = datiEnteDao.findLastContatti(enteSlave.getIdSchedaEnteGestore());
		GregREnteGestoreContatti enteDestAnag = datiEnteDao.findLastContatti(enteDest.getIdSchedaEnteGestore());
		boolean modificato = false;
		boolean modificatocomuni = false;
		// se non ci sono dati da mettere in ente da unire non fare nulla
		if (stato.getListacheckeds() != null) {
			for (int i = 0; i < stato.getListacheckeds().length; i++) {
				if (stato.getListacheckeds()[i] != null) {
					if (stato.getListacheckeds()[i].containsValue(true)
							&& !stato.getListacheckeds()[i].containsKey("comuni")) {
						modificato = true;
					} else if (stato.getListacheckeds()[i].containsValue(true)
							&& stato.getListacheckeds()[i].containsKey("comuni")) {
						modificatocomuni = true;
					}
				}
			}
			if (modificato) {
				enteDestAnag.setDataFineValidita(new Timestamp(stato.getDataMerge().getTime()));
				enteDestAnag.setUtenteOperazione(userinfo.getCodFisc());
				datiEnteDao.saveDatiAnagrafici(enteDestAnag);
				GregREnteGestoreContatti newAnagrafica = new GregREnteGestoreContatti();
				newAnagrafica.setCodSchedaEnteGestore(enteDestAnag.getCodSchedaEnteGestore());
				newAnagrafica.setDataCreazione(new Timestamp(new Date().getTime()));
				newAnagrafica.setDataFineValidita(null);
				newAnagrafica.setDataInizioValidita(new Timestamp(stato.getDataMerge().getTime()));
				newAnagrafica.setEmail2(enteDestAnag.getEmail2());
				newAnagrafica.setFax(enteDestAnag.getFax());
				newAnagrafica.setGregDAmbitiTerritoriali(enteDestAnag.getGregDAmbitiTerritoriali());
				newAnagrafica.setGregDTipoEnte(enteDestAnag.getGregDTipoEnte());
				newAnagrafica.setGregTPresidenteEnteGestore(enteDestAnag.getGregTPresidenteEnteGestore());
				newAnagrafica.setGregRResponsabileContatti(enteDestAnag.getGregRResponsabileContatti());
				newAnagrafica.setGregTSchedeEntiGestori(enteDestAnag.getGregTSchedeEntiGestori());
				newAnagrafica.setIndirizzo(enteDestAnag.getIndirizzo());
				newAnagrafica.setNote(enteDestAnag.getNote());
				newAnagrafica.setSitoweb(enteDestAnag.getSitoweb());
				newAnagrafica.setTelefono2(enteDestAnag.getTelefono2());
				newAnagrafica.setUtenteOperazione(userinfo.getCodFisc());
				newAnagrafica.setDenominazione(enteDestAnag.getDenominazione());
				newAnagrafica.setPartitaIva(enteDestAnag.getPartitaIva());
				newAnagrafica.setGregDComuni(enteDestAnag.getGregDComuni());
				newAnagrafica.setCodIstatEnte(enteDestAnag.getCodIstatEnte());
				newAnagrafica.setGregDAsl(enteDestAnag.getGregDAsl());
				newAnagrafica.setEmail(enteDestAnag.getEmail());
				newAnagrafica.setTelefono(enteDestAnag.getTelefono());
				newAnagrafica.setPec(enteDestAnag.getPec());
				for (int i = 0; i < stato.getListacheckeds().length; i++) {
					// denominazione
					if (stato.getListacheckeds()[i] != null) {
						if (stato.getListacheckeds()[i].containsKey("denominazione")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setDenominazione(enteSlaveAnag.getDenominazione());
							}
						} else if (stato.getListacheckeds()[i].containsKey("partitaIva")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setPartitaIva(enteSlaveAnag.getPartitaIva());
							}
						} else if (stato.getListacheckeds()[i].containsKey("comune")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setGregDComuni(enteSlaveAnag.getGregDComuni());
							}
						} else if (stato.getListacheckeds()[i].containsKey("codiceIstat")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setCodIstatEnte(enteSlaveAnag.getCodIstatEnte());
							}
						} else if (stato.getListacheckeds()[i].containsKey("asl")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setGregDAsl(enteSlaveAnag.getGregDAsl());
							}
						} else if (stato.getListacheckeds()[i].containsKey("email")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setEmail(enteSlaveAnag.getEmail());
							}
						} else if (stato.getListacheckeds()[i].containsKey("telefono")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setTelefono(enteSlaveAnag.getTelefono());
							}
						} else if (stato.getListacheckeds()[i].containsKey("pec")) {
							if (stato.getListacheckeds()[i].containsValue(true)) {
								newAnagrafica.setPec(enteSlaveAnag.getPec());
							}
						}
					}
				}
				datiEnteDao.saveDatiAnagrafici(newAnagrafica);
			}

			if (modificatocomuni) {
				// List<GregRSchedeEntiGestoriComuni>
				// comuniEnteSlave=findGregRComuniAnagraficaAssociati(enteSlave.getIdSchedaEnteGestore());
				List<GregRSchedeEntiGestoriComuni> comuniEnteSlaveForMerge = findGregRComuniAnagraficaAssociatiWithDataMerge(
						enteSlave.getIdSchedaEnteGestore(), stato.getDataMerge());

				for (GregRSchedeEntiGestoriComuni comuniSlaveDaUnire : comuniEnteSlaveForMerge) {
					GregRSchedeEntiGestoriComuni comuneDaInserire = new GregRSchedeEntiGestoriComuni();
					comuneDaInserire.setDataCancellazione(null);
					comuneDaInserire.setDataCreazione(new Timestamp(new Date().getTime()));
					// non nulla ma corrispondente alla fine esistente nel vecchio
					comuneDaInserire.setDataFineValidita(comuniSlaveDaUnire.getDataFineValidita());
					comuneDaInserire.setDataInizioValidita(new Timestamp(stato.getDataMerge().getTime()));
					comuneDaInserire.setDataModifica(new Timestamp(new Date().getTime()));
					comuneDaInserire.setGregDComuni(comuniSlaveDaUnire.getGregDComuni());
					// mettere ente di destinazione non ente dal comune
					comuneDaInserire.setGregTSchedeEntiGestori(enteDest);
					comuneDaInserire.setUtenteOperazione(userinfo.getCodFisc());
					datiEnteDao.updateComuneAssociato(comuneDaInserire);
					if (comuniSlaveDaUnire.getDataFineValidita() == null) {
						comuniSlaveDaUnire.setDataFineValidita(
								new Timestamp(Converter.aggiungiGiorniAData(stato.getDataMerge(), -1).getTime()));
					}
					comuniSlaveDaUnire.setDataModifica(new Timestamp(new Date().getTime()));
					comuniSlaveDaUnire.setUtenteOperazione(userinfo.getCodFisc());
					datiEnteDao.updateComuneAssociato(comuniSlaveDaUnire);
				}
			}
		}
		Timestamp dataChiusuraggprev = new Timestamp(Converter.aggiungiGiorniAData(stato.getDataMerge(), -1).getTime());
		// Chiudo l'ente slave
		GregREnteGestoreStatoEnte entedaChiudere = datiEnteDao.findLastStato(enteSlave.getIdSchedaEnteGestore());
		entedaChiudere.setDataFineValidita(dataChiusuraggprev);
		entedaChiudere.setDataModifica(new Timestamp(new Date().getTime()));
		entedaChiudere.setUtenteOperazione(userinfo.getCodFisc());
		datiEnteDao.saveEnteGestoreStatoEnte(entedaChiudere);
		// Inserisco nuovo record nella gregREnteGestoreStatoEnte
		GregREnteGestoreStatoEnte newStatoEnte = new GregREnteGestoreStatoEnte();
		newStatoEnte.setDataCancellazione(null);
		newStatoEnte.setDataCreazione(new Timestamp(new Date().getTime()));
		newStatoEnte.setDataInizioValidita(new Timestamp(stato.getDataMerge().getTime()));
		newStatoEnte.setDataFineValidita(null);
		newStatoEnte.setGregDMotivazione(findMotivazioneByCod(SharedConstants.MOTIVAZIONEUNIONEENTE));
		newStatoEnte.setDataModifica(new Timestamp(new Date().getTime()));
		newStatoEnte.setGregDStatoEnte(findStatoByCod(SharedConstants.CHIUSO));
		newStatoEnte.setGregTSchedeEntiGestori(enteSlave);
		newStatoEnte.setNotaInterna(null);
		newStatoEnte.setNotaPerEnte("Unione con ente " + stato.getDenominazioneEnteDest());
		newStatoEnte.setUtenteOperazione(userinfo.getCodFisc());
		datiEnteDao.saveEnteGestoreStatoEnte(newStatoEnte);
		// Insert in tabella di merge
		GregRMergeEnti merge = new GregRMergeEnti();
		merge.setDataCancellazione(null);
		merge.setDataCreazione(new Timestamp(new Date().getTime()));
		merge.setDataMerge(new Timestamp(stato.getDataMerge().getTime()));
		merge.setDataModifica(new Timestamp(new Date().getTime()));
		merge.setUtenteOperazione(userinfo.getCodFisc());
		merge.setGregTSchedeEntiGestori1(enteDest);
		merge.setGregTSchedeEntiGestori2(enteSlave);
		datiEnteDao.saveDatiEnteMerge(merge);
		return true;
	}

	public List<GregRSchedeEntiGestoriComuni> findGregRComuniAnagraficaAssociatiWithDataMerge(Integer idScheda,
			Date dataMerge) {

		List<GregRSchedeEntiGestoriComuni> lista = new ArrayList<GregRSchedeEntiGestoriComuni>();
		lista = datiEnteDao.findGregRComuniAssegnatiWithDataMerge(idScheda, dataMerge);

		return lista;
	}

	public ModelDettaglioPrestRegionale getPrestazioneRegionale(String codPrestRegionale, Integer annoGestione)
			throws Exception {
		ModelDettaglioPrestRegionale dettPrest = new ModelDettaglioPrestRegionale();
		GregTPrestazioniRegionali1 prestazione = datiEnteDao.findPrestazioneByCod(codPrestRegionale, annoGestione);
		dettPrest.setCodPrestRegionale(prestazione.getCodPrestReg1());
		dettPrest.setDesPrestRegionale(prestazione.getDesPrestReg1());
		dettPrest.setDesTipologia(prestazione.getGregDTipologia().getDesTipologia());
		dettPrest.setTipo(prestazione.getGregTPrestazioniRegionali1() != null
				? prestazione.getGregDTipologiaQuota() != null
						? prestazione.getGregDTipologiaQuota().getDescTipologiaQuota()
						: " "
				: prestazione.getGregDTipoStruttura() != null
						? prestazione.getGregDTipoStruttura().getDescTipoStruttura()
						: " ");
		dettPrest.setOrdinamento(prestazione.getOrdinamento());

		List<ModelMacroaggregati> macroaggregati = datiEnteDao.findMacroaggregatiByPrestRegionale(codPrestRegionale,
				annoGestione);
		dettPrest.setMacroaggregati(macroaggregati);
		List<ModelPrestUtenza> utenza = datiEnteDao.findUtenzeByPrestRegionale(codPrestRegionale, annoGestione);
		dettPrest.setTargetUtenzaPrestReg1(utenza);
		dettPrest.setNotaPrestazione(prestazione.getInformativa());
		List<ModelPrest1Prest2> prest1Prest2 = datiEnteDao.findPrest2ByPrest1(codPrestRegionale, annoGestione);
		dettPrest.setPrest1Prest2(prest1Prest2);
		dettPrest.setPrest1PrestMin(datiEnteDao.findPrestMinByPrest1(codPrestRegionale, annoGestione));
		List<ModelPrest1PrestCollegate> prestCollegate = new ArrayList<ModelPrest1PrestCollegate>();
		if (prestazione.getGregTPrestazioniRegionali1() != null) {
			GregTPrestazioniRegionali1 prestazionePadre = datiEnteDao
					.findPrestazioneByCod(prestazione.getGregTPrestazioniRegionali1().getCodPrestReg1(), annoGestione);
			ModelPrest1PrestCollegate prestazioneColl = new ModelPrest1PrestCollegate();
			prestazioneColl.setCodPrestRegionale(prestazionePadre.getCodPrestReg1());
			prestazioneColl.setDesPrestRegionale(prestazionePadre.getDesPrestReg1());
			prestazioneColl.setDesTipologia(prestazionePadre.getGregDTipologia().getDesTipologia());
			prestazioneColl.setTipo(prestazionePadre.getGregTPrestazioniRegionali1() != null
					? prestazionePadre.getGregDTipologiaQuota() != null
							? prestazionePadre.getGregDTipologiaQuota().getDescTipologiaQuota()
							: " "
					: prestazionePadre.getGregDTipoStruttura() != null
							? prestazionePadre.getGregDTipoStruttura().getDescTipoStruttura()
							: " ");
			prestCollegate.add(prestazioneColl);
		} else {
			List<GregTPrestazioniRegionali1> prestazioniFiglie = datiEnteDao
					.findPrestazioneFiglieByCod(prestazione.getCodPrestReg1(), annoGestione);
			for (GregTPrestazioniRegionali1 prestFiglia : prestazioniFiglie) {
				ModelPrest1PrestCollegate prestazioneColl = new ModelPrest1PrestCollegate();
				prestazioneColl.setCodPrestRegionale(prestFiglia.getCodPrestReg1());
				prestazioneColl.setDesPrestRegionale(prestFiglia.getDesPrestReg1());
				prestazioneColl.setDesTipologia(prestFiglia.getGregDTipologia().getDesTipologia());
				prestazioneColl.setTipo(prestFiglia.getGregTPrestazioniRegionali1() != null
						? prestFiglia.getGregDTipologiaQuota() != null
								? prestFiglia.getGregDTipologiaQuota().getDescTipologiaQuota()
								: " "
						: prestFiglia.getGregDTipoStruttura() != null
								? prestFiglia.getGregDTipoStruttura().getDescTipoStruttura()
								: " ");
				prestCollegate.add(prestazioneColl);
			}
		}
		dettPrest.setPrestazioniCollegate(prestCollegate);

		return dettPrest;
	}

	public String getDataInizioMax(Integer idScheda) {
		return datiEnteDao.findDataInizioMassima(idScheda);
	}
}
