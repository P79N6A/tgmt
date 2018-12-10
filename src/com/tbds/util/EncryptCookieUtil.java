package com.tbds.util;

import com.jfinal.core.Controller;
import com.jfinal.kit.Base64Kit;
import com.jfinal.kit.HashKit;
import java.math.BigInteger;

public class EncryptCookieUtil {
	private final static String COOKIE_SEPARATOR = "#";
	private static String COOKIE_ENCRYPT_KEY = "$@))()^@^_!(*^)$!*";
	
	/**
	 * 设置cookie，cookie有效期默认时长为一周
	 * @param ctr
	 * @param key
	 * @param value
	 */
	public static void put(Controller ctr, String key, String value) {
        put(ctr, key, value, 60 * 60 * 24 * 7);
    }
	
	/**
	 * 设置cookie，cookie有效期默认时长为一周
	 * @param ctr
	 * @param key
	 * @param value
	 */
    public static void put(Controller ctr, String key, Object value) {
        put(ctr, key, value.toString());
    }

    /**
	 * 设置cookie及cookie有效期时长
	 * @param ctr
	 * @param key
	 * @param value
	 */
    public static void put(Controller ctr, String key, String value, int maxAgeInSeconds) {
        String cookie = buildCookieValue(value, maxAgeInSeconds);
        ctr.setCookie(key, cookie, maxAgeInSeconds);
    }
    
    /**
	 * 根据domain来设置cookie及cookie有效期时长
	 * @param ctr
	 * @param key
	 * @param value
	 */
    public static void put(Controller ctr, String key, String value, String domain) {
        put(ctr, key, value, 60 * 60 * 24 * 7, domain);
    }

    public static void put(Controller ctr, String key, String value, int maxAgeInSeconds, String domain) {
        String cookie = buildCookieValue(value, maxAgeInSeconds);
        ctr.setCookie(key, cookie, maxAgeInSeconds, null, domain, false);
    }

    /**
	 * 移除cookie
	 * @param ctr
	 * @param key
	 * @param value
	 */
    public static void remove(Controller ctr, String key) {
        ctr.removeCookie(key);
    }

    public static void remove(Controller ctr, String key, String path, String domain) {
        ctr.removeCookie(key, path, domain);
    }
    
    /**
	 * 移除cookie
	 * @param ctr
	 * @param key
	 * @param value
	 */
    public static String get(Controller ctr, String key) {

        String encrypt_key = COOKIE_ENCRYPT_KEY;
        String cookieValue = ctr.getCookie(key);

        if (cookieValue == null) {
            return null;
        }

        String value = new String(Base64Kit.decode(cookieValue));
        return getFromCookieInfo(encrypt_key, value);
    }
    
    
    public static String buildCookieValue(String value, int maxAgeInSeconds) {
        String encrypt_key = COOKIE_ENCRYPT_KEY;
        long saveTime = System.currentTimeMillis();
        String encrypt_value = encrypt(encrypt_key, saveTime, maxAgeInSeconds + "", value);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(encrypt_value);
        stringBuilder.append(COOKIE_SEPARATOR);
        stringBuilder.append(saveTime);
        stringBuilder.append(COOKIE_SEPARATOR);
        stringBuilder.append(maxAgeInSeconds);
        stringBuilder.append(COOKIE_SEPARATOR);
        stringBuilder.append(Base64Kit.encode(value));

        return Base64Kit.encode(stringBuilder.toString());
    }

    private static String encrypt(String encrypt_key, long saveTime, String maxAgeInSeconds, String value) {
        return HashKit.md5(encrypt_key + saveTime + maxAgeInSeconds + value);
    }


    public static String getFromCookieInfo(String encrypt_key, String cookieValue) {
        if (StrUtil.isBlank(cookieValue)) {
            return null;
        }

        String cookieStrings[] = cookieValue.split(COOKIE_SEPARATOR);
        if (cookieStrings == null || cookieStrings.length != 4) {
            return null;
        }

        String encrypt_value = cookieStrings[0];
        String saveTime = cookieStrings[1];
        String maxAgeInSeconds = cookieStrings[2];
        String value = Base64Kit.decodeToStr(cookieStrings[3]);

        String encrypt = encrypt(encrypt_key, Long.valueOf(saveTime), maxAgeInSeconds, value);

        //判断cookie值是否与加密值是否一致，防止篡改的问题
        if (!encrypt.equals(encrypt_value)) {
            return null;
        }

        long stime = Long.parseLong(saveTime);
        long maxtime = Long.parseLong(maxAgeInSeconds) * 1000;

        // 查看是否过时
        if ((stime + maxtime) - System.currentTimeMillis() > 0) {
            return value;
        }
        /**
         * 已经超时了
         */
        else {
            return null;
        }

    }

    public static Long getLong(Controller ctr, String key) {
        String value = get(ctr, key);
        return null == value ? null : Long.parseLong(value);
    }

    public static long getLong(Controller ctr, String key, long defalut) {
        String value = get(ctr, key);
        return null == value ? defalut : Long.parseLong(value);
    }

    public static Integer getInt(Controller ctr, String key) {
        String value = get(ctr, key);
        return null == value ? null : Integer.parseInt(value);
    }

    public static int getInt(Controller ctr, String key, int defalut) {
        String value = get(ctr, key);
        return null == value ? defalut : Integer.parseInt(value);
    }

    public static BigInteger getBigInteger(Controller ctr, String key) {
        String value = get(ctr, key);
        return null == value ? null : new BigInteger(value);
    }

    public static BigInteger getBigInteger(Controller ctr, String key, BigInteger defalut) {
        String value = get(ctr, key);
        return null == value ? defalut : new BigInteger(value);
    }
	
}
