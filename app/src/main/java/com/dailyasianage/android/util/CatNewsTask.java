package com.dailyasianage.android.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dailyasianage.android.Adpter.FrontPageAdapter;
import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.Fragments.AllCatFragment;
import com.dailyasianage.android.Fragments.FrontPageFragment;
import com.dailyasianage.android.item.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bijoy on 10/6/2016.
 */

public class CatNewsTask extends AsyncTask<String, String, String> {
    private Context context;
    private RecyclerView recyclerView;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private UrlLink urlLink = new UrlLink();
    private News news;
    private NewsDatabase database;
    private String idList;
    private RecyclerAdapter recyclerAdapter;
    private FrontPageAdapter frontPageAdapter;
    private ArrayList<News> newsArrayList;
    private ArrayList<News> newsAllArrayList;
    private int cat_id;
    private boolean value;

    public CatNewsTask(Context context, boolean value, RecyclerView recyclerView, int cat_id, String idList) {
        this.context = context;
        this.value = value;
//        Log.e("CatNewsTask.java", "value : "+value);
        this.recyclerView = recyclerView;
        this.idList = idList;
        this.cat_id = cat_id;
        database = new NewsDatabase(context);
    }

    public CatNewsTask(Context context, boolean value, RecyclerView recyclerView, String idList) {
        this.context = context;
        this.value = value;
        this.recyclerView = recyclerView;
        this.idList = idList;
        database = new NewsDatabase(context);
    }


    @Override
    protected String doInBackground(String... params) {
        String result = new HTTPGET().SendHttpRequest(urlLink.newsIdLink + idList); //send news request at server
        try {
            jsonObject = new JSONObject(result);
            jsonArray = new JSONArray(jsonObject.getString("news")); //get all news and put into json array


            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                String catId = jsonObject.getString("cat_id");
                String id = jsonObject.getString("id");

                String heading = jsonObject.getString("heading");

                String sub_heading;
                if (jsonObject.isNull("sub_heading")) {
                    sub_heading = null;
                } else {
                    sub_heading = jsonObject.getString("sub_heading");
                }

                String shoulder = jsonObject.getString("shoulder");
                String pub_time = jsonObject.getString("publish_time");
                int publish_serial = jsonObject.getInt("publish_serial");
                int top_news = jsonObject.getInt("n_top_news");
                int home_slider = jsonObject.getInt("n_home_slider");
                int inside_news = jsonObject.getInt("n_inside_news");

                String reporter;
                if (jsonObject.isNull("reporter")) {
                    reporter = null;
                } else {
                    reporter = jsonObject.getString("reporter");
                }

                String details = jsonObject.getString("details");

                String image = jsonObject.getString("image");

                String imageLink = urlLink.imageLink;


                news = new News(catId, id, heading, sub_heading, shoulder, pub_time, reporter, details, imageLink + image, publish_serial, top_news, home_slider, inside_news);

                if (database.getNewsExist(Integer.parseInt(id)) == 0) {
                    database.addNews(news);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (value == false) {
            AllCatFragment.progressBar.setVisibility(View.GONE);
            newsArrayList = database.getCategoryNews(String.valueOf(cat_id));
            recyclerAdapter = new RecyclerAdapter(context, newsArrayList);
            recyclerView.setAdapter(recyclerAdapter);
        } else {
            FrontPageFragment.progressBar.setVisibility(View.GONE);
//            newsAllArrayList = database.getAllNews(1 + "");
            newsArrayList = database.getCategoryNews(String.valueOf(1));
            recyclerAdapter = new RecyclerAdapter(context, newsArrayList);
//            frontPageAdapter = new FrontPageAdapter(context, newsAllArrayList, FrontPageFragment.catName, FrontPageFragment.catPosition);
            recyclerView.setAdapter(recyclerAdapter);
        }

    }
}
