//
//  GetConfigurationResponse.swift
//  Sporthor
//
//  Created by derTurke on 6.03.2025.
//

import Foundation

struct GetConfigurationResponse: Codable {
    var showExperience: Bool?
    var userRoles: [NameValueDetailModel]?
    var branches: [NameValueDetailModel]?
    var coachRoles: [NameValueDetailModel]?
    
    enum CodingKeys: CodingKey {
        case showExperience
        case userRoles
        case branches
        case coachRoles
    }
    
    init(showExperience: Bool? = nil,
         userRoles: [NameValueDetailModel]? = nil,
         branches: [NameValueDetailModel]? = nil,
         coachRoles: [NameValueDetailModel]? = nil) {
        self.showExperience = showExperience
        self.userRoles = userRoles
        self.branches = branches
        self.coachRoles = coachRoles
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(showExperience, forKey: .showExperience)
        try container.encodeIfPresent(userRoles, forKey: .userRoles)
        try container.encodeIfPresent(branches, forKey: .branches)
        try container.encodeIfPresent(coachRoles, forKey: .coachRoles)
    }
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.showExperience = try container.decodeIfPresent(Bool.self, forKey: .showExperience)
        self.userRoles = try container.decodeIfPresent([NameValueDetailModel].self, forKey: .userRoles)
        self.branches = try container.decodeIfPresent([NameValueDetailModel].self, forKey: .branches)
        self.coachRoles = try container.decodeIfPresent([NameValueDetailModel].self, forKey: .coachRoles)
    }
}
