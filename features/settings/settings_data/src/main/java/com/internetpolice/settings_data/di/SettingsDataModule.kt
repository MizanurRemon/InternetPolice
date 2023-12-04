package com.internetpolice.settings_data.di

import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.WebsiteApiService
import com.internetpolice.core.network.di.TypeEnum
import com.internetpolice.core.network.di.qualifier.PrivateNetwork
import com.internetpolice.core.network.di.qualifier.PublicNetwork
import com.internetpolice.core.network.di.qualifier.WebsiteNetwork
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.database.dao.UserDao
import com.internetpolice.settings_data.dataSource.SettingsLocalDataSource
import com.internetpolice.settings_data.dataSource.SettingsRemoteDataSource
import com.internetpolice.settings_data.dataSourceImpl.SettingsLocalDataSourceImpl
import com.internetpolice.settings_data.dataSourceImpl.SettingsRemoteDataSourceImpl
import com.internetpolice.settings_data.repository.SettingsRepositoryImpl
import com.internetpolice.settings_domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SettingsDataModule {

    @Singleton
    @Provides
    fun provideWebPageRemoteDataSource(
        @WebsiteNetwork(TypeEnum.SERVICE) websiteApiService: WebsiteApiService,
        @PrivateNetwork(TypeEnum.SERVICE) privateApiService: PrivateApiService,
        @PublicNetwork(TypeEnum.SERVICE) publicApiService: PublicApiService,
    ): SettingsRemoteDataSource {
        return SettingsRemoteDataSourceImpl(websiteApiService, privateApiService, publicApiService)
    }

    @Singleton
    @Provides
    fun provideSettingsLocalDataSource(
        userDao: UserDao,
    ): SettingsLocalDataSource {
        return SettingsLocalDataSourceImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideWebPageRepository(
        settingsRemoteDataSource: SettingsRemoteDataSource,
        settingsLocalDataSource: SettingsLocalDataSource,
        preferenceDataStoreHelper: PreferenceDataStoreHelper,
        networkHandler: NetworkHandler,
    ): SettingsRepository {
        return SettingsRepositoryImpl(
            settingsRemoteDataSource,
            settingsLocalDataSource,
            preferenceDataStoreHelper,
            networkHandler
        )
    }
}