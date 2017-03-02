package com.dailyasianage.android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dailyasianage.android.item.DbDrawerItem;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

/**
 * Created by ripon on 3/2/2017.
 */

public class DrawerMenuManager {

    private Context context;
    private NewsDatabase newsDatabase;
    private SQLiteDatabase database;
    private Cursor cursor;

    public DrawerMenuManager(Context context) {
        this.context = context;
        newsDatabase = new NewsDatabase(context);
    }


    public void addDrawerList(DbDrawerItem drawerItem) {
        ContentValues values = new ContentValues();

        values.put(NewsDatabase.DRAWER_CAT_ID, drawerItem.getCat_id());
        values.put(NewsDatabase.DRAWER_CAT_NAME, drawerItem.getCat_name());
        values.put(NewsDatabase.DRAWER_CAT_IMAGE, drawerItem.getCat_imag());
        values.put(NewsDatabase.DRAWER_CAT_PARENT, drawerItem.getLevel());
        values.put(NewsDatabase.DRAWER_CAT_ORDER, drawerItem.getOrder());
        try {
            database = newsDatabase.getWritableDatabase();
            database.insert(NewsDatabase.DRAWER_TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            if (database.isOpen()) {
                database.close();
            }
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    public void updateDrawerList(DbDrawerItem drawerItem, String cat_id) {
        ContentValues values = new ContentValues();

//        values.put(NewsDatabase.DRAWER_CAT_ID, drawerItem.getCat_id());
        values.put(NewsDatabase.DRAWER_CAT_NAME, drawerItem.getCat_name());
        values.put(NewsDatabase.DRAWER_CAT_IMAGE, drawerItem.getCat_imag());
        values.put(NewsDatabase.DRAWER_CAT_PARENT, drawerItem.getLevel());
        values.put(NewsDatabase.DRAWER_CAT_ORDER, drawerItem.getOrder());
        try {
            database = newsDatabase.getWritableDatabase();
            database.update(NewsDatabase.DRAWER_TABLE_NAME, values, NewsDatabase.DRAWER_CAT_ID + "=?", new String[]{cat_id});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
        }
    }


    public ArrayList<DbDrawerItem> getAllDrawer() {
        ArrayList<DbDrawerItem> itemArrayList = null;

        try {
            itemArrayList = new ArrayList<DbDrawerItem>();
            database = newsDatabase.getReadableDatabase();
            cursor = database.query(NewsDatabase.DRAWER_TABLE_NAME, null, null, null, null, null, "CAST(" + NewsDatabase.DRAWER_CAT_ID + " AS INTEGER) asc");

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DbDrawerItem item = new DbDrawerItem();
                    item.setCat_id(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_ID)));
                    item.setCat_name(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_NAME)));
                    item.setCat_imag(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_IMAGE)));
                    itemArrayList.add(item);
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

        return itemArrayList;
    }

    // /get all parent from drawer_list table for nav items..
    public ArrayList<DbDrawerItem> getAllParentInfo() {

        ArrayList<DbDrawerItem> itemArrayList = null;
        DbDrawerItem item1 = new DbDrawerItem();

        try {
            itemArrayList = new ArrayList<DbDrawerItem>();
            database = newsDatabase.getReadableDatabase();
            cursor = database.query(NewsDatabase.DRAWER_TABLE_NAME, null, NewsDatabase.DRAWER_CAT_PARENT + "=0", null, null, null, NewsDatabase.DRAWER_CAT_ORDER + " asc");
            if (!cursor.isLast()) {
//                item1.setCat_name("Home");
//                itemArrayList.add(item1);

                while (cursor.moveToNext()) {
                    DbDrawerItem item = new DbDrawerItem();
                    item.setCat_id(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_ID)));
                    item.setCat_name(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_NAME)));
                    item.setLevel(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_PARENT)));
                    item.setCat_imag(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_IMAGE)));
                    itemArrayList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            if (database.isOpen()) {
                database.close();
            }

        }

        return itemArrayList;
    }


    // / /get all child info from drawer_list table for nav items..
    public ArrayList<DbDrawerItem> getAllChildInfo(String cat_id) {

        ArrayList<DbDrawerItem> itemArrayList = null;

        try {
            itemArrayList = new ArrayList<DbDrawerItem>();
            database = newsDatabase.getReadableDatabase();

            cursor = database.query(NewsDatabase.DRAWER_TABLE_NAME, null, NewsDatabase.DRAWER_CAT_PARENT + " = " + cat_id + "", null, null, null, NewsDatabase.DRAWER_CAT_ORDER + " asc");

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DbDrawerItem item = new DbDrawerItem();
                    item.setCat_id(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_ID)));
                    item.setCat_name(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_NAME)));
                    item.setLevel(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_ORDER)));
                    item.setCat_imag(cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_IMAGE)));
                    itemArrayList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }

        return itemArrayList;
    }


    public String getAllDrawerCategoryName(String catid) {

        String cat = "";
        try {
            database = newsDatabase.getReadableDatabase();
            String QUERY = "SELECT " + NewsDatabase.DRAWER_CAT_NAME + " FROM " + NewsDatabase.DRAWER_TABLE_NAME + " where " + NewsDatabase.DRAWER_CAT_ID + " =" + catid;
            cursor = database.rawQuery(QUERY, null);

            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    cat = cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_NAME));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return cat;
    }


    public String[] getAllCatId() {

        ArrayList<String> strings = new ArrayList<>();
        String final_cat = "";
        String[] cat = {""};
        try {
            database = newsDatabase.getWritableDatabase();
            String QUERY = "SELECT " + NewsDatabase.DRAWER_CAT_ID + "  FROM " + NewsDatabase.DRAWER_TABLE_NAME;
            cursor = database.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    final_cat = cursor.getString(cursor.getColumnIndex(NewsDatabase.DRAWER_CAT_ID));
                    strings.add(final_cat + "");
                }
            }

            cat = new String[strings.size()];
            for (int j = 0; j < strings.size(); j++) {
                cat[j] = strings.get(j);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
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
        int num = 0;
        try {
            database = newsDatabase.getReadableDatabase();

            String QUERY = "SELECT * FROM " + NewsDatabase.DRAWER_TABLE_NAME + "  where  " + NewsDatabase.DRAWER_CAT_ID + "=" + catid;
            cursor = database.rawQuery(QUERY, null);
            num = cursor.getCount();
            if (num > 0) {
                return "1";
            } else {
                return "0";
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        } finally {
            if (database.isOpen()) {
                database.close();
            }
            cursor.close();
        }
        return "0";

    }


}
