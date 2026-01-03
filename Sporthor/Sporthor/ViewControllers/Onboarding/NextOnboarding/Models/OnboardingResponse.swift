//
//  OnboardingResponse.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//

struct OnboardingResponse: Decodable {
    var pages: [OnboardingModel]?
}

struct OnboardingModel: Decodable {
    var image: String?
    var title: String?
    var description: String?
}
