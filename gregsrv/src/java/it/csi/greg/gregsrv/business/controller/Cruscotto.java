/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.CruscottoService;
import it.csi.greg.gregsrv.business.services.DatiRendicontazioneService;
import it.csi.greg.gregsrv.business.services.EntiGestoriAttiviService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnCheckErr;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.InfoModello;
import it.csi.greg.gregsrv.dto.IviaModelli;
import it.csi.greg.gregsrv.dto.ModelRicercaCruscotto;
import it.csi.greg.gregsrv.dto.ModelStatoCruscotto;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTabTrancheCruscotto;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.CRUSCOTTO)
public class Cruscotto {	
	
	/**Constants Mapping**/
	public static final String SEARCH_SCHEDE_ENTI_GESTORI = "/searchSchedeEntiGestori";
	public static final String GET_MODELLI = "/getModelli";
	public static final String GET_STATI = "/getStati";
	public static final String GET_INFO_MODELLO = "/getInfoModello";
	public static final String GET_MAX_ANNO = "/getMaxAnnoRendicontazione";

	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	/**Dependency - Injection**/
	@Autowired
	protected EntiGestoriAttiviService entiGestoriAttiviService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected CruscottoService cruscottoService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= SEARCH_SCHEDE_ENTI_GESTORI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getSchedeEntiGestori(@RequestBody(required = true) RicercaEntiGestori ricerca) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + Cruscotto.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			List<ModelRicercaCruscotto> schedeEntiGestori = new ArrayList<ModelRicercaCruscotto>();
			schedeEntiGestori = cruscottoService.findSchedeEntiGestori(ricerca);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + Cruscotto.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelRicercaCruscotto>>(schedeEntiGestori, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + Cruscotto.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= GET_MODELLI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getModelli(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelTabTrancheCruscotto> listaModelliAssociati = new ArrayList<ModelTabTrancheCruscotto>();
			listaModelliAssociati = cruscottoService.findModelli();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelTabTrancheCruscotto>>(listaModelliAssociati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_STATI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getStati(@RequestBody(required = true) RicercaEntiGestori ricerca) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelStatoCruscotto> listaStati = new ArrayList<ModelStatoCruscotto>();
			listaStati = cruscottoService.findStati(ricerca);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelStatoCruscotto>>(listaStati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_INFO_MODELLO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getInfoModello(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) InfoModello body) throws Exception{
		log.debug("[Inizio operazione controllo invio tranche ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		try {		
			GenericResponseWarnCheckErr response = cruscottoService.controlloModelloEnte(
					body);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione controllo invia I tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI
					+ " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<GenericResponseWarnCheckErr>(response, HttpStatus.OK);	
	
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERROR_INVIO_GENERICO).getTestoMessaggio().replace("SPECIFICARE", body.getCodModello());
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_MAX_ANNO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMaxAnnoRendicontazione(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			Integer anno = null;
			anno = cruscottoService.getMaxAnno();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<Integer>(anno, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
