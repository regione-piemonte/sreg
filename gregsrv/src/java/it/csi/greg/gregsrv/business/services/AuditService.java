/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.AuditDao;
import it.csi.greg.gregsrv.business.entity.GregDOperazioneAudit;
import it.csi.greg.gregsrv.business.entity.GregTAudit;

@Service("auditService")
public class AuditService {

	@Autowired
	protected AuditDao auditDao;
	
	public GregDOperazioneAudit getOperazionePerTipo(String operazione) {
		return auditDao.getOperazionePerTipo(operazione);
	}
	
	public void ScriviAudit(String idApp, String ipAddress, String utente, String operazione, String oggOper, String riqres, String keyoper) {
		GregDOperazioneAudit tipooper= new GregDOperazioneAudit();
		tipooper = getOperazionePerTipo(operazione);		
		GregTAudit audit = new GregTAudit(idApp, ipAddress, utente, tipooper, oggOper+" - " + riqres, keyoper);
		insertAudit(audit);	
	}
	public void insertAudit(GregTAudit audit) {
		auditDao.insertAudit(audit);
	}
}
