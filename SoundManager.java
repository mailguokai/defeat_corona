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

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager
{
  private SoundPool soundPool;
  private int[] sm;
  Context context;

  public SoundManager(Context context) {
    this.context = context;
    soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
    sm = new int[FrozenBubble.NUM_SOUNDS];
    sm[FrozenBubble.SOUND_WON] = soundPool.load(context, R.raw.applause, 1);
    sm[FrozenBubble.SOUND_LOST] = soundPool.load(context, R.raw.lose, 1);
    sm[FrozenBubble.SOUND_LAUNCH] = soundPool.load(context, R.raw.launch, 1);
    sm[FrozenBubble.SOUND_DESTROY] =
        soundPool.load(context, R.raw.destroy_group, 1);
    sm[FrozenBubble.SOUND_REBOUND] =
        soundPool.load(context, R.raw.rebound, 1);
    sm[FrozenBubble.SOUND_STICK] = soundPool.load(context, R.raw.stick, 1);
    sm[FrozenBubble.SOUND_HURRY] = soundPool.load(context, R.raw.hurry, 1);
    sm[FrozenBubble.SOUND_NEWROOT] =
        soundPool.load(context, R.raw.newroot_solo, 1);
    sm[FrozenBubble.SOUND_NOH] = soundPool.load(context, R.raw.noh, 1);
  }

  public final void playSound(int sound) {
    if (FrozenBubble.getSoundOn()) {
      AudioManager mgr = (AudioManager)context.getSystemService(
          Context.AUDIO_SERVICE);  
      float streamVolumeCurrent =
          mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
      float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      float volume = streamVolumeCurrent / streamVolumeMax;
      soundPool.play(sm[sound], volume, volume, 1, 0, 1f);
    }
  }

  public final void cleanUp() {
    sm = null;
    context = null;
    soundPool.release();
    soundPool = null;
  }
}
