//
//  TeamSquadSizeCalculator.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import DesignKit
import UIKit

final class TeamSquadSizeCalculator {
    
    private enum Constants {
        static let totalTopPadding: CGFloat = 16
        static let memberSpacing: CGFloat = 12
        static let verticalSpacing: CGFloat = 16
        static let titleHeight: CGFloat = 20
    }
    
    // MARK: - Private Properties
    
    private let width: CGFloat
    private let teamSquad: TeamGroup?
    
    // MARK: - Private Properties
    
    init(width: CGFloat, teamSquad: TeamGroup?) {
        self.width = width
        self.teamSquad = teamSquad
    }
    
    var componentSize: CGSize {
        guard let teamSquad else { return .zero }
        let squadHeight = teamMembersHeight(with: teamSquad.members)
        let totalHeight = Constants.totalTopPadding + squadHeight + Constants.titleHeight + Constants.verticalSpacing
        return CGSize(width: width, height: totalHeight)
    }
    
    
    private func teamMembersHeight(with teamMembers: [TeamMemberItem]?) -> CGFloat {
        guard let teamMembers, !teamMembers.isEmpty else { return .zero }
        let allMemberHeight = CGFloat(teamMembers.count * 48)
        let allMemberSpacingHeight = CGFloat(teamMembers.count - 1) * Constants.memberSpacing
        let totalHeight = allMemberHeight + allMemberSpacingHeight
        return totalHeight
    }
}
