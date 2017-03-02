package com.dailyasianage.android.Adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dailyasianage.android.R;
import com.dailyasianage.android.item.DrawerListItem;

import java.util.ArrayList;

/**
 * Created by optimal on 24-Apr-16.
 */
public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DrawerListItem>  itemArrayList;
    private LayoutInflater layoutInflater;
    private LinearLayout dItemLayout;

    public DrawerListAdapter(Context context, ArrayList<DrawerListItem> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

      final ViewHolder viewHolder;

        if (view==null){
            LayoutInflater inflater = getLayoutInflater();
            view= inflater.inflate(R.layout.drawer_list_item,null);
            viewHolder=new ViewHolder();

            dItemLayout=(LinearLayout)view.findViewById(R.id.dItemLayout);
            viewHolder.iconView=(ImageView)view.findViewById(R.id.iconImageView);
            viewHolder.listView=(TextView)view.findViewById(R.id.listTextView);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.iconView.setImageResource(itemArrayList.get(i).getIconImage());
        viewHolder.listView.setText(itemArrayList.get(i).getListDrawer());

        return view;
    }

    public class ViewHolder{
        private ImageView iconView;
        private TextView listView;
        private LinearLayout dItemLayout;
      //  private ImageView arrowView;
    }
    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }
}
