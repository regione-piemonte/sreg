/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.ArchivioDatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.EntiGestoriAttiviDao;
import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ListaEntiAnno;
import it.csi.greg.gregsrv.dto.ModelComune;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelDatiEnte;
import it.csi.greg.gregsrv.dto.ModelDenominazioniComuniAssociati;
import it.csi.greg.gregsrv.dto.ModelEntiComuniAssociati;
import it.csi.greg.gregsrv.dto.ModelRicercaEntiGestoriAttivi;
import it.csi.greg.gregsrv.dto.ModelStatiEnte;
import it.csi.greg.gregsrv.dto.ModelStatoRendicontazione;
import it.csi.greg.gregsrv.dto.ModelStoricoEnte;
import it.csi.greg.gregsrv.dto.ModelTipoEnte;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ResultCreaAnno;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;

@Service("entiGestoriAttiviService")
public class EntiGestoriAttiviService {

	@Autowired
	protected EntiGestoriAttiviDao entiGestoriAttiviDao;
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ArchivioDatiRendicontazioneDao archivioDatiRendicontazioneDao;
	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
	
	private static String ENTI_GESTORI = "Enti gestori attivi";
	private static String FNPS = "Mod. FNPS";
	
	
	public List<ModelComune> findAllComuni(String codregione, String dataValidita) {

		List<GregDComuni> resultList = new ArrayList<GregDComuni>();
		Date d = null;
		if (Checker.isValorizzato(dataValidita))
			d = Converter.getDataEnglish(dataValidita);
		resultList = entiGestoriAttiviDao.findAllComuni(codregione, d);

		List<ModelComune> lista = new ArrayList<ModelComune>();

		for (GregDComuni comune : resultList) {
			ModelComune elemento = new ModelComune(comune);
			lista.add(elemento);
		}

		return lista;
	}

	public List<ModelStatoRendicontazione> findAllStatoRendicontazione() {

		List<GregDStatoRendicontazione> resultList = new ArrayList<GregDStatoRendicontazione>();

		resultList = entiGestoriAttiviDao.findAllStatoRendicontazione();

		List<ModelStatoRendicontazione> lista = new ArrayList<ModelStatoRendicontazione>();

		for (GregDStatoRendicontazione stato : resultList) {
			ModelStatoRendicontazione elemento = new ModelStatoRendicontazione(stato);
			lista.add(elemento);
		}

		return lista;
	}

	public List<ModelTipoEnte> findAllTipoEnte() {

		List<GregDTipoEnte> resultList = new ArrayList<GregDTipoEnte>();

		resultList = entiGestoriAttiviDao.findAllTipoEnte();

		List<ModelTipoEnte> lista = new ArrayList<ModelTipoEnte>();

		for (GregDTipoEnte ente : resultList) {
			ModelTipoEnte elemento = new ModelTipoEnte(ente);
			lista.add(elemento);
		}

		return lista;
	}

	public List<ModelDatiEnte> findAllDenominazioni() {

		List<GregTSchedeEntiGestori> resultList = new ArrayList<GregTSchedeEntiGestori>();

		resultList = entiGestoriAttiviDao.findAllDenominazioni();

		List<ModelDatiEnte> lista = new ArrayList<ModelDatiEnte>();

		for (GregTSchedeEntiGestori ente : resultList) {
			ModelDatiEnte elemento = new ModelDatiEnte(ente);
			lista.add(elemento);
		}

		return lista;
	}

	public List<ModelDenominazioniComuniAssociati> getListaDenominazioniWithComuniAssociati() {

		List<ModelDatiEnte> listaDenominazioni = findAllDenominazioni();
		List<Object> resultList = entiGestoriAttiviDao.getListaDenominazioniWithComuniAssociati();
		List<ModelEntiComuniAssociati> resultListToModel = new ArrayList<ModelEntiComuniAssociati>();
		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			resultListToModel.add(new ModelEntiComuniAssociati(obj));
		}

		List<ModelDenominazioniComuniAssociati> listaDenomComAss = new ArrayList<ModelDenominazioniComuniAssociati>();

		for (ModelDatiEnte ente : listaDenominazioni) {
			ModelDenominazioniComuniAssociati model = new ModelDenominazioniComuniAssociati();
			model.setIdSchedaEnteGestore(ente.getIdSchedaEntegestore());
			model.setDenominazione(ente.getDenominazione());
			model.setComuniAssociati(new ArrayList<Integer>());
			for (ModelEntiComuniAssociati scheda : resultListToModel) {
				if (ente.getIdSchedaEntegestore().equals(scheda.getIdSchedaEnteGestore())) {
					model.getComuniAssociati().add(scheda.getIdComune());
				}
			}
			if (model.getComuniAssociati().size() > 0) {
				listaDenomComAss.add(model);
			}
		}

