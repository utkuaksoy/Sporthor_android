package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain

import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.model.RestResult

interface SearchUseCase : IUseCase<String, RestResult<SocialSearchUIModel>>
