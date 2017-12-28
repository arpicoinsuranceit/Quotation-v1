package org.arpicoinsurance.groupit.main.controller;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Rms_Sbu;
import org.arpicoinsurance.groupit.main.service.Rms_SbuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
public class Rms_SbuController {

	@Autowired
	private Rms_SbuService sbuService;
	
	@RequestMapping("/test")
	public String navSaveSbu() {
		return "Success";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/sbu/{id}")
	public Rms_Sbu navGetSbu(@PathVariable Integer id) {
		try {
			Rms_Sbu sbu=sbuService.getSbu(id);
			System.out.println(sbu.getAddress());
			return sbu;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sbu")
	public String navSaveSbu(@RequestBody Rms_Sbu rms_Sbu) {
		try {
			if(sbuService.saveSbu(rms_Sbu)){
					return "201";
			}else {
				return "409";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "409";
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/sbu")
	public List<Rms_Sbu> navAllSbu() {
		try {
			return sbuService.getAllSbu();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/sbu/{id}")
	public String navDeleteSbu(@PathVariable Integer id) {
		try {
			
			if(sbuService.deleteSbu(id)){
				return "201";
			}else {
				return "409";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "409";
		
	}
}
