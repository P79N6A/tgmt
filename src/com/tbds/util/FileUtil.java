/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import org.w3c.dom.Document;

import com.jfinal.kit.PropKit;

/**
 *
 * @author totan
 */
public class FileUtil {
	/**
	 * 获取文件上传默认路径
	 */
	public static String getBaseUploadFilePath() {
		PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
		return PropKit.get(com.tbds.util.Constants.BASE_UPLOAD_FILE_PATH);
	}
	/**
	 * 根据文件绝对路径来获取文件类型
	 * @param filePath
	 * @return
	 */
	public static String getMimeTypeByFilePath(String filePath) {
		if(StrUtil.isBlank(filePath)) {
			return null;
		}
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		return mimeTypesMap.getContentType(filePath);
	}
	
	
	/**
	 * 根据文件路径或文件名来获取文件扩展名
	 * @param fileName / filePath
	 * @return
	 */
	public static String getFileExtension(String fileName){
		if(StrUtil.isBlank(fileName)) {
			return "";
		}
		
	    int lastIndexOfFileDot = fileName.lastIndexOf(".");
	    if(lastIndexOfFileDot < 0){
	        return "";//没有拓展名
	    }
	    
	    String extension = fileName.substring(lastIndexOfFileDot + 1);
	    return extension;
	}
	
	/**
	 * 根据文件路径或文件扩展名判断文件是否是XML文件
	 * @param filePath
	 * @return
	 */
	public static boolean isXmlByFileExtension(String filePath) {
		String fileExtension = getFileExtension(filePath);
		if(!"xml".equalsIgnoreCase(fileExtension)) {
			return false;
		} 
		return true;
	}
	
    /**
     * 读文件最后一行操作
     */
    public static String readFileLastLine(String fileName) {
        File fileHanlder = new File(fileName);
        BufferedReader buffReader = null;
        String lastLine = null;
        
        try {
        	
        	if(!fileHanlder.exists()) {//文件不存在
        		return lastLine;
        	}
        	
            buffReader = new BufferedReader(new FileReader(fileHanlder));
            int lineNum = 1;
            String tempLine = null;
            /**
             * 每次读一行，直到读入null则文件结束
             * 读入每行记录在临时变量
             */
            while ((tempLine = buffReader.readLine()) != null) {
                lastLine = tempLine;
                lineNum++;
            }
            buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (IOException exe) {
                }
            }
        }
        return lastLine;
    }
    
    /**
     *  加载文件内容到字符串中
     * @param filePath
     * @return
     */
    public static String readFile2String(String filePath) {
    	
    	File fileHanlder = new File(filePath);
        BufferedReader buffReader = null;
        String result = null;
        
        try {
        	
        	if(!fileHanlder.exists()) {//文件不存在
        		return result;
        	}
        	
        	StringBuilder resultBuilder = new StringBuilder();
        	
            buffReader = new BufferedReader(new FileReader(fileHanlder));
            int lineNum = 1;
            String tempLine = null;
            
            while ((tempLine = buffReader.readLine()) != null) {
            	resultBuilder.append(tempLine);
                lineNum++;
            }
            
            result = resultBuilder.toString();
            
            buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (IOException exe) {
                }
            }
        }
        return result;
    	
    }
    
    public static void main(String[] args) {
//        String filePath = "C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log" + "/" + "ATP_0223A" + ".state";
//        String stateLine = FileUtil.readFileLastLine(filePath);
//        System.out.println(stateLine);
//        
//        if(StrUtil.notBlank(stateLine)) {
//            String[] detail = stateLine.split("=");
//            System.out.println(detail[0]);
//            System.out.println(detail[1]);
//            System.out.println(detail[2]);
//        }
        
    	
    	String filePath = "C:\\Users\\temp\\job_fs_collect_mpsdata.job.xml";
    	System.out.println(readFile2String(filePath));
        
    }
    
}
