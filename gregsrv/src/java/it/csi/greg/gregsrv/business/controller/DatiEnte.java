/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.DatiEnteService;
import it.csi.greg.gregsrv.business.services.EntiGestoriAttiviService;
import it.csi.greg.gregsrv.dto.ModelAllegatiAssociati;
import it.csi.greg.gregsrv.dto.ModelChiusura;
import it.csi.greg.gregsrv.dto.UnioneEnte;
import it.csi.greg.gregsrv.dto.ModelComune;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelAbilitazioni;
import it.csi.greg.gregsrv.dto.ModelComuniAssociati;
import it.csi.greg.gregsrv.dto.ModelCronogiaStato;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelDatiAnagrafici;
import it.csi.greg.gregsrv.dto.ModelDatiAnagraficiToSave;
import it.csi.greg.gregsrv.dto.ModelDatiAsl;
import it.csi.greg.gregsrv.dto.ModelDatiEnte;
import it.csi.greg.gregsrv.dto.ModelDatiEnteRendicontazione;
import it.csi.greg.gregsrv.dto.ModelDatiEnteToSave;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestRegionale;
import it.csi.greg.gregsrv.dto.ModelMotivazioni;
import it.csi.greg.gregsrv.dto.ModelPrestazioni;
import it.csi.greg.gregsrv.dto.ModelPrestazioniAssociate;
import it.csi.greg.gregsrv.dto.ModelProvincia;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModB;
import it.csi.greg.gregsrv.dto.ModelStatiEnte;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.RicercaListaEntiDaunire;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.business.entity.GregTAllegatiRendicontazione;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.DATI_ENTE)
public class DatiEnte {
	
