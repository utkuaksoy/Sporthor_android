package com.iamkurtgoz.feature.home.paymentList.data.di

import com.iamkurtgoz.feature.home.paymentList.data.useCase.AddFeeUsersOrTrainingGroupsUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.data.useCase.GetFeeCategoryUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.data.useCase.GetFeesDetailUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.data.useCase.GetFeesUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.data.useCase.GetTrainingGroupUserUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.data.useCase.MarkAsPaidFeeUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.data.useCase.SendFeePaymentNotificationUseCaseImpl
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.AddFeeUsersOrTrainingGroupsUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeeCategoryUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeesDetailUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeesUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetTrainingGroupUserUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.MarkAsPaidFeeUseCase
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.SendFeePaymentNotificationUseCase
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
    abstract fun bindAddFeeUsersOrTrainingGroupsUseCase(
        impl: AddFeeUsersOrTrainingGroupsUseCaseImpl,
    ): AddFeeUsersOrTrainingGroupsUseCase

    @Binds
    @Singleton
    abstract fun bindGetFeesUseCase(
        impl: GetFeesUseCaseImpl,
    ): GetFeesUseCase

    @Binds
    @Singleton
    abstract fun bindGetFeesDetailUseCase(
        impl: GetFeesDetailUseCaseImpl,
    ): GetFeesDetailUseCase

    @Binds
    @Singleton
    abstract fun bindGetFeeCategoryUseCase(
        impl: GetFeeCategoryUseCaseImpl,
    ): GetFeeCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindGetTrainingGroupUserUseCase(
        impl: GetTrainingGroupUserUseCaseImpl,
    ): GetTrainingGroupUserUseCase

    @Binds
    @Singleton
    abstract fun bindMarkAsPaidFeeUseCase(
        impl: MarkAsPaidFeeUseCaseImpl,
    ): MarkAsPaidFeeUseCase

    @Binds
    @Singleton
    abstract fun bindSendFeePaymentNotificationUseCase(
        impl: SendFeePaymentNotificationUseCaseImpl,
    ): SendFeePaymentNotificationUseCase

}
