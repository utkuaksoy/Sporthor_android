//
//  TrainingGroup.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import Foundation
import ModelParsers

struct TrainingGroupResponse: Codable {
    @SafeDecode var trainingGroupId: String
    @SafeDecode var teamName: String
    @SafeDecode var logo: String
    @SafeDecode var teamId: String
    @SafeDecode var season: String
    @SafeDecode var groupName: String
    
    init(trainingGroupId: String = "",
         teamName: String = "",
         logo: String = "",
         teamId: String = "",
         season: String = "",
         groupName: String = "") {
        self.trainingGroupId = trainingGroupId
        self.teamName = teamName
        self.logo = logo
        self.teamId = teamId
        self.season = season
        self.groupName = groupName
    }
}
