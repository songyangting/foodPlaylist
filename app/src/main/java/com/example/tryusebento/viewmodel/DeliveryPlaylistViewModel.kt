package com.example.tryusebento.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryusebento.data.DeliveryPlaylistRepository
import com.example.tryusebento.model.Playlist
import com.example.tryusebento.model.RestaurantItem
import com.example.tryusebento.playlistList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DeliveryPlaylistViewModel : ViewModel() {
    private val repository = DeliveryPlaylistRepository()

//    private val _playlistList = MutableLiveData<List<Playlist>>()
//    val playlistList: LiveData<List<Playlist>> = _playlistList

    private val _restaurantItems = MutableLiveData<List<RestaurantItem>>()
    val restaurantItems: LiveData<List<RestaurantItem>> = _restaurantItems

//    fun getAllPlaylists(): LiveData<List<Playlist>> {
//        viewModelScope.launch {
//            val playlists = repository.getAllPlaylists()
//            _playlistList.value = playlists
//        }
//        return _playlistList
//    }

//    fun getPlaylistById(playlistId: Int): Playlist? {
//        for (playlist in _playlistList.value!!) {
//            if (playlist.playlistId == playlistId) {
//                return playlist
//            }
//        }
//        return null
//    }

    fun getPlaylistById(playlistId: Int): Playlist? {
        for (playlist in playlistList) {
            if (playlist.playlistId == playlistId) {
                return playlist
            }
        }
        return null
    }

    fun getAllRestaurantItems(): LiveData<List<RestaurantItem>> {
        viewModelScope.launch {
            val restaurantItems = repository.getAllRestaurantItems()
            _restaurantItems.value = restaurantItems
        }
        return _restaurantItems
    }

    fun deleteRestaurantItem(itemId: Int) {
        viewModelScope.launch {
            repository.deleteRestaurantItem(itemId)
        }
    }
}