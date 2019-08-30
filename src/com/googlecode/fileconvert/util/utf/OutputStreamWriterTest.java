package com.googlecode.fileconvert.util.utf;

import java.io.*;

public class OutputStreamWriterTest {

    public static void main(String[] args) {
        try {
            String sourceEncoding= null;
            OutputStreamWriter osw =null;
            String fileContent = null;
/*
            osw = new OutputStreamWriter(new FileOutputStream("F:\\test\\test.txt"));
            osw.write("学海无涯，维勤是岸！！！");
            sourceEncoding=osw.getEncoding();
            System.out.println("文件默认编码：" + sourceEncoding);// 使用getEncoding()方法取得当前系统的默认字符编码
            osw.close();
*/

            /*
             * 如果在调用FileOutputStream的构造方法时没有加入true，那么新加入的字符串就会替换掉原来写入的字符串，
             * 在调用构造方法时指定了字符的编码,新写入的字符，会使用新指定的编码
             */
            File file=new File("F:\\test\\PublicMethod\\src\\com\\publicmetod\\func\\StrHex.java");
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[(int) file.length()];
            bis.read(bytes);
            bis.close();


/*
            fileContent = new String(bytes, "GBK");
            osw = new OutputStreamWriter(new FileOutputStream("F:\\test\\test.txt"), "UTF-8");
            osw.write(fileContent);
            System.out.println("修改文件编码之后getEncoding：" + osw.getEncoding());
            osw.close();
*/


            fileContent = new String(bytes, "ASCII");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(fileContent);
            bw.close();


            System.out.println(getEncoding("F:\\test\\PublicMethod\\src\\com\\publicmetod\\func\\StrHex.java"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
    }

    public static String getEncoding(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        // 其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            case 0x5c75:
                code = "ANSI|ASCII";
                break;
            default:
                code = "GBK";
        }
        return code;
    }
}