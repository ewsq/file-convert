package com.googlecode.fileconvert.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class ScanPath {
	/**
	 * 给定目录下，待转换的文件列表
	 */
	private List<File> listFiles = new ArrayList<File>();

	public List<File> getListFiles() {
		return listFiles;
	}



	/**
	 * 列举所有文本文件
	 * 
	 * @param path
	 */
	private void listFiles(File path,FileFilter filter) {
		if (path.isDirectory()) {
			File[] files = path.listFiles(filter);
			for (File file1 : files) {
				if (file1.isFile() ) {
					listFiles.add(file1);
				} else if (file1.isDirectory()) {
					listFiles(file1,filter);
				}
			}
		}else{
			listFiles.add(path);
		}
	}

	/**
	 * 
	 * @param path
	 *            文件完整路径
	 * @param filter
	 *            文件名后缀
	 */
	public ScanPath(String path, FileFilter  filter) {

			listFiles(new File(path), filter);

	}

}