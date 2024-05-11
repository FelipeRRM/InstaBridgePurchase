import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feliperrm.instabridge.ui.theme.Purple40
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    stroke: Float = 28f,
    thumbSize: Float = 36f,
    cap: StrokeCap = StrokeCap.Round,
    touchStroke: Float = 120f,
    selection: Float = 0.8f,
    thumbColor: Color = Purple40,
    progressColor: Color = Purple40,
    onChange: ((Float) -> Unit)? = null
) {
    val padding = max(thumbSize, stroke)
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }
    var angle by remember { mutableFloatStateOf(selection * 360 - 90f) }
    var last by remember { mutableFloatStateOf(selection * 360) }
    var down by remember { mutableStateOf(false) }
    var radius by remember { mutableFloatStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var appliedAngle by remember { mutableFloatStateOf(selection * 360) }
    var isEditing by remember { mutableStateOf(false) }
    val animateValue = remember { Animatable(initialValue = selection) }

    LaunchedEffect(key1 = selection) {
        if(isEditing){
            println("Is Editing!")
            angle = selection * 360 - 90f
            last = selection * 360
            appliedAngle = selection * 360
            animateValue.animateTo(selection)
        }
        else{
            animateValue.animateTo(selection, animationSpec = spring(stiffness = Spring.StiffnessLow)){
                angle = this.value * 360 - 90f
                last = this.value * 360
                appliedAngle = this.value * 360
            }
            println("NOT editing")
        }

    }
    LaunchedEffect(key1 = angle) {
        var a = angle
        a += 90
        if (a <= 0f) {
            a += 360
        }
        a = a.coerceIn(0f, 360f)
        if (last < 270f && a >= 330f) {
            a = 0f
        }
        if (last > 270f && a < 30) {
            a = last
        }
        last = a
        appliedAngle = a
    }
    LaunchedEffect(key1 = appliedAngle) {
        if(isEditing) {
            onChange?.invoke(appliedAngle / 360f)
        }
    }

    val gradient = Brush.sweepGradient(
        colorStops = arrayOf(
            0.0f to Color(0x00FFFFFF),
            1f to Color(0xFFFFFFFF),
        )
    )


    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                width = it.size.width
                height = it.size.height
                center = Offset(width / 2f, height / 2f)
                radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f
            }
            .pointerInteropFilter {
                val x = it.x
                val y = it.y
                val offset = Offset(x, y)
                val indicatorCenter = center + Offset(
                    radius * cos((90 + appliedAngle) * PI / 180f).toFloat(),
                    radius * sin((90 + appliedAngle) * PI / 180f).toFloat()
                )
                isEditing = false
                when (it.action) {

                    MotionEvent.ACTION_DOWN -> {
                        isEditing = true
                        val d = distance(offset, center)
                        val a = angle(center, offset)

                        if (d >= radius - touchStroke / 2f && d <= radius + touchStroke / 2f && (it.x in indicatorCenter.x - touchStroke / 2..indicatorCenter.x + touchStroke / 2) && (it.y in indicatorCenter.y - touchStroke / 2..indicatorCenter.y + touchStroke / 2)) {
                            down = true
                            angle = a
                        } else {
                            down = false
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        isEditing = true
                        if (down && (it.x in indicatorCenter.x - touchStroke..indicatorCenter.x + touchStroke) && (it.y in indicatorCenter.y - touchStroke..indicatorCenter.y + touchStroke)) {
                            angle = angle(center, offset)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        down = false
                    }

                    else -> return@pointerInteropFilter false
                }
                return@pointerInteropFilter true
            }
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    ) {
        rotate(90f) {
            drawArc(
                brush = gradient,
                startAngle = 0f,
                sweepAngle = 360f,
                topLeft = center - Offset(radius, radius),
                size = Size(radius * 2, radius * 2),
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = cap
                )
            )
        }
        drawArc(
            color = progressColor,
            startAngle = 90f,
            sweepAngle = appliedAngle,
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )




        drawCircle(
            color = thumbColor,
            radius = thumbSize,
            center = center + Offset(
                radius * cos((90 + appliedAngle) * PI / 180f).toFloat(),
                radius * sin((90 + appliedAngle) * PI / 180f).toFloat()
            )
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize / 2.5f,
            blendMode = BlendMode.Clear,
            center = center + Offset(
                radius * cos((90 + appliedAngle) * PI / 180f).toFloat(),
                radius * sin((90 + appliedAngle) * PI / 180f).toFloat()
            )
        )


    }
}

fun angle(center: Offset, offset: Offset): Float {
    val rad = atan2(center.y - offset.y, center.x - offset.x)
    val deg = Math.toDegrees(rad.toDouble())
    return deg.toFloat()
}

fun distance(first: Offset, second: Offset): Float {
    return sqrt((first.x - second.x).square() + (first.y - second.y).square())
}

fun Float.square(): Float {
    return this * this
}

@Composable
@Preview
private fun Preview_CircularProgressBar() {
    Box(modifier = Modifier.size(200.dp)) {
        CircularProgressBar(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            stroke = 20f
        ) {

        }
    }
}