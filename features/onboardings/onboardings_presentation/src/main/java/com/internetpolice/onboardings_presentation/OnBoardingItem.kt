package com.internetpolice.onboardings_presentation

import com.internetpolice.core.common.R as CommonR
import com.internetpolice.core.designsystem.R as DesignSystemR

class OnBoardingItem(
    val drawableId: Int,
    val titleStringId: Int,
    val subTitleStringId: Int
) {

    companion object {
        fun getData(): List<OnBoardingItem> {
            return listOf(
                OnBoardingItem(
                    drawableId = DesignSystemR.drawable.ic_onboard_1,
                    CommonR.string.onBoardingTitle1,
                    CommonR.string.onBoardingSubTitle1
                ),
                OnBoardingItem(
                    drawableId = DesignSystemR.drawable.ic_onboard_2,
                    CommonR.string.onBoardingTitle2,
                    CommonR.string.onBoardingSubTitle2
                ),
                OnBoardingItem(
                    drawableId = DesignSystemR.drawable.ic_onboard_3,
                    CommonR.string.onBoardingTitle3,
                    CommonR.string.onBoardingSubTitle3
                ),
            )
        }
    }
}
