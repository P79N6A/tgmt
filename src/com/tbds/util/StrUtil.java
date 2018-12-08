/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.util;

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

	public static void main(String[] args) {

		String ctrlKey = "/auth/permission";

		String head = ctrlKey.substring(0, ctrlKey.substring(1).indexOf("/") + 1);

		System.out.println("***" + head);

	}

}
