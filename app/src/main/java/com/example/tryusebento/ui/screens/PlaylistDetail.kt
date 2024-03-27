package com.example.tryusebento.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.foundation.BentoTheme
import com.deliveryhero.bento.foundation.brand.BrandConfiguration
import com.example.tryusebento.model.Playlist
import com.example.tryusebento.model.RestaurantItem
import com.example.tryusebento.viewmodel.DeliveryPlaylistViewModel

@Composable
fun PlaylistDetailPage(playlistId: Int, viewModel: DeliveryPlaylistViewModel) {
    BentoTheme(brandConfiguration = BrandConfiguration.FOODPANDA) {
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
                    .padding(start = BentoTheme.spacings.sm, bottom = BentoTheme.spacings.sm),
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
                .padding(top = BentoTheme.spacings.sm, start = BentoTheme.spacings.sm, end = BentoTheme.spacings.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = playlist.playlistName,
                style = BentoTheme.typography.titleLarge,
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
                        tint = BentoTheme.colors.brandDark,
                        modifier = Modifier.size(55.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.PlayCircle,
                        contentDescription = "Play Button",
                        tint = BentoTheme.colors.brandDark,
                        modifier = Modifier.size(55.dp)
                    )

                }
            }
        }
        Text(
            text = "Delivers every ${playlist.getDeliveryDaysString()} at ${playlist.deliveryTiming}",
            modifier = Modifier.padding(
                start = BentoTheme.spacings.sm
            )
        )

        Spacer(modifier = Modifier.height(BentoTheme.spacings.sm))
        Text(
            text = "Next delivery",
            modifier = Modifier
                .padding(horizontal = BentoTheme.spacings.sm),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        if (isPlaying.value) {
            Text(
                text = "Next Thu",
                modifier = Modifier.padding(start = BentoTheme.spacings.sm)
            )
        } else {
            Text(
                text = "Start playlist to receive orders!",
                modifier = Modifier.padding(start = BentoTheme.spacings.sm)
            )
        }
        Spacer(modifier = Modifier.height(BentoTheme.spacings.sm))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RestaurantItem(restaurantItem: RestaurantItem) {

    val swipeableState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, 100f to 1)

    Card(
        shape = RoundedCornerShape(BentoTheme.cornerRadiuses.container),
        backgroundColor = BentoTheme.colors.brandHighlight,
        modifier = Modifier
            .padding(
                start = BentoTheme.spacings.st,
                end = BentoTheme.spacings.st,
                bottom = BentoTheme.spacings.xs
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
                model = "https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/M6HASPARCZHYNN4XTUYT7H6PTE.jpg&w=1440",
                contentDescription = "restaurant item icon",
                modifier = Modifier
                    .padding(start = BentoTheme.spacings.xs)
                    .size(50.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop,
                clipToBounds = true
            )
            Column(
                modifier = Modifier
                    .padding(vertical = BentoTheme.spacings.xs, horizontal = BentoTheme.spacings.sm)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = BentoTheme.spacings.xs),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = restaurantItem.Name,
                        style = BentoTheme.typography.titleMedium
                    )
                    Text(
                        text = "S$ ${restaurantItem.Price}",
                        style = BentoTheme.typography.titleMediumStrong
                    )
                }
                Spacer(modifier = Modifier.height(BentoTheme.spacings.st))
                Text(text = restaurantItem.Description)
            }
        }
    }
}