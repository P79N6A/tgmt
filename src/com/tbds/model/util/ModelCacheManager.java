/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.model.util;

import com.jfinal.config.JFinalConfig;
import com.tbds.util.Constants;
import com.tbds.exception.TbdsError;
import com.tbds.exception.TbdsException;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *
 * @author totan
 */
public class ModelCacheManager {
    private static final Logger log = Logger.getLogger(ModelCacheManager.class);
    private static ModelCacheManager cache = null;
    
    private ModelCacheManager(boolean needRefresh) {
        if (cache != null) {
            log.error("No need reload CacheManager.");
            throw new TbdsException(TbdsError.ERRCODE_CACHE,
                    TbdsError.ERRDESC_CACHE + " No need reload CacheManager.");
        }
        try {
            synchronized (this) {
                log.info("[" + new Date() + "][" + ModelCacheManager.class.getName() + "]:CacheManager init success！");
                
                if (!needRefresh) {
                    refreshDB();
                }
                
            }
        } catch (Exception e) {
            log.error("CacheManager init fail.");
            throw new TbdsException(TbdsError.ERRCODE_CACHE,
                    TbdsError.ERRDESC_CACHE + " CacheManager init fail.");
        }
    }

    public static ModelCacheManager getInstance() {
        if (cache == null) {
            log.info("[" + new Date() + "][" + ModelCacheManager.class.getName() + "]:CacheManager is null！");
            throw new TbdsException(TbdsError.ERRCODE_CACHE,
                    TbdsError.ERRDESC_CACHE + " get CacheManager instance fail, not load already.");
        } else {
            return cache;
        }
    }

    public static synchronized void createInstance(boolean fresh) {
        if (cache == null) {
            cache = new ModelCacheManager(fresh);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void refreshDB() {
//        Hashtable httmp = getDictionay();
//        if (httmp != null) {
//            htParameters = httmp;
//        }
    }

    /**
     * 获取数据字典表信息，返回 key = type + code value = content
     *
     * @return Hashtable<String, String>
     */
//    private Hashtable<String, String> getDictionay() {
//
//        Hashtable<String, String> ht = new Hashtable<String, String>();
//        
//        Dictionary dicty = new Dictionary();
//        
//        List<Dictionary> dicts = dicty.find("select * from " 
//        		+ Dictionary.TABLE_NAME + " order by " 
//        		+ Dictionary.PRIMARY_KEY + " asc");
//        if(dicts!=null){
//        	for(Dictionary dict:dicts){
//        		ht.put(dict.getStr("type")+"-"+dict.getStr("code"), dict.getStr("content"));
//        	}
//        }
//        
//        return ht;
//    }
    /**
     * 根据参数列名获取参数值
     *
     * @param key = type + "-" + code
     * @return value = content
     */
//    public String getDictionary(String key) {
//        String value = "";
//        if (needRefresh) {
//            synchronized (this) {
//                htParameters = this.getDictionay();
//            }
//        }
//        if (htParameters.containsKey(key)) {
//            value = htParameters.get(key);
//        }
//        return value;
//    }

}
