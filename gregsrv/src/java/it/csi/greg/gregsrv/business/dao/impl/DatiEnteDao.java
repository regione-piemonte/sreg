/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.greg.gregsrv.business.entity.GregDAsl;
import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDMotivazione;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDProvince;
import it.csi.greg.gregsrv.business.entity.GregDStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;
import it.csi.greg.gregsrv.business.entity.GregDTipologiaSpesa;
import it.csi.greg.gregsrv.business.entity.GregDVoceIstat;
import it.csi.greg.gregsrv.business.entity.GregRCartaServiziPreg1;
import it.csi.greg.gregsrv.business.entity.GregREnteTab;
import it.csi.greg.gregsrv.business.entity.GregRMergeEnti;
import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregREnteGestoreContatti;
import it.csi.greg.gregsrv.business.entity.GregREnteGestoreStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregRResponsabileContatti;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.business.entity.GregTAllegatiRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTLista;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTNomenclatoreNazionale;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniMinisteriali;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali2;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTResponsabileEnteGestore;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.ModelComune;
import it.csi.greg.gregsrv.dto.ModelComuniAssociati;
import it.csi.greg.gregsrv.dto.ModelCronogiaStato;
import it.csi.greg.gregsrv.dto.ModelDatiAnagrafici;
import it.csi.greg.gregsrv.dto.ModelDatiEnte;
import it.csi.greg.gregsrv.dto.ModelDatiEnteRendicontazione;
import it.csi.greg.gregsrv.dto.ModelMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelMotivazioni;
import it.csi.greg.gregsrv.dto.ModelPrest1Prest2;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestMin;
import it.csi.greg.gregsrv.dto.ModelPrestUtenza;
import it.csi.greg.gregsrv.dto.ModelProvincia;
import it.csi.greg.gregsrv.dto.RicercaListaEntiDaunire;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;

@Repository("datiEnteDao")
@Transactional(readOnly = true)
public class DatiEnteDao {
	@PersistenceContext
	private EntityManager em;

	public GregTSchedeEntiGestori findScheda(Integer idEnte) {
		TypedQuery<GregTSchedeEntiGestori> query = em.createNamedQuery("GregTSchedeEntiGestori.findByIdNotDeleted",
				GregTSchedeEntiGestori.class);
		query.setParameter("idEnte", idEnte);
		return query.getSingleResult();
	}

	public ModelDatiAnagrafici findSchedaEnte(Integer idEnte, String codregione, Date dataValidita) {
		String sqlQuery = "select " + "	s.codice_regionale, " + "	s.codice_fiscale as codiceFiscaleEnte, "
				+ "	e.denominazione, " + "	e.partita_iva, " + "	t.cod_tipo_ente, " + "	t.des_tipo_ente, "
				+ "	c.cod_istat_comune, " + "	c.des_comune, " + "	e.cod_istat_ente, " + "	p.cod_istat_provincia, "
				+ "	p.des_provincia, " + "	a.cod_asl, " + "	a.des_asl, " + "	e.email as emailEnte, "
				+ "	e.telefono as telefonoEnte, " + "	e.pec, " + "	r.nome, " + "	r.cognome, "
				+ "	r.codice_fiscale as codiceFiscaleResponsabile, " + "	rc.cellulare, "
				+ "	rc.telefono telefonoResponsabile, " + "	rc.email as emailResponsabile " + "from "
				+ "	greg_t_schede_enti_gestori s  "
				+ "	left join greg_r_ente_gestore_contatti e on s.id_scheda_ente_gestore = e.id_scheda_ente_gestore "
				+ "	left join greg_d_tipo_ente t on t.id_tipo_ente = e.id_tipo_ente "
				+ "	left join greg_d_comuni c on c.id_comune = e.id_comune "
				+ "	left join greg_d_province p on c.id_provincia = p.id_provincia "
				+ "	left join greg_d_asl a on e.id_asl = a.id_asl "
				+ "	left join greg_r_responsabile_contatti rc on e.id_responsabile_contatti = rc.id_responsabile_contatti and ((rc.data_Inizio_Validita <= :dataValidita "
				+ "		and rc.data_fine_validita >= :dataValidita) "
				+ "	or (rc.data_Inizio_Validita <= :dataValidita " + "		and rc.data_fine_validita is null)) "
				+ "	left join greg_t_responsabile_ente_gestore r on rc.id_responsabile_ente_gestore = r.id_responsabile_ente_gestore and r.data_cancellazione is null "
				+ "	left join greg_d_regione b on b.id_regione = p.id_regione " + "where "
				+ "	s.id_scheda_ente_gestore = :idScheda " + "	and e.data_fine_validita is null "
				+ "	and b.cod_regione = :codregione " + "	and ((c.inizio_Validita_Comune <= :dataValidita "
				+ "		and c.fine_Validita_Comune >= :dataValidita) "
				+ "	or (c.inizio_Validita_Comune <= :dataValidita " + "		and c.fine_Validita_Comune is null)) "
				+ "	and ((p.data_Inizio_Validita <= :dataValidita " + "		and p.data_fine_validita >= :dataValidita) "
				+ "	or (p.data_Inizio_Validita <= :dataValidita " + "		and p.data_fine_validita is null)) "
				+ "	and ((a.data_Inizio_Validita <= :dataValidita " + "		and a.data_fine_validita >= :dataValidita) "
				+ "	or (a.data_Inizio_Validita <= :dataValidita " + "		and a.data_fine_validita is null)) "
				+ "	and s.data_cancellazione is null " + "	and t.data_cancellazione is null "
				+ "	and c.data_cancellazione is null " + "	and p.data_cancellazione is null "
				+ "	and a.data_cancellazione is null " + "	and b.data_cancellazione is null";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idScheda", idEnte);
		query.setParameter("codregione", codregione);
		query.setParameter("dataValidita", dataValidita);
		Object[] obj = (Object[]) query.getSingleResult();
		ModelDatiAnagrafici datiente = new ModelDatiAnagrafici(obj);

		return datiente;
//		return query.getSingleResult();
	}

	public ModelDatiEnte findSchedaEntexAnno(Integer idRendicontazioneEnte, Integer annoGestione) {

		Query query = em.createNativeQuery(""
				+ "with schedeenti as (select max(id_ente_gestore_contatti) idcontatti,id_responsabile_contatti idcontattiresp,id_scheda_ente_gestore from greg_r_ente_gestore_contatti "
				+ "where ('" + annoGestione + "-12-31' BETWEEN data_inizio_validita and data_fine_validita) "
				+ "or (data_inizio_validita <= '" + annoGestione + "-12-31' and data_fine_validita is NULL) "
				+ "group by id_scheda_ente_gestore,id_responsabile_contatti) "
				+ "select a.id_scheda_ente_gestore,a.codice_regionale ,a.codice_fiscale, "
				+ "b.denominazione ,b.partita_iva ,b.email ,b.telefono ,b.pec ,b.cod_istat_ente, "
				+ "f.id_responsabile_ente_gestore ,f.cognome ,f.nome,f.codice_fiscale CFResponsabile, "
				+ "h.cellulare ,h.telefono TelResponsabile,h.email EmailResponsabile, "
				+ "i.id_comune ,i.cod_istat_comune ,i.des_comune ,l.des_provincia ,l.id_provincia ,l.cod_istat_provincia , "
//				+ "c.pippi ,c.fnps ,c.vincolo_fondo ,"
				+ "c.struttura_residenziale ,c.centro_diurno_strutt_semires , "
				+ "m.id_tipo_ente ,m.cod_tipo_ente ,m.des_tipo_ente , " + "n.id_asl ,n.cod_asl ,n.des_asl , "
				+ "o.id_stato_rendicontazione ,o.cod_stato_rendicontazione ,o.des_stato_rendicontazione "
				+ "from greg_t_schede_enti_gestori a,greg_t_rendicontazione_ente c,schedeenti d, "
				+ "greg_t_responsabile_ente_gestore f,greg_r_responsabile_contatti h, "
				+ "greg_d_tipo_ente m,greg_d_stato_rendicontazione o,greg_r_ente_gestore_contatti b "
				+ "left join greg_d_comuni i on i.id_comune = b.id_comune "
				+ "left join greg_d_province l on l.id_provincia = i.id_provincia "
				+ "left join greg_d_asl n on n.id_asl = b.id_asl "
				+ "where a.id_scheda_ente_gestore = b.id_scheda_ente_gestore "
				+ "and c.id_scheda_ente_gestore = a.id_scheda_ente_gestore " + "and c.anno_gestione = :annoGestione "
				+ "and c.id_rendicontazione_ente = :idRendicontazioneEnte "
				+ "and d.idcontatti=b.id_ente_gestore_contatti "
				+ "and f.id_responsabile_ente_gestore =h.id_responsabile_ente_gestore "
				+ "and h.id_responsabile_contatti = d.idcontattiresp "
				+ "and b.id_responsabile_contatti = h.id_responsabile_contatti " + "and m.id_tipo_ente =b.id_tipo_ente "
				+ "and o.id_stato_rendicontazione = c.id_stato_rendicontazione " + "and a.data_cancellazione is null "
				+ "and c.data_cancellazione is null " + "and f.data_cancellazione is null "
				+ "and i.data_cancellazione is null " + "and l.data_cancellazione is null "
				+ "and m.data_cancellazione is null " + "and n.data_cancellazione is null "
				+ "and o.data_cancellazione is null");

		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("annoGestione", annoGestione);
		Object[] obj = (Object[]) query.getSingleResult();

		ModelDatiEnte datiente = new ModelDatiEnte(obj);

		return datiente;
	}

	public ModelDatiEnteRendicontazione findDatiEntexAnno(Integer idRendicontazioneEnte, Integer annoGestione) {

		Query query = em.createNativeQuery(""
				+ "with schedeenti as (select max(id_ente_gestore_contatti) idcontatti,id_responsabile_contatti idcontattiresp,id_scheda_ente_gestore from greg_r_ente_gestore_contatti "
				+ "where ('" + annoGestione + "-12-31' BETWEEN data_inizio_validita and data_fine_validita) "
				+ "or (data_inizio_validita <= '" + annoGestione + "-12-31' and data_fine_validita is NULL) "
				+ "group by id_scheda_ente_gestore,id_responsabile_contatti) "
				+ "select b.denominazione,c.cod_tipo_ente " + "from greg_t_rendicontazione_ente a,schedeenti d, "
				+ "greg_d_tipo_ente c,greg_r_ente_gestore_contatti b "
				+ "where a.id_scheda_ente_gestore = b.id_scheda_ente_gestore " + "and a.anno_gestione = :annoGestione "
				+ "and a.id_rendicontazione_ente = :idRendicontazioneEnte "
				+ "and d.idcontatti=b.id_ente_gestore_contatti " + "and c.id_tipo_ente =b.id_tipo_ente "
				+ "and a.data_cancellazione is null " + "and c.data_cancellazione is null");

		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("annoGestione", annoGestione);
		Object[] obj = (Object[]) query.getSingleResult();

		ModelDatiEnteRendicontazione datiente = new ModelDatiEnteRendicontazione(obj);

		return datiente;
	}

