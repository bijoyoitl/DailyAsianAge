package com.dailyasianage.android;

import com.dailyasianage.android.item.NewsMain;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by ripon on 3/12/2017.
 */

public interface NewsApis {

    @GET()
    Call<JsonObject> getAllCategoryInfo(@Url String s);

    @GET()
    Call<JsonObject> getAllCatNewsId(@Url String s);

    @GET()
    Call<JsonObject> getSingleCatNewsId(@Url String s);

    @GET()
    Call<NewsMain> getAllNews(@Url String s);

}
