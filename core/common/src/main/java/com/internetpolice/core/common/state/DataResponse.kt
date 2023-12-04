package com.internetpolice.core.common.state

import com.internetpolice.core.common.exception.Failure
import com.internetpolice.core.common.util.INTERNAL_ERROR

sealed class DataResponse<T>

data class DataSuccessResponse<T>(
    val data: T? = null,
) : DataResponse<T>()

data class DataErrorResponse<T>(
    val statusCode: Int = INTERNAL_ERROR,
    val reason: Failure = Failure.None,
    val errorMessage: String? = null,
) : DataResponse<T>()