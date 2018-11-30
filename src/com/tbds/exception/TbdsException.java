/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 * @author totan
 */
public class TbdsException extends RuntimeException {

    private static final long serialVersionUID = 2739956770813691861L;

    private String code = null;
    private String desc = null;
    private String trace = null;
    private Exception innerException = null;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTrace() {
        return trace;
    }

    public Exception getInnerException() {
        return innerException;
    }

    public TbdsException() {
        super();
    }

    /**
     * 构造函数</br>
     * 调用父类的构造函数<br>
     * 设置返回码和描述
     *
     * @param errCode
     * @param message
     */
    public TbdsException(String errCode, String errDesc) {
        super(errCode + errDesc);
        this.code = errCode;
        this.desc = errDesc;
        this.innerException = null;
        this.trace = this.toString();
    }

    /**
     * 构造函数</br>
     * 调用父类的构造函数<br>
     * 设置返回码和描述
     *
     * @param returnCode
     * @param message
     * @param ex
     */
    public TbdsException(String errCode, String errDesc, Exception ex) {
        super(errCode + errDesc, ex);
        this.code = errCode;
        this.desc = errDesc;
        this.innerException = ex;
        this.trace = this.toString();
    }

    /**
     * 打印程序运行堆栈信息
     */
    public void printStackTrace() {
        if (innerException != null) {
            innerException.printStackTrace();
        } else {
            super.printStackTrace();
        }
    }

    /**
     * 打印Trace信息
     *
     * @param e
     * @return
     */
    public static String getStackTrace(Exception e) {
        if (null == e) {
            return null;
        }
        ByteArrayOutputStream out = null;
        PrintStream print = null;
        try {
            out = new ByteArrayOutputStream();
            print = new PrintStream(out);
            e.printStackTrace(print);
            print.flush();
            out.flush();
            return out.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 以字符串形式返回
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[" + code + "] " + desc);
        Exception e = innerException;
        while (e != null && e instanceof TbdsException) {
            sb.append(" || \n");
            TbdsException ae = (TbdsException) e;
            sb.append("[" + ae.getCode() + "] " + ae.getDesc() + ae.getMessage());
            e = ae.innerException;
        }
        if (e != null && !(e instanceof TbdsException)) {
            sb.append(" || \n");
            sb.append(TbdsException.getStackTrace(e));
        }
        return sb.toString();
    }

}
