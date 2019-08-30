package com.googlecode.fileconvert.util;

import com.googlecode.fileconvert.util.utf.EncodeUtil;

import java.io.*;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * 文件转码工具类
 */
public class ConvertFile {

    /**
     * @param file           文件
     * @param targetEncoding 期望的文件编码
     * @return 转换成功返回 "ok" ,失败返回 文件的完整路径名
     * @throws IOException
     */
    public static boolean covertFile(File file, String sourceEncoding,
                                     String targetEncoding) throws IOException {
        if (sourceEncoding.equals(targetEncoding)) {
            return true;
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int) file.length()];
        bis.read(bytes);
        bis.close();

        String fileContent = "";
        BufferedWriter bw = null;
        if(EncodeUtil.isUTF8withBOM(file)&&sourceEncoding.equalsIgnoreCase("UTF-8 BOM")&&targetEncoding.equalsIgnoreCase("UTF-8")){
            fileContent = new String(bytes, 3, bytes.length - 3,"UTF-8");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), targetEncoding));
            bw.write(fileContent);
        }else if(EncodeUtil.isUTF8withoutBOM(file)&&sourceEncoding.equalsIgnoreCase("UTF-8")&&targetEncoding.equalsIgnoreCase("UTF-8 BOM")){
            byte[] head = new byte[3];
            head[0] = -17;
            head[1] = -69;
            head[2] = -65;
            byte[] targetBytes = new byte[(int) file.length()+3];
            System.arraycopy(head,0,targetBytes,0,head.length);
            System.arraycopy(bytes,0,targetBytes,3,bytes.length);
            fileContent = new String(targetBytes, "UTF-8");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(fileContent);
        }else if(!sourceEncoding.equalsIgnoreCase("UTF-8 BOM")&&!targetEncoding.equalsIgnoreCase("UTF-8 BOM")){
            System.out.println("sourceEncoding：" + sourceEncoding +"   targetEncoding：" + targetEncoding);
            fileContent = new String(bytes, sourceEncoding);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), targetEncoding));
            bw.write(fileContent);
        }else if(!sourceEncoding.equalsIgnoreCase("UTF-8")&&!sourceEncoding.equalsIgnoreCase("UTF-8 BOM")&&targetEncoding.equalsIgnoreCase("UTF-8 BOM")){
            fileContent = new String(bytes, sourceEncoding);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(fileContent);
        }else if(!targetEncoding.equalsIgnoreCase("UTF-8 BOM")){
            if(sourceEncoding.equalsIgnoreCase("UTF-8 BOM")){
                byte[] targetBytes = new byte[(int) file.length()-3];
                System.arraycopy(bytes,3,targetBytes,0,targetBytes.length);
                fileContent = new String(targetBytes, "UTF-8");
            }else{
                fileContent = new String(bytes, sourceEncoding);
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), targetEncoding));
            bw.write(fileContent);
        }
        if(bw!=null){
            bw.close();
        }
        return true;
    }
}
