package com.de.PDB;

import java.io.File;
import java.io.FileFilter;

public class MyFilter implements FileFilter {
	private String exName;
	public MyFilter(String exName){
		this.exName = exName;
	}
	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		return pathname.getName().endsWith(exName);
	}

}
