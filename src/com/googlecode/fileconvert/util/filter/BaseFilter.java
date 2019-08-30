package com.googlecode.fileconvert.util.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 * 文件过滤器基类,类似javaIO包得包装器模式
 * 注意 ! 子类必须实现父类的所有构造方法
 */
public class BaseFilter implements FileFilter{
     protected boolean result=false;
    protected String data;
    protected BaseFilter nextFilter=null;

   

	public BaseFilter(BaseFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public BaseFilter(BaseFilter nextFilter,String data) {
        this.nextFilter = nextFilter;
        this.data=data;
    }

    public BaseFilter(String  data) {
        this.data = data;
    }

    public BaseFilter() {
    }

    /**
     * listFiles(Filter filter)调用的方法,链式调用下一个拦截器
     * @param file
     * @return
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        if (nextFilter != null) {
            result = nextFilter.accept(file) && doAccept(file);
        } else {
            result = doAccept(file);
        }

        return result;
    }

    /**
     * 子类重写的方法,声明过滤器时可以类似IO流的包装,声明一个过滤器链,过滤器由右向左依次执行
     * @param file
     * @return
     */
    public boolean doAccept(File file){
          return true;
    }

}
