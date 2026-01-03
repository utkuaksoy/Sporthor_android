//
//  AuthResponse.swift
//  UserKit
//
//  Created by derTurke on 9.03.2025.
//


import Foundation

public struct AuthResponse: Codable {
    public var name: String?
    public var userId: String?
    public var userName: String?
    public var profilePhoto: String?
    public var surname: String?
    public var authenticateResult: Bool?
    public var authToken: String?
    public var accessTokenExpireDate: String?
    
    public enum CodingKeys: CodingKey {
        case name
        case surname
        case userId
        case username
        case profilePhoto
        case authenticateResult
        case authToken
        case accessTokenExpireDate
    }
    
    public init(
        name: String = "",
        surname: String = "",
        userId: String = "",
        userName: String = "",
        profilePhoto: String = "",
        authenticateResult: Bool = false,
        authToken: String = "",
        accessTokenExpireDate: String = ""
        
    ) {
        self.name = name
        self.surname = surname
        self.userId = userId
        self.userName = userName
        self.profilePhoto = profilePhoto
        self.authenticateResult = authenticateResult
        self.authToken = authToken
        self.accessTokenExpireDate = accessTokenExpireDate
    }
    
    public init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.name = try container.decodeIfPresent(String.self, forKey: .name)
        self.surname = try container.decodeIfPresent(String.self, forKey: .surname)
        self.userId = try container.decodeIfPresent(String.self, forKey: .userId)
        self.userName = try container.decodeIfPresent(String.self, forKey: .username)
        self.profilePhoto = try container.decodeIfPresent(String.self, forKey: .profilePhoto)
        self.authenticateResult = try container.decodeIfPresent(Bool.self, forKey: .authenticateResult)
        self.authToken = try container.decodeIfPresent(String.self, forKey: .authToken)
        self.accessTokenExpireDate = try container.decodeIfPresent(String.self, forKey: .accessTokenExpireDate)
    }
    
    public func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.name, forKey: .name)
        try container.encodeIfPresent(self.surname, forKey: .surname)
        try container.encodeIfPresent(self.userId, forKey: .userId)
        try container.encodeIfPresent(self.userName, forKey: .username)
        try container.encodeIfPresent(self.profilePhoto, forKey: .profilePhoto)
        try container.encodeIfPresent(self.authenticateResult, forKey: .authenticateResult)
        try container.encodeIfPresent(self.authToken, forKey: .authToken)
        try container.encodeIfPresent(self.accessTokenExpireDate, forKey: .accessTokenExpireDate)
    }
}
