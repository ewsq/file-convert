package com.googlecode.fileconvert.util.utf;

/**
 *
 */

import java.io.*;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.fileconvert.util.detector.EncodingDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 自动识别文件编码格式
 *
 */
public class EncodeUtil {
    private static Logger logger = LoggerFactory.getLogger(EncodeUtil.class);

    private static int BYTE_SIZE = 8;
    public static String CODE_UTF8 = "UTF-8";
    public static String CODE_UTF8_BOM = "UTF-8 BOM";
    public static String CODE_GBK = "GBK";

    /**
     * 通过文件全名称获取编码集名称
     *
     * @param fullFileName
     * @param ignoreBom
     * @return
     * @throws Exception
     */
    public static String getEncode(String fullFileName, boolean ignoreBom) throws Exception {
        logger.debug("fullFileName ; {}", fullFileName);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fullFileName));
        return getEncode(bis, ignoreBom);
    }
    /**
     * 通过文件全名称获取编码集名称
     *
     * @param file
     * @param ignoreBom
     * @return
     * @throws Exception
     */
    public static String getEncode(File file, boolean ignoreBom) throws Exception {
        logger.debug("fullFileName ; {}", file.getAbsolutePath());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        return getEncode(bis, ignoreBom);
    }
    /**
     * 通过文件缓存流获取编码集名称，文件流必须为未曾
     *
     * @param bis
     * @param ignoreBom 是否忽略utf-8 bom
     * @return
     * @throws Exception
     */
    public static String getEncode(BufferedInputStream bis, boolean ignoreBom){
        bis.mark(0);
        String encodeType = "未识别";
        byte[] head = new byte[3];
        try {
            bis.read(head);
            if (head[0] == -1 && head[1] == -2) {
                encodeType = "UTF-16";
            } else if (head[0] == -2 && head[1] == -1) {
                encodeType = "Unicode";
            } else if (head[0] == -17 && head[1] == -69 && head[2] == -65) { //带BOM
                if (ignoreBom) {
                    encodeType = CODE_UTF8;
                } else {
                    encodeType = CODE_UTF8_BOM;
                }
            } else if ("Unicode".equals(encodeType)) {
                encodeType = "UTF-16";
            } else if (isUTF8(bis)) {
                encodeType = CODE_UTF8;
            } else {
                encodeType = CODE_GBK;
            }
            logger.info("result encode type : " + encodeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodeType;
    }

    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param bis
     * @return
     */
    private static boolean isUTF8( BufferedInputStream bis){
        boolean bl=false;
        try {
            bis.reset();
            //读取第一个字节
            int code = bis.read();
            do {
                BitSet bitSet = convert2BitSet(code);
                //判断是否为单字节
                if (bitSet.get(0)) {//多字节时，再读取N个字节
                    if (!checkMultiByte(bis, bitSet)) {//未检测通过,直接返回
                        return false;
                    }
                } else {
                    //单字节时什么都不用做，再次读取字节
                }
                code = bis.read();
            } while (code != -1);
            bl=true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }
    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param filePath
     * @return
     */
    public static boolean isUTF8withoutBOM(String filePath) throws Exception {
        boolean bl=false;
        try {
            //String filePath ="F:\\test\\PublicMethod\\src\\com\\publicmethod\\Constant.java";
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
            String code = EncodeUtil.getEncode(bis, false);
            System.out.println("文件编码格式为："+code);
            if(CODE_UTF8_BOM.equalsIgnoreCase(code)) {
                bl=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }
    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param file
     * @return
     */
    public static boolean isUTF8withoutBOM(File file){
        boolean bl=false;
        try {
            //String filePath ="F:\\test\\PublicMethod\\src\\com\\publicmethod\\Constant.java";
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            String code = EncodeUtil.getEncode(bis, false);
            System.out.println("文件编码格式为："+code);
            if(CODE_UTF8.equalsIgnoreCase(code)) {
                bl=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }
    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param file
     * @return
     */
    public static boolean isUTF8withBOM(File file){
        boolean bl=false;
        try {
            //String filePath ="F:\\test\\PublicMethod\\src\\com\\publicmethod\\Constant.java";
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            String code = EncodeUtil.getEncode(bis, false);
            System.out.println("文件编码格式为："+code);
            if(CODE_UTF8_BOM.equalsIgnoreCase(code)) {
                bl=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }

    /**
     * 检测多字节，判断是否为utf8，已经读取了一个字节
     *
     * @param bis
     * @param bitSet
     * @return
     */
    private static boolean checkMultiByte(BufferedInputStream bis, BitSet bitSet) throws Exception {
        int count = getCountOfSequential(bitSet);
        byte[] bytes = new byte[count - 1];//已经读取了一个字节，不能再读取
        bis.read(bytes);
        for (byte b : bytes) {
            if (!checkUtf8Byte(b)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测单字节，判断是否为utf8
     *
     * @param b
     * @return
     */
    private static boolean checkUtf8Byte(byte b) throws Exception {
        BitSet bitSet = convert2BitSet(b);
        return bitSet.get(0) && !bitSet.get(1);
    }

    /**
     * 检测bitSet中从开始有多少个连续的1
     *
     * @param bitSet
     * @return
     */
    private static int getCountOfSequential( BitSet bitSet) {
        int count = 0;
        for (int i = 0; i < BYTE_SIZE; i++) {
            if (bitSet.get(i)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }


    /**
     * 将整形转为BitSet
     *
     * @param code
     * @return
     */
    private static BitSet convert2BitSet(int code) {
        BitSet bitSet = new BitSet(BYTE_SIZE);

        for (int i = 0; i < BYTE_SIZE; i++) {
            int tmp3 = code >> (BYTE_SIZE - i - 1);
            int tmp2 = 0x1 & tmp3;
            if (tmp2 == 1) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }


    /**
     * 获取字符串的unicode编码
     *
     * \ufeff控制字符 用来表示「字节次序标记（Byte Order Mark）」不占用宽度 unicode码中一个字符占用2个字节
     * @param s
     * @return
     */
    public static String stringToUnicode(String s) {
        if (s==null || s.length()<1) {
            return null;
        }
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");

            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");

                // 将字节码转化成十六进制(& oxff 是进行补码操作)
                String str = Integer.toHexString(bytes[i] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                out.append(str);
                String str1 = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str1.length(); j < 2; j++) {
                    out.append("0");
                }
                out.append(str1);
            }

            out.delete(0, "\\ufeff".length());
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * unicode码转化成字符串
     * @param str
     * @return
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            String group = matcher.group(2);
            ch = (char) Integer.parseInt(group, 16);
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }

    /**
     * 字符串转化成对应的utf8编码
     * @param s
     * @return 16进制的数据流
     */
    public static String convertStringToUTF8(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(Integer.toHexString(c).toUpperCase());
                } else {
                    byte[] b;
                    b = Character.toString(c).getBytes("utf-8");
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        // 转换为unsigned integer 无符号integer
                        /*
                         * if (k < 0) k += 256;
                         */
                        k = k < 0 ? k + 256 : k;
                        // 返回整数参数的字符串表示形式 作为十六进制（base16）中的无符号整数
                        // 该值以十六进制（base16）转换为ASCII数字的字符串
                        sb.append(Integer.toHexString(k).toUpperCase());

                        // url转置形式
                        // sb.append("%" +Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * UTF-8编码 转换为对应的 字符串
     * 实现方式：将16进制数转化成有符号的十进制数
     * @param s
     * @return
     */
    public static String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        try {
            s = s.toUpperCase();
            int total = s.length() / 2;
            //标识字节长度
            int pos = 0;
            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {
                int start = i * 2;
                //将字符串参数解析为第二个参数指定的基数中的有符号整数。
                buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
                pos++;
            }
            //通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
            //新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
            return new String(buffer, 0, pos, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * unicode码转化成utf8码
     * unicode码 -> 字符串 -> utf8码
     * @param str
     * @return
     */
    public static String unicodeToUTF8(String str){
        return EncodeUtil.convertStringToUTF8(EncodeUtil.unicodeToString(str));
    }

    /**
     * utf8码转化成unicode码
     * utf8码 -> 字符串 -> unicode码
     * @param str
     * @return
     */
    public static String utf8ToUnicode(String str){
        return EncodeUtil.stringToUnicode(EncodeUtil.convertUTF8ToString(str));
    }


    /**
     * 将一指定编码的文件转换为另一编码的文件
     *
     * @param oldFullFileName
     * @param oldCharsetName
     * @param newFullFileName
     * @param newCharsetName
     */
    public static void convert(String oldFullFileName, String oldCharsetName, String newFullFileName, String newCharsetName) throws Exception {
        logger.info("the old file name is : {}, The oldCharsetName is : {}", oldFullFileName, oldCharsetName);
        logger.info("the new file name is : {}, The newCharsetName is : {}", newFullFileName, newCharsetName);

        StringBuffer content = new StringBuffer();

        BufferedReader bin = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullFileName), oldCharsetName));
        String line;
        while ((line = bin.readLine()) != null) {
            content.append(line);
            content.append(System.getProperty("line.separator"));
        }
        newFullFileName = newFullFileName.replace("\\", "/");
        File dir = new File(newFullFileName.substring(0, newFullFileName.lastIndexOf("/")));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Writer out = new OutputStreamWriter(new FileOutputStream(newFullFileName), newCharsetName);
        out.write(content.toString());
    }

    public static void turnUTF8withoutBOM(String file, String targetFile) throws IOException {
        File fl=new File(targetFile);
        if (!fl.exists()) {
            fl.createNewFile();
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
        int i = 0;
        String str = "";
        while ((str = br.readLine()) != null) {
            if (i == 0)//读取第一行，将前三个字节去掉，重新new个String对象
            {
                byte[] bytes = str.getBytes("UTF-8");
                str = new String(bytes, 3, bytes.length - 3);
                bw.write(str+"\r\n");
                i++;
            } else
                bw.write(str+"\r\n");
        }
        br.close();
        bw.close();
    }

    public static void withUTF82withoutBOM(File file, File targetFile) throws IOException {
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
        int i = 0;
        String str = "";
        while ((str = br.readLine()) != null) {
            if (i == 0)//读取第一行，将前三个字节去掉，重新new个String对象
            {
                byte[] bytes = str.getBytes("UTF-8");
                str = new String(bytes, 3, bytes.length - 3);
                bw.write(str+"\r\n");
                i++;
            } else
                bw.write(str+"\r\n");
        }
        br.close();
        bw.close();
    }

    public static void main(String[] args) {
        try {
            String filePath ="F:\\test\\PublicMethod\\src\\com\\publicmetod\\func\\Logger.java";
            System.out.println("是否是UTF-8 BOM："+isUTF8withoutBOM(filePath));

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
            String code = EncodeUtil.getEncode(bis, false);
            System.out.println("文件编码格式为："+code);

            code =EncodingDetector.detect(new File(filePath));
            System.out.println("文件编码格式为："+code);
            /*if(CODE_UTF8_BOM.equalsIgnoreCase(code)) {
                turnUTF8withoutBOM(filePath,"F:\\test\\PublicMethod\\src\\com\\publicmethod\\Constant1.java");
            }*/


/*
            System.out.println(EncodeUtil.stringToUnicode("木999，你好，不错,~!@#$"));
            System.out.println(EncodeUtil.unicodeToString("\\u6728\\u0039\\u0039\\u0039\\uff0c\\u4f60\\u597d\\uff0c\\u4e0d\\u9519\\u002c\\u007e\\u0021\\u0040\\u0023\\u0024"));

            System.out.println(EncodeUtil.convertStringToUTF8("你好"));
            System.out.println(EncodeUtil.convertUTF8ToString("E4BDA0E5A5BD"));

            System.out.println(EncodeUtil.unicodeToUTF8("\\u6728\\u0039\\u0039\\u0039\\uff0c\\u4f60\\u597d\\uff0c\\u4e0d\\u9519\\u002c\\u007e\\u0021\\u0040\\u0023\\u0024"));

            System.out.println(EncodeUtil.utf8ToUnicode("E69CA8393939EFBC8CE4BDA0E5A5BDEFBC8CE4B88DE994992C7E21402324"));
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}