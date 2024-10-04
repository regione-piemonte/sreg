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
import it.csi.greg.gregsrv.business.services.ModelloA2Service;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.EsportaModelloA2Input;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GetTrasferimentiA2Output;
import it.csi.greg.gregsrv.dto.ModelCausali;
import it.csi.greg.gregsrv.dto.SaveModelloA2Input;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_A2)
public class ModelloA2 {
	
	/**Constants Mapping**/
	public static final String GET_CAUSALI = "/getCausali";
	public static final String GET_CAUSALI_STATICHE = "/getCausaliStatiche";
	public static final String GET_TRASFERIMENTI_A2 = "/getTrasferimentiA2";
	public static final String SAVE_MODELLO_A2 = "/saveModelloA2";
	public static final String ESPORTA_MODELLO_A2 = "/esportaModelloA2";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ModelloA2Service modelloA2Service;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_CAUSALI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCausali(@Context HttpServletRequest httpRequest, @RequestParam(name = "id_ente", required = true) Integer idEnte ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETCAUSALI + " nella classe " + ModelloA2.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<ModelCausali> causali = modelloA2Service.getCausali(idEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETCAUSALI + " nella classe " + ModelloA2.class.getName());
			return new ResponseEntity<List<ModelCausali>>(causali, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETCAUSALI + " nella classe " + ModelloA2.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= GET_CAUSALI_STATICHE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCausaliStatiche(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETCAUSALISTATICHE + " nella classe " + ModelloA2.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<ModelCausali> causali = modelloA2Service.getCausaliStatiche();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETCAUSALISTATICHE + " nella classe " + ModelloA2.class.getName());
			return new ResponseEntity<List<ModelCausali>>(causali, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETCAUSALISTATICHE + " nella classe " + ModelloA2.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_TRASFERIMENTI_A2, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTrasferimentiA2(@Context HttpServletRequest httpRequest, @RequestParam(name = "id_ente", required = true) Integer idEnte ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETTRASFERIMENTIMODA2 + " nella classe " + ModelloA2.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			GetTrasferimentiA2Output trasferimenti = modelloA2Service.getTrasferimentiModA2(idEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETTRASFERIMENTIMODA2 + " nella classe " + ModelloA2.class.getName());
			return new ResponseEntity<GetTrasferimentiA2Output>(trasferimenti, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETTRASFERIMENTIMODA2 + " nella classe " + ModelloA2.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_MODELLO_A2, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveModelloA2(@Context HttpServletRequest httpRequest, @RequestBody(required = true) SaveModelloA2Input body ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA2 + " nella classe " + ModelloA2.class.getName());
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
			SaveModelloOutput response  = modelloA2Service.saveModelloA2(body, userInfo);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA2 + " nella classe " + ModelloA2.class.getName());
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);		
		}catch (SessionException e) {
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA2 + " nella classe " + ModelloA2.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= ESPORTA_MODELLO_A2, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaModelloA2(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) EsportaModelloA2Input body) {
		log.debug("[Inizio operazione esportazione excel Modello A2 ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOA2 + " nella classe " + ModelloA2.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		try {
				//salva tutti i dati
			String filexls = modelloA2Service.esportaModelloA2(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello A2 ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOA2 + " nella classe " + ModelloA2.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modello A2");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModelloA2_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "A2");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOA2 + " nella classe " + ModelloA2.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
