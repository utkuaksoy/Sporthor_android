//
//  CareerHistoryCalculator.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import DesignKit
import UIKit

final class CareerHistoryCalculator {
    
    private enum Constants {
        static let totalTopPadding: CGFloat = 24
        static let titleHeight: CGFloat = 20
        static let verticalSpacing: CGFloat = 6
        static let memberSpacing: CGFloat = 16
    }
    
    // MARK: - Private Properties
    
    private let width: CGFloat
    private let items:  [CareerHistoryItemModel]
        
    init(width: CGFloat, items: [CareerHistoryItemModel]) {
        self.width = width
        self.items = items
    }
    
    var componentSize: CGSize {
        let careersHeight = careerItemHeight(with: items)
        let totalHeight = Constants.totalTopPadding + careersHeight + Constants.titleHeight + Constants.verticalSpacing
        return CGSize(width: width, height: totalHeight)
    }
    
    
    private func careerItemHeight(with items: [CareerHistoryItemModel]) -> CGFloat {
        let allCareersHeight = CGFloat(items.count * 88)
        return allCareersHeight
    }
}
