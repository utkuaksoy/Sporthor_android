//
//  ChatUser.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 28.01.2025.
//

import Foundation
import MessageKit

public struct ChatUser: SenderType {
    public let senderId: String
    public let displayName: String
    public let image: String?
    public let isGroup: Bool?
    public let toUserId: String?
    
    public init(senderId: String, displayName: String, image: String?, isGroup: Bool? = false, toUserId: String? = nil) {
        self.senderId = senderId
        self.displayName = displayName
        self.image = image
        self.isGroup = isGroup
        self.toUserId = toUserId
    }
}
