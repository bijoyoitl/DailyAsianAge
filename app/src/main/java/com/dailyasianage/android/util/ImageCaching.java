package com.dailyasianage.android.util;

import android.content.Context;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.widget.ImageView;

import com.dailyasianage.android.All_URL.UrlLink;
import com.dailyasianage.android.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by ripon on 3/21/2017.
 */

public class ImageCaching {
     private ImageLoader loader;
    private DisplayImageOptions options;
    private Context context;

    public void initOptions(Context context) {
        this.context=context;
        this.loader = ImageLoader.getInstance();
        this.loader.init(getConfiguration());
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loadingicon).showImageForEmptyUri( R.drawable.dummy).showImageOnFail( R.drawable.dummy).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).build();
    }

    public ImageLoaderConfiguration getConfiguration() {
        return new ImageLoaderConfiguration.Builder(context).diskCacheExtraOptions(480, 800, null).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(AccessibilityNodeInfoCompat.ACTION_SET_TEXT)).memoryCacheSize(AccessibilityNodeInfoCompat.ACTION_SET_TEXT).diskCacheSize(52428800).build();
    }

    public void imageSet(String url, ImageView imageView) {
        if (url.equals("")) {
            String img = "";
            this.loader.displayImage(img, imageView, this.options);
        }else {
            String img = UrlLink.imageLink + url;
            this.loader.displayImage(img, imageView, this.options);
        }
    }

}
