package com.internetpolice.report_presentation.di

import com.internetpolice.report_presentation.ReportDetailsViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun mainViewModelFactory(): ReportDetailsViewModel.Factory
}

