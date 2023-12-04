package com.internetpolice.app.permission

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AccessibilityViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {
    val isUserConsent = mutableStateOf(true)

    init {
        checkConsent()
    }

    fun checkConsent() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                userDao.getUsers().collect {

                    if (it.isNotEmpty()) {
                        isUserConsent.value = !it[0].isUserGiveConsent
                    }
                }
            }
        }

    }

    fun updateConsent() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.updateUserConsent(true)
        }
    }
}