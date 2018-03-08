package com.weaver.dcs.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.ArrayList;

public class ZipUtil {

    public static boolean addFilesStoreComp(String sourcePath, String targetPath) {

        try {
            ZipFile zipFile = new ZipFile(sourcePath);
            ArrayList filesToAdd = new ArrayList();
            filesToAdd.add(new File(targetPath));
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        finally {
            File file = new File(sourcePath);
            file.delete();
        }
        return true;
    }

    public static String extractFile(String sourcePath,String fileName,String targetPath) {
        try {
            ZipFile zipFile = new ZipFile(sourcePath);
            FileHeader fileHeader = (FileHeader)zipFile.getFileHeaders().get(0);
            zipFile.extractFile(fileHeader,targetPath,null,fileName);
            return targetPath+File.separator+fileName;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        finally {
            File file = new File(sourcePath);
            file.delete();
        }
        return "";
    }
}
