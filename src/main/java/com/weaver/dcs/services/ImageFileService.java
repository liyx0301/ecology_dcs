package com.weaver.dcs.services;

import com.weaver.dcs.bean.ImageFileBean;
import com.weaver.dcs.convert.pool.ConvertHtmlUtil;
import com.weaver.dcs.dao.ImageFileMapper;
import com.weaver.dcs.util.AESUtil;
import com.weaver.dcs.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * 文件转换服务类
 */

@Service
public class ImageFileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //临时文件输出目录，通过配置文件application.properties获取
    @Value("${convert.output}")
    private String output;

    @Autowired
    private ImageFileMapper imageFileMapper;

    public int toHtml(int imageFileId)
    {
        try
        {
            logger.debug("ImageFileService toHtml----->imageFileId:"+imageFileId);

            ImageFileBean imageFileBean = convertInfo(imageFileId);
            if(imageFileBean != null)
            {
                String convertStratus = imageFileBean.getConvertStatus(); // 0:转换中，1转换成功，2转换失败
                logger.debug("convertInfo-->convertStatus:"+convertStratus);

                if("1".equals(convertStratus))
                {
                    return imageFileBean.getHtmlFileid();
                }
                else if("0".equals(convertStratus))
                {
                    Thread.sleep(5000);
                    this.toHtml(imageFileId);
                }
                else if("2".equals(convertStratus))
                {
                    return -5; // 转换失败
                }

                String imageFileName = imageFileBean.getImageFileName();
                logger.debug("convertInfo-->imageFileName:"+imageFileName);
                if(imageFileName.toLowerCase().endsWith("doc")
                        || imageFileName.toLowerCase().endsWith("docx")
                        || imageFileName.toLowerCase().endsWith("xls")
                        || imageFileName.toLowerCase().endsWith("xlsx"))
                {
                    String filePath = this.getFilePathByImageFileId(imageFileBean);
                    logger.debug("convertInfo-->filePath:"+filePath);
                    if(filePath.startsWith("-"))
                    {
                        return -1; // 文件不存在
                    }

                    Integer htmlImageFileId = ConvertHtmlUtil.convert(filePath);
                    logger.debug("convertInfo-->htmlImageFileId:"+htmlImageFileId);
                    if(htmlImageFileId > 0)
                    {
                        return htmlImageFileId;
                    }
                    else
                    {
                        return -5; // 转换失败
                    }

                }
                else
                {
                    return -2; // 文件格式不支持
                }
            }
            else
            {
                return -1; // 文件不存在
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return -5; // 转换失败
    }

    public ImageFileBean convertInfo(int imageFileId) {
        try
        {
            return imageFileMapper.findImageFile(imageFileId);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return null;
    }

    private String getFilePathByImageFileId(ImageFileBean imageFileBean) {
        try
        {
            logger.debug("ImageFileService getFilePathByImageFileId----->imageFileId:"+imageFileBean.getImageFileId());

            if(imageFileBean == null)
            {
                logger.error("imageFileId文件损坏或不存在");
                return "-2：文件损坏或不存在";
            }
            int fileSize = imageFileBean.getFileSize();
            if(fileSize > 5*1024*1024)
            {
                logger.error("imageFileId 文件过大");
                return "-3：文件过大";
            }

            String fileName = imageFileBean.getImageFileName();
            String fileType = fileName.substring(fileName.indexOf("."));
            String fileRealPath = imageFileBean.getFileRealpath();
            String isZip = imageFileBean.getIsZip();
            logger.debug("ImageFileService getFilePathByImageFileId----->fileName:"+fileName);
            logger.debug("ImageFileService getFilePathByImageFileId----->fileType:"+fileType);
            logger.debug("ImageFileService getFilePathByImageFileId----->fileRealPath:"+fileRealPath);
            logger.debug("ImageFileService getFilePathByImageFileId----->isZip:"+isZip);
            if("1".equals(isZip))
            {
                fileRealPath = "/Users/liyx/IdeaProjects/test.zip"; //测试使用，使用完后需要删除
                String targetPath = output+File.separator+UUID.randomUUID().toString()+ fileType;
                logger.debug("ImageFileService getFilePathByImageFileId----->targetPath:"+targetPath);
                ZipUtil.extractFile(fileRealPath,fileName,targetPath);
                fileRealPath = targetPath;
                logger.debug("ImageFileService getFilePathByImageFileId----->fileRealPath:"+fileRealPath);
            }

            String isAESEncrypt = imageFileBean.getIsAESEncrypt();
            logger.debug("ImageFileService getFilePathByImageFileId----->isAESEncrypt:"+isAESEncrypt);

            if("1".equals(isAESEncrypt))
            {
                String targetPath = output+File.pathSeparator+UUID.randomUUID().toString()+ File.separator + fileType;
                String aesCode = imageFileBean.getAesCode();
                AESUtil.encryptAndDecrypt(fileRealPath,targetPath,aesCode,"2");
                fileRealPath = targetPath;
                logger.debug("ImageFileService getFilePathByImageFileId----->fileRealPath:"+fileRealPath);
            }
            return fileRealPath;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return "";
    }
}