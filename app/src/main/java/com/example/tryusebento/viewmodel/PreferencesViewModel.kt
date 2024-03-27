package com.example.tryusebento.viewmodel

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryusebento.data.DeliveryPlaylistRepository
import com.example.tryusebento.model.Cuisine
import com.example.tryusebento.model.Preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

val deliveryDayMap: Map<Int, String> = mapOf(
    1 to "Mon",
    2 to "Tue",
    3 to "Wed",
    4 to "Thu",
    5 to "Fri",
    6 to "Sat",
    7 to "Sun"
)
val cuisineMapper: Map<String, Int> = mapOf(
    "Japanese" to 1,
    "Thai" to 2,
    "Indian" to 3,
    "Korean" to 4,
    "Western" to 5,
    "Dessert" to 6,
    "Healthy" to 7,
    "Chinese" to 8
)
val restrictionMapper: Map<String, Int> = mapOf(
    "None" to 0,
    "Halal" to 1,
    "Vegetarian" to 2,
    "Vegan" to 3,
    "No nuts" to 4,
    "No shellfish" to 5,
)
class PreferencesViewModel : ViewModel() {

    val repository = DeliveryPlaylistRepository()

    var selectedCuisines = mutableListOf<Int>()
    val alreadySelectedThreeCuisines get() = selectedCuisines.size >= 3
    var showMoreThanThreeCuisinesMessage = mutableStateOf(false)
    fun addCuisine(cuisine: Cuisine) {
        if (!alreadySelectedThreeCuisines) {
            selectedCuisines.add(cuisineMapper[cuisine.cuisineName]!!)
        }
    }
    fun removeCuisine(cuisine: Cuisine) {
        selectedCuisines.removeIf {
            cuisineMapper[cuisine.cuisineName] == it
        }
        if (selectedCuisines.size <= 3) {
            showMoreThanThreeCuisinesMessage.value = false
        }
    }

    var restrictionList = mutableListOf<Int>()
    var isRestrictionEnabled =
        mutableStateOf(restrictionList.contains(restrictionMapper["None"]) || restrictionList.isEmpty())
    var specialInstructionsInput = mutableStateOf("")
    fun addRestriction(restrictionName: String) {
        restrictionList.add(restrictionMapper[restrictionName]!!)
    }

    fun removeRestriction(restrictionName: String) {
        restrictionList.removeIf {
            restrictionMapper[restrictionName] == it
        }
    }

    var budgetRange = mutableStateOf(5f..30f)
    val minBudget get() = budgetRange.value.start
    val maxBudget get() = budgetRange.value.endInclusive
    var minRestaurantRating = mutableFloatStateOf(4.5f)

    private var _selectedDeliveryTiming = MutableStateFlow("09:00-09:15")
    var selectedDeliveryTiming = _selectedDeliveryTiming.asStateFlow()

    val timeValues =
        listOf(
            "09:00-09:15",
            "09:15-09:30",
            "09:30-09:45",
            "09:45-10:00",
            "10:00-10:15",
            "10:15-10:30",
            "10:30-10:45",
            "10:45-11:00",
            "11:00-11:15",
            "11:15-11:30",
            "11:30-11:45",
            "11:45-12:00",
            "12:00-12:15",
            "12:15-12:30",
            "12:30-12:45",
            "12.45-13:00",
            "13:00-13:15",
            "13:15-13:30",
            "13:30-13:45",
            "13.45-14:00",
            "14:15-14:30",
            "14:30-14:45",
            "14.45-15:00",
            "15:00-15:15",
            "15:15-15:30",
            "15:30-15:45",
            "15.45-16:00",
            "16:00-16:15",
            "16:15-16:30",
            "16:30-16:45",
            "16:45-17:00",
            "17:00-17:15",
            "17:15-17:30",
            "17:30-17:45",
            "17:45-18:00",
            "18:00-18:15",
            "18:15-18:30",
            "18:30-18:45",
            "18:45-19:00",
            "19:00-19:15",
            "19:15-19:30",
            "19:30-19:45",
            "19:45-20:00",
            "20:00-20:15",
            "20:15-20:30",
            "20:30-20:45",
            "20:45-21:00",
            "21:00-21:15",
            "21:15-21:30",
            "21:30-21:45",
            "21:45-22:00",
        )

    fun updateSelectedDeliveryTiming(deliveryTiming: String) {
        _selectedDeliveryTiming.value = deliveryTiming
    }

    var selectedDeliveryDays: MutableList<Int> = mutableListOf()
    fun addDeliveryDay(day: Int) {
        selectedDeliveryDays.add(day)
    }
    fun removeDeliveryDay(day: Int) {
        selectedDeliveryDays.removeIf {
            it == day
        }
    }
    suspend fun putPreferences(preference: Preference) {
        viewModelScope.launch {
            repository.putPreferences(preference)
        }
    }

}