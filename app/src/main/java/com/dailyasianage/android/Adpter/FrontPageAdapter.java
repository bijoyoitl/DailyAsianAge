package com.dailyasianage.android.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailyasianage.android.Database.DrawerMenuManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.DetailsActivity;
import com.dailyasianage.android.MainActivity;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.News;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by optimal on 15-Jun-16.
 */
public class FrontPageAdapter extends RecyclerView.Adapter<FrontPageAdapter.NewsViewHolder> {
    Context context;
    ArrayList<News> arrayList = new ArrayList<News>();
    String[] catName;
    JSONArray CatP;
    public int currentP = 0;
    NewsDatabase database;
    DrawerMenuManager drawerMenuManager;

    public FrontPageAdapter(Context context, ArrayList<News> arrayList, String[] catNames, JSONArray catPosition) {
        this.context = context;
        catName = catNames;
        CatP = catPosition;
        this.arrayList = arrayList;
        database = new NewsDatabase(context);
        drawerMenuManager = new DrawerMenuManager(context);

    }

    public FrontPageAdapter() {

    }


    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_lead, parent, false);

                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cardview, parent, false);
                break;
        }

        NewsViewHolder newsViewHolder = new NewsViewHolder(view, context, arrayList);

        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        String details = "";
        News news = arrayList.get(position);


        holder.heading.setText(Html.fromHtml(news.getHeading()));
        details = String.valueOf(Html.fromHtml(news.getDetails()));
        details = details.replace("\n", "").replace("\r", "");
        holder.details.setText(details);

        Picasso.with(context)
                .load(arrayList.get(position).getImage())
                .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy_l)
                .into(holder.imageView);


        holder.timeTextView.setText(news.getPublish_time());
        MainActivity activity = new MainActivity();
//        String cat_name = activity.getCategoryNameById(news.getCat_id());
        String cat_name = drawerMenuManager.getAllDrawerCategoryName(news.getCat_id());
        holder.cat_titleView.setText(cat_name);
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        currentP++;
        for (int x = 0; x < CatP.length(); x++) {
            try {
                if (Integer.parseInt(CatP.getString(x)) == position) {
                    currentP = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (currentP == 0) {
            viewType = 0;
        }
        return viewType;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView heading;
        public TextView details;
        public ImageView imageView;
        public TextView timeTextView;
        public TextView cat_titleView;


        Context context;
        ArrayList<News> arrayList = new ArrayList<News>();

        public NewsViewHolder(View itemView) {
            super(itemView);
        }

        public NewsViewHolder(View itemView, Context context, ArrayList<News> arrayList) {
            super(itemView);
            this.context = context;
            this.arrayList = arrayList;
            itemView.setOnClickListener(this);

            heading = (TextView) itemView.findViewById(R.id.headingTextView);
            details = (TextView) itemView.findViewById(R.id.detailsTextView);
            imageView = (ImageView) itemView.findViewById(R.id.cardImageView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            cat_titleView = (TextView) itemView.findViewById(R.id.cat_titleTextView);

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            News item = this.arrayList.get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("catid", item.getCat_id());
            intent.putExtra("id", item.getId());
            intent.putExtra("heading", item.getHeading());
            intent.putExtra("details", item.getDetails());
            intent.putExtra("image", arrayList.get(position).getImage());
            intent.putExtra("time", item.getPublish_time());
            intent.putExtra("reporter", item.getReporter());
            intent.putExtra("subheading", item.getSub_heading());
            intent.putExtra("shoulder", item.getShoulder());
            this.context.startActivity(intent);

        }
    }

}
