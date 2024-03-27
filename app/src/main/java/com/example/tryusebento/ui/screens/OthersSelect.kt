package com.example.tryusebento.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.RangeSlider
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.foundation.BentoTheme
import com.example.tryusebento.viewmodel.PreferencesViewModel
import kotlin.math.floor


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OthersSelect(viewModel: PreferencesViewModel) {

    var restaurantRating by remember { viewModel.minRestaurantRating }
    val restaurantRatingRounded = String.format("%.1f", restaurantRating).toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "What is your budget?",
            style = BentoTheme.typography.titleMediumStrong,
            modifier = Modifier.padding(
                start = BentoTheme.spacings.sm,
                top = BentoTheme.spacings.sm
            )
        )

        Text(
            text = "Price of a meal before fees and taxes.",
            modifier = Modifier.padding(start = BentoTheme.spacings.sm)
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(BentoTheme.spacings.sm)
        )

        RangeSlider(
            colors = SliderDefaults.colors(
                thumbColor = BentoTheme.colors.interactionPrimary,
                activeTrackColor = BentoTheme.colors.interactionPrimary,
                activeTickColor = BentoTheme.colors.interactionPrimary
            ),
            modifier = Modifier
                .padding(horizontal = BentoTheme.spacings.md),
            value = viewModel.budgetRange.value,
            onValueChange = {
                newValues ->
                viewModel.budgetRange.value = newValues
            },
            valueRange = 5f..30f
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Card(
                shape = RoundedCornerShape(BentoTheme.cornerRadiuses.container),
                backgroundColor = BentoTheme.colors.brandHighlight,
                modifier = Modifier
                    .width(140.dp)
                    .wrapContentHeight()

            ) {
                Column {
                    Text(
                        text = "Minimum",
                        style = BentoTheme.typography.highlightBase,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(top = BentoTheme.spacings.xs, start = BentoTheme.spacings.xs)
                    )
                    Text(
                        text = "%.1f".format(viewModel.minBudget),
                        style = BentoTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(bottom = BentoTheme.spacings.xs, start = BentoTheme.spacings.xs)
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(BentoTheme.cornerRadiuses.container),
                backgroundColor = BentoTheme.colors.brandHighlight,
                modifier = Modifier
                    .width(140.dp)
                    .wrapContentHeight()
            ) {
                Column {
                    Text(
                        text = "Maximum",
                        style = BentoTheme.typography.highlightBase,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(top = BentoTheme.spacings.xs, start = BentoTheme.spacings.xs)
                    )
                    Text(
                        text = "%.1f".format(viewModel.maxBudget),
                        style = BentoTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(bottom = BentoTheme.spacings.xs, start = BentoTheme.spacings.xs)
                    )
                }
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(BentoTheme.spacings.md)
        )
        Text(
            text = "Restaurant rating",
            style = BentoTheme.typography.titleMediumStrong,
            modifier = Modifier.padding(
                start = BentoTheme.spacings.sm,
                top = BentoTheme.spacings.sm
            )
        )
        Text(
            text = "Minimum rating of the restaurant you are willing to order from.",
            modifier = Modifier.padding(start = BentoTheme.spacings.sm)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BentoTheme.spacings.md),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RestaurantRatingBar(
                restaurantRating = restaurantRatingRounded,
                starSize = 50.dp
            )
            Text(
                text = restaurantRatingRounded.toString(),
                style = BentoTheme.typography.titleLarge,
            )
        }
        Slider(
            value = restaurantRatingRounded,
            valueRange = 0f..5f,
            onValueChange = {newRating -> restaurantRating = newRating},
            enabled = true,
            colors = SliderDefaults.colors(
                thumbColor = BentoTheme.colors.interactionPrimary,
                activeTrackColor = BentoTheme.colors.interactionPrimary,
                activeTickColor = BentoTheme.colors.interactionPrimary
            ),
            modifier = Modifier
                .padding(horizontal = BentoTheme.spacings.md),
        )
    }
}

@Composable
fun RestaurantRatingBar(
    modifier: Modifier = Modifier,
    restaurantRating: Float,
    starSize: Dp
) {
    Box(
        modifier = modifier
    ) {
        Row {
            for (rating in 1..floor(restaurantRating).toInt()) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    modifier = Modifier.size(starSize),
                    contentDescription = "half star",
                    tint = BentoTheme.colors.brandPrimary
                )
            }
            if (restaurantRating > floor(restaurantRating)) {
                Icon(
                    imageVector = Icons.Outlined.StarHalf,
                    modifier = Modifier.size(starSize),
                    contentDescription = "half star",
                    tint = BentoTheme.colors.brandPrimary
                )
            }
        }
        Row {
            for (rating in 1..5) {
                Icon(
                    imageVector = Icons.Outlined.StarBorder,
                    modifier = Modifier.size(starSize),
                    contentDescription = "star border",
                )
            }
        }
    }
}