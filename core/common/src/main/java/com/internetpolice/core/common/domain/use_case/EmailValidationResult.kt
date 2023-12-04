package com.internetpolice.core.common.domain.use_case

import com.internetpolice.core.common.util.EMAIL_REGEX
import java.util.regex.Pattern

class EmailValidationResult {
    operator fun invoke(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_REGEX)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}