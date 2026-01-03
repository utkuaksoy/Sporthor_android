//
//  MessagesResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//

import Foundation
import ModelParsers

struct MessagesResponseModel: Decodable {
    @LossyArray
    private(set) var messages: [MessagesItemModel]
}

struct MessagesItemModel: Decodable {
    @SafeDecode
    private(set) var messageId: String
    @SafeDecode
    private(set) var isGroup: Bool
    @SafeDecode
    private(set) var image: String
    @SafeDecode
    private(set) var name: String
    @SafeDecode
    private(set) var userName: String
    @SafeDecode
    var lastMessage: String
    @SafeDecode
    private(set) var userId: String
    @SafeDecode
    var unReadMessageCount: Int
    @SafeDecode
    private(set) var messageDate: String
    @SafeOptionalDecode
    private(set) var toUserId: String?
   
}
