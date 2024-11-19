package com.galton.movies.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galton.movies.R

@Composable
fun EmptyView(@StringRes message: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        TextView(
            TextView.Model(
                stringId = message,
                textSizes = TextSizes.DETAILS
            )
        )
    }
}

@Preview
@Composable
fun EmptyViewPreview() {
    EmptyView(R.string.message_empty_list)
}