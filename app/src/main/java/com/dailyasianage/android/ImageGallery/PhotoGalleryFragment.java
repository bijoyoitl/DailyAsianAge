package com.dailyasianage.android.ImageGallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dailyasianage.android.ConnectionDetector;
import com.dailyasianage.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotoGalleryFragment extends Fragment {

    private String TAG = PhotoGalleryFragment.class.getSimpleName();
    private static final String endpoint = "http://dailyasianage.com/app/?module=img_gallery&start=10";
    private ArrayList<Model> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    public RecyclerView recyclerView;

    ProgressBar progressBar;
    ConnectionDetector detector;
    Boolean isInternet = false;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String myValue = bundle.getString("message");

        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.photoRecyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
        detector = new ConnectionDetector(getContext());
        isInternet = detector.isConnectingToInternet();


        pDialog = new ProgressDialog(getContext());
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(images, getContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchImages();
        return view;
    }

    private void fetchImages() {
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                pDialog.hide();
                images.clear();
                try {
                    String img = response.getString("img");
                    JSONArray jsonArray = new JSONArray(img);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        String title = object.getString("title");
                        String caption = object.getString("caption");
                        String src = object.getString("src");
                        String img_url = "http://dailyasianage.com/library/";
                        Model image = new Model(title, caption, img_url + src);
                        images.add(image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });

        AppController.getInstance(getActivity()).addRequest(request);
    }


}
