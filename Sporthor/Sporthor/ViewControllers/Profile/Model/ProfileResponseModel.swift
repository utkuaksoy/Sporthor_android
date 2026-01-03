//
//  ProfileResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import ComponentBaseKit
import ModelParsers

struct ProfileResponseModel: Decodable {
    var info: ProfileInfoModel?
    @ProfileComponentsContracts.CollectionDecodable
    var components: [any CollectionComponent]
}

struct ProfileInfoModel: Decodable {
    @SafeDecode
    private(set) var id: String
    @SafeDecode
    private(set) var username: String
    @SafeDecode
    private(set) var name: String
    @SafeDecode
    private(set) var avatar: String
    @SafeDecode
    private(set) var isCurrentUser: Bool
}
