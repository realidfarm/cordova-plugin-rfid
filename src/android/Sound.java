package com.realidtek.rfid;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * 提醒声音和提醒震动.<br>
 * <br>
 * CreateDate: 2013-8-30<br>
 * Copyright: Copyright(c) 2013-8-30<br>
 * <br>
 * 
 * @since v1.0.0
 * @Description 2013-8-30::::创建此类</br>
 */
public class Sound {

	/**
	 * 失败提示音和提示震动.<br>
	 * <br>
	 * 
	 * @param context
	 * @Description 2013-9-9::::创建此方法</br>
	 */
	public static void callAlarmAsShiBai(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE); // 播放提示音
		int max = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		int current = audioManager
				.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		MediaPlayer player = MediaPlayer.create(context, R.raw.shibai);
		player.setVolume((float) current / (float) max, (float) current
				/ (float) max); // 设置提示音量
		player.start();// 播放提示音

		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE); // 振动100毫秒
		vibrator.vibrate(100); // 振动100毫秒
	}

	/**
	 * 成功提示音和提示震动.<br>
	 * <br>
	 * 
	 * @param context
	 * @Description 2013-9-9::::创建此方法</br>
	 */
	public static void callAlarm(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE); // 播放提示音
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		MediaPlayer player = MediaPlayer.create(context, R.raw.duka3);
		player.setVolume((float) current / (float) max, (float) current
				/ (float) max); // 设置提示音量
		player.start();// 播放提示音

		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE); // 振动100毫秒
		vibrator.vibrate(100); // 振动100毫秒
	}
}
