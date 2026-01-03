//
//  ProfileInfoDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

struct ProfileInfoDataModel: Decodable {
    let imageUrl: String?
    let username: String?
    let postCount: Int?
    let followerCount: Int?
    let followingCount: Int?
}
