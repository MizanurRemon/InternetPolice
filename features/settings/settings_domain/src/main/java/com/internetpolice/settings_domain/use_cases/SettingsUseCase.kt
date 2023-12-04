package com.internetpolice.settings_domain.use_cases

data class SettingsUseCase(
    val getFaqList : GetFaqList,
    val getAboutUs: GetAboutUs,
    val reportProblemUseCase: ReportProblemUseCase,
    val descriptionHelpContact: HelpContactUseCase,
    val updateLanguageUseCases: UpdateLanguageUseCase
)
