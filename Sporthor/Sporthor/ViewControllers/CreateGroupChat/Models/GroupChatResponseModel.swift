//
//  GroupChatResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 4.04.2025.
//

import Foundation
import ModelParsers

struct GroupChatResponseModel: Decodable {
    @SafeDecode
    private(set) var id: String
    @SafeDecode
    private(set) var name: String
    @SafeDecode
    private(set) var image: String
    @LossyArray
    private(set) var users: [String]
    @SafeDecode
    private(set) var toUserId: String
}
