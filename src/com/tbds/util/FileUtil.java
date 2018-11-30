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

/**
 *
 * @author totan
 */
public class FileUtil {
    /**
     * 读文件行操作
     */
    public static String readFileLastLine(String fileName) {
        File fileHanlder = new File(fileName);
        BufferedReader buffReader = null;
        String lastLine = null;
        
        try {
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
    
    public static void main(String[] args) {
        String filePath = "C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log" + "/" + "ATP_0223A" + ".state";
        String stateLine = FileUtil.readFileLastLine(filePath);
        System.out.println(stateLine);
        
        if(StrUtil.notBlank(stateLine)) {
            String[] detail = stateLine.split("=");
            System.out.println(detail[0]);
            System.out.println(detail[1]);
            System.out.println(detail[2]);
        }
        
        
    }
    
}
