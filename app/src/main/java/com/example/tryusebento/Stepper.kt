package com.example.tryusebento

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.deliveryhero.bento.components.core.scaffold.LocalScaffoldContentAtTheTop
import com.deliveryhero.bento.foundation.motion_tokens.BentoSpringEasingSpecParams
import com.deliveryhero.bento.foundation.spring
import com.deliveryhero.bento.foundation.BentoTheme
import kotlin.math.abs

@Stable
public fun <T> spring(
    spec: BentoSpringEasingSpecParams,
    visibilityThreshold: T? = null
): SpringSpec<T> = androidx.compose.animation.core.spring(spec.DampingRatio, spec.Stiffness, visibilityThreshold)

/**
 * Use this to get a current [Activity] reference from compose (if any)
 */
public fun Context.getActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

internal fun snapValueToTick(
    current: Float,
    tickFractions: List<Float>,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return tickFractions
        .minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
        ?.run { lerp(minPx, maxPx, this) }
        ?: current
}

internal fun stepsToTickFractions(steps: Int): List<Float> {
    return if (steps == 0) emptyList() else List(steps + 2) { it.toFloat() / (steps + 1) }
}

// Scale x1 from a1..b1 range to a2..b2 range
internal fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    lerp(a2, b2, calcFraction(a1, b1, x1))

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
internal fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)


/**
 * State of the [Stepper] component.
 *
 * @property steps A list of [Step]s for the [Stepper]. The size should be between 2 and 5, inclusive.
 * @param initialStep The initial step(list index) in the [Stepper]. The value should be within the size
 * of the [steps] list.
 */
@Stable
public class StepperState(
    public val steps: List<Step>,
    initialStep: Int
) {

    init {
        require(steps.size in 2..5) { "Stepper supports only 2 - 5 steps" }
        require(initialStep in steps.indices) { "Initial step can't be larger than the number of steps" }
    }

    /**
     * The current step of the [Stepper].
     */
    public var currentValue: Int by mutableStateOf(initialStep)
        private set

    /**
     * Move to the next step of the [Stepper], if available.
     */
    public fun goForward() {
        if (currentValue < steps.size - 1) currentValue++
    }

    /**
     * Move to the previous step of the [Stepper], if available
     */
    public fun goBackward() {
        if (currentValue > 0) currentValue--
    }

    /**
     * A companion object that contains a function for creating a Saver.
     */
    public companion object {
        /**
         * The [Saver] saves and restores the [StepperState].
         *
         * @param steps The list of [Step]s that the [Stepper] will display.
         */
        public fun Saver(
            steps: List<Step>
        ): Saver<StepperState, Int> = Saver(
            save = { it.currentValue },
            restore = { StepperState(steps = steps, initialStep = it) }
        )
    }
}

/**
 * Creates a [StepperState] and remembers it.
 *
 * @param steps A list of [Step]s for the [Stepper]. The size should be between 2 and 5, inclusive.
 * @param initialStep The initial step(list index) in the [Stepper]. The value should be within the size
 * of the [steps] list.
 */
@Composable
public fun rememberStepperState(
    steps: List<Step>,
    initialStep: Int
): StepperState {
    return rememberSaveable(
        saver = StepperState.Saver(steps = steps)
    ) {
        StepperState(
            steps = steps,
            initialStep = initialStep
        )
    }
}

/**
 * [Stepper] represents Bento-styled component to highlight the current page shown on the screen.
 *
 * @param modifier Optional [Modifier] to be applied to the [Stepper].
 * @param colors to be resolved by the [Stepper]. Check [StepperDefaults] for default colors.
 * @param state current state of the Stepper.
 *
 * @see [Figma Specs](https://www.figma.com/file/E6tt0OvYJx9Uvy9iSnE4g5/%F0%9F%8D%8E%5BBento%5D-iOS?node-id=10305%3A51304)
 * @sample com.deliveryhero.bento.sample.screen.scaffolds.StepperScaffoldScreen
 */
