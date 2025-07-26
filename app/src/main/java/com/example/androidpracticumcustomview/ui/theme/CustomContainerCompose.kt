package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Задание:
 * Реализуйте необходимые компоненты;
 * Создайте проверку что дочерних элементов не более 2-х;
 * Предусмотрите обработку ошибок рендера дочерних элементов.
 * Задание по желанию:
 * Предусмотрите параметризацию длительности анимации.
 */

@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?,
    alphaAnimDurationMillis: Int = 2000,
    offsetAnimDurationMillis: Int = 5000,
) {
    val density = LocalDensity.current.density

    var parentHeight by remember { mutableFloatStateOf(0f) }
    var firstChildHeight by remember { mutableFloatStateOf(0f) }
    var secondChildHeight by remember { mutableFloatStateOf(0f) }

    val firstChildAlpha = remember { Animatable(0f) }
    val firstChildOffsetY = remember { Animatable(0f) }

    val secondChildAlpha = remember { Animatable(0f) }
    val secondChildOffsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // First child animation
        launch {
            firstChildAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(alphaAnimDurationMillis)
            )
        }
        launch {
            firstChildOffsetY.animateTo(
                targetValue = -parentHeight / 2 + firstChildHeight / 2,
                animationSpec = tween(offsetAnimDurationMillis)
            )
        }

        // Second child animation
        launch {
            delay(2000)
            secondChildAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(alphaAnimDurationMillis)
            )
        }
        launch {
            delay(2000)
            secondChildOffsetY.animateTo(
                targetValue = parentHeight / 2f - secondChildHeight / 2,
                animationSpec = tween(offsetAnimDurationMillis),
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPlaced { layoutCoordinates ->
                parentHeight = layoutCoordinates.size.height.toFloat() / density
            }
    ) {
        firstChild?.let { child ->
            Box(
                modifier = Modifier
                    .offset(y = firstChildOffsetY.value.dp)
                    .align(Alignment.Center)
                    .onPlaced { layoutCoordinates ->
                        firstChildHeight = layoutCoordinates.size.height / density
                    }
                    .alpha(firstChildAlpha.value)
            ) {
                child.invoke()
            }
        }

        secondChild?.let { child ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = secondChildOffsetY.value.dp)
                    .onPlaced { layoutCoordinates ->
                        secondChildHeight = layoutCoordinates.size.height / density
                    }
                    .alpha(secondChildAlpha.value)
            ) {
                child.invoke()
            }
        }
    }
}