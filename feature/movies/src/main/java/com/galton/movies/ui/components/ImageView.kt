package com.galton.movies.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.galton.movies.R

class ImageView {
    data class Model(
        val modifier: Modifier = Modifier,
        val url: String? = null,
        @DrawableRes val drawableId: Int? = null,
        val placeHolder: Int = 0,
        val contentScale: ContentScale = ContentScale.Crop,
        val contentDescription: String? = null,
        val tint: ColorFilter? = null,
        val alignment: Alignment = Alignment.Center
    )
}

@Composable
fun ImageView(model: ImageView.Model) {
    ImageView(
        modifier = model.modifier,
        url = model.url,
        drawableId = model.drawableId,
        placeHolder = model.placeHolder,
        contentScale = model.contentScale,
        contentDescription = model.contentDescription,
        tint = model.tint,
        alignment = model.alignment
    )
}


@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    url: String? = null,
    @DrawableRes drawableId: Int? = null,
    @DrawableRes placeHolder: Int = 0,
    tint: ColorFilter? = null,
    placeHolderTint: ColorFilter? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
) {
    var imageTint by remember {
        mutableStateOf(placeHolderTint)
    }

    var loaded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(tint, loaded) {
        if (loaded) {
            imageTint = tint
        }
    }

    if (drawableId != null) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Image",
            contentScale = contentScale,
            modifier = modifier,
            colorFilter = tint
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription ?: "Image",
            modifier = modifier,
            contentScale = contentScale,
            colorFilter = imageTint,
            alignment = alignment,
            placeholder = painterResource(id = placeHolder),
            onSuccess = {
                loaded = true
            },
        )
    }
}

@Preview
@Composable
fun ImageViewPreview() {
    ImageView(
        modifier = Modifier,
        drawableId = R.drawable.img_placeholder_small,
        contentScale = ContentScale.Crop
    )
}