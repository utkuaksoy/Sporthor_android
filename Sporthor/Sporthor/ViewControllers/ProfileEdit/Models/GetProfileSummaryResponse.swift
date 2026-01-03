//
//  GetProfileSummaryResponse.swift
//  Sporthor
//
//  Created by derTurke on 13.04.2025.
//

import Foundation

struct GetProfileSummaryResponse: Codable {
    var profileImage: String?
    var profileInfo: ProfileSummaryInfo?
    var teamInfo: ProfileSummaryTeamInfo?
    var highlights: ProfileSummaryHighlights?
}

struct ProfileSummaryInfo: Codable {
    var title: String?
    var row: [ProfileSummaryTextFieldRow]?
}

struct ProfileSummaryTeamInfo: Codable {
    var title: String?
    var info: String?
    var teams: [ProfileSummaryTeamInfoTeams]?
}

struct ProfileSummaryHighlights: Codable {
    var title: String?
    var branches: [ProfileSummaryHighlightsBranch]?
    var branchesAttributes: [ProfileSummaryHighlightsBranchAttributes]?
}

struct ProfileSummaryTeamInfoTeams: Codable {
    var teamImage: String?
    var teamName: String?
}

struct ProfileSummaryHighlightsBranch: Codable {
    var branchImage: String?
    var branchTitle: String?
    var branchId: String?
    var isSelected: Bool?
}

struct ProfileSummaryHighlightsBranchAttributes: Codable {
    var branchId: String?
    var branchInfoRow: [ProfileSummaryTextFieldRow]?
}

struct ProfileSummaryTextFieldRow: Codable, Equatable {
    var title: String?
    var placeholder: String?
    var text: String?
    var parameterName: String?
    var isRequired: Bool?
    var type: Int?
    var isEnabled: Bool?
}

