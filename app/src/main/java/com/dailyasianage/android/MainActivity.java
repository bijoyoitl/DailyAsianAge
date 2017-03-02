package com.dailyasianage.android;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dailyasianage.android.Adpter.ExAdapter;
import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.Fragments.AllCatFragment;
import com.dailyasianage.android.Fragments.FavoriteFragment;
import com.dailyasianage.android.Fragments.FrontPageFragment;
import com.dailyasianage.android.ImageGallery.PhotoGalleryFragment;
import com.dailyasianage.android.item.DbDrawerItem;
import com.dailyasianage.android.item.News;
import com.dailyasianage.android.util.HTTPGET;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fragmentManager;
    private Context context;
    private TextView titleTextView;
    private DrawerLayout drawer_layout;
    private NavigationView navigationView;
    private LinearLayout slideId;
    LinearLayout linearLayout;
    private ImageView settingImageView;
    private ImageView favoriteViewImageView;
    private ImageView photoGalleryImageView;
    private LinearLayout titleLayout;
    public int catid = 0;
    private LinearLayout homeSettingImageView;
    private ExpandableListView expandableListView;
    public ArrayList<DbDrawerItem> heading = new ArrayList<DbDrawerItem>();
    public ArrayList<DbDrawerItem> childs = new ArrayList<DbDrawerItem>();
    NewsDatabase database;
    public String cat;
    public String cat_name;
    ExAdapter adapter;

    Fragment fragment = null;
    Fragment fragment2 = null;
    Handler handler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        database = new NewsDatabase(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        slideId = (LinearLayout) findViewById(R.id.slideId);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        settingImageView = (ImageView) findViewById(R.id.settingImageView);
        favoriteViewImageView = (ImageView) findViewById(R.id.favoriteViewImageView);
        photoGalleryImageView = (ImageView) findViewById(R.id.photoGalleryImageView);
        linearLayout = (LinearLayout) findViewById(R.id.topnewsLayout);
        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        homeSettingImageView = (LinearLayout) findViewById(R.id.homeSettingImageView);
        expandableListView = (ExpandableListView) findViewById(R.id.exListview);

        expandableListView.setDividerHeight(2);
        expandableListView.setGroupIndicator(null);
        expandableListView.setClickable(true);


        Intent i = new Intent(MainActivity.this, BackgroundUpdateService.class);
        MainActivity.this.startService(i);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                cat = heading.get(i).getCat_id();
                cat_name = heading.get(i).getCat_name();
                if (i == 0) {
                    fragment2 = new FrontPageFragment();
                    titleLayout.setVisibility(View.GONE);
                    if (fragment2 != null) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentF, fragment2).commit();
                        drawer_layout.closeDrawer(navigationView);
                    }
                }

                if (adapter.getChildrenCount(i) == 0) {
                    childs = database.getAllChildInfo(cat);
//                    database.close();
                    adapter.addChildren(i, childs);

                    if (cat != null) {
                        int cat_id = Integer.parseInt(cat);
                        if (childs.size() == 0) {
                            titleLayout.setVisibility(View.VISIBLE);
                            fragment = new AllCatFragment(cat_id);
                            titleTextView.setText(heading.get(i).getCat_name());
                            if (fragment != null) {
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contentF, fragment).commit();
                                drawer_layout.closeDrawer(navigationView);
                            }

                        }

                    }

                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int i, int i1, long l) {
                drawer_layout.closeDrawer(navigationView);
                cat = childs.get(i1).getCat_id();

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(i, i1));
                parent.setItemChecked(index, true);
                titleLayout.setVisibility(View.VISIBLE);
                int cat_id = Integer.parseInt(cat);
                fragment = new AllCatFragment(cat_id);
                titleTextView.setText(childs.get(i1).getCat_name());
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentF, fragment).commit();
                    drawer_layout.closeDrawer(navigationView);
                }
                return true;
            }

        });


        homeSettingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeCustomizeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        photoGalleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String myMessage = "Gallery";
                bundle.putString("message", myMessage);
                PhotoGalleryFragment fragInfo = new PhotoGalleryFragment();
                fragInfo.setArguments(bundle);
                titleLayout.setVisibility(View.VISIBLE);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentF, fragInfo).commit();
                titleTextView.setText("Photo Gallery");
                drawer_layout.closeDrawer(navigationView);
            }
        });
        favoriteViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String myMessage = "favorite";
                bundle.putString("message", myMessage);
                FavoriteFragment fragInfo = new FavoriteFragment();
                fragInfo.setArguments(bundle);
                titleLayout.setVisibility(View.VISIBLE);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentF, fragInfo).commit();
                titleTextView.setText("Favorite News");
                drawer_layout.closeDrawer(navigationView);
            }
        });
        settingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                drawer_layout.closeDrawer(navigationView);

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (savedInstanceState == null) {
            view(0);

        }

    }


    @Override
    protected void onResume() {
//        new AppRater(context).show();
        super.onResume();
        heading = database.getAllParentInfo();
        adapter = new ExAdapter(context, heading, childs);
        expandableListView.setAdapter(adapter);

    }

    public void view(int i) {
        if (i == 0) {
            linearLayout.setVisibility(View.GONE);
            titleLayout.setVisibility(View.VISIBLE);

        } else {
            linearLayout.setVisibility(View.GONE);
            titleLayout.setVisibility(View.VISIBLE);
        }

        if (i == 0) {
            fragment2 = new FrontPageFragment();
//            titleLayout.setVisibility(View.GONE);
            if (fragment2 != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentF, fragment2).commit();
                drawer_layout.closeDrawer(navigationView);
            }
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            android.app.FragmentManager manager = getFragmentManager();
            ExitDialog dialog = new ExitDialog();
            dialog.show(manager, "Exit_Dialog");  //Exit alert show when presses in Backbutton
        }
    }

    public void Toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    public static class ExitDialog extends DialogFragment {  //Exit dialog work

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Exit/Stay?");
            builder.setMessage("Do you want to close this app?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            return alertDialog;
        }
    }

    public class AutoRefreshAsyncTask extends AsyncTask<String, String, String> {
        private NewsDatabase database;
        private Context context;
        private RecyclerView nidGridView;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private JSONArray array;
        String catNewsId;
        private News news;
        int l = 0;
        String idList = "";
        String catnews = "";
        String[] catID = {"1","2","3"};
        String f = "";
        ArrayList nId = new ArrayList();
        String result;
        private String LOG = "JsonForNid.java";
        String p = "";
        UrlLink urlLink = new UrlLink();
        int k = 0;
        int id;
        String tempNewsIds = "";

        public AutoRefreshAsyncTask(Context context) {
            this.context = context;
            database = new NewsDatabase(context);
        }

        @Override
        protected String doInBackground(String... params) {
            int i;
            String catIdString = "";
            for (i = 0; i < catID.length; i++) {
                if (i == 0) {
                    catIdString += catID[i];
                } else {
                    catIdString += "," + catID[i];
                }
            }


            String response = new HTTPGET().SendHttpRequest(urlLink.multiCategory + catIdString);
            Log.e("MainActivity.java", " cat 365 : "+catIdString);
            try {
                jsonObject = new JSONObject(response);
                catnews = jsonObject.getString("cat");
                jsonObject = new JSONObject(catnews);
                Log.e(LOG, "category = " + catnews);
                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
//                        Log.e(LOG, "key "+key);
                    try {
                        catnews = jsonObject.getString(key);
//                        if (isInternetConnection) {
                        database.cleanCatByID(key);
                        database.addCatNews(String.valueOf(key), catnews);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONArray allNewsId;

                JSONObject dbobject;
                int catRun = 0;
                for (i = 0; i < catID.length; i++) {
                    String dbArray = database.getCatNews(String.valueOf(catID[i]));
//                    Log.e(LOG, "cat Id = " + catID[i]);
                    dbobject = new JSONObject(dbArray);
                    catnews = dbobject.getString("nid");
                    array = new JSONArray(catnews);
                    if (array.length() > 0) {
                        for (int xyz = 0; xyz < array.length(); xyz++) {
                            if (tempNewsIds == "") {
                                tempNewsIds += array.getString(xyz);
                            } else {
                                tempNewsIds += "," + array.getString(xyz);
                            }

                        }
                    }

//                      Log.e(LOG, "all news id  = " + tempNewsIds);
                }
                allNewsId = new JSONArray("[" + tempNewsIds + "]");
                l = 0;
                for (i = 0; i < allNewsId.length(); i++) {
                    //jsonObject= jsonArray.getJSONArray(i);

                    catNewsId = String.valueOf(allNewsId.getString(i));

                    Log.e(LOG, "Db Values = " + catNewsId);
                    if (f.equals("")) {
                        f += catNewsId;
                    } else {
                        f += ',' + catNewsId;
                    }
                    id = Integer.parseInt(allNewsId.getString(i));
                    if (database.getNewsExist(id) == 0) {
                        //  Log.e(LOG, "New News = " + id);
                        nId.add(id);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (nId.size() > 1) {

                l = 0;
                for (Object ni : nId) {

                    String j = String.valueOf(ni);
                    if (l == 0) {
                        idList += j;
                    } else {
                        idList += ',' + j;
                    }
                }
                l++;
            }
            Log.e(LOG, "Id List = " + idList);
            result = new HTTPGET().SendHttpRequest(urlLink.newsIdLink + idList);
            try {
//                    Log.e(LOG, "return " + result);
                jsonObject = new JSONObject(result);
                jsonArray = new JSONArray(jsonObject.getString("news"));

                for (i = 0; i < jsonArray.length(); i++) {
                    //  Log.e(LOG, "r " + i);
                    k = 1;
                    jsonObject = jsonArray.getJSONObject(i);
                    String catId = jsonObject.getString("cat_id");
                    String id = jsonObject.getString("id");
                    String heading = jsonObject.getString("heading");
                    String sub_heading;
                    if (jsonObject.isNull("sub_heading")) {
                        sub_heading = null;
                    } else {
                        sub_heading = jsonObject.getString("sub_heading");
                    }
                    String shoulder = jsonObject.getString("shoulder");
                    String pub_time = jsonObject.getString("publish_time");

                    int publish_serial = jsonObject.getInt("publish_serial");
                    int top_news = jsonObject.getInt("n_top_news");
                    int home_slider = jsonObject.getInt("n_home_slider");
                    int inside_news = jsonObject.getInt("n_inside_news");
                    String reporter;
                    if (jsonObject.isNull("reporter")) {
                        reporter = null;
                    } else {
                        reporter = jsonObject.getString("reporter");
                    }
                    String details = jsonObject.getString("details");
                    String image = jsonObject.getString("image");
                    String imageLink = urlLink.imageLink;

                    news = new News(catId, id, heading, sub_heading, shoulder, pub_time, reporter, details, imageLink + image, publish_serial, top_news, home_slider, inside_news);
                    database.addNews(news);
                }

            } catch (JSONException e) {
                e.printStackTrace();


            }


            return result;
        }


    }
}
