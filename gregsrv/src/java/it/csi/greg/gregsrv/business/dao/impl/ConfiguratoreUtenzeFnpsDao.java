/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregCRegola;
import it.csi.greg.gregsrv.business.entity.GregDColori;
import it.csi.greg.gregsrv.business.entity.GregDFondi;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDSpesaMissioneProgramma;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregDTipoStruttura;
import it.csi.greg.gregsrv.business.entity.GregDTipologia;
import it.csi.greg.gregsrv.business.entity.GregDTipologiaQuota;
import it.csi.greg.gregsrv.business.entity.GregDTipologiaSpesa;
import it.csi.greg.gregsrv.business.entity.GregDVoceIstat;
import it.csi.greg.gregsrv.business.entity.GregRCatUteVocePrestReg2Istat;
import it.csi.greg.gregsrv.business.entity.GregRFnpsUtenzaCalcolo;
import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregRNomencNazPrestReg2;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1MacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestMinist;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestReg2;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1ProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg2UtenzeRegionali2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneFondi;
import it.csi.greg.gregsrv.business.entity.GregRSpeseFnps;
import it.csi.greg.gregsrv.business.entity.GregRTipologiaSpesaPrestReg1;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTNomenclatoreNazionale;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniMinisteriali;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali2;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestazConfiguratore;
import it.csi.greg.gregsrv.dto.ModelFondi;
import it.csi.greg.gregsrv.dto.ModelMacroaggregatiConf;
import it.csi.greg.gregsrv.dto.ModelNomenclatore;
import it.csi.greg.gregsrv.dto.ModelPrest1Prest2;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestMin;
import it.csi.greg.gregsrv.dto.ModelPrest2PrestIstat;
import it.csi.greg.gregsrv.dto.ModelPrestUtenza;
import it.csi.greg.gregsrv.dto.ModelUtentiFnps;
import it.csi.greg.gregsrv.dto.ModelUtenzaAllD;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;

@Repository("configuratoreUtenzeFnpsDao")
@Transactional(readOnly = true)
public class ConfiguratoreUtenzeFnpsDao {
	@PersistenceContext
	private EntityManager em;

	public List<ModelUtentiFnps> findUtenzePerCalcolo() {

		String sqlQuery = "select "
				+ "	tu.id_target_utenza, "
				+ "	tu.cod_utenza, "
				+ "	tu.des_utenza, "
				+ "	fuc.id_fnps_utenza_calcolo, "
				+ "	fuc.valore_percentuale, "
				+ "	fuc.utilizzato_per_calcolo, "
				+ "	cast(date_part('year', fuc.data_inizio_validita) as int4) as anno_inizio, "
				+ "	cast(date_part('year', fuc.data_fine_validita) as int4) as anno_fine "
				+ "from "
				+ "	greg_r_fnps_utenza_calcolo fuc, "
				+ "	greg_d_target_utenza tu, "
				+ "	greg_d_aree a  "
				+ "where "
				+ "	tu.data_cancellazione is null "
				+ "	and tu.id_target_utenza = fuc.id_target_utenza "
				+ "	and fuc.data_cancellazione is null "
				+ " and a.id_area = tu.id_area  "
				+ "	and a.data_cancellazione is null "
				+ "order by "
				+ "	a.cod_area, tu.cod_utenza, fuc.data_inizio_validita";
		Query query = em.createNativeQuery(sqlQuery);
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();

		List<ModelUtentiFnps> utenze = new ArrayList<ModelUtentiFnps>();

		for (Object[] m : result) {
			ModelUtentiFnps ma = new ModelUtentiFnps(m);
			utenze.add(ma);
		}
		return utenze;
	}
	
	
	public List<GregDTargetUtenza> findAllTarget() {

		String hqlQuery = "SELECT f FROM GregDTargetUtenza f " 
						+ "WHERE f.dataCancellazione is null "
						+ "AND f.gregDTipoFlusso.codTipoFlusso = 'MIN' "
						+ "ORDER BY f.gregDAree.codArea, f.codUtenza";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		return query.getResultList();
	}
	
