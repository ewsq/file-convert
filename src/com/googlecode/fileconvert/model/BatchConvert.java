package com.googlecode.fileconvert.model;

import com.googlecode.fileconvert.util.ConvertFile;
import com.googlecode.fileconvert.util.ScanPath;
import com.googlecode.fileconvert.util.unicode.ConvertUnicode;
import com.googlecode.fileconvert.util.detector.EncodingDetector;
import com.googlecode.fileconvert.util.filter.BaseFilter;
import com.googlecode.fileconvert.util.filter.RegexFilter;
import com.googlecode.fileconvert.util.filter.TypeFilter;
import com.googlecode.fileconvert.util.utf.EncodeUtil;
import com.googlecode.fileconvert.util.utf.EncodingDetect;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  /**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
   * 批量转码文件
 */
public class BatchConvert {
    public boolean convert(String path, String filterString, String sourceEncoding,
                          String targetEncoding) throws Exception {
        List<File> files = getFiles(path, filterString);
        return convertFiles(files, sourceEncoding, targetEncoding);

    }

    public List<File> getFiles(String path, String filterString) {
    	BaseFilter filter;
    	if (filterString.equals("")) {
    		 filter = new TypeFilter("(text.*)|application/octet-stream");	
		}else {
			 filter = new RegexFilter(new TypeFilter("(text/.*)|application/octet-stream"),filterString);
		}
        return new ScanPath(path, filter).getListFiles();
    }

    public boolean convertFiles(List<File> files, String sourceEncoding, String targetEncoding) throws Exception {
        if (sourceEncoding.equals(targetEncoding)) {
            throw new Exception("目标编码和源编码相同");
        }
        Map<File, String> noAscii = new HashMap<File, String>();
        Map<File, String> ascii = new HashMap<File, String>();
        if (sourceEncoding.equals("自动侦测") || sourceEncoding.equals("Unicode") || targetEncoding.equals("Unicode")) {
            for (File file : files) {
                //String encoding = EncodingDetector.detect(file);
                String encoding = EncodingDetect.detect(file);
                if (encoding.equals("ASCII")) {
                    ascii.put(file, "ASCII");
                } else {
                    noAscii.put(file, encoding);
                }
            }

        } else {
            if (!sourceEncoding.equals("Unicode") && !targetEncoding.equals("Unicode")) {
                for (File file : files) {
                    ConvertFile.covertFile(file, sourceEncoding, targetEncoding);
                }
                return true;
            }
        }
        if (sourceEncoding.equals("Unicode")) {
            for (File file : ascii.keySet()) {
                ConvertUnicode.fromUnicode(file, targetEncoding);
            }
        } else if (targetEncoding.equals("Unicode")) {
            if (!sourceEncoding.equals("自动侦测")) {
                for (File file : files) {
                    ConvertUnicode.toUnicode(file,sourceEncoding);
                }
            }
            for (File file : noAscii.keySet()) {
                ConvertUnicode.toUnicode(file, noAscii.get(file));
            }
        }else{
            for (File file : noAscii.keySet()) {
                ConvertFile.covertFile(file,noAscii.get(file),targetEncoding);
            }
            for (File file : ascii.keySet()) {
                ConvertFile.covertFile(file,ascii.get(file),targetEncoding);
            }
        }
        return true;
    }

}
