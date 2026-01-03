//
//  LocalizationResponse.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

public struct LocalizationResponse: Codable, Sendable {
    var localizations: [LocalizationItem]?
    
    public enum CodingKeys: CodingKey {
        case localizations
    }
    
    public init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.localizations = try container.decodeIfPresent([LocalizationItem].self, forKey: .localizations)
    }
    
    public func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.localizations, forKey: .localizations)
    }
}

public struct LocalizationItem: Codable, Sendable {
    var code: String?
    var text: String?
    var languageId: Int?
    
    public enum CodingKeys: CodingKey {
        case code
        case text
        case languageId
    }
    
    public init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.code = try container.decodeIfPresent(String.self, forKey: .code)
        self.text = try container.decodeIfPresent(String.self, forKey: .text)
        self.languageId = try container.decodeIfPresent(Int.self, forKey: .languageId)
    }
    
    public func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.code, forKey: .code)
        try container.encodeIfPresent(self.text, forKey: .text)
        try container.encodeIfPresent(self.languageId, forKey: .languageId)
    }
}
