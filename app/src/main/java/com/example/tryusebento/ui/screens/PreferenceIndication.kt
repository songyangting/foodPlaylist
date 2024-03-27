package com.example.tryusebento.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowCircleLeft
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.deliveryhero.bento.components.core.Step
import com.deliveryhero.bento.components.core.Stepper
import com.deliveryhero.bento.components.core.StepperState
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.components.core.rememberStepperState
import com.deliveryhero.bento.foundation.BentoTheme
import com.deliveryhero.bento.foundation.brand.BrandConfiguration
import com.example.tryusebento.R
import com.example.tryusebento.model.Playlist
import com.example.tryusebento.model.Preference
import com.example.tryusebento.playlistList
import com.example.tryusebento.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen()  {
    BentoTheme(brandConfiguration = BrandConfiguration.FOODPANDA) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .background(color = BentoTheme.colors.brandPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(BentoTheme.spacings.sm),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Can't decide what to eat?",
                    color = Color.White,
                    style = BentoTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(40.dp))
                Text(
                    text = "Input your preferences and pau pau will create a delivery playlist for you!!",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = BentoTheme.typography.bodyBase,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(20.dp))

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BentoTheme.colors.brandHighlight),
                    modifier = Modifier
                ) {

                    Text(
                        text = "Lets get started!",
                        color = BentoTheme.colors.brandDark,
                        style = BentoTheme.typography.highlightBase
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "arrow",
                        tint = BentoTheme.colors.brandDark
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.paupau_splash),
                contentDescription = "pau pau",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun PreferenceIndicationScreen(navController: NavHostController) {
    BentoTheme(brandConfiguration = BrandConfiguration.FOODPANDA) {

        val viewModel = remember { PreferencesViewModel() }

        val steps = listOf(Step(label="Step 1"), Step(label = "Step 2"), Step(label="Step 3"), Step(label= "Step 4"))
        val preferenceStepperState = rememberStepperState(steps = steps, initialStep = 0)
        var showPlaylistDialog by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        if (showPlaylistDialog) {
            PlaylistNamingDialog(navController, viewModel, onDismissRequest = { showPlaylistDialog = false })
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = {
                    if (preferenceStepperState.currentValue > 0) {
                        preferenceStepperState.goBackward()
                    } else {
                        navController.navigateUp()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowCircleLeft,
                        contentDescription = "next",
                        tint = if (preferenceStepperState.currentValue == 0) {
                            Color.DarkGray
                        } else {
                            BentoTheme.colors.brandDark
                        }
                    )
                }
                IconButton(onClick = {
                    if (preferenceStepperState.currentValue < 3) {
                        preferenceStepperState.goForward()
                    } else {
                        val preference = Preference(
                            cuisine = viewModel.selectedCuisines,
                            minBudget = viewModel.minBudget,
                            maxBudget = viewModel.maxBudget,
                            minRestaurantRating = viewModel.minRestaurantRating.value,
                            specialInstruction = viewModel.specialInstructionsInput.value,
                            restrictions = viewModel.restrictionList
                        )
                        Log.d("preferences", "$preference")
                        coroutineScope.launch {
                            viewModel.putPreferences(preference)
                        }
                        showPlaylistDialog = true
                    }
                }) {
                    Icon(
                        imageVector = if (preferenceStepperState.currentValue == 3) {
                            Icons.Rounded.CheckCircle
                        } else {
                            Icons.Rounded.ArrowCircleRight
                       },
                        contentDescription = "next",
                        tint = BentoTheme.colors.brandDark
                    )
                }
            }

            PreferenceIndicationHeader(preferenceStepperState)

            Stepper(
                modifier = Modifier
                    .padding(top = BentoTheme.spacings.xs),
                state = preferenceStepperState
            )
            when (preferenceStepperState.currentValue) {
                0 -> CuisineSelect(viewModel)
                1 -> SpecialInstructions(viewModel)
                2 -> OthersSelect(viewModel)
                3 -> DeliveryTiming(viewModel)
            }
        }
    }
}
@Composable
fun PlaylistNamingDialog(
    navController: NavHostController,
    viewModel: PreferencesViewModel,
    onDismissRequest: () -> Unit
) {
    var playlistName by remember { mutableStateOf("") }
    
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            backgroundColor = BentoTheme.colors.brandHighlight,
            shape = RoundedCornerShape(BentoTheme.cornerRadiuses.container)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical=BentoTheme.spacings.xxxl,
                        horizontal = BentoTheme.spacings.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Give your playlist a name",
                    style = BentoTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(BentoTheme.spacings.xl))
                TextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    modifier = Modifier.padding(horizontal = BentoTheme.spacings.sm),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = BentoTheme.colors.brandDark,
                        focusedIndicatorColor = BentoTheme.colors.brandDark,
                    )
                )
                Spacer(modifier = Modifier.height(BentoTheme.spacings.xl))
                Button(
                    onClick = {
                        val playlist = Playlist(
                            playlistId = playlistList.size+1,
                            preferenceId = 1,
                            playlistName = playlistName,
                            deliveryTiming = viewModel.selectedDeliveryTiming.value,
                            deliveryDays = viewModel.selectedDeliveryDays,
                            itemIds = listOf("1"),
                            isPlaying = mutableStateOf(true)
                        )
                        playlistList += listOf(playlist)
                        onDismissRequest()
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BentoTheme.colors.brandDark),
                    shape = RoundedCornerShape(BentoTheme.cornerRadiuses.button)
                ) {
                    Text(
                        text = "Create Playlist",
                        style = BentoTheme.typography.highlightBase,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PreferenceIndicationHeader(preferenceStep: StepperState) {
    var title = ""
    var subtitle = ""
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    start = BentoTheme.spacings.sm
                )
                .fillMaxWidth(0.7f)
            ,
            verticalArrangement = Arrangement.Top
        ) {
            when (preferenceStep.currentValue) {
                0 -> {
                    title = "What kind of foods do you like?"
                    subtitle = "Select up to 3."
                }
                1 -> {
                    title = "Any special instructions?"
                    subtitle = "Please let pau pau know if you are allergic to anything or if we need to avoid anything!"
                }
                2 -> {
                    title = "More special instructions"
                    subtitle = "Set your budget and minimum restaurant rating!"
                }
                3 -> {
                    title = "When should we deliver?"
                    subtitle = "Create your own personalised delivery schedule!"
                }
            }
            Text(
                text = title,
                style = BentoTheme.typography.titleLarge
            )
            Text(
                text = subtitle,
                style = BentoTheme.typography.bodyBase
            )
        }
        Image(
            painter = painterResource(id = R.drawable.paupau),
            contentDescription = "paupau",
            modifier = Modifier
                .size(100.dp)
        )
    }
}

@Preview(showBackground=true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}