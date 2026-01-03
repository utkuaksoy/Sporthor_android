//
//  UpdateProfileRequest.swift
//  Sporthor
//
//  Created by derTurke on 6.03.2025.
//

import Foundation

struct UpdateProfileRequest: Encodable {
    var birthDate: String?
    var gender: Int?
    var branchesofInterests: [String]?
    var userRoles: [String]?
    var coachRoles: [String]?
    
    init(birthDate: String? = nil,
         gender: Int? = nil,
         branchesofInterests: [String]? = nil,
         userRoles: [String]? = nil,
         coachRoles: [String]? = nil) {
        self.birthDate = birthDate
        self.gender = gender
        self.branchesofInterests = branchesofInterests
        self.userRoles = userRoles
        self.coachRoles = coachRoles
    }
    
    enum CodingKeys: CodingKey {
        case birthDate
        case gender
        case branchesofInterests
        case userRoles
        case coachRoles
    }
    
    func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.birthDate, forKey: .birthDate)
        try container.encodeIfPresent(self.gender, forKey: .gender)
        try container.encodeIfPresent(self.branchesofInterests, forKey: .branchesofInterests)
        try container.encodeIfPresent(self.userRoles, forKey: .userRoles)
        try container.encodeIfPresent(self.coachRoles, forKey: .coachRoles)
    }
}
