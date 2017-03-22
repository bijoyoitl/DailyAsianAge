package com.dailyasianage.android;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dailyasianage.android.Adpter.ExAdapter;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.CategoryManager;
import com.dailyasianage.android.Database.DrawerMenuManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.Fragments.AllCatFragment;
import com.dailyasianage.android.Fragments.FavoriteFragment;
import com.dailyasianage.android.Fragments.InitialNewsFragment;
import com.dailyasianage.android.ImageGallery.PhotoGalleryFragment;
import com.dailyasianage.android.item.DbDrawerItem;

import java.util.ArrayList;

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

    DrawerMenuManager drawerMenuManager;
    AllNewsManager allNewsManager;
    CategoryManager categoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        database = new NewsDatabase(context);
        drawerMenuManager = new DrawerMenuManager(context);
        allNewsManager = new AllNewsManager(context);
        categoryManager = new CategoryManager(context);

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


//        Intent i = new Intent(MainActivity.this, BackgroundUpdateService.class);
//        MainActivity.this.startService(i);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                cat = heading.get(i).getCat_id();
                cat_name = heading.get(i).getCat_name();
//                if (i == 0) {
//                    fragment2 = new InitialNewsFragment();
//                    titleLayout.setVisibility(View.GONE);
//                    if (fragment2 != null) {
//                        fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.contentF, fragment2).commit();
//                        drawer_layout.closeDrawer(navigationView);
//                    }
//                }

                if (adapter.getChildrenCount(i) == 0) {
                    childs = drawerMenuManager.getAllChildInfo(cat);
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
        heading = drawerMenuManager.getAllParentInfo();
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
            fragment2 = new InitialNewsFragment();
//            titleLayout.setVisibility(View.GONE);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentF, fragment2).commit();
            drawer_layout.closeDrawer(navigationView);
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
                    System.exit(0);
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

}
