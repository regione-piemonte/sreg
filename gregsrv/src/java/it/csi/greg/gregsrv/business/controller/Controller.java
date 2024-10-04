/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.CONTROLLER)
public class Controller {
	
	/**Constants Mapping**/
	public static final String GET_CONTROLLER = "/getController";//GET
	public static final String GET_PARAM_CONTROLLER = "/getParamController";//GET
	public static final String POST_CONTROLLER = "/postController";//POST
	public static final String POST_PARAM_CONTROLLER = "/postParamController";//POST
	
	@RequestMapping(value= GET_CONTROLLER, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> testGet() {
		try {			
			String response = "TEST GET";
				
			return new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= GET_PARAM_CONTROLLER, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> testGetParam(@RequestParam(name = "param", required = true) String param) {
		try {			
			String response = "TEST GET PARAM "+ param;
				
			return new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= POST_CONTROLLER, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> testPost(@RequestBody(required = true) String prova) {
		try {			
			String response = "TEST POST " + prova;
				
			return new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= POST_PARAM_CONTROLLER, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> testPostParam(@RequestParam(value= "param", required = true) String param, @RequestBody(required = true) String prova) {
		try {			
			String response = "TEST POST PARAM "+ param + " BODY "+ prova;
				
			return new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};

}
