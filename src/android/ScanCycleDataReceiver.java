package com.realidtek.rfid;

/**
 * ���ݽ���.<br>
 * <br>
 * CreateDate: 2013-9-26<br>
 * Copyright: Copyright(c) 2013-9-26<br>
 * <br>
 * 
 * @since v1.0.0
 * @Description 2013-9-26::::��������</br>
 */
public interface ScanCycleDataReceiver {
	public void onScanCycleDataReceived(String cardID);

	public void onNoTagReceived(boolean isNoTag);

	public void onExceptionReceived(Exception ex);

}
