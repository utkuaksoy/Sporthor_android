//
//  CreatePostRequest.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//

import Foundation
import ModelParsers

struct CreatePostRequest: Encodable {
    @SafeDecode var description: String = ""
    @LossyArray var media: [MediaItem] = []
}
