//
//  TournamentDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import Foundation

struct TournamentDataModel: Decodable {
    let title: String?
    let tournaments: [TournamentModel]?
}

struct TournamentModel: Decodable {
    let title: String?
    let stage: String?
    let stageIcon: String?
    let icon: String?
}
