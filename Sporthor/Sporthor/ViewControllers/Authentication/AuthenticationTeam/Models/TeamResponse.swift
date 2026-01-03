//
//  TeamResponse.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

struct TeamResponse: Codable {
    var teams: [TeamItemModel]?
    
    enum CodingKeys: CodingKey {
        case teams
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.teams, forKey: .teams)
    }
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.teams = try container.decodeIfPresent([TeamItemModel].self, forKey: .teams)
    }
}

struct TeamItemModel: Codable {
    var name: String?
    var value: String?
    var detail: String?
    var image: String?
    var isSelected: Bool = false
    
    enum CodingKeys: CodingKey {
        case name
        case value
        case detail
        case image
    }
    
    init(name: String? = nil,
         value: String? = nil,
         detail: String? = nil,
         image: String? = nil) {
        self.name = name
        self.value = value
        self.detail = detail
        self.image = image
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.name, forKey: .name)
        try container.encodeIfPresent(self.value, forKey: .value)
        try container.encodeIfPresent(self.image, forKey: .image)
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.name = try container.decodeIfPresent(String.self, forKey: .name)
        self.value = try container.decodeIfPresent(String.self, forKey: .value)
        self.detail = try container.decodeIfPresent(String.self, forKey: .detail)
        self.image = try container.decodeIfPresent(String.self, forKey: .image)
    }
}
