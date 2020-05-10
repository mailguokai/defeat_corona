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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import java.util.Random;
import java.util.Vector;

public class PenguinSprite extends Sprite
{
  public final static int STATE_TURN_LEFT = 0;
  public final static int STATE_TURN_RIGHT = 1;
  public final static int STATE_FIRE = 2;
  public final static int STATE_VOID = 3;
  public final static int STATE_GAME_WON = 4;
  public final static int STATE_GAME_LOST = 5;

  public final static int[][] LOST_SEQUENCE =
      {{1,0}, {2,8}, {3,9}, {4,10}, {5,11}, {6,12}, {7,13}, {5,14}};
  public final static int[][] WON_SEQUENCE =
      {{1,0}, {2,7}, {3,6}, {4,15}, {5,16}, {6,17}, {7,18}, {4,19}};

  private BmpWrap spritesImage;

  private int currentPenguin;

  private int count;

  private Random rand;

  private int finalState;
  private int nextPosition;

  public PenguinSprite(BmpWrap sprites, Random rand)
  {
    super(new Rect(361, 436, 361 + 55, 436 + 43));

    this.spritesImage = sprites;
    this.rand = rand;

    currentPenguin = 0;

    finalState = STATE_VOID;
    nextPosition = 0;
  }

  public PenguinSprite(BmpWrap sprites, Random rand,
                       int currentPenguin, int count,
                       int finalState, int nextPosition)
  {
    super(new Rect(361, 436, 361 + 55, 436 + 43));

    this.spritesImage = sprites;
    this.rand = rand;
    this.currentPenguin = currentPenguin;
    this.count = count;
    this.finalState = finalState;
    this.nextPosition = nextPosition;
  }

  public void saveState(Bundle map, Vector saved_sprites) {
    if (getSavedId() != -1) {
      return;
    }
    super.saveState(map, saved_sprites);
    map.putInt(String.format("%d-currentPenguin", getSavedId()),
               currentPenguin);
    map.putInt(String.format("%d-count", getSavedId()), count);
    map.putInt(String.format("%d-finalState", getSavedId()), finalState);
    map.putInt(String.format("%d-nextPosition", getSavedId()), nextPosition);
  }

  public int getTypeId()
  {
    return Sprite.TYPE_PENGUIN;
  }

  public void updateState(int state)
  {
    if (finalState != STATE_VOID) {
      count++;

      if (count % 6 == 0) {
        if (finalState == STATE_GAME_LOST) {
          currentPenguin = LOST_SEQUENCE[nextPosition][1];
          nextPosition = LOST_SEQUENCE[nextPosition][0];
        } else if (finalState == STATE_GAME_WON) {
          currentPenguin = WON_SEQUENCE[nextPosition][1];
          nextPosition = WON_SEQUENCE[nextPosition][0];
        }
      }
    } else {
      count++;

      switch(state) {
      case STATE_TURN_LEFT :
        count = 0;
        currentPenguin = 3;
        break;
      case STATE_TURN_RIGHT :
        count = 0;
        currentPenguin = 2;
        break;
      case STATE_FIRE :
        count = 0;
        currentPenguin = 1;
        break;
      case STATE_VOID :
        if (currentPenguin<4 || currentPenguin>7) {
          currentPenguin = 0;
        }
        break;
      case STATE_GAME_WON :
      case STATE_GAME_LOST :
        count = 0;
        finalState = state;
        currentPenguin = 0;
        return;
      }

      if (count>100) {
        currentPenguin = 7;
      } else if (count % 15 == 0 && count>25) {
        currentPenguin = (rand.nextInt() % 3)+4;
        if (currentPenguin < 4) {
          currentPenguin = 0;
        }
      }
    }
  }

  public void paint(Canvas c, double scale, int dx, int dy)
  {
    Rect r = this.getSpriteArea();
    drawImageClipped(spritesImage,
                     360 - (currentPenguin % 4) * 57,
                     435 - (currentPenguin / 4) * 45,
                     r, c, scale, dx, dy);
  }
}
