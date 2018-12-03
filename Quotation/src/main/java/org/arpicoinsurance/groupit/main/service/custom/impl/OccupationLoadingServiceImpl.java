package org.arpicoinsurance.groupit.main.service.custom.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.dao.OccupationLodingDao;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.OcupationLoading;
import org.arpicoinsurance.groupit.main.service.custom.OccupationLoadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OccupationLoadingServiceImpl implements OccupationLoadingService {

	@Autowired
	private OccupationLodingDao occupationLodingDao;

	@Override
	public BigDecimal calculateOccupationLoading(boolean addOccuLoad, Double premium, Double bsa, Occupation occupation,
			Benefits benefict, QuotationQuickCalResponse calResp) throws Exception {
		
		if(occupation != null) {

			System.out.println(occupation.getOcupationName());
		}
		

		System.out.println(addOccuLoad + "  " + premium + "  " + bsa + "  "
				+ benefict.getBenefitName() + "  " + calResp.getWithoutLoadingTot() + "  "
				+ calResp.getOccuLodingTot());

		BigDecimal loadingPremium = null;

		System.out.println(calResp.getWithoutLoadingTot());

		if(occupation != null) {
				
			OcupationLoading loading = occupationLodingDao.findByOccupationAndBenefits(occupation, benefict);

			if (loading != null) {
				System.out.println(loading.toString());

				if (loading.getValue() == 0) {

					// premium = premium + ((bsa / 1000) * rete)

					if (loading.getRateMil() != 0) {

						loadingPremium = new BigDecimal(premium).add((new BigDecimal(bsa).divide(new BigDecimal(1000)))
								.setScale(4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(loading.getRateMil())))
								.setScale(4, BigDecimal.ROUND_HALF_UP);

					} else {
						loadingPremium = new BigDecimal(premium);
					}

				} else {
					loadingPremium = new BigDecimal(premium).multiply(new BigDecimal(loading.getValue())).setScale(0,
							BigDecimal.ROUND_HALF_UP);
				}

			} else {
				loadingPremium = new BigDecimal(premium);
			}
		} else {
			loadingPremium = new BigDecimal(premium);
		}
		
		

		System.out.println(loadingPremium.doubleValue());

		if (addOccuLoad) {
			calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot() + premium);
			calResp.setOccuLodingTot(calResp.getOccuLodingTot() + (loadingPremium.doubleValue() - premium));
		}

		System.out.println(calResp.getOccuLodingTot());

		return loadingPremium;

	}

//	
//	calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot() + bsa);
//	calResp.setOccuLodingTot(calResp.getOccuLodingTot() + (premium - bsa));
}
