package com.googlecode.fileconvert.util.filter;

import java.io.File;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * 文件后缀过滤器
 */
public class RegexFilter extends BaseFilter{
    @Override
    public boolean doAccept(File file) {
        return file.getName().matches(data);
    }

    public RegexFilter() {
    }

    public RegexFilter(String data) {
        super(data);
    }

    public RegexFilter(BaseFilter nextFilter, String data) {
        super(nextFilter, data);
    }

    public RegexFilter(BaseFilter nextFilter) {
        super(nextFilter);
    }
}
