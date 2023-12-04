package com.internetpolice.news_domain.di

import com.internetpolice.news_domain.repository.NewsRepository
import com.internetpolice.news_domain.use_cases.GetNewsData
import com.internetpolice.news_domain.use_cases.GetNewsDetails
import com.internetpolice.news_domain.use_cases.NewsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
class NewsDomainModule {
    @ViewModelScoped
    @Provides
    fun provideNewsUseCases(newsRepository: NewsRepository): NewsUseCases {
        return NewsUseCases(
            getNewsData = GetNewsData(newsRepository),
            getNewsDetails = GetNewsDetails(newsRepository)
        )
    }
}