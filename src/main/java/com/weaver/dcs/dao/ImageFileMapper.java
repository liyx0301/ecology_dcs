package com.weaver.dcs.dao;

import com.weaver.dcs.bean.ImageFileBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 附件数据操作类
 */
@Mapper
public interface ImageFileMapper {

    @Select("select " +
            "t1.IMAGEFILEID," +
            "t1.IMAGEFILENAME," +
            "t1.FILEREALPATH," +
            "t1.FILESIZE," +
            "t1.ISZIP," +
            "t1.ISAESENCRYPT," +
            "t1.AESCODE," +
            "t1.STORAGESTATUS," +
            "t1.TOKENKEY," +
            "t2.HTMLFILEID," +
            "t2.MUSTRECONVERTED as CONVERTSTRATUS " +
            "from imagefile t1 " +
            "left JOIN  docpreviewhtml t2 " +
            "on t1.IMAGEFILEID = t2.IMAGEFILEID " +
            "WHERE t1.IMAGEFILEID  = #{imageFileId}")
    ImageFileBean findImageFile(@Param("imageFileId") int imageFileId);
}