		return listaDenomComAss;
	}

	public List<ModelRicercaEntiGestoriAttivi> findSchedeEntiGestori(RicercaEntiGestori ricerca) {

		List<Object> resultList = new ArrayList<Object>();
		List<ModelRicercaEntiGestoriAttivi> lista = new ArrayList<ModelRicercaEntiGestoriAttivi>();

		if ((ricerca.getStatoRendicontazione() != null && !ricerca.getStatoRendicontazione().equals(""))
				|| Checker.isValorizzato(ricerca.getDenominazioneEnte())
				|| (ricerca.getComune() != null && !ricerca.getComune().equals(""))
				|| (ricerca.getTipoEnte() != null && !ricerca.getTipoEnte().equals(""))
				|| (ricerca.getStatoEnte() != null && !ricerca.getStatoEnte().equals(""))
				|| (ricerca.getAnnoEsercizio() != null && !ricerca.getAnnoEsercizio().equals(""))) {
			resultList = entiGestoriAttiviDao.findSchedeEntiGestoriByValue(ricerca);
		}

		else {
			resultList = entiGestoriAttiviDao.findSchedeEntiGestori(ricerca);
		}

		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			lista.add(new ModelRicercaEntiGestoriAttivi(obj));
		}

		if ((ricerca.getStatoRendicontazione() != null && !ricerca.getStatoRendicontazione().equals(""))
				|| (ricerca.getAnnoEsercizio() != null && !ricerca.getAnnoEsercizio().equals(""))) {
			for (ModelRicercaEntiGestoriAttivi ente : lista) {
				ente.setRendicontazioni(entiGestoriAttiviDao
						.findRendicontazioniApertebyIdSchedaAndValue(ente.getIdSchedaEnteGestore(), ricerca));
			}
		} else {
			for (ModelRicercaEntiGestoriAttivi ente : lista) {
				ente.setRendicontazioni(
						entiGestoriAttiviDao.findAllRendicontazioniApertebyIdScheda(ente.getIdSchedaEnteGestore()));
			}
		}

		return lista;
	}

	public List<ModelCronologiaEnte> findCronologiaEnte(Integer idRendicontazione) {

		List<GregTCronologia> resultList = new ArrayList<GregTCronologia>();

		resultList = entiGestoriAttiviDao.findCronologiaEnte(idRendicontazione);

		List<ModelCronologiaEnte> lista = new ArrayList<ModelCronologiaEnte>();

		for (GregTCronologia cronologia : resultList) {
			ModelCronologiaEnte elemento = new ModelCronologiaEnte(cronologia);
			lista.add(elemento);
		}

		return lista;
	}

	public List<ModelStoricoEnte> findStoricoEnte(Integer idScheda) {

		List<ModelStoricoEnte> lista = new ArrayList<ModelStoricoEnte>();

		lista = entiGestoriAttiviDao.findStoricoEnte(idScheda);

		return lista;
	}

	// public List<UserInfo> findUtenteAccessoAzione(String codFiscale){
	// ArrayList<Integer> elencoenti = new ArrayList<>(new HashSet<>());
	// ArrayList<Object> entiresult =
	// entiGestoriAttiviDao.findUtenteAccesso(codFiscale);
	// ArrayList<UserInfo> acessolista = new ArrayList<UserInfo>();
	// Iterator itr = entiresult.iterator();
	// while(itr.hasNext()){
	// UserInfo enteutente = new UserInfo();
	// Object[] obj = (Object[]) itr.next();
	// enteutente.setCodProfilo(String.valueOf(obj[0]));
	// enteutente.setCodFisc(String.valueOf(obj[1]));
	// enteutente.setCognome(String.valueOf(obj[2]));
	// enteutente.setNome(String.valueOf(obj[3]));
	// enteutente.setEmail(String.valueOf(obj[4]));
	// elencoenti.add(Integer.parseInt(String.valueOf(obj[5])));
	// enteutente.setCodAzione(String.valueOf(obj[6]));
	// //superuser
	// if
	// (String.valueOf(obj[0]).equalsIgnoreCase(SharedConstants.OPERATORE_REGIONALE)
	// || String.valueOf(obj[0]).equalsIgnoreCase(SharedConstants.SUPER_USER))
	// enteutente.setRuolo("SU");
	// else
	// enteutente.setRuolo("ENTE");
	// enteutente.setIdEnte(elencoenti);
	// acessolista.add(enteutente);
	// }
	//
	// return acessolista;
	// }

	// public List<UserInfo> findUtenteAccesso(String codFiscale){
	// ArrayList<Integer> elencoenti = new ArrayList<>(new HashSet<>());
	// ArrayList<Object> entiresult =
	// entiGestoriAttiviDao.findUtenteAccesso(codFiscale);
	// ArrayList<UserInfo> acessolista = new ArrayList<>(new HashSet<>());
	// Iterator itr = entiresult.iterator();
	// while(itr.hasNext()){
	// UserInfo enteutente = new UserInfo();
	// Object[] obj = (Object[]) itr.next();
	// enteutente.setCodProfilo(null);
	// enteutente.setCodFisc(String.valueOf(obj[1]));
	// enteutente.setCognome(String.valueOf(obj[2]));
	// enteutente.setNome(String.valueOf(obj[3]));
	// enteutente.setEmail(String.valueOf(obj[4]));
	// elencoenti.add(Integer.parseInt(String.valueOf(obj[5])));
	// enteutente.setCodAzione(String.valueOf(obj[6]));
	// //superuser
	// if
	// (String.valueOf(obj[0]).equalsIgnoreCase(SharedConstants.OPERATORE_REGIONALE)
	// || String.valueOf(obj[0]).equalsIgnoreCase(SharedConstants.SUPER_USER))
	// enteutente.setRuolo("SU");
	// else
	// enteutente.setRuolo("ENTE");
	// enteutente.setIdEnte(elencoenti);
	// acessolista.add(enteutente);
	// }
	//
	// return acessolista;
	// }

	@SuppressWarnings("rawtypes")
	public Map<Integer, String> getElencoEntiProfili(String codFiscale) {
		Map<Integer, String> elencoenti = new HashMap<Integer, String>();
		ArrayList<Object> entiresult = entiGestoriAttiviDao.findUtenteProfilo(codFiscale);
		Iterator itr = entiresult.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			if (Checker.isValorizzato(String.valueOf(obj[0])))
				elencoenti.put(Integer.parseInt(String.valueOf(obj[0])), String.valueOf(obj[1]));
			else
				elencoenti.put(0, String.valueOf(obj[1]));
		}
		return elencoenti;
	}

