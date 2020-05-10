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

import java.util.HashMap;
import java.util.Map;

import plus.koubou.defeat_corona.R;
import plus.koubou.defeat_corona.common.ApkGoogoleAdver;
import plus.koubou.defeat_corona.common.CommonFunctions;
import plus.koubou.defeat_corona.common.CommonInfo;
import plus.koubou.defeat_corona.common.CommonInfo.ADVER_TYPE;
import plus.koubou.defeat_corona.common.DB;
import plus.koubou.defeat_corona.common.Sounds;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainMenu extends Activity implements OnClickListener {
	/** 連打防止ためのフラグ */
	boolean blIsStartActivity = true;
	private plus.koubou.defeat_corona.customize.ApkImageViewButton aivBack;
	public static final int MENU_SETTING = 1;
	public static final int MENU_ABOUT = MENU_SETTING + 1;
	public static final int MENU_CONTACT = MENU_SETTING + 2;
	private static int MAIN_SETTING_P_NOW = 0; // 現在の設定画面番号を標識する。画面更新の時に古くなるかもしれない。
	private static final long WAITING_TIME = 3000;
	View myView;
	LinearLayout lltMainSettingNowP;
	LinearLayout lltMainSettingNextP;
	LinearLayout lltAds;
	private ApkImageViewButton aivPlay;
	//private ImageButton ibtNewGame;
	private ApkImageViewButton aivOptions;
	private ApkImageViewButton aivAbout;
	private ApkImageViewButton aivExit;

	Thread thdButtonFire;
	private LoadingView ldvSettingBGMusic;
	private LoadingView ldvSettingSound;
	private LoadingView ldvSettingVibration;
	private LoadingView ldvRemoveAdver;
	
	private ApkImageViewButton aivSettingBGMusic_Off;
	private ApkImageViewButton aivSettingSound_Off;
	private ApkImageViewButton aivSettingVibration_Off;
	private ApkImageViewButton aivRemoveAdver_Off;
	
	private ImageView imvLogoTitle;
	private ApkGoogoleAdver apkAdview;
	
	// ゲーム選択画面
	Integer[] intGameIds;
	Map<Integer, String> mapGameIdTitle = new HashMap<Integer, String>();
	Map<Integer, Integer> mapGameIdStatus = new HashMap<Integer, Integer>();
	HashMap<String, HashMap<Integer, String>> hmpMenuImageValue = new HashMap<String, HashMap<Integer, String>>();

    //static CObjectPool cObjectPool = CObjectPool.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(this);
		myView = mInflater.inflate(R.layout.main_menu, null);
		// custom title 
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(myView);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
		initWidgets();

		// 広告の表示を開始
		// adView を作成する
		apkAdview = new ApkGoogoleAdver(this, ADVER_TYPE.ADVIEW, getBaseContext().getResources().getString(R.string.txtAdsLicenseMainMenu));
	    lltAds.addView(apkAdview.MakeAdView());
	    // BGMの初期化
	    Sounds.init(this);
	}

	private void initWidgets() {
		// 最初はHome
		CommonInfo.MAIN_SETTING_P = 1;
		MAIN_SETTING_P_NOW = CommonInfo.MAIN_SETTING_P;
		this.aivBack = (ApkImageViewButton) myView.findViewById(R.id.aivBack);
		aivBack.setOnClickListener(this);
		aivBack.ImageIdClicked = R.drawable.btn_return_clicked;
		// 最初は非表示
		aivBack.setVisibility(View.INVISIBLE);
		this.lltAds = (LinearLayout) myView.findViewById(R.id.lltAds);
		this.aivPlay = (ApkImageViewButton) myView.findViewById(R.id.aivPlay);
		aivPlay.setOnClickListener(this);
		aivPlay.ImageIdClicked = R.drawable.play_clicked;
		
		this.aivOptions = (ApkImageViewButton) myView.findViewById(R.id.aivOptions);
		aivOptions.setOnClickListener(this);
		aivOptions.ImageIdClicked = R.drawable.options_clicked;
		
		this.aivAbout = (ApkImageViewButton) myView.findViewById(R.id.aivAbout);
		aivAbout.setOnClickListener(this);
		aivAbout.ImageIdClicked = R.drawable.about_clicked;
		
		this.aivExit = (ApkImageViewButton) myView.findViewById(R.id.aivExit);
		aivExit.setOnClickListener(this);
		aivExit.ImageIdClicked = R.drawable.exit_clicked;
		
		this.ldvSettingBGMusic = (LoadingView)findViewById(R.id.ldvSettingBGMusic);
		ldvSettingBGMusic.setOnClickListener(this);
		this.aivSettingBGMusic_Off = (ApkImageViewButton) myView.findViewById(R.id.aivSettingBGMusic_Off);
		aivSettingBGMusic_Off.setOnClickListener(this);
		this.ldvSettingSound = (LoadingView)findViewById(R.id.ldvSettingSound);
		ldvSettingSound.setOnClickListener(this);
		this.aivSettingSound_Off = (ApkImageViewButton) myView.findViewById(R.id.aivSettingSound_Off);
		aivSettingSound_Off.setOnClickListener(this);
		this.ldvSettingVibration = (LoadingView) myView.findViewById(R.id.ldvSettingVibration);
		ldvSettingVibration.setOnClickListener(this);
		this.aivSettingVibration_Off = (ApkImageViewButton) myView.findViewById(R.id.aivSettingVibration_Off);
		aivSettingVibration_Off.setOnClickListener(this);
		this.ldvRemoveAdver = (LoadingView) myView.findViewById(R.id.ldvRemoveAdver);
		ldvRemoveAdver.setOnClickListener(this);
		this.aivRemoveAdver_Off = (ApkImageViewButton) myView.findViewById(R.id.aivRemoveAdver_Off);
		aivRemoveAdver_Off.setOnClickListener(this);

		hmpMenuImageValue = CommonInfo.GetMenuViewValue();
		// 画面の表示・非表示（初期化）
		SetViewVisibleByPageNo(GetLinearLayoutByPageNo(CommonInfo.MAIN_SETTING_P), true);
		
		// メールアドレスをクリックすることで送信できるようの設定
		SpannableString msp = new SpannableString(getResources().getString(R.string.e_mail));
		msp.setSpan(new UnderlineSpan(), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		msp.setSpan(new URLSpan("mailto:" + getResources().getString(R.string.e_mail)
				+ "?subject=" + getResources().getString(R.string.app_name) + "について")
				, 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		TextView tv = (TextView) this.findViewById(R.id.txMail);
		tv.setText(msp);  
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		
		// Logoのサイズを設定
		this.imvLogoTitle = (ImageView) myView.findViewById(R.id.imvLogoTitle);
        LayoutParams lp = imvLogoTitle.getLayoutParams();
        lp.height = (int)(CommonInfo.SCREEN_HEIGHT * 0.25);
        lp.width = (int)(CommonInfo.SCREEN_WIDTH * 0.85);
        imvLogoTitle.setLayoutParams(lp);
        
        // Logoの位置を設定
        MarginLayoutParams mlp=(MarginLayoutParams)imvLogoTitle.getLayoutParams();
        mlp.setMargins(0, (int)(CommonInfo.SCREEN_HEIGHT * 0.1), 0, 0);
        imvLogoTitle.setLayoutParams(mlp);
        
        // LoadingViewの初期化
        initLoadingImages();
        // LoadingViewの動画開始
        thdButtonFire = new Thread() {
			public void run() {
				// 起動画面
				ldvSettingBGMusic.startAnim(5);
				ldvSettingSound.startAnim(5);
				ldvSettingVibration.startAnim(5);
				ldvRemoveAdver.startAnim(5);
				
				try {
					Thread.sleep(WAITING_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thdButtonFire.start();
	}

	@Override
	protected void onDestroy() {
    	// Threadを停止させる
    	if(thdButtonFire != null)
    	{
    		thdButtonFire.interrupt();
    		thdButtonFire = null;
    	}
		super.onDestroy();
	}

    private void initLoadingImages()
    {
    	// charactor
        int[] imageIds = new int[2];
        imageIds[0] = R.drawable.btn_menu_on_0;
        imageIds[1] = R.drawable.btn_menu_on_1;
        // 背景音
        ldvSettingBGMusic.setImageIds(imageIds);
        ldvSettingSound.setImageIds(imageIds);
        ldvSettingVibration.setImageIds(imageIds);
        ldvRemoveAdver.setImageIds(imageIds);
    }

	public void onClick(final View v) {
		// クリック音を流すため
		try{
			ApkImageViewButton aivClickSound = (ApkImageViewButton)v;
			aivClickSound.onClick(aivClickSound);
		}
		catch(Exception e)
		{
		}
		if (v == aivBack) {
			//aivBack.onClick(aivBack);
			CommonInfo.MAIN_SETTING_P = GetReturnToSettingPageNo(MAIN_SETTING_P_NOW);
		}
		else if (v == aivPlay)
		{
			if(!blIsStartActivity)
			{
				return;
			}
			blIsStartActivity = false;

			CommonFunctions.Transition(MainMenu.this, LevelSelecting.class, true);
		}
		//else if (v == ibtNewGame)
		//{
		//	CommonInfo.MAIN_SETTING_P = 2; // Play画面へ
			// ゲーム選択画面を初期化 (最初から)
		//	SetGameSelectGridview(0);
		//}
		else if (v == aivOptions)
		{
			//aivOptions.onClick(aivOptions);
			CommonInfo.MAIN_SETTING_P = 3; // オプション画面へ
			IndicateUserSettingInfo("bg_music");
			IndicateUserSettingInfo("sound");
			IndicateUserSettingInfo("vibration");
			IndicateUserSettingInfo("adver_visible");
		}
		else if (v == aivAbout)
		{
			//aivAbout.onClick(aivAbout);
			CommonInfo.MAIN_SETTING_P = 4; // About画面へ
		}
		else if (v == aivExit) // ゲームを終了
		{
			//aivExit.onClick(aivExit);
			CommonFunctions.ExitApp(MainMenu.this);
		}
		else if (v == aivBack)
		{
			//aivBack.onClick(aivBack);
			CommonFunctions.ExitApp(MainMenu.this); // アプリを終了
        }
		else if (v == ldvSettingBGMusic)
		{
            //ldvSettingBGMusic.onClick(ldvSettingBGMusic);
            UpdateSettingInfoByImageView("bg_music", R.id.aivSettingBGMusic_Off);
            Sounds.pauseBgmMainMenu();
        }
		else if (v == aivSettingBGMusic_Off)
		{
			//aivSettingBGMusic_Off.onClick(aivSettingBGMusic_Off);
			UpdateSettingInfoByImageView("bg_music", R.id.ldvSettingBGMusic);
			Sounds.playBgmMainMenu();
		}
		else if (v == ldvSettingSound)
		{
			//ldvSettingSound.onClick(ldvSettingSound);
			UpdateSettingInfoByImageView("sound", R.id.aivSettingSound_Off);
		}
		else if (v == aivSettingSound_Off)
		{
			//aivSettingSound_Off.onClick(aivSettingSound_Off);
			UpdateSettingInfoByImageView("sound", R.id.ldvSettingSound);
		}
		else if (v == ldvSettingVibration)
		{
			//ldvSettingVibration.onClick(ldvSettingVibration);
			UpdateSettingInfoByImageView("vibration", R.id.aivSettingVibration_Off);
		}
		else if (v == aivSettingVibration_Off)
		{
			//aivSettingVibration_Off.onClick(aivSettingVibration_Off);
			UpdateSettingInfoByImageView("vibration", R.id.ldvSettingVibration);
		}

		if (MAIN_SETTING_P_NOW != CommonInfo.MAIN_SETTING_P)
		{
			// Homeの時のみ非表示
			if(CommonInfo.MAIN_SETTING_P == 1)
			{
				aivBack.setVisibility(View.INVISIBLE);
			}
			else
			{
				aivBack.setVisibility(View.VISIBLE);
			}
	
			Animation amsNow = null;
			Animation amsNext = null;
			if(MAIN_SETTING_P_NOW < CommonInfo.MAIN_SETTING_P)
			{
				amsNow = AnimationUtils.loadAnimation(MainMenu.this,
						R.anim.anim_from_center_to_left);
				amsNext = AnimationUtils.loadAnimation(MainMenu.this,
						R.anim.anim_from_right_to_center);
			}
			else
			{
				amsNow = AnimationUtils.loadAnimation(MainMenu.this,
						R.anim.anim_from_center_to_right);
				amsNext = AnimationUtils.loadAnimation(MainMenu.this,
						R.anim.anim_from_left_to_center);
			}
			
			lltMainSettingNowP = GetLinearLayoutByPageNo(MAIN_SETTING_P_NOW);
			lltMainSettingNextP = GetLinearLayoutByPageNo(CommonInfo.MAIN_SETTING_P);
			
			amsNow.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationEnd(Animation animation) {
				}
				public void onAnimationRepeat(Animation animation) {
				}
	
				public void onAnimationStart(Animation animation) {
				}
			});
			amsNow.setFillAfter(false);
			lltMainSettingNowP.startAnimation(amsNow);
			// 現在の画面を非表示にする
			SetViewVisibleByPageNo(lltMainSettingNowP, false);
			// 次の画面を表示にする
			SetViewVisibleByPageNo(lltMainSettingNextP, true);
			amsNext.setFillAfter(false);
			lltMainSettingNextP.startAnimation(amsNext);
			
			// 忘れないで
			MAIN_SETTING_P_NOW = CommonInfo.MAIN_SETTING_P;
		}
	}

	/**
	 * ユーザが設定された情報をメニュー上に表示
	 */
	private void IndicateUserSettingInfo(String strMenuName) {
		String strSettingInfo = CommonFunctions.GetUserSettingInfo(strMenuName);
		
		if(!strSettingInfo.equals(""))
		{
			View viwSelector;
			for(Map.Entry<Integer, String> entry : hmpMenuImageValue.get(strMenuName).entrySet()){
				viwSelector = (View) findViewById(entry.getKey());
				if(strSettingInfo.equals(entry.getValue()))
				{
					viwSelector.setVisibility(View.VISIBLE);
				}
				else
				{
					viwSelector.setVisibility(View.GONE);
				}
			}
		}
	}
	
	/**
	 * 設定情報の更新と画面表示の変更
	 */
	private void UpdateSettingInfoByImageView(String strMenuName, int intThisView) {
		
		View viwThis;
		for(Map.Entry<Integer, String> entry : hmpMenuImageValue.get(strMenuName).entrySet()) // <imvView_id, value>
		{
			viwThis = (View) findViewById(entry.getKey());
			if(intThisView == entry.getKey())
			{
				viwThis.setVisibility(View.VISIBLE);
				// DB更新
				CommonFunctions.UpdateUserSettingInfo(strMenuName, entry.getValue());
			}
			else
			{
				viwThis.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// BGMを停止
		Sounds.stopBgmMainMenu();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// BGMを開始
		Sounds.playBgmMainMenu();
		// activityをスタートできるようにする
		blIsStartActivity = true;
		super.onResume();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		} else {
			if(MAIN_SETTING_P_NOW == 1) // メインメニューの場合は終了する
			{
				CommonFunctions.ExitApp(MainMenu.this);
			}
			else
			{
				onClick(aivBack);
			}
			
			return true;
		}
	}
	
	private LinearLayout GetLinearLayoutByPageNo(int no)
	{
		switch(no)
		{
			case 1: // Home画面
				return (LinearLayout) myView.findViewById(R.id.lltMainSettingP1);
			case 2: // 倉庫選択画面
				//return (LinearLayout) myView.findViewById(R.id.lltMainSettingP2);
			case 3: // オプション画面
				return (LinearLayout) myView.findViewById(R.id.lltMainSettingP3);
			case 4: // About画面
				return (LinearLayout) myView.findViewById(R.id.lltMainSettingP4);
			case 5: // 効果音設定画面
				//return (LinearLayout) myView.findViewById(R.id.lltMainSettingP5);
			
			default:
				return (LinearLayout) myView.findViewById(R.id.lltMainSettingP1);
		}
	}
	
	/**
	 * 設定画面の戻るボタンを押した時に、戻り先の画面番号を戻す
	 * @param now
	 * @return
	 */
	public static int GetReturnToSettingPageNo(int now)
    {
		switch(now)
		{
			case 1: // Home画面の場合は戻り先はないため、自分自身を戻す
				return 1;
			case 2: // 倉庫選択画面の場合は、Homeへ
				return 1;
			case 3: // オプション画面の場合は、Homeへ
				return 1;
			case 4: // About画面の場合は、Homeへ
				return 1;
			case 5: // 効果音設定画面の場合は、オプション画面へ
				return 3;
				
			default:
				return 1;
		}
    }
	
	/**
	 * 指定された画面を表示/非表示にする
	 * @pviewam no
	 * @param isVisible
	 */
	private void SetViewVisibleByPageNo(View view , boolean isVisible)
	{	
		if(view == null)
		{
			return;
		}
		
		if(isVisible)
		{
			view.setVisibility(View.VISIBLE);
		}
		else
		{
			view.setVisibility(View.GONE);
		}
	}





}