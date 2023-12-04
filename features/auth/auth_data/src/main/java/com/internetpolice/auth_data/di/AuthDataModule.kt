package com.internetpolice.auth_data.di

import android.content.Context
import com.internetpolice.auth.auth_domain.repository.AuthRepository
import com.internetpolice.auth_data.dataSource.local.AuthLocalDataSource
import com.internetpolice.auth_data.dataSource.remote.AuthRemoteDataSource
import com.internetpolice.auth_data.dataSourceImpl.local.AuthLocalDataSourceImpl
import com.internetpolice.auth_data.dataSourceImpl.remote.AuthRemoteDataSourceImpl
import com.internetpolice.auth_data.repository.AuthRepositoryImpl
import com.internetpolice.core.datastore.PreferenceDataStoreHelper
import com.internetpolice.core.network.PrivateApiService
import com.internetpolice.core.network.PublicApiService
import com.internetpolice.core.network.di.TypeEnum
import com.internetpolice.core.network.di.qualifier.PrivateNetwork
import com.internetpolice.core.network.di.qualifier.PublicNetwork
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AuthDataModule {

    @Singleton
    @Provides
    fun provideAuthRemoteDataSource(
        @PublicNetwork(TypeEnum.SERVICE) publicApiService: PublicApiService,
        @PrivateNetwork(TypeEnum.SERVICE) privateApiService: PrivateApiService,
    ): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(publicApiService, privateApiService)
    }

    @Singleton
    @Provides
    fun provideAuthLocalDataSource(
        userDao: UserDao,
    ): AuthLocalDataSource {
        return AuthLocalDataSourceImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideNetworkHandler(
        @ApplicationContext context: Context,
    ): NetworkHandler {
        return NetworkHandler(context)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        authRemoteDataSource: AuthRemoteDataSource,
        authLocalDataSource: AuthLocalDataSource,
        preferenceDataStoreHelper: PreferenceDataStoreHelper,
        networkHandler: NetworkHandler,
    ): AuthRepository {
        return AuthRepositoryImpl(
            authRemoteDataSource, authLocalDataSource, preferenceDataStoreHelper, networkHandler
        )
    }
}