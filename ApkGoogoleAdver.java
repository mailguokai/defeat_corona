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

import plus.koubou.defeat_corona.R;
import plus.koubou.defeat_corona.common.CommonInfo.ADVER_TYPE;
import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * com.google.android.gms.ads.InterstitialAdのオブジェクト及び表示を
 * カバーするクラス。例えば、ユーザの設定情報を見て広告の表示・非表示を制御するなど。
 * @author guo
 *
 */
public class ApkGoogoleAdver{

	private InterstitialAd _interstitialAd;
	private AdView _adView;
    private RewardedVideoAd _rewardedVideoAd;
	private boolean _is_showed = false; // ワンタイムのみ表示のためのフラグ
    private ADVER_TYPE _type;

	public ApkGoogoleAdver(Context context, ADVER_TYPE type, String license, RewardedVideoAdListener rewardedVideoAdListener)
	{
	    String application_id = context.getResources().getString(R.string.txtAdsApplicationId);
        _type = type;
		////////////////////////////////////////////////////////////////////////////
		// ここはテスト用のユニットIDで、アプリを発行する際は、必ず消して
		////////////////////////////////////////////////////////////////////////////
/*
		if(_type == ADVER_TYPE.INTERSTITIALAD)
		{
			license = "ca-app-pub-3940256099942544/1033173712";  //////////  この行を消して
		}else if(_type == ADVER_TYPE.ADVIEW){
            license = "ca-app-pub-3940256099942544/6300978111";  //////////  この行を消して
        }else if(_type == ADVER_TYPE.REWARD_VIDEO){
            application_id = "ca-app-pub-3940256099942544~3347511713";  //////////  この行を消して
            license = "ca-app-pub-3940256099942544/5224354917";  //////////  この行を消して
        }
*/

		if(_type == ADVER_TYPE.INTERSTITIALAD)
		{
			_interstitialAd = new InterstitialAd(context);
			_interstitialAd.setAdUnitId(license);
			AdRequest adRequest = new AdRequest.Builder().build();
			// Load the interstitial ad.
			_interstitialAd.loadAd(adRequest);
		}
		else if(_type == ADVER_TYPE.ADVIEW)
		{
			_adView = new AdView(context);
			_adView.setAdUnitId(license);
			_adView.setAdSize(AdSize.SMART_BANNER);
			// 一般的なリクエストを行う
			AdRequest adRequest = new AdRequest.Builder().build();
			// 広告リクエストを行って adView を読み込む
			_adView.loadAd(adRequest);
		}
		else if(_type == ADVER_TYPE.REWARD_VIDEO && rewardedVideoAdListener != null)
		{
            MobileAds.initialize(context, application_id);
            _rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
            _rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
            _rewardedVideoAd.loadAd(license, new AdRequest.Builder().build());
		}
	}

    public ApkGoogoleAdver(Context context, ADVER_TYPE type, String license)
    {
        this(context, type, license, null);
    }

	public AdView MakeAdView()
	{
		_adView.setVisibility(IsAdverVisible() ? View.VISIBLE : View.GONE);
		return _adView;
	}

	private boolean IsAdverVisible()
	{
		return CommonFunctions.GetUserSettingInfo("adver_visible").equals("1");
		//return false;
	}

	public boolean IsRewardedVideoAdLoaded()
    {
        return _rewardedVideoAd.isLoaded();
    }

    public void ReloadRewardedVideoAd(String license)
    {
        _rewardedVideoAd.loadAd(license, new AdRequest.Builder().build());
    }

	public void Show()
	{
        if(_type == ADVER_TYPE.INTERSTITIALAD) // インタースティシャル広告の場合
        {
            if(IsAdverVisible() && _interstitialAd.isLoaded())
            {
                _interstitialAd.show();
            }
        }

        if(_type == ADVER_TYPE.REWARD_VIDEO) // リワード広告の場合　※IsAdverVisible()に影響されない
        {
            if(_rewardedVideoAd.isLoaded())
            {
                _rewardedVideoAd.show();
            }
        }
	}

	/**
	 * 一回のみを実行。
	 * 背景はSurfaceViewでdoDraw()が繰り返し実行される場合
	 * 広告のShow()を繰り返し呼び出さないため、ワンタイムのShow()を行うこと
	 */
	public void ShowOnce()
	{
		if(!_is_showed)
		{
			Show();
			_is_showed = true;
		}
	}

	// 再び広告の表示が出来るようにフラグをリセット
	public void ResetShowStatus()
	{
		_is_showed = false;
	}

	// 広告を隠す
	public void Hide()
	{
		_adView.setVisibility(View.GONE);
	}

	public void ReloadInterstitial()
	{
		AdRequest adRequest = new AdRequest.Builder().build();
		// Load the interstitial ad.
		_interstitialAd.loadAd(adRequest);
	}
}
