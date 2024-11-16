package com.galton.movies.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.galton.movies.R
import com.galton.utils.descriptionTextSize
import com.galton.utils.titleTextSize

class TextView {
    data class Model(
        val modifier: Modifier = Modifier,
        val string: String? = null,
        @StringRes val stringId: Int? = null,
        val textSizes: TextSizes = TextSizes.TITLE
    )
}

enum class TextSizes {
    TITLE, DESCRIPTION
}

@Composable
fun TextView(model: TextView.Model) {
    TextView(
        modifier = model.modifier,
        string = model.string,
        stringId = model.stringId,
        textSizes = model.textSizes
    )
}


@Composable
fun TextView(
    modifier: Modifier = Modifier,
    string: String? = null,
    @StringRes stringId: Int? = null,
    textSizes: TextSizes = TextSizes.TITLE
) {
    val text = string ?: stringResource(id = stringId!!)
    if (textSizes == TextSizes.TITLE) {
        Text(
            modifier = modifier.basicMarquee(),
            text = text,
            fontSize = titleTextSize,
            fontWeight = FontWeight.Bold
        )
    } else {
        Text(
            modifier = modifier,
            text = text,
            fontSize = descriptionTextSize,
        )
    }
}

@Preview
@Composable
fun TextViewPreview() {
    Column {
        TextView(
            modifier = Modifier,
            stringId = R.string.hello_blank_fragment,
            textSizes = TextSizes.TITLE
        )
        TextView(
            modifier = Modifier,
            string = "Hello",
            textSizes = TextSizes.DESCRIPTION
        )
    }
}