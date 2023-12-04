package com.internetpolice.profile_data.di

import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.di.TypeEnum
import com.internetpolice.core.network.di.qualifier.PrivateNetwork
import com.internetpolice.core.network.di.qualifier.PublicNetwork
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.database.dao.UserDao
import com.internetpolice.profile_data.dataSource.local.ProfileLocalDataSource
import com.internetpolice.profile_data.dataSource.remote.ProfileRemoteDataSource
import com.internetpolice.profile_data.dataSourceImpl.local.ProfileLocalDataSourceImpl
import com.internetpolice.profile_data.dataSourceImpl.remote.ProfileRemoteDataSourceImpl
import com.internetpolice.profile_data.repository.ProfileRepositoryImpl
import com.internetpolice.profile_domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ProfileDataModule {

    @Singleton
    @Provides
    fun provideProfileRemoteDataSource(
        @PublicNetwork(TypeEnum.SERVICE) publicApiService: PublicApiService,
        @PrivateNetwork(TypeEnum.SERVICE) privateApiService: PrivateApiService,
    ): ProfileRemoteDataSource {
        return ProfileRemoteDataSourceImpl(publicApiService, privateApiService)
    }

    @Singleton
    @Provides
    fun provideProfileLocalDataSource(
        userDao: UserDao,
    ): ProfileLocalDataSource {
        return ProfileLocalDataSourceImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideReportRepository(
        profileRemoteDataSource: ProfileRemoteDataSource,
        profileLocalDataSource: ProfileLocalDataSource,
        preferenceDataStoreHelper: PreferenceDataStoreHelper,
        networkHandler: NetworkHandler,
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            profileRemoteDataSource, profileLocalDataSource, preferenceDataStoreHelper, networkHandler
        )
    }
}