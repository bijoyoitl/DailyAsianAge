package com.dailyasianage.android.Fragments;

import android.annotation.SuppressLint;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllCatFragment extends Fragment {
    public int cat_id;
    private RecyclerView recyclerView;
    ProgressBar progressBar2;
    ProgressBar progressBar;
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

    CategoryManager categoryManager;
    AllNewsManager allNewsManager;

    Retrofit retrofit;
    NewsApis newsApis;
    ArrayList<String> serverNewsId;
    ArrayList<String> localNewsId;
    String updateNewsIds = "";
    String serverNewsIds = "";

    public AllCatFragment() {
    }

    @SuppressLint("ValidFragment")
    public AllCatFragment(int cat_id) {
        super();
        this.cat_id = cat_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_cat, container, false);
        database = new NewsDatabase(getActivity());
        detector = new ConnectionDetector(getContext());
        allNewsManager = new AllNewsManager(getActivity());
        categoryManager = new CategoryManager(getActivity());
        serverNewsId = new ArrayList<>();
        localNewsId = new ArrayList<>();

        urlLink = new UrlLink();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar);


        isInternetConnection = detector.isConnectingToInternet();
        progressBar.setVisibility(View.VISIBLE);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);

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

        if (isInternetConnection) {
            newsIds = allNewsManager.getCategoryNews(String.valueOf(cat_id));
            recyclerAdapter = new RecyclerAdapter(getActivity(), newsIds);
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclerAdapter);
            progressBar.setVisibility(View.GONE);
            getCatNewsId();
        } else {
            newsIds = allNewsManager.getCategoryNews(String.valueOf(cat_id));
            recyclerAdapter = new RecyclerAdapter(getActivity(), newsIds);
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclerAdapter);
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    public void getCatNewsId() {
//        progressBar2.setVisibility(View.VISIBLE);
        String link = "app/?module=cat_news&cat=" + cat_id;
        retrofit = new Retrofit.Builder().baseUrl(UrlLink.baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        newsApis = retrofit.create(NewsApis.class);

        Call<JsonObject> getSingleCatNewsIds = newsApis.getSingleCatNewsId(link);

        getSingleCatNewsIds.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body() + "");

                    String nid = jsonObject.getString("nid");
                    JSONArray jsonArray = new JSONArray(nid);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id = jsonArray.getString(i);
                        serverNewsId.add(id);

                        if (serverNewsIds.equals("")) {
                            serverNewsIds += id;
                        } else {
                            serverNewsIds += "," + id;
                        }

                    }

                    String dbNewsIds = categoryManager.getCatNewsId(cat_id + "");
//                    Log.e("ACAT", "db ids : " + dbNewsIds);
                    String[] strValues = dbNewsIds.split(",");

                    localNewsId = new ArrayList<String>(Arrays.asList(strValues));

                    for (String id : serverNewsId) {
                        if (!localNewsId.contains(id)) {
                            if (updateNewsIds.equals("")) {
                                updateNewsIds += id;
                            } else {
                                updateNewsIds += "," + id;
                            }

                        }
                    }

//                    Log.e("ACAT", "update ids : " + updateNewsIds);

                    if (!updateNewsIds.equals("")) {
                        getAllData(updateNewsIds);
                        categoryManager.addCatNews(cat_id + "", serverNewsIds);
                    } else {
                        progressBar2.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private void getAllData(String newsId) {
//        Log.e("NA", String.valueOf(123));

        retrofit = new Retrofit.Builder().baseUrl(UrlLink.baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        newsApis = retrofit.create(NewsApis.class);

        String link = "/app/?module=news&n=" + newsId;
        Call<NewsMain> newsMainCall = newsApis.getAllNews(link);

        newsMainCall.enqueue(new Callback<NewsMain>() {
            @Override
            public void onResponse(Call<NewsMain> call, Response<NewsMain> response) {
                NewsMain newsMain = response.body();


                ArrayList<NewsAll> newsAlls = (ArrayList<NewsAll>) newsMain.getNews();

                for (int i = 0; i < newsAlls.size(); i++) {
                    String id = newsAlls.get(i).getId();
//                    Log.e("NA", String.valueOf(id));
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


                newsIds = allNewsManager.getCategoryNews(String.valueOf(cat_id));
                recyclerAdapter = new RecyclerAdapter(getActivity(), newsIds);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);
                progressBar2.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<NewsMain> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

}


