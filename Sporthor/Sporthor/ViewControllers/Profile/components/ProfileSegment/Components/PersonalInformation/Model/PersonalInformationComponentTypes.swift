//
//  PersonalInformationComponentTypes.swift
//  Sporthor
//
//  Created by Mesut on 20.03.2025.
//

import Foundation

enum PersonalInformationComponentTypes: String, Codable {
    case profileCard = "ProfileCard"
    case currentTeams = "CurrentTeams"
    case featuredSkills = "FeaturedSkills"
    case tournaments = "Tournaments"
    case careerHistory = "CareerHistory"
    
    static let componentModelMap: [Self: Decodable.Type] = [
        .profileCard: PersonaInfoDataModel.self,
        .currentTeams: TeamsDataModel.self,
        .featuredSkills: FeaturedSkillsDataModel.self,
        .tournaments: TournamentDataModel.self,
        .careerHistory: CareerHistoryDataModel.self
    ]
}
