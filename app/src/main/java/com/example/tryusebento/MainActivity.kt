package com.example.tryusebento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.deliveryhero.bento.foundation.BentoTheme
import com.deliveryhero.bento.foundation.brand.BrandConfiguration
import com.example.tryusebento.model.Playlist
import com.example.tryusebento.ui.screens.HomeScreen
import com.example.tryusebento.ui.screens.PlaylistDetailPage
import com.example.tryusebento.ui.screens.PlaylistLibrary
import com.example.tryusebento.ui.screens.PreferenceIndicationScreen
import com.example.tryusebento.viewmodel.DeliveryPlaylistViewModel
import com.example.tryusebento.viewmodel.PreferencesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BentoTheme(
                isSystemInDarkTheme = false,
                brandConfiguration = BrandConfiguration.FOODPANDA,
                isProductionBuild = true,
            ) {
                val navController = rememberNavController()
                MainApplication(navController)
            }
        }
    }
}

var playlistList: List<Playlist> = listOf()

@Composable
fun MainApplication(navController: NavHostController) {

//    val preferencesViewModel = PreferencesViewModel()
    val playlistViewModel = DeliveryPlaylistViewModel()

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") { HomeScreen(navController = navController) }
        composable("playlist_library") { PlaylistLibrary(playlistItems = playlistList, navController = navController) }
        composable("preference_indication") { PreferenceIndicationScreen(navController) }
        composable(
            route = "playlist_detail/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
        ) { backStackEntry ->
            PlaylistDetailPage(backStackEntry.arguments!!.getInt("playlistId"), playlistViewModel)
        }
    }
}
