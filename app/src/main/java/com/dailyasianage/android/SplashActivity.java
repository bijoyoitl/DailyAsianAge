package com.dailyasianage.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.CategoryManager;
import com.dailyasianage.android.Database.DrawerMenuManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.item.DbDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dailyasianage.android.util.CurrentData;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ripon on 3/12/2017.
 */

public class SplashActivity extends AppCompatActivity {

    Context context;

    ProgressBar circleProgressBar;
    ProgressBar horizontalProgressBar;

    Handler handler;

    UrlLink urlLink;
    ConnectionDetector detector;
    NewsDatabase database;
    DrawerMenuManager drawerMenuManager;
    AllNewsManager allNewsManager;
    CategoryManager categoryManager;
    DbDrawerItem dbDrawerItem;
    NewsApis newsApis;
    Retrofit retrofit;

    Boolean isInternetConnection = false;
    String LOG = "SplashScreenActivity.java";
    int progressStatus = 0;
    String allcat = "";
    String allNewsId = "";
    String singleNewsId = "";
    String newsId = "";
    ArrayList<String> updateNewsIds;

    String fIds = "";
    String sIds = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.context = this;

        updateNewsIds = new ArrayList<>();
        uiInit();

        if (isInternetConnection) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlLink.baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
            newsApis = retrofit.create(NewsApis.class);

            getAllCategoryInfo();
        } else {
            splashTimeOut();
        }

    }

    private void uiInit() {
        circleProgressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        horizontalProgressBar = (ProgressBar) findViewById(R.id.splashProgressId);
        database = new NewsDatabase(context);
        drawerMenuManager = new DrawerMenuManager(context);
        allNewsManager = new AllNewsManager(context);
        categoryManager = new CategoryManager(context);

        detector = new ConnectionDetector(context);
        isInternetConnection = detector.isConnectingToInternet();
        urlLink = new UrlLink();
        handler = new Handler();
    }


    private void getAllCategoryInfo() {

        String link = "app/?module=cat_info";
        final Call<JsonObject> getAllCatInfo = newsApis.getAllCategoryInfo(link);

        getAllCatInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {

                    JSONObject jsonObject = new JSONObject(response.body() + "");
                    String cat = jsonObject.getString("c");
                    jsonObject = new JSONObject(cat);
                    int i = 0;
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String catId = iterator.next();

                        try {
                            String category = jsonObject.getString(catId);
                            JSONObject jsonObject1 = new JSONObject(category);
                            String id = jsonObject1.getString("id");
                            String title = jsonObject1.getString("title");
                            String img = jsonObject1.getString("img");
                            String level = jsonObject1.getString("parent");
                            int order = jsonObject1.getInt("order");
                           i++;

                            if (i < 6) {
                                if (fIds.equals("")) {
                                    fIds += id;
                                } else {
                                    fIds += "," + id;
                                }
                            }else {
                                if (sIds.equals("")) {
                                    sIds += id;
                                } else {
                                    sIds += "," + id;
                                }

                            }

                            if (allcat.equals("")) {
                                allcat += id;
                            } else {
                                allcat += "," + id;
                            }

                            dbDrawerItem = new DbDrawerItem(id, title, img, level, order);
                            if (drawerMenuManager.getDrawerCatExist(catId).equals("0")) {
                                drawerMenuManager.addDrawerList(dbDrawerItem);
                            } else {
                                drawerMenuManager.updateDrawerList(dbDrawerItem, catId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.e("SA", allcat);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getAllCatNewsId(allcat);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void getAllCatNewsId(String allcat) {

        String link = "app/?module=multi_cat_news&cat=" + allcat;
        final Call<JsonObject> getAllCatNId = newsApis.getAllCatNewsId(link);

        getAllCatNId.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body() + "");
                    String cat = jsonObject.getString("cat");
                    Log.e("M", cat);
                    jsonObject = new JSONObject(cat);
                    Iterator<String> iterator = jsonObject.keys();

                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Log.e("Key", key);

                        cat = jsonObject.getString(key);
                        Log.e("cat", cat);

                        JSONArray jsonArray = new JSONArray(cat);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String id = jsonArray.getString(i);
                            updateNewsIds.add(id);

                            if (singleNewsId.equals("")) {
                                singleNewsId += id;
                            } else {
                                singleNewsId += "," + id;
                            }


                        }
//                        Log.e("final","cat : "+key+" news id : "+cat_id);

                        categoryManager.addCatNews(key, singleNewsId);
                        singleNewsId = "";


                        ArrayList<String> strings;
                        strings = allNewsManager.getAllNewsId();


/*
                        for (String id: updateNewsIds){
                            if (!strings.contains(id)){
                                if (allNewsId.equals("")) {
                                    allNewsId += id;

                                } else {
                                    allNewsId += "," + id;
                                }
                            }
                        }
*/


                    }
                    allNewsId = categoryManager.getCatNewsId(fIds);
                    newsId = categoryManager.getCatNewsId(sIds);

                    CurrentData.initialNewsIds = allNewsId;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                long f = System.currentTimeMillis();
                final String end =
                        new SimpleDateFormat("HH:mm:ss:SSS").format(f);
                Log.e("end time : ", end);

                circleProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Intent intent1 = new Intent(SplashActivity.this, AllNewsService.class);
                intent1.putExtra("ids", newsId);
                startService(intent1);


                finish();
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void splashTimeOut() {

        circleProgressBar.setVisibility(View.GONE);
        horizontalProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            horizontalProgressBar.setProgress(progressStatus);
                        }
                    });

                    try {
                        Thread.sleep(25); //progress bar waiting time.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (progressStatus == 100) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }
        }).start();
    }

}
