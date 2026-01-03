//
//  FollowersResponse.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 3.04.2025.
//

import Foundation
import ModelParsers

struct FollowersResponse: Decodable {
    @LossyArray
    private(set) var users: [FollowerModel]
}
