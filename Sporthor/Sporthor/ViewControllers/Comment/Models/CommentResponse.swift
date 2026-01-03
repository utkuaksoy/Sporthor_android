//
//  CommentResponse.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//

import Foundation
import ModelParsers

struct CommentResponse: Decodable {
    @LossyArray var comments: [CommentModel]
}

struct CommentModel: Decodable {
    @SafeDecode var id: String
    @SafeDecode var postId: String
    @SafeDecode var userId: String
    @SafeDecode var profileImageUrl: String
    @SafeDecode var text: String
    @SafeDecode var username: String
    @SafeDecode var commentDate: String
}
