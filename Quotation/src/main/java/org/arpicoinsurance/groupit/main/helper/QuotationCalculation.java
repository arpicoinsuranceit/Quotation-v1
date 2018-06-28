package org.arpicoinsurance.groupit.main.helper;

public class QuotationCalculation{
	private String _product;
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
	public String get_product() {
		return _product;
	}
	public void set_product(String _product) {
		this._product = _product;
	}
	
	@Override
	public String toString() {
		return "QuotationCalculation [_product=" + _product + ", _personalInfo=" + _personalInfo + ", _riderDetails="
				+ _riderDetails + "]" + _personalInfo.toString() + _riderDetails;
	}
	
	
	
}