	public boolean utenzaIsModificabile(Integer idUtenzaFnps) {

		String sqlQuery = "select count(distinct rmf.id_rendicontazione_ente)>0 "
				+ "from greg_r_rendicontazione_modulo_fnps rmf, "
				+ "greg_r_fnps_utenza_calcolo fuc, "
				+ "greg_r_prest_minist_utenze_minist pmum, "
				+ "greg_t_rendicontazione_ente re  "
				+ "where rmf.id_prest_minist_utenze_minist = pmum.id_prest_minist_utenze_minist  "
				+ "and pmum.id_target_utenza = fuc.id_target_utenza  "
				+ "and rmf.id_rendicontazione_ente = re.id_rendicontazione_ente  "
				+ "and rmf.data_cancellazione is null  "
				+ "and fuc.data_cancellazione is null  "
				+ "and pmum.data_cancellazione is null  "
				+ "and re.data_cancellazione is null "
				+ "and rmf.valore > 0 "
				+ "and fuc.id_fnps_utenza_calcolo = :idUtenzaFnps "
				+ "and ((cast(re.anno_gestione || '-12-31' as TIMESTAMP) >= fuc.data_inizio_validita "
				+ "and cast(re.anno_gestione || '-12-31' as TIMESTAMP) <= fuc.data_fine_validita) "
				+ "OR (cast(re.anno_gestione || '-12-31' as TIMESTAMP) >= fuc.data_inizio_validita "
				+ "and fuc.data_fine_validita is null))";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idUtenzaFnps", idUtenzaFnps);
		boolean result = (boolean) query.getSingleResult();

		return result;
	}
	
	public GregRFnpsUtenzaCalcolo findUtenzaCalcolobyId(Integer idUtenzaFnps) {

		String hqlQuery = "SELECT u FROM GregRFnpsUtenzaCalcolo u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idFnpsUtenzaCalcolo = :idUtenzaFnps ";

		TypedQuery<GregRFnpsUtenzaCalcolo> query = em.createQuery(hqlQuery, GregRFnpsUtenzaCalcolo.class);
		query.setParameter("idUtenzaFnps", idUtenzaFnps);
		return query.getSingleResult();
	}
	
	public GregRFnpsUtenzaCalcolo aggiornaRFnpsCalcolo(GregRFnpsUtenzaCalcolo utenza) {
		return em.merge(utenza);
	}
	
	public List<ModelUtentiFnps> recuperaUtenzePerCalcoloByAnnoEsercizio(Integer annoEsercizio) {

		String sqlQuery = "select tu.des_utenza, fuc.valore_percentuale, fuc.utilizzato_per_calcolo, tu.id_target_utenza, tu.cod_utenza  "
				+ "from greg_r_fnps_utenza_calcolo fuc, "
				+ "greg_d_target_utenza tu left join greg_d_aree a on a.id_area = tu.id_area and a.data_cancellazione is null  "
				+ "where tu.id_target_utenza = fuc.id_target_utenza  "
				+ "and fuc.data_cancellazione is null "
				+ "and tu.data_cancellazione is null  "
				+ "and (( cast('"+annoEsercizio+"-12-31' as TIMESTAMP) >= fuc.data_inizio_validita "
				+ "and cast('"+annoEsercizio+"-12-31' as TIMESTAMP) <= fuc.data_fine_validita) "
						+ "OR (cast('"+annoEsercizio+"-12-31' as TIMESTAMP) >= fuc.data_inizio_validita "
						+ "and fuc.data_fine_validita is null)) "
						+ "order by a.cod_area, tu.cod_utenza ";
		Query query = em.createNativeQuery(sqlQuery);
		
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();

		List<ModelUtentiFnps> utenze = new ArrayList<ModelUtentiFnps>();

		for (Object[] m : result) {
			ModelUtentiFnps ma = new ModelUtentiFnps();
			ma.setDescUtenza((String) m[0]);
			ma.setValorePercentuale((Double) m[1]);
			ma.setUtilizzatoPerCalcolo((Boolean) m[2]);
			ma.setIdUtenza((Integer) m[3]);
			ma.setCodUtenza((String) m[4]);
			utenze.add(ma);
		}
		return utenze;
	}
	
