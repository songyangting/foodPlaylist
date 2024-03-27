package com.example.tryusebento.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.foundation.BentoTheme
import com.deliveryhero.bento.foundation.brand.BrandConfiguration
import com.example.tryusebento.model.Playlist

@Composable
fun PlaylistLibrary(
    playlistItems: List<Playlist>,
    navController: NavHostController
) {
    BentoTheme(brandConfiguration = BrandConfiguration.FOODPANDA) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "My delivery playlists",
                        style = BentoTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(BentoTheme.spacings.sm)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "search icon",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = BentoTheme.spacings.sm)
                    )
                }
                if (playlistItems.isEmpty()) {
                    EmptyPlaylistView(navController)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        items(playlistItems) { playlistItem ->
                            PlaylistItem(playlistItem, navController)
                        }
                    }
                }
            }
            if (playlistItems.isNotEmpty()) {
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    CreateNewButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        navHostController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, navController: NavHostController) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                start = BentoTheme.spacings.sm,
                end = BentoTheme.spacings.sm,
                top = BentoTheme.spacings.xxs,
                bottom = BentoTheme.spacings.xxs
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    navController.navigate("playlist_detail/${playlist.playlistId}")
                }
        ) {
            if (playlist.isPlaying.value) {
                Text(
                    text = "Next delivery on ...",
                    style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                    color = Color.DarkGray
                )
            }
            Text(
                text = playlist.playlistName,
                style = BentoTheme.typography.titleSmall
            )
            Text(
                text = "Every ${playlist.getDeliveryDaysString()} at ${playlist.deliveryTiming}.",
                style = BentoTheme.typography.bodySmall
            )
        }
        IconButton(
            onClick = { playlist.isPlaying.value = !playlist.isPlaying.value },
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            if (!playlist.isPlaying.value) {
                Icon(
                    imageVector = Icons.Rounded.PlayCircle,
                    contentDescription = "Play button",
                    modifier = Modifier
                        .size(45.dp),
                    tint = BentoTheme.colors.brandDark
                )

            } else {
                Icon(
                    imageVector = Icons.Rounded.PauseCircle,
                    contentDescription = "Pause button",
                    modifier = Modifier
                        .size(45.dp),
                    tint = BentoTheme.colors.brandDark
                )
            }
        }
    }
}

@Composable
fun CreateNewButton(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Button(
        onClick = {
            navHostController.navigate("preference_indication")
        },
        shape = RoundedCornerShape(BentoTheme.cornerRadiuses.button),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BentoTheme.colors.brandDark,
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.PlaylistAdd,
            contentDescription = "add new playlist button"
        )
        Text(
            text = "Create new playlist",
            style = BentoTheme.typography.highlightBase,
            color = Color.White
        )
    }
}

@Composable
fun EmptyPlaylistView(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No playlists. Create a new playlist to get started!",
            style = BentoTheme.typography.highlightBase
        )
        Spacer(modifier = Modifier.height(BentoTheme.spacings.sm))
        CreateNewButton(
            navHostController = navController,
            modifier = Modifier
        )
    }

}