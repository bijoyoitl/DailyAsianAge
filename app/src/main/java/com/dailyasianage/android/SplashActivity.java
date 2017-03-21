package com.dailyasianage.android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.dailyasianage.android.Database.FavoriteNewsManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.item.DbDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dailyasianage.android.util.CurrentData;
import com.dailyasianage.android.util.HTTPGET;
import com.google.gson.JsonObject;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

    String allNewsIds = "";
    UrlLink urlLink;
    ConnectionDetector detector;
    NewsDatabase database;
    DrawerMenuManager drawerMenuManager;
    AllNewsManager allNewsManager;
    CategoryManager categoryManager;
    FavoriteNewsManager favoriteNewsManager;
    DbDrawerItem dbDrawerItem;
    NewsApis newsApis;

    Boolean isInternetConnection = false;
    String LOG = "SplashScreenActivity.java";
    int progressStatus = 0;
    String allcat = "";
    String allNewsId = "";
    String singleNewsId = "";
    String newsId = "";
    String firstIds = "";
    String lastIds = "";
    String[] updateNewsIds1;
    String[] updateNewsIds2;
    Retrofit retrofit;
    String fIds = "";
    String sIds = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.context = this;
        uiInit();

        if (isInternetConnection) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();

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
        favoriteNewsManager = new FavoriteNewsManager(context);

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
                            } else {
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
                    new CategoryNewsIdTask().execute(allcat);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();

            }

        });
    }

    private class CategoryNewsIdTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            String response = "";
            try {
                response = new HTTPGET().SendHttpRequest(UrlLink.multiCategory + link);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String cat = jsonObject.getString("cat");

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

                        if (singleNewsId.equals("")) {
                            singleNewsId += id;
                        } else {
                            singleNewsId += "," + id;
                        }


                    }
                    Log.e("final", "cat : " + key + " news id : " + singleNewsId);

                    categoryManager.addCatNews(key, singleNewsId);
                    singleNewsId = "";


                }


                ArrayList<String> firstNewsIds;
                firstNewsIds = allNewsManager.getSingleCategoryNewsId(fIds);

                ArrayList<String> lastNewsIds;
                lastNewsIds = allNewsManager.getSingleCategoryNewsId(sIds);

                allNewsId = categoryManager.getCatNewsId(fIds);
                newsId = categoryManager.getCatNewsId(sIds);

                updateNewsIds1 = allNewsId.split(",");
                updateNewsIds2 = newsId.split(",");

                for (String id : updateNewsIds1) {
                    if (!firstNewsIds.contains(id)) {
                        if (firstIds.equals("")) {
                            firstIds += id;
                        } else {
                            firstIds += "," + id;
                        }
                    }
                }

                for (String id : updateNewsIds2) {
                    if (!lastNewsIds.contains(id)) {
                        if (lastIds.equals("")) {
                            lastIds += id;
                        } else {
                            lastIds += "," + id;
                        }
                    }
                }

                CurrentData.initialNewsIds = firstIds;
                CurrentData.othersNewsIds = lastIds;
                CurrentData.allNewsId = allNewsId + newsId + favoriteNewsManager.getFavIds();

            } catch (Exception e) {
                e.printStackTrace();
            }


            circleProgressBar.setVisibility(View.GONE);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (!lastIds.equals("")) {
                Intent intent1 = new Intent(SplashActivity.this, AllNewsService.class);
                startService(intent1);
            }
            finish();
        }
    }

    private void getAllCatNewsId(final String allcat) {

        String link = "app/?module=multi_cat_news&cat=" + allcat;
        final Call<JsonObject> getAllCatNId = newsApis.getAllCatNewsId(link);

        getAllCatNId.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Something Went Wrong....!", Toast.LENGTH_SHORT).show();
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
