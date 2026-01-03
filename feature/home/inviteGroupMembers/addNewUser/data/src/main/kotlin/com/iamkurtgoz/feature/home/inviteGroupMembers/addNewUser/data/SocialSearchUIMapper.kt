package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.data

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.SearchSocialDomainModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.SocialSearchUIModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.domain.toSocialSearchUIItemType
import javax.inject.Inject

internal class SocialSearchUIMapper @Inject constructor() : IMapper<SearchSocialDomainModel, SocialSearchUIModel> {
    override fun map(response: SearchSocialDomainModel): SocialSearchUIModel {
        return with(response) {
            SocialSearchUIModel(
                searchList = searchList?.map {
                    SocialSearchUIItemModel(
                        id = it?.id,
                        image = it?.image,
                        name = it?.name,
                        attribute = it?.attribute,
                        type = it?.type?.toSocialSearchUIItemType(),
                    )
                },
            )
        }
    }
}
