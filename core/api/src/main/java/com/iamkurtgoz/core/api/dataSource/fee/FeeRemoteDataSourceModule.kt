package com.iamkurtgoz.core.api.dataSource.fee

import com.iamkurtgoz.data.dataSource.FeeRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FeeRemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindFeeRemoteDataSource(
        impl: FeeRemoteDataSourceImpl,
    ): FeeRemoteDataSource
}
