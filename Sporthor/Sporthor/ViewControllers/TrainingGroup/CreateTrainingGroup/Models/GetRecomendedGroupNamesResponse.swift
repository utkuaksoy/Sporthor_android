//
//  GetRecomendedGroupNamesResponse.swift
//  Sporthor
//
//  Created by derTurke on 16.07.2025.
//

import Foundation
import ModelParsers

struct GetRecomendedGroupNamesResponse: Decodable {
    @LossyArray private(set) var names: [String]
}
