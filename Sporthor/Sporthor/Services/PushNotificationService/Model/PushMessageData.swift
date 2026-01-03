//
//  PushMessageData.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.04.2025.
//

import Foundation

public struct PushMessageData {
    let messageId: String
    let unReadCount: String
    let lastMessage: String
    let senderName: String
    let imageURL: String
    let groupId: String
    let isGroup: String
    let toUserId: String
    let userName: String
    let sendDate: String
    let type: String
    
    init(
        messageId: String,
        unReadCount: String,
        lastMessage: String,
        senderName: String,
        imageURL: String,
        groupId: String,
        isGroup: String,
        toUserId: String,
        userName: String,
        sendDate: String,
        type: String
    ) {
        self.messageId = messageId
        self.unReadCount = unReadCount
        self.lastMessage = lastMessage
        self.senderName = senderName
        self.imageURL = imageURL
        self.groupId = groupId
        self.isGroup = isGroup
        self.toUserId = toUserId
        self.userName = userName
        self.sendDate = sendDate
        self.type = type
    }
}
