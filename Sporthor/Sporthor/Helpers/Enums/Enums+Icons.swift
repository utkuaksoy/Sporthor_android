//
//  Enums+Icons.swift
//  Sporthor
//
//  Created by derTurke on 27.03.2025.
//

import UIKit

enum Icons: Codable {
    case headerApp
    case calendar
    case calendarBadge
    case bell
    case bellBadge
    
    var image: UIImage {
        switch self {
        case .headerApp:
            return Asset.headerAppIcon.image
        case .calendar:
            return Asset.calendar.image
        case .calendarBadge:
            return Asset.calendarBadge.image
        case .bell:
            return Asset.bellBadge.image
        case .bellBadge:
            return Asset.bellBadge.image
        }
    }
    
    var tag: Int {
        switch self {
        case .headerApp:
            return 0
        case .calendar:
            return 1
        case .calendarBadge:
            return 1
        case .bell:
            return 2
        case .bellBadge:
            return 2
        }
    }
}
