package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.model.Child;

public class QuoChildBenef {
	private Child child;
	private ArrayList<QuoBenf> benfs;
	public Child getChild() {
		return child;
	}
	public void setChild(Child child) {
		this.child = child;
	}
	public ArrayList<QuoBenf> getBenfs() {
		return benfs;
	}
	public void setBenfs(ArrayList<QuoBenf> benfs) {
		this.benfs = benfs;
	}
	
	

}
