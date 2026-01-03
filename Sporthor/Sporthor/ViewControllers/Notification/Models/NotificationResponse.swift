//
//  NotificationResponse.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import Foundation
import ModelParsers

struct NotificationResponse: Decodable {
    @LossyArray var notifications: [NotificationModel]
}

struct NotificationModel: Decodable {
    @SafeDecode var id: String
    @SafeDecode var status: Bool
    @SafeDecode var isDeleted: Bool
    @SafeDecode var createdAt: String
    @SafeDecode var updatedAt: String
    @SafeDecode var deletedAt: String
    @SafeDecode var userId: String
    @SafeDecode var title: String
    @SafeDecode var message: String
    @SafeDecode var pushMessageType: PushMessageType
    @SafeDecode var notificationType: NotificationParamType
    @SafeDecode var sendDate: String
    @SafeDecode var image: String
    var data: NotificationDataModel?
}

struct NotificationDataModel: Decodable {
    @SafeDecode var userId: String
    @SafeDecode var username: String
    @SafeDecode var type: String
    @SafeDecode var messageId: String
    @SafeDecode var lastMessage: String
    @SafeDecode var sendDate: String
    @SafeDecode var unReadCount: String
    @SafeDecode var senderName: String
    @SafeDecode var imageURL: String
    @SafeDecode var groupId: String
    @SafeDecode var isGroup: String
    @SafeDecode var toUserId: String
    @SafeDecode var postId: String
    @SafeDecode var profilePhoto: String
    @SafeDecode var trainingGroupId: String
}

enum PushMessageType: Int, Decodable {
    case chat = 0
    case like = 1
    case follow = 2
    case newPost = 3
    case trainingGroupRequest = 4
    case newTask = 5
    case followRequest = 6
}

extension PushMessageType: SafeDecodable {
    static func safeDecode(from decoder: Decoder) throws -> PushMessageType {
        let container = try decoder.singleValueContainer()
        let rawValue = try? container.decode(Int.self)
        return PushMessageType(rawValue: rawValue ?? -1) ?? .chat
    }
}

enum NotificationParamType: Int, Decodable {
    case notification = 0
    case confirm = 1
}

extension NotificationParamType: SafeDecodable {
    static func safeDecode(from decoder: Decoder) throws -> NotificationParamType {
        let container = try decoder.singleValueContainer()
        let rawValue = try? container.decode(Int.self)
        return NotificationParamType(rawValue: rawValue ?? -1) ?? .notification
    }
}
