//
//  PostsResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 13.03.2025.
//

import ModelParsers
import Foundation

struct PostsResponseModel: Decodable {
    @LossyArray
    private(set) var posts: [PostItemModel]
}

struct PostItemModel: Decodable {
    @SafeDecode
    private(set) var id: String
    @SafeDecode
    private(set) var images: String
    
    public static func safeDecode(from decoder: any Decoder) throws -> PostItemModel {
        try PostItemModel(from: decoder)
    }
}