@Composable
public fun Stepper(
    modifier: Modifier = Modifier,
    colors: StepperColors = StepperDefaults.colors(),
    state: StepperState
) {
    var stepsInternal: List<StepInternal>

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        if (state.steps.size == 2) {
            val slice = maxWidth / 3
            stepsInternal = state.steps.mapIndexed { index, thumb ->
                val position = index + 1
                StepInternal(
                    position = position,
                    x = slice.times(position),
                    label = thumb.label,
                    active = index <= state.currentValue
                )
            }
        } else {
            val fractions = stepsToTickFractions(state.steps.size - 2)
            stepsInternal = fractions.mapIndexed { index, fraction ->
                val position = index + 1
                StepInternal(
                    position = position,
                    x = StepperTrackMinOffset + (maxWidth - StepperTrackMinOffset * 2) * fraction,
                    label = state.steps[index].label,
                    active = index <= state.currentValue
                )
            }
        }

        val currentStep = stepsInternal[state.currentValue]

        Track(
            modifier = Modifier.fillMaxWidth(),
            activeTrackX = currentStep.x,
            activeTrackColor = colors.trackActiveColor(),
            inactiveTrackColor = colors.trackInactiveColor(),
            approachingInactive = !currentStep.active
        )

        stepsInternal.forEach { step ->
            Thumb(
                x = step.x,
                isActive = step.active,
                colors = colors,
                text = step.position.toString(),
                label = step.label
            )
        }
    }
}

@Composable
private fun Thumb(
    x: Dp,
    isActive: Boolean,
    colors: StepperColors,
    text: String,
    label: String?
) {
    val alpha by animateFloatAsState(
        targetValue = if (LocalScaffoldContentAtTheTop.current) 1f else 0f,
        animationSpec = tween(
            durationMillis = BentoTheme.motionDurations.Duration150,
            easing = BentoTheme.easings.Standard
        )
    )

    val scale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = if (isActive) {
            tween(
                durationMillis = BentoTheme.motionDurations.Duration300,
                delayMillis = BentoTheme.motionDurations.Duration150,
                easing = { OvershootInterpolator().getInterpolation(it) }
            )
        } else {
            spring(BentoTheme.springEasingSpecs.Subtle)
        }
    )

    val color by animateColorAsState(
        targetValue = colors.thumbTextColor(active = isActive).value,
        animationSpec = tween(
            durationMillis = BentoTheme.motionDurations.Duration150,
            delayMillis = if (isActive) BentoTheme.motionDurations.Duration100 else 0,
            easing = BentoTheme.easings.Standard
        )
    )

    Column {
        Box(
            modifier = Modifier
                .moveLeftByHalfWidth(x = x)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(StepperThumbDiameter)
                    .background(
                        color = colors.thumbBottomLayerBackgroundColor(),
                        shape = CircleShape
                    )
                    .border(
                        width = 4.dp,
                        color = colors.trackInactiveColor(),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .scale(scale)
                    .background(
                        color = colors.thumbTopLayerBackgroundColor(),
                        shape = CircleShape
                    )
                    .size(StepperThumbDiameter)
            )
            Text(
                text = text,
                style = BentoTheme.typography.highlightXsmall,
                color = color
            )
        }
        label?.let {
            Text(
                modifier = Modifier
                    .padding(top = BentoTheme.spacings.xxs)
                    .moveLeftByHalfWidth(x = x)
                    .alpha(alpha),
                text = label,
                style = BentoTheme.typography.highlightXsmall,
                color = colors.labelTextColor()
            )
        }
    }
}

@Composable
private fun Track(
    modifier: Modifier,
    activeTrackX: Dp,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
    approachingInactive: Boolean
) {
    val trackHeightPx = with(LocalDensity.current) { StepperTrackHeight.toPx() }
    val activeTrackXPx = with(LocalDensity.current) { activeTrackX.toPx() }
    val thumbRadiusPx = with(LocalDensity.current) { StepperThumbRadius.toPx() }

    val activeTrackEndX by animateFloatAsState(
        targetValue = activeTrackXPx,
        animationSpec = tween(
            durationMillis = BentoTheme.motionDurations.Duration150,
            delayMillis = if (approachingInactive) 0 else BentoTheme.motionDurations.Duration100,
            easing = BentoTheme.easings.Standard
        )
    )

    Canvas(modifier) {
        val trackY = center.y - thumbRadiusPx + trackHeightPx
        val trackStart = Offset(x = 0f, y = trackY)
        val inactiveTrackEnd = Offset(x = size.width, y = trackY)
        val activeTrackEnd = Offset(x = activeTrackEndX, y = trackY)

        drawLine(
            color = inactiveTrackColor,
            start = trackStart,
            end = inactiveTrackEnd,
            strokeWidth = trackHeightPx
        )
        drawLine(
            color = activeTrackColor,
            start = trackStart,
            end = activeTrackEnd,
            cap = StrokeCap.Round,
            strokeWidth = trackHeightPx
        )
    }
}

/**
 * [Modifier] which moves content left by the half of its width
 */
private fun Modifier.moveLeftByHalfWidth(x: Dp): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(
        width = placeable.width,
        height = placeable.height
    ) {
        placeable.place(x = x.roundToPx() - placeable.width / 2, y = 0)
    }
}

/**
 * An interface for defining colors used in the [Stepper].
 */
@Stable
public interface StepperColors {

