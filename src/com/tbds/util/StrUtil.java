/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.StrKit;

/**
 *
 * @author totan
 */
public class StrUtil extends StrKit {

	/** 正则表达式 **/
	private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	// \\b 表示 限定单词边界 比如 select 不通过 1select则是可以的
	private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	public static boolean isValid(String str) {
		if (sqlPattern.matcher(str).find()) {
			System.err.println("未能通过过滤器：str=" + str);
			return false;
		}
		return true;
	}

	/**
	 * 字符串是否匹配某个正则
	 *
	 * @param string
	 * @param regex
	 * @return
	 */
	public static boolean match(String string, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	/**
	 * 这个字符串是否是全是数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null)
			return false;
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	/**
	 * 是否是邮件的字符串
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches("\\w+@(\\w+.)+[a-z]{2,3}", email);
	}

	/**
	 * 是否是中国地区手机号码
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isMobileNumber(String phoneNumber) {
		return Pattern.matches("^(1[3,4,5,7,8,9])\\d{9}$", phoneNumber);
	}

	/**
	 * 生成一个新的UUID
	 *
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 去除特殊字符
	 *
	 * @param string
	 * @return
	 */
	public static String clearSpecialCharacter(String string) {
		if (isBlank(string)) {
			return string;
		}

		/**
		 * P：标点字符； L：字母； M：标记符号（一般不会单独出现）； Z：分隔符（比如空格、换行等）； S：符号（比如数学符号、货币符号等）；
		 * N：数字（比如阿拉伯数字、罗马数字等）； C：其他字符
		 */
//        return string.replaceAll("[\\pP\\pZ\\pM\\pC]", "");
		return string.replaceAll("[\\\\\'\"\\/\f\n\r\t]", "");
	}

	/**
	 * 把字符串拆分成一个set
	 *
	 * @param src
	 * @param regex
	 * @return
	 */
	public static Set<String> splitToSet(String src, String regex) {
		if (src == null) {
			return null;
		}

		String[] strings = src.split(regex);
		Set<String> set = new HashSet<>();
		for (String table : strings) {
			if (StrUtil.isBlank(table)) {
				continue;
			}
			set.add(table.trim());
		}
		return set;
	}

	public static void main(String[] args) {

		String ctrlKey = "/auth/permission";

		String head = ctrlKey.substring(0, ctrlKey.substring(1).indexOf("/") + 1);

		System.out.println("***" + head);

	}

}
