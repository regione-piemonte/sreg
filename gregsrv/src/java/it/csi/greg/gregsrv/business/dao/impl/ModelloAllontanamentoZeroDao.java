/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.dto.FondiEnteAllontanamentoZero;
import it.csi.greg.gregsrv.dto.PrestazioniAlZeroPerFnpsDTO;
import it.csi.greg.gregsrv.dto.PrestazioniAllontanamentoZeroDTO;
import it.csi.greg.gregsrv.dto.ToggleValidazioneAllontanamentoZeroDTO;

@Repository("modelloAllontanamentoZeroDao")
@Transactional(readOnly = true)
public class ModelloAllontanamentoZeroDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<PrestazioniAllontanamentoZeroDTO> findAllPrestazioniAllontanamentoZero(Integer idEnte) {

		List<PrestazioniAllontanamentoZeroDTO> lista = new ArrayList<PrestazioniAllontanamentoZeroDTO>();

		Query query = em.createNativeQuery(" select gtpr.id_prest_reg_1, gtpr.cod_prest_reg_1  , gtpr.des_prest_reg_1,    "
				+ "				 (   "
				+ "				 	select grrmaz.valore as valore_per_prestazione_al_zero   "
				+ "				 	from greg.greg_r_rendicontazione_modulo_al_zero grrmaz    "
				+ "				 	where grrmaz.id_rendicontazione_ente = :idEnte   "
				+ "				 	and grrmaz.id_prest_reg_uno_al_zero = (   "
				+ "				 		select greg.greg_r_prest_reg_uno_al_zero.id_prest_reg_uno_al_zero    "
				+ "				 		from greg.greg_r_prest_reg_uno_al_zero   "
				+ "				 		where greg.greg_r_prest_reg_uno_al_zero.data_cancellazione is null   "
				+ "				 		and greg.greg_r_prest_reg_uno_al_zero.id_prest_reg_1 = gtpr.id_prest_reg_1   "
				+ "				 		and now() between greg.greg_r_prest_reg_uno_al_zero.data_inizio_validita and coalesce(greg.greg_r_prest_reg_uno_al_zero.data_fine_validita, now())   "
				+ "				 	)   "
				+ "				 ),   "
				+ "				 coalesce((   "
				+ "				 	select  grrpu.valore valore_per_famiglie_minori_b1   "
				+ "				 	from greg.greg_r_rendicontazione_preg1_utereg1 grrpu "
				+ "				 	where grrpu.id_rendicontazione_ente = :idEnte   "
				+ "				 	and grrpu.data_cancellazione is null   "
				+ "				 	and grrpu.id_prest_reg1_utenza_regionale1 = (   "
				+ "				 		select grprur.id_prest_reg1_utenza_regionale1   "
				+ "				 		from greg.greg_r_prest_reg1_utenze_regionali1 grprur   "
				+ "				 		where grprur.id_prest_reg_1 = gtpr.id_prest_reg_1   "
				+ "				 		and grprur.data_cancellazione is null   "
				+ "				 		and now() between grprur.data_inizio_validita and coalesce(grprur.data_fine_validita, now())    "
				+ "				 		and grprur.id_target_utenza = (   "
				+ "				 			select gdtu.id_target_utenza   "
				+ "				 			from greg.greg_d_target_utenza gdtu   "
				+ "				 			where gdtu.cod_utenza = 'U01'   "
				+ "				 			and gdtu.data_cancellazione is null   "
				+ "				 		)   "
				+ "				 	)   "
				+ "				 ),0)  - coalesce((select sum(grrpu.valore) "
				+ "from greg_r_rendicontazione_mod_a_part2 grrpu "
				+ "where grrpu.id_rendicontazione_ente = :idEnte   "
				+ "and grrpu.data_cancellazione is null   "
				+ "and (grrpu.id_prest_reg1_utenza_regionale1 =( "
				+ "select grprur.id_prest_reg1_utenza_regionale1   "
				+ "from greg.greg_r_prest_reg1_utenze_regionali1 grprur   "
				+ "where grprur.id_prest_reg_1 = ( "
				+ "	select c.id_prest_reg_1  "
				+ "	from greg_t_prestazioni_regionali_1 c "
				+ "	where c.cod_prest_reg_1 =gtpr.cod_prest_reg_1   || '_UT' "
				+ "	) "
				+ "and grprur.data_cancellazione is null   "
				+ "and now() between grprur.data_inizio_validita and coalesce(grprur.data_fine_validita, now())    "
				+ "and grprur.id_target_utenza = (   "
				+ "	select gdtu.id_target_utenza   "
				+ "	from greg.greg_d_target_utenza gdtu   "
				+ "	where gdtu.cod_utenza = 'U01'   "
				+ "	and gdtu.data_cancellazione is null   "
				+ ")  ) or grrpu.id_prest_reg1_utenza_regionale1 =( "
				+ "select grprur.id_prest_reg1_utenza_regionale1   "
				+ "from greg.greg_r_prest_reg1_utenze_regionali1 grprur   "
				+ "where grprur.id_prest_reg_1 = ( "
				+ "	select c.id_prest_reg_1  "
				+ "	from greg_t_prestazioni_regionali_1 c "
				+ "	where c.cod_prest_reg_1 =gtpr.cod_prest_reg_1 || '_ASR' "
				+ "	) "
				+ "and grprur.data_cancellazione is null   "
				+ "and now() between grprur.data_inizio_validita and coalesce(grprur.data_fine_validita, now())    "
				+ "and grprur.id_target_utenza = (   "
				+ "	select gdtu.id_target_utenza   "
				+ "	from greg.greg_d_target_utenza gdtu   "
				+ "	where gdtu.cod_utenza = 'U01'   "
				+ "	and gdtu.data_cancellazione is null   "
				+ ")  ))), 0) as valore_per_famiglie_minori_b1,   "
				+ "				 (   "
				+ "				 	select prest_reg_uno_al_zero_desc   "
				+ "				 	from greg_r_prest_reg_uno_al_zero   "
				+ "				  	where id_prest_reg_1 = gtpr.id_prest_reg_1   "
				+ "				 ) as desc_tooltip  "
				+ "				 from greg_t_prestazioni_regionali_1 gtpr    "
				+ "				 where id_prest_reg_1 in (   "
				+ "				 	select grpruaz.id_prest_reg_1   "
				+ "				 	from greg_r_prest_reg_uno_al_zero grpruaz   "
				+ "				 	where grpruaz.data_cancellazione is null   "
				+ "				 	and now() between grpruaz.data_inizio_validita and coalesce(grpruaz.data_fine_validita, now())   "
				+ "				 )   "
				+ "				 and gtpr.data_cancellazione is null   "
				+ "				 and now() between gtpr.data_inizio_validita and coalesce(gtpr.data_fine_validita, now())   "
				+ "				 order by gtpr.cod_prest_reg_1  ");
		query.setParameter("idEnte", idEnte);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		Iterator<Object> itr = result.iterator();
		while (itr.hasNext()) {
			Object[] o = (Object[]) itr.next();
			PrestazioniAllontanamentoZeroDTO prestazione = new PrestazioniAllontanamentoZeroDTO();
			prestazione.setPrestazioneId((Integer) o[0]);
			prestazione.setPrestazioneCod((String) o[1]);
			prestazione.setPrestazioneDesc((String) o[2]);
			prestazione.setValorePerPrestazioneAlZero((BigDecimal) o[3]);
			prestazione.setValorePerFamiglieMinoriB1((BigDecimal) o[4]);
			prestazione.setTooltipDesc((String) o[5]);
			lista.add(prestazione);
		}

