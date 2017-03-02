package com.dailyasianage.android.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.News;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    int cat_id;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView cat_titleTextView;
    NewsDatabase database;
    ArrayList<News> arrayList = new ArrayList<>();
    Context context;
    RecyclerAdapter recyclerAdapter;

    public FavoriteFragment() {
        this.cat_id = 3;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String myValue = bundle.getString("message");
        View view = inflater.inflate(R.layout.fragment_all_cat, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
        cat_titleTextView = (TextView) view.findViewById(R.id.cat_titleTextView);
        database = new NewsDatabase(getActivity());
        progressBar.setVisibility(View.GONE);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 5 == 0) {
                    return 2;
                } else
                    return 1;
            }

        });

        recyclerView.setLayoutManager(manager);
        FavNews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList = database.getFav();
        recyclerAdapter = new RecyclerAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void FavNews() {
        arrayList = database.getFav();
        recyclerAdapter = new RecyclerAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(recyclerAdapter);
    }


}
