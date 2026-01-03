package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.data

import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.AddTrainingGroupUserUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.GetUserRelationUseCase
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SearchUseCase
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
    abstract fun bindSearchUseCase(
        impl: SearchUseCaseImpl,
    ): SearchUseCase

    @Binds
    @Singleton
    abstract fun bindGetFollowersUseCase(
        impl: UserRelationUseCaseImpl,
    ): GetUserRelationUseCase

    @Binds
    @Singleton
    abstract fun bindAddTrainingGroupUserUseCase(
        impl: AddTrainingGroupUserUseCaseImpl,
    ): AddTrainingGroupUserUseCase
}
