package com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain.useCase

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.RemoveBlockUserRequest

interface RemoveBlockedUserUseCase : IUseCase<RemoveBlockUserRequest, RestResult<Unit>>
