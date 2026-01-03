//
//  SocialInfoRequest.swift
//  Sporthor
//
//  Created by derTurke on 6.03.2025.
//

public struct SocialInfo: Codable {
    public let platform: Int?
    public let accountId: String?
    public let email: String?
    public let accessToken: String?
    
    public init(platform: Int? = nil,
                accountId: String? = nil,
                email: String? = nil,
                accessToken: String? = nil) {
        self.platform = platform
        self.accountId = accountId
        self.email = email
        self.accessToken = accessToken
    }
    
    public enum CodingKeys: CodingKey {
        case platform
        case accountId
        case email
        case accessToken
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.platform, forKey: .platform)
        try container.encodeIfPresent(self.accountId, forKey: .accountId)
        try container.encodeIfPresent(self.email, forKey: .email)
        try container.encodeIfPresent(self.accessToken, forKey: .accessToken)
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.platform = try container.decodeIfPresent(Int.self, forKey: .platform)
        self.accountId = try container.decodeIfPresent(String.self, forKey: .accountId)
        self.email = try container.decodeIfPresent(String.self, forKey: .email)
        self.accessToken = try container.decodeIfPresent(String.self, forKey: .accessToken)
    }
}
