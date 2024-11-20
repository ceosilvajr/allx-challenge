package com.galton.movies.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import com.galton.movies.R
import com.galton.utils.descriptionTextSize
import com.galton.utils.detailsTextSize
import com.galton.utils.titleTextSize

class TextView {
    data class Model(
        val modifier: Modifier = Modifier,
        val string: String? = null,
        @StringRes val stringId: Int? = null,
        val textSizes: TextSizes = TextSizes.TITLE,
        val maxLines: Int = Int.MAX_VALUE
    )
}

enum class TextSizes {
    TITLE, DESCRIPTION, DETAILS
}

@Composable
fun TextView(model: TextView.Model) {
    TextView(
        modifier = model.modifier,
        string = model.string,
        stringId = model.stringId,
        textSizes = model.textSizes,
        maxLines = model.maxLines
    )
}

@Composable
fun TextView(
    modifier: Modifier = Modifier,
    string: String? = null,
    @StringRes stringId: Int? = null,
    textSizes: TextSizes = TextSizes.TITLE,
    maxLines: Int = 1
) {
    val text = string ?: stringResource(id = stringId!!)
    when (textSizes) {
        TextSizes.TITLE -> {
            Text(
                modifier = modifier,
                maxLines = maxLines,
                text = text,
                fontSize = titleTextSize,
                fontWeight = FontWeight.Bold,
                style = textStyle()
            )
        }

        TextSizes.DESCRIPTION -> {
            Text(
                modifier = modifier,
                text = text,
                maxLines = maxLines,
                fontSize = descriptionTextSize,
                style = textStyle()
            )
        }

        TextSizes.DETAILS -> {
            Text(
                modifier = modifier,
                text = text,
                maxLines = maxLines,
                fontSize = detailsTextSize,
                style = textStyle()
            )
        }
    }
}

/**
 * Adjust Text line height and padding
 * https://developer.android.com/develop/ui/compose/text/style-paragraph#adjust-line-height
 */
@Composable
private fun textStyle() = LocalTextStyle.current.merge(
    TextStyle(
        lineHeight = 1.25.em,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Top,
            trim = LineHeightStyle.Trim.None
        )
    )
)

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
