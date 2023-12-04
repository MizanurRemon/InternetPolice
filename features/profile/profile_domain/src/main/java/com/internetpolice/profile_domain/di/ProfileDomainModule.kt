package com.internetpolice.profile_domain.di

import com.internetpolice.profile_domain.repository.ProfileRepository
import com.internetpolice.profile_domain.use_cases.BasicInfoUpdateUseCase
import com.internetpolice.profile_domain.use_cases.DeleteAccountUseCase
import com.internetpolice.profile_domain.use_cases.LogOutUseCase
import com.internetpolice.profile_domain.use_cases.PasswordChangeUseCase
import com.internetpolice.profile_domain.use_cases.ProfileCompleteUseCase
import com.internetpolice.profile_domain.use_cases.ProfileUseCases
import com.internetpolice.profile_domain.use_cases.ProgressUseCase
import com.internetpolice.profile_domain.use_cases.UpdateScoreUseCase
import com.internetpolice.profile_domain.use_cases.UserDataUpdateUseCase
import com.internetpolice.profile_domain.use_cases.UserNameUpdateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class ProfileDomainModule {
    @ViewModelScoped
    @Provides
    fun provideProfileUseCases(
        profileRepository: ProfileRepository,
    ): ProfileUseCases {
        return ProfileUseCases(
            profileCompleteUseCase = ProfileCompleteUseCase(profileRepository),
            userNameUpdateUseCase = UserNameUpdateUseCase(profileRepository),
            userDataUpdateUseCase = UserDataUpdateUseCase(profileRepository),
            progressUseCase = ProgressUseCase(profileRepository),
            updateScoreUseCase = UpdateScoreUseCase(profileRepository),
            logOutUseCase = LogOutUseCase(profileRepository),
            passwordResetUseCase = PasswordChangeUseCase(profileRepository),
            deleteAccountUseCase = DeleteAccountUseCase(profileRepository),
            basicInfoUpdateUseCase = BasicInfoUpdateUseCase(profileRepository)
        )
    }
}