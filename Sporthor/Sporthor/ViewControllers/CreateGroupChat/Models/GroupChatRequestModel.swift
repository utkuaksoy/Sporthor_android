//
//  GroupChatRequestModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 4.04.2025.
//

struct GroupChatRequestModel: Encodable {
    var name: String
    var image: String?
    var users: [String]
    var isPrivate: Bool
}
