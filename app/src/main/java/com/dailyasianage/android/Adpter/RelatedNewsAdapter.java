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

import com.dailyasianage.android.DetailsActivity;
import com.dailyasianage.android.MainActivity;
import com.dailyasianage.android.R;
import com.dailyasianage.android.item.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by optimal on 25-Jul-16.
 */
public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder>{
    Context context;
    ArrayList<News> arrayList = new ArrayList<News>();


    public RelatedNewsAdapter(Context context, ArrayList<News> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public RelatedNewsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context, arrayList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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

        holder.titleTextView.setText(news.getCat_id());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView heading;
    private TextView details;
    private ImageView imageView;
    private TextView timeTextView;
    private TextView titleTextView;

    Context context;
    ArrayList<News> arrayList = new ArrayList<News>();

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public ViewHolder(View itemView, Context context, ArrayList<News> arrayList) {
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
}