	/**Constants Mapping**/
	public static final String GET_PROVINCIA_SEDE_LEGALE = "/getProvinciaSedeLegale";//GET
	public static final String GET_PRESTAZIONI = "/getPrestazioni";//GET
	public static final String GET_SCHEDA_ENTE = "/getSchedaEnte";//GET
	public static final String GET_COMUNI_ASSOCIATI = "/getComuniAssociati";//GET
	public static final String GET_PRESTAZIONI_ASSOCIATE = "/getPrestazioniAssociate"; //GET
	public static final String SAVE_DATI_ENTE= "/saveDatiEnte";//POST
	public static final String GET_ASL = "/getAsl";//GET
	public static final String GET_ALLEGATI_ASSOCIATI = "/getAllegatiAssociati";//GET
	public static final String GET_ALLEGATO_TO_DOWNLOAD = "/getAllegatoToDownload";//GET
	public static final String GET_PRESTAZIONI_RES_SEMIRES = "/getPrestazioniResSemires";//GET
	public static final String GET_LISTA_PRESTAZIONI_VALORIZZATE_MOD_A = "/getListaPrestazioniValorizzateModA";//GET
	public static final String GET_LISTA_PRESTAZIONI_VALORIZZATE_MOD_B1 = "/getListaPrestazioniValorizzateModB1";//GET
	public static final String GET_LISTA_PRESTAZIONI_VALORIZZATE_MOD_C = "/getListaPrestazioniValorizzateModC";//GET
	public static final String GET_LISTA_COMUNI_VALORIZZATI_MOD_A1 = "/getListaComuniValorizzatiModA1";//GET
	public static final String GET_LISTA_COMUNI_VALORIZZATI_MOD_A2 = "/getListaComuniValorizzatiModA2";//GET
	public static final String GET_LISTA_COMUNI_VALORIZZATI_MOD_E = "/getListaComuniValorizzatiModE";//GET
	public static final String GET_DATI_ENTE_RENDICONTAZIONE = "/getDatiEnteRendicontazione";//GET
	public static final String GET_SCHEDA_ANAGRAFICA = "/getSchedaAnagrafica";//GET
	public static final String GET_COMUNI_ANAGRAFICA_ASSOCIATI = "/getComuniAnagraficaAssociati";//GET
	public static final String SAVE_DATI_ANAGRAFICA= "/saveDatiAnagrafici";//POST
	public static final String GET_SCHEDA_ANAGRAFICA_STORICO = "/getSchedaAnagraficaStorico";//GET
	public static final String GET_COMUNI_ANAGRAFICA_ASSOCIATI_STORICO = "/getComuniAnagraficaAssociatiStorico";//GET
	public static final String GET_MOTIVAZIONI_CHIUSURA = "/getMotivazioniChiusura";//GET
	public static final String GET_CRONOLOGIA_STATO = "/getCronologiaStato";//GET
	public static final String CLOSE_ENTE = "/closeEnte";//POST
	public static final String GET_LAST_STATO = "/getLastStato";//GET
	public static final String RIPRISTINO_ENTE = "/ripristinoEnte";//POST
	public static final String GET_MOTIVAZIONI_RIPRISTINO = "/getMotivazioniRipristino";//GET
	public static final String GET_ENTI_UNIONE="/getEntiunione"; //POST
	public static final String UNIONE="/unioneEnte"; //POST
	public static final String CREA_NUOVO_ENTE= "/creaNuovoEnte";//POST
	public static final String GET_PROVINCIA_COMUNE_LIBERO = "/getProvinciaComuneLibero";//GET
	public static final String GET_PRESTAZIONE_REGIONALE = "/getPrestazioneRegionale";//GET
	public static final String GET_DATA_INIZIO_MAX = "/getDataInizioMax";//GET


	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	/**Dependency - Injection**/
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected EntiGestoriAttiviService entiGestoriAttiviService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_PROVINCIA_SEDE_LEGALE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getProvinciaSedeLegale(@RequestParam(name = "dataValidita", required = true) String dataValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_PROVINCIA + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelProvincia> listaProvince = new ArrayList<ModelProvincia>();
			String codregione = listeService.getParametro(SharedConstants.CODREGIONE).getValtext();
			listaProvince = datiEnteService.findAllProvince(codregione,dataValidita);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_PROVINCIA + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelProvincia>>(listaProvince, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_PROVINCIA + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioni(@RequestParam(name = "anno", required = true) Integer anno) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_PRESTAZIONI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {	
			List<ModelPrestazioni> listaProvince = new ArrayList<ModelPrestazioni>();
			listaProvince = datiEnteService.findAllPrestazioniMadre(anno);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_PRESTAZIONI + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelPrestazioni>>(listaProvince, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_PRESTAZIONI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONI_RES_SEMIRES, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioniResSemires(@RequestParam(name = "codTipologia", required = true) String codTipologia, 
			@RequestParam(name = "codTipoStruttura", required = true) String codTipoStruttura, Integer anno) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_PRESTAZIONIRESSEMIRES + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {	
			List<ModelPrestazioni> listaProvince = new ArrayList<ModelPrestazioni>();
			listaProvince = datiEnteService.findPrestazioniResSemires(codTipologia,codTipoStruttura, anno);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_PRESTAZIONIRESSEMIRES + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelPrestazioni>>(listaProvince, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_PRESTAZIONIRESSEMIRES + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_SCHEDA_ENTE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSchedaEnte(@RequestParam(name = "idScheda", required = true) Integer idScheda,@RequestParam(name = "annoGestione", required = true) Integer annoGestione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelDatiEnte schedaEnteGestore = new ModelDatiEnte();
			schedaEnteGestore = datiEnteService.findSchedaEntexAnno(idScheda,annoGestione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelDatiEnte>(schedaEnteGestore, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_COMUNI_ASSOCIATI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getComuniAssociati(@RequestParam(name = "id", required = true) Integer id,@RequestParam(name = "annoGestione", required = true) Integer annoGestione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelComuniAssociati> listaComuniAssociati = new ArrayList<ModelComuniAssociati>();
			listaComuniAssociati = datiEnteService.findComuniAssociati(id,annoGestione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelComuniAssociati>>(listaComuniAssociati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONI_ASSOCIATE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioniAssociate(@RequestParam(name = "id", required = true) Integer id) {

		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_PRESTAZIONIASSOCIATE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelPrestazioniAssociate> listaPrestazioniAssociate = new ArrayList<ModelPrestazioniAssociate>();
			listaPrestazioniAssociate = datiEnteService.findPrestazioniAssociate(id);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_PRESTAZIONIASSOCIATE + " nella classe " + DatiEnte.class.getName());			
			return new ResponseEntity<List<ModelPrestazioniAssociate>>(listaPrestazioniAssociate, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_PRESTAZIONIASSOCIATE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_DATI_ENTE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveDatiEnte(@RequestBody ModelDatiEnteToSave datiEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			// Gestione Allegati
			List<GregTAllegatiRendicontazione> listAllegatiNew = new ArrayList<>();
			
			if(datiEnte.getFileIniziale() != null) {
				GregTAllegatiRendicontazione newAllegatoIniziale = new GregTAllegatiRendicontazione();
				byte[] fileInizialeBytes = Base64.getDecoder().decode(datiEnte.getFileIniziale().getFileBytes());
				newAllegatoIniziale.setFileAllegato(fileInizialeBytes);
				newAllegatoIniziale.setFileSize(datiEnte.getFileIniziale().getDimensione());
				newAllegatoIniziale.setNomeFile(datiEnte.getFileIniziale().getNomeFile());
				newAllegatoIniziale.setNoteFile(Checker.isValorizzato(datiEnte.getFileIniziale().getNoteFile())? datiEnte.getFileIniziale().getNoteFile() : null);
				newAllegatoIniziale.setTipoDocumentazione(SharedConstants.TRANCHEI);
				newAllegatoIniziale.setUtenteOperazione(userInfo.getCodFisc());
				listAllegatiNew.add(newAllegatoIniziale);
			}

			if(datiEnte.getFileFinale() != null) {
				GregTAllegatiRendicontazione newAllegatoFinale = new GregTAllegatiRendicontazione();
				byte[] fileFinaleBytes = Base64.getDecoder().decode(datiEnte.getFileFinale().getFileBytes());
				newAllegatoFinale.setFileAllegato(fileFinaleBytes);
				newAllegatoFinale.setFileSize(datiEnte.getFileFinale().getDimensione());
				newAllegatoFinale.setNomeFile(datiEnte.getFileFinale().getNomeFile());
				newAllegatoFinale.setNoteFile(Checker.isValorizzato(datiEnte.getFileFinale().getNoteFile())? datiEnte.getFileFinale().getNoteFile() : null);
				newAllegatoFinale.setTipoDocumentazione(SharedConstants.TRANCHEII);
				newAllegatoFinale.setUtenteOperazione(userInfo.getCodFisc());
				listAllegatiNew.add(newAllegatoFinale);
			}
			GenericResponseWarnErr response = datiEnteService.saveDatiEnte(datiEnte, listAllegatiNew, userInfo);
			
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	@RequestMapping(value= GET_ASL, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAsl(@RequestParam(name = "dataValidita", required = true) String dataValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETASL + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelDatiAsl> listaAsl = new ArrayList<ModelDatiAsl>();
			String codregione = listeService.getParametro(SharedConstants.CODREGIONE).getValtext();
			listaAsl = datiEnteService.findAllAsl(codregione,dataValidita);	
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETASL + " nella classe " + DatiEnte.class.getName());			
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelDatiAsl>>(listaAsl, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETASL + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_ALLEGATI_ASSOCIATI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAllegatiAssociati(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETALLEGATIASSOCIATI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			
			List<ModelAllegatiAssociati> listaAllegati = new ArrayList<ModelAllegatiAssociati>();
			listaAllegati = datiEnteService.findAllegatiAssociati(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETALLEGATIASSOCIATI + " nella classe " + DatiEnte.class.getName());	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelAllegatiAssociati>>(listaAllegati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETALLEGATIASSOCIATI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_ALLEGATO_TO_DOWNLOAD, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAllegatoToDownload(@RequestParam(name = "idAllegato", required = true) Integer idAllegato) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETALLEGATOTODOWNLOAD + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			
			ModelAllegatiAssociati allegato = datiEnteService.getAllegatoToDownload(idAllegato);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETALLEGATOTODOWNLOAD + " nella classe " + DatiEnte.class.getName());	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<ModelAllegatiAssociati>(allegato, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETALLEGATOTODOWNLOAD + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_LISTA_PRESTAZIONI_VALORIZZATE_MOD_A, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaPrestazioniValorizzateModA(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
			log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_A + " nella classe " + DatiEnte.class.getName());
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			String idApp = SharedConstants.COMPONENT_NAME;
			String ipAddress = httpRequest.getRemoteAddr();
			String utente = userInfo.getCodFisc();
			String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<String> listaPrestAssModA = datiEnteService.getListaPrestazioniValorizzateModA(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_A + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<String>>(listaPrestAssModA, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_A + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_LISTA_PRESTAZIONI_VALORIZZATE_MOD_B1, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaPrestazioniValorizzateModB1(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
			log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_B1 + " nella classe " + DatiEnte.class.getName());
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			String idApp = SharedConstants.COMPONENT_NAME;
			String ipAddress = httpRequest.getRemoteAddr();
			String utente = userInfo.getCodFisc();
			String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<String> listaPrestAssModB1 = datiEnteService.getListaPrestazioniValorizzateModB1(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_B1 + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<String>>(listaPrestAssModB1, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_B1 + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_LISTA_PRESTAZIONI_VALORIZZATE_MOD_C, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaPrestazioniValorizzateModC(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
			log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_C + " nella classe " + DatiEnte.class.getName());
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			String idApp = SharedConstants.COMPONENT_NAME;
			String ipAddress = httpRequest.getRemoteAddr();
			String utente = userInfo.getCodFisc();
			String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<String> listaPrestAssModC = datiEnteService.getListaPrestazioniValorizzateModC(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_C + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<String>>(listaPrestAssModC, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LISTA_PRESTAZIONI_VALORIZZATE_MOD_C + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_LISTA_COMUNI_VALORIZZATI_MOD_A1, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaComuniValorizzatiModA1(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
			log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A1 + " nella classe " + DatiEnte.class.getName());
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			String idApp = SharedConstants.COMPONENT_NAME;
			String ipAddress = httpRequest.getRemoteAddr();
			String utente = userInfo.getCodFisc();
			String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<String> listaComuniValorrizatiModA1 = datiEnteService.getListaComuniValorizzatiModA1(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A1 + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<String>>(listaComuniValorrizatiModA1, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A1 + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_LISTA_COMUNI_VALORIZZATI_MOD_A2, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaComuniValorizzatiModA2(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
			log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A2 + " nella classe " + DatiEnte.class.getName());
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			String idApp = SharedConstants.COMPONENT_NAME;
			String ipAddress = httpRequest.getRemoteAddr();
			String utente = userInfo.getCodFisc();
			String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<String> listaComuniValorrizatiModA2 = datiEnteService.getListaComuniValorizzatiModA2(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A2 + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<String>>(listaComuniValorrizatiModA2, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_A2 + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_LISTA_COMUNI_VALORIZZATI_MOD_E, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaComuniValorizzatiModE(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
			log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_E + " nella classe " + DatiEnte.class.getName());
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			String idApp = SharedConstants.COMPONENT_NAME;
			String ipAddress = httpRequest.getRemoteAddr();
			String utente = userInfo.getCodFisc();
			String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<String> listaComuniValorrizatiModE = datiEnteService.getListaComuniValorizzatiModE(idScheda);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_E + " nella classe " + DatiEnte.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<String>>(listaComuniValorrizatiModE, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LISTA_COMUNI_VALORIZZATI_MOD_E + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value= GET_DATI_ENTE_RENDICONTAZIONE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDatiEnteRendicontazione(@Context HttpServletRequest httpRequest,@RequestParam(value="idRendicontazioneEnte", required = true) Integer idRendicontazioneEnte,@RequestParam(name = "annoGestione", required = true) Integer annoGestione) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_DATI_SCHEDA_ENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelDatiEnteRendicontazione schedaEnteGestore = new ModelDatiEnteRendicontazione();
			schedaEnteGestore = datiEnteService.findDatiEntexAnno(idRendicontazioneEnte,annoGestione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_DATI_SCHEDA_ENTE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelDatiEnteRendicontazione>(schedaEnteGestore, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_DATI_SCHEDA_ENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= GET_SCHEDA_ANAGRAFICA, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSchedaAnagrafica(@RequestParam(name = "idScheda", required = true) Integer idScheda, @RequestParam(name = "dataValidita", required = true) String dataValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelDatiAnagrafici schedaEnteGestore = new ModelDatiAnagrafici();
			String codregione = listeService.getParametro(SharedConstants.CODREGIONE).getValtext();
			schedaEnteGestore = datiEnteService.findSchedaEnte(idScheda,codregione,dataValidita);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelDatiAnagrafici>(schedaEnteGestore, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_COMUNI_ANAGRAFICA_ASSOCIATI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getComuniAnagraficaAssociati(@RequestParam(name = "id", required = true) Integer id,@RequestParam(name = "dataValidita", required = true) String dataValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelComuniAssociati> listaComuniAssociati = new ArrayList<ModelComuniAssociati>();
			listaComuniAssociati = datiEnteService.findComuniAnagraficaAssociati(id,dataValidita);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelComuniAssociati>>(listaComuniAssociati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_DATI_ANAGRAFICA, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveDatiAnagrafica(@RequestBody ModelDatiAnagraficiToSave datiEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			if (datiEnte.getDataModifica()==null) {
				String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_SALVA_DATA_MANCANTE).getTestoMessaggio();
				GenericResponseWarnErr errore = new GenericResponseWarnErr();
				errore.setId("500");
				errore.setDescrizione(errorMessage);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
				log.debug("[Errore Manca Data Modifica ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.BAD_REQUEST);
			}
			GenericResponseWarnErr response = datiEnteService.saveDatiAnagrafica(datiEnte, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_SCHEDA_ANAGRAFICA_STORICO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSchedaAnagraficaStorico(@RequestParam(name = "idScheda", required = true) Integer idScheda, @RequestParam(name = "dataFineValidita", required = true) String dataFineValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelDatiAnagrafici schedaEnteGestore = new ModelDatiAnagrafici();
			schedaEnteGestore = datiEnteService.findSchedaAnagraficaStorico(idScheda, dataFineValidita);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelDatiAnagrafici>(schedaEnteGestore, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_COMUNI_ANAGRAFICA_ASSOCIATI_STORICO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getComuniAnagraficaAssociatiStorico(@RequestParam(name = "id", required = true) Integer id, @RequestParam(name = "dataFineValidita", required = true) String dataFineValidita
			, @RequestParam(name = "dataInizioValidita", required = true) String dataInizioValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelComuniAssociati> listaComuniAssociati = new ArrayList<ModelComuniAssociati>();
			listaComuniAssociati = datiEnteService.findComuniAnagraficaAssociatiStorico(id, dataFineValidita,dataInizioValidita);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelComuniAssociati>>(listaComuniAssociati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= GET_MOTIVAZIONI_CHIUSURA, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMotivazioniChiusura() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelMotivazioni> listaMotivazioni = new ArrayList<ModelMotivazioni>();
			listaMotivazioni = datiEnteService.getMotivazioniChiusura();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelMotivazioni>>(listaMotivazioni, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_CRONOLOGIA_STATO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCronologiaEnte(@RequestParam(value = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelCronogiaStato> listaCronologia = new ArrayList<ModelCronogiaStato>();
			listaCronologia = datiEnteService.getCronologiaStato(idScheda);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
			return new ResponseEntity<List<ModelCronogiaStato>>(listaCronologia, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= CLOSE_ENTE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> closeEnte(@RequestBody(required = true) ModelChiusura chiuso) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			GenericResponseWarnErr response = datiEnteService.closeEnte(chiuso, userInfo);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= GET_LAST_STATO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getLastEnte(@RequestParam(value = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			ModelCronogiaStato lastCronologia = new ModelCronogiaStato();
			lastCronologia = datiEnteService.findLastStatoEnte(idScheda);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
			return new ResponseEntity<ModelCronogiaStato>(lastCronologia, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= RIPRISTINO_ENTE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> ripristinoEnte(@RequestBody(required = true) ModelChiusura chiuso) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			GenericResponseWarnErr response = datiEnteService.ripristinoEnte(chiuso, userInfo);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= GET_MOTIVAZIONI_RIPRISTINO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMotivazioniRipristino() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelMotivazioni> listaMotivazioni = new ArrayList<ModelMotivazioni>();
			listaMotivazioni = datiEnteService.getMotivazioniRipristino();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelMotivazioni>>(listaMotivazioni, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_COMUNE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PROVINCIA_COMUNE_LIBERO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getProvinciaComuneLibero(@RequestParam(name = "dataValidita", required = true) String dataValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPROVINCIACOMUNELIBERO + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelComune> listaProvinceComuni = new ArrayList<ModelComune>();
			String codregione = listeService.getParametro(SharedConstants.CODREGIONE).getValtext();
			listaProvinceComuni = datiEnteService.findProvinceComuniLiberi(codregione,dataValidita);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPROVINCIACOMUNELIBERO + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelComune>>(listaProvinceComuni, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPROVINCIACOMUNELIBERO + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value= CREA_NUOVO_ENTE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaNuovoEnte(@RequestBody ModelDatiAnagraficiToSave datiEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			
			GenericResponseWarnErr response = datiEnteService.creaNuovoEnte(datiEnte, userInfo);
			
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= GET_ENTI_UNIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getSchedaAnagraficaUnione(@RequestBody(required = true) RicercaListaEntiDaunire stato) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICAUNIONE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelDatiAnagrafici> schedaEnteGestore = new ArrayList<ModelDatiAnagrafici>();
			schedaEnteGestore = datiEnteService.findSchedaEnteUnione(stato);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICA + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelDatiAnagrafici> >(schedaEnteGestore, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICAUNIONE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value=UNIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> UnioneEnte(@RequestBody(required = true) UnioneEnte stato) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_UNIONEENTI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			//List<ModelDatiAnagrafici> schedaEnteGestore = new ArrayList<ModelDatiAnagrafici>();
			Boolean unione=null;
			unione = datiEnteService.unioneEnti(stato,userInfo);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_UNIONEENTI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<>(unione, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETSCHEDAANAGRAFICAUNIONE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value=GET_PRESTAZIONE_REGIONALE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioneRegionale(@RequestParam(name = "codPrestRegionale", required = true) String codPrestRegionale, @RequestParam(name = "annoGestione", required = true) Integer annoGestione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelDettaglioPrestRegionale dettPrest = datiEnteService.getPrestazioneRegionale(codPrestRegionale, annoGestione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelDettaglioPrestRegionale>(dettPrest, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value=GET_DATA_INIZIO_MAX, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDataInizioMax(@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.GETDATAINIZIOMAX + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {	
			ModelAbilitazioni a = new ModelAbilitazioni();
			a.setStato(datiEnteService.getDataInizioMax(idScheda));
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.GETDATAINIZIOMAX + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelAbilitazioni>(a, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.GETDATAINIZIOMAX + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
