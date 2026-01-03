//
//  MatchesResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import ComponentBaseKit
import Foundation
import ModelParsers

struct MatchesResponseModel: Decodable {
    let title: String?
    let matches: [MatchModel]?
}
