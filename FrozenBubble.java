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


package plus.koubou.defeat_corona;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import android.util.Log;

import plus.koubou.defeat_corona.GameView;
import plus.koubou.defeat_corona.GameView.GameThread;
import plus.koubou.defeat_corona.common.CommonFunctions;
import plus.koubou.defeat_corona.common.CommonInfo;
import plus.koubou.defeat_corona.customize.MainMenu;
import plus.koubou.defeat_corona.common.ApkGoogoleAdver;

public class FrozenBubble extends Activity
{
  public final static int SOUND_WON = 0;
  public final static int SOUND_LOST = 1;
  public final static int SOUND_LAUNCH = 2;
  public final static int SOUND_DESTROY = 3;
  public final static int SOUND_REBOUND = 4;
  public final static int SOUND_STICK = 5;
  public final static int SOUND_HURRY = 6;
  public final static int SOUND_NEWROOT = 7;
  public final static int SOUND_NOH = 8;
  public final static int NUM_SOUNDS = 9;

  public final static int GAME_NORMAL = 0;
  public final static String PREFS_NAME = "defeat_corona";

  private static int gameMode = GAME_NORMAL;
  private static boolean dontRushMe = false;
  private static boolean aimThenShoot = false;

  private boolean fullscreen = true;

  private GameThread mGameThread;
  private GameView mGameView;
  
  private static final String EDITORACTION = "plus.koubou.defeat_corona.GAME";
  private boolean activityCustomStarted = false;
  /** The interstitial ad. */
  private static ApkGoogoleAdver apkInterstitialAd;

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    super.onCreateOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    return false;
  }

  private void setFullscreen()
  {
    if (fullscreen) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().clearFlags(
          WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    } else {
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().addFlags(
          WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
    mGameView.requestLayout();
  }

  public synchronized static void setMode(int newMode)
  {
    gameMode = newMode;
  }

  public synchronized static int getMode()
  {
    return gameMode;
  }

  public synchronized static boolean getSoundOn()
  {
    return CommonFunctions.GetUserSettingInfo("sound").equals("1");
  }

  public synchronized static boolean getAimThenShoot()
  {
    return aimThenShoot;
  }

  public synchronized static boolean getDontRushMe()
  {
    return dontRushMe;
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    activityCustomStarted = false;
    setContentView(R.layout.main);
    mGameView = (GameView)findViewById(R.id.game);
    mGameThread = mGameView.getThread();

    if (savedInstanceState != null) {
      mGameThread.restoreState(savedInstanceState);
    }
    mGameView.requestFocus();
    // InterstitialAd
    apkInterstitialAd = new ApkGoogoleAdver(this, CommonInfo.ADVER_TYPE.INTERSTITIALAD, getResources().getString(R.string.txtInterstitialAdsLicense));
    setFullscreen();
  }

  /**
   * Invoked when the Activity loses user focus.
   */
  @Override
  protected void onPause() {
    super.onPause();
    mGameView.getThread().pause();
    // Allow editor functionalities.
    Intent i = getIntent();
    // If I didn't run game from editor, save last played level.
    if (null == i || !activityCustomStarted) {
      SharedPreferences sp = getSharedPreferences(PREFS_NAME,
                                                  Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sp.edit();
      editor.putInt("level", mGameThread.getCurrentLevelIndex());
      editor.commit();
    } else {
      // Editor's intent is running.
      SharedPreferences sp = getSharedPreferences(PREFS_NAME,
                                                  Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sp.edit();
      editor.putInt("levelCustom", mGameThread.getCurrentLevelIndex());
      editor.commit();
    }
  }

  @Override
  protected void onStop() {
    //Log.i("frozen-bubble", "FrozenBubble.onStop()");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    //Log.i("frozen-bubble", "FrozenBubble.onDestroy()");
    super.onDestroy();
    if (mGameView != null) {
      mGameView.cleanUp();
    }
    mGameView = null;
    mGameThread = null;    
  }

  /**
   * Notification that something is about to happen, to give the Activity a
   * chance to save state.
   *
   * @param outState a Bundle into which this Activity should save its state
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    //Log.i("frozen-bubble", "FrozenBubble.onSaveInstanceState()");
    // Just have the View's thread save its state into our Bundle.
    super.onSaveInstanceState(outState);
    mGameThread.saveState(outState);
  }

  /* (non-Javadoc)
   * @see android.app.Activity#onNewIntent(android.content.Intent)
   */
  @Override
  protected void onNewIntent(Intent intent) {
    if (null != intent && EDITORACTION.equals(intent.getAction())) {
      if (!activityCustomStarted) {
        activityCustomStarted = true;

        // Get custom level last played.
        int startingLevel = Integer.parseInt(CommonFunctions.GetUserStatus("selected_level"));
        mGameView = null;
        mGameView = new GameView(
            this, intent.getExtras().getByteArray("levels"), startingLevel);
        setContentView(mGameView);
        mGameThread = mGameView.getThread();
        mGameThread.newGame();
        mGameView.requestFocus();
        setFullscreen();
      }
    }
  }

  // 戻るボタン
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode != KeyEvent.KEYCODE_BACK) {
      return super.onKeyDown(keyCode, event);
    } else {
      ShowInterstitialAd();

      DialogInterface.OnClickListener okOnClicked = new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int whichButton)
        {
          CommonFunctions.Transition(FrozenBubble.this, MainMenu.class, true);
        }
      };
      CommonFunctions.ShowDialogWithView(FrozenBubble.this
              , R.drawable.dialog_icon
              , R.string.battle_pause
              , 24
              , R.string.yes
              , okOnClicked
              , R.string.no
              , null);
      return true;
    }
  }

  public synchronized static void ShowInterstitialAd()
  {
    if(apkInterstitialAd != null)
    {
      apkInterstitialAd.Show();
    }
  }

  public synchronized static void ReloadInterstitialAd()
  {
    if(apkInterstitialAd != null)
    {
      apkInterstitialAd.ReloadInterstitial();
    }
  }

}
