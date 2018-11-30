/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.model.util;

import com.tbds.model.eo.*;
import com.tbds.util.StrUtil;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 *
 * @author totan
 */

public class DbManager {

    public DbManager() {
        super();
    }
    
    private static C3p0Plugin c3p0Plugin = null;
    private static ActiveRecordPlugin arp = null;
    
    /**
     * 关闭数据库相关插件
     */
    public static void stop() {
        if(c3p0Plugin != null) {
            c3p0Plugin.stop();
        }
        if(arp != null) {
            arp.stop();
        }
    }
    
    
    /**
     * 初始化配置C3p0数据库连接池插件
     */
    public static C3p0Plugin initC3p0Plugin(String url, String uid, String pwd) {
        C3p0Plugin c3p0Plugin = null;
 
        if (StrUtil.notBlank(url) && StrUtil.notBlank(uid) && StrUtil.notBlank(pwd)) {
            c3p0Plugin = new C3p0Plugin(url.trim(), uid.trim(), pwd.trim());

            c3p0Plugin.setInitialPoolSize(10);
            c3p0Plugin.setMaxPoolSize(50);
            c3p0Plugin.setMinPoolSize(10);
            c3p0Plugin.setMaxIdleTime(20); //defaults:20;
            //c3p0Plugin.setMaxStatements(0);
            //c3p0Plugin.setIdleConnectionTestPeriod(1800);//defauts: 0;
        }
        return c3p0Plugin;
    }
    
    /**
     * 初始化配置ActiveRecord插件
     * @param c3p0Plugin - C3p0Plugin
     */
    public static ActiveRecordPlugin initActiveRecordPlugin(C3p0Plugin c3p0Plugin) {
        ActiveRecordPlugin arp = null;
        if (null != c3p0Plugin) {
            arp = new ActiveRecordPlugin(c3p0Plugin);
            //忽略大小写
            arp.setContainerFactory(new CaseInsensitiveContainerFactory());
            arp.setShowSql(true);
        }
        return arp;
    }
    
    /**
     * 初始化Model与数据库表的关系
     * @param arp
     */
    public static void initModelMapping(ActiveRecordPlugin arp) {
        if(null != arp) {
            arp.addMapping(Mps.TABLE_NAME, Mps.PRIMARY_KEY, Mps.class);
            
            //忽略大小写
            arp.setContainerFactory(new CaseInsensitiveContainerFactory());
        }
    }
    
    /**
     * 初始数据源、Active Record、Model与表的关系
     * @param url - JDBC Url
     * @param uid - JDBC User id
     * @param pwd - JDBC User password
     */
    public static void start(String url, String uid, String pwd) {
        //配置数据源池插件
        c3p0Plugin = initC3p0Plugin(url, uid, pwd);
        
        if(null != c3p0Plugin) {
            c3p0Plugin.start();
            //配置ActiveRecord插件
            arp = initActiveRecordPlugin(c3p0Plugin);
            if(null != arp) {
                initModelMapping(arp);
                arp.start();
            } else {
                System.err.println(">>>Error on Init ActiveRecord Plugin, please check it.");
            }
        } else {
            System.err.println(">>>Error on Init C3p0Plugin, please check it.");
        }
    }
}
