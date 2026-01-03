//
//  NextMatchesDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import Foundation
import ModelParsers

struct NextMatchesDataModel: Decodable {
    let title: String?
    let matches: [MatchModel]?
}

struct MatchModel: Decodable {
    @SafeDecode
    private(set) var matchId: Int
    private(set) var leagueInfo: LeagueInfoModel?
    @LossyArray
    private(set) var teams: [MatchTeamModel]
}

struct LeagueInfoModel: Decodable {
    @SafeDecode
    private(set) var leagueLogoURL: String
    @SafeDecode
    private(set) var title: String
    @SafeDecode
    private(set) var date: String
}

struct MatchTeamModel: Decodable {
    @SafeDecode
    private(set) var teamId: Int
    @SafeDecode
    private(set) var teamName: String
    @SafeDecode
    private(set) var teamLogoImageUrl: String
    @SafeDecode
    private(set) var score: Int
}
