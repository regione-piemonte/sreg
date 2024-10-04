/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import it.csi.greg.gregsrv.business.services.ModelloB1Service;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.EsportaModelloB1Input;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelB1ElencoLbl;
import it.csi.greg.gregsrv.dto.ModelB1ProgrammiMissioneTotali;
import it.csi.greg.gregsrv.dto.ModelB1Save;
import it.csi.greg.gregsrv.dto.ModelB1Voci;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_B1)
public class ModelloB1Controller {
	
	/** Constants Mapping **/
	public static final String GET_PRESTAZIONI = "/getPrestazioni";
	public static final String GET_ELENCO_LBL = "/getElencoLbl";
	public static final String GET_PROGRAMMI_MISSIONE_TOT_MODB = "/getProgrammiMissioneTotaliModB";
	public static final String SAVE_MODELLO = "/saveModello";
	public static final String ESPORTA_MODELLO_B1 = "/esportaModelloB1";
	public static final String CHECK_MODELLO_B1 = "/checkModelloB1";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ModelloB1Service modelloB1Service;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value = GET_ELENCO_LBL, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMissioneProgramma(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_B1_GET_ELENCO_LBL + " nella classe "
				+ ModelloB1Controller.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			ModelB1ElencoLbl elenco = new ModelB1ElencoLbl(); 
			
			elenco.setMissione_programma(modelloB1Service.getLblMissioniProgramma());
			elenco.setMacroaggregati(modelloB1Service.getLblMacroaggregati(idSchedaEnte));
			elenco.setUtenze(modelloB1Service.getLblUtenze(4, idSchedaEnte));
			elenco.setMsgInformativi(modelloB1Service.getLblMsginformativi());
			
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_B1_GET_ELENCO_LBL + " nella classe "
					+ ModelloB1Controller.class.getName());
			return new ResponseEntity<ModelB1ElencoLbl>(elenco, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_B1_GET_ELENCO_LBL + " nella classe "
					+ ModelloB1Controller.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = GET_PRESTAZIONI, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioni(@Context HttpServletRequest httpRequest, 
			@RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte, 
			@RequestParam(name = "anno", required = false) Optional<Integer> anno) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_B1_GET_PRESTAZIONI + " nella classe "
				+ ModelloB1Controller.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelB1Voci> voci = modelloB1Service.getVoci(idSchedaEnte);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_B1_GET_PRESTAZIONI + " nella classe "
					+ ModelloB1Controller.class.getName());
			return new ResponseEntity<List<ModelB1Voci>>(voci, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_B1_GET_PRESTAZIONI + " nella classe "
					+ ModelloB1Controller.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = GET_PROGRAMMI_MISSIONE_TOT_MODB, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getProgrammiMissioneTotModB(@Context HttpServletRequest httpRequest, 
			@RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte, 
			@RequestParam(name = "anno", required = false) Optional<Integer> anno) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_B1_GET_PROGRAMMI_MISSIONE_TOT_MODB + " nella classe "
				+ ModelloB1Controller.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);
		try {
			List<ModelB1ProgrammiMissioneTotali> totali = modelloB1Service.getProgrammiMissioneTotali(idSchedaEnte);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_B1_GET_PROGRAMMI_MISSIONE_TOT_MODB + " nella classe "
					+ ModelloB1Controller.class.getName());
			return new ResponseEntity<List<ModelB1ProgrammiMissioneTotali>>(totali, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_B1_GET_PROGRAMMI_MISSIONE_TOT_MODB + " nella classe "
					+ ModelloB1Controller.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_MODELLO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveModelloB1(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) ModelB1Save body) throws Exception {
		log.debug("[Inizio operazione salvataggio Modello B1 ] " + SharedConstants.OPERAZIONE_B1_SAVE + " nella classe " + ModelloB1Controller.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			//verifica se ente deve esserci la nota
			if(body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaRegionale")[1] 
					&& body.getProfilo().getListaazioni().get("CronologiaRegionale")[0] && (body.getCronologia().getNotaEnte()==null || body.getCronologia().getNotaEnte().equals(""))) {
			//if ((body.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || body.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)) && body.getCronologia().getNotaEnte()==null){
				//manca la nota per l'ente
				String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
				errore.setId("400");
				errore.setDescrizione(errornota);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
				log.debug("[Errore manca nota ] " + SharedConstants.OPERAZIONE_B1_SAVE + " nella classe " + ModelloB1Controller.class.getName());
				return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
			}
			//salva tutti i dati
			SaveModelloOutput response = modelloB1Service.saveModello(body, userInfo);
			// Controllo Eventuali Prestazioni non valorizzate per Warnings
			List<String> listaWarnings = modelloB1Service.controllaWarningsPrestNotValModelloB1(body.getDati());
			if(response.getWarnings() != null) response.getWarnings().addAll(listaWarnings); 
			else response.setWarnings(listaWarnings);
						
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione salva modello B1 ] " + SharedConstants.OPERAZIONE_B1_SAVE + " nella classe " + ModelloB1Controller.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio().replace("oggettosalvo", "Modello B1");
			response.setId("200");
			response.setDescrizione(errorMessage);
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_SALVATAGGIO_MODELLO).getTestoMessaggio().replace("SPECIFICARE", "B1");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_B1_SAVE + " nella classe " + ModelloB1Controller.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= ESPORTA_MODELLO_B1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaModelloB1(@Context HttpServletRequest httpRequest, @RequestBody(required = true) EsportaModelloB1Input body) {
		log.debug("[Inizio operazione esportazione excel Modello B1 ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOB1 + " nella classe " + ModelloB1Controller.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		try {
			String filexls = modelloB1Service.esportaModelloB1(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello B1 ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOB1 + " nella classe " + ModelloB1Controller.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modello B1");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModelloB1_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "B1");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOB1 + " nella classe " + ModelloB1Controller.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= CHECK_MODELLO_B1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> checkMacroaggregati(@Context HttpServletRequest httpRequest, @RequestBody(required = true) Integer idRendicontazione ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB1 + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponseWarnErr response  = modelloB1Service.checkModelloB1(idRendicontazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB1 + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB1 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB1 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
