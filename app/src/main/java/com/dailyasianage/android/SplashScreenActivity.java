package com.dailyasianage.android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.dailyasianage.android.util.HTTPGET;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SplashScreenActivity extends AppCompatActivity {

    private Context context;
    public static int SPLASH_TIME_OUT = 3000;
    private ProgressBar circleProgressBar;
    private ProgressBar horizontalProgressBar;
    private int progressStatus = 0;
    ConnectionDetector detector;
    Boolean isInternetConnection = false;
    Handler handler = new Handler();
    NewsDatabase database;
    String allcat = "";
    int k = 0;
    String catId;
    ArrayList<DbDrawerItem> itemArrayList = new ArrayList<>();
    ArrayList<DbDrawerItem> itemArray = new ArrayList<>();
    DbDrawerItem drawerItem = null;
    String LOG;
    UrlLink urlLink;
    ArrayList<String> uncommon_news_id = new ArrayList<>();

    DrawerMenuManager drawerMenuManager;
    AllNewsManager allNewsManager;
    CategoryManager categoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.context = this;

        database = new NewsDatabase(context);
        drawerMenuManager=new DrawerMenuManager(context);
        allNewsManager= new AllNewsManager(context);
        categoryManager=new CategoryManager(context);

        detector = new ConnectionDetector(context);
        isInternetConnection = detector.isConnectingToInternet();

        circleProgressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        horizontalProgressBar = (ProgressBar) findViewById(R.id.splashProgressId);
        urlLink = new UrlLink();

        LOG = "SplashScreenActivity.java";


        if (isInternetConnection) {
            new CatInfoTask(context).execute();
        } else {
            circleProgressBar.setVisibility(View.GONE);
            horizontalProgressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() { //thread
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


    public class CatInfoTask extends AsyncTask<Void, Integer, String> {
         Context context;

        public CatInfoTask(Context context) {
            this.context = context;
            database = new NewsDatabase(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circleProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            if (isInternetConnection) {
                response = new HTTPGET().SendHttpRequest("http://dailyasianage.com/app/?module=cat_info");
                try {
                    k = 1;
                    JSONObject jsonObject = new JSONObject(response);
                    String cat = jsonObject.getString("c");
                    jsonObject = new JSONObject(cat);

                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();

                        try {
                            String category = jsonObject.getString(key);
                            JSONObject jsonObject1 = new JSONObject(category);
                            catId = jsonObject1.getString("id");
                            String title = jsonObject1.getString("title");
                            String img = jsonObject1.getString("img");
                            String imag = "http://dailyasianage.com/images/cat_icon/";
                            String level = jsonObject1.getString("parent");
                            int order = jsonObject1.getInt("order");

                            DbDrawerItem item = new DbDrawerItem(catId, title, imag + img, level, order);

                            itemArrayList.add(item);
                            drawerItem = new DbDrawerItem(catId);
                            itemArray.add(drawerItem);

                            if (allcat.equals("")) {
                                allcat += catId;
                            } else {
                                allcat += "," + catId;
                            }

                            if (drawerMenuManager.getDrawerCatExist(catId).equals("0")) {
                                drawerMenuManager.addDrawerList(item);
                            }else {
                                drawerMenuManager.updateDrawerList(item,catId);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (database.getCustomizeId().equals("0")) {
                        for (DbDrawerItem drawerItem : itemArray) {
                            if (database.getCustomizeExit(drawerItem.getCat_id()).equals("0")) {
                                database.addCustomNews(drawerItem.getCat_id());  //Add cat id into  home customize table...
                            }
                        }
                    }

//                    Log.e("Splash.java", " cat id : " + allcat);

                    String request = new HTTPGET().SendHttpRequest(urlLink.multiCategory + allcat);
                    try {
                        jsonObject = new JSONObject(request);
                        String cat_news = jsonObject.getString("cat");
                        jsonObject = new JSONObject(cat_news);
                        Iterator<String> iter = jsonObject.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                cat_news = jsonObject.getString(key);
                                Log.e("SP","cat news id : "+cat_news);

                                ArrayList<String> server_news_id_arrayList = new ArrayList<String>();
                                ArrayList<String> local_id_arrayList = new ArrayList<String>();

                                JSONArray server_news_id_Array = new JSONArray(cat_news);

                                if (server_news_id_Array != null) {
                                    for (int i = 0; i < server_news_id_Array.length(); i++) {
                                        server_news_id_arrayList.add(server_news_id_Array.get(i).toString());
                                    }
                                }


                                String old_cat = categoryManager.getCatNews(String.valueOf(key));
                                JSONObject jsonObject1 = new JSONObject(old_cat);
                                String cat_old = jsonObject1.getString("nid");
                                JSONArray jArray2 = new JSONArray(cat_old);
                                if (jArray2 != null) {
                                    for (int i = 0; i < jArray2.length(); i++) {
                                        local_id_arrayList.add(jArray2.get(i).toString());
                                    }
                                }

                                for (String a : local_id_arrayList) {
                                    Log.e("Splash.java", " local db id : " + a);
                                    if (!server_news_id_arrayList.contains(a)) {
                                        uncommon_news_id.add(a);
                                    }
                                }
                                for (String b : server_news_id_arrayList) {
                                    Log.e("Splash.java", " server db id : " + b);
                                    if (!local_id_arrayList.contains(b)) {
                                        uncommon_news_id.add(b);
                                    }
                                }

                                Log.e("Splash.java", " un common : " + uncommon_news_id.toString());
                                if (!uncommon_news_id.equals("")) {
                                    Log.e("SA","uncommon : "+!uncommon_news_id.equals(""));
                                    categoryManager.addCatNews(String.valueOf(key), cat_news);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (k == 1)

                {
                    allNewsManager.deleteOldNews(allcat);
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }
    }

}
