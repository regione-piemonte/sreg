/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.ModelMsgApplicativo;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelStatoRendicontazione;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.LISTE)
public class Liste {
	
	/**Constants Mapping**/
	public static final String GET_MSG_INFORMATIVI = "/getMessaggiInformativi";
	public static final String GET_MSG_APPLICATIVO = "/getMessaggioApplicativo";
	public static final String GET_DESC_STATO_RENDICONTAZIONE = "/getDescStatoRendicontazione";
	public static final String GET_PARAMETRO = "/getParametro";
	public static final String GET_PARAMETROPERINFORMATIVA = "/getParametroPerInformativa";
	public static final String GET_MSG_INFORMATIVIPERCOD = "/getMessaggiInformativipercodice";
	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	/**Dependency - Injection**/
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	
	
	@RequestMapping(value= GET_MSG_INFORMATIVI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMsgInformativo(@Context HttpServletRequest httpRequest, @RequestParam(value = "section", required = true) String section) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETMSGINFO + " nella classe " + Liste.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelMsgInformativo> listaMsgInformativi = listeService.findMsgInformativi(section);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETMSGINFO + " nella classe " + Liste.class.getName());
			return new ResponseEntity<List<ModelMsgInformativo>>(listaMsgInformativi, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETMSGINFO + " nella classe " + Liste.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_MSG_APPLICATIVO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMsgApplicativo(@Context HttpServletRequest httpRequest, @RequestParam(value = "param", required = true) String param) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETMSGAPP + " nella classe " + Liste.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			ModelMsgApplicativo msgApplicativo = new ModelMsgApplicativo(listeService.getMessaggio(param));  
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETMSGAPP + " nella classe " + Liste.class.getName());
			return new ResponseEntity<ModelMsgApplicativo>(msgApplicativo, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETMSGAPP + " nella classe " + Liste.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_DESC_STATO_RENDICONTAZIONE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDescStatoRendicontazione(@Context HttpServletRequest httpRequest, @RequestParam(value = "codStatoRend", required = true) String codStatoRend) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETDESCSTATOREND + " nella classe " + Liste.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			ModelStatoRendicontazione statoRend = listeService.getStatoRendicontazione(codStatoRend);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETDESCSTATOREND + " nella classe " + Liste.class.getName());
			return new ResponseEntity<ModelStatoRendicontazione>(statoRend, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETDESCSTATOREND + " nella classe " + Liste.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PARAMETRO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getParametro(@Context HttpServletRequest httpRequest, @RequestParam(value = "codParam", required = true) String codParam) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPARAMETRO + " nella classe " + Liste.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			ModelParametro msgApplicativo = listeService.getParametro(codParam);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPARAMETRO + " nella classe " + Liste.class.getName());
			return new ResponseEntity<ModelParametro>(msgApplicativo, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPARAMETRO + " nella classe " + Liste.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PARAMETROPERINFORMATIVA, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getParametroPerInformativa(@Context HttpServletRequest httpRequest, @RequestParam(value = "informativa", required = true) String informativa) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPARAMETROPERINFORMATIVA + " nella classe " + Liste.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelParametro> msgApplicativo = listeService.getParametroPerInformativa(informativa);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPARAMETROPERINFORMATIVA + " nella classe " + Liste.class.getName());
			return new ResponseEntity<List<ModelParametro>>(msgApplicativo, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPARAMETROPERINFORMATIVA + " nella classe " + Liste.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_MSG_INFORMATIVIPERCOD, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMsgInformativoPerCod(@Context HttpServletRequest httpRequest, @RequestParam(value = "codice", required = true) String codice) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETMSGINFOPERCOD + " nella classe " + Liste.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			ModelMsgInformativo msgInformativiByCodice = listeService.getMsgInformativiByCodice(codice);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETMSGINFOPERCOD + " nella classe " + Liste.class.getName());
			return new ResponseEntity<ModelMsgInformativo>(msgInformativiByCodice, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETMSGINFOPERCOD + " nella classe " + Liste.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
