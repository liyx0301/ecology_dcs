package com.weaver.dcs.convert.pool;

import com.weaver.dcs.convert.toHtml.DocToHtml;
import com.weaver.dcs.convert.toHtml.DocxToHtml;

public class ConvertToHtml {
    public int convertMSToHtml(String sourcePath,String fileType){
        try
        {
            if(fileType.equals("doc"))
            {
                return DocToHtml.convert(sourcePath);
            }
            else if(fileType.equals("docx"))
            {
                return DocxToHtml.convert(sourcePath);
            }
            else if(fileType.equals("xls"))
            {
                return DocToHtml.convert(sourcePath);
            }
            else if(fileType.equals("xlsx"))
            {
                return DocToHtml.convert(sourcePath);
            }
            return -1;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return -1;
    }

    public boolean deleteFiles(String filePath)
    {
        return true;
    }
}
