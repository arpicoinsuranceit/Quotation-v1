package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class InvpSavePersonalInfo {
	private MainLife _mainlife;
	private Spouse _spouse;
	private ArrayList<Children> _childrenList;
	private Plan _plan;
	
	public MainLife get_mainlife() {
		return _mainlife;
	}
	public void set_mainlife(MainLife _mainlife) {
		this._mainlife = _mainlife;
	}
	public Spouse get_spouse() {
		return _spouse;
	}
	public void set_spouse(Spouse _spouse) {
		this._spouse = _spouse;
	}
	
	public ArrayList<Children> get_childrenList() {
		return _childrenList;
	}
	public void set_childrenList(ArrayList<Children> _childrenList) {
		this._childrenList = _childrenList;
	}
	public Plan get_plan() {
		return _plan;
	}
	public void set_plan(Plan _plan) {
		this._plan = _plan;
	}
	@Override
	public String toString() {
		
		String childrenString = "";

		if (_childrenList != null) {

			for (Children children : _childrenList) {
				childrenString += children.toString();
			}
		}
		
		return "InvpSavePersonalInfo [_mainlife=" + _mainlife.toString() + ", _spouse=" + _spouse.toString() + ", _childrenList="
				+ childrenString + ", _plan=" + _plan.toString() + "]";
	}
	
	
}
