//
//  ProfileSettingItems.swift
//  Sporthor
//
//  Created by derTurke on 16.08.2025.
//

import UIKit

enum ProfileSettingItems: Int, CaseIterable {
    case blockUser = 0
    
    var title: String {
        switch self {
        case .blockUser:
            return "Kullanıcıyı Engelle"
        }
    }
    
    var icon: UIImage? {
        switch self {
        case .blockUser:
            return Asset.userCross.image
        }
    }
    
    var isDestructive: Bool {
        return self == .blockUser
    }
}
