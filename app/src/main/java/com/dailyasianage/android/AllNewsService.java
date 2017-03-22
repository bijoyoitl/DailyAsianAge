package com.dailyasianage.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.View;

import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.CategoryManager;
import com.dailyasianage.android.item.NewsAll;
import com.dailyasianage.android.item.NewsMain;
import com.dailyasianage.android.util.CurrentData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllNewsService extends Service {

    NewsApis newsApis;
    Retrofit retrofit;
    String d = "";
    Context context;
    String allNewsId = "";
    String base_url = "http://dailyasianage.com/";
    CategoryManager categoryManager;
    AllNewsManager allNewsManager;


    public AllNewsService() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;
        allNewsManager = new AllNewsManager(context);
        categoryManager = new CategoryManager(context);
        try {
            if (intent != null) {
                allNewsId = CurrentData.othersNewsIds;
                getAllData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getAllData() {


        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        newsApis = retrofit.create(NewsApis.class);

        String link = "/app/?module=news&n=" + allNewsId;
        Call<NewsMain> newsMainCall = newsApis.getAllNews(link);

        newsMainCall.enqueue(new Callback<NewsMain>() {
            @Override
            public void onResponse(Call<NewsMain> call, Response<NewsMain> response) {
                NewsMain newsMain = response.body();

//                Log.e("NA", String.valueOf(newsMain.getStatus()));

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

//                    if (d.equals("")) {
//                        d += id;
//                    } else {
//                        d += "," + id;
//                    }

                    NewsAll newsAll = new NewsAll(id, catId, shoulder, publishTime, publishSerial, nTopNews, nHomeSlider, nInside, heading, subHeading, reporter, details, image);

                    if (allNewsManager.getNewsExist(Integer.parseInt(id)) == 0) {
                        allNewsManager.addAllNews(newsAll);
                    }

                }


//                Log.e("service arr cat : ", d);

                allNewsManager.deleteOldNews(CurrentData.allNewsId);
//                Log.e("all news id :  dd ", CurrentData.allNewsId);

            }

            @Override
            public void onFailure(Call<NewsMain> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

}
