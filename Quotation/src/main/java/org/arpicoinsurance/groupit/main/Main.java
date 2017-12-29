package org.arpicoinsurance.groupit.main;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.RateCardDao;
import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.model.RateCard;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner{
	
	@Autowired
	private RateCardINVPDao repository;
	
	public static void main(String args []) {
		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		//System.out.println(repository.getRate(18, 10, 8.0, new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
		//System.out.println(repository.getRate3(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))); 
		RateCardINVP  rateCardINVP= repository.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(18, 10, 8.0, new Date(), new Date(),new Date(),new Date());
		System.out.println(" Rate : " +rateCardINVP.getRate()+"");
		
	}

}
