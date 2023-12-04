package com.internetpolice.profile_domain.model

data class RankingEarningInfo(
    val image: Int? = null,
    val xp: String? = null,
    val rank: Int? = null,
    val title: Int? = null,
    val contentList: List<ContentList>? = null
)

data class ContentList(
    val icon: Int? = null,
    val text: Int? = null
)
