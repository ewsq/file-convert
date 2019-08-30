package com.googlecode.fileconvert.util.detector;

import eu.medsea.mimeutil.MimeUtil;

import java.io.File;
import java.util.Collection;
/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 */
public class FileTypeDetector {
	static {
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
	}

    /**
     * 根据MIME检测文件类型
     * @param file
     * @return
     */
	public static String detect(File file) {
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
		String fileType = mimeTypes.toString().trim();
	    return fileType;
	}

}
