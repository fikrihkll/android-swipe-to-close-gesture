@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.molt.dragtoclosecompose

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onNavigateToDetail: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardWidth = (0.8f * screenWidth).dp
    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(550) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .width(cardWidth)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        onNavigateToDetail.invoke()
                    }
            ) {
                Column {
                    sharedTransitionScope.run {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(4f / 3f)
                                then(
                                    sharedTransitionScope?.run {
                                        Modifier.sharedElement(
                                            rememberSharedContentState(key = "image"),
                                            animatedVisibilityScope = animatedVisibilityScope!!,
                                            boundsTransform = boundsTransform
                                        )
                                    } ?: Modifier
                                ),
                            contentScale = ContentScale.Crop,
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
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text("Cinque Terre, Italy", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Image by Kevin & Amanda")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onNavigateToDetail = {})
}