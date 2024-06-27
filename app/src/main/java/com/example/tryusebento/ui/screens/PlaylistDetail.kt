package com.example.tryusebento.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tryusebento.model.Playlist
import com.example.tryusebento.model.RestaurantItem
import com.example.tryusebento.ui.theme.Colors
import com.example.tryusebento.ui.theme.CornerRadiuses
import com.example.tryusebento.ui.theme.Spacings
import com.example.tryusebento.viewmodel.DeliveryPlaylistViewModel

@Composable
fun PlaylistDetailPage(playlistId: Int, viewModel: DeliveryPlaylistViewModel) {
    val restaurantItems = viewModel.getAllRestaurantItems()
    val playlist = viewModel.getPlaylistById(playlistId)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        remember{playlist}?.let { PlaylistHeader(it) }
        Text(
            text = "Upcoming Orders",
            modifier = Modifier
                .padding(start = Spacings().sm, bottom = Spacings().sm),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        LazyColumn{
            restaurantItems.value?.let {
                items(it.size) {
                    item ->
                    RestaurantItem(restaurantItems.value!![item])
                }
            }
        }
    }
}
@Composable
fun PlaylistHeader(playlist: Playlist) {

    val isPlaying = playlist.isPlaying

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacings().sm, start = Spacings().sm, end = Spacings().sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = playlist.playlistName,
//                style = BentoTheme.typography.titleLarge,
            )
            IconButton(
                onClick = {
                    isPlaying.value = !isPlaying.value
                },
                modifier = Modifier
                    .align(Alignment.Bottom)
            ) {
                if (isPlaying.value) {
                    Icon(
                        imageVector = Icons.Rounded.PauseCircle,
                        contentDescription = "Pause Button",
                        tint = Colors().brandDark,
                        modifier = Modifier.size(55.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.PlayCircle,
                        contentDescription = "Play Button",
                        tint = Colors().brandDark,
                        modifier = Modifier.size(55.dp)
                    )

                }
            }
        }
        Text(
            text = "Delivers every ${playlist.getDeliveryDaysString()} at ${playlist.deliveryTiming}",
            modifier = Modifier.padding(
                start = Spacings().sm
            )
        )

        Spacer(modifier = Modifier.height(Spacings().sm))
        Text(
            text = "Next delivery",
            modifier = Modifier
                .padding(horizontal = Spacings().sm),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        if (isPlaying.value) {
            Text(
                text = "Next Thu",
                modifier = Modifier.padding(start = Spacings().sm)
            )
        } else {
            Text(
                text = "Start playlist to receive orders!",
                modifier = Modifier.padding(start = Spacings().sm)
            )
        }
        Spacer(modifier = Modifier.height(Spacings().sm))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RestaurantItem(restaurantItem: RestaurantItem) {

    val swipeableState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, 100f to 1)

    Card(
        shape = RoundedCornerShape(CornerRadiuses().container),
        backgroundColor = Colors().brandHighlight,
        modifier = Modifier
            .padding(
                start = Spacings().st,
                end = Spacings().st,
                bottom = Spacings().xs
            )
            .fillMaxWidth()
            .height(100.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                thresholds = { _, _ -> FractionalThreshold(0.5f) }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AsyncImage(
                model = restaurantItem.ImageURL,
                contentDescription = "restaurant item icon",
                modifier = Modifier
                    .padding(start = Spacings().xs)
                    .size(50.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop,
                clipToBounds = true
            )
            Column(
                modifier = Modifier
                    .padding(vertical = Spacings().xs, horizontal = Spacings().sm)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacings().xs),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = restaurantItem.Name,
//                        style = BentoTheme.typography.titleMedium
                    )
                    Text(
                        text = "S$ ${restaurantItem.Price}",
//                        style = BentoTheme.typography.titleMediumStrong
                    )
                }
                Spacer(modifier = Modifier.height(Spacings().st))
                Text(text = restaurantItem.Description)
            }
        }
    }
}