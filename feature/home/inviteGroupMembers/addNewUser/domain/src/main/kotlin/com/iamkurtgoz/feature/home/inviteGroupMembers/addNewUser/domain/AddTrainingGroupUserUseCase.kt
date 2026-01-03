package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.domain.model.request.AddTrainingGroupUserRequest

interface AddTrainingGroupUserUseCase : IUseCase<AddTrainingGroupUserRequest?, RestResult<Unit>>

