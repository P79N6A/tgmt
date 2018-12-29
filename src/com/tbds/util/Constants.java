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
     * MPS设备日志路径
     */
    public static final String MPS_STATUS_LOG_PATH = "mpsStatusPath";

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
    public static final String LOGINER_UNI_ID = "_tbds_logon_user_id_";
    public static final String LOGINER_UNI_NAME = "_tbds_logon_user_name_";
    public static final String LOGINER_USER = "_tbds_logon_user_obj_";
    
    
    /**
     * Login user cookie attribute
     */
    public static final String COOKIE_UUUID = "_tbdsuid";
    
    /**
     * Init Login User Menu Key
     */
    public static final String LOGIN_USER_MENU_ATTR = "_my_permission_menu_";
    
    /**
     * Permission Type
     */
    public static final String PERMISSION_MENU_TYPE = "permissionMenuType";
    public static final String PERMISSION_OPER_TYPE = "permissionOperType";
    
    /**
     * 标准日期格式：yyyy-MM-dd
     */
    public static final String STANDARD_DATE = "yyyy-MM-dd";
    /**
     * 标准时间格式：HH:mm:ss
     */
    public static final String STANDARD_TIME = "HH:mm:ss";
    /**
     * 标准日期时间格式：yyyy-MM-dd hh:mm:ss
     */
    public static final String STANDARD_DATETIME = STANDARD_DATE + " " + STANDARD_TIME;
    
    /**
     * 隐藏文件mps list data
     */
    public static final String MPS_SERVER_LIST_FILE_NAME = ".mpslistdata";
    
    /**
     * 换行符
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    /**
     * scheduler job template path key field
     */
    public static final String SCHEDULER_JOB_TEMPLATE_PATH = "schedulerJobTemplatePath";
    
    /**
     * scheduler job file path
     */
    public static final String SCHEDULER_JOB_FILE_PATH = "schedulerJobFilePath";
    
    /**
	 * #文件上传大小限制 10 * 1024 * 1024 = 10M
	 */
	public static final int TEMPLATE_MAX_POST_SIZE = 10485760;
	
	public static final String BASE_UPLOAD_FILE_PATH = "baseUploadFilePath";
	
	public static final String BASE_DOWNLOAD_FILE_PATH = "baseDownloadFilePath";
    
    
}
