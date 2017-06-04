package com.de.PDB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PartitionFile {
	private int size;
	private String srcpath;
	private String despath;
	
	public PartitionFile(String srcpath,String despath){
		 this.size = 0;
		 this.srcpath = srcpath;
		 this.despath = despath;
	}
	public PartitionFile(int size,String srcpath,String despath){
		 this.size = size;
		 this.srcpath = srcpath;
		 this.despath = despath;
	}
	
	public static void main(String[] args){
		String str1 = "C:\\Users\\Foggy\\Desktop\\merge";
		String str2 = "C:\\Users\\Foggy\\Desktop\\doctor";
		DBOperation dbo = null;
		PartitionFile pf = new PartitionFile(100,str1,str2);
//		pf.partitionData();
		pf.mergeData();
		File[] list = new File(str2).listFiles();
		for(File f:list){
			dbo = new DBOperation(f.getAbsolutePath()+"/thyroid.db",f.getAbsolutePath()+"/img");
			dbo.mergeTable("noduleInfo");
			dbo.mergeTable("noduleNum");
		}
	}
	
	public void partitionData(){
		int page,start,end;
		File[] imglist = new File(srcpath+"/img").listFiles(new MyFilter(".bin"));
		File[] masklist = new File(srcpath+"/mask").listFiles(new MyFilter(".bmp"));
		File db = new File(srcpath+"/thyroid.db");
		File data,img,mask;
		page = imglist.length/size;
		for(int i=0;i<page;i++){
			start = size*i;
			end = size*(i+1);
			data = new File(despath+"/数据"+(i+1));
			img = new File(despath+"/数据"+(i+1)+"/img");
			mask = new File(despath+"/数据"+(i+1)+"/mask");
//			createFolder(data);
			createFolder(img);
			createFolder(mask);
			try {
				Files.copy(db.toPath(),new File(data.getAbsolutePath()+"/"+db.getName()).toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int j=start;j<end;j++){
				try {
				Files.copy(imglist[j].toPath(),new File(img.getAbsolutePath()+"/"+imglist[j].getName()).toPath());
				Files.copy(masklist[j].toPath(),new File(mask.getAbsolutePath()+"/"+masklist[j].getName()).toPath());
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void mergeData(){
		File[] imglist,masklist,dblist;
		File[] list = new File(srcpath).listFiles();
		File desimg = new File(despath+"/img");
		File desmask =new File(despath+"/mask");
		File desdb =new File(despath+"/db");
		createFolder(desimg);
		createFolder(desmask);
		createFolder(desdb);
		for(int i=0;i<list.length;i++){
			imglist = new File(list[i].getPath()+"/img").listFiles(new MyFilter(".bin"));
			masklist = new File(list[i].getPath()+"/mask").listFiles(new MyFilter(".bmp"));
			dblist = new File(list[i].getPath()).listFiles(new MyFilter(".db"));
			for(File f:imglist){
				try {
					Files.copy(f.toPath(), new File(desimg.getAbsolutePath()+"/"+f.getName()).toPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(File f:masklist){
				try {
					Files.copy(f.toPath(), new File(desmask.getAbsolutePath()+"/"+f.getName()).toPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(File f:dblist){
				try {
					Files.copy(f.toPath(), new File(desdb.getAbsolutePath()+"/"+f.getName()).toPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			Files.copy(desdb.listFiles(new MyFilter(".db"))[0].toPath(), new File(desdb.getParent()+"/thyroid.db").toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private boolean createFolder(File file){
		if(!file.isDirectory()){
			file.mkdirs();
			return true;
		}else
			return false;
		
	}
}
