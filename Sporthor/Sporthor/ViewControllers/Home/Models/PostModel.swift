//
//  PostModel.swift
//  Sporthor
//
//  Created by derTurke on 27.03.2025.
//

import Foundation
import ModelParsers

struct PostResponse: Decodable {
    var posts: [Post]?
}

struct Post: Decodable, Equatable, Hashable {
    @SafeDecode var id: String
    @SafeDecode var description: String
    @LossyArray var media: [MediaItem]
    @SafeDecode var likeCount: Int
    @SafeDecode var commentCount: Int
    @SafeDecode var createdAt: String
    @SafeDecode var userId: String
    @SafeDecode var username: String
    @SafeDecode var name: String
    @SafeDecode var lastName: String
    @SafeDecode var profileImageUrl: String
    @LossyArray var lastLikedUsers: [PostLastLikedUser]
    @LossyArray var lastComments: [PostLastComment]
    @SafeDecode var isLiked: Bool
    @SafeDecode var time: String
}

struct MediaItem: Codable, Equatable, Hashable {
    @SafeDecode var url: String
    @SafeDecode var type: Int
}

struct PostLastLikedUser: Decodable, Equatable, Hashable {
    @SafeDecode var userId: String
    @SafeDecode var username: String
    @SafeDecode var profileImageUrl: String
}

struct PostLastComment: Decodable, Equatable, Hashable {
    @SafeDecode var id: String
    @SafeDecode var userId: String
    @SafeDecode var text: String
    @SafeDecode var createdAt: String
    @SafeDecode var username: String
    @SafeDecode var profileImageUrl: String
}
