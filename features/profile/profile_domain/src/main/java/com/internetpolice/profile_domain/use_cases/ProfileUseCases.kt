package com.internetpolice.profile_domain.use_cases


data class ProfileUseCases(
    val profileCompleteUseCase: ProfileCompleteUseCase,
    val userNameUpdateUseCase: UserNameUpdateUseCase,
    val userDataUpdateUseCase: UserDataUpdateUseCase,
    val progressUseCase: ProgressUseCase,
    val updateScoreUseCase: UpdateScoreUseCase,
    val logOutUseCase: LogOutUseCase,
    val passwordResetUseCase: PasswordChangeUseCase,
    val deleteAccountUseCase: DeleteAccountUseCase,
    val basicInfoUpdateUseCase: BasicInfoUpdateUseCase
)