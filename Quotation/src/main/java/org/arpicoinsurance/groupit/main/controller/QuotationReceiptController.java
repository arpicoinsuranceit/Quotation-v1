package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.MediTestReceiptHelper;
import org.arpicoinsurance.groupit.main.helper.QuotationReceipt;
import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.arpicoinsurance.groupit.main.helper.ViewQuotation;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationReceiptService;
import org.arpicoinsurance.groupit.main.service.SheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin (origins = "*")
public class QuotationReceiptController {
	
	@Autowired
	private QuotationReceiptService quotationReceiptService;
	
	@Autowired
	private QuotationDetailsService quotationDetailService;
	
	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@Autowired
	private HealthRequirmentsService healthRequirmentsService;
	
	@Autowired
	private SheduleService sheduleService;
	
	@RequestMapping(value = "/quotationsearch/{id}")
	public List<QuotationSearch> getQuotationList(@PathVariable String id){
		try {
			return quotationReceiptService.searchQuotation(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/getquotationdetail/{qdId}")
	public QuotationReceipt getQuotationList(@PathVariable Integer qdId){
		try {
			return quotationDetailService.findQuotationDetailsForReceipt(qdId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/getQuoDetail",method=RequestMethod.POST)
	public ViewQuotation viewQuotation(@RequestParam("qdId") Integer qdId,@RequestParam("qId") Integer qId) {
		try {
			ViewQuotation viewQuo=quoBenefDetailService.getQuotationDetail(qdId, qId);
			System.out.println(viewQuo.get_mainlife().get_mCustCode() + "   custCode");
			return viewQuo;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value="/getShedule",method=RequestMethod.POST)
	public List<Shedule> getShedule(@RequestParam("qdId") Integer qdId) {
		try {
			List<Shedule> shedules = sheduleService.findByQuotationDetails(qdId);
			return shedules;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Shedule>();
	}
	
	@RequestMapping(value="/getMediDetails",method=RequestMethod.POST)
	public List<MediTestReceiptHelper> getMediTestReceiptHelper(@RequestParam("qdId") Integer qdId){
		
		System.out.println("called medi");
		
		try {
			List<MediTestReceiptHelper> list =  healthRequirmentsService.getMediTestByQuoDetails(qdId);
			
			System.out.println("aa");
			list.forEach(e -> System.out.println(e.toString()));
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<MediTestReceiptHelper>();
	}
	
	
	
	@RequestMapping(value="/isavailable",method=RequestMethod.POST)
	public String isQuotationAvailable (@RequestParam("qdId") Integer qdId, @RequestParam("qId") Integer qId) {
	
		try {
			if(quotationDetailService.isAvailable(qdId, qId)) {
				return "true";
			}
			return "false";
		}catch (Exception e) {
			return "false";
		}
	}
	
}
