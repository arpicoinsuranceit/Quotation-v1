package org.arpicoinsurance.groupit.main.helper;

public class QuotationCalculation{
	private PersonalInfo _personalInfo;
	private RiderDetails _riderDetails = null;
	
	public PersonalInfo get_personalInfo() {
		return _personalInfo;
	}
	public void set_personalInfo(PersonalInfo _personalInfo) {
		this._personalInfo = _personalInfo;
	}
	public RiderDetails get_riderDetails() {
		return _riderDetails;
	}
	public void set_riderDetails(RiderDetails _riderDetails) {
		this._riderDetails = _riderDetails;
	}
	
}
