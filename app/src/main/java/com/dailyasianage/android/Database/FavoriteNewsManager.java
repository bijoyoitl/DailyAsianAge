package com.dailyasianage.android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dailyasianage.android.item.NewsAll;

import java.util.ArrayList;

/**
 * Created by ripon on 3/2/2017.
 */

public class FavoriteNewsManager {

    private Context context;
    private NewsDatabase newsDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public FavoriteNewsManager(Context context) {
        this.context = context;
        newsDatabase = new NewsDatabase(context);
    }

    //Add favorite id into favorite table
    public void addFavorite(String favId) {

        ContentValues values = new ContentValues();
        values.put(NewsDatabase.FAVORITE_ID, favId);
        try {
            database = newsDatabase.getWritableDatabase();
            database.insert(NewsDatabase.FAVORITE_TABLE_BANE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

    }


    public ArrayList<NewsAll> getFav() {

        ArrayList<NewsAll> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<NewsAll>();
            database = newsDatabase.getReadableDatabase();

            String QUERY = "SELECT * FROM " + NewsDatabase.NEWS_TABLE_NAME + " where " + NewsDatabase.NEWS_ID + " in(SELECT " + NewsDatabase.FAVORITE_ID + " FROM " + NewsDatabase.FAVORITE_TABLE_BANE + ")";
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    NewsAll news1 = new NewsAll();
                    news1.setCatId(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_CATEGORY_ID)));
                    news1.setId(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_ID)));
                    news1.setHeading(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_HEADING)));
                    news1.setSubHeading(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_SUB_HEADING)));
                    news1.setDetails(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_DETAILS)));
                    news1.setShoulder(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_SHOULDER)));
                    news1.setPublishTime(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_PUBLISH_TIME)));
                    news1.setReporter(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_REPORTER)));
                    news1.setImage(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_IMAGE)));
                    newsArrayList.add(news1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return newsArrayList;
    }

    public String getFavIds() {
        String favIds = "";
        try {
            database = newsDatabase.getReadableDatabase();

            String QUERY = "SELECT " + NewsDatabase.FAVORITE_ID + " FROM " + NewsDatabase.FAVORITE_TABLE_BANE;

            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(NewsDatabase.FAVORITE_ID));

                    if (favIds.equals("")) {
                        favIds += id;
                    } else {
                        favIds += "," + id;
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return favIds;
    }


    //cheek favorite news exits or not into favorite table
    public String getFavExit(String fav_id) {
        int num = 0;
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT * FROM " + NewsDatabase.FAVORITE_TABLE_BANE + " where " + NewsDatabase.FAVORITE_ID + " =" + fav_id;

            cursor = database.rawQuery(QUERY, null);
//            Log.e("NewsDatabase.java", "q 2= " + QUERY);
            num = cursor.getCount();
//           databaseRead.close();
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

    //Delete favorite id from favorite table
    public void deleteFavid(String id) {

        try {
            database = newsDatabase.getWritableDatabase();
            database.delete(NewsDatabase.FAVORITE_TABLE_BANE, NewsDatabase.FAVORITE_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

}
