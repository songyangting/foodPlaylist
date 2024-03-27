package com.example.tryusebento.data

import com.example.tryusebento.model.Playlist
import com.example.tryusebento.model.Preference
import com.example.tryusebento.model.RestaurantItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DeliveryPlaylistApi {
    @POST("preference")
    suspend fun putPreferences(@Body preference: Preference)

    @GET("restaurant-items")
    suspend fun getAllRestaurantItems(): List<RestaurantItem>

    @GET("playlist")
    suspend fun getAllPlaylists(): List<Playlist>

//    @GET("restaurant_item/{itemId}")
//    suspend fun getRestaurantItemById(@Path("itemId") itemId: Int): RestaurantItem

    //implement swipe to delete
    @DELETE("restaurant_item/{item_id}")
    suspend fun deleteRestaurantItem(@Path("itemId") itemId: Int)

}