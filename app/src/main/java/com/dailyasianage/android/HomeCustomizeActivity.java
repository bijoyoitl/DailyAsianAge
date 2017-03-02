package com.dailyasianage.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dailyasianage.android.Adpter.HomeSettingsAdapter;
import com.dailyasianage.android.Database.DrawerMenuManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.item.DbDrawerItem;
import com.dailyasianage.android.item.HSItem;

import java.util.ArrayList;

public class HomeCustomizeActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    // private String[] category;
    private ListView listView;
    private HSItem hsItem;
    private ArrayList<HSItem> hsItemArrayList;
    private ArrayList<DbDrawerItem> dbDrawerItemArrayList;
    private DbDrawerItem dbDrawerItem;
    private LinearLayout homeSettingImageView;
    NewsDatabase database;
    public int catid = 0;
    String cat = "";
    String LOG;
    HomeSettingsAdapter adapter;
    LinearLayout backImageView;

    DrawerMenuManager drawerMenuManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_customize);

        this.context = this;
        database = new NewsDatabase(context);
        drawerMenuManager=new DrawerMenuManager(context);

        listView = (ListView) findViewById(R.id.catIdListView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        homeSettingImageView = (LinearLayout) findViewById(R.id.homeSettingImageView);
        backImageView=(LinearLayout) findViewById(R.id.backImageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        dbDrawerItemArrayList = new ArrayList<DbDrawerItem>();

        dbDrawerItemArrayList = drawerMenuManager.getAllDrawer(); // get cat name, id and image and finally save into dbdrawerarraylist.
        LOG = "HomeCustomizeActivity.java";
        adapter = new HomeSettingsAdapter(this, dbDrawerItemArrayList);
        listView.setAdapter(adapter);  //set adapter

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                catid= Integer.parseInt(dbDrawerItemArrayList.get(i).getCat_id());

                NewsDatabase database = new NewsDatabase(context);

                if ((database.getCustomizeExit(String.valueOf(catid))).equals("0")) {
                    setCustomizeId(String.valueOf(catid));  //pass cat id into setCustomized method .
                } else {
                    deleteCustomizeId(String.valueOf(catid)); //pass cat id into deleteCustomized method .
                }
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,MainActivity.class);
                Toast.makeText(context, "Home Customize Complete..", Toast.LENGTH_LONG).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }


    public void setCustomizeId(String catId) {
        NewsDatabase database = new NewsDatabase(context);
        cat = catId;
        database.addCustomNews(catId); //add custom cat id into home customize table.
        this.adapter.notifyDataSetChanged();

    }
    public void deleteCustomizeId(String catId) {
        NewsDatabase database = new NewsDatabase(context);
        cat = catId;
        database.deleteCustomNews(catId);//delete custom cat id into home customize table.
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(context, "Home Customize Complete..", Toast.LENGTH_LONG).show();
        startActivity(intent);
        super.onBackPressed();
    }

}
