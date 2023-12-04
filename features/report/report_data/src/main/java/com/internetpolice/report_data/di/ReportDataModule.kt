package com.internetpolice.report_data.di

import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.di.TypeEnum
import com.internetpolice.core.network.di.qualifier.PrivateNetwork
import com.internetpolice.core.network.di.qualifier.PublicNetwork
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.database.dao.UserDao
import com.internetpolice.report_data.dataSource.local.ReportLocalDataSource
import com.internetpolice.report_data.dataSource.remote.ReportRemoteDataSource
import com.internetpolice.report_data.dataSourceImpl.local.ReportLocalDataSourceImpl
import com.internetpolice.report_data.dataSourceImpl.remote.ReportRemoteDataSourceImpl
import com.internetpolice.report_data.repository.ReportRepositoryImpl
import com.internetpolice.report_domain.repository.ReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ReportDataModule {

    @Singleton
    @Provides
    fun provideReportRemoteDataSource(
        @PublicNetwork(TypeEnum.SERVICE) publicApiService: PublicApiService,
        @PrivateNetwork(TypeEnum.SERVICE) privateApiService: PrivateApiService,
    ): ReportRemoteDataSource {
        return ReportRemoteDataSourceImpl(publicApiService, privateApiService)
    }

    @Singleton
    @Provides
    fun provideReportLocalDataSource(
        userDao: UserDao,
    ): ReportLocalDataSource {
        return ReportLocalDataSourceImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideReportRepository(
        reportRemoteDataSource: ReportRemoteDataSource,
        reportLocalDataSource: ReportLocalDataSource,
        preferenceDataStoreHelper: PreferenceDataStoreHelper,
        networkHandler: NetworkHandler,
    ): ReportRepository {
        return ReportRepositoryImpl(
            reportRemoteDataSource, reportLocalDataSource, preferenceDataStoreHelper, networkHandler
        )
    }
}