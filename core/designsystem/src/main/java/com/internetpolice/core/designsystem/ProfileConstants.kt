package com.internetpolice.core.designsystem

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

val IPRankingList = listOf(
    IpRanksType.OFFICER, IpRanksType.DETECTIVE, IpRanksType.SERGEANT, IpRanksType.LIEUTENANT
)

enum class IpRanksType(
    @StringRes val rankNameStringRes: Int,
    @DrawableRes val iconResId: Int,
    val minLimit: Int,
    val maxLimit: Int,
) {
    OFFICER(
        CommonR.string.officer,
        DesignSystemR.drawable.ic_rank_officer,
        minLimit = 0,
        maxLimit = 249
    ),
    DETECTIVE(
        CommonR.string.detective,
        DesignSystemR.drawable.ic_rank_detective,
        minLimit = 250,
        maxLimit = 1499
    ),
    SERGEANT(
        CommonR.string.sergeant,
        DesignSystemR.drawable.ic_sergeant,
        minLimit = 1500,
        maxLimit = 9999
    ),
    LIEUTENANT(
        CommonR.string.lieutenant,
        DesignSystemR.drawable.ic_lieutenant,
        minLimit = 10000,
        maxLimit = Int.MAX_VALUE
    );

    companion object {
        @JvmStatic
        fun fromScore(score: Int): IpRanksType =
            values().find { value -> value.maxLimit >= score && value.minLimit <= score } ?: OFFICER
    }

}

enum class IpReportStatusType(
    @StringRes val reportStatusTypeNameStringRes: Int,
    @DrawableRes val iconResId: Int,
    @DrawableRes val imageResId: Int,
    @StringRes val reportStatusContent: Int,
) {
    NEW(
        CommonR.string.pending,
        DesignSystemR.drawable.ic_pending,
        DesignSystemR.drawable.ic_female_cop_pending,
        CommonR.string.new_status_message
    ),
    PENDING(
        CommonR.string.pending,
        DesignSystemR.drawable.ic_pending,
        DesignSystemR.drawable.ic_female_cop_pending,
        CommonR.string.new_status_message
    ),
    REVIEWING(
        CommonR.string.checked,
        DesignSystemR.drawable.ic_verified,
        DesignSystemR.drawable.ic_female_cop_checked,
        CommonR.string.approve_status_message
    ),
    REVIEWED(
        CommonR.string.confirmed,
        DesignSystemR.drawable.ic_green_check,
        DesignSystemR.drawable.ic_female_cop_verified,
        CommonR.string.approve_status_message
    );
}

