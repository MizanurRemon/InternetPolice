package com.internetpolice.profile_presentation.avater

data class AvatarData(
    var skinTone: SkinBrushType? = null,
    var face: FaceType? = null,
    var hairColorType: HairColorType? = null,
    var hairStyleType: HairStyleType? = null,
    var genderType: GenderType? = null
)