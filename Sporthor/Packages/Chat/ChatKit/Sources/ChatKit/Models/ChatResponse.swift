//
//  ChatResponse.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 19.02.2025.
//

import Foundation
import ModelParsers

public struct ChatResponse: Decodable {
    public let users: [UserModel]?
    public let messages: [ChatMessageResponse]?
}

public struct ChatMessageResponse: Decodable {
    public let id: String?
    public let sendDate: String?
    public let content: String?
    public let messageType: Int?
    public let from: ChatUserResponse?
    public let fileExtension: String?
}

public struct ChatUserResponse: Decodable {
    public let id: String?
    public let userType: String?
    public let name: String?
    public let image: String?
}

public struct UserModel: Decodable {
    @SafeDecode
    public var id: String
    @SafeDecode
    public var username: String
    @SafeDecode
    public var name: String
    @SafeOptionalDecode
    public var imageUrl: String?
    @SafeDecode
    public var isFollow: Bool
    @SafeDecode
    public var summary: String
    @SafeDecode
    public var isCurrentUser: Bool
}
