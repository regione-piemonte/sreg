/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;
import it.csi.greg.gregsrv.business.services.ArchivioDatiRendicontazioneService;
import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelRicercaEntiGestoriAttivi;
import it.csi.greg.gregsrv.dto.ModelStatoRendicontazione;
import it.csi.greg.gregsrv.dto.RicercaArchivioRendicontazione;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.ARCHIVIO_DATI_RENDICONTAZIONE)
public class ArchivioDatiRendicontazione {
	
	/**Constants Mapping**/
	public static final String GET_LISTA_STATO_RENDICONTAZIONE = "/getListaStatoRendicontazione";
	public static final String GET_LISTA_COMUNI = "/getListaComuni";
	public static final String GET_LISTA_TIPO_ENTE = "/getListaTipoEnte";
	public static final String SEARCH_ARCHIVIO_SCHEDE_ENTI_GESTORI = "/searchArchivioRendicontazione";
	public static final String GET_CRONOLOGIA = "/getCronologia";
	public static final String SEARCH_SCHEDE_ENTI_GESTORI_RENDICONTAZIONE_CONCLUSA = "/searchSchedeEntiGestoriRendicontazioneConclusa";
	public static final String GET_STATO_RENDICONTAZIONE_CONCLUSA = "/getStatoRendicontazioneConclusa";
	public static final String GET_ANNI_ESERCIZIO_ARCHIVIO = "/getAnniEsercizio";
	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	/**Dependency - Injection**/
	@Autowired
	protected ArchivioDatiRendicontazioneService archivioDatiRendicontazioneService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected HttpServletRequest httpRequest;
	
	
	@RequestMapping(value= GET_LISTA_STATO_RENDICONTAZIONE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaStatoRendicontazione() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		
		try {			
			List<ModelStatoRendicontazione> listaStatoRendicontazione = new ArrayList<ModelStatoRendicontazione>();
			listaStatoRendicontazione = archivioDatiRendicontazioneService.findAllStatoRendicontazione();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			return new ResponseEntity<List<ModelStatoRendicontazione>>(listaStatoRendicontazione, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONE + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	
	@RequestMapping(value= GET_LISTA_COMUNI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaComuni() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTACOMUNI + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		
		try {			
			List<GregDComuni> listaComuni = new ArrayList<GregDComuni>();
			listaComuni = archivioDatiRendicontazioneService.findAllComuni();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTACOMUNI + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			return new ResponseEntity<List<GregDComuni>>(listaComuni, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTACOMUNI + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
	};
	
	
	@RequestMapping(value= GET_LISTA_TIPO_ENTE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaTipoEnte() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTATIPOENTE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		
		try {			
			List<GregDTipoEnte> listaTipoEnte = new ArrayList<GregDTipoEnte>();
			listaTipoEnte = archivioDatiRendicontazioneService.findAllTipoEnte();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTATIPOENTE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			return new ResponseEntity<List<GregDTipoEnte>>(listaTipoEnte, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTATIPOENTE + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	
	@RequestMapping(value= SEARCH_ARCHIVIO_SCHEDE_ENTI_GESTORI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getSchedeEntiGestori(@RequestBody(required = true) RicercaArchivioRendicontazione ricerca) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelRicercaEntiGestoriAttivi> schedeEntiGestori = new ArrayList<ModelRicercaEntiGestoriAttivi>();
			schedeEntiGestori = archivioDatiRendicontazioneService.findArchivioRendicontazione(ricerca);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			return new ResponseEntity<List<ModelRicercaEntiGestoriAttivi>>(schedeEntiGestori, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	
	@RequestMapping(value= GET_CRONOLOGIA, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCronologiaEnte(@RequestParam(value = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelCronologiaEnte> listaCronologia = new ArrayList<ModelCronologiaEnte>();
			listaCronologia = archivioDatiRendicontazioneService.findCronologiaEnte(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			return new ResponseEntity<List<ModelCronologiaEnte>>(listaCronologia, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= SEARCH_SCHEDE_ENTI_GESTORI_RENDICONTAZIONE_CONCLUSA, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getSchedeEntiGestoriRendicontazioneConclusa(@RequestBody(required = true) RicercaEntiGestori ricerca) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			List<ModelRicercaEntiGestoriAttivi> schedeEntiGestoriRendicontazioneConclusa = new ArrayList<ModelRicercaEntiGestoriAttivi>();
			schedeEntiGestoriRendicontazioneConclusa = archivioDatiRendicontazioneService.findSchedeEntiGestoriRendicontazioneConclusa(ricerca);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelRicercaEntiGestoriAttivi>>(schedeEntiGestoriRendicontazioneConclusa, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= GET_STATO_RENDICONTAZIONE_CONCLUSA, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaStatoRendicontazioneConclusa() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONECONCLUSA + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelStatoRendicontazione statoRendicontazione = new ModelStatoRendicontazione();
			statoRendicontazione = archivioDatiRendicontazioneService.findStatoRendicontazioneConclusa();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONECONCLUSA + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<ModelStatoRendicontazione>(statoRendicontazione, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONECONCLUSA + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= GET_ANNI_ESERCIZIO_ARCHIVIO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAnniEsercizio() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETANNIESERCIZIO + " nella classe " + ArchivioDatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<Integer> listaStatoRendicontazione = new ArrayList<Integer>();
			listaStatoRendicontazione = archivioDatiRendicontazioneService.findAllAnnoEsercizio();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETANNIESERCIZIO + " nella classe " + ArchivioDatiRendicontazione.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<Integer>>(listaStatoRendicontazione, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETANNIESERCIZIO + " nella classe " + ArchivioDatiRendicontazione.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
