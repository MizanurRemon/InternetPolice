package com.internetpolice.settings_domain.di

import com.internetpolice.settings_domain.repository.SettingsRepository
import com.internetpolice.settings_domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
class SettingsDomainModule {

    @ViewModelScoped
    @Provides
    fun provideWebPageUseCases(
        settingsRepository: SettingsRepository,
    ): SettingsUseCase {
        return SettingsUseCase(
            getFaqList = GetFaqList(settingsRepository),
            getAboutUs = GetAboutUs(settingsRepository),
            reportProblemUseCase = ReportProblemUseCase(settingsRepository),
            descriptionHelpContact = HelpContactUseCase(settingsRepository),
            updateLanguageUseCases = UpdateLanguageUseCase(settingsRepository)
        )
    }
}