//
//  SuggestionResponse.swift
//  Sporthor
//
//  Created by derTurke on 19.03.2025.
//

import UIKit

struct SuggestionResponse: Codable {
    var suggestions: [SuggestionModel]?
}

struct SuggestionModel: Codable {
    var id: Int?
    var profileImage: String?
    var teamImage: String?
    var name: String?
    var isFollow: Bool = false
}
