package com.internetpolice.report_domain.di

import com.internetpolice.report_domain.repository.ReportRepository
import com.internetpolice.report_domain.use_cases.GetCategoryTypeUseCase
import com.internetpolice.report_domain.use_cases.GetDomainDetailsUseCase
import com.internetpolice.report_domain.use_cases.GetDomainListUseCase
import com.internetpolice.report_domain.use_cases.ReportDomainUseCase
import com.internetpolice.report_domain.use_cases.ReportUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ReportDomainModule {

    @Singleton
    @Provides
    fun provideReportUseCases(
        reportRepository: ReportRepository,
    ): ReportUseCases {
        return ReportUseCases(
            getCategoryTypeUseCase = GetCategoryTypeUseCase(reportRepository),
            reportDomainUseCase = ReportDomainUseCase(reportRepository),
            getDomainUseCase = GetDomainListUseCase(reportRepository),
            getDomainDetailsUseCase = GetDomainDetailsUseCase(reportRepository)
        )
    }
}