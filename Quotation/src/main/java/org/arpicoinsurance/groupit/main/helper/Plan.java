package org.arpicoinsurance.groupit.main.helper;

public class Plan {
	private Integer _term;
	private String _frequance;
	private Double _bsa;
	private Double contribution;
	private Double _interestRate;
	private String _payingterm;
	private Double _msfb;
	
	public Integer get_term() {
		return _term;
	}
	public void set_term(Integer _term) {
		this._term = _term;
	}
	public String get_frequance() {
		return _frequance;
	}
	public void set_frequance(String _frequance) {
		this._frequance = _frequance;
	}
	public Double get_bsa() {
		return _bsa;
	}
	public void set_bsa(Double _bsa) {
		this._bsa = _bsa;
	}
	public Double getContribution() {
		return contribution;
	}
	public void setContribution(Double contribution) {
		this.contribution = contribution;
	}
	public Double get_interestRate() {
		return _interestRate;
	}
	public void set_interestRate(Double _intrate) {
		this._interestRate = _intrate;
	}
	public String get_payingterm() {
		return _payingterm;
	}
	public void set_payingterm(String _payingterm) {
		this._payingterm = _payingterm;
	}
	public Double get_msfb() {
		return _msfb;
	}
	public void set_msfb(Double _msfb) {
		this._msfb = _msfb;
	}
	
}
