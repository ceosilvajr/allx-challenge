package com.galton.movies.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galton.models.Movie
import com.galton.movies.R
import com.galton.utils.Purple40
import com.galton.utils.cardContentPadding
import com.galton.utils.cardElevation

@Composable
fun MovieItemView(
    modifier: Modifier,
    movie: Movie,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit,
    onMovieItemClicked: (Movie) -> Unit
) {
    var favoriteState by rememberSaveable { mutableStateOf(movie.favorite ?: false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable {
                onMovieItemClicked.invoke(movie)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation
        )
    ) {
        Row(
            modifier = Modifier
                .padding(cardContentPadding)
                .fillMaxWidth()
        ) {
            val url = movie.artworkUrl
            ImageView(
                ImageView.Model(
                    modifier = Modifier
                        .width(60.dp)
                        .height(84.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    url = url,
                    contentScale = ContentScale.Crop,
                    drawableId = if (url.isNullOrBlank()) R.drawable.img_placeholder_small else null,
                    placeHolder = R.drawable.img_placeholder_small
                )
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Column(
                modifier = Modifier.weight(1f).padding(top = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                TextView(
                    TextView.Model(
                        string = movie.trackName,
                        maxLines = 2,
                        textSizes = TextSizes.TITLE
                    )
                )
                Spacer(modifier = Modifier.padding(3.dp))
                TextView(
                    TextView.Model(
                        string = movie.genre,
                        textSizes = TextSizes.DESCRIPTION
                    )
                )
                Spacer(modifier = Modifier.padding(1.dp))
                TextView(
                    TextView.Model(
                        string = "AUD ${movie.price}",
                        textSizes = TextSizes.DESCRIPTION
                    )
                )
            }
            IconButton(onClick = {
                favoriteState = favoriteState.not()
                onFavoriteItemClicked.invoke(favoriteState, movie)
            }) {
                Icon(
                    imageVector = if (favoriteState) Icons.Outlined.Favorite else Icons.Filled.FavoriteBorder,
                    tint = Purple40,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun MovieItemPreview() {
    MovieItemView(
        modifier = Modifier,
        movie = Movie(
            0,
            "A Star Is Born (2018)",
            null,
            "14.99",
            "Romance",
            "-",
            false,
            "Bradley Cooper",
        ),
        onFavoriteItemClicked = { _, _ -> },
        onMovieItemClicked = {}
    )
}