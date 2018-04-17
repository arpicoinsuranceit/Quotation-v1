package org.arpicoinsurance.groupit.main.dao.custom;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.MedicalRequirementsHelper;


public interface MedicalRequirementsDaoCustom {
	
	List<MedicalRequirementsHelper> findByQuoDetail(Integer id) throws Exception;


}
