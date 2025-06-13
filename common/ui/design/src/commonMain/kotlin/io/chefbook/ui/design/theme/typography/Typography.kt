package io.chefbook.ui.design.theme.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import io.chefbook.ui.utils.compose.providers.theme.Typography
import io.chefbook.ui.design.Res
import io.chefbook.ui.design.inter_medium
import io.chefbook.ui.design.inter_regular
import io.chefbook.ui.design.inter_semibold
import org.jetbrains.compose.resources.Font

@get:Composable
private val fontFamilyInter get() = FontFamily(
  Font(Res.font.inter_regular, FontWeight.Normal),
  Font(Res.font.inter_medium, FontWeight.Medium),
  Font(Res.font.inter_semibold, FontWeight.SemiBold)
)

@get:Composable
internal val chefBookTypography get(): Typography {
  val fontFamilyInter = fontFamilyInter
  return Typography(
    h1 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 28.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = TextUnit(1.2F, TextUnitType.Em)
    ),
    h2 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 22.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    h3 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 20.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    h4 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 16.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    headline1 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    headline2 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
      lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    body1 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 16.sp,
      fontWeight = FontWeight.Normal,
      lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    body2 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 14.sp,
      fontWeight = FontWeight.Normal,
      lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    subhead1 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    subhead2 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 10.sp,
      fontWeight = FontWeight.SemiBold,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    caption1 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    caption2 = TextStyle(
      fontFamily = fontFamilyInter,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      lineHeight = TextUnit(1.3F, TextUnitType.Em)
    )
  )
}
