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

import android.os.Bundle;
import java.util.Vector;

public class LevelManager
{
        private int currentLevel;
        private Vector levelList;

        public void saveState(Bundle map)
        {
                map.putInt("LevelManager-currentLevel", currentLevel);
        }

        public void restoreState(Bundle map)
        {
                currentLevel = map.getInt("LevelManager-currentLevel");
        }

        public LevelManager(byte[] levels, int startingLevel)
        {
                String allLevels = new String(levels);

                currentLevel = startingLevel;
                levelList = new Vector();

                int nextLevel = allLevels.indexOf("\n\n");
                if (nextLevel == -1 && allLevels.trim().length() != 0)
                {
                        nextLevel = allLevels.length();
                }

                while (nextLevel != -1)
                {
                        String currentLevel = allLevels.substring(0, nextLevel).trim();

                        levelList.addElement(getLevel(currentLevel));

                        allLevels = allLevels.substring(nextLevel).trim();

                        if (allLevels.length() == 0)
                        {
                                nextLevel = -1;
                        }
                        else
                        {
                                nextLevel = allLevels.indexOf("\n\n");

                                if (nextLevel == -1)
                                {
                                        nextLevel = allLevels.length();
                                }
                        }
                }

                if (currentLevel >= levelList.size())
                {
                        currentLevel = 0;
                }
        }

        private byte[][] getLevel(String data)
        {
                byte[][] temp = new byte[8][12];

                for (int j=0 ; j<12 ; j++)
                {
                        for (int i=0 ; i<8 ; i++)
                        {
                                temp[i][j] = -1;
                        }
                }

                int tempX = 0;
                int tempY = 0;

                for (int i=0 ; i<data.length() ; i++)
                {
                        if (data.charAt(i) >= 48 && data.charAt(i) <= 55)
                        {
                                temp[tempX][tempY] = (byte)(data.charAt(i) - 48);
                                tempX++;
                        }
                        else if (data.charAt(i) == 45)
                        {
                                temp[tempX][tempY] = -1;
                                tempX++;
                        }

                        if (tempX == 8)
                        {
                                tempY++;

                                if (tempY == 12)
                                {
                                        return temp;
                                }

                                tempX = tempY % 2;
                        }
                }

                return temp;
        }

        public byte[][] getCurrentLevel()
        {
                if (currentLevel < levelList.size())
                {
                        return (byte[][])levelList.elementAt(currentLevel);
                }

                return null;
        }

        public void goToNextLevel()
        {
                currentLevel++;
                if (currentLevel >= levelList.size()) {
                  currentLevel = 0;
                }
        }

        public void goToFirstLevel()
        {
                currentLevel = 0;
        }

        /**
         * 0 - 99
         * @return
         */
        public int getLevelIndex()
        {
                return currentLevel;
        }
}
