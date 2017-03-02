package com.dailyasianage.android.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dailyasianage.android.DetailsActivity;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by optimal on 22-May-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NewsViewHolder> {
    private Context context;
    ArrayList<News> arrayList = new ArrayList<News>();
    private ImageLoader imageLoader;


    public RecyclerAdapter(Context context, ArrayList<News> arrayList) {
        Log.e("RecyclerAdapter.java", " array in recyclerView.");
        this.context = context;
        this.arrayList = arrayList;

    }

    public RecyclerAdapter() {
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_lead_cardview, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cardview, parent, false);
                break;
        }

        NewsViewHolder newsViewHolder = new NewsViewHolder(view, context, arrayList);

        return newsViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        if (position % 5 == 0) {

            viewType = 0;
        }
        return viewType;
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
//        Glide.with(context)
//                .load(arrayList.get(position).getImage())
//                .thumbnail(0.5f)
//                .dontAnimate()
//                .placeholder(R.drawable.dummy)
//                .error(R.drawable.dummy_l)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.imageView);
//        imageLoader = AppController.getInstance(context).getImageLoader();
//        imageLoader.get(news.getImage(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.dummy, R.drawable.dummy_l));
//
//        holder.networkImageView.setImageUrl(news.getImage(),imageLoader);
        holder.timeTextView.setText(news.getPublish_time());
        holder.titleTextView.setText(news.getCat_id());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView heading;
        private TextView details;
        private ImageView imageView;
        private TextView timeTextView;
        private TextView titleTextView;
        private NetworkImageView networkImageView;


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
            titleTextView = (TextView) itemView.findViewById(R.id.cat_titleTextView);
        


        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            News item = this.arrayList.get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("catid", item.getCat_id());
            intent.putExtra("heading", item.getHeading());
            intent.putExtra("details", item.getDetails());
            intent.putExtra("image", arrayList.get(position).getImage());
            intent.putExtra("time", item.getPublish_time());
            intent.putExtra("reporter", item.getReporter());
            intent.putExtra("subheading", item.getSub_heading());
            intent.putExtra("shoulder", item.getShoulder());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.context.startActivity(intent);

        }
    }

    public void setUpdate(ArrayList<News> update) {
        this.arrayList = update;
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<News> update) {
        this.arrayList = update;
        notifyDataSetChanged();
    }
}



