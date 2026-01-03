//
//  CheckUsernameResponse.swift
//  Sporthor
//
//  Created by derTurke on 4.03.2025.
//

struct CheckUsernameResponse: Decodable {
    var isUsable: Bool?
    var suggestions: [String]?
    
    enum CodingKeys: CodingKey {
        case isUsable
        case suggestions
    }
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.isUsable = try container.decodeIfPresent(Bool.self, forKey: .isUsable)
        self.suggestions = try container.decodeIfPresent([String].self, forKey: .suggestions)
    }
}
