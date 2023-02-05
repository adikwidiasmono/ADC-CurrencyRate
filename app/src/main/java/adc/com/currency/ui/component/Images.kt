package adc.com.currency.ui.component

import adc.com.currency.R
import adc.com.currency.ui.theme.App_Gray
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun AsyncImageWithPlaceholder(
    modifier: Modifier = Modifier,
    imgUrl: String
) {
    Box(modifier = modifier.background(color = App_Gray)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            placeholder = ImagePlaceholder(),
            error = ImageError(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ImagePlaceholder(): Painter {
    return rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(R.drawable.ic_image_search)
            .build()
    )
}

@Composable
private fun ImageError(): Painter {
    return rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(R.drawable.ic_broken_image)
            .build()
    )
}