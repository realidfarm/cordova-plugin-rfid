package com.realidtek.rfid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.text.TextUtils;

public class RFIDLog {
	public static boolean WRITE_LOG = true;
	public static long MAX_SIZE = 2 * FileHelper.MB;

	public static void sendLog(byte[] data) {
		if (WRITE_LOG && data != null) {
			String msg = "send:";
			for (byte b : data) {
				msg += String.format("%x ", b);
			}
			msg += "\n";
			writeLog(msg);
		}
	}

	public static void recieveLog(byte[] data) {
		if (WRITE_LOG && data != null) {
			String msg = "recieve data:";
			for (byte b : data) {
				msg += String.format("%x ", b);
			}
			msg += "\n\n";
			writeLog(msg);
		}
	}

	public static void log(String info) {
		writeLog(info);
	}

	public static void exceptionLog(String tag, Exception e) {
		String msg = "";
		StackTraceElement[] messages = e.getStackTrace();
		int length = messages.length;
		for (int i = 0; i < length; i++) {
			msg += "ClassName:" + messages[i].getClassName() + "\n";
			msg += "getFileName:" + messages[i].getFileName() + "\n";
			msg += "getLineNumber:" + messages[i].getLineNumber() + "\n";
			msg += "getMethodName:" + messages[i].getMethodName() + "\n";
			msg += "toString:" + messages[i].toString() + "\n";
		}
		log(tag + ":\n" + msg);
	}

	private static void writeLog(String msg) {
		if (WRITE_LOG && !TextUtils.isEmpty(msg)) {
			String info = "";
			info += "[" + Datetime.nowDateTime("yyyy-MM-dd HH:mm:ss") + "]"
					+ msg;
			try {
				String path = "/sdcard/rfid_log";
				File p = new File(path);
				if (!p.exists()) {
					p.mkdirs();
				}

				String fileName = path + "/Log.txt";
				File file = new File(fileName);
				if (!file.exists()) {
					file.createNewFile();
				}

				// 检查文件大小，文件过大则备份，然后清空日志文件
				if (FileHelper.getFileSizes(file) >= MAX_SIZE) {
					FileHelper.move(fileName, path + "/Log_bak.txt");
				}

				if (!file.exists()) {
					file.createNewFile();
				}
				OutputStream output = new FileOutputStream(file, true);
				output.write(info.getBytes());
				output.flush();
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
