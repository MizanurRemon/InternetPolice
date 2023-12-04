package com.internetpolice.app.home.di

import com.internetpolice.app.home.use_cases.HomeUseCases
import com.internetpolice.news_domain.repository.NewsRepository
import com.internetpolice.news_domain.use_cases.GetNewsData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
class HomeDomainModule {
    @ViewModelScoped
    @Provides
    fun provideHomeUseCases(newsRepository: NewsRepository): HomeUseCases {
        return HomeUseCases(
            getNewsData = GetNewsData(newsRepository),
        )
    }
}