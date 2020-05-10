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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class LoadingView extends ApkImageViewButton implements Runnable
{
	private boolean _isStop = false;
	private int[] imageIds;
	private int index = 0;
	private int length = 1;
	private int speedRate = 100;
	private int speed = 5;
	private  boolean isRunOnce = false;
	
	public LoadingView(Context context)
	{
		this(context, null);
	}

	public LoadingView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public void setImageIds(int[] imageId)
	{
		this.imageIds = imageId;
		if(imageIds != null && imageIds.length > 0)
		{
			length = imageIds.length;
		}
	}
	
		@Override
	protected void onDetachedFromWindow()
	{
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		_isStop = true;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(imageIds != null && imageIds.length > 0)
		{
			this.setImageResource(imageIds[index]);
		}
	}

	public void run()
	{
		while(!_isStop)
		{
			index = ++index % length;
			postInvalidate();
			try
			{
				Thread.sleep(speed * speedRate);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if(isRunOnce && index >= length-1)
			{
				_isStop = true;
			}
		}
	}
	
	public void startAnim(int _speed)
	{
		speed = _speed;
		new Thread(this).start();
	}

	/**
	 * 一回のみ再生可能
	 * @param _speed
	 * @param isOnce
	 */
	public void startAnim(int _speed, boolean isOnce)
	{
		isRunOnce = isOnce;
		startAnim(_speed);
	}

	public  boolean isStop()
	{
		return _isStop;
	}
}
