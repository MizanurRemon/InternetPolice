package com.internetpolice.auth.auth_domain.use_cases

import android.util.Log
import com.internetpolice.auth.auth_domain.model.UserWaitingModel
import com.internetpolice.auth.auth_domain.model.UserWaitingResponse
import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.core.network.model.UserWaitingRequest

class UserWaitingUseCases(private val authRepository: AuthRepository) {
    suspend operator fun invoke(userWaitingModel: UserWaitingModel): Result<UserWaitingResponse> {
        return authRepository.userWaiting(
            UserWaitingRequest(
                email = userWaitingModel.email
            )
        )
    }
}