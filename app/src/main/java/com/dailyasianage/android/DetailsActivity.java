package com.dailyasianage.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dailyasianage.android.Adpter.RecyclerAdapter;
import com.dailyasianage.android.Adpter.RelatedNewsAdapter;
import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.Database.AllNewsManager;
import com.dailyasianage.android.Database.FavoriteNewsManager;
import com.dailyasianage.android.Database.NewsDatabase;
import com.dailyasianage.android.ImageGallery.SlideshowDialogFragment;
import com.dailyasianage.android.item.News;
import com.dailyasianage.android.util.Utils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private Context context;
    private ImageView d_ImageView;
    private ImageView shareButton;
    private ImageView speechButton;
    private ImageView d_favoriteImageView;

    private TextView id;
    public TextView heding;
    public TextView details;
    private TextView d_timeTextView;
    private TextView d_reporterTextView;
    private TextView d_sub_heading;
    private TextView d_shoulderTextView;

    private LinearLayout profileLayout;
    private TextToSpeech tts;
    NewsDatabase newsDatabase;

    private RecyclerView recyclerView;
    String news_id;
    String fav_id;
    int cat_id;
    String catId;
    String nid;
    WebView detailsNewaWebView;

    public ArrayList<News> newsIds = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    public DetailsActivity detailsActivity;
    ArrayList<News> newsArrayList = new ArrayList<News>();

    AllNewsManager allNewsManager;
    FavoriteNewsManager favoriteNewsManager;

    private ImageLoader loader;
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detailsActivity = this;
        this.context = this;

        newsDatabase = new NewsDatabase(context);
        allNewsManager = new AllNewsManager(context);
        favoriteNewsManager = new FavoriteNewsManager(context);
        recyclerAdapter = new RecyclerAdapter();

        this.loader = ImageLoader.getInstance();
        initOptions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        d_ImageView = (ImageView) findViewById(R.id.d_imageView);
        d_favoriteImageView = (ImageView) findViewById(R.id.d_favoriteImageView);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        speechButton = (ImageView) findViewById(R.id.d_speechImageView);

        id = (TextView) findViewById(R.id.idDeTextView);
        heding = (TextView) findViewById(R.id.idOfHeading);
        d_sub_heading = (TextView) findViewById(R.id.d_sub_heading);
        d_shoulderTextView = (TextView) findViewById(R.id.d_shoulderTextView);
        details = (TextView) findViewById(R.id.detailsDTextView);
        d_timeTextView = (TextView) findViewById(R.id.d_timeTextView);
        d_reporterTextView = (TextView) findViewById(R.id.d_reporterTextView);

        profileLayout = (LinearLayout) findViewById(R.id.profileLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        detailsNewaWebView = (WebView) findViewById(R.id.detailsNewaWebView);

        tts = new TextToSpeech(context, this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        shareButton.setOnClickListener(this);
        speechButton.setOnClickListener(this);
        d_favoriteImageView.setOnClickListener(this);

        id.setText(getIntent().getStringExtra("id"));
        fav_id = getIntent().getStringExtra("id");
        heding.setText(Html.fromHtml(getIntent().getStringExtra("heading")));
        nid = getIntent().getStringExtra("id");

        cat_id = Integer.parseInt(getIntent().getStringExtra("catid"));
        catId = getIntent().getStringExtra("catid");

//        Picasso.with(context)
//                .load(getIntent().getStringExtra("image"))
//                .into(d_ImageView);

        if (getIntent().getStringExtra("image").equals("")) {
            String img = "";
            this.loader.displayImage(img, d_ImageView, this.options);
        } else {
            String img = UrlLink.imageLink + getIntent().getStringExtra("image");
            this.loader.displayImage(img, d_ImageView, this.options);
        }

        String fontSIze = newsDatabase.getFontSize(); //get front size from server and set into fonSize variable.
        int detailsTextSize = Integer.parseInt(fontSIze);

        String de = getIntent().getStringExtra("details");
        WebSettings settings = detailsNewaWebView.getSettings();
        settings.setTextZoom(settings.getTextZoom() + detailsTextSize);
        detailsNewaWebView.loadData(de, "text/html", "utf-8");
        String detail = String.valueOf(Html.fromHtml(de));
        detail = detail.replace("\n", "").replace("\r", "");
        details.setText(detail);

//        details.setTextSize(detailsTextSize);
//        details.setGravity(Gravity.FILL_HORIZONTAL);

        d_timeTextView.setText(getIntent().getStringExtra("time"));
        d_reporterTextView.setText(getIntent().getStringExtra("reporter"));
        if (d_reporterTextView == null) {
            profileLayout.setVisibility(View.INVISIBLE);
        } else {
            profileLayout.setVisibility(View.VISIBLE);
        }

        String subHeading;
        subHeading = getIntent().getStringExtra("subheading");
        d_sub_heading.setText(subHeading);

        String shoulder;
        shoulder = getIntent().getStringExtra("shoulder");

        if (shoulder != null) {
            d_shoulderTextView.setVisibility(View.VISIBLE);
            d_shoulderTextView.setText(shoulder);

        } else {
            d_shoulderTextView.setVisibility(View.GONE);
        }
        if ((favoriteNewsManager.getFavExit(fav_id)).equals("0")) {
            d_favoriteImageView.setImageResource(R.drawable.favorite);
        } else {
            d_favoriteImageView.setImageResource(R.drawable.favorite_two);
        }
        GridLayoutManager manager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }

        });
        RelatedNews();
        recyclerView.setLayoutManager(manager);

    }

    private void initOptions() {
        this.loader.init(getConfiguration());
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loadingicon).showImageForEmptyUri(R.drawable.dummy).showImageOnFail(R.drawable.dummy).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).build();
    }

    private ImageLoaderConfiguration getConfiguration() {
        return new ImageLoaderConfiguration.Builder(this.context).diskCacheExtraOptions(480, 800, null).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(AccessibilityNodeInfoCompat.ACTION_SET_TEXT)).memoryCacheSize(AccessibilityNodeInfoCompat.ACTION_SET_TEXT).diskCacheSize(52428800).build();
    }

    private void RelatedNews() {
        newsIds = allNewsManager.getRelatedNews(catId, nid);
        recyclerView.setAdapter(new RelatedNewsAdapter(context, newsIds));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.d_favoriteImageView:

                if ((favoriteNewsManager.getFavExit(fav_id)).equals("0")) { //cheek exist or not in favorite table.
                    fav_add();
                    d_favoriteImageView.setImageResource(R.drawable.favorite_two);
                } else {
                    deleteFav();
                    d_favoriteImageView.setImageResource(R.drawable.favorite);
                }
                break;

            case R.id.d_speechImageView:

                if (!tts.isSpeaking()) {
                    SpeakOut();
                } else if (tts.isSpeaking()) {
                    onPause();
                }
                break;

            case R.id.shareButton:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody;
                shareBody = id.getText().toString();
                String heading;
                heading = "Daily Asian Age";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.dailyasianage.com/news/" + shareBody);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, heading);
                context.startActivity(Intent.createChooser(shareIntent, "Share via " + heading));
                break;

            case R.id.d_imageView:
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", getIntent().getStringExtra("image"));
                bundle.putInt("position", 0);
                FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
                break;
        }
    }

    @Override
    public void onInit(int status) {  //init work for text to speech.
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
            tts.setPitch(1f);
            tts.setSpeechRate(0.8f);
        } else {
            Toast.makeText(getApplicationContext(), "Something is Missing !!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void SpeakOut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(details.getText().toString());
        } else {
            ttsUnder20(details.getText().toString());
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {   // text to speech work for api under 20.
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        speechButton.setImageResource(R.drawable.f_audio);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    private void ttsGreater21(String text) {  // // text to speech work for api upper 20.
        String utterenceId = this.hashCode() + " ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            speechButton.setImageResource(R.drawable.f_audio);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utterenceId);
        }
    }

    protected void onPause() {
        tts.stop();
        speechButton.setImageResource(R.drawable.daudio);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        tts.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            speechButton.setImageResource(R.drawable.daudio);
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void fav_add() {
        news_id = id.getText().toString();
        favoriteNewsManager.addFavorite(news_id); //add favorite id into favorite table.
        d_favoriteImageView.setImageResource(R.drawable.favorite_two);
        recyclerAdapter.notifyDataSetChanged();
    }


    public void deleteFav() { //delete favorite id from favorite table.
        news_id = id.getText().toString();
        favoriteNewsManager.deleteFavid(news_id);
        d_favoriteImageView.setImageResource(R.drawable.favorite);
        recyclerAdapter.notifyDataSetChanged();
    }

}