//	public ArrayList<String> getElencoEntiAzioni(String codFiscale,Integer idente,String ruolo){
//		ArrayList<String> elencoazioni = entiGestoriAttiviDao.getElencoAzioniEnte(codFiscale,idente,ruolo);
//		return elencoazioni;
//	}

//	public UserInfo getAccessoUtente(UserInfo utente){
//		Map<Integer,String> elencoenti = getElencoEntiProfili(utente.getCodFisc());
//		utente.setEnteprofilo(elencoenti);
//		return utente;
//	}

	public UserInfo getAccessoUtente(UserInfo utente) {
		return entiGestoriAttiviDao.findUtenteProfiloLista(utente.getCodFisc());
	}

//	public List<ModelRicercaEntiGestoriAttivi> findSchedeMultiEntiGestori(ArrayList<Integer> listaenti) {
//		
//		List<GregTSchedeEntiGestori> resultList = new ArrayList<GregTSchedeEntiGestori>();
//		resultList = entiGestoriAttiviDao.findSchedeMultiEntiGestori(listaenti);				    
//	    
//	    List<ModelRicercaEntiGestoriAttivi> lista = new ArrayList<ModelRicercaEntiGestoriAttivi>();
//	    
//	    for(GregTSchedeEntiGestori scheda : resultList) {
//	    	ModelRicercaEntiGestoriAttivi elemento = new ModelRicercaEntiGestoriAttivi(scheda);
//			lista.add(elemento);
//		}
//	  
//	  return lista;
//	}

