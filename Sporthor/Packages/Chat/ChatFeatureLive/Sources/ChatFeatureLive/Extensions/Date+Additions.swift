//
//  Date+Additions.swift
//  ChatFeatureLive
//
//  Created by Mesut on 23.02.2025.
//

import Foundation

extension Date {
    func toString(format: DateFormat = .date) -> String {
        return format.formatter.string(from: self)
    }
    
    enum DateFormat {
        case date
        case time
        case dateTime
        
        var formatter: DateFormatter {
            let formatter = DateFormatter()
            formatter.locale = Locale(identifier: "tr_TR")
            
            switch self {
            case .date:
                formatter.dateFormat = "d MMMM yyyy"
            case .time:
                formatter.dateFormat = "HH:mm"
            case .dateTime:
                formatter.dateFormat = "d MMMM yyyy HH:mm"
            }
            
            return formatter
        }
    }
}
