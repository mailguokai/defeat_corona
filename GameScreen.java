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

import java.util.Vector;
import android.graphics.Canvas;
import android.os.Bundle;

public abstract class GameScreen
{
  private Vector sprites;

  public final void saveSprites(Bundle map, Vector savedSprites)
  {
    for (int i = 0; i < sprites.size(); i++) {
      ((Sprite)sprites.elementAt(i)).saveState(map, savedSprites);
      map.putInt(String.format("game-%d", i),
                 ((Sprite)sprites.elementAt(i)).getSavedId());
    }
    map.putInt("numGameSprites", sprites.size());
  }

  public final void restoreSprites(Bundle map, Vector savedSprites)
  {
    sprites = new Vector();
    int numSprites = map.getInt("numGameSprites");
    for (int i = 0; i < numSprites; i++) {
      int spriteIdx = map.getInt(String.format("game-%d", i));
      sprites.addElement(savedSprites.elementAt(spriteIdx));
    }
  }

  public GameScreen()
  {
    sprites = new Vector();
  }

  public final void addSprite(Sprite sprite)
  {
    sprites.removeElement(sprite);
    sprites.addElement(sprite);
  }

  public final void removeSprite(Sprite sprite)
  {
    sprites.removeElement(sprite);
  }

  public final void spriteToBack(Sprite sprite)
  {
    sprites.removeElement(sprite);
    sprites.insertElementAt(sprite,0);
  }

  public final void spriteToFront(Sprite sprite)
  {
    sprites.removeElement(sprite);
    sprites.addElement(sprite);
  }

  public void paint(Canvas c, double scale, int dx, int dy) {
    for (int i = 0; i < sprites.size(); i++) {
      ((Sprite)sprites.elementAt(i)).paint(c, scale, dx, dy);
    }
  }

  public abstract boolean play(boolean key_left, boolean key_right,
                               boolean key_fire, double trackball_dx,
                               boolean touch_fire,
                               double touch_x, double touch_y,
                               boolean ats_touch_fire, double ats_touch_dx);
}
