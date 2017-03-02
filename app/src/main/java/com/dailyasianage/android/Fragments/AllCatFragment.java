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
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.News;
import com.dailyasianage.android.util.CatNewsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCatFragment extends Fragment {
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
    public ArrayList<News> newsIds = new ArrayList<>();
    public RecyclerAdapter recyclerAdapter;
    private boolean isTrue= false;
    private static boolean value= false;


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
        urlLink = new UrlLink();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
        detector = new ConnectionDetector(getContext());

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
        getCatNewsId();

        if (isInternetConnection && isTrue == true) {
//            Log.e("AllCatFragment.java", " value : "+isTrue);
          callCatNews();
        } else {
            newsIds = database.getCategoryNews(String.valueOf(cat_id));
            recyclerAdapter = new RecyclerAdapter(getActivity(), newsIds);
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclerAdapter);
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    public void getCatNewsId() {

        try {
            String catNews = database.getCatNews(String.valueOf(cat_id));
            JSONObject jsonObject = new JSONObject(catNews);
            String cat = jsonObject.getString("nid");
            JSONArray jsonArray = new JSONArray(cat);

            for (int i = 0; i < jsonArray.length(); i++) {
                cat_news_ids = jsonArray.getString(i);
                isTrue = database.getNewsAllExist(jsonArray.getInt(i))== 0;

                if (k == 0) {
                    final_cat_id += cat_news_ids;
                } else {
                    final_cat_id += ',' + cat_news_ids;
                }
                k++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void callCatNews() {
        new CatNewsTask(getActivity(),value, recyclerView, cat_id, final_cat_id).execute();
    }

}


