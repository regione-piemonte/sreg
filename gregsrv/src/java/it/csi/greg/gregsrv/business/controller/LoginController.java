/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;


import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.EntiGestoriAttiviService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.LOGIN_CONTROLLER)
public class LoginController {
	
	/**Constants Mapping**/
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";
	public static final String SELECTPROFILO = "/selectprofilo";
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected EntiGestoriAttiviService entiGestoriAttiviService;
	
	@RequestMapping(value= LOGIN, method= RequestMethod.GET)
	@ResponseBody

	public ResponseEntity<?> login(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		String errorLogin = listeService.getMessaggio(SharedConstants.ERRORE_LOGIN_MESSAGGIO).getTestoMessaggio();
		try {					
			UserInfo operatore = entiGestoriAttiviService.getAccessoUtente(userInfo);
			if (operatore!=null) {
//				if (operatore.getEnteprofilo().size()>1) {//vuol dire molti enti
//					userInfo.setRuolo("Responsabile Multi Ente");
//					log.debug("[Login Effettuato Operatore MULTIENTE] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
//				}		
//				else
//				{
//					for (Map.Entry<Integer, String> entry : operatore.getEnteprofilo().entrySet()) {
//						if (entry.getValue().equalsIgnoreCase(SharedConstants.SUPER_USER)) {
//							userInfo.setRuolo("Amministratore");
//							log.debug("[Login Effettuato Amministratore]" + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
//						}
//						else if (entry.getValue().equalsIgnoreCase(SharedConstants.OPERATORE_REGIONALE)) {
//							userInfo.setRuolo("Operatore Regionale");
//							log.debug("[Login Effettuato Operatore Regionale]" + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
//						}
//						else {
//						userInfo.setRuolo("Responsabile Ente");
//						log.debug("[Login Effettuato Operatore ENTE]" + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
//						}
//					}
//				}
//				if (userInfo.getRuolo()!=null) {
//				userInfo.setEnteprofilo(operatore.getEnteprofilo());
				httpRequest.getSession().setAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR,userInfo);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, userInfo.toString());
				httpRequest.getSession().setAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR,userInfo);
				log.debug("[Termine Login ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
				return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
//				}
//				else {
//					//errore mancanza ruolo
//					GenericResponse errore = new GenericResponse();
//					errore.setId("400");
//					errore.setDescrizione(errorLogin);	
//					auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
//					log.debug("[Errore manca Ruolo ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
//					return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
//				}
			} 
			else{
				GenericResponse errore = new GenericResponse();
				errore.setId("401");
				errore.setDescrizione(errorLogin);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, SharedConstants.KEY_OPER_UNAUTHORIZED + " " +errore.getDescrizione());
				log.debug("[Errore non autorizzato ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName() + " errore " + errore.getDescrizione());
				return new ResponseEntity<GenericResponse>(errore, HttpStatus.UNAUTHORIZED);
			}

		}	
		catch (Exception e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorLogin);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,SharedConstants.KEY_OPER_INTERNAL_SERVER_ERROR + " " +errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= LOGOUT, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> logout(@Context HttpServletRequest httpRequest,HttpServletResponse response) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LOGOUT + " nella classe " + LoginController.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		try {
		httpRequest.getSession(false).setMaxInactiveInterval(1);
		httpRequest.getSession().removeAttribute("XSRF_SESSION_TOKEN");
		httpRequest.getSession().removeAttribute("X-XSRF-TOKEN");
		httpRequest.getSession().removeAttribute("XSRF-TOKEN");
		httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.IRIDE_ID_SESSIONATTR);
		httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.AUTH_ID_MARKER);
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies!=null) {
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().contentEquals("XSRF-TOKEN")) {
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
			}
		}
		}
		httpRequest.getSession().invalidate();
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LOGOUT + " nella classe " + LoginController.class.getName());
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
		return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
	}	
	catch (Exception e) {
		e.printStackTrace();
		GenericResponse errore = new GenericResponse();
		errore.setId("500");
		errore.setDescrizione("Errore nel logout");		
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,SharedConstants.KEY_OPER_INTERNAL_SERVER_ERROR + " " +errore.getDescrizione());
		log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LOGOUT + " nella classe " + LoginController.class.getName() + " errore " + e.getMessage());
		return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	@RequestMapping(value= SELECTPROFILO, method= RequestMethod.GET)
	@ResponseBody

	public ResponseEntity<?>selectprofilo (@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente =userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		String errorLogin = listeService.getMessaggio(SharedConstants.ERRORE_LOGIN_PROFILO).getTestoMessaggio();
		try {					
			UserInfo operatore = entiGestoriAttiviService.getAccessoUtente(userInfo);
			if (operatore!=null) {
				httpRequest.getSession().setAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR,operatore);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, operatore.toString());
				httpRequest.getSession().setAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR,operatore);
				log.debug("[Termine Login ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName());
				return new ResponseEntity<UserInfo>(operatore, HttpStatus.OK);
			}
			else{
				GenericResponse errore = new GenericResponse();
				errore.setId("401");
				errore.setDescrizione(errorLogin);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, SharedConstants.KEY_OPER_UNAUTHORIZED + " " +errore.getDescrizione());
				log.debug("[Errore non autorizzato ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName() + " errore " + errore.getDescrizione());
				return new ResponseEntity<GenericResponse>(errore, HttpStatus.UNAUTHORIZED);
			}

		}	
		catch (Exception e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorLogin);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,SharedConstants.KEY_OPER_INTERNAL_SERVER_ERROR + " " +errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_LOGIN + " nella classe " + LoginController.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
