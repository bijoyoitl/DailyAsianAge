package com.dailyasianage.android.Fragments;

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
import android.widget.TextView;

import com.dailyasianage.android.Adpter.FrontPageAdapter;
import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.ConnectionDetector;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.CategoryManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.News;
import com.dailyasianage.android.util.CatNewsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class FrontPageFragment extends Fragment {

    public static ProgressBar progressBar;
    public RecyclerView recyclerView;
    public GridLayoutManager manager;
    public int currentP = 0;
    public TextView cat_titleTextView;
    public static String[] catName = {"FrontPage", "Asia", "City", "World", "OP-ED", "Editorial", "Business", "Global Business", "Share Market", "Commercial Capital", "Sports", "Countrywide", "Entertainment", "Special Supplement", "Inscription", "Life Style", "Reciprocal", "Teens & Twenties", "In Vogue", "Food & Nutrition", "Bookshelf", "Back Page"};
    ConnectionDetector detector;
    Boolean isInternetConnection = false;
    public String[] catID = {" "};
    public String catnews;
    public static String final_news_id = "";
    public static JSONArray catPosition = new JSONArray();
    public ArrayList<News> newsIds = new ArrayList<>();
    private String cat_position = "";


    private NewsDatabase database;
    private JSONArray array;
    String catNewsId;
    int l = 0;
    public static String idList = "";


    ArrayList nId = new ArrayList();
    int id;
    String tempNewsIds = "";
    private boolean delete = false;
    public static boolean value = true;
    private boolean isTrue = false;

    AllNewsManager allNewsManager;
    CategoryManager categoryManager;

    public FrontPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frontpage, container, false);

        detector = new ConnectionDetector(getContext());
        database = new NewsDatabase(getContext());
        allNewsManager=new AllNewsManager(getActivity());
        categoryManager=new CategoryManager(getActivity());
        isInternetConnection = detector.isConnectingToInternet();

        progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        cat_titleTextView = (TextView) view.findViewById(R.id.cat_titleTextView);
        catID = database.getCustomCat();
        cat_position = database.getCat_position();

//        manager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);

//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//
//                currentP++;
//                for (int x = 0; x < catPosition.length(); x++) {
//                    try {
//                        if (Integer.parseInt(catPosition.getString(x)) == position) {
//                            currentP = 0;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (currentP == 0) {
//                    return 2;
//
//                } else {
//                    return 1;
//                }
//
//            }
//
//        });
        manager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
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
            Log.e("FrontPageFragment.java", " front page id after call: " + isTrue);
            callCatNews();
//            new Thread(new CallAllCatTask()).start();
        } else {
            try {
                catPosition = new JSONArray(cat_position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            for (String cat : catID) {
                if (final_news_id.equals("")) {
                    final_news_id += cat;
                } else {
                    final_news_id += ',' + cat;
                }
//                newsIds = database.getAllOfflineNews(final_news_id);
                newsIds =  allNewsManager.getCategoryNews(String.valueOf(1));
            }
//            recyclerView.setAdapter(new FrontPageAdapter(getActivity(), newsIds, catName, catPosition));
            recyclerView.setAdapter(new RecyclerAdapter(getActivity(), newsIds));
        }

        if (delete) {
            allNewsManager.deleteOldNews(tempNewsIds);
        }

        return view;
    }

    public void getCatNewsId() {
        JSONObject dbobject;
        JSONArray allNewsId;
        int catRun = 0;
        try {
            for (int h = 0; h < catID.length; h++) {
                String dbArray = categoryManager.getCatNews(String.valueOf(catID[h]));
                dbobject = new JSONObject(dbArray);
                catnews = dbobject.getString("nid");
                array = new JSONArray(catnews);
                if (array.length() > 0) {
                    catPosition.put(catRun);
                    for (int xyz = 0; xyz < array.length(); xyz++) {
                        catRun++;
                        if (tempNewsIds == "") {
                            tempNewsIds += array.getString(xyz);
                        } else {
                            tempNewsIds += "," + array.getString(xyz);
                        }

                    }
                }
            }
            allNewsId = new JSONArray("[" + tempNewsIds + "]");
            l = 0;
            for (int i = 0; i < allNewsId.length(); i++) {
                catNewsId = String.valueOf(allNewsId.getString(i));
                isTrue = allNewsManager.getNewsAllExist(allNewsId.getInt(i)) == 0;
                Log.e("FrontPageFragment.java", " front page id : " + isTrue);

                if (!Arrays.asList(catName).contains(catNewsId)) {
                    if (final_news_id.equals("")) {
                        final_news_id += catNewsId;
                    } else {
                        final_news_id += ',' + catNewsId;
                    }
                    id = Integer.parseInt(allNewsId.getString(i));
                    if (allNewsManager.getNewsExist(id) == 0) {
                        //  Log.e(LOG, "New News = " + id);
                        nId.add(id);
                    }

                } else {
                    nId.add(catNewsId);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (nId.size() > 1) {

            l = 0;
            for (Object ni : nId) {

                String j = String.valueOf(ni);
                if (!Arrays.asList(catName).contains(j)) {
                    if (l == 0) {
                        idList += j;
                    } else {
                        idList += ',' + j;
                    }
                }
                l++;
            }

        }

        String jsonString = catPosition.toString();
        database.updateCatPosition(jsonString);
    }

    public void callCatNews() {
        delete = true;
        new CatNewsTask(getActivity(), value, recyclerView, idList).execute();

    }
//    private class CallAllCatTask implements Runnable{
//
//        @Override
//        public void run() {
//            delete = true;
//            new CatNewsTask(getActivity(), value, recyclerView, idList).execute();
//
//        }
//    }


}


