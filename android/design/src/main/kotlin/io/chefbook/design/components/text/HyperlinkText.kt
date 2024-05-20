package io.chefbook.design.components.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import io.chefbook.core.android.compose.providers.theme.LocalTheme

private const val URL_TAG = "URL"

@Composable
fun HyperlinkText(
  text: String,
  hyperlinks: List<Pair<String, String>>,
  modifier: Modifier = Modifier,
  style: TextStyle = LocalTheme.typography.body1,
) {
  val colors = LocalTheme.colors

  val annotatedString = buildAnnotatedString {
    append(text)
    addStyle(
      style = SpanStyle(colors.foregroundPrimary),
      start = 0,
      end = text.length
    )
    hyperlinks.map { it.first }.forEachIndexed { index, link ->
      val startIndex = text.lowercase().indexOf(link.lowercase())
      val endIndex = startIndex + link.length
      addStyle(
        style = SpanStyle(colors.tintPrimary),
        start = startIndex,
        end = endIndex
      )
      addStringAnnotation(
        tag = URL_TAG,
        annotation = hyperlinks[index].second,
        start = startIndex,
        end = endIndex
      )
    }
  }

  val uriHandler = LocalUriHandler.current

  ClickableText(
    text = annotatedString,
    modifier = modifier,
    style = style,
    onClick = {
      annotatedString
        .getStringAnnotations(URL_TAG, it, it)
        .firstOrNull()?.let { stringAnnotation ->
          uriHandler.openUri(stringAnnotation.item)
        }
    }
  )
}
