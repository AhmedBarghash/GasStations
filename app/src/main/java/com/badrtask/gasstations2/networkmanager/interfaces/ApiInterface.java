package com.badrtask.gasstations2.networkmanager.interfaces;


import com.badrtask.gasstations2.pojos.PlacesPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ITIain on 6/18/2017.
 */

public interface ApiInterface {

    @GET("place/search/json?")
    Call<PlacesPOJO> doPlaces(@Query("location") String location, @Query("radius") String radius, @Query("types") String types, @Query("key") String key);
}
