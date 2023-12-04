package com.internetpolice.auth.auth_domain.di

import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.auth.auth_domain.use_cases.*
import com.internetpolice.core.common.domain.use_case.EmailValidationResult
import com.internetpolice.core.common.domain.use_case.PasswordValidationResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class AuthDomainModule {
    @ViewModelScoped
    @Provides
    fun provideAuthUseCases(
        authRepository: AuthRepository,
        emailValidate: EmailValidationResult,
        passwordValidate: PasswordValidationResult,
    ): AuthUseCases {
        return AuthUseCases(
            postRegistrationData = RegistrationUseCase(authRepository),
            emailValidate = emailValidate,
            passwordValidate = passwordValidate,
            verifyTokenUseCase = VerifyTokenUseCase(authRepository),
            resendVerificationEmilUseCase = ResendVerificationEmilUseCase(authRepository),
            loginUseCase = LoginUseCase(authRepository),
            resetPasswordUseCase = ResetPasswordUseCase(authRepository),
            resetPasswordLinkSendUseCase = ResetPasswordLinkSendUseCase(authRepository),
            registrationAllowUseCases = RegistrationAllowUseCases(authRepository),
            userWaitingUseCases = UserWaitingUseCases(authRepository),
            updateEmailUseCase = UpdateEmailUseCase(authRepository),
            authenticationDraftUseCase = AuthenticationDraftUseCase(authRepository),
            draftVerificationUseCases = DraftVerificationUseCases(authRepository),
            draftUserTokenVerifyUseCase = DraftUserTokenVerifyUseCases(authRepository)
        )
    }
}