package com.realidtek.rfid.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.text.TextUtils;

public class Datetime {
	/**
	 * ��ָ����ʽ��ʾ��ǰ����ʱ��.<br>
	 * <br>
	 * 
	 * @param format
	 * @return
	 * @Description 2013-6-15::��˳::�����˷���</br>
	 */
	public static String nowDateTime(String format) {
		if (TextUtils.isEmpty(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(format);
		String now_date = simpleDateTimeFormat.format(c.getTime());
		return now_date;
	}

	/**
	 * ��ָ����ʽ��ʾ��ǰ����ʱ��.<br>
	 * <br>
	 * 
	 * @return
	 * @Description 2013-6-15::��˳::�����˷���</br>
	 */
	public static String nowDateTime() {
		return nowDateTime("");
	}

	public static int compare(String data1, String data2) {
		int re = 0;

		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(data1));
			c2.setTime(df.parse(data2));
		} catch (java.text.ParseException e) {
			System.err.println("��ʽ����ȷ");
		}
		re = c1.compareTo(c2);

		return re;
	}
}
