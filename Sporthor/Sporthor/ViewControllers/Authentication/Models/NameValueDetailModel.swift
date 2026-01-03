//
//  NameValueDetailModel.swift
//  Sporthor
//
//  Created by derTurke on 6.03.2025.
//

import Foundation

struct NameValueDetailModel: Codable {
    var name: String?
    var value: String?
    var detail: String?
    var isSelected: Bool = false
    
    enum CodingKeys: CodingKey {
        case name
        case value
        case detail
    }
    
    init(name: String? = "",
         value: String? = "",
         detail: String? = "") {
        self.name = name
        self.value = value
        self.detail = detail
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.name = try container.decodeIfPresent(String.self, forKey: .name)
        self.value = try container.decodeIfPresent(String.self, forKey: .value)
        self.detail = try container.decodeIfPresent(String.self, forKey: .detail)
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(self.name, forKey: .name)
        try container.encodeIfPresent(self.value, forKey: .value)
        try container.encodeIfPresent(self.detail, forKey: .detail)
    }
}
