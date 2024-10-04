/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.ControlloDao;
import it.csi.greg.gregsrv.business.entity.GregCMessaggioApplicativo;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.LunghezzaCampiException;
import it.csi.greg.gregsrv.exception.ParametriObbligatoriException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("controlloService")
public class ControlloService {

	@Autowired
	protected ControlloDao controlloDao;
	
	@Autowired
	protected ListeService listeService;
	
	
	public String numberDecimalValidate(String campo, String oggetto, int precisione, int decimali)
			 {
		String warning = null;
		if (Checker.isValorizzato(campo)) {
			campo = campo.replace(",", ".");
			if (!Checker.isNumberM_N(Converter.getDouble(campo), precisione, decimali)) {
				warning = Util.composeMessage(
						listeService.getMessaggio(SharedConstants.ERROR_CODE_DATI_NONVALIDI).getTestoMessaggio(), oggetto);
			}
		}
		return warning;
	}
	
	public String isNegativeNumber(BigDecimal valore, String oggetto) throws IntegritaException {
		String warning = null;
		
		if (valore != null) {
			// Se il numero e' negativo
			if (valore.signum() == -1) {
				warning = oggetto;
			}
		}
		return warning;
	}
	
	public ResponseEntity<?> integritaFailedResponse(IntegritaException e) {
		final GenericResponse errore = new GenericResponse();
		if(e==null) {
			GregCMessaggioApplicativo msg = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE);
			errore.setId(msg.getCodMessaggio());
			errore.setDescrizione(msg.getTestoMessaggio());
		} else {
			errore.setId(e.getCodMsg());
			errore.setDescrizione(e.getMessage());		
		}
		return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> parametriObbligatoriFailedResponse(ParametriObbligatoriException e) {
		final GenericResponse errore = new GenericResponse();
		if(e==null) {
			GregCMessaggioApplicativo msg = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE);
			errore.setId(msg.getCodMessaggio());
			errore.setDescrizione(msg.getTestoMessaggio());		
		} else {
			errore.setId(e.getCodMsg());
			errore.setDescrizione(e.getMessage());		
		}
		return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
	}	
	
	public ResponseEntity<?> lunghezzacampiFailedResponse(LunghezzaCampiException e) {	             
		final GenericResponse errore = new GenericResponse();
		if(e==null) {
			GregCMessaggioApplicativo msg = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE);
			errore.setId(msg.getCodMessaggio());
			errore.setDescrizione(msg.getTestoMessaggio());	
		} else {
			errore.setId(e.getCodMsg());
			errore.setDescrizione(e.getMessage());		
		}
		return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
	}
	
	public boolean checkSessionValid(UserInfo userInfo) {
		if(userInfo.getCodFisc() == null) {
			return false;
		} else {
			return true;
		}
	}

}
