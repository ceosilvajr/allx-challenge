package com.galton.movies.ui.pages

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavBackStackEntry
import com.galton.models.Movie
import com.galton.movies.R
import com.galton.movies.data.movieInitialState
import com.galton.movies.ui.components.ImageView
import com.galton.movies.ui.components.TextSizes
import com.galton.movies.ui.components.TextView
import com.galton.movies.viewmodel.MovieViewModel
import com.galton.utils.Purple40

@Composable
fun MovieDetailsPage(
    modifier: Modifier,
    backStackEntry: NavBackStackEntry,
    viewModel: MovieViewModel
) {
    val movie = viewModel.getMovieById(
        backStackEntry.arguments?.getInt("id") ?: 0
    )?.collectAsState(movieInitialState())

    if (movie == null) return

    MovieDetailsPage(modifier, movie.value) { favoriteState, data ->
        data.id?.let {
            if (favoriteState) {
                viewModel.addFavorite(it)
            } else {
                viewModel.deleteFavorite(it)
            }
        }
    }
}

@Composable
private fun MovieDetailsPage(
    modifier: Modifier,
    movie: Movie,
    onFavoriteItemClicked: (Boolean, Movie) -> Unit
) {
    var favoriteState by rememberSaveable { mutableStateOf(false) }
    favoriteState = movie.favorite ?: false

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val url = movie.artworkUrl
                ImageView(
                    ImageView.Model(
                        modifier = Modifier
                            .width(70.dp)
                            .height(95.dp)
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
                Spacer(Modifier)
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
            Spacer(modifier = Modifier.padding(8.dp))
            TextView(
                TextView.Model(
                    string = movie.description,
                    textSizes = TextSizes.DETAILS
                )
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                movie.genre?.let {
                    SuggestionChip(
                        modifier = Modifier.height(24.dp),
                        onClick = { },
                        label = {
                            TextView(
                                TextView.Model(
                                    string = movie.genre,
                                    textSizes = TextSizes.DESCRIPTION
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                movie.artistName?.let {
                    SuggestionChip(
                        modifier = Modifier.height(24.dp),
                        onClick = { },
                        label = {
                            TextView(
                                TextView.Model(
                                    string = movie.artistName,
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

@Preview
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
        "Bradley Cooper",
        0L
    )
    MovieDetailsPage(Modifier, movie) { _, _ -> }
}
