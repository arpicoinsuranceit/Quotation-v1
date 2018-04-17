package org.arpicoinsurance.groupit.main.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.arpicoinsurance.groupit.main.dao.custom.MedicalRequirementsDaoCustom;
import org.arpicoinsurance.groupit.main.dao.custom.MedicalRequirementsHelperRowMapper;
import org.arpicoinsurance.groupit.main.helper.MedicalRequirementsHelper;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class Medical_RequirementsDaoImpl implements MedicalRequirementsDaoCustom {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@PersistenceContext
	EntityManager entityManager;

	// @Autowired
	// JdbcTemplate jdbcTemplate;

	@Override
	public List<MedicalRequirementsHelper> findByQuoDetail(Integer id) throws Exception {

		List<MedicalRequirementsHelper> requirementsHelpers = null;
		try {
			List<Object> args = new ArrayList<>();
			args.add(id);

			requirementsHelpers = jdbcTemplate.query(
					"select x.med_name,if(max(x.main)=1,'Required','NA') main,if(max(x.spouse)=1,'Required','NA') spouse from (  "
							+ "select r.med_name,if(d.cust_status='main',1,0) main,   "
							+ "if(d.cust_status='spouse',1,0) spouse,d.medical_req_id  "
							+"from medical_details d inner join medical_req r on d.medical_req_id=r.id "
							+ "where d.quotation_detail_id=? ) x group by x.medical_req_id",
					args.toArray(), new ResultSetExtractor<List<MedicalRequirementsHelper>>() {

						@Override
						public List<MedicalRequirementsHelper> extractData(ResultSet resultSet)
								throws SQLException, DataAccessException {
							List<MedicalRequirementsHelper> requirementsHelpersTemp = new ArrayList<MedicalRequirementsHelper>();
							while (resultSet.next()) {
								MedicalRequirementsHelper requirementsHelper = getReq(resultSet);
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

	protected MedicalRequirementsHelper getReq(ResultSet resultSet) throws SQLException {
		MedicalRequirementsHelper helper = new MedicalRequirementsHelper();
		helper.setMainStatus(resultSet.getString("main"));
		helper.setMedicalReqname(resultSet.getString("med_name"));
		helper.setSpouseStatus(resultSet.getString("spouse"));
		return helper; 
	
		
	}

}
