package com.realidtek.rfid;

/**
 * 数据接收.<br>
 * <br>
 * CreateDate: 2013-9-26<br>
 * Copyright: Copyright(c) 2013-9-26<br>
 * <br>
 * 
 * @since v1.0.0
 * @Description 2013-9-26::::创建此类</br>
 */
public interface ScanCycleDataReceiver {
	public void onScanCycleDataReceived(String cardID);

	public void onNoTagReceived(boolean isNoTag);

	public void onExceptionReceived(Exception ex);

}
