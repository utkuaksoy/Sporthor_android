//
//  FeaturedSkillsDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import Foundation

struct FeaturedSkillsDataModel: Decodable {
    let title: String?
    let skills: [SportSkillModel]?
}

struct SportSkillModel: Decodable {
    let id: String?
    let name: String?
    let icon: String?
    let isSelected: Bool?
    let details: [DetailedSkillModel]?
}

struct DetailedSkillModel: Decodable {
    let title: String?
    let value: String?
    let unit: Int?
}
