package com.weaver.dcs.web;

import com.weaver.dcs.services.ImageFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 文档转换入口类
 */
@RestController
@RequestMapping("/dcs")

public class ConvertToHtml {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImageFileService imageFileService;

    /**
     * 文档转换入口，接收附件ID（imageFileId），返回生成html后的附件ID（imageFileId）
     * @param  imageFileId 需要转换的附件ID（imageFileId）
     * @return imageFileId 生成的html文件对应的附件ID（imageFileId）
     *         返回值说明：
     *             大于0时返回的是正常转换的html附件ID
     *             小于0时：
     *                 -1：文件不存在
     *                 -2：文件格式不支持
     *                 -3：文件过大
     *                 -4：word（页数过多或段落过多）、excel（行数或列数过多）
     *                 -5：转换失败
     */
    @RequestMapping(value = "/convertToHtml", method = RequestMethod.GET)
    public int convertToHtml(@RequestParam("imageFileId") int imageFileId) {
        try
        {
            logger.debug("convertToHtml----->imageFileId:"+imageFileId);
            int htmlImageFileId = imageFileService.toHtml(imageFileId);
            logger.debug("convertToHtml----->htmlImageFileId:"+htmlImageFileId);
            return htmlImageFileId;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return -5;
    }
}
