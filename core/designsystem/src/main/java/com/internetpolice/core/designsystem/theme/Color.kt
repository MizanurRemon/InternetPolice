package com.internetpolice.core.designsystem.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

val ColorAppBackground = Color(0xFFEBEBEB)
val ColorPrimaryDark = Color(0xFF005DFF)
val ColorPrimaryLight = Color(0xFFE0EBFF)
val ColorError = Color(0xFFFF0000)
val ColorSuccess = Color(0xff7EBA1D)
val ColorDivider = Color(0xffE3E3E3)
val ColorTextPrimary = Color(0xff021561)
val ColorTextSecondary = Color(0xff606888)
val ColorTextFieldPlaceholder = Color(0xffA7AEC1)
val BORDER_COLOR = Color(0xFF8CBCFC)
val DISABLED_COLOR = Color(0xFF91B9FF)
val LIGHT_BLACK = Color(0xFF858585)
val SOCIAL_ICON_BORDER_C0LOR = Color(0xFFB7D4FF)
val CUSTOM_LIGHT_GRAY = Color(0xFFE2E7F0)
val ITEM_DETAILED_COLOR = Color(0xFFEBEBEB)
val SEARCH_BOX_BG = Color(0xFFEEF4FF)
val SEARCH_BOX_HINT_COLOR = Color(0xFF3468C8)

val AppBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF),
        Color(0xFFF3F6FF),
        Color(0xFFDBE3FF),
    ),
)
val AppBrushReversed = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFF0F0F0),
        Color(0xFFF3F6FF),
        Color(0xFFDBE3FF),
    ),
)
val MaliciousBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xffFFD4C6),
        Color(0xffFFFFFF),
        Color(0xffFFFFFF),
        Color(0xffFFFFFF),

        ),
)
val SuspiciousBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xffFFEBC0),
        Color(0xffFFFFFF),
        Color(0xffFFFFFF),
        Color(0xffFFFFFF),
    ),
)
val HighGreenBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF87CE6D),
        Color(0xFF82DC61),
    ),
)

val MediumOrangeBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFA3D00),
        Color(0xFFFFAC00),
        Color(0xFFFAAC03),
        Color(0xFFDBE9FF),
        Color(0xFFDCE9FF),
    ),
)

val LowRedBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFA3D00),
        Color(0xFFDBE9FF),
        Color(0xFFDCE9FF),
        Color(0xFFDBE9FF),
        Color(0xFFDCE9FF),
    ),
)





