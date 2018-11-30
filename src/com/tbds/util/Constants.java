/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.util;

/**
 *
 * @author totan
 */
public class Constants {

    /*=========================================================
    *====  0. common                                     =====
    *=========================================================*/
    /**
     * 配置文件
     */
    public static final String CONFIG_FILE = "config.properties";

    /**
     * 数据库URL
     */
    public static final String DB_URL = "dburl";

    /**
     * 数据库登陆ID
     */
    public static final String DB_UID = "dbuid";

    /**
     * 数据库登陆密码
     */
    public static final String DB_PWD = "dbpwd";

    /**
     * 开发模式：ON-true, OFF-false
     */
    public static final String DEV_MODE = "devMode";
    
    /**
     * 手动刷新数据库字典值: ON-true, OFF-false
     */
    public static final String MANNUAL_REFRESH_DB = "manualRefreshDB";

    /**
     * 终端mps在线状态
     */
    public static final int ONLINE = 1;
    /**
     * 终端mps离线状态
     */
    public static final int OFFLINE = 0;
    
    /**
     * UTF-8 coding
     */
    public static final String UTF8 = "utf-8";
    
    
    /**
     * Login user session attribute
     */
    public static final String LOGINER = "_tbds_logon_user_";
    
    /**
     * 标准日期格式：yyyy-MM-dd
     */
    public static final String STANDARD_DATE = "yyyy-MM-dd";
    /**
     * 标准时间格式：hh:mm:ss
     */
    public static final String STANDARD_TIME = "hh:mm:ss";
    /**
     * 标准日期时间格式：yyyy-MM-dd hh:mm:ss
     */
    public static final String STANDARD_DATETIME = STANDARD_DATE + " " + STANDARD_TIME;
}
