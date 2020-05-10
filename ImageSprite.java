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
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import java.util.Vector;

public class ImageSprite extends Sprite
{
  private BmpWrap displayedImage;

  public ImageSprite(Rect area, BmpWrap img)
  {
    super(area);

    this.displayedImage = img;
  }

  public void saveState(Bundle map, Vector savedSprites) {
    if (getSavedId() != -1) {
      return;
    }
    super.saveState(map, savedSprites);
    map.putInt(String.format("%d-imageId", getSavedId()), displayedImage.id);
  }

  public int getTypeId()
  {
    return Sprite.TYPE_IMAGE;
  }

  public void changeImage(BmpWrap img)
  {
    this.displayedImage = img;
  }

  public final void paint(Canvas c, double scale, int dx, int dy)
  {
    Point p = super.getSpritePosition();
    drawImage(displayedImage, p.x, p.y, c, scale, dx, dy);
  }
}
