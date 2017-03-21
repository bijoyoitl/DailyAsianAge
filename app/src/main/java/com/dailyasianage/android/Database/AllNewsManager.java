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

public class AllNewsManager {
    private Context context;
    private NewsDatabase newsDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public AllNewsManager(Context context) {
        this.context = context;
        newsDatabase = new NewsDatabase(context);
    }

    /* Add All news info into News table..*/
    public void addAllNews(NewsAll news) {
        ContentValues values = new ContentValues();

        values.put(NewsDatabase.NEWS_CATEGORY_ID, news.getCatId());
        values.put(NewsDatabase.NEWS_ID, news.getId());
        values.put(NewsDatabase.NEWS_HEADING, news.getHeading());
        values.put(NewsDatabase.NEWS_SUB_HEADING, news.getSubHeading());
        values.put(NewsDatabase.NEWS_SHOULDER, news.getShoulder());
        values.put(NewsDatabase.NEWS_PUBLISH_TIME, news.getPublishTime());
        values.put(NewsDatabase.NEWS_REPORTER, news.getReporter());
        values.put(NewsDatabase.NEWS_DETAILS, news.getDetails());
        values.put(NewsDatabase.NEWS_IMAGE, news.getImage());
        values.put(NewsDatabase.NEWS_PUBLISH_SERIAL, news.getPublishSerial());
        values.put(NewsDatabase.NEWS_TOP_NEWS, news.getNTopNews());
        values.put(NewsDatabase.NEWS_HOME_SLIDER, news.getNHomeSlider());
        values.put(NewsDatabase.NEWS_INSIDE_NEWS, news.getNInsideNews());

        try {
            database = newsDatabase.getWritableDatabase();
            database.insert(NewsDatabase.NEWS_TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    //get all news from news table
    public ArrayList<NewsAll> getAllNews(String nIds) {

        ArrayList<NewsAll> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<NewsAll>();
            database = newsDatabase.getReadableDatabase();

            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, null, NewsDatabase.NEWS_ID + " in(" + nIds + ")", null, null, null, " CAST(" + NewsDatabase.NEWS_CATEGORY_ID + " as Integer) asc, " + NewsDatabase.NEWS_PUBLISH_SERIAL + " desc," + NewsDatabase.NEWS_TOP_NEWS + " desc, " + NewsDatabase.NEWS_HOME_SLIDER + " desc, " + NewsDatabase.NEWS_INSIDE_NEWS + " desc");

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

    //get all news from news table
    public NewsAll getSingleNews(String newsId) {

        NewsAll newsAll = null;
        try {
            newsAll = new NewsAll();
            database = newsDatabase.getReadableDatabase();

            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, null, NewsDatabase.NEWS_ID + " = " + newsId, null, null, null, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    newsAll.setCatId(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_CATEGORY_ID)));
                    newsAll.setId(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_ID)));
                    newsAll.setHeading(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_HEADING)));
                    newsAll.setSubHeading(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_SUB_HEADING)));
                    newsAll.setDetails(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_DETAILS)));
                    newsAll.setShoulder(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_SHOULDER)));
                    newsAll.setPublishTime(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_PUBLISH_TIME)));
                    newsAll.setReporter(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_REPORTER)));
                    newsAll.setImage(cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_IMAGE)));
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
        return newsAll;
    }

    public ArrayList<String> getAllNewsId() {
        ArrayList<String> allNewsId = null;

        try {
            allNewsId = new ArrayList<>();
            database = newsDatabase.getReadableDatabase();

            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, new String[]{NewsDatabase.NEWS_ID}, null, null, null, null, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_ID));
                    allNewsId.add(id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
        return allNewsId;
    }

    public ArrayList<String> getSingleCategoryNewsId(String catIds) {
        ArrayList<String> allNewsId = null;

        try {
            allNewsId = new ArrayList<>();
            database = newsDatabase.getReadableDatabase();

//            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, new String[]{NewsDatabase.NEWS_ID}, NewsDatabase.NEWS_CATEGORY_ID +"=?", null, null, null, null);

            String query = "select " + NewsDatabase.NEWS_ID + " from " + NewsDatabase.NEWS_TABLE_NAME + " where " + NewsDatabase.NEWS_CATEGORY_ID + " in(" + catIds + ")";
            cursor = database.rawQuery(query, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(NewsDatabase.NEWS_ID));
                    allNewsId.add(id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
        return allNewsId;
    }


    public ArrayList<NewsAll> getAlNews() {

        ArrayList<NewsAll> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<NewsAll>();
            database = newsDatabase.getReadableDatabase();

            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, null, null, null, null, null, " CAST(" + NewsDatabase.NEWS_CATEGORY_ID + " as Integer) asc, " + NewsDatabase.NEWS_PUBLISH_SERIAL + " desc," + NewsDatabase.NEWS_TOP_NEWS + " desc, " + NewsDatabase.NEWS_HOME_SLIDER + " desc, " + NewsDatabase.NEWS_INSIDE_NEWS + " desc");

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


    public ArrayList<NewsAll> getAllOfflineNews(String cat_id) {

        ArrayList<NewsAll> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<NewsAll>();
            database = newsDatabase.getReadableDatabase();
            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, null, NewsDatabase.NEWS_CATEGORY_ID + " in(" + cat_id + ")", null, null, null, " CAST(" + NewsDatabase.NEWS_CATEGORY_ID + " as Integer) asc, " + NewsDatabase.NEWS_PUBLISH_SERIAL + " desc," + NewsDatabase.NEWS_TOP_NEWS + " desc, " + NewsDatabase.NEWS_HOME_SLIDER + " desc, " + NewsDatabase.NEWS_INSIDE_NEWS + " desc");
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

    //cheek drawer category exits or not into drawer table
    public String getNewsIdExist(String catid) {
        int num = 0;
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT * FROM " + NewsDatabase.NEWS_TABLE_NAME + " where " + NewsDatabase.NEWS_ID + " = " + catid;
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

    //get all news from news table
    public ArrayList<NewsAll> getCategoryNews(String cat_id) {

        ArrayList<NewsAll> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<NewsAll>();
            database = newsDatabase.getReadableDatabase();
            cursor = database.query(NewsDatabase.NEWS_TABLE_NAME, null, NewsDatabase.NEWS_CATEGORY_ID + " = " + cat_id, null, null, null, " CAST(" + NewsDatabase.NEWS_CATEGORY_ID + " as Integer) asc, " + NewsDatabase.NEWS_PUBLISH_SERIAL + " desc," + NewsDatabase.NEWS_TOP_NEWS + " desc, " + NewsDatabase.NEWS_HOME_SLIDER + " desc, " + NewsDatabase.NEWS_INSIDE_NEWS + " desc");

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


    public ArrayList<NewsAll> getRelatedNews(String cat_id, String news_id) {

        database = newsDatabase.getReadableDatabase();
        ArrayList<NewsAll> newsArrayList = null;
        try {
            newsArrayList = new ArrayList<NewsAll>();
            database = newsDatabase.getReadableDatabase();

            String query = "SELECT * FROM " + NewsDatabase.NEWS_TABLE_NAME + " where " + NewsDatabase.NEWS_CATEGORY_ID + "= " + cat_id + " and " + NewsDatabase.NEWS_ID + " not in(" + news_id + ")" + " ORDER BY " + NewsDatabase.NEWS_PUBLISH_SERIAL + " desc," + NewsDatabase.NEWS_TOP_NEWS + " desc, " + NewsDatabase.NEWS_HOME_SLIDER + " desc, " + NewsDatabase.NEWS_INSIDE_NEWS + " desc" + " LIMIT 10";

            cursor = database.rawQuery(query, null);
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

    //cheek news exits or not into News table
    public int getNewsExist(int newsID) {
        int num = 0;
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT * FROM " + NewsDatabase.NEWS_TABLE_NAME + " where id =" + newsID;
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            return num;
        } catch (Exception e) {
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
        int num = 0;
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT * FROM " + NewsDatabase.NEWS_TABLE_NAME + " where " + NewsDatabase.NEWS_ID + " in(" + newsID + ")";
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            return num;
        } catch (Exception e) {
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
        try {
            database = newsDatabase.getWritableDatabase();
            database.delete(NewsDatabase.NEWS_TABLE_NAME, NewsDatabase.NEWS_ID + " not in(" + news_ids + ")", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    public void deleteOldNewsByCategory(String catId, String news_ids) {
        try {
            database = newsDatabase.getWritableDatabase();
            database.delete(NewsDatabase.NEWS_TABLE_NAME, NewsDatabase.NEWS_CATEGORY_ID + " = " + catId + " and " + NewsDatabase.NEWS_ID + " not in(" + news_ids + ")", null);

//            String q = "delete from " + NewsDatabase.NEWS_TABLE_NAME + " where " + NewsDatabase.NEWS_CATEGORY_ID + " = " + catId + " and " + NewsDatabase.NEWS_ID + " not in(" + news_ids + ")";
//            Log.e("ANM", "query : " + q);
//
//            database.rawQuery(q, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

}
