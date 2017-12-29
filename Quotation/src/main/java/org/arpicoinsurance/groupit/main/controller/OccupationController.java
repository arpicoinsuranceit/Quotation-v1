package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.service.OccupationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class OccupationController {

	@Autowired
	private OccupationServce occupationService;
	
	@RequestMapping(method=RequestMethod.GET, value="/occupation")
	public ArrayList<Occupation> getAll(){
		try {
			ArrayList<Occupation>occList=(ArrayList<Occupation>) occupationService.getAllOccupations();
			for (Occupation occupation : occList) {
				occupation.setOcupationCreateBy(null);
				occupation.setOcupationCreateDate(null);
				occupation.setOcupationModifyBy(null);
				occupation.setOcupationModifyDate(null);
			}
			return occList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
}
