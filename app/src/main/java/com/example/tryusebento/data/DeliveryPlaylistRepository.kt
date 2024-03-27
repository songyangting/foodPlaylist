package com.example.tryusebento.data

import com.example.tryusebento.model.Playlist
import com.example.tryusebento.model.Preference
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeliveryPlaylistRepository {

    val BASE_URL = "http://10.0.2.2:8080/"

    private val playlistApi: DeliveryPlaylistApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DeliveryPlaylistApi::class.java)

    suspend fun getAllPlaylists() = playlistApi.getAllPlaylists()

    suspend fun putPreferences(preference: Preference) = playlistApi.putPreferences(preference)

    suspend fun getAllRestaurantItems() = playlistApi.getAllRestaurantItems()

    suspend fun deleteRestaurantItem(itemId: Int) = playlistApi.deleteRestaurantItem(itemId)

}