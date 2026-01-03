//
//  TeamsDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import Foundation

struct TeamsDataModel: Decodable {
    var title: String?
    var teams: [TeamModel]?
}

struct TeamModel: Decodable {
    var teamId: String?
    var teamLogoImageUrl: String?
    var teamName: String?
}
