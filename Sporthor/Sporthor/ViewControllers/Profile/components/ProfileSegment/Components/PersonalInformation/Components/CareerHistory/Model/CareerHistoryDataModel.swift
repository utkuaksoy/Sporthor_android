//
//  CareerHistoryDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import Foundation

struct CareerHistoryDataModel: Decodable {
    let title: String?
    let items: [CareerHistoryItemModel]?
}

struct CareerHistoryItemModel: Decodable {
    let teamName: String?
    let teamLogoURL: String?
    let startDate: String?
    let endDate: String?
}
