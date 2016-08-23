package com.realidtek.rfid;

import java.io.IOException;
import com.realidtek.rfid.CommunicateShell;

/**
 * 电源.<br>
 * <br>
 * CreateDate: 2013-9-27<br>
 * Copyright: Copyright(c) 2013-9-27<br>
 * <br>
 * 
 * @since v1.0.0
 * @Description 2013-9-27::::创建此类</br>
 */
public class Power {
	/**
	 * 电源开启.<br>
	 * <br>
	 * 
	 * @Description 2013-9-27::::创建此方法</br>
	 */
	public static void on() {
		try {
			System.out.println("电源开");
			//CommunicateShell.postShellComm("echo on >/proc/usb_dc_en");
			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/usb_dc_en");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 电源关闭.<br>
	 * <br>
	 * 
	 * @Description 2013-9-27::::创建此方法</br>
	 */
	public static void off() {
		try {
			//CommunicateShell.postShellComm("echo off >/proc/usb_dc_en");
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/usb_dc_en");
			System.out.println("电源关");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
