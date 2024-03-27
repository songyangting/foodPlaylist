package com.example.tryusebento.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.foundation.BentoTheme
import com.deliveryhero.designtokens.android.compose.typography.Roboto
import com.example.tryusebento.R
import com.example.tryusebento.model.Cuisine
import com.example.tryusebento.viewmodel.PreferencesViewModel
import com.example.tryusebento.viewmodel.cuisineMapper

@Composable
fun CuisineSelect(viewModel: PreferencesViewModel) {
    val cuisineList = listOf(
        Cuisine("Japanese", R.drawable.cuisine_japanese),
        Cuisine("Thai", R.drawable.cuisine_thai),
        Cuisine("Indian", R.drawable.cuisine_indian),
        Cuisine("Korean", R.drawable.cuisine_korean),
        Cuisine("Western", R.drawable.cuisine_western),
        Cuisine("Dessert", R.drawable.cuisine_dessert),
        Cuisine("Healthy", R.drawable.cuisine_healthy),
        Cuisine("Chinese", R.drawable.cuisine_chinese)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(viewModel.showMoreThanThreeCuisinesMessage.value) {
            Text(
                text = "Please select only 3 cuisines",
                modifier = Modifier.padding(
                    start = BentoTheme.spacings.sm,
                    top = BentoTheme.spacings.sm
                )
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(
                    top = BentoTheme.spacings.md,
                    bottom = BentoTheme.spacings.md,
                    start = BentoTheme.spacings.xs,
                    end = BentoTheme.spacings.xs
                ),
            content = {
                items(cuisineList.size) {
                        index -> CuisineTile(cuisine = cuisineList[index], viewModel)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CuisineTile(cuisine: Cuisine, viewModel: PreferencesViewModel) {

    var isSelected by remember { mutableStateOf(false) }
    val isAlreadySelected = viewModel.selectedCuisines.contains(cuisineMapper[cuisine.cuisineName])

    Card(
        onClick = {
            if (!viewModel.alreadySelectedThreeCuisines || isAlreadySelected) {
                isSelected = !isSelected
            } else {
                viewModel.showMoreThanThreeCuisinesMessage.value = true
            }
            if (isSelected) {
                viewModel.addCuisine(cuisine)
                Log.d("selected cuisines", viewModel.selectedCuisines.toString())
                Log.d("selected 3 cuisines", viewModel.alreadySelectedThreeCuisines.toString())
            } else {
                viewModel.removeCuisine(cuisine)
                Log.d("selected cuisines", viewModel.selectedCuisines.toString())
            }
        },
        border = if (isSelected || viewModel.selectedCuisines.contains(cuisineMapper[cuisine.cuisineName])) {
            BorderStroke(
                3.dp,
                BentoTheme.colors.brandPrimary
            )
        } else null,
        shape = RoundedCornerShape(BentoTheme.cornerRadiuses.button),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(BentoTheme.spacings.xxs)
    ) {
        Box {
            Image(
                painter = painterResource(id = cuisine.cuisineImg),
                contentDescription = "cuisine image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.7f
            )
            Text(
                text = cuisine.cuisineName,
                style = TextStyle(
                    fontFamily = Roboto,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    shadow = Shadow(
                        color = Color.DarkGray,
                        blurRadius = 6f
                    )
                ),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = BentoTheme.spacings.st)
            )
        }
    }
}