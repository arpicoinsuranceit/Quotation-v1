package org.arpicoinsurance.groupit.main.webclient;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.BenefictHistory;

public interface BenefictHistoryWebClient {

	List<BenefictHistory> getHistory (String nic) throws Exception;
}
