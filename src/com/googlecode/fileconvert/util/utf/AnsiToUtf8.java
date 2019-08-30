package com.googlecode.fileconvert.util.utf;

import java.io.*;

public class AnsiToUtf8 {
    public static void main(String[] args) {
        change("F:\\test\\PublicMethod\\src\\com\\publicmetod\\func\\StrHex.java");
        //System.out.println(EncodingDetect.detect("D:/test.txt"));
        //System.out.println(EncodingDetect.detect("D:/Test/a.out"));
    }

    public static void change(String filepath){
        BufferedReader buf = null;
        OutputStreamWriter pw=null;
        String str = null;
        String allstr="";

        //用于输入换行符的字节码
        byte[] c=new byte[2];
        c[0]=0x0d;
        c[1]=0x0a;
        String t=new String(c);

        try {
            buf=new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            while((str = buf.readLine()) != null){
                allstr=allstr+str+t;
            }
            buf.close();

            pw =new OutputStreamWriter(new FileOutputStream(filepath),"UTF-8");
            pw.write(allstr);
            pw.flush();
            pw.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
