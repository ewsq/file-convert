package com.googlecode.fileconvert.util.filter;

import com.googlecode.fileconvert.util.detector.FileTypeDetector;

import java.io.File;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * 文件类型过滤器
 */
public class TypeFilter extends BaseFilter{

    @Override
    public boolean doAccept(File file) {
           return FileTypeDetector.detect(file).matches(data);
    }

    public TypeFilter(BaseFilter nextFilter) {
        super(nextFilter);
    }

    public TypeFilter(BaseFilter nextFilter, String data) {
        super(nextFilter, data);
    }

    public TypeFilter(String data) {
        super(data);
    }

    public TypeFilter() {
    }
}
