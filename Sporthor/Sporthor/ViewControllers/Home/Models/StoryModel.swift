//
//  StoryModel.swift
//  Sporthor
//
//  Created by derTurke on 27.03.2025.
//

import Foundation
import ModelParsers

struct StoryResponse: Decodable, Equatable, Hashable {
    var stories: [Story]?
}

struct Story: Decodable, Equatable, Hashable {
//    var id: String = UUID().uuidString
    @SafeDecode var userId: String
    @SafeDecode var username: String
    @SafeDecode var name: String
    @SafeDecode var lastName: String
    @SafeDecode var isOwn: Bool
    @SafeDecode var profileImageUrl: String
    @LossyArray var details: [StoryDetail]
    @SafeDecode var isWatched: Bool
}

struct StoryDetail: Decodable, Equatable, Hashable {
    @SafeDecode var stroryId: String
    var media: MediaItem?
    @SafeDecode var publishDate: String
    @SafeDecode var isWatched: Bool
}
