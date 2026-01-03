//
//  SaveUserTeamResponse.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

struct SaveUserTeamResponse: Codable {
    let teams: [UserTeamModel]?
    
    enum CodingKeys: CodingKey {
        case teams
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.teams = try container.decodeIfPresent([UserTeamModel].self, forKey: .teams)
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.teams, forKey: .teams)
    }
}
