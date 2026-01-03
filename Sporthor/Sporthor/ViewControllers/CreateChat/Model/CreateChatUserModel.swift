//
//  CreateChatUserModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//

import ModelParsers
import Foundation

struct ChatUserResponseModel: Decodable {
    @LossyArray
    private(set) var friends: [CreateChatUserModel]
}

struct CreateChatUserModel: Decodable {
    @SafeDecode
    private(set) var id: String
    @SafeDecode
    private(set) var name: String
    @SafeDecode
    private(set) var username: String
    @SafeDecode
    private(set) var summary: String
    @SafeDecode
    private(set) var imageUrl: String
    @SafeDecode
    private(set) var isFollow: Bool
    @SafeDecode
    private(set) var isCurrentUser: Bool
}
