package com.realidtek.rfid;

public interface DFRfid {

	/**
	 * ���豸.<br>
	 * <br>
	 * 
	 * @return �Ƿ���
	 * @Description 2013-9-5::::�����˷���<br>
	 */
	public boolean openDevice();

	/**
	 * �ر��豸.<br>
	 * <br>
	 * 
	 * @return �Ƿ�ر�
	 * @Description 2013-9-5::::�����˷���<br>
	 */
	public boolean closeDevice();

	/**
	 * ѭ��ɨ������ǩ.<br>
	 * <br>
	 * 
	 * @param scanCycleDataReceiver
	 *            ѭ���������ݽ��սӿ�
	 * @return
	 * @Description 2013-9-26::::�����˷���</br>
	 */
	public void scanCycle(ScanCycleDataReceiver scanCycleDataReceiver);

	/**
	 * ֹͣѭ��ɨ��.<br>
	 * <br>
	 * 
	 * @return �Ƿ�ɹ�
	 * @Description 2013-9-26::::�����˷���</br>
	 */
	public boolean scanCycleStop();
}
