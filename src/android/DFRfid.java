package com.realidtek.rfid;

public interface DFRfid {

	/**
	 * 打开设备.<br>
	 * <br>
	 * 
	 * @return 是否开启
	 * @Description 2013-9-5::::创建此方法<br>
	 */
	public boolean openDevice();

	/**
	 * 关闭设备.<br>
	 * <br>
	 * 
	 * @return 是否关闭
	 * @Description 2013-9-5::::创建此方法<br>
	 */
	public boolean closeDevice();

	/**
	 * 循环扫卡读标签.<br>
	 * <br>
	 * 
	 * @param scanCycleDataReceiver
	 *            循环读卡数据接收接口
	 * @return
	 * @Description 2013-9-26::::创建此方法</br>
	 */
	public void scanCycle(ScanCycleDataReceiver scanCycleDataReceiver);

	/**
	 * 停止循环扫卡.<br>
	 * <br>
	 * 
	 * @return 是否成功
	 * @Description 2013-9-26::::创建此方法</br>
	 */
	public boolean scanCycleStop();
}
