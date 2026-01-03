//
//  SportClubResponse.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import Foundation
import ModelParsers

struct SportClubResponse: Decodable {
    var club: SportClub?
}

struct SportClub: Decodable {
    @SafeDecode var id: String
    @SafeDecode var clubId: String
    @SafeDecode var status: Bool
    @SafeDecode var isDeleted: Bool
    @SafeDecode var createdAt: String
    @SafeDecode var updatedAt: String
    @SafeDecode var deletedAt: String
    @SafeDecode var founderUserId: String
    @SafeDecode var clubName: String
    @SafeDecode var logo: String
    @SafeDecode var city: String
    @SafeDecode var county: String
    @SafeDecode var address: String
    @LossyArray var files: [String]
    @SafeDecode var foundationYear: String
    @SafeDecode var confirmationStatus: Int
    @SafeDecode var branchId: String
    @SafeDecode var isSelected: Bool
    var branch: SportClubBranch?
    
    init(id: String = "",
         clubId: String = "",
         status: Bool = false,
         isDeleted: Bool = false,
         createdAt: String = "",
         updatedAt: String = "",
         deletedAt: String = "",
         founderUserId: String = "",
         clubName: String = "",
         logo: String = "",
         city: String = "",
         county: String = "",
         address: String = "",
         files: [String] = [],
         foundationYear: String = "",
         confirmationStatus: Int = -1,
         branchId: String = "",
         isSelected: Bool = false) {
        self.id = id
        self.clubId = clubId
        self.status = status
        self.isDeleted = isDeleted
        self.createdAt = createdAt
        self.updatedAt = updatedAt
        self.deletedAt = deletedAt
        self.founderUserId = founderUserId
        self.clubName = clubName
        self.logo = logo
        self.city = city
        self.county = county
        self.address = address
        self.files = files
        self.foundationYear = foundationYear
        self.confirmationStatus = confirmationStatus
        self.branchId = branchId
        self.isSelected = isSelected
    }
}

struct SportClubBranch: Decodable {
    @SafeDecode var name: String
    @SafeDecode var val: String
}
