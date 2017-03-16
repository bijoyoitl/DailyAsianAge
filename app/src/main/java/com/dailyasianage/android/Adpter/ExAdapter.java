package com.dailyasianage.android.Adpter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.DbDrawerItem;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by optimal on 04-Aug-16.
 */
public class ExAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<DbDrawerItem> header;
    private ArrayList<DbDrawerItem> child;
    SparseArray<List<DbDrawerItem>> listChild = new SparseArray<List<DbDrawerItem>>();
    private ImageLoader loader;
    private DisplayImageOptions options;


    public ExAdapter(Context context, ArrayList<DbDrawerItem> header, ArrayList<DbDrawerItem> child) {
        this.context = context;
        this.header = header;
        this.child = child;
        this.loader = ImageLoader.getInstance();
        initOptions();
    }

    public ExAdapter(Context context, ArrayList<DbDrawerItem> header) {
        this.context = context;
        this.header = header;
    }

    @Override
    public int getGroupCount() {
        return header.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (listChild.get(i) == null) {
            return 0;
        } else
            return listChild.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;

    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.iconImageView);
        ImageView arrowImageView = (ImageView) view.findViewById(R.id.arrowImageView);

        String cat_name = header.get(i).getCat_name();

//        Picasso.with(context)
//                .load(header.get(i).getCat_imag())
//                .error(R.mipmap.apps_logo)
//                .placeholder(R.mipmap.apps_logo)
//                .into(imageView);

        if (header.get(i).getCat_imag().equals("")) {
            String img = "";
            this.loader.displayImage(img, imageView, this.options);
        }else {
            String img = UrlLink.catImageLink + header.get(i).getCat_imag();
            this.loader.displayImage(img, imageView, this.options);
        }


//        if (cat_name.equals("Home")) {
//            Picasso.with(context)
//                    .load(R.drawable.home_icon)
//                    .error(R.mipmap.apps_logo)
//                    .placeholder(R.mipmap.apps_logo)
//                    .into(imageView);
//
//        }

        TextView listTextView = (TextView) view.findViewById(R.id.listTextView);
        listTextView.setText(header.get(i).getCat_name());



        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.child_item, null);

        }
        ImageView imageView = (ImageView) view.findViewById(R.id.iconImageView);
//        Picasso.with(context)
//                .load(listChild.get(i).get(i1).getCat_imag())
//                .resize(100, 100)
//                .error(R.mipmap.apps_logo)
//                .placeholder(R.mipmap.apps_logo)
//                .into(imageView);

        if (listChild.get(i).get(i1).getCat_imag().equals("")) {
            String img = "";
            this.loader.displayImage(img, imageView, this.options);
        }else {
            String img = UrlLink.catImageLink +listChild.get(i).get(i1).getCat_imag();
            this.loader.displayImage(img, imageView, this.options);
        }

        TextView listTextView = (TextView) view.findViewById(R.id.listTextView);
        listTextView.setText(listChild.get(i).get(i1).getCat_name());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void addChildren(int groupPosition, List<DbDrawerItem> children) {
        this.listChild.put(groupPosition, children);
    }
    private void initOptions() {
        this.loader.init(getConfiguration());
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loadingicon).showImageForEmptyUri( R.mipmap.apps_logo).showImageOnFail( R.mipmap.apps_logo).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).build();
    }

    private ImageLoaderConfiguration getConfiguration() {
        return new ImageLoaderConfiguration.Builder(this.context).diskCacheExtraOptions(480, 800, null).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(AccessibilityNodeInfoCompat.ACTION_SET_TEXT)).memoryCacheSize(AccessibilityNodeInfoCompat.ACTION_SET_TEXT).diskCacheSize(52428800).build();
    }
}
