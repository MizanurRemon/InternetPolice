package com.internetpolice.profile_presentation.info

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internetpolice.core.designsystem.IpRanksType
import com.internetpolice.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class InfoViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    val userRank = mutableStateOf(IpRanksType.OFFICER.toString())

    init {
        checkUserRank()
    }

    private fun checkUserRank() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                userDao.getUsers().collect {

                    if (it.isNotEmpty()) {
                        userRank.value = it[0].rank.toString()
                    }
                }
            }
        }

    }
}