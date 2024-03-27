package com.example.tryusebento.model

import androidx.compose.runtime.MutableState
import com.example.tryusebento.viewmodel.deliveryDayMap
import com.google.gson.annotations.SerializedName

data class Playlist(
    val playlistId: Int,
    val preferenceId: Int,
    var playlistName: String,
    var deliveryTiming: String,
    var deliveryDays: List<Int>,
    val itemIds: List<String>,
    var isPlaying: MutableState<Boolean>
) {
    fun getDeliveryDaysString(): String {
        val sortedDeliveryDays = this.deliveryDays.sorted()
        var deliveryDaysString = ""
        if (sortedDeliveryDays.size == 1) {
            return deliveryDayMap[deliveryDays[0]]!!
        }
        for (deliveryDay in sortedDeliveryDays) {
            deliveryDaysString += if (this.deliveryDays.indexOf(deliveryDay) == this.deliveryDays.lastIndex) {
                "and " + deliveryDayMap[deliveryDay]
            } else {
                deliveryDayMap[deliveryDay] + if (this.deliveryDays.size > 2) {", "} else {" "}
            }
        }
        return deliveryDaysString
    }
}

data class Preference(
    @SerializedName("userid") val preferenceId: Int = 0,
    val cuisine: List<Int> = emptyList(),
    val minBudget: Float,
    val maxBudget: Float,
    val minRestaurantRating: Float,
    val specialInstruction: String = "",
    val restrictions: List<Int> = emptyList()
)

data class PreferenceAndPlaylistName(
    val preference: Preference,
    val playlistName: String
)