package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class RiderDetails {

	private ArrayList<Benifict> _mRiders = null;
	private ArrayList<Benifict> _sRiders = null;
	private ArrayList<Benifict> _cRiders = null;

	public ArrayList<Benifict> get_mRiders() {
		return _mRiders;
	}

	public void set_mRiders(ArrayList<Benifict> _mRiders) {
		this._mRiders = _mRiders;
	}

	public ArrayList<Benifict> get_sRiders() {
		return _sRiders;
	}

	public void set_sRiders(ArrayList<Benifict> _sRiders) {
		this._sRiders = _sRiders;
	}

	public ArrayList<Benifict> get_cRiders() {
		return _cRiders;
	}

	public void set_cRiders(ArrayList<Benifict> _cRiders) {
		this._cRiders = _cRiders;
	}

	@Override
	public String toString() {

		String mainRiders = "Main : ";
		if (_mRiders != null) {
			for (Benifict benifict : _mRiders) {
				mainRiders += benifict.toString();
			}
		}

		String spouseRiders = "Spouse : ";
		if (_sRiders != null) {
			for (Benifict benifict : _sRiders) {
				spouseRiders += benifict.toString();
			}
		}

		String childRiders = "Child : ";
		if (_cRiders != null) {
			for (Benifict benifict : _cRiders) {
				childRiders += benifict.toString();
			}
		}
		return "RiderDetails [_mRiders=" + _mRiders + ", _sRiders=" + _sRiders + ", _cRiders=" + _cRiders + "]"
				+ mainRiders + " : " + spouseRiders + " : " + childRiders;
	}

}
