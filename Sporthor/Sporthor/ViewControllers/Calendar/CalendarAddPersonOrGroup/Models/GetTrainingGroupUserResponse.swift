//
//  GetTrainingGroupUserResponse.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import Foundation
import ModelParsers

struct GetTrainingGroupUserResponse: Decodable {
    @LossyArray var groups: [GetTrainingGroupUserModel]
}

struct GetTrainingGroupUserModel: Decodable {
    @SafeDecode var groupName: String
    @SafeDecode var groupImage: String
    @SafeDecode var groupId: String
    @SafeDecode var season: String
    var team: TeamItemModel?
    @LossyArray var users: [GetTrainingGroupUserModelUser]
    @LossyArray var coaches: [GetTrainingGroupUserModelUser]
    @SafeDecode var isSelected: Bool
}

struct GetTrainingGroupUserModelUser: Decodable {
    @SafeDecode var id: String
    @SafeDecode var name: String
    @SafeDecode var username: String
    @SafeDecode var summary: String
    @SafeDecode var imageUrl: String
    @SafeDecode var isFollow: Bool
    @SafeDecode var isCurrentUser: Bool
    @SafeDecode var isSelected: Bool
}
