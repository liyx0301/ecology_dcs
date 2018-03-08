package com.weaver.dcs.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageFileBean {
    /**
     * 附件id
     */
    private Integer imageFileId;

    /**
     * 附件名
     */
    private String imageFileName;

    /**
     * 绝对路径
     */
    private String fileRealpath;

    /**
     * 是否压缩
     */
    private String isZip;

    /**
     * 文件大小
     */
    private Integer fileSize;

    /**
     * 是否加密
     */
    private String isAESEncrypt;

    /**
     * 密钥
     */
    private String aesCode;

    /**
     *  转换状态
     */
    private String convertStatus;

    /**
     * 转换后的html附件ID
     */
    private Integer htmlFileid;
}
