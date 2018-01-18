package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.model.CustomerDetails;

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
	
	
}
