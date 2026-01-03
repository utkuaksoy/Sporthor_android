//
//  Season.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import Foundation
import ModelParsers

struct SeasonResponse: Decodable {
    @LossyArray var seasons: [Season]
}

struct Season: Decodable {
    @SafeDecode var name: String
    @SafeDecode var value: String
    @SafeDecode var detail: String
    @SafeDecode var isSelected: Bool
}
