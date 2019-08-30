package com.googlecode.fileconvert.util.filter;

import com.googlecode.fileconvert.util.detector.EncodingDetector;

import java.io.File;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * 文件编码过滤器
 */
public class EncodingFilter extends BaseFilter {

    public boolean doAccept(File file) {
        return EncodingDetector.detect(file).equals(data);
    }

    public EncodingFilter(BaseFilter nextFilter) {
        super(nextFilter);
    }

    public EncodingFilter(BaseFilter nextFilter, String data) {
        super(nextFilter, data);
    }

    public EncodingFilter(String data) {
        super(data);
    }

    public EncodingFilter() {
    }
}
