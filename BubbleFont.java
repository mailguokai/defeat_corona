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

public class BubbleFont {
  private char[] characters = {
    '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*',
    '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
    '5', '6', '7', '8', '9', ':', ';', '<', '=', '>',
    '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
    's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '{',
    '}', '[', ']', ' ', '\\', ' ', ' '};

  private int[] position  = {
    0, 9, 16, 31, 39, 54, 69, 73, 80, 88, 96, 116, 121, 131,
    137, 154, 165, 175, 187, 198, 210, 223, 234, 246, 259,
    271, 276, 282, 293, 313, 324, 336, 351, 360, 370, 381,
    390, 402, 411, 421, 435, 446, 459, 472, 483, 495, 508,
    517, 527, 538, 552, 565, 578, 589, 602, 616, 631, 645,
    663, 684, 700, 716, 732, 748, 764, 780, 796, 812 };

  public int SEPARATOR_WIDTH = 1;
  public int SPACE_CHAR_WIDTH = 6;

  private BmpWrap fontMap;
  private Rect clipRect;

  public BubbleFont(BmpWrap fontMap)
  {
    this.fontMap = fontMap;
    clipRect = new Rect();
  }

  public final void print(String s, int x, int y, Canvas canvas,
                         double scale, int dx, int dy)
  {
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      x += paintChar(c, x, y, canvas, scale, dx, dy);
    }
  }

  public final int paintChar(char c, int x, int y, Canvas canvas,
                             double scale, int dx, int dy)
  {
    if (c == ' ') {
      return SPACE_CHAR_WIDTH + SEPARATOR_WIDTH;
    }
    int index = getCharIndex(c);
    if (index == -1) {
      return 0;
    }
    int imageWidth = position[index+1]-position[index];

    clipRect.left = x;
    clipRect.right = x + imageWidth;
    clipRect.top = y;
    clipRect.bottom = y + 22;
    Sprite.drawImageClipped(fontMap, x - position[index], y, clipRect,
                            canvas, scale, dx, dy);

    return imageWidth + SEPARATOR_WIDTH;
  }

  private final int getCharIndex(char c)
  {
    for (int i=0 ; i<characters.length ; i++) {
      if (characters[i] == c) {
        return i;
      }
    }

    return -1;
  }
}
