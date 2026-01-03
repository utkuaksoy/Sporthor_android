//
//  ProfileAboutDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import Foundation

struct ProfileAboutDataModel: Decodable {
    let title: String?
    let description: String?
    let details: [KeyValueItem]?

    enum CodingKeys: String, CodingKey {
        case title
        case description
        case details = "extraInfo"
    }
}

struct KeyValueItem: Decodable, Hashable {
    let key: String
    let value: String
    let icon: String
}
