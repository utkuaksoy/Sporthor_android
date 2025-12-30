package com.iamkurtgoz.domain.model.response

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GetClubsAndDetailsDomainModel(
    @SerialName("clubs")
    val clubs: List<GetClubsAndDetailsDomainModelClub?>?,
)

@Keep
@Serializable
data class GetClubsAndDetailsDomainModelClub(
    @SerialName("coaches")
    val coaches: List<GetClubsAndDetailsDomainModelCoache?>?,
    @SerialName("id")
    val id: String?,
    @SerialName("logo")
    val logo: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("trainingGroups")
    val trainingGroups: List<GetClubsAndDetailsDomainModelTrainingGroup?>?,
)

@Keep
@Serializable
data class GetClubsAndDetailsDomainModelCoach(
    @SerialName("id")
    val id: String?,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("isCurrentUser")
    val isCurrentUser: Boolean?,
    @SerialName("isFollow")
    val isFollow: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("username")
    val username: String?,
)

@Keep
@Serializable
data class GetClubsAndDetailsDomainModelCoache(
    @SerialName("id")
    val id: String?,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("isCurrentUser")
    val isCurrentUser: Boolean?,
    @SerialName("isFollow")
    val isFollow: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("username")
    val username: String?,
)

@Keep
@Serializable
data class GetClubsAndDetailsDomainModelTrainingGroup(
    @SerialName("coach")
    val coach: GetClubsAndDetailsDomainModelCoach?,
    @SerialName("id")
    val id: String?,
    @SerialName("name")
    val name: String?,
)

