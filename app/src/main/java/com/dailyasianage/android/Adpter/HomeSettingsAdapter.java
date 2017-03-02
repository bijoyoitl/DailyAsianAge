package com.dailyasianage.android.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.HomeCustomizeActivity;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.DbDrawerItem;

import java.util.ArrayList;

/**
 * Created by optimal on 15-Jun-16.
 */
public class HomeSettingsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DbDrawerItem> hsItemArrayList;
    private LayoutInflater layoutInflater;
//    private ArrayList<DbDrawerItem> dbDrawerItems;
    String cat = "";
    HomeCustomizeActivity activity = new HomeCustomizeActivity();
    public ImageView imageView;
    private NewsDatabase database;

    public HomeSettingsAdapter(Context context, ArrayList<DbDrawerItem> hsItemArrayList) {
        this.context = context;
        this.hsItemArrayList = hsItemArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        database= new NewsDatabase(context);
    }

    public HomeSettingsAdapter() {

    }

    @Override
    public int getCount() {
        return hsItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return hsItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if (view == null) {
            layoutInflater = getLayoutInflater();
            view = layoutInflater.inflate(R.layout.home_settings_item, null);
            viewHolder = new ViewHolder();

            viewHolder.textView = (TextView) view.findViewById(R.id.homeSettingTextView);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.homeSettingImageView);
            viewHolder.catId = (TextView)view.findViewById(R.id.catId);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(hsItemArrayList.get(i).getCat_id());
        viewHolder.catId.setText(hsItemArrayList.get(i).getCat_name());
        final String k = viewHolder.textView.getText().toString();
        if (database.getCustomizeExit(k) == "1") {
            viewHolder.imageView.setImageResource(R.drawable.minus);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.plus);
        }

        return view;
    }

    public class ViewHolder {
        private TextView textView;
        public ImageView imageView;
        private TextView catId;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

}
