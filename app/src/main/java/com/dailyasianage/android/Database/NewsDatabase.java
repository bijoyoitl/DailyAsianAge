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

    public static final String NEWS_TABLE_NAME = "News";
    public static final String NEWS_CATEGORY_ID = "category_id";
    public static final String NEWS_ID = "id";
    public static final String NEWS_HEADING = "heading";
    public static final String NEWS_SUB_HEADING = "sub_heading";
    public static final String NEWS_SHOULDER = "shoulder";
    public static final String NEWS_PUBLISH_TIME = "publish_time";
    public static final String NEWS_REPORTER = "reporter";
    public static final String NEWS_DETAILS = "details";
    public static final String NEWS_IMAGE = "image";
//    public static final String TOP_NEWS = "n_top_news";
//    public static final String HOME_SLIDER = "n_home_slider";
//    public static final String INSIDE_NEWS = "n_inside_news";


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
    //    private SQLiteDatabase database;
    private Cursor cursor;
    private static final String NEWS_TABLE = "CREATE TABLE " + NEWS_TABLE_NAME + "(" +
            "serial INTEGER PRIMARY KEY   AUTOINCREMENT," +
            "publish_serial INTEGER NOT NULL," +
            NEWS_ID + " VARCHAR NOT NULL, " +
            NEWS_CATEGORY_ID + " VARCHAR NULL, " +
            NEWS_HEADING + " VARCHAR NULL, " +
            NEWS_SUB_HEADING + " VARCHAR NULL, " +
            NEWS_SHOULDER + " VARCHAR NULL, " +
            NEWS_PUBLISH_TIME + " VARCHAR NULL, " +
            NEWS_REPORTER + " VARCHAR NULL, " +
            NEWS_DETAILS + " VARCHAR NULL, " +
            NEWS_IMAGE + " VARCHAR NULL, " +
            "n_top_news INTEGER NOT NULL, " +
            "n_home_slider INTEGER NOT NULL , " +
            "n_inside_news INTEGER NOT NULL);";

    private static final String CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + "(" + CATEGORY_ID + " VARCHAR NOT NULL, " +
            CATEGORY_NEWS_ID + " VARCHAR NOT NULL);";

    private static final String FAVORITE_TABLE = "CREATE TABLE " + FAVORITE_TABLE_BANE + "(" +
            FAVORITE_ID + " VARCHAR NOT NULL);";

    private static final String HOME_CUSTOMIZE_TABLE = "CREATE TABLE " + HOME_CUSTOMIZE_TABLE_NAME +
            "(" + HOME_CAT_ID + " VARCHAR NOT NULL);";


    private static final String DRAWER_TABLE = " CREATE TABLE " + " drawer_list " + "(" + "d_id INTEGER PRIMARY KEY AUTOINCREMENT," + "cat_id VARCHAR NOT NULL," + "cat_name VARCHAR NOT NULL, " + "cat_image VARCHAR NOT NULL, " + "parent VARCHAR NOT NULL, " + "cat_order INTEGER NOT NULL);";

    private static final String SETTING_TABLE = " CREATE TABLE " + " settings " + "(" + "settings_id INTEGER PRIMARY KEY AUTOINCREMENT," + "key VARCHAR NOT NULL, " + "value VARCHAR NOT NULL);";

    private static final String REQUEST_TIME = "CREATE TABLE " + REQUEST_TIME_TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REQUEST_TIME_CAT_ID + " INTEGER NOT NULL, "+LAST_REQUEST_TIME_VALUE+ " VARCHAR NOT NULL);";


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
        String query;
        query = "DROP TABLE IF EXISTS " + DATABASE_NAME + "";
        db.execSQL(query);
        onCreate(db);

    }


    //Add cat id, cat name and image into drawer table.
    public void addDrawerList(DbDrawerItem drawerItem) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("cat_id", drawerItem.getCat_id());
        values.put("cat_name", drawerItem.getCat_name());
        values.put("cat_image", drawerItem.getCat_imag());
        values.put("parent", drawerItem.getLevel());
        values.put("cat_order", drawerItem.getOrder());
        try {
            database.insert("drawer_list", null, values);
        } catch (Exception e) {
            Log.e("problem", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //get all cat id, cat name and image from drawer_list table
    public ArrayList<DbDrawerItem> getAllDrawer() {
        SQLiteDatabase database = this.getReadableDatabase();

        ArrayList<DbDrawerItem> itemArrayList = null;
        try {
            itemArrayList = new ArrayList<DbDrawerItem>();
            cursor = database.query("drawer_list", null, null, null, null, null, "CAST(cat_id AS INTEGER) asc");
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DbDrawerItem item = new DbDrawerItem();
                    item.setCat_id(cursor.getString(cursor.getColumnIndex("cat_id")));
                    item.setCat_name(cursor.getString(cursor.getColumnIndex("cat_name")));
                    item.setCat_imag(cursor.getString(cursor.getColumnIndex("cat_image")));
                    itemArrayList.add(item);
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

        return itemArrayList;
    }//get all cat id, cat name and image from drawer_list table

    // /get all parent from drawer_list table for nav items..
    public ArrayList<DbDrawerItem> getAllParentInfo() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<DbDrawerItem> itemArrayList = null;
        DbDrawerItem item1 = new DbDrawerItem();
//        DbDrawerItem item2 = new DbDrawerItem();
        try {
            itemArrayList = new ArrayList<DbDrawerItem>();
            cursor = database.query("drawer_list", null, "parent = 0", null, null, null, "cat_order asc");
            if (!cursor.isLast()) {
//                item1.setCat_name("Home");
//                itemArrayList.add(item1);

                while (cursor.moveToNext()) {
                    DbDrawerItem item = new DbDrawerItem();
                    item.setCat_id(cursor.getString(cursor.getColumnIndex("cat_id")));
                    item.setCat_name(cursor.getString(cursor.getColumnIndex("cat_name")));
                    item.setLevel(cursor.getString(cursor.getColumnIndex("parent")));
                    item.setCat_imag(cursor.getString(cursor.getColumnIndex("cat_image")));
                    itemArrayList.add(item);
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

        return itemArrayList;
    }

    // / /get all child info from drawer_list table for nav items..
    public ArrayList<DbDrawerItem> getAllChildInfo(String cat_id) {
        SQLiteDatabase database = this.getReadableDatabase();

        ArrayList<DbDrawerItem> itemArrayList = null;
        try {
            itemArrayList = new ArrayList<DbDrawerItem>();
            cursor = database.query("drawer_list", null, "parent = " + cat_id + "", null, null, null, "cat_order asc");
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DbDrawerItem item = new DbDrawerItem();
                    item.setCat_id(cursor.getString(cursor.getColumnIndex("cat_id")));
                    item.setCat_name(cursor.getString(cursor.getColumnIndex("cat_name")));
                    item.setLevel(cursor.getString(cursor.getColumnIndex("parent")));
                    item.setCat_imag(cursor.getString(cursor.getColumnIndex("cat_image")));
                    itemArrayList.add(item);
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

        return itemArrayList;
    }

    public String getAllDrawerCategoryName(String catid) {
        SQLiteDatabase database = this.getReadableDatabase();
        String cat = "";
        try {

            String QUERY = "SELECT cat_name FROM drawer_list where cat_id =" + catid;
            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    cat = cursor.getString(cursor.getColumnIndex("cat_name"));
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
        return cat;
    }
    public String[] getAllCatId() {
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<String> strings = new ArrayList<>();
        String final_cat = "";
        String[] cat = {""};
        try {
            String QUERY = "SELECT cat_id  FROM drawer_list";
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    final_cat = cursor.getString(cursor.getColumnIndex("cat_id"));
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
    //cheek drawer category exits or not into drawer table
    public String getDrawerCatExist(String catid) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;

        try {
            String QUERY = "SELECT * FROM " + "drawer_list" + "  where  " + "cat_id" + "=" + catid;

            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            if (num > 0) {
                return "1";
            } else {
                return "0";
            }

        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return "0";

    }


    /* Add All news info into News table..*/
    public void addNews(News news) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NEWS_CATEGORY_ID, news.getCat_id());
        values.put(NEWS_ID, news.getId());
        values.put(NEWS_HEADING, news.getHeading());
        values.put(NEWS_SUB_HEADING, news.getSub_heading());
        values.put(NEWS_SHOULDER, news.getShoulder());
        values.put(NEWS_PUBLISH_TIME, news.getPublish_time());
        values.put(NEWS_REPORTER, news.getReporter());
        values.put(NEWS_DETAILS, news.getDetails());
        values.put(NEWS_IMAGE, news.getImage());
        values.put("publish_serial", news.getPublish_serial());
        values.put("n_top_news", news.getTop_news());
        values.put("n_home_slider", news.getHome_slider());
        values.put("n_inside_news", news.getInside_news());
        try {
            database.insert(NEWS_TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("problem", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //get all news from news table
    public ArrayList<News> getAllNews(String nIds) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<News> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<News>();
            cursor = database.query(NEWS_TABLE_NAME, null, NEWS_ID + " in(" + nIds + ")", null, null, null, " CAST(" + NEWS_CATEGORY_ID + " as Integer) asc, publish_serial desc,n_top_news desc, n_home_slider desc, n_inside_news desc");
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    News news1 = new News();
                    news1.setCat_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    news1.setId(cursor.getString(cursor.getColumnIndex("id")));
                    news1.setHeading(cursor.getString(cursor.getColumnIndex("heading")));
                    news1.setSub_heading(cursor.getString(cursor.getColumnIndex("sub_heading")));
                    news1.setDetails(cursor.getString(cursor.getColumnIndex("details")));
                    news1.setShoulder(cursor.getString(cursor.getColumnIndex("shoulder")));
                    news1.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
                    news1.setReporter(cursor.getString(cursor.getColumnIndex("reporter")));
                    news1.setImage(cursor.getString(cursor.getColumnIndex("image")));
                    newsArrayList.add(news1);
                }
            }
        } catch (Exception e) {
            Log.e("get all news error", e + "");
            e.printStackTrace();

        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return newsArrayList;
    }

    public ArrayList<News> getAllOfflineNews(String cat_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<News> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<News>();
            cursor = database.query(NEWS_TABLE_NAME, null, NEWS_CATEGORY_ID + " in(" + cat_id + ")", null, null, null, " CAST(" + NEWS_CATEGORY_ID + " as Integer) asc, publish_serial desc,n_top_news desc, n_home_slider desc, n_inside_news desc");
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    News news1 = new News();
                    news1.setCat_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    news1.setId(cursor.getString(cursor.getColumnIndex("id")));
                    news1.setHeading(cursor.getString(cursor.getColumnIndex("heading")));
                    news1.setSub_heading(cursor.getString(cursor.getColumnIndex("sub_heading")));
                    news1.setDetails(cursor.getString(cursor.getColumnIndex("details")));
                    news1.setShoulder(cursor.getString(cursor.getColumnIndex("shoulder")));
                    news1.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
                    news1.setReporter(cursor.getString(cursor.getColumnIndex("reporter")));
                    news1.setImage(cursor.getString(cursor.getColumnIndex("image")));
                    newsArrayList.add(news1);
                }
            }
        } catch (Exception e) {
            Log.e("get all news error", e + "");
            e.printStackTrace();

        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return newsArrayList;
    }

    //cheek drawer category exits or not into drawer table
    public String getNewsIdExist(String catid) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;

        try {
//            String QUERY = "SELECT * FROM " + NEWS_TABLE_NAME + "  where  " + NEWS_ID + "=" + catid;
            String QUERY = "SELECT * FROM " + NEWS_TABLE_NAME + " where " + NEWS_ID + " = " + catid;
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            if (num > 0) {
                return "1";
            } else {
                return "0";
            }

        } catch (Exception e) {
            Log.e(" get news id error", e + "");
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return "0";

    }

    //get all news from news table
    public ArrayList<News> getCategoryNews(String cat_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<News> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<News>();
            cursor = database.query(NEWS_TABLE_NAME, null, NEWS_CATEGORY_ID + " = " + cat_id, null, null, null, " CAST(" + NEWS_CATEGORY_ID + " as Integer) asc, publish_serial desc,n_top_news desc, n_home_slider desc, n_inside_news desc");
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
//                    Log.e("NewsDatabase.java", " cat in db : " + cat_id);
                    News news1 = new News();
                    news1.setCat_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    news1.setId(cursor.getString(cursor.getColumnIndex("id")));
                    news1.setHeading(cursor.getString(cursor.getColumnIndex("heading")));
                    news1.setSub_heading(cursor.getString(cursor.getColumnIndex("sub_heading")));
                    news1.setDetails(cursor.getString(cursor.getColumnIndex("details")));
                    news1.setShoulder(cursor.getString(cursor.getColumnIndex("shoulder")));
                    news1.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
                    news1.setReporter(cursor.getString(cursor.getColumnIndex("reporter")));
                    news1.setImage(cursor.getString(cursor.getColumnIndex("image")));
                    newsArrayList.add(news1);
                }
            }
        } catch (Exception e) {
            Log.e("get category error", e + "");
            e.printStackTrace();

        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return newsArrayList;
    }


    public ArrayList<News> getRelatedNews(String cat_id, String news_id) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<News> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<News>();
            String query = "SELECT * FROM " + NEWS_TABLE_NAME + " where " + NEWS_CATEGORY_ID + "= " + cat_id + " and " + NEWS_ID + " not in(" + news_id + ")" + " ORDER BY publish_serial desc, n_top_news desc, n_home_slider desc, n_inside_news desc " + " LIMIT 10";
            cursor = database.rawQuery(query, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    News news1 = new News();
                    news1.setCat_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    news1.setId(cursor.getString(cursor.getColumnIndex("id")));
                    news1.setHeading(cursor.getString(cursor.getColumnIndex("heading")));
                    news1.setSub_heading(cursor.getString(cursor.getColumnIndex("sub_heading")));
                    news1.setDetails(cursor.getString(cursor.getColumnIndex("details")));
                    news1.setShoulder(cursor.getString(cursor.getColumnIndex("shoulder")));
                    news1.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
                    news1.setReporter(cursor.getString(cursor.getColumnIndex("reporter")));
                    news1.setImage(cursor.getString(cursor.getColumnIndex("image")));
                    newsArrayList.add(news1);
                    //
                }
            }
            //  databaseRead.close();
        } catch (Exception e) {
            Log.e("get related news error", e + "");
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return newsArrayList;
    }

    //cheek news exits or not into News table
    public int getNewsExist(int newsID) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;

        try {
            String QUERY = "SELECT * FROM " + NEWS_TABLE_NAME + " where id =" + newsID;
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            return num;
        } catch (Exception e) {
            Log.e("get news exit error", e + "");
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }

        return 0;
    }
    public int getNewsAllExist(int newsID) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;

        try {
            String QUERY = "SELECT * FROM " + NEWS_TABLE_NAME + " where "+NEWS_ID + " in(" + newsID+ ")";
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            return num;
        } catch (Exception e) {
            Log.e("get news exit error", e + "");
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }

        return 0;
    }

    //delete old news data from database (news table)
    public void deleteOldNews(String news_ids) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(NEWS_TABLE_NAME, NEWS_ID + " not in(" + news_ids + ") and " + NEWS_ID + " not in(SELECT " + FAVORITE_ID + " FROM " + FAVORITE_TABLE_BANE + ")", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

    }


    //Add favorite id into favorite table
    public String addFavorite(String favId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITE_ID, favId);
        try {
            database.insert(FAVORITE_TABLE_BANE, null, values);
        } catch (Exception e) {
            Log.e("problem", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }

        return FAVORITE_ID;
    }


    public ArrayList<News> getFav() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<News> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<News>();
            String QUERY = "SELECT * FROM " + NEWS_TABLE_NAME + " where " + NEWS_ID + " in(SELECT " + FAVORITE_ID + " FROM " + FAVORITE_TABLE_BANE + ")";
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    News news1 = new News();
                    news1.setCat_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    news1.setId(cursor.getString(cursor.getColumnIndex("id")));
                    news1.setHeading(cursor.getString(cursor.getColumnIndex("heading")));
                    news1.setSub_heading(cursor.getString(cursor.getColumnIndex("sub_heading")));
                    news1.setDetails(cursor.getString(cursor.getColumnIndex("details")));
                    news1.setShoulder(cursor.getString(cursor.getColumnIndex("shoulder")));
                    news1.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
                    news1.setReporter(cursor.getString(cursor.getColumnIndex("reporter")));
                    news1.setImage(cursor.getString(cursor.getColumnIndex("image")));
                    newsArrayList.add(news1);
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
        return newsArrayList;
    }


    //cheek favorite news exits or not into favorite table
    public String getFavExit(String fav_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;

        try {
            String QUERY = "SELECT * FROM " + FAVORITE_TABLE_BANE + " where " + FAVORITE_ID + " =" + fav_id;

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
            Log.e("error", e + "");
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
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(FAVORITE_TABLE_BANE, FAVORITE_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }


    // add category id and category`s news id into category table
    public void addCatNews(String catid, String catstring) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(CATEGORY_TABLE_NAME, CATEGORY_ID + "=?", new String[]{catid});
            ContentValues contentValues = new ContentValues();
            contentValues.put(CATEGORY_ID, catid);
            contentValues.put(CATEGORY_NEWS_ID, catstring);
            database.insert(CATEGORY_TABLE_NAME, null, contentValues);

        } catch (Exception e) {
            Log.e("problem", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //get cat news ids from category table
    public String getCatNews(String catid) {
        SQLiteDatabase database = this.getReadableDatabase();
        String catnews = "";
        try {
            String QUERY = "SELECT * FROM " + CATEGORY_TABLE_NAME + " where " + CATEGORY_ID + "=" + catid;
            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    catnews = cursor.getString(cursor.getColumnIndex("news_id"));
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
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(CATEGORY_TABLE_NAME, CATEGORY_ID + "=?", new String[]{cat_id});
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
        SQLiteDatabase database = this.getReadableDatabase();
        int num = 0;
        try {
            String QUERY = "SELECT * FROM " + CATEGORY_TABLE_NAME + " where " + CATEGORY_ID + " =" + catid;
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            if (num > 0) {
                return "1";
            } else {
                return "0";
            }

        } catch (Exception e) {
            Log.e("error", e + "");
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return "0";

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
//            Log.e("NewsDatabase.java", "q 2= " + QUERY);
            num = cursor.getCount();
//           databaseRead.close();
            if (num > 0) {
                return "1";
            } else {
                return "0";
            }
        } catch (Exception e) {
            Log.e("get customized error", e + "");
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
            String QUERY = "SELECT * FROM "+REQUEST_TIME_TABLE_NAME+" WHERE "+REQUEST_TIME_CAT_ID+" = "+_id;
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
            String strSQL = "UPDATE "+REQUEST_TIME_TABLE_NAME+" SET "+LAST_REQUEST_TIME_VALUE+"= '" + time + "' WHERE "+REQUEST_TIME_CAT_ID+"= "+cat_id;
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
