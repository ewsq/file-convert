package com.googlecode.fileconvert.util.unicode;

import java.io.*;
/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * Unicode转码工具类
 */
public class ConvertUnicode {
    public static void toUnicode(File file, String sourceEncoding) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        byte[] bytes = new byte[(int) file.length()];
        bis.read(bytes);
        bis.close();
        String fileContent = new String(bytes, sourceEncoding);
        fileContent = Native2AsciiUtils.native2Ascii(fileContent);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "UTF-8"));
        bw.write(fileContent);
        bw.close();


    }

    public static void fromUnicode(File file, String targetEncoding) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        byte[] bytes = new byte[(int) file.length()];
        bis.read(bytes);
        bis.close();
        String fileContent = new String(bytes);
        fileContent = Native2AsciiUtils.ascii2Native(fileContent);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), targetEncoding));
        bw.write(fileContent);
        bw.close();
    }
}
