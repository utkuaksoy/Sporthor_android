/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.feature.home.onboarding.fake

import com.iamkurtgoz.feature.home.onboarding.domain.model.OnBoardingPageItemUIModel

object MockOnBoarding {
    val list: List<OnBoardingPageItemUIModel> = listOf(
        OnBoardingPageItemUIModel(
            image = "/Uploads/pages/page1.png",
            title = "Takımın, Hedeflerin ve \r\nSenin Dünyan!",
            description = "Takım arkadaşlarınla iletişimde kal, antrenörlerinin görevlerine kolayca ulaş ve performansını zirveye taşı. Sporthor ile sporun her anını daha keyifli ve etkili hale getir! 🎯 🏆",
        ),
        OnBoardingPageItemUIModel(
            image = "/Uploads/pages/page1.png",
            title = "2 Takımın, Hedeflerin ve \r\nSenin Dünyan!",
            description = "Takım arkadaşlarınla iletişimde kal, antrenörlerinin görevlerine kolayca ulaş ve performansını zirveye taşı. Sporthor ile sporun her anını daha keyifli ve etkili hale getir! 🎯 🏆",
        ),
        OnBoardingPageItemUIModel(
            image = "/Uploads/pages/page1.png",
            title = "3 Takımın, Hedeflerin ve \r\nSenin Dünyan!",
            description = "Takım arkadaşlarınla iletişimde kal, antrenörlerinin görevlerine kolayca ulaş ve performansını zirveye taşı. Sporthor ile sporun her anını daha keyifli ve etkili hale getir! 🎯 🏆",
        ),
    )
}
