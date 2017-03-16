package com.dailyasianage.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.CategoryManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.item.News;
import com.dailyasianage.android.item.NewsAll;
import com.dailyasianage.android.item.NewsMain;
import com.dailyasianage.android.util.HTTPGET;
import com.dailyasianage.android.util.Utils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ripon on 3/13/2017.
 */

public class NewsCheckClass extends AppCompatActivity {
    //    String allNewsId = "";
    String allNewsId = "";
    NewsApis newsApis;
    Retrofit retrofit;
    String d = "";


    public int cat_id;
    private RecyclerView recyclerView;
    public static ProgressBar progressBar;
    private ConnectionDetector detector;
    private Boolean isInternetConnection = false;
    private NewsDatabase database;
    private String cat_news_ids;
    private String final_cat_id = "";
    private int k;
    public ArrayList<News> newsIds = new ArrayList<>();
    public RecyclerAdapter recyclerAdapter;
    private boolean isTrue = false;
    private static boolean value = false;
    Context context;

    CategoryManager categoryManager;
    AllNewsManager allNewsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_cat);

        this.context = this;

        database = new NewsDatabase(this);
        detector = new ConnectionDetector(this);
        isInternetConnection = detector.isConnectingToInternet();
        allNewsManager = new AllNewsManager(this);
        categoryManager = new CategoryManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.loading_progressBar);


        isInternetConnection = detector.isConnectingToInternet();
        progressBar.setVisibility(View.VISIBLE);

        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { //recycler position set
            @Override
            public int getSpanSize(int position) {
                if (position % 5 == 0) {
                    return 2;
                } else
                    return 1;
            }

        });

        recyclerView.setLayoutManager(manager);


        allNewsId = getIntent().getStringExtra("newsId");
        Log.e("N"," ca : "+ allNewsId);

        if (isInternetConnection) {
            if (allNewsId.equals("")) {
                newsIds = allNewsManager.getAlNews();
                recyclerAdapter = new RecyclerAdapter(context, newsIds);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);
                progressBar.setVisibility(View.GONE);
            } else {
                getAllData();
            }
        } else {
            newsIds = allNewsManager.getAlNews();
            recyclerAdapter = new RecyclerAdapter(context, newsIds);
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclerAdapter);
            progressBar.setVisibility(View.GONE);
        }


    }


    private void getAllData() {


        retrofit = new Retrofit.Builder().baseUrl(UrlLink.baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        newsApis = retrofit.create(NewsApis.class);

        String link = "/app/?module=news&n=" + allNewsId;
        Call<NewsMain> newsMainCall = newsApis.getAllNews(link);

        newsMainCall.enqueue(new Callback<NewsMain>() {
            @Override
            public void onResponse(Call<NewsMain> call, Response<NewsMain> response) {
                NewsMain newsMain = response.body();

                Log.e("NA", String.valueOf(newsMain.getStatus()));

                ArrayList<NewsAll> newsAlls = (ArrayList<NewsAll>) newsMain.getNews();

                for (int i = 0; i < newsAlls.size(); i++) {
                    String id = newsAlls.get(i).getId();
                    String catId = newsAlls.get(i).getCatId();
                    String shoulder = newsAlls.get(i).getShoulder();
                    String publishTime = newsAlls.get(i).getPublishTime();
                    String publishSerial = newsAlls.get(i).getPublishSerial();
                    String nTopNews = newsAlls.get(i).getNTopNews();
                    String nHomeSlider = newsAlls.get(i).getNHomeSlider();
                    String nInside = newsAlls.get(i).getNInsideNews();
                    String heading = newsAlls.get(i).getHeading();
                    String subHeading = newsAlls.get(i).getSubHeading();
                    String reporter = newsAlls.get(i).getReporter();
                    String details = newsAlls.get(i).getDetails();
                    String image = newsAlls.get(i).getImage();

                    if (d.equals("")) {
                        d += id;
                    } else {
                        d += "," + id;
                    }

                    NewsAll newsAll = new NewsAll(id, catId, shoulder, publishTime, publishSerial, nTopNews, nHomeSlider, nInside, heading, subHeading, reporter, details, image);

                    if (allNewsManager.getNewsExist(Integer.parseInt(id)) == 0) {
                        allNewsManager.addAllNews(newsAll);
                    }

                }


                Log.e("arr cat : ", d);
//                NewsAll  newsAll= new NewsAll(catId, id, heading, sub_heading, shoulder, pub_time, reporter, details, img, publish_serial, top_news, home_slider, inside_news);


                newsIds = allNewsManager.getAlNews();
                recyclerAdapter = new RecyclerAdapter(context, newsIds);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<NewsMain> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
}
