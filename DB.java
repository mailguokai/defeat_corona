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
package plus.koubou.defeat_corona.common;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import plus.koubou.defeat_corona.R;

public class DB {
	// ↓↓↓↓↓DBのバージョンアップの際は、ここの番号だけを変更↓↓↓↓↓
	public  static final int DB_VERSION = 1; // DBのバージョン
	public  static final int R_DB_FILE_ID = R.raw.defeat_corona_1;
	// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
	public static final String DB_NAME_BASE = "defeat_corona_";
	public static final String DB_PATH = "data/data/plus.koubou.defeat_corona/databases/";
	public static final String DB_NAME = DB_PATH + DB_NAME_BASE + DB_VERSION + ".db";
	//public static final String DB_RESULT_NULL_VALUE = "DB_RESULT_NULL_VALUE";

	public void Insert(Context context,String table, ContentValues values){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		try{
			db.insert(table, null, values);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
		}
	
	public void Update(Context context,String table, ContentValues values, String whereClause, String[] whereArgs){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		try{
			db.update(table, values, whereClause, whereArgs); 
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public Cursor Query(Context context,String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		Cursor cursor = null ;
		try{
			cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
		return cursor;
		
	}
	
	public void Delete(Context context,String table, String whereClause, String[] whereArgs){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		try{
			db.delete(table, whereClause, whereArgs);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public void DeleteTable(Context context,String table){

		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		String sql="drop table " + table;
		try{
		db.execSQL(sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
		db.close();
	}
	
	public static Cursor CsrDBSelect(String sql){
		Cursor cursor = null ;
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		try{
			cursor=db.rawQuery(sql, null);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		//db.close();
		return cursor;
	}
	
	public static void Execute(String sql){
		SQLiteDatabase db=SQLiteDatabase.openDatabase( 
				DB_NAME, null, SQLiteDatabase.OPEN_READWRITE); 
		try{
			db.execSQL(sql);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public static String GetSingleDataFromTable(String strTableName, String strColumName, String strWhere)
	{
		Cursor csrTableData = null;
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DB.DB_NAME, null);
		try {
			csrTableData = db
					.rawQuery(
							"SELECT " + strColumName + " FROM " + strTableName + " WHERE " + strWhere,
							null);
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		if (null != csrTableData && csrTableData.moveToFirst()) {
			String strReturnValue = csrTableData.getString(0);
        	csrTableData.close();
        	db.close();	
        	return strReturnValue;
        }else{
        	csrTableData.close();
        	db.close();	
        	return null;
        }
	}
	
	public static Cursor GetDataFromTable(SQLiteDatabase db, String strTableName, String strColumName, String strWhere, String strOrderBy, Cursor csrOut)
	{
		//Cursor csrTableData = null;
		String strSQL = "SELECT " + strColumName + " FROM " + strTableName;
		if(!strWhere.equals(""))
		{
			strSQL += " WHERE " + strWhere;
		}
		if(!strOrderBy.equals(""))
		{
			strSQL += " ORDER BY " + strOrderBy;
		}
		try {
			csrOut = db.rawQuery(strSQL,null);
		} catch (Exception e) {
			e.getStackTrace();
		}

		return csrOut;
	}
	
	public static Cursor GetDataFromTable(SQLiteDatabase db, String strTableName, String strColumName, String strWhere, Cursor csrOut)
	{
		return GetDataFromTable(db, strTableName, strColumName, strWhere, "", csrOut);
	}
	
	public static Cursor GetDataFromTable(SQLiteDatabase db, String strTableName, String strColumName, Cursor csrOut)
	{
		return GetDataFromTable(db, strTableName, strColumName, "", csrOut);
	}
	
	public static void CheckAndCreateDB(InputStream is)
	{
		// DBファイルの読み込み
		File dir = new File(DB.DB_PATH);
		if (!dir.exists())
			dir.mkdir();

		if (!(new File(DB.DB_NAME)).exists()) {

			FileOutputStream fos;
			try {
				fos = new FileOutputStream(DB.DB_NAME);

				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}

