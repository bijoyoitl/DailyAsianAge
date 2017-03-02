package com.dailyasianage.android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dailyasianage.android.item.DbDrawerItem;
import com.dailyasianage.android.item.News;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by optimal on 09-May-16.
 */
public class NewsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.sqlite";
    private static final int DATABASE_VERSION = 1;

    public static final String DRAWER_TABLE_NAME = "drawer_list";
    public static final String DRAWER_ID = "d_id";
    public static final String DRAWER_CAT_ID = "cat_id";
    public static final String DRAWER_CAT_NAME = "cat_name";
    public static final String DRAWER_CAT_IMAGE = "cat_image";
    public static final String DRAWER_CAT_PARENT = "parent";
    public static final String DRAWER_CAT_ORDER = "cat_order";

    public static final String NEWS_TABLE_NAME = "News";
    public static final String NEWS_SERIAL = "serial";
    public static final String NEWS_PUBLISH_SERIAL = "publish_serial";
    public static final String NEWS_CATEGORY_ID = "category_id";
    public static final String NEWS_ID = "id";
    public static final String NEWS_HEADING = "heading";
    public static final String NEWS_SUB_HEADING = "sub_heading";
    public static final String NEWS_SHOULDER = "shoulder";
    public static final String NEWS_PUBLISH_TIME = "publish_time";
    public static final String NEWS_REPORTER = "reporter";
    public static final String NEWS_DETAILS = "details";
    public static final String NEWS_IMAGE = "image";
    public static final String NEWS_TOP_NEWS = "n_top_news";
    public static final String NEWS_HOME_SLIDER = "n_home_slider";
    public static final String NEWS_INSIDE_NEWS = "n_inside_news";


    public static final String CATEGORY_TABLE_NAME = "cat";
    public static final String CATEGORY_ID = "cat_id";
    public static final String CATEGORY_NEWS_ID = "news_id";

    public static final String FAVORITE_TABLE_BANE = "favorite";
    public static final String FAVORITE_ID = "fab_id";

    public static final String HOME_CUSTOMIZE_TABLE_NAME = "home_customize";
    public static final String HOME_CAT_ID = "home_cat_id";

    public static final String REQUEST_TIME_TABLE_NAME = "server_request_time";
    public static final String ID = "id";
    public static final String REQUEST_TIME_CAT_ID = "request_cat_id";
    public static final String LAST_REQUEST_TIME_VALUE = "last_request_time";

    String TAG = "NewsDatabase.java";
    private Cursor cursor;

    private static final String NEWS_TABLE = "CREATE TABLE " + NEWS_TABLE_NAME + "(" +
            NEWS_SERIAL + " INTEGER PRIMARY KEY   AUTOINCREMENT," +
            NEWS_PUBLISH_SERIAL + " INTEGER NOT NULL," +
            NEWS_ID + " VARCHAR NOT NULL, " +
            NEWS_CATEGORY_ID + " VARCHAR NULL, " +
            NEWS_HEADING + " VARCHAR NULL, " +
            NEWS_SUB_HEADING + " VARCHAR NULL, " +
            NEWS_SHOULDER + " VARCHAR NULL, " +
            NEWS_PUBLISH_TIME + " VARCHAR NULL, " +
            NEWS_REPORTER + " VARCHAR NULL, " +
            NEWS_DETAILS + " VARCHAR NULL, " +
            NEWS_IMAGE + " VARCHAR NULL, " +
            NEWS_TOP_NEWS + " INTEGER NOT NULL, " +
            NEWS_HOME_SLIDER + " INTEGER NOT NULL , " +
            NEWS_INSIDE_NEWS + " INTEGER NOT NULL);";

    private static final String CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + "(" + CATEGORY_ID + " VARCHAR NOT NULL, " +
            CATEGORY_NEWS_ID + " VARCHAR NOT NULL);";

    private static final String FAVORITE_TABLE = "CREATE TABLE " + FAVORITE_TABLE_BANE + "(" +
            FAVORITE_ID + " VARCHAR NOT NULL);";

    private static final String HOME_CUSTOMIZE_TABLE = "CREATE TABLE " + HOME_CUSTOMIZE_TABLE_NAME +
            "(" + HOME_CAT_ID + " VARCHAR NOT NULL);";


    private static final String DRAWER_TABLE = " CREATE TABLE " + DRAWER_TABLE_NAME + "(" + DRAWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DRAWER_CAT_ID + " VARCHAR NOT NULL," + DRAWER_CAT_NAME + " VARCHAR NOT NULL, " + DRAWER_CAT_IMAGE + " VARCHAR NOT NULL, " + DRAWER_CAT_PARENT + " VARCHAR NOT NULL, " + DRAWER_CAT_ORDER + " INTEGER NOT NULL);";

    private static final String SETTING_TABLE = " CREATE TABLE " + " settings " + "(" + "settings_id INTEGER PRIMARY KEY AUTOINCREMENT," + "key VARCHAR NOT NULL, " + "value VARCHAR NOT NULL);";

    private static final String REQUEST_TIME = "CREATE TABLE " + REQUEST_TIME_TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REQUEST_TIME_CAT_ID + " INTEGER NOT NULL, " + LAST_REQUEST_TIME_VALUE + " VARCHAR NOT NULL);";


    String fontSize = "insert into " + "settings" + " (" + "key" + ", " + "value" + ") values('font_size', '18');";
    String radioSelector = "insert into " + "settings" + " (" + "key" + ", " + "value" + ") values('position', '1');";
    String fontLevel = "insert into " + "settings" + " (" + "key" + ", " + "value" + ") values('font_level', 'Normal');";
    String single_cat_server_request = "insert into " + "settings" + " (" + "key" + ", " + "value" + ") values('last_request', '00000');";
    String all_cat_server_request = "insert into " + "settings" + " (" + "key" + ", " + "value" + ") values('last_request_all_cat', '00000');";
    String cat_position = "insert into " + "settings" + " (" + "key" + ", " + "value" + ") values('cat_position', '');";


    public NewsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NEWS_TABLE);
        db.execSQL(CATEGORY_TABLE);
        db.execSQL(FAVORITE_TABLE);
        db.execSQL(HOME_CUSTOMIZE_TABLE);
        db.execSQL(DRAWER_TABLE);
        db.execSQL(SETTING_TABLE);
        db.execSQL(fontSize);
        db.execSQL(radioSelector);
        db.execSQL(fontLevel);
        db.execSQL(single_cat_server_request);
        db.execSQL(cat_position);
        db.execSQL(all_cat_server_request);
        db.execSQL(REQUEST_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DRAWER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +NEWS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +FAVORITE_TABLE_BANE);
        db.execSQL("DROP TABLE IF EXISTS " +HOME_CUSTOMIZE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS settings");
        db.execSQL("DROP TABLE IF EXISTS " +REQUEST_TIME_TABLE_NAME);
        onCreate(db);

    }


    //add category id into home customize table..
    public void addCustomNews(String catid) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(HOME_CUSTOMIZE_TABLE_NAME, HOME_CAT_ID + "=?", new String[]{catid});

            ContentValues values = new ContentValues();
            values.put(HOME_CAT_ID, catid);
