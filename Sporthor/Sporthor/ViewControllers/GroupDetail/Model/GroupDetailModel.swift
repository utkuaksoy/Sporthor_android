//
//  GroupDetailResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import Foundation
import ModelParsers

struct GroupDetailResponseModel: Decodable {
    
    @SafeDecode
    private(set) var groupName: String
    @SafeDecode
    private(set) var groupId: String
    @SafeDecode
    private(set) var groupImageUrl: String
    @SafeDecode
    private(set) var mediaCount: Int
    @SafeDecode
    private(set) var groupCeratedDate: String
    @LossyArray
    private(set) var members: [FollowerModel]
}
