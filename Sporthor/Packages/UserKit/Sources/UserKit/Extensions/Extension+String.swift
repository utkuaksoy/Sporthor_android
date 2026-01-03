//
//  Extension+String.swift
//  UserKit
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

public extension String {
    var localizedText: String {
        LocalizationHelper.shared.localizedText(for: self)
    }
}
