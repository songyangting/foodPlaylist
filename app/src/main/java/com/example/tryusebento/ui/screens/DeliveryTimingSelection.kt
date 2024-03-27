package com.example.tryusebento.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deliveryhero.bento.components.core.Modal
import com.deliveryhero.bento.components.core.ModalCloseButton
import com.deliveryhero.bento.components.core.ModalContent
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.foundation.BentoTheme
import com.example.tryusebento.viewmodel.PreferencesViewModel
import com.example.tryusebento.viewmodel.deliveryDayMap
import io.woong.wheelpicker.compose.ValuePicker
import io.woong.wheelpicker.compose.rememberValuePickerState


@Composable
fun DeliveryTiming(viewModel: PreferencesViewModel) {

    var showTimeSelection by remember { mutableStateOf(false) }

    val selectedDeliveryTime = viewModel.selectedDeliveryTiming.collectAsState()
    val deliveryIndex = viewModel.timeValues.indexOf(selectedDeliveryTime.value)

    Card(
        shape = RoundedCornerShape(BentoTheme.cornerRadiuses.container),
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(BentoTheme.spacings.sm),
        border = BorderStroke(width = Dp.Hairline, color = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Delivery Timing",
                style = BentoTheme.typography.titleMediumStrong,
                modifier = Modifier
                    .padding(start = BentoTheme.spacings.sm)
            )

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = BentoTheme.spacings.sm)
            ) {
                Text(
                    text = selectedDeliveryTime.value
                )

                Text(
                    text = "Change",
                    color = BentoTheme.colors.brandDark,
                    modifier = Modifier
                        .clickable {
                            showTimeSelection = true
                            Log.d("index",
                                viewModel.timeValues.indexOf(selectedDeliveryTime.value).toString()
                            )
                        }
                )
            }
        }
    }

    if (showTimeSelection) {
        TimeSelectionModal(viewModel = viewModel, index = deliveryIndex) {
            showTimeSelection = false
        }
    }

    Text(
        text = "Days to deliver",
        style = BentoTheme.typography.titleMediumStrong,
        modifier = Modifier.padding(start = BentoTheme.spacings.sm)
    )
    Spacer(modifier = Modifier.height(BentoTheme.spacings.sm))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BentoTheme.spacings.sm),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DeliveryDaySelectionButton(viewModel,1)
        DeliveryDaySelectionButton(viewModel,2)
        DeliveryDaySelectionButton(viewModel,3)
        DeliveryDaySelectionButton(viewModel,4)
        DeliveryDaySelectionButton(viewModel,5)
        DeliveryDaySelectionButton(viewModel,6)
        DeliveryDaySelectionButton(viewModel,7)
    }
}

@Composable
fun TimeSelectionModal(viewModel: PreferencesViewModel, index: Int = 0, onDismissRequest: () -> Unit) {

    val timeValues = viewModel.timeValues
    val timePickerState = rememberValuePickerState<String>(
        initialIndex = index
    )

    Modal {
        ModalContent(
            title = {
                Text("Choose a delivery timing")
            },
            closeButton = {
                ModalCloseButton(onClick = onDismissRequest )
            },
            buttons = {
                Button(
                    onClick = {
                        viewModel.updateSelectedDeliveryTiming(timeValues[timePickerState.currentIndex])
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(BentoTheme.cornerRadiuses.button),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BentoTheme.colors.brandPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Select Timing",
                        color = Color.White
                    )
                }
            }
        ) {
            ValuePicker(
                values = viewModel.timeValues,
                modifier = Modifier
                    .fillMaxSize()
                    .height(120.dp),
                state = timePickerState,
                isCyclic = false,
                decorationBox = {
                        innerPicker ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    )
                    innerPicker()
                }
            ) {
                    value ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    BasicText(text = value, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun DeliveryDaySelectionButton(viewModel: PreferencesViewModel, dayInt: Int) {
    var dayIsSelected by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable {
                dayIsSelected = !dayIsSelected
                if (dayIsSelected) {
                    viewModel.addDeliveryDay(dayInt)
                } else {
                    viewModel.removeDeliveryDay(dayInt)
                }
                Log.d("selected delivery days", viewModel.selectedDeliveryDays.toString())
            }
            .width(45.dp),
        border = if (!dayIsSelected) {
            BorderStroke(width = Dp.Hairline, color = Color.DarkGray)
        } else {
            BorderStroke(width = 2.dp, color = BentoTheme.colors.brandPrimary)
        }
    ) {
        Text(
            text = deliveryDayMap[dayInt]!!,
            modifier = Modifier
                .padding(BentoTheme.spacings.xs),
            textAlign = TextAlign.Center
        )
    }
}
