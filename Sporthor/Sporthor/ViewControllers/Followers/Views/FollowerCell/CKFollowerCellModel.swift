//
//  CKFollowerCellModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 09.04.2025.
//

import DesignKit
import UIKit

public enum FollowState {
    case following
    case notFollowing
    
    var title: String {
        switch self {
        case .following:
            return "Takiptesin"
        case .notFollowing:
            return "Takip Et"
        }
    }
    
    var style: ButtonStyle {
        switch self {
        case .following:
            return .init(
                backgroundColor: .white,
                titleColor: ColorName.contentStrong900.color,
                borderColor: ColorName.borderStrong900.color
            )
        case .notFollowing:
            return .init(
                backgroundColor: ColorName.contentStrong900.color,
                titleColor: .white,
                borderColor: .clear
            )
        }
    }
}

public struct ButtonStyle {
    let backgroundColor: UIColor
    let titleColor: UIColor
    let borderColor: UIColor
}

public struct CKFollowerCellModel {
    public let imageUrl: String?
    public let name: String
    public let summary: String
    public let followState: FollowState
    public let isCurrentUser: Bool
    
    public init(
        imageUrl: String?,
        name: String,
        summary: String,
        followState: FollowState,
        isCurrentUser: Bool
    ) {
        self.imageUrl = imageUrl
        self.name = name
        self.summary = summary
        self.followState = followState
        self.isCurrentUser = isCurrentUser
    }
} 
