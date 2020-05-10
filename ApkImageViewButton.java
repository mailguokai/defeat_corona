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

import android.annotation.SuppressLint;
import android.content.Context;
import plus.koubou.defeat_corona.common.Sounds;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ApkImageViewButton extends ImageView implements OnClickListener, OnTouchListener {
	
	public int ImageIdClicked = 0;
	private int ImageId;
	@SuppressLint("ClickableViewAccessibility")
	public ApkImageViewButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// xmlで指定したResourceIdを取得
		ImageId = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "src").replace("@", ""));
		setSoundEffectsEnabled(false);
	    //this.setOnClickListener(this);
	    this.setOnTouchListener(this);
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouch(View v, MotionEvent event) {
		if(ImageIdClicked != 0)
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				((ImageView)v).setImageResource(ImageIdClicked);
			}
			if(event.getAction() == MotionEvent.ACTION_UP)
			{
				((ImageView)v).setImageResource(ImageId);
			}
		}
		
		return false;
	}
	
	@Override
	public void onClick(View v) {
		// クリック音を設定
		Sounds.playTouch();
	}

	public int getImageId()
	{
		return ImageId;
	}

    public void setImageId(int image_id)
    {
		ImageId = image_id;
        ((ImageView)this).setImageResource(ImageId);
    }
}