object GetClubsAndDetailsDomainModelMock {
    fun mock(): GetClubsAndDetailsDomainModel {
        return GetClubsAndDetailsDomainModel(
            clubs = listOf(
                // Fenerbahçe Test Takım
                GetClubsAndDetailsDomainModelClub(
                    id = "687997d99f0b5e1be60d78c0",
                    name = "Fenerbahçe Test Takım 1",
                    logo = "https://api.sporthor.com/Uploads/493b9376-7d23-4ea6-8bb9-deca2f9eed03.jpg",
                    coaches = listOf(
                        GetClubsAndDetailsDomainModelCoache(
                            id = "685c63585970f8eef1545150",
                            name = "Der Turke",
                            username = "derturke3",
                            summary = "Experienced volleyball coach with 10+ years",
                            imageUrl = "https://api.sporthor.com/Uploads/d1fbeb8c-f3e9-488c-be9d-aa85998c8db5.jpg",
                            isFollow = false,
                            isCurrentUser = false,
                        ),
                        GetClubsAndDetailsDomainModelCoache(
                            id = "688a6702455a3fcf18ff66fe",
                            name = "Ersin Terzi",
                            username = "terziersin",
                            summary = "Professional sports trainer",
                            imageUrl = null,
                            isFollow = false,
                            isCurrentUser = false,
                        ),
                        GetClubsAndDetailsDomainModelCoache(
                            id = "688a6ab0e05363d83b6a6062",
                            name = "Memati Bas",
                            username = "mematibas",
                            summary = "Youth development specialist",
                            imageUrl = null,
                            isFollow = false,
                            isCurrentUser = true,
                        ),
                    ),
                    trainingGroups = listOf(
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "687eab38777dd7173c66fe4b",
                            name = "Başlangıç-Miniminik Kız",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "685c63585970f8eef1545150",
                                name = "Der Turke",
                                username = "derturke3",
                                summary = "Experienced volleyball coach",
                                imageUrl = "https://api.sporthor.com/Uploads/d1fbeb8c-f3e9-488c-be9d-aa85998c8db5.jpg",
                                isFollow = false,
                                isCurrentUser = false,
                            ),
                        ),
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "688a86f1d4277a78637964ce",
                            name = "Başlangıç-Miniminik Erkek",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "685c63585970f8eef1545150",
                                name = "Der Turke",
                                username = "derturke3",
                                summary = "Experienced volleyball coach",
                                imageUrl = "https://api.sporthor.com/Uploads/d1fbeb8c-f3e9-488c-be9d-aa85998c8db5.jpg",
                                isFollow = false,
                                isCurrentUser = false,
                            ),
                        ),
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "688a6b5be05363d83b6a6063",
                            name = "Social voleybol kulübü",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "688a6702455a3fcf18ff66fe",
                                name = "Ersin Terzi",
                                username = "terziersin",
                                summary = "Professional sports trainer",
                                imageUrl = null,
                                isFollow = false,
                                isCurrentUser = false,
                            ),
                        ),
                    ),
                ),

                // Galatasaray Mock Club
                GetClubsAndDetailsDomainModelClub(
                    id = "688a60da6357d0d2c059968e",
                    name = "Galatasaray Spor Kulübü",
                    logo = "https://api.sporthor.com/Uploads/ab3fd942-09cc-483e-9005-74c76c1dbc10.jpg",
                    coaches = listOf(
                        GetClubsAndDetailsDomainModelCoache(
                            id = "689b7f08a2774cbac35a22fe",
                            name = "Ahmet Yılmaz",
                            username = "ahmetyilmaz",
                            summary = "Senior basketball coach",
                            imageUrl = "https://api.sporthor.com/Uploads/coach-ahmet.jpg",
                            isFollow = true,
                            isCurrentUser = false,
                        ),
                        GetClubsAndDetailsDomainModelCoache(
                            id = "689b7f15a2774cbac35a22ff",
                            name = "Zeynep Kaya",
                            username = "zeynepkaya",
                            summary = "Youth athletics coordinator",
                            imageUrl = "https://api.sporthor.com/Uploads/coach-zeynep.jpg",
                            isFollow = false,
                            isCurrentUser = false,
                        ),
                    ),
                    trainingGroups = listOf(
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "689b7f20a2774cbac35a2300",
                            name = "U-16 Basketbol Takımı",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "689b7f08a2774cbac35a22fe",
                                name = "Ahmet Yılmaz",
                                username = "ahmetyilmaz",
                                summary = "Senior basketball coach",
                                imageUrl = "https://api.sporthor.com/Uploads/coach-ahmet.jpg",
                                isFollow = true,
                                isCurrentUser = false,
                            ),
                        ),
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "689b7f30a2774cbac35a2301",
                            name = "Atletizm Grubu",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "689b7f15a2774cbac35a22ff",
                                name = "Zeynep Kaya",
                                username = "zeynepkaya",
                                summary = "Youth athletics coordinator",
                                imageUrl = "https://api.sporthor.com/Uploads/coach-zeynep.jpg",
                                isFollow = false,
                                isCurrentUser = false,
                            ),
                        ),
                    ),
                ),

                // Beşiktaş Mock Club
                GetClubsAndDetailsDomainModelClub(
                    id = "688fb33bc44b0aeed6e872cb",
                    name = "Beşiktaş JK",
                    logo = "https://api.sporthor.com/Uploads/dca0f945-4f80-4603-ad20-ca1b112cc4a1.jpg",
                    coaches = listOf(
                        GetClubsAndDetailsDomainModelCoache(
                            id = "689c8g19b3885dcd46b33g0",
                            name = "Mehmet Özkan",
                            username = "mehmetozkan",
                            summary = "Football development coach",
                            imageUrl = "https://api.sporthor.com/Uploads/coach-mehmet.jpg",
                            isFollow = false,
                            isCurrentUser = false,
                        ),
                    ),
                    trainingGroups = listOf(
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "689c8g25b3885dcd46b33g1",
                            name = "U-14 Futbol Takımı",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "689c8g19b3885dcd46b33g0",
                                name = "Mehmet Özkan",
                                username = "mehmetozkan",
                                summary = "Football development coach",
                                imageUrl = "https://api.sporthor.com/Uploads/coach-mehmet.jpg",
                                isFollow = false,
                                isCurrentUser = false,
                            ),
                        ),
                    ),
                ),

                // Empty Club (no coaches, no training groups)
                GetClubsAndDetailsDomainModelClub(
                    id = "688fb359c44b0aeed6e872cc",
                    name = "İstanbul Spor Kulübü",
                    logo = "https://api.sporthor.com/Uploads/faabded4-0c40-49a3-b0cf-e0eb89d638db.jpg",
                    coaches = emptyList(),
                    trainingGroups = emptyList(),
                ),

                // Club with null values
                GetClubsAndDetailsDomainModelClub(
                    id = "688fb37bc44b0aeed6e872cd",
                    name = "Ankara Gençlik Kulübü",
                    logo = null,
                    coaches = listOf(
                        GetClubsAndDetailsDomainModelCoache(
                            id = "689d9h20c4996ede57c44h1",
                            name = "Ayşe Demir",
                            username = "aysedemir",
                            summary = null,
                            imageUrl = null,
                            isFollow = false,
                            isCurrentUser = false,
                        ),
                    ),
                    trainingGroups = listOf(
                        GetClubsAndDetailsDomainModelTrainingGroup(
                            id = "689d9h35c4996ede57c44h2",
                            name = "Yüzme Kursu",
                            coach = GetClubsAndDetailsDomainModelCoach(
                                id = "689d9h20c4996ede57c44h1",
                                name = "Ayşe Demir",
                                username = "aysedemir",
                                summary = null,
                                imageUrl = null,
                                isFollow = false,
                                isCurrentUser = false,
                            ),
                        ),
                    ),
                ),
            ),
        )
    }
}
