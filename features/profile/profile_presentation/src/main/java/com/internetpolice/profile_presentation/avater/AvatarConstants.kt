package com.internetpolice.profile_presentation.avater

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.internetpolice.core.common.R as commonR
import com.internetpolice.core.designsystem.R as designSystemR


val SkinBrush = listOf(
    Pair(
        SkinBrushType.light,
        Brush.linearGradient(
            colors = listOf(
                Color(0xffFFDECC),
                Color(0xffFFB589),
            ),
        )
    ),

    Pair(
        SkinBrushType.lightdark,
        Brush.linearGradient(
            colors = listOf(
                Color(0xffB57566),
                Color(0xffD19587),
            ),
        )
    ),

    Pair(
        SkinBrushType.dark,
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF80534C),
                Color(0xffAB6F65),
            ),
        )
    ),
    Pair(
        SkinBrushType.pale,
        Brush.linearGradient(
            colors = listOf(
                Color(0xffE5C7AE),
                Color(0xffFFE9D6),
            ),
        )
    ),
)
val HairColorList = listOf(
    Pair(
        HairColorType.brown,
        Color(0xff64432F)
    ),
    Pair(HairColorType.lightred, Color(0xff874141)),
    Pair(HairColorType.darkred, Color(0xff582929)),
    Pair(HairColorType.blonde, Color(0xffA08343)),
)
val HairColorBrushList = listOf(
    Pair(
        HairColorType.brown,
        Brush.linearGradient(
            colors = listOf(
                Color(0xff64432F),
                Color(0xff64432F),
            ),
        )
    ),
    Pair(
        HairColorType.lightred,
        Brush.linearGradient(
            colors = listOf(
                Color(0xff874141),
                Color(0xff874141),
            ),
        )
    ),
    Pair(
        HairColorType.darkred,
        Brush.linearGradient(
            colors = listOf(
                Color(0xff582929),
                Color(0xff582929),
            ),
        )
    ),
    Pair(
        HairColorType.blonde,
        Brush.linearGradient(
            colors = listOf(
                Color(0xffA08343),
                Color(0xffA08343),
            ),
        )
    ),
)
val AvatarFaceColorResIdList = listOf(
    Pair(
        SkinBrushType.light,
        designSystemR.drawable.ic_face_1
    ),
    Pair(
        SkinBrushType.lightdark,
        designSystemR.drawable.ic_face_2
    ),
    Pair(
        SkinBrushType.dark,
        designSystemR.drawable.ic_face_3
    ),
    Pair(
        SkinBrushType.pale,
        designSystemR.drawable.ic_face_4
    )
)


val AvatarHairStyleResIdList = listOf(
    Pair(
        HairStyleType.short,
        designSystemR.drawable.ic_hair_style_1
    ),
    Pair(
        HairStyleType.ponytail,
        designSystemR.drawable.ic_hair_style_2
    ),
    Pair(
        HairStyleType.peaky,

        designSystemR.drawable.ic_hair_style_3
    ),
    Pair(
        HairStyleType.puffed,
        designSystemR.drawable.ic_hair_style_4
    ),
    Pair(
        HairStyleType.midlong,
        designSystemR.drawable.ic_hair_style_5
    ),
    Pair(
        HairStyleType.curly,
        designSystemR.drawable.ic_hair_style_6
    ),
)

val AvatarHairStyleMainResIdList = listOf(

    Pair(
        HairStyleType.short,
        designSystemR.drawable.ic_hair_style_1_main
    ),
    Pair(
        HairStyleType.ponytail,
        designSystemR.drawable.ic_hair_style_2_main
    ),
    Pair(
        HairStyleType.peaky,

        designSystemR.drawable.ic_hair_style_3_main
    ),
    Pair(
        HairStyleType.puffed,
        designSystemR.drawable.ic_hair_style_4_main
    ),
    Pair(
        HairStyleType.midlong,
        designSystemR.drawable.ic_hair_style_5_main
    ),
    Pair(
        HairStyleType.curly,
        designSystemR.drawable.ic_hair_style_6_main
    ),

    )


val AvatarFacePatternResIdList = listOf(

    Pair(
        FaceType.face6,
        designSystemR.drawable.ic_face_pattern_1
    ),
    Pair(
        FaceType.face1,
        designSystemR.drawable.ic_face_pattern_2
    ),
    Pair(
        FaceType.face5,
        designSystemR.drawable.ic_face_pattern_3
    ),
    Pair(
        FaceType.face4,
        designSystemR.drawable.ic_face_pattern_4
    ),
    Pair(
        FaceType.face3,
        designSystemR.drawable.ic_face_pattern_5
    ),
    Pair(
        FaceType.face2,
        designSystemR.drawable.ic_face_pattern_6
    ),
)
val AvatarFacePatternSelectionResIdList = listOf(
    Pair(
        FaceType.face1,
        designSystemR.drawable.ic_face_selection_1
    ),
    Pair(
        FaceType.face2,
        designSystemR.drawable.ic_face_selection_2
    ),
    Pair(
        FaceType.face3,
        designSystemR.drawable.ic_face_selection_3
    ),
    Pair(
        FaceType.face4,
        designSystemR.drawable.ic_face_selection_4
    ),
    Pair(
        FaceType.face5,
        designSystemR.drawable.ic_face_selection_5
    ),
    Pair(
        FaceType.face6,
        designSystemR.drawable.ic_face_selection_6
    ),
)


val AvatarGenderStringResIdList = listOf(
    Pair(
        GenderType.male,
        commonR.string.male
    ),
    Pair(
        GenderType.female,
        commonR.string.female
    ),
    Pair(
        GenderType.other,
        commonR.string.other
    ),
)


enum class SkinBrushType {
    light,
    lightdark,
    dark,
    pale,
}

enum class FaceType {
    face6,
    face1,
    face5,
    face4,
    face3,
    face2
}

enum class HairColorType {
    brown,
    lightred,
    darkred,
    blonde,
}

enum class HairStyleType {
    short,
    ponytail,
    peaky,
    puffed,
    midlong,
    curly,
}

enum class GenderType {
    male,
    female,
    other
}

fun <T : Enum<T>> getEnumIndex(enumType: Class<T>, name: String?): Int {
    return try {
        enumType.enumConstants?.first { it.name == name }?.ordinal?:-1
    } catch (e: NoSuchElementException) {
        -1;
    }
}

inline fun <reified T : Enum<T>> getEnumByName(name: String): T? {
    return enumValues<T>().find { it.name == name }
}

