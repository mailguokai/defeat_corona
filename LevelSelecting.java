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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import plus.koubou.defeat_corona.FrozenBubble;
import plus.koubou.defeat_corona.R;
import plus.koubou.defeat_corona.common.ApkGoogoleAdver;
import plus.koubou.defeat_corona.common.CommonFunctions;
import plus.koubou.defeat_corona.common.CommonInfo;
import plus.koubou.defeat_corona.common.Sounds;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

@SuppressLint("HandlerLeak")
public class LevelSelecting extends Activity implements View.OnClickListener {
	LinearLayout lltAds;
	private ApkGoogoleAdver apkAdview;
	private ApkGoogoleAdver myRewardedVideoAd;
	private ApkImageViewButton aivBack;
	/**
	 * レベルボタン。
	 */
	private ApkImageViewButton[] ivbLevel = new ApkImageViewButton[CommonInfo.TOTAL_LEVEL_CNT];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.level_select);

		/**
		 *  ①リワード広告のリスナーを生成
		 */
		RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
			@Override
			public void onRewardedVideoAdLoaded() {}

			@Override
			public void onRewardedVideoAdOpened() {}

			@Override
			public void onRewardedVideoStarted() {}

			@Override
			public void onRewardedVideoAdClosed() {
				// 広告を一回閉じたら、再度ロードしないと表示できないため、再ロードさせる
				myRewardedVideoAd.ReloadRewardedVideoAd(getResources().getString(R.string.txtAdsRewardAdsLicense));
			}

			@Override
			public void onRewarded(RewardItem rewardItem) {
				for(int i=0; i< CommonInfo.TOTAL_LEVEL_CNT; i++ )
				{
					if(ivbLevel[i].getImageId() == getResources().getIdentifier("level_" + (i+1) + "_locked", "drawable", getPackageName()))
					{
						ivbLevel[i].setImageId(getResources().getIdentifier("level_" + (i+1), "drawable", getPackageName()));
						CommonFunctions.SaveUserStatusToDB("license_for_level", i);
					}
				}
			}

			@Override
			public void onRewardedVideoAdLeftApplication() {}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {}

			@Override
			public void onRewardedVideoCompleted() {}
		};

		/*
		 *  ②リワード広告のオブジェクトを生成
		 */
		myRewardedVideoAd = new ApkGoogoleAdver(this
				, CommonInfo.ADVER_TYPE.REWARD_VIDEO
				, getResources().getString(R.string.txtAdsRewardAdsLicense)
				, rewardedVideoAdListener);

		// 戻るボタンの設置
		this.aivBack = (ApkImageViewButton) findViewById(R.id.aivBack);
		aivBack.setOnClickListener(this);
		aivBack.ImageIdClicked = R.drawable.btn_return_clicked;

		// レベルボタンの初期化
		int activeLevel = Integer.parseInt(CommonFunctions.GetUserStatus("level"));
		// リワード広告を見ることで解除するレベル関連。license_for_levelより高いレベルはリワード広告を見ることで解除する必要がある。
		//int license_for_level = Integer.parseInt(CommonFunctions.GetUserStatus("license_for_level"));
		// 当面はリワード広告を適用しない。仮の数字を設定
		// 但し、FrozenGameのレベルは 0 -- 99 、もぐらたたきと１つずれるため、将来リワード広告を適用する時に要注意
		int license_for_level = 999;
		for(int i=0; i< CommonInfo.TOTAL_LEVEL_CNT; i++ )
		{
			ivbLevel[i] = (ApkImageViewButton)findViewById(getResources().getIdentifier("aivLevel_" + (i+1), "id", getPackageName()));
			if(i <= activeLevel)
			{
				ivbLevel[i].setOnClickListener(this);
				ivbLevel[i].ImageIdClicked = getResources().getIdentifier("level_" + (i+1) + "_clicked", "drawable", getPackageName());
				// リワード広告を見ることで解除する必要なレベル
				if(i > license_for_level)
				{
					ivbLevel[i].setImageId(getResources().getIdentifier("level_" + (i+1) + "_locked", "drawable", getPackageName()));
				}
				else
				{
					ivbLevel[i].setImageId(getResources().getIdentifier("level_" + (i+1), "drawable", getPackageName()));
				}
			}
			else{
				ivbLevel[i].setImageId(getResources().getIdentifier("level_" + (i+1) + "_clicked", "drawable", getPackageName()));
			}
		}

		// BGMの初期化
		Sounds.init(this);

		// 広告の表示を開始
		// adView を作成する
		apkAdview = new ApkGoogoleAdver(this, CommonInfo.ADVER_TYPE.ADVIEW, getBaseContext().getResources().getString(R.string.txtAdsLicenseLevelSelect));
		this.lltAds = (LinearLayout) findViewById(R.id.lltAds);
		lltAds.addView(apkAdview.MakeAdView());
	}


	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// MainMenuを起動
				CommonFunctions.Transition(LevelSelecting.this, FrozenBubble.class);
			}
		}
	};

	@Override
	public void onClick(View v) {
		// クリック音を流すため
		try{
			ApkImageViewButton aivClickSound = (ApkImageViewButton)v;
			aivClickSound.onClick(aivClickSound);
		}
		catch(Exception e)
		{
		}
		if (v == aivBack) {
			CommonFunctions.Transition(LevelSelecting.this, MainMenu.class, true);
		}
		else
		{
			for(int i=0; i<CommonInfo.TOTAL_LEVEL_CNT; i++ )
			{
				if(v.getId() == ivbLevel[i].getId())
				{
					CommonFunctions.SaveUserStatusToDB("selected_level", i);

//					if(ivbLevel[i].getImageId() == getResources().getIdentifier("level_" + (i+1) + "_locked", "drawable", getPackageName()))
//					{
//						DialogInterface.OnClickListener okOnClicked = new DialogInterface.OnClickListener(){
//							public void onClick(DialogInterface dialog, int whichButton) {
//								/*
//								 *  ③リワード広告を表示
//								 */
//								if(myRewardedVideoAd.IsRewardedVideoAdLoaded())
//								{
//									myRewardedVideoAd.Show();
//								}
//								else
//								{
//									CommonFunctions.Transition(LevelSelecting.this, GamePrepared.class, true);
//								}
//							}
//						};
//						CommonFunctions.ShowDialogWithView(LevelSelecting.this
//								, R.drawable.dialog_icon
//								, R.string.unlock_level_introduce
//								, 18
//								, R.string.watch_reward_ads
//								, okOnClicked
//								, R.string.do_not_unlock
//								, null);
//					}
//					else
//					{
						CommonFunctions.Transition(LevelSelecting.this, GamePrepared.class, true);
					//}
					break;
				}
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		else
		{
			onClick(aivBack);
			return true;
		}
	}

	@Override
	protected void onPause() {
		// BGMを停止
		Sounds.stopBgmLevelSelect();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// BGMを開始
		Sounds.playBgmLevelSelect();
		super.onResume();
	}
}
