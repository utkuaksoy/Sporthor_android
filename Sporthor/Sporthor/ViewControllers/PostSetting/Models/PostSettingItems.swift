//
//  PostSettingItems.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//

import UIKit

enum PostSettingItems: Int, CaseIterable {
    case complain = 0
    case hide
    case blockUser
    case remove
    
    var title: String {
        switch self {
        case .complain:
            "Şikayet Et"
        case .hide:
            "Gizle"
        case .blockUser:
            "Kullanıcıyı Engelle"
        case .remove:
            "Kaldır"
        }
    }
    
    var icon: UIImage? {
        switch self {
        case .complain:
            return Asset.errorAlert.image
        case .hide:
            return Asset.eyeSlash.image
        case .blockUser:
            return Asset.userCross.image
        case .remove:
            return Asset.error.image
        }
    }
    
    var isDestructive: Bool {
        return self == .remove || self == .blockUser
    }
}