//	public ModelRicercaEntiGestoriAttivi findSchedaEnteGestore(Integer idEnte) {
//		GregTSchedeEntiGestori result= datiEnteService.getSchedaEnte(idEnte);				    
//		return new ModelRicercaEntiGestoriAttivi(result);
//	}
//	
	public List<ModelStatiEnte> findAllStatoEnte() {

		List<GregDStatoEnte> resultList = new ArrayList<GregDStatoEnte>();

		resultList = entiGestoriAttiviDao.findAllStatoEnte();

		List<ModelStatiEnte> lista = new ArrayList<ModelStatiEnte>();

		for (GregDStatoEnte stato : resultList) {
			ModelStatiEnte elemento = new ModelStatiEnte(stato);
			lista.add(elemento);
		}

		return lista;
	}

	public List<Integer> findAllAnnoEsercizio() {

		List<Integer> resultList = new ArrayList<Integer>();

		resultList = entiGestoriAttiviDao.findAllAnnoEsercizio();

		return resultList;
	}

	public GenericResponseWarnErr creaNuovoAnno(ListaEntiAnno entiAnno, UserInfo userInfo) {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setErrors(new ArrayList<String>());
		response.setOk(new ArrayList<String>());
		String testo = "";

		for (ModelRicercaEntiGestoriAttivi ente : entiAnno.getEnti()) {
			testo += entiAnno.getAnno() + "," + ente.getIdSchedaEnteGestore() + "," + userInfo.getCodFisc() + ";";
		}

		List<ResultCreaAnno> resultList = new ArrayList<ResultCreaAnno>();

		resultList = entiGestoriAttiviDao.creaNuovoAnno(testo);
		String entiOk = "";
		int ok = 0;
		String output = "";
		for (ResultCreaAnno t : resultList) {
			output += t.getAnno() + "," + t.getIdScheda() + "," + t.getCf() + "," + t.getEsito() + ","
					+ (t.getErrore() != null ? t.getErrore() : "");
			String denominazione = null;
			for (ModelRicercaEntiGestoriAttivi ente : entiAnno.getEnti()) {
				if (t.getIdScheda().equals(ente.getIdSchedaEnteGestore())) {
					denominazione = ente.getDenominazione();
				}
			}
			if (t.getEsito().equals("ok")) {
				entiOk += denominazione + ", ";
				ok++;
				if (entiAnno.getProfilo() != null && entiAnno.getProfilo().getListaazioni().get("InviaEmail")[1]) {
					ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(t.getIdScheda());
					// Invio Mail a EG e ResponsabileEnte
					boolean trovataemail = mailService
							.verificaMailAzione(SharedConstants.MAIL_GENERA_NUOVO_ANNO_CONTABILE);
					if (trovataemail) {
						EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
								ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
								SharedConstants.MAIL_GENERA_NUOVO_ANNO_CONTABILE);
					}
				}
			} else {
				response.getErrors().add(listeService.getMessaggio(SharedConstants.CREANUOVOANNO_ERRORE)
						.getTestoMessaggio().replace("ENTE", denominazione).replace("MOTIVO", t.getErrore()));
			}
		}

		if (ok > 0) {
			response.getOk().add(listeService.getMessaggio(SharedConstants.CREANUOVOANNO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk).replace("ANNO", entiAnno.getAnno().toString()));
		}
		response.setId("Ok");
		response.setDescrizione(output);
		return response;
	}

	public GenericResponseWarnErr concludiRendicontazione(List<ModelRicercaEntiGestoriAttivi> enti, UserInfo userInfo) {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		GregDStatoRendicontazione stato = archivioDatiRendicontazioneDao.findStatoRendicontazioneConclusa();
		String newNotaEnte = "";

		String statoOld = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA).getDesStatoRendicontazione().toUpperCase(); 
		String statoNew = stato.getDesStatoRendicontazione().toUpperCase();
		if (!statoOld.equals(statoNew)) {
			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
					.getTestoMessaggio().replace("OPERAZIONE", "CONCLUDI RENDICONTAZIONE").replace("STATOOLD", "'" + statoOld + "'")
					.replace("STATONEW", "'" + statoNew + "'");
		}

		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		String entiOk = "<ul>";
		String anno = null;
		for (ModelRicercaEntiGestoriAttivi ente : enti) {
			GregTRendicontazioneEnte rend = datiRendicontazioneService
					.getRendicontazione(ente.getRendicontazioni().get(0).getIdRendicontazioneEnte());
			rend.setGregDStatoRendicontazione(stato);
			rend.setUtenteOperazione(userInfo.getCodFisc());
			rend.setDataModifica(dataAttuale);
			// Recupero eventuale ultima cronologia inserita
			if (Checker.isValorizzato(ente.getNotaEnte())) {
//					newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + notaEnte : notaEnte;
				newNotaEnte = ente.getNotaEnte();
			}
			String modello = "";
			if (enti.size() > 1) {
				modello = ENTI_GESTORI;
			} else {
				modello = FNPS;
			}
			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(ente.getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rend);
				cronologia.setGregDStatoRendicontazione(rend.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello(modello);
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(ente.getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataAttuale);
				cronologia.setDataCreazione(dataAttuale);
				cronologia.setDataModifica(dataAttuale);
				datiRendicontazioneService.insertCronologia(cronologia);
			}
			
			rend = entiGestoriAttiviDao.saveRendicontazione(rend);
			anno = rend.getAnnoGestione().toString();
			if (enti.size() > 1) {
				entiOk += "<li>" + ente.getDenominazione() + ", </li>";
			} else {
				entiOk += ente.getRendicontazioni().get(0).getAnnoEsercizio()+ " dell'ente " + ente.getDenominazione();
			}

		}
		if (enti.size() > 1) {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.CONCLUDI_MULTIPLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk).replace("ANNO", anno));
		} else {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.CONCLUDI_SINGOLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk));
		}
		response.setId("Ok");
		return response;
	}
	
	public GenericResponseWarnErr ripristinaRendicontazione(List<ModelRicercaEntiGestoriAttivi> enti, UserInfo userInfo) {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		
		String newNotaEnte = "";
		
		String statoOld = archivioDatiRendicontazioneDao.findStatoRendicontazioneConclusa().getDesStatoRendicontazione().toUpperCase();
		String statoNew = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA).getDesStatoRendicontazione().toUpperCase();
		if (!statoOld.equals(statoNew)) {
			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
					.getTestoMessaggio().replace("OPERAZIONE", "RIAPRI FNPS").replace("STATOOLD", "'" + statoOld + "'")
					.replace("STATONEW", "'" + statoNew + "'");
		}
		
		String entiOk = "<ul>";
		String anno = null;
		for (ModelRicercaEntiGestoriAttivi ente : enti) {
			GregTRendicontazioneEnte rend = datiRendicontazioneService
					.getRendicontazione(ente.getRendicontazioni().get(0).getIdRendicontazioneEnte());
			GregDStatoRendicontazione stato = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA); 
			rend.setGregDStatoRendicontazione(stato);
			rend.setUtenteOperazione(userInfo.getCodFisc());
			rend.setDataModifica(dataAttuale);
			
			if (Checker.isValorizzato(ente.getNotaEnte())) {
//					newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + notaEnte : notaEnte;
				newNotaEnte = ente.getNotaEnte();
			}
			String modello = "";
			if (enti.size() > 1) {
				modello = ENTI_GESTORI;
			} else {
				modello = FNPS;
			}
			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(ente.getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rend);
				cronologia.setGregDStatoRendicontazione(rend.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello(modello);
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(ente.getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataAttuale);
				cronologia.setDataCreazione(dataAttuale);
				cronologia.setDataModifica(dataAttuale);
				datiRendicontazioneService.insertCronologia(cronologia);
			}
			
			anno = rend.getAnnoGestione().toString();
			rend = entiGestoriAttiviDao.saveRendicontazione(rend);
			if (enti.size() > 1) {
				entiOk += "<li>" + ente.getDenominazione() + ", </li>";
			} else {
				entiOk += ente.getRendicontazioni().get(0).getAnnoEsercizio()+ " dell'ente " +ente.getDenominazione();
			}

		}
		if (enti.size() > 1) {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.RIAPRI_MULTIPLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk).replace("ANNO", anno));
		} else {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.RIAPRI_SINGOLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk));
		}
		response.setId("Ok");
		return response;
	}
	
	public GenericResponseWarnErr ripristinaCompilazione(List<ModelRicercaEntiGestoriAttivi> enti, UserInfo userInfo) {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		
		String newNotaEnte = "";
		
		String statoOld = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA).getDesStatoRendicontazione().toUpperCase();
		String statoNew = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II).getDesStatoRendicontazione().toUpperCase();
		if (!statoOld.equals(statoNew)) {
			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
					.getTestoMessaggio().replace("OPERAZIONE", "RIAPRI RENDICONTAZIONE").replace("STATOOLD", "'" + statoOld + "'")
					.replace("STATONEW", "'" + statoNew + "'");
		}
		
		String entiOk = "<ul>";
		String anno = null;
		for (ModelRicercaEntiGestoriAttivi ente : enti) {
			GregTRendicontazioneEnte rend = datiRendicontazioneService
					.getRendicontazione(ente.getRendicontazioni().get(0).getIdRendicontazioneEnte());
			GregDStatoRendicontazione stato = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II);
			rend.setGregDStatoRendicontazione(stato);
			rend.setUtenteOperazione(userInfo.getCodFisc());
			rend.setDataModifica(dataAttuale);
			
			if (Checker.isValorizzato(ente.getNotaEnte())) {
//				newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + notaEnte : notaEnte;
				newNotaEnte = ente.getNotaEnte();
			}
			String modello = "";
			if (enti.size() > 1) {
				modello = ENTI_GESTORI;
			} else {
				modello = FNPS;
			}
			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(ente.getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rend);
				cronologia.setGregDStatoRendicontazione(rend.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello(modello);
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(ente.getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataAttuale);
				cronologia.setDataCreazione(dataAttuale);
				cronologia.setDataModifica(dataAttuale);
				datiRendicontazioneService.insertCronologia(cronologia);
			}
			anno = rend.getAnnoGestione().toString();
			rend = entiGestoriAttiviDao.saveRendicontazione(rend);
			if (enti.size() > 1) {
				entiOk += "<li>" + ente.getDenominazione() + ", </li>";
			} else {
				entiOk += ente.getRendicontazioni().get(0).getAnnoEsercizio()+ " dell'ente " +ente.getDenominazione();
			}

		}
		if (enti.size() > 1) {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.RIAPRI_COMPILAZIONE_MULTIPLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk).replace("ANNO", anno));
		} else {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.RIAPRI_COMPILAZIONE_SINGOLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk));
		}
		response.setId("Ok");
		return response;
	}
	
	public GenericResponseWarnErr storicizzaMultiplo(List<ModelRicercaEntiGestoriAttivi> enti, UserInfo userInfo) {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		
		String newNotaEnte = "";
		
		String statoOld = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA).getDesStatoRendicontazione().toUpperCase();
		String statoNew = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA).getDesStatoRendicontazione().toUpperCase();
		if (!statoOld.equals(statoNew)) {
			newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
					.getTestoMessaggio().replace("OPERAZIONE", "STORICIZZA RENDICONTAZIONE").replace("STATOOLD", "'" + statoOld + "'")
					.replace("STATONEW", "'" + statoNew + "'");
		}
		
		String entiOk = "<ul>";
		String anno = null;
		for (ModelRicercaEntiGestoriAttivi ente : enti) {
			GregTRendicontazioneEnte rend = datiRendicontazioneService
					.getRendicontazione(ente.getRendicontazioni().get(0).getIdRendicontazioneEnte());
			GregDStatoRendicontazione stato = datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA);
			rend.setGregDStatoRendicontazione(stato);
			rend.setUtenteOperazione(userInfo.getCodFisc());
			rend.setDataModifica(dataAttuale);
			
			if (Checker.isValorizzato(ente.getNotaEnte())) {
//				newNotaEnte = Checker.isValorizzato(newNotaEnte)? newNotaEnte + " " + notaEnte : notaEnte;
				newNotaEnte = ente.getNotaEnte();
			}
			String modello = "";
			if (enti.size() > 1) {
				modello = ENTI_GESTORI;
			} else {
				modello = FNPS;
			}
			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(ente.getNotaInterna())) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rend);
				cronologia.setGregDStatoRendicontazione(rend.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello(modello);
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(ente.getNotaInterna());
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataAttuale);
				cronologia.setDataCreazione(dataAttuale);
				cronologia.setDataModifica(dataAttuale);
				datiRendicontazioneService.insertCronologia(cronologia);
			}
			anno = rend.getAnnoGestione().toString();
			rend = entiGestoriAttiviDao.saveRendicontazione(rend);
			if (enti.size() > 1) {
				entiOk += "<li>" + ente.getDenominazione() + ", </li>";
			} else {
				entiOk += ente.getRendicontazioni().get(0).getAnnoEsercizio()+ " dell'ente " +ente.getDenominazione();
			}

		}
		if (enti.size() > 1) {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.STORICIZZA_MULTIPLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk).replace("ANNO", anno));
		} else {
			response.setDescrizione(listeService.getMessaggio(SharedConstants.STORICIZZA_SINGOLO_OK).getTestoMessaggio()
					.replace("ENTI", entiOk));
		}
		response.setId("Ok");
		return response;
	}
}
