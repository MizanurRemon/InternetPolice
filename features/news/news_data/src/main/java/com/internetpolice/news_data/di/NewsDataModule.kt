package com.internetpolice.news_data.di

import com.internetpolice.core.network.WebsiteApiService
import com.internetpolice.core.network.di.TypeEnum
import com.internetpolice.core.network.di.qualifier.WebsiteNetwork
import com.internetpolice.core.network.util.NetworkHandler
import com.internetpolice.news_data.dataSource.NewsRemoteDataSource
import com.internetpolice.news_data.dataSourceImpl.NewsRemoteDataSourceImpl
import com.internetpolice.news_data.repository.NewsRepositoryImpl
import com.internetpolice.news_domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NewsDataModule {
    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
        @WebsiteNetwork(TypeEnum.SERVICE) appApiService: WebsiteApiService
    ): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(appApiService)
    }

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsRemoteDataSource: NewsRemoteDataSource,
        networkHandler: NetworkHandler
    ): NewsRepository {
        return NewsRepositoryImpl(newsRemoteDataSource, networkHandler)
    }
}