		return lista;
	}

	public FondiEnteAllontanamentoZero findFondiEnteAllontanamentoZero(BigInteger idEnte, Integer annoRendicontazione) {

		Query query = em.createNativeQuery("select ( "
				+ "select grrf.valore "
				+ "from greg_r_rendicontazione_fondi grrf  "
				+ "where grrf.id_rendicontazione_ente = :idEnte "
				+ "and grrf.id_fondo = ( "
				+ "	select gdf.id_fondo "
				+ "	from greg_d_fondi gdf  "
				+ "	where gdf.cod_fondo = 'RIND' "
				+ "	and gdf.data_cancellazione is null "
				+ "	and :annoRendicontazione between EXTRACT(YEAR FROM gdf.data_inizio_validita) and coalesce (EXTRACT(YEAR FROM gdf.data_fine_validita), :annoRendicontazione) "
				+ ")and grrf.data_cancellazione is null) as fondo_regionale, "
				+ "( "
				+ "	select gdf.desc_fondo "
				+ "	from greg_d_fondi gdf  "
				+ "	where gdf.cod_fondo = 'RIND' "
				+ "	and gdf.data_cancellazione is null "
				+ "	and :annoRendicontazione between EXTRACT(YEAR FROM gdf.data_inizio_validita) and coalesce (EXTRACT(YEAR FROM gdf.data_fine_validita), :annoRendicontazione) "
				+ ") as labelFondiRegionale,  "
				+ "( "
				+ "select grrf.valore "
				+ "from greg_r_rendicontazione_fondi grrf  "
				+ "where grrf.id_rendicontazione_ente = :idEnte "
				+ "and grrf.id_fondo = ( "
				+ "	select gdf.id_fondo "
				+ "	from greg_d_fondi gdf "
				+ "	where gdf.cod_fondo = 'ALLZERO' "
				+ "	and gdf.data_cancellazione is null "
				+ "	and :annoRendicontazione between EXTRACT(YEAR FROM gdf.data_inizio_validita) and coalesce (EXTRACT(YEAR FROM gdf.data_fine_validita), :annoRendicontazione) "
				+ ")and grrf.data_cancellazione is null) as quota_allontanamento_zero,  "
				+ "( "
				+ "	select gdf.desc_fondo "
				+ "	from greg_d_fondi gdf  "
				+ "	where gdf.cod_fondo = 'ALLZERO' "
				+ "	and gdf.data_cancellazione is null "
				+ "	and :annoRendicontazione between EXTRACT(YEAR FROM gdf.data_inizio_validita) and coalesce (EXTRACT(YEAR FROM gdf.data_fine_validita), :annoRendicontazione) "
				+ ") as labelQuotaAllontanamentoZero ");
		query.setParameter("idEnte", idEnte);
		query.setParameter("annoRendicontazione", annoRendicontazione);

		Object result[] = (Object[]) query.getSingleResult();

		FondiEnteAllontanamentoZero fondi = new FondiEnteAllontanamentoZero(result);

		return fondi;
	}

	public String findGiustificativoAllontanamentoZero(Integer idRendEnte) {
		String giustificativo = null;
		try {
			Query query = em.createNativeQuery("SELECT grrgaz.giustificativo "
					+ "FROM greg.greg_r_rendicontazione_giustificazione_al_zero grrgaz "
					+ "WHERE grrgaz.id_rendicontazione_ente = :idRendEnte "
					+ "AND grrgaz.data_cancellazione is null ");
			query.setParameter("idRendEnte", idRendEnte);
			giustificativo = (String) query.getSingleResult();
		} catch (NoResultException e) {
			giustificativo = null;
		}
		return giustificativo;
	}

	public boolean getValidazioneAlZero(Integer idRendicontazioneEnte) {
		boolean isValidato = false;
		try {
			Query query = em.createNativeQuery("select gtre.validazione_al_zero  "
					+ "from greg_t_rendicontazione_ente gtre  "
					+ "where gtre.id_rendicontazione_ente = :idRendicontazioneEnte "
					+ "and gtre.data_cancellazione is null");
			query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);

//			Object result[] = (Object[]) query.getSingleResult();
//			isValidato = (boolean) result[0];
			isValidato = (boolean) query.getSingleResult();
		} catch (NoResultException e) {
			return false;
		}
		return isValidato;
	}

	public void toggleValidazioneAlZero(ToggleValidazioneAllontanamentoZeroDTO toggle) {
		Query query = em.createNativeQuery("update greg.greg_t_rendicontazione_ente  "
				+ "set validazione_al_zero = :toggle "
				+ "where id_rendicontazione_ente = :idRendicontazioneEnte "
				+ "and data_cancellazione is null");
		query.setParameter("toggle", toggle.getToggle());
		query.setParameter("idRendicontazioneEnte", toggle.getIdRendicontazioneEnte());

		query.executeUpdate();
	}

	public boolean checkExistsRecordPrestazione(Integer idPrestazione, Integer idRendicontazione) {
		BigInteger exists;
		try {
			Query query = em.createNativeQuery("select count(*) "
					+ "from greg_r_rendicontazione_modulo_al_zero grrmaz  "
					+ "where grrmaz.id_prest_reg_uno_al_zero = ( "
					+ "		select id_prest_reg_uno_al_zero "
					+ "		from greg_r_prest_reg_uno_al_zero "
					+ "		where id_prest_reg_1 = :idPrestazione "
					+ "	) "
					+ "and grrmaz.id_rendicontazione_ente = :idRendicontazioneEnte "
					+ "and data_cancellazione is null ");
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idRendicontazioneEnte", idRendicontazione);
			exists = (BigInteger) query.getSingleResult();
			return exists.intValue() > 0;
		} catch (NoResultException e) {
			return false;
		}
	}

	public void deleteRecordPrestazione(Integer idPrestazione, Integer idRendicontazione) {
		Query query = em.createNativeQuery("delete from greg_r_rendicontazione_modulo_al_zero "
				+ "where id_prest_reg_uno_al_zero = ( "
				+ "	select id_prest_reg_uno_al_zero "
				+ "	from greg_r_prest_reg_uno_al_zero "
				+ "	where id_prest_reg_1 = :idPrestazione "
				+ "	) "
				+ "and id_rendicontazione_ente = :idRendicontazioneEnte "
				+ "and data_cancellazione is null ");
		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idRendicontazioneEnte", idRendicontazione);
		query.executeUpdate();
	}

	public void updateRecordPrestazione(Integer idPrestazione, BigDecimal valore, String utenteOperazione, Integer idRendicontazione) {
		Query query = em.createNativeQuery("update greg_r_rendicontazione_modulo_al_zero  "
				+ "set "
				+ "	valore = :valore, "
				+ "	utente_operazione = :utenteOperazione, "
				+ "	data_modifica = now() "
				+ "where id_prest_reg_uno_al_zero = ( "
				+ "	select id_prest_reg_uno_al_zero "
				+ "	from greg_r_prest_reg_uno_al_zero "
				+ "	where id_prest_reg_1 = :idPrestazione "
				+ "	) "
				+ "and id_rendicontazione_ente = :idRendicontazioneEnte "
				+ "and data_cancellazione is null ");
		query.setParameter("valore", valore);
		query.setParameter("utenteOperazione", utenteOperazione);
		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idRendicontazioneEnte", idRendicontazione);
		query.executeUpdate();
	}

	public void insertRecordPrestazione(Integer idPrestazione, Integer idRendicontazioneEnte, BigDecimal valore,
			String utenteOperazione) {
		Query query = em.createNativeQuery("insert into greg_r_rendicontazione_modulo_al_zero "
				+ "( "
				+ "	id_prest_reg_uno_al_zero, "
				+ "	id_rendicontazione_ente, "
				+ "	valore, "
				+ "	utente_operazione, "
				+ "	data_creazione, "
				+ "	data_modifica "
				+ ") "
				+ "values "
				+ "( "
				+ "	( "
				+ "		select id_prest_reg_uno_al_zero "
				+ "		from greg_r_prest_reg_uno_al_zero "
				+ "		where id_prest_reg_1 = :idPrestazione "
				+ "	), "
				+ "	:idRendicontazioneEnte, "
				+ "	:valore, "
				+ "	:utenteOperazione, "
				+ "	now(), "
				+ "	now() "
				+ ") ");
		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("valore", valore);
		query.setParameter("utenteOperazione", utenteOperazione);

		query.executeUpdate();

	}

	public String checkExistsRecordGiustificazione(Integer idRendicontazioneEnte) {
		try {
			Query query = em.createNativeQuery("select grrgaz.giustificativo "
					+ "from greg_r_rendicontazione_giustificazione_al_zero grrgaz "
					+ "where grrgaz.id_rendicontazione_ente  = :idRendicontazioneEnte "
					+ "and data_cancellazione is null ");
			query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
			return (String) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void insertRecordGiustificativo(Integer idRendicontazioneEnte, String giustificativo,
			String utenteOperazione) {
		Query query = em.createNativeQuery("insert into greg_r_rendicontazione_giustificazione_al_zero  "
				+ "( "
				+ "	id_rendicontazione_ente , "
				+ "	giustificativo , "
				+ "	utente_operazione, "
				+ "	data_creazione, "
				+ "	data_modifica "
				+ ") "
				+ "values "
				+ "( "
				+ "	:idRendicontazioneEnte, "
				+ "	:giustificativo, "
				+ "	:utenteOperazione, "
				+ "	now(), "
				+ "	now() "
				+ ") ");
		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("giustificativo", giustificativo);
		query.setParameter("utenteOperazione", utenteOperazione);

		query.executeUpdate();
	}

	public Integer deleteLogicGiustificativo(Integer idRendicontazioneEnte, String utenteOperazione) {
		Query query = em.createNativeQuery("update greg_r_rendicontazione_giustificazione_al_zero   "
				+ "set "
				+ "	utente_operazione = :utenteOperazione, "
				+ "	data_modifica = now(), "
				+ "	data_cancellazione = now() "
				+ "where id_rendicontazione_ente  = :idRendicontazioneEnte");
		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("utenteOperazione", utenteOperazione);
		return query.executeUpdate();
	}

	public Integer updateRecordGiustificativo(Integer idRendicontazioneEnte, String giustificativo,
			String utenteOperazione) {
		Query query = em.createNativeQuery("update greg_r_rendicontazione_giustificazione_al_zero   "
				+ "set "
				+ "	giustificativo = :giustificativo, "
				+ "	utente_operazione = :utenteOperazione, "
				+ "	data_modifica = now() "
				+ "where id_rendicontazione_ente  = :idRendicontazioneEnte "
				+ "and data_cancellazione is null ");
		query.setParameter("giustificativo", giustificativo);
		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("utenteOperazione", utenteOperazione);
		return query.executeUpdate();
	}
	
	public String getStatusModelloAlZero(Integer idRendicontazioneEnte, Integer annoRendicontazione) {
		Query query = em.createNativeQuery(
				 "SELECT  "
				 + "    case "
				 + "	    WHEN bianco = 0 THEN 'bianco' "
				 + "	    WHEN black = 1 THEN 'nero'  "
				 + "	    WHEN (quota_allontanamento_zero - somma_valori_modulo_al_zero) <> 0 THEN 'rosso'  "
				 + "	    WHEN (quota_allontanamento_zero - somma_valori_modulo_al_zero) = 0 AND NOT validazione_modello_al_zero THEN 'giallo'  "
				 + "     WHEN (quota_allontanamento_zero - somma_valori_modulo_al_zero) = 0 AND validazione_modello_al_zero THEN 'verde'  "
				 + "     else 'rosso'  "
				 + "    END AS colore  "
				 + "FROM (  "
				 + "    select"
				 + "    	("
				 + "    		select count(grrmaz.valore)"
				 + "			from greg.greg_r_rendicontazione_modulo_al_zero grrmaz"
				 + "			where grrmaz.id_rendicontazione_ente = :idRendicontazioneEnte"
				 + "			and grrmaz.valore > 0"
				 + "			and grrmaz.data_cancellazione is null"
				 + "			and now() between grrmaz.data_inizio_validita and coalesce(grrmaz.data_fine_validita, now())"
				 + "    	) as bianco,"
				 + "        (  "
				 + "            SELECT sum(grrmaz.valore)  "
				 + "            FROM greg.greg_r_rendicontazione_modulo_al_zero grrmaz   "
				 + "            WHERE grrmaz.id_rendicontazione_ente = :idRendicontazioneEnte  "
				 + "            AND grrmaz.data_cancellazione IS NULL  "
				 + "        ) AS somma_valori_modulo_al_zero,  "
				 + "        (  "
				 + "          	select grrf.valore  "
				 + "			from greg_r_rendicontazione_fondi grrf   "
				 + "			where grrf.id_rendicontazione_ente = :idRendicontazioneEnte  "
				 + "			and grrf.id_fondo = (  "
				 + "				select gdf.id_fondo  "
				 + "				from greg_d_fondi gdf  "
				 + "				where gdf.cod_fondo = 'ALLZERO'  "
				 + "				and gdf.data_cancellazione is null  "
				 + "				and :annoRendicontazione between EXTRACT(YEAR FROM gdf.data_inizio_validita) and coalesce (EXTRACT(YEAR FROM gdf.data_fine_validita), :annoRendicontazione)  "
				 + "			)and grrf.data_cancellazione is null  "
				 + "        ) AS quota_allontanamento_zero,  "
				 + "        (  "
				 + "            SELECT gtre.validazione_al_zero   "
				 + "            FROM greg_t_rendicontazione_ente gtre   "
				 + "            WHERE gtre.id_rendicontazione_ente = :idRendicontazioneEnte  "
				 + "            AND gtre.data_cancellazione IS NULL  "
				 + "        ) AS validazione_modello_al_zero,  "
				 + "        (  "
				 + "			select max (  "
				 + "    		CASE   "
				 + "        		WHEN valore_per_prestazione_al_zero > valore_per_famiglie_minori_b1 THEN 1  "
				 + "        		ELSE 0  "
				 + "    		end )  "
				 + "			FROM (  "
				 + "				select (  "
				 + "					select grrmaz.valore  "
				 + "					from greg.greg_r_rendicontazione_modulo_al_zero grrmaz   "
				 + "					where grrmaz.id_rendicontazione_ente = :idRendicontazioneEnte  "
				 + "					and grrmaz.id_prest_reg_uno_al_zero = (  "
				 + "						select greg.greg_r_prest_reg_uno_al_zero.id_prest_reg_uno_al_zero   "
				 + "						from greg.greg_r_prest_reg_uno_al_zero  "
				 + "						where greg.greg_r_prest_reg_uno_al_zero.data_cancellazione is null  "
				 + "						and greg.greg_r_prest_reg_uno_al_zero.id_prest_reg_1 = gtpr.id_prest_reg_1  "
				 + "						and now() between greg.greg_r_prest_reg_uno_al_zero.data_inizio_validita and coalesce(greg.greg_r_prest_reg_uno_al_zero.data_fine_validita, now())  "
				 + "					)  "
				 + "				) as valore_per_prestazione_al_zero,  "
				 + "				(  "
				 + "					select grrpu.valore  "
				 + "					from greg.greg_r_rendicontazione_preg1_utereg1 grrpu   "
				 + "					where grrpu.id_rendicontazione_ente = :idRendicontazioneEnte  "
				 + "					and grrpu.data_cancellazione is null  "
				 + "					and grrpu.id_prest_reg1_utenza_regionale1 = (  "
				 + "						select grprur.id_prest_reg1_utenza_regionale1  "
				 + "						from greg.greg_r_prest_reg1_utenze_regionali1 grprur  "
				 + "						where grprur.id_prest_reg_1 = gtpr.id_prest_reg_1  "
				 + "						and grprur.data_cancellazione is null  "
				 + "						and now() between grprur.data_inizio_validita and coalesce(grprur.data_fine_validita, now())   "
				 + "						and grprur.id_target_utenza = (  "
				 + "							select gdtu.id_target_utenza  "
				 + "							from greg.greg_d_target_utenza gdtu  "
				 + "							where gdtu.cod_utenza = 'U01'  "
				 + "							and gdtu.data_cancellazione is null  "
				 + "						)  "
				 + "					)  "
				 + "				) as valore_per_famiglie_minori_b1  "
				 + "				from greg_t_prestazioni_regionali_1 gtpr   "
				 + "				where id_prest_reg_1 in (  "
				 + "					select grpruaz.id_prest_reg_1  "
				 + "					from greg_r_prest_reg_uno_al_zero grpruaz  "
				 + "					where grpruaz.data_cancellazione is null  "
				 + "					and now() between grpruaz.data_inizio_validita and coalesce(grpruaz.data_fine_validita, now())  "
				 + "				)  "
				 + "				and gtpr.data_cancellazione is null  "
				 + "				and now() between gtpr.data_inizio_validita and coalesce(gtpr.data_fine_validita, now())  "
				 + "				order by gtpr.cod_prest_reg_1  "
				 + "			)as subquery   "
				 + "		) as black  "
				 + ") AS subquery");
		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		query.setParameter("annoRendicontazione", annoRendicontazione);
		return (String)query.getSingleResult();
	}
	
	
	public List<PrestazioniAlZeroPerFnpsDTO> getPrestazioniAdattamentoFnps(Integer idRendicontazioneEnte) {
		
		List<PrestazioniAlZeroPerFnpsDTO> lista = new ArrayList<PrestazioniAlZeroPerFnpsDTO>();
		
		Query query = em.createNativeQuery("select "
				+ "( "
				+ "	select gtpm.cod_prestazione_ministeriale "
				+ "	from greg_t_prestazioni_ministeriali gtpm, greg_r_prest_reg1_prest_minist grprpm  "
				+ "	where grprpm.id_prest_reg1 = gtpr.id_prest_reg_1 "
				+ "	and grprpm.id_prest_minist = gtpm.id_prestazione_ministeriale "
				+ "	and gtpm.data_cancellazione is null "
				+ "	and grprpm.data_cancellazione is null "
				+ "	and now() between grprpm.data_inizio_validita and coalesce(grprpm.data_fine_validita, now())  "
				+ ") as cod_prestazione_ministeriale, "
				+ "coalesce (sum( "
				+ "		(  "
				+ "			select grrmaz.valore as valore_per_prestazione_al_zero  "
				+ "			from greg.greg_r_rendicontazione_modulo_al_zero grrmaz   "
				+ "			where grrmaz.id_rendicontazione_ente = :idRendicontazioneEnte  "
				+ "			and grrmaz.id_prest_reg_uno_al_zero = (  "
				+ "				select greg.greg_r_prest_reg_uno_al_zero.id_prest_reg_uno_al_zero   "
				+ "				from greg.greg_r_prest_reg_uno_al_zero  "
				+ "				where greg.greg_r_prest_reg_uno_al_zero.data_cancellazione is null  "
				+ "				and greg.greg_r_prest_reg_uno_al_zero.id_prest_reg_1 = gtpr.id_prest_reg_1  "
				+ "				and now() between greg.greg_r_prest_reg_uno_al_zero.data_inizio_validita and coalesce(greg.greg_r_prest_reg_uno_al_zero.data_fine_validita, now())  "
				+ "			)  "
				+ "		) "
				+ "), 0 "
				+ ") as somma_valori "
				+ "from greg_t_prestazioni_regionali_1 gtpr   "
				+ "where gtpr.id_prest_reg_1 in (  "
				+ "	select grpruaz.id_prest_reg_1  "
				+ "	from greg_r_prest_reg_uno_al_zero grpruaz  "
				+ "	where grpruaz.data_cancellazione is null  "
				+ "	and now() between grpruaz.data_inizio_validita and coalesce(grpruaz.data_fine_validita, now())  "
				+ ") "
				+ "and gtpr.data_cancellazione is null  "
				+ "and now() between gtpr.data_inizio_validita and coalesce(gtpr.data_fine_validita, now()) "
				+ "group by cod_prestazione_ministeriale "
				+ "order by cod_prestazione_ministeriale");
		query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
		
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		Iterator<Object> itr = result.iterator();
		while (itr.hasNext()) {
			Object[] o = (Object[]) itr.next();
			PrestazioniAlZeroPerFnpsDTO prestazione = new PrestazioniAlZeroPerFnpsDTO();
			prestazione.setCodPrestMinisteriale((String) o[0]);
			prestazione.setSommaValore((BigDecimal) o[1]);
			lista.add(prestazione);
		}

		return lista;
	}
}
