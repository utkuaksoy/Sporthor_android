//
//  UserInfoResponse.swift
//  UserKit
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

public struct UserInfoResponse: Codable {
    public let id: String?
    public let status, isDeleted: Bool?
    public let createdAt,
               updatedAt,
               deletedAt: String?
    public let profilePhoto: String?
    public let username,
               name,
               lastName,
               email,
               password,
               mobilePhone,
               otpCode,
               tempPassword: String?
    public let firebaseId,
               firebaseToken: String?
    public let birthDate,
               lastLoginDate: String?
    public let genderType,
               userType: Int?
    public let teams: [UserTeamModel]?
    public let socialAccounts: [SocialInfo]?
    
    init(id: String? = nil,
         status: Bool? = nil,
         isDeleted: Bool? = nil,
         createdAt: String? = nil,
         updatedAt: String? = nil,
         deletedAt: String? = nil,
         profilePhoto: String? = nil,
         username: String? = nil,
         name: String? = nil,
         lastName: String? = nil,
         email: String? = nil,
         password: String? = nil,
         mobilePhone: String? = nil,
         otpCode: String? = nil,
         tempPassword: String? = nil,
         firebaseId: String? = nil,
         firebaseToken: String? = nil,
         birthDate: String? = nil,
         lastLoginDate: String? = nil,
         genderType: Int? = nil,
         userType: Int? = nil,
         teams: [UserTeamModel]? = nil,
         socialAccounts: [SocialInfo]? = nil) {
        self.id = id
        self.status = status
        self.isDeleted = isDeleted
        self.createdAt = createdAt
        self.updatedAt = updatedAt
        self.deletedAt = deletedAt
        self.profilePhoto = profilePhoto
        self.username = username
        self.name = name
        self.lastName = lastName
        self.email = email
        self.password = password
        self.mobilePhone = mobilePhone
        self.otpCode = otpCode
        self.tempPassword = tempPassword
        self.firebaseId = firebaseId
        self.firebaseToken = firebaseToken
        self.birthDate = birthDate
        self.lastLoginDate = lastLoginDate
        self.genderType = genderType
        self.userType = userType
        self.teams = teams
        self.socialAccounts = socialAccounts
    }
    
    enum CodingKeys: CodingKey {
        case id
        case status
        case isDeleted
        case createdAt
        case updatedAt
        case deletedAt
        case profilePhoto
        case username
        case name
        case lastName
        case email
        case password
        case mobilePhone
        case otpCode
        case tempPassword
        case firebaseId
        case firebaseToken
        case birthDate
        case lastLoginDate
        case genderType
        case userType
        case teams
        case socialAccounts
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try container.decodeIfPresent(String.self, forKey: .id)
        self.status = try container.decodeIfPresent(Bool.self, forKey: .status)
        self.isDeleted = try container.decodeIfPresent(Bool.self, forKey: .isDeleted)
        self.createdAt = try container.decodeIfPresent(String.self, forKey: .createdAt)
        self.updatedAt = try container.decodeIfPresent(String.self, forKey: .updatedAt)
        self.deletedAt = try container.decodeIfPresent(String.self, forKey: .deletedAt)
        self.profilePhoto = try container.decodeIfPresent(String.self, forKey: .profilePhoto)
        self.username = try container.decodeIfPresent(String.self, forKey: .username)
        self.name = try container.decodeIfPresent(String.self, forKey: .name)
        self.lastName = try container.decodeIfPresent(String.self, forKey: .lastName)
        self.email = try container.decodeIfPresent(String.self, forKey: .email)
        self.password = try container.decodeIfPresent(String.self, forKey: .password)
        self.mobilePhone = try container.decodeIfPresent(String.self, forKey: .mobilePhone)
        self.otpCode = try container.decodeIfPresent(String.self, forKey: .otpCode)
        self.tempPassword = try container.decodeIfPresent(String.self, forKey: .tempPassword)
        self.firebaseId = try container.decodeIfPresent(String.self, forKey: .firebaseId)
        self.firebaseToken = try container.decodeIfPresent(String.self, forKey: .firebaseToken)
        self.birthDate = try container.decodeIfPresent(String.self, forKey: .birthDate)
        self.lastLoginDate = try container.decodeIfPresent(String.self, forKey: .lastLoginDate)
        self.genderType = try container.decodeIfPresent(Int.self, forKey: .genderType)
        self.userType = try container.decodeIfPresent(Int.self, forKey: .userType)
        self.teams = try container.decodeIfPresent([UserTeamModel].self, forKey: .teams)
        self.socialAccounts = try container.decodeIfPresent([SocialInfo].self, forKey: .socialAccounts)
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.id, forKey: .id)
        try container.encodeIfPresent(self.status, forKey: .status)
        try container.encodeIfPresent(self.isDeleted, forKey: .isDeleted)
        try container.encodeIfPresent(self.createdAt, forKey: .createdAt)
        try container.encodeIfPresent(self.updatedAt, forKey: .updatedAt)
        try container.encodeIfPresent(self.deletedAt, forKey: .deletedAt)
        try container.encodeIfPresent(self.profilePhoto, forKey: .profilePhoto)
        try container.encodeIfPresent(self.username, forKey: .username)
        try container.encodeIfPresent(self.name, forKey: .name)
        try container.encodeIfPresent(self.lastName, forKey: .lastName)
        try container.encodeIfPresent(self.email, forKey: .email)
        try container.encodeIfPresent(self.password, forKey: .password)
        try container.encodeIfPresent(self.mobilePhone, forKey: .mobilePhone)
        try container.encodeIfPresent(self.otpCode, forKey: .otpCode)
        try container.encodeIfPresent(self.tempPassword, forKey: .tempPassword)
        try container.encodeIfPresent(self.firebaseId, forKey: .firebaseId)
        try container.encodeIfPresent(self.firebaseToken, forKey: .firebaseToken)
        try container.encodeIfPresent(self.birthDate, forKey: .birthDate)
        try container.encodeIfPresent(self.lastLoginDate, forKey: .lastLoginDate)
        try container.encodeIfPresent(self.genderType, forKey: .genderType)
        try container.encodeIfPresent(self.userType, forKey: .userType)
        try container.encodeIfPresent(self.teams, forKey: .teams)
        try container.encodeIfPresent(self.socialAccounts, forKey: .socialAccounts)
    }
}
