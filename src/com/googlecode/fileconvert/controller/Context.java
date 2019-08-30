
package com.googlecode.fileconvert.controller;

import com.googlecode.fileconvert.model.BatchConvert;

import javax.swing.*;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * 用于存储准备数据,转发请求,异常处理
 */
public class Context {
	private  String soureCode="";
	private  String targetCode="";
	private  String path="";
	private  String filter="";
   //对主窗口的引用
    private JFrame frame;

    public Context(JFrame frame) {
        this.frame = frame;
    }

    public String getSoureCode() {
        return soureCode;
    }

    public void setSoureCode(String soureCode) {
        this.soureCode = soureCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Context() {
		super();
		
	}

    /**
     *      转换文件
     * @return
     */
	public  boolean convert() {
        boolean ok;
        try {
            ok=	new BatchConvert().convert(path, filter, soureCode, targetCode);
        } catch (Exception e) {
        	e.printStackTrace();
        	ok=false;
           JOptionPane.showMessageDialog(frame, e.getMessage());
        }
        return ok;
    }




	

}
