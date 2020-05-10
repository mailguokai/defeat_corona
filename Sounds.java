/*
 *                 [[ Defeat Corona ]]
 *
 * Copyright (c) 2020 Kaku.
 *
 * This code is distributed under the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * This App is based on frozen-bubble.
 *
 *          [[ http://glenn.sanson.free.fr/fb/ ]]
 *          [[ http://www.frozen-bubble.org/   ]]
 */
// This file is derived from the LunarView.java file which is part of
// the Lunar Lander game included with Android documentation.  The copyright
// notice for the Lunar Lander is reproduced below.
/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package plus.koubou.defeat_corona.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import plus.koubou.defeat_corona.R;

/**
 * BGMおよび効果音の管理クラス
 */
public final class Sounds {
	private static Context context;
	private static MediaPlayer mediaPlayer_Main_Menu; // Main MenuのBGM
	private static MediaPlayer mediaPlayer_level_select; // レベル選択画面のBGM
	private static MediaPlayer mediaPlayer_Game_Preparation; // Game StartのBGM
	private static MediaPlayer mediaPlayer_Game; // ゲームのBGM
	private static SoundPool soundPool;
	private static int sidTouch;
	private static int sidLevelClear;
	private static int sidQuestionRight;
	private static int sidQuestionWrong;
	
	/**
	 * BGMを多重再生を防止するためのフラグ。
	 * 必ず0の場合のみ再生可能。再生し始めるとプラス1させる。
	 */
	private static int flgBGM = 0;
	private static int flgBGM_menu = 0;
	private static int flgBGM_level_select = 0;
	private static int flgBGM_game_preparation = 0;

	public static void init(final Context context) {
		Sounds.context = context;
		flgBGM = 0;
		flgBGM_menu = 0;
		flgBGM_level_select = 0;
		flgBGM_game_preparation = 0;
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
	}

	public static void term() {
		soundPool.release();
	}
	
	/**
	 * All the parameters are the same to the method of "soundPool.play"
	 */
	private static void CommonSoundPoolPlay(int soundID, float leftVolume, float rightVolume,
            int priority, int loop, float rate)
	{
		if(CommonFunctions.GetUserSettingInfo("sound").equals("1"))
		{
			soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
		}
	}

	// ボタン等をタッチした時
	public static void playTouch() {
		CommonSoundPoolPlay(sidTouch, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	// クリアした時
	public static void playLevelClear() {
		CommonSoundPoolPlay(sidLevelClear, 0.8F, 0.8F, 0, 0, 0.9F);
	}
	
	// 問題正解時
	public static void playQuestionRight() {
		CommonSoundPoolPlay(sidQuestionRight, 0.7F, 0.7F, 0, 0, 1.0F);
	}
	
	// 時間回復
	public static void playQuestionWrong(){
		CommonSoundPoolPlay(sidQuestionWrong, 1.0F, 1.0F, 0, 0, 2.0F);
	}

	/**
	 * ゲーム準備画面のBGM
	 */
	public static void playBgmGamePreparation() {
		if(CommonFunctions.GetUserSettingInfo("bg_music").equals("1") && flgBGM_game_preparation == 0)
		{
			flgBGM_game_preparation++;
			mediaPlayer_Game_Preparation = MediaPlayer.create(context, R.raw.bgm_nezuminoensoku_preparation);
			mediaPlayer_Game_Preparation.setLooping(true);
			mediaPlayer_Game_Preparation.setVolume(0.8F, 0.8F);
			mediaPlayer_Game_Preparation.start();
		}
	}

	/**
	 * MainMenu画面のBGM
	 */
	public static void playBgmMainMenu() {
		if(CommonFunctions.GetUserSettingInfo("bg_music").equals("1") && flgBGM_menu == 0)
		{
			flgBGM_menu++;
			mediaPlayer_Main_Menu = MediaPlayer.create(context, R.raw.bgm_menu);
			mediaPlayer_Main_Menu.setLooping(true);
			mediaPlayer_Main_Menu.setVolume(0.5F, 0.5F);
			mediaPlayer_Main_Menu.start();
		}
		//playBackgroundMusic(mediaPlayer_Main_Menu);
	}

	/**
	 * MainMenu画面のBGM
	 */
	public static void playBgmLevelSelect() {
		if(CommonFunctions.GetUserSettingInfo("bg_music").equals("1") && flgBGM_level_select == 0)
		{
			flgBGM_level_select++;
			mediaPlayer_level_select = MediaPlayer.create(context, R.raw.bgm_nezuminoensoku_level_select);
			mediaPlayer_level_select.setLooping(true);
			mediaPlayer_level_select.setVolume(0.6F, 0.6F);
			mediaPlayer_level_select.start();
		}
	}

	public static void pauseBgmMainMenu() {
		if (mediaPlayer_Main_Menu != null) {
			mediaPlayer_Main_Menu.pause();
			//mediaPlayer_Main_Menu.release();
		}
	}

	public static void pauseBgmLevelSelect() {
		if (mediaPlayer_level_select!= null) {
			mediaPlayer_level_select.pause();
		}
	}
	
	public static void pauseBgmPreparation() {
		if (mediaPlayer_Game_Preparation != null) {
			mediaPlayer_Game_Preparation.pause();
			//mediaPlayer_Game_Preparation.release();
		}
	}
	
	public static void pauseBgmGame() {
		if (mediaPlayer_Game != null) {
			mediaPlayer_Game.pause();
			//mediaPlayer_Game.release();
		}
	}

	public static void resumeBgmMainMenu() {
		if (mediaPlayer_Main_Menu != null) {
			mediaPlayer_Main_Menu.start();
		}
	}

	public static void resumeBgmLevelSelect() {
		if (mediaPlayer_level_select != null) {
			mediaPlayer_level_select.start();
		}
	}
	
	public static void resumeBgmPreparation() {
		if (mediaPlayer_Game_Preparation != null) {
			mediaPlayer_Game_Preparation.start();
		}
	}
	
	public static void resumeBgmGame() {
		if (mediaPlayer_Game != null) {
			mediaPlayer_Game.start();
		}
	}

	public static void stopBgmMainMenu() {
		if (mediaPlayer_Main_Menu != null) {
			flgBGM_menu = 0;
			mediaPlayer_Main_Menu.stop();
			//mediaPlayer_Main_Menu.release();
		}
	}

	public static void stopBgmLevelSelect() {
		if (mediaPlayer_level_select != null) {
			flgBGM_level_select = 0;
			mediaPlayer_level_select.stop();
		}
	}
	
	public static void stopBgmGamePreparation() {
		if (mediaPlayer_Game_Preparation != null) {
			flgBGM_game_preparation = 0;
			mediaPlayer_Game_Preparation.stop();
		}
	}
	
	public static void stopBgmGame() {
		if (mediaPlayer_Game != null) {
			flgBGM = 0;
			mediaPlayer_Game.stop();
			//mediaPlayer_Game.release();
		}
	}
	
	public static void vibrate(long milliseconds) {
		if(CommonFunctions.GetUserSettingInfo("vibration").equals("1"))
		{
			Vibrator vibrator = (Vibrator)Sounds.context.getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(milliseconds);
		}
	  }
}
