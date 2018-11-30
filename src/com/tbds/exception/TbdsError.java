/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.exception;

/**
 *
 * @author totan
 */
public class TbdsError {
    public static final String NO_PERMITION = "无权限";
    public static final String NO_VALID_INFO = "无有效信息";

    public static final String ERRCODE_CACHE = "E0001";
    public static final String ERRDESC_CACHE = "[Cache异常]";

    public static final String ERRCODE_DATA = "E0002";
    public static final String ERRDESC_DATA = "[数据更新异常]";

    public static final String ERRCODE_PLUGIN_START_FAIL = "E0500";
    public static final String ERRDESC_PLUGIN_START_FAIL = "[组件启动失败]";


    public TbdsError() {
        // TODO Auto-generated constructor stub
    }
}
