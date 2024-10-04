/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.business.services.ModelloEService;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.EsportaModelloEInput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.ModelAttivitaSocioAssist;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModE;
import it.csi.greg.gregsrv.dto.SaveModelloEInput;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_E)
public class ModelloE {
	
	/**Constants Mapping**/
	public static final String GET_ATTIVITA_SOCIO_ASSIST = "/getAttivitaSocioAssist";
	public static final String GET_RENDICONTAZIONE_MOD_E = "/getRendicontazioneModE";
	public static final String SAVE_MODELLO_E = "/saveModelloE";
	public static final String ESPORTA_MODELLO_E = "/esportaModelloE";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ModelloEService modelloEService;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_ATTIVITA_SOCIO_ASSIST, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAttivitaSocioAssist(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETATTIVITASOCIOASSIST + " nella classe " + ModelloE.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<ModelAttivitaSocioAssist> attivitaSocioAssist = modelloEService.getAttivitaSocioAssist();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETATTIVITASOCIOASSIST + " nella classe " + ModelloE.class.getName());
			return new ResponseEntity<List<ModelAttivitaSocioAssist>>(attivitaSocioAssist, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETATTIVITASOCIOASSIST + " nella classe " + ModelloE.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value= GET_RENDICONTAZIONE_MOD_E, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRendicontazioneModE(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODE + " nella classe " + ModelloE.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			ModelRendicontazioneModE dati = modelloEService.getRendicontazioneModE(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODE + " nella classe " + ModelloE.class.getName());
			return new ResponseEntity<ModelRendicontazioneModE>(dati, HttpStatus.OK);
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODE + " nella classe " + ModelloE.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value= SAVE_MODELLO_E, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveModelloE(@Context HttpServletRequest httpRequest, @RequestBody(required = true) SaveModelloEInput body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOE + " nella classe " + ModelloE.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {	
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput response  = modelloEService.saveModelloE(body, userInfo);
						
			String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio().replace("oggettosalvo", "Modello E");
			response.setEsito("OK");
			response.setMessaggio(message);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOE + " nella classe " + ModelloE.class.getName());
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);		
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOE + " nella classe " + ModelloE.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= ESPORTA_MODELLO_E, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaModelloE(@Context HttpServletRequest httpRequest, @RequestBody(required = true) EsportaModelloEInput body) {
		log.debug("[Inizio operazione esportazione excel Modello E ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOE + " nella classe " + ModelloE.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		try {
			String filexls = modelloEService.esportaModelloE(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello E ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOE + " nella classe " + ModelloE.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modello E");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModelloE_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "E");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOE + " nella classe " + ModelloE.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
