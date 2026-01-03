//
//  TournamentsCalculator.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import DesignKit
import UIKit

final class TournamentsCalculator {
    
    private enum Constants {
        static let totalTopPadding: CGFloat = 24
        static let titleHeight: CGFloat = 20
        static let verticalSpacing: CGFloat = 16
        static let memberSpacing: CGFloat = 16
    }
    
    // MARK: - Private Properties
    
    private let width: CGFloat
    private let tournaments:  [TournamentModel]
    
    // MARK: - Private Properties
    
    init(width: CGFloat, tournaments: [TournamentModel]) {
        self.width = width
        self.tournaments = tournaments
    }
    
    var componentSize: CGSize {
        let tournamentsHeight = tournamentsHeight(with: tournaments)
        let totalHeight = Constants.totalTopPadding + tournamentsHeight + Constants.titleHeight + Constants.verticalSpacing
        return CGSize(width: width, height: totalHeight)
    }
    
    
    private func tournamentsHeight(with tournaments: [TournamentModel]) -> CGFloat {
        let allTournamentsHeight = CGFloat(tournaments.count * 56)
        let allTournamentsSpacingHeight = CGFloat(tournaments.count - 1) * Constants.memberSpacing
        let totalHeight = allTournamentsHeight + allTournamentsSpacingHeight
        return totalHeight
    }
}
