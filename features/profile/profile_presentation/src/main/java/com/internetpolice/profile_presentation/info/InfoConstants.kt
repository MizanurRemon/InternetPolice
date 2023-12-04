package com.internetpolice.profile_presentation.info

import com.internetpolice.core.designsystem.IpRanksType
import com.internetpolice.profile_domain.model.ContentList
import com.internetpolice.profile_domain.model.RankingEarningInfo
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.core.common.R as CommonR

val RANKING_LIST = listOf(
    RankingEarningInfo(
        image = IpRanksType.valueOf(IpRanksType.OFFICER.toString()).iconResId,//DesignSystemR.drawable.ic_rank_officer,
        rank = IpRanksType.valueOf(IpRanksType.OFFICER.toString()).rankNameStringRes,
        xp = "${IpRanksType.valueOf(IpRanksType.OFFICER.toString()).minLimit} - ${
            IpRanksType.valueOf(
                IpRanksType.OFFICER.toString()
            ).maxLimit
        }",
        contentList = listOf(
            ContentList(
                icon = DesignSystemR.drawable.ic_checked,
                text = CommonR.string.basic_protection
            )
        )
    ),
    RankingEarningInfo(
        image = IpRanksType.valueOf(IpRanksType.DETECTIVE.toString()).iconResId,//DesignSystemR.drawable.ic_rank_officer,
        rank = IpRanksType.valueOf(IpRanksType.DETECTIVE.toString()).rankNameStringRes,
        xp = "${IpRanksType.valueOf(IpRanksType.DETECTIVE.toString()).minLimit} - ${
            IpRanksType.valueOf(
                IpRanksType.DETECTIVE.toString()
            ).maxLimit
        }",
        contentList = listOf(
            ContentList(
                icon = DesignSystemR.drawable.ic_checked,
                text = CommonR.string.discount_on_premium_20
            )
        )
    ),
    RankingEarningInfo(
        image = IpRanksType.valueOf(IpRanksType.SERGEANT.toString()).iconResId,//DesignSystemR.drawable.ic_rank_officer,
        rank = IpRanksType.valueOf(IpRanksType.SERGEANT.toString()).rankNameStringRes,
        xp = "${IpRanksType.valueOf(IpRanksType.SERGEANT.toString()).minLimit} - ${
            IpRanksType.valueOf(
                IpRanksType.SERGEANT.toString()
            ).maxLimit
        }",
        contentList = listOf(
            ContentList(
                icon = DesignSystemR.drawable.ic_checked,
                text = CommonR.string.discount_on_premium_50
            )
        )
    ),
    RankingEarningInfo(
        image = IpRanksType.valueOf(IpRanksType.LIEUTENANT.toString()).iconResId,
        rank = IpRanksType.valueOf(IpRanksType.LIEUTENANT.toString()).rankNameStringRes,
        xp = "10,000+ XP",
        contentList = listOf(
            ContentList(
                icon = DesignSystemR.drawable.ic_checked,
                text = CommonR.string.free_family_subscription
            ),
            ContentList(
                icon = DesignSystemR.drawable.ic_checked,
                text = CommonR.string.community_moderator
            ),
            ContentList(
                icon = DesignSystemR.drawable.ic_checked,
                text = CommonR.string.official_advisor_for_internet_police
            )
        )
    )
)

val REPORTING_LIST = listOf(
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        xp = "1 XP",
        title = CommonR.string.report_a_website,
        contentList = listOf(ContentList(text = CommonR.string.when_reporting_a_suspicious_or_malicious))
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        xp = "10 XP",
        title = CommonR.string.providing_additional_information,
        contentList = listOf(ContentList(text = CommonR.string.when_you_provide_additional_information))
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        xp = "10 XP",
        title = CommonR.string.profile_completion,
        contentList = listOf(ContentList(text = CommonR.string.when_completing_the_creation_of_your_profile))
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_silver,
        xp = "25 XP",
        title = CommonR.string.verified_suspicious_report,
        contentList = listOf(ContentList(text = CommonR.string.when_your_report_for_a_suspicious_website_gets_verified))
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_yellow,
        xp = "200 XP",
        title = CommonR.string.verified_malicious_report,
        contentList = listOf(ContentList(text = CommonR.string.when_your_report_for_a_malicious_website_gets_verified))
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        xp = "-5 XP",
        title = CommonR.string.abusing_the_report_function,
        contentList = listOf(ContentList(text = CommonR.string.to_prevent_abuse_reporting_is_limited))
    )
)

val INVITING_SUGGESTING_LIST = listOf(
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.invite_user,
        xp = "5 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.invite_a_friend_to_join_us_help_make_the_internet_safer
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_silver,
        title = CommonR.string.invited_friend_levels_up,
        xp = "50 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_your_invited_friend_reaches_a_higher_rank
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_silver,
        title = CommonR.string.suggesting_a_potential_partner_for_us,
        xp = "25 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_you_suggest_a_potential_partner_for_us
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.suggested_partner_is_approved,
        xp = "500 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_the_potential_partner_you_suggested_is_approved
            )
        )
    )
)

val CONTRIBUTING_COMMUNITY_LIST = listOf(
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.starting_a_new_discussion,
        xp = "2 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_you_start_a_new_discussion
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.reply_in_a_discussion,
        xp = "1 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.reply_to_a_discussion_in_our_community
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.get_a_favorite_on_your_discussion,
        xp = "2 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_someone_adds_your_discussion_to_their_favorites
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.start_following_someone,
        xp = "1 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_you_start_following_someone
            )
        )
    ),
    RankingEarningInfo(
        image = DesignSystemR.drawable.ic_xp_point_orange,
        title = CommonR.string.get_a_follower,
        xp = "2 XP",
        contentList = listOf(
            ContentList(
                text = CommonR.string.when_someone_starts_following_you
            )
        )
    )
)