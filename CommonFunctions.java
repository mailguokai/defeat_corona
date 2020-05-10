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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import plus.koubou.defeat_corona.R;
//import koubou.plus.android.license.moguratataki.core.control.CBlockGroup;
//import koubou.plus.android.license.moguratataki.core.control.CObjectPool;
//import koubou.plus.android.license.moguratataki.core.views.CItem;
//import koubou.plus.android.license.moguratataki.core.views.CSurfaceButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommonFunctions {
	final static String LOGTAG = "CommonFunctions";
	public static float ConvertToRealHeight(float standardHeight)
	{
		return standardHeight * CommonInfo.SCREEN_HEIGHT / 100;
	}
	
	public static float ConvertToRealWidth(float standardWidth)
	{
		return standardWidth * CommonInfo.SCREEN_WIDTH / 100;
	}
	
	public static float ConvertToStandard_Y(float realHeight)
	{
		return realHeight * 100 / CommonInfo.SCREEN_HEIGHT;
	}
	
	public static float ConvertToStandard_X(float realWidth)
	{
		return realWidth * 100 / CommonInfo.SCREEN_WIDTH;
	}

	/**
	 * Viewの位置を設定する
	 */
	public static void LocateView(View v, int marginLeft, int marginTop, int marginRight, int marginBottom)
	{
		MarginLayoutParams mlp = (MarginLayoutParams)v.getLayoutParams();
		mlp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		v.setLayoutParams(mlp);
	}

	/**
	 * Viewのサイズと位置を設定する
	 */
	public static void LocateView(View v, int height, int width, int marginLeft, int marginTop, int marginRight, int marginBottom)
	{
		// サイズを設定
		LayoutParams lp = v.getLayoutParams();
		lp.height = height;
		lp.width = width;
		v.setLayoutParams(lp);

		// 位置を設定
		LocateView(v, marginLeft, marginTop, marginRight, marginBottom);
	}

	/**
	 * Viewの位置を設定する（数字はディスプレーに対する比率）
	 */
	public static void LocateView(View v, double marginLeft, double marginTop, double marginRight, double marginBottom)
	{
		MarginLayoutParams mlp = (MarginLayoutParams)v.getLayoutParams();
		mlp.setMargins((int) (CommonInfo.SCREEN_WIDTH * marginLeft), (int) (CommonInfo.SCREEN_HEIGHT * marginTop), (int) (CommonInfo.SCREEN_WIDTH * marginRight), (int) (CommonInfo.SCREEN_HEIGHT * marginBottom));
		v.setLayoutParams(mlp);
	}

	/**
	 * Viewのサイズと位置を設定する（数字はディスプレーに対する比率）
	 */
	public static void LocateView(View v, double height, double width, double marginLeft, double marginTop, double marginRight, double marginBottom)
	{
		// サイズを設定
		LayoutParams lp = v.getLayoutParams();
		lp.height = (int)(CommonInfo.SCREEN_HEIGHT * height);
		lp.width = (int)(CommonInfo.SCREEN_WIDTH * width);
		v.setLayoutParams(lp);

		// 位置を設定
		LocateView(v, marginLeft, marginTop, marginRight, marginBottom);
	}

	/**
	 * 画面遷移
	 * blCloseActivityFrom : 遷移元のActivityを閉じるか
	 */
	public static void Transition(Context from, Class<?> to, Boolean blCloseActivityFrom)
	{
		Transition(from, to, blCloseActivityFrom, null);
	}

	/**
	 * 画面遷移
	 * 遷移元のActivityを閉じるようにする
	 */
	public static void Transition(Context from, Class<?> to)
	{
		Transition(from, to, true);
	}

	/**
	 * 画面遷移
	 * blCloseActivityFrom : 遷移元のActivityを閉じるか
	 */
	public static void Transition(Context from, Class<?> to, Boolean blCloseActivityFrom, Bundle bundle)
	{
		Intent intent = new Intent();
		intent.setClass(from, to);
		if(bundle != null)
		{
			intent.putExtras(bundle);
		}
		from.startActivity(intent);
		if(blCloseActivityFrom)
		{
			((Activity)from).finish();
		}
	}

	/**
	 * ユーザのすべての設定情報を取得する関数
	 */
	public static Map<String,String> GetUserSettingInfo()
	{
		Map<String,String> mapSettingInfo = new HashMap<String,String>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DB.DB_NAME, null);
		Cursor csrSettingInfo = null;
		csrSettingInfo = DB.GetDataFromTable(db, "tb_user_setting", "name, value", csrSettingInfo);

		if(null != csrSettingInfo)
		{
			while(csrSettingInfo.moveToNext())
			{
				mapSettingInfo.put(csrSettingInfo.getString(0), csrSettingInfo.getString(1));
			}
		}
		csrSettingInfo.close();
		db.close();

		return mapSettingInfo;
	}

	/**
	 * ユーザの特定の設定情報を取得する関数
	 * sound: 効果音
	 * bg_music: 背景音
	 * vibration: 振動
	 * adver_visible: 広告表示
	 */
	public static String GetUserSettingInfo(String name)
	{
		Map<String,String> mapSettingInfo = GetUserSettingInfo();
		if(mapSettingInfo != null && mapSettingInfo.containsKey(name))
		{
			return mapSettingInfo.get(name);
		}
		else
		{
			return "";
		}
	}

    /**
     * アプリを終了
     */
    public static void ExitApp(final Activity classThis)
    {
		DialogInterface.OnClickListener okOnClicked = new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton)
			{
				classThis.finish();
			}
		};
		CommonFunctions.ShowDialogWithView(classThis
				, R.drawable.dialog_icon
				, R.string.game_end
				, 24
				, R.string.yes
				, okOnClicked
				, R.string.no
				, null);
    }

    /**
     * AlertDialog表示用のViewを作る
     * テキストサイズ指定
     */
    public static View MakeAlertDialogView(Context context, int dialogIconId, int messageTextId, int textSize)
    {
        LinearLayout lltAlertDialog = new LinearLayout(context);
        lltAlertDialog.setOrientation(LinearLayout.VERTICAL);

        lltAlertDialog.setGravity(Gravity.CENTER);
        lltAlertDialog.setBackgroundResource(R.drawable.skin_paper);

        ImageView imvAlertIcon = new ImageView(context);
        imvAlertIcon.setImageResource(dialogIconId);
        LayoutParams lp2 = new LayoutParams((int)(CommonInfo.SCREEN_WIDTH * 0.4), (int)(CommonInfo.SCREEN_HEIGHT * 0.35));
        lltAlertDialog.addView(imvAlertIcon, lp2);

        TextView txvDialogBattlePause = new TextView(context);
        txvDialogBattlePause.setText(messageTextId);
        txvDialogBattlePause.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        txvDialogBattlePause.setTextColor(Color.BLUE);
        txvDialogBattlePause.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        txvDialogBattlePause.setGravity(Gravity.LEFT);
        txvDialogBattlePause.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        LayoutParams lp = txvDialogBattlePause.getLayoutParams();
        MarginLayoutParams mlp = (MarginLayoutParams)lp;
        mlp.setMargins(30, 0, 30, 50);
        //マージンを設定
        txvDialogBattlePause.setLayoutParams(mlp);
        lltAlertDialog.addView(txvDialogBattlePause);

        return lltAlertDialog;
    }

	public static void ShowDialogWithView(Context context
			, int dialogIconId
			, int message
			, int textSize
			, int intPositiveButton, DialogInterface.OnClickListener lPositive
			, int intNegativeButton, DialogInterface.OnClickListener lNegative)
	{
		Dialog dialog = new AlertDialog.Builder(context)
				.setView(MakeAlertDialogView(context, dialogIconId, message, textSize))
				//.setTitle(title)
				//.setMessage(message)
				.setPositiveButton(intPositiveButton, lPositive)
				.setNegativeButton(intNegativeButton, lNegative)
				.create();
		dialog.show();
	}

	/**
	 * ユーザの設定情報を更新する
	 * @param value
	 * @param name
	 */
	public static void UpdateUserSettingInfo(String name, String value)
	{
		String sql = "";
		if(GetUserSettingInfo(name).equals(""))
		{
			sql += " Insert Into tb_user_setting(name, value) ";
			sql += " Values('" + name + "', '" + value + "')";
		}
		else
		{
			sql += " Update tb_user_setting ";
			sql += " set value = '" + value + "'";
			sql += " where name = '" + name + "' ";
		}

		DB.Execute(sql);
	}

	/**
	 * ユーザのすべてのステータス情報を取得する関数
	 */
	public static Map<String,String> GetUserStatus()
	{
		Map<String,String> mapUserStatus = new HashMap<String,String>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DB.DB_NAME, null);
		Cursor csrSettingInfo = null;
		csrSettingInfo = DB.GetDataFromTable(db, "tb_user_status", "name, value", csrSettingInfo);

		if(null != csrSettingInfo)
		{
			while(csrSettingInfo.moveToNext())
			{
				mapUserStatus.put(csrSettingInfo.getString(0), csrSettingInfo.getString(1));
			}
		}
		csrSettingInfo.close();
		db.close();

		return mapUserStatus;
	}

	/**
	 * ユーザの特定のステータス情報を取得する関数
	 * nameとなる項目
	 * level : ユーザの現在のレベル
	 * selected_level: レベル選択画面で選んだレベル。このレベルに基づきゲームをプレイ
	 */
	public static String GetUserStatus(String name)
	{
		Map<String,String> mapUserStatus = GetUserStatus();
		if(mapUserStatus != null && mapUserStatus.containsKey(name))
		{
			return mapUserStatus.get(name);
		}
		else
		{
			return "";
		}
	}

	/**
	 * 全体のステータス情報を更新する
	 * @param value
	 * @param name
	 */
	public static void SaveUserStatusToDB(String name, String value)
	{
		String sql = "";
		if(GetUserStatus(name).equals(""))
		{
			sql += " Insert Into tb_user_status(name, value) ";
			sql += " Values('" + name + "', '" + value + "')";
		}
		else
		{
			sql += " Update tb_user_status ";
			sql += " set value = '" + value + "'";
			sql += " where name = '" + name + "' ";
		}

		DB.Execute(sql);
	}

	/**
	 * 全体のステータス情報を更新する
	 * @param value
	 * @param name
	 */
	public static void SaveUserStatusToDB(String name, int value)
	{
		SaveUserStatusToDB(name, value + "");
	}
}
