package com.iamkurtgoz.feature.home.paymentList.data.useCase

import com.iamkurtgoz.core.common.model.RestResult
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.iamkurtgoz.domain.model.request.GetFeesRequest
import com.iamkurtgoz.domain.model.response.FeeGroupDomainModel
import com.iamkurtgoz.domain.model.response.FeeSummaryDomainModel
import com.iamkurtgoz.domain.model.response.FeeUserDomainModel
import com.iamkurtgoz.domain.model.response.GetFeesDomainModel
import com.iamkurtgoz.domain.repository.FeeRepository
import com.iamkurtgoz.feature.home.paymentList.domain.useCase.GetFeesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetFeesUseCaseImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val feeRepository: FeeRepository,
) : GetFeesUseCase, CoreUseCase(coroutineDispatcher) {

    // Keep mock disabled so the screen reflects backend data.
    private val useMockResponse = false

    override fun invoke(params: GetFeesRequest): Flow<RestResult<GetFeesDomainModel>> = prepare {
        if (useMockResponse) {
            RestResult.Success(
                result = buildMockResponse(filterType = params.filterType),
            )
        } else {
            feeRepository.getFees(params)
        }
    }

    private fun buildMockResponse(filterType: Int): GetFeesDomainModel {
        val allGroups = listOf(
            FeeGroupDomainModel(
                trainingGroupId = "grp_101",
                trainingGroupName = "Eczacıbaşı Spor Kulübü - Yıldız Kız / Eczacıbaşı C",
                trainingGroupPhotoUrl = null,
                summary = FeeSummaryDomainModel(
                    completedCount = 4,
                    overdueCount = 1,
                ),
                users = listOf(
                    FeeUserDomainModel("u-1", "Emine Türk", null, 2, "40.000 TL", 40000.0, "TRY"),
                    FeeUserDomainModel("u-2", "Cansu Bilgi", null, 1, "20.000 TL", 20000.0, "TRY"),
                    FeeUserDomainModel("u-3", "Gizem Leyla Tuna", null, 1, "20.000 TL", 20000.0, "TRY"),
                    FeeUserDomainModel("u-4", "Yeşim Karalı", null, 1, "20.000 TL", 20000.0, "TRY"),
                ),
            ),
            FeeGroupDomainModel(
                trainingGroupId = "grp_102",
                trainingGroupName = "Eczacıbaşı Spor Kulübü - Yıldız Kız / Eczacıbaşı B",
                trainingGroupPhotoUrl = null,
                summary = FeeSummaryDomainModel(
                    completedCount = 8,
                    overdueCount = 1,
                ),
                users = listOf(
                    FeeUserDomainModel("u-5", "Aylin Koc", null, 2, null, 32000.0, "TRY"),
                    FeeUserDomainModel("u-6", "Mina Kurt", null, 1, "18.000 TL", 18000.0, "TRY"),
                ),
            ),
            FeeGroupDomainModel(
                trainingGroupId = "grp_103",
                trainingGroupName = "Fenerbahçe Spor Kulübü - U16 A Takımı",
                trainingGroupPhotoUrl = null,
                summary = FeeSummaryDomainModel(
                    completedCount = 2,
                    overdueCount = 1,
                ),
                users = listOf(
                    FeeUserDomainModel("u-7", "Defne Öztürk", null, 3, null, null, "TRY"),
                    FeeUserDomainModel("u-8", null, null, null, "15.000 TL", 15000.0, "TRY"),
                ),
            ),
        )

        val filteredGroups = when (filterType) {
            // Odenenler
            1 -> allGroups.filter { group ->
                group.users.orEmpty().any { user -> (user.paymentStatus ?: 0) == 1 }
            }
            // Odenmemis
            2 -> allGroups.filter { group ->
                group.users.orEmpty().any { user -> (user.paymentStatus ?: 0) == 2 }
            }
            // Iptal Edilen
            3 -> allGroups.filter { group ->
                group.users.orEmpty().any { user -> (user.paymentStatus ?: 0) == 3 }
            }
            else -> allGroups
        }

        return GetFeesDomainModel(
            filterType = filterType,
            isShowPlus = true,
            fees = filteredGroups,
        )
    }
}
