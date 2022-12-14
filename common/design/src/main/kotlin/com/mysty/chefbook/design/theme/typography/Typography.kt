package com.mysty.chefbook.design.theme.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.mysty.chefbook.core.ui.compose.providers.theme.Typography
import com.mysty.chefbook.design.R

private val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold)
)

@OptIn(ExperimentalUnitApi::class)
internal val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Inter,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = TextUnit(1.2F, TextUnitType.Em)
    ),
    h2 = TextStyle(
        fontFamily = Inter,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    h3 = TextStyle(
        fontFamily = Inter,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    h4 = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    headline1 = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    headline2 = TextStyle(
        fontFamily = Inter,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    body1 = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    body2 = TextStyle(
        fontFamily = Inter,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    subhead1 = TextStyle(
        fontFamily = Inter,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    subhead2 = TextStyle(
        fontFamily = Inter,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = TextUnit(1.3F, TextUnitType.Em)
    ),
    caption1 = TextStyle(
        fontFamily = Inter,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = TextUnit(1.4F, TextUnitType.Em)
    ),
    caption2 = TextStyle(
        fontFamily = Inter,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = TextUnit(1.4F, TextUnitType.Em)
    )
)