	public List<ModelFondi> findFondiByidRendicontazione(Integer idRendicontazione) {

		String sqlQuery = "select rf.id_fondo, df.cod_fondo, df.desc_fondo, rf.id_regola, cr.codice_regola, cr.descrizione_regola, rf.valore, tf.cod_tipologia_fondo, cr.funzione1_regola, rf.id_rendicontazione_fondi, re.id_rendicontazione_ente, df.dettagliato_in_rendicontazione, df.msg_informativo "
				+ "from greg_r_rendicontazione_fondi rf left join greg_c_regola cr on rf.id_regola = cr.id_regola, "
				+ "greg_t_rendicontazione_ente re, "
				+ "greg_d_fondi df,greg_d_tipologia_fondi tf "
				+ "where rf.id_rendicontazione_ente = re.id_rendicontazione_ente  "
				+ "and rf.id_fondo = df.id_fondo  "
				+ "and tf.id_tipologia_fondo = df.id_tipologia_fondo "
				+ "and rf.data_cancellazione is null  "
				+ "and re.data_cancellazione is null  "
				+ "and df.data_cancellazione is null  "
				+ "and tf.data_cancellazione is null  "
				+ "and ((cast(re.anno_gestione || '-12-31' as TIMESTAMP) >= df.data_inizio_validita and cast(re.anno_gestione || '-12-31' as TIMESTAMP) <= df.data_fine_validita) OR (cast(re.anno_gestione || '-12-31' as TIMESTAMP) >= df.data_inizio_validita and df.data_fine_validita is null) )"
				+ "and re.id_rendicontazione_ente = :idRendicontazione "
				+ "order by tf.id_tipologia_fondo, df.cod_fondo";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();

		List<ModelFondi> fondi = new ArrayList<ModelFondi>();

		for (Object[] m : result) {
			ModelFondi ma = new ModelFondi(m);
			fondi.add(ma);
		}
		return fondi;
	}
	
	public List<ModelFondi> findFondiByAnnoEsercizio(Integer annoEsercizio) {

		String sqlQuery = "select f.id_fondo, f.cod_fondo, f.desc_fondo, tf.cod_tipologia_fondo, f.dettagliato_in_rendicontazione "
				+ "from greg_d_fondi f, "
				+ "greg_d_tipologia_fondi tf  "
				+ "where tf.id_tipologia_fondo = f.id_tipologia_fondo  "
				+ "and tf.data_cancellazione is null  "
				+ "and f.data_cancellazione is null  "
				+ "and ((cast('"+annoEsercizio+"-12-31' as TIMESTAMP) >= f.data_inizio_validita and cast('"+annoEsercizio+"-12-31' as TIMESTAMP) <= f.data_fine_validita)  OR (cast('"+annoEsercizio+"-12-31' as TIMESTAMP) >= f.data_inizio_validita and f.data_fine_validita is null)) ";
		Query query = em.createNativeQuery(sqlQuery);
		
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();

		List<ModelFondi> fondi = new ArrayList<ModelFondi>();

		for (Object[] m : result) {
			ModelFondi ma = new ModelFondi();
			ma.setIdFondo((Integer) m[0]);
			ma.setCodFondo((String) m[1]);
			ma.setDescFondo((String) m[2]);
			ma.setCodTipologiaFondo((String) m[3]);
			ma.setLeps(m[4]!=null ? (Boolean) m[4] : false);
			fondi.add(ma);
		}
		return fondi;
	}
	
