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

import java.util.Random;
import android.os.Bundle;

public class BubbleManager
{
        int bubblesLeft;
        BmpWrap[] bubbles;
        int[] countBubbles;

        public BubbleManager(BmpWrap[] bubbles)
        {
                this.bubbles = bubbles;
                this.countBubbles = new int[bubbles.length];
                this.bubblesLeft = 0;
        }

        public void saveState(Bundle map)
        {
                map.putInt("BubbleManager-bubblesLeft", bubblesLeft);
                map.putIntArray("BubbleManager-countBubbles", countBubbles);
        }

        public void restoreState(Bundle map)
        {
                bubblesLeft = map.getInt("BubbleManager-bubblesLeft");
                countBubbles = map.getIntArray("BubbleManager-countBubbles");
        }

        public void addBubble(BmpWrap bubble)
        {
                countBubbles[findBubble(bubble)]++;
                bubblesLeft++;
        }

        public void removeBubble(BmpWrap bubble)
        {
                countBubbles[findBubble(bubble)]--;
                bubblesLeft--;
        }

        public int countBubbles()
        {
                return bubblesLeft;
        }

        public int nextBubbleIndex(Random rand)
        {
                int select = rand.nextInt() % bubbles.length;

                if (select < 0)
                {
                        select = -select;
                }

                int count = -1;
                int position = -1;

                while (count != select)
                {
                        position++;

                        if (position == bubbles.length)
                        {
                                position = 0;
                        }

                        if (countBubbles[position] != 0)
                        {
                                count++;
                        }
                }

                return position;
        }

        public BmpWrap nextBubble(Random rand)
        {
                return bubbles[nextBubbleIndex(rand)];
        }

        private int findBubble(BmpWrap bubble)
        {
                for (int i=0 ; i<bubbles.length ; i++)
                {
                        if (bubbles[i] == bubble)
                        {
                                return i;
                        }
                }

                return -1;
        }
}
