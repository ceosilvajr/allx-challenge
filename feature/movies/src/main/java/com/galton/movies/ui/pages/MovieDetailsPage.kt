package com.galton.movies.ui.pages

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.SuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.galton.movies.movieInitialState
import com.galton.movies.ui.components.ImageView
import com.galton.movies.ui.components.TextSizes
import com.galton.movies.ui.components.TextView
import kotlinx.coroutines.flow.flowOf

@Composable
fun MovieDetailsPage(
    modifier: Modifier,
    movieState: State<Movie>,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit
) {

    var favoriteState by rememberSaveable { mutableStateOf(false) }
    favoriteState = movieState.value.favorite ?: false

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val url = movieState.value.artworkUrl
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
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    TextView(
                        TextView.Model(
                            string = movieState.value.trackName,
                            maxLines = 2,
                            textSizes = TextSizes.TITLE
                        )
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    TextView(
                        TextView.Model(
                            string = movieState.value.genre,
                            textSizes = TextSizes.DESCRIPTION
                        )
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    TextView(
                        TextView.Model(
                            string = "AUD ${movieState.value.price}",
                            textSizes = TextSizes.DESCRIPTION
                        )
                    )
                }
                Spacer(Modifier)
                ImageView(
                    ImageView.Model(
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable {
                                favoriteState = favoriteState.not()
                                onFavoriteItemClicked.invoke(favoriteState, movieState.value)
                            },
                        drawableId = if (favoriteState) R.drawable.ic_fill_favorite else R.drawable.ic_not_fill_favorite,
                    )
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            TextView(
                TextView.Model(
                    string = movieState.value.description,
                    textSizes = TextSizes.DETAILS
                )
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                movieState.value.genre?.let {
                    SuggestionChip(modifier = Modifier.height(24.dp),
                        onClick = { },
                        label = {
                            TextView(
                                TextView.Model(
                                    string = movieState.value.genre,
                                    textSizes = TextSizes.DESCRIPTION
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                movieState.value.artistName?.let {
                    SuggestionChip(modifier = Modifier.height(24.dp),
                        onClick = { },
                        label = {
                            TextView(
                                TextView.Model(
                                    string = movieState.value.artistName,
                                    textSizes = TextSizes.DESCRIPTION
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_6")
@Composable
fun MovieDetailsPagePreview() {
    val movie = Movie(
        0,
        "A Star Is Born (2018)",
        null,
        "14.99",
        "Romance",
        "Visionary director J.J. Abrams brings to life the motion picture event of a generation. As Kylo Ren and the sinister First Order rise from the ashes of the Empire, Luke Skywalker is missing when the galaxy needs him most. It's up to Rey, a desert scavenger, and Finn, a defecting stormtrooper, to join forces with Han Solo and Chewbacca in a desperate search for the one hope of restoring peace to the galaxy.",
        false,
        "Bradley Cooper"
    )
    MovieDetailsPage(
        modifier = Modifier.fillMaxSize(),
        movieState = flowOf(movie).collectAsState(movieInitialState()),
        onFavoriteItemClicked = { _, _ -> }
    )
}