	public GregTResponsabileEnteGestore findResponsabileEnte(String codiceFiscale) {
		try {
			TypedQuery<GregTResponsabileEnteGestore> query = em.createNamedQuery(
					"GregTResponsabileEnteGestore.findByCodNotDeleted", GregTResponsabileEnteGestore.class);
			query.setParameter("codiceFiscale", codiceFiscale);
			GregTResponsabileEnteGestore resp = query.getSingleResult();
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	public GregRResponsabileContatti findResponsabileContatti(String codiceFiscale) {
		TypedQuery<GregRResponsabileContatti> query = em.createNamedQuery("GregRResponsabileContatti.findLastContatti",
				GregRResponsabileContatti.class);
		query.setParameter("codiceFiscale", codiceFiscale);
		return query.getSingleResult();
	}

	public GregRResponsabileContatti findResponsabileContattiByCod(Integer idRespContatti) {
		TypedQuery<GregRResponsabileContatti> query = em
				.createNamedQuery("GregRResponsabileContatti.findLastContattiByCod", GregRResponsabileContatti.class);
		query.setParameter("idRespContatti", idRespContatti);
		return query.getSingleResult();
	}

	public List<GregDProvince> findAllProvince(String codregione, Date dataValidita) {
		TypedQuery<GregDProvince> query = null;
		if (dataValidita != null) {
			query = em.createNamedQuery("GregDProvince.findAllNotDeleted", GregDProvince.class);
			query.setParameter("dataValidita", dataValidita);
			query.setParameter("codregione", codregione);
		} else
			query = em.createNamedQuery("GregDProvince.findAllStorico", GregDProvince.class);

		return query.getResultList();
	}

	public List<GregTPrestazioniRegionali1> findAllPrestazioniMadre(Integer anno) {

		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g "
				+ "LEFT JOIN Fetch GregDTipologia gdt ON g.gregDTipologia.idTipologia = gdt.idTipologia "
				+ "LEFT JOIN Fetch GregDTipologiaQuota gdtq ON g.gregDTipologiaQuota.idTipologiaQuota = gdtq.idTipologiaQuota "
				+ "WHERE g.dataCancellazione IS NULL AND gdtq.dataCancellazione IS NULL "
				+ "AND coalesce(gdtq.codTipologiaQuota,'00') NOT IN ('01','02','03') "
				+ "and (:anno - year(g.dataInizioValidita)>=0 "
				+ "and (:anno - year(g.dataFineValidita)<=0 or g.dataFineValidita is null)) "
				+ "AND gdt.codTipologia <> 'MA03' " + "AND gdt.dataCancellazione is null "
				+ "AND gdtq.dataCancellazione is null " + "ORDER BY g.codPrestReg1";

		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("anno", anno);
		return query.getResultList();
	}

	public List<GregTPrestazioniRegionali1> findPrestazioniFiglie(Integer idPrestazioneMadre, Integer anno) {

		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g "
				+ "LEFT JOIN Fetch GregDTipologiaQuota gdtq ON g.gregDTipologiaQuota.idTipologiaQuota = gdtq.idTipologiaQuota "
				+ "WHERE g.dataCancellazione IS NULL AND gdtq.dataCancellazione IS NULL "
				+ "AND coalesce(gdtq.codTipologiaQuota,'00') IN ('01','02','03') "
				+ "AND g.gregTPrestazioniRegionali1.idPrestReg1 = :idPrestazioneMadre "
				+ "and (:anno - year(g.dataInizioValidita)>=0 "
				+ "and (:anno - year(g.dataFineValidita)<=0 or g.dataFineValidita is null)) "
				+ "AND gdtq.dataCancellazione is null " + "ORDER BY g.codPrestReg1";

		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("idPrestazioneMadre", idPrestazioneMadre);
		query.setParameter("anno", anno);
		return query.getResultList();
	}

	public List<GregTPrestazioniRegionali1> findPrestazioniResSemires(String codTipologia, String codTipoStruttura) {

		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g "
				+ "LEFT JOIN Fetch GregDTipologia gdt ON g.gregDTipologia.idTipologia = gdt.idTipologia "
				+ "LEFT JOIN Fetch GregDTipoStruttura gdts ON g.gregDTipoStruttura.idTipoStruttura = gdts.idTipoStruttura "
				+ "WHERE g.dataCancellazione IS NULL " + "AND gdt.dataCancellazione IS NULL "
				+ "AND gdts.dataCancellazione IS NULL " + "AND gdt.codTipologia = :codTipologia "
				+ "AND gdts.codTipoStruttura = :codTipoStruttura " + "ORDER BY g.codPrestReg1 ";

		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("codTipologia", codTipologia);
		query.setParameter("codTipoStruttura", codTipoStruttura);
		return query.getResultList();
	}

	public List<GregRSchedeEntiGestoriComuni> findComuniAssociati(Integer idScheda, Integer annoGestione) {

		String hqlQuery = "SELECT g FROM GregRSchedeEntiGestoriComuni g WHERE g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idScheda "
				+ "AND ((" + annoGestione + " - date_part('year', g.dataInizioValidita)>=0 and " + annoGestione
				+ " - date_part('year', g.dataFineValidita)<=0) " + "or (" + annoGestione
				+ " - date_part('year', g.dataInizioValidita)>=0 and g.dataFineValidita is NULL)) "
				+ "AND ((" + annoGestione + " - date_part('year', g.gregDComuni.inizioValiditaComune)>=0 and " + annoGestione
				+ " - date_part('year', g.gregDComuni.fineValiditaComune)<=0) " + "or (" + annoGestione
				+ " - date_part('year', g.gregDComuni.inizioValiditaComune)>=0 and g.gregDComuni.fineValiditaComune is NULL)) "
				+ "AND g.dataCancellazione IS NULL ORDER BY g.gregDComuni.desComune";
		TypedQuery<GregRSchedeEntiGestoriComuni> query = em.createQuery(hqlQuery, GregRSchedeEntiGestoriComuni.class);

		query.setParameter("idScheda", idScheda);
		return query.getResultList();
	}

	public List<ModelComuniAssociati> findComuniAnagraficaAssociati(Integer idScheda) {

		String hqlQuery = "select s.id_scheda_ente_gestore, c.cod_istat_comune, c.des_comune,to_char(sc.data_inizio_validita,'dd/mm/yyyy'),c.id_comune "
				+ "from greg_t_schede_enti_gestori s, " + "greg_r_schede_enti_gestori_comuni sc, " + "greg_d_comuni c  "
				+ "where  " + "s.id_scheda_ente_gestore = :idScheda " + "and  "
				+ "s.id_scheda_ente_gestore = sc.id_scheda_ente_gestore  " + "and  " + "c.id_comune = sc.id_comune  "
				+ "and  " + "c.data_cancellazione is null  " + "and  "
				+ "sc.data_fine_validita is null and sc.data_cancellazione is null "
				+ "and ((c.inizio_validita_comune <= now() and c.fine_validita_comune is null) or (c.inizio_validita_comune <= now() and c.fine_validita_comune > now())) "
				+ "and s.data_cancellazione is null order by c.des_comune";

		Query query = em.createNativeQuery(hqlQuery);
		query.setParameter("idScheda", idScheda);
		ArrayList<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
		List<ModelComuniAssociati> lista = new ArrayList<ModelComuniAssociati>();
		for (Object[] o : obj) {
			ModelComuniAssociati comune = new ModelComuniAssociati(o);
			lista.add(comune);
		}

		return lista;
	}

	public List<ModelComuniAssociati> findComuniAnagraficaAssociatiEliminati(Integer idScheda, String dataValidita) {

		String hqlQuery = "select s.id_scheda_ente_gestore, c.cod_istat_comune, c.des_comune,to_char(sc.data_fine_validita,'dd/mm/yyyy'),c.id_comune "
				+ "from greg_t_schede_enti_gestori s, " + "greg_r_schede_enti_gestori_comuni sc, " + "greg_d_comuni c  "
				+ "where  " + "s.id_scheda_ente_gestore = :idScheda " + "and  "
				+ "s.id_scheda_ente_gestore = sc.id_scheda_ente_gestore  " + "and  " + "c.id_comune = sc.id_comune  "
				+ "and  " + "c.data_cancellazione is null  " + "and sc.data_fine_validita is not null "
				+ "and sc.data_cancellazione is null " + "and s.data_cancellazione is null order by c.des_comune";

		Query query = em.createNativeQuery(hqlQuery);
		query.setParameter("idScheda", idScheda);
		// query.setParameter("dataValidita", dataValidita);
		ArrayList<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
		List<ModelComuniAssociati> lista = new ArrayList<ModelComuniAssociati>();
		for (Object[] o : obj) {
			ModelComuniAssociati comune = new ModelComuniAssociati(o);
			lista.add(comune);
		}

		return lista;
	}

	public List<GregRCartaServiziPreg1> findPrestazioniAssociate(Integer idScheda) {
		TypedQuery<GregRCartaServiziPreg1> query = em
				.createNamedQuery("GregRCartaServiziPreg1.findByIdSchedaEnteNotDeleted", GregRCartaServiziPreg1.class);
		query.setParameter("idScheda", idScheda);
//		query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);
		return query.getResultList();
	}

	//Prende solo i file relativi alla sezione dei dati ente
	public List<GregTAllegatiRendicontazione> findAllAllegatiAssociati(Integer idScheda) {
		String hqlQuery = "SELECT g FROM GregTAllegatiRendicontazione g "
				+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
				+ "AND g.dataCancellazione IS NULL " + "ORDER BY g.nomeFile";

		TypedQuery<GregTAllegatiRendicontazione> query = em.createQuery(hqlQuery, GregTAllegatiRendicontazione.class);
		query.setParameter("idScheda", idScheda);
		return query.getResultList();
	}

	public GregDTipoEnte findTipoEnte(Integer idTipoEnte) {
		TypedQuery<GregDTipoEnte> query = em.createNamedQuery("GregDTipoEnte.findByIdNotDeleted", GregDTipoEnte.class);
		query.setParameter("idTipoEnte", idTipoEnte);
		return query.getSingleResult();
	}

	public GregDComuni findComuneByIdNotDeleted(Integer idComune) {
		TypedQuery<GregDComuni> query = em.createNamedQuery("GregDComuni.findByIdNotDeleted", GregDComuni.class);
		query.setParameter("idComune", idComune);
		return query.getSingleResult();
	}

	public GregDAsl findAsl(Integer idAsl) {
		TypedQuery<GregDAsl> query = em.createNamedQuery("GregDAsl.findByIdNotDeleted", GregDAsl.class);
		query.setParameter("idAsl", idAsl);
		return query.getSingleResult();
	}

	public GregTPrestazioniRegionali1 findPrestazione(Integer idPrestazione) {
		TypedQuery<GregTPrestazioniRegionali1> query = em
				.createNamedQuery("GregTPrestazioniRegionali1.findByIdNotDeleted", GregTPrestazioniRegionali1.class);
		query.setParameter("idPrestazione", idPrestazione);
		return query.getSingleResult();
	}

	public GregTSchedeEntiGestori saveDatiEnte(GregTSchedeEntiGestori schedaToUpdate) {
		return em.merge(schedaToUpdate);
	}

	public GregREnteGestoreContatti saveDatiAnagrafici(GregREnteGestoreContatti schedaToUpdate) {
		return em.merge(schedaToUpdate);
	}

	public GregTResponsabileEnteGestore saveResponsabileEnte(GregTResponsabileEnteGestore respToupdate) {
		return em.merge(respToupdate);
	}

	public GregRResponsabileContatti saveResponsabileContatti(GregRResponsabileContatti respToupdate) {
		return em.merge(respToupdate);
	}

	public GregRSchedeEntiGestoriComuni updateComuneAssociato(GregRSchedeEntiGestoriComuni comune) {
		return em.merge(comune);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveNewComuneAssociato(GregRSchedeEntiGestoriComuni comune) {
		em.persist(comune);
	}

	public GregRCartaServiziPreg1 updatePrestazioneAssociata(GregRCartaServiziPreg1 prestazione) {
		return em.merge(prestazione);
	}

	public GregTAllegatiRendicontazione updateAllegatoAssociato(GregTAllegatiRendicontazione allegato) {
		return em.merge(allegato);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveNewPrestazioneAssociata(GregRCartaServiziPreg1 prestazione) {
		em.persist(prestazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertAllegato(GregTAllegatiRendicontazione allegato) {
		em.persist(allegato);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertCronologia(GregTCronologia cronologia) {
		em.persist(cronologia);
	}

	public List<GregDAsl> findAllAsl(String codregione, Date dataValidita) {
		TypedQuery<GregDAsl> query = null;
		if (dataValidita != null) {
			query = em.createNamedQuery("GregDAsl.findAllNotDeleted", GregDAsl.class);
			query.setParameter("dataValidita", dataValidita);
			query.setParameter("codregione", codregione);
		} else {
			query = em.createNamedQuery("GregDAsl.findAllStorico", GregDAsl.class);
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object> findAllegatiAssociati(Integer idScheda) {
		Query query = em.createNativeQuery(""
				+ "SELECT g.id_Allegati_Rendicontazione, g.file_Size, g.nome_File, g.note_File, g.tipo_Documentazione, g.utente_Operazione "
				+ "FROM Greg_T_Allegati_Rendicontazione g " + "WHERE g.id_rendicontazione_ente = :idScheda "
				+ "AND g.data_Cancellazione IS NULL " + " AND g.tipo_Documentazione <> '" + SharedConstants.AL_ZERO + "' " + "ORDER BY g.nome_File");

		query.setParameter("idScheda", idScheda);

		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		return result;
	}

	public GregTAllegatiRendicontazione getAllegatoToDownload(Integer idAllegato) {
		TypedQuery<GregTAllegatiRendicontazione> query = em.createNamedQuery(
				"GregTAllegatiRendicontazione.findByIdNotDeleted", GregTAllegatiRendicontazione.class);
		query.setParameter("idAllegato", idAllegato);
		return query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<String> getListaPrestazioniValorizzateModA(Integer idScheda) {

		Query query = em.createNativeQuery("select distinct p2.cod_prest_reg_1  "
				+ "from greg_t_prestazioni_regionali_1 p2  " + "where p2.id_prest_reg_1 in (  "
				+ "select distinct p2.collegato_a_id_prest_reg_1  " + "from greg_t_rendicontazione_ente g  "
				+ "left join greg_r_rendicontazione_mod_a_part2 r on g.id_rendicontazione_ente = r.id_rendicontazione_ente  "
				+ "left join greg_r_prest_reg1_utenze_regionali1 p on r.id_prest_reg1_utenza_regionale1 = p.id_prest_reg1_utenza_regionale1  "
				+ "left join greg_t_prestazioni_regionali_1 p2 on p.id_prest_reg_1 = p2.id_prest_reg_1  "
				+ "where g.id_rendicontazione_ente  = :idScheda  " + "and r.valore is not null  "
				+ "and g.data_cancellazione is null  " + "and r.data_cancellazione is null  "
				+ "and p.data_cancellazione is null  " + "and p2.data_cancellazione is null )");

		query.setParameter("idScheda", idScheda);

		List<String> result = (List<String>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getListaPrestazioniValorizzateModB1(Integer idScheda) {
		String q = "select distinct p2.cod_prest_reg_1   " + "from greg_t_prestazioni_regionali_1 p2   "
				+ "where p2.id_prest_reg_1 in (   " + "select distinct p2.id_prest_reg_1   "
				+ "from greg_t_rendicontazione_ente g   "
				+ "left join greg_r_rendicontazione_preg1_macro r on g.id_rendicontazione_ente = r.id_rendicontazione_ente   "
				+ "left join greg_r_prest_reg1_macroaggregati_bilancio macro on macro.id_prest_reg1_macroaggregati_bilancio = r.id_prest_reg1_macroaggregati_bilancio   "
				+ "left join greg_t_prestazioni_regionali_1 p2 on macro.id_prest_reg_1 = p2.id_prest_reg_1   "
				+ "where g.id_rendicontazione_ente = :idScheda  " + "and r.valore is not null   "
				+ "and g.data_cancellazione is null   " + "and r.data_cancellazione is null   "
				+ "and p2.data_cancellazione is null   " + "union   " + "select distinct p2.id_prest_reg_1   "
				+ "from greg_t_rendicontazione_ente g   "
				+ "left join greg_r_rendicontazione_preg1_utereg1 r1 on g.id_rendicontazione_ente = r1.id_rendicontazione_ente   "
				+ "left join greg_r_prest_reg1_utenze_regionali1 ute on ute.id_prest_reg1_utenza_regionale1 = r1.id_prest_reg1_utenza_regionale1   "
				+ "left join greg_t_prestazioni_regionali_1 p2 on ute.id_prest_reg_1 = p2.id_prest_reg_1   "
				+ "where g.id_rendicontazione_ente = :idScheda  " + "and r1.valore is not null   "
				+ "and g.data_cancellazione is null   " + "and r1.data_cancellazione is null   "
				+ "and p2.data_cancellazione is null   " + ")   " + "union   " + "select distinct p1.cod_prest_reg_1   "
				+ "from greg_t_prestazioni_regionali_2 p3   "
				+ "left join greg_r_prest_reg1_prest_reg2 p4 on p4.id_prest_reg_2 = p3.id_prest_reg_2   "
				+ "left join greg_t_prestazioni_regionali_1 p1 on p4.id_prest_reg_1 = p1.id_prest_reg_1    "
				+ "where p3.id_prest_reg_2 in (   " + "select distinct p3.id_prest_reg_2   "
				+ "from greg_t_rendicontazione_ente g   "
				+ "left join greg_r_rendicontazione_preg2_utereg2 r3 on g.id_rendicontazione_ente = r3.id_rendicontazione_ente   "
				+ "left join greg_r_prest_reg2_utenze_regionali2 ute2 on ute2.id_prest_reg2_utenza_regionale2 = r3.id_prest_reg2_utenza_regionale2   "
				+ "left join greg_t_prestazioni_regionali_2 p3 on ute2.id_prest_reg_2 = p3.id_prest_reg_2  "
				+ "where g.id_rendicontazione_ente = :idScheda  " + "and r3.valore is not null   "
				+ "and g.data_cancellazione is null   " + "and r3.data_cancellazione is null   "
				+ "and p3.data_cancellazione is null)";
		Query query = em.createNativeQuery(q);

		query.setParameter("idScheda", idScheda);
		List<String> result = (List<String>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getListaPrestazioniValorizzateModC(Integer idScheda) {
		String q = "select distinct p2.cod_prest_reg_1  " + "from greg_t_rendicontazione_ente g  "
				+ "left join greg_r_rendicontazione_mod_c_parte1 r on g.id_rendicontazione_ente = r.id_rendicontazione_ente  "
				+ "left join greg_r_prest_reg1_utenze_modc p on r.id_prest_reg1_utenza_modc = p.id_prest_reg1_utenza_modc  "
				+ "left join greg_t_prestazioni_regionali_1 p2 on p.id_prest_reg_1 = p2.id_prest_reg_1  "
				+ "where g.id_rendicontazione_ente = :idScheda  " + "and r.valore is not null  " + "and r.valore !=0  "
				+ "and g.data_cancellazione is null  " + "and r.data_cancellazione is null  "
				+ "and p.data_cancellazione is null  " + "and p2.data_cancellazione is null";
		Query query = em.createNativeQuery(q);

		query.setParameter("idScheda", idScheda);
		List<String> result = (List<String>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getListaComuniValorizzatiModA1(Integer idScheda) {

		Query query = em.createNativeQuery("" + "select distinct cod_istat_comune "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_r_rendicontazione_mod_a1 r on g.id_rendicontazione_ente = r.id_rendicontazione_ente "
				+ "left join greg_d_comuni c on r.id_comune = c.id_comune "
				+ "where g.id_rendicontazione_ente = :idScheda " + "and r.valore is not null "
				+ "and g.data_cancellazione is null " + "and r.data_cancellazione is null "
				+ "and c.data_cancellazione is null ");

		query.setParameter("idScheda", idScheda);

		List<String> result = (List<String>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getListaComuniValorizzatiModA2(Integer idScheda) {

		Query query = em.createNativeQuery("" + "select distinct cod_istat_comune "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_r_rendicontazione_comune_ente_mod_a2 r1 on g.id_rendicontazione_ente = r1.id_rendicontazione_ente "
				+ "left join greg_d_comuni c on r1.id_comune = c.id_comune "
				+ "where g.id_rendicontazione_ente = :idScheda " + "and r1.valore is not null "
				+ "and c.data_cancellazione is null " + "and g.data_cancellazione is null "
				+ "and r1.data_cancellazione is null " + "union " + "select distinct cod_istat_comune "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_r_rendicontazione_ente_comune_mod_a2 r1 on g.id_rendicontazione_ente = r1.id_rendicontazione_ente "
				+ "left join greg_d_comuni c on r1.id_comune = c.id_comune "
				+ "where g.id_rendicontazione_ente = :idScheda " + "and r1.valore is not null "
				+ "and g.data_cancellazione is null " + "and r1.data_cancellazione is null "
				+ "and c.data_cancellazione is null ");

		query.setParameter("idScheda", idScheda);

		List<String> result = (List<String>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getListaComuniValorizzatiModE(Integer idScheda) {
		Query query = em.createNativeQuery("" + "select distinct cod_istat_comune "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_r_rendicontazione_mod_e r on g.id_rendicontazione_ente = r.id_rendicontazione_ente "
				+ "left join greg_d_comuni c on r.id_comune = c.id_comune "
				+ "where g.id_rendicontazione_ente = :idScheda " + "and r.valore is not null "
				+ "and g.data_cancellazione is null " + "and r.data_cancellazione is null "
				+ "and c.data_cancellazione is null ");

		query.setParameter("idScheda", idScheda);

		List<String> result = (List<String>) query.getResultList();
		return result;
	}

	public GregTRendicontazioneEnte saveRendicontazioneEnte(GregTRendicontazioneEnte schedaToUpdate) {
		return em.merge(schedaToUpdate);
	}

	public GregREnteTab updateModelloAssociato(GregREnteTab modello) {
		return em.merge(modello);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertModello(GregREnteTab modello) {
		em.persist(modello);
	}

	public GregREnteGestoreContatti findLastContatti(Integer idEnte) {
		TypedQuery<GregREnteGestoreContatti> query = em.createNamedQuery("GregREnteGestoreContatti.findLastContatti",
				GregREnteGestoreContatti.class);
		query.setParameter("idEnte", idEnte);
		return query.getSingleResult();
	}

	public List<String> findComuniAssegnati(Integer idEnte, List<ModelComuniAssociati> comuni) {
		String sqlQuery = "select c.cod_istat_comune  " + "from greg_d_comuni c, "
				+ "greg_r_schede_enti_gestori_comuni sc, " + "greg_t_schede_enti_gestori s " + "where "
				+ "s.id_scheda_ente_gestore  <> :idScheda  " + "and "
				+ "s.id_scheda_ente_gestore = sc.id_scheda_ente_gestore  " + "and  " + "sc.id_comune = c.id_comune  "
				+ "and  " + "sc.data_fine_validita is null  " + "and  " + "c.data_cancellazione is null  "
				+ "and sc.data_cancellazione is null " + "and s.data_cancellazione is null "
				+ "and c.cod_istat_comune in (";
		for (ModelComuniAssociati comune : comuni) {
			sqlQuery += "'" + comune.getCodiceIstat() + "', ";
		}
		sqlQuery += "'0')";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idScheda", idEnte);
		List<String> obj = (List<String>) query.getResultList();

		return obj;
//		return query.getSingleResult();
	}

	public List<GregRSchedeEntiGestoriComuni> findGregRComuniAssegnati(Integer idEnte) {
		TypedQuery<GregRSchedeEntiGestoriComuni> query = em.createNamedQuery(
				"GregRSchedeEntiGestoriComuni.findByIdSchedaEnte", GregRSchedeEntiGestoriComuni.class);
		query.setParameter("idScheda", idEnte);
		return query.getResultList();
//		return query.getSingleResult();
	}

	public ModelDatiAnagrafici findSchedaAnagraficaStorico(Integer idEnte, Date dataFineValidita) {
		String sqlQuery = "select " + "	s.codice_regionale, " + "	s.codice_fiscale as codiceFiscaleEnte, "
				+ "	e.denominazione, " + "	e.partita_iva, " + "	t.cod_tipo_ente, " + "	t.des_tipo_ente, "
				+ "	c.cod_istat_comune, " + "	c.des_comune, " + "	e.cod_istat_ente, " + "	p.cod_istat_provincia, "
				+ "	p.des_provincia, " + "	a.cod_asl, " + "	a.des_asl, " + "	e.email as emailEnte, "
				+ "	e.telefono as telEnte, " + "	e.pec, " + "	r.nome, " + "	r.cognome, "
				+ "	r.codice_fiscale as codiceFiscaleResp, " + "	rc.cellulare, " + "	rc.telefono as telResp, "
				+ "	rc.email as emailResp,e.id_ente_gestore_contatti " + "from " + "	greg_t_schede_enti_gestori s, "
				+ "	greg_r_ente_gestore_contatti e, " + "	greg_t_responsabile_ente_gestore r, "
				+ "	greg_r_responsabile_contatti rc, " + "	greg_d_comuni c, " + "	greg_d_asl a, "
				+ "	greg_d_tipo_ente t ,greg_d_province p " + "where " + "	s.id_scheda_ente_gestore = :idScheda "
				+ "	and  " + "e.id_scheda_ente_gestore = s.id_scheda_ente_gestore " + "	and  "
				+ "e.id_comune = c.id_comune " + "	and " + "e.id_asl = a.id_asl " + "	and  "
				+ "e.id_tipo_ente = t.id_tipo_ente " + "	and  "
				+ "e.id_responsabile_contatti = rc.id_responsabile_contatti " + "	and  "
				+ "r.id_responsabile_ente_gestore = rc.id_responsabile_ente_gestore " + "	and  "
				+ "p.id_provincia = c.id_provincia " + "	and ((e.data_inizio_validita < :dataFineValidita "
				+ "		and e.data_fine_validita >= :dataFineValidita) " + "	or  "
				+ "(e.data_inizio_validita < :dataFineValidita " + "		and e.data_fine_validita is null)) "
				+ "	and ((rc.data_inizio_validita < :dataFineValidita "
				+ "		and rc.data_fine_validita >= :dataFineValidita) " + "	or  "
				+ "(rc.data_inizio_validita < :dataFineValidita " + "		and rc.data_fine_validita is null)) "
				+ "and s.data_cancellazione is null " + "and r.data_cancellazione is null "
				+ "and c.data_cancellazione is null " + "and a.data_cancellazione is null "
				+ "and p.data_cancellazione is null "
				+ "and t.data_cancellazione is null order by e.id_ente_gestore_contatti desc";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idScheda", idEnte);
		query.setParameter("dataFineValidita", dataFineValidita);
		List<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
		ModelDatiAnagrafici datiente = new ModelDatiAnagrafici(obj.get(0));

		return datiente;

	}

	public List<ModelComuniAssociati> findComuniAnagraficaAssociatiStorico(Integer idScheda, Date dataFineValidita) {

		String sqlQuery = "select " + "	s.id_scheda_ente_gestore, " + "	c.cod_istat_comune, "
				+ "	c.des_comune,to_char(sc.data_inizio_validita,'dd/mm/yyyy'),c.id_comune " + "from "
				+ "	greg_t_schede_enti_gestori s, " + "	greg_r_schede_enti_gestori_comuni sc, " + "	greg_d_comuni c "
				+ "where " + "	s.id_scheda_ente_gestore = :idScheda " + "	and  "
				+ "sc.id_scheda_ente_gestore = s.id_scheda_ente_gestore " + "	and  " + "sc.id_comune = c.id_comune "
				+ "	and " + " ((sc.data_inizio_validita < :dataFineValidita "
				+ "		and sc.data_fine_validita >= :dataFineValidita) " + "	or  "
				+ "(sc.data_inizio_validita < :dataFineValidita " + "		and sc.data_fine_validita is null)) "
				+ "and s.data_cancellazione is null " + "and sc.data_cancellazione is null "
				+ "and c.data_cancellazione is null order by c.des_comune";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idScheda", idScheda);
		query.setParameter("dataFineValidita", dataFineValidita);
		ArrayList<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
		List<ModelComuniAssociati> lista = new ArrayList<ModelComuniAssociati>();
		for (Object[] o : obj) {
			ModelComuniAssociati comune = new ModelComuniAssociati(o);
			lista.add(comune);
		}

		return lista;

	}

	public List<ModelComuniAssociati> findComuniAnagraficaEliminatiStorico(Integer idScheda, String annoInizioValidita,
			String annoFineValidita, Date dlong) {

		String annoAttuale = Converter.getAnno(new Date());
		int annoFine = 0;
		int annoInizio = 0;
		annoInizio = Converter.getInt(annoInizioValidita);
		annoFine = Converter.getInt(annoFineValidita);
		if (annoInizioValidita.equalsIgnoreCase(annoAttuale) && annoFineValidita.equalsIgnoreCase(annoAttuale))
			return null;
		if (annoFineValidita.equalsIgnoreCase(annoAttuale)) {
			annoFine = Converter.getInt(annoFineValidita) - 1;
		}
		List<ModelComuniAssociati> lista = new ArrayList<ModelComuniAssociati>();
		for (int i = 0; i <= annoFine - annoInizio; i++) {
			String sqlQuery = "select " + "	s.id_scheda_ente_gestore, " + "	c.cod_istat_comune, "
					+ "	c.des_comune,to_char(sc.data_fine_validita,'dd/mm/yyyy'),c.id_comune " + "from "
					+ "	greg_t_schede_enti_gestori s, " + "	greg_r_schede_enti_gestori_comuni sc, "
					+ "	greg_d_comuni c " + "where " + "	s.id_scheda_ente_gestore = :idScheda " + "	and  "
					+ "sc.id_scheda_ente_gestore = s.id_scheda_ente_gestore "
					+ "and  date_part('year',sc.data_fine_validita) - " + (annoInizio + i) + " =0 "
					+ "and sc.id_comune = c.id_comune " + "and s.data_cancellazione is null "
					+ "and sc.data_cancellazione is null " + "and c.data_cancellazione is null "
					+ "and c.cod_istat_comune not in ( " + "select " + "	c.cod_istat_comune " + "from "
					+ "	greg_t_schede_enti_gestori s, " + "	greg_r_schede_enti_gestori_comuni sc, "
					+ "	greg_d_comuni c " + "where " + "	s.id_scheda_ente_gestore = :idScheda " + "	and  "
					+ "sc.id_scheda_ente_gestore = s.id_scheda_ente_gestore " + "	and  "
					+ "sc.id_comune = c.id_comune " + "	and " + " ((sc.data_inizio_validita < :dataFineValidita "
					+ "		and sc.data_fine_validita >= :dataFineValidita) " + "	or  "
					+ "(sc.data_inizio_validita < :dataFineValidita " + "		and sc.data_fine_validita is null)) "
					+ "and s.data_cancellazione is null " + "and sc.data_cancellazione is null "
					+ "and c.data_cancellazione is null) order by c.des_comune";
			Query query = em.createNativeQuery(sqlQuery);
			query.setParameter("idScheda", idScheda);
			query.setParameter("dataFineValidita", dlong);
			ArrayList<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
			for (Object[] o : obj) {
				ModelComuniAssociati comune = new ModelComuniAssociati(o);
				lista.add(comune);
			}
		}

		return lista;

	}

	public List<ModelMotivazioni> getMotivazioniChiusura() {
		TypedQuery<GregDMotivazione> query = em.createNamedQuery("GregDMotivazione.findAllChiusura",
				GregDMotivazione.class);
		List<GregDMotivazione> motivazioni = query.getResultList();
		List<ModelMotivazioni> lista = new ArrayList<ModelMotivazioni>();
		for (GregDMotivazione mot : motivazioni) {
			ModelMotivazioni motiv = new ModelMotivazioni(mot);
			lista.add(motiv);
		}
		return lista;
//		return query.getSingleResult();
	}

	public List<ModelCronogiaStato> getCronologiaStato(Integer idScheda) {
		TypedQuery<GregREnteGestoreStatoEnte> query = em.createNamedQuery("GregREnteGestoreStatoEnte.findAllStatiById",
				GregREnteGestoreStatoEnte.class);
		query.setParameter("idScheda", idScheda);
		List<GregREnteGestoreStatoEnte> statoEnte = query.getResultList();
		String sqlQuery = "select u.nome || ' ' || u.cognome " + "from greg_t_user u " + "where "
				+ "u.codice_fiscale = :codiceFiscale ";
		Query queryUtente = em.createNativeQuery(sqlQuery);
		List<ModelCronogiaStato> lista = new ArrayList<ModelCronogiaStato>();
		for (GregREnteGestoreStatoEnte stato : statoEnte) {
			queryUtente.setParameter("codiceFiscale", stato.getUtenteOperazione());
			String operatore;
			try {
				operatore = (String) queryUtente.getSingleResult();
			} catch (Exception e) {
				operatore = "CSI";
			}

			ModelCronogiaStato statoCrono = new ModelCronogiaStato(stato, operatore);
			lista.add(statoCrono);
		}
		return lista;
//		return query.getSingleResult();
	}

	public GregREnteGestoreStatoEnte findLastStato(Integer idScheda) {
		TypedQuery<GregREnteGestoreStatoEnte> query = em.createNamedQuery("GregREnteGestoreStatoEnte.findLastStato",
				GregREnteGestoreStatoEnte.class);
		query.setParameter("idScheda", idScheda);
		GregREnteGestoreStatoEnte stato = query.getSingleResult();
		return stato;
//		return query.getSingleResult();
	}

	public GregREnteGestoreStatoEnte saveEnteStatoEnte(GregREnteGestoreStatoEnte statoEnte) {
		return em.merge(statoEnte);
	}

	public GregDMotivazione findMotivazioneByCod(String codMotivazione) {
		TypedQuery<GregDMotivazione> query = em.createNamedQuery("GregDMotivazione.findMotivazioneByCod",
				GregDMotivazione.class);
		query.setParameter("codMotivazione", codMotivazione);
		GregDMotivazione motivazione = query.getSingleResult();
		return motivazione;
//		return query.getSingleResult();
	}

	public GregDStatoEnte findStatoByCod(String codStato) {
		TypedQuery<GregDStatoEnte> query = em.createNamedQuery("GregDStatoEnte.findStatobyCod", GregDStatoEnte.class);
		query.setParameter("codStato", codStato);
		GregDStatoEnte stato = query.getSingleResult();
		return stato;
//		return query.getSingleResult();
	}

	public ModelCronogiaStato findLastStatoEnte(Integer idScheda) {
		TypedQuery<GregREnteGestoreStatoEnte> query = em.createNamedQuery("GregREnteGestoreStatoEnte.findLastStato",
				GregREnteGestoreStatoEnte.class);
		query.setParameter("idScheda", idScheda);
		GregREnteGestoreStatoEnte stato = query.getSingleResult();
		ModelCronogiaStato statoEnte = new ModelCronogiaStato(stato, null);
		return statoEnte;
//		return query.getSingleResult();
	}

	public List<ModelMotivazioni> getMotivazioniRipristino() {
		TypedQuery<GregDMotivazione> query = em.createNamedQuery("GregDMotivazione.findAllRipristino",
				GregDMotivazione.class);
		List<GregDMotivazione> motivazioni = query.getResultList();
		List<ModelMotivazioni> lista = new ArrayList<ModelMotivazioni>();
		for (GregDMotivazione mot : motivazioni) {
			ModelMotivazioni motiv = new ModelMotivazioni(mot);
			lista.add(motiv);
		}
		return lista;
//		return query.getSingleResult();
	}

	public GregREnteGestoreStatoEnte findLastStatoAperto(Integer idScheda) {
		TypedQuery<GregREnteGestoreStatoEnte> query = em
				.createNamedQuery("GregREnteGestoreStatoEnte.findLastStatoAperto", GregREnteGestoreStatoEnte.class);
		query.setParameter("idScheda", idScheda);
		GregREnteGestoreStatoEnte stato = query.getSingleResult();
		return stato;
//		return query.getSingleResult();
	}

	public List<ModelDatiAnagrafici> findSchedaEnteApertoeChiuso(RicercaListaEntiDaunire stato) {

		List<ModelDatiAnagrafici> results = new ArrayList<ModelDatiAnagrafici>();
		String sqlQuery = null;
		if (stato.isStatoEnte()) {
			sqlQuery = "select distinct s.codice_regionale, s.codice_fiscale as codiceFiscaleEnte,  "
					+ "	e.denominazione, e.partita_iva, t.cod_tipo_ente, t.des_tipo_ente,  "
					+ "	c.cod_istat_comune,	c.des_comune, e.cod_istat_ente,  "
					+ "	p.cod_istat_provincia, p.des_provincia, a.cod_asl,a.des_asl,  "
					+ "	e.email as emailEnte, e.telefono as telefonoEnte,e.pec,r.nome,  "
					+ "	r.cognome, r.codice_fiscale as codiceFiscaleResponsabile, rc.cellulare,  "
					+ "	rc.telefono telefonoResponsabile, rc.email as emailResponsabile from  "
					+ "	greg_t_schede_enti_gestori s, greg_r_ente_gestore_contatti e,  "
					+ "	greg_d_tipo_ente t, greg_d_comuni c, greg_d_province p, greg_d_asl a,  "
					+ "	greg_t_responsabile_ente_gestore r, greg_r_responsabile_contatti rc , "
					+ "	greg_r_ente_gestore_stato_ente gres,greg_d_stato_ente gdse " + "where  ";
			if (stato.getLista().size() >= 1) {
				sqlQuery += "s.id_scheda_ente_gestore in (";
				for (int ente : stato.getLista()) {
					sqlQuery += ente + ",";
				}
				sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
				sqlQuery += ") and ";
			}
			sqlQuery += "s.id_scheda_ente_gestore = e.id_scheda_ente_gestore and "
					+ "e.data_fine_validita is null and t.id_tipo_ente = e.id_tipo_ente and "
					+ "c.id_comune = e.id_comune and c.id_provincia = p.id_provincia and "
					+ "e.id_asl = a.id_asl and gres.id_scheda_ente_gestore = s.id_scheda_ente_gestore and "
					+ "gdse.cod_stato_ente ='APE'  and gres.id_stato_ente = gdse.id_stato_ente and "
					+ "((gres.data_inizio_validita <= :dataVal and gres.data_fine_validita is null)  or "
					+ "	(gres.data_inizio_validita <= :dataVal and gres.data_fine_validita > :dataVal)) and "
					+ "rc.id_responsabile_contatti = e.id_responsabile_contatti and "
					+ "rc.id_responsabile_ente_gestore = r.id_responsabile_ente_gestore and "
					+ "r.data_cancellazione is null and "
					+ "gres.data_cancellazione is null and rc.data_fine_validita is null and "
					+ "s.data_cancellazione is null and t.data_cancellazione is null and "
					+ "c.data_cancellazione is null and p.data_cancellazione is null and "
					+ "a.data_cancellazione is null and r.data_cancellazione is null " + "order by s.codice_regionale";

		} else {
			sqlQuery = "select distinct 	s.codice_regionale,	s.codice_fiscale as codiceFiscaleEnte,  "
					+ "	e.denominazione,	e.partita_iva,	t.cod_tipo_ente,	t.des_tipo_ente,  "
					+ "	c.cod_istat_comune,	c.des_comune,	e.cod_istat_ente,  "
					+ "	p.cod_istat_provincia,	p.des_provincia,	a.cod_asl,	a.des_asl,  "
					+ "	e.email as emailEnte,	e.telefono as telefonoEnte,	e.pec,	r.nome,  "
					+ "	r.cognome,	r.codice_fiscale as codiceFiscaleResponsabile,	rc.cellulare,  "
					+ "	rc.telefono telefonoResponsabile,	rc.email as emailResponsabile from  "
					+ "	greg_t_schede_enti_gestori s,	greg_r_ente_gestore_contatti e,  "
					+ "	greg_d_tipo_ente t,	greg_d_comuni c,	greg_d_province p,	greg_d_asl a,  "
					+ "	greg_t_responsabile_ente_gestore r,	greg_r_responsabile_contatti rc,"
					+ " greg_r_ente_gestore_stato_ente gres  " + "where  ";
			if (stato.getLista().size() >= 1) {
				sqlQuery += "s.id_scheda_ente_gestore in (";
				for (int ente : stato.getLista()) {
					sqlQuery += ente + ",";
				}
				sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
				sqlQuery += ") and ";
			}
			sqlQuery += "s.id_scheda_ente_gestore = e.id_scheda_ente_gestore	and   "
					+ "e.data_fine_validita is null	and t.id_tipo_ente = e.id_tipo_ente	and   "
					+ "c.id_comune = e.id_comune	and c.id_provincia = p.id_provincia	and   "
					+ "e.id_asl = a.id_asl and   "
					+ "rc.id_responsabile_contatti = e.id_responsabile_contatti and r.data_cancellazione is null  "
					+ "and   "
					+ "rc.id_responsabile_ente_gestore = r.id_responsabile_ente_gestore and rc.data_fine_validita is null  "
					+ "and gres.id_scheda_ente_gestore = s.id_scheda_ente_gestore and "
					+ " gres.data_cancellazione is null  and gres.id_scheda_ente_gestore NOT IN ("
					+ "select id_ente_da_unire from greg_r_merge_enti  where "
					+ " data_cancellazione is null ) and s.codice_regionale!= :codRegionale and"
					+ "((gres.data_inizio_validita <= :dataVal and gres.data_fine_validita is null)  or "
					+ "	(gres.data_inizio_validita <= :dataVal and gres.data_fine_validita > :dataVal)) "
					+ "and s.data_cancellazione is null and t.data_cancellazione is null  "
					+ "and c.data_cancellazione is null  and p.data_cancellazione is null  "
					+ "and a.data_cancellazione is null  and r.data_cancellazione is null  "
					+ " order by s.codice_regionale ";
		}

		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("dataVal", stato.getDataMerge());
		if (!stato.isStatoEnte()) {
			query.setParameter("codRegionale", stato.getCodregionale());
		}
		ArrayList<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
		for (Object[] o : obj) {
			ModelDatiAnagrafici comune = new ModelDatiAnagrafici(o);
			results.add(comune);
		}

		return results;

	}

	public GregTSchedeEntiGestori findSchedaByCod(String codRegionale) {
		TypedQuery<GregTSchedeEntiGestori> query = em.createNamedQuery("GregTSchedeEntiGestori.findByValue",
				GregTSchedeEntiGestori.class);
		query.setParameter("codReg", codRegionale);
		return query.getSingleResult();
	}

	public GregREnteGestoreStatoEnte findSchedaEnteById(Integer idScheda) {
		TypedQuery<GregREnteGestoreStatoEnte> query = em.createNamedQuery("GregREnteGestoreStatoEnte.findAllStatiById",
				GregREnteGestoreStatoEnte.class);
		query.setParameter("idScheda", idScheda);
		return query.getSingleResult();
	}

	public GregREnteGestoreStatoEnte saveEnteGestoreStatoEnte(GregREnteGestoreStatoEnte schedaToUpdate) {
		return em.merge(schedaToUpdate);
	}

	public GregRMergeEnti saveDatiEnteMerge(GregRMergeEnti schedaToUpdate) {
		return em.merge(schedaToUpdate);
	}

	public List<GregRSchedeEntiGestoriComuni> findGregRComuniAssegnatiWithDataMerge(Integer idEnte, Date dataMerge) {
		TypedQuery<GregRSchedeEntiGestoriComuni> query = em.createNamedQuery(
				"GregRSchedeEntiGestoriComuni.findByIdSchedaEnteAndDataMerge", GregRSchedeEntiGestoriComuni.class);
		query.setParameter("idScheda", idEnte);
		query.setParameter("dataMerge", dataMerge);
		return query.getResultList();

	}

	public GregTSchedeEntiGestori findSchedabyCodFisc(String codiceFiscale) {
		try {
			TypedQuery<GregTSchedeEntiGestori> query = em.createNamedQuery("GregTSchedeEntiGestori.findByCodFis",
					GregTSchedeEntiGestori.class);
			query.setParameter("codFisc", codiceFiscale);
			GregTSchedeEntiGestori resp = query.getSingleResult();
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	public GregTSchedeEntiGestori findSchedabyCodReg(String codiceRegionale) {
		try {
			TypedQuery<GregTSchedeEntiGestori> query = em.createNamedQuery("GregTSchedeEntiGestori.findByCodReg",
					GregTSchedeEntiGestori.class);
			query.setParameter("codReg", codiceRegionale);
			GregTSchedeEntiGestori resp = query.getSingleResult();
			return resp;
		} catch (Exception e) {
			return null;
		}
	}

	public GregDComuni findComuneByCodNotDeleted(String codComune) {
		TypedQuery<GregDComuni> query = em.createNamedQuery("GregDComuni.findByCod", GregDComuni.class);
		query.setParameter("codComune", codComune);
		return query.getSingleResult();
	}

	public GregDAsl findAslbyCod(String codAsl) {
		TypedQuery<GregDAsl> query = em.createNamedQuery("GregDAsl.findByCod", GregDAsl.class);
		query.setParameter("codAsl", codAsl);
		return query.getSingleResult();
	}

	public GregDTipoEnte findTipoEntebyCod(String codTipo) {
		TypedQuery<GregDTipoEnte> query = em.createNamedQuery("GregDTipoEnte.findByCodNotDeleted", GregDTipoEnte.class);
		query.setParameter("codTipoEnte", codTipo);
		return query.getSingleResult();
	}

	public GregTLista findListabyCod(String codLista) {
		TypedQuery<GregTLista> query = em.createNamedQuery("GregTLista.findByCod", GregTLista.class);
		query.setParameter("codLista", codLista);
		return query.getSingleResult();
	}

	public GregRListaEntiGestori updateListaEnte(GregRListaEntiGestori listaEnte) {
		return em.merge(listaEnte);
	}

	public List<ModelComune> findProvicieComuniLiberi(String codregione, Date dataValidita) {

		String hqlQuery = "with tab1 as(select distinct id_comune from greg_d_comuni a " + "where id_comune not in "
				+ "(select id_comune  from greg_r_schede_enti_gestori_comuni a " + "where "
				+ "(data_inizio_validita <= :dataValidita and data_fine_validita >= :dataValidita) " + "or "
				+ "(data_fine_validita is null and data_inizio_validita <= :dataValidita))) "
				+ "select p.id_provincia ,p.cod_istat_provincia,p.des_provincia , "
				+ "c.id_comune ,c.cod_istat_comune,c.des_comune " + "from " + "greg_d_province p, "
				+ "greg_d_comuni c,tab1 t,greg_d_regione r " + "where p.data_cancellazione is null "
				+ "and c.data_cancellazione is null " + "and r.data_cancellazione is null "
				+ "and ((p.data_inizio_validita <= :dataValidita and p.data_fine_validita >= :dataValidita) " + "or "
				+ "(p.data_fine_validita is null and p.data_inizio_validita <= :dataValidita)) "
				+ "and ((c.inizio_validita_comune  <= :dataValidita and c.fine_validita_comune >= :dataValidita) "
				+ "or " + "(c.fine_validita_comune is null and c.inizio_validita_comune <= :dataValidita)) "
				+ "and c.id_provincia = p.id_provincia " + "and t.id_comune=c.id_comune "
				+ "and r.id_regione = p.id_regione and r.cod_regione = :codregione order by c.des_comune";

		Query query = em.createNativeQuery(hqlQuery);
		query.setParameter("codregione", codregione);
		query.setParameter("dataValidita", dataValidita);
		ArrayList<Object[]> obj = (ArrayList<Object[]>) query.getResultList();
		List<ModelComune> lista = new ArrayList<ModelComune>();
		for (Object[] o : obj) {
			ModelProvincia provincia = new ModelProvincia();
			provincia.setIdProvincia((Integer) o[0]);
			provincia.setCodIstatProvincia(String.valueOf(o[1]).equalsIgnoreCase("null") ? "" : String.valueOf(o[1]));
			provincia.setDesProvincia(String.valueOf(o[2]).equalsIgnoreCase("null") ? "" : String.valueOf(o[2]));
			ModelComune comune = new ModelComune();
			comune.setIdComune((Integer) o[3]);
			comune.setCodIstatComune(String.valueOf(o[4]).equalsIgnoreCase("null") ? "" : String.valueOf(o[4]));
			comune.setDesComune(String.valueOf(o[5]).equalsIgnoreCase("null") ? "" : String.valueOf(o[5]));
			comune.setProvincia(provincia);
			lista.add(comune);
		}

		return lista;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteSchedaEnteGestore(Integer idScheda) {
		GregTSchedeEntiGestori rendToDelete = em.find(GregTSchedeEntiGestori.class, idScheda);

		if (idScheda != null && idScheda != 0) {
			em.remove(rendToDelete);
		}
	}

	public GregTPrestazioniRegionali1 findPrestazioneByCod(String codPrestazione, Integer annoGestione) {
		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g " + "LEFT JOIN FETCH g.gregDTipoStruttura s "
				+ "LEFT JOIN FETCH g.gregDTipologia t " + "LEFT JOIN FETCH g.gregDTipologiaQuota tq "
				+ "LEFT JOIN FETCH g.gregTPrestazioniRegionali1 p1 "
				+ "WHERE g.codPrestReg1 = :codPrestazione AND g.dataCancellazione IS NULL "
				+ "AND t.dataCancellazione IS NULL " + "AND tq.dataCancellazione IS NULL "
				+ "AND s.dataCancellazione IS NULL " + "AND p1.dataCancellazione IS NULL " + "and ("
				+ annoGestione.toString() + " - year(g.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(g.dataFineValidita)<=0 or g.dataFineValidita is null)) ";
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("codPrestazione", codPrestazione);
		return query.getSingleResult();
	}

	public List<ModelMacroaggregati> findMacroaggregatiByPrestRegionale(String codPrestazione, Integer annoGestione) {
		String hqlQuery = "SELECT m FROM GregTMacroaggregatiBilancio m "
				+ "LEFT JOIN GregRPrestReg1MacroaggregatiBilancio pm on m.idMacroaggregatoBilancio = pm.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p on pm.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
				+ "WHERE p.codPrestReg1 = :codPrestazione " + "AND p.dataCancellazione IS NULL "
				+ "AND pm.dataCancellazione IS NULL " + "AND m.dataCancellazione IS NULL " + "and ("
				+ annoGestione.toString() + " - year(pm.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(pm.dataFineValidita)<=0 or pm.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p.dataFineValidita)<=0 or p.dataFineValidita is null)) "
				+ "Order by m.codMacroaggregatoBilancio";
		TypedQuery<GregTMacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregTMacroaggregatiBilancio.class);
		query.setParameter("codPrestazione", codPrestazione);
		List<GregTMacroaggregatiBilancio> macro = query.getResultList();
		List<ModelMacroaggregati> macroaggregati = new ArrayList<ModelMacroaggregati>();
		for (GregTMacroaggregatiBilancio m : macro) {
			ModelMacroaggregati macroaggregato = new ModelMacroaggregati();
			macroaggregato.setCodMacroaggregato(m.getCodMacroaggregatoBilancio());
			macroaggregato.setDescMacroaggregato(m.getDesMacroaggregatoBilancio());
			macroaggregati.add(macroaggregato);
		}
		return macroaggregati;
	}

	public List<GregDTargetUtenza> findTargetUtenzeByPrestRegionale(String codPrestazione, Integer annoGestione) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1 pu on u.idTargetUtenza = pu.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p on pu.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
				+ "WHERE p.codPrestReg1 = :codPrestazione " + "AND p.dataCancellazione IS NULL "
				+ "AND pu.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL " + "and ("
				+ annoGestione.toString() + " - year(pu.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(pu.dataFineValidita)<=0 or pu.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p.dataFineValidita)<=0 or p.dataFineValidita is null)) " + "Order by u.codUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("codPrestazione", codPrestazione);
		return query.getResultList();
	}

	public GregDProgrammaMissione findProgrammaMissioneByPrestRegionaleUtenza(String codPrestazione, String codUtenza,
			Integer annoGestione) {
		try {
			String hqlQuery = "SELECT pm FROM GregDProgrammaMissione pm "
					+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1ProgrammaMissione pupm on pm.idProgrammaMissione = pupm.gregDProgrammaMissione.idProgrammaMissione "
					+ "LEFT JOIN GregDTargetUtenza u on u.idTargetUtenza = pupm.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pupm.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.codPrestReg1 = :codPrestazione " + "AND u.codUtenza = :codUtenza "
					+ "AND p.dataCancellazione IS NULL " + "AND pm.dataCancellazione IS NULL "
					+ "AND pupm.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL " + "and ("
					+ annoGestione.toString() + " - year(pupm.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(pupm.dataFineValidita)<=0 or pupm.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p.dataFineValidita)<=0 or p.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString()
					+ " - year(pupm.gregRPrestReg1UtenzeRegionali1.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString()
					+ " - year(pupm.gregRPrestReg1UtenzeRegionali1.dataFineValidita)<=0 or pupm.gregRPrestReg1UtenzeRegionali1.dataFineValidita is null)) "
					+ "Order by pm.siglaProgrammaMissione";
			TypedQuery<GregDProgrammaMissione> query = em.createQuery(hqlQuery, GregDProgrammaMissione.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codUtenza", codUtenza);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregDTipologiaSpesa findTipologiaSpesaByPrestRegionaleUtenza(String codPrestazione, String codUtenza,
			Integer annoGestione) {
		try {
			String hqlQuery = "SELECT ts FROM GregDTipologiaSpesa ts "
					+ "LEFT JOIN GregRTipologiaSpesaPrestReg1 tspu on ts.idTipologiaSpesa = tspu.gregDTipologiaSpesa.idTipologiaSpesa "
					+ "LEFT JOIN GregDTargetUtenza u on u.idTargetUtenza = tspu.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on tspu.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.codPrestReg1 = :codPrestazione " + "AND u.codUtenza = :codUtenza "
					+ "AND p.dataCancellazione IS NULL " + "AND ts.dataCancellazione IS NULL "
					+ "AND tspu.dataCancellazione IS NULL "
					+ "AND tspu.gregRPrestReg1UtenzeRegionali1.dataCancellazione IS NULL "
					+ "AND u.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
					+ " - year(tspu.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
					+ " - year(tspu.dataFineValidita)<=0 or tspu.dataFineValidita is null)) " + "and ("
					+ annoGestione.toString() + " - year(tspu.gregRPrestReg1UtenzeRegionali1.dataInizioValidita)>=0 "
					+ "and (" + annoGestione.toString()
					+ " - year(tspu.gregRPrestReg1UtenzeRegionali1.dataFineValidita)<=0 or tspu.gregRPrestReg1UtenzeRegionali1.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p.dataFineValidita)<=0 or p.dataFineValidita is null)) "
					+ "Order by ts.codTipologiaSpesa";
			TypedQuery<GregDTipologiaSpesa> query = em.createQuery(hqlQuery, GregDTipologiaSpesa.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codUtenza", codUtenza);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<ModelPrestUtenza> findUtenzeByPrestRegionale(String codPrestazione, Integer annoGestione) {
		List<GregDTargetUtenza> targetUtenze = findTargetUtenzeByPrestRegionale(codPrestazione, annoGestione);

		List<ModelPrestUtenza> utenze = new ArrayList<ModelPrestUtenza>();
		for (GregDTargetUtenza u : targetUtenze) {
			ModelPrestUtenza utenza = new ModelPrestUtenza();
			utenza.setCodUtenza(u.getCodUtenza());
			utenza.setDescUtenza(u.getDesUtenza());

			GregDProgrammaMissione programma = findProgrammaMissioneByPrestRegionaleUtenza(codPrestazione,
					u.getCodUtenza(), annoGestione);
			if (programma != null) {
				utenza.setCodMissioneProgramma(programma.getSiglaProgrammaMissione());
				utenza.setDescMissioneProgramma(programma.getInformativa());
				utenza.setColoreMissioneProgramma(programma.getGregDColori().getRgb());
			}
			GregDTipologiaSpesa spesa = findTipologiaSpesaByPrestRegionaleUtenza(codPrestazione, u.getCodUtenza(),
					annoGestione);
			if (spesa != null) {
				utenza.setCodTipologiaSpesa(spesa.getCodTipologiaSpesa());
				utenza.setDescTipologiaSpesa(spesa.getDesTipologiaSpesa());
			}
			utenze.add(utenza);
		}
		return utenze;
	}

	public List<GregTPrestazioniRegionali2> findPrestazione2ByPrestRegionale(String codPrestazione,
			Integer annoGestione) {
		String hqlQuery = "SELECT p2 FROM GregTPrestazioniRegionali2 p2 "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.codPrestReg1 = :codPrestazione " + "AND p12.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
				+ " - year(p2.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p2.dataFineValidita)<=0 or p2.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p12.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p12.dataFineValidita)<=0 or p12.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) " + "Order by p2.codPrestReg2";
		TypedQuery<GregTPrestazioniRegionali2> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali2.class);
		query.setParameter("codPrestazione", codPrestazione);
		return query.getResultList();
	}

	public List<GregDTargetUtenza> findUtenzaByPrest2(String codPrestazione1, String codPrestazione2,
			Integer annoGestione) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestReg2UtenzeRegionali2 p2u on u.idTargetUtenza = p2u.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2u.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.codPrestReg1 = :codPrestazione1 " + "AND p2.codPrestReg2 = :codPrestazione2 "
				+ "AND p1.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL "
				+ "AND p2u.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
				+ "AND p12.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
				+ " - year(p2u.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p2u.dataFineValidita)<=0 or p2u.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p2.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p2.dataFineValidita)<=0 or p2.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p12.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p12.dataFineValidita)<=0 or p12.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) " + "Order by u.codUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("codPrestazione1", codPrestazione1);
		query.setParameter("codPrestazione2", codPrestazione2);
		return query.getResultList();
	}

	public GregDVoceIstat findPrestMinisterialeByPrest2(String codPrestazione1, String codPrestazione2,
			Integer annoGestione) {
		try {
			String hqlQuery = "SELECT vi FROM GregDVoceIstat vi "
					+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.codPrestReg1 = :codPrestazione1 " + "AND p2.codPrestReg2 = :codPrestazione2 "
					+ "AND p1.dataCancellazione IS NULL " + "AND vi.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "AND p12.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
					+ " - year(p2.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
					+ " - year(p2.dataFineValidita)<=0 or p2.dataFineValidita is null)) " + "and ("
					+ annoGestione.toString() + " - year(p12.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p12.dataFineValidita)<=0 or p12.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p2ui.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p2ui.dataFineValidita)<=0 or p2ui.dataFineValidita is null)) "
					+ "Order by vi.codVoceIstat";
			TypedQuery<GregDVoceIstat> query = em.createQuery(hqlQuery, GregDVoceIstat.class);
			query.setParameter("codPrestazione1", codPrestazione1);
			query.setParameter("codPrestazione2", codPrestazione2);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<GregDTargetUtenza> findUtenzeMinisterialeByPrest2Istat(String codPrestazione1, String codPrestazione2,
			String codIstat, Integer annoGestione) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on u.idTargetUtenza = p2ui.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregDVoceIstat vi on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.codPrestReg1 = :codPrestazione1 " + "AND p2.codPrestReg2 = :codPrestazione2 "
				+ "AND vi.codVoceIstat = :codIstat " + "AND p1.dataCancellazione IS NULL "
				+ "AND u.dataCancellazione IS NULL " + "AND p2ui.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL " + "AND vi.dataCancellazione IS NULL "
				+ "AND p12.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
				+ " - year(p2.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p2.dataFineValidita)<=0 or p2.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p12.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p12.dataFineValidita)<=0 or p12.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p2ui.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p2ui.dataFineValidita)<=0 or p2ui.dataFineValidita is null)) " + "Order by u.desUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("codPrestazione1", codPrestazione1);
		query.setParameter("codPrestazione2", codPrestazione2);
		query.setParameter("codIstat", codIstat);
		return query.getResultList();
	}

	public GregTNomenclatoreNazionale findNomenclatoreByPrest2Istat(String codPrestazione1, String codPrestazione2,
			Integer annoGestione) {
		try {
			String hqlQuery = "SELECT u FROM GregTNomenclatoreNazionale u "
					+ "LEFT JOIN GregRNomencNazPrestReg2 p2ui on u.idNomenclatoreNazionale = p2ui.gregTNomenclatoreNazionale.idNomenclatoreNazionale "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.codPrestReg1 = :codPrestazione1 " + "AND p2.codPrestReg2 = :codPrestazione2 "
					+ "AND p1.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "AND p12.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
					+ " - year(p2.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
					+ " - year(p2.dataFineValidita)<=0 or p2.dataFineValidita is null)) " + "and ("
					+ annoGestione.toString() + " - year(p12.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p12.dataFineValidita)<=0 or p12.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p2ui.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p2ui.dataFineValidita)<=0 or p2ui.dataFineValidita is null)) "
					+ "Order by u.codNomenclatoreNazionale";
			TypedQuery<GregTNomenclatoreNazionale> query = em.createQuery(hqlQuery, GregTNomenclatoreNazionale.class);
			query.setParameter("codPrestazione1", codPrestazione1);
			query.setParameter("codPrestazione2", codPrestazione2);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<ModelPrest1Prest2> findPrest2ByPrest1(String codPrestazione, Integer annoGestione) {
		List<GregTPrestazioniRegionali2> prest2 = findPrestazione2ByPrestRegionale(codPrestazione, annoGestione);

		List<ModelPrest1Prest2> prestazioni2 = new ArrayList<ModelPrest1Prest2>();
		for (GregTPrestazioniRegionali2 p2 : prest2) {
			ModelPrest1Prest2 prestazione2 = new ModelPrest1Prest2();
			prestazione2.setCodPrest2(p2.getCodPrestReg2());
			prestazione2.setDescPrest2(p2.getDesPrestReg2());
			List<GregDTargetUtenza> targetUtenze = findUtenzaByPrest2(codPrestazione, p2.getCodPrestReg2(),
					annoGestione);
			List<String> utenze = new ArrayList<String>();
			for (GregDTargetUtenza u : targetUtenze) {
				utenze.add(u.getDesUtenza());
			}
			prestazione2.setUtenze(utenze);
			GregDVoceIstat voce = findPrestMinisterialeByPrest2(codPrestazione, p2.getCodPrestReg2(), annoGestione);
			if (voce != null) {
				prestazione2.setCodPrestIstat(voce.getCodVoceIstat());
				prestazione2.setDescPrestIstat(voce.getDescVoceIstat());
				List<GregDTargetUtenza> utenzeMin = findUtenzeMinisterialeByPrest2Istat(codPrestazione,
						p2.getCodPrestReg2(), voce.getCodVoceIstat(), annoGestione);
				List<String> utenzeM = new ArrayList<String>();
				for (GregDTargetUtenza uM : utenzeMin) {
					utenzeM.add(uM.getDesUtenza());
				}
				prestazione2.setUtenzeMin(utenzeM);
			}
			GregTNomenclatoreNazionale nomenclatore = findNomenclatoreByPrest2Istat(codPrestazione,
					p2.getCodPrestReg2(), annoGestione);
			if (nomenclatore != null) {
				prestazione2.setClassificazionePresidio(nomenclatore.getGregDClassificazionePresidio() != null
						? nomenclatore.getGregDClassificazionePresidio().getDesClassificazionePresidio()
						: " ");
				prestazione2.setFunzionePresidio(nomenclatore.getGregDFunzionePresidio() != null
						? nomenclatore.getGregDFunzionePresidio().getDesFunzionePresidio()
						: " ");
				prestazione2.setMacroArea(
						nomenclatore.getGregDMacroaree() != null
								? nomenclatore.getGregDMacroaree().getCodMacroarea() + " - "
										+ nomenclatore.getGregDMacroaree().getDesMacroarea()
								: " ");
				prestazione2.setPresidio(
						nomenclatore.getGregDTipoPresidio() != null
								? nomenclatore.getGregDTipoPresidio().getCodTipoPresidio() + " - "
										+ nomenclatore.getGregDTipoPresidio().getDesTipoPresidio()
								: " ");
				prestazione2.setSottoArea(
						nomenclatore.getGregDSottoaree() != null
								? nomenclatore.getGregDSottoaree().getCodSottoarea() + " - "
										+ nomenclatore.getGregDSottoaree().getDesSottoarea()
								: " ");
				prestazione2.setSottoAreaDet(
						nomenclatore.getGregDSottoareeDet() != null
								? nomenclatore.getGregDSottoareeDet().getCodSottoareaDet() + " - "
										+ nomenclatore.getGregDSottoareeDet().getDesSottoareaDet()
								: " ");
				prestazione2.setSottoVoce(
						nomenclatore.getGregDSottovoci() != null
								? nomenclatore.getGregDSottovoci().getCodSottovoce() + " - "
										+ nomenclatore.getGregDSottovoci().getDesSottovoce()
								: " ");
				prestazione2.setTipoResidenza(
						nomenclatore.getGregDTipoResidenza() != null
								? nomenclatore.getGregDTipoResidenza().getCodTipoResidenza() + " - "
										+ nomenclatore.getGregDTipoResidenza().getDesTipoResidenza()
								: " ");
				prestazione2.setVoce(nomenclatore.getGregDVoci() != null
						? nomenclatore.getGregDVoci().getCodVoce() + " - " + nomenclatore.getGregDVoci().getDesVoce()
						: " ");

			}
			prestazioni2.add(prestazione2);
		}
		return prestazioni2;
	}

	public GregTPrestazioniMinisteriali findPrestazioneMinByPrestRegionale(String codPrestazione,
			Integer annoGestione) {
		try {
			String hqlQuery = "SELECT pm FROM GregTPrestazioniMinisteriali pm "
					+ "LEFT JOIN GregRPrestReg1PrestMinist p1pm on pm.idPrestazioneMinisteriale = p1pm.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p1pm.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.codPrestReg1 = :codPrestazione " + "AND p1.dataCancellazione IS NULL "
					+ "AND pm.dataCancellazione IS NULL " + "AND p1pm.dataCancellazione IS NULL " + "and ("
					+ annoGestione.toString() + " - year(p1pm.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p1pm.dataFineValidita)<=0 or p1pm.dataFineValidita is null)) "
					+ "and (" + annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and ("
					+ annoGestione.toString() + " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) "
					+ "Order by pm.codPrestazioneMinisteriale";
			TypedQuery<GregTPrestazioniMinisteriali> query = em.createQuery(hqlQuery,
					GregTPrestazioniMinisteriali.class);
			query.setParameter("codPrestazione", codPrestazione);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<GregDTargetUtenza> findUtenzeMinisterialeByPrestMin(String codPrestazione1, String codPrestazioneMin,
			Integer annoGestione) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestMinistUtenzeMinist puu on u.idTargetUtenza = puu.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniMinisteriali pm on pm.idPrestazioneMinisteriale = puu.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
				+ "LEFT JOIN GregRPrestReg1PrestMinist p1pm on pm.idPrestazioneMinisteriale = p1pm.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p1pm.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.codPrestReg1 = :codPrestazione1 "
				+ "AND pm.codPrestazioneMinisteriale = :codPrestazioneMin " + "AND p1.dataCancellazione IS NULL "
				+ "AND u.dataCancellazione IS NULL " + "AND puu.dataCancellazione IS NULL "
				+ "AND pm.dataCancellazione IS NULL " + "AND p1pm.dataCancellazione IS NULL " + "and ("
				+ annoGestione.toString() + " - year(puu.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(puu.dataFineValidita)<=0 or puu.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p1pm.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p1pm.dataFineValidita)<=0 or p1pm.dataFineValidita is null)) " + "and ("
				+ annoGestione.toString() + " - year(p1.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(p1.dataFineValidita)<=0 or p1.dataFineValidita is null)) " + "Order by u.codUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("codPrestazione1", codPrestazione1);
		query.setParameter("codPrestazioneMin", codPrestazioneMin);
		return query.getResultList();
	}

	public ModelPrest1PrestMin findPrestMinByPrest1(String codPrestazione, Integer annoGestione) {

		GregTPrestazioniMinisteriali prestMin = findPrestazioneMinByPrestRegionale(codPrestazione, annoGestione);

		ModelPrest1PrestMin prestazioniMin = new ModelPrest1PrestMin();
		if (prestMin != null) {
			prestazioniMin.setCodMacro(prestMin.getGregDMacroattivita().getCodMacroattivita());
			prestazioniMin.setDescMacro(prestMin.getGregDMacroattivita().getDescMacroattivita());
			prestazioniMin.setCodPrestMin(prestMin.getCodPrestazioneMinisteriale());
			prestazioniMin.setDescPrestMin(prestMin.getDescPrestazioneMinisteriale());
			List<GregDTargetUtenza> utenzeMin = findUtenzeMinisterialeByPrestMin(codPrestazione,
					prestMin.getCodPrestazioneMinisteriale(), annoGestione);
			List<String> utenzeM = new ArrayList<String>();
			for (GregDTargetUtenza uM : utenzeMin) {
				utenzeM.add(uM.getDesUtenza());
			}
			prestazioniMin.setUtenzeMin(utenzeM);
		}
		return prestazioniMin;
	}

	public List<GregTPrestazioniRegionali1> findPrestazioneFiglieByCod(String codPrestazione, Integer annoGestione) {
		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g " + "LEFT JOIN FETCH g.gregDTipoStruttura s "
				+ "LEFT JOIN FETCH g.gregDTipologia t " + "LEFT JOIN FETCH g.gregDTipologiaQuota tq "
				+ "LEFT JOIN FETCH g.gregTPrestazioniRegionali1 p1 "
				+ "WHERE g.gregTPrestazioniRegionali1.codPrestReg1 = :codPrestazione "
				+ "AND g.dataCancellazione IS NULL " + "AND s.dataCancellazione IS NULL "
				+ "AND t.dataCancellazione IS NULL " + "AND tq.dataCancellazione IS NULL "
				+ "AND p1.dataCancellazione IS NULL " + "and (" + annoGestione.toString()
				+ " - year(g.dataInizioValidita)>=0 " + "and (" + annoGestione.toString()
				+ " - year(g.dataFineValidita)<=0 or g.dataFineValidita is null)) " + "Order By g.codPrestReg1";
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("codPrestazione", codPrestazione);
		return query.getResultList();
	}

	public String findDataInizioMassima(Integer idEnte) {

		String hqlQuery = "with t as (select a.data_inizio_validita "
				+ "from greg_r_ente_gestore_contatti a, "
				+ "greg_t_schede_enti_gestori b  "
				+ "where a.id_scheda_ente_gestore = b.id_scheda_ente_gestore  "
				+ "and a.data_fine_validita is null  "
				+ "and b.data_cancellazione is null "
				+ "and a.id_scheda_ente_gestore = :idSchedaEnteGestore "
				+ "union  "
				+ "select c.data_inizio_validita "
				+ "from greg_r_ente_gestore_contatti a, "
				+ "greg_t_schede_enti_gestori b, greg_r_responsabile_contatti c "
				+ "where a.id_scheda_ente_gestore = b.id_scheda_ente_gestore  "
				+ "and a.data_fine_validita is null "
				+ "and b.data_cancellazione is null "
				+ "and c.data_fine_validita is null "
				+ "and a.id_scheda_ente_gestore = :idSchedaEnteGestore "
				+ "and c.id_responsabile_contatti = a.id_responsabile_contatti) "
				+ "select TO_CHAR(max(t.data_inizio_validita), 'DD/MM/YYYY') "
				+ "from t;";

		Query query = em.createNativeQuery(hqlQuery);
		query.setParameter("idSchedaEnteGestore", idEnte);
		String obj = (String) query.getSingleResult();
		
		return obj;
	}

}
