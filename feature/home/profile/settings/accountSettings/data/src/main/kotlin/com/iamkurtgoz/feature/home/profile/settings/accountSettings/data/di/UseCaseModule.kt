package com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.di

import com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.useCase.DeleteAccountUseCaseImpl
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.useCase.GetBlockedUsersUseCaseImpl
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.data.useCase.UpdateProfilePublicPrivateUseCaseImpl
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.DeleteAccountUseCase
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.GetBlockedUsersUseCase
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase.UpdateProfilePublicPrivateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindDeleteAccountUseCase(
        impl: DeleteAccountUseCaseImpl,
    ): DeleteAccountUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateProfilePublicPrivateUseCase(
        impl: UpdateProfilePublicPrivateUseCaseImpl,
    ): UpdateProfilePublicPrivateUseCase

    @Binds
    @Singleton
    abstract fun bindGetBlockedUsersUseCase(
        impl: GetBlockedUsersUseCaseImpl,
    ): GetBlockedUsersUseCase
}
