//
//  ChatMediaAndDocumentResponse.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import ChatKit
import Foundation
import ModelParsers

struct ChatMediaAndDocumentResponse: Decodable {
    @LossyArray
    private(set) var medias: [ChatMessageResponse]
    @LossyArray
    private(set) var files: [ChatMessageResponse]
}
