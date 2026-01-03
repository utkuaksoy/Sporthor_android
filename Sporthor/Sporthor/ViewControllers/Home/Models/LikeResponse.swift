//
//  LikeResponse.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//

import Foundation
import ModelParsers

struct LikeAndUnlikeResponse: Decodable {
    @SafeDecode var totalLikedCount: Int
    @SafeDecode var isLiked: Bool
}
