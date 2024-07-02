@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.molt.dragtoclosecompose

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onNavigatePop: () -> Unit,
) {

    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(550) }
    val imageYOffset = remember {
        Animatable(0f)
    }
    val scaleFactor = remember {
        Animatable(1f)
    }
    val scope = rememberCoroutineScope()
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val minOffsetToClose = screenHeightDp / 5
    val swipeToCloseGesture = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDrag = { _, dragAmount ->
                val dragY = dragAmount.y
                scope.launch {
                    launch {
                        imageYOffset.animateTo(imageYOffset.value + (dragY))
                    }
                    launch {
                        scaleFactor.animateTo((1f - (imageYOffset.value / 1000f)).coerceIn(0.5f, 1f))
                    }
                }
            },
            onDragEnd = {
                if (imageYOffset.value >= minOffsetToClose) {
                    onNavigatePop.invoke()
                } else {
                    scope.launch {
                        launch {
                            imageYOffset.animateTo(0f)
                        }
                        launch {
                            scaleFactor.animateTo(1f)
                        }
                    }
                }
            }
        )
    }

    Surface(
        modifier = Modifier
            .background(Color.Transparent)
            .graphicsLayer {
                scaleX = scaleFactor.value
                scaleY = scaleFactor.value
            }
            .offset(0.dp, imageYOffset.value.dp)
            .then(swipeToCloseGesture)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    sharedTransitionScope?.run {
                        Modifier.sharedElement(
                            rememberSharedContentState(key = "image"),
                            animatedVisibilityScope = animatedVisibilityScope!!,
                            boundsTransform = boundsTransform
                        )
                    } ?: Modifier
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://www.kevinandamanda.com/wp-content/uploads/2012/05/cinque-terre-301.jpg")
                .crossfade(true)
                .build(),
            placeholder = painterResource(
                id = R.drawable.ic_launcher_background
            ),
            contentDescription = "granada image"
        )
    }
}