/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.filter;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
/**
 * Interceptor JPA-Hibernate NON UTILIZZATO.
 * Rimane a disposizione per il futuro
 * @author Vincenzo Sciancalepore
 *
 */
public class CifraturaInterceptor implements Interceptor{

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		System.out.println("a");
		
//		if (entity instanceof ScerevTPratica) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if (propertyNames[i].equalsIgnoreCase("cfassistito")) {
//					System.out.println("awannddd");
//					state[i] = "DECIFRATO";
//				}
//			}
//		}
		
		return false;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		System.out.println("a");
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		System.out.println("a");
		return false;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		System.out.println("a");
		
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
		System.out.println("a");
		
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
		System.out.println("a");
		
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
		System.out.println("a");
		
	}

	@Override
	public void preFlush(Iterator entities) throws CallbackException {
		System.out.println("a");
		
	}

	@Override
	public void postFlush(Iterator entities) throws CallbackException {
		System.out.println("a");
		
	}

	@Override
	public Boolean isTransient(Object entity) {
		System.out.println("a");
		return null;
	}

	@Override
	public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		System.out.println("a");
		return null;
	}

	@Override
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
		System.out.println("a");
		return null;
	}

	@Override
	public String getEntityName(Object object) throws CallbackException {
		System.out.println("a");
		return null;
	}

	@Override
	public Object getEntity(String entityName, Serializable id) throws CallbackException {
		System.out.println("a");
		return null;
	}

	@Override
	public void afterTransactionBegin(Transaction tx) {
		System.out.println("a");
		
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		System.out.println("a");
		
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		System.out.println("a");
		
	}

	@Override
	public String onPrepareStatement(String sql) {
		System.out.println("a");
		return null;
	}

}
