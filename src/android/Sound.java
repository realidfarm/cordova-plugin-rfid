package com.realidtek.rfid;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * ����������������.<br>
 * <br>
 * CreateDate: 2013-8-30<br>
 * Copyright: Copyright(c) 2013-8-30<br>
 * <br>
 * 
 * @since v1.0.0
 * @Description 2013-8-30::::��������</br>
 */
public class Sound {

	/**
	 * ʧ����ʾ������ʾ��.<br>
	 * <br>
	 * 
	 * @param context
	 * @Description 2013-9-9::::�����˷���</br>
	 */
	public static void callAlarmAsShiBai(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE); // ������ʾ��
		int max = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		int current = audioManager
				.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		MediaPlayer player = MediaPlayer.create(context, R.raw.shibai);
		player.setVolume((float) current / (float) max, (float) current
				/ (float) max); // ������ʾ����
		player.start();// ������ʾ��

		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE); // ��100����
		vibrator.vibrate(100); // ��100����
	}

	/**
	 * �ɹ���ʾ������ʾ��.<br>
	 * <br>
	 * 
	 * @param context
	 * @Description 2013-9-9::::�����˷���</br>
	 */
	public static void callAlarm(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE); // ������ʾ��
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		MediaPlayer player = MediaPlayer.create(context, R.raw.duka3);
		player.setVolume((float) current / (float) max, (float) current
				/ (float) max); // ������ʾ����
		player.start();// ������ʾ��

		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE); // ��100����
		vibrator.vibrate(100); // ��100����
	}
}
