package com.koreait.matzip;

import java.io.File;

public class FileUtils {
	
	//폴더 생성
	public static void makeFolder(String path) {
		
		File dir = new File(path);
		
		if(!dir.exists()) {
			dir.mkdirs();
		} 
	}
	
	public static String getExt(String fileNm) {
		return fileNm.substring(fileNm.lastIndexOf("."));
	}

}