    /**
     * Returns a state object representing the text color of the thumb in the [Stepper] based on whether it is active or not.
     *
     * @param active A flag indicating whether the thumb in the [Stepper] is active.
     */
    @Composable
    public fun thumbTextColor(active: Boolean): State<Color>

    /**
     * Returns the background color for the [Stepper].
     */
    public fun backgroundColor(): Color

    /**
     * Returns the active track color for the [Stepper].
     */
    public fun trackActiveColor(): Color

    /**
     * Returns the inactive track color for the [Stepper].
     */
    public fun trackInactiveColor(): Color

    /**
     * Returns the background color for the bottom layer of the thumb in the [Stepper].
     */
    public fun thumbBottomLayerBackgroundColor(): Color

    /**
     * Returns the background color for the top layer of the thumb in the [Stepper].
     */
    public fun thumbTopLayerBackgroundColor(): Color

    /**
     * Returns the text color for labels in the [Stepper].
     */
    public fun labelTextColor(): Color
}

/**
 * A class representing the default colors used in a [Stepper].
 *
 * @property backgroundColor The background color of the [Stepper].
 * @property trackActiveColor The active track color of the [Stepper].
 * @property trackInactiveColor The inactive track color of the [Stepper].
 * @property thumbBottomLayerBackgroundColor The background color of the bottom layer of the thumb in the [Stepper].
 * @property thumbTopLayerBackgroundColor The background color of the top layer of the thumb in the [Stepper].
 * @property thumbActiveTextColor The text color of the thumb in the [Stepper] when it is active.
 * @property thumbInactiveTextColor The text color of the thumb in the [Stepper] when it is inactive.
 * @property labelTextColor The text color of the [Stepper] labels.
 */
@Immutable
public class DefaultStepperColors(
    public val backgroundColor: Color,
    public val trackActiveColor: Color,
    public val trackInactiveColor: Color,
    public val thumbBottomLayerBackgroundColor: Color,
    public val thumbTopLayerBackgroundColor: Color,
    public val thumbActiveTextColor: Color,
    public val thumbInactiveTextColor: Color,
    public val labelTextColor: Color
) : StepperColors {

    override fun backgroundColor(): Color = backgroundColor

    override fun trackActiveColor(): Color = trackActiveColor

    override fun trackInactiveColor(): Color = trackInactiveColor

    override fun thumbBottomLayerBackgroundColor(): Color = thumbBottomLayerBackgroundColor

    override fun thumbTopLayerBackgroundColor(): Color = thumbTopLayerBackgroundColor

    @Composable
    override fun thumbTextColor(active: Boolean): State<Color> =
        rememberUpdatedState(newValue = if (active) thumbActiveTextColor else thumbInactiveTextColor)

    override fun labelTextColor(): Color = labelTextColor
}

/**
 * Singleton object providing default values for the [Stepper].
 */
@Stable
public object StepperDefaults {

    /**
     * Function to create a [StepperColors] object with default color values.
     */
    @Composable
    public fun colors(
        backgroundColor: Color = BentoTheme.colors.white,
        trackActiveColor: Color = BentoTheme.colors.interactionPrimary,
        trackInactiveColor: Color = BentoTheme.colors.neutralBorder,
        thumbBottomLayerBackgroundColor: Color = BentoTheme.colors.white,
        thumbTopLayerBackgroundColor: Color = BentoTheme.colors.interactionPrimary,
        thumbActiveTextColor: Color = BentoTheme.colors.white,
        thumbInactiveTextColor: Color = BentoTheme.colors.neutralSecondary,
        labelTextColor: Color = BentoTheme.colors.neutralSecondary
    ): StepperColors {
        return DefaultStepperColors(
            backgroundColor = backgroundColor,
            trackActiveColor = trackActiveColor,
            trackInactiveColor = trackInactiveColor,
            thumbBottomLayerBackgroundColor = thumbBottomLayerBackgroundColor,
            thumbTopLayerBackgroundColor = thumbTopLayerBackgroundColor,
            thumbActiveTextColor = thumbActiveTextColor,
            thumbInactiveTextColor = thumbInactiveTextColor,
            labelTextColor = labelTextColor
        )
    }
}

internal val StepperThumbDiameter = 26.dp
internal val StepperThumbRadius = StepperThumbDiameter / 2
internal val StepperTrackHeight = 4.dp
private val StepperTrackMinOffset = 32.dp

@Immutable
private class StepInternal(
    val position: Int,
    val x: Dp,
    val label: String,
    var active: Boolean
)

/**
 * Data class representing a single step in a of a [Stepper].
 *
 * @property label Label for the step.
 */
@Immutable
public class Step(public val label: String)
