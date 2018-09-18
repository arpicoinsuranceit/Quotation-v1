package org.arpicoinsurance.groupit.main.dao.custom;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.AipPrintShedule;

public interface AipPrintSheduleDaoCustom {

	List<AipPrintShedule> findByQuoDetail(Integer id) throws Exception;

}
