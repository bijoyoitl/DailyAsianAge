package com.dailyasianage.android.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.ConnectionDetector;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.CategoryManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.NewsApis;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.NewsAll;
import com.dailyasianage.android.item.NewsMain;
import com.dailyasianage.android.util.CurrentData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InitialNewsFragment extends Fragment {

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
    private UrlLink urlLink;
    private String cat_news_ids;
    private String final_cat_id = "";
    private int k;
    public ArrayList<NewsAll> newsIds = new ArrayList<>();
    public RecyclerAdapter recyclerAdapter;
    private boolean isTrue = false;
    private static boolean value = false;
    Context context;

    CategoryManager categoryManager;
    AllNewsManager allNewsManager;


    public InitialNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initaial_news, container, false);
        this.context = getActivity();

        database = new NewsDatabase(context);
        detector = new ConnectionDetector(context);
        isInternetConnection = detector.isConnectingToInternet();
        allNewsManager = new AllNewsManager(context);
        categoryManager = new CategoryManager(context);

        urlLink = new UrlLink();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);


        isInternetConnection = detector.isConnectingToInternet();
        progressBar.setVisibility(View.VISIBLE);

        GridLayoutManager manager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);

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

        allNewsId = CurrentData.initialNewsIds;

        if (isInternetConnection) {
            if (allNewsId.equals("")) {
                newsIds = allNewsManager.getCategoryNews("1");
                recyclerAdapter = new RecyclerAdapter(context, newsIds);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);
                progressBar.setVisibility(View.GONE);
            } else {
                newsIds = allNewsManager.getCategoryNews("1");
                recyclerAdapter = new RecyclerAdapter(context, newsIds);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);
                getAllData();
            }
        } else {
            newsIds = allNewsManager.getCategoryNews("1");
            recyclerAdapter = new RecyclerAdapter(context, newsIds);
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclerAdapter);
            progressBar.setVisibility(View.GONE);
        }

        return view;
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

                    NewsAll newsAll = new NewsAll(id, catId, shoulder, publishTime, publishSerial, nTopNews, nHomeSlider, nInside, heading, subHeading, reporter, details, image);

                    if (allNewsManager.getNewsExist(Integer.parseInt(id)) == 0) {
                        allNewsManager.addAllNews(newsAll);
                    }

                }


                newsIds = allNewsManager.getCategoryNews("1");
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
