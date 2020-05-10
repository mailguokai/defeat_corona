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
package plus.koubou.defeat_corona.customize;

import plus.koubou.defeat_corona.FrozenBubble;
import plus.koubou.defeat_corona.R;
import plus.koubou.defeat_corona.common.CommonFunctions;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import plus.koubou.defeat_corona.common.Sounds;

@SuppressLint("HandlerLeak")
public class GamePrepared extends Activity {
	// 起動画面
	private static final long WAITING_TIME = 500;
	private LoadingView ldvMogura_0;
	private LoadingView ldvMogura_1;
	private LoadingView ldvMogura_2;
	private LoadingView ldvMogura_3;
	private LoadingView ldvMogura_4;
	private LoadingView ldvMogura_5;
	private LoadingView ldvMogura_6;
	private LoadingView ldvMogura_7;
	private LoadingView ldvMogura_8;

	private LoadingView ldvCountDown;
	Thread thdMoguraTataki;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.game_prepared);

		// 起動画面
		ldvCountDown = (LoadingView) findViewById(R.id.ldvCountDown);
		ldvMogura_0 = (LoadingView) findViewById(R.id.ldvMogura_0);
		ldvMogura_1 = (LoadingView) findViewById(R.id.ldvMogura_1);
		ldvMogura_2 = (LoadingView) findViewById(R.id.ldvMogura_2);
		ldvMogura_3 = (LoadingView) findViewById(R.id.ldvMogura_3);
		ldvMogura_4 = (LoadingView) findViewById(R.id.ldvMogura_4);
		ldvMogura_5 = (LoadingView) findViewById(R.id.ldvMogura_5);
		ldvMogura_6 = (LoadingView) findViewById(R.id.ldvMogura_6);
		ldvMogura_7 = (LoadingView) findViewById(R.id.ldvMogura_7);
		ldvMogura_8 = (LoadingView) findViewById(R.id.ldvMogura_8);


		initLoadingImages();

		thdMoguraTataki = new Thread() {
			public void run() {
				// モグラたたき動画
				ldvMogura_0.startAnim(6);
				ldvMogura_1.startAnim(4);
				ldvMogura_2.startAnim(5);
				ldvMogura_3.startAnim(6);
				ldvMogura_4.startAnim(6);
				ldvMogura_5.startAnim(4);
				ldvMogura_6.startAnim(5);
				ldvMogura_7.startAnim(7);
				ldvMogura_8.startAnim(5);

				// カウントダウン動画
				ldvCountDown.startAnim(4, true);
				try {
					while (!ldvCountDown.isStop())
					{
						Thread.sleep(WAITING_TIME);
					}
					Message m = new Message();
					m.what = 1;
					GamePrepared.this.mHandler.sendMessage(m);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thdMoguraTataki.start();
		// BGMの初期化
		Sounds.init(this);
	}

	@Override
	protected void onDestroy() {
		// 戦火のThreadを停止させる
		if (thdMoguraTataki != null) {
			thdMoguraTataki.interrupt();
			thdMoguraTataki = null;
		}
		super.onDestroy();
	}

	private void initLoadingImages() {
		// モグラ
		int[] imageIds = new int[8];
		imageIds[0] = R.drawable.bubble_1;
		imageIds[1] = R.drawable.bubble_2;
		imageIds[2] = R.drawable.bubble_3;
		imageIds[3] = R.drawable.bubble_4;
		imageIds[4] = R.drawable.bubble_5;
		imageIds[5] = R.drawable.bubble_6;
		imageIds[6] = R.drawable.bubble_7;
		imageIds[7] = R.drawable.bubble_8;

		ldvMogura_0.setImageIds(imageIds);
		ldvMogura_1.setImageIds(imageIds);
		ldvMogura_5.setImageIds(imageIds);
		ldvMogura_8.setImageIds(imageIds);

		imageIds = new int[8];
		imageIds[0] = R.drawable.bubble_5;
		imageIds[1] = R.drawable.bubble_6;
		imageIds[2] = R.drawable.bubble_7;
		imageIds[3] = R.drawable.bubble_8;
		imageIds[4] = R.drawable.bubble_1;
		imageIds[5] = R.drawable.bubble_2;
		imageIds[6] = R.drawable.bubble_3;
		imageIds[7] = R.drawable.bubble_4;

		ldvMogura_2.setImageIds(imageIds);
		ldvMogura_3.setImageIds(imageIds);

		imageIds = new int[8];
		imageIds[0] = R.drawable.bubble_7;
		imageIds[1] = R.drawable.bubble_8;
		imageIds[2] = R.drawable.bubble_1;
		imageIds[3] = R.drawable.bubble_2;
		imageIds[4] = R.drawable.bubble_4;
		imageIds[5] = R.drawable.bubble_5;
		imageIds[6] = R.drawable.bubble_3;
		imageIds[7] = R.drawable.bubble_6;
		ldvMogura_4.setImageIds(imageIds);
		ldvMogura_6.setImageIds(imageIds);
		ldvMogura_7.setImageIds(imageIds);

		imageIds = new int[9];
		imageIds[0] = R.drawable.count_down_3_small;
		imageIds[1] = R.drawable.count_down_3_small;
		imageIds[2] = R.drawable.count_down_3;
		imageIds[3] = R.drawable.count_down_3_small;
		imageIds[4] = R.drawable.count_down_2;
		imageIds[5] = R.drawable.count_down_2_small;
		imageIds[6] = R.drawable.count_down_1;
		imageIds[7] = R.drawable.count_down_1_small;
		imageIds[8] = R.drawable.count_down_start;
		ldvCountDown.setImageIds(imageIds);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// MainMenuを起動
				CommonFunctions.Transition(GamePrepared.this, FrozenBubble.class);
			}
		}
	};

	@Override
	protected void onPause() {
		// BGMを停止
		Sounds.stopBgmGamePreparation();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// BGMを開始
		Sounds.playBgmGamePreparation();
		super.onResume();
	}
}
