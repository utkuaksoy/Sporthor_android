//
//  ProfileAboutSizeCalculator.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.03.2025.
//

import DesignKit
import UIKit

final class ProfileAboutSizeCalculator {
    
    private enum Constants {
        static let totalTopPadding: CGFloat = 16
        static let totalBottomPadding: CGFloat = 16
        static let labelsPadding: CGFloat = 8
        static let verticalSpacing: CGFloat = 12
    }
    
    private let width: CGFloat
    private let title: String
    private let profileAbout: String?
    private let detailCount: Int
    
    init(width: CGFloat, title: String, profileAbout: String?, detailCount: Int) {
        self.width = width
        self.title = title
        self.profileAbout = profileAbout
        self.detailCount = detailCount
    }
    
    var componentSize: CGSize {
        let titleHeight = title.getLabelHeight(width: width, font: .bold03Compact)
        
        let profileAboutHeight = profileAbout != nil ? profileAbout?.getLabelHeight(width: width, font: .body04Compact) ?? .zero : .zero
        
        let totalHeight = titleHeight + profileAboutHeight + Constants.verticalSpacing + Constants.labelsPadding + detailsKeyValueHeight(detailCount: detailCount)

        return CGSize(width: width, height: totalHeight)
    }
    
    
    private func detailsKeyValueHeight(detailCount: Int) -> CGFloat {
        guard detailCount > .zero else { return .zero }
        let detailHeight = CGFloat(detailCount * 18)
        let totalDetailSpacing = CGFloat((detailCount - 1) * 8)
        return detailHeight + totalDetailSpacing
    }
}
