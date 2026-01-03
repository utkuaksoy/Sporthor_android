//
//  Extension+Date.swift
//  CommonKit
//
//  Created by derTurke on 21.05.2025.
//

import Foundation

public extension Date {
    func toString(_ format: String = "dd.MM.yyyy",
                  locale: String = "tr_TR") -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: locale)
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }
}
