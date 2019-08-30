package com.googlecode.fileconvert.util.detector;

import com.googlecode.fileconvert.util.utf.EncodeUtil;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 */
public class EncodingDetector {
    /**
     * 检测文件编码,如果检测不出作为ASCII文件处理
     * @param file
     * @return
     */
	public static String detect(File file)  {
		byte[] buf = new byte[4096];
        java.io.FileInputStream fis = null;
        UniversalDetector detector = new UniversalDetector(null);
        String encoding="";
        try {
            fis = new java.io.FileInputStream(file);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            encoding = detector.getDetectedCharset();
            detector.reset();
            //System.out.println("111111file.getName():"+file.getName());
            if (null==encoding) {
                encoding="ASCII";
            }
            //System.out.println("22222222222encoding:"+encoding);
            if(encoding.equalsIgnoreCase("UTF-8")){
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                encoding=EncodeUtil.getEncode(bis,false);
                //System.out.println("33333333333encoding:"+encoding);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return encoding;
	}
}
