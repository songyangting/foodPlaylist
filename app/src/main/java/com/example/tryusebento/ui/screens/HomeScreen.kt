package com.example.tryusebento.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tryusebento.R
import com.example.tryusebento.ui.theme.Colors
import com.example.tryusebento.ui.theme.CornerRadiuses
import com.example.tryusebento.ui.theme.Spacings

//TODO
// - get someone to export to proper png file for PANDAMART & DINE-IN
// - dialog for currently playing playlist
// - UI to show how far the delivery is (timing)


@Composable
fun HomeScreen(navController: NavHostController) {
    Column {
        AppBar()
        TileScreen(navController)
    }
}

@Composable
fun AppBar() {
    var searchInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(color = Colors().brandPrimary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "menu icon",
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp),
                tint = Color.White
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {

                Text(
                    text = "Home",
                    color = Color.White,
//                    style = BentoTheme.typography.titleSmall
                )
                Text(
                    text = "Kingroad 4",
                    color = Color.White,
//                    style = BentoTheme.typography.bodyBase
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = "likes icon",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                        .padding(2.dp),
                    tint = Color.White
                )

                Icon(
                    imageVector = Icons.Rounded.ShoppingCart,
                    contentDescription = "shopping cart icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    tint = Color.White
                )

            }
        }
        TextField(
            value = searchInput,
            label = {Text("Search for your local favorites")},
            onValueChange = { searchInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }

}

@Composable
fun TileScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = Spacings().sm,
                    start = Spacings().sm,
                    end = Spacings().sm
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                MenuTile(
                    titleTxt = "Food delivery",
                    subtitleTxt = "Big savings on delivery",
                    height = 244.dp,
                    imageResource = R.drawable.fooddelivery_img
                )
                MenuTile(
                    titleTxt = "Pick-up",
                    subtitleTxt = "Up to 50% off",
                    height = 76.dp,
                    imageResource = R.drawable.pickup_img
                )
                MenuTile(
                    titleTxt = "pandago",
                    subtitleTxt = "Send parcels",
                    height = 76.dp,
                    imageResource = R.drawable.pandago_img
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MenuTile(
                    titleTxt = "pandamart",
                    subtitleTxt = "Fresh groceries & more",
                    height = 160.dp,
                    imageResource = R.drawable.pandamart_img
                )
                MenuTile(
                    titleTxt = "Shops",
                    subtitleTxt = "Giant, CS Fresh & more",
                    height = 160.dp,
                    imageResource = R.drawable.shops_img
                )
                MenuTile(
                    titleTxt = "Dine-in",
                    subtitleTxt = "Up to 50% off\nEntire bill",
                    height = 76.dp,
                    imageResource = R.drawable.dinein_img
                )

            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal=Spacings().sm)
        ) {
            MenuTile(
                navController = navController,
                titleTxt = "Delivery playlist",
                subtitleTxt = "Create personalised delivery schedules!",
                height = 76.dp,
                imageResource = R.drawable.fooddelivery_img
            )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MenuTile(
    navController: NavHostController? = null,
    titleTxt: String,
    subtitleTxt: String,
    height: Dp,
    imageResource: Int
) {
    Card(
        onClick = {
          if (titleTxt == "Delivery playlist") {
              navController!!.navigate("playlist_library")
          }
        },
        modifier = Modifier
            .padding(Spacings().xxs)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(CornerRadiuses().container)
            )
            .border(
                width = Dp.Hairline,
                color = Color.LightGray,
                shape = RoundedCornerShape(CornerRadiuses().container)
            )
            .padding(Spacings().xxxs)
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Food delivery image",
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .align(Alignment.BottomEnd)
            )
            Column() {
                Text(
                    text = titleTxt,
//                    style = BentoTheme.typography.titleMedium,
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF333333)
                    ),
                    modifier = Modifier
                        .padding(top = Spacings().xs, start = Spacings().sm)
                )
                Text(
                    text = subtitleTxt,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF707070)
                    ),
                    color = Colors().illuSecondaryDark,
                    modifier = Modifier
                        .padding(top = Spacings().xxs, start = Spacings().sm)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}

