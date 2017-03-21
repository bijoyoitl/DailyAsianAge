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

import com.dailyasianage.android.ConnectionDetector;
import com.dailyasianage.android.DetailsActivity;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.NewsAll;
import com.dailyasianage.android.util.ImageCaching;

import java.util.ArrayList;

/**
 * Created by optimal on 22-May-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NewsViewHolder> {

    private Context context;
    private ArrayList<NewsAll> arrayList = new ArrayList<NewsAll>();
    private ImageCaching imageCaching = new ImageCaching();
    private String imgUrl = "";
    private ConnectionDetector detector;
    private boolean isInternet=false;


    public RecyclerAdapter(Context context, ArrayList<NewsAll> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        imageCaching.initOptions(context);
        detector= new ConnectionDetector(context);
        isInternet=detector.isConnectingToInternet();
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

        return new NewsViewHolder(view, context, arrayList);
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
        NewsAll news = arrayList.get(position);
        holder.heading.setText(Html.fromHtml(news.getHeading()));
        details = String.valueOf(Html.fromHtml(news.getDetails()));
        details = details.replace("\n", "").replace("\r", "");
        holder.details.setText(details);

        imgUrl = arrayList.get(position).getImage();
        if (!imgUrl.equals("")) {
            imageCaching.imageSet(imgUrl, holder.imageView);
        }else {
            imageCaching.imageSet("", holder.imageView);
        }

        holder.timeTextView.setText(news.getPublishTime());
        holder.titleTextView.setText(news.getCatId());


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView heading;
        private TextView details;
        private ImageView imageView;
        private TextView timeTextView;
        private TextView titleTextView;


        Context context;
        ArrayList<NewsAll> arrayList = new ArrayList<NewsAll>();


        public NewsViewHolder(View itemView) {
            super(itemView);
        }

        NewsViewHolder(View itemView, Context context, ArrayList<NewsAll> arrayList) {
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

            NewsAll item = this.arrayList.get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("id", item.getId());
//            intent.putExtra("catid", item.getCat_id());
//            intent.putExtra("heading", item.getHeading());
//            intent.putExtra("details", item.getDetails());
//            intent.putExtra("image", arrayList.get(position).getImage());
//            intent.putExtra("time", item.getPublish_time());
//            intent.putExtra("reporter", item.getReporter());
//            intent.putExtra("subheading", item.getSub_heading());
//            intent.putExtra("shoulder", item.getShoulder());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.context.startActivity(intent);

        }
    }

    public void setUpdate(ArrayList<NewsAll> update) {
        this.arrayList = update;
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<NewsAll> update) {
        this.arrayList = update;
        notifyDataSetChanged();
    }


}



