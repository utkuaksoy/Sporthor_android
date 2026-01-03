//
//  RegisterRequest.swift
//  Sporthor
//
//  Created by derTurke on 1.03.2025.
//

import Foundation

struct RegisterRequest: Encodable {
    var name: String
    var surname: String
    var email: String
    var username: String
    var mobilePhone: String
    var password: String
    var firebaseToken: String
    var firebaseId: String
    var socialInfo: SocialInfo?
    
    init(name: String = "",
         surname: String = "",
         email: String = "",
         username: String = "",
         mobilePhone: String = "",
         password: String = "",
         firebaseToken: String = "",
         firebaseId: String = "",
         socialInfo: SocialInfo? = nil) {
        self.name = name
        self.surname = surname
        self.email = email
        self.username = username
        self.mobilePhone = mobilePhone
        self.password = password
        self.firebaseToken = firebaseToken
        self.firebaseId = firebaseId
        self.socialInfo = socialInfo
    }
    
    enum CodingKeys: CodingKey {
        case name
        case surname
        case email
        case username
        case mobilePhone
        case password
        case firebaseToken
        case firebaseId
        case socialInfo
    }
    
    func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.surname, forKey: .surname)
        try container.encode(self.email, forKey: .email)
        try container.encode(self.username, forKey: .username)
        try container.encode(self.mobilePhone, forKey: .mobilePhone)
        try container.encode(self.password, forKey: .password)
        try container.encode(self.firebaseToken, forKey: .firebaseToken)
        try container.encode(self.firebaseId, forKey: .firebaseId)
        try container.encode(self.socialInfo, forKey: .socialInfo)
    }
}
