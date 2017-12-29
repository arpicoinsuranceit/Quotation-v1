package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class QuotationCalculation{
	private PersonalInfo _personalInfo;
	private ArrayList<Benifict> _riderDetails;
	
	public PersonalInfo get_personalInfo() {
		return _personalInfo;
	}
	public void set_personalInfo(PersonalInfo _personalInfo) {
		this._personalInfo = _personalInfo;
	}
	public ArrayList<Benifict> get_riderDetails() {
		return _riderDetails;
	}
	public void set_riderDetails(ArrayList<Benifict> _riderDetails) {
		this._riderDetails = _riderDetails;
	}
}
