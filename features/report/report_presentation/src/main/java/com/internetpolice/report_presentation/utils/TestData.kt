package com.internetpolice.report_presentation.utils

import androidx.compose.ui.graphics.Color
import com.internetpolice.core.designsystem.R as DesignSystemR
import com.internetpolice.report_presentation.screen_website.*

val testWebSearchDetailsScreenParams = WebsiteSearchDetailsScreenParams(
    websiteName = "Github.com",
    actionTitle = "Is listed as suspicious",
    topBannerActionColor = Color(0xffDC9605),
    topBannerBgID = DesignSystemR.drawable.ic_cop_medium,
    resultTitle = "github.com is listed as Malicious",
    results = listOf(
        Result(isPositive = true, resultText = "WHOIS identity is not available"),
        Result(isPositive = true, resultText = "Website age is very young"),
        Result(
            isPositive = true,
            resultText = "This website is hosted in a high risk country"
        ),
        Result(
            isPositive = true,
            resultText = "Website is listed as malicious by <b>DomainTools</b>"
        ),
        Result(
            isPositive = false,
            resultText = "Website is listed as malicious by <b>Phishunt.io</b>"
        )
    ),
    reports = listOf(
        Report(
            reporterProfilePicUrl =
            "https://www.gravatar.com/avatar/2433495de6d2b99746f8e25344209fa7?s=256&d=identicon&r=PG",
            category = ReportCategory.MALICIOUS,
            subCategory = ReportSubCategory.PHISHING,
            reporterName = "Tommie_de_Held",
            designation = "Officer",
            reportText =
            "Lorem Ipsum is simply dummy text of the printing and Lorem Ipsum is simply dummy" +
                    " text of the printing and Lorem Ipsum is simply dummy text of the printing" +
                    " and Lorem Ipsum is simply dummy text of the printing and Lorem Ipsum " +
                    "is simply dummy text of the printing and."
        ),
        Report(
            reporterProfilePicUrl =
            "https://www.gravatar.com/avatar/2433495de6d2b99746f8e25344209fa7?s=256&d=identicon&r=PG",
            category = ReportCategory.SUSPICIOUS,
            subCategory = ReportSubCategory.PHISHING,
            reporterName = "Tommie_de_Held",
            designation = "Officer",
            reportText =
            "Lorem Ipsum is simply dummy text of the printing and Lorem Ipsum is simply dummy" +
                    " text of the printing and Lorem Ipsum is simply dummy text of the printing" +
                    " and Lorem Ipsum is simply dummy text of the printing and Lorem Ipsum " +
                    "is simply dummy text of the printing and."
        )
    ),
    hasUserSubscription = true,
    score = 12.0
)
val testNewsTypeNames = listOf("ALL", "SCAM", "WHATSAPP FRAUD", "DATING FRAUD", "PHISHING FRAUD")
const val testLongText =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n" +
            "" +
            "It was popularised in the 1960s with the release of Elettra sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. \n" +
            "" +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "" +
            "-"
val  testImageUrl="https://picsum.photos/200"

