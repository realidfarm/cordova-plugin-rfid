package com.realidtek.rfid.utils;

import android.text.*;

public class Text {
	public static int byteToInt2(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < b.length; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}

	/**
	 * 清除字串中所有空格.<br>
	 * <br>
	 * 
	 * @param data
	 *            待处理字串
	 * @return
	 * @Description 2013-9-5::::创建此方法</br>
	 */
	public static String clearSpace(String data) {
		String re = "";
		String[] tmp = data.split(" ");
		for (String str : tmp) {
			re += str;
		}

		return re;
	}

	/**
	 * 将指定字符串以每两个字符分割转换为16进制形式.<br>
	 * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}<br>
	 * 
	 * @param src
	 * @return 16进制形式的字节数组
	 * @Description 2013-9-5::::创建此方法</br>
	 */
	public static byte[] hexString2Bytes(String src) {
		byte[] tmp = src.getBytes();
		int len = tmp.length;
		if (len % 2 != 2) {
			len++;
		}
		byte[] ret = new byte[len / 2];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 将两个ASCII字符合成一个字节.<br>
	 * 如："EF"--> 0xEF<br>
	 * 
	 * @param 低位字节
	 * @param 高位字节
	 * @return 合并后的字节
	 * @Description 2013-9-5::::创建此方法</br>
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 将字节数组转为字符串.<br>
	 * <br>
	 * 
	 * @param bArray
	 *            待转换的字节数组
	 * @return 转换后的字符串
	 * @Description 2013-9-5::::创建此方法</br>
	 */
	public static String byteToHexString(byte[] bArray) {
		if (bArray == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length + bArray.length / 2);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 字符串转字节数组.<br>
	 * <br>
	 * 
	 * @param s
	 * @return
	 * @Description 2013-9-26::::创建此方法</br>
	 */
	public static byte[] hexStringToByteArray(String s) {
		byte[] buf = new byte[s.length() / 2];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) (chr2hex(s.substring(i * 2, i * 2 + 1)) * 0x10 + chr2hex(s.substring(i * 2 + 1, i * 2 + 2)));
		}
		return buf;
	}

	/**
	 * 单个字符转字节数.<br>
	 * <br>
	 * 
	 * @param chr
	 * @return
	 * @Description 2013-9-26::::创建此方法</br>
	 */
	public static byte chr2hex(String chr) {
		if (chr.equals("0")) {
			return 0x00;
		} else if (chr.equals("1")) {
			return 0x01;
		} else if (chr.equals("2")) {
			return 0x02;
		} else if (chr.equals("3")) {
			return 0x03;
		} else if (chr.equals("4")) {
			return 0x04;
		} else if (chr.equals("5")) {
			return 0x05;
		} else if (chr.equals("6")) {
			return 0x06;
		} else if (chr.equals("7")) {
			return 0x07;
		} else if (chr.equals("8")) {
			return 0x08;
		} else if (chr.equals("9")) {
			return 0x09;
		} else if (chr.equalsIgnoreCase("A")) {
			return 0x0a;
		} else if (chr.equalsIgnoreCase("B")) {
			return 0x0b;
		} else if (chr.equalsIgnoreCase("C")) {
			return 0x0c;
		} else if (chr.equalsIgnoreCase("D")) {
			return 0x0d;
		} else if (chr.equalsIgnoreCase("E")) {
			return 0x0e;
		} else if (chr.equalsIgnoreCase("F")) {
			return 0x0f;
		}
		return 0x00;
	}

	/**
	 * 去掉字符串结尾的0.<br>
	 * <br>
	 * 
	 * @param str
	 * @return
	 * @Description 2013-9-27::::创建此方法</br>
	 */
	public synchronized static String removeTail0(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		return str.replaceAll("(0){1,}$", "");
	}

	/**
	 * 去掉字符串开头的0.<br>
	 * <br>
	 * 
	 * @param str
	 * @return
	 * @Description 2013-9-27::::创建此方法</br>
	 */
	public synchronized static String removeHead0(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		return str.replaceFirst("^0+", "");
	}

	public synchronized static String removeHeadTail0(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		return str.replaceAll("(0){1,}$", "").replaceFirst("^0+", "");
	}

	public static String padLeft(String str, int totalWidth, String paddingString) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < totalWidth) {
			sb = new StringBuffer();
			sb.append(paddingString).append(str);
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	public static String PadRight(String str, int totalWidth, String paddingString) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < totalWidth) {
			sb = new StringBuffer();
			sb.append(str).append(paddingString);// 右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	public static byte[] intToByte2(int i) {
		byte[] targets = new byte[2];
		targets[1] = (byte) (i & 0xFF);
		targets[0] = (byte) (i >> 8 & 0xFF);
		return targets;
	}

	// ====================================================================
	/**
	 * int整数转换为4字节的byte数组
	 * 
	 * @param i
	 *            整数
	 * @return byte数组
	 */
	public static byte[] intToByte4(int i) {
		byte[] targets = new byte[4];
		targets[3] = (byte) (i & 0xFF);
		targets[2] = (byte) (i >> 8 & 0xFF);
		targets[1] = (byte) (i >> 16 & 0xFF);
		targets[0] = (byte) (i >> 24 & 0xFF);
		return targets;
	}

	/**
	 * long整数转换为8字节的byte数组
	 * 
	 * @param lo
	 *            long整数
	 * @return byte数组
	 */
	public static byte[] longToByte8(long lo) {
		byte[] targets = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((lo >>> offset) & 0xFF);
		}
		return targets;
	}

	/**
	 * short整数转换为2字节的byte数组
	 * 
	 * @param s
	 *            short整数
	 * @return byte数组
	 */
	public static byte[] unsignedShortToByte2(int s) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (s >> 8 & 0xFF);
		targets[1] = (byte) (s & 0xFF);
		return targets;
	}

	/**
	 * byte数组转换为无符号short整数
	 * 
	 * @param bytes
	 *            byte数组
	 * @return short整数
	 */
	public static int byte2ToUnsignedShort(byte[] bytes) {
		return byte2ToUnsignedShort(bytes, 0);
	}

	/**
	 * byte数组转换为无符号short整数
	 * 
	 * @param bytes
	 *            byte数组
	 * @param off
	 *            开始位置
	 * @return short整数
	 */
	public static int byte2ToUnsignedShort(byte[] bytes, int off) {
		int high = bytes[off];
		int low = bytes[off + 1];
		return (high << 8 & 0xFF00) | (low & 0xFF);
	}

	/**
	 * byte数组转换为int整数
	 * 
	 * @param bytes
	 *            byte数组
	 * @param off
	 *            开始位置
	 * @return int整数
	 */
	public static int byte4ToInt(byte[] bytes, int off) {
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		int b2 = bytes[off + 2] & 0xFF;
		int b3 = bytes[off + 3] & 0xFF;
		return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
	}

	public static short byte2ToShort(byte[] bytes) {
		int b0 = bytes[0] & 0xFF;
		int b1 = bytes[1] & 0xFF;
		return (short) ((b0 << 8) | b1);
	}
}
