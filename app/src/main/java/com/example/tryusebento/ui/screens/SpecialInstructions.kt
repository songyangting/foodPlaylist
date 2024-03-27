package com.example.tryusebento.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.deliveryhero.bento.components.core.Checkbox
import com.deliveryhero.bento.components.core.Text
import com.deliveryhero.bento.components.core.input.InputFieldLabelType
import com.deliveryhero.bento.components.core.input.MultilineInputField
import com.deliveryhero.bento.foundation.BentoTheme
import com.example.tryusebento.viewmodel.PreferencesViewModel

@Composable
fun SpecialInstructions(viewModel: PreferencesViewModel) {

    var specialInstructionsInput by remember { viewModel.specialInstructionsInput }
    val isRestrictionEnabled = viewModel.isRestrictionEnabled.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Dietary Restrictions",
            style = BentoTheme.typography.titleMediumStrong,
            modifier = Modifier.padding(start = BentoTheme.spacings.sm, top = BentoTheme.spacings.sm)
        )
        Spacer(modifier = Modifier.height(BentoTheme.spacings.xs))

        RestrictionCheckbox("None", viewModel = viewModel)
        RestrictionCheckbox("Halal", viewModel = viewModel, isRestrictionEnabled)
        RestrictionCheckbox("Vegetarian", viewModel = viewModel, isRestrictionEnabled)
        RestrictionCheckbox("Vegan", viewModel = viewModel, isRestrictionEnabled)
        RestrictionCheckbox("No nuts", viewModel = viewModel, isRestrictionEnabled)
        RestrictionCheckbox("No shellfish", viewModel = viewModel, isRestrictionEnabled)

        Text(
            text = "Other special instructions",
            style = BentoTheme.typography.titleMediumStrong,
            modifier = Modifier.padding(start = BentoTheme.spacings.sm, top = BentoTheme.spacings.sm)
        )

        MultilineInputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(BentoTheme.spacings.sm),
            text = specialInstructionsInput,
            onTextChange = {  newValue -> specialInstructionsInput = newValue },
            label = "eg. no mayo",
            labelType = InputFieldLabelType.Stable,
            placeholder = "eg. no mayo"
        )
    }
}

@Composable
fun RestrictionCheckbox(restrictionName: String, viewModel: PreferencesViewModel, isEnabled: Boolean = true) {

    var restrictionSelected by remember { mutableStateOf(false) }

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = BentoTheme.spacings.sm)
    ) {
        Text(
            text = restrictionName,
            modifier = Modifier
                .padding(start = BentoTheme.spacings.xs),
            textDecoration = if (!isEnabled) TextDecoration.LineThrough else null
        )
        Checkbox(
            checked = restrictionSelected,
            enabled = isEnabled,
            onCheckedChange =
            {
                restrictionSelected = !restrictionSelected
                if (restrictionSelected) {
                    viewModel.addRestriction(restrictionName)
                    if (restrictionName == "None") {
                        viewModel.isRestrictionEnabled.value = false
                    }

                } else {
                    viewModel.removeRestriction(restrictionName)
                    if (restrictionName == "None") {
                        viewModel.isRestrictionEnabled.value = true
                    }
                }
            },
            modifier = Modifier
                .padding(end = BentoTheme.spacings.xs),
            contentPadding = PaddingValues(0.dp)
        )
    }
}

