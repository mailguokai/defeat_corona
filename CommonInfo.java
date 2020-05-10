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

import java.util.HashMap;

import plus.koubou.defeat_corona.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonInfo {
	/**
	 * スクリーンの高さ（REAL）
	 */
	public static float SCREEN_HEIGHT = 0; // 0は初期値
	/**
	 * スクリーンの幅（REAL）
	 */
	public static float SCREEN_WIDTH = 0; // 0は初期値
	/**
	 * 現在の1ピクセルに対する1dipの倍率 1ピクセル * SCALED_DENSITY == 1dp
	 */
	public static float SCALED_DENSITY = 1; // 1は初期値

	public static Context BASIC_CONTEXT;
	public static final int FRAMES_PER_SECOND = 50; // １秒あたりのFRAME数

	// 最高レベルの合格ライン。これ以上のレベルアップはないため、大数字を与える
	public static final int TOP_LEVEL_PASS_LINE = 9999999;

	/**
	 * メイン設定画面の画面番号 0:初期状態 1:Home 2:
	 */
	public static int MAIN_SETTING_P = 0;

	/**
	 * ゲームのレベルの数
	 */
	public static final int TOTAL_LEVEL_CNT = 100;

	@SuppressLint("UseSparseArrays")
	static HashMap<Integer, Integer> image = new HashMap<Integer, Integer>(); // ！！！！！Imageを追加や削除した時に、必ずここの数字を変える！！！！！

	public static void SetImages() {
		image.put(1, R.drawable.background); // 背景画像
	}
	//--------------------------------メニュー系----------------------------------------------------
	@SuppressLint("UseSparseArrays")
	public static HashMap<String, HashMap<Integer, String>> GetMenuViewValue() {
		HashMap<String, HashMap<Integer, String>> mapMenuImageValue = new HashMap<String, HashMap<Integer, String>>();
		HashMap<Integer, String> mapMy = new HashMap<Integer, String>();

		// 背景音
		mapMy.put(R.id.ldvSettingBGMusic, "1");
		mapMy.put(R.id.aivSettingBGMusic_Off, "0");
		mapMenuImageValue.put("bg_music", mapMy);

		// 効果音
		mapMy = new HashMap<Integer, String>();
		mapMy.put(R.id.ldvSettingSound, "1");
		mapMy.put(R.id.aivSettingSound_Off, "0");
		mapMenuImageValue.put("sound", mapMy);

		// 振動
		mapMy = new HashMap<Integer, String>();
		mapMy.put(R.id.ldvSettingVibration, "1");
		mapMy.put(R.id.aivSettingVibration_Off, "0");
		mapMenuImageValue.put("vibration", mapMy);
		
		// 広告表示
		mapMy = new HashMap<Integer, String>();
		mapMy.put(R.id.ldvRemoveAdver, "1");
		mapMy.put(R.id.aivRemoveAdver_Off, "0");
		mapMenuImageValue.put("adver_visible", mapMy);

		return mapMenuImageValue;
	}

	/*
	 * 広告の種類
	 */
	public static enum ADVER_TYPE
	{
		INTERSTITIALAD
		, ADVIEW
		, REWARD_VIDEO
	}

}
