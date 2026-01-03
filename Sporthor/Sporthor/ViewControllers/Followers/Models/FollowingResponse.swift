//
//  FollowingResponse.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 3.04.2025.
//

import Foundation

struct FollowingResponse: Decodable {
    let following: [FollowerModel]
}
