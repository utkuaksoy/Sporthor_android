//
//  StorySettingItems.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//

import UIKit

enum StorySettingItems: Int, CaseIterable {
    case remove = 0
    
    var title: String {
        switch self {
        case .remove:
            "Kaldır"
        }
    }
    
    var icon: UIImage? {
        switch self {
        case .remove:
            return Asset.redTrashIcon.image
        }
    }
    
    var isDestructive: Bool {
        return self == .remove
    }
}
