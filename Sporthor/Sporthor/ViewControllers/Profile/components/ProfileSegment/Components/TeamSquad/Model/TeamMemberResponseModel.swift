//
//  TeamMemberResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import Foundation
import ModelParsers

struct TeamMemberResponseModel: Decodable {
    let teamMembers: [TeamGroup]?
    private enum CodingKeys: String, CodingKey {
        case teamMembers = "team_members"
    }
}

struct TeamGroup: Decodable {
    let title: String?
    let members: [TeamMemberItem]?
}

struct TeamMemberItem: Decodable {
    private(set) var name: String?
    private(set) var role: String?
    private(set) var imageURL: String?
    @SafeDecode
    private(set) var isFollowing: Bool
    
    private enum CodingKeys: String, CodingKey {
        case name, role
        case imageURL = "image_url"
        case isFollowing = "is_following"
    }
}
