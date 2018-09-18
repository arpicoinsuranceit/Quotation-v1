package org.arpicoinsurance.groupit.main.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class QuotationCalculationController {

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/ageCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateAge(@RequestBody String dob) {
		try {
			Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String parsedDate = formatter.format(initDate);
			// System.out.println(parsedDate+" ddddddddddddddddddddddddddddddddddddddd");
			LocalDate dateOfBirth = LocalDate.parse(parsedDate);
			LocalDate currentDate = LocalDate.now();
			long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			if(diffInYears == 0) {
				long diffInMonth = ChronoUnit.MONTHS.between(dateOfBirth, currentDate);
				
				if(diffInMonth<6) {
					return new ResponseEntity<Object>(diffInYears , HttpStatus.OK);
				}
				//System.out.println(diffInMonth);
			}
			diffInYears = diffInYears + 1;
			return new ResponseEntity<Object>(diffInYears , HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n dob : " + dob);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateAge : QuotationCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}
	
	
	@RequestMapping(value = "/ageCalNominee", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateAgeNominee(@RequestBody String dob) {
		try {
			Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String parsedDate = formatter.format(initDate);
			// System.out.println(parsedDate+" ddddddddddddddddddddddddddddddddddddddd");
			LocalDate dateOfBirth = LocalDate.parse(parsedDate);
			LocalDate currentDate = LocalDate.now();
			long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			diffInYears = diffInYears + 1;
			return new ResponseEntity<Object>(diffInYears , HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n dob : " + dob);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateAge : QuotationCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	/*
	 * @RequestMapping(value="/asfpCal",method=RequestMethod.POST) public String
	 * calculateASFP() { try { asfpService.calculateL10(29, 10, 6.0, new Date(),
	 * 100000.0, 1); asfpService.calculateL2(10, 100000.00);
	 * sfpoService.calculateSFPO(29, 10, new Date(), 1000000.0, "Y", 1.0); return
	 * "ok";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return null; }
	 */

	/*
	 * @RequestMapping(value="/atrmCal",method=RequestMethod.POST) public String
	 * calculateATRM() { try { atrmService.calculateL2(1,29, 30, 0.0, new Date(),
	 * 2500000.0, 12); return "ok";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return null; }
	 */
	/*
	 * @RequestMapping(value="/endCal",method=RequestMethod.POST) public String
	 * calculateEND() { try { endService.calculateL2(1,29, 10, 0.0, new Date(),
	 * 500000.0, 12); endService.calculateMaturity(10, 500000.00); return "ok";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return null; }
	 */

	/*
	 * @RequestMapping(value="/dtaCal",method=RequestMethod.POST) public String
	 * calculateDTA() { try { dtaService.calculateL2(29, 10, 22.0, "M", new Date(),
	 * 15000000.0); tpddtaService.calculateTPDDTA(29, 10, 22.0, "M", new Date(),
	 * 15000000.0); jlbService.calculateJLB(27, 10, 22.0, "F", new Date(),
	 * 15000000.0); tpddtasService.calculateTPDDTAS(27, 10, 22.0, "F", new Date(),
	 * 15000000.0); return "ok";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return null; }
	 */

	/*
	 * @RequestMapping(value="/dtaplCal",method=RequestMethod.POST) public String
	 * calculateDTAPL() { try { dtaplService.calculateL2(29, 10, 22.0, "M", new
	 * Date(), 15000000.0); tpddtaplService.calculateTPDDTAPL(29, 10, 22.0, "M", new
	 * Date(), 15000000.0); jlbplService.calculateJLBPL(27, 10, 22.0, "F", new
	 * Date(), 15000000.0); tpddtasplService.calculateTPDDTASPL(27, 10, 22.0, "F",
	 * new Date(), 15000000.0); return "ok";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return null; }
	 */

	@RequestMapping(value = "/ageCalculate", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateAgeFromNic(@RequestBody String nic) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			
			int year = 0;
			int day = 0;
			int bday = 0;
			int month = 0;
			
			if (nic.length() == 9) {
				year = (1900 + Integer.parseInt(nic.substring(0, 2)));
				// System.out.println("---- "+nic);
				day = Integer.parseInt(nic.substring(2, 5));
			} else if (nic.length() == 12) {
				year = Integer.parseInt(nic.substring(0, 4));
				day = Integer.parseInt(nic.substring(4, 7));
			}
			
			
			Date date = new SimpleDateFormat("D yyyy").parse(day + " " + year);
			
			System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(date));

			Integer[] daysofmonth = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			int daystodate = 0;

			if (day >= 500) {
				day -= 500;
			}

			for (int i = 0; i < 12; ++i) {
				daystodate += daysofmonth[i];
				if (daystodate > day) {
					month = i + 1;
					bday = daysofmonth[i] - (daystodate - day);
					break;
				}
			}

			if (bday == 0) {
				bday++;
			}

			String birthday = (bday < 10 ? ("0" + bday) : bday) + "-" + (month < 10 ? ("0" + month) : month) + "-"
					+ (year > 1000 ? year : "19" + year);

			Calendar dob = Calendar.getInstance();
			dob.set(year, Integer.parseInt(month < 10 ? ("0" + month) : month + ""),
					Integer.parseInt(bday < 10 ? ("0" + bday) : bday + ""));
			Calendar today = Calendar.getInstance();
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			if ((today.get(Calendar.MONTH) + 1) > dob.get(Calendar.MONTH)) {
				age++;
			} else if (((today.get(Calendar.MONTH) + 1) == (dob.get(Calendar.MONTH)))
					&& today.get(Calendar.DAY_OF_MONTH) >= dob.get(Calendar.DAY_OF_MONTH)) {
				age++;
			}

			map.put("Age", age);
			map.put("DOB", birthday);
			map.put("Gender", getGender(nic));

			// System.out.println(birthday);
			return new ResponseEntity<Object>(map , HttpStatus.OK);

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + nic);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateAgeFromNic : QuotationCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	private String getGender(String nic) {
		int day = 0;
		if (nic.length() == 9) {
			day = Integer.parseInt(nic.substring(2, 5));
		} else if (nic.length() == 12) {
			day = Integer.parseInt(nic.substring(4, 7));
		}

		if (day >= 500) {
			return "F";
		} else {
			return "M";
		}

	}

}