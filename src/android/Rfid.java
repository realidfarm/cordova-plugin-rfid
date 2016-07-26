package com.realidtek.rfid;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realidtek.rfid.utils.RFIDLog;
import com.realidtek.rfid.utils.Text;
import android.text.TextUtils;

import com.langcoo.serialport.ReceiveDataListener;
import com.langcoo.serialport.SerialPort;

public class Rfid extends CordovaPlugin implements DFRfid {

    public boolean isDebug = true;
    private final String SERIAL_PORT_NAME = "/dev/ttyMSM2";
	//private final String SERIAL_PORT_NAME = "/dev/ttyHSL1";
    private final int BAUDRATE = 115200;

    private int WAIT_TIME = 800;

    private boolean isOpen;

    private ScanCycleDataReceiver scanCycleDataReceiver;
    private SerialPort serialPortHelper;

    public static DFRfid rfid;

    public static String cardids = ""; 

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Activity activity = this.cordova.getActivity();
        if (action.equals("openDevice")) {
            if(isOpen == true){
                callbackContext.success("设备已开启");
            }else{
                openDevice();
                callbackContext.success("设备已开启");
            }
            return true;
        }else if(action.equals("closeDevice")) {
            if(isOpen == true){
                closeDevice();
                if(isOpen == false){
                    callbackContext.success("设备已关闭");
                }
            }else{
                callbackContext.success("设备已关闭");
            }
            return true;
        }else if(action.equals("scanCycle")) {
            scanCycle(new ScanCycleDataReceiver() {
                    
                @Override
                public void onScanCycleDataReceived(String cardID) {
                    synchronized (this) {
                        // 处理获取数据
                        cardids = cardID;
                    }
                }

                @Override
                public void onExceptionReceived(Exception ex) {
                    // TODO 获取扫描异常

                }

                @Override
                public void onNoTagReceived(boolean isNoTag) {
                    // TODO 获取是否标签为离开状态
                }
            });
            callbackContext.success(cardids);
            if(cardids != ""){
                Sound.callAlarm(cordova.getActivity());
            }
            cardids = "";
            return true;
        }else if(action.equals("scanCycleStop")) {
            boolean scan = scanCycleStop();
            if(scan == true){
                callbackContext.success("循环已停止");
            }else{
                callbackContext.success("设备循环中");
            }
            
            return true;
        }
        return false;
    }

    public void setWaitTime(int waitTime) {
        WAIT_TIME = waitTime;
    }

    public void toWait(long waitTime) {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (Exception e) {
            e.printStackTrace();

            RFIDLog.exceptionLog("toWait", e);
        }
    }

    @Override
    public boolean openDevice() {
        isOpen = false;
        try {
            serialPortHelper = SerialPort.getInstance();
            serialPortHelper.openSerialPort(new File(SERIAL_PORT_NAME),
                    BAUDRATE);
            isOpen = true;
            System.out.println("串口开：" + isOpen);
        } catch (Exception e) {
            System.out.println("串口开：" + (e != null ? e.getMessage() : "e null"));
            isOpen = false;
        }

        return isOpen;
    }

    @Override
    public boolean closeDevice() {
        boolean re = _closeDevice();
        return re;
    }

    private boolean _closeDevice() {
        try {
            if (serialPortHelper != null) {
                serialPortHelper.closeSerialPort();
            }
            isOpen = false;
        } catch (Exception e) {
            e.printStackTrace();
            RFIDLog.exceptionLog("closeDevice", e);
        }
        return !isOpen;
    }

    @Override
    public void scanCycle(final ScanCycleDataReceiver scanCycleDataReceiver) {
        this.scanCycleDataReceiver = scanCycleDataReceiver;

        try {
            serialPortHelper.sendData(null, new ReceiveDataListener() {

                @Override
                public void receive(final byte[] data) {
                    synchronized (this) {
                        if (scanCycleDataReceiver != null && data != null) {
                            if (data != null) {
                                String dataStr = Text.byteToHexString(data);
                                if (!TextUtils.isEmpty(dataStr)) {
                                    dataStr = dataStr.toUpperCase();
                                    dataStr = dataStr.replaceAll("1B9A", "1A");
                                    dataStr = dataStr.replaceAll("1B97", "17");
                                    dataStr = dataStr.replaceAll("1B9B", "1B");
                                }
                                cl(dataStr);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            RFIDLog.exceptionLog("SerialException", e);
            System.out.println("SerialException:" + e.getMessage());
        }
    }

    private String HEAD = "1A92";

    private void cl(String _dataStr) {
        if (!TextUtils.isEmpty(_dataStr)) {
            int idx = _dataStr.indexOf(HEAD);
            if (idx != -1) {
                int idx1 = _dataStr.indexOf(HEAD, idx + 1);
                if (idx1 != -1) {
                    String dataStr = _dataStr.substring(idx, idx1);
                    String tail = _dataStr.substring(idx1);
                    print(dataStr);
                    cl(tail);
                } else {
                    String dataStr = _dataStr.substring(idx);
                    print(dataStr);
                }
            }
        }
    }

    private void print(String dataStr) {
        if (!TextUtils.isEmpty(dataStr)) {
            if (scanCycleDataReceiver != null) {
                byte[] data = Text.hexString2Bytes(dataStr);
                if (data != null && data.length > 3) {
                    int dLen = data.length;
                    byte oldCRC = data[dLen - 2];
                    byte[] crcCheckData = new byte[dLen - 3];
                    System.arraycopy(data, 1, crcCheckData, 0,
                            crcCheckData.length);
                    byte newCRC = computeCrc8(crcCheckData);
                    if (newCRC == oldCRC && dLen > 17
                            && data[10] == (byte) 0x80
                            && data[14] == (byte) 0x11
                            && data[15] == (byte) 0x10) {
                        byte[] pLenBytes = new byte[2];
                        pLenBytes[0] = data[17];
                        pLenBytes[1] = data[16];
                        int pLen = Text.byteToInt2(pLenBytes);
                        if (pLen > 0 && dLen > (17 + pLen)) {
                            byte[] cardIDBytes = new byte[pLen];
                            System.arraycopy(data, 18, cardIDBytes, 0,
                                    cardIDBytes.length);
                            String cardID = new String(cardIDBytes);
                            if (!cardID.startsWith("NOTAG")) {
                                scanCycleDataReceiver
                                        .onScanCycleDataReceived(cardID);
                                scanCycleDataReceiver.onNoTagReceived(false);
                            } else {
                                scanCycleDataReceiver.onNoTagReceived(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private char CRC_PRESET = 0xFF;
    private char[] CRC8TABLE = { 0x00, 0x1D, 0x3A, 0x27, 0x74, 0x69, 0x4E,
            0x53, 0xE8, 0xF5, 0xD2, 0xCF, 0x9C, 0x81, 0xA6, 0xBB, 0xCD, 0xD0,
            0xF7, 0xEA, 0xB9, 0xA4, 0x83, 0x9E, 0x25, 0x38, 0x1F, 0x02, 0x51,
            0x4C, 0x6B, 0x76, 0x87, 0x9A, 0xBD, 0xA0, 0xF3, 0xEE, 0xC9, 0xD4,
            0x6F, 0x72, 0x55, 0x48, 0x1B, 0x06, 0x21, 0x3C, 0x4A, 0x57, 0x70,
            0x6D, 0x3E, 0x23, 0x04, 0x19, 0xA2, 0xBF, 0x98, 0x85, 0xD6, 0xCB,
            0xEC, 0xF1, 0x13, 0x0E, 0x29, 0x34, 0x67, 0x7A, 0x5D, 0x40, 0xFB,
            0xE6, 0xC1, 0xDC, 0x8F, 0x92, 0xB5, 0xA8, 0xDE, 0xC3, 0xE4, 0xF9,
            0xAA, 0xB7, 0x90, 0x8D, 0x36, 0x2B, 0x0C, 0x11, 0x42, 0x5F, 0x78,
            0x65, 0x94, 0x89, 0xAE, 0xB3, 0xE0, 0xFD, 0xDA, 0xC7, 0x7C, 0x61,
            0x46, 0x5B, 0x08, 0x15, 0x32, 0x2F, 0x59, 0x44, 0x63, 0x7E, 0x2D,
            0x30, 0x17, 0x0A, 0xB1, 0xAC, 0x8B, 0x96, 0xC5, 0xD8, 0xFF, 0xE2,
            0x26, 0x3B, 0x1C, 0x01, 0x52, 0x4F, 0x68, 0x75, 0xCE, 0xD3, 0xF4,
            0xE9, 0xBA, 0xA7, 0x80, 0x9D, 0xEB, 0xF6, 0xD1, 0xCC, 0x9F, 0x82,
            0xA5, 0xB8, 0x03, 0x1E, 0x39, 0x24, 0x77, 0x6A, 0x4D, 0x50, 0xA1,
            0xBC, 0x9B, 0x86, 0xD5, 0xC8, 0xEF, 0xF2, 0x49, 0x54, 0x73, 0x6E,
            0x3D, 0x20, 0x07, 0x1A, 0x6C, 0x71, 0x56, 0x4B, 0x18, 0x05, 0x22,
            0x3F, 0x84, 0x99, 0xBE, 0xA3, 0xF0, 0xED, 0xCA, 0xD7, 0x35, 0x28,
            0x0F, 0x12, 0x41, 0x5C, 0x7B, 0x66, 0xDD, 0xC0, 0xE7, 0xFA, 0xA9,
            0xB4, 0x93, 0x8E, 0xF8, 0xE5, 0xC2, 0xDF, 0x8C, 0x91, 0xB6, 0xAB,
            0x10, 0x0D, 0x2A, 0x37, 0x64, 0x79, 0x5E, 0x43, 0xB2, 0xAF, 0x88,
            0x95, 0xC6, 0xDB, 0xFC, 0xE1, 0x5A, 0x47, 0x60, 0x7D, 0x2E, 0x33,
            0x14, 0x09, 0x7F, 0x62, 0x45, 0x58, 0x0B, 0x16, 0x31, 0x2C, 0x97,
            0x8A, 0xAD, 0xB0, 0xE3, 0xFE, 0xD9, 0xC4 };

    private byte computeCrc8(byte[] data) {
        int i;
        char j = CRC_PRESET;
        if (data != null && data.length > 0) {
            for (i = 0; i < data.length; i++) {
                int tmp = data[i];
                tmp = tmp & 0x00FF;
                j = CRC8TABLE[j ^ (char) tmp];
            }
        }
        return (byte) j;
    }

    @Override
    public boolean scanCycleStop() {
        boolean re = false;

        try {
            this.scanCycleDataReceiver = null;
            serialPortHelper.setReceiveDataListener(null);
            re = (this.scanCycleDataReceiver == null);
        } catch (Exception e) {
            e.printStackTrace();
            RFIDLog.exceptionLog("scanCycleStop", e);
            re = false;
        }

        return re;
    }
}