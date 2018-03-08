package com.weaver.dcs.convert.pool;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConvertHtmlUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConvertHtmlUtil.class);

    public static int convert(String sourcePath)
    {
        logger.debug("ConvertHtmlUtil-->sourcePath:"+sourcePath);
        ConvertorPool instance = null;
        ConvertorPool.ConvertorObject convert = null;
        int returnInt = -5;
        try{
            File sourceFile = new File(sourcePath);
            if(sourceFile.exists() && sourceFile.isFile()) {
                if(StringUtils.isBlank(sourcePath) || StringUtils.isBlank(sourcePath)){
                    logger.debug("ConvertHtmlUtil-->sourcePath 为空");
                    return returnInt;
                } else {
                    int point = sourcePath.lastIndexOf(".");
                    logger.debug("ConvertHtmlUtil-->sourcePath 文件没有后缀");
                    if(point < 1) {
                        return returnInt;
                    }
                }
                instance = ConvertorPool.getInstance();
                convert = instance.getConvertor();
                returnInt = convert.convertor.convertMSToHtml(sourcePath,"");
                logger.debug("ConvertHtmlUtil-->returnInt："+returnInt);
                convert.convertor.deleteFiles("");
            }
        } catch(Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if(null != instance) {
                    instance.returnConvertor(convert);
                }
            } catch(Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return returnInt;
    }
}
