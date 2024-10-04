/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDOperazioneAudit;
import it.csi.greg.gregsrv.business.entity.GregTAudit;



@Repository("auditDao")
public class AuditDao {
	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertAudit(GregTAudit audit) {
		audit.setDataOra(new Timestamp(new Date().getTime()));
		em.persist(audit);
	}
	
	public GregDOperazioneAudit getOperazionePerTipo(String operazione) {
	TypedQuery<GregDOperazioneAudit> query = em.createNamedQuery("GregDOperazioneAudit.findByDescOper",GregDOperazioneAudit.class);
	query.setParameter("operazione", operazione);
	return query.getSingleResult();
	}
}
