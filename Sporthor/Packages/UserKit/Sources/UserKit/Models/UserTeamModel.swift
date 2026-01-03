//
//  UserTeamModel.swift
//  UserKit
//
//  Created by derTurke on 9.03.2025.
//


public struct UserTeamModel: Codable {
    public let teamId: String?
    public let teamName: String?
    public let status: Int?
    
    public enum CodingKeys: CodingKey {
        case teamId
        case teamName
        case status
    }
    
    public init(teamId: String? = nil,
                teamName: String? = nil,
                status: Int? = nil) {
        self.teamId = teamId
        self.teamName = teamName
        self.status = status
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.teamId = try container.decodeIfPresent(String.self, forKey: .teamId)
        self.teamName = try container.decodeIfPresent(String.self, forKey: .teamName)
        self.status = try container.decodeIfPresent(Int.self, forKey: .status)
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.teamId, forKey: .teamId)
        try container.encodeIfPresent(self.teamName, forKey: .teamName)
        try container.encodeIfPresent(self.status, forKey: .status)
    }
}
