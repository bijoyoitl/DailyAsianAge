package com.dailyasianage.android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ripon on 3/2/2017.
 */

public class CategoryManager {

    private Context context;
    private NewsDatabase newsDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public CategoryManager(Context context) {
        this.context = context;
        newsDatabase = new NewsDatabase(context);
    }


    // add category id and category`s news id into category table
    public void addCatNews(String catid, String catstring) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsDatabase.CATEGORY_ID, catid);
        contentValues.put(NewsDatabase.CATEGORY_NEWS_ID, catstring);
        try {
            database = newsDatabase.getWritableDatabase();
            database.delete(NewsDatabase.CATEGORY_TABLE_NAME, NewsDatabase.CATEGORY_ID + "=?", new String[]{catid});

            database.insert(NewsDatabase.CATEGORY_TABLE_NAME, null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //get cat news ids from category table
    public String getCatNewsId(String catIds) {
        String catNewsIds = "";
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT " + NewsDatabase.CATEGORY_NEWS_ID + " FROM " + NewsDatabase.CATEGORY_TABLE_NAME + " WHERE " + NewsDatabase.CATEGORY_ID + " in(" + catIds + ")";

            Log.e("CA", "news query : " + QUERY);

            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    String nesIds = cursor.getString(cursor.getColumnIndex(NewsDatabase.CATEGORY_NEWS_ID));

                    if (catNewsIds.equals("")) {
                        catNewsIds += nesIds;
                    } else {
                        catNewsIds += "," + nesIds;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

        Log.e("CM ", " cat n : " + catNewsIds);

        return catNewsIds;
    }

    //get cat news ids from category table
    public String getCatNews(String catid) {
        String catnews = "";
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT * FROM " + NewsDatabase.CATEGORY_TABLE_NAME + " where " + NewsDatabase.CATEGORY_ID + "=" + catid;
            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    catnews = cursor.getString(cursor.getColumnIndex(NewsDatabase.CATEGORY_NEWS_ID));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
        if (catnews == "") {
            catnews = "[]";
        }
//        Log.e(TAG,"cat n"+ catnews);
        return "{ \"status\":1,\"msg\":\"\", \"nid\":" + catnews + "}";
    }

    //clean category from category table
    public void cleanCatByID(String cat_id) {
        database = newsDatabase.getWritableDatabase();
        try {
            database.delete(NewsDatabase.CATEGORY_TABLE_NAME, NewsDatabase.CATEGORY_ID + "=?", new String[]{cat_id});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //cheek category news exits or not into category table
    public String getCatExist(String catid) {

        int num = 0;
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT * FROM " + NewsDatabase.CATEGORY_TABLE_NAME + " where " + NewsDatabase.CATEGORY_ID + " =" + catid;
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            if (num > 0) {
                return "1";
            } else {
                return "0";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return "0";

    }
}