//            Log.e("NewsDatabase.java", " add home customize id : "+catid);
            database.insert(HOME_CUSTOMIZE_TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //get customize id from home customize table
    public String getCustomizeId() {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;
        try {
            String QUERY = "SELECT " + HOME_CAT_ID + " FROM " + HOME_CUSTOMIZE_TABLE_NAME;
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

    //get all custom category from home customize table
    public String[] getCustomCat() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<String> strings = new ArrayList<>();
        String final_cat = "";
        String[] cat = {""};
        try {
            String QUERY = "SELECT " + HOME_CAT_ID + " FROM " + HOME_CUSTOMIZE_TABLE_NAME;
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    final_cat = cursor.getString(cursor.getColumnIndex("home_cat_id"));
//                    Log.e("NewsDatabase.java", " home customize id : "+final_cat);
                    strings.add(final_cat + "");
                }
            }

            cat = new String[strings.size()];
            for (int j = 0; j < strings.size(); j++) {
                cat[j] = strings.get(j);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return cat;
    }

    //cheek customize news exits or not into home customize table
    public String getCustomizeExit(String cat_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;
        try {
            String QUERY = "SELECT * FROM " + HOME_CUSTOMIZE_TABLE_NAME + " where " + HOME_CAT_ID + " = " + cat_id;
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
        }
        return "0";
    }

    //Delete custom id from customize table..
    public void deleteCustomNews(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(HOME_CUSTOMIZE_TABLE_NAME, HOME_CAT_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }


    //get font size from settings table
    public String getFontSize() {
        SQLiteDatabase database = this.getReadableDatabase();
        String values = "";
        try {
            String QUERY = "SELECT * FROM settings WHERE key = 'font_size'";
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return values;
    }

    //update font size value into settings table
    public String updateFontSize(String size) {
        SQLiteDatabase database = this.getWritableDatabase();
        String values = "";
        try {
            String strSQL = "UPDATE settings SET value= '" + size + "' WHERE key= 'font_size'";
            cursor = database.rawQuery(strSQL, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
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

        return values;
    }

    //get font level value from settings table
    public String getFontLevel() {
        SQLiteDatabase database = this.getReadableDatabase();
        String values = "";

        try {
            String QUERY = "SELECT * FROM settings WHERE key = 'font_level'";
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return values;
    }

    //update font level value into settings table
    public String updateFontLevel(String size) {
        SQLiteDatabase database = this.getWritableDatabase();
        String values = "";
        try {
            String strSQL = "UPDATE settings SET value= '" + size + "' WHERE key= 'font_level'";
            cursor = database.rawQuery(strSQL, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
        return values;
    }

    //get font size selector value from settings table
    public String getFontSelector() {
        SQLiteDatabase database = this.getReadableDatabase();
        String values = "";
        try {
            String QUERY = "SELECT * FROM settings WHERE key = 'position'";
            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return values;
    }

    //update font selector value into settings table
    public String updateSelector(String size) {
        SQLiteDatabase database = this.getWritableDatabase();
        String values = "";
        try {
            String strSQL = "UPDATE settings SET value= '" + size + "' WHERE key= 'position'";
            cursor = database.rawQuery(strSQL, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

        return values;
    }

    public String getCat_position() {
        SQLiteDatabase database = this.getReadableDatabase();
        String values = "";
        try {
            String QUERY = "SELECT * FROM settings WHERE key = 'cat_position'";
            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            Log.e("error", e + "");
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return values;
    }

    //update font selector value into settings table
    public String updateCatPosition(String position) {
        SQLiteDatabase database = this.getWritableDatabase();
        String values = "";
        try {
            String strSQL = "UPDATE settings SET value= '" + position + "' WHERE key= 'cat_position'";
            cursor = database.rawQuery(strSQL, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = (cursor.getString(cursor.getColumnIndex("value")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

        return values;
    }

    //Add favorite id into favorite table
    public void addCatRequestTime(int cat_id, long time) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_TIME_CAT_ID, cat_id);
        values.put(LAST_REQUEST_TIME_VALUE, time);
        try {
            database.insert(REQUEST_TIME_TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("problem", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

    }

    public int getCatRequestIdExist(int cat_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;

        try {
            String QUERY = "SELECT * FROM " + REQUEST_TIME_TABLE_NAME + "  where " + REQUEST_TIME_CAT_ID + "=" + cat_id;

            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            if (num > 0) {
                return 1;
            } else {
                return 0;
            }

        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return 0;

    }

    public Long getServerUpdateTime(int _id) {
        SQLiteDatabase database = this.getReadableDatabase();
        long values = 0;
        try {
            String QUERY = "SELECT * FROM " + REQUEST_TIME_TABLE_NAME + " WHERE " + REQUEST_TIME_CAT_ID + " = " + _id;
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = Long.parseLong((cursor.getString(cursor.getColumnIndex("last_request_time"))));
                }
            }
        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return values;
    }

    //update font selector value into settings table
    public Long updateServerTime(int cat_id, long time) {
        SQLiteDatabase database = this.getWritableDatabase();
        long values = 0;
        try {
            String strSQL = "UPDATE " + REQUEST_TIME_TABLE_NAME + " SET " + LAST_REQUEST_TIME_VALUE + "= '" + time + "' WHERE " + REQUEST_TIME_CAT_ID + "= " + cat_id;
            cursor = database.rawQuery(strSQL, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = Long.parseLong((cursor.getString(cursor.getColumnIndex("last_request_time"))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

        return values;
    }

    public Long getAllCatServerUpdateTime() {
        SQLiteDatabase database = this.getReadableDatabase();
        long values = 0;
        try {
            String QUERY = "SELECT * FROM settings WHERE key = 'last_request_all_cat'";
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = Long.parseLong((cursor.getString(cursor.getColumnIndex("value"))));
                }
            }
        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return values;
    }

    //update font selector value into settings table
    public Long updateAllcatServerTime(long time) {
        SQLiteDatabase database = this.getWritableDatabase();
        long values = 0;
        try {
            String strSQL = "UPDATE settings SET value= '" + time + "' WHERE key= 'last_request_all_cat'";
            cursor = database.rawQuery(strSQL, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    values = Long.parseLong((cursor.getString(cursor.getColumnIndex("value"))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

        return values;
    }


    //clean table
    public void cleanCatTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(CATEGORY_TABLE_NAME, CATEGORY_ID + ">?", new String[]{"0"});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

    }


}
