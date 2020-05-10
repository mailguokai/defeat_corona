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

import plus.koubou.defeat_corona.R;
import java.io.File;
import plus.koubou.defeat_corona.common.CommonFunctions;
import plus.koubou.defeat_corona.common.CommonInfo;
import plus.koubou.defeat_corona.common.DB;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Loading extends Activity {
    // 起動画面
    private static final long WAITING_TIME = 3000;
    private ImageView imvLogoTitle;
    private LoadingView ldvLoadingBar;
    private TextView tvwLoading;
    Thread thdMoguraTataki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.loading);

        // DBが存在しない場合は作成する
        Resources res = getApplicationContext().getResources();
        DB.CheckAndCreateDB(res.openRawResource(DB.R_DB_FILE_ID));

        imvLogoTitle = (ImageView) findViewById(R.id.imvLogoTitle);
        ldvLoadingBar = (LoadingView) findViewById(R.id.ldvLoadingBar);
        tvwLoading = (TextView) findViewById(R.id.tvwLoading);
        initLoadingImages();

        // 最初に画面のサイズを獲得。アプリ全体で使う。
        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplicationContext().getResources().getDisplayMetrics();
        CommonInfo.SetImages();
        CommonInfo.SCALED_DENSITY = dm.scaledDensity;
        CommonInfo.SCREEN_HEIGHT = dm.heightPixels;
        CommonInfo.SCREEN_WIDTH = dm.widthPixels;

        // *************************xlr test begin*************************//
        SharedPreferences pref = getSharedPreferences(getResources().getString(R.string.package_name) + "_preferences", MODE_PRIVATE);
        Editor e = pref.edit();
        e.putFloat("SCREEN_HEIGHT", CommonInfo.SCREEN_HEIGHT);
        e.putFloat("SCREEN_HEIGHT", CommonInfo.SCREEN_WIDTH);
        e.commit();
        // *************************xlr test end*************************//

        CommonInfo.BASIC_CONTEXT = getBaseContext();

        // Logo画像のサイズと位置の設定
        CommonFunctions.LocateView(imvLogoTitle, 0.25, 0.85, 0.075, 0.2, 0, 0);

        //Loading Barの位置を設定
        CommonFunctions.LocateView(ldvLoadingBar, 0.08, 0.8, 0.1, 0.05);
        CommonFunctions.LocateView(tvwLoading, 0.4, 0.75, 0, 0);

        thdMoguraTataki = new Thread() {
            public void run() {
                ldvLoadingBar.startAnim(5);

                try {
                    int intExistDBVer = IsUpgradeDB();
                    if (intExistDBVer > 0 && intExistDBVer < DB.DB_VERSION) // DBの更新作業が必要になった場合
                    {
                        //new Hander 「データベースアップグレード中...」を表示 //http://andante.in/i/java/activity%E3%81%A8thread%E3%81%AB%E3%81%A4%E3%81%84%E3%81%A6/
                        //更新開始時刻を取る
                        long tStart = System.currentTimeMillis();

                        //Upgrade DB  中断防止対策：最後に古いＤＢを消す。残っている場合に次回続けてこの処理を行う。
                        UpDateNewDBInfo(intExistDBVer);

                        // DBファイルを削除
                        DeleteDBFiles(intExistDBVer);

                        //時刻を取る
                        long intTimeSpan = System.currentTimeMillis() - tStart;
                        if (intTimeSpan < WAITING_TIME) {
                            Thread.sleep(WAITING_TIME - intTimeSpan);
                        }
                    } else {
                        Thread.sleep(WAITING_TIME);
                    }
                    Message m = new Message();
                    m.what = 1;
                    Loading.this.mHandler.sendMessage(m);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        thdMoguraTataki.start();

    }

    private int IsUpgradeDB() {
        int retValue = 0;
        String strDbName = DB.DB_NAME_BASE;
        for (int i = DB.DB_VERSION - 1; i > 0; i--) {
            if ((new File(DB.DB_PATH + strDbName + i + ".db")).exists()) {
                retValue = i;
            }
        }
        return retValue;
    }

    // intDbVer以前のバージョンのＤＢファイルを削除する
    private void DeleteDBFiles(int intDbVer) {
        String strDbName = DB.DB_NAME_BASE;
        try {
            for (int i = intDbVer; i > 0; i--) {
                String strDbFile = DB.DB_PATH + strDbName + i + ".db";
                File file = new File(strDbFile);
                file.delete();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // Threadを停止させる
        if (thdMoguraTataki != null) {
            thdMoguraTataki.interrupt();
            thdMoguraTataki = null;
        }
        super.onDestroy();
    }

    //
    private void UpDateNewDBInfo(int intDbVer) {
        // アップデート前のDBを取得
        String strDbFile = DB.DB_PATH + DB.DB_NAME_BASE + intDbVer + ".db";
        SQLiteDatabase dbPre = SQLiteDatabase.openOrCreateDatabase(strDbFile, null);

        if (intDbVer <= 2)
        {
            // DBの更新処理
            // tb_questioning_historyのデータ移行
            Cursor csrUserData = null;

            // tb_user_settingのデータ移行
            try {
                csrUserData = dbPre.rawQuery("SELECT name, value FROM tb_user_setting ",
                        null);
                if (null != csrUserData && csrUserData.moveToFirst()) {
                    String sql = "";
                    do {
                        sql = " UPDATE tb_user_setting ";
                        sql += " SET value = '" + csrUserData.getString(1) + "' ";
                        sql += " WHERE name = '" + csrUserData.getString(0) + "' ";

                        DB.Execute(sql);
                    } while (csrUserData.moveToNext());
                }
            } catch (Exception e) {
                e.getStackTrace();
            }

            // tb_user_statusのデータ移行
            csrUserData = null;
            try {
                csrUserData = dbPre.rawQuery("SELECT name, value FROM tb_user_status ",
                        null);
                if (null != csrUserData && csrUserData.moveToFirst()) {
                    String sql = "";
                    do {
                        sql = " UPDATE tb_user_status ";
                        sql += " SET value = '" + csrUserData.getString(1) + "' ";
                        sql += " WHERE name = '" + csrUserData.getString(0) + "' ";

                        DB.Execute(sql);
                    } while (csrUserData.moveToNext());
                }
            } catch (Exception e) {
                e.getStackTrace();
            }

            // 最後に閉じる
            csrUserData.close();
        }
    }

    private void initLoadingImages() {
        int[] imageIds;
        // progress bar
        imageIds = new int[6];
        imageIds[0] = R.drawable.loader_frame_1;
        imageIds[1] = R.drawable.loader_frame_2;
        imageIds[2] = R.drawable.loader_frame_3;
        imageIds[3] = R.drawable.loader_frame_4;
        imageIds[4] = R.drawable.loader_frame_5;
        imageIds[5] = R.drawable.loader_frame_6;
        ldvLoadingBar.setImageIds(imageIds);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                // MainMenuを起動
                CommonFunctions.Transition(Loading.this, MainMenu.class);
            }
        }
    };
}
