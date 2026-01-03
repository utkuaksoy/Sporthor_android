//
//  GetSearchHistoryResponse.swift
//  Sporthor
//
//  Created by derTurke on 12.04.2025.
//

import Foundation

struct GetSearchHistoryResponse: Codable {
    var histories: [SearchHistory]?
}

struct SearchHistory: Codable {
    var id: String?
    var status: Bool?
    var isDeleted: Bool?
    var createdAt: String?
    var updatedAt: String?
    var deletedAt: String?
    var userId: String?
    var searchQuery: String?
    var searchUser: SearchHistoryUser?
}

struct SearchHistoryUser: Codable {
    var isTeam: Bool?
    var image: String?
    var name: String?
    var userName: String?
    var summary: String?
    var userId: String?
}
