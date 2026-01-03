//
//  String+Additions.swift
//  ChatFeatureLive
//
//  Created by Mesut on 29.01.2025.
//

import Foundation

public extension String {
    var nilIfEmpty: String? {
        return self.isEmpty ? nil : self
    }
    
    func toDate(
        format: String = "dd/MM/yyyy HH:mm:ss",
        locale: String = "tr_TR",
        timeZone: TimeZone = .current
    ) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        dateFormatter.locale = Locale(identifier: locale)
        dateFormatter.timeZone = timeZone
        return dateFormatter.date(from: self)
    }
}
