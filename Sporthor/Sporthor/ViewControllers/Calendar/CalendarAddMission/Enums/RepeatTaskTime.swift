//
//  RepeatTaskTime.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

enum RepeatTaskTime: Int, CaseIterable {
    case everyDay = 1
    case weekly
    case oneEveryTwoWeeks
    case everyMonth
    case everyYear
    
    var title: String {
        switch self {
        case .everyDay:
            return "Her Gün"
        case .weekly:
            return "Her Hafta"
        case .oneEveryTwoWeeks:
            return "Her 2 Haftada 1"
        case .everyMonth:
            return "Her Ay"
        case .everyYear:
            return "Her Yıl"
        }
    }
    
    var isSelected: Bool {
        switch self {
        case .everyDay:
            return false
        case .weekly:
            return false
        case .oneEveryTwoWeeks:
            return false
        case .everyMonth:
            return false
        case .everyYear:
            return false
        }
    }
}
