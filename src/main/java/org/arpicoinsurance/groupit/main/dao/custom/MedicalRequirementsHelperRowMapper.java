package org.arpicoinsurance.groupit.main.dao.custom;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.arpicoinsurance.groupit.main.helper.MedicalRequirementsHelper;
import org.springframework.jdbc.core.RowMapper;

public class MedicalRequirementsHelperRowMapper implements RowMapper<MedicalRequirementsHelper> {

	@Override
	public MedicalRequirementsHelper mapRow(ResultSet rs, int rowNum) throws SQLException {
		MedicalRequirementsHelper medicalRequirementsHelper = new MedicalRequirementsHelper();
		medicalRequirementsHelper.setMedicalReqname(rs.getString("med_name"));
		medicalRequirementsHelper.setMainStatus(rs.getString("main"));
		medicalRequirementsHelper.setSpouseStatus(rs.getString("spouse"));

		
		return medicalRequirementsHelper;
	}

}