	public List<ModelFondi> findRegole() {

		String sqlQuery = "select r.id_regola, r.codice_regola, r.descrizione_regola, r.funzione1_regola "
				+ "from greg_c_regola r  "
				+ "where r.descrizione_regola = 'Somma' or r.descrizione_regola = 'Sottrazione'";
		Query query = em.createNativeQuery(sqlQuery);
		
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();

		List<ModelFondi> fondi = new ArrayList<ModelFondi>();

		for (Object[] m : result) {
			ModelFondi ma = new ModelFondi();
			ma.setIdRegola((Integer) m[0]);
			ma.setCodRegola((String) m[1]);
			ma.setDescRegola((String) m[2]);
			ma.setFunzioneRegola((String) m[3]);
			fondi.add(ma);
		}
		return fondi;
	}
	
	public boolean azioneIsModificabile(Integer idEnte, Integer idFondo) {

		String sqlQuery = "select count(*)>0 "
				+ "from greg_r_spese_fnps sf,  "
				+ "greg_d_fondi f,  "
				+ "greg_t_rendicontazione_ente re "
				+ "where  "
				+ "sf.id_rendicontazione_ente = re.id_rendicontazione_ente  "
				+ "and  "
				+ "sf.id_fondo = f.id_fondo  "
				+ "and  "
				+ "sf.data_cancellazione is null "
				+ "and  "
				+ "f.data_cancellazione is null  "
				+ "and  "
				+ "re.data_cancellazione is null  "
				+ "and  "
				+ "sf.valore > 0 "
				+ "and "
				+ "re.id_rendicontazione_ente = :idEnte "
				+ "and  "
				+ "f.id_fondo = :idFondo";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idEnte", idEnte);
		query.setParameter("idFondo", idFondo);
		boolean result = (boolean) query.getSingleResult();

		return result;
	}
	
	public GregRRendicontazioneFondi findFondoRendiscontazionebyId(Integer idFondoRendicontazione) {
		try {
		String hqlQuery = "SELECT u FROM GregRRendicontazioneFondi u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idRendicontazioneFondi = :idFondoRendicontazione ";

		TypedQuery<GregRRendicontazioneFondi> query = em.createQuery(hqlQuery, GregRRendicontazioneFondi.class);
		query.setParameter("idFondoRendicontazione", idFondoRendicontazione);
		return query.getSingleResult();
		}catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneFondi aggiornaFondoRendicontazione(GregRRendicontazioneFondi fondo) {
		return em.merge(fondo);
	}
	
	public GregCRegola findRegolabyId(Integer idRegola) {

		String hqlQuery = "SELECT u FROM GregCRegola u " + "WHERE "
				+ " u.idRegola = :idRegola ";

		TypedQuery<GregCRegola> query = em.createQuery(hqlQuery, GregCRegola.class);
		query.setParameter("idRegola", idRegola);
		return query.getSingleResult();
	}
	

	public GregDFondi findFondobyId(Integer idFondo) {

		String hqlQuery = "SELECT u FROM GregDFondi u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idFondo = :idFondo ";

		TypedQuery<GregDFondi> query = em.createQuery(hqlQuery, GregDFondi.class);
		query.setParameter("idFondo", idFondo);
		return query.getSingleResult();
	}
	
	public GregRSpeseFnps findSpesaFnpsbyId(Integer idFondo, Integer idRendicontazione) {
		try {
		String hqlQuery = "SELECT u FROM GregRSpeseFnps u " + "WHERE "
				+ " u.gregDFondi.idFondo = :idFondo "
				+ " AND u.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione";

		TypedQuery<GregRSpeseFnps> query = em.createQuery(hqlQuery, GregRSpeseFnps.class);
		query.setParameter("idFondo", idFondo);
		query.setParameter("idRendicontazione", idRendicontazione);
		return query.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}
	}
}
