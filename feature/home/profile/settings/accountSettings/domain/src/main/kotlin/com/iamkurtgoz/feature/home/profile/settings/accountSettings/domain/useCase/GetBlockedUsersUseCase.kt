package com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCaseWithoutParams
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.response.BlockedUsersDomainModel

interface GetBlockedUsersUseCase : IUseCaseWithoutParams<RestResult<BlockedUsersDomainModel>>
