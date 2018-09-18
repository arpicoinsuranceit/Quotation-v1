package org.arpicoinsurance.groupit.main.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.arpicoinsurance.groupit.main.dao.custom.AipPrintSheduleDaoCustom;
import org.arpicoinsurance.groupit.main.helper.AipPrintShedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class AipPrintSheduleDaoImpl implements AipPrintSheduleDaoCustom {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<AipPrintShedule> findByQuoDetail(Integer id) throws Exception {

		List<AipPrintShedule> requirementsHelpers = null;
		try {
			List<Object> args = new ArrayList<>();
			args.add(id);

			requirementsHelpers = jdbcTemplate.query(
					"select a.polyer, " 
					+ "a.prmpyr, " 
					+ "sum(a.rdrprm) rdrprm, " 
					+ "a.prmpad, " 
					+ "a.surrnd, "
					+ "(sum(es)+sum(bsum)+sum(atpb)+sum(FEB)) mlbpad, "
					+ "(sum(es)+sum(bsum)+sum(atpb)+sum(FEB)+sum(adb))mlbad "
					+ "from " 
					+ "(select a.polyer,a.padtrm,a.prmpyr,a.prmpad,a.surrnd,b.rier_code,b.rider_sum, " 
					+ "if(b.rier_code='L2',(b.rider_premium*a.padtrm)* " 
					+ "case " 
					+ "when c.pay_mode = 'M' then 12 " 
					+ "when c.pay_mode = 'Q' then 4 " 
					+ "when c.pay_mode = 'H' then 2 " 
					+ "when c.pay_mode = 'Y' then 1 "
					+ "when c.pay_mode = 'S' then 1 " 
					+ "end " 
					+ ",0.00) rdrprm, " 
					+ "if(b.rier_code='ADB',b.rider_sum,0.00) adb, " 
					+ "if(b.rier_code='ATPB',b.rider_sum,0.00) atpb, "
					+ "if(b.rier_code='FEB',b.rider_sum,0.00) feb, " 	 
					+ "if(b.rier_code='L2',b.rider_sum,0.00) bsum, "
					+ "if(b.rier_code='L2',((b.rider_sum*2.5)/100),0.00)*a.polyer es " 
					+ "from surrendervals a " 
					+ "inner join quotation_details c on  a.quotation_detail_id=c.qd_id " 
					+ "left outer join quo_benef_details b on a.quotation_detail_id=b.quodetails_id "
					+ "where a.quotation_detail_id=?  and b.rider_sum<>0 and b.rier_code in ('ADB','ATPB','L2','FEB'))a "
					+ "group by a.polyer "
					+ "order by CAST(a.polyer AS UNSIGNED) ",

					args.toArray(), new ResultSetExtractor<List<AipPrintShedule>>() {

						@Override
						public List<AipPrintShedule> extractData(ResultSet resultSet)
								throws SQLException, DataAccessException {
							List<AipPrintShedule> requirementsHelpersTemp = new ArrayList<AipPrintShedule>();
							while (resultSet.next()) {
								AipPrintShedule requirementsHelper = getReq(resultSet);
								requirementsHelpersTemp.add(requirementsHelper);
							}
							return requirementsHelpersTemp;

						}

					});

		} catch (DataAccessException dataAccessException) {
			throw dataAccessException;

		}
		return requirementsHelpers;

	}

	protected AipPrintShedule getReq(ResultSet resultSet) throws SQLException {
		AipPrintShedule shedule = new AipPrintShedule();
		shedule.setPolyer(resultSet.getInt("polyer"));
		shedule.setPrmpyr(resultSet.getDouble("prmpyr"));
		shedule.setRdrprm(resultSet.getDouble("rdrprm"));
		shedule.setPrmpad(resultSet.getDouble("prmpad"));
		shedule.setSurrnd(resultSet.getDouble("surrnd"));
		shedule.setMlbpad(resultSet.getDouble("mlbpad"));
		shedule.setMlbad(resultSet.getDouble("mlbad"));

		return shedule;

	}

}
