//
//  GetClubsAndDetailsResponse.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//

import Foundation
import ModelParsers

struct GetClubsAndDetailsResponse: Decodable {
    @LossyArray var clubs: [GetClubsAndDetailClub]
}

struct GetClubsAndDetailClub: Decodable {
    @SafeDecode var id: String
    @SafeDecode var name: String
    @SafeDecode var logo: String
    @LossyArray var coaches: [GetTrainingGroupUserModelUser]
    @LossyArray var trainingGroups: [GetClubsAndDetailTrainingGroup]
}

struct GetClubsAndDetailTrainingGroup: Decodable {
    @SafeDecode var id: String
    @SafeDecode var name: String
    var coach: GetTrainingGroupUserModelUser?
    @LossyArray var coaches: [GetTrainingGroupUserModelUser]
}
